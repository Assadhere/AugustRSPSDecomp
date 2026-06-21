package net.runelite.client.events;

import java.util.Collection;
import net.runelite.api.Player;
import net.runelite.client.game.ItemStack;

public final class PlayerLootReceived {
   private final Player player;
   private final Collection<ItemStack> items;

   public PlayerLootReceived(Player player, Collection<ItemStack> items) {
      this.player = player;
      this.items = items;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Collection<ItemStack> getItems() {
      return this.items;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PlayerLootReceived)) {
         return false;
      } else {
         PlayerLootReceived other = (PlayerLootReceived)o;
         Object this$player = this.getPlayer();
         Object other$player = other.getPlayer();
         if (this$player == null) {
            if (other$player != null) {
               return false;
            }
         } else if (!this$player.equals(other$player)) {
            return false;
         }

         Object this$items = this.getItems();
         Object other$items = other.getItems();
         if (this$items == null) {
            if (other$items != null) {
               return false;
            }
         } else if (!this$items.equals(other$items)) {
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
      Object $items = this.getItems();
      result = result * 59 + ($items == null ? 43 : $items.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getPlayer());
      return "PlayerLootReceived(player=" + var10000 + ", items=" + String.valueOf(this.getItems()) + ")";
   }
}
