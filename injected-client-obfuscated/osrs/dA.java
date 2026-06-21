package osrs;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import javax.annotation.Nullable;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Renderable;
import net.runelite.api.TileObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public final class dA implements GameObject, TileObject {
   public long a = 0L;
   public int b = 0;
   public int c;
   public int d;
   public int e;
   public int f;
   public int g;
   public int h;
   public int i;
   public int j;
   public int k;
   public int l;
   public int m;
   public dG n;

   public WorldPoint getWorldLocation() {
      bG var1 = bG.b(this.b());
      if (var1 == null) {
         var1 = bo.aQ;
      }

      if (this instanceof dA) {
         int var3 = this.l;
         int var4 = this.j;
         int var5 = this.d - var3;
         int var6 = this.k - var4;
         return WorldPoint.fromScene(var1, var5 / 2 + var3, var6 / 2 + var4, this.getPlane());
      } else {
         return WorldPoint.fromLocal(var1, this.getX(), this.getY(), this.getPlane());
      }
   }

   public Point getCanvasLocation() {
      return this.getCanvasLocation(0);
   }

   public Shape getClickbox() {
      return Perspective.getClickbox(Client.s, this.c(), this.a(), this.getModelOrientation(), this.getX(), this.getY(), this.getZ());
   }

   public Point getCanvasTextLocation(Graphics2D var1, String var2, int var3) {
      return Perspective.getCanvasTextLocation(Client.s, var1, this.getLocalLocation(), var2, var3);
   }

   public LocalPoint getLocalLocation() {
      return new LocalPoint(this.getX(), this.getY(), this.b());
   }

   public Point getCanvasLocation(int var1) {
      return Perspective.localToCanvas(Client.s, this.getLocalLocation(), this.getPlane(), var1);
   }

   @Nullable
   public String getOpOverride(int var1) {
      ic var2 = this.c().a(this.getHash());
      return var2 == null ? null : var2.f(var1);
   }

   public boolean isOpShown(int var1) {
      ic var2 = this.c().a(this.getHash());
      return var2 == null ? true : var2.g(var1);
   }

   public int sizeY() {
      return this.k - this.j + 1;
   }

   public aH a() {
      if (this.n == null) {
         return null;
      } else {
         return this.n instanceof aH ? (aH)this.n : this.n.ai();
      }
   }

   public int b() {
      long var1 = this.getHash();
      int var3 = (int)(var1 >> 52 & 4095L);
      if ((long)var3 == 4095L) {
         var3 = -1;
      }

      return var3;
   }

   public Shape getConvexHull() {
      aH var1 = this.a();
      return var1 == null ? null : var1.a(this.c(), this.getX(), this.getY(), this.getModelOrientation(), this.getZ());
   }

   public bG c() {
      return bG.b(this.b());
   }

   public Point getSceneMinLocation() {
      return new Point(this.l, this.j);
   }

   public WorldView getWorldView() {
      return this.c();
   }

   public Renderable getRenderable() {
      return this.d();
   }

   public int sizeX() {
      return this.d - this.l + 1;
   }

   public Point getSceneMaxLocation() {
      return new Point(this.d, this.k);
   }

   public Point getMinimapLocation() {
      return Perspective.localToMinimap(Client.s, this.getLocalLocation());
   }

   public Polygon getCanvasTilePoly() {
      int var1 = 1;
      int var2 = 1;
      if (this instanceof dA) {
         var1 = this.d - this.l + 1;
         var2 = this.k - this.j + 1;
      }

      return Perspective.getCanvasTileAreaPoly(Client.s, this.getLocalLocation(), var1, var2, this.getPlane(), 0);
   }

   public int getId() {
      long var1 = this.getHash();
      return (int)(var1 >>> 20 & 4294967295L);
   }

   public int getOrientation() {
      int var1 = this.getModelOrientation();
      int var2 = this.getConfig() >> 6 & 3;
      return var2 * 512 + var1;
   }

   public void a(long var1) {
      this.a = var1;
   }

   public void a(int var1) {
      this.m = var1;
   }

   public long getHash() {
      return this.a;
   }

   public int getZ() {
      return this.f;
   }

   public void b(int var1) {
      this.e = var1;
   }

   public void c(int var1) {
      this.h = var1;
   }

   public dG d() {
      return this.n;
   }

   public void d(int var1) {
      this.c = var1;
   }

   public void e(int var1) {
      this.b = var1;
   }

   public int getX() {
      return this.g;
   }

   public int getConfig() {
      return this.b;
   }

   public int getModelOrientation() {
      return this.c;
   }

   public void f(int var1) {
      this.g = var1;
   }

   public int getPlane() {
      return this.e;
   }

   public int getY() {
      return this.h;
   }

   public void a(dG var1) {
      this.n = var1;
   }

   public void g(int var1) {
      this.f = var1;
   }
}
