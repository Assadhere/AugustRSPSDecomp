package osrs;

public class bE {
   public static final bE a = new bE(0.0F, 0.0F, 0.0F);
   public static final bE b;
   public static final bE c;
   public static final bE d;
   public static int e;
   public static final int f;
   public static final bE[] g;
   public float h;
   public float i;
   public float j;

   public bE() {
   }

   public bE(float var1, float var2, float var3) {
      this.h = var1;
      this.i = var2;
      this.j = var3;
   }

   public bE(bE var1) {
      this.h = var1.h;
      this.i = var1.i;
      this.j = var1.j;
   }

   public static bE a(float var0, float var1, float var2) {
      synchronized(g) {
         if (e == 0) {
            return new bE(var0, var1, var2);
         } else {
            g[--e].b(var0, var1, var2);
            return g[e];
         }
      }
   }

   public static bE a(bE var0) {
      synchronized(g) {
         if (e == 0) {
            return new bE(var0);
         } else {
            g[--e].b(var0);
            return g[e];
         }
      }
   }

   public static final bE a(bE var0, bE var1) {
      bE var2 = a(var0);
      var2.d(var1);
      return var2;
   }

   public static final bE b(bE var0, bE var1) {
      bE var2 = a(var0);
      var2.e(var1);
      return var2;
   }

   public void a() {
      synchronized(g) {
         if (e < f) {
            g[++e - 1] = this;
         }

      }
   }

   public void b(float var1, float var2, float var3) {
      this.h = var1;
      this.i = var2;
      this.j = var3;
   }

   public void b(bE var1) {
      this.b(var1.h, var1.i, var1.j);
   }

   public final void b() {
      this.j = 0.0F;
      this.i = 0.0F;
      this.h = 0.0F;
   }

   public boolean c(bE var1) {
      return this.h == var1.h && this.i == var1.i && this.j == var1.j;
   }

   public final boolean c() {
      return A.a(this.h) & A.a(this.i) & A.a(this.j);
   }

   public final void d() {
      float var1 = 1.0F / this.e();
      this.h *= var1;
      this.i *= var1;
      this.j *= var1;
   }

   public final void d(bE var1) {
      this.h += var1.h;
      this.i += var1.i;
      this.j += var1.j;
   }

   public final void c(float var1, float var2, float var3) {
      this.h += var1;
      this.i += var2;
      this.j += var3;
   }

   public final void e(bE var1) {
      this.h -= var1.h;
      this.i -= var1.i;
      this.j -= var1.j;
   }

   public final float f(bE var1) {
      return this.j * var1.j + this.i * var1.i + this.h * var1.h;
   }

   public final void g(bE var1) {
      this.b(this.i * var1.j - this.j * var1.i, this.j * var1.h - this.h * var1.j, this.h * var1.i - this.i * var1.h);
   }

   public float e() {
      return (float)Math.sqrt((double)(this.j * this.j + this.i * this.i + this.h * this.h));
   }

   public final void a(float var1) {
      this.h *= var1;
      this.i *= var1;
      this.j *= var1;
   }

   public final void a(fY var1) {
      fY var2 = bo.a(this.h, this.i, this.j, 0.0F);
      fY var3;
      synchronized(fY.c) {
         if (fY.a == 0) {
            var3 = new fY(var1);
         } else {
            fY.c[--fY.a].b(var1);
            var3 = fY.c[fY.a];
         }
      }

      var3.d();
      fY var4 = fY.b(var3, var2);
      var4.d(var1);
      this.b(var4.d, var4.e, var4.g);
      var2.b();
      var3.b();
      var4.b();
   }

   public String toString() {
      return this.h + ", " + this.i + ", " + this.j;
   }

   public static bE d(float var0, float var1, float var2) {
      return a(var0, var1, var2);
   }

   public void e(float var1, float var2, float var3) {
      this.b(var1, var2, var3);
   }

   public void f() {
      this.a();
   }

   public void g() {
      this.d();
   }

   public void b(fY var1) {
      this.a(var1);
   }

   static {
      new bE(1.0F, 1.0F, 1.0F);
      new bE(1.0F, 0.0F, 0.0F);
      b = new bE(0.0F, 1.0F, 0.0F);
      new bE(0.0F, 0.0F, 1.0F);
      new bE(1.0F, 0.0F, 0.0F);
      new bE(-1.0F, 0.0F, 0.0F);
      new bE(0.0F, 0.0F, 1.0F);
      c = new bE(0.0F, 0.0F, -1.0F);
      new bE(0.0F, 1.0F, 0.0F);
      d = new bE(0.0F, -1.0F, 0.0F);
      e = 0;
      f = 16;
      g = new bE[f];
   }
}
