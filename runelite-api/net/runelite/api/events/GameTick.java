package net.runelite.api.events;

public class GameTick {
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GameTick)) {
         return false;
      } else {
         GameTick other = (GameTick)o;
         return other.canEqual(this);
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof GameTick;
   }

   public int hashCode() {
      int result = true;
      return 1;
   }

   public String toString() {
      return "GameTick()";
   }
}
