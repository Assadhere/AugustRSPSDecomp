package net.runelite.client.plugins.implings;

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
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class ImplingsOverlay extends Overlay {
   private static final int PURO_PURO = 10307;
   private final Client client;
   private final ImplingsConfig config;
   private final ImplingsPlugin plugin;

   @Inject
   private ImplingsOverlay(Client client, ImplingsConfig config, ImplingsPlugin plugin) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.config = config;
      this.client = client;
      this.plugin = plugin;
   }

   public Dimension render(Graphics2D graphics) {
      if (this.client.getLocalPlayer().getWorldLocation().getRegionID() != 10307) {
         return null;
      } else {
         if (this.config.showSpawn()) {
            ImplingSpawn[] var2 = ImplingSpawn.values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               ImplingSpawn spawn = var2[var4];
               if (this.plugin.showImplingType(spawn.getType()) != ImplingsConfig.ImplingMode.NONE) {
                  String impName = spawn.getType().getName();
                  this.drawSpawn(graphics, spawn.getSpawnLocation(), impName, this.config.getSpawnColor());
               }
            }
         }

         return null;
      }
   }

   private void drawSpawn(Graphics2D graphics, WorldPoint point, String text, Color color) {
      if (point.distanceTo(this.client.getLocalPlayer().getWorldLocation()) < 32) {
         LocalPoint localPoint = LocalPoint.fromWorld(this.client, point);
         if (localPoint != null) {
            Polygon poly = Perspective.getCanvasTilePoly(this.client, localPoint);
            if (poly != null) {
               OverlayUtil.renderPolygon(graphics, poly, color);
            }

            Point textPoint = Perspective.getCanvasTextLocation(this.client, graphics, localPoint, text, 0);
            if (textPoint != null) {
               OverlayUtil.renderTextLocation(graphics, textPoint, text, color);
            }

         }
      }
   }
}
