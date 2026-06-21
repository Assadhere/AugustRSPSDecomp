package osrs;

public class bR {
   public static float[][] a = new float[2][8];
   public static int[][] b = new int[2][8];
   public int[] c = new int[2];
   public int[][][] d = new int[2][2][4];
   public int[][][] e = new int[2][2][4];
   public int[] f = new int[2];

   public static float a(float var0, int var1) {
      float var2 = 32.703197F * (float)Math.pow(2.0, (double)var0);
      return var2 * 3.1415927F / (float)(var1 / 2);
   }

   public float a(int var1, int var2, float var3) {
      float var4 = (float)(this.e[var1][1][var2] - this.e[var1][0][var2]) * var3 + (float)this.e[var1][0][var2];
      float var5 = var4 * 0.0015258789F;
      return 1.0F - (float)Math.pow(10.0, (double)(-var5 / 20.0F));
   }

   public float a(int var1, int var2, float var3, int var4) {
      float var5 = (float)(this.d[var1][1][var2] - this.d[var1][0][var2]) * var3 + (float)this.d[var1][0][var2];
      float var6 = var5 * 1.2207031E-4F;
      return a(var6, var4);
   }

   public int a(int var1, float var2, int var3) {
      float var4;
      if (var1 == 0) {
         var4 = (float)(this.f[1] - this.f[0]) * var2 + (float)this.f[0];
         float var5 = var4 * 0.0030517578F;
         bo.bC = (float)Math.pow(0.1, (double)(var5 / 20.0F));
         bo.bE = (int)(bo.bC * 65536.0F);
      }

      if (this.c[var1] == 0) {
         return 0;
      } else {
         var4 = this.a(var1, 0, var2);
         a[var1][0] = var4 * -2.0F * (float)Math.cos((double)this.a(var1, 0, var2, var3));
         a[var1][1] = var4 * var4;

         int var6;
         float[] var11;
         for(var6 = 1; var6 < this.c[var1]; ++var6) {
            float var7 = this.a(var1, var6, var2);
            float var8 = var7 * -2.0F * (float)Math.cos((double)this.a(var1, var6, var2, var3));
            float var9 = var7 * var7;
            a[var1][var6 * 2 + 1] = a[var1][var6 * 2 - 1] * var9;
            a[var1][var6 * 2] = a[var1][var6 * 2 - 1] * var8 + a[var1][var6 * 2 - 2] * var9;

            for(int var10 = var6 * 2 - 1; var10 >= 2; --var10) {
               var11 = a[var1];
               var11[var10] += a[var1][var10 - 1] * var8 + a[var1][var10 - 2] * var9;
            }

            var11 = a[var1];
            var11[1] += a[var1][0] * var8 + var9;
            var11 = a[var1];
            var11[0] += var8;
         }

         if (var1 == 0) {
            for(var6 = 0; var6 < this.c[0] * 2; ++var6) {
               var11 = a[0];
               var11[var6] *= bo.bC;
            }
         }

         for(var6 = 0; var6 < this.c[var1] * 2; ++var6) {
            b[var1][var6] = (int)(a[var1][var6] * 65536.0F);
         }

         return this.c[var1] * 2;
      }
   }

   public void a(aR var1, iI var2) {
      int var3 = var1.b();
      this.c[0] = var3 >> 4;
      this.c[1] = var3 & 15;
      if (var3 != 0) {
         this.f[0] = var1.d();
         this.f[1] = var1.d();
         int var4 = var1.b();

         int var5;
         int var6;
         for(var5 = 0; var5 < 2; ++var5) {
            for(var6 = 0; var6 < this.c[var5]; ++var6) {
               this.d[var5][0][var6] = var1.d();
               this.e[var5][0][var6] = var1.d();
            }
         }

         for(var5 = 0; var5 < 2; ++var5) {
            for(var6 = 0; var6 < this.c[var5]; ++var6) {
               if ((var4 & 1 << var5 * 4 << var6) != 0) {
                  this.d[var5][1][var6] = var1.d();
                  this.e[var5][1][var6] = var1.d();
               } else {
                  this.d[var5][1][var6] = this.d[var5][0][var6];
                  this.e[var5][1][var6] = this.e[var5][0][var6];
               }
            }
         }

         if (var4 != 0 || this.f[0] != this.f[1]) {
            var2.b(var1);
         }
      } else {
         int[] var7 = this.f;
         this.f[1] = 0;
         var7[0] = 0;
      }

   }
}
