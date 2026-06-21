package osrs;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class aG extends cl {
   public int[] a = new int[16];
   public int[] b = new int[16];
   public int[] c = new int[16];
   public int[] d = new int[16];
   public int[] e = new int[16];
   public int[] f = new int[16];
   public int[] g = new int[16];
   public int[] h = new int[16];
   public int[] i = new int[16];
   public int[] j = new int[16];
   public eR[][] k = new eR[16][128];
   public eR[][] l = new eR[16][128];
   public eV m = new eV();
   public boolean n;
   public int o = 256;
   public int p = -872838592;
   public long q;
   public long r;
   public fb s = new fb(this);
   public aQ t = null;
   public static AtomicBoolean u = null;
   public static ThreadPoolExecutor v = null;
   public PriorityQueue w = new PriorityQueue(5, new eS());
   public int x = 0;
   public int[] y = new int[16];
   public int[] z = new int[16];
   public int[] A = new int[16];
   public int[] B = new int[16];
   public int[] C = new int[16];
   public fT D;
   public int E;
   public int F;

   public aG(aQ var1) {
      this.t = var1;
      this.D = new fT(128);
      this.h();
   }

   public void a(int var1) {
      synchronized(this.t) {
         this.o = var1;
      }
   }

   public int a() {
      return this.o;
   }

   public boolean a(eT var1, au var2, iJ var3) {
      synchronized(this.t) {
         boolean var5 = true;
         synchronized(this.w) {
            this.w.clear();
         }

         for(eQ var6 = (eQ)var1.a.b(); var6 != null; var6 = (eQ)var1.a.c()) {
            int var7 = (int)var6.cm;
            eP var8 = (eP)this.D.a((long)var7);
            if (var8 == null) {
               var8 = eP.a(var2, var7);
               if (var8 == null) {
                  var5 = false;
                  continue;
               }

               this.D.a(var8, (long)var7);
            }

            if (!var8.a(var3, var6.b)) {
               var5 = false;
            } else if (this.w != null) {
               synchronized(this.w) {
                  Iterator var10 = var8.a.iterator();

                  while(var10.hasNext()) {
                     ci var11 = (ci)var10.next();
                     this.w.add(new eN(var6.a, var11));
                  }
               }
            }
         }

         return var5;
      }
   }

   public void b() {
      if (this.w != null) {
         if (u != null) {
            u.set(true);
         }

         u = new AtomicBoolean(false);
         AtomicBoolean var1 = u;
         if (v == null) {
            int var2 = Runtime.getRuntime().availableProcessors();
            v = new ThreadPoolExecutor(0, var2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new b());
         }

         v.submit(new a(var1));
      }

   }

   public void c() {
      synchronized(this.t) {
         for(eP var2 = (eP)this.D.b(); var2 != null; var2 = (eP)this.D.c()) {
            var2.a();
         }

      }
   }

   public void d() {
      synchronized(this.t) {
         for(eP var2 = (eP)this.D.b(); var2 != null; var2 = (eP)this.D.c()) {
            var2.X();
         }

      }
   }

   public void a(eT var1, boolean var2) {
      synchronized(this.t) {
         this.e();
         this.m.a(var1.b);
         this.n = var2;
         this.q = 0L;
         int var4 = this.m.c();

         for(int var5 = 0; var5 < var4; ++var5) {
            this.m.a(var5);
            this.m.c(var5);
            this.m.b(var5);
         }

         this.E = this.m.e();
         this.F = this.m.h[this.E];
         this.r = this.m.f(this.F);
      }
   }

   public void e() {
      synchronized(this.t) {
         this.m.a();
         this.h();
      }
   }

   public boolean f() {
      synchronized(this.t) {
         return this.m.b();
      }
   }

   public boolean g() {
      synchronized(this.t) {
         return this.D.a() > 0;
      }
   }

   public void a(int var1, int var2) {
      synchronized(this.t) {
         this.b(var1, var2);
      }
   }

   public void b(int var1, int var2) {
      this.b[var1] = var2;
      this.d[var1] = var2 & -128;
      this.c(var1, var2);
   }

   public void c(int var1, int var2) {
      if (this.c[var1] != var2) {
         this.c[var1] = var2;

         for(int var3 = 0; var3 < 128; ++var3) {
            this.l[var1][var3] = null;
         }
      }

   }

   public void a(int var1, int var2, int var3) {
      this.b(var1, var2, 64);
      if ((this.y[var1] & 2) != 0) {
         for(eR var4 = (eR)this.s.a.e(); var4 != null; var4 = (eR)this.s.a.g()) {
            if (var4.q == var1 && var4.a < 0) {
               this.k[var1][var4.j] = null;
               this.k[var1][var2] = var4;
               int var5 = (var4.n * var4.l >> 12) + var4.m;
               var4.m += var2 - var4.j << 8;
               var4.n = var5 - var4.m;
               var4.l = 4096;
               var4.j = var2;
               return;
            }
         }
      }

      eP var8 = (eP)this.D.a((long)this.c[var1]);
      if (var8 != null && var8.b[var2] != null) {
         ix var9 = var8.b[var2].c();
         if (var9 != null) {
            eR var6 = new eR();
            var6.q = var1;
            var6.t = var8;
            var6.s = var9;
            var6.u = var8.f[var2];
            var6.i = var8.g[var2];
            var6.j = var2;
            var6.k = var8.i * var3 * var3 * var8.d[var2] + 1024 >> 11;
            var6.h = var8.e[var2] & 255;
            var6.m = (var2 << 8) - (var8.c[var2] & 32767);
            var6.p = 0;
            var6.o = 0;
            var6.r = 0;
            var6.a = -1;
            var6.b = 0;
            if (this.z[var1] == 0) {
               var6.e = bT.a(var9, this.a(var6), this.b(var6), this.c(var6));
            } else {
               var6.e = bT.a(var9, this.a(var6), 0, this.c(var6));
               this.a(var6, var8.c[var2] < 0);
            }

            if (var8.c[var2] < 0) {
               var6.e.a(-1);
            }

            if (var6.i >= 0) {
               eR var7 = this.l[var1][var6.i];
               if (var7 != null && var7.a < 0) {
                  this.k[var1][var7.j] = null;
                  var7.a = 0;
               }

               this.l[var1][var6.i] = var6;
            }

            this.s.a.a((az)var6);
            this.k[var1][var2] = var6;
         }
      }

   }

   public void a(eR var1, boolean var2) {
      int var3 = var1.s.d.length;
      int var4;
      if (var2 && var1.s.g) {
         int var5 = var3 + var3 - var1.s.e;
         var4 = (int)((long)this.z[var1.q] * (long)var5 >> 6);
         int var6 = var3 << 8;
         if (var4 >= var6) {
            var4 = var6 + var6 - 1 - var4;
            var1.e.a(true);
         }
      } else {
         var4 = (int)((long)this.z[var1.q] * (long)var3 >> 6);
      }

      var1.e.d(var4);
   }

   public void b(int var1, int var2, int var3) {
      eR var4 = this.k[var1][var2];
      if (var4 != null) {
         this.k[var1][var2] = null;
         if ((this.y[var1] & 2) != 0) {
            for(eR var5 = (eR)this.s.a.d(); var5 != null; var5 = (eR)this.s.a.f()) {
               if (var4.q == var5.q && var5.a < 0 && var4 != var5) {
                  var4.a = 0;
                  break;
               }
            }
         } else {
            var4.a = 0;
         }
      }

   }

   public void c(int var1, int var2, int var3) {
   }

   public void d(int var1, int var2) {
   }

   public void e(int var1, int var2) {
      this.e[var1] = var2;
   }

   public void b(int var1) {
      for(eR var2 = (eR)this.s.a.d(); var2 != null; var2 = (eR)this.s.a.f()) {
         if (var1 < 0 || var2.q == var1) {
            if (var2.e != null) {
               var2.e.e(bo.fG / 100);
               if (var2.e.m()) {
                  this.s.b.a((cl)var2.e);
               }

               var2.a();
            }

            if (var2.a < 0) {
               this.k[var2.q][var2.j] = null;
            }

            var2.X();
         }
      }

   }

   public void c(int var1) {
      if (var1 >= 0) {
         this.B[var1] = 12800;
         this.C[var1] = 8192;
         this.a[var1] = 16383;
         this.e[var1] = 8192;
         this.f[var1] = 0;
         this.g[var1] = 8192;
         this.e(var1);
         this.f(var1);
         this.y[var1] = 0;
         this.h[var1] = 32767;
         this.i[var1] = 256;
         this.z[var1] = 0;
         this.f(var1, 8192);
      } else {
         for(int var2 = 0; var2 < 16; ++var2) {
            this.c(var2);
         }
      }

   }

   public void d(int var1) {
      for(eR var2 = (eR)this.s.a.d(); var2 != null; var2 = (eR)this.s.a.f()) {
         if ((var1 < 0 || var2.q == var1) && var2.a < 0) {
            this.k[var2.q][var2.j] = null;
            var2.a = 0;
         }
      }

   }

   public void h() {
      this.b(-1);
      this.c(-1);

      int var1;
      for(var1 = 0; var1 < 16; ++var1) {
         this.c[var1] = this.b[var1];
      }

      for(var1 = 0; var1 < 16; ++var1) {
         this.d[var1] = this.b[var1] & -128;
      }

   }

   public void e(int var1) {
      if ((this.y[var1] & 2) != 0) {
         for(eR var2 = (eR)this.s.a.d(); var2 != null; var2 = (eR)this.s.a.f()) {
            if (var2.q == var1 && this.k[var1][var2.j] == null && var2.a < 0) {
               var2.a = 0;
            }
         }
      }

   }

   public void f(int var1) {
      if ((this.y[var1] & 4) != 0) {
         for(eR var2 = (eR)this.s.a.d(); var2 != null; var2 = (eR)this.s.a.f()) {
            if (var2.q == var1) {
               var2.g = 0;
            }
         }
      }

   }

   public void g(int var1) {
      int var2 = var1 & 240;
      int var3;
      int var4;
      int var5;
      if (var2 == 128) {
         var3 = var1 & 15;
         var4 = var1 >> 8 & 127;
         var5 = var1 >> 16 & 127;
         this.b(var3, var4, var5);
      } else if (var2 == 144) {
         var3 = var1 & 15;
         var4 = var1 >> 8 & 127;
         var5 = var1 >> 16 & 127;
         if (var5 > 0) {
            this.a(var3, var4, var5);
         } else {
            this.b(var3, var4, 64);
         }
      } else if (var2 == 160) {
         var3 = var1 & 15;
         var4 = var1 >> 8 & 127;
         var5 = var1 >> 16 & 127;
         this.c(var3, var4, var5);
      } else if (var2 == 176) {
         var3 = var1 & 15;
         var4 = var1 >> 8 & 127;
         var5 = var1 >> 16 & 127;
         if (var4 == 0) {
            this.d[var3] = (var5 << 14) + (this.d[var3] & -2080769);
         }

         if (var4 == 32) {
            this.d[var3] = (var5 << 7) + (this.d[var3] & -16257);
         }

         if (var4 == 1) {
            this.f[var3] = (var5 << 7) + (this.f[var3] & -16257);
         }

         if (var4 == 33) {
            this.f[var3] = (this.f[var3] & -128) + var5;
         }

         if (var4 == 5) {
            this.g[var3] = (var5 << 7) + (this.g[var3] & -16257);
         }

         if (var4 == 37) {
            this.g[var3] = (this.g[var3] & -128) + var5;
         }

         if (var4 == 7) {
            this.B[var3] = (var5 << 7) + (this.B[var3] & -16257);
         }

         if (var4 == 39) {
            this.B[var3] = (this.B[var3] & -128) + var5;
         }

         if (var4 == 10) {
            this.C[var3] = (var5 << 7) + (this.C[var3] & -16257);
         }

         if (var4 == 42) {
            this.C[var3] = (this.C[var3] & -128) + var5;
         }

         if (var4 == 11) {
            this.a[var3] = (var5 << 7) + (this.a[var3] & -16257);
         }

         if (var4 == 43) {
            this.a[var3] = (this.a[var3] & -128) + var5;
         }

         int[] var6;
         if (var4 == 64) {
            if (var5 >= 64) {
               var6 = this.y;
               var6[var3] |= 1;
            } else {
               var6 = this.y;
               var6[var3] &= -2;
            }
         }

         if (var4 == 65) {
            if (var5 >= 64) {
               var6 = this.y;
               var6[var3] |= 2;
            } else {
               this.e(var3);
               var6 = this.y;
               var6[var3] &= -3;
            }
         }

         if (var4 == 99) {
            this.h[var3] = (var5 << 7) + (this.h[var3] & 127);
         }

         if (var4 == 98) {
            this.h[var3] = (this.h[var3] & 16256) + var5;
         }

         if (var4 == 101) {
            this.h[var3] = (var5 << 7) + (this.h[var3] & 127) + 16384;
         }

         if (var4 == 100) {
            this.h[var3] = (this.h[var3] & 16256) + 16384 + var5;
         }

         if (var4 == 120) {
            this.b(var3);
         }

         if (var4 == 121) {
            this.c(var3);
         }

         if (var4 == 123) {
            this.d(var3);
         }

         int var7;
         if (var4 == 6) {
            var7 = this.h[var3];
            if (var7 == 16384) {
               this.i[var3] = (var5 << 7) + (this.i[var3] & -16257);
            }
         }

         if (var4 == 38) {
            var7 = this.h[var3];
            if (var7 == 16384) {
               this.i[var3] = (this.i[var3] & -128) + var5;
            }
         }

         if (var4 == 16) {
            this.z[var3] = (var5 << 7) + (this.z[var3] & -16257);
         }

         if (var4 == 48) {
            this.z[var3] = (this.z[var3] & -128) + var5;
         }

         if (var4 == 81) {
            if (var5 >= 64) {
               var6 = this.y;
               var6[var3] |= 4;
            } else {
               this.f(var3);
               var6 = this.y;
               var6[var3] &= -5;
            }
         }

         if (var4 == 17) {
            this.f(var3, (var5 << 7) + (this.j[var3] & -16257));
         }

         if (var4 == 49) {
            this.f(var3, (this.j[var3] & -128) + var5);
         }
      } else if (var2 == 192) {
         var3 = var1 & 15;
         var4 = var1 >> 8 & 127;
         this.c(var3, this.d[var3] + var4);
      } else if (var2 == 208) {
         var3 = var1 & 15;
         var4 = var1 >> 8 & 127;
         this.d(var3, var4);
      } else if (var2 == 224) {
         var3 = var1 & 15;
         var4 = (var1 >> 8 & 127) + (var1 >> 9 & 16256);
         this.e(var3, var4);
      } else {
         var3 = var1 & 255;
         if (var3 == 255) {
            this.h();
         }
      }

   }

   public void f(int var1, int var2) {
      this.j[var1] = var2;
      this.A[var1] = (int)(2097152.0 * Math.pow(2.0, (double)var2 * 5.4931640625E-4) + 0.5);
   }

   public int a(eR var1) {
      int var2 = (var1.n * var1.l >> 12) + var1.m;
      int var3 = ((this.e[var1.q] - 8192) * this.i[var1.q] >> 12) + var2;
      eW var4 = var1.u;
      int var5;
      if (var4.f > 0 && (var4.g > 0 || this.f[var1.q] > 0)) {
         var5 = var4.g << 2;
         int var6 = var4.e << 1;
         if (var1.c < var6) {
            var5 = var1.c * var5 / var6;
         }

         int var7 = (this.f[var1.q] >> 7) + var5;
         double var8 = Math.sin((double)(var1.d & 511) * 0.01227184630308513);
         var3 += (int)((double)var7 * var8);
      }

      var5 = (int)((double)(var1.s.c * 256) * Math.pow(2.0, (double)var3 * 3.255208333333333E-4) / (double)bo.fG + 0.5);
      return var5 < 1 ? 1 : var5;
   }

   public int b(eR var1) {
      eW var2 = var1.u;
      int var3 = this.a[var1.q] * this.B[var1.q] + 4096 >> 13;
      int var4 = var3 * var3 + 16384 >> 15;
      int var5 = var1.k * var4 + 16384 >> 15;
      int var6 = this.o * var5 + 128 >> 8;
      if (var2.b > 0) {
         var6 = (int)((double)var6 * Math.pow(0.5, (double)var1.p * 1.953125E-5 * (double)var2.b) + 0.5);
      }

      int var7;
      int var8;
      int var9;
      int var10;
      if (var2.h != null) {
         var7 = var1.o;
         var8 = var2.h[var1.r + 1];
         if (var1.r < var2.h.length - 2) {
            var9 = (var2.h[var1.r] & 255) << 8;
            var10 = (var2.h[var1.r + 2] & 255) << 8;
            var8 += (var2.h[var1.r + 3] - var8) * (var7 - var9) / (var10 - var9);
         }

         var6 = var6 * var8 + 32 >> 6;
      }

      if (var1.a > 0 && var2.i != null) {
         var7 = var1.a;
         var8 = var2.i[var1.b + 1];
         if (var1.b < var2.i.length - 2) {
            var9 = (var2.i[var1.b] & 255) << 8;
            var10 = (var2.i[var1.b + 2] & 255) << 8;
            var8 += (var2.i[var1.b + 3] - var8) * (var7 - var9) / (var10 - var9);
         }

         var6 = var6 * var8 + 32 >> 6;
      }

      return var6;
   }

   public int c(eR var1) {
      int var2 = this.C[var1.q];
      return var2 < 8192 ? var1.h * var2 + 32 >> 6 : 16384 - ((128 - var1.h) * (16384 - var2) + 32 >> 6);
   }

   public cl i() {
      return this.s;
   }

   public cl j() {
      return null;
   }

   public int k() {
      return 0;
   }

   public void a(int[] var1, int var2, int var3) {
      if (this.m.b()) {
         int var4 = this.p * 1987034081 * this.m.c / bo.fG;

         do {
            long var5 = (long)var3 * (long)var4 + this.q;
            if (this.r - var5 >= 0L) {
               this.q = var5;
               break;
            }

            int var7 = (int)((this.r - this.q + (long)var4 - 1L) / (long)var4);
            this.q += (long)var4 * (long)var7;
            this.s.a(var1, var2, var7);
            var2 += var7;
            var3 -= var7;
            this.l();
         } while(this.m.b());
      }

      this.s.a(var1, var2, var3);
   }

   public void h(int var1) {
      if (this.m.b()) {
         int var2 = this.p * 1987034081 * this.m.c / bo.fG;

         do {
            long var3 = (long)var1 * (long)var2 + this.q;
            if (this.r - var3 >= 0L) {
               this.q = var3;
               break;
            }

            int var5 = (int)((this.r - this.q + (long)var2 - 1L) / (long)var2);
            this.q += (long)var2 * (long)var5;
            this.s.h(var5);
            var1 -= var5;
            this.l();
         } while(this.m.b());
      }

      this.s.h(var1);
   }

   public void l() {
      int var1 = this.E;
      int var2 = this.F;

      long var3;
      for(var3 = this.r; this.F == var2; var3 = this.m.f(var2)) {
         while(this.m.h[var1] == var2) {
            this.m.a(var1);
            int var5 = this.m.d(var1);
            if (var5 == 1) {
               this.m.d();
               this.m.b(var1);
               if (this.m.f()) {
                  if (!this.n || var2 == 0) {
                     this.h();
                     this.m.a();
                     return;
                  }

                  this.m.a(var3);
               }
               break;
            }

            if ((var5 & 128) != 0) {
               this.g(var5);
            }

            this.m.c(var1);
            this.m.b(var1);
         }

         var1 = this.m.e();
         var2 = this.m.h[var1];
      }

      this.E = var1;
      this.F = var2;
      this.r = var3;
   }

   public boolean d(eR var1) {
      if (var1.e == null) {
         if (var1.a >= 0) {
            var1.X();
            if (var1.i > 0 && this.l[var1.q][var1.i] == var1) {
               this.l[var1.q][var1.i] = null;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean a(eR var1, int[] var2, int var3, int var4) {
      var1.f = bo.fG / 100 * -418814767;
      if (var1.a < 0 || var1.e != null && !var1.e.l()) {
         int var5 = var1.l;
         if (var5 > 0) {
            int var6 = var5 - (int)(16.0 * Math.pow(2.0, (double)this.g[var1.q] * 4.921259842519685E-4) + 0.5);
            if (var6 < 0) {
               var6 = 0;
            }

            var1.l = var6;
         }

         var1.e.f(this.a(var1));
         eW var10 = var1.u;
         boolean var7 = false;
         ++var1.c;
         var1.d += var10.f;
         double var8 = (double)((var1.j - 60 << 8) + (var1.n * var1.l >> 12)) * 5.086263020833333E-6;
         if (var10.b > 0) {
            if (var10.a > 0) {
               var1.p += (int)(128.0 * Math.pow(2.0, (double)var10.a * var8) + 0.5);
            } else {
               var1.p += 128;
            }
         }

         if (var10.h != null) {
            if (var10.c > 0) {
               var1.o += (int)(128.0 * Math.pow(2.0, (double)var10.c * var8) + 0.5);
            } else {
               var1.o += 128;
            }

            while(var1.r < var10.h.length - 2 && var1.o > (var10.h[var1.r + 2] & 255) << 8) {
               var1.r += 2;
            }

            if (var1.r == var10.h.length - 2 && var10.h[var1.r + 1] == 0) {
               var7 = true;
            }
         }

         if (var1.a >= 0 && var10.i != null && (this.y[var1.q] & 1) == 0 && (var1.i < 0 || this.l[var1.q][var1.i] != var1)) {
            if (var10.d > 0) {
               var1.a += (int)(128.0 * Math.pow(2.0, (double)var10.d * var8) + 0.5);
            } else {
               var1.a += 128;
            }

            while(var1.b < var10.i.length - 2 && var1.a > (var10.i[var1.b + 2] & 255) << 8) {
               var1.b += 2;
            }

            if (var1.b == var10.i.length - 2) {
               var7 = true;
            }
         }

         if (var7) {
            var1.e.e(var1.f * -764054479);
            if (var2 != null) {
               var1.e.a(var2, var3, var4);
            } else {
               var1.e.h(var4);
            }

            if (var1.e.m()) {
               this.s.b.a((cl)var1.e);
            }

            var1.a();
            if (var1.a >= 0) {
               var1.X();
               if (var1.i > 0 && this.l[var1.q][var1.i] == var1) {
                  this.l[var1.q][var1.i] = null;
               }
            }

            return true;
         } else {
            var1.e.a(var1.f * -764054479, this.b(var1), this.c(var1));
            return false;
         }
      } else {
         var1.a();
         var1.X();
         if (var1.i > 0 && this.l[var1.q][var1.i] == var1) {
            this.l[var1.q][var1.i] = null;
         }

         return true;
      }
   }

   class a implements Callable {
      public final AtomicBoolean a;

      public a(AtomicBoolean var2) {
         this.a = var2;
      }

      public Object call() {
         PriorityQueue var1 = aG.this.w;

         while(true) {
            Object var2 = null;
            eN var3;
            synchronized(var1) {
               if (var1.isEmpty() || this.a.get()) {
                  return null;
               }

               var3 = (eN)var1.remove();
            }

            var3.b.b();
         }
      }
   }

   class b implements ThreadFactory {
      public b() {
      }

      public Thread newThread(Runnable var1) {
         return new Thread(var1, "OSRS WAV Load");
      }
   }
}
