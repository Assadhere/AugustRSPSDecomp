package net.runelite.client.plugins.itemstats.special;

import java.util.ArrayList;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.Positivity;
import net.runelite.client.plugins.itemstats.RangeStatChange;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class SpicyStew implements Effect {
   public StatsChanges calculate(Client client) {
      int redBoost = spiceBoostOf(client.getVarbitValue(1879));
      int yellowBoost = spiceBoostOf(client.getVarbitValue(1880));
      int orangeBoost = spiceBoostOf(client.getVarbitValue(1882));
      int brownBoost = spiceBoostOf(client.getVarbitValue(1881));
      List<StatChange> changes = new ArrayList();
      if (redBoost > 0) {
         changes.add(statChangeOf(Stats.ATTACK, redBoost, client));
         changes.add(statChangeOf(Stats.STRENGTH, redBoost, client));
         changes.add(statChangeOf(Stats.DEFENCE, redBoost, client));
         changes.add(statChangeOf(Stats.RANGED, redBoost, client));
         changes.add(statChangeOf(Stats.MAGIC, redBoost, client));
      }

      if (yellowBoost > 0) {
         changes.add(statChangeOf(Stats.PRAYER, yellowBoost, client));
         changes.add(statChangeOf(Stats.AGILITY, yellowBoost, client));
         changes.add(statChangeOf(Stats.THIEVING, yellowBoost, client));
         changes.add(statChangeOf(Stats.SLAYER, yellowBoost, client));
         changes.add(statChangeOf(Stats.HUNTER, yellowBoost, client));
         changes.add(statChangeOf(Stats.SAILING, yellowBoost, client));
      }

      if (orangeBoost > 0) {
         changes.add(statChangeOf(Stats.SMITHING, orangeBoost, client));
         changes.add(statChangeOf(Stats.COOKING, orangeBoost, client));
         changes.add(statChangeOf(Stats.CRAFTING, orangeBoost, client));
         changes.add(statChangeOf(Stats.FIREMAKING, orangeBoost, client));
         changes.add(statChangeOf(Stats.FLETCHING, orangeBoost, client));
         changes.add(statChangeOf(Stats.RUNECRAFT, orangeBoost, client));
         changes.add(statChangeOf(Stats.CONSTRUCTION, orangeBoost, client));
      }

      if (brownBoost > 0) {
         changes.add(statChangeOf(Stats.MINING, brownBoost, client));
         changes.add(statChangeOf(Stats.HERBLORE, brownBoost, client));
         changes.add(statChangeOf(Stats.FISHING, brownBoost, client));
         changes.add(statChangeOf(Stats.WOODCUTTING, brownBoost, client));
         changes.add(statChangeOf(Stats.FARMING, brownBoost, client));
      }

      StatsChanges changesReturn = new StatsChanges(4);
      changesReturn.setStatChanges((StatChange[])changes.toArray(new StatChange[changes.size()]));
      return changesReturn;
   }

   private static int spiceBoostOf(int spiceDoses) {
      return Math.max(0, spiceDoses * 2 - 1);
   }

   private static StatChange statChangeOf(Stat stat, int spiceBoost, Client client) {
      int currentValue = stat.getValue(client);
      int currentBase = stat.getMaximum(client);
      int currentBoost = currentValue - currentBase;
      int spiceBoostCapped = currentBoost <= 0 ? spiceBoost : Math.max(0, spiceBoost - currentBoost);
      RangeStatChange change = new RangeStatChange();
      change.setStat(stat);
      change.setMinRelative(-spiceBoost);
      change.setRelative(spiceBoostCapped);
      change.setMinTheoretical(-spiceBoost);
      change.setTheoretical(spiceBoost);
      change.setMinAbsolute(Math.max(-spiceBoost, -currentValue));
      change.setAbsolute(stat.getValue(client) + spiceBoostCapped);
      Positivity positivity;
      if (spiceBoostCapped == 0) {
         positivity = Positivity.NO_CHANGE;
      } else if (spiceBoost > spiceBoostCapped) {
         positivity = Positivity.BETTER_CAPPED;
      } else {
         positivity = Positivity.BETTER_UNCAPPED;
      }

      change.setPositivity(positivity);
      return change;
   }
}
