package net.runelite.client.plugins.particles.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.util.CollisionContext;
import net.runelite.client.plugins.particles.util.EffectorSnapshot;

public class ParticleEmitter {
   public static final int[] SINE_TABLE = new int[16384];
   public static final int[] COSINE_TABLE = new int[16384];
   private ParticleEmitterConfig config;
   private final List<Particle> particles = new ArrayList();
   private int worldX;
   private int worldY;
   private int worldZ;
   private int prevWorldX;
   private int prevWorldY;
   private int prevWorldZ;
   private int prevBaseX;
   private int prevBaseY;
   private boolean hasPrevWorldPos;
   private boolean hasSpawnArea = false;
   private int v1X;
   private int v1Y;
   private int v1Z;
   private int v2X;
   private int v2Y;
   private int v2Z;
   private int v3X;
   private int v3Y;
   private int v3Z;
   private int normalX = 0;
   private int normalY = 32767;
   private int normalZ = 0;
   private int yawBase;
   private int yawSpread;
   private int pitchBase;
   private int pitchSpread;
   private boolean normalComputed = false;
   private int cachedCenterX;
   private int cachedCenterY;
   private int cachedCenterZ;
   private int prevV1X;
   private int prevV1Y;
   private int prevV1Z;
   private int prevV2X;
   private int prevV2Y;
   private int prevV2Z;
   private int prevV3X;
   private int prevV3Y;
   private int prevV3Z;
   private int startCycle;
   private int lastUpdateCycle;
   private int lastAttachmentCycle;
   private int emitCounter;
   private boolean needsInitialBurst;
   private boolean active = true;
   private boolean hasMoved;
   private boolean distanceCulled;
   private int gpuDrawDistanceSq;
   private WorldPoint worldPoint;
   private int baseX;
   private int baseY;
   private int tickBaseX;
   private int tickBaseY;
   private float cullFwdX;
   private float cullFwdY;
   private float cullFwdZ;
   private float cullRightX;
   private float cullRightZ;
   private float cullUpX;
   private float cullUpY;
   private float cullUpZ;
   private float cullFrustumW;
   private float cullFrustumH;
   private float cullHalfSizeBase;
   private float cullCamX;
   private float cullCamY;
   private float cullCamZ;
   private boolean hasCullState;
   private int workerFrustumCulledCount;
   private int workerDepthCulledCount;
   private int plane;
   private WorldView worldView;
   private WorldEntity worldEntity;
   private int worldEntityYOffset;
   private int attachedFaceIndex = -1;
   private int attachedMarkerId = -1;
   private ParticleSystem system;
   private EffectorSnapshot[] preparedSnapshots;
   private int preparedSnapshotCount;
   private final CollisionContext collisionContext = new CollisionContext();
   private final ArrayList<Particle> particlePool = new ArrayList();
   private static final int LOCAL_FINE_SCALE = 128;
   private int renderCount;
   private float[] renderX = new float[16];
   private float[] renderY = new float[16];
   private float[] renderZ = new float[16];
   private int[] renderEncColour = new int[16];
   private float[] renderScales = new float[16];
   private int[] renderTexIds = new int[16];
   private int[] renderSpriteMeta = new int[16];
   private int[] renderRotations = new int[16];

   void setGpuDrawDistanceSq(int sq) {
      this.gpuDrawDistanceSq = sq;
   }

   public boolean isAttachedToFace() {
      return this.attachedFaceIndex >= 0;
   }

   public ParticleEmitter(ParticleEmitterConfig config) {
      this.config = config;
   }

   public void start(int worldX, int worldY, int worldZ, int clientCycle, WorldPoint worldPoint, int baseX, int baseY, WorldView worldView, WorldEntity worldEntity) {
      this.worldX = worldX;
      this.worldY = worldY;
      this.worldZ = worldZ;
      this.startCycle = clientCycle;
      this.lastUpdateCycle = clientCycle;
      this.lastAttachmentCycle = clientCycle;
      this.emitCounter = 0;
      this.needsInitialBurst = true;
      this.hasMoved = false;
      this.active = true;
      this.worldPoint = worldPoint;
      this.baseX = baseX;
      this.baseY = baseY;
      this.worldView = worldView;
      this.worldEntity = worldEntity;
      if (!this.normalComputed) {
         this.computeYawPitchFromNormal();
      }

   }

