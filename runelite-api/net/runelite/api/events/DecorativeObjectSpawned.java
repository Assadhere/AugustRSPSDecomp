package net.runelite.api.events;

import net.runelite.api.DecorativeObject;
import net.runelite.api.Tile;

public class DecorativeObjectSpawned {
   private Tile tile;
   private DecorativeObject decorativeObject;

   public Tile getTile() {
      return this.tile;
   }

   public DecorativeObject getDecorativeObject() {
      return this.decorativeObject;
   }

   public void setTile(Tile tile) {
      this.tile = tile;
   }

   public void setDecorativeObject(DecorativeObject decorativeObject) {
      this.decorativeObject = decorativeObject;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DecorativeObjectSpawned)) {
         return false;
      } else {
         DecorativeObjectSpawned other = (DecorativeObjectSpawned)o;
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

            Object this$decorativeObject = this.getDecorativeObject();
            Object other$decorativeObject = other.getDecorativeObject();
            if (this$decorativeObject == null) {
               if (other$decorativeObject != null) {
                  return false;
               }
            } else if (!this$decorativeObject.equals(other$decorativeObject)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof DecorativeObjectSpawned;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $tile = this.getTile();
      result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
      Object $decorativeObject = this.getDecorativeObject();
      result = result * 59 + ($decorativeObject == null ? 43 : $decorativeObject.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getTile());
      return "DecorativeObjectSpawned(tile=" + var10000 + ", decorativeObject=" + String.valueOf(this.getDecorativeObject()) + ")";
   }
}
