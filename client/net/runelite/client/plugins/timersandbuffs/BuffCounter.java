package net.runelite.client.plugins.timersandbuffs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.ui.overlay.infobox.Counter;

class BuffCounter extends Counter {
   private final TimersAndBuffsPlugin plugin;
   private final GameCounter gameCounter;

   BuffCounter(TimersAndBuffsPlugin plugin, GameCounter gameCounter, int count) {
      super((BufferedImage)null, plugin, count);
      this.plugin = plugin;
      this.gameCounter = gameCounter;
   }

   public String getText() {
      return this.gameCounter.isShouldDisplayCount() ? Integer.toString(this.getCount()) : "";
   }

   public Color getTextColor() {
      return this.gameCounter.getColorBoundaryType().shouldRecolor(this.getCount(), this.gameCounter.getBoundary()) ? this.gameCounter.getColor() : Color.WHITE;
   }

   TimersAndBuffsPlugin getPlugin() {
      return this.plugin;
   }

   GameCounter getGameCounter() {
      return this.gameCounter;
   }
}
