package net.runelite.client.plugins.party;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.plugins.party.data.PartyTilePingData;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class PartyPingOverlay extends Overlay {
   private final Client client;
   private final PartyPlugin plugin;

   @Inject
   private PartyPingOverlay(Client client, PartyPlugin plugin) {
      this.client = client;
      this.plugin = plugin;
      this.setPosition(OverlayPosition.DYNAMIC);
   }

   public Dimension render(Graphics2D graphics) {
      if (this.plugin.getPartyDataMap().isEmpty()) {
         return null;
      } else {
         synchronized(this.plugin.getPendingTilePings()) {
            Iterator<PartyTilePingData> iterator = this.plugin.getPendingTilePings().iterator();

            while(iterator.hasNext()) {
               PartyTilePingData next = (PartyTilePingData)iterator.next();
               if (next.getAlpha() <= 0) {
                  iterator.remove();
               } else {
                  this.renderPing(graphics, next);
                  long elapsedTimeMillis = (System.nanoTime() - next.getCreationTime()) / 1000000L;
                  next.setAlpha((int)Math.max(0L, 255L - elapsedTimeMillis / 4L));
               }
            }

            return null;
         }
      }
   }

   private void renderPing(Graphics2D graphics, PartyTilePingData ping) {
      LocalPoint localPoint = LocalPoint.fromWorld(this.client, ping.getPoint());
      if (localPoint != null) {
         Polygon poly = Perspective.getCanvasTilePoly(this.client, localPoint);
         if (poly != null) {
            Color color = new Color(ping.getColor().getRed(), ping.getColor().getGreen(), ping.getColor().getBlue(), ping.getAlpha());
            OverlayUtil.renderPolygon(graphics, poly, color);
         }
      }
   }
}
