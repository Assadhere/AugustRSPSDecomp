package osrs;

import java.util.Objects;

public final class fX {
   public static fX a;
   public float b;
   public float c;
   public float d;
   public float e;
   public float f;
   public float g;
   public static int h;
   public static final int i;
   public static final fX[] j;
   public float k;
   public float l;
   public float m;
   public float n;
   public float o;
   public float p;
   public float q;
   public float r;
   public float s;
   public float t;
   public float[] u;

   public static fX a() {
      synchronized(j) {
         if (h == 0) {
            return new fX();
         } else {
            j[--h].d();
            return j[h];
         }
      }
   }

   public void b() {
      synchronized(j) {
         if (h < i) {
            j[++h - 1] = this;
         }

      }
   }

   public float[] c() {
      float[] var1 = new float[]{(float)(-Math.asin((double)this.t)), 0.0F, 0.0F};
      double var2 = Math.cos((double)var1[0]);
      double var4;
      double var6;
      if (Math.abs(var2) > 0.005) {
         var4 = (double)this.r;
         var6 = (double)this.g;
         double var8 = (double)this.l;
         double var10 = (double)this.f;
         var1[1] = (float)Math.atan2(var4, var6);
         var1[2] = (float)Math.atan2(var8, var10);
      } else {
         var4 = (double)this.m;
         var6 = (double)this.c;
         if (this.t < 0.0F) {
            var1[1] = (float)Math.atan2(var4, var6);
         } else {
            var1[1] = (float)(-Math.atan2(var4, var6));
         }

         var1[2] = 0.0F;
      }

      return var1;
   }

   public void d() {
      this.c = 1.0F;
      this.m = 0.0F;
      this.r = 0.0F;
      this.q = 0.0F;
      this.l = 0.0F;
      this.f = 1.0F;
      this.t = 0.0F;
      this.d = 0.0F;
      this.n = 0.0F;
      this.b = 0.0F;
      this.g = 1.0F;
      this.s = 0.0F;
      this.k = 0.0F;
      this.p = 0.0F;
      this.o = 0.0F;
      this.e = 1.0F;
   }

   public void e() {
      this.c = 0.0F;
      this.m = 0.0F;
      this.r = 0.0F;
      this.q = 0.0F;
      this.l = 0.0F;
      this.f = 0.0F;
      this.t = 0.0F;
      this.d = 0.0F;
      this.n = 0.0F;
      this.b = 0.0F;
      this.g = 0.0F;
      this.s = 0.0F;
      this.k = 0.0F;
      this.p = 0.0F;
      this.o = 0.0F;
      this.e = 0.0F;
   }

   public void a(float var1, float var2, float var3) {
      this.d();
      this.c = var1;
      this.f = var2;
      this.g = var3;
   }

   public void a(fX var1) {
      float var2 = this.q * var1.k + this.r * var1.n + this.c * var1.c + this.m * var1.l;
      float var3 = this.q * var1.p + this.r * var1.b + this.c * var1.m + this.m * var1.f;
      float var4 = this.q * var1.o + this.r * var1.g + this.c * var1.r + this.m * var1.t;
      float var5 = this.q * var1.e + this.r * var1.s + this.c * var1.q + this.m * var1.d;
      float var6 = this.d * var1.k + this.t * var1.n + this.f * var1.l + this.l * var1.c;
      float var7 = this.d * var1.p + this.t * var1.b + this.f * var1.f + this.l * var1.m;
      float var8 = this.d * var1.o + this.t * var1.g + this.f * var1.t + this.l * var1.r;
      float var9 = this.d * var1.e + this.t * var1.s + this.f * var1.d + this.l * var1.q;
      float var10 = this.s * var1.k + this.g * var1.n + this.n * var1.c + this.b * var1.l;
      float var11 = this.s * var1.p + this.g * var1.b + this.n * var1.m + this.b * var1.f;
      float var12 = this.s * var1.o + this.g * var1.g + this.n * var1.r + this.b * var1.t;
      float var13 = this.s * var1.e + this.g * var1.s + this.n * var1.q + this.b * var1.d;
      float var14 = this.e * var1.k + this.o * var1.n + this.p * var1.l + this.k * var1.c;
      float var15 = this.e * var1.p + this.o * var1.b + this.p * var1.f + this.k * var1.m;
      float var16 = this.e * var1.o + this.o * var1.g + this.p * var1.t + this.k * var1.r;
      float var17 = this.e * var1.e + this.o * var1.s + this.p * var1.d + this.k * var1.q;
      this.c = var2;
      this.m = var3;
      this.r = var4;
      this.q = var5;
      this.l = var6;
      this.f = var7;
      this.t = var8;
      this.d = var9;
      this.n = var10;
      this.b = var11;
      this.g = var12;
      this.s = var13;
      this.k = var14;
      this.p = var15;
      this.o = var16;
      this.e = var17;
   }

