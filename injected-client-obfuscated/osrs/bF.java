package osrs;

public class bF {
   public int[] a;
   public byte[] b;
   public int[] c;

   public bF(byte[] var1) {
      int var2 = var1.length;
      this.a = new int[var2];
      this.b = var1;
      int[] var3 = new int[33];
      this.c = new int[8];
      int var4 = 0;

      for(int var5 = 0; var5 < var2; ++var5) {
         byte var6 = var1[var5];
         if (var6 != 0) {
            int var7 = 1 << 32 - var6;
            int var8 = var3[var6];
            this.a[var5] = var8;
            int var9;
            int var10;
            int var11;
            int var12;
            if ((var8 & var7) != 0) {
               var9 = var3[var6 - 1];
            } else {
               var9 = var8 | var7;

               for(var10 = var6 - 1; var10 >= 1; --var10) {
                  var11 = var3[var10];
                  if (var8 != var11) {
                     break;
                  }

                  var12 = 1 << 32 - var10;
                  if ((var11 & var12) != 0) {
                     var3[var10] = var3[var10 - 1];
                     break;
                  }

                  var3[var10] = var11 | var12;
               }
            }

            var3[var6] = var9;

            for(var10 = var6 + 1; var10 <= 32; ++var10) {
               if (var3[var10] == var8) {
                  var3[var10] = var9;
               }
            }

            var10 = 0;

            for(var11 = 0; var11 < var6; ++var11) {
               var12 = Integer.MIN_VALUE >>> var11;
               if ((var8 & var12) != 0) {
                  if (this.c[var10] == 0) {
                     this.c[var10] = var4;
                  }

                  var10 = this.c[var10];
               } else {
                  ++var10;
               }

               if (var10 >= this.c.length) {
                  int[] var13 = new int[this.c.length * 2];

                  for(int var14 = 0; var14 < this.c.length; ++var14) {
                     var13[var14] = this.c[var14];
                  }

                  this.c = var13;
               }

               int var15 = var12 >>> 1;
            }

            this.c[var10] = ~var5;
            if (var10 >= var4) {
               var4 = var10 + 1;
            }
         }
      }

   }

   public int a(byte[] var1, int var2, int var3, byte[] var4, int var5) {
      int var6 = 0;
      int var7 = var5 << 3;

      for(int var8 = var2 + var3; var2 < var8; ++var2) {
         int var9 = var1[var2] & 255;
         int var10 = this.a[var9];
         byte var11 = this.b[var9];
         if (var11 == 0) {
            throw new RuntimeException("" + var9);
         }

         int var12 = var7 >> 3;
         int var13 = var7 & 7;
         int var14 = var6 & -var13 >> 31;
         int var15 = (var11 + var13 - 1 >> 3) + var12;
         var13 += 24;
         var4[var12] = (byte)(var6 = var14 | var10 >>> var13);
         if (var12 < var15) {
            ++var12;
            var13 -= 8;
            var4[var12] = (byte)(var6 = var10 >>> var13);
            if (var12 < var15) {
               ++var12;
               var13 -= 8;
               var4[var12] = (byte)(var6 = var10 >>> var13);
               if (var12 < var15) {
                  ++var12;
                  var13 -= 8;
                  var4[var12] = (byte)(var6 = var10 >>> var13);
                  if (var12 < var15) {
                     ++var12;
                     var13 -= 8;
                     var4[var12] = (byte)(var6 = var10 << -var13);
                  }
               }
            }
         }

         var7 += var11;
      }

      return (var7 + 7 >> 3) - var5;
   }

   public int a(byte[] var1, int var2, byte[] var3, int var4, int var5) {
      if (var5 == 0) {
         return 0;
      } else {
         int var6 = 0;
         int var7 = var4 + var5;
         int var8 = var2;

         while(true) {
            byte var9 = var1[var8];
            if (var9 < 0) {
               var6 = this.c[var6];
            } else {
               ++var6;
            }

            int var10;
            if ((var10 = this.c[var6]) < 0) {
               var3[var4++] = (byte)(~var10);
               if (var4 >= var7) {
                  break;
               }

               var6 = 0;
            }

            if ((var9 & 64) != 0) {
               var6 = this.c[var6];
            } else {
               ++var6;
            }

            int var11;
            if ((var11 = this.c[var6]) < 0) {
               var3[var4++] = (byte)(~var11);
               if (var4 >= var7) {
                  break;
               }

               var6 = 0;
            }

            if ((var9 & 32) != 0) {
               var6 = this.c[var6];
            } else {
               ++var6;
            }

            int var12;
            if ((var12 = this.c[var6]) < 0) {
               var3[var4++] = (byte)(~var12);
               if (var4 >= var7) {
                  break;
               }

               var6 = 0;
            }

            if ((var9 & 16) != 0) {
               var6 = this.c[var6];
            } else {
               ++var6;
            }

            int var13;
            if ((var13 = this.c[var6]) < 0) {
               var3[var4++] = (byte)(~var13);
               if (var4 >= var7) {
                  break;
               }

               var6 = 0;
            }

            if ((var9 & 8) != 0) {
               var6 = this.c[var6];
            } else {
               ++var6;
            }

            int var14;
            if ((var14 = this.c[var6]) < 0) {
               var3[var4++] = (byte)(~var14);
               if (var4 >= var7) {
                  break;
               }

               var6 = 0;
            }

            if ((var9 & 4) != 0) {
               var6 = this.c[var6];
            } else {
               ++var6;
            }

            int var15;
            if ((var15 = this.c[var6]) < 0) {
               var3[var4++] = (byte)(~var15);
               if (var4 >= var7) {
                  break;
               }

               var6 = 0;
            }

            if ((var9 & 2) != 0) {
               var6 = this.c[var6];
            } else {
               ++var6;
            }

            int var16;
            if ((var16 = this.c[var6]) < 0) {
               var3[var4++] = (byte)(~var16);
               if (var4 >= var7) {
                  break;
               }

               var6 = 0;
            }

            if ((var9 & 1) != 0) {
               var6 = this.c[var6];
            } else {
               ++var6;
            }

            int var17;
            if ((var17 = this.c[var6]) < 0) {
               var3[var4++] = (byte)(~var17);
               if (var4 >= var7) {
                  break;
               }

               var6 = 0;
            }

            ++var8;
         }

         return var8 + 1 - var2;
      }
   }
}
