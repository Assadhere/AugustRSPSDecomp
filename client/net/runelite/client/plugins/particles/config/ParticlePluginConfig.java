package net.runelite.client.plugins.particles.config;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("particles")
public interface ParticlePluginConfig extends Config {
   @ConfigItem(
      keyName = "enabled",
      name = "Enable Particles",
      description = "Enable or disable the particle system",
      position = 0
   )
   default boolean enabled() {
      return true;
   }

   @ConfigItem(
      keyName = "maxParticles",
      name = "Max Particles",
      description = "Maximum number of particles to render",
      position = 1
   )
   @Range(
      min = 100,
      max = 100000
   )
   default int maxParticles() {
      return 50000;
   }

   @ConfigItem(
      keyName = "particleSize",
      name = "Particle Size",
      description = "Base size multiplier for particles",
      position = 2
   )
   @Range(
      min = 1,
      max = 10
   )
   default int particleSize() {
      return 1;
   }

   @ConfigItem(
      keyName = "maxRenderDistance",
      name = "Max Render Distance",
      description = "Maximum distance (in tiles) at which particle emitters spawn new particles. 184 = unlimited.",
      position = 3
   )
   @Range(
      min = 1,
      max = 184
   )
   default int maxRenderDistance() {
      return 184;
   }

   @ConfigItem(
      keyName = "showDebug",
      name = "Show Debug Info",
      description = "Show particle count and performance info",
      position = 4
   )
   default boolean showDebug() {
      return false;
   }

   @ConfigItem(
      keyName = "showEffectorDebug",
      name = "Show Effector Zones",
      description = "Visualize the affected zones of all active effectors in the game world",
      position = 5
   )
   default boolean showEffectorDebug() {
      return false;
   }

   @ConfigItem(
      keyName = "objectEmitters",
      name = "Object Emitters",
      description = "Enable particle emitters attached to game objects",
      position = 10
   )
   default boolean objectEmitters() {
      return true;
   }

   @ConfigItem(
      keyName = "playerParticles",
      name = "Player Particles",
      description = "Enable particle effects attached to player models",
      position = 11
   )
   default boolean playerParticles() {
      return true;
   }

   @ConfigItem(
      keyName = "npcParticles",
      name = "NPC Particles",
      description = "Enable particle effects attached to NPC models",
      position = 12
   )
   default boolean npcParticles() {
      return true;
   }

   @ConfigItem(
      keyName = "projectileTrails",
      name = "Projectile Particles",
      description = "Enable particle effects attached to projectiles",
      position = 20
   )
   default boolean projectileTrails() {
      return false;
   }

   @ConfigItem(
      keyName = "graphicsObjectParticles",
      name = "Graphics Object Particles",
      description = "Enable particle effects attached to graphics objects (spotanims)",
      position = 30
   )
   default boolean graphicsObjectParticles() {
      return true;
   }
}
