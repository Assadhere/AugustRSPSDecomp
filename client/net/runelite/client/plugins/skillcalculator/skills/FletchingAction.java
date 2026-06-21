package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;

public enum FletchingAction implements ItemSkillAction {
   ARROW_SHAFT(52, 1, 0.33F),
   HEADLESS_ARROW(53, 1, 1.0F),
   BRONZE_ARROW(882, 1, 1.3F),
   BRONZE_JAVELIN(825, 3, 1.0F),
   OGRE_ARROW(2866, 5, 1.0F),
   SHORTBOW(841, 5, 5.0F),
   SHORTBOW_U(50, 5, 5.0F),
   BRONZE_BOLTS(877, 9, 0.5F),
   BRONZE_CROSSBOW(9174, 9, 6.0F),
   WOODEN_STOCK(9440, 9, 6.0F),
   BRONZE_CROSSBOW_U(9454, 9, 12.0F),
   BRONZE_DART(806, 10, 1.8F),
   LONGBOW(839, 10, 10.0F),
   LONGBOW_U(48, 10, 10.0F),
   OPAL_BOLTS(879, 11, 1.6F),
   IRON_ARROW(884, 15, 2.5F),
   IRON_JAVELIN(826, 17, 2.0F),
   OAK_SHORTBOW(843, 20, 16.5F),
   OAK_SHORTBOW_U(54, 20, 16.5F),
   OAK_VALE_TOTEM(31054, 20, 254.8F) {
      public String getName(ItemManager itemManager) {
         return "Oak vale totem";
      }
   },
   IRON_DART(807, 22, 3.8F),
   BLURITE_CROSSBOW(9176, 24, 16.0F),
   OAK_STOCK(9442, 24, 16.0F),
   BLURITE_CROSSBOW_U(9456, 24, 32.0F),
   OAK_LONGBOW(845, 25, 25.0F),
   OAK_LONGBOW_U(56, 25, 25.0F),
   OAK_SHIELD(22251, 27, 50.0F),
   STEEL_ARROW(886, 30, 5.0F),
   KEBBIT_BOLTS(10158, 32, 1.0F),
   STEEL_JAVELIN(827, 32, 5.0F),
   WILLOW_SHORTBOW(849, 35, 33.3F),
   WILLOW_SHORTBOW_U(60, 35, 33.3F),
   WILLOW_VALE_TOTEM(31054, 35, 634.4F) {
      public String getName(ItemManager itemManager) {
         return "Willow vale totem";
      }
   },
   STEEL_DART(808, 37, 7.5F),
   IRON_BOLTS(9140, 39, 1.5F),
   IRON_CROSSBOW(9177, 39, 22.0F),
   WILLOW_STOCK(9444, 39, 22.0F),
   IRON_CROSSBOW_U(9457, 39, 44.0F),
   WILLOW_LONGBOW(847, 40, 41.5F),
   WILLOW_LONGBOW_U(58, 40, 41.5F),
   BATTLESTAFF(1391, 40, 80.0F),
   PEARL_BOLTS(880, 41, 3.2F),
   LONG_KEBBIT_BOLTS(10159, 42, 1.3F),
   WILLOW_SHIELD(22254, 42, 83.0F),
   SILVER_BOLTS(9145, 43, 2.5F),
   MITHRIL_ARROW(888, 45, 7.5F),
   STEEL_BOLTS(9141, 46, 3.5F),
   STEEL_CROSSBOW(9179, 46, 27.0F),
   TEAK_STOCK(9446, 46, 27.0F),
   STEEL_CROSSBOW_U(9459, 46, 54.0F),
   MITHRIL_JAVELIN(828, 47, 8.0F),
   MAPLE_SHORTBOW(853, 50, 50.0F),
   MAPLE_SHORTBOW_U(64, 50, 50.0F),
   MAPLE_VALE_TOTEM(31054, 50, 1007.2F) {
      public String getName(ItemManager itemManager) {
         return "Maple vale totem";
      }
   },
   BARBED_BOLTS(881, 51, 9.5F),
   BROAD_ARROWS(4150, 52, 10.0F),
   MITHRIL_DART(809, 52, 11.2F),
   GREENMAN_STATUE(31027, 53, 55.0F),
   MITHRIL_BOLTS(9142, 54, 5.0F),
   MAPLE_STOCK(9448, 54, 32.0F),
   MITHRIL_CROSSBOW(9181, 54, 32.0F),
   MITHRIL_CROSSBOW_U(9461, 54, 64.0F),
   BROAD_BOLTS(11875, 55, 3.0F),
   MAPLE_LONGBOW(851, 55, 58.2F),
   MAPLE_LONGBOW_U(62, 55, 58.3F),
   SAPPHIRE_BOLTS(9337, 56, 4.7F),
   MAPLE_SHIELD(22257, 57, 116.5F),
   EMERALD_BOLTS(9338, 58, 5.5F),
   CAMPHOR_BLOWPIPE(31575, 58, 140.0F),
   HUNTERS_SPEAR(29305, 60, 9.5F),
   ADAMANT_ARROW(890, 60, 10.0F),
   ADAMANT_BOLTS(9143, 61, 7.0F),
   ADAMANT_CROSSBOW(9183, 61, 41.0F),
   MAHOGANY_STOCK(9450, 61, 41.0F),
   ADAMANT_CROSSBOW_U(9463, 61, 82.0F),
   ADAMANT_JAVELIN(829, 62, 10.0F),
   RUBY_BOLTS(9339, 63, 6.3F),
   DIAMOND_BOLTS(9340, 65, 7.0F),
   YEW_SHORTBOW(857, 65, 67.5F),
   YEW_SHORTBOW_U(68, 65, 67.5F),
   YEW_VALE_TOTEM(31054, 65, 1635.2F) {
      public String getName(ItemManager itemManager) {
         return "Yew vale totem";
      }
   },
   ADAMANT_DART(810, 67, 15.0F),
   RUNITE_BOLTS(9144, 69, 10.0F),
   RUNE_CROSSBOW(9185, 69, 50.0F),
   YEW_STOCK(9452, 69, 50.0F),
   RUNITE_CROSSBOW_U(9465, 69, 100.0F),
   YEW_LONGBOW(855, 70, 75.0F),
   YEW_LONGBOW_U(66, 70, 75.0F),
   DRAGONSTONE_BOLTS(9341, 71, 8.2F),
   YEW_SHIELD(22260, 72, 150.0F),
   IRONWOOD_BLOWPIPE(31579, 72, 170.0F),
   ONYX_BOLTS(9342, 73, 9.4F),
   ATLATL_DART_TIPS(30998, 74, 0.1F),
   ATLATL_DART_SHAFT(31004, 74, 0.3F),
   HEADLESS_ATLATL_DART(31010, 74, 1.0F),
   ATLATL_DART(28991, 74, 9.5F),
   RUNE_ARROW(892, 75, 12.5F),
   AMETHYST_BROAD_BOLTS(21316, 76, 10.6F),
   RUNE_JAVELIN(830, 77, 12.4F),
   DRAGON_CROSSBOW(21902, 78, 70.0F),
   MAGIC_STOCK(21952, 78, 70.0F),
   TOXIC_BLOWPIPE(12926, 78, 120.0F),
   DRAGON_CROSSBOW_U(21921, 78, 135.0F),
   GREENMAN_CARVING(31024, 79, 70.0F) {
      public String getName(ItemManager itemManager) {
         return "Greenman carving";
      }
   },
   MAGIC_SHORTBOW(861, 80, 83.3F),
   MAGIC_SHORTBOW_U(72, 80, 83.3F),
   MAGIC_VALE_TOTEM(31054, 80, 3103.6F) {
      public String getName(ItemManager itemManager) {
         return "Magic vale totem";
      }
   },
   RUNE_DART(811, 81, 18.8F),
   AMETHYST_ARROW(21326, 82, 13.5F),
   DRAGON_BOLTS(21930, 84, 12.0F),
   AMETHYST_JAVELIN(21318, 84, 13.5F),
   ROSEWOOD_BLOWPIPE(31583, 84, 200.0F),
   MAGIC_LONGBOW(859, 85, 91.5F),
   MAGIC_LONGBOW_U(70, 85, 91.5F),
   MAGIC_SHIELD(22263, 87, 183.0F),
   REDWOOD_HIKING_STAFF(31049, 90, 10.5F),
   DRAGON_ARROW(11212, 90, 15.0F),
   AMETHYST_DART(25849, 90, 21.0F),
   REDWOOD_VALE_TOTEM(31054, 90, 3787.2F) {
      public String getName(ItemManager itemManager) {
         return "Redwood vale totem";
      }
   },
   DRAGON_JAVELIN(19484, 92, 15.0F),
   REDWOOD_SHIELD(22266, 92, 216.0F),
   DRAGON_DART(11230, 95, 25.0F);

   private final int itemId;
   private final int level;
   private final float xp;

   public boolean isMembers(ItemManager itemManager) {
      return true;
   }

   private FletchingAction(int itemId, int level, float xp) {
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