   public void a(fY var1) {
      float var2 = var1.f * var1.f;
      float var3 = var1.f * var1.d;
      float var4 = var1.f * var1.e;
      float var5 = var1.g * var1.f;
      float var6 = var1.d * var1.d;
      float var7 = var1.e * var1.d;
      float var8 = var1.g * var1.d;
      float var9 = var1.e * var1.e;
      float var10 = var1.g * var1.e;
      float var11 = var1.g * var1.g;
      this.c = var2 + var6 - var11 - var9;
      this.m = var5 + var7 + var7 + var5;
      this.r = var8 - var4 - var4 + var8;
      this.l = var7 - var5 - var5 + var7;
      this.f = var2 + var9 - var6 - var11;
      this.t = var3 + var10 + var10 + var3;
      this.n = var4 + var8 + var8 + var4;
      this.b = var10 - var3 - var3 + var10;
      this.g = var2 + var11 - var9 - var6;
   }

   public void a(gf var1) {
      this.c = var1.j;
      this.m = var1.b;
      this.r = var1.d;
      this.q = 0.0F;
      this.l = var1.l;
      this.f = var1.e;
      this.t = var1.g;
      this.d = 0.0F;
      this.n = var1.h;
      this.b = var1.i;
      this.g = var1.f;
      this.s = 0.0F;
      this.k = var1.k;
      this.p = var1.c;
      this.o = var1.m;
      this.e = 1.0F;
   }

   public float f() {
      return this.t * this.q * this.b * this.k + (this.q * this.f * this.n * this.o + this.q * this.l * this.g * this.p + (this.r * this.d * this.n * this.p + this.r * this.f * this.s * this.k + (this.r * this.l * this.b * this.e + this.m * this.d * this.g * this.k + (this.t * this.m * this.n * this.e + this.m * this.l * this.s * this.o + (this.c * this.d * this.b * this.o + this.c * this.t * this.s * this.p + (this.c * this.f * this.g * this.e - this.c * this.f * this.s * this.o - this.c * this.t * this.b * this.e) - this.c * this.d * this.g * this.p - this.m * this.l * this.g * this.e) - this.t * this.m * this.s * this.k - this.m * this.d * this.n * this.o) - this.r * this.l * this.s * this.p - this.r * this.f * this.n * this.e) - this.r * this.d * this.b * this.k - this.q * this.l * this.b * this.o) - this.q * this.f * this.g * this.k - this.t * this.q * this.n * this.p);
   }

