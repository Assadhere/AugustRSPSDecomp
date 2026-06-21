package net.runelite.client.plugins.itemstats.potions;

import com.google.common.annotations.VisibleForTesting;
import java.util.Comparator;
import java.util.stream.Stream;
import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.Builders;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.Positivity;
import net.runelite.client.plugins.itemstats.SimpleStatBoost;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class SuperRestore implements Effect {
   private static final Stat[] superRestoreStats;
   @VisibleForTesting
   public final double percR;
   private final int delta;

   public StatsChanges calculate(Client client) {
      StatsChanges changes = new StatsChanges(0);
      SimpleStatBoost calc = new SimpleStatBoost((Stat)null, false, Builders.perc(this.percR, this.delta));
      PrayerPotion prayer = new PrayerPotion(this.delta, this.percR);
      changes.setStatChanges((StatChange[])Stream.concat(Stream.of(prayer.effect(client)), Stream.of(superRestoreStats).filter((stat) -> {
         return stat.getValue(client) < stat.getMaximum(client);
      }).map((stat) -> {
         calc.setStat(stat);
         return calc.effect(client);
      })).toArray((x$0) -> {
         return new StatChange[x$0];
      }));
      changes.setPositivity((Positivity)Stream.of(changes.getStatChanges()).map((sc) -> {
         return sc.getPositivity();
      }).max(Comparator.naturalOrder()).get());
      return changes;
   }

   public SuperRestore(double percR, int delta) {
      this.percR = percR;
      this.delta = delta;
   }

   static {
      superRestoreStats = new Stat[]{Stats.ATTACK, Stats.DEFENCE, Stats.STRENGTH, Stats.RANGED, Stats.MAGIC, Stats.COOKING, Stats.WOODCUTTING, Stats.FLETCHING, Stats.FISHING, Stats.FIREMAKING, Stats.CRAFTING, Stats.SMITHING, Stats.MINING, Stats.HERBLORE, Stats.AGILITY, Stats.THIEVING, Stats.SLAYER, Stats.FARMING, Stats.RUNECRAFT, Stats.HUNTER, Stats.CONSTRUCTION};
   }
}
