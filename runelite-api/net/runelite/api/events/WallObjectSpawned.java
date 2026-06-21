package net.runelite.api.events;

import net.runelite.api.Tile;
import net.runelite.api.WallObject;

public class WallObjectSpawned {
   private Tile tile;
   private WallObject wallObject;

   public Tile getTile() {
      return this.tile;
   }

   public WallObject getWallObject() {
      return this.wallObject;
   }

   public void setTile(Tile tile) {
      this.tile = tile;
   }

   public void setWallObject(WallObject wallObject) {
      this.wallObject = wallObject;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WallObjectSpawned)) {
         return false;
      } else {
         WallObjectSpawned other = (WallObjectSpawned)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$tile = this.getTile();
            Object other$tile = other.getTile();
            if (this$tile == null) {
               if (other$tile != null) {
                  return false;
               }
            } else if (!this$tile.equals(other$tile)) {
               return false;
            }

            Object this$wallObject = this.getWallObject();
            Object other$wallObject = other.getWallObject();
            if (this$wallObject == null) {
               if (other$wallObject != null) {
                  return false;
               }
            } else if (!this$wallObject.equals(other$wallObject)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof WallObjectSpawned;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $tile = this.getTile();
      result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
      Object $wallObject = this.getWallObject();
      result = result * 59 + ($wallObject == null ? 43 : $wallObject.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getTile());
      return "WallObjectSpawned(tile=" + var10000 + ", wallObject=" + String.valueOf(this.getWallObject()) + ")";
   }
}
