package net.runelite.client.plugins.itemstats.special;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.plugins.itemstats.Builders;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class DwarvenRockCake implements Effect {
   public StatsChanges calculate(Client client) {
      int current = client.getBoostedSkillLevel(Skill.HITPOINTS);
      int eat = current <= 1 ? 0 : -1;
      int guzzle = current <= 1 ? 0 : -1 * (current / 10 + 1);
      StatChange eatChange = Builders.heal(Stats.HITPOINTS, eat).effect(client);
      StatChange guzzleChange = Builders.heal(Stats.HITPOINTS, guzzle).effect(client);
      StatsChanges changes = new StatsChanges(2);
      changes.setStatChanges(new StatChange[]{eatChange, guzzleChange});
      return changes;
   }
}
