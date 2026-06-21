package osrs;

public class bS extends az {
   public byte[][] a;
   public int b;
   public int c;
   public int d;
   public int e;
   public boolean f;
   public float[] g;
   public int h;
   public int i;
   public boolean j;
   public float[] k;
   public short[] l;
   public int m;
   public int n;
   public static ch o = new ch();
   public static boolean p = false;

   public bS(byte[] var1) {
      this.b(var1);
   }

   public static float a(int var0) {
      int var1 = var0 & 2097151;
      int var2 = var0 & Integer.MIN_VALUE;
      int var3 = (var0 & 2145386496) >> 21;
      if (var2 != 0) {
         var1 = -var1;
      }

      return (float)((double)var1 * Math.pow(2.0, (double)(var3 - 788)));
   }

   public static void a(byte[] var0) {
      ch var1 = o;
      var1.a(var0, 0);
      bo.av = 1 << var1.a(4);
      bo.ao = 1 << var1.a(4);

      int var2;
      int var3;
      int var4;
      int var5;
      int var6;
      int var8;
      for(var2 = 0; var2 < 2; ++var2) {
         var3 = var2 != 0 ? bo.ao : bo.av;
         var4 = var3 >> 1;
         var5 = var3 >> 2;
         var6 = var3 >> 3;
         float[] var7 = new float[var4];

         for(var8 = 0; var8 < var5; ++var8) {
            var7[var8 * 2] = (float)Math.cos((double)(var8 * 4) * Math.PI / (double)var3);
            var7[var8 * 2 + 1] = -((float)Math.sin((double)(var8 * 4) * Math.PI / (double)var3));
         }

         float[] var17 = new float[var4];

         for(int var9 = 0; var9 < var5; ++var9) {
            var17[var9 * 2] = (float)Math.cos((double)(var9 * 2 + 1) * Math.PI / (double)(var3 * 2));
            var17[var9 * 2 + 1] = (float)Math.sin((double)(var9 * 2 + 1) * Math.PI / (double)(var3 * 2));
         }

         float[] var18 = new float[var5];

         for(int var10 = 0; var10 < var6; ++var10) {
            var18[var10 * 2] = (float)Math.cos((double)(var10 * 4 + 2) * Math.PI / (double)var3);
            var18[var10 * 2 + 1] = -((float)Math.sin((double)(var10 * 4 + 2) * Math.PI / (double)var3));
         }

         int[] var19 = new int[var6];
         int var11 = ks.d(var6 - 1);

         for(int var12 = 0; var12 < var6; ++var12) {
            int var13 = var12;
            int var14 = var11;

            int var15;
            for(var15 = 0; var14 > 0; --var14) {
               var15 = var15 << 1 | var13 & 1;
               var13 >>>= 1;
            }

            var19[var12] = var15;
         }

         if (var2 != 0) {
            bo.aw = var7;
            bo.at = var17;
            bo.ax = var18;
            bo.ay = var19;
         } else {
            bo.ap = var7;
            bo.aq = var17;
            bo.ar = var18;
            bo.au = var19;
         }
      }

      var2 = var1.a(8) + 1;
      bo.af = new ca[var2];

      for(var3 = 0; var3 < var2; ++var3) {
         bo.af[var3] = new ca(o);
      }

      var3 = var1.a(6) + 1;

      for(var4 = 0; var4 < var3; ++var4) {
         var1.a(16);
      }

      var4 = var1.a(6) + 1;
      bo.aj = new iy[var4];

      for(var5 = 0; var5 < var4; ++var5) {
         bo.aj[var5] = new iy(o);
      }

      var5 = var1.a(6) + 1;
      bo.ak = new iE[var5];

      for(var6 = 0; var6 < var5; ++var6) {
         bo.ak[var6] = new iE(o);
      }

      var6 = var1.a(6) + 1;
      bo.al = new cd[var6];

      int var16;
      for(var16 = 0; var16 < var6; ++var16) {
         bo.al[var16] = new cd(o);
      }

      var16 = var1.a(6) + 1;
      bo.an = new boolean[var16];
      bo.ah = new int[var16];

      for(var8 = 0; var8 < var16; ++var8) {
         bo.an[var8] = var1.a() != 0;
         var1.a(16);
         var1.a(16);
         bo.ah[var8] = var1.a(8);
      }

   }

