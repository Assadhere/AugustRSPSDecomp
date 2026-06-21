package net.runelite.client.plugins.particles.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParticleWhitelist {
   private final List<Integer> graphicIds;
   private final List<Integer> wornItemIds;
   private final List<Integer> npcIds;
   private final List<Integer> locIds;
   private final List<Integer> kitIds;
   private transient Set<Integer> graphicIdSet;
   private transient Set<Integer> wornItemIdSet;
   private transient Set<Integer> npcIdSet;
   private transient Set<Integer> locIdSet;
   private transient Set<Integer> kitIdSet;

   public ParticleWhitelist() {
      this.graphicIds = Collections.emptyList();
      this.wornItemIds = Collections.emptyList();
      this.npcIds = Collections.emptyList();
      this.locIds = Collections.emptyList();
      this.kitIds = Collections.emptyList();
      this.graphicIdSet = Collections.emptySet();
      this.wornItemIdSet = Collections.emptySet();
      this.npcIdSet = Collections.emptySet();
      this.locIdSet = Collections.emptySet();
      this.kitIdSet = Collections.emptySet();
   }

   public ParticleWhitelist(Set<Integer> graphicIds, Set<Integer> wornItemIds, Set<Integer> npcIds, Set<Integer> locIds, Set<Integer> kitIds) {
      this.graphicIds = null;
      this.wornItemIds = null;
      this.npcIds = null;
      this.locIds = null;
      this.kitIds = null;
      this.graphicIdSet = graphicIds != null ? graphicIds : Collections.emptySet();
      this.wornItemIdSet = wornItemIds != null ? wornItemIds : Collections.emptySet();
      this.npcIdSet = npcIds != null ? npcIds : Collections.emptySet();
      this.locIdSet = locIds != null ? locIds : Collections.emptySet();
      this.kitIdSet = kitIds != null ? kitIds : Collections.emptySet();
   }

   public void postDecode() {
      this.graphicIdSet = (Set)(this.graphicIds != null ? new HashSet(this.graphicIds) : Collections.emptySet());
      this.wornItemIdSet = (Set)(this.wornItemIds != null ? new HashSet(this.wornItemIds) : Collections.emptySet());
      this.npcIdSet = (Set)(this.npcIds != null ? new HashSet(this.npcIds) : Collections.emptySet());
      this.locIdSet = (Set)(this.locIds != null ? new HashSet(this.locIds) : Collections.emptySet());
      this.kitIdSet = (Set)(this.kitIds != null ? new HashSet(this.kitIds) : Collections.emptySet());
   }

   public boolean isGraphicWhitelisted(int graphicId) {
      return this.graphicIdSet.contains(graphicId);
   }

   public boolean isWornItemWhitelisted(int itemId) {
      return this.wornItemIdSet.contains(itemId);
   }

   public boolean isNpcWhitelisted(int npcId) {
      return this.npcIdSet.contains(npcId);
   }

   public boolean isLocWhitelisted(int locId) {
      return this.locIdSet.contains(locId);
   }

   public boolean isKitWhitelisted(int kitId) {
      return this.kitIdSet.contains(kitId);
   }

   public Set<Integer> getGraphicIds() {
      return this.graphicIdSet;
   }

   public Set<Integer> getWornItemIds() {
      return this.wornItemIdSet;
   }

   public Set<Integer> getNpcIds() {
      return this.npcIdSet;
   }

   public Set<Integer> getLocIds() {
      return this.locIdSet;
   }

   public Set<Integer> getKitIds() {
      return this.kitIdSet;
   }
}
