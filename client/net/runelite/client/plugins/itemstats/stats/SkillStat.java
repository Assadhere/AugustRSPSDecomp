package net.runelite.client.plugins.itemstats.stats;

import net.runelite.api.Client;
import net.runelite.api.Skill;

public class SkillStat extends Stat {
   private final Skill skill;

   SkillStat(Skill skill) {
      super(skill.getName());
      this.skill = skill;
   }

   public int getValue(Client client) {
      return client.getBoostedSkillLevel(this.skill);
   }

   public int getMaximum(Client client) {
      return client.getRealSkillLevel(this.skill);
   }
}
