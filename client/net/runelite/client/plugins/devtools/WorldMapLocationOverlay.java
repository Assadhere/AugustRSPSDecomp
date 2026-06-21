package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.worldmap.WorldMap;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.worldmap.WorldMapOverlay;

public class WorldMapLocationOverlay extends Overlay {
   private final Client client;
   private final WorldMapOverlay worldMapOverlay;
   private final DevToolsPlugin plugin;

   @Inject
   private WorldMapLocationOverlay(Client client, WorldMapOverlay worldMapOverlay, DevToolsPlugin plugin) {
      this.client = client;
      this.worldMapOverlay = worldMapOverlay;
      this.plugin = plugin;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(1.0F);
      this.setLayer(OverlayLayer.MANUAL);
      this.drawAfterInterface(595);
   }

   public Dimension render(Graphics2D graphics) {
      if (!this.plugin.getWorldMapLocation().isActive()) {
         return null;
      } else {
         WorldMap worldMap = this.client.getWorldMap();
         Widget worldMapWidget = this.client.getWidget(38993927);
         if (worldMap != null && worldMapWidget != null) {
            Rectangle worldMapRectangle = worldMapWidget.getBounds();
            graphics.setClip(worldMapRectangle);
            graphics.setColor(Color.CYAN);
            WorldPoint mapCenterPoint = new WorldPoint(worldMap.getWorldMapPosition().getX(), worldMap.getWorldMapPosition().getY(), 0);
            Point middle = this.worldMapOverlay.mapWorldPointToGraphicsPoint(mapCenterPoint);
            if (middle == null) {
               return null;
            } else {
               graphics.drawLine(middle.getX(), worldMapRectangle.y, middle.getX(), worldMapRectangle.y + worldMapRectangle.height);
               graphics.drawLine(worldMapRectangle.x, middle.getY(), worldMapRectangle.x + worldMapRectangle.width, middle.getY());
               int var10000 = mapCenterPoint.getX();
               String output = "Center: " + var10000 + ", " + mapCenterPoint.getY();
               graphics.setColor(Color.white);
               FontMetrics fm = graphics.getFontMetrics();
               int height = fm.getHeight();
               int width = fm.stringWidth(output);
               graphics.fillRect((int)worldMapRectangle.getX(), (int)worldMapRectangle.getY() + worldMapRectangle.height - height, (int)worldMapRectangle.getX() + width, (int)worldMapRectangle.getY() + worldMapRectangle.height);
               graphics.setColor(Color.BLACK);
               graphics.drawString(output, (int)worldMapRectangle.getX(), (int)worldMapRectangle.getY() + worldMapRectangle.height);
               return null;
            }
         } else {
            return null;
         }
      }
   }
}
