package net.runelite.api;

public interface Texture extends Node {
   int[] getPixels();

   int getAnimationDirection();

   int getAnimationSpeed();

   boolean isLoaded();

   float getU();

   void setU(float var1);

   float getV();

   void setV(float var1);
}
