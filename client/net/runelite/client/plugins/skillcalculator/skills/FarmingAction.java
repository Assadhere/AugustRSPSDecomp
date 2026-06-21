package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;

public enum FarmingAction implements NamedSkillAction {
   POTATOES("Potatoes", 1, 8.0F, 1942),
   WINTER_SQIRK("Winter Sq'irk", 1, 30.0F, 10847),
   DOCK_LEAF("Dock Leaf", 1, 31.0F, 8183),
   FERN_BIG_PLANT("Fern (big plant)", 1, 31.0F, 8186),
   PLANT("Plant", 1, 31.0F, 8180),
   SHORT_PLANT("Short Plant", 1, 31.0F, 8189),
   SPRING_SQIRK("Spring Sq'irk", 1, 40.0F, 10844),
   AUTUMN_SQIRK("Autumn Sq'irk", 1, 50.0F, 10846),
   SUMMER_SQIRK("Summer Sq'irk", 1, 60.0F, 10845),
   BUSH("Bush", 1, 70.0F, 8187),
   LARGE_LEAF_BUSH("Large Leaf Bush", 1, 70.0F, 8190),
   SMALL_FERN("Small Fern", 1, 70.0F, 8181),
   THISTLE("Thistle", 1, 70.0F, 8184),
   FERN_SMALL_PLANT("Fern (small plant)", 1, 100.0F, 8182),
   HUGE_PLANT("Huge Plant", 1, 100.0F, 8191),
   REEDS("Reeds", 1, 100.0F, 8185),
   TALL_PLANT("Tall Plant", 1, 100.0F, 8188),
   ONIONS("Onions", 5, 10.0F, 1957),
   CABBAGES("Cabbages", 7, 10.0F, 1965),
   GUAM_LEAF("Guam Leaf", 9, 11.0F, 249),
   TOMATOES("Tomatoes", 12, 12.5F, 1982),
   MARRENTILL("Marrentill", 14, 13.5F, 251),
   OAK_TREE("Oak Tree", 15, 481.3F, 1521),
   FLAX("Flax", 18, 16.0F, 1779),
   TARROMIN("Tarromin", 19, 16.0F, 253),
   SWEETCORN("Sweetcorn", 20, 17.0F, 5986),
   GIANT_SEAWEED("Giant seaweed", 23, 21.0F, 21504),
   HARRALANDER("Harralander", 26, 21.5F, 255),
   LIMPWURT_PLANT("Limpwurt Plant", 26, 40.0F, 225),
   APPLE_TREE("Apple Tree", 27, 1221.5F, 1955),
   ELKHRON_CORAL("Elkhorn Coral", 28, 20.5F, 31481),
   GOUTWEED("Goutweed", 29, 105.0F, 3261),
   WILLOW_TREE("Willow Tree", 30, 1481.5F, 1519),
   STRAWBERRIES("Strawberries", 31, 26.0F, 5504),
   RANARR_WEED("Ranarr Weed", 32, 27.0F, 257),
   BANANA_TREE("Banana Tree", 33, 1778.5F, 1963),
   TEAK_TREE("Teak Tree", 35, 7315.0F, 6333),
   HEMP("Hemp", 37, 33.0F, 31457),
   TOADFLAX("Toadflax", 38, 34.0F, 2998),
   ORANGE_TREE("Orange Tree", 39, 2505.7F, 2108),
   CURRY_TREE("Curry Tree", 42, 2946.9F, 5970),
   IRIT_LEAF("Irit Leaf", 44, 43.0F, 259),
   MAPLE_TREE("Maple Tree", 45, 3448.4F, 1517),
   WATERMELONS("Watermelons", 47, 49.0F, 5982),
   AVANTOE("Avantoe", 50, 54.5F, 261),
   PINEAPPLE_PLANT("Pineapple Plant", 51, 4662.7F, 2114),
   PILLAR_CORAL("Pillar Coral", 52, 52.0F, 31484),
   MAHOGANY_TREE("Mahogany Tree", 55, 15783.0F, 6332),
   KWUARM("Kwuarm", 56, 69.0F, 263),
   PAPAYA_TREE("Papaya Tree", 57, 6218.4F, 5972),
   WHITE_LILY("White lily", 58, 292.0F, 22932),
   YEW_TREE("Yew Tree", 60, 7150.9F, 1515),
   SNAPE_GRASS("Snape grass", 61, 82.0F, 231),
   SNAPDRAGON("Snapdragon", 62, 87.5F, 3000),
   HUASCA("Huasca", 65, 86.5F, 30097),
   HESPORI("Hespori", 65, 12662.0F, 23044),
   CAMPHOR_TREE("Camphor Tree", 66, 17928.0F, 32904),
   CADANTINE("Cadantine", 67, 106.5F, 265),
   PALM_TREE("Palm Tree", 68, 10260.6F, 5974),
   COTTON_BOLL("Cotton Boll", 71, 72.0F, 31460),
   CALQUAT_TREE("Calquat Tree", 72, 12225.5F, 5980),
   LANTADYME("Lantadyme", 73, 134.5F, 2481),
   CRYSTAL_TREE("Crystal Tree", 74, 13366.0F, 23962),
   MAGIC_TREE("Magic Tree", 75, 13913.8F, 1513),
   UMBRAL_CORAL("Umbral Coral", 77, 136.0F, 31487),
   DWARF_WEED("Dwarf Weed", 79, 170.5F, 267),
   IRONWOOD_TREE("Ironwood Tree", 80, 20525.0F, 32907),
   DRAGONFRUIT_TREE("Dragonfruit Tree", 81, 17895.0F, 22929),
   SPIRIT_TREE("Spirit Tree", 83, 19501.3F, 6063),
   TORSTOL("Torstol", 85, 199.5F, 269),
   CELASTRUS_TREE("Celastrus Tree", 85, 14334.0F, 22935),
   REDWOOD_TREE("Redwood Tree", 90, 22680.0F, 19669),
   ROSEWOOD_TREE("Rosewood Tree", 92, 23352.0F, 32910);

   private final String name;
   private final int level;
   private final float xp;
   private final int icon;

   public boolean isMembers(ItemManager itemManager) {
      return true;
   }

   private FarmingAction(String name, int level, float xp, int icon) {
      this.name = name;
      this.level = level;
      this.xp = xp;
      this.icon = icon;
   }

   public String getName() {
      return this.name;
   }

   public int getLevel() {
      return this.level;
   }

   public float getXp() {
      return this.xp;
   }

   public int getIcon() {
      return this.icon;
   }
}
