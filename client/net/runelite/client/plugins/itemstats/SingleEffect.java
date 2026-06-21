package net.runelite.client.plugins.itemstats;

import net.runelite.api.Client;

public abstract class SingleEffect implements Effect {
   public final StatsChanges calculate(Client client) {
      StatsChanges c = new StatsChanges(1);
      StatChange[] statChanges = c.getStatChanges();
      statChanges[0] = this.effect(client);
      c.setPositivity(statChanges[0].getPositivity());
      return c;
   }

   public abstract StatChange effect(Client var1);
}
