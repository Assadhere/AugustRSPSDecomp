package net.runelite.client.plugins.particles.render;

import java.nio.IntBuffer;
import java.util.List;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.core.Particle;
import net.runelite.client.plugins.particles.core.ParticleEmitter;
import net.runelite.client.plugins.particles.core.ParticleSystem;

public class Particle3DRenderer {
   private static final int INSTANCE_SIZE = 24;
   private static final int INSTANCE_SIZE_INTS = 6;
   public static final float BASE_PARTICLE_SIZE = 4.0F;
   private float camRightX;
   private float camRightZ;
   private float camUpX;
   private float camUpY;
   private float camUpZ;
   private float camForwardX;
   private float camForwardY;
   private float camForwardZ;
   private float frustumScaleW;
   private float frustumScaleH;
   private int camX;
   private int camY;
   private int camZ;
   private int[] tmpBatch = new int[6000];
   private static final int DEPTH_BUCKET_COUNT = 1600;
   private static final int PARTICLES_PER_BUCKET = 64;
   private final int[][] depthBuckets = new int[1600][64];
   private final int[] bucketCounts = new int[1600];
   private int[] overflowList = new int[256];
   private int overflowCount;
   private int[] particleDepths = new int[1000];
   private int[] renderCountSnapshot = new int[64];
   private int maxBucketUsed = 0;
   private int[] batchData = new int[6000];
   private int batchCount;
   private int batchOffset;
   private int lastTotalCount;
   private int lastDepthCulledCount;
   private int lastFrustumCulledCount;

   public void updateCamera(float cameraYaw, float cameraPitch, int cameraX, int cameraY, int cameraZ, int scale, int halfViewW, int halfViewH) {
      this.camX = cameraX;
      this.camY = cameraY;
      this.camZ = cameraZ;
      float sinYaw = (float)Math.sin((double)cameraYaw);
      float cosYaw = (float)Math.cos((double)cameraYaw);
      float sinPitch = (float)Math.sin((double)cameraPitch);
      float cosPitch = (float)Math.cos((double)cameraPitch);
      this.camRightX = cosYaw;
      this.camRightZ = sinYaw;
      this.camUpX = sinPitch * sinYaw;
      this.camUpY = cosPitch;
      this.camUpZ = -sinPitch * cosYaw;
      this.camForwardX = -sinYaw * cosPitch;
      this.camForwardY = sinPitch;
      this.camForwardZ = cosYaw * cosPitch;
      float invScale = scale > 0 ? 1.0F / (float)scale : 0.0F;
      this.frustumScaleW = (float)halfViewW * invScale;
      this.frustumScaleH = (float)halfViewH * invScale;
   }

   public int getRequiredBufferSize(int particleCount) {
      return particleCount * 24;
   }

