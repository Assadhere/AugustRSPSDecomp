package net.runelite.api;

public final class Item {
   private final int id;
   private final int quantity;

   public Item(int id, int quantity) {
      this.id = id;
      this.quantity = quantity;
   }

   public int getId() {
      return this.id;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Item)) {
         return false;
      } else {
         Item other = (Item)o;
         if (this.getId() != other.getId()) {
            return false;
         } else {
            return this.getQuantity() == other.getQuantity();
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getId();
      result = result * 59 + this.getQuantity();
      return result;
   }

   public String toString() {
      int var10000 = this.getId();
      return "Item(id=" + var10000 + ", quantity=" + this.getQuantity() + ")";
   }
}
