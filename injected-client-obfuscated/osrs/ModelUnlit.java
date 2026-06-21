package osrs;

import net.runelite.api.Model;
import net.runelite.api.ModelData;

public class ModelUnlit extends dG implements ModelData {
   public int[] a;
   public byte[] b;
   public byte[] c;
   public byte[] d;
   public byte[] e;
   public short[] f;
   public short[] g;
   public byte[] h;
   public byte i = 0;
   public int j;
   public byte[] k;
   public short[] l;
   public short[] m;
   public short[] n;
   public dL[] o;
   public dK[] p;
   public short q;
   public short r;
   public int[][] s;
   public int[][] t;
   public int[] u;
   public int[] v;
   public int[][] w;
   public int[][] x;
   public dK[] y;
   public boolean z = false;
   public int A = 0;
   public int B;
   public int C;
   public int D = 0;
   public int E;
   public int[] F;
   public float[] G;
   public int[] H;
   public float[] I;
   public static int[] J = new int[10000];
   public static int[] K = new int[10000];
   public static int L = 0;
   public static int[] M;
   public static int[] N;
   public float[] O;
   public int[] P;
   public int[] Q;
   public int R;
   public int S;
   public int[] T;

   public ModelUnlit() {
   }

   public ModelUnlit(byte[] var1) {
      if (var1[var1.length - 1] == -3 && var1[var1.length - 2] == -1) {
         this.loadObj3(var1);
      } else if (var1[var1.length - 1] == -2 && var1[var1.length - 2] == -1) {
         this.loadObj2(var1);
      } else if (var1[var1.length - 1] == -1 && var1[var1.length - 2] == -1) {
         this.a(var1);
      } else {
         this.b(var1);
      }

   }

   public ModelUnlit(ModelUnlit[] var1, int var2) {
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;
      this.A = 0;
      this.D = 0;
      this.B = 0;
      this.i = -1;

      int var11;
      ModelUnlit var12;
      for(var11 = 0; var11 < var2; ++var11) {
         var12 = var1[var11];
         if (var12 != null) {
            this.A += var12.A;
            this.D += var12.D;
            this.B += var12.B;
            if (var12.c != null) {
               var4 = true;
            } else {
               if (this.i == -1) {
                  this.i = var12.i;
               }

               if (this.i != var12.i) {
                  var4 = true;
               }
            }

            var3 |= var12.b != null;
            var5 |= var12.d != null;
            var6 |= var12.v != null;
            var7 |= var12.g != null;
            var8 |= var12.e != null;
            var9 |= var12.s != null;
            var10 |= var12.h != null;
         }
      }

      this.I = new float[this.A];
      this.G = new float[this.A];
      this.O = new float[this.A];
      this.u = new int[this.A];
      this.P = new int[this.D];
      this.Q = new int[this.D];
      this.a = new int[this.D];
      if (var3) {
         this.b = new byte[this.D];
      }

      if (var4) {
         this.c = new byte[this.D];
      }

      if (var5) {
         this.d = new byte[this.D];
      }

      if (var6) {
         this.v = new int[this.D];
      }

      if (var7) {
         this.g = new short[this.D];
      }

      if (var8) {
         this.e = new byte[this.D];
      }

      if (var9) {
         this.s = new int[this.A][];
         this.t = new int[this.A][];
      }

      if (var10) {
         this.h = new byte[this.D];
      }

      this.f = new short[this.D];
      if (this.B > 0) {
         this.k = new byte[this.B];
         this.l = new short[this.B];
         this.m = new short[this.B];
         this.n = new short[this.B];
      }

      this.A = 0;
      this.D = 0;
      this.B = 0;

      for(var11 = 0; var11 < var2; ++var11) {
         var12 = var1[var11];
         if (var12 != null) {
            int var13;
            for(var13 = 0; var13 < var12.D; ++var13) {
               if (var3 && var12.b != null) {
                  this.b[this.D] = var12.b[var13];
               }

               if (var4) {
                  if (var12.c != null) {
                     this.c[this.D] = var12.c[var13];
                  } else {
                     this.c[this.D] = var12.i;
                  }
               }

               if (var5 && var12.d != null) {
                  this.d[this.D] = var12.d[var13];
               }

               if (var6 && var12.v != null) {
                  this.v[this.D] = var12.v[var13];
               }

               if (var7) {
                  if (var12.g != null) {
                     this.g[this.D] = var12.g[var13];
                  } else {
                     this.g[this.D] = -1;
                  }
               }

               if (var8) {
                  if (var12.e != null && var12.e[var13] != -1) {
                     this.e[this.D] = (byte)(var12.e[var13] + this.B);
                  } else {
                     this.e[this.D] = -1;
                  }
               }

               if (var10 && var12.h != null) {
                  this.h[this.D] = var12.h[var13];
               }

               this.f[this.D] = var12.f[var13];
               this.P[this.D] = this.a(var12, var12.P[var13]);
               this.Q[this.D] = this.a(var12, var12.Q[var13]);
               this.a[this.D] = this.a(var12, var12.a[var13]);
               ++this.D;
            }

            for(var13 = 0; var13 < var12.B; ++var13) {
               byte var14 = this.k[this.B] = var12.k[var13];
               if (var14 == 0) {
                  this.l[this.B] = (short)this.a(var12, var12.l[var13]);
                  this.m[this.B] = (short)this.a(var12, var12.m[var13]);
                  this.n[this.B] = (short)this.a(var12, var12.n[var13]);
               }

               ++this.B;
            }
         }
      }

   }

   public ModelUnlit(ModelUnlit var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      this.A = var1.A;
      this.D = var1.D;
      this.B = var1.B;
      int var6;
      if (var2) {
         this.I = var1.I;
         this.G = var1.G;
         this.O = var1.O;
      } else {
         this.I = new float[this.A];
         this.G = new float[this.A];
         this.O = new float[this.A];

         for(var6 = 0; var6 < this.A; ++var6) {
            this.I[var6] = var1.I[var6];
            this.G[var6] = var1.G[var6];
            this.O[var6] = var1.O[var6];
         }
      }

      if (var3) {
         this.f = var1.f;
      } else {
         this.f = new short[this.D];

         for(var6 = 0; var6 < this.D; ++var6) {
            this.f[var6] = var1.f[var6];
         }
      }

      if (!var4 && var1.g != null) {
         this.g = new short[this.D];

         for(var6 = 0; var6 < this.D; ++var6) {
            this.g[var6] = var1.g[var6];
         }
      } else {
         this.g = var1.g;
      }

      if (var5) {
         this.d = var1.d;
      } else {
         this.d = new byte[this.D];
         if (var1.d == null) {
            for(var6 = 0; var6 < this.D; ++var6) {
               this.d[var6] = 0;
            }
         } else {
            for(var6 = 0; var6 < this.D; ++var6) {
               this.d[var6] = var1.d[var6];
            }
         }
      }

      this.P = var1.P;
      this.Q = var1.Q;
      this.a = var1.a;
      this.b = var1.b;
      this.c = var1.c;
      this.e = var1.e;
      this.i = var1.i;
      this.k = var1.k;
      this.l = var1.l;
      this.m = var1.m;
      this.n = var1.n;
      this.u = var1.u;
      this.v = var1.v;
      this.w = var1.w;
      this.x = var1.x;
      this.p = var1.p;
      this.o = var1.o;
      this.y = var1.y;
      this.s = var1.s;
      this.t = var1.t;
      this.q = var1.q;
      this.r = var1.r;
      this.h = var1.h;
   }

