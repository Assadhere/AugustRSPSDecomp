package net.runelite.client.plugins.itemstats.potions;

import java.util.Comparator;
import java.util.stream.Stream;
import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.BoostedStatBoost;
import net.runelite.client.plugins.itemstats.Builders;
import net.runelite.client.plugins.itemstats.CappedStatBoost;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.Positivity;
import net.runelite.client.plugins.itemstats.SimpleStatBoost;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class AncientBrew implements Effect {
   private static final Stat[] LOWERED_STATS;
   private static final CappedStatBoost PRAYER_BOOST;
   private static final BoostedStatBoost MELEE_DRAIN;
   private final double magicBoostPercent;
   private final int magicBoostAmount;

   public StatsChanges calculate(Client client) {
      SimpleStatBoost magic = new SimpleStatBoost(Stats.MAGIC, true, Builders.perc(this.magicBoostPercent, this.magicBoostAmount));
      StatsChanges changes = new StatsChanges(0);
      changes.setStatChanges((StatChange[])((Stream)Stream.of(Stream.of(PRAYER_BOOST.effect(client)), Stream.of(magic.effect(client)), Stream.of(LOWERED_STATS).filter((stat) -> {
         return 0 < stat.getValue(client);
      }).map((stat) -> {
         MELEE_DRAIN.setStat(stat);
         return MELEE_DRAIN.effect(client);
      })).reduce(Stream::concat).orElseGet(Stream::empty)).toArray((x$0) -> {
         return new StatChange[x$0];
      }));
      changes.setPositivity((Positivity)Stream.of(changes.getStatChanges()).map(StatChange::getPositivity).max(Comparator.naturalOrder()).get());
      return changes;
   }

   public AncientBrew(double magicBoostPercent, int magicBoostAmount) {
      this.magicBoostPercent = magicBoostPercent;
      this.magicBoostAmount = magicBoostAmount;
   }

   static {
      LOWERED_STATS = new Stat[]{Stats.ATTACK, Stats.STRENGTH, Stats.DEFENCE};
      PRAYER_BOOST = new CappedStatBoost(Stats.PRAYER, Builders.perc(0.1, 2), Builders.perc(0.05, 0));
      MELEE_DRAIN = new BoostedStatBoost((Stat)null, false, Builders.perc(0.1, -2));
   }
}
