package net.runelite.client.plugins.inventoryviewer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;

class InventoryViewerOverlay extends OverlayPanel {
   private static final int INVENTORY_SIZE = 28;
   private static final ImageComponent PLACEHOLDER_IMAGE = new ImageComponent(new BufferedImage(36, 32, 6));
   private final Client client;
   private final ItemManager itemManager;
   private final InventoryViewerConfig config;
   private boolean hidden;

   @Inject
   private InventoryViewerOverlay(Client client, ItemManager itemManager, InventoryViewerConfig config) {
      this.setPosition(OverlayPosition.BOTTOM_RIGHT);
      this.panelComponent.setWrap(true);
      this.panelComponent.setGap(new Point(6, 4));
      this.panelComponent.setPreferredSize(new Dimension(168, 0));
      this.panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
      this.itemManager = itemManager;
      this.client = client;
      this.config = config;
      this.hidden = config.hiddenDefault();
   }

   public Dimension render(Graphics2D graphics) {
      if (this.hidden) {
         return null;
      } else if ((this.client.getVarcIntValue(171) == 3 || this.client.getWidget(786433) != null) && this.config.hideIfInventoryActive()) {
         return null;
      } else {
         ItemContainer itemContainer = this.client.getItemContainer(93);
         if (itemContainer == null) {
            return null;
         } else {
            Item[] items = itemContainer.getItems();

            for(int i = 0; i < 28; ++i) {
               if (i < items.length) {
                  Item item = items[i];
                  if (item.getQuantity() > 0) {
                     BufferedImage image = this.getImage(item);
                     if (image != null) {
                        this.panelComponent.getChildren().add(new ImageComponent(image));
                        continue;
                     }
                  }
               }

               this.panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
            }

            return super.render(graphics);
         }
      }
   }

   private BufferedImage getImage(Item item) {
      ItemComposition itemComposition = this.itemManager.getItemComposition(item.getId());
      return this.itemManager.getImage(item.getId(), item.getQuantity(), itemComposition.isStackable());
   }

   protected void toggle() {
      this.hidden = !this.hidden;
   }
}