   public void setPosition(int worldX, int worldY, int worldZ, int clientCycle) {
      this.worldX = worldX;
      this.worldY = worldY;
      this.worldZ = worldZ;
      this.lastAttachmentCycle = clientCycle;
   }

   public void setPosition(int worldX, int worldY, int worldZ) {
      this.worldX = worldX;
      this.worldY = worldY;
      this.worldZ = worldZ;
   }

   public void updatePosition(int worldX, int worldY, int worldZ, WorldPoint worldPoint, int baseX, int baseY) {
      this.worldX = worldX;
      this.worldY = worldY;
      this.worldZ = worldZ;
      this.worldPoint = worldPoint;
      this.baseX = baseX;
      this.baseY = baseY;
   }

   public void setSpawnArea(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3, int clientCycle) {
      this.lastAttachmentCycle = clientCycle;
      if (this.hasSpawnArea) {
         this.prevV1X = this.v1X;
         this.prevV1Y = this.v1Y;
         this.prevV1Z = this.v1Z;
         this.prevV2X = this.v2X;
         this.prevV2Y = this.v2Y;
         this.prevV2Z = this.v2Z;
         this.prevV3X = this.v3X;
         this.prevV3Y = this.v3Y;
         this.prevV3Z = this.v3Z;
      }

      this.hasSpawnArea = true;
      this.v1X = x1;
      this.v1Y = y1;
      this.v1Z = z1;
      this.v2X = x2;
      this.v2Y = y2;
      this.v2Z = z2;
      this.v3X = x3;
      this.v3Y = y3;
      this.v3Z = z3;
      int centerX = (x1 + x2 + x3) / 3;
      int centerY = (y1 + y2 + y3) / 3;
      int centerZ = (z1 + z2 + z3) / 3;
      this.worldX = centerX;
      this.worldY = centerY;
      this.worldZ = centerZ;
      if (this.cachedCenterX != centerX || this.cachedCenterY != centerY || this.cachedCenterZ != centerZ) {
         this.cachedCenterX = centerX;
         this.cachedCenterY = centerY;
         this.cachedCenterZ = centerZ;
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
         this.computeYawPitchFromNormal();
      }

      this.initPreviousTriangle();
   }

   private void shiftSpawnArea(int shiftX, int shiftZ) {
      if (this.hasSpawnArea) {
         this.v1X += shiftX;
         this.v1Z += shiftZ;
         this.v2X += shiftX;
         this.v2Z += shiftZ;
         this.v3X += shiftX;
         this.v3Z += shiftZ;
         this.prevV1X += shiftX;
         this.prevV1Z += shiftZ;
         this.prevV2X += shiftX;
         this.prevV2Z += shiftZ;
         this.prevV3X += shiftX;
         this.prevV3Z += shiftZ;
         this.worldX += shiftX;
         this.worldZ += shiftZ;
         this.cachedCenterX += shiftX;
         this.cachedCenterZ += shiftZ;
      }
   }

   public void renderTimeShift(int currentX, int currentY, int currentZ) {
      if (this.config.isLocalSpace()) {
         if (this.hasPrevWorldPos) {
            int dx = currentX - this.worldX;
            int dy = currentY - this.worldY;
            int dz = currentZ - this.worldZ;
            if ((dx | dy | dz) != 0) {
               this.worldX = currentX;
               this.worldY = currentY;
               this.worldZ = currentZ;
               if (this.hasSpawnArea) {
                  this.v1X += dx;
                  this.v1Y += dy;
                  this.v1Z += dz;
                  this.v2X += dx;
                  this.v2Y += dy;
                  this.v2Z += dz;
                  this.v3X += dx;
                  this.v3Y += dy;
                  this.v3Z += dz;
               }

               int i = 0;

               for(int sz = this.particles.size(); i < sz; ++i) {
                  Particle p = (Particle)this.particles.get(i);
                  if (p.isAlive()) {
                     p.shiftPosition(dx, dy, dz);
                  }
               }

               float fdx = (float)dx;
               float fdy = (float)dy;
               float fdz = (float)dz;

               for(int i = 0; i < this.renderCount; ++i) {
                  float[] var10000 = this.renderX;
                  var10000[i] += fdx;
                  var10000 = this.renderY;
                  var10000[i] += fdy;
                  var10000 = this.renderZ;
                  var10000[i] += fdz;
               }

               this.prevWorldX = currentX;
               this.prevWorldY = currentY;
               this.prevWorldZ = currentZ;
            }
         }
      }
   }

