package osrs;

import net.runelite.api.IndexedSprite;

public final class hX extends aU implements IndexedSprite {
   public int i;
   public int j;
   public int k;
   public int l;
   public int m;
   public int n;
   public byte[] o;
   public int[] p;

   public static void a(int[] var0, byte[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = -(var5 >> 2);
      int var10 = -(var5 & 3);

      for(int var11 = -var6; var11 < 0; ++var11) {
         int var12;
         byte var13;
         for(var12 = var9; var12 < 0; ++var12) {
            var13 = var1[var3++];
            if (var13 != 0) {
               var0[var4++] = var2[var13 & 255] | -16777216;
            } else {
               ++var4;
            }

            byte var14 = var1[var3++];
            if (var14 != 0) {
               var0[var4++] = var2[var14 & 255] | -16777216;
            } else {
               ++var4;
            }

            byte var15 = var1[var3++];
            if (var15 != 0) {
               var0[var4++] = var2[var15 & 255] | -16777216;
            } else {
               ++var4;
            }

            byte var16 = var1[var3++];
            if (var16 != 0) {
               var0[var4++] = var2[var16 & 255] | -16777216;
            } else {
               ++var4;
            }
         }

         for(var12 = var10; var12 < 0; ++var12) {
            var13 = var1[var3++];
            if (var13 != 0) {
               var0[var4++] = var2[var13 & 255] | -16777216;
            } else {
               ++var4;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   public static void a(int[] var0, byte[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      int var12 = var3;

      for(int var13 = -var8; var13 < 0; ++var13) {
         int var14 = (var4 >> 16) * var11;

         for(int var15 = -var7; var15 < 0; ++var15) {
            byte var16 = var1[(var3 >> 16) + var14];
            if (var16 != 0) {
               var0[var5++] = var2[var16 & 255] | -16777216;
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

   public void e() {
      if (this.j != this.i || this.n != this.k) {
         byte[] var1 = new byte[this.n * this.j];
         int var2 = 0;

         for(int var3 = 0; var3 < this.k; ++var3) {
            for(int var4 = 0; var4 < this.i; ++var4) {
               var1[(this.l + var3) * this.j + this.m + var4] = this.o[var2++];
            }
         }

         this.o = var1;
         this.i = this.j;
         this.k = this.n;
         this.m = 0;
         this.l = 0;
      }

   }

   public void c(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.p.length; ++var4) {
         int var5 = this.p[var4] >> 16 & 255;
         int var6 = var1 + var5;
         if (var6 < 0) {
            var6 = 0;
         } else if (var6 > 255) {
            var6 = 255;
         }

         int var7 = this.p[var4] >> 8 & 255;
         int var8 = var2 + var7;
         if (var8 < 0) {
            var8 = 0;
         } else if (var8 > 255) {
            var8 = 255;
         }

         int var9 = this.p[var4] & 255;
         int var10 = var3 + var9;
         if (var10 < 0) {
            var10 = 0;
         } else if (var10 > 255) {
            var10 = 255;
         }

         this.p[var4] = (var6 << 16) + (var8 << 8) + var10;
      }

   }

   public void a(int var1, int var2) {
      int var3 = this.m + var1;
      int var4 = this.l + var2;
      int var5 = aU.f * var4 + var3;
      int var6 = 0;
      int var7 = this.k;
      int var8 = this.i;
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
         a(aU.h, this.o, this.p, var6, var5, var8, var7, var9, var10);
      }

   }

   public void f(int var1, int var2, int var3, int var4) {
      int var5 = this.i;
      int var6 = this.k;
      int var7 = 0;
      int var8 = 0;
      int var9 = this.j;
      int var10 = this.n;
      int var11 = (var9 << 16) / var3;
      int var12 = (var10 << 16) / var4;
      int var13;
      if (this.m > 0) {
         var13 = ((this.m << 16) + var11 - 1) / var11;
         var1 += var13;
         var7 += var11 * var13 - (this.m << 16);
      }

      if (this.l > 0) {
         var13 = ((this.l << 16) + var12 - 1) / var12;
         var2 += var13;
         var8 += var12 * var13 - (this.l << 16);
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

      a(aU.h, this.o, this.p, var7, var8, var13, var14, var3, var4, var11, var12, var5);
   }

   public void g(int var1, int var2, int var3, int var4) {
      this.f(var1, var2, var3, var4);
   }

   public int getHeight() {
      return this.k;
   }

   public void setPalette(int[] var1) {
      this.p = var1;
   }

   public void setWidth(int var1) {
      this.i = var1;
   }

   public int getWidth() {
      return this.i;
   }

   public void setOffsetX(int var1) {
      this.m = var1;
   }

   public void setOriginalWidth(int var1) {
      this.j = var1;
   }

   public void setOriginalHeight(int var1) {
      this.n = var1;
   }

   public int getOffsetY() {
      return this.l;
   }

   public int getOriginalWidth() {
      return this.j;
   }

   public void setPixels(byte[] var1) {
      this.o = var1;
   }

   public byte[] getPixels() {
      return this.o;
   }

   public void setOffsetY(int var1) {
      this.l = var1;
   }

   public int getOriginalHeight() {
      return this.n;
   }

   public void setHeight(int var1) {
      this.k = var1;
   }

   public int getOffsetX() {
      return this.m;
   }

   public int[] getPalette() {
      return this.p;
   }
}
