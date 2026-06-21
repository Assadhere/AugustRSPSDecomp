package net.runelite.client.plugins.particles.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import net.runelite.api.Client;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.particles.config.ParticleEffectorConfig;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.util.EffectorSnapshot;

public class ParticleSystem {
   private final Map<Integer, ParticleEmitterConfig> emitterConfigs = new HashMap();
   private final Map<Integer, ParticleEffectorConfig> effectorConfigs = new HashMap();
   private final List<ParticleEmitter> activeEmitters = new ArrayList();
   private final List<ParticleEffector> allEffectors = new ArrayList();
   private final Map<Integer, ParticleEffector> worldEffectorCache = new HashMap();
   private final List<ParticleEffector> localEffectorCache = new ArrayList();
   private final List<ParticleEffector> embeddedEffectorPool = new ArrayList();
   private int embeddedEffectorPoolIndex = 0;
   private EffectorSnapshot[] effectorSnapshots = new EffectorSnapshot[64];
   private int activeSnapshotCount = 0;
   private EffectorSnapshot[] filteredSnapshots = new EffectorSnapshot[64];
   private int filteredSnapshotCount = 0;
   private int clientCycle = 0;
   private int maxParticlesPerEmitter = 1000;
   private int maxTotalParticles = 10000;
   private boolean enabled = true;
   private int maxEmitterDistanceSq = 0;
   private int gpuDrawDistanceSq = 0;
   private final AtomicInteger totalParticleCount = new AtomicInteger();
   private static final int MAX_WORKER_THREADS = 7;
   private final int nWorkers = Math.max(1, Math.min(7, Runtime.getRuntime().availableProcessors() - 1));
   private final ExecutorService workerPool;
   private final UpdateTask[] workerTasks;
   private final Future<Void>[] pendingFutures = new Future[7];
   private int pendingFutureCount = 0;
   private final ArrayDeque<ParticleEmitter> pendingAddEmitters = new ArrayDeque();
   private final ArrayDeque<ParticleEmitter> pendingRemoveEmitters = new ArrayDeque();
   private int lastClientCycle = -1;
   private int lastDeltaTime = 1;
   private static final int MAX_DELTA_TIME = 5;
   private static final int GAP_RESCAN_THRESHOLD = 50;
   private static final float CULL_PAD = 1.1F;
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
   private int lastWorkerFrustumCulledCount;
   private int lastWorkerDepthCulledCount;

   public void setMaxEmitterDistanceTiles(int tiles) {
      if (tiles >= 184) {
         this.maxEmitterDistanceSq = 0;
      } else {
         int localUnits = tiles * 128;
         this.maxEmitterDistanceSq = localUnits * localUnits;
      }

   }

   public void setGpuDrawDistanceTiles(int tiles) {
      if (tiles <= 0) {
         this.gpuDrawDistanceSq = 0;
      } else {
         int localUnits = tiles * 128;
         this.gpuDrawDistanceSq = localUnits * localUnits;
      }

   }

   public ParticleSystem() {
      this.workerPool = Executors.newFixedThreadPool(this.nWorkers, (r) -> {
         Thread t = new Thread(r, "particle-worker");
         t.setDaemon(true);
         return t;
      });
      this.workerTasks = new UpdateTask[this.nWorkers];

      for(int i = 0; i < this.nWorkers; ++i) {
         this.workerTasks[i] = new UpdateTask();
      }

   }

   public void syncClientCycle(int gameCycle) {
      if (this.lastClientCycle == -1) {
         this.clientCycle = gameCycle;
      }

   }

   public void registerEmitterConfig(ParticleEmitterConfig config) {
      config.postDecode();
      this.emitterConfigs.put(config.getId(), config);
   }

   public void registerEffectorConfig(ParticleEffectorConfig config) {
      config.postDecode();
      this.effectorConfigs.put(config.getId(), config);
   }

   public ParticleEmitterConfig getEmitterConfig(int id) {
      return (ParticleEmitterConfig)this.emitterConfigs.get(id);
   }

