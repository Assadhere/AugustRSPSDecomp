package net.runelite.client.plugins.skillcalculator.skills;

public enum SmithingBonus implements SkillBonus {
   GOLDSMITH_GAUNTLETS("Goldsmith Gauntlets", 2.5F);

   private final String name;
   private final float value;

   private SmithingBonus(String name, float value) {
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
