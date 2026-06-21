package net.runelite.client.plugins.particles.util;

import net.runelite.client.plugins.particles.config.ParticleEffectorConfig;
import net.runelite.client.plugins.particles.core.ParticleEffector;

public final class EffectorSnapshot {
   public int effectorId;
   public int scope;
   public int worldX;
   public int worldY;
   public int worldZ;
   public long maxRangeSquared;
   public int forceX;
   public int forceY;
   public int forceZ;
   public int coneAngleCosine;
   public float coneCheckMultiplier;
   public int forceMagnitude;
   public int falloffType;
   public int falloffRate;
   public boolean radialMode;
   public boolean directPositionMode;
   public boolean debugAffectAll;

   public void populate(ParticleEffector effector) {
      ParticleEffectorConfig config = effector.getConfig();
      this.effectorId = config.getId();
      this.scope = config.getScope();
      this.worldX = effector.getWorldX();
      this.worldY = effector.getWorldY();
      this.worldZ = effector.getWorldZ();
      this.maxRangeSquared = config.getMaxRangeSquared();
      this.forceX = effector.getTransformedForceX();
      this.forceY = config.getForceDirectionY();
      this.forceZ = effector.getTransformedForceZ();
      this.coneAngleCosine = config.getConeAngleCosine();
      this.coneCheckMultiplier = config.getConeCheckMultiplier();
      this.forceMagnitude = config.getForceMagnitude();
      this.falloffType = config.getFalloffType();
      this.falloffRate = config.getFalloffRate();
      this.radialMode = config.isRadialForceMode();
      this.directPositionMode = config.isDirectPositionMode();
      this.debugAffectAll = effector.isDebugAffectAll();
   }
}
