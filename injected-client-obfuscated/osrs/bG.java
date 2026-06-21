package osrs;

import java.util.Iterator;
import net.runelite.api.Actor;
import net.runelite.api.CollisionData;
import net.runelite.api.Deque;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.Projectile;
import net.runelite.api.Projection;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AmbientSoundEffectCreated;
import net.runelite.api.events.ItemSpawned;

public class bG extends az implements WorldView {
   public fJ[][][] a;
   public fJ b;
   public fJ c;
   public go d;
   public go e;
   public static int[][] f = new int[2][3];
   public static int[][] g = new int[2][3];
   public static int[][] h = new int[2][3];
   public int[][][] i;
   public boolean j;
   public fJ k = new fJ();
   public final fJ l;
   public final fJ[][][] m;
   public z[] n;
   public int o;
   public int p;
   public int q;
   public fO r;
   public fO s;
   public W t;
   public int[][][] u;
   public byte[][][] v;
   public int[][] w;
   public dh x;
   public int y;
   public int z;
   public int A;
   public int B;
   public int[] C;
   public int[][] D;

   public bG(int var1, int var2, int var3, int var4, dn var5) {
      this.m = new fJ[4][var2][var3];
      this.l = new fJ();
      this.n = new z[4];
      this.b = new fJ();
      this.c = new fJ();
      this.d = new go(149);
      this.e = new go(25);
      this.o = var1;
      this.p = var2;
      this.q = var3;
      this.r = new fO(var1 == 0 ? 512 : 8);
      this.s = new fO(var1 == 0 ? 128 : 8);
      this.t = new W(var1 == 0 ? 32 : 1);
      this.a = new fJ[4][var2][var3];
      this.u = new int[4][var2 + 1][var3 + 1];
      this.v = new byte[4][var2][var3];
      this.w = new int[var2][var3];
      boolean var6 = var1 != 0;

      for(int var7 = 0; var7 < 4; ++var7) {
         this.n[var7] = new z(var2, var3, var6);
      }

      this.x = new dh(this.a(), 4, var2, var3, var4, var5, this.u);
      this.g();
   }

   public boolean a() {
      return this.o == 0;
   }

   public void b() {
      this.d.b();
      this.e.b();
      this.r.a();
      this.s.a();
      this.t.a();
      this.c.a();
      this.b = new fJ();

      int var1;
      for(var1 = 0; var1 < 4; ++var1) {
         for(int var2 = 0; var2 < this.p; ++var2) {
            for(int var3 = 0; var3 < this.q; ++var3) {
               this.a[var1][var2][var3] = null;
            }
         }
      }

      this.x.p();

      for(var1 = 0; var1 < 4; ++var1) {
         this.n[var1].e();
      }

   }

   public B a(int var1, int var2, int var3) {
      return new B(var1, var2 - this.B, var3 - this.z);
   }

   public void c() {
      this.r.a();

      o var1;
      for(Iterator var2 = this.s.iterator(); var2.hasNext(); var1.f = false) {
         var1 = (o)var2.next();
         var1.Z = -1;
         var1.f(-1);
      }

   }

   public boolean a(int var1, int var2) {
      return var1 >= 0 && var2 >= 0 && var1 < this.p && var2 < this.q;
   }

   public boolean b(int var1, int var2, int var3) {
      return var1 >= 0 && var1 < 4 && this.a(var2, var3);
   }

   public t a(int var1) {
      return (t)this.r.a((long)var1);
   }

   public fJ d() {
      return this.l;
   }

   public void a(int var1, int var2, int var3, aC var4, int var5) {
      hu var6 = new hu(this.o, var1, var2, var3, var5, var4);
      this.l.a((az)var6);
      this.c(var1, var2, var3, var4, var5);
   }

   public void e() {
      for(hu var1 = (hu)this.l.d(); var1 != null; var1 = (hu)this.l.f()) {
         var1.j();
      }

   }

