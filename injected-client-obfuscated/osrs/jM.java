package osrs;

public class jM {
   public static int a(gq var0, int var1, gw var2) {
      int var3 = 0;
      if (!var0.b()) {
         var0.b(0);
         return var3;
      } else {
         bk var4 = var0.c();
         if (var4.q == -1) {
            var3 |= 8;
         }

         if (var4.h()) {
            var0.b(0);
         } else {
            var0.b(var0.j() + var1);
         }

         int var5;
         if (!var4.c()) {
            var5 = var3 | b(var0, var1, var2);
         } else {
            var5 = var3 | c(var0, var1, var2);
         }

         return var5;
      }
   }

   public static int b(gq var0, int var1, gw var2) {
      int var3 = 0;
      bk var4 = var0.c();
      if (var4.q > 0 && var4.p > 0) {
         var1 %= var4.p;
      }

      int var5 = var0.e();
      int var6 = var0.f();
      int var7 = var0.g();
      if (var5 >= var4.m.length) {
         var5 = 0;
         var6 = 0;
      }

      int var8 = var1 + var6;

      while(true) {
         do {
            if (var8 <= var4.o[var5]) {
               var0.a(var5, var8, var7);
               return var3;
            }

            var8 -= var4.o[var5];
            ++var5;
            var3 |= 4;
            if ((var3 & 2) == 0 && var2 != null) {
               var2.a(var4, var5);
            }
         } while(var5 < var4.m.length);

         ++var7;
         var3 |= 1;
         var5 -= var4.q;
         if (var7 >= var4.y) {
            var3 |= 2;
         }

         if (var5 < 0 || var5 >= var4.m.length) {
            var3 |= 2;
            var5 = 0;
         }

         if ((var3 & 2) == 0 && var2 != null) {
            var2.a(var4, var5);
         }
      }
   }

   public static int c(gq var0, int var1, gw var2) {
      int var3 = 0;
      bk var4 = var0.c();
      if (var4.q > 0 && var1 > 0) {
         var1 -= (var1 - 1) / var4.q * var4.q;
      }

      int var5 = var0.e();
      int var6 = var0.g();

      while(true) {
         int var7;
         do {
            if (var1 <= 0) {
               var0.a(var5, var6);
               return var3;
            }

            --var1;
            ++var5;
            var3 |= 4;
            if ((var3 & 2) == 0 && var2 != null) {
               var2.a(var4, var5);
            }

            var7 = var4.d();
         } while(var5 < var7);

         ++var6;
         var3 |= 1;
         var5 -= var4.q;
         if (var6 >= var4.y) {
            var3 |= 2;
         }

         if (var5 < 0 || var5 >= var7) {
            var3 |= 2;
            var5 = 0;
         }

         if ((var3 & 2) == 0 && var2 != null) {
            var2.a(var4, var5);
         }
      }
   }
}
