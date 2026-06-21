package osrs;

public class dB {
   public static final bE a = new bE();
   public static boolean b = false;
   public static int c = 0;
   public static int d = 0;
   public static boolean e = false;
   public static int f = 0;
   public static long[] g = new long[1000];
   public static int[] h = new int[1000];

   public static boolean a(long var0) {
      boolean var2 = var0 != 0L;
      if (var2) {
         boolean var3 = (int)(var0 >>> 19 & 1L) == 1;
         var2 = !var3;
      }

      return var2;
   }

   public static int b(long var0) {
      return (int)(var0 >>> 7 & 127L);
   }

   public static int c(long var0) {
      return (int)(var0 >>> 20 & 4294967295L);
   }

   public static long a(int var0, int var1, int var2, int var3, boolean var4, int var5, int var6) {
      long var7 = (long)((var1 & 127) << 0 | (var2 & 127) << 7 | (var0 & 3) << 14 | (var3 & 7) << 16) | ((long)var5 & 4294967295L) << 20 | ((long)var6 & 4095L) << 52;
      if (var4) {
         var7 |= 524288L;
      }

      return var7;
   }

   public static final void a(long var0, int var2) {
      if (h[f] != Integer.MAX_VALUE && g[f] != var0) {
         ++f;
         h[f] = Integer.MAX_VALUE;
      }

      g[f] = var0;
      h[f] = Math.min(h[f], var2);
   }

   public static final void a(float var0, float var1, float var2, float var3, int var4, int var5, int var6) {
      if (!e) {
         float var7 = 50.0F;
         float var8 = (float)df.e();
         float var9 = (float)(c - var4) * var7 / (float)var6;
         float var10 = (float)(d - var5) * var7 / (float)var6;
         float var11 = (float)(c - var4) * var8 / (float)var6;
         float var12 = (float)(d - var5) * var8 / (float)var6;
         float var13 = jl.b(var10, var7, var1, var0);
         float var14 = jl.c(var10, var7, var1, var0);
         float var15 = jl.b(var12, var8, var1, var0);
         float var16 = jl.c(var12, var8, var1, var0);
         float var17 = var3 * var9 - var2 * var14;
         float var18 = jl.a(var9, var14, var3, var2);
         float var19 = var3 * var11 - var2 * var16;
         float var20 = jl.a(var11, var16, var3, var2);
         a((int)var17, (int)var13, (int)var18, (int)var19, (int)var15, (int)var20);
      }

   }

