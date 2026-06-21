package osrs;

public class jZ {
   public static int[] a;
   public static int[] b;
   public static int[] c;
   public static int[][] d;
   public static int[] e;
   public static String[] f;
   public static int[] g;
   public static gJ h;
   public static int i;
   public static int j;
   public static int k = 0;
   public static int l = 50;
   public static int[] m;
   public static int[] n;
   public static int[] o;

   public static void a() {
      h.c();
   }

   public static void a(aV[] var0) {
      bo.cc = var0;
   }

   public static boolean b() {
      return bo.cc != null;
   }

   public static boolean c() {
      return bo.dK != null;
   }

   public static boolean d() {
      return bo.dp != null;
   }

   public static void a(int var0, int var1, int var2, int var3, int var4) {
      int var5 = Client.cS;
      gg var6 = bo.ck;

      for(int var7 = 0; var7 < k; ++var7) {
         int var8 = m[var7];
         int var9 = n[var7];
         int var10 = a[var7];
         int var11 = o[var7];
         boolean var12 = true;

         while(var12) {
            var12 = false;

            for(int var13 = 0; var13 < var7; ++var13) {
               if (var9 + 2 > n[var13] - o[var13] && var9 - var11 < n[var13] + 2 && var8 - var10 < a[var13] + m[var13] && var8 + var10 > m[var13] - a[var13] && n[var13] - o[var13] < var9) {
                  var9 = n[var13] - o[var13];
                  var12 = true;
               }
            }
         }

         i = m[var7];
         j = n[var7] = var9;
         String var19 = f[var7];
         if (var5 == 0) {
            int var14 = 16776960;
            if (b[var7] < 6) {
               var14 = g[b[var7]];
            }

            if (b[var7] == 6) {
               var14 = var4 % 20 < 10 ? 16711680 : 16776960;
            }

            if (b[var7] == 7) {
               var14 = var4 % 20 < 10 ? 255 : '\uffff';
            }

            if (b[var7] == 8) {
               var14 = var4 % 20 < 10 ? '뀀' : 8454016;
            }

            int var15;
            if (b[var7] == 9) {
               var15 = 150 - e[var7];
               if (var15 < 50) {
                  var14 = var15 * 1280 + 16711680;
               } else if (var15 < 100) {
                  var14 = 16776960 - (var15 - 50) * 327680;
               } else if (var15 < 150) {
                  var14 = (var15 - 100) * 5 + '\uff00';
               }
            }

            if (b[var7] == 10) {
               var15 = 150 - e[var7];
               if (var15 < 50) {
                  var14 = var15 * 5 + 16711680;
               } else if (var15 < 100) {
                  var14 = 16711935 - (var15 - 50) * 327680;
               } else if (var15 < 150) {
                  var14 = (var15 - 100) * 327680 + 255 - (var15 - 100) * 5;
               }
            }

            if (b[var7] == 11) {
               var15 = 150 - e[var7];
               if (var15 < 50) {
                  var14 = 16777215 - var15 * 327685;
               } else if (var15 < 100) {
                  var14 = (var15 - 50) * 327685 + '\uff00';
               } else if (var15 < 150) {
                  var14 = 16777215 - (var15 - 100) * 327680;
               }
            }

            int var16;
            if (b[var7] == 12 && d[var7] == null) {
               var15 = var19.length();
               d[var7] = new int[var15];

               for(var16 = 0; var16 < var15; ++var16) {
                  int var17 = (int)((float)var16 / (float)var15 * 64.0F);
                  int var18 = var17 << 10 | 896 | 64;
                  d[var7][var16] = bo.ec[var18];
               }
            }

            if (c[var7] == 0) {
               var6.a(var19, i + var0, j + var1, var14, 0, d[var7]);
            }

            if (c[var7] == 1) {
               var6.a(var19, i + var0, j + var1, var14, 0, var4, d[var7]);
            }

            if (c[var7] == 2) {
               var6.b(var19, i + var0, j + var1, var14, 0, var4, d[var7]);
            }

            if (c[var7] == 3) {
               var6.a(var19, i + var0, j + var1, var14, 0, var4, 150 - e[var7], d[var7]);
            }

            if (c[var7] == 4) {
               var15 = (150 - e[var7]) * (var6.b(var19) + 100) / 150;
               aU.b(i + var0 - 50, var1, i + var0 + 50, var1 + var3);
               ((aX)var6).b(var19, i + var0 + 50 - var15, j + var1, var14, 0, d[var7]);
               aU.a(var0, var1, var0 + var2, var1 + var3);
            }

            if (c[var7] == 5) {
               var15 = 150 - e[var7];
               var16 = 0;
               if (var15 < 25) {
                  var16 = var15 - 25;
               } else if (var15 > 125) {
                  var16 = var15 - 125;
               }

               aU.b(var0, j + var1 - var6.m - 1, var0 + var2, j + var1 + 5);
               var6.a(var19, i + var0, j + var1 + var16, var14, 0, d[var7]);
               aU.a(var0, var1, var0 + var2, var1 + var3);
            }
         } else {
            var6.c(var19, i + var0, j + var1, 16776960, 0);
         }
      }

   }

