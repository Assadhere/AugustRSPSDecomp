package net.runelite.client.plugins.team;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

abstract class MembersIndicator extends InfoBox {
   MembersIndicator(BufferedImage image, Plugin plugin) {
      super(image, plugin);
   }

   public Color getTextColor() {
      return Color.WHITE;
   }
}