   public void b(int var1, int var2, int var3, aC var4, int var5) {
      if (var4 != null && var4.f()) {
         int var6 = var4.t;
         int var7 = var4.u;
         if (var5 == 1 || var5 == 3) {
            var6 = var4.u;
            var7 = var4.t;
         }

         int var8 = osrs.B.b(var2 + var6);
         int var9 = osrs.B.b(var3 + var7);
         int var10 = osrs.B.b(var2);
         int var11 = osrs.B.b(var3);
         int var12 = var4.X;
         int var13 = osrs.B.b(var4.Y);
         int var14 = Math.max(osrs.B.b(var4.Z - 1), 0);
         if (var4.U != null) {
            aC var15 = var4.e();
            if (var15 != null) {
               var12 = var15.X;
               var13 = osrs.B.b(var15.Y);
               var14 = Math.max(osrs.B.b(var4.Z - 1), 0);
            }
         }

         for(hu var16 = (hu)this.l.d(); var16 != null; var16 = (hu)this.l.f()) {
            if (var16.p() == var1 && var16.l() == var10 && var16.n() == var11 && var16.m() == var8 && var16.o() == var9 && var16.k() == var12 && var16.q() == var13 && var16.r() == var14) {
               var16.j();
               break;
            }
         }
      }

   }

   public int a(du var1, int var2, int var3, int var4, int var5) {
      for(int var6 = 0; var6 < var1.t.length; ++var6) {
         int var7 = var1.q[var1.t[var6]];
         int var8 = var1.s[var1.t[var6]];
         int var9 = var1.q[var1.a[var6]];
         int var10 = var1.s[var1.a[var6]];
         int var11 = var1.q[var1.b[var6]];
         int var12 = var1.s[var1.b[var6]];
         if (dh.a(var4, var5, var8, var10, var12, var7, var9, var11)) {
            int var13 = var1.r[var1.t[var6]];
            int var14 = var1.r[var1.a[var6]];
            int var15 = var1.r[var1.b[var6]];
            float var16 = ks.a(var4, var5, var7, var9, var11, var8, var10, var12, (float)var13, (float)var14, (float)var15);
            return (int)var16;
         }
      }

      return -2147483647;
   }

   public final int a(int var1, int var2, int var3, int var4) {
      int var5 = (var1 >> 7) + this.x.s;
      int var6 = (var2 >> 7) + this.x.s;
      int var7 = this.x.s << 1;
      if (var5 >= 0 && var6 >= 0 && var5 < this.p + var7 && var6 < this.q + var7) {
         jh var8 = this.x.bh[var3][var5][var6];
         int var9 = var3;
         if (var3 < 3 && (this.x.bY[1][var5][var6] & 2) == 2) {
            var9 = var3 + 1;
         }

         int var10 = -2147483647;
         if (var8 != null) {
            du var11 = var8.e();
            if (var11 != null) {
               var10 = this.b(var11, -1, -1, var1, var2);
            }

            dv var12 = var8.i();
            if (var10 == -2147483647 && var12 != null) {
               var10 = this.b(var12, var1 >> 7, var2 >> 7, var1, var2, var9);
            }
         }

         if (var10 == -2147483647) {
            var10 = this.a(var5, var6, var1, var2, var9);
         }

         if (var8 != null) {
            dr var13 = var8.a();
            if (var13 != null) {
               aC var14 = aC.c(var13.getId());
               var10 -= var14.T;
            }
         }

         return var10;
      } else {
         return 0;
      }
   }

   public int getTileHeight(int var1, int var2, int var3) {
      int var4 = (var1 >> 7) + this.x.s;
      int var5 = (var2 >> 7) + this.x.s;
      int var6 = this.x.s << 1;
      if (var4 >= 0 && var5 >= 0 && var4 < this.p + var6 && var5 < this.q + var6) {
         int var7 = var3;
         if (var3 > 0 && (this.x.bY[1][var4][var5] & 2) == 2) {
            var7 = var3 - 1;
         }

         jh var8 = this.x.bh[var7][var4][var5];
         int var9 = -2147483647;
         if (var8 != null) {
            du var10 = var8.e();
            if (var10 != null) {
               var9 = this.b(var10, -1, -1, var1, var2);
            }

            dv var11 = var8.i();
            if (var9 == -2147483647 && var11 != null) {
               var9 = this.b(var11, var1 >> 7, var2 >> 7, var1, var2, var3);
            }
         }

         if (var9 == -2147483647) {
            var9 = this.a(var4, var5, var1, var2, var3);
         }

         if (var8 != null) {
            dr var12 = var8.a();
            if (var12 != null) {
               aC var13 = aC.c(var12.getId());
               var9 -= var13.T;
            }
         }

         return var9;
      } else {
         return 0;
      }
   }

