package net.runelite.api.events;

import net.runelite.api.GameObject;
import net.runelite.api.Tile;

public class GameObjectDespawned {
   private Tile tile;
   private GameObject gameObject;

   public Tile getTile() {
      return this.tile;
   }

   public GameObject getGameObject() {
      return this.gameObject;
   }

   public void setTile(Tile tile) {
      this.tile = tile;
   }

   public void setGameObject(GameObject gameObject) {
      this.gameObject = gameObject;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GameObjectDespawned)) {
         return false;
      } else {
         GameObjectDespawned other = (GameObjectDespawned)o;
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

            Object this$gameObject = this.getGameObject();
            Object other$gameObject = other.getGameObject();
            if (this$gameObject == null) {
               if (other$gameObject != null) {
                  return false;
               }
            } else if (!this$gameObject.equals(other$gameObject)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof GameObjectDespawned;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $tile = this.getTile();
      result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
      Object $gameObject = this.getGameObject();
      result = result * 59 + ($gameObject == null ? 43 : $gameObject.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getTile());
      return "GameObjectDespawned(tile=" + var10000 + ", gameObject=" + String.valueOf(this.getGameObject()) + ")";
   }
}