   public void shiftHeight(int deltaY) {
      if (this.hasSpawnArea) {
         this.v1Y += deltaY;
         this.v2Y += deltaY;
         this.v3Y += deltaY;
         this.prevV1Y += deltaY;
         this.prevV2Y += deltaY;
         this.prevV3Y += deltaY;
         this.cachedCenterY += deltaY;
      }

      this.worldY += deltaY;
   }

   public void setDirection(int yaw, int pitch) {
      this.normalComputed = true;
      this.yawSpread = this.config.getSpreadYawMax() - this.config.getSpreadYawMin();
      this.yawBase = yaw + this.config.getSpreadYawMin() - (this.yawSpread >> 1);
      this.pitchSpread = this.config.getSpreadPitchMax() - this.config.getSpreadPitchMin();
      this.pitchBase = pitch + this.config.getSpreadPitchMin() - (this.pitchSpread >> 1);
   }

   private void computeYawPitchFromNormal() {
      this.normalComputed = true;
      if (this.config.getSpreadYawMax() > 0 || this.config.getSpreadPitchMax() > 0) {
         int baseYaw = (int)(2607.5945876176133 * Math.atan2((double)this.normalZ, (double)this.normalX));
         double horizontalDist = Math.sqrt((double)(this.normalX * this.normalX + this.normalZ * this.normalZ));
         int basePitch = (int)(Math.atan2((double)this.normalY, horizontalDist) * 2607.5945876176133);
         this.yawSpread = this.config.getSpreadYawMax() - this.config.getSpreadYawMin();
         this.yawBase = baseYaw + this.config.getSpreadYawMin() - (this.yawSpread >> 1);
         this.pitchSpread = this.config.getSpreadPitchMax() - this.config.getSpreadPitchMin();
         this.pitchBase = basePitch + this.config.getSpreadPitchMin() - (this.pitchSpread >> 1);
      }

   }

   private void initPreviousTriangle() {
      if (this.prevV1X == 0 && this.prevV1Y == 0 && this.prevV1Z == 0 && this.prevV2X == 0 && this.prevV2Y == 0 && this.prevV2Z == 0 && this.prevV3X == 0 && this.prevV3Y == 0 && this.prevV3Z == 0) {
         this.prevV1X = this.v1X;
         this.prevV1Y = this.v1Y;
         this.prevV1Z = this.v1Z;
         this.prevV2X = this.v2X;
         this.prevV2Y = this.v2Y;
         this.prevV2Z = this.v2Z;
         this.prevV3X = this.v3X;
         this.prevV3Y = this.v3Y;
         this.prevV3Z = this.v3Z;
      }

   }

   public void prepareForTick() {
      if (this.config.isLocalSpace() && this.hasPrevWorldPos && this.baseX == this.prevBaseX && this.baseY == this.prevBaseY) {
         int dx = this.worldX - this.prevWorldX;
         int dy = this.worldY - this.prevWorldY;
         int dz = this.worldZ - this.prevWorldZ;
         if ((dx | dy | dz) != 0) {
            int i = 0;

            for(int sz = this.particles.size(); i < sz; ++i) {
               Particle p = (Particle)this.particles.get(i);
               if (p.isAlive()) {
                  p.shiftPosition(dx, dy, dz);
               }
            }
         }
      }

      this.prevWorldX = this.worldX;
      this.prevWorldY = this.worldY;
      this.prevWorldZ = this.worldZ;
      this.prevBaseX = this.baseX;
      this.prevBaseY = this.baseY;
      this.hasPrevWorldPos = true;
      if (this.active && this.worldView != null) {
         this.tickBaseX = this.worldView.getBaseX();
         this.tickBaseY = this.worldView.getBaseY();
         this.collisionContext.populate(this, this.config, this.plane);
      }
   }

