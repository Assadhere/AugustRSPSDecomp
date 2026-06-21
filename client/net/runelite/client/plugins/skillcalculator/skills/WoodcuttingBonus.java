package net.runelite.client.plugins.skillcalculator.skills;

import java.util.EnumSet;
import java.util.Set;

public enum WoodcuttingBonus implements SkillBonus {
   LUMBERJACK_OUTFIT("Lumberjack Outfit", 1.025F),
   FELLING_AXE_RATIONS("Felling Axe + Rations", 1.1F);

   private final String name;
   private final float value;

   public Set<WoodcuttingBonus> getCanBeStackedWith() {
      EnumSet<WoodcuttingBonus> others = EnumSet.allOf(WoodcuttingBonus.class);
      others.remove(this);
      return others;
   }

   private WoodcuttingBonus(String name, float value) {
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
