package net.runelite.client.plugins.itemstats.potions;

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.client.plugins.itemstats.StatBoost;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class StaminaPotion extends StatBoost {
   private final int baseRestore;

   public StaminaPotion(int baseRestore) {
      super(Stats.RUN_ENERGY, false);
      this.baseRestore = baseRestore;
   }

   public int heals(Client client) {
      ItemContainer equipContainer = client.getItemContainer(94);
      if (equipContainer != null) {
         Item ring = equipContainer.getItem(EquipmentInventorySlot.RING.getSlotIdx());
         if (ring != null && ring.getId() == 24736) {
            return this.baseRestore * 2;
         }
      }

      return this.baseRestore;
   }
}
