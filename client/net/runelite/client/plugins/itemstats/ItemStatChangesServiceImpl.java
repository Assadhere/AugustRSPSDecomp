package net.runelite.client.plugins.itemstats;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ItemStatChangesServiceImpl implements ItemStatChangesService {
   private final ItemStatChanges itemstatchanges;

   @Inject
   private ItemStatChangesServiceImpl(ItemStatChanges itemstatchanges) {
      this.itemstatchanges = itemstatchanges;
   }

   public Effect getItemStatChanges(int id) {
      return this.itemstatchanges.get(id);
   }
}