   public void setCullState(float fwdX, float fwdY, float fwdZ, float rightX, float rightZ, float upX, float upY, float upZ, float frustumW, float frustumH, float halfSizeBase, float camX, float camY, float camZ) {
      this.cullFwdX = fwdX;
      this.cullFwdY = fwdY;
      this.cullFwdZ = fwdZ;
      this.cullRightX = rightX;
      this.cullRightZ = rightZ;
      this.cullUpX = upX;
      this.cullUpY = upY;
      this.cullUpZ = upZ;
      this.cullFrustumW = frustumW;
      this.cullFrustumH = frustumH;
      this.cullHalfSizeBase = halfSizeBase;
      this.cullCamX = camX;
      this.cullCamY = camY;
      this.cullCamZ = camZ;
      this.hasCullState = true;
   }

   public void checkAliveParticles() {
      if (this.active && this.worldView != null) {
         int baseX = this.tickBaseX;
         int baseY = this.tickBaseY;
         int i;
         int size;
         if (baseX == this.baseX && baseY == this.baseY) {
            i = 0;

            for(size = this.particles.size(); i < size; ++i) {
               ((Particle)this.particles.get(i)).checkAliveStatus(this.collisionContext);
            }

         } else {
            i = baseX - this.baseX;
            size = baseY - this.baseY;
            int shiftX = -i * 128;
            int shiftY = -size * 128;
            WorldPoint old = this.worldPoint;
            int extOffset = 40;
            int extMinX = baseX - extOffset;
            int extMinY = baseY - extOffset;
            int i;
            int size;
            if (old.getX() >= extMinX && old.getX() < extMinX + 184 && old.getY() >= extMinY && old.getY() < extMinY + 184) {
               this.shiftSpawnArea(shiftX, shiftY);
               i = 0;

               for(size = this.particles.size(); i < size; ++i) {
                  Particle p = (Particle)this.particles.get(i);
                  if (p.isAlive()) {
                     p.shiftPosition(shiftX, shiftY);
                  }
               }

               this.baseX = baseX;
               this.baseY = baseY;
            } else {
               this.active = false;
               i = 0;

               for(size = this.particles.size(); i < size; ++i) {
                  ((Particle)this.particles.get(i)).kill();
               }

            }
         }
      }
   }

   public boolean update(int clientCycle, int deltaTime) {
      this.checkAliveParticles();
      if (!this.active) {
         this.updateParticles(deltaTime);
         return this.attachedFaceIndex >= 0 || !this.particles.isEmpty();
      } else if (this.distanceCulled) {
         this.updateParticles(deltaTime);
         this.lastUpdateCycle = clientCycle;
         return this.attachedFaceIndex >= 0 || !this.particles.isEmpty();
      } else {
         if (this.lastAttachmentCycle == clientCycle && this.lastAttachmentCycle != this.lastUpdateCycle) {
            this.hasMoved = true;
         } else {
            this.hasMoved = false;
            this.lastAttachmentCycle = clientCycle;
         }

         int particlesToSpawn;
         if (this.config.getEmissionCycleDuration() != -1) {
            int elapsed = clientCycle - this.startCycle;
            if (!this.config.isLoopEmission() && elapsed >= this.config.getEmissionCycleDuration()) {
               this.updateParticles(deltaTime);
               this.lastUpdateCycle = clientCycle;
               return true;
            }

            particlesToSpawn = elapsed % this.config.getEmissionCycleDuration();
            if (this.config.isEmitOnlyBeforeTime()) {
               if (particlesToSpawn >= this.config.getEmissionTimeThreshold()) {
                  this.updateParticles(deltaTime);
                  this.lastUpdateCycle = clientCycle;
                  return true;
               }
            } else if (particlesToSpawn < this.config.getEmissionTimeThreshold()) {
               this.updateParticles(deltaTime);
               this.lastUpdateCycle = clientCycle;
               return true;
            }
         }

         boolean allowSpawning;
         if (this.needsInitialBurst && this.config.getInitialSpawnCount() > 0) {
            allowSpawning = !this.hasMoved;
            if (allowSpawning) {
               for(particlesToSpawn = 0; particlesToSpawn < this.config.getInitialSpawnCount(); ++particlesToSpawn) {
                  this.spawnParticle();
               }
            }

            this.needsInitialBurst = false;
         }

         allowSpawning = !this.hasMoved;
         if (allowSpawning) {
            this.emitCounter += (int)(((double)this.config.getMinSpawnCount() + ThreadLocalRandom.current().nextDouble() * (double)(this.config.getMaxSpawnCount() - this.config.getMinSpawnCount())) * (double)deltaTime);
            if (this.emitCounter > 63) {
               particlesToSpawn = this.emitCounter >> 6;
               this.emitCounter &= 63;

               for(int i = 0; i < particlesToSpawn; ++i) {
                  this.spawnParticle();
               }
            }
         }

         this.updateParticles(deltaTime);
         this.lastUpdateCycle = clientCycle;
         return true;
      }
   }

