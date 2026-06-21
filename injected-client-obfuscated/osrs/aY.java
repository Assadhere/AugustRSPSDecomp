package osrs;

public class aY {
   public static hX[] a(au var0, int var1, int var2) {
      if (!c(var0, var1, var2)) {
         return null;
      } else {
         hX[] var3 = new hX[bo.ew];

         for(int var4 = 0; var4 < bo.ew; ++var4) {
            hX var5 = var3[var4] = new hX();
            var5.j = bo.ex;
            var5.n = bo.cx;
            var5.m = bo.eA[var4];
            var5.l = bo.p[var4];
            var5.i = bo.ea[var4];
            var5.k = bo.ez[var4];
            var5.p = bo.dW;
            var5.o = bo.bh[var4];
         }

         bo.eA = null;
         bo.p = null;
         bo.ea = null;
         bo.ez = null;
         bo.dW = null;
         bo.bh = (byte[][])null;
         return var3;
      }
   }

   public static aV[] b(au var0, int var1, int var2) {
      return !c(var0, var1, var2) ? null : b();
   }

   public static gg a(au var0, au var1, int var2, int var3) {
      if (!c(var0, var2, var3)) {
         return null;
      } else {
         byte[] var4 = var1.b(var2, var3);
         gg var5;
         if (var4 == null) {
            var5 = null;
         } else {
            gg var6 = new gg(var4, bo.eA, bo.p, bo.ea, bo.ez, bo.dW, bo.bh);
            bo.eA = null;
            bo.p = null;
            bo.ea = null;
            bo.ez = null;
            bo.dW = null;
            bo.bh = (byte[][])null;
            var5 = var6;
         }

         return var5;
      }
   }

   public static hX[] a(au var0, String var1, String var2) {
      if (!var0.a(var1, var2)) {
         return null;
      } else {
         int var3 = var0.a(var1);
         int var4 = var0.a(var3, var2);
         return a(var0, var3, var4);
      }
   }

   public static hX b(au var0, String var1, String var2) {
      if (!var0.a(var1, var2)) {
         return null;
      } else {
         int var3 = var0.a(var1);
         int var4 = var0.a(var3, var2);
         hX var5;
         if (!c(var0, var3, var4)) {
            var5 = null;
         } else {
            var5 = a();
         }

         return var5;
      }
   }

   public static aV[] c(au var0, String var1, String var2) {
      if (!var0.a(var1, var2)) {
         return null;
      } else {
         int var3 = var0.a(var1);
         int var4 = var0.a(var3, var2);
         return b(var0, var3, var4);
      }
   }

   public static hX a(au var0, int var1) {
      return !b(var0, var1) ? null : a();
   }

   public static hX a() {
      hX var0 = new hX();
      var0.j = bo.ex;
      var0.n = bo.cx;
      var0.m = bo.eA[0];
      var0.l = bo.p[0];
      var0.i = bo.ea[0];
      var0.k = bo.ez[0];
      var0.p = bo.dW;
      var0.o = bo.bh[0];
      bo.eA = null;
      bo.p = null;
      bo.ea = null;
      bo.ez = null;
      bo.dW = null;
      bo.bh = (byte[][])null;
      return var0;
   }

   public static aV[] b() {
      aV[] var0 = new aV[bo.ew];

      for(int var1 = 0; var1 < bo.ew; ++var1) {
         aV var2 = var0[var1] = new aV();
         var2.i = bo.ex;
         var2.j = bo.cx;
         var2.o = bo.eA[var1];
         var2.n = bo.p[var1];
         var2.l = bo.ea[var1];
         var2.m = bo.ez[var1];
         int var3 = var2.m * var2.l;
         byte[] var4 = bo.bh[var1];
         var2.k = new int[var3];

         for(int var5 = 0; var5 < var3; ++var5) {
            int var6 = var4[var5] & 255;
            var2.k[var5] = bo.dW[var6];
         }
      }

      bo.eA = null;
      bo.p = null;
      bo.ea = null;
      bo.ez = null;
      bo.dW = null;
      bo.bh = (byte[][])null;
      return var0;
   }

   public static boolean c(au var0, int var1, int var2) {
      byte[] var3 = var0.b(var1, var2);
      if (var3 == null) {
         return false;
      } else {
         a(var3);
         return true;
      }
   }

   public static boolean b(au var0, int var1) {
      byte[] var2 = var0.d(var1);
      if (var2 == null) {
         return false;
      } else {
         a(var2);
         return true;
      }
   }

   public static void a(byte[] var0) {
      aR var1 = new aR(var0);
      var1.d = var0.length - 2;
      bo.ew = var1.d();
      bo.eA = new int[bo.ew];
      bo.p = new int[bo.ew];
      bo.ea = new int[bo.ew];
      bo.ez = new int[bo.ew];
      bo.bh = new byte[bo.ew][];
      var1.d = var0.length - 7 - bo.ew * 8;
      bo.ex = var1.d();
      bo.cx = var1.d();
      int var2 = (var1.b() & 255) + 1;

      int var3;
      for(var3 = 0; var3 < bo.ew; ++var3) {
         bo.eA[var3] = var1.d();
      }

      for(var3 = 0; var3 < bo.ew; ++var3) {
         bo.p[var3] = var1.d();
      }

      for(var3 = 0; var3 < bo.ew; ++var3) {
         bo.ea[var3] = var1.d();
      }

      for(var3 = 0; var3 < bo.ew; ++var3) {
         bo.ez[var3] = var1.d();
      }

      var1.d = var0.length - 7 - bo.ew * 8 - (var2 - 1) * 3;
      bo.dW = new int[var2];

      for(var3 = 1; var3 < var2; ++var3) {
         bo.dW[var3] = var1.f();
         if (bo.dW[var3] == 0) {
            bo.dW[var3] = 1;
         }
      }

      var1.d = 0;

      for(var3 = 0; var3 < bo.ew; ++var3) {
         int var4 = bo.ea[var3];
         int var5 = bo.ez[var3];
         int var6 = var4 * var5;
         byte[] var7 = new byte[var6];
         bo.bh[var3] = var7;
         int var8 = var1.b();
         boolean var9 = (var8 & 1) == 1;
         boolean var10 = (var8 & 2) == 2;
         int var11;
         if (!var9) {
            for(var11 = 0; var11 < var6; ++var11) {
               var7[var11] = var1.c();
            }
         } else {
            for(var11 = 0; var11 < var4; ++var11) {
               for(int var12 = 0; var12 < var5; ++var12) {
                  var7[var4 * var12 + var11] = var1.c();
               }
            }
         }

         if (var10) {
            var1.d += var6;
         }
      }

   }
}
