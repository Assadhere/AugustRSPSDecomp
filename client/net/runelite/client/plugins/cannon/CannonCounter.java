package net.runelite.client.plugins.cannon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.ui.overlay.infobox.InfoBox;

class CannonCounter extends InfoBox {
   private final CannonPlugin plugin;

   CannonCounter(BufferedImage img, CannonPlugin plugin) {
      super(img, plugin);
      this.plugin = plugin;
   }

   public String getText() {
      return String.valueOf(this.plugin.getCballsLeft());
   }

   public Color getTextColor() {
      return this.plugin.getStateColor();
   }
}