   public void prepareSnapshots(EffectorSnapshot[] filtered, int filteredCount, ParticleSystem system) {
      int[] embedded = this.config.getEmbeddedEffectors();
      int embeddedCount = embedded != null ? embedded.length : 0;
      int totalCount = filteredCount + embeddedCount;
      if (this.preparedSnapshots == null || this.preparedSnapshots.length < totalCount) {
         this.preparedSnapshots = new EffectorSnapshot[Math.max(totalCount, 8)];
      }

      if (filtered != null && filteredCount > 0) {
         System.arraycopy(filtered, 0, this.preparedSnapshots, 0, filteredCount);
      }

      if (embeddedCount > 0 && system != null) {
         system.addEmbeddedEffectorSnapshots(this, this.preparedSnapshots, filteredCount);
      }

      this.preparedSnapshotCount = totalCount;
   }

   private void updateParticles(int deltaTime) {
      EffectorSnapshot[] snapshots = this.preparedSnapshots;
      int snapshotCount = this.preparedSnapshotCount;
      int texId = 0;
      int frameCount = 0;
      int texCols = 0;
      int texRows = 0;
      if (this.config.getTextureIndex() >= 0) {
         texId = this.config.getTextureIndex() + 1;
         frameCount = this.config.getSpriteFrameCount();
         texCols = Math.min(this.config.getSpriteColumns(), 15);
         texRows = Math.min(this.config.getSpriteRows(), 15);
      }

      boolean onWorldEntity = this.isOnWorldEntity();
      int renderIdx = 0;
      int workerFrustumCulled = 0;
      int workerDepthCulled = 0;
      int i = 0;
      int size = this.particles.size();
      boolean doCull = this.hasCullState;
      float cFwdX = this.cullFwdX;
      float cFwdY = this.cullFwdY;
      float cFwdZ = this.cullFwdZ;
      float cRightX = this.cullRightX;
      float cRightZ = this.cullRightZ;
      float cUpX = this.cullUpX;
      float cUpY = this.cullUpY;
      float cUpZ = this.cullUpZ;
      float cFrustumW = this.cullFrustumW;
      float cFrustumH = this.cullFrustumH;
      float cHalfSizeBase = this.cullHalfSizeBase;
      float cCamX = this.cullCamX;
      float cCamY = this.cullCamY;
      float cCamZ = this.cullCamZ;
      int cGpuDistSq = this.gpuDrawDistanceSq;

      while(true) {
         while(i < size) {
            Particle p = (Particle)this.particles.get(i);
            int wy;
            if (cGpuDistSq > 0) {
               float ddx = (float)p.getWorldX() - cCamX;
               float ddz = (float)p.getWorldZ() - cCamZ;
               if (ddx * ddx + ddz * ddz > (float)cGpuDistSq) {
                  this.particlePool.add(p);
                  if (this.system != null) {
                     this.system.onParticleRemoved();
                  }

                  --size;
                  wy = size;
                  if (i != wy) {
                     this.particles.set(i, (Particle)this.particles.get(wy));
                  }

                  this.particles.remove(wy);
                  continue;
               }
            }

            if (!p.update(deltaTime, snapshots, snapshotCount)) {
               this.particlePool.add(p);
               if (this.system != null) {
                  this.system.onParticleRemoved();
               }

               --size;
               if (i != size) {
                  this.particles.set(i, (Particle)this.particles.get(size));
               }

               this.particles.remove(size);
            } else {
               int encColour = p.getEncodedColour();
               if (encColour >>> 24 != 255) {
                  if (renderIdx >= this.renderX.length) {
                     this.growRenderSnapshot(renderIdx + 1);
                  }

                  int wx = p.getWorldX();
                  wy = p.getWorldY();
                  int wz = p.getWorldZ();
                  if (onWorldEntity) {
                     LocalPoint t = this.transformToMainWorld(wx, wz);
                     if (t != null) {
                        wx = t.getX();
                        wz = t.getY();
                     }

                     wy += this.worldEntityYOffset;
                  }

                  if (doCull) {
                     float dx = (float)wx - cCamX;
                     float dy = (float)wy - cCamY;
                     float dz = (float)wz - cCamZ;
                     int depth = (int)(dx * cFwdX + dy * cFwdY + dz * cFwdZ);
                     if (depth <= 0) {
                        ++workerDepthCulled;
                        ++i;
                        continue;
                     }

                     float halfSize = p.getRenderScale() * cHalfSizeBase;
                     if (Math.abs(dx * cRightX + dz * cRightZ) > (float)depth * cFrustumW + halfSize) {
                        ++workerFrustumCulled;
                        ++i;
                        continue;
                     }

                     if (Math.abs(dx * cUpX + dy * cUpY + dz * cUpZ) > (float)depth * cFrustumH + halfSize) {
                        ++workerFrustumCulled;
                        ++i;
                        continue;
                     }
                  }

                  int spriteMeta = 0;
                  if (texId > 0 && frameCount > 1) {
                     int frame = p.getAge() * frameCount / p.getInitialLifetime();
                     if (frame >= frameCount) {
                        frame = frameCount - 1;
                     }

                     spriteMeta = texCols << 12 | texRows << 8 | frame & 255;
                  } else if (texId > 0) {
                     spriteMeta = texCols << 12 | texRows << 8;
                  }

                  this.renderX[renderIdx] = (float)wx;
                  this.renderY[renderIdx] = (float)wy;
                  this.renderZ[renderIdx] = (float)wz;
                  this.renderEncColour[renderIdx] = encColour;
                  this.renderScales[renderIdx] = p.getRenderScale();
                  this.renderTexIds[renderIdx] = texId;
                  this.renderSpriteMeta[renderIdx] = spriteMeta;
                  this.renderRotations[renderIdx] = p.getRotation();
                  ++renderIdx;
               }

               ++i;
            }
         }

         this.renderCount = renderIdx;
         this.workerFrustumCulledCount = workerFrustumCulled;
         this.workerDepthCulledCount = workerDepthCulled;
         return;
      }
   }

