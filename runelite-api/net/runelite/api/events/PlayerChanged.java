package net.runelite.api.events;

import net.runelite.api.Player;

public final class PlayerChanged {
   private final Player player;

   public PlayerChanged(Player player) {
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PlayerChanged)) {
         return false;
      } else {
         PlayerChanged other = (PlayerChanged)o;
         Object this$player = this.getPlayer();
         Object other$player = other.getPlayer();
         if (this$player == null) {
            if (other$player != null) {
               return false;
            }
         } else if (!this$player.equals(other$player)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $player = this.getPlayer();
      result = result * 59 + ($player == null ? 43 : $player.hashCode());
      return result;
   }

   public String toString() {
      return "PlayerChanged(player=" + String.valueOf(this.getPlayer()) + ")";
   }
}
