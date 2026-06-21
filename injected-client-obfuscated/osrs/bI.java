package osrs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.runelite.api.Point;
import net.runelite.api.RenderOverview;
import net.runelite.api.WorldMapData;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.worldmap.WorldMapRenderer;

public class bI implements RenderOverview {
   public HashSet a = new HashSet();
   public au b;
   public au c;
   public au d;
   public gg e;
   public HashMap f;
   public hX[] g;
   public HashMap h;
   public bK i;
   public bK j;
   public bK k;
   public bN l;
   public bM m;
   public int n = -1;
   public int o = -1;
   public int p = 3;
   public int q = -1;
   public float r;
   public float s;
   public int t;
   public int u = -1;
   public int v = -1;
   public int w;
   public int x = -1;
   public int y = -1;
   public boolean z = false;
   public HashSet A = null;
   public int B = -1;
   public int C = -1;
   public int D = -1;
   public int E = -1;
   public int F;
   public int G = 0;
   public long H;
   public int I;
   public int J = -1;
   public boolean K = true;
   public HashSet L = new HashSet();
   public HashSet M = new HashSet();
   public HashSet N = new HashSet();
   public HashSet O = new HashSet();
   public boolean P = false;
   public int Q = -1;
   public List R;
   public Iterator S;
   public B T = null;
   public aV U;
   public int V = 50;
   public int W;
   public int X = -1;
   public int Y = -1;
   public static final hv Z;
   public static final hv aa;
   public static final hv ab;
   public final int[] ac = new int[]{1008, 1009, 1010, 1011, 1012};
   public boolean ad = false;

   public void a(au var1, au var2, au var3, gg var4, HashMap var5, hX[] var6) {
      this.g = var6;
      this.b = var1;
      this.c = var2;
      this.d = var3;
      this.e = var4;
      this.f = new HashMap();
      this.f.put(et.a, var5.get(Z));
      this.f.put(et.b, var5.get(aa));
      this.f.put(et.c, var5.get(ab));
      this.m = new bM(var1);
      int var7 = this.b.a(bL.a.f);
      int[] var8 = this.b.g(var7);
      int var9 = var8 == null ? 0 : var8.length;
      this.h = new HashMap(var9);

      for(int var10 = 0; var10 < var9; ++var10) {
         aR var11 = new aR(this.b.b(var7, var8[var10]));
         bK var12 = new bK();
         var12.a(var11, var8[var10]);
         this.h.put(var12.d(), var12);
         if (var12.c()) {
            this.i = var12;
         }
      }

      this.a(this.i);
      this.k = null;
   }

   public void a() {
      bc.a.a(5);
   }

   public void a(int var1, int var2, boolean var3, int var4, int var5, int var6, int var7) {
      if (this.m.b()) {
         this.b();
         this.c();
         if (var3) {
            int var8 = (int)Math.ceil((double)((float)var6 / this.r));
            int var9 = (int)Math.ceil((double)((float)var7 / this.r));
            List var10 = this.l.a(this.w - var8 / 2 - 1, this.F - var9 / 2 - 1, var8 / 2 + this.w + 1, var9 / 2 + this.F + 1, var4, var5, var6, var7, var1, var2);
            HashSet var11 = new HashSet();

            ac var12;
            Iterator var13;
            eo var14;
            aF var15;
            Object[] var16;
            for(var13 = var10.iterator(); var13.hasNext(); bh.a(var12)) {
               var14 = (eo)var13.next();
               var11.add(var14);
               var15 = new aF(var14.a(), var14.e, var14.f);
               var16 = new Object[]{var15, var1, var2};
               if (this.a.contains(var14)) {
                  var12 = osrs.ac.a(17).a(var16).a();
               } else {
                  var12 = osrs.ac.a(15).a(var16).a();
               }
            }

            var13 = this.a.iterator();

            while(var13.hasNext()) {
               var14 = (eo)var13.next();
               if (!var11.contains(var14)) {
                  var15 = new aF(var14.a(), var14.e, var14.f);
                  var16 = new Object[]{var15, var1, var2};
                  ac var17 = osrs.ac.a(16).a(var16).a();
                  bh.a(var17);
               }
            }

            this.a = var11;
         }
      }

   }

   public void a(int var1, int var2, boolean var3, boolean var4) {
      long var5 = bd.a();
      this.a(var1, var2, var4, var5);
      if (!this.e() && (var4 || var3) && !Client.an()) {
         if (var4) {
            this.X = var1;
            this.u = var2;
            this.Q = this.w;
            this.n = this.F;
         }

         if (this.Q != -1) {
            int var7 = var1 - this.X;
            int var8 = var2 - this.u;
            this.a(this.Q - (int)((float)var7 / this.s), this.n + (int)((float)var8 / this.s), false);
         }
      } else {
         this.d();
      }

      if (var4) {
         this.H = var5;
         this.W = var1;
         this.t = var2;
      }

   }

