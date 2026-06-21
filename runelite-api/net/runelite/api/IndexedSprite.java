package net.runelite.api;

public interface IndexedSprite {
   byte[] getPixels();

   void setPixels(byte[] var1);

   int[] getPalette();

   void setPalette(int[] var1);

   int getOffsetX();

   void setOffsetX(int var1);

   int getOffsetY();

   void setOffsetY(int var1);

   int getWidth();

   void setWidth(int var1);

   int getHeight();

   void setHeight(int var1);

   int getOriginalWidth();

   void setOriginalWidth(int var1);

   int getOriginalHeight();

   void setOriginalHeight(int var1);
}
