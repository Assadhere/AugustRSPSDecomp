package net.runelite.client.plugins.skillcalculator.skills;

public enum FishingBonus implements SkillBonus {
   ANGLERS_OUTFIT("Angler's Outfit", 1.025F);

   private final String name;
   private final float value;

   private FishingBonus(String name, float value) {
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
