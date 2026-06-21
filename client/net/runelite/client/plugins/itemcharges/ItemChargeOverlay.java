package net.runelite.client.plugins.itemcharges;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

class ItemChargeOverlay extends WidgetItemOverlay {
   private final ItemChargePlugin itemChargePlugin;
   private final ItemChargeConfig config;

   @Inject
   ItemChargeOverlay(ItemChargePlugin itemChargePlugin, ItemChargeConfig config) {
      this.itemChargePlugin = itemChargePlugin;
      this.config = config;
      this.showOnInventory();
      this.showOnEquipment();
   }

   public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem) {
      ItemWithConfig itemWithConfig = ItemWithConfig.findItem(itemId);
      int charges;
      if (itemWithConfig != null) {
         if (!itemWithConfig.getType().getEnabled().test(this.config)) {
            return;
         }

         charges = this.itemChargePlugin.getItemCharges(itemWithConfig.getConfigKey());
      } else {
         ItemWithCharge chargeItem = ItemWithCharge.findItem(itemId);
         if (chargeItem == null) {
            return;
         }

         ItemChargeType type = chargeItem.getType();
         if (!type.getEnabled().test(this.config)) {
            return;
         }

         charges = chargeItem.getCharges();
      }

      graphics.setFont(FontManager.getRunescapeSmallFont());
      Rectangle bounds = widgetItem.getCanvasBounds();
      TextComponent textComponent = new TextComponent();
      textComponent.setPosition(new Point(bounds.x - 1, bounds.y + 15));
      textComponent.setText(charges < 0 ? "?" : String.valueOf(charges));
      textComponent.setColor(this.itemChargePlugin.getColor(charges));
      textComponent.render(graphics);
   }
}
