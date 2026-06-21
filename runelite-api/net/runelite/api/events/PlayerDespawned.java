package net.runelite.api.events;

import net.runelite.api.Actor;
import net.runelite.api.Player;

public final class PlayerDespawned {
   private final Player player;

   public Actor getActor() {
      return this.player;
   }

   public PlayerDespawned(Player player) {
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PlayerDespawned)) {
         return false;
      } else {
         PlayerDespawned other = (PlayerDespawned)o;
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
      return "PlayerDespawned(player=" + String.valueOf(this.getPlayer()) + ")";
   }
}
