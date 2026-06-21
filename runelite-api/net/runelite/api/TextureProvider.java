package net.runelite.api;

public interface TextureProvider {
   double getBrightness();

   void setBrightness(double var1);

   Texture[] getTextures();

   int[] load(int var1);

   int getDefaultColor(int var1);
}