   public static void a(ModelUnlit var0, ModelUnlit var1, int var2, int var3, int var4, boolean var5) {
      var0.g();
      var0.e();
      var1.g();
      var1.e();
      ++L;
      int var6 = 0;
      float[] var7 = var1.I;
      int var8 = var1.A;

      int var9;
      for(var9 = 0; var9 < var0.A; ++var9) {
         dK var10 = var0.p[var9];
         if (var10.d != 0) {
            int var11 = (int)var0.G[var9] - var3;
            if (var11 <= var1.j) {
               int var12 = (int)var0.I[var9] - var2;
               if (var12 >= var1.R && var12 <= var1.E) {
                  int var13 = (int)var0.O[var9] - var4;
                  if (var13 >= var1.S && var13 <= var1.C) {
                     for(int var14 = 0; var14 < var8; ++var14) {
                        dK var15 = var1.p[var14];
                        if ((int)var7[var14] == var12 && (int)var1.O[var14] == var13 && (int)var1.G[var14] == var11 && var15.d != 0) {
                           if (var0.y == null) {
                              var0.y = new dK[var0.A];
                           }

                           if (var1.y == null) {
                              var1.y = new dK[var8];
                           }

                           dK var16 = var0.y[var9];
                           if (var16 == null) {
                              var16 = var0.y[var9] = new dK(var10);
                           }

                           dK var17 = var1.y[var14];
                           if (var17 == null) {
                              var17 = var1.y[var14] = new dK(var15);
                           }

                           var16.a += var15.a;
                           var16.b += var15.b;
                           var16.c += var15.c;
                           var16.d += var15.d;
                           var17.a += var10.a;
                           var17.b += var10.b;
                           var17.c += var10.c;
                           var17.d += var10.d;
                           ++var6;
                           J[var9] = L;
                           K[var14] = L;
                        }
                     }
                  }
               }
            }
         }
      }

      if (var6 >= 3 && var5) {
         for(var9 = 0; var9 < var0.D; ++var9) {
            if (J[var0.P[var9]] == L && J[var0.Q[var9]] == L && J[var0.a[var9]] == L) {
               if (var0.b == null) {
                  var0.b = new byte[var0.D];
               }

               var0.b[var9] = 2;
            }
         }

         for(var9 = 0; var9 < var1.D; ++var9) {
            if (K[var1.P[var9]] == L && K[var1.Q[var9]] == L && K[var1.a[var9]] == L) {
               if (var1.b == null) {
                  var1.b = new byte[var1.D];
               }

               var1.b[var9] = 2;
            }
         }
      }

   }

   public static final int a(int var0, int var1) {
      int var2 = (var0 & 127) * var1 >> 7;
      if (var2 < 2) {
         var2 = 2;
      } else if (var2 > 126) {
         var2 = 126;
      }

      return (var0 & 'ﾀ') + var2;
   }

   public static final int a(int var0) {
      if (var0 < 2) {
         var0 = 2;
      } else if (var0 > 126) {
         var0 = 126;
      }

      return var0;
   }

   public void loadObj3(byte[] var1) {
      aR var2 = new aR(var1);
      aR var3 = new aR(var1);
      aR var4 = new aR(var1);
      aR var5 = new aR(var1);
      aR var6 = new aR(var1);
      aR var7 = new aR(var1);
      aR var8 = new aR(var1);
      var2.d = var1.length - 26;
      int var9 = var2.d();
      int var10 = var2.d();
      int var11 = var2.b();
      int var12 = var2.b();
      int var13 = var2.b();
      int var14 = var2.b();
      int var15 = var2.b();
      int var16 = var2.b();
      int var17 = var2.b();
      int var18 = var2.b();
      int var19 = var2.d();
      int var20 = var2.d();
      int var21 = var2.d();
      int var22 = var2.d();
      int var23 = var2.d();
      int var24 = var2.d();
      int var25 = 0;
      int var26 = 0;
      int var27 = 0;
      int var28;
      int var29;
      if (var11 > 0) {
         this.k = new byte[var11];
         var2.d = 0;

         for(var28 = 0; var28 < var11; ++var28) {
            var29 = this.k[var28] = var2.c();
            if (var29 == 0) {
               ++var25;
            }

            if (var29 >= 1 && var29 <= 3) {
               ++var26;
            }

            if (var29 == 2) {
               ++var27;
            }
         }
      }

      var28 = var9 + var11;
      var29 = var28;
      if (var12 == 1) {
         var28 += var10;
      }

      int var30 = var10 + var28;
      int var31 = var30;
      if (var13 == 255) {
         var30 += var10;
      }

      int var32 = var30;
      if (var15 == 1) {
         var30 += var10;
      }

      int var33 = var24 + var30;
      int var34 = var33;
      if (var14 == 1) {
         var33 += var10;
      }

      int var35 = var22 + var33;
      int var36 = var35;
      if (var16 == 1) {
         var35 += var10 * 2;
      }

      int var37 = var23 + var35;
      int var38 = var10 * 2 + var37;
      int var39 = var19 + var38;
      int var40 = var20 + var39;
      int var41 = var21 + var40;
      int var42 = var25 * 6 + var41;
      int var43 = var26 * 6 + var42;
      int var44 = var26 * 6 + var43;
      int var45 = var26 * 2 + var44;
      int var46 = var26 + var45;
      int var47 = var26 * 2 + var27 * 2 + var46;
      this.A = var9;
      this.D = var10;
      this.B = var11;
      this.I = new float[var9];
      this.G = new float[var9];
      this.O = new float[var9];
      this.P = new int[var10];
      this.Q = new int[var10];
      this.a = new int[var10];
      if (var17 == 1) {
         this.u = new int[var9];
      }

      if (var12 == 1) {
         this.b = new byte[var10];
      }

      if (var13 == 255) {
         this.c = new byte[var10];
      } else {
         this.i = (byte)var13;
      }

      if (var14 == 1) {
         this.d = new byte[var10];
      }

      if (var15 == 1) {
         this.v = new int[var10];
      }

      if (var16 == 1) {
         this.g = new short[var10];
      }

      if (var16 == 1 && var11 > 0) {
         this.e = new byte[var10];
      }

      if (var18 == 1) {
         this.s = new int[var9][];
         this.t = new int[var9][];
      }

      this.f = new short[var10];
      if (var11 > 0) {
         this.l = new short[var11];
         this.m = new short[var11];
         this.n = new short[var11];
      }

      var2.d = var11;
      var3.d = var38;
      var4.d = var39;
      var5.d = var40;
      var6.d = var30;
      int var48 = 0;
      int var49 = 0;
      int var50 = 0;

      int var51;
      int var52;
      int var53;
      int var54;
      int var55;
      for(var51 = 0; var51 < var9; ++var51) {
         var52 = var2.b();
         var53 = 0;
         if ((var52 & 1) != 0) {
            var53 = var3.p();
         }

         var54 = 0;
         if ((var52 & 2) != 0) {
            var54 = var4.p();
         }

         var55 = 0;
         if ((var52 & 4) != 0) {
            var55 = var5.p();
         }

         this.I[var51] = (float)(var48 + var53);
         this.G[var51] = (float)(var49 + var54);
         this.O[var51] = (float)(var50 + var55);
         var48 = (int)this.I[var51];
         var49 = (int)this.G[var51];
         var50 = (int)this.O[var51];
         if (var17 == 1) {
            this.u[var51] = var6.b();
         }
      }

      if (var18 == 1) {
         for(var51 = 0; var51 < var9; ++var51) {
            var52 = var6.b();
            this.s[var51] = new int[var52];
            this.t[var51] = new int[var52];

            for(var53 = 0; var53 < var52; ++var53) {
               this.s[var51][var53] = var6.b();
               this.t[var51][var53] = var6.b();
            }
         }
      }

      var2.d = var37;
      var3.d = var29;
      var4.d = var31;
      var5.d = var34;
      var6.d = var32;
      var7.d = var36;
      var8.d = var35;

      for(var51 = 0; var51 < var10; ++var51) {
         this.f[var51] = (short)var2.d();
         if (var12 == 1) {
            this.b[var51] = var3.c();
         }

         if (var13 == 255) {
            this.c[var51] = var4.c();
         }

         if (var14 == 1) {
            this.d[var51] = var5.c();
         }

         if (var15 == 1) {
            this.v[var51] = var6.b();
         }

         if (var16 == 1) {
            this.g[var51] = (short)(var7.d() - 1);
         }

         if (this.e != null && this.g[var51] != -1) {
            this.e[var51] = (byte)(var8.b() - 1);
         }
      }

      var2.d = var33;
      var3.d = var28;
      var51 = 0;
      var52 = 0;
      var53 = 0;
      var54 = 0;

      int var56;
      int var57;
      for(var55 = 0; var55 < var10; ++var55) {
         var56 = var3.b();
         if (var56 == 1) {
            var51 = var2.p() + var54;
            var52 = var2.p() + var51;
            var53 = var2.p() + var52;
            var54 = var53;
            this.P[var55] = var51;
            this.Q[var55] = var52;
            this.a[var55] = var53;
         }

         if (var56 == 2) {
            var52 = var53;
            var53 = var2.p() + var54;
            var54 = var53;
            this.P[var55] = var51;
            this.Q[var55] = var52;
            this.a[var55] = var53;
         }

         if (var56 == 3) {
            var51 = var53;
            var53 = var2.p() + var54;
            var54 = var53;
            this.P[var55] = var51;
            this.Q[var55] = var52;
            this.a[var55] = var53;
         }

         if (var56 == 4) {
            var57 = var51;
            var51 = var52;
            var52 = var57;
            var53 = var2.p() + var54;
            var54 = var53;
            this.P[var55] = var51;
            this.Q[var55] = var57;
            this.a[var55] = var53;
         }
      }

      var2.d = var41;
      var3.d = var42;
      var4.d = var43;
      var5.d = var44;
      var6.d = var45;
      var7.d = var46;

      for(var55 = 0; var55 < var11; ++var55) {
         var56 = this.k[var55] & 255;
         if (var56 == 0) {
            this.l[var55] = (short)var2.d();
            this.m[var55] = (short)var2.d();
            this.n[var55] = (short)var2.d();
         }
      }

      var2.d = var47;
      var55 = var2.b();
      if (var55 != 0) {
         new dC();
         var2.d();
         var2.d();
         var2.d();
         var2.h();
      }

      boolean var58 = var2.b() == 1;
      if (var58) {
         this.h = new byte[var10];

         for(var57 = 0; var57 < var10; ++var57) {
            this.h[var57] = var2.c();
         }
      }

   }

