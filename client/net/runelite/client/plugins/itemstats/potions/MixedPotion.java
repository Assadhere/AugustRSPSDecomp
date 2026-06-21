package net.runelite.client.plugins.itemstats.potions;

import java.util.Comparator;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.Builders;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.Positivity;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stats;
import org.apache.commons.lang3.ArrayUtils;

public class MixedPotion implements Effect {
   private final int heal;
   @Nonnull
   private final Effect potion;

   public StatsChanges calculate(Client client) {
      StatsChanges changes = new StatsChanges(0);
      StatChange mixedPotionHpBoost = Builders.food(this.heal).effect(client);
      StatsChanges potionChanges = this.potion.calculate(client);
      int mixedPotionHitpointsHealing = mixedPotionHpBoost.getRelative();
      if (Stream.of(potionChanges.getStatChanges()).anyMatch((statChange) -> {
         return statChange.getStat() == Stats.HITPOINTS;
      })) {
         changes.setStatChanges((StatChange[])Stream.of(potionChanges.getStatChanges()).map((change) -> {
            if (change.getStat() == Stats.HITPOINTS && mixedPotionHitpointsHealing != 0 && change.getTheoretical() < 0) {
               int max = Stats.HITPOINTS.getMaximum(client);
               int absolute = change.getAbsolute();
               int relative = change.getRelative();
               if (absolute + mixedPotionHitpointsHealing > max) {
                  change.setPositivity(Positivity.BETTER_CAPPED);
               } else if (relative + mixedPotionHitpointsHealing > 0) {
                  change.setPositivity(Positivity.BETTER_UNCAPPED);
               } else if (relative + mixedPotionHitpointsHealing == 0) {
                  change.setPositivity(Positivity.NO_CHANGE);
               } else {
                  change.setPositivity(Positivity.WORSE);
               }

               change.setAbsolute(Math.min(max, absolute + mixedPotionHitpointsHealing));
               change.setRelative(change.getRelative() + mixedPotionHitpointsHealing);
               change.setTheoretical(change.getTheoretical() + mixedPotionHitpointsHealing);
               return change;
            } else {
               return change;
            }
         }).toArray((x$0) -> {
            return new StatChange[x$0];
         }));
      } else {
         changes.setStatChanges((StatChange[])ArrayUtils.addAll(new StatChange[]{mixedPotionHpBoost}, potionChanges.getStatChanges()));
      }

      changes.setPositivity((Positivity)Stream.of(changes.getStatChanges()).map(StatChange::getPositivity).max(Comparator.naturalOrder()).get());
      return changes;
   }

   public MixedPotion(int heal, @Nonnull Effect potion) {
      if (potion == null) {
         throw new NullPointerException("potion is marked non-null but is null");
      } else {
         this.heal = heal;
         this.potion = potion;
      }
   }
}