   private Particle acquireParticle() {
      int sz = this.particlePool.size();
      return sz > 0 ? (Particle)this.particlePool.remove(sz - 1) : new Particle();
   }

   private void growRenderSnapshot(int minCap) {
      int newLen = Math.max(this.renderX.length * 2, minCap);
      this.renderX = Arrays.copyOf(this.renderX, newLen);
      this.renderY = Arrays.copyOf(this.renderY, newLen);
      this.renderZ = Arrays.copyOf(this.renderZ, newLen);
      this.renderEncColour = Arrays.copyOf(this.renderEncColour, newLen);
      this.renderScales = Arrays.copyOf(this.renderScales, newLen);
      this.renderTexIds = Arrays.copyOf(this.renderTexIds, newLen);
      this.renderSpriteMeta = Arrays.copyOf(this.renderSpriteMeta, newLen);
      this.renderRotations = Arrays.copyOf(this.renderRotations, newLen);
   }

   public int getRenderCount() {
      return this.renderCount;
   }

   public float[] getRenderX() {
      return this.renderX;
   }

   public float[] getRenderY() {
      return this.renderY;
   }

   public float[] getRenderZ() {
      return this.renderZ;
   }

   public int[] getRenderEncColour() {
      return this.renderEncColour;
   }

   public float[] getRenderScales() {
      return this.renderScales;
   }

   public int[] getRenderTexIds() {
      return this.renderTexIds;
   }

   public int[] getRenderSpriteMeta() {
      return this.renderSpriteMeta;
   }

   public int[] getRenderRotations() {
      return this.renderRotations;
   }