   public Tile getSelectedSceneTile() {
      int var1 = Client.s.isMenuOpen() ? this.x.bD : this.x.w;
      int var2 = Client.s.isMenuOpen() ? this.x.bE : this.x.x;
      return var1 >= this.x.o && var2 >= this.x.bS && var1 < this.x.z && var2 < this.x.aK ? this.x.bh[this.getPlane()][this.x.s + var1][this.x.s + var2] : null;
   }

   public void f() {
      if (Client.s.be() != null) {
         jh[][][] var1 = this.x.q();
         fJ[][] var2 = this.a[this.y];

         for(int var3 = 0; var3 < 104; ++var3) {
            for(int var4 = 0; var4 < 104; ++var4) {
               fJ var5 = var2[var3][var4];
               if (var5 != null) {
                  jh var6 = var1[this.y][var3][var4];
                  if (this.x.n(var6.a)) {
                     assert var6.b() != null;

                     for(iC var7 = (iC)var5.j(); var7 != null; var7 = (iC)var5.h()) {
                        var7.c(var3);
                        var7.e(var4);
                        var7.c = this.getId();
                        ItemSpawned var8 = new ItemSpawned(var6, var7);
                        Client.s.getCallbacks().post(var8);
                     }
                  }
               }
            }
         }
      }

   }

   public int getSizeY() {
      return this.q;
   }

   public int getBaseY() {
      return this.z;
   }

   public Deque getGraphicsObjects() {
      return this.c;
   }

   public int a(dv var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = this.x.s + var2;
      int var8 = this.x.s + var3;
      int var9 = (this.x.s << 7) + var4;
      int var10 = (this.x.s << 7) + var5;
      int[][][] var11 = this.u;
      this.u = this.x.H;
      int var12 = var6;
      int var13 = var10;
      int var14 = var9;
      int var15 = var8;
      int var16 = var7;
      int var17;
      if (var1.e) {
         int var18 = this.u[var6][var7][var8];
         var17 = var18;
      } else {
         label42: {
            int[][][] var40 = this.u;
            int[] var19 = f[0];
            int[] var20 = g[0];
            int[] var21 = h[0];
            int[] var22 = dv.d[0];

            for(int var23 = 0; var23 < var22.length; ++var23) {
               var19[var23] = dv.a(var22[var23], var16);
               int var24 = var22[var23];
               int var25 = var40[var12][dv.b[var24] + var16][dv.c[var24] + var15];
               var20[var23] = var25;
               int var26 = var22[var23];
               int var27 = (var15 << 7) + dv.c[var26] * 128;
               var21[var23] = var27;
            }

            int[][][] var41 = this.u;
            int[] var42 = f[1];
            int[] var43 = g[1];
            int[] var44 = h[1];
            int[] var45 = dv.d[1];

            int var28;
            int var29;
            int var30;
            int var31;
            int var32;
            for(var28 = 0; var28 < var45.length; ++var28) {
               var42[var28] = dv.a(var45[var28], var16);
               var29 = var45[var28];
               var30 = var41[var12][dv.b[var29] + var16][dv.c[var29] + var15];
               var43[var28] = var30;
               var31 = var45[var28];
               var32 = (var15 << 7) + dv.c[var31] * 128;
               var44[var28] = var32;
            }

            for(var28 = 0; var28 < 2; ++var28) {
               var29 = f[var28][0];
               var30 = h[var28][0];
               var31 = f[var28][1];
               var32 = h[var28][1];
               int var33 = f[var28][2];
               int var34 = h[var28][2];
               if (dh.a(var14, var13, var30, var32, var34, var29, var31, var33)) {
                  int var35 = g[var28][0];
                  int var36 = g[var28][1];
                  int var37 = g[var28][2];
                  float var38 = ks.a(var14, var13, var29, var31, var33, var30, var32, var34, (float)var35, (float)var36, (float)var37);
                  int var39 = (int)var38;
                  var17 = var39;
                  break label42;
               }
            }

            var29 = -2147483647;
            var17 = var29;
         }
      }

      this.u = var11;
      return var17;
   }

   public boolean contains(WorldPoint var1) {
      int var2 = var1.getX();
      int var3 = var1.getY();
      return var2 >= this.B && var2 < this.B + this.p && var3 >= this.z && var3 < this.z + this.q;
   }