   public void loadObj2(byte[] var1) {
      boolean var2 = false;
      boolean var3 = false;
      aR var4 = new aR(var1);
      aR var5 = new aR(var1);
      aR var6 = new aR(var1);
      aR var7 = new aR(var1);
      aR var8 = new aR(var1);
      var4.d = var1.length - 23;
      int var9 = var4.d();
      int var10 = var4.d();
      int var11 = var4.b();
      int var12 = var4.b();
      int var13 = var4.b();
      int var14 = var4.b();
      int var15 = var4.b();
      int var16 = var4.b();
      int var17 = var4.b();
      int var18 = var4.d();
      int var19 = var4.d();
      int var20 = var4.d();
      int var21 = var4.d();
      int var22 = var4.d();
      byte var23 = 0;
      int var24 = var9 + var23;
      int var25 = var10 + var24;
      int var26 = var25;
      if (var13 == 255) {
         var25 += var10;
      }

      int var27 = var25;
      if (var15 == 1) {
         var25 += var10;
      }

      int var28 = var25;
      if (var12 == 1) {
         var25 += var10;
      }

      int var29 = var22 + var25;
      int var30 = var29;
      if (var14 == 1) {
         var29 += var10;
      }

      int var31 = var21 + var29;
      int var32 = var10 * 2 + var31;
      int var33 = var11 * 6 + var32;
      int var34 = var18 + var33;
      int var35 = var19 + var34;
      int var36 = var20 + var35;
      this.A = var9;
      this.D = var10;
      this.B = var11;
      this.I = new float[var9];
      this.G = new float[var9];
      this.O = new float[var9];
      this.P = new int[var10];
      this.Q = new int[var10];
      this.a = new int[var10];
      if (var11 > 0) {
         this.k = new byte[var11];
         this.l = new short[var11];
         this.m = new short[var11];
         this.n = new short[var11];
      }

      if (var16 == 1) {
         this.u = new int[var9];
      }

      if (var12 == 1) {
         this.b = new byte[var10];
         this.e = new byte[var10];
         this.g = new short[var10];
      }

      if (var13 == 255) {
         this.c = new byte[var10];
      } else {
         this.i = (byte)var13;
      }

      if (var14 == 1) {
         this.d = new byte[var10];
      }

      if (var15 == 1) {
         this.v = new int[var10];
      }

      if (var17 == 1) {
         this.s = new int[var9][];
         this.t = new int[var9][];
      }

      this.f = new short[var10];
      var4.d = var23;
      var5.d = var33;
      var6.d = var34;
      var7.d = var35;
      var8.d = var25;
      int var37 = 0;
      int var38 = 0;
      int var39 = 0;

      int var40;
      int var41;
      int var42;
      int var43;
      int var44;
      for(var40 = 0; var40 < var9; ++var40) {
         var41 = var4.b();
         var42 = 0;
         if ((var41 & 1) != 0) {
            var42 = var5.p();
         }

         var43 = 0;
         if ((var41 & 2) != 0) {
            var43 = var6.p();
         }

         var44 = 0;
         if ((var41 & 4) != 0) {
            var44 = var7.p();
         }

         this.I[var40] = (float)(var37 + var42);
         this.G[var40] = (float)(var38 + var43);
         this.O[var40] = (float)(var39 + var44);
         var37 = (int)this.I[var40];
         var38 = (int)this.G[var40];
         var39 = (int)this.O[var40];
         if (var16 == 1) {
            this.u[var40] = var8.b();
         }
      }

      if (var17 == 1) {
         for(var40 = 0; var40 < var9; ++var40) {
            var41 = var8.b();
            this.s[var40] = new int[var41];
            this.t[var40] = new int[var41];

            for(var42 = 0; var42 < var41; ++var42) {
               this.s[var40][var42] = var8.b();
               this.t[var40][var42] = var8.b();
            }
         }
      }

      var4.d = var31;
      var5.d = var28;
      var6.d = var26;
      var7.d = var30;
      var8.d = var27;

      for(var40 = 0; var40 < var10; ++var40) {
         this.f[var40] = (short)var4.d();
         if (var12 == 1) {
            var41 = var5.b();
            if ((var41 & 1) == 1) {
               this.b[var40] = 1;
               var2 = true;
            } else {
               this.b[var40] = 0;
            }

            if ((var41 & 2) == 2) {
               this.e[var40] = (byte)(var41 >> 2);
               this.g[var40] = this.f[var40];
               this.f[var40] = 127;
               if (this.g[var40] != -1) {
                  var3 = true;
               }
            } else {
               this.e[var40] = -1;
               this.g[var40] = -1;
            }
         }

         if (var13 == 255) {
            this.c[var40] = var6.c();
         }

         if (var14 == 1) {
            this.d[var40] = var7.c();
         }

         if (var15 == 1) {
            this.v[var40] = var8.b();
         }
      }

      var4.d = var29;
      var5.d = var24;
      var40 = 0;
      var41 = 0;
      var42 = 0;
      var43 = 0;

      int var45;
      int var46;
      for(var44 = 0; var44 < var10; ++var44) {
         var45 = var5.b();
         if (var45 == 1) {
            var40 = var4.p() + var43;
            var41 = var4.p() + var40;
            var42 = var4.p() + var41;
            var43 = var42;
            this.P[var44] = var40;
            this.Q[var44] = var41;
            this.a[var44] = var42;
         }

         if (var45 == 2) {
            var41 = var42;
            var42 = var4.p() + var43;
            var43 = var42;
            this.P[var44] = var40;
            this.Q[var44] = var41;
            this.a[var44] = var42;
         }

         if (var45 == 3) {
            var40 = var42;
            var42 = var4.p() + var43;
            var43 = var42;
            this.P[var44] = var40;
            this.Q[var44] = var41;
            this.a[var44] = var42;
         }

         if (var45 == 4) {
            var46 = var40;
            var40 = var41;
            var41 = var46;
            var42 = var4.p() + var43;
            var43 = var42;
            this.P[var44] = var40;
            this.Q[var44] = var46;
            this.a[var44] = var42;
         }
      }

      var4.d = var32;

      for(var44 = 0; var44 < var11; ++var44) {
         this.k[var44] = 0;
         this.l[var44] = (short)var4.d();
         this.m[var44] = (short)var4.d();
         this.n[var44] = (short)var4.d();
      }

      var4.d = var36;
      boolean var49 = var4.b() == 1;
      if (var49) {
         this.h = new byte[var10];

         for(var45 = 0; var45 < var10; ++var45) {
            this.h[var45] = var4.c();
         }
      }

      if (this.e != null) {
         boolean var48 = false;

         for(var46 = 0; var46 < var10; ++var46) {
            int var47 = this.e[var46] & 255;
            if (var47 != 255) {
               if ((this.l[var47] & '\uffff') == this.P[var46] && (this.m[var47] & '\uffff') == this.Q[var46] && (this.n[var47] & '\uffff') == this.a[var46]) {
                  this.e[var46] = -1;
               } else {
                  var48 = true;
               }
            }
         }

         if (!var48) {
            this.e = null;
         }
      }

      if (!var3) {
         this.g = null;
      }

      if (!var2) {
         this.b = null;
      }

   }

