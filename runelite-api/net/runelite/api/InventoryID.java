package net.runelite.api;

/** @deprecated */
@Deprecated
public enum InventoryID {
   FISHING_TRAWLER_REWARD(0),
   TRADE(90),
   TRADEOTHER(32858),
   INVENTORY(93),
   EQUIPMENT(94),
   BANK(95),
   PUZZLE_BOX(140),
   BARROWS_REWARD(141),
   MONKEY_MADNESS_PUZZLE_BOX(221),
   DRIFT_NET_FISHING_REWARD(307),
   KINGDOM_OF_MISCELLANIA(390),
   CHAMBERS_OF_XERIC_CHEST(581),
   THEATRE_OF_BLOOD_CHEST(612),
   SEED_VAULT(626),
   GROUP_STORAGE(659),
   GROUP_STORAGE_INV(660),
   WILDERNESS_LOOT_CHEST(797),
   TOA_REWARD_CHEST(811),
   LUNAR_CHEST(847),
   FORTIS_COLOSSEUM_REWARD_CHEST(843);

   private final int id;

   private InventoryID(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }
}
