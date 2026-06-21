package net.runelite.client.plugins.npcunaggroarea;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.geometry.Geometry;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class NpcAggroAreaOverlay extends Overlay {
   private static final int MAX_LOCAL_DRAW_LENGTH = 2560;
   private final Client client;
   private final NpcAggroAreaConfig config;
   private final NpcAggroAreaPlugin plugin;

   @Inject
   private NpcAggroAreaOverlay(Client client, NpcAggroAreaConfig config, NpcAggroAreaPlugin plugin) {
      this.client = client;
      this.config = config;
      this.plugin = plugin;
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.setPriority(0.0F);
      this.setPosition(OverlayPosition.DYNAMIC);
   }

   public Dimension render(Graphics2D graphics) {
      if (this.plugin.isActive() && this.plugin.getSafeCenters()[1] != null) {
         Player localPlayer = this.client.getLocalPlayer();
         if (localPlayer.getHealthScale() == -1 && this.config.hideIfOutOfCombat()) {
            return null;
         } else {
            GeneralPath lines = this.plugin.getLinesToDisplay()[this.client.getPlane()];
            if (lines == null) {
               return null;
            } else {
               Color outlineColor = this.config.unaggroAreaColor();
               if (outlineColor == null || this.plugin.getEndTime() != null && Instant.now().isBefore(this.plugin.getEndTime())) {
                  outlineColor = this.config.aggroAreaColor();
               }

               this.renderPath(graphics, lines, outlineColor);
               return null;
            }
         }
      } else {
         return null;
      }
   }

   private void renderPath(Graphics2D graphics, GeneralPath path, Color color) {
      LocalPoint playerLp = this.client.getLocalPlayer().getLocalLocation();
      Rectangle viewArea = new Rectangle(playerLp.getX() - 2560, playerLp.getY() - 2560, 5120, 5120);
      graphics.setColor(color);
      graphics.setStroke(new BasicStroke(1.0F));
      path = Geometry.clipPath(path, viewArea);
      path = Geometry.filterPath(path, (p1, p2) -> {
         return Perspective.localToCanvas(this.client, new LocalPoint((int)p1[0], (int)p1[1]), this.client.getPlane()) != null && Perspective.localToCanvas(this.client, new LocalPoint((int)p2[0], (int)p2[1]), this.client.getPlane()) != null;
      });
      path = Geometry.transformPath(path, (coords) -> {
         Point point = Perspective.localToCanvas(this.client, new LocalPoint((int)coords[0], (int)coords[1]), this.client.getPlane());
         coords[0] = (float)point.getX();
         coords[1] = (float)point.getY();
      });
      graphics.draw(path);
   }
}
