package net.runelite.client.plugins.itemstats.special;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.StatBoost;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class CaveNightshade extends StatBoost {
   public CaveNightshade() {
      super(Stats.HITPOINTS, false);
   }

   public int heals(Client client) {
      int currentHP = this.getStat().getValue(client);
      return currentHP < 20 ? -currentHP / 2 : -15;
   }
}
