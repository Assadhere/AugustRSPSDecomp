package net.runelite.client.plugins.cluescrolls.clues.item;

import net.runelite.api.Client;
import net.runelite.api.Item;

public class RangeItemRequirement implements ItemRequirement {
   private final String name;
   private final int startItemId;
   private final int endItemId;

   public RangeItemRequirement(String name, int startItemId, int endItemId) {
      this.name = name;
      this.startItemId = startItemId;
      this.endItemId = endItemId;
   }

   public boolean fulfilledBy(int itemId) {
      return itemId >= this.startItemId && itemId <= this.endItemId;
   }

   public boolean fulfilledBy(Item[] items) {
      Item[] var2 = items;
      int var3 = items.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Item item = var2[var4];
         if (item.getId() >= this.startItemId && item.getId() <= this.endItemId) {
            return true;
         }
      }

      return false;
   }

   public String getCollectiveName(Client client) {
      return this.name;
   }
}
