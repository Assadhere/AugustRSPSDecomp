package net.runelite.client.plugins.itemstats;

import net.runelite.client.plugins.itemstats.delta.DeltaCalculator;
import net.runelite.client.plugins.itemstats.delta.DeltaPercentage;
import net.runelite.client.plugins.itemstats.stats.Stat;

public class Builders {
   public static Food food(int diff) {
      return food((max) -> {
         return diff;
      });
   }

   public static Food food(DeltaCalculator p) {
      return new Food(p);
   }

   public static Effect combo(SingleEffect... effect) {
      return new Combo(effect);
   }

   public static SimpleStatBoost boost(Stat stat, int boost) {
      return boost(stat, (max) -> {
         return boost;
      });
   }

   public static SimpleStatBoost boost(Stat stat, DeltaCalculator p) {
      return new SimpleStatBoost(stat, true, p);
   }

   public static SimpleStatBoost heal(Stat stat, int boost) {
      return heal(stat, (max) -> {
         return boost;
      });
   }

   public static SimpleStatBoost heal(Stat stat, DeltaCalculator p) {
      return new SimpleStatBoost(stat, false, p);
   }

   public static SimpleStatBoost dec(Stat stat, int boost) {
      return heal(stat, -boost);
   }

   public static DeltaPercentage perc(double perc, int delta) {
      return new DeltaPercentage(perc, delta);
   }

   public static RangeStatBoost range(StatBoost a, StatBoost b) {
      return new RangeStatBoost(a, b);
   }
}
