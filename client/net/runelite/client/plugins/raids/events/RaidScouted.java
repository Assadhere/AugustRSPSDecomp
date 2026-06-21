package net.runelite.client.plugins.raids.events;

import net.runelite.client.plugins.raids.Raid;

public final class RaidScouted {
   private final Raid raid;
   private final boolean firstScout;

   public RaidScouted(Raid raid, boolean firstScout) {
      this.raid = raid;
      this.firstScout = firstScout;
   }

   public Raid getRaid() {
      return this.raid;
   }

   public boolean isFirstScout() {
      return this.firstScout;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RaidScouted)) {
         return false;
      } else {
         RaidScouted other = (RaidScouted)o;
         if (this.isFirstScout() != other.isFirstScout()) {
            return false;
         } else {
            Object this$raid = this.getRaid();
            Object other$raid = other.getRaid();
            if (this$raid == null) {
               if (other$raid != null) {
                  return false;
               }
            } else if (!this$raid.equals(other$raid)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isFirstScout() ? 79 : 97);
      Object $raid = this.getRaid();
      result = result * 59 + ($raid == null ? 43 : $raid.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getRaid());
      return "RaidScouted(raid=" + var10000 + ", firstScout=" + this.isFirstScout() + ")";
   }
}
