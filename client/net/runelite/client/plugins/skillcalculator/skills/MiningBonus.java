package net.runelite.client.plugins.skillcalculator.skills;

public enum MiningBonus implements SkillBonus {
   PROSPECTOR_KIT("Prospector Kit", 1.025F);

   private final String name;
   private final float value;

   private MiningBonus(String name, float value) {
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