   public void a(int var1, int var2, boolean var3, long var4) {
      if (this.j != null) {
         int var6 = (int)((float)this.w + ((float)(var1 - this.o) - (float)this.o() * this.r / 2.0F) / this.r);
         int var7 = (int)((float)this.F - ((float)(var2 - this.q) - (float)this.p() * this.r / 2.0F) / this.r);
         this.T = this.j.b(var6 + this.j.i() * 64, var7 + this.j.k() * 64);
         if (this.T != null && var3) {
            q var8 = Client.c();
            if (Client.ai() && var8.c(82) && var8.c(81)) {
               Client.a(this.T.b, this.T.c, this.T.a, false);
            } else {
               boolean var9 = true;
               if (this.K) {
                  int var10 = var1 - this.W;
                  int var11 = var2 - this.t;
                  if (var4 - this.H > 500L || var10 < -25 || var10 > 25 || var11 < -25 || var11 > 25) {
                     var9 = false;
                  }
               }

               if (var9) {
                  r var12 = osrs.r.a(osrs.u.an, Client.b.p);
                  var12.f.d(this.T.a());
                  Client.b.a(var12);
                  this.H = 0L;
               }
            }
         }
      } else {
         this.T = null;
      }

   }

   public void b() {
      if (bo.dx != null) {
         this.r = this.s;
      } else {
         if (this.r < this.s) {
            this.r = Math.min(this.s, this.r / 30.0F + this.r);
         }

         if (this.r > this.s) {
            this.r = Math.max(this.s, this.r - this.r / 30.0F);
         }
      }

   }

   public void c() {
      if (this.e()) {
         int var1 = this.v - this.w;
         int var2 = this.B - this.F;
         if (var1 != 0) {
            var1 /= Math.min(8, Math.abs(var1));
         }

         if (var2 != 0) {
            var2 /= Math.min(8, Math.abs(var2));
         }

         this.a(this.w + var1, this.F + var2, true);
         if (this.v == this.w && this.B == this.F) {
            this.v = -1;
            this.B = -1;
         }
      }

   }

   public final void a(int var1, int var2, boolean var3) {
      this.w = var1;
      this.F = var2;
      bd.a();
      if (var3) {
         this.d();
      }

   }

   public final void d() {
      this.u = -1;
      this.X = -1;
      this.n = -1;
      this.Q = -1;
   }

   public boolean e() {
      return this.v != -1 && this.B != -1;
   }

   public bK a(int var1, int var2, int var3) {
      Iterator var4 = this.h.values().iterator();

      while(var4.hasNext()) {
         bK var5 = (bK)var4.next();
         if (var5.a(var1, var2, var3)) {
            return var5;
         }
      }

      return null;
   }

   public void a(int var1, int var2, int var3, boolean var4) {
      bK var5 = this.a(var1, var2, var3);
      if (var5 == null) {
         if (!var4) {
            return;
         }

         var5 = this.i;
      }

      boolean var6 = false;
      if (this.k != var5 || var4) {
         this.k = var5;
         this.a(var5);
         var6 = true;
      }

      if (var6 || var4) {
         this.b(var1, var2, var3);
      }

   }

   public void a(int var1) {
      bK var2 = this.d(var1);
      if (var2 != null) {
         this.a(var2);
      }

   }

   public int f() {
      return this.j == null ? -1 : this.j.b();
   }

   public bK g() {
      return this.j;
   }

   public void a(bK var1) {
      if (this.j == null || this.j != var1) {
         this.b(var1);
         this.b(-1, -1, -1);
      }

   }

   public void b(bK var1) {
      this.j = var1;
      this.l = new bN(this.g, this.f, this.c, this.d);
      this.m.a(this.j == null ? null : this.j.d());
   }

   public void a(bK var1, B var2, B var3, boolean var4) {
      if (var1 != null) {
         if (this.j == null || this.j != var1) {
            this.b(var1);
         }

         if (!var4 && this.j.a(var2.a, var2.b, var2.c)) {
            this.b(var2.a, var2.b, var2.c);
         } else {
            this.b(var3.a, var3.b, var3.c);
         }
      }

   }