   public void g() {
      float var1 = 1.0F / this.f();
      float var2 = (this.d * this.b * this.o + this.t * this.s * this.p + (this.g * this.f * this.e - this.s * this.f * this.o - this.t * this.b * this.e) - this.g * this.d * this.p) * var1;
      float var3 = (this.g * this.q * this.p + (this.r * this.b * this.e + -this.m * this.g * this.e + this.s * this.m * this.o - this.s * this.r * this.p - this.q * this.b * this.o)) * var1;
      float var4 = (this.q * this.f * this.o + this.r * this.d * this.p + (this.t * this.m * this.e - this.m * this.d * this.o - this.r * this.f * this.e) - this.t * this.q * this.p) * var1;
      float var5 = (this.t * this.q * this.b + (this.r * this.f * this.s + -this.m * this.t * this.s + this.m * this.d * this.g - this.r * this.d * this.b - this.q * this.f * this.g)) * var1;
      float var6 = (this.g * this.d * this.k + (this.t * this.n * this.e + -this.l * this.g * this.e + this.s * this.l * this.o - this.t * this.s * this.k - this.n * this.d * this.o)) * var1;
      float var7 = (this.q * this.n * this.o + this.s * this.r * this.k + (this.c * this.g * this.e - this.c * this.s * this.o - this.r * this.n * this.e) - this.g * this.q * this.k) * var1;
      float var8 = (this.t * this.q * this.k + (this.r * this.l * this.e + -this.c * this.t * this.e + this.c * this.d * this.o - this.r * this.d * this.k - this.q * this.l * this.o)) * var1;
      float var9 = (this.q * this.l * this.g + this.r * this.d * this.n + (this.c * this.t * this.s - this.c * this.d * this.g - this.r * this.l * this.s) - this.t * this.q * this.n) * var1;
      float var10 = (this.n * this.d * this.p + this.s * this.f * this.k + (this.l * this.b * this.e - this.s * this.l * this.p - this.n * this.f * this.e) - this.d * this.b * this.k) * var1;
      float var11 = (this.q * this.b * this.k + (this.n * this.m * this.e + -this.c * this.b * this.e + this.c * this.s * this.p - this.s * this.m * this.k - this.q * this.n * this.p)) * var1;
      float var12 = (this.q * this.l * this.p + this.m * this.d * this.k + (this.c * this.f * this.e - this.c * this.d * this.p - this.m * this.l * this.e) - this.q * this.f * this.k) * var1;
      float var13 = (this.q * this.f * this.n + (this.m * this.l * this.s + -this.c * this.f * this.s + this.c * this.d * this.b - this.m * this.d * this.n - this.q * this.l * this.b)) * var1;
      float var14 = (this.t * this.b * this.k + (this.n * this.f * this.o + -this.l * this.b * this.o + this.g * this.l * this.p - this.g * this.f * this.k - this.t * this.n * this.p)) * var1;
      float var15 = (this.r * this.n * this.p + this.g * this.m * this.k + (this.c * this.b * this.o - this.c * this.g * this.p - this.n * this.m * this.o) - this.r * this.b * this.k) * var1;
      float var16 = (this.r * this.f * this.k + (this.m * this.l * this.o + -this.c * this.f * this.o + this.c * this.t * this.p - this.t * this.m * this.k - this.r * this.l * this.p)) * var1;
      float var17 = (this.r * this.l * this.b + this.t * this.m * this.n + (this.c * this.f * this.g - this.c * this.t * this.b - this.m * this.l * this.g) - this.r * this.f * this.n) * var1;
      this.c = var2;
      this.m = var3;
      this.r = var4;
      this.q = var5;
      this.l = var6;
      this.f = var7;
      this.t = var8;
      this.d = var9;
      this.n = var10;
      this.b = var11;
      this.g = var12;
      this.s = var13;
      this.k = var14;
      this.p = var15;
      this.o = var16;
      this.e = var17;
   }

   public void a(float var1, float var2, float var3, float var4) {
      this.c = var1;
      this.m = 0.0F;
      this.r = 0.0F;
      this.q = 0.0F;
      this.l = 0.0F;
      this.f = var2;
      this.t = 0.0F;
      this.d = 0.0F;
      this.n = 0.0F;
      this.b = 0.0F;
      this.g = var3;
      this.s = 0.0F;
      this.k = 0.0F;
      this.p = 0.0F;
      this.o = 0.0F;
      this.e = var4;
   }

   public void a(float var1, float var2, float var3, float[] var4) {
      var4[0] = this.n * var3 + this.c * var1 + this.l * var2 + this.k;
      var4[1] = this.b * var3 + this.f * var2 + this.m * var1 + this.p;
      var4[2] = this.g * var3 + this.t * var2 + this.r * var1 + this.o;
      if (var4.length > 3) {
         var4[3] = this.s * var3 + this.q * var1 + this.d * var2 + this.e;
      }

   }

   public bE b(float var1, float var2, float var3) {
      float var4 = 1.0F / (this.s * var3 + this.q * var1 + this.d * var2 + this.e);
      return bE.a((this.n * var3 + this.c * var1 + this.l * var2 + this.k) * var4, (this.b * var3 + this.f * var2 + this.m * var1 + this.p) * var4, (this.g * var3 + this.t * var2 + this.r * var1 + this.o) * var4);
   }

   public float c(float var1, float var2, float var3) {
      return this.n * var3 + this.c * var1 + this.l * var2 + this.k;
   }

   public float d(float var1, float var2, float var3) {
      return this.b * var3 + this.f * var2 + this.m * var1 + this.p;
   }

   public float e(float var1, float var2, float var3) {
      return this.g * var3 + this.t * var2 + this.r * var1 + this.o;
   }

   public float[] h() {
      float[] var1 = new float[3];
      bE var2 = new bE(this.c, this.m, this.r);
      bE var3 = new bE(this.l, this.f, this.t);
      bE var4 = new bE(this.n, this.b, this.g);
      var1[0] = var2.e();
      var1[1] = var3.e();
      var1[2] = var4.e();
      return var1;
   }

