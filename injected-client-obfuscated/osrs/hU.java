package osrs;

public final class hU {
   public static ij a = new ij();

   public static int a(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      synchronized(a) {
         a.K = var2;
         a.G = var4;
         a.L = var0;
         a.H = 0;
         a.I = var1;
         a.e = 0;
         a.d = 0;
         a.J = 0;
         a.a = 0;
         b(a);
         int var6 = var1 - a.I;
         a.K = null;
         a.L = null;
         return var6;
      }
   }

   public static void a(ij var0) {
      byte var1 = var0.b;
      int var2 = var0.c;
      int var3 = var0.k;
      int var4 = var0.i;
      int[] var5 = bo.et;
      int var6 = var0.h;
      byte[] var7 = var0.L;
      int var8 = var0.H;
      int var9 = var0.I;
      int var10 = var0.x + 1;

      int var11;
      label62:
      while(true) {
         if (var2 > 0) {
            while(true) {
               if (var9 == 0) {
                  break label62;
               }

               if (var2 == 1) {
                  if (var9 == 0) {
                     var2 = 1;
                     break label62;
                  }

                  var7[var8] = var1;
                  ++var8;
                  --var9;
                  break;
               }

               var7[var8] = var1;
               --var2;
               ++var8;
               --var9;
            }
         }

         while(var3 != var10) {
            var1 = (byte)var4;
            var11 = var5[var6];
            byte var12 = (byte)var11;
            var6 = var11 >> 8;
            ++var3;
            if (var4 != var12) {
               var4 = var12;
               if (var9 == 0) {
                  var2 = 1;
                  break label62;
               }

               var7[var8] = var1;
               ++var8;
               --var9;
            } else {
               if (var3 != var10) {
                  var2 = 2;
                  int var13 = var5[var6];
                  byte var14 = (byte)var13;
                  var6 = var13 >> 8;
                  ++var3;
                  if (var3 != var10) {
                     if (var4 != var14) {
                        var4 = var14;
                     } else {
                        var2 = 3;
                        int var15 = var5[var6];
                        byte var16 = (byte)var15;
                        var6 = var15 >> 8;
                        ++var3;
                        if (var3 != var10) {
                           if (var4 != var16) {
                              var4 = var16;
                           } else {
                              int var17 = var5[var6];
                              byte var18 = (byte)var17;
                              int var19 = var17 >> 8;
                              ++var3;
                              var2 = (var18 & 255) + 4;
                              int var20 = var5[var19];
                              var4 = (byte)var20;
                              var6 = var20 >> 8;
                              ++var3;
                           }
                        }
                     }
                  }
                  continue label62;
               }

               if (var9 == 0) {
                  var2 = 1;
                  break label62;
               }

               var7[var8] = var1;
               ++var8;
               --var9;
            }
         }

         var2 = 0;
         break;
      }

      var11 = var0.a;
      var0.a += var9 - var9;
      if (var0.a < var11) {
      }

      var0.b = var1;
      var0.c = var2;
      var0.k = var3;
      var0.i = var4;
      bo.et = var5;
      var0.h = var6;
      var0.L = var7;
      var0.H = var8;
      var0.I = var9;
   }

