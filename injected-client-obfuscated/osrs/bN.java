package osrs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.runelite.api.worldmap.WorldMapRegion;
import net.runelite.api.worldmap.WorldMapRenderer;

public final class bN implements WorldMapRenderer {
   public int a;
   public int b = 0;
   public boolean c = false;
   public boolean d = false;
   public HashMap e = new HashMap();
   public hX[] f;
   public final HashMap g;
   public final au h;
   public final au i;
   public int j;
   public int k;
   public int l;
   public bJ m;
   public aV n;
   public HashMap o;
   public bc[][] p;

   public bN(hX[] var1, HashMap var2, au var3, au var4) {
      this.f = var1;
      this.g = var2;
      this.h = var3;
      this.i = var4;
   }

   public void a(au var1, String var2, boolean var3) {
      if (!this.d) {
         this.c = false;
         this.d = true;
         System.nanoTime();
         int var4 = var1.a(bL.a.f);
         int var5 = var1.a(var4, var2);
         aR var6 = new aR(var1.b(bL.a.f, var2));
         aR var7 = new aR(var1.b(bL.b.f, var2));
         System.nanoTime();
         System.nanoTime();
         this.m = new bJ();

         try {
            this.m.a(var6, var7, var5, var3);
         } catch (IllegalStateException var16) {
            return;
         }

         this.m.m();
         this.m.n();
         this.m.o();
         this.j = this.m.i() * 64;
         this.k = this.m.k() * 64;
         this.l = (this.m.j() - this.m.i() + 1) * 64;
         this.a = (this.m.l() - this.m.k() + 1) * 64;
         int var8 = this.m.j() - this.m.i() + 1;
         int var9 = this.m.l() - this.m.k() + 1;
         System.nanoTime();
         System.nanoTime();
         bc.a.a();
         this.p = new bc[var8][var9];
         Iterator var10 = this.m.c.iterator();

         int var12;
         while(var10.hasNext()) {
            en var11 = (en)var10.next();
            var12 = var11.l;
            int var13 = var11.k;
            int var14 = var12 - this.m.i();
            int var15 = var13 - this.m.k();
            this.p[var14][var15] = new bc(var12, var13, this.m.f(), this.g);
            this.p[var14][var15].a(var11, this.m.a);
         }

         for(int var17 = 0; var17 < var8; ++var17) {
            for(var12 = 0; var12 < var9; ++var12) {
               if (this.p[var17][var12] == null) {
                  this.p[var17][var12] = new bc(this.m.i() + var17, this.m.k() + var12, this.m.f(), this.g);
                  this.p[var17][var12].a(this.m.b, this.m.a);
               }
            }
         }

         System.nanoTime();
         System.nanoTime();
         if (var1.a(bL.c.f, var2)) {
            byte[] var18 = var1.b(bL.c.f, var2);
            this.n = jV.a(var18);
         }

         System.nanoTime();
         var1.i(2025157697);
         var1.a((byte)77);
         this.c = true;
      }

   }

   public final void a() {
      this.o = null;
   }

   public final void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int[] var9 = aU.h;
      int var10 = aU.f;
      int var11 = aU.e;
      float[] var12 = aU.g;
      int[] var13 = new int[4];
      aU.a(var13);
      a var14 = this.a(var1, var2, var3, var4);
      float var15 = this.a(var7 - var5, var3 - var1, -2131071909);
      int var16 = (int)Math.ceil((double)var15);
      this.b = var16;
      if (!this.e.containsKey(var16)) {
         eD var17 = new eD(var16);
         var17.a();
         this.e.put(var16, var17);
      }

      int var24 = var14.c + var14.a - 1;
      int var18 = var14.d + var14.b - 1;

      int var19;
      int var20;
      for(var19 = var14.a; var19 <= var24; ++var19) {
         for(var20 = var14.d; var20 <= var18; ++var20) {
            this.p[var19][var20].a(var16, (eD)this.e.get(var16), this.f, this.h, this.i, 0.725);
         }
      }

      aW.a(var9, var10, var11, var12);
      aU.b(var13);
      var19 = (int)(var15 * 64.0F);
      var20 = this.j + var1;
      int var21 = this.k + var2;

