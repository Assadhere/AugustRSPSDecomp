package net.runelite.client.ui.overlay.infobox;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.runelite.client.plugins.Plugin;

public class LoopTimer extends InfoBox {
   private final Instant startTime;
   private final Duration duration;
   private final boolean reverse;

   public LoopTimer(long period, ChronoUnit unit, BufferedImage image, Plugin plugin, boolean reverse) {
      super(image, plugin);
      Preconditions.checkArgument(period > 0L, "negative period!");
      this.startTime = Instant.now();
      this.duration = Duration.of(period, unit);
      this.reverse = reverse;
   }

   public LoopTimer(long period, ChronoUnit unit, BufferedImage image, Plugin plugin) {
      this(period, unit, image, plugin, false);
   }

   public String getText() {
      Duration progress = this.getProgress();
      int seconds = (int)(progress.toMillis() / 1000L);
      int minutes = seconds % 3600 / 60;
      int secs = seconds % 60;
      return String.format("%d:%02d", minutes, secs);
   }

   public Color getTextColor() {
      Duration progress = this.getProgress();
      return (double)progress.getSeconds() < (double)this.duration.getSeconds() * 0.1 ? Color.RED.brighter() : Color.WHITE;
   }

   private Duration getProgress() {
      Duration passed = Duration.between(this.startTime, Instant.now());
      long passedMillis = passed.toMillis();
      long durationMillis = this.duration.toMillis();
      long progress = passedMillis % durationMillis;
      return Duration.ofMillis(this.reverse ? durationMillis - progress : progress);
   }

   public Instant getStartTime() {
      return this.startTime;
   }

   public Duration getDuration() {
      return this.duration;
   }

   public boolean isReverse() {
      return this.reverse;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getStartTime());
      return "LoopTimer(startTime=" + var10000 + ", duration=" + String.valueOf(this.getDuration()) + ", reverse=" + this.isReverse() + ")";
   }
}
