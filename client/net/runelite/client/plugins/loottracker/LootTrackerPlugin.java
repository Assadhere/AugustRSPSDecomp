package net.runelite.client.plugins.loottracker;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Provides;
import custom.AddToLootTrackerScript;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.NonNull;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MessageNode;
import net.runelite.api.NPCComposition;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.WorldType;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.PostClientTick;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.account.AccountSession;
import net.runelite.client.account.SessionManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ConfigSync;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.events.ServerNpcLoot;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;
import net.runelite.client.game.LootManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;
import net.runelite.http.api.item.ItemPrice;
import net.runelite.http.api.loottracker.GameItem;
import net.runelite.http.api.loottracker.LootRecord;
import net.runelite.http.api.loottracker.LootRecordType;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Loot Tracker",
   description = "Tracks loot from monsters and minigames",
   tags = {"drops"}
)
public class LootTrackerPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(LootTrackerPlugin.class);
   private static final int MAX_DROPS = 1024;
   private static final Duration MAX_AGE = Duration.ofDays(365L);
   private static final int INVCHANGE_TIMEOUT = 10;
   private static final Pattern CLUE_SCROLL_PATTERN = Pattern.compile("You have completed [0-9]+ ([a-z]+) Treasure Trails?\\.");
   private static final int THEATRE_OF_BLOOD_REGION = 12867;
   private static final int THEATRE_OF_BLOOD_LOBBY = 14642;
   private static final int BA_LOBBY_REGION = 10039;
   private static final List<Integer> PVP_LOOT_KEY_CONTAINERS = List.of(558, 559, 560, 561, 562);
   private static final List<Integer> PVP_LOOT_KEYS = List.of(26651, 26652, 26653, 26654, 26655);
   @VisibleForTesting
   static final String HERBIBOAR_LOOTED_MESSAGE = "You harvest herbs from the herbiboar, whereupon it escapes.";
   private static final String HERBIBOAR_EVENT = "Herbiboar";
   private static final Pattern HERBIBOAR_HERB_SACK_PATTERN = Pattern.compile(".+(Grimy .+?) herb.+");
   static final String ZOMBIE_PIRATE_LOCKER_EVENT = "Zombie Pirate's Locker";
   private static final Pattern ZOMBIE_PIRATE_LOCKER_PATTERN = Pattern.compile("You loot the locker and receive <col=[\\da-f]{6}>(?<qty>[\\d,]+) x (?<item>.+)</col>\\.");
   private static final Pattern SALVAGE_PATTERN = Pattern.compile("You sort through the\\s+(?<tier>\\S+)\\s+salvage.*");
   private static final String SEEDPACK_EVENT = "Seed pack";
   private static final String CHEST_LOOTED_MESSAGE = "You find some treasure in the chest!";
   private static final Pattern ROGUES_CHEST_PATTERN = Pattern.compile("You find (a|some)([a-z\\s]*) inside.");
   private static final Pattern LARRAN_LOOTED_PATTERN = Pattern.compile("You have opened Larran's (big|small) chest .*");
   private static final String ALCHEMIST_SIGNET_CHEST_MESSAGE = "You take some loot from inside.";
   private static final String OTHER_CHEST_LOOTED_MESSAGE = "You steal some loot from the chest.";
   private static final String DORGESH_KAAN_CHEST_LOOTED_MESSAGE = "You find treasure inside!";
   private static final Pattern GRUBBY_CHEST_LOOTED_MESSAGE = Pattern.compile("You find treasure(?: and supplies|, supplies, and a weirdly coloured egg sac) within the chest.");
   private static final String ANCIENT_CHEST_LOOTED_MESSAGE = "You open the chest and find";
   private static final Pattern HAM_CHEST_LOOTED_PATTERN = Pattern.compile("Your (?<key>[a-z]+) key breaks in the lock.*");
   private static final int HAM_STOREROOM_REGION = 10321;
   private static final int LAVA_MAZE_NORTH_EAST_REGION = 12348;
   private static final Map<Integer, String> CHEST_EVENT_TYPES = (new ImmutableMap.Builder()).put(5179, "Brimstone Chest").put(11573, "Crystal Chest").put(12093, "Larran's big chest").put(13113, "Larran's small chest").put(13151, "Elven Crystal Chest").put(5277, "Stone chest").put(10835, "Dorgesh-Kaan Chest").put(10834, "Dorgesh-Kaan Chest").put(7323, "Grubby Chest").put(8593, "Isle of Souls Chest").put(7827, "Dark Chest").put(13117, "Rogues' Chest").put(13156, "Chest (Ancient Vault)").put(12348, "Muddy Chest").put(5422, "Chest (Aldarin Villas)").put(6550, "Chest (Moon key)").put(5521, "Chest (Alchemist's signet)").put(12073, "Rusty chest").put(7470, "Rusty chest").put(6187, "Tarnished chest").put(6953, "Tarnished chest").put(7743, "Reinforced chest").put(8758, "Reinforced chest").build();
   private static final Set<String> SLAYER_CHEST_EVENT_TYPES = ImmutableSet.of("Brimstone Chest", "Larran's big chest", "Larran's small chest");
   private static final Pattern SHADE_CHEST_NO_KEY_PATTERN = Pattern.compile("You need a [a-z]+ key with a [a-z]+ trim to open this chest .*");
   private static final Map<Integer, String> SHADE_CHEST_OBJECTS = (new ImmutableMap.Builder()).put(4111, "Bronze key red").put(4112, "Bronze key brown").put(4113, "Bronze key crimson").put(4114, "Bronze key black").put(4115, "Bronze key purple").put(4116, "Steel key red").put(4117, "Steel key brown").put(4118, "Steel key crimson").put(4119, "Steel key black").put(4120, "Steel key purple").put(4121, "Black key red").put(4122, "Black key brown").put(4123, "Black key crimson").put(4124, "Black key black").put(4125, "Black key purple").put(4126, "Silver key red").put(4127, "Silver key brown").put(4128, "Silver key crimson").put(4129, "Silver key black").put(4130, "Silver key purple").put(41212, "Gold key red").put(41213, "Gold key brown").put(41214, "Gold key crimson").put(41215, "Gold key black").put(41216, "Gold key purple").build();
   private static final String HALLOWED_SACK_EVENT = "Hallowed Sack";
   private static final Set<Integer> LAST_MAN_STANDING_REGIONS = ImmutableSet.of(13658, 13659, 13660, 13914, 13915, 13916, new Integer[]{13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432});
   private static final Pattern PICKPOCKET_REGEX = Pattern.compile("You pick (the )?(?<target>.+)'s? pocket.*");
   private static final String BIRDNEST_EVENT = "Bird nest";
   private static final Set<Integer> BIRDNEST_IDS = ImmutableSet.of(5070, 5071, 5072, 5073, 5074, 7413, new Integer[]{13653, 22798, 22800});
   private static final Pattern BIRDHOUSE_PATTERN = Pattern.compile("You dismantle and discard the trap, retrieving (?:(?:a|\\d{1,2}) nests?, )?10 dead birds, \\d{1,3} feathers and (\\d,?\\d{1,3}) Hunter XP\\.");
   private static final Map<Integer, String> BIRDHOUSE_XP_TO_TYPE = (new ImmutableMap.Builder()).put(280, "Regular Bird House").put(420, "Oak Bird House").put(560, "Willow Bird House").put(700, "Teak Bird House").put(820, "Maple Bird House").put(960, "Mahogany Bird House").put(1020, "Yew Bird House").put(1140, "Magic Bird House").put(1200, "Redwood Bird House").build();
   private static final Multimap<String, String> PICKPOCKET_DISAMBIGUATION_MAP = ImmutableMultimap.of("H.A.M. Member", "Man", "H.A.M. Member", "Woman");
   private static final String CASKET_EVENT = "Casket";
   private static final String ORE_PACK_VM_EVENT = "Ore Pack (Volcanic Mine)";
   private static final String WINTERTODT_SUPPLY_CRATE_EVENT = "Supply crate (Wintertodt)";
   private static final String WINTERTODT_REWARD_CART_EVENT = "Reward cart (Wintertodt)";
   private static final String WINTERTODT_LOOT_STRING = "You found some loot: ";
   private static final int WINTERTODT_REGION = 6461;
   private static final String BAG_FULL_OF_GEMS_PERCY_EVENT = "Bag full of gems (Percy)";
   private static final String BAG_FULL_OF_GEMS_BELONA_EVENT = "Bag full of gems (Belona)";
   private static final String BAG_FULL_OF_GEMS_DUSURI_EVENT = "Bag full of gems (Dusuri)";
   private static final String SPOILS_OF_WAR_EVENT = "Spoils of war";
   private static final Set<Integer> SOUL_WARS_REGIONS = ImmutableSet.of(8493, 8749, 9005);
   private static final String TEMPOROSS_EVENT = "Reward pool (Tempoross)";
   private static final String TEMPOROSS_CASKET_EVENT = "Casket (Tempoross)";
   private static final String TEMPOROSS_LOOT_STRING = "You found some loot: ";
   private static final int TEMPOROSS_REGION = 12588;
   private static final String GUARDIANS_OF_THE_RIFT_EVENT = "Guardians of the Rift";
   private static final String GUARDIANS_OF_THE_RIFT_LOOT_STRING = "You found some loot: ";
   private static final int GUARDIANS_OF_THE_RIFT_REGION = 14484;
   private static final String MAHOGANY_CRATE_EVENT = "Supply crate (Mahogany Homes)";
   private static final String CHAMBERS_OF_XERIC = "Chambers of Xeric";
   private static final String THEATRE_OF_BLOOD = "Theatre of Blood";
   private static final String TOMBS_OF_AMASCUT = "Tombs of Amascut";
   private static final int FONT_OF_CONSUMPTION_REGION = 12106;
   private static final String FONT_OF_CONSUMPTION_USE_MESSAGE = "You place the Unsired into the Font of Consumption...";
   private static final String BA_HIGH_GAMBLE = "Barbarian Assault high gamble";
   private static final String DOM = "Doom of Mokhaiotl";
   private static final Set<Character> VOWELS = ImmutableSet.of('a', 'e', 'i', 'o', 'u');
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private ItemManager itemManager;
   @Inject
   private SpriteManager spriteManager;
   @Inject
   private LootTrackerConfig config;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private SessionManager sessionManager;
   @Inject
   private ScheduledExecutorService executor;
   @Inject
   private EventBus eventBus;
   @Inject
   private ChatMessageManager chatMessageManager;
   @Inject
   private LootManager lootManager;
   @Inject
   private ConfigManager configManager;
   @Inject
   private Gson gson;
   @Inject
   private LootTrackerClient lootTrackerClient;
   private LootTrackerPanel panel;
   private NavigationButton navButton;
   private boolean chestLooted;
   private boolean pvpKeysLooted;
   private boolean lastLoadingIntoInstance;
   private String lastPickpocketTarget;
   private int ignorePickpocketLoot;
   private List<String> ignoredItems = new ArrayList();
   private List<String> ignoredEvents = new ArrayList();
   private int inventoryId = -1;
   private Multiset<Integer> inventorySnapshot;
   private InvChangeCallback inventorySnapshotCb;
   private int inventoryTimeout;
   private final List<LootRecord> queuedLoots = new ArrayList();
   private String profileKey;

   private static Collection<ItemStack> stack(Collection<ItemStack> items) {
      List<ItemStack> list = new ArrayList();
      Iterator var2 = items.iterator();

      while(var2.hasNext()) {
         ItemStack item = (ItemStack)var2.next();
         int quantity = 0;
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            ItemStack i = (ItemStack)var5.next();
            if (i.getId() == item.getId()) {
               quantity = i.getQuantity();
               list.remove(i);
               break;
            }
         }

         if (quantity > 0) {
            list.add(new ItemStack(item.getId(), item.getQuantity() + quantity));
         } else {
            list.add(item);
         }
      }

      return list;
   }

   @Provides
   LootTrackerConfig provideConfig(ConfigManager configManager) {
      return (LootTrackerConfig)configManager.getConfig(LootTrackerConfig.class);
   }

   @Subscribe
   public void onSessionOpen(SessionOpen sessionOpen) {
      AccountSession accountSession = this.sessionManager.getAccountSession();
      if (accountSession.getUuid() != null) {
         this.lootTrackerClient.setUuid(accountSession.getUuid());
      } else {
         this.lootTrackerClient.setUuid((UUID)null);
      }

   }

   @Subscribe
   public void onSessionClose(SessionClose sessionClose) {
      this.lootTrackerClient.setUuid((UUID)null);
   }

   @Subscribe
   public void onConfigSync(ConfigSync configSync) {
      this.submitLoot();
   }

   @Subscribe
   public void onRuneScapeProfileChanged(RuneScapeProfileChanged e) {
      String profileKey = this.configManager.getRSProfileKey();
      if (profileKey != null) {
         if (!profileKey.equals(this.profileKey)) {
            this.switchProfile(profileKey);
         }
      }
   }

   private void switchProfile(String profileKey) {
      this.executor.execute(() -> {
         this.submitLoot();
         this.profileKey = profileKey;
         log.debug("Switched to profile {}", profileKey);
         if (this.config.rememberLoot()) {
            int drops = 0;
            List<ConfigLoot> loots = new ArrayList();
            Instant old = Instant.now().minus(MAX_AGE);
            Iterator var5 = this.configManager.getRSProfileConfigurationKeys("loottracker", profileKey, "drops_").iterator();

            while(var5.hasNext()) {
               String key = (String)var5.next();
               String json = this.configManager.getConfiguration("loottracker", profileKey, key);

               ConfigLoot configLoot;
               try {
                  configLoot = (ConfigLoot)this.gson.fromJson(json, ConfigLoot.class);
               } catch (JsonSyntaxException var10) {
                  JsonSyntaxException ex = var10;
                  log.warn("Removing loot with malformed json: {}", json, ex);
                  this.configManager.unsetConfiguration("loottracker", profileKey, key);
                  continue;
               }

               if (configLoot.last.isBefore(old)) {
                  log.debug("Removing old loot for {} {}", configLoot.type, configLoot.name);
                  this.configManager.unsetConfiguration("loottracker", profileKey, key);
               } else if (drops < 1024 || loots.isEmpty() || !((ConfigLoot)loots.get(0)).last.isAfter(configLoot.last)) {
                  sortedInsert(loots, configLoot, Comparator.comparing(ConfigLoot::getLast));
                  drops += configLoot.numDrops();
                  if (drops >= 1024) {
                     ConfigLoot top = (ConfigLoot)loots.remove(0);
                     drops -= top.numDrops();
                  }
               }
            }

            log.debug("Loaded {} records", loots.size());
            this.clientThread.invokeLater(() -> {
               if (this.client.getGameState().getState() < GameState.LOGIN_SCREEN.getState()) {
                  return false;
               } else {
                  List<LootTrackerRecord> records = (List)loots.stream().map(this::convertToLootTrackerRecord).collect(Collectors.toList());
                  SwingUtilities.invokeLater(() -> {
                     this.panel.clearRecords();
                     this.panel.addRecords(records);
                  });
                  return true;
               }
            });
         }
      });
   }

   private static <T> void sortedInsert(List<T> list, T value, Comparator<? super T> c) {
      int idx = Collections.binarySearch(list, value, c);
      list.add(idx < 0 ? -idx - 1 : idx, value);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("loottracker")) {
         LootTrackerPanel var10000;
         if (!"ignoredItems".equals(event.getKey()) && !"ignoredEvents".equals(event.getKey())) {
            if ("priceType".equals(event.getKey()) || "showPriceType".equals(event.getKey())) {
               var10000 = this.panel;
               Objects.requireNonNull(var10000);
               SwingUtilities.invokeLater(var10000::rebuild);
            }
         } else {
            this.ignoredItems = Text.fromCSV(this.config.getIgnoredItems());
            this.ignoredEvents = Text.fromCSV(this.config.getIgnoredEvents());
            var10000 = this.panel;
            Objects.requireNonNull(var10000);
            SwingUtilities.invokeLater(var10000::updateIgnoredRecords);
         }
      }

   }

   protected void startUp() throws Exception {
      this.profileKey = null;
      this.ignoredItems = Text.fromCSV(this.config.getIgnoredItems());
      this.ignoredEvents = Text.fromCSV(this.config.getIgnoredEvents());
      this.panel = new LootTrackerPanel(this, this.itemManager, this.config);
      SpriteManager var10000 = this.spriteManager;
      LootTrackerPanel var10003 = this.panel;
      Objects.requireNonNull(var10003);
      var10000.getSpriteAsync(900, 0, (Consumer)(var10003::loadHeaderIcon));
      BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "panel_icon.png");
      this.navButton = NavigationButton.builder().tooltip("Loot Tracker").icon(icon).priority(5).panel(this.panel).build();
      this.clientToolbar.addNavigation(this.navButton);
      AccountSession accountSession = this.sessionManager.getAccountSession();
      if (accountSession != null) {
         this.lootTrackerClient.setUuid(accountSession.getUuid());
      }

      String profileKey = this.configManager.getRSProfileKey();
      if (profileKey != null) {
         this.switchProfile(profileKey);
      }

   }

   protected void shutDown() {
      this.submitLoot();
      this.clientToolbar.removeNavigation(this.navButton);
      this.lootTrackerClient.setUuid((UUID)null);
      this.chestLooted = false;
   }

   @Subscribe
   public void onClientShutdown(ClientShutdown event) {
      Future<Void> future = this.submitLoot();
      if (future != null) {
         event.waitFor(future);
      }

   }

   @Subscribe
   public void onAddToLootTrackerScript(AddToLootTrackerScript packet) {
      List<ItemStack> items = new ArrayList();

      for(int i = 0; i < packet.getItemIds().length; ++i) {
         items.add(new ItemStack(packet.getItemIds()[i], packet.getItemAmounts()[i], (LocalPoint)null));
      }

      this.addLoot(packet.getName(), -1, LootRecordType.EVENT, (Object)null, items, packet.getAmount());
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      boolean inInstancedRegion = this.client.isInInstancedRegion();
      if (event.getGameState() == GameState.LOADING && inInstancedRegion != this.lastLoadingIntoInstance) {
         this.lastLoadingIntoInstance = inInstancedRegion;
         this.chestLooted = false;
      }

   }

   private void initLoot(LootRecordType type, String name) {
      if (!this.panel.hasRecord(type, name) && this.config.rememberLoot()) {
         ConfigLoot loot = this.getLootConfig(type, name);
         if (loot != null) {
            log.debug("Loaded {} records for {} {}", new Object[]{loot.numDrops(), type, name});
            LootTrackerRecord record = this.convertToLootTrackerRecord(loot);
            SwingUtilities.invokeLater(() -> {
               this.panel.addRecords(Collections.singleton(record));
            });
         }
      }
   }

   void addLoot(@NonNull String name, int combatLevel, LootRecordType type, Object metadata, Collection<ItemStack> items) {
      if (name == null) {
         throw new NullPointerException("name is marked non-null but is null");
      } else {
         this.addLoot(name, combatLevel, type, metadata, items, 1);
      }
   }

   void addLoot(@NonNull String name, int combatLevel, LootRecordType type, Object metadata, Collection<ItemStack> items, int amount) {
      if (name == null) {
         throw new NullPointerException("name is marked non-null but is null");
      } else {
         this.initLoot(type, name);
         LootTrackerItem[] entries = this.buildEntries(stack(items));
         SwingUtilities.invokeLater(() -> {
            this.panel.add(name, type, combatLevel, entries, amount);
         });
         LootRecord lootRecord = new LootRecord(name, type, metadata, toGameItems(items), Instant.now(), this.getLootWorldId(), amount, (String)null);
         synchronized(this.queuedLoots) {
            this.queuedLoots.add(lootRecord);
         }

         this.eventBus.post(new LootReceived(name, combatLevel, type, items, amount, metadata));
      }
   }

   private Integer getLootWorldId() {
      EnumSet<WorldType> worldType = this.client.getWorldType();
      return !worldType.contains(WorldType.SEASONAL) && !worldType.contains(WorldType.TOURNAMENT_WORLD) && !worldType.contains(WorldType.BETA_WORLD) ? null : this.client.getWorld();
   }

   @Subscribe
   public void onPostClientTick(PostClientTick postClientTick) {
      if (this.inventoryTimeout > 0 && --this.inventoryTimeout == 0) {
         log.debug("Inventory snapshot: Loot timeout");
         this.resetEvent();
      }

   }

   private Object buildNpcMetadata(NPCComposition npc) {
      if (this.client.getWorldType().contains(WorldType.SEASONAL)) {
         NpcMetadata md = new NpcMetadata();
         md.setId(npc.getId());
         md.setR1(this.client.getVarbitValue(10049));
         md.setR2(this.client.getVarbitValue(10050));
         md.setR3(this.client.getVarbitValue(10051));
         md.setR4(this.client.getVarbitValue(10052));
         md.setR5(this.client.getVarbitValue(10053));
         md.setR6(this.client.getVarbitValue(11696));
         md.setR7(this.client.getVarbitValue(17301));
         md.setR8(this.client.getVarbitValue(17302));
         return md;
      } else if (npc.getName() != null && npc.getName().equals("Cave horror")) {
         Player localPlayer = this.client.getLocalPlayer();
         if (localPlayer == null) {
            return null;
         } else {
            WorldPoint location = localPlayer.getWorldLocation();
            return Map.of("id", npc.getId(), "x", location.getX(), "y", location.getY(), "plane", location.getPlane(), "world", this.client.getWorld());
         }
      } else {
         return npc.getId();
      }
   }

   @Subscribe
   public void onServerNpcLoot(ServerNpcLoot event) {
      NPCComposition npc = event.getComposition();
      Collection<ItemStack> items = event.getItems();
      String name = Text.removeTags(npc.getName());
      int combat = npc.getCombatLevel();
      if (this.ignorePickpocketLoot != this.client.getTickCount()) {
         this.addLoot(name, combat, LootRecordType.NPC, this.buildNpcMetadata(npc), items);
         if (this.config.npcKillChatMessage()) {
            String prefix = VOWELS.contains(Character.toLowerCase(name.charAt(0))) ? "an" : "a";
            this.lootReceivedChatMessage(items, prefix + " " + name);
         }

      }
   }

   @Subscribe
   public void onPluginLootReceived(PluginLootReceived event) {
      log.debug("Plugin loot received from {}: {}", event.getSource().getName(), event.getItems());
      this.addLoot(event.getName(), event.getCombatLevel(), event.getType(), event.getItems(), event.getItems());
   }

   @Subscribe
   public void onPlayerLootReceived(PlayerLootReceived playerLootReceived) {
      if (!this.isPlayerWithinMapRegion(LAST_MAN_STANDING_REGIONS) && !this.isPlayerWithinMapRegion(SOUL_WARS_REGIONS)) {
         Player player = playerLootReceived.getPlayer();
         Collection<ItemStack> items = playerLootReceived.getItems();
         String name = player.getName();
         int combat = player.getCombatLevel();
         this.addLoot(name, combat, LootRecordType.PLAYER, (Object)null, items);
         if (this.config.pvpKillChatMessage()) {
            this.lootReceivedChatMessage(items, name);
         }

      }
   }

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
      Object metadata = null;
      String event;
      ItemContainer container;
      switch (widgetLoaded.getGroupId()) {
         case 23:
            if (!this.chestLooted && this.inTobChestRegion()) {
               event = "Theatre of Blood";
               container = this.client.getItemContainer(612);
               this.chestLooted = true;
               break;
            }

            return;
         case 155:
            event = "Barrows";
            container = this.client.getItemContainer(141);
            break;
         case 367:
            event = "Fishing Trawler";
            metadata = this.client.getBoostedSkillLevel(Skill.FISHING);
            container = this.client.getItemContainer(0);
            break;
         case 539:
            if (this.chestLooted) {
               return;
            }

            event = "Chambers of Xeric";
            container = this.client.getItemContainer(581);
            this.chestLooted = true;
            break;
         case 607:
            event = "Drift Net";
            metadata = this.client.getBoostedSkillLevel(Skill.FISHING);
            container = this.client.getItemContainer(307);
            break;
         case 616:
            event = "Kingdom of Miscellania";
            container = this.client.getItemContainer(390);
            break;
         case 742:
            if (this.pvpKeysLooted) {
               return;
            } else {
               this.pvpKeysLooted = this.addPvpChestLoot();
               return;
            }
         case 771:
            if (this.chestLooted) {
               return;
            }

            int raidLevel = this.client.getVarbitValue(14380);
            int teamSize = Math.min(this.client.getVarbitValue(14346), 1) + Math.min(this.client.getVarbitValue(14347), 1) + Math.min(this.client.getVarbitValue(14348), 1) + Math.min(this.client.getVarbitValue(14349), 1) + Math.min(this.client.getVarbitValue(14350), 1) + Math.min(this.client.getVarbitValue(14351), 1) + Math.min(this.client.getVarbitValue(14352), 1) + Math.min(this.client.getVarbitValue(14353), 1);
            int raidDamage = this.client.getVarbitValue(14325);
            event = "Tombs of Amascut";
            container = this.client.getItemContainer(811);
            metadata = new int[]{raidLevel, teamSize, raidDamage};
            this.chestLooted = true;
            break;
         case 864:
            if (this.chestLooted) {
               return;
            }

            event = "Fortis Colosseum";
            container = this.client.getItemContainer(843);
            this.chestLooted = true;
            break;
         case 868:
            event = "Lunar Chest";
            container = this.client.getItemContainer(847);
            break;
         default:
            return;
      }

      if (container != null) {
         Collection<ItemStack> items = toItemStacks(container);
         if (this.config.showRaidsLootValue() && (event.equals("Theatre of Blood") || event.equals("Chambers of Xeric") || event.equals("Tombs of Amascut"))) {
            long totalValue = items.stream().filter((item) -> {
               return item.getId() > -1;
            }).mapToLong((item) -> {
               return this.config.priceType() == LootTrackerPriceType.GRAND_EXCHANGE ? (long)this.itemManager.getItemPrice(item.getId()) * (long)item.getQuantity() : (long)this.itemManager.getItemComposition(item.getId()).getHaPrice() * (long)item.getQuantity();
            }).sum();
            String chatMessage = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Your loot is worth around ").append(ChatColorType.HIGHLIGHT).append(QuantityFormatter.formatNumber(totalValue)).append(ChatColorType.NORMAL).append(" coins.").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHATNOTIFICATION).runeLiteFormattedMessage(chatMessage).build());
         }

         if (items.isEmpty()) {
            log.debug("No items to find for Event: {} | Container: {}", event, container);
         } else {
            this.addLoot(event, -1, LootRecordType.EVENT, metadata, items);
         }
      }
   }

   private boolean addPvpChestLoot() {
      boolean recordedLoot = false;
      Iterator var2 = PVP_LOOT_KEY_CONTAINERS.iterator();

      while(var2.hasNext()) {
         int containerId = (Integer)var2.next();
         Collection<ItemStack> items = toItemStacks(this.client.getItemContainer(containerId));
         if (!items.isEmpty()) {
            this.addLoot("Loot Chest", -1, LootRecordType.EVENT, (Object)null, items);
            recordedLoot = true;
         }
      }

      return recordedLoot;
   }

   private static Collection<ItemStack> toItemStacks(@Nullable ItemContainer container) {
      return (Collection)(container == null ? Collections.emptyList() : (Collection)Arrays.stream(container.getItems()).filter((item) -> {
         return item.getId() > -1;
      }).map((item) -> {
         return new ItemStack(item.getId(), item.getQuantity());
      }).collect(Collectors.toList()));
   }

   @Subscribe
   public void onScriptPreFired(ScriptPreFired event) {
      if (event.getScriptId() == 7928) {
         Collection<ItemStack> items = toItemStacks(this.client.getItemContainer(923));
         String title = this.client.getWidget(60227586).getChild(1).getText();
         int level = Integer.parseInt(title.split(" ")[1]);
         this.addLoot("Doom of Mokhaiotl", -1, LootRecordType.EVENT, level, items);
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      ChatMessageType chatType = event.getType();
      if (chatType == ChatMessageType.GAMEMESSAGE || chatType == ChatMessageType.SPAM || chatType == ChatMessageType.MESBOX) {
         String message = event.getMessage();
         if (!message.equals("You find some treasure in the chest!") && !message.equals("You steal some loot from the chest.") && !message.equals("You find treasure inside!") && !message.equals("You take some loot from inside.") && !GRUBBY_CHEST_LOOTED_MESSAGE.matcher(message).matches() && !message.startsWith("You open the chest and find") && !LARRAN_LOOTED_PATTERN.matcher(message).matches() && !ROGUES_CHEST_PATTERN.matcher(message).matches()) {
            Matcher zombiePirateLockerMatcher = ZOMBIE_PIRATE_LOCKER_PATTERN.matcher(message);
            if (zombiePirateLockerMatcher.matches()) {
               this.processZombiePirateLockerLoot(zombiePirateLockerMatcher);
            }

            Matcher shipwreckSalvagingMatcher = SALVAGE_PATTERN.matcher(message);
            if (shipwreckSalvagingMatcher.matches()) {
               String tier = shipwreckSalvagingMatcher.group("tier");
               String eventName = WordUtils.capitalizeFully(tier) + " salvage";
               this.onInvChange(this.collectInvItems(LootRecordType.EVENT, eventName));
            } else if (message.equals("You harvest herbs from the herbiboar, whereupon it escapes.")) {
               if (!this.processHerbiboarHerbSackLoot(event.getTimestamp())) {
                  this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, "Herbiboar", this.client.getBoostedSkillLevel(Skill.HERBLORE)));
               }
            } else {
               Player localPlayer = this.client.getLocalPlayer();
               if (localPlayer != null) {
                  int regionID = localPlayer.getWorldLocation().getRegionID();
                  Matcher hamStoreroomMatcher = HAM_CHEST_LOOTED_PATTERN.matcher(message);
                  if (hamStoreroomMatcher.matches() && regionID == 10321) {
                     String keyType = hamStoreroomMatcher.group("key");
                     this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, String.format("H.A.M. chest (%s)", keyType)));
                  } else {
                     Matcher pickpocketMatcher = PICKPOCKET_REGEX.matcher(message);
                     if (pickpocketMatcher.matches()) {
                        String pickpocketTarget = WordUtils.capitalize(pickpocketMatcher.group("target"));
                        if (PICKPOCKET_DISAMBIGUATION_MAP.get(this.lastPickpocketTarget).contains(pickpocketTarget)) {
                           pickpocketTarget = this.lastPickpocketTarget;
                        }

                        this.ignorePickpocketLoot = this.client.getTickCount();
                        this.onInvChange(this.collectInvAndGroundItems(LootRecordType.PICKPOCKET, pickpocketTarget));
                     } else {
                        Matcher m = CLUE_SCROLL_PATTERN.matcher(Text.removeTags(message));
                        if (m.find()) {
                           String eventType;
                           switch (m.group(1).toLowerCase()) {
                              case "beginner":
                                 eventType = "Clue Scroll (Beginner)";
                                 break;
                              case "easy":
                                 eventType = "Clue Scroll (Easy)";
                                 break;
                              case "medium":
                                 eventType = "Clue Scroll (Medium)";
                                 break;
                              case "hard":
                                 eventType = "Clue Scroll (Hard)";
                                 break;
                              case "elite":
                                 eventType = "Clue Scroll (Elite)";
                                 break;
                              case "master":
                                 eventType = "Clue Scroll (Master)";
                                 break;
                              default:
                                 log.debug("Unrecognized clue type: {}", type);
                                 return;
                           }

                           this.onInvChange(141, this.collectInvItems(LootRecordType.EVENT, eventType));
                        } else if (SHADE_CHEST_NO_KEY_PATTERN.matcher(message).matches()) {
                           this.resetEvent();
                        } else {
                           Matcher matcher = BIRDHOUSE_PATTERN.matcher(message);
                           if (matcher.matches()) {
                              int xp = Integer.parseInt(matcher.group(1));
                              String type = (String)BIRDHOUSE_XP_TO_TYPE.get(xp);
                              if (type == null) {
                                 log.debug("Unknown bird house type {}", xp);
                              } else {
                                 this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, type, this.client.getBoostedSkillLevel(Skill.HUNTER)));
                              }
                           } else if (regionID == 12588 && message.startsWith("You found some loot: ")) {
                              this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Reward pool (Tempoross)", this.client.getBoostedSkillLevel(Skill.FISHING)));
                           } else if (regionID == 14484 && message.startsWith("You found some loot: ")) {
                              this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Guardians of the Rift", this.client.getBoostedSkillLevel(Skill.RUNECRAFT)));
                           } else if (regionID == 6461 && message.contains("You found some loot: ")) {
                              this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Reward cart (Wintertodt)", this.client.getBoostedSkillLevel(Skill.FIREMAKING)));
                           } else {
                              if (regionID == 12106 && message.equals("You place the Unsired into the Font of Consumption...")) {
                                 this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Unsired"));
                              }

                              if (regionID == 10039 && chatType == ChatMessageType.MESBOX && message.contains("High level gamble count:")) {
                                 this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, "Barbarian Assault high gamble"));
                              }

                              if (message.startsWith("You rummage through the offerings")) {
                                 this.countChangedItems(31054, this.client.getBoostedSkillLevel(Skill.FLETCHING));
                              } else if (message.equals("You clean a batch of arrowtips.")) {
                                 this.countChangedItems(31047, this.client.getBoostedSkillLevel(Skill.FLETCHING));
                              }

                           }
                        }
                     }
                  }
               }
            }
         } else {
            Player localPlayer = this.client.getLocalPlayer();
            if (localPlayer != null) {
               int regionID = localPlayer.getWorldLocation().getRegionID();
               log.debug("Chest loot matched '{}' region {}", message, regionID);
               if (CHEST_EVENT_TYPES.containsKey(regionID)) {
                  if (regionID == 12348) {
                     this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, (String)CHEST_EVENT_TYPES.get(regionID), this.client.getWorld()));
                  } else if (SLAYER_CHEST_EVENT_TYPES.contains(CHEST_EVENT_TYPES.get(regionID))) {
                     this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, (String)CHEST_EVENT_TYPES.get(regionID), this.client.getBoostedSkillLevel(Skill.FISHING)));
                  } else {
                     this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, (String)CHEST_EVENT_TYPES.get(regionID)));
                  }
               }
            }
         }
      }
   }

   private void countChangedItems(int itemId, Object metadata) {
      this.onInvChange((invItems, groundItems, removedItems) -> {
         int cnt = removedItems.count(itemId);
         if (cnt > 0) {
            String name = this.itemManager.getItemComposition(itemId).getMembersName();
            List<ItemStack> combined = new ArrayList();
            combined.addAll(invItems);
            combined.addAll(groundItems);
            this.addLoot(name, -1, LootRecordType.EVENT, metadata, combined, cnt);
         }

      });
   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      ItemContainer inventoryContainer;
      if (this.pvpKeysLooted && event.getContainerId() == 93) {
         inventoryContainer = this.client.getItemContainer(93);
         this.pvpKeysLooted = PVP_LOOT_KEYS.stream().anyMatch((lootKeyId) -> {
            return inventoryContainer.contains(lootKeyId);
         });
      }

      if (this.inventoryId != -1 && event.getContainerId() == this.inventoryId) {
         inventoryContainer = event.getItemContainer();
         Multiset<Integer> currentInventory = HashMultiset.create();
         Arrays.stream(inventoryContainer.getItems()).forEach((item) -> {
            currentInventory.add(item.getId(), item.getQuantity());
         });
         Player localPlayer = this.client.getLocalPlayer();
         if (localPlayer != null) {
            WorldPoint playerLocation = localPlayer.getWorldLocation();
            Collection<ItemStack> groundItems = this.lootManager.getItemSpawns(playerLocation);
            Multiset<Integer> diff = Multisets.difference(currentInventory, this.inventorySnapshot);
            Multiset<Integer> diffr = Multisets.difference(this.inventorySnapshot, currentInventory);
            List<ItemStack> items = (List)diff.entrySet().stream().map((e) -> {
               return new ItemStack((Integer)e.getElement(), e.getCount());
            }).collect(Collectors.toList());
            log.debug("Inv change: {} Ground items: {}", items, groundItems);
            if (this.inventorySnapshotCb != null) {
               this.inventorySnapshotCb.accept(items, groundItems, diffr);
            }

            this.resetEvent();
         }
      }
   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked event) {
      if (isNPCOp(event.getMenuAction())) {
         if (event.getMenuOption().equals("Pickpocket")) {
            this.lastPickpocketTarget = Text.removeTags(event.getMenuTarget());
         }
      } else if (isObjectOp(event.getMenuAction()) && event.getMenuOption().equals("Open") && SHADE_CHEST_OBJECTS.containsKey(event.getId())) {
         this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, (String)SHADE_CHEST_OBJECTS.get(event.getId())));
      } else if (event.isItemOp()) {
         if (event.getItemId() == 22993 && (event.getMenuOption().equals("Take") || event.getMenuOption().equals("Take-all"))) {
            this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Seed pack"));
         } else if (event.getMenuOption().equals("Search") && BIRDNEST_IDS.contains(event.getItemId())) {
            this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Bird nest"));
         } else if (event.getMenuOption().equals("Open")) {
            switch (event.getItemId()) {
               case 405:
                  this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Casket"));
                  break;
               case 19473:
                  this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, "Bag full of gems (Percy)"));
                  break;
               case 20703:
               case 20791:
                  this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, "Supply crate (Wintertodt)"));
                  break;
               case 24853:
                  this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, "Bag full of gems (Belona)"));
                  break;
               case 24884:
                  this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Supply crate (Mahogany Homes)", this.client.getBoostedSkillLevel(Skill.CONSTRUCTION)));
                  break;
               case 24946:
                  this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, "Hallowed Sack"));
                  break;
               case 25342:
                  this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Spoils of war"));
                  break;
               case 25537:
                  this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, "Bag full of gems (Dusuri)"));
                  break;
               case 25590:
                  this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, "Casket (Tempoross)"));
                  break;
               case 25647:
               case 25649:
               case 25651:
               case 26908:
               case 27293:
               case 27622:
               case 28082:
               case 28084:
               case 28086:
               case 28088:
               case 28090:
               case 28092:
               case 28094:
               case 28096:
               case 28098:
               case 29971:
               case 29972:
               case 29973:
               case 30690:
               case 30763:
               case 30803:
                  this.onInvChange(this.collectInvAndGroundItems(LootRecordType.EVENT, this.itemManager.getItemComposition(event.getItemId()).getName()));
                  break;
               case 27693:
                  this.onInvChange(this.collectInvItems(LootRecordType.EVENT, "Ore Pack (Volcanic Mine)"));
                  break;
               case 29242:
               case 29244:
               case 29246:
               case 29248:
               case 29250:
                  int itemId = event.getItemId();
                  Map<String, Integer> levels = (new ImmutableMap.Builder()).put("WOODCUTTING", this.client.getBoostedSkillLevel(Skill.WOODCUTTING)).put("HERBLORE", this.client.getBoostedSkillLevel(Skill.HERBLORE)).put("HUNTER", this.client.getBoostedSkillLevel(Skill.HUNTER)).build();
                  this.onInvChange((invItems, groundItems, removedItems) -> {
                     int cnt = removedItems.count(itemId);
                     if (cnt > 0) {
                        String name = this.itemManager.getItemComposition(itemId).getMembersName();
                        this.addLoot(name, -1, LootRecordType.EVENT, levels, invItems, cnt);
                     }

                  });
            }
         }
      }

   }

   private static boolean isNPCOp(MenuAction menuAction) {
      int id = menuAction.getId();
      return id >= MenuAction.NPC_FIRST_OPTION.getId() && id <= MenuAction.NPC_FIFTH_OPTION.getId();
   }

   private static boolean isObjectOp(MenuAction menuAction) {
      int id = menuAction.getId();
      return id >= MenuAction.GAME_OBJECT_FIRST_OPTION.getId() && id <= MenuAction.GAME_OBJECT_FOURTH_OPTION.getId() || id == MenuAction.GAME_OBJECT_FIFTH_OPTION.getId();
   }

   @Nullable
   private CompletableFuture<Void> submitLoot() {
      ArrayList copy;
      synchronized(this.queuedLoots) {
         if (this.queuedLoots.isEmpty()) {
            return null;
         }

         copy = new ArrayList(this.queuedLoots);
         this.queuedLoots.clear();
      }

      this.saveLoot(copy);
      if (this.client.getEnvironment() != 0) {
         return null;
      } else {
         log.debug("Submitting {} loot records", copy.size());
         return this.lootTrackerClient.submit(copy);
      }
   }

   private Collection<ConfigLoot> combine(List<LootRecord> records) {
      Map<ConfigLoot, ConfigLoot> map = new HashMap();
      Iterator var3 = records.iterator();

      while(var3.hasNext()) {
         LootRecord record = (LootRecord)var3.next();
         ConfigLoot key = new ConfigLoot(record.getType(), record.getEventId());
         ConfigLoot loot = (ConfigLoot)map.computeIfAbsent(key, (k) -> {
            return key;
         });
         loot.kills += record.getAmount();
         Iterator var7 = record.getDrops().iterator();

         while(var7.hasNext()) {
            GameItem item = (GameItem)var7.next();
            loot.add(item.getId(), item.getQty());
         }
      }

      return map.values();
   }

   private void saveLoot(List<LootRecord> records) {
      Instant now = Instant.now();
      Collection<ConfigLoot> combinedRecords = this.combine(records);
      Iterator var4 = combinedRecords.iterator();

      while(var4.hasNext()) {
         ConfigLoot record = (ConfigLoot)var4.next();
         ConfigLoot lootConfig = this.getLootConfig(record.type, record.name);
         if (lootConfig == null) {
            lootConfig = record;
         } else {
            lootConfig.kills += record.kills;

            for(int i = 0; i < record.drops.length; i += 2) {
               lootConfig.add(record.drops[i], record.drops[i + 1]);
            }
         }

         lootConfig.last = now;
         this.setLootConfig(lootConfig.type, lootConfig.name, lootConfig);
      }

   }

   private void resetEvent() {
      this.inventoryId = -1;
      this.inventorySnapshot = null;
      this.inventorySnapshotCb = null;
      this.inventoryTimeout = 0;
   }

   private InvChangeCallback collectInvItems(LootRecordType type, String event) {
      return this.collectInvItems(type, event, (Object)null);
   }

   private InvChangeCallback collectInvItems(LootRecordType type, String event, Object metadata) {
      return (invItems, groundItems, removedItems) -> {
         this.addLoot(event, -1, type, metadata, invItems);
      };
   }

   private InvChangeCallback collectInvAndGroundItems(LootRecordType type, String event) {
      return this.collectInvAndGroundItems(type, event, (Object)null);
   }

   private InvChangeCallback collectInvAndGroundItems(LootRecordType type, String event, Object metadata) {
      return (invItems, groundItems, removedItems) -> {
         List<ItemStack> combined = new ArrayList();
         combined.addAll(invItems);
         combined.addAll(groundItems);
         this.addLoot(event, -1, type, metadata, combined);
      };
   }

   private void onInvChange(InvChangeCallback cb) {
      this.onInvChange(93, cb);
   }

   private void onInvChange(int inv, InvChangeCallback cb) {
      this.onInvChange(inv, cb, 10);
   }

   private void onInvChange(int inv, InvChangeCallback cb, int timeout) {
      this.inventoryId = inv;
      this.inventorySnapshot = HashMultiset.create();
      this.inventorySnapshotCb = cb;
      this.inventoryTimeout = timeout * 600 / 20;
      ItemContainer itemContainer = this.client.getItemContainer(inv);
      if (itemContainer != null) {
         Arrays.stream(itemContainer.getItems()).forEach((item) -> {
            this.inventorySnapshot.add(item.getId(), item.getQuantity());
         });
      }

   }

   private boolean processHerbiboarHerbSackLoot(int timestamp) {
      List<ItemStack> herbs = new ArrayList();
      Iterator var3 = this.client.getMessages().iterator();

      while(var3.hasNext()) {
         MessageNode messageNode = (MessageNode)var3.next();
         if (messageNode.getTimestamp() == timestamp && messageNode.getType() == ChatMessageType.SPAM) {
            Matcher matcher = HERBIBOAR_HERB_SACK_PATTERN.matcher(messageNode.getValue());
            if (matcher.matches()) {
               herbs.add(new ItemStack(((ItemPrice)this.itemManager.search(matcher.group(1)).get(0)).getId(), 1));
            }
         }
      }

      if (herbs.isEmpty()) {
         return false;
      } else {
         int herbloreLevel = this.client.getBoostedSkillLevel(Skill.HERBLORE);
         this.addLoot("Herbiboar", -1, LootRecordType.EVENT, herbloreLevel, herbs);
         return true;
      }
   }

   private void processZombiePirateLockerLoot(Matcher matcher) {
      int quantity = Integer.parseInt(matcher.group("qty").replaceAll(",", ""));
      String itemName = matcher.group("item");
      int itemId = "Coins".equals(itemName) ? 995 : ((ItemPrice)this.itemManager.search(itemName).get(0)).getId();
      this.addLoot("Zombie Pirate's Locker", -1, LootRecordType.EVENT, (Object)null, List.of(new ItemStack(itemId, quantity)));
   }

   @VisibleForTesting
   boolean inTobChestRegion() {
      Player localPlayer = this.client.getLocalPlayer();
      if (localPlayer == null) {
         return false;
      } else {
         int region = WorldPoint.fromLocalInstance(this.client, localPlayer.getLocalLocation()).getRegionID();
         return region == 12867 || region == 14642;
      }
   }

   void toggleItem(String name, boolean ignore) {
      Set<String> ignoredItemSet = new LinkedHashSet(this.ignoredItems);
      if (ignore) {
         ignoredItemSet.add(name);
      } else {
         ignoredItemSet.remove(name);
      }

      this.config.setIgnoredItems(Text.toCSV(ignoredItemSet));
   }

   boolean isIgnored(String name) {
      return this.ignoredItems.contains(name);
   }

   void toggleEvent(String name, boolean ignore) {
      Set<String> ignoredSet = new LinkedHashSet(this.ignoredEvents);
      if (ignore) {
         ignoredSet.add(name);
      } else {
         ignoredSet.remove(name);
      }

      this.config.setIgnoredEvents(Text.toCSV(ignoredSet));
   }

   boolean isEventIgnored(String name) {
      return this.ignoredEvents.contains(name);
   }

   private LootTrackerItem buildLootTrackerItem(int itemId, int quantity) {
      ItemComposition itemComposition = this.itemManager.getItemComposition(itemId);
      int gePrice = this.itemManager.getItemPrice(itemId);
      int haPrice = itemComposition.getHaPrice();
      boolean ignored = this.ignoredItems.contains(itemComposition.getMembersName());
      return new LootTrackerItem(itemId, itemComposition.getMembersName(), quantity, gePrice, haPrice, ignored);
   }

   private LootTrackerItem[] buildEntries(Collection<ItemStack> itemStacks) {
      return (LootTrackerItem[])itemStacks.stream().map((itemStack) -> {
         return this.buildLootTrackerItem(itemStack.getId(), itemStack.getQuantity());
      }).toArray((x$0) -> {
         return new LootTrackerItem[x$0];
      });
   }

   private static Collection<GameItem> toGameItems(Collection<ItemStack> items) {
      return (Collection)items.stream().map((item) -> {
         return new GameItem(item.getId(), item.getQuantity());
      }).collect(Collectors.toList());
   }

   private LootTrackerRecord convertToLootTrackerRecord(ConfigLoot configLoot) {
      LootTrackerItem[] items = new LootTrackerItem[configLoot.drops.length / 2];

      for(int i = 0; i < configLoot.drops.length; i += 2) {
         int id = configLoot.drops[i];
         int qty = configLoot.drops[i + 1];
         items[i >> 1] = this.buildLootTrackerItem(id, qty);
      }

      return new LootTrackerRecord(configLoot.name, "", configLoot.type, items, configLoot.kills);
   }

   private boolean isPlayerWithinMapRegion(Set<Integer> definedMapRegions) {
      int[] mapRegions = this.client.getMapRegions();
      int[] var3 = mapRegions;
      int var4 = mapRegions.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int region = var3[var5];
         if (definedMapRegions.contains(region)) {
            return true;
         }
      }

      return false;
   }

   private void lootReceivedChatMessage(Collection<ItemStack> items, String name) {
      long totalPrice = items.stream().mapToLong((item) -> {
         return this.config.priceType() == LootTrackerPriceType.GRAND_EXCHANGE ? (long)this.itemManager.getItemPrice(item.getId()) * (long)item.getQuantity() : (long)this.itemManager.getItemComposition(item.getId()).getHaPrice() * (long)item.getQuantity();
      }).sum();
      String message = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append("You've killed ").append(name).append(" for ").append(QuantityFormatter.quantityToStackSize(totalPrice)).append(" loot.").build();
      this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
   }

   ConfigLoot getLootConfig(LootRecordType type, String name) {
      String profile = this.profileKey;
      if (Strings.isNullOrEmpty(profile)) {
         log.debug("Trying to get loot with no profile!");
         return null;
      } else {
         ConfigManager var10000 = this.configManager;
         String var10003 = String.valueOf(type);
         String json = var10000.getConfiguration("loottracker", profile, "drops_" + var10003 + "_" + name);
         return json == null ? null : (ConfigLoot)this.gson.fromJson(json, ConfigLoot.class);
      }
   }

   void setLootConfig(LootRecordType type, String name, ConfigLoot loot) {
      String profile = this.profileKey;
      if (Strings.isNullOrEmpty(profile)) {
         log.debug("Trying to set loot with no profile!");
      } else {
         String json = this.gson.toJson(loot);
         this.configManager.setConfiguration("loottracker", profile, "drops_" + String.valueOf(type) + "_" + name, json);
      }
   }

   void removeLootConfig(LootRecordType type, String name) {
      String profile = this.profileKey;
      if (Strings.isNullOrEmpty(profile)) {
         log.debug("Trying to remove loot with no profile!");
      } else {
         ConfigManager var10000 = this.configManager;
         String var10003 = String.valueOf(type);
         var10000.unsetConfiguration("loottracker", profile, "drops_" + var10003 + "_" + name);
      }
   }

   void removeAllLoot() {
      String profile = this.profileKey;
      if (Strings.isNullOrEmpty(profile)) {
         log.debug("Trying to clear loot with no profile!");
      } else {
         Iterator var2 = this.configManager.getRSProfileConfigurationKeys("loottracker", profile, "drops_").iterator();

         while(var2.hasNext()) {
            String key = (String)var2.next();
            this.configManager.unsetConfiguration("loottracker", profile, key);
         }

      }
   }

   LootTrackerClient getLootTrackerClient() {
      return this.lootTrackerClient;
   }

   @FunctionalInterface
   interface InvChangeCallback {
      void accept(Collection<ItemStack> var1, Collection<ItemStack> var2, Multiset<Integer> var3);
   }
}
