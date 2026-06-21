package osrs;

import net.runelite.api.Rasterizer;

public class iO implements Rasterizer {
   public void rasterGouraud(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      df.c((float)var1, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6, 0.0F, 0.0F, 0.0F, var7, var8, var9);
   }

   public void setRasterGouraudLowRes(boolean var1) {
      aW.h.a(var1);
   }

   public void rasterFlat(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      df.c((float)var1, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6, 0.0F, 0.0F, 0.0F, var7);
   }

   public int[] getPixels() {
      return aU.h;
   }

   public int getHeight() {
      return aU.e;
   }

   public int getWidth() {
      return aU.f;
   }

   public void fillRectangle(int var1, int var2, int var3, int var4, int var5) {
      aU.b(var1, var2, var3, var4, var5);
   }

   public void resetRasterClipping() {
      aW.k();
   }

   public void setDrawRegion(int var1, int var2, int var3, int var4) {
      aU.a(var1, var2, var3, var4);
   }
}
