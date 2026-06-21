package net.runelite.client.plugins.particles.util;

import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.WorldView;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.core.ParticleEmitter;

public final class CollisionContext {
   public WorldView worldView;
   public Scene scene;
   public Tile[][][] tiles;
   public int[][][] tileHeights;
   public int tilesX;
   public int tilesZ;
   public int sceneOffset;
   public boolean hasHeightBounds;
   public boolean clipToTerrain;
   public boolean collidesWithObjects;
   public int upperBoundLevel;
   public int lowerBoundLevel;
   public int plane;
   public boolean valid;
   public boolean needsAnyCheck;
   public boolean needsPlaneDetermination;

   public boolean populate(ParticleEmitter emitter, ParticleEmitterConfig cfg, int plane) {
      this.plane = plane;
      this.worldView = emitter.getWorldView();
      if (this.worldView == null) {
         this.valid = false;
         return false;
      } else {
         this.scene = this.worldView.getScene();
         this.tiles = this.scene.getTiles();
         this.tileHeights = this.worldView.getTileHeights();
         this.tilesX = this.tiles[plane].length;
         this.tilesZ = this.tiles[plane][0].length;
         this.sceneOffset = this.worldView.isTopLevel() ? 40 : 0;
         this.hasHeightBounds = cfg.isHasHeightBounds();
         this.clipToTerrain = cfg.isClipToTerrain();
         this.collidesWithObjects = cfg.isCollidesWithObjects();
         this.upperBoundLevel = cfg.getUpperBoundLevel();
         this.lowerBoundLevel = cfg.getLowerBoundLevel();
         this.needsAnyCheck = this.hasHeightBounds || this.clipToTerrain || this.collidesWithObjects;
         this.needsPlaneDetermination = this.clipToTerrain || this.collidesWithObjects;
         this.valid = true;
         return true;
      }
   }

   public void invalidate() {
      this.valid = false;
      this.worldView = null;
      this.scene = null;
      this.tiles = null;
      this.tileHeights = null;
   }
}
