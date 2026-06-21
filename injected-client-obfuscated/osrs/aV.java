package osrs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.api.SpritePixels;

public final class aV extends aU implements SpritePixels {
   public int i;
   public int j;
   public int[] k;
   public int l;
   public int m;
   public int n;
   public int o;

   public aV() {
   }

   public aV(int var1, int var2) {
      this(new int[var1 * var2], var1, var2);
   }

   public aV(int[] var1, int var2, int var3) {
      this.k = var1;
      this.l = this.i = var2;
      this.m = this.j = var3;
      this.n = 0;
      this.o = 0;
   }

   public static void a(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      for(int var8 = -var5; var8 < 0; ++var8) {
         int var9;
         for(var9 = var3 + var4 - 3; var3 < var9; var0[var3++] = var1[var2++] | -16777216) {
            var0[var3++] = var1[var2++] | -16777216;
            var0[var3++] = var1[var2++] | -16777216;
            var0[var3++] = var1[var2++] | -16777216;
         }

         for(var9 += 3; var3 < var9; var0[var3++] = var1[var2++] | -16777216) {
         }

         var3 += var6;
         var2 += var7;
      }

   }

   public static void a(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = -(var5 >> 2);
      int var10 = -(var5 & 3);

      for(int var11 = -var6; var11 < 0; ++var11) {
         int var12;
         int var13;
         for(var12 = var9; var12 < 0; ++var12) {
            var13 = var1[var3++];
            if (var13 != 0) {
               var0[var4++] = var13 | -16777216;
            } else {
               ++var4;
            }

            int var14 = var1[var3++];
            if (var14 != 0) {
               var0[var4++] = var14 | -16777216;
            } else {
               ++var4;
            }

            int var15 = var1[var3++];
            if (var15 != 0) {
               var0[var4++] = var15 | -16777216;
            } else {
               ++var4;
            }

            int var16 = var1[var3++];
            if (var16 != 0) {
               var0[var4++] = var16 | -16777216;
            } else {
               ++var4;
            }
         }

         for(var12 = var10; var12 < 0; ++var12) {
            var13 = var1[var3++];
            if (var13 != 0) {
               var0[var4++] = var13 | -16777216;
            } else {
               ++var4;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   public static void a(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      int var12 = var3;

      for(int var13 = -var8; var13 < 0; ++var13) {
         int var14 = (var4 >> 16) * var11;

         for(int var15 = -var7; var15 < 0; ++var15) {
            int var16 = var1[(var3 >> 16) + var14];
            if (var16 != 0) {
               var0[var5++] = var16 | -16777216;
            } else {
               ++var5;
            }

            var3 += var9;
         }

         var4 += var10;
         var3 = var12;
         var5 += var6;
      }

   }

   public static void a(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      int var11 = 256 - var9;
      int var12 = (var10 & 16711935) * var11 & -16711936;
      int var13 = (var10 & '\uff00') * var11 & 16711680;
      int var14 = (var12 | var13) >>> 8;

      for(int var15 = -var6; var15 < 0; ++var15) {
         for(int var16 = -var5; var16 < 0; ++var16) {
            int var17 = var1[var3++];
            if (var17 != 0) {
               int var18 = (var17 & 16711935) * var9 & -16711936;
               int var19 = (var17 & '\uff00') * var9 & 16711680;
               var0[var4++] = ((var18 | var19) >>> 8) + var14 | -16777216;
            } else {
               ++var4;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   public static void a(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      int var10 = 256 - var9;

      for(int var11 = -var6; var11 < 0; ++var11) {
         for(int var12 = -var5; var12 < 0; ++var12) {
            int var13 = var1[var3++];
            if (var13 != 0) {
               int var14 = var0[var4];
               Client.a(var0, var4++, ((var13 & '\uff00') * var9 + (var14 & '\uff00') * var10 & 16711680) + ((var13 & 16711935) * var9 + (var14 & 16711935) * var10 & -16711936) >> 8, var9);
            } else {
               ++var4;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   public static void a(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      int var13 = 256 - var12;
      int var14 = var3;

      for(int var15 = -var8; var15 < 0; ++var15) {
         int var16 = (var4 >> 16) * var11;

         for(int var17 = -var7; var17 < 0; ++var17) {
            int var18 = var1[(var3 >> 16) + var16];
            if (var18 != 0) {
               int var19 = var0[var5];
               Client.a(var0, var5++, ((var18 & '\uff00') * var12 + (var19 & '\uff00') * var13 & 16711680) + ((var18 & 16711935) * var12 + (var19 & 16711935) * var13 & -16711936) >> 8, var12);
            } else {
               ++var5;
            }

            var3 += var9;
         }

         var4 += var10;
         var3 = var14;
         var5 += var6;
      }

   }

   public static void a(int var0, int var1, int var2, int[] var3, int[] var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      for(int var13 = -var10; var13 < 0; ++var13) {
         for(int var14 = -var9; var14 < 0; ++var14) {
            int var15 = var4[var5++];
            if (var15 != 0) {
               int var16 = var3[var7];
               int var17 = var15 + var16;
               int var18 = (var15 & 16711935) + (var16 & 16711935);
               int var19 = (var17 - var18 & 65536) + (var18 & 16777472);
               var3[var7++] = var17 - var19 | var19 - (var19 >>> 8);
            } else {
               ++var7;
            }
         }

         var7 += var11;
         var5 += var12;
      }

   }

   public static void a(int var0, int var1, int var2, int[] var3, int[] var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13) {
      for(int var14 = -var10; var14 < 0; ++var14) {
         for(int var15 = -var9; var15 < 0; ++var15) {
            int var16 = var4[var5++];
            if (var16 != 0) {
               int var17 = (var16 & 16711935) * var13;
               int var18 = (var13 * var16 - var17 & 16711680) + (var17 & -16711936) >>> 8;
               int var19 = var3[var7];
               int var20 = var18 + var19;
               int var21 = (var18 & 16711935) + (var19 & 16711935);
               int var22 = (var20 - var21 & 65536) + (var21 & 16777472);
               var3[var7++] = var20 - var22 | var22 - (var22 >>> 8);
            } else {
               ++var7;
            }
         }

         var7 += var11;
         var5 += var12;
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int[] var4, int[] var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15) {
      for(int var16 = var3; var8 < 0; ++var8) {
         int var17 = (var9 >> 16) * var15;

         for(int var18 = -var12; var18 < 0; ++var18) {
            int var19 = var4[(var3 >> 16) + var17];
            if (var19 != 0) {
               int var20 = var5[var10];
               int var21 = var19 + var20;
               int var22 = (var19 & 16711935) + (var20 & 16711935);
               int var23 = (var21 - var22 & 65536) + (var22 & 16777472);
               var5[var10++] = var21 - var23 | var23 - (var23 >>> 8);
            } else {
               ++var10;
            }

            var3 += var13;
         }

         var9 += var14;
         var3 = var16;
         var10 += var11;
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int[] var4, int[] var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16) {
      for(int var17 = var3; var8 < 0; ++var8) {
         int var18 = (var9 >> 16) * var15;

         for(int var19 = -var12; var19 < 0; ++var19) {
            int var20 = var4[(var3 >> 16) + var18];
            if (var20 != 0) {
               int var21 = (var20 & 16711935) * var16;
               int var22 = (var16 * var20 - var21 & 16711680) + (var21 & -16711936) >>> 8;
               int var23 = var5[var10];
               int var24 = var22 + var23;
               int var25 = (var22 & 16711935) + (var23 & 16711935);
               int var26 = (var24 - var25 & 65536) + (var25 & 16777472);
               var5[var10++] = var24 - var26 | var26 - (var26 >>> 8);
            } else {
               ++var10;
            }

            var3 += var13;
         }

         var9 += var14;
         var3 = var17;
         var10 += var11;
      }

   }

   public aV e() {
      aV var1 = new aV(this.l, this.m);
      var1.i = this.i;
      var1.j = this.j;
      var1.o = this.i - this.l - this.o;
      var1.n = this.n;

      for(int var2 = 0; var2 < this.m; ++var2) {
         for(int var3 = 0; var3 < this.l; ++var3) {
            var1.k[this.l * var2 + var3] = this.k[this.l * var2 + this.l - 1 - var3];
         }
      }

      return var1;
   }

   public aV f() {
      aV var1 = new aV(this.l, this.m);
      var1.i = this.i;
      var1.j = this.j;
      var1.o = this.o;
      var1.n = this.n;
      int var2 = this.k.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.k[var3] = this.k[var3];
      }

      return var1;
   }

   public aV g() {
      aV var1 = new aV(this.i, this.j);

      for(int var2 = 0; var2 < this.m; ++var2) {
         for(int var3 = 0; var3 < this.l; ++var3) {
            var1.k[(this.n + var2) * this.i + this.o + var3] = this.k[this.l * var2 + var3];
         }
      }

      return var1;
   }

   public void h() {
      a(this.k, this.l, this.m, (float[])null);
   }

   public void i() {
      if (this.i != this.l || this.m != this.j) {
         int[] var1 = new int[this.i * this.j];

         for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.l; ++var3) {
               var1[(this.n + var2) * this.i + this.o + var3] = this.k[this.l * var2 + var3];
            }
         }

         this.k = var1;
         this.l = this.i;
         this.m = this.j;
         this.o = 0;
         this.n = 0;
      }

   }

   public void a(int var1) {
      if (this.i != this.l || this.m != this.j) {
         int var2 = var1;
         if (var1 > this.o) {
            var2 = this.o;
         }

         int var3 = var1;
         if (this.o + var1 + this.l > this.i) {
            var3 = this.i - this.o - this.l;
         }

         int var4 = var1;
         if (var1 > this.n) {
            var4 = this.n;
         }

         int var5 = var1;
         if (this.n + var1 + this.m > this.j) {
            var5 = this.j - this.n - this.m;
         }

         int var6 = this.l + var2 + var3;
         int var7 = this.m + var4 + var5;
         int[] var8 = new int[var6 * var7];

         for(int var9 = 0; var9 < this.m; ++var9) {
            for(int var10 = 0; var10 < this.l; ++var10) {
               var8[(var4 + var9) * var6 + var2 + var10] = this.k[this.l * var9 + var10];
            }
         }

         this.k = var8;
         this.l = var6;
         this.m = var7;
         this.o -= var2;
         this.n -= var4;
      }

   }

   public void j() {
      int[] var1 = new int[this.m * this.l];
      int var2 = 0;

      for(int var3 = 0; var3 < this.m; ++var3) {
         for(int var4 = this.l - 1; var4 >= 0; --var4) {
            var1[var2++] = this.k[this.l * var3 + var4];
         }
      }

      this.k = var1;
      this.o = this.i - this.l - this.o;
   }

   public void k() {
      int[] var1 = new int[this.m * this.l];
      int var2 = 0;

      for(int var3 = this.m - 1; var3 >= 0; --var3) {
         for(int var4 = 0; var4 < this.l; ++var4) {
            var1[var2++] = this.k[this.l * var3 + var4];
         }
      }

      this.k = var1;
      this.n = this.j - this.m - this.n;
   }

   public void b(int var1) {
      int[] var2 = new int[this.m * this.l];
      int var3 = 0;

      for(int var4 = 0; var4 < this.m; ++var4) {
         for(int var5 = 0; var5 < this.l; ++var5) {
            int var6 = this.k[var3];
            if (var6 == 0) {
               if (var5 > 0 && this.k[var3 - 1] != 0) {
                  var6 = var1;
               } else if (var4 > 0 && this.k[var3 - this.l] != 0) {
                  var6 = var1;
               } else if (var5 < this.l - 1 && this.k[var3 + 1] != 0) {
                  var6 = var1;
               } else if (var4 < this.m - 1 && this.k[this.l + var3] != 0) {
                  var6 = var1;
               }
            }

            var2[var3++] = var6;
         }
      }

      this.k = var2;
   }

   public void c(int var1) {
      for(int var2 = this.m - 1; var2 > 0; --var2) {
         int var3 = this.l * var2;

         for(int var4 = this.l - 1; var4 > 0; --var4) {
            if (this.k[var3 + var4] == 0 && this.k[var3 + var4 - 1 - this.l] != 0) {
               this.k[var3 + var4] = var1;
            }
         }
      }

   }

   public void a(int var1, int var2) {
      int var3 = this.o + var1;
      int var4 = this.n + var2;
      int var5 = aU.f * var4 + var3;
      int var6 = 0;
      int var7 = this.m;
      int var8 = this.l;
      int var9 = aU.f - var8;
      int var10 = 0;
      int var11;
      if (var4 < a) {
         var11 = a - var4;
         var7 -= var11;
         var4 = a;
         var6 += var8 * var11;
         var5 += aU.f * var11;
      }

      if (var4 + var7 > b) {
         var7 -= var4 + var7 - b;
      }

      if (var3 < c) {
         var11 = c - var3;
         var8 -= var11;
         var3 = c;
         var6 += var11;
         var5 += var11;
         var10 += var11;
         var9 += var11;
      }

      if (var3 + var8 > d) {
         var11 = var3 + var8 - d;
         var8 -= var11;
         var10 += var11;
         var9 += var11;
      }

      if (var8 > 0 && var7 > 0) {
         a(aU.h, this.k, var6, var5, var8, var7, var9, var10);
      }

   }

   public void b(int var1, int var2) {
      int var3 = this.o + var1;
      int var4 = this.n + var2;
      int var5 = aU.f * var4 + var3;
      int var6 = 0;
      int var7 = this.m;
      int var8 = this.l;
      int var9 = aU.f - var8;
      int var10 = 0;
      int var11;
      if (var4 < a) {
         var11 = a - var4;
         var7 -= var11;
         var4 = a;
         var6 += var8 * var11;
         var5 += aU.f * var11;
      }

      if (var4 + var7 > b) {
         var7 -= var4 + var7 - b;
      }

      if (var3 < c) {
         var11 = c - var3;
         var8 -= var11;
         var3 = c;
         var6 += var11;
         var5 += var11;
         var10 += var11;
         var9 += var11;
      }

      if (var3 + var8 > d) {
         var11 = var3 + var8 - d;
         var8 -= var11;
         var10 += var11;
         var9 += var11;
      }

      if (var8 > 0 && var7 > 0) {
         a(aU.h, this.k, 0, var6, var5, var8, var7, var9, var10);
      }

   }

   public void f(int var1, int var2, int var3, int var4) {
      if (var3 > 0 && var4 > 0) {
         int var5 = this.l;
         int var6 = this.m;
         int var7 = 0;
         int var8 = 0;
         int var9 = this.i;
         int var10 = this.j;
         int var11 = (var9 << 16) / var3;
         int var12 = (var10 << 16) / var4;
         int var13;
         if (this.o > 0) {
            var13 = ((this.o << 16) + var11 - 1) / var11;
            var1 += var13;
            var7 += var11 * var13 - (this.o << 16);
         }

         if (this.n > 0) {
            var13 = ((this.n << 16) + var12 - 1) / var12;
            var2 += var13;
            var8 += var12 * var13 - (this.n << 16);
         }

         if (var5 < var9) {
            var3 = ((var5 << 16) - var7 + var11 - 1) / var11;
         }

         if (var6 < var10) {
            var4 = ((var6 << 16) - var8 + var12 - 1) / var12;
         }

         var13 = aU.f * var2 + var1;
         int var14 = aU.f - var3;
         if (var2 + var4 > b) {
            var4 -= var2 + var4 - b;
         }

         int var15;
         if (var2 < a) {
            var15 = a - var2;
            var4 -= var15;
            var13 += aU.f * var15;
            var8 += var12 * var15;
         }

         if (var1 + var3 > d) {
            var15 = var1 + var3 - d;
            var3 -= var15;
            var14 += var15;
         }

         if (var1 < c) {
            var15 = c - var1;
            var3 -= var15;
            var13 += var15;
            var7 += var11 * var15;
            var14 += var15;
         }

         a(aU.h, this.k, 0, var7, var8, var13, var14, var3, var4, var11, var12, var5);
      }

   }

   public void g(int var1, int var2, int var3, int var4) {
      if (var3 == 256) {
         this.b(var1, var2);
      } else {
         int var5 = this.o + var1;
         int var6 = this.n + var2;
         int var7 = aU.f * var6 + var5;
         int var8 = 0;
         int var9 = this.m;
         int var10 = this.l;
         int var11 = aU.f - var10;
         int var12 = 0;
         int var13;
         if (var6 < a) {
            var13 = a - var6;
            var9 -= var13;
            var6 = a;
            var8 += var10 * var13;
            var7 += aU.f * var13;
         }

         if (var6 + var9 > b) {
            var9 -= var6 + var9 - b;
         }

         if (var5 < c) {
            var13 = c - var5;
            var10 -= var13;
            var5 = c;
            var8 += var13;
            var7 += var13;
            var12 += var13;
            var11 += var13;
         }

         if (var5 + var10 > d) {
            var13 = var5 + var10 - d;
            var10 -= var13;
            var12 += var13;
            var11 += var13;
         }

         if (var10 > 0 && var9 > 0) {
            a(aU.h, this.k, 0, var8, var7, var10, var9, var11, var12, var3, var4);
         }
      }

   }

   public void c(int var1, int var2, int var3) {
      int var4 = this.o + var1;
      int var5 = this.n + var2;
      int var6 = aU.f * var5 + var4;
      int var7 = 0;
      int var8 = this.m;
      int var9 = this.l;
      int var10 = aU.f - var9;
      int var11 = 0;
      int var12;
      if (var5 < a) {
         var12 = a - var5;
         var8 -= var12;
         var5 = a;
         var7 += var9 * var12;
         var6 += aU.f * var12;
      }

      if (var5 + var8 > b) {
         var8 -= var5 + var8 - b;
      }

      if (var4 < c) {
         var12 = c - var4;
         var9 -= var12;
         var4 = c;
         var7 += var12;
         var6 += var12;
         var11 += var12;
         var10 += var12;
      }

      if (var4 + var9 > d) {
         var12 = var4 + var9 - d;
         var9 -= var12;
         var11 += var12;
         var10 += var12;
      }

      if (var9 > 0 && var8 > 0) {
         a(aU.h, this.k, 0, var7, var6, var9, var8, var10, var11, var3);
      }

   }

   public void i(int var1, int var2, int var3, int var4, int var5) {
      if (var3 > 0 && var4 > 0) {
         int var6 = this.l;
         int var7 = this.m;
         int var8 = 0;
         int var9 = 0;
         int var10 = this.i;
         int var11 = this.j;
         int var12 = (var10 << 16) / var3;
         int var13 = (var11 << 16) / var4;
         int var14;
         if (this.o > 0) {
            var14 = ((this.o << 16) + var12 - 1) / var12;
            var1 += var14;
            var8 += var12 * var14 - (this.o << 16);
         }

         if (this.n > 0) {
            var14 = ((this.n << 16) + var13 - 1) / var13;
            var2 += var14;
            var9 += var13 * var14 - (this.n << 16);
         }

         if (var6 < var10) {
            var3 = ((var6 << 16) - var8 + var12 - 1) / var12;
         }

         if (var7 < var11) {
            var4 = ((var7 << 16) - var9 + var13 - 1) / var13;
         }

         var14 = aU.f * var2 + var1;
         int var15 = aU.f - var3;
         if (var2 + var4 > b) {
            var4 -= var2 + var4 - b;
         }

         int var16;
         if (var2 < a) {
            var16 = a - var2;
            var4 -= var16;
            var14 += aU.f * var16;
            var9 += var13 * var16;
         }

         if (var1 + var3 > d) {
            var16 = var1 + var3 - d;
            var3 -= var16;
            var15 += var16;
         }

         if (var1 < c) {
            var16 = c - var1;
            var3 -= var16;
            var14 += var16;
            var8 += var12 * var16;
            var15 += var16;
         }

         a(aU.h, this.k, 0, var8, var9, var14, var15, var3, var4, var12, var13, var6, var5);
      }

   }

   public void d(int var1, int var2, int var3) {
      int var4 = this.o + var1;
      int var5 = this.n + var2;
      int var6 = aU.f * var5 + var4;
      int var7 = 0;
      int var8 = this.m;
      int var9 = this.l;
      int var10 = aU.f - var9;
      int var11 = 0;
      int var12;
      if (var5 < a) {
         var12 = a - var5;
         var8 -= var12;
         var5 = a;
         var7 += var9 * var12;
         var6 += aU.f * var12;
      }

      if (var5 + var8 > b) {
         var8 -= var5 + var8 - b;
      }

      if (var4 < c) {
         var12 = c - var4;
         var9 -= var12;
         var4 = c;
         var7 += var12;
         var6 += var12;
         var11 += var12;
         var10 += var12;
      }

      if (var4 + var9 > d) {
         var12 = var4 + var9 - d;
         var9 -= var12;
         var11 += var12;
         var10 += var12;
      }

      if (var9 > 0 && var8 > 0) {
         if (var3 == 256) {
            a(0, 0, 0, aU.h, this.k, var7, 0, var6, 0, var9, var8, var10, var11);
         } else {
            a(0, 0, 0, aU.h, this.k, var7, 0, var6, 0, var9, var8, var10, var11, var3);
         }
      }

   }

   public void j(int var1, int var2, int var3, int var4, int var5) {
      if (var3 > 0 && var4 > 0) {
         int var6 = this.l;
         int var7 = this.m;
         int var8 = 0;
         int var9 = 0;
         int var10 = this.i;
         int var11 = this.j;
         int var12 = (var10 << 16) / var3;
         int var13 = (var11 << 16) / var4;
         int var14;
         if (this.o > 0) {
            var14 = ((this.o << 16) + var12 - 1) / var12;
            var1 += var14;
            var8 += var12 * var14 - (this.o << 16);
         }

         if (this.n > 0) {
            var14 = ((this.n << 16) + var13 - 1) / var13;
            var2 += var14;
            var9 += var13 * var14 - (this.n << 16);
         }

         if (var6 < var10) {
            var3 = ((var6 << 16) - var8 + var12 - 1) / var12;
         }

         if (var7 < var11) {
            var4 = ((var7 << 16) - var9 + var13 - 1) / var13;
         }

         var14 = aU.f * var2 + var1;
         int var15 = aU.f - var3;
         if (var2 + var4 > b) {
            var4 -= var2 + var4 - b;
         }

         int var16;
         if (var2 < a) {
            var16 = a - var2;
            var4 -= var16;
            var14 += aU.f * var16;
            var9 += var13 * var16;
         }

         if (var1 + var3 > d) {
            var16 = var1 + var3 - d;
            var3 -= var16;
            var15 += var16;
         }

         if (var1 < c) {
            var16 = c - var1;
            var3 -= var16;
            var14 += var16;
            var8 += var12 * var16;
            var15 += var16;
         }

         if (var5 == 256) {
            a(0, 0, 0, var8, this.k, aU.h, 0, 0, -var4, var9, var14, var15, var3, var12, var13, var6);
         } else {
            a(0, 0, 0, var8, this.k, aU.h, 0, 0, -var4, var9, var14, var15, var3, var12, var13, var6, var5);
         }
      }

   }

   public void toBufferedOutline(BufferedImage var1, int var2) {
      int var3 = this.getWidth();
      int var4 = this.getHeight();
      if (var1.getWidth() == var3 && var1.getHeight() == var4) {
         int[] var5 = this.getPixels();
         int[] var6 = new int[var3 * var4];
         int var7 = 0;

         for(int var8 = 0; var8 < var4; ++var8) {
            for(int var9 = 0; var9 < var3; ++var9) {
               int var10 = var5[var7];
               if (var10 == 16777215 || var10 == 0) {
                  if (var9 > 0 && var5[var7 - 1] != 0) {
                     var10 = var2;
                  } else if (var8 > 0 && var5[var7 - var3] != 0) {
                     var10 = var2;
                  } else if (var9 < var3 - 1 && var5[var7 + 1] != 0) {
                     var10 = var2;
                  } else if (var8 < var4 - 1 && var5[var3 + var7] != 0) {
                     var10 = var2;
                  }

                  var6[var7] = var10;
               }

               ++var7;
            }
         }

         var1.setRGB(0, 0, var3, var4, var6, 0, var3);
      } else {
         throw new IllegalArgumentException("Image bounds do not match SpritePixels");
      }
   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, int[] var7, int[] var8) {
      int var9 = var2 < 0 ? -var2 : 0;
      int var10 = this.m + var2 <= var6 ? this.m : var6 - var2;
      int var11 = var1 < 0 ? -var1 : 0;
      if (this.l + var1 <= var5) {
         int var12 = this.l;
      } else {
         int var10000 = var5 - var1;
      }

      int var13 = (var2 + var4 + var9) * aU.f + var1 + var3 + var11;
      int var14 = var2 + var9;

      for(int var15 = var9; var15 < var10; ++var15) {
         int var16 = var7[var14];
         int var17 = var8[var14++];
         int var18 = var13;
         int var19;
         if (var1 < var16) {
            var19 = var16 - var1;
            var18 = var19 - var11 + var13;
         } else {
            var19 = var11;
         }

         int var20;
         if (this.l + var1 <= var16 + var17) {
            var20 = this.l;
         } else {
            var20 = var16 + var17 - var1;
         }

         for(int var21 = var19; var21 < var20; ++var21) {
            int var22 = this.k[this.l * var15 + var21];
            if (var22 != 0) {
               aU.h[var18++] = var22 | -16777216;
            } else {
               ++var18;
            }
         }

         var13 += aU.f;
      }

   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int[] var11, int[] var12) {
      try {
         int var13 = -var3 / 2;
         int var14 = -var4 / 2;
         int var15 = (int)(Math.sin((double)var7 / 326.11) * 65536.0);
         int var16 = (int)(Math.cos((double)var7 / 326.11) * 65536.0);
         int var17 = (int)(((long)var15 << 8) / (long)var8);
         int var18 = (int)(((long)var16 << 8) / (long)var8);
         int var19 = (var5 << 16) + var13 * var18 + var14 * var17;
         int var20 = (var6 << 16) + (var14 * var18 - var13 * var17);
         int var21 = aU.f * var2 + var1;

         for(int var22 = 0; var22 < var4; ++var22) {
            int var23 = var2 + var22;
            int var24 = var23 - var10;
            if (var24 >= 0 && var24 < var11.length && var11[var24] >= 0) {
               int var25 = var21;
               int var26 = var19;
               int var27 = var20;

               for(int var28 = -var3; var28 < 0; ++var28) {
                  int var29 = var1 + var3 + var28;
                  int var30 = var29 - var9;
                  if (var30 >= var11[var24] && var30 < var11[var24] + var12[var24]) {
                     int var31 = var26 >> 16;
                     int var32 = var27 >> 16;
                     if (var31 >= 0 && var31 < this.l && var32 >= 0 && var32 < this.m) {
                        int var33 = this.l * var32 + var31;
                        int var34 = this.k[var33];
                        if (var34 != 0) {
                           aU.h[var25] = var34 | -16777216;
                        }
                     }
                  }

                  ++var25;
                  var26 += var18;
                  var27 -= var17;
               }
            }

            var19 += var17;
            var20 += var18;
            var21 += aU.f;
         }
      } catch (Exception var35) {
      }

   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, double var7, int var9) {
      try {
         if (var7 < 1.5707963267948966 || var7 > 4.71238898038469) {
            if (aU.f % 2 != 0) {
               ++var5;
            }

            if (aU.e % 2 != 0) {
               ++var6;
            }
         }

         int var10 = -var3 / 2;
         int var11 = -var4 / 2;
         int var12 = (int)(Math.sin(var7) * 65536.0);
         int var13 = (int)(Math.cos(var7) * 65536.0);
         int var14 = (int)(((long)var12 << 8) / (long)var9);
         int var15 = (int)(((long)var13 << 8) / (long)var9);
         int var16 = (var5 << 16) + var10 * var15 + var11 * var14;
         int var17 = (var6 << 16) + (var11 * var15 - var10 * var14);
         int var18 = aU.f * var2 + var1;

         for(int var19 = 0; var19 < var4; ++var19) {
            int var20 = var18;
            int var21 = var16;
            int var22 = var17;

            for(int var23 = -var3; var23 < 0; ++var23) {
               int var24 = var21 >> 16;
               int var25 = var22 >> 16;
               if (var24 >= 0 && var24 < this.l && var25 >= 0 && var25 < this.m) {
                  int var26 = this.l * var25 + var24;
                  if (var26 >= 0 && var26 < this.k.length) {
                     int var27 = this.k[var26];
                     if (var27 != 0) {
                        aU.h[var20] = var27 | -16777216;
                     }
                  }
               }

               ++var20;
               var21 += var15;
               var22 -= var14;
            }

            var16 += var14;
            var17 += var15;
            var18 += aU.f;
         }
      } catch (Exception var28) {
      }

   }

   public void h(int var1, int var2, int var3, int var4) {
      this.e(this.i << 3, this.j << 3, var1 << 4, var2 << 4, var3, var4);
   }

   public void e(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var6 != 0) {
         int var7 = var1 - (this.o << 4);
         int var8 = var2 - (this.n << 4);
         double var9 = (double)(var5 & '\uffff') * 9.587379924285257E-5;
         int var11 = (int)Math.floor(Math.sin(var9) * (double)var6 + 0.5);
         int var12 = (int)Math.floor(Math.cos(var9) * (double)var6 + 0.5);
         int var13 = -var7 * var12 + -var8 * var11;
         int var14 = -(-var7) * var11 + -var8 * var12;
         int var15 = ((this.l << 4) - var7) * var12 + -var8 * var11;
         int var16 = -((this.l << 4) - var7) * var11 + -var8 * var12;
         int var17 = ((this.m << 4) - var8) * var11 + -var7 * var12;
         int var18 = ((this.m << 4) - var8) * var12 + -(-var7) * var11;
         int var19 = ((this.m << 4) - var8) * var11 + ((this.l << 4) - var7) * var12;
         int var20 = ((this.m << 4) - var8) * var12 + -((this.l << 4) - var7) * var11;
         int var21;
         int var22;
         if (var13 < var15) {
            var21 = var13;
            var22 = var15;
         } else {
            var21 = var15;
            var22 = var13;
         }

         if (var17 < var21) {
            var21 = var17;
         }

         if (var19 < var21) {
            var21 = var19;
         }

         if (var17 > var22) {
            var22 = var17;
         }

         if (var19 > var22) {
            var22 = var19;
         }

         int var23;
         int var24;
         if (var14 < var16) {
            var23 = var14;
            var24 = var16;
         } else {
            var23 = var16;
            var24 = var14;
         }

         if (var18 < var23) {
            var23 = var18;
         }

         if (var20 < var23) {
            var23 = var20;
         }

         if (var18 > var24) {
            var24 = var18;
         }

         if (var20 > var24) {
            var24 = var20;
         }

         int var25 = var21 >> 12;
         int var26 = var22 + 4095 >> 12;
         int var27 = var23 >> 12;
         int var28 = var24 + 4095 >> 12;
         int var29 = var3 + var25;
         int var30 = var3 + var26;
         int var31 = var4 + var27;
         int var32 = var4 + var28;
         int var33 = var29 >> 4;
         int var34 = var30 + 15 >> 4;
         int var35 = var31 >> 4;
         int var36 = var32 + 15 >> 4;
         if (var33 < c) {
            var33 = c;
         }

         if (var34 > d) {
            var34 = d;
         }

         if (var35 < a) {
            var35 = a;
         }

         if (var36 > b) {
            var36 = b;
         }

         int var37 = var33 - var34;
         if (var37 < 0) {
            int var38 = var35 - var36;
            if (var38 < 0) {
               int var39 = aU.f * var35 + var33;
               double var40 = 1.6777216E7 / (double)var6;
               int var42 = (int)Math.floor(Math.sin(var9) * var40 + 0.5);
               int var43 = (int)Math.floor(Math.cos(var9) * var40 + 0.5);
               int var44 = (var33 << 4) + 8 - var3;
               int var45 = (var35 << 4) + 8 - var4;
               int var46 = (var7 << 8) - (var42 * var45 >> 4);
               int var47 = (var8 << 8) + (var43 * var45 >> 4);
               int var48;
               int var49;
               int var50;
               int var51;
               int var52;
               int var53;
               int var54;
               int var55;
               if (var43 == 0) {
                  if (var42 == 0) {
                     for(var48 = var38; var48 < 0; var39 += aU.f) {
                        var49 = var39;
                        var50 = var46;
                        var51 = var47;
                        var52 = var37;
                        if (var46 >= 0 && var47 >= 0 && var46 - (this.l << 12) < 0 && var47 - (this.m << 12) < 0) {
                           for(; var52 < 0; ++var52) {
                              var53 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                              if (var53 != 0) {
                                 aU.h[var49++] = var53 | -16777216;
                              } else {
                                 ++var49;
                              }
                           }
                        }

                        ++var48;
                     }
                  } else if (var42 < 0) {
                     for(var48 = var38; var48 < 0; var39 += aU.f) {
                        var49 = var39;
                        var50 = var46;
                        var51 = (var42 * var44 >> 4) + var47;
                        var52 = var37;
                        if (var46 >= 0 && var46 - (this.l << 12) < 0) {
                           if ((var53 = var51 - (this.m << 12)) >= 0) {
                              var54 = (var42 - var53) / var42;
                              var52 = var37 + var54;
                              var51 += var42 * var54;
                              var49 = var39 + var54;
                           }

                           if ((var54 = (var51 - var42) / var42) > var52) {
                              var52 = var54;
                           }

                           while(var52 < 0) {
                              var55 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                              if (var55 != 0) {
                                 aU.h[var49++] = var55 | -16777216;
                              } else {
                                 ++var49;
                              }

                              var51 += var42;
                              ++var52;
                           }
                        }

                        ++var48;
                        var46 -= var42;
                     }
                  } else {
                     for(var48 = var38; var48 < 0; var39 += aU.f) {
                        var49 = var39;
                        var50 = var46;
                        var51 = (var42 * var44 >> 4) + var47;
                        var52 = var37;
                        if (var46 >= 0 && var46 - (this.l << 12) < 0) {
                           if (var51 < 0) {
                              var53 = (var42 - 1 - var51) / var42;
                              var52 = var37 + var53;
                              var51 += var42 * var53;
                              var49 = var39 + var53;
                           }

                           if ((var53 = (var51 + 1 - (this.m << 12) - var42) / var42) > var52) {
                              var52 = var53;
                           }

                           while(var52 < 0) {
                              var54 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                              if (var54 != 0) {
                                 aU.h[var49++] = var54 | -16777216;
                              } else {
                                 ++var49;
                              }

                              var51 += var42;
                              ++var52;
                           }
                        }

                        ++var48;
                        var46 -= var42;
                     }
                  }
               } else {
                  int var56;
                  if (var43 < 0) {
                     if (var42 == 0) {
                        for(var48 = var38; var48 < 0; var39 += aU.f) {
                           var49 = var39;
                           var50 = (var43 * var44 >> 4) + var46;
                           var51 = var47;
                           var52 = var37;
                           if (var47 >= 0 && var47 - (this.m << 12) < 0) {
                              if ((var53 = var50 - (this.l << 12)) >= 0) {
                                 var54 = (var43 - var53) / var43;
                                 var52 = var37 + var54;
                                 var50 += var43 * var54;
                                 var49 = var39 + var54;
                              }

                              if ((var54 = (var50 - var43) / var43) > var52) {
                                 var52 = var54;
                              }

                              while(var52 < 0) {
                                 var55 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                                 if (var55 != 0) {
                                    aU.h[var49++] = var55 | -16777216;
                                 } else {
                                    ++var49;
                                 }

                                 var50 += var43;
                                 ++var52;
                              }
                           }

                           ++var48;
                           var47 += var43;
                        }
                     } else if (var42 < 0) {
                        for(var48 = var38; var48 < 0; var39 += aU.f) {
                           var49 = var39;
                           var50 = (var43 * var44 >> 4) + var46;
                           var51 = (var42 * var44 >> 4) + var47;
                           var52 = var37;
                           if ((var53 = var50 - (this.l << 12)) >= 0) {
                              var54 = (var43 - var53) / var43;
                              var52 = var37 + var54;
                              var50 += var43 * var54;
                              var51 += var42 * var54;
                              var49 = var39 + var54;
                           }

                           if ((var54 = (var50 - var43) / var43) > var52) {
                              var52 = var54;
                           }

                           if ((var55 = var51 - (this.m << 12)) >= 0) {
                              var56 = (var42 - var55) / var42;
                              var52 += var56;
                              var50 += var43 * var56;
                              var51 += var42 * var56;
                              var49 += var56;
                           }

                           if ((var56 = (var51 - var42) / var42) > var52) {
                              var52 = var56;
                           }

                           while(var52 < 0) {
                              int var57 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                              if (var57 != 0) {
                                 aU.h[var49++] = var57 | -16777216;
                              } else {
                                 ++var49;
                              }

                              var50 += var43;
                              var51 += var42;
                              ++var52;
                           }

                           ++var48;
                           var46 -= var42;
                           var47 += var43;
                        }
                     } else {
                        for(var48 = var38; var48 < 0; var39 += aU.f) {
                           var49 = var39;
                           var50 = (var43 * var44 >> 4) + var46;
                           var51 = (var42 * var44 >> 4) + var47;
                           var52 = var37;
                           if ((var53 = var50 - (this.l << 12)) >= 0) {
                              var54 = (var43 - var53) / var43;
                              var52 = var37 + var54;
                              var50 += var43 * var54;
                              var51 += var42 * var54;
                              var49 = var39 + var54;
                           }

                           if ((var54 = (var50 - var43) / var43) > var52) {
                              var52 = var54;
                           }

                           if (var51 < 0) {
                              var55 = (var42 - 1 - var51) / var42;
                              var52 += var55;
                              var50 += var43 * var55;
                              var51 += var42 * var55;
                              var49 += var55;
                           }

                           if ((var55 = (var51 + 1 - (this.m << 12) - var42) / var42) > var52) {
                              var52 = var55;
                           }

                           while(var52 < 0) {
                              var56 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                              if (var56 != 0) {
                                 aU.h[var49++] = var56 | -16777216;
                              } else {
                                 ++var49;
                              }

                              var50 += var43;
                              var51 += var42;
                              ++var52;
                           }

                           ++var48;
                           var46 -= var42;
                           var47 += var43;
                        }
                     }
                  } else if (var42 == 0) {
                     for(var48 = var38; var48 < 0; var39 += aU.f) {
                        var49 = var39;
                        var50 = (var43 * var44 >> 4) + var46;
                        var51 = var47;
                        var52 = var37;
                        if (var47 >= 0 && var47 - (this.m << 12) < 0) {
                           if (var50 < 0) {
                              var53 = (var43 - 1 - var50) / var43;
                              var52 = var37 + var53;
                              var50 += var43 * var53;
                              var49 = var39 + var53;
                           }

                           if ((var53 = (var50 + 1 - (this.l << 12) - var43) / var43) > var52) {
                              var52 = var53;
                           }

                           while(var52 < 0) {
                              var54 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                              if (var54 != 0) {
                                 aU.h[var49++] = var54 | -16777216;
                              } else {
                                 ++var49;
                              }

                              var50 += var43;
                              ++var52;
                           }
                        }

                        ++var48;
                        var47 += var43;
                     }
                  } else if (var42 < 0) {
                     for(var48 = var38; var48 < 0; var39 += aU.f) {
                        var49 = var39;
                        var50 = (var43 * var44 >> 4) + var46;
                        var51 = (var42 * var44 >> 4) + var47;
                        var52 = var37;
                        if (var50 < 0) {
                           var53 = (var43 - 1 - var50) / var43;
                           var52 = var37 + var53;
                           var50 += var43 * var53;
                           var51 += var42 * var53;
                           var49 = var39 + var53;
                        }

                        if ((var53 = (var50 + 1 - (this.l << 12) - var43) / var43) > var52) {
                           var52 = var53;
                        }

                        if ((var54 = var51 - (this.m << 12)) >= 0) {
                           var55 = (var42 - var54) / var42;
                           var52 += var55;
                           var50 += var43 * var55;
                           var51 += var42 * var55;
                           var49 += var55;
                        }

                        if ((var55 = (var51 - var42) / var42) > var52) {
                           var52 = var55;
                        }

                        while(var52 < 0) {
                           var56 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                           if (var56 != 0) {
                              aU.h[var49++] = var56 | -16777216;
                           } else {
                              ++var49;
                           }

                           var50 += var43;
                           var51 += var42;
                           ++var52;
                        }

                        ++var48;
                        var46 -= var42;
                        var47 += var43;
                     }
                  } else {
                     for(var48 = var38; var48 < 0; var39 += aU.f) {
                        var49 = var39;
                        var50 = (var43 * var44 >> 4) + var46;
                        var51 = (var42 * var44 >> 4) + var47;
                        var52 = var37;
                        if (var50 < 0) {
                           var53 = (var43 - 1 - var50) / var43;
                           var52 = var37 + var53;
                           var50 += var43 * var53;
                           var51 += var42 * var53;
                           var49 = var39 + var53;
                        }

                        if ((var53 = (var50 + 1 - (this.l << 12) - var43) / var43) > var52) {
                           var52 = var53;
                        }

                        if (var51 < 0) {
                           var54 = (var42 - 1 - var51) / var42;
                           var52 += var54;
                           var50 += var43 * var54;
                           var51 += var42 * var54;
                           var49 += var54;
                        }

                        if ((var54 = (var51 + 1 - (this.m << 12) - var42) / var42) > var52) {
                           var52 = var54;
                        }

                        while(var52 < 0) {
                           var55 = this.k[(var50 >> 12) + (var51 >> 12) * this.l];
                           if (var55 != 0) {
                              aU.h[var49++] = var55 | -16777216;
                           } else {
                              ++var49;
                           }

                           var50 += var43;
                           var51 += var42;
                           ++var52;
                        }

                        ++var48;
                        var46 -= var42;
                        var47 += var43;
                     }
                  }
               }
            }
         }
      }

   }

   public void i(int var1, int var2, int var3, int var4) {
      if (var3 <= this.i && var4 <= this.j) {
         int var5 = this.o * var3 / this.i + var1;
         int var6 = ((this.o + this.l) * var3 + this.i - 1) / this.i + var1;
         int var7 = this.n * var4 / this.j + var2;
         int var8 = ((this.m + this.n) * var4 + this.j - 1) / this.j + var2;
         if (var5 < c) {
            var5 = c;
         }

         if (var6 > d) {
            var6 = d;
         }

         if (var7 < a) {
            var7 = a;
         }

         if (var8 > b) {
            var8 = b;
         }

         if (var5 < var6 && var7 < var8) {
            int var9 = aU.f * var7 + var5;
            int var10 = aU.f - (var6 - var5);
            if (var9 < aU.h.length) {
               for(int var11 = var7; var11 < var8; ++var11) {
                  for(int var12 = var5; var12 < var6; ++var12) {
                     int var13 = var12 - var1 << 4;
                     int var14 = var11 - var2 << 4;
                     int var15 = this.i * var13 / var3 - (this.o << 4);
                     int var16 = (var13 + 16) * this.i / var3 - (this.o << 4);
                     int var17 = this.j * var14 / var4 - (this.n << 4);
                     int var18 = (var14 + 16) * this.j / var4 - (this.n << 4);
                     int var19 = (var16 - var15) * (var18 - var17) >> 1;
                     if (var19 != 0) {
                        if (var15 < 0) {
                           var15 = 0;
                        }

                        if (var16 >= this.l << 4) {
                           var16 = this.l << 4;
                        }

                        if (var17 < 0) {
                           var17 = 0;
                        }

                        if (var18 >= this.m << 4) {
                           var18 = this.m << 4;
                        }

                        --var16;
                        --var18;
                        int var20 = 16 - (var15 & 15);
                        int var21 = (var16 & 15) + 1;
                        int var22 = 16 - (var17 & 15);
                        int var23 = (var18 & 15) + 1;
                        int var24 = var15 >> 4;
                        int var25 = var16 >> 4;
                        int var26 = var17 >> 4;
                        int var27 = var18 >> 4;
                        int var28 = 0;
                        int var29 = 0;
                        int var30 = 0;
                        int var31 = 0;

                        int var32;
                        for(var32 = var26; var32 <= var27; ++var32) {
                           int var33 = 16;
                           if (var26 == var32) {
                              var33 = var22;
                           }

                           if (var27 == var32) {
                              var33 = var23;
                           }

                           for(int var34 = var24; var34 <= var25; ++var34) {
                              int var35 = this.k[this.l * var32 + var34];
                              if (var35 != 0) {
                                 int var36;
                                 if (var24 == var34) {
                                    var36 = var20 * var33;
                                 } else if (var25 == var34) {
                                    var36 = var21 * var33;
                                 } else {
                                    var36 = var33 << 4;
                                 }

                                 var31 += var36;
                                 var28 += (var35 >> 16 & 255) * var36;
                                 var29 += (var35 >> 8 & 255) * var36;
                                 var30 += (var35 & 255) * var36;
                              }
                           }
                        }

                        if (var31 >= var19) {
                           var32 = var30 / var31 + (var28 / var31 << 16) + (var29 / var31 << 8);
                           if (var32 == 0) {
                              var32 = 1;
                           }

                           aU.h[var9] = var32 | -16777216;
                        }

                        ++var9;
                     }
                  }

                  var9 += var10;
               }
            }
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   public void toBufferedImage(BufferedImage var1) {
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      if (var1.getWidth() == var2 && var1.getHeight() == var3) {
         int[] var4 = this.getPixels();
         int[] var5 = new int[var4.length];

         for(int var6 = 0; var6 < var4.length; ++var6) {
            if (var4[var6] != 0) {
               var5[var6] = var4[var6] | -16777216;
            }
         }

         var1.setRGB(0, 0, var2, var3, var5, 0, var2);
      } else {
         throw new IllegalArgumentException("Image bounds do not match SpritePixels");
      }
   }

   public BufferedImage toBufferedImage() {
      BufferedImage var1 = new BufferedImage(this.getWidth(), this.getHeight(), 2);
      this.toBufferedImage(var1);
      return var1;
   }

   public BufferedImage toBufferedOutline(Color var1) {
      BufferedImage var2 = new BufferedImage(this.getWidth(), this.getHeight(), 2);
      this.toBufferedOutline(var2, var1.getRGB());
      return var2;
   }

   public void l() {
      this.h();
   }

   public aV m() {
      return this.f();
   }

   public int getOffsetX() {
      return this.o;
   }

   public void setOffsetX(int var1) {
      this.o = var1;
   }

   public int getOffsetY() {
      return this.n;
   }

   public int getMaxWidth() {
      return this.i;
   }

   public int getMaxHeight() {
      return this.j;
   }

   public void c(int var1, int var2) {
      this.a(var1, var2);
   }

   public void drawAt(int var1, int var2) {
      this.b(var1, var2);
   }

   public void setMaxHeight(int var1) {
      this.j = var1;
   }

   public void setOffsetY(int var1) {
      this.n = var1;
   }

   public int getWidth() {
      return this.l;
   }

   public void setMaxWidth(int var1) {
      this.i = var1;
   }

   public int getHeight() {
      return this.m;
   }

   public void b(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int[] var11, int[] var12) {
      this.a(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   public aV n() {
      return this.e();
   }

   public void b(int var1, int var2, int var3, int var4, int var5, int var6, double var7, int var9) {
      this.a(var1, var2, var3, var4, var5, var6, var7, var9);
   }

   public int[] getPixels() {
      return this.k;
   }
}
