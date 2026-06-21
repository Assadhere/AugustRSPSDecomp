package net.runelite.client.plugins.bosstimer;

import java.awt.image.BufferedImage;
import java.time.temporal.ChronoUnit;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.Timer;

class RespawnTimer extends Timer {
   private final Boss boss;

   public RespawnTimer(Boss boss, BufferedImage bossImage, Plugin plugin) {
      super(boss.getSpawnTime().toMillis(), ChronoUnit.MILLIS, bossImage, plugin);
      this.boss = boss;
   }

   public Boss getBoss() {
      return this.boss;
   }
}
