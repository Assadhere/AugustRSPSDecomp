package net.runelite.client.plugins.itemstats.special;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.StatBoost;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class LocatorOrb extends StatBoost {
   public LocatorOrb() {
      super(Stats.HITPOINTS, false);
   }

   public int heals(Client client) {
      int current = this.getStat().getValue(client);
      return -1 * Math.max(0, Math.min(current - 1, 10));
   }
}
