package net.runelite.client.plugins.skillcalculator.skills;

import java.util.EnumSet;
import java.util.Set;

public enum PrayerBonus implements SkillBonus {
   BONECRUSHER("Bonecrusher", 0.5F),
   SACRED_BONE_BURNER("Sacred Bone Burner", 3.0F),
   SINISTER_OFFERING("Sinister Offering", 3.0F),
   LIT_GILDED_ALTAR("Lit Gilded Altar", 3.5F),
   ECTOFUNTUS("Ectofuntus", 4.0F),
   CHAOS_ALTAR("Chaos Altar", 7.0F),
   BLESSED_SUNFIRE_WINE("Blessed Sunfire Wine", 1.2F),
   DEMONIC_OFFERING("Demonic Offering", 3.0F),
   MORYTANIA_DIARY_3_SHADES("Morytania Diary 3 Shades", 1.5F),
   ZEALOT_ROBES("Zealot Robes", 1.05F);

   private final String name;
   private final float value;

   public Set<PrayerBonus> getCanBeStackedWith() {
      switch (this) {
         case ECTOFUNTUS:
         case LIT_GILDED_ALTAR:
         case CHAOS_ALTAR:
         case SINISTER_OFFERING:
            return EnumSet.complementOf(EnumSet.of(ECTOFUNTUS, LIT_GILDED_ALTAR, CHAOS_ALTAR, SINISTER_OFFERING, SACRED_BONE_BURNER, BONECRUSHER));
         case BONECRUSHER:
         case SACRED_BONE_BURNER:
            return EnumSet.complementOf(EnumSet.of(ECTOFUNTUS, LIT_GILDED_ALTAR, CHAOS_ALTAR, SINISTER_OFFERING, SACRED_BONE_BURNER, BONECRUSHER, ZEALOT_ROBES));
         case ZEALOT_ROBES:
            return EnumSet.complementOf(EnumSet.of(BONECRUSHER, SACRED_BONE_BURNER, ZEALOT_ROBES));
         default:
            return EnumSet.complementOf(EnumSet.of(this));
      }
   }

   private PrayerBonus(String name, float value) {
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
