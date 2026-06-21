package net.runelite.client.plugins.screenshot;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PostClientTick;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageCapture;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Screenshot",
   description = "Enable the manual and automatic taking of screenshots",
   tags = {"external", "images", "integration", "notifications"}
)
public class ScreenshotPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(ScreenshotPlugin.class);
   private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM. dd, yyyy");
   private static final String COLLECTION_LOG_TEXT = "New item added to your collection log: ";
   private static final String CHEST_LOOTED_MESSAGE = "You find some treasure in the chest!";
   private static final Map<Integer, String> CHEST_LOOT_EVENTS = ImmutableMap.of(12127, "The Gauntlet");
   private static final int GAUNTLET_REGION = 7512;
   private static final int CORRUPTED_GAUNTLET_REGION = 7768;
   private static final Pattern NUMBER_PATTERN = Pattern.compile("([,0-9]+)");
   private static final Pattern LEVEL_UP_PATTERN = Pattern.compile(".*Your ([a-zA-Z]+) (?:level is|are)? now (\\d+)\\.");
   private static final Pattern LEVEL_UP_MESSAGE_PATTERN = Pattern.compile("Congratulations, you've (just advanced your (?<skill>[a-zA-Z]+) level\\. You are now level (?<level>\\d+)|reached the highest possible (?<skill99>[a-zA-Z]+) level of 99)\\.");
   private static final Pattern BOSSKILL_MESSAGE_PATTERN = Pattern.compile("Your (.+) (?:kill|success) count is: ?<col=[0-9a-f]{6}>([0-9,]+)</col>");
   private static final Pattern VALUABLE_DROP_PATTERN = Pattern.compile(".*Valuable drop: ([^<>]+?\\(((?:\\d+,?)+) coins\\))(?:</col>)?");
   private static final Pattern UNTRADEABLE_DROP_PATTERN = Pattern.compile(".*Untradeable drop: ([^<>]+)(?:</col>)?");
   private static final Pattern DUEL_END_PATTERN = Pattern.compile("You have now (won|lost) ([0-9,]+) duels?\\.");
   private static final Pattern QUEST_PATTERN_1 = Pattern.compile(".+?ve\\.*? (?<verb>been|rebuilt|.+?ed)? ?(?:the )?'?(?<quest>.+?)'?(?: [Qq]uest)?[!.]?$");
   private static final Pattern QUEST_PATTERN_2 = Pattern.compile("'?(?<quest>.+?)'?(?: [Qq]uest)? (?<verb>[a-z]\\w+?ed)?(?: f.*?)?[!.]?$");
   private static final Pattern COMBAT_ACHIEVEMENTS_PATTERN = Pattern.compile("Congratulations, you've completed an? (?<tier>\\w+) combat task: <col=[0-9a-f]+>(?<task>(.+))</col>");
   private static final ImmutableList<String> RFD_TAGS = ImmutableList.of("Another Cook", "freed", "defeated", "saved");
   private static final ImmutableList<String> WORD_QUEST_IN_NAME_TAGS = ImmutableList.of("Another Cook", "Doric", "Heroes", "Legends", "Observatory", "Olaf", "Waterfall");
   private static final ImmutableList<String> PET_MESSAGES = ImmutableList.of("You have a funny feeling like you're being followed", "You feel something weird sneaking into your backpack", "You have a funny feeling like you would have been followed");
   private static final Pattern BA_HIGH_GAMBLE_REWARD_PATTERN = Pattern.compile("(?<reward>.+)!<br>High level gamble count: <col=7f0000>(?<gambleCount>.+)</col>");
   private static final Set<Integer> REPORT_BUTTON_TLIS = ImmutableSet.of(548, 161, 164);
   private static final String SD_KINGDOM_REWARDS = "Kingdom Rewards";
   private static final String SD_BOSS_KILLS = "Boss Kills";
   private static final String SD_CLUE_SCROLL_REWARDS = "Clue Scroll Rewards";
   private static final String SD_FRIENDS_CHAT_KICKS = "Friends Chat Kicks";
   private static final String SD_PETS = "Pets";
   private static final String SD_CHEST_LOOT = "Chest Loot";
   private static final String SD_VALUABLE_DROPS = "Valuable Drops";
   private static final String SD_UNTRADEABLE_DROPS = "Untradeable Drops";
   private static final String SD_DUELS = "Duels";
   private static final String SD_COLLECTION_LOG = "Collection Log";
   private static final String SD_PVP_KILLS = "PvP Kills";
   private static final String SD_DEATHS = "Deaths";
   private static final String SD_COMBAT_ACHIEVEMENTS = "Combat Achievements";
   private static final String SD_WILDERNESS_LOOT_CHEST = "Wilderness Loot Chest";
   private static final String SD_LEVELS = "Levels";
   private static final String SD_LEAGUE_TASKS = "League Tasks";
   private String clueType;
   private Integer clueNumber;
   private KillType killType;
   private Integer killCountNumber;
   private boolean shouldTakeScreenshot;
   private boolean notificationStarted;
   @Inject
   private ScreenshotConfig config;
   @Inject
   private Client client;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private DrawManager drawManager;
   @Inject
   private ScheduledExecutorService executor;
   @Inject
   private KeyManager keyManager;
   @Inject
   private ImageCapture imageCapture;
   @Inject
   private ClientThread clientThread;
   private NavigationButton titleBarButton;
   private String kickPlayerName;
   final Queue<Consumer<Image>> consumers = new ConcurrentLinkedQueue();
   private String reportButtonText;
   private final HotkeyListener hotkeyListener = new HotkeyListener(() -> {
      return this.config.hotkey();
   }) {
      public void hotkeyPressed() {
         ScreenshotPlugin.this.manualScreenshot();
      }
   };

   @Provides
   ScreenshotConfig getConfig(ConfigManager configManager) {
      return (ScreenshotConfig)configManager.getConfig(ScreenshotConfig.class);
   }

   protected void startUp() throws Exception {
      RuneLite.SCREENSHOT_DIR.mkdirs();
      this.keyManager.registerKeyListener(this.hotkeyListener);
      BufferedImage iconImage = ImageUtil.loadImageResource(this.getClass(), "screenshot.png");
      this.titleBarButton = NavigationButton.builder().tooltip("Take screenshot").icon(iconImage).onClick(this::manualScreenshot).popup(ImmutableMap.builder().put("Open screenshot folder...", () -> {
         LinkBrowser.open(RuneLite.SCREENSHOT_DIR.toString());
      }).build()).build();
      this.clientToolbar.addNavigation(this.titleBarButton);
   }

   protected void shutDown() throws Exception {
      this.clientToolbar.removeNavigation(this.titleBarButton);
      this.keyManager.unregisterKeyListener(this.hotkeyListener);
      this.kickPlayerName = null;
      this.notificationStarted = false;
   }

   @Subscribe
   public void onGameTick(GameTick event) {
      if (this.shouldTakeScreenshot) {
         this.shouldTakeScreenshot = false;
         String screenshotSubDir = null;
         String fileName = null;
         if (this.client.getWidget(15269890) != null) {
            fileName = this.parseLevelUpWidget(15269890);
            screenshotSubDir = "Levels";
         } else {
            String text;
            if (this.client.getWidget(12648450) != null) {
               text = this.client.getWidget(12648450).getText();
               if (Text.removeTags(text).contains("High level gamble")) {
                  if (this.config.screenshotHighGamble()) {
                     fileName = parseBAHighGambleWidget(text);
                     screenshotSubDir = "BA High Gambles";
                  }
               } else if (this.config.screenshotLevels()) {
                  fileName = this.parseLevelUpWidget(12648450);
                  screenshotSubDir = "Levels";
               }
            } else if (this.client.getWidget(10027012) != null) {
               text = this.client.getWidget(10027012).getText();
               fileName = parseQuestCompletedWidget(text);
               screenshotSubDir = "Quests";
            }
         }

         if (fileName != null) {
            this.takeScreenshot(fileName, screenshotSubDir);
         }

      }
   }

   @Subscribe
   public void onActorDeath(ActorDeath actorDeath) {
      Actor actor = actorDeath.getActor();
      if (actor instanceof Player) {
         Player player = (Player)actor;
         if (player == this.client.getLocalPlayer() && this.config.screenshotPlayerDeath()) {
            this.takeScreenshot("Death", "Deaths");
         } else if (player != this.client.getLocalPlayer() && player.getCanvasTilePoly() != null && ((player.isFriendsChatMember() || player.isFriend()) && this.config.screenshotFriendDeath() || player.isClanMember() && this.config.screenshotClanDeath())) {
            this.takeScreenshot("Death " + player.getName(), "Deaths");
         }
      }

   }

   @Subscribe
   public void onAnimationChanged(AnimationChanged animationChanged) {
      Actor actor = animationChanged.getActor();
      if (actor == this.client.getLocalPlayer() && actor.getAnimation() == 10873 && this.config.screenshotPlayerDeath()) {
         this.takeScreenshot("Doom Death", "Deaths");
      }

   }

   @Subscribe
   public void onPlayerLootReceived(PlayerLootReceived playerLootReceived) {
      if (this.config.screenshotKills()) {
         Player player = playerLootReceived.getPlayer();
         String name = player.getName();
         String fileName = "Kill " + name;
         this.takeScreenshot(fileName, "PvP Kills");
      }

   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent e) {
      if ("confirmFriendsChatKick".equals(e.getEventName())) {
         Object[] objectStack = this.client.getObjectStack();
         int objectStackSize = this.client.getObjectStackSize();
         this.kickPlayerName = (String)objectStack[objectStackSize - 1];
      }
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.GAMEMESSAGE || event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.TRADE || event.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION) {
         String chatMessage = event.getMessage();
         Matcher m;
         if (chatMessage.contains("You have completed") && chatMessage.contains("Treasure")) {
            m = NUMBER_PATTERN.matcher(Text.removeTags(chatMessage));
            if (m.find()) {
               this.clueNumber = Integer.valueOf(m.group().replace(",", ""));
               this.clueType = chatMessage.substring(chatMessage.lastIndexOf(m.group()) + m.group().length() + 1, chatMessage.indexOf("Treasure") - 1);
               return;
            }
         }

         if (chatMessage.startsWith("Your Barrows chest count is")) {
            m = NUMBER_PATTERN.matcher(Text.removeTags(chatMessage));
            if (m.find()) {
               this.killType = ScreenshotPlugin.KillType.BARROWS;
               this.killCountNumber = Integer.valueOf(m.group().replace(",", ""));
               return;
            }
         }

         if (chatMessage.startsWith("Your completed Chambers of Xeric count is:")) {
            m = NUMBER_PATTERN.matcher(Text.removeTags(chatMessage));
            if (m.find()) {
               this.killType = ScreenshotPlugin.KillType.COX;
               this.killCountNumber = Integer.valueOf(m.group().replace(",", ""));
               return;
            }
         }

         if (chatMessage.startsWith("Your completed Chambers of Xeric Challenge Mode count is:")) {
            m = NUMBER_PATTERN.matcher(Text.removeTags(chatMessage));
            if (m.find()) {
               this.killType = ScreenshotPlugin.KillType.COX_CM;
               this.killCountNumber = Integer.valueOf(m.group().replace(",", ""));
               return;
            }
         }

         if (chatMessage.startsWith("Your completed Theatre of Blood")) {
            m = NUMBER_PATTERN.matcher(Text.removeTags(chatMessage));
            if (m.find()) {
               this.killType = chatMessage.contains("Hard Mode") ? ScreenshotPlugin.KillType.TOB_HM : (chatMessage.contains("Story Mode") ? ScreenshotPlugin.KillType.TOB_SM : ScreenshotPlugin.KillType.TOB);
               this.killCountNumber = Integer.valueOf(m.group().replace(",", ""));
               return;
            }
         }

         if (chatMessage.startsWith("Your completed Tombs of Amascut")) {
            m = NUMBER_PATTERN.matcher(Text.removeTags(chatMessage));
            if (m.find()) {
               this.killType = chatMessage.contains("Expert Mode") ? ScreenshotPlugin.KillType.TOA_EXPERT_MODE : (chatMessage.contains("Entry Mode") ? ScreenshotPlugin.KillType.TOA_ENTRY_MODE : ScreenshotPlugin.KillType.TOA);
               this.killCountNumber = Integer.valueOf(m.group().replace(",", ""));
               return;
            }
         }

         if (chatMessage.startsWith("Your Lunar Chest count is")) {
            m = NUMBER_PATTERN.matcher(Text.removeTags(chatMessage));
            if (m.find()) {
               this.killType = ScreenshotPlugin.KillType.MOONS_OF_PERIL;
               this.killCountNumber = Integer.valueOf(m.group().replace(",", ""));
               return;
            }
         }

         if (chatMessage.contains("Search the chest nearby to retrieve your earned rewards!")) {
            this.killType = ScreenshotPlugin.KillType.FORTIS_COLOSSEUM;
         } else {
            if (chatMessage.equals("Your request to kick/ban this user was successful.") && this.config.screenshotKick()) {
               if (this.kickPlayerName == null) {
                  return;
               }

               this.takeScreenshot("Kick " + this.kickPlayerName, "Friends Chat Kicks");
               this.kickPlayerName = null;
            }

            Stream var10000 = PET_MESSAGES.stream();
            Objects.requireNonNull(chatMessage);
            String fileName;
            if (var10000.anyMatch(chatMessage::contains) && this.config.screenshotPet()) {
               fileName = "Pet";
               this.takeScreenshot(fileName, "Pets");
            }

            String skillName;
            String skillLevel;
            String fileName;
            if (this.config.screenshotBossKills()) {
               m = BOSSKILL_MESSAGE_PATTERN.matcher(chatMessage);
               if (m.find()) {
                  skillName = Text.removeTags(m.group(1));
                  skillLevel = m.group(2).replace(",", "");
                  fileName = skillName + "(" + skillLevel + ")";
                  this.takeScreenshot(fileName, "Boss Kills");
               }
            }

            if (chatMessage.equals("You find some treasure in the chest!") && this.config.screenshotRewards()) {
               int regionID = this.client.getLocalPlayer().getWorldLocation().getRegionID();
               skillName = (String)CHEST_LOOT_EVENTS.get(regionID);
               if (skillName != null) {
                  this.takeScreenshot(skillName, "Chest Loot");
               }
            }

            if (this.config.screenshotValuableDrop()) {
               m = VALUABLE_DROP_PATTERN.matcher(chatMessage);
               if (m.matches()) {
                  int valuableDropValue = Integer.parseInt(m.group(2).replace(",", ""));
                  if (valuableDropValue >= this.config.valuableDropThreshold()) {
                     skillLevel = m.group(1);
                     fileName = "Valuable drop " + skillLevel;
                     this.takeScreenshot(fileName, "Valuable Drops");
                  }
               }
            }

            if (this.config.screenshotUntradeableDrop() && !this.isInsideGauntlet()) {
               m = UNTRADEABLE_DROP_PATTERN.matcher(chatMessage);
               if (m.matches()) {
                  skillName = m.group(1);
                  skillLevel = "Untradeable drop " + skillName;
                  this.takeScreenshot(skillLevel, "Untradeable Drops");
               }
            }

            if (this.config.screenshotDuels()) {
               m = DUEL_END_PATTERN.matcher(chatMessage);
               if (m.find()) {
                  skillName = m.group(1);
                  skillLevel = m.group(2).replace(",", "");
                  fileName = "Duel " + skillName + " (" + skillLevel + ")";
                  this.takeScreenshot(fileName, "Duels");
               }
            }

            if (chatMessage.startsWith("New item added to your collection log: ") && this.client.getVarbitValue(11959) == 1 && this.config.screenshotCollectionLogEntries()) {
               fileName = Text.removeTags(chatMessage).substring("New item added to your collection log: ".length());
               skillName = "Collection log (" + fileName + ")";
               this.takeScreenshot(skillName, "Collection Log");
            }

            if (chatMessage.contains("combat task") && this.config.screenshotCombatAchievements() && this.client.getVarbitValue(12455) == 1) {
               fileName = parseCombatAchievementWidget(chatMessage);
               if (!fileName.isEmpty()) {
                  this.takeScreenshot(fileName, "Combat Achievements");
               }
            }

            if (this.client.getVarbitValue(20157) == 1 && this.config.screenshotLevels()) {
               m = LEVEL_UP_MESSAGE_PATTERN.matcher(chatMessage);
               if (m.matches()) {
                  skillName = StringUtils.capitalize(m.group("skill") != null ? m.group("skill") : m.group("skill99"));
                  skillLevel = m.group("level") != null ? m.group("level") : "99";
                  fileName = skillName + "(" + skillLevel + ")";
                  this.takeScreenshot(fileName, "Levels");
               }
            }

         }
      }
   }

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded event) {
      int groupId = event.getGroupId();
      switch (groupId) {
         case 23:
         case 73:
         case 153:
         case 155:
         case 539:
         case 771:
         case 864:
         case 868:
            if (!this.config.screenshotRewards()) {
               return;
            }
            break;
         case 193:
            if (!this.config.screenshotLevels() && !this.config.screenshotHighGamble()) {
               return;
            }
            break;
         case 233:
            if (!this.config.screenshotLevels()) {
               return;
            }
            break;
         case 616:
            if (!this.config.screenshotKingdom()) {
               return;
            }
            break;
         case 742:
            if (!this.config.screenshotWildernessLootChest()) {
               return;
            }
      }

      String fileName;
      String screenshotSubDir;
      switch (groupId) {
         case 23:
            if (this.killType != ScreenshotPlugin.KillType.TOB && this.killType != ScreenshotPlugin.KillType.TOB_SM && this.killType != ScreenshotPlugin.KillType.TOB_HM) {
               return;
            }

            switch (this.killType) {
               case TOB:
                  fileName = "Theatre of Blood(" + this.killCountNumber + ")";
                  break;
               case TOB_SM:
                  fileName = "Theatre of Blood Story Mode(" + this.killCountNumber + ")";
                  break;
               case TOB_HM:
                  fileName = "Theatre of Blood Hard Mode(" + this.killCountNumber + ")";
                  break;
               default:
                  throw new IllegalStateException();
            }

            screenshotSubDir = "Boss Kills";
            this.killType = null;
            this.killCountNumber = 0;
            break;
         case 73:
            if (this.clueType == null || this.clueNumber == null) {
               return;
            }

            char var10000 = Character.toUpperCase(this.clueType.charAt(0));
            fileName = "" + var10000 + this.clueType.substring(1) + "(" + this.clueNumber + ")";
            screenshotSubDir = "Clue Scroll Rewards";
            this.clueType = null;
            this.clueNumber = null;
            break;
         case 153:
         case 193:
         case 233:
            this.shouldTakeScreenshot = true;
            return;
         case 155:
            if (this.killType != ScreenshotPlugin.KillType.BARROWS) {
               return;
            }

            fileName = "Barrows(" + this.killCountNumber + ")";
            screenshotSubDir = "Boss Kills";
            this.killType = null;
            this.killCountNumber = 0;
            break;
         case 539:
            if (this.killType == ScreenshotPlugin.KillType.COX) {
               fileName = "Chambers of Xeric(" + this.killCountNumber + ")";
               screenshotSubDir = "Boss Kills";
               this.killType = null;
               this.killCountNumber = 0;
            } else {
               if (this.killType != ScreenshotPlugin.KillType.COX_CM) {
                  return;
               }

               fileName = "Chambers of Xeric Challenge Mode(" + this.killCountNumber + ")";
               screenshotSubDir = "Boss Kills";
               this.killType = null;
               this.killCountNumber = 0;
            }
            break;
         case 616:
            fileName = "Kingdom " + String.valueOf(LocalDate.now());
            screenshotSubDir = "Kingdom Rewards";
            break;
         case 742:
            fileName = "Loot key";
            screenshotSubDir = "Wilderness Loot Chest";
            break;
         case 771:
            if (this.killType != ScreenshotPlugin.KillType.TOA && this.killType != ScreenshotPlugin.KillType.TOA_ENTRY_MODE && this.killType != ScreenshotPlugin.KillType.TOA_EXPERT_MODE) {
               return;
            }

            switch (this.killType) {
               case TOA:
                  fileName = "Tombs of Amascut(" + this.killCountNumber + ")";
                  break;
               case TOA_ENTRY_MODE:
                  fileName = "Tombs of Amascut Entry Mode(" + this.killCountNumber + ")";
                  break;
               case TOA_EXPERT_MODE:
                  fileName = "Tombs of Amascut Expert Mode(" + this.killCountNumber + ")";
                  break;
               default:
                  throw new IllegalStateException();
            }

            screenshotSubDir = "Boss Kills";
            this.killType = null;
            this.killCountNumber = 0;
            break;
         case 864:
            if (this.killType != ScreenshotPlugin.KillType.FORTIS_COLOSSEUM) {
               return;
            }

            fileName = "Fortis Colosseum Chest";
            screenshotSubDir = "Chest Loot";
            this.killType = null;
            break;
         case 868:
            if (this.killType != ScreenshotPlugin.KillType.MOONS_OF_PERIL) {
               return;
            }

            fileName = "Moons of Peril(" + this.killCountNumber + ")";
            screenshotSubDir = "Boss Kills";
            this.killType = null;
            this.killCountNumber = 0;
            break;
         default:
            return;
      }

      this.takeScreenshot(fileName, screenshotSubDir);
   }

   @Subscribe
   public void onScriptPreFired(ScriptPreFired scriptPreFired) {
      switch (scriptPreFired.getScriptId()) {
         case 3346:
            this.notificationStarted = true;
            break;
         case 3347:
            if (!this.notificationStarted) {
               return;
            }

            String topText = this.client.getVarcStrValue(387);
            String bottomText = this.client.getVarcStrValue(388);
            log.debug("Notification: top: {} bottom: {}", topText, bottomText);
            String task;
            if (topText.equalsIgnoreCase("Collection log") && this.config.screenshotCollectionLogEntries()) {
               String entry = Text.removeTags(bottomText).substring("New item:".length());
               task = "Collection log (" + entry + ")";
               this.takeScreenshot(task, "Collection Log");
            }

            String fileName;
            String[] s;
            if (topText.equalsIgnoreCase("Combat Task Completed!") && this.config.screenshotCombatAchievements() && this.client.getVarbitValue(12455) == 0) {
               s = bottomText.split("<.*?>");
               task = s[1];
               fileName = "Combat task (" + task.replaceAll("[:?]", "") + ")";
               this.takeScreenshot(fileName, "Combat Achievements");
            }

            if (topText.equalsIgnoreCase("League Task Complete!") && this.config.screenshotLeagueTasks()) {
               s = bottomText.split("<.*?>");
               task = s[1];
               fileName = "League task (" + task.replaceAll("[:?]", "") + ")";
               this.takeScreenshot(fileName, "League Tasks");
            }

            this.notificationStarted = false;
      }

   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired e) {
      if (e.getScriptId() == 7928 && this.config.screenshotRewards()) {
         this.takeScreenshot("Doom of Mokhaiotl", "Chest Loot");
      }

   }

   @Subscribe
   private void onPostClientTick(PostClientTick e) {
      if (!this.consumers.isEmpty()) {
         Widget reportButtonTextWidget = this.client.getWidget(10616865);
         if (reportButtonTextWidget != null) {
            if (this.reportButtonText == null) {
               this.reportButtonText = reportButtonTextWidget.getText();
            }

            reportButtonTextWidget.setText(DATE_FORMAT.format(new Date()));
         }

         Consumer consumer;
         while((consumer = (Consumer)this.consumers.poll()) != null) {
            this.drawManager.requestNextFrameListener(consumer);
         }
      }

   }

   private void manualScreenshot() {
      this.takeScreenshot("", (String)null);
   }

   String parseLevelUpWidget(int levelUpLevel) {
      Widget levelChild = this.client.getWidget(levelUpLevel);
      if (levelChild == null) {
         return null;
      } else {
         Matcher m = LEVEL_UP_PATTERN.matcher(levelChild.getText());
         if (!m.matches()) {
            return null;
         } else {
            String skillName = m.group(1);
            String skillLevel = m.group(2);
            return skillName + "(" + skillLevel + ")";
         }
      }
   }

   @VisibleForTesting
   static String parseQuestCompletedWidget(String text) {
      Matcher questMatch1 = QUEST_PATTERN_1.matcher(text);
      Matcher questMatch2 = QUEST_PATTERN_2.matcher(text);
      Matcher questMatchFinal = questMatch1.matches() ? questMatch1 : questMatch2;
      if (!questMatchFinal.matches()) {
         return "Quest(quest not found)";
      } else {
         String quest = questMatchFinal.group("quest");
         String verb = questMatchFinal.group("verb") != null ? questMatchFinal.group("verb") : "";
         if (verb.contains("kind of")) {
            quest = quest + " partial completion";
         } else if (verb.contains("completely")) {
            quest = quest + " II";
         }

         Stream var10000 = RFD_TAGS.stream();
         String var10001 = quest + verb;
         Objects.requireNonNull(var10001);
         if (var10000.anyMatch(var10001::contains)) {
            quest = "Recipe for Disaster - " + quest;
         }

         var10000 = WORD_QUEST_IN_NAME_TAGS.stream();
         Objects.requireNonNull(quest);
         if (var10000.anyMatch(quest::contains)) {
            quest = quest + " Quest";
         }

         return "Quest(" + quest + ")";
      }
   }

   @VisibleForTesting
   static String parseBAHighGambleWidget(String text) {
      Matcher highGambleMatch = BA_HIGH_GAMBLE_REWARD_PATTERN.matcher(text);
      if (highGambleMatch.find()) {
         String gambleCount = highGambleMatch.group("gambleCount");
         return String.format("High Gamble(%s)", gambleCount);
      } else {
         return "High Gamble(count not found)";
      }
   }

   @VisibleForTesting
   static String parseCombatAchievementWidget(String text) {
      Matcher m = COMBAT_ACHIEVEMENTS_PATTERN.matcher(text);
      if (m.find()) {
         String task = m.group("task").replaceAll("[:?]", "");
         return "Combat task (" + task + ")";
      } else {
         return "";
      }
   }

   @VisibleForTesting
   void takeScreenshot(String fileName, String subDir) {
      if (this.client.getGameState() == GameState.LOGIN_SCREEN) {
         log.info("Login screenshot prevented");
      } else {
         Consumer<Image> imageCallback = (img) -> {
            this.executor.submit(() -> {
               this.saveScreenshot(fileName, subDir, img);
               if (this.reportButtonText != null) {
                  this.clientThread.invokeLater(() -> {
                     Widget reportButtonTextWidget = this.client.getWidget(10616865);
                     if (reportButtonTextWidget != null) {
                        reportButtonTextWidget.setText(this.reportButtonText);
                     }

                     this.reportButtonText = null;
                  });
               }

            });
         };
         if (this.config.displayDate() && REPORT_BUTTON_TLIS.contains(this.client.getTopLevelInterfaceId())) {
            this.queueForTimestamp(imageCallback);
         } else {
            this.drawManager.requestNextFrameListener(imageCallback);
         }

      }
   }

   void queueForTimestamp(Consumer<Image> screenshotConsumer) {
      this.consumers.add(screenshotConsumer);
   }

   private void saveScreenshot(String fileName, String subDir, Image image) {
      BufferedImage screenshot;
      if (!this.config.includeFrame()) {
         screenshot = ImageUtil.bufferedImageFromImage(image);
      } else {
         screenshot = this.imageCapture.addClientFrame(image);
      }

      this.imageCapture.saveScreenshot(screenshot, fileName, subDir, this.config.notifyWhenTaken(), this.config.copyToClipboard());
   }

   private boolean isInsideGauntlet() {
      return this.client.isInInstancedRegion() && this.client.getMapRegions().length > 0 && (this.client.getMapRegions()[0] == 7512 || this.client.getMapRegions()[0] == 7768);
   }

   @VisibleForTesting
   int getClueNumber() {
      return this.clueNumber;
   }

   @VisibleForTesting
   String getClueType() {
      return this.clueType;
   }

   @VisibleForTesting
   KillType getKillType() {
      return this.killType;
   }

   @VisibleForTesting
   int getKillCountNumber() {
      return this.killCountNumber;
   }

   static enum KillType {
      BARROWS,
      COX,
      COX_CM,
      MOONS_OF_PERIL,
      TOB,
      TOB_SM,
      TOB_HM,
      TOA_ENTRY_MODE,
      TOA,
      TOA_EXPERT_MODE,
      FORTIS_COLOSSEUM;
   }
}
