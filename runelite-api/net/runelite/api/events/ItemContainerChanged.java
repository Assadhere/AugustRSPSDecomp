package net.runelite.api.events;

import net.runelite.api.ItemContainer;

public final class ItemContainerChanged {
   private final int containerId;
   private final ItemContainer itemContainer;

   public ItemContainerChanged(int containerId, ItemContainer itemContainer) {
      this.containerId = containerId;
      this.itemContainer = itemContainer;
   }

   public int getContainerId() {
      return this.containerId;
   }

   public ItemContainer getItemContainer() {
      return this.itemContainer;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ItemContainerChanged)) {
         return false;
      } else {
         ItemContainerChanged other = (ItemContainerChanged)o;
         if (this.getContainerId() != other.getContainerId()) {
            return false;
         } else {
            Object this$itemContainer = this.getItemContainer();
            Object other$itemContainer = other.getItemContainer();
            if (this$itemContainer == null) {
               if (other$itemContainer != null) {
                  return false;
               }
            } else if (!this$itemContainer.equals(other$itemContainer)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getContainerId();
      Object $itemContainer = this.getItemContainer();
      result = result * 59 + ($itemContainer == null ? 43 : $itemContainer.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getContainerId();
      return "ItemContainerChanged(containerId=" + var10000 + ", itemContainer=" + String.valueOf(this.getItemContainer()) + ")";
   }
}
