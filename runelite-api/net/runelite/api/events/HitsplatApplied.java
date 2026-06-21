package net.runelite.api.events;

import net.runelite.api.Actor;
import net.runelite.api.Hitsplat;

public class HitsplatApplied {
   private Actor actor;
   private Hitsplat hitsplat;

   public Actor getActor() {
      return this.actor;
   }

   public Hitsplat getHitsplat() {
      return this.hitsplat;
   }

   public void setActor(Actor actor) {
      this.actor = actor;
   }

   public void setHitsplat(Hitsplat hitsplat) {
      this.hitsplat = hitsplat;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HitsplatApplied)) {
         return false;
      } else {
         HitsplatApplied other = (HitsplatApplied)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$actor = this.getActor();
            Object other$actor = other.getActor();
            if (this$actor == null) {
               if (other$actor != null) {
                  return false;
               }
            } else if (!this$actor.equals(other$actor)) {
               return false;
            }

            Object this$hitsplat = this.getHitsplat();
            Object other$hitsplat = other.getHitsplat();
            if (this$hitsplat == null) {
               if (other$hitsplat != null) {
                  return false;
               }
            } else if (!this$hitsplat.equals(other$hitsplat)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof HitsplatApplied;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $actor = this.getActor();
      result = result * 59 + ($actor == null ? 43 : $actor.hashCode());
      Object $hitsplat = this.getHitsplat();
      result = result * 59 + ($hitsplat == null ? 43 : $hitsplat.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getActor());
      return "HitsplatApplied(actor=" + var10000 + ", hitsplat=" + String.valueOf(this.getHitsplat()) + ")";
   }
}
