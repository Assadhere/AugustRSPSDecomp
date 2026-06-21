package net.runelite.client.plugins.itemstats.potions;

import java.util.Comparator;
import java.util.stream.Stream;
import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.BoostedStatBoost;
import net.runelite.client.plugins.itemstats.Builders;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.Positivity;
import net.runelite.client.plugins.itemstats.SimpleStatBoost;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class SaradominBrew implements Effect {
   private static final Stat[] saradominBrewStats;
   private final double percH;
   private final double percD;
   private final double percSD;
   private final int deltaB;
   private final int deltaR;

   public StatsChanges calculate(Client client) {
      StatsChanges changes = new StatsChanges(0);
      SimpleStatBoost hitpoints = new SimpleStatBoost(Stats.HITPOINTS, true, Builders.perc(this.percH, this.deltaB));
      SimpleStatBoost defence = new SimpleStatBoost(Stats.DEFENCE, true, Builders.perc(this.percD, this.deltaB));
      BoostedStatBoost calc = new BoostedStatBoost((Stat)null, false, Builders.perc(this.percSD, -this.deltaR));
      changes.setStatChanges((StatChange[])((Stream)Stream.of(Stream.of(hitpoints.effect(client)), Stream.of(defence.effect(client)), Stream.of(saradominBrewStats).filter((stat) -> {
         return 1 < stat.getValue(client);
      }).map((stat) -> {
         calc.setStat(stat);
         return calc.effect(client);
      })).reduce(Stream::concat).orElseGet(Stream::empty)).toArray((x$0) -> {
         return new StatChange[x$0];
      }));
      changes.setPositivity((Positivity)Stream.of(changes.getStatChanges()).map(StatChange::getPositivity).max(Comparator.naturalOrder()).get());
      return changes;
   }

   public SaradominBrew(double percH, double percD, double percSD, int deltaB, int deltaR) {
      this.percH = percH;
      this.percD = percD;
      this.percSD = percSD;
      this.deltaB = deltaB;
      this.deltaR = deltaR;
   }

   static {
      saradominBrewStats = new Stat[]{Stats.ATTACK, Stats.STRENGTH, Stats.RANGED, Stats.MAGIC};
   }
}