   public void a(byte[] var1) {
      aR var2 = new aR(var1);
      aR var3 = new aR(var1);
      aR var4 = new aR(var1);
      aR var5 = new aR(var1);
      aR var6 = new aR(var1);
      aR var7 = new aR(var1);
      aR var8 = new aR(var1);
      var2.d = var1.length - 23;
      int var9 = var2.d();
      int var10 = var2.d();
      int var11 = var2.b();
      int var12 = var2.b();
      int var13 = var2.b();
      int var14 = var2.b();
      int var15 = var2.b();
      int var16 = var2.b();
      int var17 = var2.b();
      int var18 = var2.d();
      int var19 = var2.d();
      int var20 = var2.d();
      int var21 = var2.d();
      int var22 = var2.d();
      int var23 = 0;
      int var24 = 0;
      int var25 = 0;
      int var26;
      int var27;
      if (var11 > 0) {
         this.k = new byte[var11];
         var2.d = 0;

         for(var26 = 0; var26 < var11; ++var26) {
            var27 = this.k[var26] = var2.c();
            if (var27 == 0) {
               ++var23;
            }

            if (var27 >= 1 && var27 <= 3) {
               ++var24;
            }

            if (var27 == 2) {
               ++var25;
            }
         }
      }

      var26 = var9 + var11;
      var27 = var26;
      if (var12 == 1) {
         var26 += var10;
      }

      int var28 = var10 + var26;
      int var29 = var28;
      if (var13 == 255) {
         var28 += var10;
      }

      int var30 = var28;
      if (var15 == 1) {
         var28 += var10;
      }

      int var31 = var28;
      if (var17 == 1) {
         var28 += var9;
      }

      int var32 = var28;
      if (var14 == 1) {
         var28 += var10;
      }

      int var33 = var21 + var28;
      int var34 = var33;
      if (var16 == 1) {
         var33 += var10 * 2;
      }

      int var35 = var22 + var33;
      int var36 = var10 * 2 + var35;
      int var37 = var18 + var36;
      int var38 = var19 + var37;
      int var39 = var20 + var38;
      int var40 = var23 * 6 + var39;
      int var41 = var24 * 6 + var40;
      int var42 = var24 * 6 + var41;
      int var43 = var24 * 2 + var42;
      int var44 = var24 + var43;
      int var45 = var24 * 2 + var25 * 2 + var44;
      this.A = var9;
      this.D = var10;
      this.B = var11;
      this.I = new float[var9];
      this.G = new float[var9];
      this.O = new float[var9];
      this.P = new int[var10];
      this.Q = new int[var10];
      this.a = new int[var10];
      if (var17 == 1) {
         this.u = new int[var9];
      }

      if (var12 == 1) {
         this.b = new byte[var10];
      }

      if (var13 == 255) {
         this.c = new byte[var10];
      } else {
         this.i = (byte)var13;
      }

      if (var14 == 1) {
         this.d = new byte[var10];
      }

      if (var15 == 1) {
         this.v = new int[var10];
      }

      if (var16 == 1) {
         this.g = new short[var10];
      }

      if (var16 == 1 && var11 > 0) {
         this.e = new byte[var10];
      }

      this.f = new short[var10];
      if (var11 > 0) {
         this.l = new short[var11];
         this.m = new short[var11];
         this.n = new short[var11];
      }

      var2.d = var11;
      var3.d = var36;
      var4.d = var37;
      var5.d = var38;
      var6.d = var31;
      int var46 = 0;
      int var47 = 0;
      int var48 = 0;

      int var49;
      int var50;
      int var51;
      int var52;
      int var53;
      for(var49 = 0; var49 < var9; ++var49) {
         var50 = var2.b();
         var51 = 0;
         if ((var50 & 1) != 0) {
            var51 = var3.p();
         }

         var52 = 0;
         if ((var50 & 2) != 0) {
            var52 = var4.p();
         }

         var53 = 0;
         if ((var50 & 4) != 0) {
            var53 = var5.p();
         }

         this.I[var49] = (float)(var46 + var51);
         this.G[var49] = (float)(var47 + var52);
         this.O[var49] = (float)(var48 + var53);
         var46 = (int)this.I[var49];
         var47 = (int)this.G[var49];
         var48 = (int)this.O[var49];
         if (var17 == 1) {
            this.u[var49] = var6.b();
         }
      }

      var2.d = var35;
      var3.d = var27;
      var4.d = var29;
      var5.d = var32;
      var6.d = var30;
      var7.d = var34;
      var8.d = var33;

      for(var49 = 0; var49 < var10; ++var49) {
         this.f[var49] = (short)var2.d();
         if (var12 == 1) {
            this.b[var49] = var3.c();
         }

         if (var13 == 255) {
            this.c[var49] = var4.c();
         }

         if (var14 == 1) {
            this.d[var49] = var5.c();
         }

         if (var15 == 1) {
            this.v[var49] = var6.b();
         }

         if (var16 == 1) {
            this.g[var49] = (short)(var7.d() - 1);
         }

         if (this.e != null && this.g[var49] != -1) {
            this.e[var49] = (byte)(var8.b() - 1);
         }
      }

      var2.d = var28;
      var3.d = var26;
      var49 = 0;
      var50 = 0;
      var51 = 0;
      var52 = 0;

      int var54;
      for(var53 = 0; var53 < var10; ++var53) {
         var54 = var3.b();
         if (var54 == 1) {
            var49 = var2.p() + var52;
            var50 = var2.p() + var49;
            var51 = var2.p() + var50;
            var52 = var51;
            this.P[var53] = var49;
            this.Q[var53] = var50;
            this.a[var53] = var51;
         }

         if (var54 == 2) {
            var50 = var51;
            var51 = var2.p() + var52;
            var52 = var51;
            this.P[var53] = var49;
            this.Q[var53] = var50;
            this.a[var53] = var51;
         }

         if (var54 == 3) {
            var49 = var51;
            var51 = var2.p() + var52;
            var52 = var51;
            this.P[var53] = var49;
            this.Q[var53] = var50;
            this.a[var53] = var51;
         }

         if (var54 == 4) {
            int var55 = var49;
            var49 = var50;
            var50 = var55;
            var51 = var2.p() + var52;
            var52 = var51;
            this.P[var53] = var49;
            this.Q[var53] = var55;
            this.a[var53] = var51;
         }
      }

      var2.d = var39;
      var3.d = var40;
      var4.d = var41;
      var5.d = var42;
      var6.d = var43;
      var7.d = var44;

      for(var53 = 0; var53 < var11; ++var53) {
         var54 = this.k[var53] & 255;
         if (var54 == 0) {
            this.l[var53] = (short)var2.d();
            this.m[var53] = (short)var2.d();
            this.n[var53] = (short)var2.d();
         }
      }

      var2.d = var45;
      var53 = var2.b();
      if (var53 != 0) {
         new dC();
         var2.d();
         var2.d();
         var2.d();
         var2.h();
      }

   }

