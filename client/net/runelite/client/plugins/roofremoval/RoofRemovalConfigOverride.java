package net.runelite.client.plugins.roofremoval;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

enum RoofRemovalConfigOverride {
   POH(RoofRemovalConfig::overridePOH, new Integer[]{7257, 7534, 7535, 7790, 7791, 8046, 8047, 8302, 8303});

   private final Predicate<RoofRemovalConfig> enabled;
   private final List<Integer> regions;

   private RoofRemovalConfigOverride(Predicate enabled, Integer... regions) {
      this.enabled = enabled;
      this.regions = Arrays.asList(regions);
   }

   public Predicate<RoofRemovalConfig> getEnabled() {
      return this.enabled;
   }

   public List<Integer> getRegions() {
      return this.regions;
   }
}