   public int render(IntBuffer buffer, ParticleSystem system, float baseSize, int offsetX, int offsetZ) {
      if (system == null) {
         return 0;
      } else {
         int camPosX = this.camX;
         int camPosY = this.camY;
         int camPosZ = this.camZ;
         List<ParticleEmitter> emitters = system.getActiveEmitters();
         int emitterCount = emitters.size();
         if (this.renderCountSnapshot.length < emitterCount) {
            this.renderCountSnapshot = new int[emitterCount * 2];
         }

         int totalCapacity = 0;

         int collectedCount;
         int totalCount;
         for(collectedCount = 0; collectedCount < emitterCount; ++collectedCount) {
            totalCount = ((ParticleEmitter)emitters.get(collectedCount)).getRenderCount();
            this.renderCountSnapshot[collectedCount] = totalCount;
            totalCapacity += totalCount;
         }

         if (this.tmpBatch.length < totalCapacity * 6) {
            this.tmpBatch = new int[totalCapacity * 6];
            this.particleDepths = new int[totalCapacity];
         }

         if (this.batchData.length < totalCapacity * 6) {
            this.batchData = new int[totalCapacity * 6];
         }

         collectedCount = 0;
         totalCount = 0;
         int depthCulled = 0;
         int minDepth = Integer.MAX_VALUE;
         int maxDepth = Integer.MIN_VALUE;
         float rFwdX = this.camForwardX;
         float rFwdY = this.camForwardY;
         float rFwdZ = this.camForwardZ;
         float scaleMultiplier = 4.0F * baseSize * 256.0F;
         int[] tmpBatch = this.tmpBatch;
         int[] particleDepths = this.particleDepths;
         int tmpBase = 0;

         for(int e = 0; e < emitterCount; ++e) {
            ParticleEmitter emitter = (ParticleEmitter)emitters.get(e);
            int pCount = this.renderCountSnapshot[e];
            if (pCount != 0) {
               totalCount += pCount;
               float[] rX = emitter.getRenderX();
               float[] rY = emitter.getRenderY();
               float[] rZ = emitter.getRenderZ();
               int[] rEncColour = emitter.getRenderEncColour();
               float[] rScales = emitter.getRenderScales();
               int[] rTexIds = emitter.getRenderTexIds();
               int[] rSpriteMeta = emitter.getRenderSpriteMeta();
               int[] rRotations = emitter.getRenderRotations();

               for(int i = 0; i < pCount; ++i) {
                  float wx = rX[i] + (float)offsetX;
                  float wz = rZ[i] + (float)offsetZ;
                  float wy = rY[i];
                  float dx = wx - (float)camPosX;
                  float dy = wy - (float)camPosY;
                  float dz = wz - (float)camPosZ;
                  int depth = (int)(dx * rFwdX + dy * rFwdY + dz * rFwdZ);
                  if (depth <= 0) {
                     ++depthCulled;
                  } else {
                     tmpBatch[tmpBase] = Float.floatToRawIntBits(wx);
                     tmpBatch[tmpBase + 1] = Float.floatToRawIntBits(wy);
                     tmpBatch[tmpBase + 2] = Float.floatToRawIntBits(wz);
                     tmpBatch[tmpBase + 3] = rEncColour[i];
                     tmpBatch[tmpBase + 4] = (int)(rScales[i] * scaleMultiplier) << 16 | rTexIds[i] & '\uffff';
                     tmpBatch[tmpBase + 5] = rSpriteMeta[i] << 16 | rRotations[i] * 4 & '\uffff';
                     particleDepths[collectedCount] = depth;
                     ++collectedCount;
                     tmpBase += 6;
                     if (depth < minDepth) {
                        minDepth = depth;
                     }

                     if (depth > maxDepth) {
                        maxDepth = depth;
                     }
                  }
               }
            }
         }

         this.lastTotalCount = totalCount;
         this.lastDepthCulledCount = depthCulled;
         this.lastFrustumCulledCount = 0;
         if (collectedCount == 0) {
            return 0;
         } else {
            this.placeIntoBuckets(collectedCount, minDepth, maxDepth);
            this.batchCount = 0;
            this.batchOffset = 0;
            this.renderSortedParticles();
            buffer.put(this.batchData, 0, this.batchCount * 6);
            return this.batchCount;
         }
      }
   }

   private void placeIntoBuckets(int particleCount, int minDepth, int maxDepth) {
      int depthRange;
      for(depthRange = 0; depthRange <= this.maxBucketUsed && depthRange < 1600; ++depthRange) {
         this.bucketCounts[depthRange] = 0;
      }

      this.maxBucketUsed = 0;
      this.overflowCount = 0;
      depthRange = maxDepth - minDepth;
      int shift = 0;
      if (depthRange + 2 > 1600) {
         while((depthRange >> shift) + 2 > 1600) {
            ++shift;
         }
      }

      int bucketCount = (depthRange >> shift) + 2;
      if (bucketCount > 1600) {
         bucketCount = 1600;
      }

      for(int i = 0; i < particleCount; ++i) {
         int bucketIndex = this.particleDepths[i] - minDepth >> shift;
         if (bucketIndex < 0) {
            bucketIndex = 0;
         }

         if (bucketIndex >= bucketCount) {
            bucketIndex = bucketCount - 1;
         }

         if (bucketIndex > this.maxBucketUsed) {
            this.maxBucketUsed = bucketIndex;
         }

         int srcBase = i * 6;
         if (this.bucketCounts[bucketIndex] < 64) {
            this.depthBuckets[bucketIndex][this.bucketCounts[bucketIndex]++] = srcBase;
         } else {
            if (this.overflowCount >= this.overflowList.length) {
               int[] grown = new int[this.overflowList.length * 2];
               System.arraycopy(this.overflowList, 0, grown, 0, this.overflowCount);
               this.overflowList = grown;
            }

            this.overflowList[this.overflowCount++] = srcBase;
         }
      }

   }

