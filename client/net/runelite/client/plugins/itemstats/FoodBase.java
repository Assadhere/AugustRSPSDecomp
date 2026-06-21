package net.runelite.client.plugins.itemstats;

import net.runelite.client.plugins.itemstats.stats.Stats;

public abstract class FoodBase extends StatBoost {
   public FoodBase() {
      super(Stats.HITPOINTS, false);
   }
}