   public void b(int var1, int var2, int var3) {
      if (this.j != null) {
         int[] var4 = this.j.b(var1, var2, var3);
         if (var4 == null) {
            var4 = this.j.b(this.j.n(), this.j.m(), this.j.o());
         }

         this.a(var4[0] - this.j.i() * 64, var4[1] - this.j.k() * 64, true);
         this.v = -1;
         this.B = -1;
         this.r = this.b(this.j.h());
         this.s = this.r;
         this.R = null;
         this.S = null;
         this.l.a();
      }

   }

   public void a(int var1, int var2, int var3, int var4, int var5, double var6) {
      int[] var8 = new int[4];
      aU.a(var8);
      aU.a(var1, var2, var1 + var3, var2 + var4);
      int var9 = this.m.c();
      if (var9 < 100) {
         this.a(var1, var2, var3, var4, var9);
      } else {
         if (!this.l.b()) {
            this.l.a(this.b, this.j.d(), Client.v);
            if (!this.l.b()) {
               return;
            }
         }

         int var10 = jt.a(this.l.c(), var6);
         aU.b(var1, var2, var3, var4, var10);
         if (this.A != null) {
            ++this.J;
            if (this.J % this.V == 0) {
               this.J = 0;
               ++this.C;
            }

            if (this.C >= this.p && !this.z) {
               this.A = null;
            }
         }

         int var11 = (int)Math.ceil((double)((float)var3 / this.r));
         int var12 = (int)Math.ceil((double)((float)var4 / this.r));
         double var13 = aW.a();
         this.l.a(this.w - var11 / 2, this.F - var12 / 2, var11 / 2 + this.w, var12 / 2 + this.F, var1, var2, var1 + var3, var2 + var4);
         if (!this.P) {
            boolean var15 = false;
            if (var5 - this.G > 100) {
               this.G = var5;
               var15 = true;
            }

            this.l.a(this.w - var11 / 2, this.F - var12 / 2, var11 / 2 + this.w, var12 / 2 + this.F, var1, var2, var1 + var3, var2 + var4, this.O, this.A, this.J, this.V, var15);
         }

         this.b(var1, var2, var3, var4, var11, var12);
         if (Client.ai() && this.ad && this.T != null) {
            this.e.a("Coord: " + String.valueOf(this.T), aU.c + 10, aU.a + 20, 16776960, -1);
         }

         this.D = var11;
         this.x = var12;
         this.o = var1;
         this.q = var2;
         aU.b(var8);
         if (var13 != aW.a()) {
            aW.a(var13);
         }
      }

   }

   public void h() {
      bc.a.a();
   }

