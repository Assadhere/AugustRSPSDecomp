package net.runelite.client.plugins.cluescrolls.clues.item;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;

public class MultipleOfItemRequirement implements ItemRequirement {
   private final int itemId;
   private final int quantity;

   public MultipleOfItemRequirement(int itemId, int quantity) {
      this.itemId = itemId;
      this.quantity = quantity;
   }

   public boolean fulfilledBy(int itemId) {
      return itemId == this.itemId;
   }

   public boolean fulfilledBy(Item[] items) {
      int quantityFound = 0;
      Item[] var3 = items;
      int var4 = items.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Item item = var3[var5];
         if (item.getId() == this.itemId) {
            quantityFound += item.getQuantity();
            if (quantityFound >= this.quantity) {
               return true;
            }
         }
      }

      return false;
   }

   public String getCollectiveName(Client client) {
      ItemComposition definition = client.getItemDefinition(this.itemId);
      if (definition == null) {
         return "N/A";
      } else {
         String var10000 = definition.getName();
         return var10000 + " x" + this.quantity;
      }
   }
}
