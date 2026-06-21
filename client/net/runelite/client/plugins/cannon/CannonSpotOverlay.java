package net.runelite.client.plugins.cannon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class CannonSpotOverlay extends Overlay {
   private final Client client;
   private final CannonPlugin plugin;
   private final CannonConfig config;
   @Inject
   private ItemManager itemManager;
   private boolean hidden;

   @Inject
   CannonSpotOverlay(Client client, CannonPlugin plugin, CannonConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D graphics) {
      List<WorldPoint> spotPoints = this.plugin.getSpotPoints();
      if (!this.hidden && !spotPoints.isEmpty() && this.config.showCannonSpots() && !this.plugin.isCannonPlaced()) {
         Iterator var3 = spotPoints.iterator();

         while(var3.hasNext()) {
            WorldPoint spot = (WorldPoint)var3.next();
            if (spot.getPlane() == this.client.getPlane()) {
               LocalPoint spotPoint = LocalPoint.fromWorld(this.client, spot);
               LocalPoint localLocation = this.client.getLocalPlayer().getLocalLocation();
               if (spotPoint != null && localLocation.distanceTo(spotPoint) <= 4100) {
                  this.renderCannonSpot(graphics, this.client, spotPoint, this.itemManager.getImage(2), Color.RED);
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void renderCannonSpot(Graphics2D graphics, Client client, LocalPoint point, BufferedImage image, Color color) {
      Polygon poly = Perspective.getCanvasTilePoly(client, point);
      if (poly != null) {
         OverlayUtil.renderPolygon(graphics, poly, color);
      }

      Point imageLoc = Perspective.getCanvasImageLocation(client, point, image, 0);
      if (imageLoc != null) {
         OverlayUtil.renderImageLocation(graphics, imageLoc, image);
      }

   }

   void setHidden(boolean hidden) {
      this.hidden = hidden;
   }
}