   public void b(byte[] var1) {
      boolean var2 = false;
      boolean var3 = false;
      aR var4 = new aR(var1);
      aR var5 = new aR(var1);
      aR var6 = new aR(var1);
      aR var7 = new aR(var1);
      aR var8 = new aR(var1);
      var4.d = var1.length - 18;
      int var9 = var4.d();
      int var10 = var4.d();
      int var11 = var4.b();
      int var12 = var4.b();
      int var13 = var4.b();
      int var14 = var4.b();
      int var15 = var4.b();
      int var16 = var4.b();
      int var17 = var4.d();
      int var18 = var4.d();
      int var19 = var4.d();
      int var20 = var4.d();
      byte var21 = 0;
      int var22 = var9 + var21;
      int var23 = var10 + var22;
      int var24 = var23;
      if (var13 == 255) {
         var23 += var10;
      }

      int var25 = var23;
      if (var15 == 1) {
         var23 += var10;
      }

      int var26 = var23;
      if (var12 == 1) {
         var23 += var10;
      }

      int var27 = var23;
      if (var16 == 1) {
         var23 += var9;
      }

      int var28 = var23;
      if (var14 == 1) {
         var23 += var10;
      }

      int var29 = var20 + var23;
      int var30 = var10 * 2 + var29;
      int var31 = var11 * 6 + var30;
      int var32 = var17 + var31;
      int var33 = var18 + var32;
      int var10000 = var19 + var33;
      this.A = var9;
      this.D = var10;
      this.B = var11;
      this.I = new float[var9];
      this.G = new float[var9];
      this.O = new float[var9];
      this.P = new int[var10];
      this.Q = new int[var10];
      this.a = new int[var10];
      if (var11 > 0) {
         this.k = new byte[var11];
         this.l = new short[var11];
         this.m = new short[var11];
         this.n = new short[var11];
      }

      if (var16 == 1) {
         this.u = new int[var9];
      }

      if (var12 == 1) {
         this.b = new byte[var10];
         this.e = new byte[var10];
         this.g = new short[var10];
      }

      if (var13 == 255) {
         this.c = new byte[var10];
      } else {
         this.i = (byte)var13;
      }

      if (var14 == 1) {
         this.d = new byte[var10];
      }

      if (var15 == 1) {
         this.v = new int[var10];
      }

      this.f = new short[var10];
      var4.d = var21;
      var5.d = var31;
      var6.d = var32;
      var7.d = var33;
      var8.d = var27;
      int var35 = 0;
      int var36 = 0;
      int var37 = 0;

      int var38;
      int var39;
      int var40;
      int var41;
      int var42;
      for(var38 = 0; var38 < var9; ++var38) {
         var39 = var4.b();
         var40 = 0;
         if ((var39 & 1) != 0) {
            var40 = var5.p();
         }

         var41 = 0;
         if ((var39 & 2) != 0) {
            var41 = var6.p();
         }

         var42 = 0;
         if ((var39 & 4) != 0) {
            var42 = var7.p();
         }

         this.I[var38] = (float)(var35 + var40);
         this.G[var38] = (float)(var36 + var41);
         this.O[var38] = (float)(var37 + var42);
         var35 = (int)this.I[var38];
         var36 = (int)this.G[var38];
         var37 = (int)this.O[var38];
         if (var16 == 1) {
            this.u[var38] = var8.b();
         }
      }

      var4.d = var29;
      var5.d = var26;
      var6.d = var24;
      var7.d = var28;
      var8.d = var25;

      for(var38 = 0; var38 < var10; ++var38) {
         this.f[var38] = (short)var4.d();
         if (var12 == 1) {
            var39 = var5.b();
            if ((var39 & 1) == 1) {
               this.b[var38] = 1;
               var2 = true;
            } else {
               this.b[var38] = 0;
            }

            if ((var39 & 2) == 2) {
               this.e[var38] = (byte)(var39 >> 2);
               this.g[var38] = this.f[var38];
               this.f[var38] = 127;
               if (this.g[var38] != -1) {
                  var3 = true;
               }
            } else {
               this.e[var38] = -1;
               this.g[var38] = -1;
            }
         }

         if (var13 == 255) {
            this.c[var38] = var6.c();
         }

         if (var14 == 1) {
            this.d[var38] = var7.c();
         }

         if (var15 == 1) {
            this.v[var38] = var8.b();
         }
      }

      var4.d = var23;
      var5.d = var22;
      var38 = 0;
      var39 = 0;
      var40 = 0;
      var41 = 0;

      int var43;
      int var44;
      for(var42 = 0; var42 < var10; ++var42) {
         var43 = var5.b();
         if (var43 == 1) {
            var38 = var4.p() + var41;
            var39 = var4.p() + var38;
            var40 = var4.p() + var39;
            var41 = var40;
            this.P[var42] = var38;
            this.Q[var42] = var39;
            this.a[var42] = var40;
         }

         if (var43 == 2) {
            var39 = var40;
            var40 = var4.p() + var41;
            var41 = var40;
            this.P[var42] = var38;
            this.Q[var42] = var39;
            this.a[var42] = var40;
         }

         if (var43 == 3) {
            var38 = var40;
            var40 = var4.p() + var41;
            var41 = var40;
            this.P[var42] = var38;
            this.Q[var42] = var39;
            this.a[var42] = var40;
         }

         if (var43 == 4) {
            var44 = var38;
            var38 = var39;
            var39 = var44;
            var40 = var4.p() + var41;
            var41 = var40;
            this.P[var42] = var38;
            this.Q[var42] = var44;
            this.a[var42] = var40;
         }
      }

      var4.d = var30;

      for(var42 = 0; var42 < var11; ++var42) {
         this.k[var42] = 0;
         this.l[var42] = (short)var4.d();
         this.m[var42] = (short)var4.d();
         this.n[var42] = (short)var4.d();
      }

      if (this.e != null) {
         boolean var45 = false;

         for(var43 = 0; var43 < var10; ++var43) {
            var44 = this.e[var43] & 255;
            if (var44 != 255) {
               if ((this.l[var44] & '\uffff') == this.P[var43] && (this.m[var44] & '\uffff') == this.Q[var43] && (this.n[var44] & '\uffff') == this.a[var43]) {
                  this.e[var43] = -1;
               } else {
                  var45 = true;
               }
            }
         }

         if (!var45) {
            this.e = null;
         }
      }

      if (!var3) {
         this.g = null;
      }

      if (!var2) {
         this.b = null;
      }

   }

