package net.runelite.client.plugins.fishing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.game.FishingSpot;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class FishingSpotMinimapOverlay extends Overlay {
   private final FishingPlugin plugin;
   private final FishingConfig config;
   private boolean hidden;

   @Inject
   public FishingSpotMinimapOverlay(FishingPlugin plugin, FishingConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D graphics) {
      if (this.hidden) {
         return null;
      } else {
         Iterator var2 = this.plugin.getFishingSpots().iterator();

         while(true) {
            NPC npc;
            FishingSpot spot;
            do {
               do {
                  if (!var2.hasNext()) {
                     return null;
                  }

                  npc = (NPC)var2.next();
                  spot = FishingSpot.findSpot(npc.getId());
               } while(spot == null);
            } while(this.config.onlyCurrentSpot() && this.plugin.getCurrentSpot() != null && this.plugin.getCurrentSpot() != spot);

            Color color = npc.getGraphic() == 1387 ? this.config.getMinnowsOverlayColor() : (npc.getId() == 10569 ? this.config.getHarpoonfishOverlayColor() : this.config.getOverlayColor());
            Point minimapLocation = npc.getMinimapLocation();
            if (minimapLocation != null) {
               OverlayUtil.renderMinimapLocation(graphics, minimapLocation, color.darker());
            }
         }
      }
   }

   void setHidden(boolean hidden) {
      this.hidden = hidden;
   }
}