   public boolean contains(LocalPoint var1) {
      if (var1.getWorldView() != this.o) {
         return false;
      } else {
         int var2 = var1.getX();
         int var3 = var1.getY();
         int var4 = var2 >> 7;
         int var5 = var3 >> 7;
         return var4 >= 0 && var4 < this.p && var5 >= 0 && var5 < this.q;
      }
   }

   public int[][][] getTileHeights() {
      return this.u;
   }

   public Scene getScene() {
      return this.h();
   }

   public IndexedObjectSet players() {
      return this.r;
   }

   public void g() {
      this.x.aZ = this;
      this.x.aT = this.o;
      this.x.bY = this.v;
   }

   public IndexedObjectSet worldEntities() {
      return this.t;
   }

   public boolean isTopLevel() {
      return this.o == 0;
   }

   public ic a(long var1) {
      if (!Client.s.isClientThread()) {
         if (!T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else if ((var1 >> 16 & 7L) != 2L) {
         return null;
      } else {
         int var3 = (int)(var1 >> 0 & 127L);
         int var4 = (int)(var1 >> 7 & 127L);
         int var5 = (int)(var1 >> 14 & 3L);
         int var6 = (int)(var1 >> 20 & 4294967295L);

         for(ic var7 = (ic)this.b.j(); var7 != null; var7 = (ic)this.b.h()) {
            if (var7.o == var6 && var7.e == var5 && var7.h == var3 && var7.i == var4) {
               return var7;
            }
         }

         return null;
      }
   }

   public int getYellowClickAction() {
      if (!Client.s.isClientThread()) {
         if (!T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         hf var1 = Client.D.i(this.o);
         return var1.e();
      }
   }

   public int getId() {
      return this.o;
   }

   public dh h() {
      return this.x;
   }

   public int[] getMapRegions() {
      return this.C;
   }

   public int[][] getXteaKeys() {
      return this.D;
   }

   public int getSizeX() {
      return this.p;
   }

   public int[][][] getInstanceTemplateChunks() {
      return this.i;
   }

   public static bG b(int var0) {
      return var0 == -1 ? bo.aQ : Client.D.j(var0);
   }

   public int getBaseX() {
      return this.B;
   }

   public Projectile createProjectile(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, Actor var11, int var12, int var13) {
      return Client.s.a(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
   }

   public int getPlane() {
      return this.y;
   }

   public IndexedObjectSet npcs() {
      return this.s;
   }

   public Projection getCanvasProjection() {
      if (this.x.bz instanceof dc) {
         dc var1 = (dc)this.x.bz;
         if (fX.a.equals(var1.c)) {
            return null;
         }
      }

      return this.x.bz;
   }

   public z[] i() {
      return this.n;
   }

   public CollisionData[] getCollisionMaps() {
      return this.i();
   }

   public int a(int var1, int var2, int var3, int var4, int var5) {
      int var6 = var3 & 127;
      int var7 = var4 & 127;
      int var8 = (128 - var6) * this.x.H[var5][var1][var2] + this.x.H[var5][var1 + 1][var2] * var6 >> 7;
      int var9 = (128 - var6) * this.x.H[var5][var1][var2 + 1] + this.x.H[var5][var1 + 1][var2 + 1] * var6 >> 7;
      return (128 - var7) * var8 + var7 * var9 >> 7;
   }

   public IndexedObjectSet worldViews() {
      return new iV(this);
   }

   public boolean isInstance() {
      return this.j;
   }

   public Projection getMainWorldProjection() {
      return fX.a.equals(this.x.aL.c) ? null : this.x.aL;
   }

   public void c(int var1) {
      this.A = this.y;
   }

   public byte[][][] getTileSettings() {
      return this.v;
   }

   public void c(int var1, int var2, int var3, aC var4, int var5) {
      hu var6 = (hu)this.l.i();
      AmbientSoundEffectCreated var7 = new AmbientSoundEffectCreated(var6);
      Client.s.getCallbacks().post(var7);
   }

   public int b(du var1, int var2, int var3, int var4, int var5) {
      return this.a(var1, var2, var3, var4, var5);
   }

   public int b(dv var1, int var2, int var3, int var4, int var5, int var6) {
      return this.a(var1, var2, var3, var4, var5, var6);
   }
}
