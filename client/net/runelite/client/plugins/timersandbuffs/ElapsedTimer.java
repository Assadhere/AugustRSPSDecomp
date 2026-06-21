package net.runelite.client.plugins.timersandbuffs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import org.apache.commons.lang3.time.DurationFormatUtils;

class ElapsedTimer extends InfoBox {
   private final Instant startTime;
   private final Instant lastTime;

   ElapsedTimer(BufferedImage image, TimersAndBuffsPlugin plugin, Instant startTime, Instant lastTime) {
      super(image, plugin);
      this.startTime = startTime;
      this.lastTime = lastTime;
   }

   public String getText() {
      if (this.startTime == null) {
         return null;
      } else {
         Duration time = Duration.between(this.startTime, this.lastTime == null ? Instant.now() : this.lastTime);
         String formatString = "mm:ss";
         return DurationFormatUtils.formatDuration(time.toMillis(), "mm:ss", true);
      }
   }

   public Color getTextColor() {
      return Color.WHITE;
   }

   public String getTooltip() {
      if (this.startTime == null) {
         return null;
      } else {
         Duration time = Duration.between(this.startTime, this.lastTime == null ? Instant.now() : this.lastTime);
         long var10000 = time.toMillis();
         return "Elapsed time: " + DurationFormatUtils.formatDuration(var10000, "HH:mm:ss", true);
      }
   }

   public Instant getStartTime() {
      return this.startTime;
   }

   public Instant getLastTime() {
      return this.lastTime;
   }
}
