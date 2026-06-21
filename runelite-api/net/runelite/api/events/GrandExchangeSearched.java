package net.runelite.api.events;

public class GrandExchangeSearched {
   private boolean consumed;

   public void consume() {
      this.consumed = true;
   }

   public boolean isConsumed() {
      return this.consumed;
   }

   public void setConsumed(boolean consumed) {
      this.consumed = consumed;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GrandExchangeSearched)) {
         return false;
      } else {
         GrandExchangeSearched other = (GrandExchangeSearched)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            return this.isConsumed() == other.isConsumed();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof GrandExchangeSearched;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isConsumed() ? 79 : 97);
      return result;
   }

   public String toString() {
      return "GrandExchangeSearched(consumed=" + this.isConsumed() + ")";
   }
}