   private void renderSortedParticles() {
      int bucket;
      for(bucket = 0; bucket < this.overflowCount; ++bucket) {
         this.packParticleBillboard(this.overflowList[bucket]);
      }

      for(bucket = this.maxBucketUsed; bucket >= 0; --bucket) {
         int count = this.bucketCounts[bucket];
         if (count != 0) {
            int[] bucketArray = this.depthBuckets[bucket];

            for(int i = count - 1; i >= 0; --i) {
               this.packParticleBillboard(bucketArray[i]);
            }
         }
      }

   }

   void packParticleBillboard(int srcBase) {
      this.batchData[this.batchOffset] = this.tmpBatch[srcBase];
      this.batchData[this.batchOffset + 1] = this.tmpBatch[srcBase + 1];
      this.batchData[this.batchOffset + 2] = this.tmpBatch[srcBase + 2];
      this.batchData[this.batchOffset + 3] = this.tmpBatch[srcBase + 3];
      this.batchData[this.batchOffset + 4] = this.tmpBatch[srcBase + 4];
      this.batchData[this.batchOffset + 5] = this.tmpBatch[srcBase + 5];
      this.batchOffset += 6;
      ++this.batchCount;
   }

   void uploadParticleBillboard(IntBuffer buffer, Particle particle, float baseSize, int offsetX, int offsetZ) {
      int localX = particle.getWorldX();
      int localZ = particle.getWorldZ();
      int particleY = particle.getWorldY();
      ParticleEmitter emitter = particle.getEmitter();
      boolean onWorldEntity = emitter != null && emitter.isOnWorldEntity();
      if (onWorldEntity) {
         LocalPoint transformed = emitter.transformToMainWorld(localX, localZ);
         if (transformed == null) {
            return;
         }

         localX = transformed.getX();
         localZ = transformed.getY();
         particleY += emitter.getWorldEntityYOffset();
      }

      float x = (float)(localX + offsetX);
      float y = (float)particleY;
      float z = (float)(localZ + offsetZ);
      float size = particle.getRenderScale() * 4.0F * baseSize;
      int colorEncoded = particle.getEncodedColour();
      int texId = 0;
      int spriteMeta = 0;
      int frameCount;
      if (emitter != null) {
         ParticleEmitterConfig cfg = emitter.getConfig();
         if (cfg != null && cfg.getTextureIndex() >= 0) {
            texId = cfg.getTextureIndex() + 1;
            frameCount = cfg.getSpriteFrameCount();
            int frame = 0;
            int cols;
            int rows;
            if (frameCount > 1) {
               cols = particle.getAge();
               rows = particle.getInitialLifetime();
               frame = cols * frameCount / rows;
               if (frame >= frameCount) {
                  frame = frameCount - 1;
               }
            }

            cols = Math.min(cfg.getSpriteColumns(), 15);
            rows = Math.min(cfg.getSpriteRows(), 15);
            spriteMeta = cols << 12 | rows << 8 | frame & 255;
         }
      }

      int sizeFixed = (int)(size * 256.0F);
      frameCount = particle.getRotation() * 4 & '\uffff';
      buffer.put(Float.floatToRawIntBits(x));
      buffer.put(Float.floatToRawIntBits(y));
      buffer.put(Float.floatToRawIntBits(z));
      buffer.put(colorEncoded);
      buffer.put(sizeFixed << 16 | texId & '\uffff');
      buffer.put(spriteMeta << 16 | frameCount);
   }

   public int getCameraX() {
      return this.camX;
   }

   public int getCameraY() {
      return this.camY;
   }

   public int getCameraZ() {
      return this.camZ;
   }

   public float getCamRightX() {
      return this.camRightX;
   }

   public float getCamRightZ() {
      return this.camRightZ;
   }

   public float getCamUpX() {
      return this.camUpX;
   }

   public float getCamUpY() {
      return this.camUpY;
   }

   public float getCamUpZ() {
      return this.camUpZ;
   }

   public float getCamForwardX() {
      return this.camForwardX;
   }

   public float getCamForwardY() {
      return this.camForwardY;
   }

   public float getCamForwardZ() {
      return this.camForwardZ;
   }

   public float getFrustumScaleW() {
      return this.frustumScaleW;
   }

   public float getFrustumScaleH() {
      return this.frustumScaleH;
   }

   public int getLastTotalCount() {
      return this.lastTotalCount;
   }

   public int getLastDepthCulledCount() {
      return this.lastDepthCulledCount;
   }

   public int getLastFrustumCulledCount() {
      return this.lastFrustumCulledCount;
   }
}