      for(int var22 = var14.a; var22 < var14.c + var14.a; ++var22) {
         for(int var23 = var14.d; var23 < var14.d + var14.b; ++var23) {
            this.p[var22][var23].b((this.p[var22][var23].b * 64 - var20) * var19 / 64 + var5, var8 - (this.p[var22][var23].c * 64 - var21 + 64) * var19 / 64, var19);
         }
      }

   }

   public final void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, HashSet var9, HashSet var10, int var11, int var12, boolean var13) {
      a var14 = this.a(var1, var2, var3, var4);
      float var15 = this.a(var7 - var5, var3 - var1, -2141648661);
      int var16 = (int)(var15 * 64.0F);
      int var17 = this.j + var1;
      int var18 = this.k + var2;

      int var19;
      int var20;
      for(var19 = var14.a; var19 < var14.c + var14.a; ++var19) {
         for(var20 = var14.d; var20 < var14.d + var14.b; ++var20) {
            if (var13) {
               this.p[var19][var20].a();
            }

            this.p[var19][var20].a((this.p[var19][var20].b * 64 - var17) * var16 / 64 + var5, var8 - (this.p[var19][var20].c * 64 - var18 + 64) * var16 / 64, var16, var9);
         }
      }

      if (var10 != null && var11 > 0) {
         for(var19 = var14.a; var19 < var14.c + var14.a; ++var19) {
            for(var20 = var14.d; var20 < var14.d + var14.b; ++var20) {
               this.p[var19][var20].a(var10, var11, var12);
            }
         }
      }

   }

   public void a(int var1, int var2, int var3, int var4, HashSet var5, int var6, int var7) {
      if (this.n != null) {
         this.n.f(var1, var2, var3, var4);
         if (var6 > 0 && var6 % var7 < var7 / 2) {
            if (this.o == null) {
               this.e();
            }

            Iterator var8 = var5.iterator();

            while(true) {
               List var9;
               do {
                  if (!var8.hasNext()) {
                     return;
                  }

                  int var10 = (Integer)var8.next();
                  var9 = (List)this.o.get(var10);
               } while(var9 == null);

               Iterator var14 = var9.iterator();

               while(var14.hasNext()) {
                  eo var11 = (eo)var14.next();
                  int var12 = (var11.f.b - this.j) * var3 / this.l;
                  int var13 = var4 - (var11.f.c - this.k) * var4 / this.a;
                  aU.a(var1 + var12, var2 + var13, 2, 16776960, 256);
               }
            }
         }
      }

   }

   public List a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      LinkedList var11 = new LinkedList();
      if (!this.c) {
         return var11;
      } else {
         a var12 = this.a(var1, var2, var3, var4);
         float var13 = this.a(var7, var3 - var1, -2134997264);
         int var14 = (int)(var13 * 64.0F);
         int var15 = this.j + var1;
         int var16 = this.k + var2;

         for(int var17 = var12.a; var17 < var12.c + var12.a; ++var17) {
            for(int var18 = var12.d; var18 < var12.d + var12.b; ++var18) {
               List var19 = this.p[var17][var18].a((this.p[var17][var18].b * 64 - var15) * var14 / 64 + var5, var6 + var8 - (this.p[var17][var18].c * 64 - var16 + 64) * var14 / 64, var14, var9, var10);
               if (!var19.isEmpty()) {
                  var11.addAll(var19);
               }
            }
         }

         return var11;
      }
   }

   public a a(int var1, int var2, int var3, int var4) {
      a var5 = new a();
      int var6 = this.j + var1;
      int var7 = this.k + var2;
      int var8 = this.j + var3;
      int var9 = this.k + var4;
      int var10 = var6 / 64;
      int var11 = var7 / 64;
      int var12 = var8 / 64;
      int var13 = var9 / 64;
      var5.c = var12 - var10 + 1;
      var5.b = var13 - var11 + 1;
      var5.a = var10 - this.m.i();
      var5.d = var11 - this.m.k();
      if (var5.a < 0) {
         var5.c += var5.a;
         var5.a = 0;
      }

      if (var5.a > this.p.length - var5.c) {
         var5.c = this.p.length - var5.a;
      }

      if (var5.d < 0) {
         var5.b += var5.d;
         var5.d = 0;
      }

      if (var5.d > this.p[0].length - var5.b) {
         var5.b = this.p[0].length - var5.d;
      }

      var5.c = Math.min(var5.c, this.p.length);
      var5.b = Math.min(var5.b, this.p[0].length);
      return var5;
   }

   public boolean b() {
      return this.c;
   }

   public int c() {
      return this.m.g();
   }

   public HashMap d() {
      this.e();
      return this.o;
   }

   public void e() {
      if (this.o == null) {
         this.o = new HashMap();
      }

      this.o.clear();

      for(int var1 = 0; var1 < this.p.length; ++var1) {
         for(int var2 = 0; var2 < this.p[var1].length; ++var2) {
            List var3 = this.p[var1][var2].c();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               eo var5 = (eo)var4.next();
               if (var5.e()) {
                  int var6 = var5.a();
                  if (!this.o.containsKey(var6)) {
                     LinkedList var7 = new LinkedList();
                     var7.add(var5);
                     this.o.put(var6, var7);
                  } else {
                     List var8 = (List)this.o.get(var6);
                     var8.add(var5);
                  }
               }
            }
         }
      }

   }

   public float a(int var1, int var2, int var3) {
      return Client.s.bI().getWorldMapZoom();
   }

   public WorldMapRegion[][] getMapRegions() {
      return this.p;
   }

   public int f() {
      return this.k;
   }

   public boolean isLoaded() {
      return this.c;
   }

   public int g() {
      return this.j;
   }

   public final class a {
      public int a;
      public int b;
      public int c;
      public int d;
   }
}