   public static final void a(fC var0, io var1, int var2, int var3, int var4) {
      if (var0.b() == 2) {
         bG var5 = var1.a(var0.d(), var0.e());
         int var6 = (var0.d() - var5.B << 7) + var0.f();
         int var7 = (var0.e() - var5.z << 7) + var0.g();
         int var8 = var0.h() * 2;
         a(var5, var6, var7, var8, 0);
         if (i > -1 && var4 % 20 < 10) {
            bo.dK[0].b(i + var2 - 12, j + var3 - 28);
         }
      }

   }

   public static void a(bG var0, o var1, int var2) {
      a(var0, var1.t, var1.ai, var2, var1.p());
   }

   public static void a(bG var0, int var1, int var2, int var3, int var4) {
      int var5 = var1;
      int var6 = var2;
      int var7 = Client.a(var0, var1, var2, var0.y, var4) - var3;
      if (!var0.a()) {
         gZ var8 = (gZ)bo.aQ.t.a((long)var0.o);
         if (var8 != null) {
            bE var9 = Client.a(var0, var1, var2);
            var5 = (int)var9.h;
            var6 = (int)var9.j;
            var7 += Client.b(bo.aQ, var8.k(), var8.l(), bo.aQ.y);
            var9.a();
         }
      }

      a(var5, var6, var7);
   }

   public static void a(int var0, int var1, int var2) {
      short var3 = 128;
      short var4 = 128;
      int var5 = B.b(bo.aQ.p - 2);
      int var6 = B.b(bo.aQ.q - 2);
      if (var0 >= var3 && var5 >= var0 && var1 >= var4 && var6 >= var1) {
         int var7 = var0 - bo.bM;
         int var8 = var2 - bo.bD;
         int var9 = var1 - bo.dO;
         int var10 = aW.d[bo.bV];
         int var11 = aW.e[bo.bV];
         int var12 = aW.d[bo.cV];
         int var13 = aW.e[bo.cV];
         int var14 = var7 * var13 + var9 * var12 >> 16;
         int var15 = var9 * var13 - var7 * var12 >> 16;
         int var16 = var8 * var11 - var10 * var15 >> 16;
         int var17 = var8 * var10 + var11 * var15 >> 16;
         if (var17 >= 50) {
            i = Client.aD * var14 / var17 + Client.aB / 2;
            j = Client.aD * var16 / var17 + Client.aC / 2;
         } else {
            i = -1;
            j = -1;
         }
      } else {
         i = -1;
         j = -1;
      }

   }

   static {
      m = new int[l];
      n = new int[l];
      o = new int[l];
      a = new int[l];
      b = new int[l];
      c = new int[l];
      d = new int[l][];
      e = new int[l];
      f = new String[l];
      g = new int[]{16776960, 16711680, 65280, 65535, 16711935, 16777215};
      h = new gJ(8, gm.a);
      i = -1;
      j = -1;
   }
}
