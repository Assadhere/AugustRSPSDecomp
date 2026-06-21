package net.runelite.api;

public interface SceneTileModel {
   int getModelUnderlay();

   int getModelOverlay();

   int getShape();

   int getRotation();

   int[] getFaceX();

   int[] getFaceY();

   int[] getFaceZ();

   int[] getVertexX();

   int[] getVertexY();

   int[] getVertexZ();

   int[] getTriangleColorA();

   int[] getTriangleColorB();

   int[] getTriangleColorC();

   int[] getTriangleTextureId();

   boolean isFlat();

   int getBufferOffset();

   void setBufferOffset(int var1);

   int getUvBufferOffset();

   void setUvBufferOffset(int var1);

   int getBufferLen();

   void setBufferLen(int var1);
}
