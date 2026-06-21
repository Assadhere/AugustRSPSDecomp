package net.runelite.client.hiscore;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CountId {
   OLYMPIAN_RAIDS("olympian_raids", "Olympian Raids", CountCategory.RAID),
   OLYMPIAN_RAIDS_HARD_MODE("olympian_raids_hard_mode", "Olympian Raids (Hard Mode)", CountCategory.RAID),
   TOB_RAIDS("tob_raids", "Theatre of Blood", CountCategory.RAID),
   TOB_RAIDS_HARD_MODE("tob_raids_hard_mode", "Theatre of Blood (Hard Mode)", CountCategory.RAID),
   TOA_COMMON_CHEST_OPENS("toa_common_chest_opens", "Tombs of Amascut", CountCategory.RAID),
   TOA_PURPLE_CHESTS("toa_purple_chests", "Tombs of Amascut (Purple)", CountCategory.RAID),
   AKKHA("akkha", "Akkha", CountCategory.RAID),
   BA_BA("ba-ba", "Ba-Ba", CountCategory.RAID),
   KEPHRI("kephri", "Kephri", CountCategory.RAID),
   ZEBAK("zebak", "Zebak", CountCategory.RAID),
   APOLLO("apollo", "Apollo", CountCategory.RAID),
   POSEIDON("poseidon", "Poseidon", CountCategory.RAID),
   HEPHAESTUS("hephaestus", "Hephaestus", CountCategory.RAID),
   FEE_FI("fee_fi", "Fee Fi", CountCategory.RAID),
   FO_FUM("fo_fum", "Fo Fum", CountCategory.RAID),
   SOL_HEREDIT("sol_heredit", "Sol Heredit", CountCategory.RAID),
   YAMA("yama", "Yama", CountCategory.BOSS),
   ZULRAH("zulrah", "Zulrah", CountCategory.BOSS),
   DANGER_SNEK("danger_snek", "Danger Snek", CountCategory.BOSS),
   AZRAEL("azrael", "Azrael", CountCategory.BOSS),
   NEX("nex", "Nex", CountCategory.BOSS),
   CERBERUS("cerberus", "Cerberus", CountCategory.BOSS),
   IGNIS("ignis", "Ignis", CountCategory.BOSS),
   VETION("vet'ion", "Vet'ion", CountCategory.BOSS),
   TORMENTED_DEMON("tormented_demon", "Tormented Demon", CountCategory.BOSS),
   CORPOREAL_BEAST("corporeal_beast", "Corporeal Beast", CountCategory.BOSS),
   DAGANNOTH_REX("dagannoth_rex", "Dagannoth Rex", CountCategory.BOSS),
   DAGANNOTH_PRIME("dagannoth_prime", "Dagannoth Prime", CountCategory.BOSS),
   DAGANNOTH_SUPREME("dagannoth_supreme", "Dagannoth Supreme", CountCategory.BOSS),
   KRIL_TSUTSAROTH("k'ril_tsutsaroth", "K'ril Tsutsaroth", CountCategory.BOSS),
   GENERAL_GRAARDOR("general_graardor", "General Graardor", CountCategory.BOSS),
   COMMANDER_ZILYANA("commander_zilyana", "Commander Zilyana", CountCategory.BOSS),
   KREEARRA("kree'arra", "Kree'arra", CountCategory.BOSS),
   KRAKEN("kraken", "Kraken", CountCategory.BOSS),
   VORKATH("vorkath", "Vorkath", CountCategory.BOSS),
   DERWEN("derwen", "Derwen", CountCategory.BOSS),
   JUSTICIAR_ZACHARIAH("justiciar_zachariah", "Justiciar Zachariah", CountCategory.BOSS),
   PORAZDIR("porazdir", "Porazdir", CountCategory.BOSS),
   THERMONUCLEAR_SMOKE_DEVIL("thermonuclear_smoke_devil", "Thermonuclear Smoke Devil", CountCategory.BOSS),
   TZTOK_JAD("tztok-jad", "TzTok-Jad", CountCategory.BOSS),
   KING_BLACK_DRAGON("king_black_dragon", "King Black Dragon", CountCategory.BOSS),
   ZALCANO("zalcano", "Zalcano", CountCategory.BOSS),
   SKOTIZO("skotizo", "Skotizo", CountCategory.BOSS),
   DEMONIC_GORILLA("demonic_gorilla", "Demonic Gorilla", CountCategory.BOSS),
   TARN("tarn", "Tarn", CountCategory.BOSS),
   ECHO_DKS_COMPLETIONS("echo_dks_completions", "Echo DKS", CountCategory.BOSS),
   TREASURE_GOBLIN("treasure_goblin", "Treasure Goblin", CountCategory.BOSS),
   COW("cow", "Cow", CountCategory.BOSS),
   GOBLIN("goblin", "Goblin", CountCategory.BOSS),
   AHRIM_THE_BLIGHTED("ahrim_the_blighted", "Ahrim the Blighted", CountCategory.BOSS),
   DHAROK_THE_WRETCHED("dharok_the_wretched", "Dharok the Wretched", CountCategory.BOSS),
   GUTHAN_THE_INFESTED("guthan_the_infested", "Guthan the Infested", CountCategory.BOSS),
   KARIL_THE_TAINTED("karil_the_tainted", "Karil the Tainted", CountCategory.BOSS),
   TORAG_THE_CORRUPTED("torag_the_corrupted", "Torag the Corrupted", CountCategory.BOSS),
   VERAC_THE_DEFILED("verac_the_defiled", "Verac the Defiled", CountCategory.BOSS),
   GIANT_CHEST_OPENS("giant_chest_opens", "Giant Chest Opens", CountCategory.MINIGAME),
   CRYSTAL_CHEST_OPENS("crystal_chest_opens", "Crystal Chest Opens", CountCategory.MINIGAME),
   ENHANCED_CRYSTAL_CHEST_OPENS("enhanced_crystal_chest_opens", "Enhanced Crystal Chest Opens", CountCategory.MINIGAME),
   BARROWS_CHEST_OPENS("barrows_chest_opens", "Barrows Chest Opens", CountCategory.MINIGAME),
   GRUBBY_CHEST_OPENS("grubby_chest_opens", "Grubby Chest Opens", CountCategory.MINIGAME),
   FIRE_CAPE_GAMBLES("fire_cape_gambles", "Fire Cape Gambles", CountCategory.MINIGAME),
   SAY_YOUR_PRAYERS_WINS("say_your_prayers_wins", "Say Your Prayers", CountCategory.MINIGAME),
   SLAYER_TASKS("slayer_tasks", "Slayer Tasks", CountCategory.SLAYER),
   BOSS_SLAYER_TASKS("boss_slayer_tasks", "Boss Slayer Tasks", CountCategory.SLAYER),
   SUPERIOR_SLAYER_CREATURES("superior_slayer_creatures", "Superior Slayer Creatures", CountCategory.SLAYER),
   TOTAL_DEATHS("total_deaths", "Total Deaths", CountCategory.MISC),
   TOTAL_NPCS_KILLED("total_npcs_killed", "Total NPCs Killed", CountCategory.MISC),
   VOTES_CLAIMED("votes_claimed", "Votes Claimed", CountCategory.MISC),
   VOTE_GOBLIN("vote_goblin", "Vote Goblin", CountCategory.MISC),
   COLLECTION_LOG_SLOTS("collection_log_slots", "Collection Log Slots", CountCategory.MISC),
   SANDWICH_LADY("sandwich_lady", "Sandwich Lady", CountCategory.MISC),
   RICK_TURPENTINE("rick_turpentine", "Rick Turpentine", CountCategory.MISC);

   private final String id;
   private final String displayName;
   private final CountCategory category;
   private static final Map<String, CountId> BY_ID = (Map)Arrays.stream(values()).collect(Collectors.toMap(CountId::getId, Function.identity()));

   public static CountId fromId(String id) {
      return (CountId)BY_ID.get(id);
   }

   private CountId(String id, String displayName, CountCategory category) {
      this.id = id;
      this.displayName = displayName;
      this.category = category;
   }

   public String getId() {
      return this.id;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public CountCategory getCategory() {
      return this.category;
   }
}
