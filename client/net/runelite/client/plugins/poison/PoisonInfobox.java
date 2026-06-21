package net.runelite.client.plugins.poison;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.time.temporal.ChronoUnit;
import net.runelite.client.ui.overlay.infobox.Timer;

class PoisonInfobox extends Timer {
   private final PoisonPlugin plugin;

   PoisonInfobox(BufferedImage image, PoisonPlugin plugin) {
      super(18200L, ChronoUnit.MILLIS, image, plugin);
      this.plugin = plugin;
   }

   public String getTooltip() {
      return this.plugin.createTooltip();
   }

   public Color getTextColor() {
      return Color.RED.brighter();
   }
}
