package net.runelite.client.plugins.herbiboars;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

final class TrailToSpot {
   private final int varbitId;
   private final int value;
   private final int footprint;

   Set<Integer> getFootprintIds() {
      return ImmutableSet.of(this.footprint, this.footprint + 1);
   }

   public TrailToSpot(int varbitId, int value, int footprint) {
      this.varbitId = varbitId;
      this.value = value;
      this.footprint = footprint;
   }

   public int getValue() {
      return this.value;
   }

   public int getFootprint() {
      return this.footprint;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TrailToSpot)) {
         return false;
      } else {
         TrailToSpot other = (TrailToSpot)o;
         if (this.getVarbitId() != other.getVarbitId()) {
            return false;
         } else if (this.getValue() != other.getValue()) {
            return false;
         } else {
            return this.getFootprint() == other.getFootprint();
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getVarbitId();
      result = result * 59 + this.getValue();
      result = result * 59 + this.getFootprint();
      return result;
   }

   public String toString() {
      int var10000 = this.getVarbitId();
      return "TrailToSpot(varbitId=" + var10000 + ", value=" + this.getValue() + ", footprint=" + this.getFootprint() + ")";
   }

   public int getVarbitId() {
      return this.varbitId;
   }
}
