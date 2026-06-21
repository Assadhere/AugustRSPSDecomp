package osrs;

public class O extends aA {
   public static eI a = new eI(64);
   public int b = 0;
   public int c;
   public int d;
   public int e;
   public int f;
   public static au g;

   public static void a() {
      a.a();
   }

   public void b() {
      this.a(this.b);
   }

   public void a(aR var1, int var2) {
      while(true) {
         int var3 = var1.b();
         if (var3 == 0) {
            return;
         }

         this.a(var1, var3, var2);
      }
   }

   public void a(aR var1, int var2, int var3) {
      if (var2 == 1) {
         this.b = var1.f();
      }

   }

   public void a(int var1) {
      double var2 = (double)(var1 >> 16 & 255) / 256.0;
      double var4 = (double)(var1 >> 8 & 255) / 256.0;
      double var6 = (double)(var1 & 255) / 256.0;
      double var8 = var2;
      if (var4 < var2) {
         var8 = var4;
      }

      if (var6 < var8) {
         var8 = var6;
      }

      double var10 = var2;
      if (var4 > var2) {
         var10 = var4;
      }

      if (var6 > var10) {
         var10 = var6;
      }

      double var12 = 0.0;
      double var14 = 0.0;
      double var16 = (var8 + var10) / 2.0;
      if (var8 != var10) {
         if (var16 < 0.5) {
            var14 = (var10 - var8) / (var8 + var10);
         }

         if (var16 >= 0.5) {
            var14 = (var10 - var8) / (2.0 - var10 - var8);
         }

         if (var2 == var10) {
            var12 = (var4 - var6) / (var10 - var8);
         } else if (var4 == var10) {
            var12 = (var6 - var2) / (var10 - var8) + 2.0;
         } else if (var6 == var10) {
            var12 = (var2 - var4) / (var10 - var8) + 4.0;
         }
      }

      double var18 = var12 / 6.0;
      this.c = (int)(var14 * 256.0);
      this.e = (int)(var16 * 256.0);
      if (this.c < 0) {
         this.c = 0;
      } else if (this.c > 255) {
         this.c = 255;
      }

      if (this.e < 0) {
         this.e = 0;
      } else if (this.e > 255) {
         this.e = 255;
      }

      if (var16 > 0.5) {
         this.d = (int)((1.0 - var16) * var14 * 512.0);
      } else {
         this.d = (int)(var14 * var16 * 512.0);
      }

      if (this.d < 1) {
         this.d = 1;
      }

      this.f = (int)((double)this.d * var18);
   }

   public int c() {
      return this.e;
   }

   public int d() {
      return this.c;
   }

   public int e() {
      return this.d;
   }

   public void b(aR var1, int var2) {
      this.a(var1, var2);
   }

   public void f() {
      this.b();
   }
}
