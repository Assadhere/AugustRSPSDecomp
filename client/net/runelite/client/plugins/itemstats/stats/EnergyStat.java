package net.runelite.client.plugins.itemstats.stats;

import net.runelite.api.Client;

public class EnergyStat extends Stat {
   EnergyStat() {
      super("Run Energy");
   }

   public int getValue(Client client) {
      return client.getEnergy() / 100;
   }

   public int getMaximum(Client client) {
      return 100;
   }
}
