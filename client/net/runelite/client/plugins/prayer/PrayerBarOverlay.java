package net.runelite.client.plugins.prayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.ImageUtil;

@Singleton
class PrayerBarOverlay extends Overlay {
   private static final Color BAR_FILL_COLOR = new Color(0, 149, 151);
   private static final Color BAR_BG_COLOR;
   private static final Dimension PRAYER_BAR_SIZE;
   private static final int HD_PRAYER_BAR_PADDING = 1;
   private static final BufferedImage HD_FRONT_BAR;
   private static final BufferedImage HD_BACK_BAR;
   private final Client client;
   private final PrayerConfig config;
   private final PrayerPlugin plugin;
   private boolean showingPrayerBar;

   @Inject
   private PrayerBarOverlay(Client client, PrayerConfig config, PrayerPlugin plugin) {
      this.client = client;
      this.config = config;
      this.plugin = plugin;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.75F);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
   }

   public Dimension render(Graphics2D graphics) {
      if (this.config.showPrayerBar() && this.showingPrayerBar) {
         int height = this.client.getLocalPlayer().getLogicalHeight() + 10;
         LocalPoint localLocation = this.client.getLocalPlayer().getLocalLocation();
         Point canvasPoint = Perspective.localToCanvas(this.client, localLocation, this.client.getPlane(), height);
         float ratio = (float)this.client.getBoostedSkillLevel(Skill.PRAYER) / (float)this.client.getRealSkillLevel(Skill.PRAYER);
         int barX;
         int barY;
         int barWidth;
         int barHeight;
         int progressFill;
         double t;
         int xOffset;
         if (this.client.getSpriteOverrides().containsKey(2176)) {
            barX = HD_FRONT_BAR.getWidth();
            barY = HD_FRONT_BAR.getHeight();
            barWidth = canvasPoint.getX() - barX / 2;
            barHeight = canvasPoint.getY();
            progressFill = (int)Math.ceil((double)Math.max(2.0F, Math.min((float)barX * ratio, (float)barX)));
            graphics.drawImage(HD_BACK_BAR, barWidth, barHeight, barX, barY, (ImageObserver)null);
            graphics.drawImage(HD_FRONT_BAR.getSubimage(0, 0, progressFill, barY), barWidth, barHeight, progressFill, barY, (ImageObserver)null);
            if ((this.plugin.isPrayersActive() || this.config.prayerFlickAlwaysOn()) && (this.config.prayerFlickLocation().equals(PrayerFlickLocation.PRAYER_BAR) || this.config.prayerFlickLocation().equals(PrayerFlickLocation.BOTH))) {
               t = this.plugin.getTickProgress();
               xOffset = barX / 2 - 1;
               int xOffset = (int)(-Math.cos(t) * (double)xOffset) + xOffset;
               graphics.setColor(this.config.prayerFlickColor());
               graphics.fillRect(barWidth + xOffset, barHeight + 1, 1, barY - 2);
            }

            return null;
         } else {
            barX = canvasPoint.getX() - 15;
            barY = canvasPoint.getY();
            barWidth = PRAYER_BAR_SIZE.width;
            barHeight = PRAYER_BAR_SIZE.height;
            progressFill = (int)Math.ceil((double)Math.min((float)barWidth * ratio, (float)barWidth));
            graphics.setColor(BAR_BG_COLOR);
            graphics.fillRect(barX, barY, barWidth, barHeight);
            graphics.setColor(BAR_FILL_COLOR);
            graphics.fillRect(barX, barY, progressFill, barHeight);
            if ((this.plugin.isPrayersActive() || this.config.prayerFlickAlwaysOn()) && (this.config.prayerFlickLocation().equals(PrayerFlickLocation.PRAYER_BAR) || this.config.prayerFlickLocation().equals(PrayerFlickLocation.BOTH))) {
               t = this.plugin.getTickProgress();
               xOffset = (int)(-Math.cos(t) * (double)barWidth / 2.0) + barWidth / 2;
               graphics.setColor(this.config.prayerFlickColor());
               graphics.fillRect(barX + xOffset, barY, 1, barHeight);
            }

            return null;
         }
      } else {
         return null;
      }
   }

   void onTick() {
      Player localPlayer = this.client.getLocalPlayer();
      this.showingPrayerBar = true;
      if (localPlayer == null) {
         this.showingPrayerBar = false;
      } else if (this.config.hideIfNotPraying() && !this.plugin.isPrayersActive()) {
         this.showingPrayerBar = false;
      } else {
         if (this.config.hideIfOutOfCombat() && localPlayer.getHealthScale() == -1) {
            this.showingPrayerBar = false;
         }

      }
   }

   static {
      BAR_BG_COLOR = Color.black;
      PRAYER_BAR_SIZE = new Dimension(30, 5);
      HD_FRONT_BAR = ImageUtil.loadImageResource(PrayerPlugin.class, "front.png");
      HD_BACK_BAR = ImageUtil.loadImageResource(PrayerPlugin.class, "back.png");
   }
}
