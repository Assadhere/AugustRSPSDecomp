package net.runelite.api.coords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.api.Scene;
import net.runelite.api.WorldView;

public final class WorldPoint {
   private static final int[] REGION_MIRRORS = new int[]{12894, 8755, 12895, 8756, 13150, 9011, 13151, 9012};
   private final int x;
   private final int y;
   private final int plane;

   public WorldPoint dx(int dx) {
      return new WorldPoint(this.x + dx, this.y, this.plane);
   }

   public WorldPoint dy(int dy) {
      return new WorldPoint(this.x, this.y + dy, this.plane);
   }

   public WorldPoint dz(int dz) {
      return new WorldPoint(this.x, this.y, this.plane + dz);
   }

   public static boolean isInScene(Scene scene, int x, int y) {
      int baseX = scene.getBaseX();
      int baseY = scene.getBaseY();
      int maxX = baseX + 104;
      int maxY = baseY + 104;
      return x >= baseX && x < maxX && y >= baseY && y < maxY;
   }

   /** @deprecated */
   @Deprecated
   public static boolean isInScene(Client client, int x, int y) {
      return isInScene(client.getTopLevelWorldView(), x, y);
   }

   public static boolean isInScene(WorldView wv, int x, int y) {
      int baseX = wv.getBaseX();
      int baseY = wv.getBaseY();
      int maxX = baseX + wv.getSizeX();
      int maxY = baseY + wv.getSizeY();
      return x >= baseX && x < maxX && y >= baseY && y < maxY;
   }

   /** @deprecated */
   @Deprecated
   public boolean isInScene(Client client) {
      return client.getPlane() == this.plane && isInScene(client, this.x, this.y);
   }

   public static WorldPoint fromLocal(Client client, LocalPoint local) {
      WorldView wv = client.getWorldView(local.getWorldView());
      return fromLocal(wv, local.getX(), local.getY(), wv.getPlane());
   }

   public static WorldPoint fromLocal(WorldView wv, int x, int y, int plane) {
      return new WorldPoint((x >> 7) + wv.getBaseX(), (y >> 7) + wv.getBaseY(), plane);
   }

   public static WorldPoint fromLocal(Scene scene, int x, int y, int plane) {
      return new WorldPoint((x >> 7) + scene.getBaseX(), (y >> 7) + scene.getBaseY(), plane);
   }

   /** @deprecated */
   @Deprecated
   public static WorldPoint fromLocal(Client client, int x, int y, int plane) {
      return fromLocal(client.getTopLevelWorldView(), x, y, plane);
   }

   public static WorldPoint fromLocalInstance(Client client, LocalPoint localPoint) {
      WorldView wv = client.getWorldView(localPoint.getWorldView());
      return fromLocalInstance(client, localPoint, wv.getPlane());
   }

   public static WorldPoint fromLocalInstance(Client client, LocalPoint localPoint, int plane) {
      WorldView wv = client.getWorldView(localPoint.getWorldView());
      return wv.isInstance() ? fromLocalInstance(wv.getInstanceTemplateChunks(), localPoint, plane) : fromLocal(client, localPoint.getX(), localPoint.getY(), plane);
   }

   public static WorldPoint fromLocalInstance(Scene scene, LocalPoint localPoint, int plane) {
      return scene.isInstance() ? fromLocalInstance(scene.getInstanceTemplateChunks(), localPoint, plane) : fromLocal(scene, localPoint.getX(), localPoint.getY(), plane);
   }

   private static WorldPoint fromLocalInstance(int[][][] instanceTemplateChunks, LocalPoint localPoint, int plane) {
      int sceneX = localPoint.getSceneX();
      int sceneY = localPoint.getSceneY();
      int chunkX = sceneX / 8;
      int chunkY = sceneY / 8;
      int templateChunk = -1;
      if (chunkX >= 0 && chunkX < 13 && chunkY >= 0 && chunkY < 13) {
         templateChunk = instanceTemplateChunks[plane][chunkX][chunkY];
      }

      int rotation = templateChunk >> 1 & 3;
      int templateChunkY = (templateChunk >> 3 & 2047) * 8;
      int templateChunkX = (templateChunk >> 14 & 1023) * 8;
      int templateChunkPlane = templateChunk >> 24 & 3;
      int x = templateChunkX + (sceneX & 7);
      int y = templateChunkY + (sceneY & 7);
      return rotate(new WorldPoint(x, y, templateChunkPlane), 4 - rotation);
   }

   /** @deprecated */
   @Deprecated
   public static Collection<WorldPoint> toLocalInstance(Client client, WorldPoint worldPoint) {
      return toLocalInstance(client.getTopLevelWorldView(), worldPoint);
   }

   public static Collection<WorldPoint> toLocalInstance(WorldView wv, WorldPoint worldPoint) {
      if (wv.isInstance()) {
         return toLocalInstance(wv.getInstanceTemplateChunks(), wv.getBaseX(), wv.getBaseY(), worldPoint);
      } else {
         return (Collection)(wv.contains(worldPoint) ? Collections.singleton(worldPoint) : Collections.emptyList());
      }
   }

   /** @deprecated */
   @Deprecated
   public static Collection<WorldPoint> toLocalInstance(Scene scene, WorldPoint worldPoint) {
      if (scene.isInstance()) {
         return toLocalInstance(scene.getInstanceTemplateChunks(), scene.getBaseX(), scene.getBaseY(), worldPoint);
      } else {
         return (Collection)(isInScene(scene, worldPoint.getX(), worldPoint.getY()) ? Collections.singleton(worldPoint) : Collections.emptyList());
      }
   }

