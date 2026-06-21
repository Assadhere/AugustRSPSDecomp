package net.runelite.client.plugins.particles.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.runelite.client.plugins.particles.binding.GameObjectModelScanResult;
import net.runelite.client.plugins.particles.core.ParticleEffector;
import net.runelite.client.plugins.particles.core.ParticleEmitter;

public class ScanScratchPad {
   private final List<List<Integer>> intListPool = new ArrayList();
   private final List<List<ParticleEmitter>> emitterListPool = new ArrayList();
   private final List<List<ParticleEffector>> effectorListPool = new ArrayList();
   public final List<GameObjectModelScanResult.MarkerFace> emitterFaces = new ArrayList();
   public final List<GameObjectModelScanResult.MarkerFace> effectorFaces = new ArrayList();
   public final Map<Integer, List<Integer>> modelEmitterFaces = new HashMap();
   public final Map<Integer, List<Integer>> modelEffectorFaces = new HashMap();
   public final Map<Integer, List<ParticleEmitter>> existingEmittersByMarkerId = new HashMap();
   public final Map<Integer, List<ParticleEffector>> existingEffectorsByMarkerId = new HashMap();
   private int[] facePriorities = new int[256];
   private int facePrioritiesSize = 0;
   public final Set<Integer> claimedFaces = new HashSet();
   public final Set<Integer> allEmitterMarkerIds = new HashSet();
   public final Set<Integer> allEffectorMarkerIds = new HashSet();
   public final List<ParticleEmitter> newEmitters = new ArrayList();
   public final List<ParticleEmitter> emittersToRemove = new ArrayList();
   public final List<ParticleEffector> newEffectors = new ArrayList();
   public final List<ParticleEffector> effectorsToRemove = new ArrayList();
   public final List<ParticleEmitter> tempEmitterList = new ArrayList();
   public final List<ParticleEffector> tempEffectorList = new ArrayList();

   public void clear() {
      this.emitterFaces.clear();
      this.effectorFaces.clear();
      this.returnIntListsToPool(this.modelEmitterFaces);
      this.returnIntListsToPool(this.modelEffectorFaces);
      this.returnEmitterListsToPool(this.existingEmittersByMarkerId);
      this.returnEffectorListsToPool(this.existingEffectorsByMarkerId);
      this.modelEmitterFaces.clear();
      this.modelEffectorFaces.clear();
      this.existingEmittersByMarkerId.clear();
      this.existingEffectorsByMarkerId.clear();
      this.facePrioritiesSize = 0;
      this.claimedFaces.clear();
      this.allEmitterMarkerIds.clear();
      this.allEffectorMarkerIds.clear();
      this.newEmitters.clear();
      this.emittersToRemove.clear();
      this.newEffectors.clear();
      this.effectorsToRemove.clear();
      this.tempEmitterList.clear();
      this.tempEffectorList.clear();
   }

   public void prepareFacePriorities(int faceCount) {
      if (faceCount > this.facePriorities.length) {
         this.facePriorities = new int[faceCount + 64];
      }

      for(int i = 0; i < faceCount; ++i) {
         this.facePriorities[i] = -1;
      }

      this.facePrioritiesSize = faceCount;
   }

   public void setFacePriority(int faceIndex, int priority) {
      if (faceIndex >= 0 && faceIndex < this.facePrioritiesSize) {
         this.facePriorities[faceIndex] = priority;
      }

   }

   public int getFacePriority(int faceIndex, int defaultValue) {
      if (faceIndex >= 0 && faceIndex < this.facePrioritiesSize) {
         int priority = this.facePriorities[faceIndex];
         return priority >= 0 ? priority : defaultValue;
      } else {
         return defaultValue;
      }
   }

   public List<Integer> getOrCreateEmitterFaceList(int markerId) {
      List<Integer> list = (List)this.modelEmitterFaces.get(markerId);
      if (list == null) {
         list = this.getPooledIntList();
         this.modelEmitterFaces.put(markerId, list);
      }

      return list;
   }

   public List<Integer> getOrCreateEffectorFaceList(int markerId) {
      List<Integer> list = (List)this.modelEffectorFaces.get(markerId);
      if (list == null) {
         list = this.getPooledIntList();
         this.modelEffectorFaces.put(markerId, list);
      }

      return list;
   }

   public List<ParticleEmitter> getOrCreateEmitterList(int markerId) {
      List<ParticleEmitter> list = (List)this.existingEmittersByMarkerId.get(markerId);
      if (list == null) {
         list = this.getPooledEmitterList();
         this.existingEmittersByMarkerId.put(markerId, list);
      }

      return list;
   }

   public List<ParticleEffector> getOrCreateEffectorList(int markerId) {
      List<ParticleEffector> list = (List)this.existingEffectorsByMarkerId.get(markerId);
      if (list == null) {
         list = this.getPooledEffectorList();
         this.existingEffectorsByMarkerId.put(markerId, list);
      }

      return list;
   }

   private List<Integer> getPooledIntList() {
      return (List)(this.intListPool.isEmpty() ? new ArrayList() : (List)this.intListPool.remove(this.intListPool.size() - 1));
   }

   private List<ParticleEmitter> getPooledEmitterList() {
      return (List)(this.emitterListPool.isEmpty() ? new ArrayList() : (List)this.emitterListPool.remove(this.emitterListPool.size() - 1));
   }

   private List<ParticleEffector> getPooledEffectorList() {
      return (List)(this.effectorListPool.isEmpty() ? new ArrayList() : (List)this.effectorListPool.remove(this.effectorListPool.size() - 1));
   }

   private void returnIntListsToPool(Map<Integer, List<Integer>> map) {
      Iterator var2 = map.values().iterator();

      while(var2.hasNext()) {
         List<Integer> list = (List)var2.next();
         list.clear();
         this.intListPool.add(list);
      }

   }

   private void returnEmitterListsToPool(Map<Integer, List<ParticleEmitter>> map) {
      Iterator var2 = map.values().iterator();

      while(var2.hasNext()) {
         List<ParticleEmitter> list = (List)var2.next();
         list.clear();
         this.emitterListPool.add(list);
      }

   }

   private void returnEffectorListsToPool(Map<Integer, List<ParticleEffector>> map) {
      Iterator var2 = map.values().iterator();

      while(var2.hasNext()) {
         List<ParticleEffector> list = (List)var2.next();
         list.clear();
         this.effectorListPool.add(list);
      }

   }
}
