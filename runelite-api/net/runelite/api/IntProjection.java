package net.runelite.api;

public interface IntProjection extends Projection {
   int getCameraX();

   int getCameraY();

   int getCameraZ();

   float getPitchSin();

   float getPitchCos();

   float getYawSin();

   float getYawCos();
}