   public void a(aR var1, boolean var2, int var3) {
      if (var2) {
         aR var4 = var1;
         fX var5 = this;
         if (var2) {
            gf var6 = new gf();
            var6.a(hh.a(var1.e()));
            var6.b(hh.a(var1.e()));
            var6.c(hh.a(var1.e()));
            var6.a((float)var1.e(), (float)var1.e(), (float)var1.e());
            this.a(var6);
         } else {
            for(int var7 = 0; var7 < 16; ++var7) {
               var5.u[var7] = var4.j();
            }
         }
      } else {
         this.c = var1.V();
         this.m = var1.V();
         this.r = var1.V();
         this.q = var1.V();
         this.l = var1.V();
         this.f = var1.V();
         this.t = var1.V();
         this.d = var1.V();
         this.n = var1.V();
         this.b = var1.V();
         this.g = var1.V();
         this.s = var1.V();
         this.k = var1.V();
         this.p = var1.V();
         this.o = var1.V();
         this.e = var1.V();
      }

   }

   public static final void a(int var0, int var1, int var2, int var3, boolean var4, byte var5) {
      if (var4) {
         ja.b(bo.aQ);
      }

      if (var2 < 1) {
         var2 = 1;
      }

      if (var3 < 1) {
         var3 = 1;
      }

      int var6 = var3 - 334;
      double var7;
      if (var6 < 0) {
         var7 = (double)Client.ar;
      } else if (var6 >= 100) {
         var7 = (double)Client.as;
      } else {
         var7 = (double)((Client.as - Client.ar) * var6 / 100 + Client.ar);
      }

      double var9 = (double)var3 * var7 * 512.0 / (double)(var2 * 334);
      double var11;
      double var13;
      int var15;
      if (var9 < (double)Client.ax) {
         var11 = (double)Client.ax;
         var7 = (double)var2 * var11 * 334.0 / (double)(var3 * 512);
         if (var7 > (double)Client.aw) {
            var7 = (double)Client.aw;
            var13 = (double)var3 * var7 * 512.0 / (var11 * 334.0);
            var15 = (int)(((double)var2 - var13) / 2.0);
            if (var4) {
               df.j();
               df.k(var0, var1, var15, var3, -16777216);
               df.k(var0 + var2 - var15, var1, var15, var3, -16777216);
            }

            var0 += var15;
            var2 -= var15 * 2;
         }
      } else if (var9 > (double)Client.ay) {
         var11 = (double)Client.ay;
         var7 = (double)var2 * var11 * 334.0 / (double)(var3 * 512);
         if (var7 < (double)Client.av) {
            var7 = (double)Client.av;
            var13 = (double)var2 * var11 * 334.0 / (var7 * 512.0);
            var15 = (int)(((double)var3 - var13) / 2.0);
            if (var4) {
               df.j();
               df.k(var0, var1, var2, var15, -16777216);
               df.k(var0, var1 + var3 - var15, var2, var15, -16777216);
            }

            var1 += var15;
            var3 -= var15 * 2;
         }
      }

      Client.aD = (int)((double)var3 * var7 / 334.0);
      if (Client.aB != var2 || Client.aC != var3) {
         Client.m(var2, var3);
      }

      Client.az = var0;
      Client.aA = var1;
      Client.aB = var2;
      Client.aC = var3;
   }

   public fX(fX var1) {
      this.e(var1);
   }

   public static fX b(fX var0) {
      synchronized(j) {
         if (h == 0) {
            return new fX(var0);
         } else {
            fX var2 = j[--h];
            var2.e(var0);
            return var2;
         }
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         fX var2 = (fX)var1;
         return Float.compare(var2.c, this.c) == 0 && Float.compare(var2.m, this.m) == 0 && Float.compare(var2.r, this.r) == 0 && Float.compare(var2.q, this.q) == 0 && Float.compare(var2.l, this.l) == 0 && Float.compare(var2.f, this.f) == 0 && Float.compare(var2.t, this.t) == 0 && Float.compare(var2.d, this.d) == 0 && Float.compare(var2.n, this.n) == 0 && Float.compare(var2.b, this.b) == 0 && Float.compare(var2.g, this.g) == 0 && Float.compare(var2.s, this.s) == 0 && Float.compare(var2.k, this.k) == 0 && Float.compare(var2.p, this.p) == 0 && Float.compare(var2.o, this.o) == 0 && Float.compare(var2.e, this.e) == 0;
      } else {
         return false;
      }
   }

   public void a(fX var1, int var2) {
      this.c += var1.c;
      this.m += var1.m;
      this.r += var1.r;
      this.q += var1.q;
      this.l += var1.l;
      this.f += var1.f;
      this.t += var1.t;
      this.d += var1.d;
      this.n += var1.n;
      this.b += var1.b;
      this.g += var1.g;
      this.s += var1.s;
      this.k += var1.k;
      this.p += var1.p;
      this.o += var1.o;
      this.e += var1.e;
   }

