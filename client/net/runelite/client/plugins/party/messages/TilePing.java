package net.runelite.client.plugins.party.messages;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.party.messages.PartyMemberMessage;

public final class TilePing extends PartyMemberMessage {
   private final WorldPoint point;

   public TilePing(WorldPoint point) {
      this.point = point;
   }

   public WorldPoint getPoint() {
      return this.point;
   }

   public String toString() {
      return "TilePing(point=" + String.valueOf(this.getPoint()) + ")";
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TilePing)) {
         return false;
      } else {
         TilePing other = (TilePing)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else {
            Object this$point = this.getPoint();
            Object other$point = other.getPoint();
            if (this$point == null) {
               if (other$point != null) {
                  return false;
               }
            } else if (!this$point.equals(other$point)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof TilePing;
   }

   public int hashCode() {
      int PRIME = true;
      int result = super.hashCode();
      Object $point = this.getPoint();
      result = result * 59 + ($point == null ? 43 : $point.hashCode());
      return result;
   }
}
