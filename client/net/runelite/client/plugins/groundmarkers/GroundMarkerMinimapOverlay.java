package net.runelite.client.plugins.groundmarkers;

import com.google.common.collect.Multimap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.util.Iterator;
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

class GroundMarkerMinimapOverlay extends Overlay {
   private final Client client;
   private final GroundMarkerConfig config;
   private final GroundMarkerPlugin plugin;

   @Inject
   private GroundMarkerMinimapOverlay(Client client, GroundMarkerConfig config, GroundMarkerPlugin plugin) {
      this.client = client;
      this.config = config;
      this.plugin = plugin;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.0F);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
   }

   public Dimension render(Graphics2D graphics) {
      Multimap<WorldView, ColorTileMarker> points = this.plugin.getPoints();
      if (!points.isEmpty() && this.config.drawTileOnMinimmap()) {
         Iterator var3 = points.keySet().iterator();

         while(var3.hasNext()) {
            WorldView wv = (WorldView)var3.next();
            Iterator var5 = points.get(wv).iterator();

            while(var5.hasNext()) {
               ColorTileMarker point = (ColorTileMarker)var5.next();
               WorldPoint worldPoint = point.getWorldPoint();
               if (worldPoint.getPlane() == wv.getPlane()) {
                  Color tileColor = point.getColor();
                  if (tileColor == null) {
                     tileColor = this.config.markerColor();
                  }

                  this.drawOnMinimap(graphics, wv, worldPoint, tileColor);
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void drawOnMinimap(Graphics2D graphics, WorldView wv, WorldPoint point, Color color) {
      LocalPoint lp = LocalPoint.fromWorld(wv, point);
      if (lp != null) {
         int x = lp.getX() & -128;
         int y = lp.getY() & -128;
         Point mp1 = Perspective.localToMinimap(this.client, new LocalPoint(x, y, wv.getId()));
         Point mp2 = Perspective.localToMinimap(this.client, new LocalPoint(x, y + 128, wv.getId()));
         Point mp3 = Perspective.localToMinimap(this.client, new LocalPoint(x + 128, y + 128, wv.getId()));
         Point mp4 = Perspective.localToMinimap(this.client, new LocalPoint(x + 128, y, wv.getId()));
         if (mp1 != null && mp2 != null && mp3 != null && mp4 != null) {
            Polygon poly = new Polygon();
            poly.addPoint(mp1.getX(), mp1.getY());
            poly.addPoint(mp2.getX(), mp2.getY());
            poly.addPoint(mp3.getX(), mp3.getY());
            poly.addPoint(mp4.getX(), mp4.getY());
            Stroke stroke = new BasicStroke(1.0F);
            graphics.setStroke(stroke);
            graphics.setColor(color);
            graphics.drawPolygon(poly);
         }
      }
   }
}
