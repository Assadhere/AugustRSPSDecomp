package net.runelite.client.plugins.timersandbuffs;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;
import net.runelite.client.ui.overlay.infobox.Timer;

class CustomServerTimer extends Timer {
   private final int timerId;
   private final String description;
   private final GameTimerImageType imageType;

   CustomServerTimer(int timerId, GameTimerImageType imageType, String description, Duration duration, Plugin plugin) {
      super(duration.toMillis(), ChronoUnit.MILLIS, (BufferedImage)null, plugin);
      this.timerId = timerId;
      this.description = description;
      this.imageType = imageType;
      this.setPriority(InfoBoxPriority.MED);
   }

   public String getName() {
      return "custom_timer_" + this.timerId;
   }

   public int getTimerId() {
      return this.timerId;
   }

   public String getDescription() {
      return this.description;
   }

   public GameTimerImageType getImageType() {
      return this.imageType;
   }
}
