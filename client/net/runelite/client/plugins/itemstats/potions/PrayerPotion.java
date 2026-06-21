package net.runelite.client.plugins.itemstats.potions;

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.plugins.itemstats.StatBoost;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class PrayerPotion extends StatBoost {
   private static final double BASE_PERC = 0.25;
   private final int delta;
   private final double perc;
   private static final int RING_SLOT;
   private static final int CAPE_SLOT;

   public PrayerPotion(int delta) {
      this(delta, 0.25);
   }

   PrayerPotion(int delta, double perc) {
      super(Stats.PRAYER, false);
      this.delta = delta;
      this.perc = perc;
   }

   public int heals(Client client) {
      boolean hasHolyWrench = false;
      ItemContainer equipContainer = client.getItemContainer(94);
      int max;
      if (equipContainer != null) {
         Item cape = equipContainer.getItem(CAPE_SLOT);
         Item ring = equipContainer.getItem(RING_SLOT);
         hasHolyWrench = ring != null && ItemVariationMapping.getVariations(12601).stream().filter((itemId) -> {
            return itemId != 12601;
         }).anyMatch((itemId) -> {
            return itemId == ring.getId();
         });
         if (cape != null) {
            max = cape.getId();
            hasHolyWrench |= ItemVariationMapping.getVariations(9759).contains(max);
            hasHolyWrench |= ItemVariationMapping.getVariations(13280).contains(max);
         }
      }

      if (!hasHolyWrench) {
         ItemContainer invContainer = client.getItemContainer(93);
         if (invContainer != null) {
            Item[] var12 = invContainer.getItems();
            max = var12.length;

            for(int var7 = 0; var7 < max; ++var7) {
               Item itemStack = var12[var7];
               int item = itemStack.getId();
               hasHolyWrench = item == 6714;
               hasHolyWrench |= ItemVariationMapping.getVariations(9759).contains(item);
               hasHolyWrench |= ItemVariationMapping.getVariations(13280).contains(item);
               if (hasHolyWrench) {
                  break;
               }
            }
         }
      }

      double percent = hasHolyWrench ? this.perc + 0.02 : this.perc;
      max = this.getStat().getMaximum(client);
      return (int)((double)max * percent) * (this.delta >= 0 ? 1 : -1) + this.delta;
   }

   static {
      RING_SLOT = EquipmentInventorySlot.RING.getSlotIdx();
      CAPE_SLOT = EquipmentInventorySlot.CAPE.getSlotIdx();
   }
}
