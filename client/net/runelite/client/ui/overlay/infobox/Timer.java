package net.runelite.client.ui.overlay.infobox;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.runelite.client.plugins.Plugin;

public class Timer extends InfoBox {
   private static final Color HOURS_TIMER_COLOR = new Color(113, 255, 99);
   private final Instant startTime;
   private Instant endTime;
   private Duration duration;

   public Timer(long period, ChronoUnit unit, BufferedImage image, Plugin plugin) {
      super(image, plugin);
      Preconditions.checkArgument(period > 0L, "negative period!");
      this.startTime = Instant.now();
      this.duration = Duration.of(period, unit);
      this.endTime = this.startTime.plus(this.duration);
   }

   public String getText() {
      Duration timeLeft = Duration.between(Instant.now(), this.endTime);
      int seconds = (int)(timeLeft.toMillis() / 1000L);
      int hours = seconds / 3600;
      int minutes = seconds % 3600 / 60;
      int secs = seconds % 60;
      return hours > 0 ? String.format("%d:%02d", hours, minutes) : String.format("%d:%02d", minutes, secs);
   }

   public Color getTextColor() {
      Duration timeLeft = Duration.between(Instant.now(), this.endTime);
      if (timeLeft.getSeconds() / 3600L > 0L) {
         return HOURS_TIMER_COLOR;
      } else {
         return (double)timeLeft.getSeconds() < (double)this.duration.getSeconds() * 0.1 ? Color.RED.brighter() : Color.WHITE;
      }
   }

   public boolean render() {
      Duration timeLeft = Duration.between(Instant.now(), this.endTime);
      return !timeLeft.isNegative();
   }

   public boolean cull() {
      Duration timeLeft = Duration.between(Instant.now(), this.endTime);
      return timeLeft.isZero() || timeLeft.isNegative();
   }

   public void setDuration(Duration duration) {
      Preconditions.checkArgument(!duration.isNegative(), "negative duration");
      this.duration = duration;
      this.endTime = this.startTime.plus(duration);
   }

   public void updateDuration(Duration duration) {
      Preconditions.checkArgument(!duration.isNegative(), "negative duration");
      this.endTime = Instant.now().plus(duration);
      this.duration = Duration.between(this.startTime, this.endTime);
   }

   public Instant getStartTime() {
      return this.startTime;
   }

   public Instant getEndTime() {
      return this.endTime;
   }

   public Duration getDuration() {
      return this.duration;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getStartTime());
      return "Timer(startTime=" + var10000 + ", endTime=" + String.valueOf(this.getEndTime()) + ", duration=" + String.valueOf(this.getDuration()) + ")";
   }
}
