package net.runelite.client.plugins.prayer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class PrayerFlickOverlay extends Overlay {
   private final Client client;
   private final PrayerConfig config;
   private final PrayerPlugin plugin;

   @Inject
   private PrayerFlickOverlay(Client client, PrayerConfig config, PrayerPlugin plugin) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.client = client;
      this.config = config;
      this.plugin = plugin;
   }

   public Dimension render(Graphics2D graphics) {
      if ((this.plugin.isPrayersActive() || this.config.prayerFlickAlwaysOn()) && !this.config.prayerFlickLocation().equals(PrayerFlickLocation.NONE) && !this.config.prayerFlickLocation().equals(PrayerFlickLocation.PRAYER_BAR)) {
         Widget xpOrb = this.client.getWidget(10485780);
         if (xpOrb == null || xpOrb.isHidden()) {
            xpOrb = this.client.getWidget(58654738);
         }

         if (xpOrb != null && !xpOrb.isHidden()) {
            Rectangle2D bounds = xpOrb.getBounds().getBounds2D();
            if (bounds.getX() <= 0.0) {
               return null;
            } else {
               int orbInnerHeight = (int)bounds.getHeight();
               int orbInnerWidth = orbInnerHeight;
               int orbInnerX = (int)(bounds.getX() + 24.0);
               int orbInnerY = (int)(bounds.getY() - 1.0);
               double t = this.plugin.getTickProgress();
               int xOffset = (int)(-Math.cos(t) * (double)orbInnerWidth / 2.0) + orbInnerWidth / 2;
               int indicatorHeight = (int)(Math.sin(t) * (double)orbInnerHeight);
               int yOffset = orbInnerHeight / 2 - indicatorHeight / 2;
               graphics.setColor(this.config.prayerFlickColor());
               graphics.fillRect(orbInnerX + xOffset, orbInnerY + yOffset, 1, indicatorHeight);
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}