   public void a(ge var1, byte var2) {
      float var3 = var1.c.f * var1.c.f;
      float var4 = var1.c.f * var1.c.d;
      float var5 = var1.c.f * var1.c.e;
      float var6 = var1.c.g * var1.c.f;
      float var7 = var1.c.d * var1.c.d;
      float var8 = var1.c.e * var1.c.d;
      float var9 = var1.c.g * var1.c.d;
      float var10 = var1.c.e * var1.c.e;
      float var11 = var1.c.g * var1.c.e;
      float var12 = var1.c.g * var1.c.g;
      this.c = var3 + var7 - var12 - var10;
      this.m = var6 + var8 + var6 + var8;
      this.r = var9 - var5 - var5 + var9;
      this.l = var8 - var6 - var6 + var8;
      this.f = var3 + var10 - var7 - var12;
      this.t = var11 + var11 + var4 + var4;
      this.n = var9 + var9 + var5 + var5;
      this.b = var11 - var4 - var4 + var11;
      this.g = var3 + var12 - var10 - var7;
      this.s = 0.0F;
      this.d = 0.0F;
      this.q = 0.0F;
      this.k = var1.d.h;
      this.p = var1.d.i;
      this.o = var1.d.j;
      this.e = 1.0F;
   }

   public void a(float[] var1) {
      var1[0] = this.c;
      var1[1] = this.m;
      var1[2] = this.r;
      var1[3] = this.q;
      var1[4] = this.l;
      var1[5] = this.f;
      var1[6] = this.t;
      var1[7] = this.d;
      var1[8] = this.n;
      var1[9] = this.b;
      var1[10] = this.g;
      var1[11] = this.s;
      var1[12] = this.k;
      var1[13] = this.p;
      var1[14] = this.o;
      var1[15] = this.e;
   }

   public fX(aR var1, boolean var2) {
      this.a(var1, var2);
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.c, this.m, this.r, this.q, this.l, this.f, this.t, this.d, this.n, this.b, this.g, this.s, this.k, this.p, this.o, this.e});
   }

   public fX() {
      this.c = 1.0F;
      this.f = 1.0F;
      this.g = 1.0F;
      this.e = 1.0F;
   }

   public String toString() {
      return "Mat4{m0=" + this.c + ", m1=" + this.m + ", m2=" + this.r + ", m3=" + this.q + ", m4=" + this.l + ", m5=" + this.f + ", m6=" + this.t + ", m7=" + this.d + ", m8=" + this.n + ", m9=" + this.b + ", m10=" + this.g + ", m11=" + this.s + ", m12=" + this.k + ", m13=" + this.p + ", m14=" + this.o + ", m15=" + this.e + "}";
   }

   public static fX i() {
      synchronized(j) {
         if (h == 0) {
            return new fX();
         } else {
            fX var1 = j[--h];
            var1.j();
            return var1;
         }
      }
   }

   public void b(fX var1, int var2) {
      this.c = var1.c;
      this.m = var1.m;
      this.r = var1.r;
      this.q = var1.q;
      this.l = var1.l;
      this.f = var1.f;
      this.t = var1.t;
      this.d = var1.d;
      this.n = var1.n;
      this.b = var1.b;
      this.g = var1.g;
      this.s = var1.s;
      this.k = var1.k;
      this.p = var1.p;
      this.o = var1.o;
      this.e = var1.e;
   }

   public void b(float var1, float var2, float var3, float var4) {
      this.a(var1, var2, var3, var4);
   }

   public void j() {
      this.d();
   }

   public void a(aR var1, boolean var2) {
      this.a(var1, var2, -2072268879);
   }

   public float f(float var1, float var2, float var3) {
      return this.e(var1, var2, var3);
   }

   public void k() {
      this.e();
   }

   public void l() {
      this.b();
   }

   public void m() {
      this.g();
   }

   public float g(float var1, float var2, float var3) {
      return this.d(var1, var2, var3);
   }

   public void b(float var1, float var2, float var3, float[] var4) {
      this.a(var1, var2, var3, var4);
   }

   public void b(gf var1) {
      this.a(var1);
   }

   public void c(fX var1) {
      this.a(var1);
   }

   public float h(float var1, float var2, float var3) {
      return this.c(var1, var2, var3);
   }

   public void b(fY var1) {
      this.a(var1);
   }

   public void a(ge var1) {
      this.a((ge)var1, (byte)79);
   }

   public void i(float var1, float var2, float var3) {
      this.a(var1, var2, var3);
   }

   public void d(fX var1) {
      this.a(var1, 1527666798);
   }

   public void e(fX var1) {
      this.b(var1, -546471939);
   }

   static {
      new fX();
      h = 0;
      i = 16;
      j = new fX[i];
      a = new fX();
   }
}
