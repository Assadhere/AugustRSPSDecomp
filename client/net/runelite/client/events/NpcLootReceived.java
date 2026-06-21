package net.runelite.client.events;

import java.util.Collection;
import net.runelite.api.NPC;
import net.runelite.client.game.ItemStack;

public final class NpcLootReceived {
   private final NPC npc;
   private final Collection<ItemStack> items;

   public NpcLootReceived(NPC npc, Collection<ItemStack> items) {
      this.npc = npc;
      this.items = items;
   }

   public NPC getNpc() {
      return this.npc;
   }

   public Collection<ItemStack> getItems() {
      return this.items;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NpcLootReceived)) {
         return false;
      } else {
         NpcLootReceived other = (NpcLootReceived)o;
         Object this$npc = this.getNpc();
         Object other$npc = other.getNpc();
         if (this$npc == null) {
            if (other$npc != null) {
               return false;
            }
         } else if (!this$npc.equals(other$npc)) {
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
      Object $npc = this.getNpc();
      result = result * 59 + ($npc == null ? 43 : $npc.hashCode());
      Object $items = this.getItems();
      result = result * 59 + ($items == null ? 43 : $items.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getNpc());
      return "NpcLootReceived(npc=" + var10000 + ", items=" + String.valueOf(this.getItems()) + ")";
   }
}
