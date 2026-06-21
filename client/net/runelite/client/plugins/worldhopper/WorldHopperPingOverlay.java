package net.runelite.client.plugins.worldhopper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class WorldHopperPingOverlay extends Overlay {
   private static final int Y_OFFSET = 11;
   private static final int X_OFFSET = 1;
   private final Client client;
   private final WorldHopperPlugin worldHopperPlugin;
   private final WorldHopperConfig worldHopperConfig;

   @Inject
   private WorldHopperPingOverlay(Client client, WorldHopperPlugin worldHopperPlugin, WorldHopperConfig worldHopperConfig) {
      this.client = client;
      this.worldHopperPlugin = worldHopperPlugin;
      this.worldHopperConfig = worldHopperConfig;
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.setPriority(0.75F);
      this.setPosition(OverlayPosition.DYNAMIC);
   }

   public Dimension render(Graphics2D graphics) {
      if (!this.worldHopperConfig.displayPing()) {
         return null;
      } else {
         int xOffset = 1;
         Widget logoutButton = this.client.getWidget(10747939);
         if (logoutButton != null && !logoutButton.isHidden()) {
            xOffset += logoutButton.getWidth();
         }

         FontMetrics fm = graphics.getFontMetrics();
         int textHeight = fm.getAscent() - fm.getDescent();
         int width = (int)this.client.getRealDimensions().getWidth();
         int ping = this.worldHopperPlugin.getCurrentPing();
         Point point;
         if (ping >= 0) {
            String text = "" + ping + " ms";
            int textWidth = fm.stringWidth(text);
            point = new Point(width - textWidth - xOffset, textHeight + 11);
            OverlayUtil.renderTextLocation(graphics, point, text, Color.YELLOW);
            xOffset += textWidth + fm.stringWidth(" ");
         }

         int percRetransmit = this.worldHopperPlugin.retransmitCalculator.getRetransmitPercent();
         if (percRetransmit > 0) {
            String text = "" + percRetransmit + "% loss";
            point = new Point(width - fm.stringWidth(text) - xOffset, textHeight + 11);
            OverlayUtil.renderTextLocation(graphics, point, text, Color.RED);
         }

         return null;
      }
   }
}
