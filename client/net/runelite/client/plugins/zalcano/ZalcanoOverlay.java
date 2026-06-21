package net.runelite.client.plugins.zalcano;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class ZalcanoOverlay extends Overlay {
   private final Client client;
   private final ZalcanoPlugin zalcanoPlugin;

   @Inject
   private ZalcanoOverlay(Client client, ZalcanoPlugin zalcanoPlugin) {
      this.client = client;
      this.zalcanoPlugin = zalcanoPlugin;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.setPriority(0.5F);
   }

   public Dimension render(Graphics2D graphics) {
      List<GraphicsObject> rocks = this.zalcanoPlugin.getRocks();
      if (!rocks.isEmpty()) {
         rocks.removeIf(GraphicsObject::finished);
         Iterator var3 = rocks.iterator();

         while(var3.hasNext()) {
            GraphicsObject graphicsObject = (GraphicsObject)var3.next();
            Player localPlayer = this.client.getLocalPlayer();
            LocalPoint graphicsObjectLocation = graphicsObject.getLocation();
            Polygon polygon = Perspective.getCanvasTilePoly(this.client, graphicsObjectLocation);
            if (polygon != null) {
               OverlayUtil.renderPolygon(graphics, polygon, localPlayer.getLocalLocation().equals(graphicsObjectLocation) ? Color.RED : Color.ORANGE);
            }
         }
      }

      LocalPoint targetedGlowingRock = this.zalcanoPlugin.getTargetedGlowingRock();
      if (targetedGlowingRock != null && this.client.getGameCycle() < this.zalcanoPlugin.getTargetedGlowingRockEndCycle()) {
         Polygon polygon = Perspective.getCanvasTileAreaPoly(this.client, targetedGlowingRock, 3);
         if (polygon != null) {
            OverlayUtil.renderPolygon(graphics, polygon, Color.RED);
         }
      }

      return null;
   }
}
