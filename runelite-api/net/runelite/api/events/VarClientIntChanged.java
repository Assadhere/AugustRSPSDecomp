package net.runelite.api.events;

public final class VarClientIntChanged {
   private final int index;

   public VarClientIntChanged(int index) {
      this.index = index;
   }

   public int getIndex() {
      return this.index;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof VarClientIntChanged)) {
         return false;
      } else {
         VarClientIntChanged other = (VarClientIntChanged)o;
         return this.getIndex() == other.getIndex();
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getIndex();
      return result;
   }

   public String toString() {
      return "VarClientIntChanged(index=" + this.getIndex() + ")";
   }
}
