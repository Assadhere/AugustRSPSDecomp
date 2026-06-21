package net.runelite.client.events;

public final class ExternalPluginsChanged {
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else {
         return o instanceof ExternalPluginsChanged;
      }
   }

   public int hashCode() {
      int result = true;
      return 1;
   }

   public String toString() {
      return "ExternalPluginsChanged()";
   }
}