   public static boolean a(au var0) {
      if (!p) {
         byte[] var1 = var0.b(0, (int)0);
         if (var1 == null) {
            return false;
         }

         a(var1);
         p = true;
      }

      return true;
   }

   public static bS a(au var0, int var1, int var2) {
      if (!a(var0)) {
         var0.a(var1, var2);
         return null;
      } else {
         byte[] var3 = var0.b(var1, var2);
         return var3 == null ? null : new bS(var3);
      }
   }

   public void b(byte[] var1) {
      aR var2 = new aR(var1);
      this.b = var2.h();
      this.c = var2.h();
      this.d = var2.h();
      this.e = var2.h();
      if (this.e < 0) {
         this.e = ~this.e;
         this.f = true;
      }

      int var3 = var2.h();
      this.a = new byte[var3][];

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = 0;

         int var6;
         do {
            var6 = var2.b();
            var5 += var6;
         } while(var6 >= 255);

         byte[] var7 = new byte[var5];
         var2.c(var7, 0, var5);
         this.a[var4] = var7;
      }

   }

   public float[] b(int var1) {
      ch var2 = new ch();
      var2.a(this.a[var1], 0);
      this.k = new float[bo.ao];
      var2.a();
      int var3 = var2.a(ks.d(bo.ah.length - 1));
      boolean var4 = bo.an[var3];
      int var5 = var4 ? bo.ao : bo.av;
      boolean var6 = false;
      boolean var7 = false;
      if (var4) {
         var6 = var2.a() != 0;
         var7 = var2.a() != 0;
      }

      int var8 = var5 >> 1;
      int var9;
      int var10;
      int var11;
      if (var4 && !var6) {
         var9 = (var5 >> 2) - (bo.av >> 2);
         var10 = (bo.av >> 2) + (var5 >> 2);
         var11 = bo.av >> 1;
      } else {
         var9 = 0;
         var10 = var8;
         var11 = var5 >> 1;
      }

      int var12;
      int var13;
      int var14;
      if (var4 && !var7) {
         var12 = var5 - (var5 >> 2) - (bo.av >> 2);
         var13 = (bo.av >> 2) + (var5 - (var5 >> 2));
         var14 = bo.av >> 1;
      } else {
         var12 = var8;
         var13 = var5;
         var14 = var5 >> 1;
      }

      cd var15 = bo.al[bo.ah[var3]];
      int var16 = var15.b;
      int var17 = var15.c[var16];
      bU var18 = bo.aj[var17].a(var2, bo.af);
      boolean var19 = !var18.a();

      int var20;
      for(var20 = 0; var20 < var15.a; ++var20) {
         iE var21 = bo.ak[var15.d[var20]];
         float[] var22 = this.k;
         var21.a(var22, var5 >> 1, var19, var2, bo.af);
      }

      if (var18.a()) {
         var20 = var15.b;
         int var10000 = var15.c[var20];
         var18.a(this.k, var5 >> 1);
      }

      int var43;
      int var46;
      if (!var18.a()) {
         for(var20 = var5 >> 1; var20 < var5; ++var20) {
            this.k[var20] = 0.0F;
         }
      } else {
         var20 = var5 >> 1;
         var43 = var5 >> 2;
         var46 = var5 >> 3;
         float[] var23 = this.k;

         int var24;
         for(var24 = 0; var24 < var20; ++var24) {
            var23[var24] *= 0.5F;
         }

         for(var24 = var20; var24 < var5; ++var24) {
            var23[var24] = -var23[var5 - var24 - 1];
         }

         float[] var48 = var4 ? bo.aw : bo.ap;
         float[] var25 = var4 ? bo.at : bo.aq;
         float[] var26 = var4 ? bo.ax : bo.ar;
         int[] var27 = var4 ? bo.ay : bo.au;

         int var28;
         float var29;
         float var30;
         float var31;
         float var32;
         for(var28 = 0; var28 < var43; ++var28) {
            var29 = var23[var28 * 4] - var23[var5 - var28 * 4 - 1];
            var30 = var23[var28 * 4 + 2] - var23[var5 - var28 * 4 - 3];
            var31 = var48[var28 * 2];
            var32 = var48[var28 * 2 + 1];
            var23[var5 - var28 * 4 - 1] = var29 * var31 - var30 * var32;
            var23[var5 - var28 * 4 - 3] = var29 * var32 + var30 * var31;
         }

         float var33;
         float var34;
         for(var28 = 0; var28 < var46; ++var28) {
            var29 = var23[var28 * 4 + var20 + 3];
            var30 = var23[var28 * 4 + var20 + 1];
            var31 = var23[var28 * 4 + 3];
            var32 = var23[var28 * 4 + 1];
            var23[var28 * 4 + var20 + 3] = var29 + var31;
            var23[var28 * 4 + var20 + 1] = var30 + var32;
            var33 = var48[var20 - 4 - var28 * 4];
            var34 = var48[var20 - 3 - var28 * 4];
            var23[var28 * 4 + 3] = (var29 - var31) * var33 - (var30 - var32) * var34;
            var23[var28 * 4 + 1] = (var29 - var31) * var34 + (var30 - var32) * var33;
         }

         var28 = ks.d(var5 - 1);

         float var37;
         int var49;
         int var50;
         int var51;
         int var52;
         for(var49 = 0; var49 < var28 - 3; ++var49) {
            var50 = var5 >> var49 + 2;
            var51 = 8 << var49;

            for(var52 = 0; var52 < 2 << var49; ++var52) {
               int var53 = var5 - var50 * 2 * var52;
               int var54 = var5 - (var52 * 2 + 1) * var50;

               for(int var35 = 0; var35 < var5 >> var49 + 4; ++var35) {
                  int var36 = var35 * 4;
                  var37 = var23[var53 - 1 - var36];
                  float var38 = var23[var53 - 3 - var36];
                  float var39 = var23[var54 - 1 - var36];
                  float var40 = var23[var54 - 3 - var36];
                  var23[var53 - 1 - var36] = var37 + var39;
                  var23[var53 - 3 - var36] = var38 + var40;
                  float var41 = var48[var51 * var35];
                  float var42 = var48[var51 * var35 + 1];
                  var23[var54 - 1 - var36] = (var37 - var39) * var41 - (var38 - var40) * var42;
                  var23[var54 - 3 - var36] = (var37 - var39) * var42 + (var38 - var40) * var41;
               }
            }
         }

         float var55;
         float var56;
         for(var49 = 1; var49 < var46 - 1; ++var49) {
            var50 = var27[var49];
            if (var49 < var50) {
               var51 = var49 * 8;
               var52 = var50 * 8;
               var33 = var23[var51 + 1];
               var23[var51 + 1] = var23[var52 + 1];
               var23[var52 + 1] = var33;
               var34 = var23[var51 + 3];
               var23[var51 + 3] = var23[var52 + 3];
               var23[var52 + 3] = var34;
               var55 = var23[var51 + 5];
               var23[var51 + 5] = var23[var52 + 5];
               var23[var52 + 5] = var55;
               var56 = var23[var51 + 7];
               var23[var51 + 7] = var23[var52 + 7];
               var23[var52 + 7] = var56;
            }
         }

         for(var49 = 0; var49 < var20; ++var49) {
            var23[var49] = var23[var49 * 2 + 1];
         }

         for(var49 = 0; var49 < var46; ++var49) {
            var23[var5 - 1 - var49 * 2] = var23[var49 * 4];
            var23[var5 - 2 - var49 * 2] = var23[var49 * 4 + 1];
            var23[var5 - var43 - 1 - var49 * 2] = var23[var49 * 4 + 2];
            var23[var5 - var43 - 2 - var49 * 2] = var23[var49 * 4 + 3];
         }

         for(var49 = 0; var49 < var46; ++var49) {
            var30 = var26[var49 * 2];
            var31 = var26[var49 * 2 + 1];
            var32 = var23[var49 * 2 + var20];
            var33 = var23[var49 * 2 + var20 + 1];
            var34 = var23[var5 - 2 - var49 * 2];
            var55 = var23[var5 - 1 - var49 * 2];
            var56 = (var32 - var34) * var31 + (var33 + var55) * var30;
            var23[var49 * 2 + var20] = (var32 + var34 + var56) * 0.5F;
            var23[var5 - 2 - var49 * 2] = (var32 + var34 - var56) * 0.5F;
            var37 = (var33 + var55) * var31 - (var32 - var34) * var30;
            var23[var49 * 2 + var20 + 1] = (var33 - var55 + var37) * 0.5F;
            var23[var5 - 1 - var49 * 2] = (-var33 + var55 + var37) * 0.5F;
         }

         for(var49 = 0; var49 < var43; ++var49) {
            var23[var49] = var25[var49 * 2] * var23[var49 * 2 + var20] + var25[var49 * 2 + 1] * var23[var49 * 2 + 1 + var20];
            var23[var20 - 1 - var49] = var23[var49 * 2 + var20] * var25[var49 * 2 + 1] - var25[var49 * 2] * var23[var49 * 2 + 1 + var20];
         }

         for(var49 = 0; var49 < var43; ++var49) {
            var23[var5 - var43 + var49] = -var23[var49];
         }

         for(var49 = 0; var49 < var43; ++var49) {
            var23[var49] = var23[var43 + var49];
         }

         for(var49 = 0; var49 < var43; ++var49) {
            var23[var43 + var49] = -var23[var43 - var49 - 1];
         }

         for(var49 = 0; var49 < var43; ++var49) {
            var23[var20 + var49] = var23[var5 - var49 - 1];
         }

         float[] var57;
         for(var50 = var9; var50 < var10; ++var50) {
            var31 = (float)Math.sin(((double)(var50 - var9) + 0.5) / (double)var11 * 0.5 * Math.PI);
            var57 = this.k;
            var57[var50] *= (float)Math.sin((double)var31 * 1.5707963267948966 * (double)var31);
         }

         for(var50 = var12; var50 < var13; ++var50) {
            var31 = (float)Math.sin(((double)(var50 - var12) + 0.5) / (double)var14 * 0.5 * Math.PI + 1.5707963267948966);
            var57 = this.k;
            var57[var50] *= (float)Math.sin((double)var31 * 1.5707963267948966 * (double)var31);
         }
      }

      float[] var44 = null;
      if (this.h > 0) {
         var43 = this.h + var5 >> 2;
         var44 = new float[var43];
         int var47;
         if (!this.j) {
            for(var46 = 0; var46 < this.i; ++var46) {
               var47 = (this.h >> 1) + var46;
               var44[var46] += this.g[var47];
            }
         }

         if (var18.a()) {
            for(var46 = var9; var46 < var5 >> 1; ++var46) {
               var47 = var44.length - (var5 >> 1) + var46;
               var44[var47] += this.k[var46];
            }
         }
      }

      float[] var45 = this.g;
      this.g = this.k;
      this.k = var45;
      this.h = var5;
      this.i = var13 - (var5 >> 1);
      this.j = !var18.a();
      return var44;
   }

   public ix a(int[] var1) {
      if (var1 != null && var1[0] <= 0) {
         return null;
      } else {
         if (this.l == null) {
            this.h = 0;
            this.g = new float[bo.ao];
            this.l = new short[this.c];
            this.m = 0;
            this.n = 0;
         }

         for(; this.n < this.a.length; ++this.n) {
            if (var1 != null && var1[0] <= 0) {
               return null;
            }

            float[] var2 = this.b(this.n);
            if (var2 != null) {
               int var3 = this.m;
               int var4 = var2.length;
               if (var4 > this.c - var3) {
                  var4 = this.c - var3;
               }

               for(int var5 = 0; var5 < var4; ++var5) {
                  this.l[var3++] = (short)ks.c(-32768, 32767, (int)(var2[var5] * 32768.0F));
               }

               if (var1 != null) {
                  var1[0] -= var3 - this.m;
               }

               this.m = var3;
            }
         }

         this.g = null;
         short[] var6 = this.l;
         this.l = null;
         return new ix(this.b, var6, this.d, this.e, this.f, false);
      }
   }
}
