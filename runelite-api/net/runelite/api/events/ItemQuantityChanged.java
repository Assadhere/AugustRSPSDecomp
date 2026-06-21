package net.runelite.api.events;

import net.runelite.api.Tile;
import net.runelite.api.TileItem;

public final class ItemQuantityChanged {
   private final TileItem item;
   private final Tile tile;
   private final int oldQuantity;
   private final int newQuantity;

   public ItemQuantityChanged(TileItem item, Tile tile, int oldQuantity, int newQuantity) {
      this.item = item;
      this.tile = tile;
      this.oldQuantity = oldQuantity;
      this.newQuantity = newQuantity;
   }

   public TileItem getItem() {
      return this.item;
   }

   public Tile getTile() {
      return this.tile;
   }

   public int getOldQuantity() {
      return this.oldQuantity;
   }

   public int getNewQuantity() {
      return this.newQuantity;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ItemQuantityChanged)) {
         return false;
      } else {
         ItemQuantityChanged other = (ItemQuantityChanged)o;
         if (this.getOldQuantity() != other.getOldQuantity()) {
            return false;
         } else if (this.getNewQuantity() != other.getNewQuantity()) {
            return false;
         } else {
            Object this$item = this.getItem();
            Object other$item = other.getItem();
            if (this$item == null) {
               if (other$item != null) {
                  return false;
               }
            } else if (!this$item.equals(other$item)) {
               return false;
            }

            Object this$tile = this.getTile();
            Object other$tile = other.getTile();
            if (this$tile == null) {
               if (other$tile != null) {
                  return false;
               }
            } else if (!this$tile.equals(other$tile)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getOldQuantity();
      result = result * 59 + this.getNewQuantity();
      Object $item = this.getItem();
      result = result * 59 + ($item == null ? 43 : $item.hashCode());
      Object $tile = this.getTile();
      result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getItem());
      return "ItemQuantityChanged(item=" + var10000 + ", tile=" + String.valueOf(this.getTile()) + ", oldQuantity=" + this.getOldQuantity() + ", newQuantity=" + this.getNewQuantity() + ")";
   }
}
