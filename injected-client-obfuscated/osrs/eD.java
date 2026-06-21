package osrs;

public class eD {
   public int a;
   public byte[][][] b;

   public eD(int var1) {
      this.a = var1;
   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if (var7 != 0 && this.a != 0 && this.b != null) {
         int var9 = this.a(var8, var7);
         int var10 = this.a(var7);
         aU.a(var1, var2, var5, var6, var3, var4, this.b[var10 - 1][var9], this.a, true);
      }

   }

   public int a(int var1, int var2) {
      if (var2 == 9) {
         var1 = var1 + 1 & 3;
      }

      if (var2 == 10) {
         var1 = var1 + 3 & 3;
      }

      if (var2 == 11) {
         var1 = var1 + 3 & 3;
      }

      return var1;
   }

   public int a(int var1) {
      if (var1 != 9 && var1 != 10) {
         return var1 == 11 ? 8 : var1;
      } else {
         return 1;
      }
   }

   public void a() {
      if (this.b == null) {
         this.b = new byte[8][4][];
         this.b();
         this.c();
         this.d();
         this.e();
         this.f();
         this.g();
         this.h();
         this.i();
      }

   }

   public void b() {
      byte[] var1 = new byte[this.a * this.a];
      int var2 = 0;

      int var4;
      for(int var3 = 0; var3 < this.a; ++var3) {
         for(var4 = 0; var4 < this.a; ++var4) {
            if (var4 <= var3) {
               var1[var2] = -1;
            }

            ++var2;
         }
      }

      this.b[0][0] = var1;
      byte[] var11 = new byte[this.a * this.a];
      var4 = 0;

      int var6;
      for(int var5 = this.a - 1; var5 >= 0; --var5) {
         for(var6 = 0; var6 < this.a; ++var6) {
            if (var6 <= var5) {
               var11[var4] = -1;
            }

            ++var4;
         }
      }

      this.b[0][1] = var11;
      byte[] var12 = new byte[this.a * this.a];
      var6 = 0;

      int var8;
      for(int var7 = 0; var7 < this.a; ++var7) {
         for(var8 = 0; var8 < this.a; ++var8) {
            if (var8 >= var7) {
               var12[var6] = -1;
            }

            ++var6;
         }
      }

      this.b[0][2] = var12;
      byte[] var13 = new byte[this.a * this.a];
      var8 = 0;

      for(int var9 = this.a - 1; var9 >= 0; --var9) {
         for(int var10 = 0; var10 < this.a; ++var10) {
            if (var10 >= var9) {
               var13[var8] = -1;
            }

            ++var8;
         }
      }

      this.b[0][3] = var13;
   }

   public void c() {
      byte[] var1 = new byte[this.a * this.a];
      int var2 = 0;

      int var4;
      for(int var3 = this.a - 1; var3 >= 0; --var3) {
         for(var4 = 0; var4 < this.a; ++var4) {
            if (var4 <= var3 >> 1) {
               var1[var2] = -1;
            }

            ++var2;
         }
      }

      this.b[1][0] = var1;
      byte[] var11 = new byte[this.a * this.a];
      var4 = 0;

      int var6;
      for(int var5 = 0; var5 < this.a; ++var5) {
         for(var6 = 0; var6 < this.a; ++var6) {
            if (var4 >= 0 && var4 < var11.length) {
               if (var6 >= var5 << 1) {
                  var11[var4] = -1;
               }

               ++var4;
            } else {
               ++var4;
            }
         }
      }

      this.b[1][1] = var11;
      byte[] var12 = new byte[this.a * this.a];
      var6 = 0;

      int var8;
      for(int var7 = 0; var7 < this.a; ++var7) {
         for(var8 = this.a - 1; var8 >= 0; --var8) {
            if (var8 <= var7 >> 1) {
               var12[var6] = -1;
            }

            ++var6;
         }
      }

      this.b[1][2] = var12;
      byte[] var13 = new byte[this.a * this.a];
      var8 = 0;

      for(int var9 = this.a - 1; var9 >= 0; --var9) {
         for(int var10 = this.a - 1; var10 >= 0; --var10) {
            if (var10 >= var9 << 1) {
               var13[var8] = -1;
            }

            ++var8;
         }
      }

      this.b[1][3] = var13;
   }

