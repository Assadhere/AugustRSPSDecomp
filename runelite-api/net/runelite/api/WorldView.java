package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public interface WorldView {
   int TOPLEVEL = 0;

   int getId();

   boolean isTopLevel();

   Scene getScene();

   IndexedObjectSet<? extends Player> players();

   IndexedObjectSet<? extends NPC> npcs();

   IndexedObjectSet<? extends WorldEntity> worldEntities();

   IndexedObjectSet<? extends WorldView> worldViews();

   @Nullable
   CollisionData[] getCollisionMaps();

   int getPlane();

   int[][][] getTileHeights();

   byte[][][] getTileSettings();

   int getSizeX();

   int getSizeY();

   int getBaseX();

   int getBaseY();

   /** @deprecated */
   @Deprecated
   Projectile createProjectile(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, @Nullable Actor var11, int var12, int var13);

   Deque<GraphicsObject> getGraphicsObjects();

   @Nullable
   Tile getSelectedSceneTile();

   boolean isInstance();

   int[][][] getInstanceTemplateChunks();

   int[] getMapRegions();

   int[][] getXteaKeys();

   boolean contains(WorldPoint var1);

   boolean contains(LocalPoint var1);

   @Nullable
   Projection getMainWorldProjection();

   @Nullable
   Projection getCanvasProjection();

   int getYellowClickAction();

   int getTileHeight(int var1, int var2, int var3);
}
