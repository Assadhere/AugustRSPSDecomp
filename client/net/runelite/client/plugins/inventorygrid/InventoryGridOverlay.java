package net.runelite.client.plugins.inventorygrid;

import com.google.inject.Inject;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class InventoryGridOverlay extends Overlay {
   private static final int INVENTORY_SIZE = 28;
   private static final int DISTANCE_TO_ACTIVATE_HOVER = 5;
   private final InventoryGridConfig config;
   private final Client client;
   private final ItemManager itemManager;
   private Point initialMousePoint;
   private boolean hoverActive = false;

   @Inject
   private InventoryGridOverlay(InventoryGridConfig config, Client client, ItemManager itemManager) {
      this.itemManager = itemManager;
      this.client = client;
      this.config = config;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
   }

   public Dimension render(Graphics2D graphics) {
      Widget draggingWidget = this.client.getDraggedWidget();
      if (draggingWidget == null) {
         this.initialMousePoint = null;
         this.hoverActive = false;
         return null;
      } else {
         assert draggingWidget.isIf3();

         if (draggingWidget.getId() != 983043 && draggingWidget.getId() != 983044 && draggingWidget.getId() != 9764864) {
            return null;
         } else {
            Widget inventoryWidget = draggingWidget.getParent();
            net.runelite.api.Point mouse = this.client.getMouseCanvasPosition();
            Point mousePoint = new Point(mouse.getX(), mouse.getY());
            int draggedItemIndex = draggingWidget.getIndex();
            WidgetItem draggedItem = getWidgetItem(inventoryWidget, draggedItemIndex);
            Rectangle initialBounds = draggedItem.getCanvasBounds(false);
            if (this.initialMousePoint == null) {
               this.initialMousePoint = mousePoint;
            }

            if (draggedItem.getId() != -1 && this.client.getDragTime() > draggingWidget.getDragDeadTime() && (this.hoverActive || !(this.initialMousePoint.distance(mousePoint) < 5.0))) {
               this.hoverActive = true;

               for(int i = 0; i < 28; ++i) {
                  WidgetItem targetWidgetItem = getWidgetItem(inventoryWidget, i);
                  Rectangle bounds = targetWidgetItem.getCanvasBounds(false);
                  boolean inBounds = bounds.contains(mousePoint);
                  if (this.config.showItem() && inBounds) {
                     this.drawItem(graphics, bounds, draggedItem);
                     this.drawItem(graphics, initialBounds, targetWidgetItem);
                  }

                  if (this.config.showHighlight() && inBounds) {
                     graphics.setColor(this.config.highlightColor());
                     graphics.fill(bounds);
                  } else if (this.config.showGrid()) {
                     graphics.setColor(this.config.gridColor());
                     graphics.fill(bounds);
                  }
               }

               return null;
            } else {
               return null;
            }
         }
      }
   }

   private static WidgetItem getWidgetItem(Widget parentWidget, int idx) {
      assert parentWidget.isIf3();

      Widget wi = parentWidget.getChild(idx);
      return new WidgetItem(wi.getItemId(), wi.getItemQuantity(), wi.getBounds(), parentWidget, wi.getBounds());
   }

   private void drawItem(Graphics2D graphics, Rectangle bounds, WidgetItem item) {
      if (item.getId() != -1) {
         BufferedImage draggedItemImage = this.itemManager.getImage(item.getId(), item.getQuantity(), false);
         int x = (int)bounds.getX();
         int y = (int)bounds.getY();
         graphics.setComposite(AlphaComposite.SrcOver.derive(0.3F));
         graphics.drawImage(draggedItemImage, x, y, (ImageObserver)null);
         graphics.setComposite(AlphaComposite.SrcOver);
      }
   }
}
