package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.worldmap.WorldMap;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class WorldMapRegionOverlay extends Overlay {
   private static final Color WHITE_TRANSLUCENT = new Color(255, 255, 255, 127);
   private static final int LABEL_PADDING = 4;
   private static final int REGION_SIZE = 64;
   private static final int REGION_TRUNCATE = -64;
   private final Client client;
   private final DevToolsPlugin plugin;

   @Inject
   private WorldMapRegionOverlay(Client client, DevToolsPlugin plugin) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.75F);
      this.setLayer(OverlayLayer.MANUAL);
      this.drawAfterInterface(595);
      this.client = client;
      this.plugin = plugin;
   }

   public Dimension render(Graphics2D graphics) {
      if (!this.plugin.getWorldMapLocation().isActive()) {
         return null;
      } else {
         this.drawRegionOverlay(graphics);
         return null;
      }
   }

   private void drawRegionOverlay(Graphics2D graphics) {
      WorldMap worldMap = this.client.getWorldMap();
      Widget map = this.client.getWidget(38993927);
      float pixelsPerTile = worldMap.getWorldMapZoom();
      if (map != null) {
         Rectangle worldMapRect = map.getBounds();
         graphics.setClip(worldMapRect);
         int widthInTiles = (int)Math.ceil(worldMapRect.getWidth() / (double)pixelsPerTile);
         int heightInTiles = (int)Math.ceil(worldMapRect.getHeight() / (double)pixelsPerTile);
         Point worldMapPosition = worldMap.getWorldMapPosition();
         int yTileMin = worldMapPosition.getY() - heightInTiles / 2;
         int xRegionMin = worldMapPosition.getX() - widthInTiles / 2 & -64;
         int xRegionMax = (worldMapPosition.getX() + widthInTiles / 2 & -64) + 64;
         int yRegionMin = yTileMin & -64;
         int yRegionMax = (worldMapPosition.getY() + heightInTiles / 2 & -64) + 64;
         int regionPixelSize = (int)Math.ceil((double)(64.0F * pixelsPerTile));

         for(int x = xRegionMin; x < xRegionMax; x += 64) {
            for(int y = yRegionMin; y < yRegionMax; y += 64) {
               graphics.setColor(WHITE_TRANSLUCENT);
               int yTileOffset = -(yTileMin - y);
               int xTileOffset = x + widthInTiles / 2 - worldMapPosition.getX();
               int xPos = (int)((float)xTileOffset * pixelsPerTile) + (int)worldMapRect.getX();
               int yPos = worldMapRect.height - (int)((float)yTileOffset * pixelsPerTile) + (int)worldMapRect.getY();
               yPos -= regionPixelSize;
               graphics.drawRect(xPos, yPos, regionPixelSize, regionPixelSize);
               int regionId = x >> 6 << 8 | y >> 6;
               String regionText = String.valueOf(regionId);
               FontMetrics fm = graphics.getFontMetrics();
               Rectangle2D textBounds = fm.getStringBounds(regionText, graphics);
               int labelWidth = (int)textBounds.getWidth() + 8;
               int labelHeight = (int)textBounds.getHeight() + 8;
               graphics.fillRect(xPos, yPos, labelWidth, labelHeight);
               graphics.setColor(Color.BLACK);
               graphics.drawString(regionText, xPos + 4, yPos + (int)textBounds.getHeight() + 4);
            }
         }

      }
   }
}