   public boolean a(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (this.U == null) {
         return true;
      } else if (this.U.l == var1 && this.U.m == var2) {
         if (this.l.b != this.I) {
            return true;
         } else if (Client.aI != this.y) {
            return true;
         } else if (var3 <= 0 && var4 <= 0) {
            return var1 + var3 < var5 || var2 + var4 < var6;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public void b(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (bo.dx != null) {
         int var7 = 512 / (this.l.b * 2);
         int var8 = var3 + 512;
         int var9 = var4 + 512;
         float var10 = 1.0F;
         int var11 = (int)((float)var8 / var10);
         int var12 = (int)((float)var9 / var10);
         int var13 = this.l() - var5 / 2 - var7;
         int var14 = this.m() - var6 / 2 - var7;
         int var15 = var1 - this.l.b * (var7 + var13 - this.E);
         int var16 = var2 - this.l.b * (var7 - (var14 - this.Y));
         if (this.a(var11, var12, var15, var16, var3, var4)) {
            if (this.U != null && this.U.l == var11 && this.U.m == var12) {
               Arrays.fill(this.U.k, 0);
            } else {
               this.U = new aV(var11, var12);
            }

            this.E = this.l() - var5 / 2 - var7;
            this.Y = this.m() - var6 / 2 - var7;
            this.I = this.l.b;
            bo.dx.a(this.E, this.Y, this.U, (float)this.I / var10);
            this.y = Client.aI;
            var15 = var1 - this.l.b * (var7 + var13 - this.E);
            var16 = var2 - this.l.b * (var7 - (var14 - this.Y));
         }

         aU.a(var1, var2, var3, var4, 0, 128);
         if (var10 == 1.0F) {
            this.U.d(var15, var16, 192);
         } else {
            this.U.j(var15, var16, (int)((float)var11 * var10), (int)((float)var12 * var10), 192);
         }
      }

   }

   public void a(int var1, int var2, int var3, int var4) {
      if (this.m.b()) {
         if (!this.l.b()) {
            this.l.a(this.b, this.j.d(), Client.v);
            if (!this.l.b()) {
               return;
            }
         }

         this.l.a(var1, var2, var3, var4, this.A, this.J, this.V);
      }

   }

   public void a(int var1, int var2, int var3, int var4, int var5) {
      byte var6 = 20;
      int var7 = var3 / 2 + var1;
      int var8 = var4 / 2 + var2 - 18 - var6;
      aU.b(var1, var2, var3, var4, -16777216);
      aU.c(var7 - 152, var8, 304, 34, -65536);
      aU.b(var7 - 150, var8 + 2, var5 * 3, 30, -65536);
      this.e.c(bv.eZ, var7, var6 + var8, -1, -1);
   }

   public float b(int var1) {
      if (var1 == 25) {
         return 1.0F;
      } else if (var1 == 37) {
         return 1.5F;
      } else if (var1 == 50) {
         return 2.0F;
      } else if (var1 == 75) {
         return 3.0F;
      } else {
         return var1 == 100 ? 4.0F : 8.0F;
      }
   }

   public int i() {
      if ((double)this.s == 1.0) {
         return 25;
      } else if ((double)this.s == 1.5) {
         return 37;
      } else if ((double)this.s == 2.0) {
         return 50;
      } else if ((double)this.s == 3.0) {
         return 75;
      } else {
         return (double)this.s == 4.0 ? 100 : 200;
      }
   }

   public void c(int var1) {
      this.s = this.b(var1);
   }

   public void j() {
      this.m.a();
   }

   public boolean k() {
      return this.m.b();
   }

   public bK d(int var1) {
      Iterator var2 = this.h.values().iterator();

      while(var2.hasNext()) {
         bK var3 = (bK)var2.next();
         if (var3.b() == var1) {
            return var3;
         }
      }

      return null;
   }

   public void a(int var1, int var2) {
      if (this.j != null && this.j.a(var1, var2)) {
         this.v = var1 - this.j.i() * 64;
         this.B = var2 - this.j.k() * 64;
      }

   }

   public void b(int var1, int var2) {
      if (this.j != null) {
         this.a(var1 - this.j.i() * 64, var2 - this.j.k() * 64, true);
         this.v = -1;
         this.B = -1;
      }

   }

   public void c(int var1, int var2, int var3) {
      if (this.j != null) {
         int[] var4 = this.j.b(var1, var2, var3);
         if (var4 != null) {
            this.a(var4[0], var4[1]);
         }
      }

   }

   public void d(int var1, int var2, int var3) {
      if (this.j != null) {
         int[] var4 = this.j.b(var1, var2, var3);
         if (var4 != null) {
            this.b(var4[0], var4[1]);
         }
      }

   }

   public int l() {
      return this.j == null ? -1 : this.w + this.j.i() * 64;
   }

   public int m() {
      return this.j == null ? -1 : this.F + this.j.k() * 64;
   }

   public B n() {
      return this.j == null ? null : this.j.b(this.l(), this.m());
   }

   public int o() {
      return this.D;
   }

   public int p() {
      return this.x;
   }

   public void e(int var1) {
      if (var1 >= 1) {
         this.p = var1;
      }

   }

   public void q() {
      this.p = 3;
   }

   public void f(int var1) {
      if (var1 >= 1) {
         this.V = var1;
      }

   }

   public void r() {
      this.V = 50;
   }

   public void a(boolean var1) {
      this.z = var1;
   }

   public void g(int var1) {
      this.A = new HashSet();
      this.A.add(var1);
      this.C = 0;
      this.J = 0;
   }

   public void h(int var1) {
      this.A = new HashSet();
      this.C = 0;
      this.J = 0;

      for(int var2 = 0; var2 < aE.s; ++var2) {
         if (aE.a(var2) != null && aE.a(var2).q == var1) {
            this.A.add(aE.a(var2).r);
         }
      }

   }

   public void s() {
      this.A = null;
   }

   public void a(int var1, boolean var2) {
      if (!var2) {
         this.L.add(var1);
      } else {
         this.L.remove(var1);
      }

      this.u();
   }

   public void b(int var1, boolean var2) {
      if (!var2) {
         this.M.add(var1);
      } else {
         this.M.remove(var1);
      }

      for(int var3 = 0; var3 < aE.s; ++var3) {
         if (aE.a(var3) != null && aE.a(var3).q == var1) {
            int var4 = aE.a(var3).r;
            if (!var2) {
               this.N.add(var4);
            } else {
               this.N.remove(var4);
            }
         }
      }

      this.u();
   }

   public boolean t() {
      return !this.P;
   }

   public void b(boolean var1) {
      this.P = !var1;
   }

   public boolean i(int var1) {
      return !this.L.contains(var1);
   }

   public boolean j(int var1) {
      return !this.M.contains(var1);
   }

   public void u() {
      this.O.clear();
      this.O.addAll(this.L);
      this.O.addAll(this.N);
   }

   public void c(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (this.m.b()) {
         int var7 = (int)Math.ceil((double)((float)var3 / this.r));
         int var8 = (int)Math.ceil((double)((float)var4 / this.r));
         List var9 = this.l.a(this.w - var7 / 2 - 1, this.F - var8 / 2 - 1, var7 / 2 + this.w + 1, var8 / 2 + this.F + 1, var1, var2, var3, var4, var5, var6);
         if (!var9.isEmpty()) {
            Iterator var10 = var9.iterator();

            boolean var11;
            do {
               if (!var10.hasNext()) {
                  return;
               }

               eo var12 = (eo)var10.next();
               aE var13 = aE.a(var12.a());
               var11 = false;

               for(int var14 = this.ac.length - 1; var14 >= 0; --var14) {
                  if (var13.o[var14] != null) {
                     Client.a(var13.o[var14], var13.v, this.ac[var14], var12.a(), var12.e.a(), var12.f.a());
                     var11 = true;
                  }
               }
            } while(!var11);
         }
      }

   }

   public B a(int var1, B var2) {
      if (!this.m.b()) {
         return null;
      } else if (!this.l.b()) {
         return null;
      } else if (!this.j.a(var2.b, var2.c)) {
         return null;
      } else {
         HashMap var3 = this.l.d();
         List var4 = (List)var3.get(var1);
         if (var4 != null && !var4.isEmpty()) {
            eo var5 = null;
            int var6 = -1;
            Iterator var7 = var4.iterator();

            while(var7.hasNext()) {
               eo var8 = (eo)var7.next();
               int var10 = var8.f.b - var2.b;
               int var11 = var8.f.c - var2.c;
               int var9 = var10 * var10 + var11 * var11;
               if (var9 == 0) {
                  return var8.f;
               }

               if (var9 < var6 || var5 == null) {
                  var5 = var8;
                  var6 = var9;
               }
            }

            return var5.f;
         } else {
            return null;
         }
      }
   }

   public void a(int var1, int var2, B var3, B var4) {
      aF var5 = new aF(var2, var3, var4);
      Object[] var6 = new Object[]{var5};
      ac var7 = null;
      switch (var1) {
         case 1008:
            var7 = osrs.ac.a(10).a(var6).a();
            break;
         case 1009:
            var7 = osrs.ac.a(11).a(var6).a();
            break;
         case 1010:
            var7 = osrs.ac.a(12).a(var6).a();
            break;
         case 1011:
            var7 = osrs.ac.a(13).a(var6).a();
            break;
         case 1012:
            var7 = osrs.ac.a(14).a(var6).a();
      }

      if (var7 != null) {
         bh.a(var7);
      }

   }

   public eo v() {
      if (!this.m.b()) {
         return null;
      } else if (!this.l.b()) {
         return null;
      } else {
         HashMap var1 = this.l.d();
         this.R = new LinkedList();
         Iterator var2 = var1.values().iterator();

         while(var2.hasNext()) {
            List var3 = (List)var2.next();
            this.R.addAll(var3);
         }

         this.S = this.R.iterator();
         return this.w();
      }
   }

   public eo w() {
      if (this.S == null) {
         return null;
      } else {
         while(this.S.hasNext()) {
            eo var1 = (eo)this.S.next();
            if (var1.a() != -1) {
               return var1;
            }
         }

         return null;
      }
   }

   public void setWorldMapPositionTarget(WorldPoint var1) {
      this.c(var1.getX(), var1.getY());
   }

   public Point getWorldMapPosition() {
      bN var1 = this.A();
      int var2 = this.x() + var1.g();
      int var3 = this.y() + var1.f();
      return new Point(var2, var3);
   }

   public WorldMapData getWorldMapData() {
      return this.z();
   }

   public WorldMapRenderer getWorldMapRenderer() {
      return this.A();
   }

   public int x() {
      return this.w;
   }

   public float getWorldMapZoom() {
      return this.r;
   }

   public void initializeWorldMap(net.runelite.api.worldmap.WorldMapData var1) {
      this.b((bK)var1);
   }

   public int y() {
      return this.F;
   }

   public bK z() {
      return this.j;
   }

   public bN A() {
      return this.l;
   }

   public void c(int var1, int var2) {
      this.a(var1, var2);
   }

   static {
      Z = hv.d;
      aa = hv.e;
      ab = hv.f;
   }
}
