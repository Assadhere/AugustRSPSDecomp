package osrs;

public class iE {
   public int a;
   public int b;
   public int c;
   public int d;
   public int e;
   public int f;
   public int[] g;

   public iE(ch var1) {
      this.a = var1.a(16);
      this.b = var1.a(24);
      this.c = var1.a(24);
      this.d = var1.a(24) + 1;
      this.e = var1.a(6) + 1;
      this.f = var1.a(8);
      int[] var2 = new int[this.e];

      int var3;
      for(var3 = 0; var3 < this.e; ++var3) {
         int var4 = 0;
         int var5 = var1.a(3);
         boolean var6 = var1.a() != 0;
         if (var6) {
            var4 = var1.a(5);
         }

         var2[var3] = var4 << 3 | var5;
      }

      this.g = new int[this.e * 8];

      for(var3 = 0; var3 < this.e * 8; ++var3) {
         this.g[var3] = (var2[var3 >> 3] & 1 << (var3 & 7)) != 0 ? var1.a(8) : -1;
      }

   }

   public void a(float[] var1, int var2, boolean var3, ch var4, ca[] var5) {
      int var6;
      for(var6 = 0; var6 < var2; ++var6) {
         var1[var6] = 0.0F;
      }

      if (!var3) {
         var6 = var5[this.f].a;
         int var7 = this.c - this.b;
         int var8 = var7 / this.d;
         int[] var9 = new int[var8];

         for(int var10 = 0; var10 < 8; ++var10) {
            int var11 = 0;

            while(var11 < var8) {
               int var12;
               int var13;
               if (var10 == 0) {
                  var12 = var5[this.f].a(var4);

                  for(var13 = var6 - 1; var13 >= 0; --var13) {
                     if (var11 + var13 < var8) {
                        var9[var11 + var13] = var12 % this.e;
                     }

                     var12 /= this.e;
                  }
               }

               for(var12 = 0; var12 < var6; ++var12) {
                  var13 = var9[var11];
                  int var14 = this.g[var13 * 8 + var10];
                  if (var14 >= 0) {
                     int var15 = this.d * var11 + this.b;
                     ca var16 = var5[var14];
                     int var17;
                     if (this.a == 0) {
                        var17 = this.d / var16.a;

                        for(int var21 = 0; var21 < var17; ++var21) {
                           float[] var22 = var16.b(var4);

                           for(int var20 = 0; var20 < var16.a; ++var20) {
                              var1[var17 * var20 + var15 + var21] += var22[var20];
                           }
                        }
                     } else {
                        var17 = 0;

                        while(var17 < this.d) {
                           float[] var18 = var16.b(var4);

                           for(int var19 = 0; var19 < var16.a; ++var19) {
                              var1[var15 + var17] += var18[var19];
                              ++var17;
                           }
                        }
                     }
                  }

                  ++var11;
                  if (var11 >= var8) {
                     break;
                  }
               }
            }
         }
      }

   }
}