   public int a(ModelUnlit var1, int var2) {
      int var3 = -1;
      int var4 = (int)var1.I[var2];
      int var5 = (int)var1.G[var2];
      int var6 = (int)var1.O[var2];

      for(int var7 = 0; var7 < this.A; ++var7) {
         if ((int)this.I[var7] == var4 && (int)this.G[var7] == var5 && (int)this.O[var7] == var6) {
            var3 = var7;
            break;
         }
      }

      if (var3 == -1) {
         this.I[this.A] = (float)var4;
         this.G[this.A] = (float)var5;
         this.O[this.A] = (float)var6;
         if (var1.u != null) {
            this.u[this.A] = var1.u[var2];
         }

         if (var1.s != null) {
            this.s[this.A] = var1.s[var2];
            this.t[this.A] = var1.t[var2];
         }

         var3 = this.A++;
      }

      return var3;
   }

   public ModelUnlit a() {
      ModelUnlit var1 = new ModelUnlit();
      if (this.b != null) {
         var1.b = new byte[this.D];

         for(int var2 = 0; var2 < this.D; ++var2) {
            var1.b[var2] = this.b[var2];
         }
      }

      var1.A = this.A;
      var1.D = this.D;
      var1.B = this.B;
      var1.I = this.I;
      var1.G = this.G;
      var1.O = this.O;
      var1.P = this.P;
      var1.Q = this.Q;
      var1.a = this.a;
      var1.c = this.c;
      var1.d = this.d;
      var1.e = this.e;
      var1.f = this.f;
      var1.g = this.g;
      var1.i = this.i;
      var1.k = this.k;
      var1.l = this.l;
      var1.m = this.m;
      var1.n = this.n;
      var1.u = this.u;
      var1.v = this.v;
      var1.w = this.w;
      var1.x = this.x;
      var1.p = this.p;
      var1.o = this.o;
      var1.q = this.q;
      var1.r = this.r;
      var1.h = this.h;
      return var1;
   }

   public ModelUnlit a(int[][] var1, int var2, int var3, int var4, boolean var5, int var6) {
      this.g();
      int var7 = this.R + var2;
      int var8 = this.E + var2;
      int var9 = this.S + var4;
      int var10 = this.C + var4;
      if (var7 >= 0 && var8 + 128 >> 7 < var1.length && var9 >= 0 && var10 + 128 >> 7 < var1[0].length) {
         int var11 = var7 >> 7;
         int var12 = var8 + 127 >> 7;
         int var13 = var9 >> 7;
         int var14 = var10 + 127 >> 7;
         if (var1[var11][var13] == var3 && var1[var12][var13] == var3 && var1[var11][var14] == var3 && var1[var12][var14] == var3) {
            return this;
         } else {
            ModelUnlit var15;
            if (var5) {
               var15 = new ModelUnlit(this, true, true, true, true);
               var15.G = new float[var15.A];
            } else {
               var15 = this;
            }

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
            if (var6 == 0) {
               for(var16 = 0; var16 < var15.A; ++var16) {
                  var17 = (int)this.I[var16] + var2;
                  var18 = (int)this.O[var16] + var4;
                  var19 = var17 & 127;
                  var20 = var18 & 127;
                  var21 = var17 >> 7;
                  var22 = var18 >> 7;
                  var23 = (128 - var19) * var1[var21][var22] + var1[var21 + 1][var22] * var19 >> 7;
                  var24 = (128 - var19) * var1[var21][var22 + 1] + var1[var21 + 1][var22 + 1] * var19 >> 7;
                  var25 = (128 - var20) * var23 + var20 * var24 >> 7;
                  var15.G[var16] = (float)((int)this.G[var16] + var25 - var3);
               }
            } else {
               for(var16 = 0; var16 < var15.A; ++var16) {
                  var17 = (-((int)this.G[var16]) << 16) / this.cd;
                  if (var17 < var6) {
                     var18 = (int)this.I[var16] + var2;
                     var19 = (int)this.O[var16] + var4;
                     var20 = var18 & 127;
                     var21 = var19 & 127;
                     var22 = var18 >> 7;
                     var23 = var19 >> 7;
                     var24 = (128 - var20) * var1[var22][var23] + var1[var22 + 1][var23] * var20 >> 7;
                     var25 = (128 - var20) * var1[var22][var23 + 1] + var1[var22 + 1][var23 + 1] * var20 >> 7;
                     int var26 = (128 - var21) * var24 + var21 * var25 >> 7;
                     var15.G[var16] = (float)((var26 - var3) * (var6 - var17) / var6 + (int)this.G[var16]);
                  } else {
                     var15.G[var16] = this.G[var16];
                  }
               }
            }

            var15.f();
            return var15;
         }
      } else {
         return this;
      }
   }

   public void c() {
      int[] var2;
      int var3;
      int var10003;
      int var4;
      int var5;
      if (this.u != null) {
         var2 = new int[256];
         var3 = 0;

         for(var4 = 0; var4 < this.A; ++var4) {
            var5 = this.u[var4];
            var10003 = var2[var5]++;
            if (var5 > var3) {
               var3 = var5;
            }
         }

         this.w = new int[var3 + 1][];

         for(var4 = 0; var4 <= var3; ++var4) {
            this.w[var4] = new int[var2[var4]];
            var2[var4] = 0;
         }

         for(var5 = 0; var5 < this.A; this.w[var4][var2[var4]++] = var5++) {
            var4 = this.u[var5];
         }

         this.u = null;
      }

      if (this.v != null) {
         var2 = new int[256];
         var3 = 0;

         for(var4 = 0; var4 < this.D; ++var4) {
            var5 = this.v[var4];
            var10003 = var2[var5]++;
            if (var5 > var3) {
               var3 = var5;
            }
         }

         this.x = new int[var3 + 1][];

         for(var4 = 0; var4 <= var3; ++var4) {
            this.x[var4] = new int[var2[var4]];
            var2[var4] = 0;
         }

         for(var5 = 0; var5 < this.D; this.x[var4][var2[var4]++] = var5++) {
            var4 = this.v[var5];
         }

         this.v = null;
      }

   }

   public void b(int var1) {
      int var2 = M[var1];
      int var3 = N[var1];

      for(int var4 = 0; var4 < this.A; ++var4) {
         int var5 = (int)this.O[var4] * var2 + (int)this.I[var4] * var3 >> 16;
         this.O[var4] = (float)((int)this.O[var4] * var3 - (int)this.I[var4] * var2 >> 16);
         this.I[var4] = (float)var5;
      }

      this.f();
   }

   public void a(short var1, short var2) {
      for(int var3 = 0; var3 < this.D; ++var3) {
         if (this.f[var3] == var1) {
            this.f[var3] = var2;
         }
      }

   }

   public void b(short var1, short var2) {
      if (this.g != null) {
         for(int var3 = 0; var3 < this.D; ++var3) {
            if (this.g[var3] == var1) {
               this.g[var3] = var2;
            }
         }
      }

   }

   public void d() {
      int var1;
      for(var1 = 0; var1 < this.A; ++var1) {
         this.O[var1] = (float)(-((int)this.O[var1]));
      }

      for(var1 = 0; var1 < this.D; ++var1) {
         int var2 = this.P[var1];
         this.P[var1] = this.a[var1];
         this.a[var1] = var2;
      }

      this.f();
   }

