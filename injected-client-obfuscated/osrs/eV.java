package osrs;

public class eV {
   public static final byte[] a = new byte[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   public aR b = new aR((byte[])null);
   public int c;
   public int d;
   public long e;
   public int[] f;
   public int[] g;
   public int[] h;
   public int[] i;

   public void a(byte[] var1) {
      this.b.c = var1;
      this.b.d = 10;
      int var2 = this.b.d();
      this.c = this.b.d();
      this.d = 500000;
      this.f = new int[var2];

      aR var3;
      int var4;
      int var5;
      for(var5 = 0; var5 < var2; var3.d += var4) {
         int var6 = this.b.h();
         var4 = this.b.h();
         if (var6 == 1297379947) {
            this.f[var5] = this.b.d;
            ++var5;
         }

         var3 = this.b;
      }

      this.e = 0L;
      this.i = new int[var2];

      for(var5 = 0; var5 < var2; ++var5) {
         this.i[var5] = this.f[var5];
      }

      this.h = new int[var2];
      this.g = new int[var2];
   }

   public void a() {
      this.b.c = null;
      this.f = null;
      this.i = null;
      this.h = null;
      this.g = null;
   }

   public boolean b() {
      return this.b.c != null;
   }

   public int c() {
      return this.i.length;
   }

   public void a(int var1) {
      this.b.d = this.i[var1];
   }

   public void b(int var1) {
      this.i[var1] = this.b.d;
   }

   public void d() {
      this.b.d = -1;
   }

   public void c(int var1) {
      int var2 = this.b.v();
      int[] var3 = this.h;
      var3[var1] += var2;
   }

   public int d(int var1) {
      int var2 = this.e(var1);
      return var2;
   }

   public int e(int var1) {
      byte var2 = this.b.c[this.b.d];
      int var3;
      if (var2 < 0) {
         var3 = var2 & 255;
         this.g[var1] = var3;
         ++this.b.d;
      } else {
         var3 = this.g[var1];
      }

      if (var3 != 240 && var3 != 247) {
         return this.a(var1, var3);
      } else {
         int var4 = this.b.v();
         if (var3 == 247 && var4 > 0) {
            int var5 = this.b.c[this.b.d] & 255;
            if (var5 >= 241 && var5 <= 243 || var5 == 246 || var5 == 248 || var5 >= 250 && var5 <= 252 || var5 == 254) {
               ++this.b.d;
               this.g[var1] = var5;
               return this.a(var1, var5);
            }
         }

         aR var6 = this.b;
         var6.d += var4;
         return 0;
      }
   }

   public int a(int var1, int var2) {
      int var3;
      int var4;
      if (var2 == 255) {
         var3 = this.b.b();
         var4 = this.b.v();
         aR var5;
         if (var3 == 47) {
            var5 = this.b;
            var5.d += var4;
            return 1;
         } else if (var3 == 81) {
            int var6 = this.b.f();
            var4 -= 3;
            int var7 = this.h[var1];
            this.e += (long)(this.d - var6) * (long)var7;
            this.d = var6;
            var5 = this.b;
            var5.d += var4;
            return 2;
         } else {
            var5 = this.b;
            var5.d += var4;
            return 3;
         }
      } else {
         var3 = a[var2 - 128];
         var4 = var2;
         if (var3 >= 1) {
            var4 = var2 | this.b.b() << 8;
         }

         if (var3 >= 2) {
            var4 |= this.b.b() << 16;
         }

         return var4;
      }
   }

   public long f(int var1) {
      return (long)this.d * (long)var1 + this.e;
   }

   public int e() {
      int var1 = this.i.length;
      int var2 = -1;
      int var3 = Integer.MAX_VALUE;

      for(int var4 = 0; var4 < var1; ++var4) {
         if (this.i[var4] >= 0 && this.h[var4] < var3) {
            var2 = var4;
            var3 = this.h[var4];
         }
      }

      return var2;
   }

   public boolean f() {
      int var1 = this.i.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         if (this.i[var2] >= 0) {
            return false;
         }
      }

      return true;
   }

   public void a(long var1) {
      this.e = var1;
      int var3 = this.i.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         this.h[var4] = 0;
         this.g[var4] = 0;
         this.b.d = this.f[var4];
         this.c(var4);
         this.i[var4] = this.b.d;
      }

   }
}