   public ParticleEffectorConfig getEffectorConfig(int id) {
      return (ParticleEffectorConfig)this.effectorConfigs.get(id);
   }

   public ParticleEmitter createEmitter(int configId, int worldX, int worldY, int worldZ, WorldPoint point, int baseX, int baseY, WorldView worldView, WorldEntity worldEntity) {
      ParticleEmitterConfig config = (ParticleEmitterConfig)this.emitterConfigs.get(configId);
      if (config == null) {
         return null;
      } else {
         ParticleEmitter emitter = new ParticleEmitter(config);
         emitter.setSystem(this);
         emitter.start(worldX, worldY, worldZ, this.clientCycle, point, baseX, baseY, worldView, worldEntity);
         this.pendingAddEmitters.add(emitter);
         return emitter;
      }
   }

   public ParticleEmitter createEmitterWithColourOverride(int configId, int worldX, int worldY, int worldZ, WorldPoint point, int baseX, int baseY, WorldView worldView, WorldEntity worldEntity, int minColourArgb, int maxColourArgb, int targetColourArgb) {
      ParticleEmitterConfig base = (ParticleEmitterConfig)this.emitterConfigs.get(configId);
      if (base == null) {
         return null;
      } else {
         ParticleEmitterConfig config = base.copyWithColourOverride(minColourArgb, maxColourArgb, targetColourArgb);
         ParticleEmitter emitter = new ParticleEmitter(config);
         emitter.setSystem(this);
         emitter.start(worldX, worldY, worldZ, this.clientCycle, point, baseX, baseY, worldView, worldEntity);
         this.pendingAddEmitters.add(emitter);
         return emitter;
      }
   }

   public ParticleEffector createGlobalEffector(int configId, int worldX, int worldY, int worldZ) {
      ParticleEffectorConfig config = (ParticleEffectorConfig)this.effectorConfigs.get(configId);
      if (config == null) {
         return null;
      } else {
         ParticleEffector effector = new ParticleEffector(config);
         effector.setPosition(worldX, worldY, worldZ);
         this.allEffectors.add(effector);
         int scope = config.getScope();
         if (scope == 1) {
            this.worldEffectorCache.put(configId, effector);
         } else {
            this.localEffectorCache.add(effector);
         }

         return effector;
      }
   }

   public void refreshEffectorsByConfigId(int configId) {
      Iterator var2 = this.allEffectors.iterator();

      while(var2.hasNext()) {
         ParticleEffector effector = (ParticleEffector)var2.next();
         if (effector.getConfig() != null && effector.getConfig().getId() == configId) {
            effector.refreshTransformedForce();
         }
      }

   }

   void onParticleRemoved() {
      this.totalParticleCount.decrementAndGet();
   }

   boolean tryAddParticle() {
      int current;
      do {
         current = this.totalParticleCount.get();
         if (current >= this.maxTotalParticles) {
            return false;
         }
      } while(!this.totalParticleCount.compareAndSet(current, current + 1));

      return true;
   }

   public void setRenderCullState(float fwdX, float fwdY, float fwdZ, float rightX, float rightZ, float upX, float upY, float upZ, float frustumW, float frustumH, float halfSizeBase, float camX, float camY, float camZ) {
      this.cullFwdX = fwdX;
      this.cullFwdY = fwdY;
      this.cullFwdZ = fwdZ;
      this.cullRightX = rightX;
      this.cullRightZ = rightZ;
      this.cullUpX = upX;
      this.cullUpY = upY;
      this.cullUpZ = upZ;
      this.cullFrustumW = frustumW * 1.1F;
      this.cullFrustumH = frustumH * 1.1F;
      this.cullHalfSizeBase = halfSizeBase;
      this.cullCamX = camX;
      this.cullCamY = camY;
      this.cullCamZ = camZ;
      this.hasCullState = true;
   }

   public void awaitPhysics() {
      for(int i = 0; i < this.pendingFutureCount; ++i) {
         try {
            this.pendingFutures[i].get();
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
         } catch (ExecutionException var4) {
         }

         this.pendingFutures[i] = null;
      }

      this.pendingFutureCount = 0;
   }