   public void e() {
      if (this.p == null) {
         this.p = new dK[this.A];

         int var1;
         for(var1 = 0; var1 < this.A; ++var1) {
            this.p[var1] = new dK();
         }

         for(var1 = 0; var1 < this.D; ++var1) {
            int var2 = this.P[var1];
            int var3 = this.Q[var1];
            int var4 = this.a[var1];
            int var5 = (int)this.I[var3] - (int)this.I[var2];
            int var6 = (int)this.G[var3] - (int)this.G[var2];
            int var7 = (int)this.O[var3] - (int)this.O[var2];
            int var8 = (int)this.I[var4] - (int)this.I[var2];
            int var9 = (int)this.G[var4] - (int)this.G[var2];
            int var10 = (int)this.O[var4] - (int)this.O[var2];
            int var11 = var6 * var10 - var7 * var9;
            int var12 = var7 * var8 - var5 * var10;

            int var13;
            for(var13 = var5 * var9 - var6 * var8; var11 > 8192 || var12 > 8192 || var13 > 8192 || var11 < -8192 || var12 < -8192 || var13 < -8192; var13 >>= 1) {
               var11 >>= 1;
               var12 >>= 1;
            }

            int var14 = (int)Math.sqrt((double)(var13 * var13 + var11 * var11 + var12 * var12));
            if (var14 <= 0) {
               var14 = 1;
            }

            int var15 = var11 * 256 / var14;
            int var16 = var12 * 256 / var14;
            int var17 = var13 * 256 / var14;
            byte var18;
            if (this.b == null) {
               var18 = 0;
            } else {
               var18 = this.b[var1];
            }

            if (var18 == 0) {
               dK var19 = this.p[var2];
               var19.a += var15;
               var19.b += var16;
               var19.c += var17;
               ++var19.d;
               dK var20 = this.p[var3];
               var20.a += var15;
               var20.b += var16;
               var20.c += var17;
               ++var20.d;
               dK var21 = this.p[var4];
               var21.a += var15;
               var21.b += var16;
               var21.c += var17;
               ++var21.d;
            } else if (var18 == 1) {
               if (this.o == null) {
                  this.o = new dL[this.D];
               }

               dL var22 = this.o[var1] = new dL();
               var22.b = var15;
               var22.a = var16;
               var22.c = var17;
            }
         }
      }

   }

   public void f() {
      this.p = null;
      this.y = null;
      this.o = null;
      this.z = false;
   }

   public void g() {
      if (!this.z) {
         this.cd = 0;
         this.j = 0;
         this.R = 999999;
         this.E = -999999;
         this.C = -99999;
         this.S = 99999;

         for(int var1 = 0; var1 < this.A; ++var1) {
            int var2 = (int)this.I[var1];
            int var3 = (int)this.G[var1];
            int var4 = (int)this.O[var1];
            if (var2 < this.R) {
               this.R = var2;
            }

            if (var2 > this.E) {
               this.E = var2;
            }

            if (var4 < this.S) {
               this.S = var4;
            }

            if (var4 > this.C) {
               this.C = var4;
            }

            if (-var3 > this.cd) {
               this.cd = -var3;
            }

            if (var3 > this.j) {
               this.j = var3;
            }
         }

         this.z = true;
      }

   }

   public void a(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.A; ++var4) {
         float[] var5 = this.I;
         var5[var4] += (float)var1;
         var5 = this.G;
         var5[var4] += (float)var2;
         var5 = this.O;
         var5[var4] += (float)var3;
      }

