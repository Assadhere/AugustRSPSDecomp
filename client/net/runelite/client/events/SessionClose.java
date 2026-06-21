package net.runelite.client.events;

public class SessionClose {
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SessionClose)) {
         return false;
      } else {
         SessionClose other = (SessionClose)o;
         return other.canEqual(this);
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SessionClose;
   }

   public int hashCode() {
      int result = true;
      return 1;
   }

   public String toString() {
      return "SessionClose()";
   }
}
