package net.runelite.client.plugins.itemstats;

import net.runelite.client.plugins.itemstats.stats.Stat;

public class StatChange {
   private Stat stat;
   private int relative;
   private int theoretical;
   private int absolute;
   private Positivity positivity;

   public String getFormattedRelative() {
      return formatBoost(this.relative);
   }

   public String getFormattedTheoretical() {
      return formatBoost(this.theoretical);
   }

   static String formatBoost(int boost) {
      return String.format("%+d", boost);
   }

   public Stat getStat() {
      return this.stat;
   }

   public int getRelative() {
      return this.relative;
   }

   public int getTheoretical() {
      return this.theoretical;
   }

   public int getAbsolute() {
      return this.absolute;
   }

   public Positivity getPositivity() {
      return this.positivity;
   }

   public void setStat(Stat stat) {
      this.stat = stat;
   }

   public void setRelative(int relative) {
      this.relative = relative;
   }

   public void setTheoretical(int theoretical) {
      this.theoretical = theoretical;
   }

   public void setAbsolute(int absolute) {
      this.absolute = absolute;
   }

   public void setPositivity(Positivity positivity) {
      this.positivity = positivity;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof StatChange)) {
         return false;
      } else {
         StatChange other = (StatChange)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getRelative() != other.getRelative()) {
            return false;
         } else if (this.getTheoretical() != other.getTheoretical()) {
            return false;
         } else if (this.getAbsolute() != other.getAbsolute()) {
            return false;
         } else {
            Object this$stat = this.getStat();
            Object other$stat = other.getStat();
            if (this$stat == null) {
               if (other$stat != null) {
                  return false;
               }
            } else if (!this$stat.equals(other$stat)) {
               return false;
            }

            Object this$positivity = this.getPositivity();
            Object other$positivity = other.getPositivity();
            if (this$positivity == null) {
               if (other$positivity != null) {
                  return false;
               }
            } else if (!this$positivity.equals(other$positivity)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof StatChange;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getRelative();
      result = result * 59 + this.getTheoretical();
      result = result * 59 + this.getAbsolute();
      Object $stat = this.getStat();
      result = result * 59 + ($stat == null ? 43 : $stat.hashCode());
      Object $positivity = this.getPositivity();
      result = result * 59 + ($positivity == null ? 43 : $positivity.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getStat());
      return "StatChange(stat=" + var10000 + ", relative=" + this.getRelative() + ", theoretical=" + this.getTheoretical() + ", absolute=" + this.getAbsolute() + ", positivity=" + String.valueOf(this.getPositivity()) + ")";
   }
}
