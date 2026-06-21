package net.runelite.client.plugins.tileindicators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Tile;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class TileIndicatorsOverlay extends Overlay {
   private final Client client;
   private final TileIndicatorsConfig config;

   @Inject
   private TileIndicatorsOverlay(Client client, TileIndicatorsConfig config) {
      this.client = client;
      this.config = config;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.setPriority(0.5F);
   }

   public Dimension render(Graphics2D graphics) {
      if (this.config.highlightHoveredTile()) {
         WorldView wv = this.client.getLocalPlayer().getWorldView();
         Tile tile = wv.getSelectedSceneTile();
         if (tile != null) {
            this.renderTile(graphics, tile.getLocalLocation(), this.config.highlightHoveredColor(), this.config.hoveredTileBorderWidth(), this.config.hoveredTileFillColor());
         }
      }

      if (this.config.highlightDestinationTile()) {
         this.renderTile(graphics, this.client.getLocalDestinationLocation(), this.config.highlightDestinationColor(), this.config.destinationTileBorderWidth(), this.config.destinationTileFillColor());
      }

      if (this.config.highlightCurrentTile()) {
         WorldPoint playerPos = this.client.getLocalPlayer().getWorldLocation();
         if (playerPos == null) {
            return null;
         }

         LocalPoint playerPosLocal = LocalPoint.fromWorld(this.client, playerPos);
         if (playerPosLocal == null) {
            return null;
         }

         this.renderTile(graphics, playerPosLocal, this.config.highlightCurrentColor(), this.config.currentTileBorderWidth(), this.config.currentTileFillColor());
      }

      return null;
   }

   private void renderTile(Graphics2D graphics, LocalPoint dest, Color color, double borderWidth, Color fillColor) {
      if (dest != null) {
         Polygon poly = Perspective.getCanvasTilePoly(this.client, dest);
         if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, color, fillColor, new BasicStroke((float)borderWidth));
         }
      }
   }
}
