package net.runelite.client.plugins.screenmarkers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class ScreenMarkerWidgetHighlightOverlay extends Overlay {
   private final ScreenMarkerPlugin plugin;
   private final Client client;

   @Inject
   private ScreenMarkerWidgetHighlightOverlay(ScreenMarkerPlugin plugin, Client client) {
      this.plugin = plugin;
      this.client = client;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.setPriority(0.75F);
      this.setMovable(true);
   }

   public Dimension render(Graphics2D graphics) {
      if (this.plugin.isCreatingScreenMarker() && !this.plugin.isDrawingScreenMarker()) {
         MenuEntry[] menuEntries = this.client.getMenuEntries();
         if (!this.client.isMenuOpen() && menuEntries.length != 0) {
            MenuEntry menuEntry = menuEntries[menuEntries.length - 1];
            int childIdx = menuEntry.getParam0();
            int widgetId = menuEntry.getParam1();
            Widget widget = this.client.getWidget(widgetId);
            if (widget == null) {
               this.plugin.setSelectedWidgetBounds((Rectangle)null);
               return null;
            } else {
               Rectangle bounds = null;
               if (childIdx > -1) {
                  Widget child = widget.getChild(childIdx);
                  if (child != null) {
                     bounds = child.getBounds();
                  }
               } else {
                  bounds = widget.getBounds();
               }

               if (bounds == null) {
                  this.plugin.setSelectedWidgetBounds((Rectangle)null);
                  return null;
               } else {
                  drawHighlight(graphics, bounds);
                  this.plugin.setSelectedWidgetBounds(bounds);
                  return null;
               }
            }
         } else {
            this.plugin.setSelectedWidgetBounds((Rectangle)null);
            return null;
         }
      } else {
         return null;
      }
   }

   private static void drawHighlight(Graphics2D graphics, Rectangle bounds) {
      graphics.setColor(Color.GREEN);
      graphics.draw(bounds);
   }
}
