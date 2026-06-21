package net.runelite.client.plugins.cluescrolls.clues.item;

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;

public class SlotLimitationRequirement implements ItemRequirement {
   private final String description;
   private final EquipmentInventorySlot[] slots;

   public SlotLimitationRequirement(String description, EquipmentInventorySlot... slots) {
      this.description = description;
      this.slots = slots;
   }

   public boolean fulfilledBy(int itemId) {
      return false;
   }

   public boolean fulfilledBy(Item[] items) {
      EquipmentInventorySlot[] var2 = this.slots;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EquipmentInventorySlot slot = var2[var4];
         if (slot.getSlotIdx() < items.length && items[slot.getSlotIdx()].getId() != -1) {
            return false;
         }
      }

      return true;
   }

   public String getCollectiveName(Client client) {
      return this.description;
   }
}
