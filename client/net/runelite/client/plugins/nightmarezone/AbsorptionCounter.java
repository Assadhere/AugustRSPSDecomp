package net.runelite.client.plugins.nightmarezone;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.Counter;

public class AbsorptionCounter extends Counter {
   private int threshold;
   private Color aboveThresholdColor;
   private Color belowThresholdColor;

   AbsorptionCounter(BufferedImage image, Plugin plugin, int absorption, int threshold) {
      super(image, plugin, absorption);
      this.aboveThresholdColor = Color.GREEN;
      this.belowThresholdColor = Color.RED;
      this.threshold = threshold;
   }

   public Color getTextColor() {
      int absorption = this.getCount();
      return absorption >= this.threshold ? this.aboveThresholdColor : this.belowThresholdColor;
   }

   public String getTooltip() {
      int absorption = this.getCount();
      return "Absorption: " + absorption;
   }

   public void setThreshold(int threshold) {
      this.threshold = threshold;
   }

   public void setAboveThresholdColor(Color aboveThresholdColor) {
      this.aboveThresholdColor = aboveThresholdColor;
   }

   public void setBelowThresholdColor(Color belowThresholdColor) {
      this.belowThresholdColor = belowThresholdColor;
   }
}