   public void shutdown() {
      for(int i = 0; i < this.pendingFutureCount; ++i) {
         if (this.pendingFutures[i] != null) {
            this.pendingFutures[i].cancel(true);
         }
      }

      this.pendingFutureCount = 0;
      this.workerPool.shutdownNow();
   }

   public boolean update(Client client) {
      int currentCycle = client.getGameCycle();
      if (!this.enabled) {
         this.lastClientCycle = currentCycle;
         return false;
      } else if (this.lastClientCycle == -1) {
         this.lastClientCycle = currentCycle;
         this.clientCycle = currentCycle;
         return false;
      } else {
         this.awaitPhysics();
         int workerFrustumCulled = 0;
         int workerDepthCulled = 0;
         int rawDelta = 0;

         for(int sz = this.activeEmitters.size(); rawDelta < sz; ++rawDelta) {
            ParticleEmitter e = (ParticleEmitter)this.activeEmitters.get(rawDelta);
            workerFrustumCulled += e.getWorkerFrustumCulledCount();
            workerDepthCulled += e.getWorkerDepthCulledCount();
         }

         this.lastWorkerFrustumCulledCount = workerFrustumCulled;
         this.lastWorkerDepthCulledCount = workerDepthCulled;

         while(!this.pendingRemoveEmitters.isEmpty()) {
            ParticleEmitter e = (ParticleEmitter)this.pendingRemoveEmitters.poll();
            e.stop();
            this.activeEmitters.remove(e);
         }

         while(!this.pendingAddEmitters.isEmpty()) {
            this.activeEmitters.add((ParticleEmitter)this.pendingAddEmitters.poll());
         }

         for(rawDelta = this.activeEmitters.size() - 1; rawDelta >= 0; --rawDelta) {
            if (((ParticleEmitter)this.activeEmitters.get(rawDelta)).isFinished()) {
               this.activeEmitters.remove(rawDelta);
            }
         }

         int deltaTime;
         for(rawDelta = this.allEffectors.size() - 1; rawDelta >= 0; --rawDelta) {
            ParticleEffector eff = (ParticleEffector)this.allEffectors.get(rawDelta);
            if (!eff.isActive()) {
               this.allEffectors.remove(rawDelta);
               deltaTime = eff.getConfig().getScope();
               if (deltaTime == 1) {
                  this.worldEffectorCache.remove(eff.getConfig().getId());
               } else {
                  this.localEffectorCache.remove(eff);
               }
            }
         }

         rawDelta = currentCycle - this.lastClientCycle;
         if (rawDelta < 0) {
            this.lastClientCycle = currentCycle;
            this.clientCycle = currentCycle;
            return true;
         } else if (rawDelta == 0) {
            return false;
         } else {
            boolean resumedFromGap = rawDelta >= 50;
            deltaTime = Math.min(rawDelta, 5);
            this.clientCycle += deltaTime;
            this.lastClientCycle = currentCycle;
            this.lastDeltaTime = deltaTime;
            this.embeddedEffectorPoolIndex = 0;
            this.activeSnapshotCount = 0;
            int emitterCount = 0;

            int nTasks;
            for(nTasks = this.allEffectors.size(); emitterCount < nTasks; ++emitterCount) {
               ParticleEffector eff = (ParticleEffector)this.allEffectors.get(emitterCount);
               if (eff.isActive()) {
                  if (this.activeSnapshotCount >= this.effectorSnapshots.length) {
                     EffectorSnapshot[] newSnapshots = new EffectorSnapshot[this.effectorSnapshots.length * 2];
                     System.arraycopy(this.effectorSnapshots, 0, newSnapshots, 0, this.activeSnapshotCount);
                     this.effectorSnapshots = newSnapshots;
                  }

                  if (this.effectorSnapshots[this.activeSnapshotCount] == null) {
                     this.effectorSnapshots[this.activeSnapshotCount] = new EffectorSnapshot();
                  }

                  this.effectorSnapshots[this.activeSnapshotCount].populate(eff);
                  ++this.activeSnapshotCount;
               }
            }

            emitterCount = this.activeEmitters.size();

            for(nTasks = 0; nTasks < emitterCount; ++nTasks) {
               ParticleEmitter emitter = (ParticleEmitter)this.activeEmitters.get(nTasks);
               emitter.prepareForTick();
               if (this.hasCullState) {
                  emitter.setCullState(this.cullFwdX, this.cullFwdY, this.cullFwdZ, this.cullRightX, this.cullRightZ, this.cullUpX, this.cullUpY, this.cullUpZ, this.cullFrustumW, this.cullFrustumH, this.cullHalfSizeBase, this.cullCamX, this.cullCamY, this.cullCamZ);
                  emitter.setGpuDrawDistanceSq(this.gpuDrawDistanceSq);
                  float dx = (float)emitter.getWorldX() - this.cullCamX;
                  float dz = (float)emitter.getWorldZ() - this.cullCamZ;
                  float distSq = dx * dx + dz * dz;
                  emitter.setDistanceCulled(this.maxEmitterDistanceSq > 0 && distSq > (float)this.maxEmitterDistanceSq || this.gpuDrawDistanceSq > 0 && distSq > (float)this.gpuDrawDistanceSq);
               }

               this.buildFilteredSnapshots(emitter);
               emitter.prepareSnapshots(this.filteredSnapshots, this.filteredSnapshotCount, this);
            }

            if (emitterCount > 0) {
               nTasks = Math.min(this.nWorkers, emitterCount);
               int chunkSize = (emitterCount + nTasks - 1) / nTasks;

               for(int t = 0; t < nTasks; ++t) {
                  this.workerTasks[t].start = t * chunkSize;
                  this.workerTasks[t].end = Math.min((t + 1) * chunkSize, emitterCount);
                  this.pendingFutures[t] = this.workerPool.submit(this.workerTasks[t]);
               }

               this.pendingFutureCount = nTasks;
            }

            return resumedFromGap;
         }
      }
   }

