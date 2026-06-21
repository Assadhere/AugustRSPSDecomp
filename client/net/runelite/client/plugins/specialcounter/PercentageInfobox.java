package net.runelite.client.plugins.specialcounter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.ui.overlay.infobox.InfoBox;

class PercentageInfobox extends InfoBox {
   private float percent = 1.0F;

   PercentageInfobox(BufferedImage image, SpecialCounterPlugin plugin) {
      super(image, plugin);
   }

   void mul(float p) {
      this.percent *= p;
   }

   public String getTooltip() {
      return "Opponent defence has been reduced by " + this.getText() + ".";
   }

   public Color getTextColor() {
      return Color.WHITE;
   }

   public String getText() {
      float var10000 = 1.0F - this.percent;
      return (int)(var10000 * 100.0F) + "%";
   }
}
