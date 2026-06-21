package osrs;

public class eM {
   public static int a = 0;
   public static int[] b = new int[1000];
   public static int[] c = new int[1000];
   public static aV[] d = new aV[1000];
   public static int e = 0;
   public static int f = 0;
   public static int g = 0;
   public static int h = 1;

   public static final void a(int var0) {
      int[] var1 = bo.K.k;
      int var2 = var1.length;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         var1[var3] = 1;
      }

      int var4;
      int var5;
      for(var3 = 1; var3 < 103; ++var3) {
         var4 = (103 - var3) * 2048 + 24628;

         for(var5 = 1; var5 < 103; ++var5) {
            if ((bo.aQ.v[var0][var5][var3] & 24) == 0) {
               bo.aQ.x.a((int[])var1, var4, 512, var0, var5, var3);
            }

            if (var0 < 3 && (bo.aQ.v[var0 + 1][var5][var3] & 8) != 0) {
               bo.aQ.x.a((int[])var1, var4, 512, var0 + 1, var5, var3);
            }

            var4 += 4;
         }
      }

      var3 = (238 + (int)(Math.random() * 20.0) - 10 << 16) + (238 + (int)(Math.random() * 20.0) - 10 << 8) + (238 + (int)(Math.random() * 20.0) - 10);
      var4 = 238 + (int)(Math.random() * 20.0) - 10 << 16;
      bo.K.h();

      int var6;
      for(var5 = 1; var5 < 103; ++var5) {
         for(var6 = 1; var6 < 103; ++var6) {
            if ((bo.aQ.v[var0][var6][var5] & 24) == 0) {
               a(var0, var6, var5, var3, var4);
            }

            if (var0 < 3 && (bo.aQ.v[var0 + 1][var6][var5] & 8) != 0) {
               a(var0 + 1, var6, var5, var3, var4);
            }
         }
      }

      a = 0;

      for(var5 = 0; var5 < 104; ++var5) {
         for(var6 = 0; var6 < 104; ++var6) {
            long var7 = bo.aQ.x.i(bo.aQ.y, var5, var6);
            if (var7 != 0L) {
               int var9 = dB.c(var7);
               int var10 = aC.a(var9).G;
               if (var10 >= 0 && aE.a(var10).n) {
                  d[a] = aE.a(var10).a(false);
                  b[a] = var5;
                  c[a] = var6;
                  ++a;
               }
            }
         }
      }

      T.fa.a();
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4) {
      dh var5 = bo.aQ.x;
      long var6 = var5.r(var0, var1, var2);
      int var10;
      int var11;
      int var13;
      int var17;
      if (var6 != 0L) {
         int var8 = var5.a(var0, var1, var2, var6);
         int var9 = var8 >> 6 & 3;
         var10 = var8 & 31;
         var11 = var3;
         if (dB.a(var6)) {
            var11 = var4;
         }

         int[] var12 = bo.K.k;
         var13 = (103 - var2) * 2048 + var1 * 4 + 24624;
         int var14 = dB.c(var6);
         aC var15 = aC.a(var14);
         if (var15.H != -1) {
            hX var16 = bo.bq[var15.H];
            if (var16 != null) {
               var17 = (var15.t * 4 - var16.i) / 2;
               int var18 = (var15.u * 4 - var16.k) / 2;
               var16.a(var1 * 4 + 48 + var17, (104 - var2 - var15.u) * 4 + 48 + var18);
            }
         } else {
            if (var10 == 0 || var10 == 2) {
               if (var9 == 0) {
                  var12[var13] = var11;
                  var12[var13 + 512] = var11;
                  var12[var13 + 1024] = var11;
                  var12[var13 + 1536] = var11;
               } else if (var9 == 1) {
                  var12[var13] = var11;
                  var12[var13 + 1] = var11;
                  var12[var13 + 2] = var11;
                  var12[var13 + 3] = var11;
               } else if (var9 == 2) {
                  var12[var13 + 3] = var11;
                  var12[var13 + 3 + 512] = var11;
                  var12[var13 + 3 + 1024] = var11;
                  var12[var13 + 3 + 1536] = var11;
               } else if (var9 == 3) {
                  var12[var13 + 1536] = var11;
                  var12[var13 + 1536 + 1] = var11;
                  var12[var13 + 1536 + 2] = var11;
                  var12[var13 + 1536 + 3] = var11;
               }
            }

            if (var10 == 3) {
               if (var9 == 0) {
                  var12[var13] = var11;
               } else if (var9 == 1) {
                  var12[var13 + 3] = var11;
               } else if (var9 == 2) {
                  var12[var13 + 3 + 1536] = var11;
               } else if (var9 == 3) {
                  var12[var13 + 1536] = var11;
               }
            }

            if (var10 == 2) {
               if (var9 == 3) {
                  var12[var13] = var11;
                  var12[var13 + 512] = var11;
                  var12[var13 + 1024] = var11;
                  var12[var13 + 1536] = var11;
               } else if (var9 == 0) {
                  var12[var13] = var11;
                  var12[var13 + 1] = var11;
                  var12[var13 + 2] = var11;
                  var12[var13 + 3] = var11;
               } else if (var9 == 1) {
                  var12[var13 + 3] = var11;
                  var12[var13 + 3 + 512] = var11;
                  var12[var13 + 3 + 1024] = var11;
                  var12[var13 + 3 + 1536] = var11;
               } else if (var9 == 2) {
                  var12[var13 + 1536] = var11;
                  var12[var13 + 1536 + 1] = var11;
                  var12[var13 + 1536 + 2] = var11;
                  var12[var13 + 1536 + 3] = var11;
               }
            }
         }
      }

      long var19 = var5.c(var0, var1, var2);
      int var21;
      int var26;
      int var27;
      if (var19 != 0L) {
         var10 = var5.a(var0, var1, var2, var19);
         var11 = var10 >> 6 & 3;
         var21 = var10 & 31;
         var13 = dB.c(var19);
         aC var23 = aC.a(var13);
         if (var23.H != -1) {
            hX var25 = bo.bq[var23.H];
            if (var25 != null) {
               var27 = (var23.t * 4 - var25.i) / 2;
               var17 = (var23.u * 4 - var25.k) / 2;
               var25.a(var1 * 4 + 48 + var27, (104 - var2 - var23.u) * 4 + 48 + var17);
            }
         } else if (var21 == 9) {
            var26 = 15658734;
            if (dB.a(var19)) {
               var26 = 15597568;
            }

            int[] var28 = bo.K.k;
            var17 = (103 - var2) * 2048 + var1 * 4 + 24624;
            if (var11 != 0 && var11 != 2) {
               var28[var17] = var26;
               var28[var17 + 512 + 1] = var26;
               var28[var17 + 1024 + 2] = var26;
               var28[var17 + 1536 + 3] = var26;
            } else {
               var28[var17 + 1536] = var26;
               var28[var17 + 1024 + 1] = var26;
               var28[var17 + 512 + 2] = var26;
               var28[var17 + 3] = var26;
            }
         }
      }

      long var20 = var5.i(var0, var1, var2);
      if (var20 != 0L) {
         var21 = dB.c(var20);
         aC var22 = aC.a(var21);
         if (var22.H != -1) {
            hX var24 = bo.bq[var22.H];
            if (var24 != null) {
               var26 = (var22.t * 4 - var24.i) / 2;
               var27 = (var22.u * 4 - var24.k) / 2;
               var24.a(var1 * 4 + 48 + var26, (104 - var2 - var22.u) * 4 + 48 + var27);
            }
         }
      }

   }
}