   private static Collection<WorldPoint> toLocalInstance(int[][][] instanceTemplateChunks, int baseX, int baseY, WorldPoint worldPoint) {
      List<WorldPoint> worldPoints = new ArrayList();

      for(int z = 0; z < instanceTemplateChunks.length; ++z) {
         for(int x = 0; x < instanceTemplateChunks[z].length; ++x) {
            for(int y = 0; y < instanceTemplateChunks[z][x].length; ++y) {
               int chunkData = instanceTemplateChunks[z][x][y];
               int rotation = chunkData >> 1 & 3;
               int templateChunkY = (chunkData >> 3 & 2047) * 8;
               int templateChunkX = (chunkData >> 14 & 1023) * 8;
               int plane = chunkData >> 24 & 3;
               if (worldPoint.getX() >= templateChunkX && worldPoint.getX() < templateChunkX + 8 && worldPoint.getY() >= templateChunkY && worldPoint.getY() < templateChunkY + 8 && plane == worldPoint.getPlane()) {
                  WorldPoint p = new WorldPoint(baseX + x * 8 + (worldPoint.getX() & 7), baseY + y * 8 + (worldPoint.getY() & 7), z);
                  p = rotate(p, rotation);
                  worldPoints.add(p);
               }
            }
         }
      }

      return worldPoints;
   }

   private static WorldPoint rotate(WorldPoint point, int rotation) {
      int chunkX = point.getX() & -8;
      int chunkY = point.getY() & -8;
      int x = point.getX() & 7;
      int y = point.getY() & 7;
      switch (rotation) {
         case 1:
            return new WorldPoint(chunkX + y, chunkY + (7 - x), point.getPlane());
         case 2:
            return new WorldPoint(chunkX + (7 - x), chunkY + (7 - y), point.getPlane());
         case 3:
            return new WorldPoint(chunkX + (7 - y), chunkY + x, point.getPlane());
         default:
            return point;
      }
   }

   public int distanceTo(WorldArea other) {
      return this.toWorldArea().distanceTo(other);
   }

   public int distanceTo(WorldPoint other) {
      return other.plane != this.plane ? Integer.MAX_VALUE : this.distanceTo2D(other);
   }

   public int distanceTo2D(WorldPoint other) {
      return Math.max(Math.abs(this.getX() - other.getX()), Math.abs(this.getY() - other.getY()));
   }

   /** @deprecated */
   @Deprecated
   public static WorldPoint fromScene(Client client, int x, int y, int plane) {
      return fromScene(client.getTopLevelWorldView(), x, y, plane);
   }

   public static WorldPoint fromScene(WorldView wv, int x, int y, int plane) {
      return new WorldPoint(x + wv.getBaseX(), y + wv.getBaseY(), plane);
   }

   public static WorldPoint fromScene(Scene scene, int x, int y, int plane) {
      return new WorldPoint(x + scene.getBaseX(), y + scene.getBaseY(), plane);
   }

   public int getRegionID() {
      return this.x >> 6 << 8 | this.y >> 6;
   }

   public static WorldPoint fromRegion(int regionId, int regionX, int regionY, int plane) {
      return new WorldPoint((regionId >>> 8 << 6) + regionX, ((regionId & 255) << 6) + regionY, plane);
   }

   public int getRegionX() {
      return getRegionOffset(this.x);
   }

   public int getRegionY() {
      return getRegionOffset(this.y);
   }

   private static int getRegionOffset(int position) {
      return position & 63;
   }

   public static WorldPoint getMirrorPoint(WorldPoint worldPoint, boolean toOverworld) {
      int region = worldPoint.getRegionID();

      for(int i = 0; i < REGION_MIRRORS.length; i += 2) {
         int real = REGION_MIRRORS[i];
         int overworld = REGION_MIRRORS[i + 1];
         if (region == (toOverworld ? real : overworld)) {
            return fromRegion(toOverworld ? overworld : real, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane());
         }
      }

      return worldPoint;
   }

   public boolean isInArea(WorldArea... worldAreas) {
      WorldArea[] var2 = worldAreas;
      int var3 = worldAreas.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WorldArea area = var2[var4];
         if (area.contains(this)) {
            return true;
         }
      }

      return false;
   }

   public boolean isInArea2D(WorldArea... worldAreas) {
      WorldArea[] var2 = worldAreas;
      int var3 = worldAreas.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WorldArea area = var2[var4];
         if (area.contains2D(this)) {
            return true;
         }
      }

      return false;
   }

   public WorldArea toWorldArea() {
      return new WorldArea(this, 1, 1);
   }

   public static WorldPoint fromCoord(int c) {
      return new WorldPoint(c >>> 14 & 16383, c & 16383, c >>> 28 & 3);
   }

   public WorldPoint(int x, int y, int plane) {
      this.x = x;
      this.y = y;
      this.plane = plane;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getPlane() {
      return this.plane;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WorldPoint)) {
         return false;
      } else {
         WorldPoint other = (WorldPoint)o;
         if (this.getX() != other.getX()) {
            return false;
         } else if (this.getY() != other.getY()) {
            return false;
         } else {
            return this.getPlane() == other.getPlane();
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getX();
      result = result * 59 + this.getY();
      result = result * 59 + this.getPlane();
      return result;
   }

   public String toString() {
      int var10000 = this.getX();
      return "WorldPoint(x=" + var10000 + ", y=" + this.getY() + ", plane=" + this.getPlane() + ")";
   }
}
