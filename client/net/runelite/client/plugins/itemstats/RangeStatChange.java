package net.runelite.client.plugins.itemstats;

public class RangeStatChange extends StatChange {
   private int minRelative;
   private int minTheoretical;
   private int minAbsolute;

   public String getFormattedRelative() {
      return concat(this.minRelative, this.getRelative());
   }

   public String getFormattedTheoretical() {
      return concat(this.minTheoretical, this.getTheoretical());
   }

   private static String concat(int changeA, int changeB) {
      if (changeA == changeB) {
         return formatBoost(changeA);
      } else if (changeA * -1 == changeB) {
         return "±" + Math.abs(changeA);
      } else {
         StringBuilder sb = new StringBuilder();
         sb.append(String.format("%+d", changeA));
         sb.append('~');
         if ((changeA >= 0 || changeB >= 0) && (changeA < 0 || changeB < 0)) {
            sb.append(String.format("%+d", changeB));
         } else {
            sb.append(Math.abs(changeB));
         }

         return sb.toString();
      }
   }

   public int getMinRelative() {
      return this.minRelative;
   }

   public int getMinTheoretical() {
      return this.minTheoretical;
   }

   public int getMinAbsolute() {
      return this.minAbsolute;
   }

   public void setMinRelative(int minRelative) {
      this.minRelative = minRelative;
   }

   public void setMinTheoretical(int minTheoretical) {
      this.minTheoretical = minTheoretical;
   }

   public void setMinAbsolute(int minAbsolute) {
      this.minAbsolute = minAbsolute;
   }

   public String toString() {
      int var10000 = this.getMinRelative();
      return "RangeStatChange(minRelative=" + var10000 + ", minTheoretical=" + this.getMinTheoretical() + ", minAbsolute=" + this.getMinAbsolute() + ")";
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RangeStatChange)) {
         return false;
      } else {
         RangeStatChange other = (RangeStatChange)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else if (this.getMinRelative() != other.getMinRelative()) {
            return false;
         } else if (this.getMinTheoretical() != other.getMinTheoretical()) {
            return false;
         } else {
            return this.getMinAbsolute() == other.getMinAbsolute();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof RangeStatChange;
   }

   public int hashCode() {
      int PRIME = true;
      int result = super.hashCode();
      result = result * 59 + this.getMinRelative();
      result = result * 59 + this.getMinTheoretical();
      result = result * 59 + this.getMinAbsolute();
      return result;
   }
}
