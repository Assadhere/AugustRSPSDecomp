package net.runelite.api;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface SpritePixels {
   int DEFAULT_SHADOW_COLOR = 3153952;

   void drawAt(int var1, int var2);

   int getWidth();

   int getHeight();

   int getMaxWidth();

   int getMaxHeight();

   int getOffsetX();

   int getOffsetY();

   void setMaxWidth(int var1);

   void setMaxHeight(int var1);

   void setOffsetX(int var1);

   void setOffsetY(int var1);

   int[] getPixels();

   BufferedImage toBufferedImage();

   void toBufferedImage(BufferedImage var1) throws IllegalArgumentException;

   BufferedImage toBufferedOutline(Color var1);

   void toBufferedOutline(BufferedImage var1, int var2);
}
