package net.runelite.client.plugins.cannon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.TextComponent;

class CannonOverlay extends Overlay {
   private final Client client;
   private final CannonConfig config;
   private final CannonPlugin plugin;
   private final TextComponent textComponent = new TextComponent();

   @Inject
   CannonOverlay(Client client, CannonConfig config, CannonPlugin plugin) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.5F);
      this.client = client;
      this.config = config;
      this.plugin = plugin;
   }

   public Dimension render(Graphics2D graphics) {
      if (this.plugin.isCannonPlaced() && this.plugin.getCannonPosition() != null && this.plugin.getCannonWorld() == this.client.getWorld()) {
         WorldPoint cannonLocation = this.plugin.getCannonPosition().toWorldPoint().dx(1).dy(1);
         LocalPoint cannonPoint = LocalPoint.fromWorld(this.client, cannonLocation);
         if (cannonPoint == null) {
            return null;
         } else {
            LocalPoint localLocation = this.client.getLocalPlayer().getLocalLocation();
            if (localLocation.distanceTo(cannonPoint) <= 4100) {
               Point cannonLoc = Perspective.getCanvasTextLocation(this.client, graphics, cannonPoint, String.valueOf(this.plugin.getCballsLeft()), 150);
               if (cannonLoc != null) {
                  this.textComponent.setText(String.valueOf(this.plugin.getCballsLeft()));
                  this.textComponent.setPosition(new java.awt.Point(cannonLoc.getX(), cannonLoc.getY()));
                  this.textComponent.setColor(this.plugin.getStateColor());
                  this.textComponent.render(graphics);
               }

               if (this.config.showDoubleHitSpot()) {
                  Color color = this.config.highlightDoubleHitColor();
                  this.drawDoubleHitSpots(graphics, cannonPoint, color);
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   private void drawDoubleHitSpots(Graphics2D graphics, LocalPoint startTile, Color color) {
      for(int x = -3; x <= 3; ++x) {
         for(int y = -3; y <= 3; ++y) {
            if ((y == 1 || x == 1 || y == -1 || x == -1) && (y < -1 || y > 1 || x < -1 || x > 1)) {
               int xPos = startTile.getX() - x * 128;
               int yPos = startTile.getY() - y * 128;
               LocalPoint marker = new LocalPoint(xPos, yPos);
               Polygon poly = Perspective.getCanvasTilePoly(this.client, marker);
               if (poly != null) {
                  OverlayUtil.renderPolygon(graphics, poly, color);
               }
            }
         }
      }

   }
}
