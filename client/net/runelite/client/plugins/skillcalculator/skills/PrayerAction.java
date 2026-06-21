package net.runelite.client.plugins.skillcalculator.skills;

import java.util.EnumSet;
import java.util.Set;

public enum PrayerAction implements ItemSkillAction {
   GUPPY(25654, 1, 4.0F, PrayerAction.PrayerMethod.PREPARED_FISH),
   BONES(526, 1, 4.5F, PrayerAction.PrayerMethod.BONES),
   BURNT_BONES(528, 1, 4.5F, PrayerAction.PrayerMethod.BONES),
   WOLF_BONES(2859, 1, 4.5F, PrayerAction.PrayerMethod.BONES),
   MONKEY_BONES(3183, 1, 5.0F, PrayerAction.PrayerMethod.BONES),
   BAT_BONES(530, 1, 5.3F, PrayerAction.PrayerMethod.BONES),
   CAVEFISH(25660, 1, 7.0F, PrayerAction.PrayerMethod.PREPARED_FISH),
   FIENDISH_ASHES(25766, 1, 10.0F, PrayerAction.PrayerMethod.DEMONIC_ASHES),
   TETRA(25666, 1, 10.0F, PrayerAction.PrayerMethod.PREPARED_FISH),
   BIG_BONES(532, 1, 15.0F, PrayerAction.PrayerMethod.BONES),
   JOGRE_BONES(3125, 1, 15.0F, PrayerAction.PrayerMethod.BONES),
   CATFISH(25672, 1, 16.0F, PrayerAction.PrayerMethod.PREPARED_FISH),
   WYRMLING_BONES(28899, 1, 21.0F, PrayerAction.PrayerMethod.BONES),
   ZOGRE_BONES(4812, 1, 22.5F, PrayerAction.PrayerMethod.BONES),
   SHAIKAHAN_BONES(3123, 1, 25.0F, PrayerAction.PrayerMethod.BONES),
   VILE_ASHES(25769, 1, 25.0F, PrayerAction.PrayerMethod.DEMONIC_ASHES),
   BABYDRAGON_BONES(534, 1, 30.0F, PrayerAction.PrayerMethod.BONES),
   LOAR_REMAINS(3396, 1, 33.0F, PrayerAction.PrayerMethod.SHADE_REMAINS),
   PHRIN_REMAINS(3398, 1, 46.5F, PrayerAction.PrayerMethod.SHADE_REMAINS),
   WYRM_BONES(22780, 1, 50.0F, PrayerAction.PrayerMethod.BONES),
   RIYL_REMAINS(3400, 1, 59.5F, PrayerAction.PrayerMethod.SHADE_REMAINS),
   STRYKEWYRM_BONES(31726, 1, 60.0F, PrayerAction.PrayerMethod.BONES),
   MALICIOUS_ASHES(25772, 1, 65.0F, PrayerAction.PrayerMethod.DEMONIC_ASHES),
   DRAGON_BONES(536, 1, 72.0F, PrayerAction.PrayerMethod.BONES),
   WYVERN_BONES(6812, 1, 72.0F, PrayerAction.PrayerMethod.BONES),
   DRAKE_BONES(22783, 1, 80.0F, PrayerAction.PrayerMethod.BONES),
   ASYN_REMAINS(3402, 1, 82.5F, PrayerAction.PrayerMethod.SHADE_REMAINS),
   FAYRG_BONES(4830, 1, 84.0F, PrayerAction.PrayerMethod.BONES),
   FIYR_REMAINS(3404, 1, 84.0F, PrayerAction.PrayerMethod.SHADE_REMAINS),
   ABYSSAL_ASHES(25775, 1, 85.0F, PrayerAction.PrayerMethod.DEMONIC_ASHES),
   LAVA_DRAGON_BONES(11943, 1, 85.0F, PrayerAction.PrayerMethod.BONES),
   RAURG_BONES(4832, 1, 96.0F, PrayerAction.PrayerMethod.BONES),
   FROST_DRAGON_BONES(31729, 1, 100.0F, PrayerAction.PrayerMethod.BONES),
   HYDRA_BONES(22786, 1, 110.0F, PrayerAction.PrayerMethod.BONES),
   INFERNAL_ASHES(25778, 1, 110.0F, PrayerAction.PrayerMethod.DEMONIC_ASHES),
   URIUM_REMAINS(25419, 1, 120.5F, PrayerAction.PrayerMethod.SHADE_REMAINS),
   DAGANNOTH_BONES(6729, 1, 125.0F, PrayerAction.PrayerMethod.BONES),
   ENSOULED_GOBLIN_HEAD(13447, 1, 130.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   OURG_BONES(4834, 1, 140.0F, PrayerAction.PrayerMethod.BONES),
   ENSOULED_MONKEY_HEAD(13450, 1, 182.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_IMP_HEAD(13453, 1, 286.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_MINOTAUR_HEAD(13456, 1, 364.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_SCORPION_HEAD(13459, 1, 454.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_BEAR_HEAD(13462, 1, 480.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_UNICORN_HEAD(13465, 1, 494.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_DOG_HEAD(13468, 1, 520.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_CHAOS_DRUID_HEAD(13471, 1, 584.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_GIANT_HEAD(13474, 1, 650.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_OGRE_HEAD(13477, 1, 716.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_ELF_HEAD(13480, 1, 754.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_TROLL_HEAD(13483, 1, 780.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_HORROR_HEAD(13486, 1, 832.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_KALPHITE_HEAD(13489, 1, 884.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_DAGANNOTH_HEAD(13492, 1, 936.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_BLOODVELD_HEAD(13495, 1, 1040.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_TZHAAR_HEAD(13498, 1, 1104.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_DEMON_HEAD(13501, 1, 1170.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_HELLHOUND_HEAD(26996, 1, 1200.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_AVIANSIE_HEAD(13504, 1, 1234.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_ABYSSAL_HEAD(13507, 1, 1300.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   ENSOULED_DRAGON_HEAD(13510, 1, 1560.0F, PrayerAction.PrayerMethod.ENSOULED_HEAD),
   BLESSED_BONE_SHARDS(29381, 30, 5.0F, PrayerAction.PrayerMethod.BLESSED_SUNFIRE_WINE),
   SUPERIOR_DRAGON_BONES(22124, 70, 150.0F, PrayerAction.PrayerMethod.BONES);

   private static final Set<PrayerBonus> EXCLUDED_BONUSES_FOR_BONES = EnumSet.of(PrayerBonus.MORYTANIA_DIARY_3_SHADES, PrayerBonus.DEMONIC_OFFERING, PrayerBonus.BLESSED_SUNFIRE_WINE);
   private static final Set<PrayerBonus> EXCLUDED_BONUSES_FOR_ASHES = EnumSet.complementOf(EnumSet.of(PrayerBonus.DEMONIC_OFFERING, PrayerBonus.SACRED_BONE_BURNER));
   private static final Set<PrayerBonus> EXCLUDED_BONUSES_FOR_REMAINS = EnumSet.complementOf(EnumSet.of(PrayerBonus.MORYTANIA_DIARY_3_SHADES));
   private static final Set<PrayerBonus> EXCLUDED_BONUSES_FOR_BLESSED_SUNFIRE_WINE = EnumSet.complementOf(EnumSet.of(PrayerBonus.ZEALOT_ROBES, PrayerBonus.BLESSED_SUNFIRE_WINE));
   private static final Set<PrayerBonus> EXCLUDE_ALL_EXCEPT_ZEALOT_ROBES = EnumSet.complementOf(EnumSet.of(PrayerBonus.ZEALOT_ROBES));
   private final int itemId;
   private final int level;
   private final float xp;
   private final PrayerMethod methodType;

   public Set<PrayerBonus> getExcludedSkillBonuses() {
      switch (this.getMethodType()) {
         case BONES:
            return EXCLUDED_BONUSES_FOR_BONES;
         case DEMONIC_ASHES:
            return EXCLUDED_BONUSES_FOR_ASHES;
         case ENSOULED_HEAD:
         case PREPARED_FISH:
            return EXCLUDE_ALL_EXCEPT_ZEALOT_ROBES;
         case SHADE_REMAINS:
            return EXCLUDED_BONUSES_FOR_REMAINS;
         case BLESSED_SUNFIRE_WINE:
            return EXCLUDED_BONUSES_FOR_BLESSED_SUNFIRE_WINE;
         default:
            return EnumSet.allOf(PrayerBonus.class);
      }
   }

   private PrayerAction(int itemId, int level, float xp, PrayerMethod methodType) {
      this.itemId = itemId;
      this.level = level;
      this.xp = xp;
      this.methodType = methodType;
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

   public PrayerMethod getMethodType() {
      return this.methodType;
   }

   private static enum PrayerMethod {
      BONES,
      DEMONIC_ASHES,
      ENSOULED_HEAD,
      PREPARED_FISH,
      SHADE_REMAINS,
      BLESSED_SUNFIRE_WINE;
   }
}
