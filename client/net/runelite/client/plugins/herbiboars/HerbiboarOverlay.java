package net.runelite.client.plugins.herbiboars;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Set;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;

class HerbiboarOverlay extends Overlay {
   private final HerbiboarPlugin plugin;
   private final HerbiboarConfig config;

   @Inject
   public HerbiboarOverlay(HerbiboarPlugin plugin, HerbiboarConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D graphics) {
      if (!this.plugin.isInHerbiboarArea()) {
         return null;
      } else {
         HerbiboarSearchSpot.Group currentGroup = this.plugin.getCurrentGroup();
         TrailToSpot nextTrail = this.plugin.getNextTrail();
         int finishId = this.plugin.getFinishId();
         if (this.config.isStartShown() && currentGroup == null && finishId == 0) {
            this.plugin.getStarts().values().forEach((obj) -> {
               OverlayUtil.renderTileOverlay(graphics, obj, "", this.config.getStartColor());
            });
         }

         if (this.config.isTrailShown()) {
            Set<Integer> shownTrailIds = this.plugin.getShownTrails();
            this.plugin.getTrails().values().forEach((x) -> {
               int id = x.getId();
               if (shownTrailIds.contains(id) && (finishId > 0 || nextTrail != null && !nextTrail.getFootprintIds().contains(id))) {
                  OverlayUtil.renderTileOverlay(graphics, x, "", this.config.getTrailColor());
               }

            });
         }

         TileObject object;
         WorldPoint finishLoc;
         if (this.config.isObjectShown() && finishId <= 0 && currentGroup != null) {
            finishLoc = ((HerbiboarSearchSpot)Iterables.getLast(this.plugin.getCurrentPath())).getLocation();
            object = (TileObject)this.plugin.getTrailObjects().get(finishLoc);
            this.drawObjectLocation(graphics, object, this.config.getObjectColor());
         }

         if (this.config.isTunnelShown() && finishId > 0) {
            finishLoc = (WorldPoint)this.plugin.getEndLocations().get(finishId - 1);
            object = (TileObject)this.plugin.getTunnels().get(finishLoc);
            this.drawObjectLocation(graphics, object, this.config.getTunnelColor());
         }

         return null;
      }
   }

   private void drawObjectLocation(Graphics2D graphics, TileObject object, Color color) {
      if (object != null) {
         if (this.config.showClickBoxes()) {
            Shape clickbox = object.getClickbox();
            if (clickbox != null) {
               Color clickBoxColor = ColorUtil.colorWithAlpha(color, color.getAlpha() / 12);
               graphics.setColor(color);
               graphics.draw(clickbox);
               graphics.setColor(clickBoxColor);
               graphics.fill(clickbox);
            }
         } else {
            OverlayUtil.renderTileOverlay(graphics, object, "", color);
         }

      }
   }
}
