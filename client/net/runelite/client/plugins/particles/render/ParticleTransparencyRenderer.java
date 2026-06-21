package net.runelite.client.plugins.particles.render;

import java.nio.IntBuffer;
import java.util.List;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.plugins.particles.core.Particle;
import net.runelite.client.plugins.particles.core.ParticleEmitter;
import net.runelite.client.plugins.particles.core.ParticleSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParticleTransparencyRenderer {
   private static final Logger log = LoggerFactory.getLogger(ParticleTransparencyRenderer.class);
   private Particle[] behindParticles = new Particle[1000];
   private Particle[] inFrontParticles = new Particle[1000];
   private int behindCount = 0;
   private int inFrontCount = 0;
   private int[] behindDepths = new int[1000];
   private int[] inFrontDepths = new int[1000];
   private int camX;
   private int camY;
   private int camZ;
   private float camForwardX;
   private float camForwardY;
   private float camForwardZ;
   private boolean depthBinnedModeActive = false;
   private float[] sceneDepthBuffer;
   private int depthBufferWidth;
   private int depthBufferHeight;

   public void beginFrame(int cameraX, int cameraY, int cameraZ, float cameraYaw, float cameraPitch) {
      if (this.depthBinnedModeActive) {
         this.endFrame();
      }

      this.camX = cameraX;
      this.camY = cameraY;
      this.camZ = cameraZ;
      float sinYaw = (float)Math.sin((double)cameraYaw);
      float cosYaw = (float)Math.cos((double)cameraYaw);
      float sinPitch = (float)Math.sin((double)cameraPitch);
      float cosPitch = (float)Math.cos((double)cameraPitch);
      this.camForwardX = sinYaw * cosPitch;
      this.camForwardY = -sinPitch;
      this.camForwardZ = cosYaw * cosPitch;
      this.behindCount = 0;
      this.inFrontCount = 0;
      this.depthBinnedModeActive = false;
   }

   public void prepareDepthBins(ParticleSystem system, int sceneDepthTexture, int viewportWidth, int viewportHeight, float[] projectionMatrix) {
      if (system != null) {
         this.depthBinnedModeActive = true;
         this.behindCount = 0;
         this.inFrontCount = 0;
         this.collectAllParticlesBehind(system);
      }
   }

   private void collectAllParticlesBehind(ParticleSystem system) {
      List<ParticleEmitter> emitters = system.getActiveEmitters();

      for(int e = 0; e < emitters.size(); ++e) {
         ParticleEmitter emitter = (ParticleEmitter)emitters.get(e);
         boolean onWorldEntity = emitter.isOnWorldEntity();
         List<Particle> particles = emitter.getParticles();

         for(int i = 0; i < particles.size(); ++i) {
            Particle p = (Particle)particles.get(i);
            if (p.isAlive()) {
               int alpha = p.getRenderColour() >> 24 & 255;
               if (alpha != 0) {
                  int localX = p.getWorldX();
                  int localZ = p.getWorldZ();
                  if (onWorldEntity) {
                     LocalPoint transformed = emitter.transformToMainWorld(localX, localZ);
                     if (transformed != null) {
                        localX = transformed.getX();
                        localZ = transformed.getY();
                     }
                  }

                  float dx = (float)(localX - this.camX);
                  float dy = (float)(p.getWorldY() - this.camY);
                  float dz = (float)(localZ - this.camZ);
                  int depth = (int)(dx * this.camForwardX + dy * this.camForwardY + dz * this.camForwardZ);
                  this.addToBehind(p, depth);
               }
            }
         }
      }

   }

   private void addToBehind(Particle p, int depth) {
      if (this.behindCount >= this.behindParticles.length) {
         this.expandBehindArrays();
      }

      this.behindParticles[this.behindCount] = p;
      this.behindDepths[this.behindCount] = depth;
      ++this.behindCount;
   }

   private void addToInFront(Particle p, int depth) {
      if (this.inFrontCount >= this.inFrontParticles.length) {
         this.expandInFrontArrays();
      }

      this.inFrontParticles[this.inFrontCount] = p;
      this.inFrontDepths[this.inFrontCount] = depth;
      ++this.inFrontCount;
   }

   private void expandBehindArrays() {
      int newSize = this.behindParticles.length * 2;
      Particle[] newParticles = new Particle[newSize];
      int[] newDepths = new int[newSize];
      System.arraycopy(this.behindParticles, 0, newParticles, 0, this.behindCount);
      System.arraycopy(this.behindDepths, 0, newDepths, 0, this.behindCount);
      this.behindParticles = newParticles;
      this.behindDepths = newDepths;
   }

   private void expandInFrontArrays() {
      int newSize = this.inFrontParticles.length * 2;
      Particle[] newParticles = new Particle[newSize];
      int[] newDepths = new int[newSize];
      System.arraycopy(this.inFrontParticles, 0, newParticles, 0, this.inFrontCount);
      System.arraycopy(this.inFrontDepths, 0, newDepths, 0, this.inFrontCount);
      this.inFrontParticles = newParticles;
      this.inFrontDepths = newDepths;
   }

   public int renderBehindParticles(IntBuffer buffer, Particle3DRenderer renderer, float baseSize, int offsetX, int offsetZ) {
      if (this.depthBinnedModeActive && this.behindCount != 0) {
         this.sortParticlesByDepth(this.behindParticles, this.behindDepths, this.behindCount, true);

         for(int i = 0; i < this.behindCount; ++i) {
            renderer.uploadParticleBillboard(buffer, this.behindParticles[i], baseSize, offsetX, offsetZ);
         }

         return this.behindCount;
      } else {
         return 0;
      }
   }

   public int renderInFrontParticles(IntBuffer buffer, Particle3DRenderer renderer, float baseSize, int offsetX, int offsetZ) {
      if (this.depthBinnedModeActive && this.inFrontCount != 0) {
         this.sortParticlesByDepth(this.inFrontParticles, this.inFrontDepths, this.inFrontCount, true);

         for(int i = 0; i < this.inFrontCount; ++i) {
            renderer.uploadParticleBillboard(buffer, this.inFrontParticles[i], baseSize, offsetX, offsetZ);
         }

         return this.inFrontCount;
      } else {
         return 0;
      }
   }

   private void sortParticlesByDepth(Particle[] particles, int[] depths, int count, boolean backToFront) {
      for(int i = 1; i < count; ++i) {
         Particle pKey = particles[i];
         int dKey = depths[i];
         int j = i - 1;
         if (backToFront) {
            while(j >= 0 && depths[j] < dKey) {
               particles[j + 1] = particles[j];
               depths[j + 1] = depths[j];
               --j;
            }
         } else {
            while(j >= 0 && depths[j] > dKey) {
               particles[j + 1] = particles[j];
               depths[j + 1] = depths[j];
               --j;
            }
         }

         particles[j + 1] = pKey;
         depths[j + 1] = dKey;
      }

   }

   public void endFrame() {
      this.depthBinnedModeActive = false;
   }

   public int getBehindCount() {
      return this.behindCount;
   }

   public int getInFrontCount() {
      return this.inFrontCount;
   }

   public boolean isDepthBinnedModeActive() {
      return this.depthBinnedModeActive;
   }
}
