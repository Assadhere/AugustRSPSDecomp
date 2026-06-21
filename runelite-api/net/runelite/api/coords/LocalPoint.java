package net.runelite.api.coords;

import javax.annotation.Nullable;
import net.runelite.api.Client;
import net.runelite.api.Scene;
import net.runelite.api.WorldView;

public final class LocalPoint {
   private final int x;
   private final int y;
   private final int worldView;

   public LocalPoint(int x, int y, WorldView wv) {
      this(x, y, wv.getId());
   }

   /** @deprecated */
   @Deprecated
   public LocalPoint(int x, int y) {
      this(x, y, 0);
   }

   @Nullable
   public static LocalPoint fromWorld(Client client, WorldPoint point) {
      WorldView wv = client.findWorldViewFromWorldPoint(point);
      return fromWorld(wv, point);
   }

   @Nullable
   public static LocalPoint fromWorld(WorldView wv, WorldPoint world) {
      return wv.getPlane() != world.getPlane() ? null : fromWorld(wv, world.getX(), world.getY());
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public static LocalPoint fromWorld(Client client, int x, int y) {
      return fromWorld(client.getTopLevelWorldView(), x, y);
   }

   @Nullable
   public static LocalPoint fromWorld(WorldView wv, int x, int y) {
      if (!WorldPoint.isInScene(wv, x, y)) {
         return null;
      } else {
         int baseX = wv.getBaseX();
         int baseY = wv.getBaseY();
         return fromScene(x - baseX, y - baseY, wv);
      }
   }

   @Nullable
   public static LocalPoint fromWorld(Scene scene, int x, int y) {
      if (!WorldPoint.isInScene(scene, x, y)) {
         return null;
      } else {
         int baseX = scene.getBaseX();
         int baseY = scene.getBaseY();
         return fromScene(x - baseX, y - baseY, scene);
      }
   }

   public int distanceTo(LocalPoint other) {
      return this.worldView != other.worldView ? Integer.MAX_VALUE : (int)Math.hypot((double)(this.getX() - other.getX()), (double)(this.getY() - other.getY()));
   }

   /** @deprecated */
   @Deprecated
   public boolean isInScene() {
      return this.x >= 0 && this.x < 13312 && this.y >= 0 && this.y < 13312;
   }

   /** @deprecated */
   @Deprecated
   public static LocalPoint fromScene(int x, int y) {
      return new LocalPoint((x << 7) + 64, (y << 7) + 64);
   }

   public static LocalPoint fromScene(int x, int y, Scene scene) {
      return new LocalPoint((x << 7) + 64, (y << 7) + 64, scene.getWorldViewId());
   }

   public static LocalPoint fromScene(int x, int y, WorldView wv) {
      return new LocalPoint((x << 7) + 64, (y << 7) + 64, wv.getId());
   }

   public int getSceneX() {
      return this.x >> 7;
   }

   public int getSceneY() {
      return this.y >> 7;
   }

   public LocalPoint dx(int dx) {
      return this.plus(dx, 0);
   }

   public LocalPoint dy(int dy) {
      return this.plus(0, dy);
   }

   public LocalPoint plus(int dx, int dy) {
      return new LocalPoint(this.x + dx, this.y + dy, this.worldView);
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWorldView() {
      return this.worldView;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LocalPoint)) {
         return false;
      } else {
         LocalPoint other = (LocalPoint)o;
         if (this.getX() != other.getX()) {
            return false;
         } else if (this.getY() != other.getY()) {
            return false;
         } else {
            return this.getWorldView() == other.getWorldView();
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getX();
      result = result * 59 + this.getY();
      result = result * 59 + this.getWorldView();
      return result;
   }

   public String toString() {
      int var10000 = this.getX();
      return "LocalPoint(x=" + var10000 + ", y=" + this.getY() + ", worldView=" + this.getWorldView() + ")";
   }

   public LocalPoint(int x, int y, int worldView) {
      this.x = x;
      this.y = y;
      this.worldView = worldView;
   }
}
