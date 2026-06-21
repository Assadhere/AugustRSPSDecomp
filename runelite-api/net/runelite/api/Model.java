package net.runelite.api;

import javax.annotation.Nonnull;

public interface Model extends Mesh<Model>, Renderable {
   int[] getFaceColors1();

   int[] getFaceColors2();

   int[] getFaceColors3();

   short[] getUnlitFaceColors();

   int getSceneId();

   void setSceneId(int var1);

   int getBufferOffset();

   void setBufferOffset(int var1);

   int getUvBufferOffset();

   void setUvBufferOffset(int var1);

   int getBottomY();

   void calculateBoundsCylinder();

   byte[] getFaceRenderPriorities();

   byte[] getFaceBias();

   int getRadius();

   int getDiameter();

   /** @deprecated */
   @Deprecated
   void calculateExtreme(int var1);

   @Nonnull
   AABB getAABB(int var1);

   int getXYZMag();

   boolean useBoundingBox();

   int[] getVertexNormalsX();

   int[] getVertexNormalsY();

   int[] getVertexNormalsZ();

   byte getOverrideAmount();

   byte getOverrideHue();

   byte getOverrideSaturation();

   byte getOverrideLuminance();

   byte[] getTextureFaces();

   int[] getTexIndices1();

   int[] getTexIndices2();

   int[] getTexIndices3();

   Model getUnskewedModel();

   void drawFrustum(int var1, int var2, int var3, int var4, int var5, int var6, int var7);

   void drawOrtho(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);
}
