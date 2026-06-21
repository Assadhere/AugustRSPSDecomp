package net.runelite.client.plugins.mta;

import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

class MTAItemOverlay extends WidgetItemOverlay {
   private final MTAPlugin plugin;

   @Inject
   public MTAItemOverlay(MTAPlugin plugin) {
      this.plugin = plugin;
      this.showOnInventory();
   }

   public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem) {
      MTARoom[] var4 = this.plugin.getRooms();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         MTARoom room = var4[var6];
         if (room.inside()) {
            room.renderItemOverlay(graphics, itemId, widgetItem);
         }
      }

   }
}
