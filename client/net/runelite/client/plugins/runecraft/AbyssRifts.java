package net.runelite.client.plugins.runecraft;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.Predicate;

enum AbyssRifts {
   AIR_RIFT(25378, 556, RunecraftConfig::showAir),
   BLOOD_RIFT(43848, 565, RunecraftConfig::showBlood),
   BODY_RIFT(24973, 559, RunecraftConfig::showBody),
   CHAOS_RIFT(24976, 562, RunecraftConfig::showChaos),
   COSMIC_RIFT(24974, 564, RunecraftConfig::showCosmic),
   DEATH_RIFT(25035, 560, RunecraftConfig::showDeath),
   EARTH_RIFT(24972, 557, RunecraftConfig::showEarth),
   FIRE_RIFT(24971, 554, RunecraftConfig::showFire),
   LAW_RIFT(25034, 563, RunecraftConfig::showLaw),
   MIND_RIFT(25379, 558, RunecraftConfig::showMind),
   NATURE_RIFT(24975, 561, RunecraftConfig::showNature),
   SOUL_RIFT(25377, 566, RunecraftConfig::showSoul),
   WATER_RIFT(25376, 555, RunecraftConfig::showWater);

   private final int objectId;
   private final int itemId;
   private final Predicate<RunecraftConfig> configEnabled;
   private static final Map<Integer, AbyssRifts> rifts;

   static AbyssRifts getRift(int id) {
      return (AbyssRifts)rifts.get(id);
   }

   private AbyssRifts(int objectId, int itemId, Predicate configEnabled) {
      this.objectId = objectId;
      this.itemId = itemId;
      this.configEnabled = configEnabled;
   }

   public int getObjectId() {
      return this.objectId;
   }

   public int getItemId() {
      return this.itemId;
   }

   public Predicate<RunecraftConfig> getConfigEnabled() {
      return this.configEnabled;
   }

   static {
      ImmutableMap.Builder<Integer, AbyssRifts> builder = new ImmutableMap.Builder();
      AbyssRifts[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         AbyssRifts s = var1[var3];
         builder.put(s.getObjectId(), s);
      }

      rifts = builder.build();
   }
}
