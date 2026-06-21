package net.runelite.client.plugins.menuentryswapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EntityOps;
import net.runelite.api.ItemComposition;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.ObjectComposition;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetConfigNode;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.NpcUtil;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Menu Entry Swapper",
   description = "Change the default option that is displayed when hovering over objects",
   tags = {"npcs", "inventory", "items", "objects"},
   enabledByDefault = true
)
public class MenuEntrySwapperPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(MenuEntrySwapperPlugin.class);
   private static final String SHIFTCLICK_CONFIG_GROUP = "shiftclick";
   private static final String ITEM_KEY_PREFIX = "item_";
   private static final String OBJECT_KEY_PREFIX = "object_";
   private static final String OBJECT_SHIFT_KEY_PREFIX = "object_shift_";
   private static final String NPC_KEY_PREFIX = "npc_";
   private static final String NPC_SHIFT_KEY_PREFIX = "npc_shift_";
   private static final String WORN_ITEM_KEY_PREFIX = "wornitem_";
   private static final String WORN_ITEM_SHIFT_KEY_PREFIX = "wornitem_shift_";
   private static final String UI_KEY_PREFIX = "ui_";
   private static final String UI_SHIFT_KEY_PREFIX = "ui_shift_";
   private static final List<MenuAction> NPC_MENU_TYPES;
   private static final List<MenuAction> OBJECT_MENU_TYPES;
   private static final Set<String> ESSENCE_MINE_NPCS;
   private static final Set<String> TEMPOROSS_NPCS;
   private static final int[][] EQUIPMENT_SUBOP_PARAMS;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private MenuEntrySwapperConfig config;
   @Inject
   private ConfigManager configManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private ChatMessageManager chatMessageManager;
   @Inject
   private NpcUtil npcUtil;
   private final Multimap<String, Swap> swaps = LinkedHashMultimap.create();
   private final ArrayListMultimap<String, Integer> cacheOptionIndexes = ArrayListMultimap.create();
   private Menu cacheOptionMenu;
   private boolean lastShift;
   private boolean curShift;

   @Provides
   MenuEntrySwapperConfig provideConfig(ConfigManager configManager) {
      return (MenuEntrySwapperConfig)configManager.getConfig(MenuEntrySwapperConfig.class);
   }

   public void startUp() {
      this.setupSwaps();
      this.removeOldSwaps();
   }

   public void shutDown() {
      this.swaps.clear();
   }

   @VisibleForTesting
   void setupSwaps() {
      MenuEntrySwapperConfig var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("talk-to", "mage of zamorak", "teleport", var10004::swapAbyssTeleport);
      MenuEntrySwapperConfig var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "bank", var10003::swapBank);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "exchange", var10003::swapExchange);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "help", var10003::swapHelp);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "assignment", var10003::swapAssignment);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "pay", var10003::swapPay);
      Predicate var10002 = Predicates.alwaysTrue();
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swapContains("talk-to", var10002, "pay (", var10004::swapPay);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "trade", var10003::swapTrade);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "trade-with", var10003::swapTrade);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "shop", var10003::swapTrade);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "travel", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "pay-fare", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "charter", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "take-boat", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "fly", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "jatizso", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "neitiznot", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "rellekka", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "ungael", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "pirate's cove", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "waterbirth island", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "island of stone", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "miscellania", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "follow", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "transport", var10003::swapTravel);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "quick-travel", var10003::swapQuick);
      Set var5 = ESSENCE_MINE_NPCS;
      Objects.requireNonNull(var5);
      java.util.function.Predicate var6 = var5::contains;
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("talk-to", var6, "teleport", var10004::swapEssenceMineTeleport);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("talk-to", "deposit-items", var10003::swapDepositItems);
      var5 = TEMPOROSS_NPCS;
      Objects.requireNonNull(var5);
      var6 = var5::contains;
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("talk-to", var6, "leave", var10004::swapTemporossLeave);
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("pass", "energy barrier", "pay-toll(2-ecto)", var10004::swapTravel);
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("open", "gate", "pay-toll(10gp)", var10004::swapTravel);
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("inspect", "trapdoor", "travel", var10004::swapTravel);
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("board", "travel cart", "pay-fare", var10004::swapTravel);
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("board", "sacrificial boat", "quick-board", var10004::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("cage", "harpoon", var10003::swapHarpoon);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("big net", "harpoon", var10003::swapHarpoon);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("net", "harpoon", var10003::swapHarpoon);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("lure", "bait", var10003::swapBait);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("net", "bait", var10003::swapBait);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("small net", "bait", var10003::swapBait);
      this.swap("enter", "portal", "home", () -> {
         return this.config.swapHomePortal() == HouseMode.HOME;
      });
      this.swap("enter", "portal", "build mode", () -> {
         return this.config.swapHomePortal() == HouseMode.BUILD_MODE;
      });
      this.swap("enter", "portal", "friend's house", () -> {
         return this.config.swapHomePortal() == HouseMode.FRIENDS_HOUSE;
      });
      String[] var1 = new String[]{"zanaris", "tree"};
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String option = var1[var3];
         this.swapContains(option, Predicates.alwaysTrue(), "last-destination", () -> {
            return this.config.swapFairyRing() == FairyRingMode.LAST_DESTINATION;
         });
         this.swapContains(option, Predicates.alwaysTrue(), "configure", () -> {
            return this.config.swapFairyRing() == FairyRingMode.CONFIGURE;
         });
      }

      this.swapContains("configure", Predicates.alwaysTrue(), "last-destination", () -> {
         return this.config.swapFairyRing() == FairyRingMode.LAST_DESTINATION || this.config.swapFairyRing() == FairyRingMode.ZANARIS;
      });
      this.swapContains("tree", Predicates.alwaysTrue(), "zanaris", () -> {
         return this.config.swapFairyRing() == FairyRingMode.ZANARIS;
      });
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("check", "reset", var10003::swapBoxTrap);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("dismantle", "reset", var10003::swapBoxTrap);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("take", "lay", var10003::swapBoxTrap);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("pick-up", "chase", var10003::swapChase);
      var6 = (target) -> {
         return target.endsWith("birdhouse");
      };
      var10004 = this.config;
      Objects.requireNonNull(var10004);
      this.swap("interact", var6, "empty", var10004::swapBirdhouseEmpty);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("enter", "quick-enter", var10003::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("enter-crypt", "quick-enter", var10003::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("ring", "quick-start", var10003::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("pass", "quick-pass", var10003::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("pass", "quick pass", var10003::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("open", "quick-open", var10003::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("climb-down", "quick-start", var10003::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("climb-down", "pay", var10003::swapQuick);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("admire", "teleport", var10003::swapAdmire);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("admire", "spellbook", var10003::swapAdmire);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("admire", "perks", var10003::swapAdmire);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "emir's arena", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "castle wars", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "ferox enclave", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "fortis colosseum", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "burthorpe", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "barbarian outpost", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "corporeal beast", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "tears of guthix", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "wintertodt camp", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "warriors' guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "champions' guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "monastery", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "ranging guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "fishing guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "mining guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "crafting guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "cooking guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "woodcutting guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "farming guild", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "miscellania", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "grand exchange", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "falador park", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "dondakan's rock", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "edgeville", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "karamja", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "draynor village", var10003::swapJewelleryBox);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("teleport menu", "al kharid", var10003::swapJewelleryBox);
      Arrays.asList("annakarl", "ape atoll dungeon", "ardougne", "barrows", "battlefront", "camelot", "carrallanger", "catherby", "cemetery", "draynor manor", "falador", "fenkenstrain's castle", "fishing guild", "ghorrock", "grand exchange", "great kourend", "harmony island", "kharyrll", "lumbridge", "arceuus library", "lunar isle", "marim", "mind altar", "salve graveyard", "seers' village", "senntisten", "troll stronghold", "varrock", "watchtower", "waterbirth island", "weiss", "west ardougne", "yanille").forEach((location) -> {
         MenuEntrySwapperConfig var10004 = this.config;
         Objects.requireNonNull(var10004);
         this.swap(location, "portal nexus", "teleport menu", var10004::swapPortalNexus);
      });
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("shared", "private", var10003::swapPrivate);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("pick", "pick-lots", var10003::swapPick);
      this.swap("view offer", "abort offer", () -> {
         return this.shiftModifier() && this.config.swapGEAbort();
      });
      this.swap("value", "buy 1", () -> {
         return this.shiftModifier() && this.config.shopBuy() == BuyMode.BUY_1;
      });
      this.swap("value", "buy 5", () -> {
         return this.shiftModifier() && this.config.shopBuy() == BuyMode.BUY_5;
      });
      this.swap("value", "buy 10", () -> {
         return this.shiftModifier() && this.config.shopBuy() == BuyMode.BUY_10;
      });
      this.swap("value", "buy 50", () -> {
         return this.shiftModifier() && this.config.shopBuy() == BuyMode.BUY_50;
      });
      this.swap("value", "sell 1", () -> {
         return this.shiftModifier() && this.config.shopSell() == SellMode.SELL_1;
      });
      this.swap("value", "sell 5", () -> {
         return this.shiftModifier() && this.config.shopSell() == SellMode.SELL_5;
      });
      this.swap("value", "sell 10", () -> {
         return this.shiftModifier() && this.config.shopSell() == SellMode.SELL_10;
      });
      this.swap("value", "sell 50", () -> {
         return this.shiftModifier() && this.config.shopSell() == SellMode.SELL_50;
      });
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("wear", "tele to poh", var10003::swapTeleToPoh);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("wear", "rub", var10003::swapTeleportItem);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("wear", "teleport", var10003::swapTeleportItem);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("wield", "teleport", var10003::swapTeleportItem);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("wield", "invoke", var10003::swapTeleportItem);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("wear", "teleports", var10003::swapTeleportItem);
      this.swap("wear", "farm teleport", () -> {
         return this.config.swapArdougneCloakMode() == MenuEntrySwapperConfig.ArdougneCloakMode.FARM;
      });
      this.swap("wear", "monastery teleport", () -> {
         return this.config.swapArdougneCloakMode() == MenuEntrySwapperConfig.ArdougneCloakMode.MONASTERY;
      });
      this.swap("wear", "gem mine", () -> {
         return this.config.swapKaramjaGlovesMode() == MenuEntrySwapperConfig.KaramjaGlovesMode.GEM_MINE;
      });
      this.swap("wear", "slayer master", () -> {
         return this.config.swapKaramjaGlovesMode() == MenuEntrySwapperConfig.KaramjaGlovesMode.SLAYER_MASTER;
      });
      this.swap("equip", "kourend woodland", () -> {
         return this.config.swapRadasBlessingMode() == MenuEntrySwapperConfig.RadasBlessingMode.KOUREND_WOODLAND;
      });
      this.swap("equip", "mount karuulm", () -> {
         return this.config.swapRadasBlessingMode() == MenuEntrySwapperConfig.RadasBlessingMode.MOUNT_KARUULM;
      });
      this.swap("wear", "ecto teleport", () -> {
         return this.config.swapMorytaniaLegsMode() == MenuEntrySwapperConfig.MorytaniaLegsMode.ECTOFUNTUS;
      });
      this.swap("wear", "burgh teleport", () -> {
         return this.config.swapMorytaniaLegsMode() == MenuEntrySwapperConfig.MorytaniaLegsMode.BURGH_DE_ROTT;
      });
      this.swap("wear", "nardah", () -> {
         return this.config.swapDesertAmuletMode() == MenuEntrySwapperConfig.DesertAmuletMode.NARDAH;
      });
      this.swap("wear", "kalphite cave", () -> {
         return this.config.swapDesertAmuletMode() == MenuEntrySwapperConfig.DesertAmuletMode.KALPHITE_CAVE;
      });
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("bury", "use", var10003::swapBones);
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("clean", "use", var10003::swapHerbs);
      this.swap("collect-note", "collect-item", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.ITEMS;
      });
      this.swap("collect-notes", "collect-items", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.ITEMS;
      });
      this.swap("collect-item", "collect-note", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.NOTES;
      });
      this.swap("collect-items", "collect-notes", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.NOTES;
      });
      this.swap("collect to inventory", "collect to bank", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.BANK;
      });
      this.swap("collect", "bank", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.BANK;
      });
      this.swap("collect-note", "bank", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.BANK;
      });
      this.swap("collect-notes", "bank", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.BANK;
      });
      this.swap("collect-item", "bank", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.BANK;
      });
      this.swap("collect-items", "bank", () -> {
         return this.config.swapGEItemCollect() == GEItemCollectMode.BANK;
      });
      var10003 = this.config;
      Objects.requireNonNull(var10003);
      this.swap("tan 1", "tan all", var10003::swapTan);
      this.swap("climb", "climb-up", () -> {
         return (this.shiftModifier() ? this.config.swapStairsShiftClick() : this.config.swapStairsLeftClick()) == MenuEntrySwapperConfig.StairsMode.CLIMB_UP;
      });
      this.swap("climb", "climb-down", () -> {
         return (this.shiftModifier() ? this.config.swapStairsShiftClick() : this.config.swapStairsLeftClick()) == MenuEntrySwapperConfig.StairsMode.CLIMB_DOWN;
      });
   }

   private void removeOldSwaps() {
      String[] keys = new String[]{"swapBattlestaves", "swapPrayerBook", "swapContract", "claimSlime", "swapDarkMage", "swapCaptainKhaled", "swapDecant", "swapHardWoodGrove", "swapHardWoodGroveParcel", "swapHouseAdvertisement", "swapEnchant", "swapHouseTeleportSpell", "swapTeleportSpell", "swapStartMinigame", "swapQuickleave", "swapNpcContact", "swapNets", "swapGauntlet", "swapCollectMiscellania", "swapRockCake", "swapRowboatDive"};
      String[] var2 = keys;
      int var3 = keys.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String key = var2[var4];
         this.configManager.unsetConfiguration("menuentryswapper", key);
      }

   }

   private void swap(String option, String swappedOption, Supplier<Boolean> enabled) {
      this.swap(option, (java.util.function.Predicate)Predicates.alwaysTrue(), swappedOption, enabled);
   }

   private void swap(String option, String target, String swappedOption, Supplier<Boolean> enabled) {
      this.swap(option, (java.util.function.Predicate)Predicates.equalTo(target), swappedOption, enabled);
   }

   private void swap(String option, java.util.function.Predicate<String> targetPredicate, String swappedOption, Supplier<Boolean> enabled) {
      this.swaps.put(option, new Swap(Predicates.alwaysTrue(), targetPredicate, swappedOption, enabled, true));
   }

   private void swapContains(String option, java.util.function.Predicate<String> targetPredicate, String swappedOption, Supplier<Boolean> enabled) {
      this.swaps.put(option, new Swap(Predicates.alwaysTrue(), targetPredicate, swappedOption, enabled, false));
   }

   private Integer getItemSwapConfig(boolean shift, int itemId) {
      itemId = ItemVariationMapping.map(itemId);
      String config = this.configManager.getConfiguration(shift ? "shiftclick" : "menuentryswapper", "item_" + itemId);
      return config != null && !config.isEmpty() ? Integer.parseInt(config) : null;
   }

   private void setItemSwapConfig(boolean shift, int itemId, int index) {
      itemId = ItemVariationMapping.map(itemId);
      this.configManager.setConfiguration(shift ? "shiftclick" : "menuentryswapper", "item_" + itemId, (Object)index);
   }

   private void unsetItemSwapConfig(boolean shift, int itemId) {
      itemId = ItemVariationMapping.map(itemId);
      this.configManager.unsetConfiguration(shift ? "shiftclick" : "menuentryswapper", "item_" + itemId);
   }

   private Integer getWornItemSwapConfig(boolean shift, int itemId) {
      itemId = ItemVariationMapping.map(itemId);
      String config = this.configManager.getConfiguration("menuentryswapper", (shift ? "wornitem_shift_" : "wornitem_") + itemId);
      return config != null && !config.isEmpty() ? Integer.parseInt(config) : null;
   }

   private void setWornItemSwapConfig(boolean shift, int itemId, int index) {
      itemId = ItemVariationMapping.map(itemId);
      this.configManager.setConfiguration("menuentryswapper", (shift ? "wornitem_shift_" : "wornitem_") + itemId, (Object)index);
   }

   private void unsetWornItemSwapConfig(boolean shift, int itemId) {
      itemId = ItemVariationMapping.map(itemId);
      this.configManager.unsetConfiguration("menuentryswapper", (shift ? "wornitem_shift_" : "wornitem_") + itemId);
   }

   @Subscribe
   public void onMenuOpened(MenuOpened event) {
      this.configureObjectClick(event);
      this.configureNpcClick(event);
      this.configureWornItems(event);
      this.configureItems(event);
      this.configureUiSwap(event);
   }

   private int packSubID(int opIdx, int subID) {
      return subID + 1 << 8 | opIdx;
   }

   private void configureObjectClick(MenuOpened event) {
      if (this.shiftModifier() && this.config.objectCustomization()) {
         MenuEntry[] entries = event.getMenuEntries();

         for(int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            if (entry.getType() == MenuAction.EXAMINE_OBJECT) {
               ObjectComposition composition = this.client.getObjectDefinition(entry.getIdentifier());
               EntityOps ops = composition.getOps();
               Integer swapConfig = this.getObjectSwapConfig(false, composition.getId());
               Integer shiftSwapConfig = this.getObjectSwapConfig(true, composition.getId());
               MenuEntry swapLeftClick = this.client.createMenuEntry(idx).setOption("Swap left-click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
               MenuEntry swapShiftClick = this.client.createMenuEntry(idx).setOption("Swap shift-click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
               Menu subLeft = swapLeftClick.createSubMenu();
               Menu subShift = swapShiftClick.createSubMenu();

               for(int opIdx = 0; opIdx < 5; ++opIdx) {
                  String op = ops.getOp(opIdx);
                  if (!Strings.isNullOrEmpty(op)) {
                     subLeft.createMenuEntry(0).setOption(op).setType(MenuAction.RUNELITE).onClick(this.objectConsumer(composition, op, opIdx, false));
                     subShift.createMenuEntry(0).setOption(op).setType(MenuAction.RUNELITE).onClick(this.objectConsumer(composition, op, opIdx, true));
                     int numSubOps = ops.getNumSubOps(opIdx);

                     for(int subIdx = 0; subIdx < numSubOps; ++subIdx) {
                        String subOp = ops.getSubOp(opIdx, subIdx);
                        if (subOp != null) {
                           int subID = ops.getSubID(opIdx, subIdx);
                           subLeft.createMenuEntry(0).setOption(subOp).setType(MenuAction.RUNELITE).onClick(this.objectConsumer(composition, subOp, this.packSubID(opIdx, subID), false));
                           subShift.createMenuEntry(0).setOption(subOp).setType(MenuAction.RUNELITE).onClick(this.objectConsumer(composition, subOp, this.packSubID(opIdx, subID), true));
                        }
                     }
                  }
               }

               subLeft.createMenuEntry(0).setOption("Walk here").setType(MenuAction.RUNELITE).onClick(this.walkHereConsumer(false, composition));
               subShift.createMenuEntry(0).setOption("Walk here").setType(MenuAction.RUNELITE).onClick(this.walkHereConsumer(true, composition));
               if (swapConfig != null) {
                  subLeft.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick(this.objectResetConsumer(composition, false));
               }

               if (shiftSwapConfig != null) {
                  subShift.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick(this.objectResetConsumer(composition, true));
               }
            }
         }

      }
   }

   private Consumer<MenuEntry> objectConsumer(ObjectComposition composition, String text, int menuIdx, boolean shift) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been set to '").append(text).append("'.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Set object swap for {} to {}", composition.getId(), menuIdx);
         this.setObjectSwapConfig(shift, composition.getId(), menuIdx);
      };
   }

   private Consumer<MenuEntry> objectResetConsumer(ObjectComposition composition, boolean shift) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been reset.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Unset object {} swap for {}", shift ? "shift" : "left", composition.getId());
         this.unsetObjectSwapConfig(shift, composition.getId());
      };
   }

   private Consumer<MenuEntry> walkHereConsumer(boolean shift, ObjectComposition composition) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been set to Walk here.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Set object {} click swap for {} to Walk here", shift ? "shift" : "left", composition.getId());
         this.setObjectSwapConfig(shift, composition.getId(), -1);
      };
   }

   private Consumer<MenuEntry> walkHereConsumer(boolean shift, NPCComposition composition) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been set to Walk here.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Set npc {} click swap for {} to Walk here", shift ? "shift" : "left", composition.getId());
         this.setNpcSwapConfig(shift, composition.getId(), -1);
      };
   }

   private void configureNpcClick(MenuOpened event) {
      if (this.shiftModifier() && this.config.npcCustomization()) {
         MenuEntry[] entries = event.getMenuEntries();

         for(int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            MenuAction type = entry.getType();
            if (type == MenuAction.EXAMINE_NPC) {
               NPC npc = entry.getNpc();

               assert npc != null;

               NPCComposition composition = npc.getTransformedComposition();

               assert composition != null;

               String[] actions = composition.getActions();
               Integer swapConfig = this.getNpcSwapConfig(false, composition.getId());
               Integer shiftSwapConfig = this.getNpcSwapConfig(true, composition.getId());
               boolean hasAttack = Arrays.stream(composition.getActions()).anyMatch("Attack"::equalsIgnoreCase);
               MenuAction currentAction = swapConfig == null ? (hasAttack ? null : defaultAction(composition)) : (swapConfig == -1 ? MenuAction.WALK : (MenuAction)NPC_MENU_TYPES.get(swapConfig));
               MenuAction currentShiftAction = shiftSwapConfig == null ? (hasAttack ? null : defaultAction(composition)) : (shiftSwapConfig == -1 ? MenuAction.WALK : (MenuAction)NPC_MENU_TYPES.get(shiftSwapConfig));
               MenuEntry swapLeftClick = this.client.createMenuEntry(idx).setOption("Swap left-click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
               MenuEntry swapShiftClick = this.client.createMenuEntry(idx).setOption("Swap shift-click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
               Menu subLeft = swapLeftClick.createSubMenu();
               Menu subShift = swapShiftClick.createSubMenu();

               for(int actionIdx = 0; actionIdx < actions.length; ++actionIdx) {
                  if (!Strings.isNullOrEmpty(actions[actionIdx]) && !"Attack".equalsIgnoreCase(actions[actionIdx]) && !"Knock-Out".equals(actions[actionIdx]) && !"Lure".equals(actions[actionIdx])) {
                     MenuAction menuAction = (MenuAction)NPC_MENU_TYPES.get(actionIdx);
                     if (menuAction != currentAction) {
                        subLeft.createMenuEntry(0).setOption(actions[actionIdx]).setType(MenuAction.RUNELITE).onClick(this.npcConsumer(composition, actions, actionIdx, menuAction, false));
                     }

                     if (menuAction != currentShiftAction) {
                        subShift.createMenuEntry(0).setOption(actions[actionIdx]).setType(MenuAction.RUNELITE).onClick(this.npcConsumer(composition, actions, actionIdx, menuAction, true));
                     }
                  }
               }

               subLeft.createMenuEntry(0).setOption("Walk here").setType(MenuAction.RUNELITE).onClick(this.walkHereConsumer(false, composition));
               subShift.createMenuEntry(0).setOption("Walk here").setType(MenuAction.RUNELITE).onClick(this.walkHereConsumer(true, composition));
               if (this.getNpcSwapConfig(false, composition.getId()) != null) {
                  subLeft.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick(this.npcResetConsumer(composition, false));
               }

               if (this.getNpcSwapConfig(true, composition.getId()) != null) {
                  subShift.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick(this.npcResetConsumer(composition, true));
               }
            }
         }

      }
   }

   private Consumer<MenuEntry> npcConsumer(NPCComposition composition, String[] actions, int menuIdx, MenuAction menuAction, boolean shift) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been set to '").append(actions[menuIdx]).append("'.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Set npc {} swap for {} to {}", new Object[]{shift ? "shift" : "left", composition.getId(), menuAction});
         this.setNpcSwapConfig(shift, composition.getId(), menuIdx);
      };
   }

   private Consumer<MenuEntry> npcResetConsumer(NPCComposition composition, boolean shift) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been reset.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Unset npc {} swap for {}", shift ? "shift" : "left", composition.getId());
         this.unsetNpcSwapConfig(shift, composition.getId());
      };
   }

   private void configureWornItems(MenuOpened event) {
      if (this.shiftModifier()) {
         MenuEntry[] entries = event.getMenuEntries();

         for(int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            Widget w = entry.getWidget();
            if (w != null && WidgetUtil.componentToInterface(w.getId()) == 387 && "Examine".equals(entry.getOption()) && entry.getIdentifier() == 10) {
               w = w.getChild(1);
               if (w != null && w.getItemId() > -1) {
                  ItemComposition itemComposition = this.itemManager.getItemComposition(w.getItemId());
                  Integer leftClickOp = this.getWornItemSwapConfig(false, itemComposition.getId());
                  Integer shiftClickOp = this.getWornItemSwapConfig(true, itemComposition.getId());
                  MenuEntry swapLeftClick = this.client.createMenuEntry(idx).setOption("Swap left-click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
                  MenuEntry swapShiftClick = this.client.createMenuEntry(idx).setOption("Swap shift-click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
                  Menu subLeft = swapLeftClick.createSubMenu();
                  Menu subShift = swapShiftClick.createSubMenu();
                  int paramId = 451;
                  int componentOpId = 2;

                  for(int itemOpId = 1; paramId <= 458; ++itemOpId) {
                     String opName = itemComposition.getStringValue(paramId);
                     if (!Strings.isNullOrEmpty(opName)) {
                        if (leftClickOp == null || leftClickOp != componentOpId) {
                           subLeft.createMenuEntry(0).setOption(opName).setType(MenuAction.RUNELITE).onClick(this.wornItemConsumer(itemComposition, opName, componentOpId, false));
                        }

                        if (shiftClickOp == null || shiftClickOp != componentOpId) {
                           subShift.createMenuEntry(0).setOption(opName).setType(MenuAction.RUNELITE).onClick(this.wornItemConsumer(itemComposition, opName, componentOpId, true));
                        }

                        int[] subopParams = EQUIPMENT_SUBOP_PARAMS[itemOpId - 1];
                        int[] var18 = subopParams;
                        int var19 = subopParams.length;

                        for(int var20 = 0; var20 < var19; ++var20) {
                           int subopParam = var18[var20];
                           String subop = itemComposition.getStringValue(subopParam);
                           if (!Strings.isNullOrEmpty(subop)) {
                              if (leftClickOp == null || leftClickOp != subop.hashCode()) {
                                 subLeft.createMenuEntry(0).setOption(subop).setType(MenuAction.RUNELITE).onClick(this.wornItemConsumer(itemComposition, subop, subop.hashCode(), false));
                              }

                              if (shiftClickOp == null || shiftClickOp != subop.hashCode()) {
                                 subShift.createMenuEntry(0).setOption(subop).setType(MenuAction.RUNELITE).onClick(this.wornItemConsumer(itemComposition, subop, subop.hashCode(), true));
                              }
                           }
                        }
                     }

                     ++paramId;
                     ++componentOpId;
                  }

                  if (leftClickOp != null) {
                     subLeft.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((e) -> {
                        String message = (new ChatMessageBuilder()).append("The default worn left-click option for '").append(itemComposition.getMembersName()).append("' ").append("has been reset.").build();
                        this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                        log.debug("Unset worn item left swap for {}", itemComposition.getMembersName());
                        this.unsetWornItemSwapConfig(false, itemComposition.getId());
                     });
                  }

                  if (shiftClickOp != null) {
                     subShift.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((e) -> {
                        String message = (new ChatMessageBuilder()).append("The default worn shift-click option for '").append(itemComposition.getMembersName()).append("' ").append("has been reset.").build();
                        this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                        log.debug("Unset worn item shift swap for {}", itemComposition.getMembersName());
                        this.unsetWornItemSwapConfig(true, itemComposition.getId());
                     });
                  }
               }
               break;
            }
         }

      }
   }

   private Consumer<MenuEntry> wornItemConsumer(ItemComposition itemComposition, String opName, int opIdx, boolean shift) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default worn ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(itemComposition.getMembersName())).append("' ").append("has been set to '").append(opName).append("'.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Set worn item {} swap for {} to {}", new Object[]{shift ? "shift" : "left", itemComposition.getMembersName(), opIdx});
         this.setWornItemSwapConfig(shift, itemComposition.getId(), opIdx);
      };
   }

   private void configureItems(MenuOpened event) {
      if (this.shiftModifier()) {
         MenuEntry[] entries = event.getMenuEntries();

         for(int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            Widget w = entry.getWidget();
            if (w != null && WidgetUtil.componentToInterface(w.getId()) == 149 && "Examine".equals(entry.getOption()) && entry.getIdentifier() == 10) {
               ItemComposition itemComposition = this.itemManager.getItemComposition(entry.getItemId());
               String[] actions = itemComposition.getInventoryActions();
               String[][] subops = itemComposition.getSubops();
               Integer leftClickOp = this.getItemSwapConfig(false, itemComposition.getId());
               Integer shiftClickOp = this.getItemSwapConfig(true, itemComposition.getId());
               int defaultLeftClickOp = this.defaultOp(itemComposition, false);
               int defaultShiftClickOp = this.defaultOp(itemComposition, true);
               MenuEntry swapLeftClick = this.client.createMenuEntry(idx).setOption("Swap left-click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
               MenuEntry swapShiftClick = this.client.createMenuEntry(idx).setOption("Swap shift-click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
               Menu subLeft = swapLeftClick.createSubMenu();
               Menu subShift = swapShiftClick.createSubMenu();

               for(int actionIdx = 0; actionIdx < actions.length; ++actionIdx) {
                  String opName = actions[actionIdx];
                  if (!Strings.isNullOrEmpty(opName)) {
                     if (this.config.leftClickCustomization() && defaultLeftClickOp != actionIdx && (leftClickOp == null || leftClickOp != actionIdx)) {
                        subLeft.createMenuEntry(0).setOption(opName).setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, opName, actionIdx, false));
                     }

                     if (this.config.shiftClickCustomization() && defaultShiftClickOp != actionIdx && (shiftClickOp == null || shiftClickOp != actionIdx)) {
                        subShift.createMenuEntry(0).setOption(opName).setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, opName, actionIdx, true));
                     }
                  }

                  if (subops != null && subops[actionIdx] != null) {
                     String[] var19 = subops[actionIdx];
                     int var20 = var19.length;

                     for(int var21 = 0; var21 < var20; ++var21) {
                        String subop = var19[var21];
                        if (subop != null) {
                           if (leftClickOp == null || leftClickOp != subop.hashCode()) {
                              subLeft.createMenuEntry(0).setOption(subop).setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, subop, subop.hashCode(), false));
                           }

                           if (shiftClickOp == null || shiftClickOp != subop.hashCode()) {
                              subShift.createMenuEntry(0).setOption(subop).setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, subop, subop.hashCode(), true));
                           }
                        }
                     }
                  }

                  if (actionIdx + 1 == 4) {
                     if (defaultLeftClickOp != -1 && this.config.leftClickCustomization()) {
                        subLeft.createMenuEntry(0).setOption("Use").setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, "Use", -1, false));
                     }

                     if (defaultShiftClickOp != -1 && this.config.shiftClickCustomization()) {
                        subShift.createMenuEntry(0).setOption("Use").setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, "Use", -1, true));
                     }
                  }
               }

               if (leftClickOp != null && this.config.leftClickCustomization()) {
                  subLeft.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((e) -> {
                     String message = (new ChatMessageBuilder()).append("The default held left-click option for '").append(itemComposition.getMembersName()).append("' ").append("has been reset.").build();
                     this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                     log.debug("Unset held item left swap for {}", itemComposition.getMembersName());
                     this.unsetItemSwapConfig(false, itemComposition.getId());
                  });
               }

               if (shiftClickOp != null && this.config.shiftClickCustomization()) {
                  subShift.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((e) -> {
                     String message = (new ChatMessageBuilder()).append("The default held shift-click option for '").append(itemComposition.getMembersName()).append("' ").append("has been reset.").build();
                     this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                     log.debug("Unset held item shift swap for {}", itemComposition.getMembersName());
                     this.unsetItemSwapConfig(true, itemComposition.getId());
                  });
               }
               break;
            }
         }

      }
   }

   private Consumer<MenuEntry> heldItemConsumer(ItemComposition itemComposition, String opName, int opIdx, boolean shift) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default held ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(itemComposition.getMembersName())).append("' ").append("has been set to '").append(opName).append("'.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Set held item {} swap for {} to {}", new Object[]{shift ? "shift" : "left", itemComposition.getMembersName(), opIdx});
         this.setItemSwapConfig(shift, itemComposition.getId(), opIdx);
      };
   }

   private void configureUiSwap(MenuOpened event) {
      if (this.shiftModifier()) {
         MenuEntry[] entries = event.getMenuEntries();
         MenuEntry swapLeftClick = null;
         MenuEntry swapShiftClick = null;
         Menu subLeft = null;
         Menu subShift = null;
         boolean initialized = false;

         for(int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            if (entry.getType() == MenuAction.CC_OP || entry.getType() == MenuAction.CC_OP_LOW_PRIORITY || entry.getType() == MenuAction.WIDGET_TARGET) {
               Widget w = entry.getWidget();
               if (w != null && w.getActions() != null) {
                  int interId = WidgetUtil.componentToInterface(w.getId());
                  if (interId != 149 && (interId != 387 || w.getId() == 25362460) && (w.getIndex() == -1 || w.getItemId() != -1)) {
                     int componentId = w.getId();
                     int itemId = w.getIndex() == -1 ? -1 : ItemVariationMapping.map(w.getItemId());
                     int identifier = entry.getIdentifier();
                     Integer leftClick = this.getUiSwapConfig(false, componentId, itemId);
                     Integer shiftClick = this.getUiSwapConfig(true, componentId, itemId);
                     int lowestOp = this.findLowestOp(w);
                     int highestOp = 10;

                     for(int i = idx; i >= 0; --i) {
                        MenuEntry opEntry = entries[i];
                        if (opEntry.getWidget() == w) {
                           highestOp = opEntry.getIdentifier();
                        }
                     }

                     if (!initialized) {
                        initialized = true;
                        swapLeftClick = this.client.createMenuEntry(2).setOption("Swap left-click").setType(MenuAction.RUNELITE);
                        swapShiftClick = this.client.createMenuEntry(2).setOption("Swap shift-click").setType(MenuAction.RUNELITE);
                        subLeft = swapLeftClick.createSubMenu();
                        subShift = swapShiftClick.createSubMenu();
                     }

                     if (identifier != lowestOp && (leftClick == null || leftClick != identifier)) {
                        subLeft.createMenuEntry(0).setOption(entry.getOption()).setType(MenuAction.RUNELITE).onClick(this.uiConsumer(entry.getOption(), entry.getTarget(), false, componentId, itemId, identifier));
                     }

                     if (identifier != lowestOp && (shiftClick == null || shiftClick != identifier)) {
                        subShift.createMenuEntry(0).setOption(entry.getOption()).setType(MenuAction.RUNELITE).onClick(this.uiConsumer(entry.getOption(), entry.getTarget(), true, componentId, itemId, identifier));
                     }

                     if (identifier == highestOp) {
                        if (leftClick != null) {
                           subLeft.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((menuEntry) -> {
                              String message = (new ChatMessageBuilder()).append("The default left-click option for '").append(Text.removeTags(entry.getTarget())).append("' ").append("has been reset.").build();
                              this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                              log.debug("Unset ui left swap for {}/{}", componentId, menuEntry.getTarget());
                              this.unsetUiSwapConfig(false, componentId, itemId);
                           });
                        }

                        if (shiftClick != null) {
                           subShift.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((menuEntry) -> {
                              String message = (new ChatMessageBuilder()).append("The default shift-click option for '").append(Text.removeTags(entry.getTarget())).append("' ").append("has been reset.").build();
                              this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                              log.debug("Unset ui shift swap for {}/{}", componentId, menuEntry.getTarget());
                              this.unsetUiSwapConfig(true, componentId, itemId);
                           });
                        }

                        swapLeftClick.setTarget(entry.getTarget());
                        swapShiftClick.setTarget(entry.getTarget());
                     }
                  }
               }
            }
         }

      }
   }

   private int findLowestOp(Widget w) {
      for(int i = 0; i <= 9; ++i) {
         if (i == 5 && this.isOpTarget(w) && !Strings.isNullOrEmpty(w.getTargetVerb())) {
            return 0;
         }

         if ((this.testOpMask(w, i) || w.getOnOpListener() != null) && !Strings.isNullOrEmpty(w.getActions()[i])) {
            return i + 1;
         }
      }

      return -1;
   }

   private boolean testOpMask(Widget w, int op) {
      WidgetConfigNode n = this.client.getWidgetConfig(w);
      int mask = n != null ? n.getClickMask() : w.getClickMask();
      return (mask >> op + 1 & 1) != 0;
   }

   private boolean isOpTarget(Widget w) {
      WidgetConfigNode n = this.client.getWidgetConfig(w);
      int mask = n != null ? n.getClickMask() : w.getClickMask();
      return (mask & 129024) != 0;
   }

   private Consumer<MenuEntry> uiConsumer(String option, String target, boolean shift, int componentId, int itemId, int opId) {
      return (e) -> {
         String message = (new ChatMessageBuilder()).append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(target)).append("' ").append("has been set to '").append(Text.removeTags(option)).append("'.").build();
         this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         log.debug("Set ui {} swap for {}/{} to {}", new Object[]{shift ? "shift" : "left", componentId, itemId, opId});
         this.setUiSwapConfig(shift, componentId, itemId, opId);
      };
   }

   private boolean swapBank(Menu menu, MenuEntry menuEntry, MenuAction type) {
      if (type != MenuAction.CC_OP && type != MenuAction.CC_OP_LOW_PRIORITY) {
         return false;
      } else {
         int widgetGroupId = WidgetUtil.componentToInterface(menuEntry.getParam1());
         boolean isDepositBoxPlayerInventory = widgetGroupId == 192;
         boolean isChambersOfXericStorageUnitPlayerInventory = widgetGroupId == 551;
         boolean isGroupStoragePlayerInventory = widgetGroupId == 725;
         if (!this.shiftModifier() || this.config.bankDepositShiftClick() == ShiftDepositMode.OFF || type != MenuAction.CC_OP || menuEntry.getIdentifier() != (!isGroupStoragePlayerInventory && !isChambersOfXericStorageUnitPlayerInventory ? 2 : 1) || !menuEntry.getOption().startsWith("Deposit-") && !menuEntry.getOption().startsWith("Store") && !menuEntry.getOption().startsWith("Donate")) {
            if (this.shiftModifier() && this.config.bankWithdrawShiftClick() != ShiftWithdrawMode.OFF && type == MenuAction.CC_OP && menuEntry.getIdentifier() == 1 && menuEntry.getOption().startsWith("Withdraw")) {
               ShiftWithdrawMode shiftWithdrawMode = this.config.bankWithdrawShiftClick();
               MenuAction action;
               int opId;
               if (widgetGroupId != 271 && widgetGroupId != 550) {
                  action = shiftWithdrawMode.getMenuAction();
                  opId = shiftWithdrawMode.getIdentifier();
               } else {
                  action = MenuAction.CC_OP;
                  opId = shiftWithdrawMode.getIdentifierChambersStorageUnit();
               }

               this.bankModeSwap(menu, action, opId);
               return true;
            } else {
               return false;
            }
         } else {
            ShiftDepositMode shiftDepositMode = this.config.bankDepositShiftClick();
            int opId = isDepositBoxPlayerInventory ? shiftDepositMode.getIdentifierDepositBox() : (isChambersOfXericStorageUnitPlayerInventory ? shiftDepositMode.getIdentifierChambersStorageUnit() : (isGroupStoragePlayerInventory ? shiftDepositMode.getIdentifierGroupStorage() : shiftDepositMode.getIdentifier()));
            MenuAction action = opId >= 6 ? MenuAction.CC_OP_LOW_PRIORITY : MenuAction.CC_OP;
            this.bankModeSwap(menu, action, opId);
            return true;
         }
      }
   }

   private void bankModeSwap(Menu menu, MenuAction entryType, int entryIdentifier) {
      MenuEntry[] menuEntries = menu.getMenuEntries();

      for(int i = menuEntries.length - 1; i >= 0; --i) {
         MenuEntry entry = menuEntries[i];
         if (entry.getType() == entryType && entry.getIdentifier() == entryIdentifier) {
            entry.setType(MenuAction.CC_OP);
            menuEntries[i] = menuEntries[menuEntries.length - 1];
            menuEntries[menuEntries.length - 1] = entry;
            menu.setMenuEntries(menuEntries);
            break;
         }
      }

   }

   private void swapMenuEntry(MenuEntry parent, Menu menu, MenuEntry[] menuEntries, int index, MenuEntry menuEntry) {
      Menu sub = menuEntry.getSubMenu();
      int eventId;
      if (sub != null) {
         eventId = 0;
         MenuEntry[] subEntries = sub.getMenuEntries();
         MenuEntry[] var9 = subEntries;
         int var10 = subEntries.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            MenuEntry subEntry = var9[var11];
            this.swapMenuEntry(menuEntry, sub, subEntries, eventId++, subEntry);
         }
      }

      eventId = menuEntry.getIdentifier();
      MenuAction menuAction = menuEntry.getType();
      String option = Text.removeTags(menuEntry.getOption()).toLowerCase();
      String target = Text.removeTags(menuEntry.getTarget()).toLowerCase();
      Widget w = parent != null ? parent.getWidget() : menuEntry.getWidget();
      if (w != null && WidgetUtil.componentToInterface(w.getId()) == 149) {
         label194: {
            if (this.lastShift) {
               if (!this.config.shiftClickCustomization()) {
                  break label194;
               }
            } else if (!this.config.leftClickCustomization()) {
               break label194;
            }

            Integer swapIndex = this.getItemSwapConfig(this.lastShift, w.getItemId());
            if (swapIndex != null) {
               if (swapIndex == -1) {
                  this.swap(menu, menuEntries, "use", target, index, true);
               } else if (swapIndex + 1 == menuEntry.getItemOp()) {
                  this.swap(menu, menuEntries, index, menuEntries.length - 1);
               } else if (parent != null && menuEntry.getOption().hashCode() == swapIndex) {
                  this.clone(menuEntry);
               }

               return;
            }
         }
      }

      if (w != null && WidgetUtil.componentToInterface(w.getId()) == 387) {
         Widget child = w.getChild(1);
         if (child != null && child.getItemId() > -1) {
            Integer wornItemSwapConfig = this.getWornItemSwapConfig(this.shiftModifier(), child.getItemId());
            if (wornItemSwapConfig != null) {
               if (wornItemSwapConfig == menuEntry.getIdentifier()) {
                  this.swap(menu, menuEntries, index, menuEntries.length - 1);
               } else if (parent != null && menuEntry.getOption().hashCode() == wornItemSwapConfig) {
                  this.clone(menuEntry);
               }

               return;
            }
         }
      }

      int objMenuIdx = OBJECT_MENU_TYPES.indexOf(menuAction);
      Integer customOption;
      int componentId;
      if (objMenuIdx != -1) {
         componentId = eventId & '\uffff';
         ObjectComposition objectComposition = this.client.getObjectDefinition(componentId);
         if (objectComposition.getImpostorIds() != null) {
            objectComposition = objectComposition.getImpostor();
            componentId = objectComposition.getId();
         }

         customOption = this.getObjectSwapConfig(this.shiftModifier(), componentId);
         if (customOption != null && customOption >= 0) {
            if (customOption == objMenuIdx) {
               this.swap(menu, menuEntries, index, menuEntries.length - 1);
            } else if (parent != null && customOption == this.packSubID(objMenuIdx, menuEntry.getIdentifier() >> 16)) {
               this.clone(menuEntry);
            }

            return;
         }
      }

      NPC hintArrowNpc;
      if (NPC_MENU_TYPES.contains(menuAction)) {
         hintArrowNpc = menuEntry.getNpc();

         assert hintArrowNpc != null;

         NPCComposition composition = hintArrowNpc.getTransformedComposition();

         assert composition != null;

         customOption = this.getNpcSwapConfig(this.shiftModifier(), composition.getId());
         if (customOption != null && customOption >= 0) {
            MenuAction swapAction = (MenuAction)NPC_MENU_TYPES.get(customOption);
            if (swapAction == menuAction) {
               int i;
               for(i = index; i < menuEntries.length - 1 && NPC_MENU_TYPES.contains(menuEntries[i + 1].getType()); ++i) {
               }

               this.swap(menu, menuEntries, index, i);
               return;
            }
         }
      }

      if ((menuAction == MenuAction.CC_OP || menuAction == MenuAction.CC_OP_LOW_PRIORITY || menuAction == MenuAction.WIDGET_TARGET) && w != null && (w.getIndex() == -1 || w.getItemId() != -1) && w.getActions() != null && WidgetUtil.componentToInterface(w.getId()) != 149 && (WidgetUtil.componentToInterface(w.getId()) != 387 || w.getId() == 25362460) && (index > 0 && menuEntries[index - 1].getWidget() == w || index + 1 < menuEntries.length && menuEntries[index + 1].getWidget() == w)) {
         componentId = w.getId();
         int itemId = w.getIndex() == -1 ? -1 : ItemVariationMapping.map(w.getItemId());
         customOption = this.getMigratedUiSwapConfig(this.shiftModifier(), componentId, itemId);
         if (customOption != null && customOption == menuEntry.getIdentifier()) {
            this.swap(menu, menuEntries, index, menuEntries.length - 1);
            return;
         }
      }

      if (!this.swapBank(menu, menuEntry, menuAction)) {
         hintArrowNpc = this.client.getHintArrowNpc();
         if (hintArrowNpc == null || hintArrowNpc.getIndex() != eventId || !NPC_MENU_TYPES.contains(menuAction)) {
            Collection<Swap> swaps = this.swaps.get(option);
            Iterator var30 = swaps.iterator();

            while(var30.hasNext()) {
               Swap swap = (Swap)var30.next();
               if (swap.getTargetPredicate().test(target) && (Boolean)swap.getEnabled().get() && this.swap(menu, menuEntries, swap.getSwappedOption(), target, index, swap.isStrict())) {
                  break;
               }
            }

         }
      }
   }

   @Subscribe
   public void onClientTick(ClientTick clientTick) {
      this.lastShift = this.curShift;
      this.curShift = this.shiftModifier();
      if (!this.client.isMenuOpen()) {
         MenuEntry[] var2 = this.client.getMenuEntries();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            MenuEntry menuEntry = var2[var4];
            MenuAction type = menuEntry.getType();
            boolean shift;
            Integer customOption;
            if (OBJECT_MENU_TYPES.contains(type)) {
               int objectId = menuEntry.getIdentifier();
               ObjectComposition objectComposition = this.client.getObjectDefinition(objectId);
               if (objectComposition.getImpostorIds() != null) {
                  objectComposition = objectComposition.getImpostor();
                  objectId = objectComposition.getId();
               }

               shift = this.shiftModifier();
               customOption = this.getObjectSwapConfig(shift, objectId);
               if (customOption == null && shift && this.config.objectShiftClickWalkHere() || customOption != null && customOption == -1) {
                  menuEntry.setDeprioritized(true);
               }
            } else if (!NPC_MENU_TYPES.contains(type)) {
               if ((type == MenuAction.GROUND_ITEM_FIRST_OPTION || type == MenuAction.GROUND_ITEM_SECOND_OPTION || type == MenuAction.GROUND_ITEM_THIRD_OPTION || type == MenuAction.GROUND_ITEM_FOURTH_OPTION || type == MenuAction.GROUND_ITEM_FIFTH_OPTION) && this.shiftModifier() && this.config.groundItemShiftClickWalkHere()) {
                  menuEntry.setDeprioritized(true);
               }
            } else {
               NPC npc = menuEntry.getNpc();

               assert npc != null;

               NPCComposition composition = npc.getTransformedComposition();

               assert composition != null;

               shift = this.shiftModifier();
               customOption = this.getNpcSwapConfig(shift, composition.getId());
               if (customOption == null && shift && this.config.npcShiftClickWalkHere() || customOption != null && customOption == -1) {
                  menuEntry.setDeprioritized(true);
               }
            }
         }

      }
   }

   @Subscribe
   public void onPostMenuSort(PostMenuSort postMenuSort) {
      if (!this.client.isMenuOpen()) {
         Menu root = this.client.getMenu();
         MenuEntry[] menuEntries = root.getMenuEntries();
         int idx = 0;
         MenuEntry[] var5 = menuEntries;
         int var6 = menuEntries.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            MenuEntry entry = var5[var7];
            this.swapMenuEntry((MenuEntry)null, root, menuEntries, idx++, entry);
         }

         if (this.config.removeDeadNpcMenus()) {
            this.removeDeadNpcs();
         }

         this.cacheOptionIndexes.clear();
         this.cacheOptionMenu = null;
      }
   }

   private void removeDeadNpcs() {
      MenuEntry[] oldEntries = this.client.getMenuEntries();
      MenuEntry[] newEntries = (MenuEntry[])Arrays.stream(oldEntries).filter((e) -> {
         NPC npc = e.getNpc();
         return npc == null || !this.npcUtil.isDying(npc);
      }).toArray((x$0) -> {
         return new MenuEntry[x$0];
      });
      if (oldEntries.length != newEntries.length) {
         this.client.setMenuEntries(newEntries);
      }

   }

   private boolean swap(Menu menu, MenuEntry[] menuEntries, String option, String target, int index, boolean strict) {
      int optionIdx = this.findIndex(menu, menuEntries, index, option, target, strict);
      if (optionIdx >= 0) {
         this.swap(menu, menuEntries, optionIdx, index);
         return true;
      } else {
         return false;
      }
   }

   private int findIndex(Menu menu, MenuEntry[] entries, int limit, String option, String target, boolean strict) {
      if (strict) {
         List<Integer> indexes = this.findOptionIndex(menu, option);

         for(int i = indexes.size() - 1; i >= 0; --i) {
            int idx = (Integer)indexes.get(i);
            MenuEntry entry = entries[idx];
            String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();
            if (idx < limit && entryTarget.equals(target)) {
               return idx;
            }
         }
      } else {
         for(int i = limit - 1; i >= 0; --i) {
            MenuEntry entry = entries[i];
            String entryOption = Text.removeTags(entry.getOption()).toLowerCase();
            String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();
            if (entryOption.contains(option.toLowerCase()) && entryTarget.equals(target)) {
               return i;
            }
         }
      }

      return -1;
   }

   private List<Integer> findOptionIndex(Menu menu, String option) {
      if (this.cacheOptionMenu != menu || this.cacheOptionIndexes.isEmpty()) {
         int idx = 0;
         this.cacheOptionMenu = menu;
         this.cacheOptionIndexes.clear();
         MenuEntry[] var4 = menu.getMenuEntries();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            MenuEntry entry = var4[var6];
            String opt = Text.removeTags(entry.getOption()).toLowerCase();
            this.cacheOptionIndexes.put(opt, idx++);
         }

         log.trace("[{}] Rebuilt option index cache with {} entries", this.client.getGameCycle(), idx);
      }

      return this.cacheOptionIndexes.get(option);
   }

   private void swap(Menu menu, MenuEntry[] entries, int index1, int index2) {
      if (index1 != index2) {
         MenuEntry entry1 = entries[index1];
         MenuEntry entry2 = entries[index2];
         entries[index1] = entry2;
         entries[index2] = entry1;
         if (entry1.getType() == MenuAction.CC_OP_LOW_PRIORITY) {
            entry1.setType(MenuAction.CC_OP);
         }

         if (entry2.getType() == MenuAction.CC_OP_LOW_PRIORITY) {
            entry2.setType(MenuAction.CC_OP);
         }

         menu.setMenuEntries(entries);
         if (this.cacheOptionMenu == menu) {
            String option1 = Text.removeTags(entry1.getOption()).toLowerCase();
            String option2 = Text.removeTags(entry2.getOption()).toLowerCase();
            List<Integer> list1 = this.cacheOptionIndexes.get(option1);
            List<Integer> list2 = this.cacheOptionIndexes.get(option2);
            list1.remove(index1);
            list2.remove(index2);
            sortedInsert(list1, index2);
            sortedInsert(list2, index1);
            log.trace("Swapped option index {} <-> {}", index1, index2);
         }

      }
   }

   private MenuEntry clone(MenuEntry menuEntry) {
      return this.client.createMenuEntry(-1).setOption(menuEntry.getOption()).setTarget(menuEntry.getTarget()).setIdentifier(menuEntry.getIdentifier()).setType(menuEntry.getType() == MenuAction.CC_OP_LOW_PRIORITY ? MenuAction.CC_OP : menuEntry.getType()).setItemId(menuEntry.getItemId()).setParam0(menuEntry.getParam0()).setParam1(menuEntry.getParam1()).onClick(menuEntry.onClick());
   }

   private static <T extends Comparable<? super T>> void sortedInsert(List<T> list, T value) {
      int idx = Collections.binarySearch(list, value);
      list.add(idx < 0 ? -idx - 1 : idx, value);
   }

   private boolean shiftModifier() {
      return this.client.isKeyPressed(81);
   }

   private Integer getObjectSwapConfig(boolean shift, int objectId) {
      String config = this.configManager.getConfiguration("menuentryswapper", (shift ? "object_shift_" : "object_") + objectId);
      return config != null && !config.isEmpty() ? Integer.parseInt(config) : null;
   }

   private void setObjectSwapConfig(boolean shift, int objectId, int index) {
      this.configManager.setConfiguration("menuentryswapper", (shift ? "object_shift_" : "object_") + objectId, (Object)index);
   }

   private void unsetObjectSwapConfig(boolean shift, int objectId) {
      this.configManager.unsetConfiguration("menuentryswapper", (shift ? "object_shift_" : "object_") + objectId);
   }

   private Integer getNpcSwapConfig(boolean shift, int npcId) {
      String config = this.configManager.getConfiguration("menuentryswapper", (shift ? "npc_shift_" : "npc_") + npcId);
      return config != null && !config.isEmpty() ? Integer.parseInt(config) : null;
   }

   private void setNpcSwapConfig(boolean shift, int npcId, int index) {
      this.configManager.setConfiguration("menuentryswapper", (shift ? "npc_shift_" : "npc_") + npcId, (Object)index);
   }

   private void unsetNpcSwapConfig(boolean shift, int npcId) {
      this.configManager.unsetConfiguration("menuentryswapper", (shift ? "npc_shift_" : "npc_") + npcId);
   }

   private static MenuAction defaultAction(NPCComposition composition) {
      String[] actions = composition.getActions();

      for(int i = 0; i < actions.length && i < NPC_MENU_TYPES.size(); ++i) {
         if (!Strings.isNullOrEmpty(actions[i]) && !actions[i].equalsIgnoreCase("Attack")) {
            return (MenuAction)NPC_MENU_TYPES.get(i);
         }
      }

      return null;
   }

   private int defaultOp(ItemComposition itemComposition, boolean shift) {
      if (shift) {
         int shiftClickActionIndex = itemComposition.getShiftClickActionIndex();
         if (shiftClickActionIndex >= 0) {
            return shiftClickActionIndex;
         }
      }

      String[] actions = itemComposition.getInventoryActions();

      for(int actionIdx = 0; actionIdx < 3; ++actionIdx) {
         if (!Strings.isNullOrEmpty(actions[actionIdx])) {
            return actionIdx;
         }
      }

      return -1;
   }

   private Integer getMigratedUiSwapConfig(boolean shift, int componentId, int itemId) {
      Integer swap = this.getUiSwapConfig(shift, componentId, itemId);
      if (componentId == 786444) {
         if (swap == null) {
            swap = this.getUiSwapConfig(shift, 786445, itemId);
            if (swap != null) {
               this.unsetUiSwapConfig(shift, 786445, itemId);
               this.setUiSwapConfig(shift, 786444, itemId, swap);
               log.debug("Migrated swap {} for {} from scrollbar to items", swap, itemId);
            }
         } else {
            this.unsetUiSwapConfig(shift, 786445, itemId);
         }
      }

      return swap;
   }

   private Integer getUiSwapConfig(boolean shift, int componentId, int itemId) {
      String config = this.configManager.getConfiguration("menuentryswapper", (shift ? "ui_shift_" : "ui_") + componentId + (itemId != -1 ? "_" + itemId : ""));
      return config != null && !config.isEmpty() ? Integer.parseInt(config) : null;
   }

   private void setUiSwapConfig(boolean shift, int componentId, int itemId, int op) {
      this.configManager.setConfiguration("menuentryswapper", (shift ? "ui_shift_" : "ui_") + componentId + (itemId != -1 ? "_" + itemId : ""), (Object)op);
   }

   private void unsetUiSwapConfig(boolean shift, int componentId, int itemId) {
      this.configManager.unsetConfiguration("menuentryswapper", (shift ? "ui_shift_" : "ui_") + componentId + (itemId != -1 ? "_" + itemId : ""));
   }

   static {
      NPC_MENU_TYPES = ImmutableList.of(MenuAction.NPC_FIRST_OPTION, MenuAction.NPC_SECOND_OPTION, MenuAction.NPC_THIRD_OPTION, MenuAction.NPC_FOURTH_OPTION, MenuAction.NPC_FIFTH_OPTION);
      OBJECT_MENU_TYPES = ImmutableList.of(MenuAction.GAME_OBJECT_FIRST_OPTION, MenuAction.GAME_OBJECT_SECOND_OPTION, MenuAction.GAME_OBJECT_THIRD_OPTION, MenuAction.GAME_OBJECT_FOURTH_OPTION, MenuAction.GAME_OBJECT_FIFTH_OPTION);
      ESSENCE_MINE_NPCS = ImmutableSet.of("aubury", "archmage sedridor", "wizard distentor", "wizard cromperty", "brimstail");
      TEMPOROSS_NPCS = ImmutableSet.of("captain dudi", "captain pudi", "first mate deri", "first mate peri");
      EQUIPMENT_SUBOP_PARAMS = new int[][]{{661, 2074, 2082, 2090, 2098, 2106, 2114, 2122, 2130, 2138, 2146, 2154, 2162, 2170, 2178, 2186, 2194, 2202, 2210, 2218}, {662, 2075, 2083, 2091, 2099, 2107, 2115, 2123, 2131, 2139, 2147, 2155, 2163, 2171, 2179, 2187, 2195, 2203, 2211, 2219}, {663, 2076, 2084, 2092, 2100, 2108, 2116, 2124, 2132, 2140, 2148, 2156, 2164, 2172, 2180, 2188, 2196, 2204, 2212, 2220}, {2069, 2077, 2085, 2093, 2101, 2109, 2117, 2125, 2133, 2141, 2149, 2157, 2165, 2173, 2181, 2189, 2197, 2205, 2213, 2221}, {2070, 2078, 2086, 2094, 2102, 2110, 2118, 2126, 2134, 2142, 2150, 2158, 2166, 2174, 2182, 2190, 2198, 2206, 2214, 2222}, {2071, 2079, 2087, 2095, 2103, 2111, 2119, 2127, 2135, 2143, 2151, 2159, 2167, 2175, 2183, 2191, 2199, 2207, 2215, 2223}, {2072, 2080, 2088, 2096, 2104, 2112, 2120, 2128, 2136, 2144, 2152, 2160, 2168, 2176, 2184, 2192, 2200, 2208, 2216, 2224}, {2073, 2081, 2089, 2097, 2105, 2113, 2121, 2129, 2137, 2145, 2153, 2161, 2169, 2177, 2185, 2193, 2201, 2209, 2217, 2225}};
   }
}
