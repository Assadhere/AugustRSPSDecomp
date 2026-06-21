package net.runelite.client.plugins.itemstats.delta;

public class DeltaPercentage implements DeltaCalculator {
   private final double perc;
   private final int delta;

   public int calculateDelta(int max) {
      return (int)((double)max * this.perc) * (this.delta >= 0 ? 1 : -1) + this.delta;
   }

   public DeltaPercentage(double perc, int delta) {
      this.perc = perc;
      this.delta = delta;
   }
}