   private void buildFilteredSnapshots(ParticleEmitter emitter) {
      this.filteredSnapshotCount = 0;
      ParticleEmitterConfig config = emitter.getConfig();
      int[] localFilter = config.getLocalEffectorFilter();
      int[] globalFilter = config.getGlobalEffectors();
      boolean hasAnyFilter = localFilter != null && localFilter.length > 0 || globalFilter != null && globalFilter.length > 0;

      for(int i = 0; i < this.activeSnapshotCount; ++i) {
         EffectorSnapshot snapshot = this.effectorSnapshots[i];
         boolean include = false;
         if (snapshot.debugAffectAll) {
            include = true;
         } else if (!hasAnyFilter) {
            include = true;
         } else {
            int scope = snapshot.scope;
            int j;
            if (scope == 1) {
               if (globalFilter != null) {
                  for(j = 0; j < globalFilter.length; ++j) {
                     if (globalFilter[j] == snapshot.effectorId) {
                        include = true;
                        break;
                     }
                  }
               }
            } else if (localFilter != null) {
               for(j = 0; j < localFilter.length; ++j) {
                  if (localFilter[j] == snapshot.effectorId) {
                     include = true;
                     break;
                  }
               }
            }
         }

         if (include) {
            if (this.filteredSnapshotCount >= this.filteredSnapshots.length) {
               EffectorSnapshot[] newArray = new EffectorSnapshot[this.filteredSnapshots.length * 2];
               System.arraycopy(this.filteredSnapshots, 0, newArray, 0, this.filteredSnapshotCount);
               this.filteredSnapshots = newArray;
            }

            this.filteredSnapshots[this.filteredSnapshotCount++] = snapshot;
         }
      }

   }

