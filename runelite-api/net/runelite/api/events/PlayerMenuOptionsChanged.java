package net.runelite.api.events;

public class PlayerMenuOptionsChanged {
   private int index;

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PlayerMenuOptionsChanged)) {
         return false;
      } else {
         PlayerMenuOptionsChanged other = (PlayerMenuOptionsChanged)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            return this.getIndex() == other.getIndex();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PlayerMenuOptionsChanged;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getIndex();
      return result;
   }

   public String toString() {
      return "PlayerMenuOptionsChanged(index=" + this.getIndex() + ")";
   }
}
