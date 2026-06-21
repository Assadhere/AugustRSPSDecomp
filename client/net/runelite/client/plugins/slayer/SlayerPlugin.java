package net.runelite.client.plugins.slayer;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.inject.Binder;
import com.google.inject.Provides;
import custom.UpdateSlayerInfoScript;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.MessageNode;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatClient;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ChatInput;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Slayer",
   description = "Show additional slayer task related information",
   tags = {"combat", "notifications", "overlay", "tasks"}
)
public class SlayerPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(SlayerPlugin.class);
   private static final String CHAT_SUPERIOR_MESSAGE = "A superior foe has appeared...";
   private static final String TASK_COMMAND_STRING = "!task";
   private static final Pattern TASK_STRING_VALIDATION = Pattern.compile("[^a-zA-Z0-9' -]");
   private static final int TASK_STRING_MAX_LENGTH = 50;
   @Inject
   private Client client;
   @Inject
   private SlayerConfig config;
   @Inject
   private ConfigManager configManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private SlayerOverlay overlay;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private Notifier notifier;
   @Inject
   private ClientThread clientThread;
   @Inject
   private TargetWeaknessOverlay targetWeaknessOverlay;
   @Inject
   private ChatCommandManager chatCommandManager;
   @Inject
   private ScheduledExecutorService executor;
   @Inject
   private ChatClient chatClient;
   @Inject
   private NpcOverlayService npcOverlayService;
   private final List<NPC> targets = new ArrayList();
   @Inject
   @Named("developerMode")
   boolean developerMode;
   private int amount;
   private int initialAmount;
   private int streak;
   private int points;
   private String taskLocation;
   private String taskName;
   private TaskCounter counter;
   private Instant infoTimer;
   private final List<Pattern> targetNames = new ArrayList();
   private String[] taskLocations;
   public final Function<NPC, HighlightedNpc> isTarget = (n) -> {
      if ((this.config.highlightHull() || this.config.highlightTile() || this.config.highlightOutline()) && this.targets.contains(n)) {
         Color color = this.config.getTargetColor();
         return HighlightedNpc.builder().npc(n).highlightColor(color).fillColor(ColorUtil.colorWithAlpha(color, color.getAlpha() / 12)).hull(this.config.highlightHull()).tile(this.config.highlightTile()).outline(this.config.highlightOutline()).build();
      } else {
         return null;
      }
   };

   public void configure(Binder binder) {
      binder.bind(SlayerPluginService.class).to(SlayerPluginServiceImpl.class);
   }

   protected void startUp() {
      this.chatCommandManager.registerCommandAsync("!task", this::taskLookup, this::taskSubmit);
      this.npcOverlayService.registerHighlighter(this.isTarget);
      this.overlayManager.add(this.overlay);
      this.overlayManager.add(this.targetWeaknessOverlay);
      this.clientThread.invoke(() -> {
         if (this.client.getGameState().getState() < GameState.LOGIN_SCREEN.getState()) {
            return false;
         } else {
            this.taskLocations = (String[])this.client.getDBTableRows(115).stream().map((row) -> {
               return (String)this.client.getDBTableField(row, 3, 0)[0];
            }).toArray((x$0) -> {
               return new String[x$0];
            });
            return true;
         }
      });
   }

   protected void shutDown() {
      this.chatCommandManager.unregisterCommand("!task");
      this.npcOverlayService.unregisterHighlighter(this.isTarget);
      this.overlayManager.remove(this.overlay);
      this.overlayManager.remove(this.targetWeaknessOverlay);
      this.removeCounter();
      this.targets.clear();
      this.taskLocations = null;
   }

   @Provides
   SlayerConfig provideSlayerConfig(ConfigManager configManager) {
      return (SlayerConfig)configManager.getConfig(SlayerConfig.class);
   }

   @Subscribe
   public void onUpdateSlayerInfoScript(UpdateSlayerInfoScript packet) {
      this.setTask(packet.getTaskName(), packet.getTaskAmountRemaining(), packet.getTaskAmountInitial(), packet.getTaskStreak(), packet.getCurrentSlayerPoints(), packet.getTaskLocation());
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case HOPPING:
         case LOGGING_IN:
         case CONNECTION_LOST:
            this.targets.clear();
         default:
      }
   }

   @Subscribe
   public void onCommandExecuted(CommandExecuted commandExecuted) {
      if (this.developerMode && commandExecuted.getCommand().equalsIgnoreCase("task")) {
         String task = String.join(" ", commandExecuted.getArguments());
         this.setTask(task, 42, 42);
         log.debug("Set task to {}", task);
      }

   }

   private void setProfileConfig(String key, Object value) {
      if (value != null) {
         this.configManager.setRSProfileConfiguration("slayer", key, value);
      } else {
         this.configManager.unsetRSProfileConfiguration("slayer", key);
      }

   }

   private void save() {
      this.setProfileConfig("amount", this.amount);
      this.setProfileConfig("initialAmount", this.initialAmount);
      this.setProfileConfig("taskName", this.taskName);
      this.setProfileConfig("taskLocation", this.taskLocation);
   }

   @Subscribe
   public void onNpcSpawned(NpcSpawned npcSpawned) {
      NPC npc = npcSpawned.getNpc();
      if (this.isTarget(npc)) {
         this.targets.add(npc);
      }

   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned npcDespawned) {
      NPC npc = npcDespawned.getNpc();
      this.targets.remove(npc);
   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      if (this.infoTimer != null && this.config.statTimeout() != 0) {
         Duration timeSinceInfobox = Duration.between(this.infoTimer, Instant.now());
         Duration statTimeout = Duration.ofMinutes((long)this.config.statTimeout());
         if (timeSinceInfobox.compareTo(statTimeout) >= 0) {
            this.removeCounter();
         }
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.GAMEMESSAGE || event.getType() == ChatMessageType.SPAM) {
         String chatMsg = Text.removeTags(event.getMessage());
         if (chatMsg.equals("A superior foe has appeared...")) {
            this.notifier.notify(this.config.showSuperiorNotification(), "A superior foe has appeared...");
         }

      }
   }

   @Subscribe
   private void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("slayer")) {
         if (event.getKey().equals("infobox")) {
            if (this.config.showInfobox()) {
               this.clientThread.invoke(this::addCounter);
            } else {
               this.removeCounter();
            }
         } else {
            this.npcOverlayService.rebuild();
         }

      }
   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
      if ((menuOptionClicked.getMenuAction() == MenuAction.CC_OP || menuOptionClicked.getMenuAction() == MenuAction.CC_OP_LOW_PRIORITY) && menuOptionClicked.getMenuOption().equals("Check")) {
         Widget w = this.client.getWidget(menuOptionClicked.getParam1());
         if (w == null) {
            return;
         }

         if (menuOptionClicked.getParam0() != -1) {
            w = w.getChild(menuOptionClicked.getParam0());
            if (w == null) {
               return;
            }
         }

         int itemId = w.getItemId();
         Widget[] var4 = w.getDynamicChildren();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Widget child = var4[var6];
            if (itemId == -1) {
               itemId = child.getItemId();
            }
         }

         itemId = ItemVariationMapping.map(itemId);
         if (itemId == 11864 || itemId == 11866 || itemId == 4155) {
            log.debug("Checked slayer task");
            this.infoTimer = Instant.now();
            this.addCounter();
         }
      }

   }

   @VisibleForTesting
   boolean isTarget(NPC npc) {
      if (this.targetNames.isEmpty()) {
         return false;
      } else {
         NPCComposition composition = npc.getTransformedComposition();
         if (composition == null) {
            return false;
         } else {
            String name = composition.getName().replace(' ', ' ').toLowerCase();
            Iterator var4 = this.targetNames.iterator();

            Matcher targetMatcher;
            do {
               do {
                  if (!var4.hasNext()) {
                     return false;
                  }

                  Pattern target = (Pattern)var4.next();
                  targetMatcher = target.matcher(name);
               } while(!targetMatcher.find());
            } while(!ArrayUtils.contains(composition.getActions(), "Attack") && !ArrayUtils.contains(composition.getActions(), "Pick"));

            return true;
         }
      }
   }

   private void rebuildTargetNames(Task task) {
      this.targetNames.clear();
      if (task != null) {
         Stream var10000 = Arrays.stream(task.getTargetNames()).map(SlayerPlugin::targetNamePattern);
         List var10001 = this.targetNames;
         Objects.requireNonNull(var10001);
         var10000.forEach(var10001::add);
         this.targetNames.add(targetNamePattern(this.taskName.replaceAll("s$", "")));
      }

   }

   private static Pattern targetNamePattern(String targetName) {
      return Pattern.compile("(?:\\s|^)" + targetName + "(?:\\s|$)", 2);
   }

   private void rebuildTargetList() {
      this.targets.clear();
      Iterator var1 = this.client.getNpcs().iterator();

      while(var1.hasNext()) {
         NPC npc = (NPC)var1.next();
         if (this.isTarget(npc)) {
            this.targets.add(npc);
         }
      }

   }

   @VisibleForTesting
   void setTask(String name, int amt, int initAmt) {
      this.setTask(name, amt, initAmt, 0, 0, (String)null);
   }

   private void setTask(String name, int amt, int initAmt, int streak, int slayerPoints, String location) {
      this.taskName = name;
      this.amount = amt;
      this.initialAmount = initAmt;
      this.taskLocation = location;
      this.streak = streak;
      this.points = slayerPoints;
      this.save();
      this.removeCounter();
      this.infoTimer = Instant.now();
      this.addCounter();
      Task task = Task.getTask(name);
      this.rebuildTargetNames(task);
      this.rebuildTargetList();
      this.npcOverlayService.rebuild();
   }

   private void addCounter() {
      if (this.config.showInfobox() && this.counter == null && !Strings.isNullOrEmpty(this.taskName)) {
         Task task = Task.getTask(this.taskName);
         int itemSpriteId = 4155;
         if (task != null) {
            itemSpriteId = task.getItemSpriteId();
         }

         BufferedImage taskImg = this.itemManager.getImage(itemSpriteId);
         Color var10001 = new Color(255, 119, 0);
         String taskTooltip = ColorUtil.wrapWithColorTag("%s", var10001) + "</br>";
         if (this.taskLocation != null && !this.taskLocation.isEmpty()) {
            taskTooltip = taskTooltip + this.taskLocation + "</br>";
         }

         taskTooltip = taskTooltip + ColorUtil.wrapWithColorTag("Pts:", Color.YELLOW) + " %s</br>" + ColorUtil.wrapWithColorTag("Streak:", Color.YELLOW) + " %s";
         if (this.initialAmount > 0) {
            taskTooltip = taskTooltip + "</br>" + ColorUtil.wrapWithColorTag("Start:", Color.YELLOW) + " " + this.initialAmount;
         }

         this.counter = new TaskCounter(taskImg, this, this.amount);
         this.counter.setTooltip(String.format(taskTooltip, capsString(this.taskName), this.points, this.streak));
         this.infoBoxManager.addInfoBox(this.counter);
      }
   }

   private void removeCounter() {
      if (this.counter != null) {
         this.infoBoxManager.removeInfoBox(this.counter);
         this.counter = null;
      }
   }

   void taskLookup(ChatMessage chatMessage, String message) {
      if (this.config.taskCommand()) {
         ChatMessageType type = chatMessage.getType();
         String player;
         if (type.equals(ChatMessageType.PRIVATECHATOUT)) {
            player = this.client.getLocalPlayer().getName();
         } else {
            player = Text.removeTags(chatMessage.getName()).replace(' ', ' ');
         }

         net.runelite.http.api.chat.Task task;
         try {
            task = this.chatClient.getTask(player);
         } catch (IOException var10) {
            IOException ex = var10;
            log.debug("unable to lookup slayer task", ex);
            return;
         }

         if (!TASK_STRING_VALIDATION.matcher(task.getTask()).find() && task.getTask().length() <= 50 && !TASK_STRING_VALIDATION.matcher(task.getLocation()).find() && task.getLocation().length() <= 50 && Task.getTask(task.getTask()) != null && this.isValidLocation(task.getLocation())) {
            int killed = task.getInitialAmount() - task.getAmount();
            StringBuilder sb = new StringBuilder();
            sb.append(task.getTask());
            if (!Strings.isNullOrEmpty(task.getLocation())) {
               sb.append(" (").append(task.getLocation()).append(')');
            }

            sb.append(": ");
            if (killed < 0) {
               sb.append(task.getAmount()).append(" left");
            } else {
               sb.append(killed).append('/').append(task.getInitialAmount()).append(" killed");
            }

            String response = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Slayer Task: ").append(ChatColorType.HIGHLIGHT).append(sb.toString()).build();
            MessageNode messageNode = chatMessage.getMessageNode();
            messageNode.setRuneLiteFormatMessage(response);
            this.client.refreshChat();
         } else {
            log.debug("Validation failed for task name or location: {}", task);
         }
      }
   }

   private boolean taskSubmit(ChatInput chatInput, String value) {
      if (Strings.isNullOrEmpty(this.taskName)) {
         return false;
      } else {
         String playerName = this.client.getLocalPlayer().getName();
         this.executor.execute(() -> {
            try {
               this.chatClient.submitTask(playerName, capsString(this.taskName), this.amount, this.initialAmount, this.taskLocation);
            } catch (Exception var7) {
               Exception ex = var7;
               log.warn("unable to submit slayer task", ex);
            } finally {
               chatInput.resume();
            }

         });
         return true;
      }
   }

   private boolean isValidLocation(String location) {
      if (location != null && !location.isEmpty()) {
         if (this.taskLocations != null) {
            String[] var2 = this.taskLocations;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String l = var2[var4];
               if (l.equalsIgnoreCase(location)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private static String capsString(String str) {
      String var10000 = str.substring(0, 1).toUpperCase();
      return var10000 + str.substring(1);
   }

   List<NPC> getTargets() {
      return this.targets;
   }

   int getAmount() {
      return this.amount;
   }

   void setAmount(int amount) {
      this.amount = amount;
   }

   int getInitialAmount() {
      return this.initialAmount;
   }

   void setInitialAmount(int initialAmount) {
      this.initialAmount = initialAmount;
   }

   int getStreak() {
      return this.streak;
   }

   void setStreak(int streak) {
      this.streak = streak;
   }

   int getPoints() {
      return this.points;
   }

   void setPoints(int points) {
      this.points = points;
   }

   String getTaskLocation() {
      return this.taskLocation;
   }

   void setTaskLocation(String taskLocation) {
      this.taskLocation = taskLocation;
   }

   String getTaskName() {
      return this.taskName;
   }

   void setTaskName(String taskName) {
      this.taskName = taskName;
   }
}
