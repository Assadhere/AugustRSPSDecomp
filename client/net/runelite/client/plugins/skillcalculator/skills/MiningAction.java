package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;

public enum MiningAction implements ItemSkillAction {
   CLAY(434, 1, 5.0F),
   RUNE_ESSENCE(1436, 1, 5.0F),
   COPPER_ORE(436, 1, 17.5F),
   TIN_ORE(438, 1, 17.5F),
   LIMESTONE(3211, 10, 26.5F),
   BARRONITE_SHARDS(25683, 14, 16.0F) {
      public String getName(ItemManager itemManager) {
         return "Barronite shards";
      }
   },
   BARRONITE_DEPOSIT(25684, 14, 32.0F),
   IRON_ORE(440, 15, 35.0F),
   SILVER_ORE(442, 20, 40.0F),
   LEAD_ORE(31716, 25, 40.5F),
   PURE_ESSENCE(7936, 30, 5.0F) {
      public boolean isMembers(ItemManager itemManager) {
         return true;
      }
   },
   COAL(453, 30, 50.0F),
   SANDSTONE_1KG(6971, 35, 30.0F),
   SANDSTONE_2KG(6973, 35, 40.0F),
   SANDSTONE_5KG(6975, 35, 50.0F),
   SANDSTONE_10KG(6977, 35, 60.0F),
   DENSE_ESSENCE_BLOCK(13445, 38, 12.0F),
   GEM_ROCKS(1629, 40, 65.0F) {
      public String getName(ItemManager itemManager) {
         return "Gem rocks";
      }
   },
   GOLD_ORE(444, 40, 65.0F),
   CALCIFIED_ROCKS(29381, 41, 33.0F) {
      public String getName(ItemManager itemManager) {
         return "Calcified rocks";
      }
   },
   GRANITE_500G(6979, 45, 50.0F),
   GRANITE_2KG(6981, 45, 60.0F),
   GRANITE_5KG(6983, 45, 75.0F),
   RUBIUM_SPLINTERS(31722, 48, 72.0F),
   MITHRIL_ORE(447, 55, 80.0F),
   SOFT_CLAY(1761, 70, 5.0F) {
      public boolean isMembers(ItemManager itemManager) {
         return true;
      }
   },
   ADAMANTITE_ORE(449, 70, 95.0F),
   NICKEL_ORE(31719, 74, 80.5F),
   RUNITE_ORE(451, 85, 125.0F),
   AMETHYST(21347, 92, 240.0F);

   private final int itemId;
   private final int level;
   private final float xp;

   private MiningAction(int itemId, int level, float xp) {
      this.itemId = itemId;
      this.level = level;
      this.xp = xp;
   }

   public int getItemId() {
      return this.itemId;
   }

   public int getLevel() {
      return this.level;
   }

   public float getXp() {
      return this.xp;
   }
}
