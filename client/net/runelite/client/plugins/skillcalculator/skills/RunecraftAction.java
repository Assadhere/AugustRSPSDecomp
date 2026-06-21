package net.runelite.client.plugins.skillcalculator.skills;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import net.runelite.client.game.ItemManager;

public enum RunecraftAction implements ItemSkillAction {
   AIR_RUNE(556, 1, 5.0F, false),
   AIR_TIARA(5527, 1, 25.0F, true),
   MIND_TIARA(5529, 1, 27.5F, true),
   WATER_TIARA(5531, 1, 30.0F, true),
   EARTH_TIARA(5535, 1, 32.5F, true),
   FIRE_TIARA(5537, 1, 35.0F, true),
   BODY_TIARA(5533, 1, 37.5F, true),
   COSMIC_TIARA(5539, 1, 40.0F, true),
   CHAOS_TIARA(5543, 1, 42.5F, true),
   NATURE_TIARA(5541, 1, 45.0F, true),
   LAW_TIARA(5545, 1, 47.5F, true),
   DEATH_TIARA(5547, 1, 50.0F, true),
   WRATH_TIARA(22121, 1, 52.5F, true),
   MIND_RUNE(558, 2, 5.5F, false),
   MIND_CORE(25696, 2, 55.0F, true),
   WATER_RUNE(555, 5, 6.0F, false),
   MIST_RUNE(4695, 6, 8.5F, false),
   EARTH_RUNE(557, 9, 6.5F, false),
   DUST_RUNE(4696, 10, 9.0F, false),
   MUD_RUNE(4698, 13, 9.5F, false),
   FIRE_RUNE(554, 14, 7.0F, false),
   SMOKE_RUNE(4697, 15, 9.5F, false),
   STEAM_RUNE(4694, 19, 10.0F, false),
   BODY_RUNE(559, 20, 7.5F, false),
   BODY_CORE(25698, 20, 75.0F, true),
   LAVA_RUNE(4699, 23, 10.5F, false),
   COSMIC_RUNE(564, 27, 8.0F, false, true),
   SUNFIRE_RUNE(28929, 33, 9.0F, false),
   CHAOS_RUNE(562, 35, 8.5F, false, true),
   CHAOS_CORE(25700, 35, 85.0F, true),
   ASTRAL_RUNE(9075, 40, 8.7F, false),
   NATURE_RUNE(561, 44, 9.0F, false, true),
   LAW_RUNE(563, 54, 9.5F, false, true),
   DEATH_RUNE(560, 65, 10.0F, false, true),
   TRUE_BLOOD_RUNE(565, 77, 10.5F, false) {
      public String getName(ItemManager itemManager) {
         return "Blood rune (True Altar)";
      }
   },
   ZEAH_BLOOD_RUNE(565, 77, 24.425F, true) {
      public String getName(ItemManager itemManager) {
         return "Blood rune (Zeah)";
      }
   },
   AETHER_RUNE(30843, 90, 20.0F, false),
   SOUL_RUNE(566, 90, 30.325F, true),
   WRATH_RUNE(21880, 95, 8.0F, false);

   private static final Set<RunecraftBonus> RUNECRAFT_BONUSES = EnumSet.allOf(RunecraftBonus.class);
   private final int itemId;
   private final int level;
   private final float xp;
   private final boolean ignoreBonus;
   private final boolean isMembersOverride;

   private RunecraftAction(int itemId, int level, float xp, boolean ignoreBonus) {
      this(itemId, level, xp, ignoreBonus, false);
   }

   public Set<RunecraftBonus> getExcludedSkillBonuses() {
      return this.ignoreBonus ? RUNECRAFT_BONUSES : Collections.emptySet();
   }

   private RunecraftAction(int itemId, int level, float xp, boolean ignoreBonus, boolean isMembersOverride) {
      this.itemId = itemId;
      this.level = level;
      this.xp = xp;
      this.ignoreBonus = ignoreBonus;
      this.isMembersOverride = isMembersOverride;
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

   public boolean isIgnoreBonus() {
      return this.ignoreBonus;
   }

   public boolean isMembersOverride() {
      return this.isMembersOverride;
   }
}
