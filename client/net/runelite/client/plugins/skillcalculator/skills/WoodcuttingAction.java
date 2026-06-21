package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;

public enum WoodcuttingAction implements ItemSkillAction {
   ACHEY_TREE_LOGS(2862, 1, 25.0F),
   LOGS(1511, 1, 25.0F),
   OAK_LOGS(1521, 15, 37.5F),
   WILLOW_LOGS(1519, 30, 67.5F),
   TEAK_LOGS(6333, 35, 85.0F),
   JATOBA_lOGS(32902, 40, 92.0F),
   JUNIPER_LOGS(13355, 42, 35.0F),
   BARK(3239, 45, 82.5F),
   MAPLE_LOGS(1517, 45, 100.0F),
   MAHOGANY_LOGS(6332, 50, 125.0F),
   ARCTIC_PINE_LOGS(10810, 54, 40.0F),
   YEW_LOGS(1515, 60, 175.0F),
   BLISTERWOOD_LOGS(24691, 62, 76.0F),
   SULLIUSCEPS(21626, 65, 127.0F) {
      public String getName(ItemManager itemManager) {
         return "Sulliusceps";
      }
   },
   CAMPHOR_LOGS(32904, 66, 143.5F),
   MAGIC_LOGS(1513, 75, 250.0F),
   IRONWOOD_LOGS(32907, 80, 175.0F),
   REDWOOD_LOGS(19669, 90, 380.0F),
   ROSEWOOD_LOGS(32910, 92, 212.5F);

   private final int itemId;
   private final int level;
   private final float xp;

   private WoodcuttingAction(int itemId, int level, float xp) {
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
