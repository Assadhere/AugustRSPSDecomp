package net.runelite.client.events;

import java.util.Collection;
import net.runelite.api.NPCComposition;
import net.runelite.client.game.ItemStack;

public final class ServerNpcLoot {
   private final NPCComposition composition;
   private final Collection<ItemStack> items;

   public ServerNpcLoot(NPCComposition composition, Collection<ItemStack> items) {
      this.composition = composition;
      this.items = items;
   }

   public NPCComposition getComposition() {
      return this.composition;
   }

   public Collection<ItemStack> getItems() {
      return this.items;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ServerNpcLoot)) {
         return false;
      } else {
         ServerNpcLoot other = (ServerNpcLoot)o;
         Object this$composition = this.getComposition();
         Object other$composition = other.getComposition();
         if (this$composition == null) {
            if (other$composition != null) {
               return false;
            }
         } else if (!this$composition.equals(other$composition)) {
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
      Object $composition = this.getComposition();
      result = result * 59 + ($composition == null ? 43 : $composition.hashCode());
      Object $items = this.getItems();
      result = result * 59 + ($items == null ? 43 : $items.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getComposition());
      return "ServerNpcLoot(composition=" + var10000 + ", items=" + String.valueOf(this.getItems()) + ")";
   }
}
