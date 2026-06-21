package net.runelite.client.plugins.cluescrolls.clues.item;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;

public class SingleItemRequirement implements ItemRequirement {
   private final int itemId;

   public SingleItemRequirement(int itemId) {
      this.itemId = itemId;
   }

   public boolean fulfilledBy(int itemId) {
      return this.itemId == itemId;
   }

   public boolean fulfilledBy(Item[] items) {
      Item[] var2 = items;
      int var3 = items.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Item item = var2[var4];
         if (item.getId() == this.itemId) {
            return true;
         }
      }

      return false;
   }

   public String getCollectiveName(Client client) {
      ItemComposition definition = client.getItemDefinition(this.itemId);
      return definition == null ? "N/A" : definition.getName();
   }
}