   public static void b(ij var0) {
      boolean var1 = false;
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;
      boolean var14 = false;
      boolean var15 = false;
      boolean var16 = false;
      boolean var17 = false;
      boolean var18 = false;
      int var19 = 0;
      int[] var20 = null;
      int[] var21 = null;
      int[] var22 = null;
      var0.f = 1;
      if (bo.et == null) {
         bo.et = new int[var0.f * 100000];
      }

      boolean var23 = true;

      while(true) {
         while(var23) {
            byte var24 = c(var0);
            if (var24 == 23) {
               return;
            }

            byte var25 = c(var0);
            byte var26 = c(var0);
            byte var27 = c(var0);
            byte var28 = c(var0);
            byte var29 = c(var0);
            byte var30 = c(var0);
            byte var31 = c(var0);
            byte var32 = c(var0);
            byte var33 = c(var0);
            byte var34 = d(var0);
            if (var34 != 0) {
            }

            var0.g = 0;
            byte var35 = c(var0);
            var0.g = var0.g << 8 | var35 & 255;
            byte var36 = c(var0);
            var0.g = var0.g << 8 | var36 & 255;
            byte var37 = c(var0);
            var0.g = var0.g << 8 | var37 & 255;

            int var38;
            int var39;
            for(var38 = 0; var38 < 16; ++var38) {
               var39 = d(var0);
               if (var39 == 1) {
                  var0.o[var38] = true;
               } else {
                  var0.o[var38] = false;
               }
            }

            for(var38 = 0; var38 < 256; ++var38) {
               var0.n[var38] = false;
            }

            int var40;
            for(var38 = 0; var38 < 16; ++var38) {
               if (var0.o[var38]) {
                  for(var39 = 0; var39 < 16; ++var39) {
                     var40 = d(var0);
                     if (var40 == 1) {
                        var0.n[var38 * 16 + var39] = true;
                     }
                  }
               }
            }

            e(var0);
            var38 = var0.m + 2;
            var39 = a(3, var0);
            var40 = a(15, var0);

            int var42;
            int var43;
            for(int var41 = 0; var41 < var40; ++var41) {
               var42 = 0;

               while(true) {
                  var43 = d(var0);
                  if (var43 == 0) {
                     var0.F[var41] = (byte)var42;
                     break;
                  }

                  ++var42;
               }
            }

            byte[] var61 = new byte[6];

            for(byte var62 = 0; var62 < var39; var61[var62] = var62++) {
            }

            int var44;
            for(var42 = 0; var42 < var40; ++var42) {
               var43 = var0.F[var42];

               for(var44 = var61[var43]; var43 > 0; var43 = (byte)(var43 - 1)) {
                  var61[var43] = var61[var43 - 1];
               }

               var61[0] = (byte)var44;
               var0.E[var42] = (byte)var44;
            }

            int var45;
            int var46;
            for(var42 = 0; var42 < var39; ++var42) {
               var43 = a(5, var0);

               for(var44 = 0; var44 < var38; ++var44) {
                  while(true) {
                     var45 = d(var0);
                     if (var45 == 0) {
                        var0.s[var42][var44] = (byte)var43;
                        break;
                     }

                     var46 = d(var0);
                     if (var46 == 0) {
                        ++var43;
                     } else {
                        --var43;
                     }
                  }
               }
            }

            byte var64;
            for(var42 = 0; var42 < var39; ++var42) {
               byte var63 = 32;
               var64 = 0;

               for(var45 = 0; var45 < var38; ++var45) {
                  if (var0.s[var42][var45] > var64) {
                     var64 = var0.s[var42][var45];
                  }

                  if (var0.s[var42][var45] < var63) {
                     var63 = var0.s[var42][var45];
                  }
               }

               a(var0.t[var42], var0.u[var42], var0.v[var42], var0.s[var42], var63, var64, var38);
               var0.w[var42] = var63;
            }

            var42 = var0.m + 1;
            var43 = -1;
            var64 = 0;

            for(var45 = 0; var45 <= 255; ++var45) {
               var0.j[var45] = 0;
            }

            var45 = 4095;

            int var47;
            for(var46 = 15; var46 >= 0; --var46) {
               for(var47 = 15; var47 >= 0; --var47) {
                  var0.q[var45] = (byte)(var46 * 16 + var47);
                  --var45;
               }

               var0.r[var46] = var45 + 1;
            }

            var46 = 0;
            if (var64 == 0) {
               ++var43;
               var64 = 50;
               byte var65 = var0.E[var43];
               var19 = var0.w[var65];
               var20 = var0.t[var65];
               var22 = var0.v[var65];
               var21 = var0.u[var65];
            }

            var44 = var64 - 1;
            var47 = var19;

            int var48;
            byte var49;
            for(var48 = a(var19, var0); var48 > var20[var47]; var48 = var48 << 1 | var49) {
               ++var47;
               var49 = d(var0);
            }

            int var50 = var22[var48 - var21[var47]];

            while(true) {
               int[] var51;
               int var53;
               int var55;
               byte var66;
               while(var42 != var50) {
                  int var56;
                  int var57;
                  byte var67;
                  if (var50 != 0 && var50 != 1) {
                     var53 = var50 - 1;
                     int var10003;
                     if (var53 < 16) {
                        var55 = var0.r[0];

                        for(var66 = var0.q[var53 + var55]; var53 > 3; var53 -= 4) {
                           var56 = var53 + var55;
                           var0.q[var56] = var0.q[var56 - 1];
                           var0.q[var56 - 1] = var0.q[var56 - 2];
                           var0.q[var56 - 2] = var0.q[var56 - 3];
                           var0.q[var56 - 3] = var0.q[var56 - 4];
                        }

                        while(var53 > 0) {
                           var0.q[var53 + var55] = var0.q[var53 + var55 - 1];
                           --var53;
                        }

                        var0.q[var55] = var66;
                     } else {
                        var55 = var53 / 16;
                        var56 = var53 % 16;
                        var57 = var0.r[var55] + var56;

                        for(var66 = var0.q[var57]; var57 > var0.r[var55]; --var57) {
                           var0.q[var57] = var0.q[var57 - 1];
                        }

                        for(var10003 = var0.r[var55]++; var55 > 0; --var55) {
                           var10003 = var0.r[var55]--;
                           var0.q[var0.r[var55]] = var0.q[var0.r[var55 - 1] + 16 - 1];
                        }

                        var10003 = var0.r[0]--;
                        var0.q[var0.r[0]] = var66;
                        if (var0.r[0] == 0) {
                           int var58 = 4095;

                           for(int var59 = 15; var59 >= 0; --var59) {
                              for(int var60 = 15; var60 >= 0; --var60) {
                                 var0.q[var58] = var0.q[var0.r[var59] + var60];
                                 --var58;
                              }

                              var0.r[var59] = var58 + 1;
                           }
                        }
                     }

                     var10003 = var0.j[var0.p[var66 & 255] & 255]++;
                     bo.et[var46] = var0.p[var66 & 255] & 255;
                     ++var46;
                     if (var44 == 0) {
                        ++var43;
                        var44 = 50;
                        var67 = var0.E[var43];
                        var19 = var0.w[var67];
                        var20 = var0.t[var67];
                        var22 = var0.v[var67];
                        var21 = var0.u[var67];
                     }

                     --var44;
                     var55 = var19;

                     byte var68;
                     for(var56 = a(var19, var0); var56 > var20[var55]; var56 = var56 << 1 | var68) {
                        ++var55;
                        var68 = d(var0);
                     }

                     var50 = var22[var56 - var21[var55]];
                  } else {
                     var53 = -1;
                     int var54 = 1;

                     do {
                        if (var50 == 0) {
                           var53 += var54;
                        } else if (var50 == 1) {
                           var53 += var54 * 2;
                        }

                        var54 *= 2;
                        if (var44 == 0) {
                           ++var43;
                           var44 = 50;
                           var55 = var0.E[var43];
                           var19 = var0.w[var55];
                           var20 = var0.t[var55];
                           var22 = var0.v[var55];
                           var21 = var0.u[var55];
                        }

                        --var44;
                        var55 = var19;

                        for(var56 = a(var19, var0); var56 > var20[var55]; var56 = var56 << 1 | var57) {
                           ++var55;
                           var57 = d(var0);
                        }

                        var50 = var22[var56 - var21[var55]];
                     } while(var50 == 0 || var50 == 1);

                     ++var53;
                     var67 = var0.p[var0.q[var0.r[0]] & 255];
                     var51 = var0.j;

                     for(var51[var67 & 255] += var53; var53 > 0; --var53) {
                        bo.et[var46] = var67 & 255;
                        ++var46;
                     }
                  }
               }

               var0.c = 0;
               var0.b = 0;
               var0.l[0] = 0;

               for(var53 = 1; var53 <= 256; ++var53) {
                  var0.l[var53] = var0.j[var53 - 1];
               }

               for(var53 = 1; var53 <= 256; ++var53) {
                  var51 = var0.l;
                  var51[var53] += var0.l[var53 - 1];
               }

               for(var53 = 0; var53 < var46; ++var53) {
                  var66 = (byte)(bo.et[var53] & 255);
                  var51 = bo.et;
                  var55 = var0.l[var66 & 255];
                  var51[var55] |= var53 << 8;
                  ++var0.l[var66 & 255];
               }

               var0.h = bo.et[var0.g] >> 8;
               var0.k = 0;
               var0.h = bo.et[var0.h];
               var0.i = (byte)(var0.h & 255);
               var0.h >>= 8;
               ++var0.k;
               var0.x = var46;
               a(var0);
               if (var0.k == var0.x + 1 && var0.c == 0) {
                  var23 = true;
                  break;
               }

               var23 = false;
               break;
            }
         }

         return;
      }
   }

