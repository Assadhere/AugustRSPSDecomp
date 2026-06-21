package net.runelite.api;

public enum Skill {
   ATTACK("Attack", false),
   DEFENCE("Defence", false),
   STRENGTH("Strength", false),
   HITPOINTS("Hitpoints", false),
   RANGED("Ranged", false),
   PRAYER("Prayer", false),
   MAGIC("Magic", false),
   COOKING("Cooking", false),
   WOODCUTTING("Woodcutting", false),
   FLETCHING("Fletching", true),
   FISHING("Fishing", false),
   FIREMAKING("Firemaking", false),
   CRAFTING("Crafting", false),
   SMITHING("Smithing", false),
   MINING("Mining", false),
   HERBLORE("Herblore", true),
   AGILITY("Agility", true),
   THIEVING("Thieving", true),
   SLAYER("Slayer", true),
   FARMING("Farming", true),
   RUNECRAFT("Runecraft", false),
   HUNTER("Hunter", true),
   CONSTRUCTION("Construction", true),
   SAILING("Sailing", true);

   /** @deprecated */
   @Deprecated
   public static final Skill OVERALL = null;
   private final String name;
   private final boolean members;

   private Skill(String name, boolean members) {
      this.name = name;
      this.members = members;
   }

   public String getName() {
      return this.name;
   }

   public boolean isMembers() {
      return this.members;
   }
}