   public void addEmbeddedEffectorSnapshots(ParticleEmitter emitter, EffectorSnapshot[] targetArray, int startIndex) {
      ParticleEmitterConfig config = emitter.getConfig();
      int[] embedded = config.getEmbeddedEffectors();
      if (embedded != null && embedded.length != 0) {
         for(int i = 0; i < embedded.length; ++i) {
            ParticleEffectorConfig effConfig = (ParticleEffectorConfig)this.effectorConfigs.get(embedded[i]);
            if (effConfig != null) {
               ParticleEffector eff = this.getPooledEffector(effConfig);
               eff.setPosition(emitter.getWorldX(), emitter.getWorldY(), emitter.getWorldZ());
               int idx = startIndex + i;
               if (idx >= targetArray.length) {
                  break;
               }

               if (targetArray[idx] == null) {
                  targetArray[idx] = new EffectorSnapshot();
               }

               targetArray[idx].populate(eff);
            }
         }

      }
   }

   private ParticleEffector getPooledEffector(ParticleEffectorConfig config) {
      ParticleEffector eff;
      if (this.embeddedEffectorPoolIndex < this.embeddedEffectorPool.size()) {
         eff = (ParticleEffector)this.embeddedEffectorPool.get(this.embeddedEffectorPoolIndex++);
         eff.reset(config);
      } else {
         eff = new ParticleEffector(config);
         this.embeddedEffectorPool.add(eff);
         ++this.embeddedEffectorPoolIndex;
      }

      return eff;
   }

   public int getTotalParticleCount() {
      return this.totalParticleCount.get();
   }

   public List<ParticleEmitter> getActiveEmitters() {
      return Collections.unmodifiableList(this.activeEmitters);
   }

   public void removeEmitter(ParticleEmitter emitter) {
      emitter.stop();
      this.pendingRemoveEmitters.add(emitter);
   }

   public void removeEffector(ParticleEffector effector) {
      effector.deactivate();
      this.allEffectors.remove(effector);
      int scope = effector.getConfig().getScope();
      if (scope == 1) {
         this.worldEffectorCache.remove(effector.getConfig().getId());
      } else {
         this.localEffectorCache.remove(effector);
      }

   }

   public void clear() {
      this.awaitPhysics();
      this.pendingAddEmitters.clear();
      this.pendingRemoveEmitters.clear();
      this.activeEmitters.clear();
      this.allEffectors.clear();
      this.worldEffectorCache.clear();
      this.localEffectorCache.clear();
      this.totalParticleCount.set(0);
      this.lastClientCycle = -1;
   }

   public int getEmitterCount() {
      return this.activeEmitters.size();
   }

   public Map<Integer, ParticleEmitterConfig> getEmitterConfigs() {
      return this.emitterConfigs;
   }

   public Map<Integer, ParticleEffectorConfig> getEffectorConfigs() {
      return this.effectorConfigs;
   }

   public List<ParticleEffector> getAllEffectors() {
      return this.allEffectors;
   }

   public EffectorSnapshot[] getEffectorSnapshots() {
      return this.effectorSnapshots;
   }

   public int getActiveSnapshotCount() {
      return this.activeSnapshotCount;
   }

   public int getClientCycle() {
      return this.clientCycle;
   }

   public void setMaxParticlesPerEmitter(int maxParticlesPerEmitter) {
      this.maxParticlesPerEmitter = maxParticlesPerEmitter;
   }

   public int getMaxParticlesPerEmitter() {
      return this.maxParticlesPerEmitter;
   }

   public void setMaxTotalParticles(int maxTotalParticles) {
      this.maxTotalParticles = maxTotalParticles;
   }

   public int getMaxTotalParticles() {
      return this.maxTotalParticles;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public int getLastWorkerFrustumCulledCount() {
      return this.lastWorkerFrustumCulledCount;
   }

   public int getLastWorkerDepthCulledCount() {
      return this.lastWorkerDepthCulledCount;
   }

   private final class UpdateTask implements Callable<Void> {
      int start;
      int end;

      public Void call() {
         int s = this.start;
         int e = this.end;
         int cycle = ParticleSystem.this.clientCycle;
         int dt = ParticleSystem.this.lastDeltaTime;

         for(int i = s; i < e; ++i) {
            ((ParticleEmitter)ParticleSystem.this.activeEmitters.get(i)).update(cycle, dt);
         }

         return null;
      }
   }
}
