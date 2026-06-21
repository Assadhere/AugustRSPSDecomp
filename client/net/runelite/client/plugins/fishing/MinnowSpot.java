package net.runelite.client.plugins.fishing;

import java.time.Instant;
import net.runelite.api.coords.WorldPoint;

final class MinnowSpot {
   private final WorldPoint loc;
   private final Instant time;

   public WorldPoint getLoc() {
      return this.loc;
   }

   public Instant getTime() {
      return this.time;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MinnowSpot)) {
         return false;
      } else {
         MinnowSpot other = (MinnowSpot)o;
         Object this$loc = this.getLoc();
         Object other$loc = other.getLoc();
         if (this$loc == null) {
            if (other$loc != null) {
               return false;
            }
         } else if (!this$loc.equals(other$loc)) {
            return false;
         }

         Object this$time = this.getTime();
         Object other$time = other.getTime();
         if (this$time == null) {
            if (other$time != null) {
               return false;
            }
         } else if (!this$time.equals(other$time)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $loc = this.getLoc();
      result = result * 59 + ($loc == null ? 43 : $loc.hashCode());
      Object $time = this.getTime();
      result = result * 59 + ($time == null ? 43 : $time.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getLoc());
      return "MinnowSpot(loc=" + var10000 + ", time=" + String.valueOf(this.getTime()) + ")";
   }

   public MinnowSpot(WorldPoint loc, Instant time) {
      this.loc = loc;
      this.time = time;
   }
}
