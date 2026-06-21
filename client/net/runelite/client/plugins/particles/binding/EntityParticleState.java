package net.runelite.client.plugins.particles.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.client.plugins.particles.core.ParticleEffector;
import net.runelite.client.plugins.particles.core.ParticleEmitter;

public class EntityParticleState {
   public final List<ParticleEmitter> emitters = new ArrayList();
   public final List<ParticleEffector> effectors = new ArrayList();
   public int lastFaceCount = -1;
   public int pendingSpotanimScanCycle = -1;
   public String lastColourString = null;
   public final Map<Integer, int[]> colourOverrides = new HashMap();

   public boolean hasPendingSpotanimScan() {
      return this.pendingSpotanimScanCycle >= 0;
   }

   public void clearPendingSpotanimScan() {
      this.pendingSpotanimScanCycle = -1;
   }

   public void clear() {
      this.emitters.clear();
      this.effectors.clear();
      this.lastFaceCount = -1;
      this.pendingSpotanimScanCycle = -1;
      this.lastColourString = null;
      this.colourOverrides.clear();
   }

   public boolean isEmpty() {
      return this.emitters.isEmpty() && this.effectors.isEmpty() && this.pendingSpotanimScanCycle < 0;
   }

   public boolean isOnWorldEntity() {
      int i = 0;

      for(int size = this.emitters.size(); i < size; ++i) {
         if (((ParticleEmitter)this.emitters.get(i)).isOnWorldEntity()) {
            return true;
         }
      }

      return false;
   }
}
