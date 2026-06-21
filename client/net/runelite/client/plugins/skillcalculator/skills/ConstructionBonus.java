package net.runelite.client.plugins.skillcalculator.skills;

public enum ConstructionBonus implements SkillBonus {
   CARPENTERS_OUTFIT("Carpenter's Outfit", 1.025F);

   private final String name;
   private final float value;

   private ConstructionBonus(String name, float value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public float getValue() {
      return this.value;
   }
}
