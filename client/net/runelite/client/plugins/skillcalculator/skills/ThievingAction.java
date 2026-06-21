package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;

public enum ThievingAction implements NamedSkillAction {
   MAN_OR_WOMAN("Man / Woman", 1, 8.0F, 3241),
   WINTER_SQIRKJUICE("Winter Sq'irkjuice", 1, 350.0F, 10851),
   VEGETABLE_STALL("Vegetable Stall", 2, 10.0F, 1965),
   CAKE_STALL("Cake Stall", 5, 16.0F, 1891),
   MONKEY_FOOD_STALL("Monkey Food Stall", 5, 16.0F, 1963),
   TEA_STALL("Tea Stall", 5, 16.0F, 4242),
   CRAFTING_STALL("Crafting Stall", 5, 20.0F, 5601),
   MONKEY_GENERAL_STALL("Monkey General Stall", 5, 25.0F, 1931),
   FARMER("Farmer", 10, 14.5F, 3243),
   CHEST_10_COINS("Chest (10 Coins)", 13, 7.8F, 617),
   HAM_MEMBER("H.A.M. Member", 15, 22.2F, 4297),
   SILK_STALL("Silk Stall", 20, 24.0F, 950),
   WINE_STALL("Wine Stall", 22, 27.0F, 7919),
   WARRIOR_WOMEN_OR_AL_KHARID_WARRIOR("Warrior Women / Al-Kharid Warrior", 25, 26.0F, 3245),
   FRUIT_STALL("Fruit Stall", 25, 28.5F, 464),
   SPRING_SQIRKJUICE("Spring Sq'irkjuice", 25, 1350.0F, 10848),
   SEED_STALL("Seed Stall", 27, 10.0F, 5318),
   NATURE_RUNE_CHEST("Nature Rune Chest", 28, 25.0F, 561),
   ISLE_OF_SOULS_CHEST("Isle of Souls Dungeon Chest", 28, 150.0F, 25244),
   ROGUE("Rogue", 32, 36.5F, 3247),
   RUSTY_CHEST("Rusty Chest", 33, 90.0F, 31906),
   FUR_STALL("Fur Stall", 35, 45.0F, 958),
   CAVE_GOBLIN("Cave Goblin", 36, 40.0F, 10998),
   MASTER_FARMER("Master Farmer", 38, 43.0F, 5068),
   GUARD("Guard", 40, 46.8F, 3249),
   FISH_STALL("Fish Stall", 42, 42.0F, 331),
   CHEST_50_COINS("Chest (50 Coins)", 43, 125.0F, 617),
   BEARDED_POLLNIVNIAN_BANDIT("Bearded Pollnivnian Bandit", 45, 65.0F, 6782),
   FREMENNIK_CITIZEN("Fremennik Citizen", 45, 65.0F, 3686),
   AUTUMN_SQIRKJUICE("Autumn Sq'irkjuice", 45, 2350.0F, 10850),
   CHEST_STEEL_ARROWTIPS("Chest (Steel Arrowtips)", 47, 150.0F, 41),
   CROSSBOW_STALL("Crossbow Stall", 49, 52.0F, 837),
   WALL_SAFE("Wall Safe", 50, 70.0F, 5560),
   WEALTHY_CITIZEN("Wealthy Citizen", 50, 96.0F, 28821),
   SILVER_STALL("Silver Stall", 50, 205.0F, 2355),
   DORGESH_KAAN_AVERAGE_CHEST("Dorgesh-Kaan Average Chest", 52, 200.0F, 4537),
   DESERT_BANDIT("Desert Bandit", 53, 79.4F, 4625),
   TARNISHED_CHEST("Tarnished Chest", 54, 122.5F, 2),
   KNIGHT("Knight", 55, 84.3F, 3251),
   POLLNIVNIAN_BANDIT("Pollnivnian Bandit", 55, 84.3F, 6781),
   PIRATE("Pirate", 60, 72.0F, 32897),
   STONE_CHEST("Stone Chest", 64, 280.0F, 13383),
   MAGIC_STALL("Magic Stall", 65, 90.0F, 6422),
   SPICE_STALL("Spice Stall", 65, 92.0F, 2007),
   MENAPHITE_THUG("Menaphite Thug", 65, 137.5F, 6780),
   YANILLE_WATCHMAN("Yanille Watchman", 65, 137.5F, 3253),
   SCIMITAR_STALL("Scimitar Stall", 65, 210.0F, 1325),
   SUMMER_SQIRKJUICE("Summer Sq'irkjuice", 65, 3000.0F, 10849),
   PALADIN("Paladin", 70, 131.8F, 3255),
   GNOME("Gnome", 75, 133.5F, 3257),
   GEM_STALL("Gem Stall", 75, 408.0F, 1607),
   REINFORCED_CHEST("Reinforced Chest", 76, 182.5F, 31912),
   DORGESH_KAAN_RICH_CHEST("Dorgesh-Kaan Rich Chest", 78, 650.0F, 5013),
   HERO("Hero", 80, 163.3F, 3259),
   VYRE("Vyre", 82, 306.9F, 24702),
   ORE_STALL("Ore Stall", 82, 350.0F, 451),
   ROGUES_CASTLE_CHEST("Wilderness Rogues' Chest", 84, 701.7F, 1615),
   ELF("Elf", 85, 353.3F, 6105),
   CANNONBALL_STALL("Cannonball Stall", 87, 223.0F, 2),
   TZHAAR_HUR("TzHaar-Hur", 90, 103.4F, 21278);

   private final String name;
   private final int level;
   private final float xp;
   private final int icon;

   public boolean isMembers(ItemManager itemManager) {
      return true;
   }

   private ThievingAction(String name, int level, float xp, int icon) {
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
