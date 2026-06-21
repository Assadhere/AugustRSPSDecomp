package net.runelite.client.events;

public class SessionOpen {
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SessionOpen)) {
         return false;
      } else {
         SessionOpen other = (SessionOpen)o;
         return other.canEqual(this);
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SessionOpen;
   }

   public int hashCode() {
      int result = true;
      return 1;
   }

   public String toString() {
      return "SessionOpen()";
   }
}
