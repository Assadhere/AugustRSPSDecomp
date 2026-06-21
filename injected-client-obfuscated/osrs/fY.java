package osrs;

public final class fY {
   public static int a;
   public static final int b;
   public static final fY[] c;
   public float d;
   public float e;
   public float f;
   public float g;

   public fY() {
      this.c();
   }

   public fY(float var1, float var2, float var3, float var4) {
      this.a(var1, var2, var3, var4);
   }

   public fY(fY var1) {
      this.b(var1);
   }

   public static fY a() {
      synchronized(c) {
         if (a == 0) {
            return new fY();
         } else {
            c[--a].c();
            return c[a];
         }
      }
   }

   public static final float a(fY var0, fY var1) {
      return var0.c(var1);
   }

   public static final float a(fY var0) {
      return (float)Math.sqrt((double)a(var0, var0));
   }

   public static final fY b(fY var0, fY var1) {
      fY var2;
      synchronized(c) {
         if (a == 0) {
            var2 = new fY(var0);
         } else {
            c[--a].b(var0);
            var2 = c[a];
         }
      }

      var2.d(var1);
      return var2;
   }

   public void b() {
      synchronized(c) {
         if (a < b) {
            c[++a - 1] = this;
         }

      }
   }

   public void a(float var1, float var2, float var3, float var4) {
      this.d = var1;
      this.e = var2;
      this.g = var3;
      this.f = var4;
   }

   public void b(fY var1) {
      this.d = var1.d;
      this.e = var1.e;
      this.g = var1.g;
      this.f = var1.f;
   }

   public void a(bE var1, float var2) {
      this.b(var1.h, var1.i, var1.j, var2);
   }

   public void b(float var1, float var2, float var3, float var4) {
      float var5 = (float)Math.sin((double)(var4 * 0.5F));
      float var6 = (float)Math.cos((double)(var4 * 0.5F));
      this.d = var1 * var5;
      this.e = var2 * var5;
      this.g = var3 * var5;
      this.f = var6;
   }

   public void a(float var1, float var2, float var3) {
      this.b(0.0F, 1.0F, 0.0F, var1);
      fY var4 = a();
      var4.b(1.0F, 0.0F, 0.0F, var2);
      this.d(var4);
      var4.b(0.0F, 0.0F, 1.0F, var3);
      this.d(var4);
      var4.b();
   }

   public final void c() {
      this.g = 0.0F;
      this.e = 0.0F;
      this.d = 0.0F;
      this.f = 1.0F;
   }

   public final void d() {
      this.d = -this.d;
      this.e = -this.e;
      this.g = -this.g;
   }

   public final void e() {
      float var1 = 1.0F / a(this);
      this.d *= var1;
      this.e *= var1;
      this.g *= var1;
      this.f *= var1;
   }

   public final float c(fY var1) {
      return this.f * var1.f + this.g * var1.g + this.e * var1.e + this.d * var1.d;
   }

   public final void d(fY var1) {
      this.a(this.g * var1.e + this.f * var1.d + this.d * var1.f - this.e * var1.g, this.d * var1.g + this.f * var1.e + (this.e * var1.f - this.g * var1.d), this.f * var1.g + (this.g * var1.f + this.e * var1.d - this.d * var1.e), this.f * var1.f - this.d * var1.d - this.e * var1.e - this.g * var1.g);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof fY)) {
         return false;
      } else {
         fY var2 = (fY)var1;
         return this.d == var2.d && this.e == var2.e && this.g == var2.g && this.f == var2.f;
      }
   }

   public int hashCode() {
      float var1 = 1.0F;
      float var2 = var1 * 31.0F + this.d;
      float var3 = var2 * 31.0F + this.e;
      float var4 = var3 * 31.0F + this.g;
      float var5 = var4 * 31.0F + this.f;
      return (int)var5;
   }

   public String toString() {
      return this.d + "," + this.e + "," + this.g + "," + this.f;
   }

   public void b(float var1, float var2, float var3) {
      this.a(var1, var2, var3);
   }

   public void f() {
      this.b();
   }

   public void e(fY var1) {
      this.d(var1);
   }

   public static fY g() {
      return a();
   }

   public void c(float var1, float var2, float var3, float var4) {
      this.b(var1, var2, var3, var4);
   }

   static {
      new fY();
      a = 0;
      b = 16;
      c = new fY[b];
   }
}
