package net.runelite.client.game;

import net.runelite.api.coords.LocalPoint;

public final class ItemStack {
   private final int id;
   private final int quantity;

   public ItemStack(int id, int quantity) {
      this.id = id;
      this.quantity = quantity;
   }

   /** @deprecated */
   @Deprecated
   public ItemStack(int id, int quantity, LocalPoint location) {
      this.id = id;
      this.quantity = quantity;
   }

   /** @deprecated */
   @Deprecated
   public LocalPoint getLocation() {
      return null;
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
      } else if (!(o instanceof ItemStack)) {
         return false;
      } else {
         ItemStack other = (ItemStack)o;
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
      return "ItemStack(id=" + var10000 + ", quantity=" + this.getQuantity() + ")";
   }
}
