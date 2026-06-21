package net.runelite.client.plugins.herbiboars;

import com.google.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Set;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class HerbiboarMinimapOverlay extends Overlay {
   private final HerbiboarPlugin plugin;
   private final HerbiboarConfig config;

   @Inject
   public HerbiboarMinimapOverlay(HerbiboarPlugin plugin, HerbiboarConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D graphics) {
      if (this.config.isTrailShown() && this.plugin.isInHerbiboarArea()) {
         TrailToSpot nextTrail = this.plugin.getNextTrail();
         int finishId = this.plugin.getFinishId();
         Set<Integer> shownTrailIds = this.plugin.getShownTrails();
         Iterator var5 = this.plugin.getTrails().values().iterator();

         while(true) {
            int id;
            Point minimapLocation;
            do {
               do {
                  do {
                     if (!var5.hasNext()) {
                        return null;
                     }

                     TileObject tileObject = (TileObject)var5.next();
                     id = tileObject.getId();
                     minimapLocation = tileObject.getMinimapLocation();
                  } while(minimapLocation == null);
               } while(!shownTrailIds.contains(id));
            } while(finishId <= 0 && (nextTrail == null || nextTrail.getFootprintIds().contains(id)));

            OverlayUtil.renderMinimapLocation(graphics, minimapLocation, this.config.getTrailColor());
         }
      } else {
         return null;
      }
   }
}
