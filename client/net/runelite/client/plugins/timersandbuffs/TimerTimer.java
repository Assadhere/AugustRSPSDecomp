package net.runelite.client.plugins.timersandbuffs;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;
import net.runelite.client.ui.overlay.infobox.Timer;

class TimerTimer extends Timer {
   private final GameTimer timer;
   int ticks;

   TimerTimer(GameTimer timer, Duration duration, Plugin plugin) {
      super(duration.toMillis(), ChronoUnit.MILLIS, (BufferedImage)null, plugin);
      this.timer = timer;
      this.setPriority(InfoBoxPriority.MED);
   }

   public GameTimer getTimer() {
      return this.timer;
   }

   public String getName() {
      return this.timer.name();
   }
}
