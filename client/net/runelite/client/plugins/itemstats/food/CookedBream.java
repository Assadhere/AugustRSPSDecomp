package net.runelite.client.plugins.itemstats.food;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.FoodBase;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class CookedBream extends FoodBase {
   public int heals(Client client) {
      int cooking = Stats.COOKING.getValue(client) / 3;
      int fishing = Stats.FISHING.getValue(client) / 3;
      return Math.max(7, Math.min(cooking, fishing));
   }
}
