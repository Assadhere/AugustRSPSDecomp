package net.runelite.api.events;

import net.runelite.api.Nameable;

public final class NameableNameChanged {
   private final Nameable nameable;

   public NameableNameChanged(Nameable nameable) {
      this.nameable = nameable;
   }

   public Nameable getNameable() {
      return this.nameable;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NameableNameChanged)) {
         return false;
      } else {
         NameableNameChanged other = (NameableNameChanged)o;
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
      return "NameableNameChanged(nameable=" + String.valueOf(this.getNameable()) + ")";
   }
}