   private void spawnParticle() {
      if (this.system == null || this.system.tryAddParticle()) {
         ThreadLocalRandom rng = ThreadLocalRandom.current();
         int dirX;
         int dirY;
         int dirZ;
         int speed;
         int lifetime;
         int scale;
         int rotation;
         int colour;
         int spawnX;
         if (this.config.getSpreadYawMax() <= 0 && this.config.getSpreadPitchMax() <= 0) {
            dirX = this.normalX;
            dirZ = this.normalZ;
            dirY = this.normalY;
         } else {
            speed = (int)((double)this.yawSpread * rng.nextDouble()) + this.yawBase;
            speed &= 16383;
            lifetime = SINE_TABLE[speed];
            scale = COSINE_TABLE[speed];
            rotation = this.pitchBase + (int)((double)this.pitchSpread * rng.nextDouble());
            rotation &= 8191;
            colour = SINE_TABLE[rotation];
            spawnX = COSINE_TABLE[rotation];
            dirX = scale * colour >> 13;
            dirY = -1 * (spawnX << 1);
            dirZ = colour * lifetime >> 13;
         }

         speed = (int)(rng.nextDouble() * (double)(this.config.getMaxSpeed() - this.config.getMinSpeed())) + this.config.getMinSpeed();
         lifetime = (int)(rng.nextDouble() * (double)(this.config.getMaxLifetime() - this.config.getMinLifetime())) + this.config.getMinLifetime();
         scale = (int)(rng.nextDouble() * (double)(this.config.getMaxScale() - this.config.getMinScale())) + this.config.getMinScale();
         rotation = (int)(rng.nextDouble() * (double)(this.config.getMaxRotation() - this.config.getMinRotation())) + this.config.getMinRotation();
         if (this.config.isUniformColourVariation()) {
            double colourFactor = rng.nextDouble();
            colour = (int)((double)this.config.getStartBlue() + (double)this.config.getDeltaBlue() * colourFactor) | (int)((double)this.config.getStartGreen() + colourFactor * (double)this.config.getDeltaGreen()) << 8 | (int)((double)this.config.getStartRed() + (double)this.config.getDeltaRed() * colourFactor) << 16 | (int)(rng.nextDouble() * (double)this.config.getDeltaAlpha() + (double)this.config.getStartAlpha()) << 24;
         } else {
            colour = (int)((double)this.config.getStartBlue() + rng.nextDouble() * (double)this.config.getDeltaBlue()) | (int)((double)this.config.getStartGreen() + rng.nextDouble() * (double)this.config.getDeltaGreen()) << 8 | (int)(rng.nextDouble() * (double)this.config.getDeltaRed() + (double)this.config.getStartRed()) << 16 | (int)((double)this.config.getStartAlpha() + (double)this.config.getDeltaAlpha() * rng.nextDouble()) << 24;
         }

         int spawnY;
         int spawnZ;
         if (this.hasSpawnArea) {
            float r1 = rng.nextFloat();
            float r2 = rng.nextFloat();
            if (r2 + r1 > 1.0F) {
               r1 = 1.0F - r1;
               r2 = 1.0F - r2;
            }

            float r3 = 1.0F - r2 - r1;
            int currentX = (int)((float)this.v1X * r3 + (float)this.v3X * r2 + (float)this.v2X * r1);
            int currentY = (int)((float)this.v3Y * r2 + (float)this.v2Y * r1 + (float)this.v1Y * r3);
            int currentZ = (int)((float)this.v1Z * r3 + (float)this.v2Z * r1 + (float)this.v3Z * r2);
            int prevX = (int)((float)this.prevV1X * r3 + (float)this.prevV3X * r2 + (float)this.prevV2X * r1);
            int prevY = (int)((float)this.prevV3Y * r2 + (float)this.prevV2Y * r1 + (float)this.prevV1Y * r3);
            int prevZ = (int)((float)this.prevV1Z * r3 + (float)this.prevV2Z * r1 + (float)this.prevV3Z * r2);
            float t = this.config.isLocalSpace() ? 1.0F : rng.nextFloat();
            int deltaX = currentX - prevX;
            int deltaY = currentY - prevY;
            int deltaZ = currentZ - prevZ;
            spawnX = prevX + (int)((float)deltaX * t);
            spawnY = prevY + (int)((float)deltaY * t);
            spawnZ = prevZ + (int)((float)deltaZ * t);
         } else {
            spawnX = this.worldX;
            spawnY = this.worldY;
            spawnZ = this.worldZ;
         }

         Particle particle = this.acquireParticle();
         particle.init(this, spawnX, spawnY, spawnZ, dirX, dirY, dirZ, speed, lifetime, colour, scale, rotation, this.config.getTargetRotation(), this.config.getRotationTransitionTicks(), this.config.getTextureIndex());
         this.particles.add(particle);
      }
   }

