package net.runelite.api.events;

import net.runelite.api.Tile;
import net.runelite.api.TileItem;

public final class ItemDespawned {
   private final Tile tile;
   private final TileItem item;

   public ItemDespawned(Tile tile, TileItem item) {
      this.tile = tile;
      this.item = item;
   }

   public Tile getTile() {
      return this.tile;
   }

   public TileItem getItem() {
      return this.item;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ItemDespawned)) {
         return false;
      } else {
         ItemDespawned other = (ItemDespawned)o;
         Object this$tile = this.getTile();
         Object other$tile = other.getTile();
         if (this$tile == null) {
            if (other$tile != null) {
               return false;
            }
         } else if (!this$tile.equals(other$tile)) {
            return false;
         }

         Object this$item = this.getItem();
         Object other$item = other.getItem();
         if (this$item == null) {
            if (other$item != null) {
               return false;
            }
         } else if (!this$item.equals(other$item)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $tile = this.getTile();
      result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
      Object $item = this.getItem();
      result = result * 59 + ($item == null ? 43 : $item.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getTile());
      return "ItemDespawned(tile=" + var10000 + ", item=" + String.valueOf(this.getItem()) + ")";
   }
}
