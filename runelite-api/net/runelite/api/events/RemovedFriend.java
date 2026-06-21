package net.runelite.api.events;

import net.runelite.api.Nameable;

public final class RemovedFriend {
   private final Nameable nameable;

   public RemovedFriend(Nameable nameable) {
      this.nameable = nameable;
   }

   public Nameable getNameable() {
      return this.nameable;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RemovedFriend)) {
         return false;
      } else {
         RemovedFriend other = (RemovedFriend)o;
         Object this$nameable = this.getNameable();
         Object other$nameable = other.getNameable();
         if (this$nameable == null) {
            if (other$nameable != null) {
               return false;
            }
         } else if (!this$nameable.equals(other$nameable)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $nameable = this.getNameable();
      result = result * 59 + ($nameable == null ? 43 : $nameable.hashCode());
      return result;
   }

   public String toString() {
      return "RemovedFriend(nameable=" + String.valueOf(this.getNameable()) + ")";
   }
}
