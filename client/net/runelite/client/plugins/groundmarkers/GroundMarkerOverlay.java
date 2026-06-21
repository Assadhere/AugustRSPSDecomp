package net.runelite.client.plugins.groundmarkers;

import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.util.Iterator;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class GroundMarkerOverlay extends Overlay {
   private static final int MAX_DRAW_DISTANCE = 32;
   private final Client client;
   private final GroundMarkerConfig config;
   private final GroundMarkerPlugin plugin;

   @Inject
   private GroundMarkerOverlay(Client client, GroundMarkerConfig config, GroundMarkerPlugin plugin) {
      this.client = client;
      this.config = config;
      this.plugin = plugin;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.0F);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
   }

   public Dimension render(Graphics2D graphics) {
      Multimap<WorldView, ColorTileMarker> points = this.plugin.getPoints();
      if (points.isEmpty()) {
         return null;
      } else {
         Stroke stroke = new BasicStroke((float)this.config.borderWidth());
         Iterator var4 = points.keySet().iterator();

         while(var4.hasNext()) {
            WorldView wv = (WorldView)var4.next();
            Iterator var6 = points.get(wv).iterator();

            while(var6.hasNext()) {
               ColorTileMarker point = (ColorTileMarker)var6.next();
               WorldPoint worldPoint = point.getWorldPoint();
               if (worldPoint.getPlane() == wv.getPlane()) {
                  Color tileColor = point.getColor();
                  if (tileColor == null) {
                     tileColor = this.config.markerColor();
                  }

                  this.drawTile(graphics, wv, worldPoint, tileColor, point.getLabel(), stroke);
               }
            }
         }

         return null;
      }
   }

   private void drawTile(Graphics2D graphics, WorldView wv, WorldPoint point, Color color, @Nullable String label, Stroke borderStroke) {
      if (this.client.getLocalPlayer().getWorldView().isTopLevel()) {
         WorldPoint playerLocation = this.client.getLocalPlayer().getWorldLocation();
         if (point.distanceTo(playerLocation) >= 32) {
            return;
         }
      }

      LocalPoint lp = LocalPoint.fromWorld(wv, point);
      if (lp != null) {
         Polygon poly = Perspective.getCanvasTilePoly(this.client, lp);
         if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, color, new Color(0, 0, 0, this.config.fillOpacity()), borderStroke);
         }

         if (!Strings.isNullOrEmpty(label)) {
            Point canvasTextLocation = Perspective.getCanvasTextLocation(this.client, graphics, lp, label, 0);
            if (canvasTextLocation != null) {
               OverlayUtil.renderTextLocation(graphics, canvasTextLocation, label, color);
            }
         }

      }
   }
}
