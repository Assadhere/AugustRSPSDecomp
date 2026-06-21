package net.runelite.client.game;

public class NpcInfo {
   private int hitpoints;

   public int getHitpoints() {
      return this.hitpoints;
   }

   public void setHitpoints(int hitpoints) {
      this.hitpoints = hitpoints;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NpcInfo)) {
         return false;
      } else {
         NpcInfo other = (NpcInfo)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            return this.getHitpoints() == other.getHitpoints();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof NpcInfo;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getHitpoints();
      return result;
   }

   public String toString() {
      return "NpcInfo(hitpoints=" + this.getHitpoints() + ")";
   }
}