   public void stop() {
      this.active = false;
   }

   public void resetStaleTimer(int clientCycle) {
      this.lastAttachmentCycle = clientCycle;
      this.lastUpdateCycle = clientCycle;
   }

   public void reset() {
      int i = 0;

      for(int sz = this.particles.size(); i < sz; ++i) {
         Particle p = (Particle)this.particles.get(i);
         p.kill();
         this.particlePool.add(p);
         if (this.system != null) {
            this.system.onParticleRemoved();
         }
      }

      this.particles.clear();
      this.emitCounter = 0;
      this.needsInitialBurst = false;
      this.hasMoved = false;
      this.active = false;
      this.renderCount = 0;
      this.hasSpawnArea = false;
      this.cachedCenterX = 0;
      this.cachedCenterY = 0;
      this.cachedCenterZ = 0;
      this.hasPrevWorldPos = false;
      this.collisionContext.invalidate();
   }

   public void invalidateSceneCache() {
      this.collisionContext.invalidate();
   }

   public boolean isFinished() {
      if (this.attachedFaceIndex >= 0) {
         return false;
      } else {
         return !this.active && this.particles.isEmpty();
      }
   }

   public int getParticleCount() {
      return this.particles.size();
   }

   public boolean isOnWorldEntity() {
      return this.worldEntity != null;
   }

   public LocalPoint transformToMainWorld(int localX, int localZ) {
      if (this.worldEntity != null && this.worldView != null) {
         LocalPoint local = new LocalPoint(localX, localZ, this.worldView);
         return this.worldEntity.transformToMainWorld(local);
      } else {
         return new LocalPoint(localX, localZ);
      }
   }

   public ParticleEmitterConfig getConfig() {
      return this.config;
   }

   public void setConfig(ParticleEmitterConfig config) {
      this.config = config;
   }

   public List<Particle> getParticles() {
      return this.particles;
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

   public int getV1X() {
      return this.v1X;
   }

   public int getV1Y() {
      return this.v1Y;
   }

   public int getV1Z() {
      return this.v1Z;
   }

   public int getV2X() {
      return this.v2X;
   }

   public int getV2Y() {
      return this.v2Y;
   }

   public int getV2Z() {
      return this.v2Z;
   }

   public int getV3X() {
      return this.v3X;
   }

   public int getV3Y() {
      return this.v3Y;
   }

   public int getV3Z() {
      return this.v3Z;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public boolean isActive() {
      return this.active;
   }

   public void setDistanceCulled(boolean distanceCulled) {
      this.distanceCulled = distanceCulled;
   }

   public int getWorkerFrustumCulledCount() {
      return this.workerFrustumCulledCount;
   }

   public int getWorkerDepthCulledCount() {
      return this.workerDepthCulledCount;
   }

   public void setPlane(int plane) {
      this.plane = plane;
   }

   public int getPlane() {
      return this.plane;
   }

   public WorldView getWorldView() {
      return this.worldView;
   }

   public WorldEntity getWorldEntity() {
      return this.worldEntity;
   }

   public void setWorldEntityYOffset(int worldEntityYOffset) {
      this.worldEntityYOffset = worldEntityYOffset;
   }

   public int getWorldEntityYOffset() {
      return this.worldEntityYOffset;
   }

   public void setAttachedFaceIndex(int attachedFaceIndex) {
      this.attachedFaceIndex = attachedFaceIndex;
   }

   public int getAttachedFaceIndex() {
      return this.attachedFaceIndex;
   }

   public void setAttachedMarkerId(int attachedMarkerId) {
      this.attachedMarkerId = attachedMarkerId;
   }

   public int getAttachedMarkerId() {
      return this.attachedMarkerId;
   }

   public void setSystem(ParticleSystem system) {
      this.system = system;
   }

   public ParticleSystem getSystem() {
      return this.system;
   }

   static {
      double d = 3.834951969714103E-4;

      for(int i = 0; i < 16384; ++i) {
         SINE_TABLE[i] = (int)(16384.0 * Math.sin((double)i * d));
         COSINE_TABLE[i] = (int)(16384.0 * Math.cos((double)i * d));
      }

   }
}