   public static byte c(ij var0) {
      return (byte)a(8, var0);
   }

   public static byte d(ij var0) {
      return (byte)a(1, var0);
   }

   public static int a(int var0, ij var1) {
      while(var1.e < var0) {
         var1.d = var1.d << 8 | var1.K[var1.G] & 255;
         var1.e += 8;
         ++var1.G;
         ++var1.J;
         if (var1.J == 0) {
         }
      }

      int var2 = var1.d >> var1.e - var0 & (1 << var0) - 1;
      var1.e -= var0;
      return var2;
   }

   public static void e(ij var0) {
      var0.m = 0;

      for(int var1 = 0; var1 < 256; ++var1) {
         if (var0.n[var1]) {
            var0.p[var0.m] = (byte)var1;
            ++var0.m;
         }
      }

   }

   public static void a(int[] var0, int[] var1, int[] var2, byte[] var3, int var4, int var5, int var6) {
      int var7 = 0;

      int var8;
      int var9;
      for(var8 = var4; var8 <= var5; ++var8) {
         for(var9 = 0; var9 < var6; ++var9) {
            if (var3[var9] == var8) {
               var2[var7] = var9;
               ++var7;
            }
         }
      }

      for(var8 = 0; var8 < 23; ++var8) {
         var1[var8] = 0;
      }

      for(var8 = 0; var8 < var6; ++var8) {
         ++var1[var3[var8] + 1];
      }

      for(var8 = 1; var8 < 23; ++var8) {
         var1[var8] += var1[var8 - 1];
      }

      for(var8 = 0; var8 < 23; ++var8) {
         var0[var8] = 0;
      }

      var8 = 0;

      for(var9 = var4; var9 <= var5; ++var9) {
         int var10 = var1[var9 + 1] - var1[var9] + var8;
         var0[var9] = var10 - 1;
         var8 = var10 << 1;
      }

      for(var9 = var4 + 1; var9 <= var5; ++var9) {
         var1[var9] = (var0[var9 - 1] + 1 << 1) - var1[var9];
      }

   }
}
