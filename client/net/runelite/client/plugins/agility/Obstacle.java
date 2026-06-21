package net.runelite.client.plugins.agility;

import javax.annotation.Nullable;
import net.runelite.api.Tile;
import net.runelite.client.game.AgilityShortcut;

final class Obstacle {
   private final Tile tile;
   @Nullable
   private final AgilityShortcut shortcut;

   public Tile getTile() {
      return this.tile;
   }

   @Nullable
   public AgilityShortcut getShortcut() {
      return this.shortcut;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Obstacle)) {
         return false;
      } else {
         Obstacle other = (Obstacle)o;
         Object this$tile = this.getTile();
         Object other$tile = other.getTile();
         if (this$tile == null) {
            if (other$tile != null) {
               return false;
            }
         } else if (!this$tile.equals(other$tile)) {
            return false;
         }

         Object this$shortcut = this.getShortcut();
         Object other$shortcut = other.getShortcut();
         if (this$shortcut == null) {
            if (other$shortcut != null) {
               return false;
            }
         } else if (!this$shortcut.equals(other$shortcut)) {
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
      Object $shortcut = this.getShortcut();
      result = result * 59 + ($shortcut == null ? 43 : $shortcut.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getTile());
      return "Obstacle(tile=" + var10000 + ", shortcut=" + String.valueOf(this.getShortcut()) + ")";
   }

   public Obstacle(Tile tile, @Nullable AgilityShortcut shortcut) {
      this.tile = tile;
      this.shortcut = shortcut;
   }
}