   public static void a(fX var0, int var1, int var2, int var3) {
      if (!e) {
         byte var4 = 50;
         int var5 = df.e();
         int var6 = (c - var1) * var4 / var3;
         int var7 = (d - var2) * var4 / var3;
         int var8 = (c - var1) * var5 / var3;
         int var9 = (d - var2) * var5 / var3;
         fX var10;
         synchronized(fX.j) {
            if (fX.h == 0) {
               var10 = new fX(var0);
            } else {
               fX.j[--fX.h].b(var0, 241335935);
               var10 = fX.j[fX.h];
            }
         }

         var10.g();
         float[] var11 = new float[3];
         var10.a((float)var6, (float)var7, (float)var4, var11);
         int var12 = (int)var11[0];
         int var13 = (int)var11[1];
         int var14 = (int)var11[2];
         var10.a((float)var8, (float)var9, (float)var5, var11);
         int var15 = (int)var11[0];
         int var16 = (int)var11[1];
         int var17 = (int)var11[2];
         var10.a(0.0F, 0.0F, 0.0F, var11);
         bo.w = (int)var11[0];
         bo.H = (int)var11[1];
         bo.Y = (int)var11[2];
         int var18 = var12 - bo.w;
         int var19 = var13 - bo.H;
         int var20 = var14 - bo.Y;
         int var21 = var15 - bo.w;
         int var22 = var16 - bo.H;
         int var23 = var17 - bo.Y;
         a(var18, var19, var20, var21, var22, var23);
         var10.b();
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4, int var5) {
      bo.cH = var0;
      bo.eC = var1;
      bo.dr = var2;
      bo.cY = var3;
      bo.ab = var4;
      bo.dR = var5;
      bo.ct = (var0 + var3) / 2;
      bo.Z = (var1 + var4) / 2;
      bo.j = (var2 + var5) / 2;
      bo.cA = (var3 - var0) / 2;
      bo.dz = (var4 - var1) / 2;
      bo.dh = (var5 - var2) / 2;
      bo.aV = Math.abs(bo.cA);
      bo.aM = Math.abs(bo.dz);
      bo.bf = Math.abs(bo.dh);
      a.b((float)(var3 - var0), (float)(var4 - var1), (float)(var5 - var2));
      a.d();
      e = true;
   }

   public static final boolean a(aH var0, int var1, int var2, int var3, int var4, float var5, float var6, float var7, float var8, int var9, int var10, int var11) {
      if (!bo.h()) {
         return false;
      } else {
         a(var5, var6, var7, var8, var9, var10, var11);
         return a(var0, var1, var2, var3, var4);
      }
   }

   public static final boolean a(aH var0, int var1, int var2, int var3, int var4, fX var5, int var6, int var7, int var8) {
      if (!bo.h()) {
         return false;
      } else {
         a(var5, var6, var7, var8);
         return a(var0, var1, var2 - bo.w, var3 - bo.H, var4 - bo.Y);
      }
   }

   public static boolean a(aH var0, int var1, int var2, int var3, int var4) {
      dx var5 = var0.c(var1);
      int var6 = var5.b + var2;
      int var7 = var5.c + var3;
      int var8 = var5.d + var4;
      int var9 = var5.e;
      int var10 = var5.f;
      int var11 = var5.g;
      int var12 = bo.ct - var6;
      int var13 = bo.Z - var7;
      int var14 = bo.j - var8;
      if (Math.abs(var12) > bo.aV + var9) {
         return false;
      } else if (Math.abs(var13) > bo.aM + var10) {
         return false;
      } else if (Math.abs(var14) > bo.bf + var11) {
         return false;
      } else if (Math.abs(bo.dz * var14 - bo.dh * var13) > bo.bf * var10 + bo.aM * var11) {
         return false;
      } else if (Math.abs(bo.dh * var12 - bo.cA * var14) > bo.bf * var9 + bo.aV * var11) {
         return false;
      } else {
         return Math.abs(bo.cA * var13 - bo.dz * var12) <= bo.aV * var10 + bo.aM * var9;
      }
   }

   public static int b(aH var0, int var1, int var2, int var3, int var4, float var5, float var6, float var7, float var8, int var9, int var10, int var11) {
      a(var5, var6, var7, var8, var9, var10, var11);
      return bo.a(var2, var3, var4, bo.cH, bo.eC, bo.dr, bo.cY, bo.ab, bo.dR, var0.c(var1));
   }

   public static int b(aH var0, int var1, int var2, int var3, int var4, fX var5, int var6, int var7, int var8) {
      a(var5, var6, var7, var8);
      return bo.a(var2 - bo.w, var3 - bo.H, var4 - bo.Y, bo.cH, bo.eC, bo.dr, bo.cY, bo.ab, bo.dR, var0.c(var1));
   }

   public static void a() {
      boolean var0;
      int var1;
      do {
         var0 = true;

         for(var1 = 0; var1 < f; ++var1) {
            if (h[var1] < h[var1 + 1]) {
               long var2 = g[var1];
               g[var1] = g[var1 + 1];
               g[var1 + 1] = var2;
               int var4 = h[var1];
               h[var1] = h[var1 + 1];
               h[var1 + 1] = var4;
               var0 = false;
            }
         }
      } while(!var0);

      for(var1 = 0; var1 < f; ++var1) {
         for(int var5 = var1 + 1; var5 <= f; ++var5) {
            if (g[var1] == g[var5]) {
               g[var1] = -1L;
               break;
            }
         }
      }

   }
}
