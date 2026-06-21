package net.runelite.client.plugins.itemstats.food;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.FoodBase;

public class Anglerfish extends FoodBase {
   public Anglerfish() {
      this.setBoost(true);
   }

   public int heals(Client client) {
      int maxHP = this.getStat().getMaximum(client);
      byte C;
      if (maxHP <= 24) {
         C = 2;
      } else if (maxHP <= 49) {
         C = 4;
      } else if (maxHP <= 74) {
         C = 6;
      } else if (maxHP <= 92) {
         C = 8;
      } else {
         C = 13;
      }

      return maxHP / 10 + C;
   }
}
