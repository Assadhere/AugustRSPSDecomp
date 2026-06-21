package net.runelite.client.plugins.itemstats.stats;

import net.runelite.api.Skill;

public class Stats {
   public static final Stat ATTACK;
   public static final Stat DEFENCE;
   public static final Stat STRENGTH;
   public static final Stat HITPOINTS;
   public static final Stat RANGED;
   public static final Stat PRAYER;
   public static final Stat MAGIC;
   public static final Stat COOKING;
   public static final Stat WOODCUTTING;
   public static final Stat FLETCHING;
   public static final Stat FISHING;
   public static final Stat FIREMAKING;
   public static final Stat CRAFTING;
   public static final Stat SMITHING;
   public static final Stat MINING;
   public static final Stat HERBLORE;
   public static final Stat AGILITY;
   public static final Stat THIEVING;
   public static final Stat SLAYER;
   public static final Stat FARMING;
   public static final Stat RUNECRAFT;
   public static final Stat HUNTER;
   public static final Stat CONSTRUCTION;
   public static final Stat SAILING;
   public static final Stat RUN_ENERGY;

   static {
      ATTACK = new SkillStat(Skill.ATTACK);
      DEFENCE = new SkillStat(Skill.DEFENCE);
      STRENGTH = new SkillStat(Skill.STRENGTH);
      HITPOINTS = new SkillStat(Skill.HITPOINTS);
      RANGED = new SkillStat(Skill.RANGED);
      PRAYER = new SkillStat(Skill.PRAYER);
      MAGIC = new SkillStat(Skill.MAGIC);
      COOKING = new SkillStat(Skill.COOKING);
      WOODCUTTING = new SkillStat(Skill.WOODCUTTING);
      FLETCHING = new SkillStat(Skill.FLETCHING);
      FISHING = new SkillStat(Skill.FISHING);
      FIREMAKING = new SkillStat(Skill.FIREMAKING);
      CRAFTING = new SkillStat(Skill.CRAFTING);
      SMITHING = new SkillStat(Skill.SMITHING);
      MINING = new SkillStat(Skill.MINING);
      HERBLORE = new SkillStat(Skill.HERBLORE);
      AGILITY = new SkillStat(Skill.AGILITY);
      THIEVING = new SkillStat(Skill.THIEVING);
      SLAYER = new SkillStat(Skill.SLAYER);
      FARMING = new SkillStat(Skill.FARMING);
      RUNECRAFT = new SkillStat(Skill.RUNECRAFT);
      HUNTER = new SkillStat(Skill.HUNTER);
      CONSTRUCTION = new SkillStat(Skill.CONSTRUCTION);
      SAILING = new SkillStat(Skill.SAILING);
      RUN_ENERGY = new EnergyStat();
   }
}
