package net.runelite.client.plugins.itemstats.special;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.plugins.itemstats.StatBoost;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class NettleTeaRunEnergy extends StatBoost {
   public NettleTeaRunEnergy() {
      super(Stats.RUN_ENERGY, false);
   }

   public int heals(Client client) {
      return client.getBoostedSkillLevel(Skill.HITPOINTS) < client.getRealSkillLevel(Skill.HITPOINTS) ? 5 : 0;
   }
}
