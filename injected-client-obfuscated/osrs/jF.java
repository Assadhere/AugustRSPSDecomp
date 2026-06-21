package osrs;

public class jF {
   public static void a(ag var0, int var1, int var2) {
      if (var0.v == 0) {
         var0.j = var0.L;
         var0.d(-1);
      } else if (var0.v == 1) {
         var0.j = (var1 - var0.ah) / 2 + var0.L;
         var0.d(-1);
      } else if (var0.v == 2) {
         var0.j = var1 - var0.ah - var0.L;
         var0.d(-1);
      } else if (var0.v == 3) {
         var0.j = var0.L * var1 >> 14;
         var0.d(-1);
      } else if (var0.v == 4) {
         var0.j = (var0.L * var1 >> 14) + (var1 - var0.ah) / 2;
         var0.d(-1);
      } else {
         var0.j = var1 - var0.ah - (var0.L * var1 >> 14);
         var0.d(-1);
      }

      if (var0.aj == 0) {
         var0.b = var0.n;
         var0.c(-1);
      } else if (var0.aj == 1) {
         var0.b = (var2 - var0.m) / 2 + var0.n;
         var0.c(-1);
      } else if (var0.aj == 2) {
         var0.b = var2 - var0.m - var0.n;
         var0.c(-1);
      } else if (var0.aj == 3) {
         var0.b = var0.n * var2 >> 14;
         var0.c(-1);
      } else if (var0.aj == 4) {
         var0.b = (var0.n * var2 >> 14) + (var2 - var0.m) / 2;
         var0.c(-1);
      } else {
         var0.b = var2 - var0.m - (var0.n * var2 >> 14);
         var0.c(-1);
      }

   }

   public static void a(ag[] var0, ag var1, boolean var2, kd var3, bg var4) {
      int var5 = var1.X != 0 ? var1.X : var1.ah;
      int var6 = var1.w != 0 ? var1.w : var1.m;
      a(var0, var1.ad, var1.c, var1.T, var1.as, var5, var6, var2, var3, var4);
      if (var1.bN != null) {
         a(var1.bN, 0, var1.bN.length - 1, var1.T, -1, var5, var6, var2, var3, var4);
      }

      if (var1.as == -1) {
         bt var7 = (bt)var3.x.a((long)var1.T);
         if (var7 != null) {
            a(var7.c, var5, var6, var2, var3, var4);
         }
      }

   }

   public static final void a(int var0, int var1, int var2, boolean var3, kd var4, bg var5) {
      if (var4.b(var0)) {
         a(var4.m[var0], 0, var4.m[var0].length - 1, -1, -1, var1, var2, var3, var4, var5);
      }

   }

   public static void a(ag[] var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, kd var8, bg var9) {
      for(int var10 = var1; var10 <= var2; ++var10) {
         ag var11 = var0[var10];
         if (var11 != null && var11.bL == var3 && var11.bS == var4) {
            a(var11, var5, var6, var7, var8, var9);
            a(var11, var5, var6);
            if (var11.P > var11.X - var11.ah) {
               var11.P = var11.X - var11.ah;
            }

            if (var11.P < 0) {
               var11.P = 0;
            }

            if (var11.ap > var11.w - var11.m) {
               var11.ap = var11.w - var11.m;
            }

            if (var11.ap < 0) {
               var11.ap = 0;
            }

            if (var11.B == 0) {
               a(var0, var11, var7, var8, var9);
            }
         }
      }

   }

   public static void a(ag var0, int var1, int var2, boolean var3, kd var4, bg var5) {
      int var6 = var0.ah;
      int var7 = var0.m;
      if (var0.ak == 0) {
         var0.ah = var0.bR;
      } else if (var0.ak == 1) {
         var0.ah = var1 - var0.bR;
      } else if (var0.ak == 2) {
         var0.ah = var0.bR * var1 >> 14;
      }

      if (var0.A == 0) {
         var0.m = var0.O;
      } else if (var0.A == 1) {
         var0.m = var2 - var0.O;
      } else if (var0.A == 2) {
         var0.m = var0.O * var2 >> 14;
      }

      if (var0.ak == 4) {
         var0.ah = var0.U * var0.m / var0.ao;
      }

      if (var0.A == 4) {
         var0.m = var0.ah * var0.ao / var0.U;
      }

      if (var0.l == 1337) {
         var4.q = var0;
      }

      if (var0.B == 12) {
         var0.l().a(var0.ah, var0.m);
      }

      if (var3 && var0.ba != null && (var0.ah != var6 || var0.m != var7)) {
         ac var8 = ac.a(var0).a(var0.ba).a();
         var5.a(var8);
      }

   }

   public static void a(ag var0, int var1, int var2, kd var3, bg var4) {
      ag var5 = var0.bL == -1 ? null : var3.a(var0.bL, var0.bS);
      int var6;
      int var7;
      if (var5 == null) {
         var6 = var1;
         var7 = var2;
      } else {
         var6 = var5.ah;
         var7 = var5.m;
      }

      a(var0, var6, var7, false, var3, var4);
      a(var0, var6, var7);
   }
}
