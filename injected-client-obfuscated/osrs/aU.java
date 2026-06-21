package osrs;

public class aU extends aA {
   public static int a = 0;
   public static int b = 0;
   public static int c = 0;
   public static int d = 0;
   public static int e;
   public static int f;
   public static float[] g;
   public static int[] h;

   public static void a(int[] var0, int var1, int var2, float[] var3) {
      h = var0;
      f = var1;
      e = var2;
      g = var3;
      a(0, 0, var1, var2);
   }

   public static void a() {
      c = 0;
      a = 0;
      d = f;
      b = e;
   }

   public static void a(int var0, int var1, int var2, int var3) {
      if (var0 < 0) {
         var0 = 0;
      }

      if (var1 < 0) {
         var1 = 0;
      }

      if (var2 > f) {
         var2 = f;
      }

      if (var3 > e) {
         var3 = e;
      }

      c = var0;
      a = var1;
      d = var2;
      b = var3;
   }

   public static void b(int var0, int var1, int var2, int var3) {
      if (c < var0) {
         c = var0;
      }

      if (a < var1) {
         a = var1;
      }

      if (d > var2) {
         d = var2;
      }

      if (b > var3) {
         b = var3;
      }

   }

   public static void a(int[] var0) {
      var0[0] = c;
      var0[1] = a;
      var0[2] = d;
      var0[3] = b;
   }

   public static void b(int[] var0) {
      c = var0[0];
      a = var0[1];
      d = var0[2];
      b = var0[3];
   }

   public static void b() {
      int var0 = 0;

      int var1;
      for(var1 = f * e - 7; var0 < var1; h[var0++] = 0) {
         h[var0++] = 0;
         h[var0++] = 0;
         h[var0++] = 0;
         h[var0++] = 0;
         h[var0++] = 0;
         h[var0++] = 0;
         h[var0++] = 0;
      }

      for(var1 += 7; var0 < var1; h[var0++] = 0) {
      }

      d();
   }

