package net.runelite.client.plugins.skillcalculator.skills;

import java.util.EnumSet;
import java.util.Set;

public enum FiremakingBonus implements SkillBonus {
   PYROMANCER_OUTFIT("Pyromancer Outfit", 1.025F),
   MORYTANIA_ELITE_DIARY("Morytania Elite Diary", 1.5F);

   private final String name;
   private final float value;

   public Set<FiremakingBonus> getCanBeStackedWith() {
      EnumSet<FiremakingBonus> others = EnumSet.allOf(FiremakingBonus.class);
      others.remove(this);
      return others;
   }

   private FiremakingBonus(String name, float value) {
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
