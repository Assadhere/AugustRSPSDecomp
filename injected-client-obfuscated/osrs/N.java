package osrs;

public class N extends aA {
   public int a;
   public int b;
   public int c;
   public static eI d = new eI(64);
   public int e = 0;
   public int f = -1;
   public boolean g = true;
   public int h = -1;
   public int i;
   public int j;
   public int k;
   public static au l;

   public static void a(au var0) {
      l = var0;
   }

   public static N a(int var0) {
      N var1 = (N)d.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = l.b(4, (int)var0);
         N var3 = new N();
         if (var2 != null) {
            var3.a(new aR(var2), var0);
         }

         var3.a();
         d.a(var3, (long)var0);
         return var3;
      }
   }

   public void a() {
      if (this.h != -1) {
         this.b(this.h);
         this.a = this.i;
         this.b = this.j;
         this.c = this.k;
      }

      this.b(this.e);
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
         this.e = var1.f();
      } else if (var2 == 2) {
         this.f = var1.b();
      } else if (var2 == 5) {
         this.g = false;
      } else if (var2 == 7) {
         this.h = var1.f();
      } else if (var2 != 8 && var2 == 9) {
         var1.b();
      }

   }

   public void b(int var1) {
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
      this.i = (int)(var18 * 256.0);
      this.j = (int)(var14 * 256.0);
      this.k = (int)(var16 * 256.0);
      if (this.j < 0) {
         this.j = 0;
      } else if (this.j > 255) {
         this.j = 255;
      }

      if (this.k < 0) {
         this.k = 0;
      } else if (this.k > 255) {
         this.k = 255;
      }

   }

   public int b() {
      return this.c;
   }

   public int c() {
      return this.h;
   }

   public int d() {
      return this.e;
   }

   public void e() {
      this.a();
   }

   public int f() {
      return this.b;
   }

   public int g() {
      return this.k;
   }

   public int h() {
      return this.i;
   }

   public int i() {
      return this.f;
   }

   public int j() {
      return this.j;
   }

   public int k() {
      return this.a;
   }

   public void b(aR var1, int var2) {
      this.a(var1, var2);
   }
}