   public static void c(int var0, int var1, int var2, int var3) {
      if (var2 == 0) {
         b(var0, var1, var3);
      } else {
         if (var2 < 0) {
            var2 = -var2;
         }

         int var4 = var1 - var2;
         if (var4 < a) {
            var4 = a;
         }

         int var5 = var1 + var2 + 1;
         if (var5 > b) {
            var5 = b;
         }

         int var6 = var4;
         int var7 = var2 * var2;
         int var8 = 0;
         int var9 = var1 - var4;
         int var10 = var9 * var9;
         int var11 = var10 - var9;
         if (var1 > var5) {
            var1 = var5;
         }

         int var12;
         int var13;
         int var14;
         int var15;
         while(var6 < var1) {
            while(var11 <= var7 || var10 <= var7) {
               var10 += var8 + var8;
               var11 += var8++ + var8;
            }

            var12 = var0 - var8 + 1;
            if (var12 < c) {
               var12 = c;
            }

            var13 = var0 + var8;
            if (var13 > d) {
               var13 = d;
            }

            var14 = f * var6 + var12;

            for(var15 = var12; var15 < var13; ++var15) {
               h[var14++] = var3 | -16777216;
            }

            ++var6;
            var10 -= var9-- + var9;
            var11 -= var9 + var9;
         }

         var12 = var2;
         var13 = var6 - var1;
         var14 = var13 * var13 + var7;
         var15 = var14 - var2;

         for(int var16 = var14 - var13; var6 < var5; var15 += var13++ + var13) {
            while(var16 > var7 && var15 > var7) {
               var16 -= var12-- + var12;
               var15 -= var12 + var12;
            }

            int var17 = var0 - var12;
            if (var17 < c) {
               var17 = c;
            }

            int var18 = var0 + var12;
            if (var18 > d - 1) {
               var18 = d - 1;
            }

            int var19 = f * var6 + var17;

            for(int var20 = var17; var20 <= var18; ++var20) {
               h[var19++] = var3 | -16777216;
            }

            ++var6;
            var16 += var13 + var13;
         }
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4) {
      if (var4 != 0) {
         if (var4 == 256) {
            c(var0, var1, var2, var3);
         } else {
            if (var2 < 0) {
               var2 = -var2;
            }

            int var5 = 256 - var4;
            int var6 = (var3 >> 16 & 255) * var4;
            int var7 = (var3 >> 8 & 255) * var4;
            int var8 = (var3 & 255) * var4;
            int var9 = var1 - var2;
            if (var9 < a) {
               var9 = a;
            }

            int var10 = var1 + var2 + 1;
            if (var10 > b) {
               var10 = b;
            }

            int var11 = var9;
            int var12 = var2 * var2;
            int var13 = 0;
            int var14 = var1 - var9;
            int var15 = var14 * var14;
            int var16 = var15 - var14;
            if (var1 > var10) {
               var1 = var10;
            }

            int var17;
            int var18;
            int var19;
            int var20;
            int var21;
            int var22;
            int var23;
            int var24;
            while(var11 < var1) {
               while(var16 <= var12 || var15 <= var12) {
                  var15 += var13 + var13;
                  var16 += var13++ + var13;
               }

               var17 = var0 - var13 + 1;
               if (var17 < c) {
                  var17 = c;
               }

               var18 = var0 + var13;
               if (var18 > d) {
                  var18 = d;
               }

               var19 = f * var11 + var17;

               for(var20 = var17; var20 < var18; ++var20) {
                  var21 = (h[var19] >> 16 & 255) * var5;
                  var22 = (h[var19] >> 8 & 255) * var5;
                  var23 = (h[var19] & 255) * var5;
                  var24 = (var8 + var23 >> 8) + (var6 + var21 >> 8 << 16) + (var7 + var22 >> 8 << 8);
                  Client.a(h, var19++, var24, var4);
               }

               ++var11;
               var15 -= var14-- + var14;
               var16 -= var14 + var14;
            }

            var17 = var2;
            var18 = -var14;
            var19 = var18 * var18 + var12;
            var20 = var19 - var2;

            for(var21 = var19 - var18; var11 < var10; var20 += var18++ + var18) {
               while(var21 > var12 && var20 > var12) {
                  var21 -= var17-- + var17;
                  var20 -= var17 + var17;
               }

               var22 = var0 - var17;
               if (var22 < c) {
                  var22 = c;
               }

               var23 = var0 + var17;
               if (var23 > d - 1) {
                  var23 = d - 1;
               }

               var24 = f * var11 + var22;

               for(int var25 = var22; var25 <= var23; ++var25) {
                  int var26 = (h[var24] >> 16 & 255) * var5;
                  int var27 = (h[var24] >> 8 & 255) * var5;
                  int var28 = (h[var24] & 255) * var5;
                  int var29 = (var8 + var28 >> 8) + (var6 + var26 >> 8 << 16) + (var7 + var27 >> 8 << 8);
                  Client.a(h, var24++, var29, var4);
               }

               ++var11;
               var21 += var18 + var18;
            }
         }
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4, int var5) {
      if (var0 < c) {
         var2 -= c - var0;
         var0 = c;
      }

      if (var1 < a) {
         var3 -= a - var1;
         var1 = a;
      }

      if (var0 + var2 > d) {
         var2 = d - var0;
      }

      if (var1 + var3 > b) {
         var3 = b - var1;
      }

      int var6 = ((var4 & '\uff00') * var5 >> 8 & '\uff00') + ((var4 & 16711935) * var5 >> 8 & 16711935);
      int var7 = 256 - var5;
      int var8 = f - var2;
      int var9 = f * var1 + var0;

      for(int var10 = 0; var10 < var3; ++var10) {
         for(int var11 = -var2; var11 < 0; ++var11) {
            int var12 = h[var9];
            int var13 = ((var12 & '\uff00') * var7 >> 8 & '\uff00') + ((var12 & 16711935) * var7 >> 8 & 16711935);
            Client.a(h, var9++, var6 + var13, var5);
         }

         var9 += var8;
      }

   }

   public static void b(int var0, int var1, int var2, int var3, int var4) {
      if (var0 < c) {
         var2 -= c - var0;
         var0 = c;
      }

      if (var1 < a) {
         var3 -= a - var1;
         var1 = a;
      }

      if (var0 + var2 > d) {
         var2 = d - var0;
      }

      if (var1 + var3 > b) {
         var3 = b - var1;
      }

      int var5 = f - var2;
      int var6 = f * var1 + var0;

      for(int var7 = -var3; var7 < 0; ++var7) {
         for(int var8 = -var2; var8 < 0; ++var8) {
            h[var6++] = var4 | -16777216;
         }

         var6 += var5;
      }

   }

   public static void b(int var0, int var1, int var2, int var3, int var4, int var5) {
      if (var2 > 0 && var3 > 0) {
         int var6 = 0;
         int var7 = 65536 / var3;
         if (var0 < c) {
            var2 -= c - var0;
            var0 = c;
         }

         if (var1 < a) {
            var6 += (a - var1) * var7;
            var3 -= a - var1;
            var1 = a;
         }

         if (var0 + var2 > d) {
            var2 = d - var0;
         }

         if (var1 + var3 > b) {
            var3 = b - var1;
         }

         int var8 = f - var2;
         int var9 = f * var1 + var0;

         for(int var10 = -var3; var10 < 0; ++var10) {
            int var11 = 65536 - var6 >> 8;
            int var12 = var6 >> 8;
            int var13 = ((var4 & '\uff00') * var11 + (var5 & '\uff00') * var12 & 16711680) + ((var4 & 16711935) * var11 + (var5 & 16711935) * var12 & -16711936) >>> 8;

            for(int var14 = -var2; var14 < 0; ++var14) {
               h[var9++] = var13 | -16777216;
            }

            var9 += var8;
            var6 += var7;
         }
      }

   }

   public static void c(int var0, int var1, int var2, int var3, int var4) {
      d(var0, var1, var2, var4);
      d(var0, var1 + var3 - 1, var2, var4);
      e(var0, var1, var3, var4);
      e(var0 + var2 - 1, var1, var3, var4);
   }

   public static void c(int var0, int var1, int var2, int var3, int var4, int var5) {
      d(var0, var1, var2, var4, var5);
      d(var0, var1 + var3 - 1, var2, var4, var5);
      if (var3 >= 3) {
         e(var0, var1 + 1, var3 - 2, var4, var5);
         e(var0 + var2 - 1, var1 + 1, var3 - 2, var4, var5);
      }

   }

   public static void d(int var0, int var1, int var2, int var3) {
      if (var1 >= a && var1 < b) {
         if (var0 < c) {
            var2 -= c - var0;
            var0 = c;
         }

         if (var0 + var2 > d) {
            var2 = d - var0;
         }

         int var4 = f * var1 + var0;

         for(int var5 = 0; var5 < var2; ++var5) {
            h[var4 + var5] = var3 | -16777216;
         }
      }

   }

   public static void d(int var0, int var1, int var2, int var3, int var4) {
      if (var1 >= a && var1 < b) {
         if (var0 < c) {
            var2 -= c - var0;
            var0 = c;
         }

         if (var0 + var2 > d) {
            var2 = d - var0;
         }

         int var5 = 256 - var4;
         int var6 = (var3 >> 16 & 255) * var4;
         int var7 = (var3 >> 8 & 255) * var4;
         int var8 = (var3 & 255) * var4;
         int var9 = f * var1 + var0;

         for(int var10 = 0; var10 < var2; ++var10) {
            int var11 = (h[var9] >> 16 & 255) * var5;
            int var12 = (h[var9] >> 8 & 255) * var5;
            int var13 = (h[var9] & 255) * var5;
            int var14 = (var8 + var13 >> 8) + (var6 + var11 >> 8 << 16) + (var7 + var12 >> 8 << 8);
            Client.a(h, var9++, var14, var4);
         }
      }

   }

   public static void e(int var0, int var1, int var2, int var3) {
      if (var0 >= c && var0 < d) {
         if (var1 < a) {
            var2 -= a - var1;
            var1 = a;
         }

         if (var1 + var2 > b) {
            var2 = b - var1;
         }

         int var4 = f * var1 + var0;

         for(int var5 = 0; var5 < var2; ++var5) {
            h[f * var5 + var4] = var3 | -16777216;
         }
      }

   }

   public static void e(int var0, int var1, int var2, int var3, int var4) {
      if (var0 >= c && var0 < d) {
         if (var1 < a) {
            var2 -= a - var1;
            var1 = a;
         }

         if (var1 + var2 > b) {
            var2 = b - var1;
         }

         int var5 = 256 - var4;
         int var6 = (var3 >> 16 & 255) * var4;
         int var7 = (var3 >> 8 & 255) * var4;
         int var8 = (var3 & 255) * var4;
         int var9 = f * var1 + var0;

         for(int var10 = 0; var10 < var2; ++var10) {
            int var11 = (h[var9] >> 16 & 255) * var5;
            int var12 = (h[var9] >> 8 & 255) * var5;
            int var13 = (h[var9] & 255) * var5;
            int var14 = (var8 + var13 >> 8) + (var6 + var11 >> 8 << 16) + (var7 + var12 >> 8 << 8);
            Client.a(h, var9, var14, var4);
            var9 += f;
         }
      }

   }

   public static void f(int var0, int var1, int var2, int var3, int var4) {
      int var5 = var2 - var0;
      int var6 = var3 - var1;
      if (var6 == 0) {
         if (var5 >= 0) {
            d(var0, var1, var5 + 1, var4);
         } else {
            d(var0 + var5, var1, -var5 + 1, var4);
         }
      } else if (var5 == 0) {
         if (var6 >= 0) {
            e(var0, var1, var6 + 1, var4);
         } else {
            e(var0, var1 + var6, -var6 + 1, var4);
         }
      } else {
         if (var5 + var6 < 0) {
            var0 += var5;
            var5 = -var5;
            var1 += var6;
            var6 = -var6;
         }

         int var7;
         int var8;
         int var9;
         int var10;
         int var11;
         int var12;
         if (var5 > var6) {
            var7 = var1 << 16;
            var8 = var7 + '耀';
            var9 = var6 << 16;
            var10 = (int)Math.floor((double)var9 / (double)var5 + 0.5);
            var11 = var0 + var5;
            if (var0 < c) {
               var8 += (c - var0) * var10;
               var0 = c;
            }

            if (var11 >= d) {
               var11 = d - 1;
            }

            while(var0 <= var11) {
               var12 = var8 >> 16;
               if (var12 >= a && var12 < b) {
                  h[f * var12 + var0] = var4 | -16777216;
               }

               var8 += var10;
               ++var0;
            }
         } else {
            var7 = var0 << 16;
            var8 = var7 + '耀';
            var9 = var5 << 16;
            var10 = (int)Math.floor((double)var9 / (double)var6 + 0.5);
            var11 = var1 + var6;
            if (var1 < a) {
               var8 += (a - var1) * var10;
               var1 = a;
            }

            if (var11 >= b) {
               var11 = b - 1;
            }

            while(var1 <= var11) {
               var12 = var8 >> 16;
               if (var12 >= c && var12 < d) {
                  h[f * var1 + var12] = var4 | -16777216;
               }

               var8 += var10;
               ++var1;
            }
         }
      }

   }

   public static void g(int var0, int var1, int var2, int var3, int var4) {
      if (var1 >= a && var1 < b) {
         int var5 = var0 - var2;
         int var6 = var0 + var3;
         int var7 = Math.max(var5, c);
         int var8 = Math.min(var6, d - 1);
         if (var7 <= var8) {
            d(var7, var1, var8 - var7 + 1, var4);
         }
      }

   }

   public static void h(int var0, int var1, int var2, int var3, int var4) {
      if (var0 >= c && var0 < d) {
         int var5 = var1 - var2;
         int var6 = var1 + var3;
         int var7 = Math.max(var5, a);
         int var8 = Math.min(var6, b - 1);
         if (var7 <= var8) {
            e(var0, var7, var8 - var7 + 1, var4);
         }
      }

   }

   public static void a(int var0, int var1, int var2) {
      if (var1 >= a && var1 < b && var0 >= c && var0 < d) {
         h[f * var1 + var0] = var2;
      }

   }

   public static void d(int var0, int var1, int var2, int var3, int var4, int var5) {
      int var6 = var5 | -16777216;
      if (var4 <= 1) {
         f(var0, var1, var2, var3, var6);
      } else {
         int var7 = var4 / 2;
         int var8 = var4 - var7;
         int var9 = var2 - var0;
         int var10 = var3 - var1;
         int var11;
         int var12;
         int var13;
         int var14;
         int var15;
         int var16;
         int var17;
         int var18;
         int var19;
         if (var10 == 0) {
            var11 = var0;
            var12 = var2;
            if (var0 > var2) {
               var11 = var2;
               var12 = var0;
            }

            var13 = var1 - var7;
            var14 = var1 + var8;
            var15 = Math.max(var13, a);
            var16 = Math.min(var14, b - 1);
            if (var15 <= var16) {
               var17 = Math.max(var11, c);
               var18 = Math.min(var12, d - 1);
               if (var17 <= var18) {
                  for(var19 = var15; var19 <= var16; ++var19) {
                     d(var17, var19, var18 - var17 + 1, var6);
                  }
               }
            }
         } else if (var9 == 0) {
            var11 = var1;
            var12 = var3;
            if (var1 > var3) {
               var11 = var3;
               var12 = var1;
            }

            var13 = var0 - var7;
            var14 = var0 + var8;
            var15 = Math.max(var13, c);
            var16 = Math.min(var14, d - 1);
            if (var15 <= var16) {
               var17 = Math.max(var11, a);
               var18 = Math.min(var12, b - 1);
               if (var17 <= var18) {
                  for(var19 = var15; var19 <= var16; ++var19) {
                     e(var19, var17, var18 - var17 + 1, var6);
                  }
               }
            }
         } else {
            var11 = Math.abs(var9);
            var12 = Math.abs(var10);
            int var20;
            if (var11 == var12) {
               var13 = var9 >= 0 ? 1 : -1;
               var14 = var10 >= 0 ? 1 : -1;
               var15 = -var14;
               var16 = var13;
               var17 = var0;
               var18 = var1;

               for(var19 = 0; var19 <= var11; ++var19) {
                  for(var20 = -var7; var20 <= var8 - 1; ++var20) {
                     int var21 = var15 * var20;
                     int var22 = var16 * var20;
                     a(var17 + var21, var18 + var22, var6);
                     boolean var23 = (var19 & 1) != 0;
                     if (var23) {
                        a(var17 + var21 + 1, var18 + var22, var6);
                     }

                     a(var17 + var21 + var13, var18 + var22, var6);
                  }

                  var17 += var13;
                  var18 += var14;
               }
            } else if (var11 >= var12) {
               if (var2 < var0) {
                  var13 = var0;
                  var0 = var2;
                  var2 = var13;
                  var14 = var1;
                  var1 = var3;
                  var3 = var14;
               }

               var13 = var2 - var0;
               var14 = var3 - var1;
               var15 = (var1 << 16) + '耀';
               var16 = A.b(var14 << 16, var13);
               var17 = var0;
               if (var0 < c) {
                  var15 += (c - var0) * var16;
                  var17 = c;
               }

               var18 = Math.min(var2, d - 1);

               for(var19 = var17; var19 <= var18; ++var19) {
                  var20 = var15 >> 16;
                  h(var19, var20, var7, var8, var6);
                  var15 += var16;
               }
            } else {
               if (var3 < var1) {
                  var13 = var0;
                  var0 = var2;
                  var2 = var13;
                  var14 = var1;
                  var1 = var3;
                  var3 = var14;
               }

               var13 = var2 - var0;
               var14 = var3 - var1;
               var15 = (var0 << 16) + '耀';
               var16 = A.b(var13 << 16, var14);
               var17 = var1;
               if (var1 < a) {
                  var15 += (a - var1) * var16;
                  var17 = a;
               }

               var18 = Math.min(var3, b - 1);

               for(var19 = var17; var19 <= var18; ++var19) {
                  var20 = var15 >> 16;
                  g(var20, var19, var7, var8, var6);
                  var15 += var16;
               }
            }
         }
      }

   }

   public static void b(int var0, int var1, int var2) {
      if (var0 >= c && var1 >= a && var0 < d && var1 < b) {
         h[f * var1 + var0] = var2 | -16777216;
      }

   }

   public static void a(int var0, int var1, int var2, int[] var3, int[] var4) {
      int var5 = f * var1 + var0;

      for(int var6 = 0; var6 < var3.length; ++var6) {
         int var7 = var3[var6] + var5;

         for(int var8 = -var4[var6]; var8 < 0; ++var8) {
            h[var7++] = var2 | -16777216;
         }

         var5 += f;
      }

   }

   public static void c() {
      if (g != null) {
         int var0 = g.length;

         for(int var1 = 0; var1 < var0; ++var1) {
            if (var1 % f < f / 2 && g[var1] > 0.0F) {
               float var2 = g[var1];
               float var3 = 150.75377F / (var2 - 1.0100503F);
               float var4 = var3 / 14925.0F / 1.0100503F;
               int var5 = (int)(var4 * 255.0F);
               h[var1] = var5 << 16 | var5 << 8 | var5;
            }
         }
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4, int var5, byte[] var6, int var7, boolean var8) {
      int var9 = f;
      int var10 = e;
      int[] var11 = h;
      int var12;
      int var14;
      int var15;
      int var16;
      int var17;
      int var18;
      int var19;
      int var21;
      int var22;
      int var23;
      int var24;
      int var25;
      int var26;
      int var27;
      if (!Client.s.isGpu()) {
         var12 = var7;
         byte[] var13 = var6;
         var14 = var5;
         var15 = var4;
         var16 = var3;
         var17 = var2;
         var18 = var1;
         var19 = var0;
         boolean var20 = true;
         if (var2 + var0 >= 0 && var3 + var1 >= 0 && var0 < f && var1 < e) {
            var21 = 0;
            var22 = 0;
            if (var0 < 0) {
               var21 -= var0;
               var17 = var2 + var0;
            }

            if (var1 < 0) {
               var22 -= var1;
               var16 = var3 + var1;
            }

            if (var17 + var0 > f) {
               var17 = f - var0;
            }

            if (var16 + var1 > e) {
               var16 = e - var1;
            }

            var23 = var6.length / var7;
            var24 = f - var17;
            var25 = var4 >>> 24;
            var26 = var5 >>> 24;
            int var28;
            int var29;
            int var30;
            int var31;
            if (var20 && (var25 != 255 || var26 != 255)) {
               var27 = (var1 + var22) * f + var0 + var21;

               for(var28 = var1 + var22; var28 < var18 + var22 + var16; ++var28) {
                  for(var29 = var19 + var21; var29 < var19 + var21 + var17; ++var29) {
                     var30 = (var28 - var18) % var23;
                     var31 = (var29 - var19) % var12;
                     int var32 = var15;
                     if (var13[var12 * var30 + var31] != 0) {
                        var32 = var14;
                     }

                     int var33 = var32 >>> 24;
                     int var34 = 255 - var33;
                     int var35 = h[var27];
                     int var36 = ((var32 & '\uff00') * var33 + (var35 & '\uff00') * var34 & 16711680) + ((var32 & 16711935) * var33 + (var35 & 16711935) * var34 & -16711936) >> 8;
                     h[var27++] = var36;
                  }

                  var27 += var24;
               }
            } else {
               var27 = (var1 + var22) * f + var0 + var21;

               for(var28 = var1 + var22; var28 < var18 + var22 + var16; ++var28) {
                  for(var29 = var19 + var21; var29 < var19 + var21 + var17; ++var29) {
                     var30 = (var28 - var18) % var23;
                     var31 = (var29 - var19) % var12;
                     if (var13[var12 * var30 + var31] != 0) {
                        h[var27++] = var14;
                     } else {
                        h[var27++] = var15;
                     }
                  }

                  var27 += var24;
               }
            }
         }
      } else if (var0 + var2 >= 0 && var1 + var3 >= 0 && var0 < var9 && var1 < var10) {
         var12 = 0;
         int var37 = 0;
         if (var0 < 0) {
            var12 -= var0;
            var2 += var0;
         }

         if (var1 < 0) {
            var37 -= var1;
            var3 += var1;
         }

         if (var0 + var2 > var9) {
            var2 = var9 - var0;
         }

         if (var1 + var3 > var10) {
            var3 = var10 - var1;
         }

         var14 = var6.length / var7;
         var15 = var9 - var2;
         var16 = var4 >>> 24;
         var17 = var5 >>> 24;
         int var38;
         if (var16 == 255 && var17 == 255) {
            var18 = (var1 + var37) * var9 + var0 + var12;

            for(var19 = var1 + var37; var19 < var3 + var37 + var1; ++var19) {
               for(var38 = var0 + var12; var38 < var0 + var12 + var2; ++var38) {
                  var21 = (var19 - var1) % var14;
                  var22 = (var38 - var0) % var7;
                  if (var6[var7 * var21 + var22] != 0) {
                     var11[var18++] = var5;
                  } else {
                     var11[var18++] = var4;
                  }
               }

               var18 += var15;
            }
         } else {
            var18 = (var1 + var37) * var9 + var0 + var12;

            for(var19 = var1 + var37; var19 < var3 + var37 + var1; ++var19) {
               for(var38 = var0 + var12; var38 < var0 + var12 + var2; ++var38) {
                  var21 = (var19 - var1) % var14;
                  var22 = (var38 - var0) % var7;
                  var23 = var4;
                  if (var6[var7 * var21 + var22] != 0) {
                     var23 = var5;
                  }

                  var24 = var23 >>> 24;
                  var25 = 255 - var24;
                  var26 = var11[var18];
                  var27 = ((var23 & '\uff00') * var24 + (var26 & '\uff00') * var25 & 16711680) + ((var23 & 16711935) * var24 + (var26 & 16711935) * var25 & -16711936) >> 8;
                  Client.a(var11, var18++, var27, var24);
               }

               var18 += var15;
            }
         }
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = f;
      int var9 = c;
      int var10 = a;
      int var11 = d;
      int var12 = b;
      int[] var13 = h;
      int var14;
      int var15;
      int var16;
      int var17;
      int var18;
      int var19;
      int var20;
      int var21;
      int var22;
      int var23;
      int var24;
      int var25;
      int var26;
      int var27;
      if (!Client.s.isGpu()) {
         var14 = var7;
         var15 = var6;
         var16 = var5;
         var17 = var4;
         var18 = var3;
         var19 = var2;
         var20 = var1;
         var21 = var0;
         if (var2 > 0 && var3 > 0) {
            var22 = 0;
            var23 = 65536 / var3;
            if (var0 < c) {
               var19 = var2 - (c - var0);
               var21 = c;
            }

            if (var1 < a) {
               var22 += (a - var1) * var23;
               var18 = var3 - (a - var1);
               var20 = a;
            }

            if (var19 + var21 > d) {
               var19 = d - var21;
            }

            if (var18 + var20 > b) {
               var18 = b - var20;
            }

            var24 = f - var19;
            var25 = f * var20 + var21;

            for(var26 = -var18; var26 < 0; ++var26) {
               var27 = 65536 - var22 >> 8;
               int var28 = var22 >> 8;
               int var29 = (var14 * var28 + var15 * var27 & '\uff00') >>> 8;
               if (var29 == 0) {
                  var25 += f;
                  var22 += var23;
               } else {
                  int var30 = ((var16 & '\uff00') * var28 + (var17 & '\uff00') * var27 & 16711680) + ((var16 & 16711935) * var28 + (var17 & 16711935) * var27 & -16711936) >>> 8;
                  int var31 = 255 - var29;
                  int var32 = ((var30 & '\uff00') * var29 >> 8 & '\uff00') + ((var30 & 16711935) * var29 >> 8 & 16711935);

                  for(int var33 = -var19; var33 < 0; ++var33) {
                     int var34 = h[var25];
                     if (var34 == 0) {
                        h[var25++] = var32;
                     } else {
                        int var35 = ((var34 & '\uff00') * var31 >> 8 & '\uff00') + ((var34 & 16711935) * var31 >> 8 & 16711935);
                        h[var25++] = var32 + var35;
                     }
                  }

                  var25 += var24;
                  var22 += var23;
               }
            }
         }
      } else if (var2 > 0 && var3 > 0) {
         var14 = 0;
         var15 = 65536 / var3;
         if (var0 < var9) {
            var2 -= var9 - var0;
            var0 = var9;
         }

         if (var1 < var10) {
            var14 += (var10 - var1) * var15;
            var3 -= var10 - var1;
            var1 = var10;
         }

         if (var0 + var2 > var11) {
            var2 = var11 - var0;
         }

         if (var1 + var3 > var12) {
            var3 = var12 - var1;
         }

         var16 = var8 - var2;
         var17 = var1 * var8 + var0;

         for(var18 = -var3; var18 < 0; ++var18) {
            var19 = 65536 - var14 >> 8;
            var20 = var14 >> 8;
            var21 = (var6 * var19 + var7 * var20 & '\uff00') >>> 8;
            if (var21 == 0) {
               var17 += var8;
               var14 += var15;
            } else {
               var22 = ((var4 & '\uff00') * var19 + (var5 & '\uff00') * var20 & 16711680) + ((var4 & 16711935) * var19 + (var5 & 16711935) * var20 & -16711936) >>> 8;
               var23 = 255 - var21;
               var24 = ((var22 & '\uff00') * var21 >> 8 & '\uff00') + ((var22 & 16711935) * var21 >> 8 & 16711935);

               for(var25 = -var2; var25 < 0; ++var25) {
                  var26 = var13[var17];
                  var27 = ((var26 & '\uff00') * var23 >> 8 & '\uff00') + ((var26 & 16711935) * var23 >> 8 & 16711935);
                  Client.a(var13, var17++, var24 + var27, var21);
               }

               var17 += var16;
               var14 += var15;
            }
         }
      }

   }

   public static void d() {
      if (!Client.s.isGpu() && g != null) {
         int var0;
         int var1;
         int var2;
         if (c == 0 && f == d && a == 0 && e == b) {
            var0 = g.length;
            var1 = var0 - (var0 & 7);

            for(var2 = 0; var2 < var1; g[var2++] = 0.0F) {
               g[var2++] = 0.0F;
               g[var2++] = 0.0F;
               g[var2++] = 0.0F;
               g[var2++] = 0.0F;
               g[var2++] = 0.0F;
               g[var2++] = 0.0F;
               g[var2++] = 0.0F;
            }

            while(var2 < var0) {
               g[var2++] = 0.0F;
            }
         } else {
            var0 = d - c;
            var1 = b - a;
            var2 = f - var0;
            int var3 = f * a + c;
            int var4 = var0 >> 3;
            int var5 = var0 & 7;
            int var6 = var3 - 1;

            for(int var7 = -var1; var7 < 0; ++var7) {
               int var8;
               if (var4 > 0) {
                  var8 = var4;

                  do {
                     ++var6;
                     g[var6] = 0.0F;
                     ++var6;
                     g[var6] = 0.0F;
                     ++var6;
                     g[var6] = 0.0F;
                     ++var6;
                     g[var6] = 0.0F;
                     ++var6;
                     g[var6] = 0.0F;
                     ++var6;
                     g[var6] = 0.0F;
                     ++var6;
                     g[var6] = 0.0F;
                     ++var6;
                     g[var6] = 0.0F;
                     --var8;
                  } while(var8 > 0);
               }

               if (var5 > 0) {
                  var8 = var5;

                  do {
                     ++var6;
                     g[var6] = 0.0F;
                     --var8;
                  } while(var8 > 0);
               }

               var6 += var2;
            }
         }
      }

   }
}
