package net.runelite.client.plugins.itemstats;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.delta.DeltaCalculator;
import net.runelite.client.plugins.itemstats.stats.Stat;

public class CappedStatBoost extends StatBoost {
   private final DeltaCalculator deltaCalculator;
   private final DeltaCalculator capCalculator;

   public CappedStatBoost(Stat stat, DeltaCalculator deltaCalculator, DeltaCalculator capCalculator) {
      super(stat, true);
      this.deltaCalculator = deltaCalculator;
      this.capCalculator = capCalculator;
   }

   public int heals(Client client) {
      int current = this.getStat().getValue(client);
      int max = this.getStat().getMaximum(client);
      int delta = this.deltaCalculator.calculateDelta(max);
      int cap = this.capCalculator.calculateDelta(max);
      if (delta + current <= max + cap) {
         return delta;
      } else {
         return current > max + cap ? 0 : max + cap - current;
      }
   }
}
