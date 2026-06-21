package osrs;

import java.util.Arrays;

public class ko {
   public boolean a = false;
   public int[][][] b = new int[4][13][13];
   public int[] c;
   public int[][] d;

   public static int a(int var0, int var1) {
      return (var0 << 8) + var1;
   }

   public int a() {
      return this.c.length;
   }

   public void b(int var1, int var2) {
      this.a = false;
      int var3 = B.a(var1 - 6);
      int var4 = B.a(var1 + 6);
      int var5 = B.a(var2 - 6);
      int var6 = B.a(var2 + 6);
      int var7 = (var4 - var3 + 1) * (var6 - var5 + 1);
      this.c = new int[var7];
      int var8 = 0;

      for(int var9 = var3; var9 <= var4; ++var9) {
         for(int var10 = var5; var10 <= var6; ++var10) {
            this.c[var8] = a(var9, var10);
            ++var8;
         }
      }

   }

   public void a(bu var1) {
      this.a(var1, 4, 13, 13);
   }

   public void a(bu var1, int var2, int var3) {
      this.a(var1, 4, var2, var3);
   }

   public void a(bu var1, int var2, int var3, int var4) {
      this.a = true;
      int var5 = var1.d();
      var1.am();

      int var6;
      int var7;
      int var8;
      int var9;
      for(var6 = 0; var6 < var2; ++var6) {
         for(var7 = 0; var7 < var3; ++var7) {
            for(var8 = 0; var8 < var4; ++var8) {
               var9 = var1.y(1);
               if (var9 == 1) {
                  this.b[var6][var7][var8] = var1.y(26);
               } else {
                  this.b[var6][var7][var8] = -1;
               }
            }
         }
      }

      var1.an();
      this.c = new int[var5];
      Arrays.fill(this.c, -1);
      var6 = 0;

      for(var7 = 0; var7 < var2; ++var7) {
         for(var8 = 0; var8 < var3; ++var8) {
            for(var9 = 0; var9 < var4; ++var9) {
               int var10 = this.b[var7][var8][var9];
               if (var10 != -1) {
                  int var11 = bo.a(var10);
                  int var12 = jO.a(var10);
                  int var13 = B.a(var11);
                  int var14 = B.a(var12);
                  int var15 = a(var13, var14);

                  for(int var16 = 0; var16 < var6; ++var16) {
                     if (this.c[var16] == var15) {
                        var15 = -1;
                        break;
                     }
                  }

                  if (var15 != -1) {
                     this.c[var6] = var15;
                     ++var6;
                  }
               }
            }
         }
      }

   }

   @Deprecated
   public void b(bu var1, int var2, int var3) {
      this.a = false;
      int var4 = var1.d();
      this.d = new int[var4][4];

      int var5;
      int var6;
      for(var5 = 0; var5 < var4; ++var5) {
         for(var6 = 0; var6 < 4; ++var6) {
            this.d[var5][var6] = var1.h();
         }
      }

      this.c = new int[var4];
      Arrays.fill(this.c, -1);
      var5 = 0;

      for(var6 = (var2 - 6) / 8; var6 <= (var2 + 6) / 8; ++var6) {
         for(int var7 = (var3 - 6) / 8; var7 <= (var3 + 6) / 8; ++var7) {
            int var8 = (var6 << 8) + var7;
            this.c[var5] = var8;
            ++var5;
         }
      }

   }

   @Deprecated
   public void b(bu var1) {
      this.b(var1, 4, 13, 13);
   }

   @Deprecated
   public void c(bu var1, int var2, int var3) {
      this.b(var1, 4, var2, var3);
   }

   @Deprecated
   public void b(bu var1, int var2, int var3, int var4) {
      this.a = true;
      int var5 = var1.d();
      var1.am();

      int var6;
      int var7;
      int var8;
      int var9;
      for(var6 = 0; var6 < var2; ++var6) {
         for(var7 = 0; var7 < var3; ++var7) {
            for(var8 = 0; var8 < var4; ++var8) {
               var9 = var1.y(1);
               if (var9 == 1) {
                  this.b[var6][var7][var8] = var1.y(26);
               } else {
                  this.b[var6][var7][var8] = -1;
               }
            }
         }
      }

      var1.an();
      this.d = new int[var5][4];

      for(var6 = 0; var6 < var5; ++var6) {
         for(var7 = 0; var7 < 4; ++var7) {
            this.d[var6][var7] = var1.h();
         }
      }

      this.c = new int[var5];
      Arrays.fill(this.c, -1);
      var6 = 0;

      for(var7 = 0; var7 < var2; ++var7) {
         for(var8 = 0; var8 < var3; ++var8) {
            for(var9 = 0; var9 < var4; ++var9) {
               int var10 = this.b[var7][var8][var9];
               if (var10 != -1) {
                  int var11 = bo.a(var10);
                  int var12 = jO.a(var10);
                  int var13 = B.a(var11);
                  int var14 = B.a(var12);
                  int var15 = a(var13, var14);

                  for(int var16 = 0; var16 < var6; ++var16) {
                     if (this.c[var16] == var15) {
                        var15 = -1;
                        break;
                     }
                  }

                  if (var15 != -1) {
                     this.c[var6] = var15;
                     ++var6;
                  }
               }
            }
         }
      }

   }
}
