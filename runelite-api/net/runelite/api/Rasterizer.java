package net.runelite.api;

public interface Rasterizer {
   int[] getPixels();

   int getWidth();

   int getHeight();

   void fillRectangle(int var1, int var2, int var3, int var4, int var5);

   void rasterFlat(int var1, int var2, int var3, int var4, int var5, int var6, int var7);

   void setRasterGouraudLowRes(boolean var1);

   void rasterGouraud(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9);

   void setDrawRegion(int var1, int var2, int var3, int var4);

   void resetRasterClipping();
}
