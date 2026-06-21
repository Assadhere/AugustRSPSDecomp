package net.runelite.client.plugins.itemstats;

public class StatsChanges {
   private Positivity positivity;
   private StatChange[] statChanges;

   public StatsChanges(int len) {
      this.statChanges = new StatChange[len];
      this.positivity = Positivity.NO_CHANGE;
   }

   public Positivity getPositivity() {
      return this.positivity;
   }

   public void setPositivity(Positivity positivity) {
      this.positivity = positivity;
   }

   public StatChange[] getStatChanges() {
      return this.statChanges;
   }

   public void setStatChanges(StatChange[] statChanges) {
      this.statChanges = statChanges;
   }
}
