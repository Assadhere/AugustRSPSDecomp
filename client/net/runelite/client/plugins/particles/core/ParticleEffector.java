package net.runelite.client.plugins.particles.core;

import net.runelite.client.plugins.particles.config.ParticleEffectorConfig;

public class ParticleEffector {
   private ParticleEffectorConfig config;
   private int worldX;
   private int worldY;
   private int worldZ;
   private int attachedFaceIndex = -1;
   private int attachedVertexIndex = -1;
   private int attachedMarkerId = -1;
   private int transformedForceX;
   private int transformedForceY;
   private int transformedForceZ;
   private int normalX;
   private int normalY;
   private int normalZ;
   private boolean hasNormal = false;
   private boolean inheritDirection = false;
   private boolean active = true;
   private boolean debugAffectAll = false;

   public ParticleEffector(ParticleEffectorConfig config) {
      this.config = config;
      this.transformedForceX = config.getForceDirectionX();
      this.transformedForceY = config.getForceDirectionY();
      this.transformedForceZ = config.getForceDirectionZ();
   }

   public void setPosition(int worldX, int worldY, int worldZ) {
      this.worldX = worldX;
      this.worldY = worldY;
      this.worldZ = worldZ;
   }

   public boolean isAttachedToFace() {
      return this.attachedFaceIndex >= 0;
   }

   public void refreshTransformedForce() {
      this.transformedForceX = this.config.getForceDirectionX();
      this.transformedForceY = this.config.getForceDirectionY();
      this.transformedForceZ = this.config.getForceDirectionZ();
   }

   public void setFaceNormal(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3) {
      int edge1x = x3 - x2;
      int edge1y = y3 - y2;
      int edge1z = z3 - z2;
      int edge2x = x1 - x2;
      int edge2y = y1 - y2;
      int edge2z = z1 - z2;
      this.normalZ = edge1x * edge2y - edge1y * edge2x;
      this.normalX = edge1y * edge2z - edge1z * edge2y;

      for(this.normalY = edge2x * edge1z - edge2z * edge1x; this.normalX > 32767 || this.normalY > 32767 || this.normalZ > 32767 || this.normalX < -32767 || this.normalY < -32767 || this.normalZ < -32767; this.normalY >>= 1) {
         this.normalZ >>= 1;
         this.normalX >>= 1;
      }

      int length = (int)Math.sqrt((double)(this.normalX * this.normalX + this.normalY * this.normalY + this.normalZ * this.normalZ));
      if (length == 0) {
         length = 1;
      }

      this.normalY = this.normalY * 32767 / length;
      this.normalX = this.normalX * 32767 / length;
      this.normalZ = this.normalZ * 32767 / length;
      this.hasNormal = true;
      if (this.inheritDirection) {
         this.updateTransformedForceFromNormal();
      }

   }

   private void updateTransformedForceFromNormal() {
      if (!this.hasNormal) {
         this.refreshTransformedForce();
      } else {
         int fx = this.config.getForceDirectionX();
         int fy = this.config.getForceDirectionY();
         int fz = this.config.getForceDirectionZ();
         double nx = (double)this.normalX / 32767.0;
         double ny = (double)this.normalY / 32767.0;
         double nz = (double)this.normalZ / 32767.0;
         double refX = 0.0;
         double refY = 0.0;
         double refZ = 1.0;
         if (Math.abs(nz) > 0.9) {
            refX = 1.0;
            refY = 0.0;
            refZ = 0.0;
         }

         double tanXx = refY * nz - refZ * ny;
         double tanXy = refZ * nx - refX * nz;
         double tanXz = refX * ny - refY * nx;
         double tanXlen = Math.sqrt(tanXx * tanXx + tanXy * tanXy + tanXz * tanXz);
         if (tanXlen > 1.0E-4) {
            tanXx /= tanXlen;
            tanXy /= tanXlen;
            tanXz /= tanXlen;
         }

         double tanZx = ny * tanXz - nz * tanXy;
         double tanZy = nz * tanXx - nx * tanXz;
         double tanZz = nx * tanXy - ny * tanXx;
         this.transformedForceX = (int)((double)fx * tanXx + (double)fy * nx + (double)fz * tanZx);
         this.transformedForceY = (int)((double)fx * tanXy + (double)fy * ny + (double)fz * tanZy);
         this.transformedForceZ = (int)((double)fx * tanXz + (double)fy * nz + (double)fz * tanZz);
      }
   }

   public void deactivate() {
      this.active = false;
   }

   public void shiftPosition(int shiftX, int shiftZ) {
      this.worldX += shiftX;
      this.worldZ += shiftZ;
   }

   public void reset(ParticleEffectorConfig config) {
      this.config = config;
      this.transformedForceX = config.getForceDirectionX();
      this.transformedForceY = config.getForceDirectionY();
      this.transformedForceZ = config.getForceDirectionZ();
      this.attachedFaceIndex = -1;
      this.attachedVertexIndex = -1;
      this.attachedMarkerId = -1;
      this.normalX = 0;
      this.normalY = 0;
      this.normalZ = 0;
      this.hasNormal = false;
      this.inheritDirection = false;
      this.active = true;
      this.debugAffectAll = false;
   }

   public ParticleEffectorConfig getConfig() {
      return this.config;
   }

   public int getWorldX() {
      return this.worldX;
   }

   public int getWorldY() {
      return this.worldY;
   }

   public int getWorldZ() {
      return this.worldZ;
   }

   public int getAttachedFaceIndex() {
      return this.attachedFaceIndex;
   }

   public int getAttachedVertexIndex() {
      return this.attachedVertexIndex;
   }

   public int getAttachedMarkerId() {
      return this.attachedMarkerId;
   }

   public int getTransformedForceX() {
      return this.transformedForceX;
   }

   public int getTransformedForceY() {
      return this.transformedForceY;
   }

   public int getTransformedForceZ() {
      return this.transformedForceZ;
   }

   public int getNormalX() {
      return this.normalX;
   }

   public int getNormalY() {
      return this.normalY;
   }

   public int getNormalZ() {
      return this.normalZ;
   }

   public boolean isHasNormal() {
      return this.hasNormal;
   }

   public boolean isInheritDirection() {
      return this.inheritDirection;
   }

   public boolean isActive() {
      return this.active;
   }

   public boolean isDebugAffectAll() {
      return this.debugAffectAll;
   }

   public void setAttachedFaceIndex(int attachedFaceIndex) {
      this.attachedFaceIndex = attachedFaceIndex;
   }

   public void setAttachedVertexIndex(int attachedVertexIndex) {
      this.attachedVertexIndex = attachedVertexIndex;
   }

   public void setAttachedMarkerId(int attachedMarkerId) {
      this.attachedMarkerId = attachedMarkerId;
   }

   public void setInheritDirection(boolean inheritDirection) {
      this.inheritDirection = inheritDirection;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public void setDebugAffectAll(boolean debugAffectAll) {
      this.debugAffectAll = debugAffectAll;
   }
}
