package net.runelite.client.plugins.skillcalculator.skills;

public enum RunecraftBonus implements SkillBonus {
   DAEYALT_ESSENCE("Daeyalt essence", 1.5F);

   private final String name;
   private final float value;

   private RunecraftBonus(String name, float value) {
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
