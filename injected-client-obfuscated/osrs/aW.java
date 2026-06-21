package osrs;

public class aW {
   public static int[] a = new int[65536];
   public static int[] b = new int[512];
   public static int[] c = new int[2048];
   public static int[] d = new int[2048];
   public static int[] e = new int[2048];
   public static float[] f = new float[2048];
   public static float[] g = new float[2048];
   public static dN h;
   public static final df i;
   public static final df j;
   public static df k;

   public static void a(boolean var0) {
      if (var0 && aU.g != null) {
         k = j;
      } else {
         k = i;
      }

   }

   public static void a(dM var0) {
      h.c = var0;
   }

   public static void a(double var0) {
      a(var0, 0, 512);
   }

   public static void a(double var0, int var2, int var3) {
      bo.aF = var0;
      int var4 = var2 * 128;

      for(int var5 = var2; var5 < var3; ++var5) {
         double var6 = (double)(var5 >> 3) / 64.0 + 0.0078125;
         double var8 = (double)(var5 & 7) / 8.0 + 0.0625;

         for(int var10 = 0; var10 < 128; ++var10) {
            double var11 = (double)var10 / 128.0;
            double var13 = var11;
            double var15 = var11;
            double var17 = var11;
            if (var8 != 0.0) {
               double var19;
               if (var11 < 0.5) {
                  var19 = (var8 + 1.0) * var11;
               } else {
                  var19 = var8 + var11 - var8 * var11;
               }

               double var21 = var11 * 2.0 - var19;
               double var23 = var6 + 0.3333333333333333;
               if (var23 > 1.0) {
                  --var23;
               }

               double var25 = var6 - 0.3333333333333333;
               if (var25 < 0.0) {
                  ++var25;
               }

               if (var23 * 6.0 < 1.0) {
                  var13 = (var19 - var21) * 6.0 * var23 + var21;
               } else if (var23 * 2.0 < 1.0) {
                  var13 = var19;
               } else if (var23 * 3.0 < 2.0) {
                  var13 = (var19 - var21) * (0.6666666666666666 - var23) * 6.0 + var21;
               } else {
                  var13 = var21;
               }

               if (var6 * 6.0 < 1.0) {
                  var15 = (var19 - var21) * 6.0 * var6 + var21;
               } else if (var6 * 2.0 < 1.0) {
                  var15 = var19;
               } else if (var6 * 3.0 < 2.0) {
                  var15 = (var19 - var21) * (0.6666666666666666 - var6) * 6.0 + var21;
               } else {
                  var15 = var21;
               }

               if (var25 * 6.0 < 1.0) {
                  var17 = (var19 - var21) * 6.0 * var25 + var21;
               } else if (var25 * 2.0 < 1.0) {
                  var17 = var19;
               } else if (var25 * 3.0 < 2.0) {
                  var17 = (var19 - var21) * (0.6666666666666666 - var25) * 6.0 + var21;
               } else {
                  var17 = var21;
               }
            }

            int var29 = (int)(var13 * 256.0);
            int var20 = (int)(var15 * 256.0);
            int var27 = (int)(var17 * 256.0);
            int var22 = (var29 << 16) + (var20 << 8) + var27;
            int var28 = a(var22, var0);
            if (var28 == 0) {
               var28 = 1;
            }

            a[var4++] = var28;
         }
      }

   }

   public static int a(int var0, double var1) {
      double var3 = (double)(var0 >> 16) / 256.0;
      double var5 = (double)(var0 >> 8 & 255) / 256.0;
      double var7 = (double)(var0 & 255) / 256.0;
      double var9 = Math.pow(var3, var1);
      double var11 = Math.pow(var5, var1);
      double var13 = Math.pow(var7, var1);
      int var15 = (int)(var9 * 256.0);
      int var16 = (int)(var11 * 256.0);
      int var17 = (int)(var13 * 256.0);
      return (var15 << 16) + (var16 << 8) + var17;
   }

   public static double a() {
      return bo.aF;
   }

   public static int b() {
      return h.d;
   }

   public static int c() {
      return h.e;
   }

   public static int d() {
      return h.h;
   }

   public static int e() {
      return h.i;
   }

   public static int f() {
      return h.k;
   }

   public static int g() {
      return h.j;
   }

   public static int h() {
      return h.b;
   }

   public static int i() {
      return h.f;
   }

   public static boolean j() {
      return k.g();
   }

   public static void a(int[] var0, int var1, int var2, float[] var3) {
      if (var3 == null && k == j) {
         k = i;
      }

      k.b(var0, var1, var2, var3);
   }

   public static void k() {
      a(aU.c, aU.a, aU.d, aU.b);
   }

   public static void a(int var0, int var1, int var2, int var3) {
      h.f = var2 - var0;
      h.g = var3 - var1;
      l();
      if (h.l.length < h.g) {
         h.l = new int[ks.c(h.g)];
      }

      int var4 = aU.f * var1 + var0;

      for(int var5 = 0; var5 < h.g; ++var5) {
         h.l[var5] = var4;
         var4 += aU.f;
      }

   }

   public static void l() {
      h.a();
   }

   public static void a(int var0, int var1) {
      int var2 = h.l[0];
      int var3 = var2 / aU.f;
      int var4 = var2 - aU.f * var3;
      h.a(var0, var4, var1, var3);
   }

   public static void a(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9, int var10, int var11) {
      k.b(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
   }

   public static void a(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      k.b(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public static void a(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21) {
      k.c(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21);
   }

   public static void b(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21) {
      k.b(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21);
   }

   public static void b(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9, int var10, int var11) {
      k.a(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
   }

   public static void b(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      k.a(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   static {
      int var0;
      for(var0 = 1; var0 < 512; ++var0) {
         b[var0] = '耀' / var0;
      }

      for(var0 = 1; var0 < 2048; ++var0) {
         c[var0] = 65536 / var0;
      }

      for(var0 = 0; var0 < 2048; ++var0) {
         double var1 = Math.sin((double)var0 * 0.0030679615);
         double var3 = Math.cos((double)var0 * 0.0030679615);
         d[var0] = (int)(var1 * 65536.0);
         e[var0] = (int)(var3 * 65536.0);
         f[var0] = (float)var1;
         g[var0] = (float)var3;
      }

      h = new dN();
      i = new dF(h);
      j = new dt(h);
      k = i;
   }
}
