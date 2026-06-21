package net.runelite.client.plugins.devtools;

import net.runelite.api.Item;

class InventoryItem {
   private final int slot;
   private Item item;
   private final String name;
   private final boolean stackable;

   public int getSlot() {
      return this.slot;
   }

   public Item getItem() {
      return this.item;
   }

   public String getName() {
      return this.name;
   }

   public boolean isStackable() {
      return this.stackable;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof InventoryItem)) {
         return false;
      } else {
         InventoryItem other = (InventoryItem)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getSlot() != other.getSlot()) {
            return false;
         } else if (this.isStackable() != other.isStackable()) {
            return false;
         } else {
            label40: {
               Object this$item = this.getItem();
               Object other$item = other.getItem();
               if (this$item == null) {
                  if (other$item == null) {
                     break label40;
                  }
               } else if (this$item.equals(other$item)) {
                  break label40;
               }

               return false;
            }

            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
               if (other$name != null) {
                  return false;
               }
            } else if (!this$name.equals(other$name)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof InventoryItem;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getSlot();
      result = result * 59 + (this.isStackable() ? 79 : 97);
      Object $item = this.getItem();
      result = result * 59 + ($item == null ? 43 : $item.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getSlot();
      return "InventoryItem(slot=" + var10000 + ", item=" + String.valueOf(this.getItem()) + ", name=" + this.getName() + ", stackable=" + this.isStackable() + ")";
   }

   public InventoryItem(int slot, Item item, String name, boolean stackable) {
      this.slot = slot;
      this.item = item;
      this.name = name;
      this.stackable = stackable;
   }
}
