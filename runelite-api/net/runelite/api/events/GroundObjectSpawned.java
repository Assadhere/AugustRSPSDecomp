package net.runelite.api.events;

import net.runelite.api.GroundObject;
import net.runelite.api.Tile;

public class GroundObjectSpawned {
   private Tile tile;
   private GroundObject groundObject;

   public Tile getTile() {
      return this.tile;
   }

   public GroundObject getGroundObject() {
      return this.groundObject;
   }

   public void setTile(Tile tile) {
      this.tile = tile;
   }

   public void setGroundObject(GroundObject groundObject) {
      this.groundObject = groundObject;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GroundObjectSpawned)) {
         return false;
      } else {
         GroundObjectSpawned other = (GroundObjectSpawned)o;
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

            Object this$groundObject = this.getGroundObject();
            Object other$groundObject = other.getGroundObject();
            if (this$groundObject == null) {
               if (other$groundObject != null) {
                  return false;
               }
            } else if (!this$groundObject.equals(other$groundObject)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof GroundObjectSpawned;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $tile = this.getTile();
      result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
      Object $groundObject = this.getGroundObject();
      result = result * 59 + ($groundObject == null ? 43 : $groundObject.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getTile());
      return "GroundObjectSpawned(tile=" + var10000 + ", groundObject=" + String.valueOf(this.getGroundObject()) + ")";
   }
}
