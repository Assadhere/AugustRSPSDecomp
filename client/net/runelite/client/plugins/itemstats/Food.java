package net.runelite.client.plugins.itemstats;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.delta.DeltaCalculator;

public class Food extends FoodBase {
   private final DeltaCalculator p;

   public Food(DeltaCalculator p) {
      this.p = p;
   }

   public int heals(Client client) {
      return this.p.calculateDelta(this.getStat().getMaximum(client));
   }
}
