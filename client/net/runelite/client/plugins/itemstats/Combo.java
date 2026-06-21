package net.runelite.client.plugins.itemstats;

import net.runelite.api.Client;

public class Combo implements Effect {
   private final SingleEffect[] calcs;

   public StatsChanges calculate(Client client) {
      StatsChanges out = new StatsChanges(this.calcs.length);
      StatChange[] statChanges = out.getStatChanges();
      Positivity positivity = Positivity.NO_CHANGE;

      for(int i = 0; i < this.calcs.length; ++i) {
         statChanges[i] = this.calcs[i].effect(client);
         if (positivity.ordinal() < statChanges[i].getPositivity().ordinal()) {
            positivity = statChanges[i].getPositivity();
         }
      }

      out.setPositivity(positivity);
      return out;
   }

   public Combo(SingleEffect[] calcs) {
      this.calcs = calcs;
   }
}
