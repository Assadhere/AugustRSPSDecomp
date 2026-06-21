package net.runelite.api;

public interface Mesh<T extends Mesh<T>> {
   int getVerticesCount();

   float[] getVerticesX();

   float[] getVerticesY();

   float[] getVerticesZ();

   int getFaceCount();

   int[] getFaceIndices1();

   int[] getFaceIndices2();

   int[] getFaceIndices3();

   byte[] getFaceTransparencies();

   short[] getFaceTextures();

   T rotateY90Ccw();

   T rotateY180Ccw();

   T rotateY270Ccw();

   T translate(int var1, int var2, int var3);

   T scale(int var1, int var2, int var3);
}
