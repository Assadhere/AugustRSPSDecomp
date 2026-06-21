package net.runelite.api.hooks;

import java.util.Set;
import net.runelite.api.GameObject;
import net.runelite.api.Model;
import net.runelite.api.Projection;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.SceneTileModel;
import net.runelite.api.SceneTilePaint;
import net.runelite.api.Texture;
import net.runelite.api.TileObject;
import net.runelite.api.WorldView;

public interface DrawCallbacks {
   int GPU = 1;
   int HILLSKEW = 2;
   int NORMALS = 4;
   int NO_VERTEX_SNAPPING = 8;
   int ZBUF = 16;
   int ZBUF_ZONE_FRUSTUM_CHECK = 32;
   int UNLIT_FACE_COLORS = 64;
   int RENDER_THREADS_MASK = 15;
   int RENDER_THREADS_SHIFT = 7;
   int PASS_OPAQUE = 0;
   int PASS_ALPHA = 1;

   static int RENDER_THREADS(int num) {
      return (num & 15) << 7;
   }

   default void draw(Projection projection, Scene scene, Renderable renderable, int orientation, int x, int y, int z, long hash) {
   }

   default void drawScenePaint(Scene scene, SceneTilePaint paint, int plane, int tileX, int tileZ) {
   }

   default void drawSceneTileModel(Scene scene, SceneTileModel model, int tileX, int tileZ) {
   }

   void draw(int var1);

   default void drawScene(double cameraX, double cameraY, double cameraZ, double cameraPitch, double cameraYaw, int plane) {
   }

   default void postDrawScene() {
   }

   default void animate(Texture texture, int diff) {
   }

   default void loadScene(Scene scene) {
   }

   void swapScene(Scene var1);

   default boolean tileInFrustum(Scene scene, float pitchSin, float pitchCos, float yawSin, float yawCos, int cameraX, int cameraY, int cameraZ, int plane, int msx, int msy) {
      return true;
   }

   default boolean zoneInFrustum(int zoneX, int zoneZ, int maxY, int minY) {
      return false;
   }

   default void loadScene(WorldView worldView, Scene scene) {
   }

   default void despawnWorldView(WorldView worldView) {
   }

   default void preSceneDraw(Scene scene, float cameraX, float cameraY, float cameraZ, float cameraPitch, float cameraYaw, int minLevel, int level, int maxLevel, Set<Integer> hideRoofIds) {
   }

   default void postSceneDraw(Scene scene) {
   }

   default void drawPass(Projection entityProjection, Scene scene, int pass) {
   }

   default void drawZoneOpaque(Projection entityProjection, Scene scene, int zx, int zz) {
   }

   default void drawZoneAlpha(Projection entityProjection, Scene scene, int level, int zx, int zz) {
   }

   default void drawDynamic(Projection worldProjection, Scene scene, TileObject tileObject, Renderable r, Model m, int orient, int x, int y, int z) {
   }

   default void drawDynamic(int renderThreadId, Projection worldProjection, Scene scene, TileObject tileObject, Renderable r, Model m, int orient, int x, int y, int z) {
      this.drawDynamic(worldProjection, scene, tileObject, r, m, orient, x, y, z);
   }

   default void drawTemp(Projection worldProjection, Scene scene, GameObject gameObject, Model m, int orient, int x, int y, int z) {
   }

   default void invalidateZone(Scene scene, int zx, int zz) {
   }
}