   public void d() {
      byte[] var1 = new byte[this.a * this.a];
      int var2 = 0;

      int var4;
      for(int var3 = this.a - 1; var3 >= 0; --var3) {
         for(var4 = this.a - 1; var4 >= 0; --var4) {
            if (var4 <= var3 >> 1) {
               var1[var2] = -1;
            }

            ++var2;
         }
      }

      this.b[2][0] = var1;
      byte[] var11 = new byte[this.a * this.a];
      var4 = 0;

      int var6;
      for(int var5 = this.a - 1; var5 >= 0; --var5) {
         for(var6 = 0; var6 < this.a; ++var6) {
            if (var6 >= var5 << 1) {
               var11[var4] = -1;
            }

            ++var4;
         }
      }

      this.b[2][1] = var11;
      byte[] var12 = new byte[this.a * this.a];
      var6 = 0;

      int var8;
      for(int var7 = 0; var7 < this.a; ++var7) {
         for(var8 = 0; var8 < this.a; ++var8) {
            if (var8 <= var7 >> 1) {
               var12[var6] = -1;
            }

            ++var6;
         }
      }

      this.b[2][2] = var12;
      byte[] var13 = new byte[this.a * this.a];
      var8 = 0;

      for(int var9 = 0; var9 < this.a; ++var9) {
         for(int var10 = this.a - 1; var10 >= 0; --var10) {
            if (var10 >= var9 << 1) {
               var13[var8] = -1;
            }

            ++var8;
         }
      }

      this.b[2][3] = var13;
   }

   public void e() {
      byte[] var1 = new byte[this.a * this.a];
      int var2 = 0;

      int var4;
      for(int var3 = this.a - 1; var3 >= 0; --var3) {
         for(var4 = 0; var4 < this.a; ++var4) {
            if (var4 >= var3 >> 1) {
               var1[var2] = -1;
            }

            ++var2;
         }
      }

      this.b[3][0] = var1;
      byte[] var11 = new byte[this.a * this.a];
      var4 = 0;

      int var6;
      for(int var5 = 0; var5 < this.a; ++var5) {
         for(var6 = 0; var6 < this.a; ++var6) {
            if (var6 <= var5 << 1) {
               var11[var4] = -1;
            }

            ++var4;
         }
      }

      this.b[3][1] = var11;
      byte[] var12 = new byte[this.a * this.a];
      var6 = 0;

      int var8;
      for(int var7 = 0; var7 < this.a; ++var7) {
         for(var8 = this.a - 1; var8 >= 0; --var8) {
            if (var8 >= var7 >> 1) {
               var12[var6] = -1;
            }

            ++var6;
         }
      }

      this.b[3][2] = var12;
      byte[] var13 = new byte[this.a * this.a];
      var8 = 0;

      for(int var9 = this.a - 1; var9 >= 0; --var9) {
         for(int var10 = this.a - 1; var10 >= 0; --var10) {
            if (var10 <= var9 << 1) {
               var13[var8] = -1;
            }

            ++var8;
         }
      }

      this.b[3][3] = var13;
   }

   public void f() {
      byte[] var1 = new byte[this.a * this.a];
      int var2 = 0;

      int var4;
      for(int var3 = this.a - 1; var3 >= 0; --var3) {
         for(var4 = this.a - 1; var4 >= 0; --var4) {
            if (var4 >= var3 >> 1) {
               var1[var2] = -1;
            }

            ++var2;
         }
      }

      this.b[4][0] = var1;
      byte[] var11 = new byte[this.a * this.a];
      var4 = 0;

      int var6;
      for(int var5 = this.a - 1; var5 >= 0; --var5) {
         for(var6 = 0; var6 < this.a; ++var6) {
            if (var6 <= var5 << 1) {
               var11[var4] = -1;
            }

            ++var4;
         }
      }

      this.b[4][1] = var11;
      byte[] var12 = new byte[this.a * this.a];
      var6 = 0;

      int var8;
      for(int var7 = 0; var7 < this.a; ++var7) {
         for(var8 = 0; var8 < this.a; ++var8) {
            if (var8 >= var7 >> 1) {
               var12[var6] = -1;
            }

            ++var6;
         }
      }

      this.b[4][2] = var12;
      byte[] var13 = new byte[this.a * this.a];
      var8 = 0;

      for(int var9 = 0; var9 < this.a; ++var9) {
         for(int var10 = this.a - 1; var10 >= 0; --var10) {
            if (var10 <= var9 << 1) {
               var13[var8] = -1;
            }

            ++var8;
         }
      }

      this.b[4][3] = var13;
   }

