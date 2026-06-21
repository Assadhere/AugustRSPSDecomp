package net.runelite.client.ui.overlay.infobox;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;

public class Counter extends InfoBox {
   private int count;

   public Counter(BufferedImage image, Plugin plugin, int count) {
      super(image, plugin);
      this.count = count;
   }

   public String getText() {
      return Integer.toString(this.getCount());
   }

   public Color getTextColor() {
      return Color.WHITE;
   }

   public String toString() {
      return "Counter(count=" + this.getCount() + ")";
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }
}