      this.w();
   }

   public final aH a(int var1, int var2, int var3, int var4, int var5) {
      Client.dF.trace("Lighting model {}", this);
      int var6 = var5;
      int var7 = var4;
      int var8 = var3;
      int var9 = var1;
      ModelUnlit var10 = this;
      this.e();
      int var11 = (int)Math.sqrt((double)(var5 * var5 + var4 * var4 + var3 * var3));
      int var12 = var2 * var11 >> 8;
      aH var13 = new aH();
      var13.E = new int[this.D];
      var13.F = new int[this.D];
      var13.G = new int[this.D];
      if (this.B > 0 && this.e != null) {
         int[] var14 = new int[this.B];

         int var15;
         for(var15 = 0; var15 < var10.D; ++var15) {
            if (var10.e[var15] != -1) {
               ++var14[var10.e[var15] & 255];
            }
         }

         var13.N = 0;

         for(var15 = 0; var15 < var10.B; ++var15) {
            if (var14[var15] > 0 && var10.k[var15] == 0) {
               ++var13.N;
            }
         }

         var13.O = new int[var13.N];
         var13.P = new int[var13.N];
         var13.Q = new int[var13.N];
         var15 = 0;

         int var16;
         for(var16 = 0; var16 < var10.B; ++var16) {
            if (var14[var16] > 0 && var10.k[var16] == 0) {
               var13.O[var15] = var10.l[var16] & '\uffff';
               var13.P[var15] = var10.m[var16] & '\uffff';
               var13.Q[var15] = var10.n[var16] & '\uffff';
               var14[var16] = var15++;
            } else {
               var14[var16] = -1;
            }
         }

         var13.L = new byte[var10.D];

         for(var16 = 0; var16 < var10.D; ++var16) {
            if (var10.e[var16] != -1) {
               var13.L[var16] = (byte)var14[var10.e[var16] & 255];
            } else {
               var13.L[var16] = -1;
            }
         }
      }

      for(int var25 = 0; var25 < var10.D; ++var25) {
         byte var26;
         if (var10.b == null) {
            var26 = 0;
         } else {
            var26 = var10.b[var25];
         }

         byte var27;
         if (var10.d == null) {
            var27 = 0;
         } else {
            var27 = var10.d[var25];
         }

         short var17;
         if (var10.g == null) {
            var17 = -1;
         } else {
            var17 = var10.g[var25];
         }

         if (var27 == -2) {
            var26 = 3;
         }

         if (var27 == -1) {
            var26 = 2;
         }

         int var19;
         dL var28;
         if (var17 == -1) {
            if (var26 != 0) {
               if (var26 == 1) {
                  var28 = var10.o[var25];
                  var19 = (var28.c * var6 + var28.b * var8 + var28.a * var7) / (var12 / 2 + var12) + var9;
                  var13.E[var25] = a(var10.f[var25] & '\uffff', var19);
                  var13.G[var25] = -1;
               } else if (var26 == 3) {
                  var13.E[var25] = 128;
                  var13.G[var25] = -1;
               } else {
                  var13.G[var25] = -2;
               }
            } else {
               int var29 = var10.f[var25] & '\uffff';
               dK var30;
               if (var10.y != null && var10.y[var10.P[var25]] != null) {
                  var30 = var10.y[var10.P[var25]];
               } else {
                  var30 = var10.p[var10.P[var25]];
               }

               int var31 = (var30.c * var6 + var30.a * var8 + var30.b * var7) / (var30.d * var12) + var9;
               var13.E[var25] = a(var29, var31);
               dK var32;
               if (var10.y != null && var10.y[var10.Q[var25]] != null) {
                  var32 = var10.y[var10.Q[var25]];
               } else {
                  var32 = var10.p[var10.Q[var25]];
               }

               int var33 = (var32.c * var6 + var32.a * var8 + var32.b * var7) / (var32.d * var12) + var9;
               var13.F[var25] = a(var29, var33);
               dK var34;
               if (var10.y != null && var10.y[var10.a[var25]] != null) {
                  var34 = var10.y[var10.a[var25]];
               } else {
                  var34 = var10.p[var10.a[var25]];
               }

               int var24 = (var34.c * var6 + var34.a * var8 + var34.b * var7) / (var34.d * var12) + var9;
               var13.G[var25] = a(var29, var24);
            }
         } else if (var26 != 0) {
            if (var26 == 1) {
               var28 = var10.o[var25];
               var19 = (var28.c * var6 + var28.b * var8 + var28.a * var7) / (var12 / 2 + var12) + var9;
               var13.E[var25] = a(var19);
               var13.G[var25] = -1;
            } else {
               var13.G[var25] = -2;
            }
         } else {
            dK var18;
            if (var10.y != null && var10.y[var10.P[var25]] != null) {
               var18 = var10.y[var10.P[var25]];
            } else {
               var18 = var10.p[var10.P[var25]];
            }

            var19 = (var18.c * var6 + var18.a * var8 + var18.b * var7) / (var18.d * var12) + var9;
            var13.E[var25] = a(var19);
            dK var20;
            if (var10.y != null && var10.y[var10.Q[var25]] != null) {
               var20 = var10.y[var10.Q[var25]];
            } else {
               var20 = var10.p[var10.Q[var25]];
            }

            int var21 = (var20.c * var6 + var20.a * var8 + var20.b * var7) / (var20.d * var12) + var9;
            var13.F[var25] = a(var21);
            dK var22;
            if (var10.y != null && var10.y[var10.a[var25]] != null) {
               var22 = var10.y[var10.a[var25]];
            } else {
               var22 = var10.p[var10.a[var25]];
            }

            int var23 = (var22.c * var6 + var22.a * var8 + var22.b * var7) / (var22.d * var12) + var9;
            var13.G[var25] = a(var23);
         }
      }

      var10.c();
      var13.z = var10.A;
      var13.ak = var10.I;
      var13.al = var10.G;
      var13.af = var10.O;
      var13.A = var10.D;
      var13.B = var10.P;
      var13.C = var10.Q;
      var13.D = var10.a;
      var13.J = var10.c;
      var13.I = var10.d;
      var13.M = var10.i;
      var13.Z = var10.w;
      var13.aa = var10.x;
      var13.K = var10.g;
      var13.R = var10.s;
      var13.S = var10.t;
      var13.H = var10.h;
      if (var13 == null) {
         return null;
      } else {
         if ((bo.fe & 4) == 4) {
            this.j();
            var13.ay = this.H;
            var13.az = this.T;
            var13.ag = this.F;
         }

         if ((bo.fe & 64) == 64) {
            var13.aj = this.f;
         }

         return var13;
      }
   }

   public final aH h() {
      return this.a(128, 43690, 0, -1, 0);
   }

   public Model light() {
      return this.m();
   }

   public ModelData i() {
      return this.y();
   }

   public ModelUnlit a(boolean var1) {
      if (this.d != null) {
         this.d = (byte[])this.d.clone();
      } else if (var1) {
         this.d = new byte[this.getFaceCount()];
      }

      return this;
   }

   public static ModelUnlit a(au var0, int var1, int var2) {
      byte[] var3 = var0.loadData(var1, var2);

      try {
         return var3 == null ? null : new ModelUnlit(var3);
      } catch (NullPointerException var5) {
         throw new RuntimeException("loading model " + var1 + ", " + var2, var5);
      }
   }

   public ModelData b(int var1, int var2, int var3) {
      return this.e(var1, var2, var3);
   }

   public void j() {
      dK[] var1 = this.A();
      dK[] var2 = this.v();
      if (var1 != null && this.H == null) {
         int var3 = this.getVerticesCount();
         this.H = new int[var3];
         this.T = new int[var3];
         this.F = new int[var3];

         for(int var4 = 0; var4 < var3; ++var4) {
            dK var5;
            if (var2 != null && (var5 = var2[var4]) != null) {
               this.H[var4] = var5.c();
               this.T[var4] = var5.b();
               this.F[var4] = var5.a();
            } else {
               dK var6;
               if ((var6 = var1[var4]) != null) {
                  this.H[var4] = var6.c();
                  this.T[var4] = var6.b();
                  this.F[var4] = var6.a();
               }
            }
         }
      }

   }

   public ModelData recolor(short var1, short var2) {
      return this.c(var1, var2);
   }

   public void k() {
      for(int var1 = 0; var1 < this.A; ++var1) {
         float var2 = this.O[var1];
         this.O[var1] = this.I[var1];
         this.I[var1] = -var2;
      }

      this.w();
   }

   public void c(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.A; ++var4) {
         this.I[var4] = this.I[var4] * (float)var1 / 128.0F;
         this.G[var4] = this.G[var4] * (float)var2 / 128.0F;
         this.O[var4] = this.O[var4] * (float)var3 / 128.0F;
      }

      this.w();
   }

   public ModelData d(int var1, int var2, int var3) {
      return this.f(var1, var2, var3);
   }

   public ModelData shallowCopy() {
      return this.n();
   }

   public ModelData l() {
      return this.z();
   }

   public aH m() {
      return this.b(64, 768, -50, -10, -50);
   }

   public ModelUnlit n() {
      return new ModelUnlit(this, true, true, true, true);
   }

   public ModelData o() {
      return this.C();
   }

   public void p() {
      for(int var1 = 0; var1 < this.A; ++var1) {
         this.I[var1] = -this.I[var1];
         this.O[var1] = -this.O[var1];
      }

      this.w();
   }

   public float[] getVerticesZ() {
      return this.O;
   }

   public float[] getVerticesY() {
      return this.G;
   }

   public void q() {
      for(int var1 = 0; var1 < this.A; ++var1) {
         float var2 = this.I[var1];
         this.I[var1] = this.O[var1];
         this.O[var1] = -var2;
      }

      this.w();
   }

   public ModelData cloneColors() {
      return this.r();
   }

   public ModelData cloneTransparencies(boolean var1) {
      return this.a(var1);
   }

   public Model light(int var1, int var2, int var3, int var4, int var5) {
      return this.b(var1, var2, var3, var4, var5);
   }

   public ModelData retexture(short var1, short var2) {
      return this.d(var1, var2);
   }

   public ModelUnlit r() {
      this.f = (short[])this.f.clone();
      return this;
   }

   public ModelUnlit s() {
      this.I = (float[])this.I.clone();
      this.G = (float[])this.G.clone();
      this.O = (float[])this.O.clone();
      return this;
   }

   public ModelData cloneTextures() {
      return this.u();
   }

   public ModelData cloneVertices() {
      return this.s();
   }

   public ModelUnlit t() {
      return this.a(false);
   }

   public ModelData cloneTransparencies() {
      return this.t();
   }

   public float[] getVerticesX() {
      return this.I;
   }

   public ModelUnlit u() {
      this.g = (short[])this.g.clone();
      return this;
   }

   public int getFaceCount() {
      return this.D;
   }

   public ModelUnlit c(short var1, short var2) {
      this.a(var1, var2);
      return this;
   }

   public dK[] v() {
      return this.y;
   }

   public short[] getFaceTextures() {
      return this.g;
   }

   public ModelUnlit b(int[][] var1, int var2, int var3, int var4, boolean var5, int var6) {
      return this.a(var1, var2, var3, var4, var5, var6);
   }

   public void w() {
      this.f();
   }

   public ModelUnlit x() {
      return this.a();
   }

   public ModelUnlit y() {
      this.k();
      return this;
   }

   public byte[] getFaceTransparencies() {
      return this.d;
   }

   public ModelUnlit e(int var1, int var2, int var3) {
      this.c(var1, var2, var3);
      return this;
   }

   public ModelUnlit z() {
      this.q();
      return this;
   }

   public static void b(ModelUnlit var0, ModelUnlit var1, int var2, int var3, int var4, boolean var5) {
      a(var0, var1, var2, var3, var4, var5);
   }

   public int[] getFaceIndices1() {
      return this.P;
   }

   public dK[] A() {
      return this.p;
   }

   public short[] getFaceColors() {
      return this.f;
   }

   public int getVerticesCount() {
      return this.A;
   }

   public ModelUnlit d(short var1, short var2) {
      this.b(var1, var2);
      return this;
   }

   public ModelUnlit f(int var1, int var2, int var3) {
      this.a(var1, var2, var3);
      return this;
   }

   public int[] getFaceIndices2() {
      return this.Q;
   }

   public aH b(int var1, int var2, int var3, int var4, int var5) {
      return this.a(var1, var2, var3, var4, var5);
   }

   public void B() {
      this.e();
   }

   public ModelUnlit C() {
      this.p();
      return this;
   }

   public int[] getFaceIndices3() {
      return this.a;
   }

   static {
      M = aW.d;
      N = aW.e;
   }
}