   public void g() {
      byte[] var1 = new byte[this.a * this.a];
      boolean var2 = false;
      byte[] var3 = new byte[this.a * this.a];
      int var4 = 0;

      int var6;
      for(int var5 = 0; var5 < this.a; ++var5) {
         for(var6 = 0; var6 < this.a; ++var6) {
            if (var6 <= this.a / 2) {
               var3[var4] = -1;
            }

            ++var4;
         }
      }

      this.b[5][0] = var3;
      byte[] var13 = new byte[this.a * this.a];
      var6 = 0;

      int var8;
      for(int var7 = 0; var7 < this.a; ++var7) {
         for(var8 = 0; var8 < this.a; ++var8) {
            if (var7 <= this.a / 2) {
               var13[var6] = -1;
            }

            ++var6;
         }
      }

      this.b[5][1] = var13;
      byte[] var14 = new byte[this.a * this.a];
      var8 = 0;

      int var10;
      for(int var9 = 0; var9 < this.a; ++var9) {
         for(var10 = 0; var10 < this.a; ++var10) {
            if (var10 >= this.a / 2) {
               var14[var8] = -1;
            }

            ++var8;
         }
      }

      this.b[5][2] = var14;
      byte[] var15 = new byte[this.a * this.a];
      var10 = 0;

      for(int var11 = 0; var11 < this.a; ++var11) {
         for(int var12 = 0; var12 < this.a; ++var12) {
            if (var11 >= this.a / 2) {
               var15[var10] = -1;
            }

            ++var10;
         }
      }

      this.b[5][3] = var15;
   }

   public void h() {
      byte[] var1 = new byte[this.a * this.a];
      boolean var2 = false;
      byte[] var3 = new byte[this.a * this.a];
      int var4 = 0;

      int var6;
      for(int var5 = 0; var5 < this.a; ++var5) {
         for(var6 = 0; var6 < this.a; ++var6) {
            if (var6 <= var5 - this.a / 2) {
               var3[var4] = -1;
            }

            ++var4;
         }
      }

      this.b[6][0] = var3;
      byte[] var13 = new byte[this.a * this.a];
      var6 = 0;

      int var8;
      for(int var7 = this.a - 1; var7 >= 0; --var7) {
         for(var8 = 0; var8 < this.a; ++var8) {
            if (var8 <= var7 - this.a / 2) {
               var13[var6] = -1;
            }

            ++var6;
         }
      }

      this.b[6][1] = var13;
      byte[] var14 = new byte[this.a * this.a];
      var8 = 0;

      int var10;
      for(int var9 = this.a - 1; var9 >= 0; --var9) {
         for(var10 = this.a - 1; var10 >= 0; --var10) {
            if (var10 <= var9 - this.a / 2) {
               var14[var8] = -1;
            }

            ++var8;
         }
      }

      this.b[6][2] = var14;
      byte[] var15 = new byte[this.a * this.a];
      var10 = 0;

      for(int var11 = 0; var11 < this.a; ++var11) {
         for(int var12 = this.a - 1; var12 >= 0; --var12) {
            if (var12 <= var11 - this.a / 2) {
               var15[var10] = -1;
            }

            ++var10;
         }
      }

      this.b[6][3] = var15;
   }

   public void i() {
      byte[] var1 = new byte[this.a * this.a];
      boolean var2 = false;
      byte[] var3 = new byte[this.a * this.a];
      int var4 = 0;

      int var6;
      for(int var5 = 0; var5 < this.a; ++var5) {
         for(var6 = 0; var6 < this.a; ++var6) {
            if (var6 >= var5 - this.a / 2) {
               var3[var4] = -1;
            }

            ++var4;
         }
      }

      this.b[7][0] = var3;
      byte[] var13 = new byte[this.a * this.a];
      var6 = 0;

      int var8;
      for(int var7 = this.a - 1; var7 >= 0; --var7) {
         for(var8 = 0; var8 < this.a; ++var8) {
            if (var8 >= var7 - this.a / 2) {
               var13[var6] = -1;
            }

            ++var6;
         }
      }

      this.b[7][1] = var13;
      byte[] var14 = new byte[this.a * this.a];
      var8 = 0;

      int var10;
      for(int var9 = this.a - 1; var9 >= 0; --var9) {
         for(var10 = this.a - 1; var10 >= 0; --var10) {
            if (var10 >= var9 - this.a / 2) {
               var14[var8] = -1;
            }

            ++var8;
         }
      }

      this.b[7][2] = var14;
      byte[] var15 = new byte[this.a * this.a];
      var10 = 0;

      for(int var11 = 0; var11 < this.a; ++var11) {
         for(int var12 = this.a - 1; var12 >= 0; --var12) {
            if (var12 >= var11 - this.a / 2) {
               var15[var10] = -1;
            }

            ++var10;
         }
      }

      this.b[7][3] = var15;
   }
}
