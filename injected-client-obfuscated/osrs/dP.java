package osrs;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import javax.annotation.Nullable;
import net.runelite.api.DecorativeObject;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Renderable;
import net.runelite.api.TileObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.geometry.Shapes;

public final class dP implements DecorativeObject, TileObject {
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
   public dG m;
   public dG n;

   public LocalPoint getLocalLocation() {
      return new LocalPoint(this.getX(), this.getY(), this.b());
   }

   public boolean isOpShown(int var1) {
      ic var2 = this.d().a(this.getHash());
      return var2 == null ? true : var2.g(var1);
   }

   public WorldView getWorldView() {
      return this.d();
   }

   public WorldPoint getWorldLocation() {
      bG var1 = bG.b(this.b());
      if (var1 == null) {
         var1 = bo.aQ;
      }

      return WorldPoint.fromLocal(var1, this.getX(), this.getY(), this.getPlane());
   }

   public Shape getConvexHull() {
      aH var1 = this.c();
      return var1 == null ? null : var1.a(this.d(), this.f + this.c, this.j + this.g, 0, this.getZ());
   }

   public aH a() {
      dG var1 = this.e();
      if (var1 == null) {
         return null;
      } else {
         aH var2;
         if (var1 instanceof aH) {
            var2 = (aH)var1;
         } else {
            var2 = var1.ai();
         }

         return var2;
      }
   }

   public Point getCanvasLocation() {
      return this.getCanvasLocation(0);
   }

   public Point getCanvasTextLocation(Graphics2D var1, String var2, int var3) {
      return Perspective.getCanvasTextLocation(Client.s, var1, this.getLocalLocation(), var2, var3);
   }

   public Point getMinimapLocation() {
      return Perspective.localToMinimap(Client.s, this.getLocalLocation());
   }

   public int b() {
      long var1 = this.getHash();
      int var3 = (int)(var1 >> 52 & 4095L);
      if ((long)var3 == 4095L) {
         var3 = -1;
      }

      return var3;
   }

   public Shape getClickbox() {
      bG var1 = this.d();
      Shape var2 = Perspective.getClickbox(Client.s, var1, this.c(), 0, this.f + this.c, this.j + this.g, this.e);
      Shape var3 = Perspective.getClickbox(Client.s, var1, this.a(), 0, this.f + this.d, this.g, this.k + this.e);
      if (var2 != null && var3 != null) {
         return new Shapes(new Shape[]{var2, var3});
      } else {
         return var2 != null ? var2 : var3;
      }
   }

   @Nullable
   public String getOpOverride(int var1) {
      ic var2 = this.d().a(this.getHash());
      return var2 == null ? null : var2.f(var1);
   }

   public aH c() {
      dG var1 = this.f();
      if (var1 == null) {
         return null;
      } else {
         aH var2;
         if (var1 instanceof aH) {
            var2 = (aH)var1;
         } else {
            var2 = var1.ai();
         }

         return var2;
      }
   }

   public Shape getConvexHull2() {
      aH var1 = this.a();
      return var1 == null ? null : var1.a(this.d(), this.f + this.d, this.k + this.g, 0, this.getZ());
   }

   public Polygon getCanvasTilePoly() {
      byte var1 = 1;
      byte var2 = 1;
      return Perspective.getCanvasTileAreaPoly(Client.s, this.getLocalLocation(), var1, var2, this.getPlane(), 0);
   }

   public bG d() {
      return bG.b(this.b());
   }

   public Renderable getRenderable() {
      return this.f();
   }

   public int getPlane() {
      return this.l;
   }

   public Point getCanvasLocation(int var1) {
      return Perspective.localToCanvas(Client.s, this.getLocalLocation(), this.getPlane(), var1);
   }

   public Renderable getRenderable2() {
      return this.e();
   }

   public int getId() {
      long var1 = this.getHash();
      return (int)(var1 >>> 20 & 4294967295L);
   }

   public int getConfig() {
      return this.b;
   }

   public int getXOffset2() {
      return this.d;
   }

   public void a(int var1) {
      this.f = var1;
   }

   public int getZ() {
      return this.e;
   }

   public int getXOffset() {
      return this.c;
   }

   public int getY() {
      return this.g;
   }

   public dG e() {
      return this.m;
   }

   public long getHash() {
      return this.a;
   }

   public void a(long var1) {
      this.a = var1;
   }

   public void b(int var1) {
      this.b = var1;
   }

   public void a(dG var1) {
      this.n = var1;
   }

   public dG f() {
      return this.n;
   }

   public void c(int var1) {
      this.e = var1;
   }

   public int getX() {
      return this.f;
   }

   public void d(int var1) {
      this.g = var1;
   }

   public void e(int var1) {
      this.i = var1;
   }

   public int getYOffset2() {
      return this.k;
   }

   public void f(int var1) {
      this.h = var1;
   }

   public int getYOffset() {
      return this.j;
   }

   public void b(dG var1) {
      this.m = var1;
   }
}
