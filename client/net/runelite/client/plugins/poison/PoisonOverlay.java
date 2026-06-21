package net.runelite.client.plugins.poison;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

class PoisonOverlay extends Overlay {
   private final PoisonPlugin plugin;
   private final Client client;
   private final TooltipManager tooltipManager;

   @Inject
   private PoisonOverlay(PoisonPlugin plugin, Client client, TooltipManager tooltipManager) {
      this.plugin = plugin;
      this.client = client;
      this.tooltipManager = tooltipManager;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
   }

   public Dimension render(Graphics2D graphics) {
      if (this.plugin.getLastDamage() <= 0) {
         return null;
      } else {
         Widget healthOrb = this.client.getWidget(10485767);
         if (healthOrb == null || healthOrb.isHidden()) {
            healthOrb = this.client.getWidget(58654725);
         }

         if (healthOrb != null && !healthOrb.isHidden()) {
            Rectangle bounds = healthOrb.getBounds();
            if (bounds.getX() <= 0.0) {
               return null;
            } else {
               Point mousePosition = this.client.getMouseCanvasPosition();
               if (bounds.contains(mousePosition.getX(), mousePosition.getY())) {
                  this.tooltipManager.add(new Tooltip(this.plugin.createTooltip()));
               }

               return null;
            }
         } else {
            return null;
         }
      }
   }
}
