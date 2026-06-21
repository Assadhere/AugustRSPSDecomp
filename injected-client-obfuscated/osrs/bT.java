package osrs;

public class bT extends cl {
   public K a;
   public long b;
   public int c;
   public int d;
   public int e;
   public int f;
   public int g;
   public boolean h;
   public int i;
   public int j;
   public int k;
   public int l;
   public int m;
   public int n;
   public int o;
   public int p;
   public int q;
   public int r;
   public int s;
   public int t;

   public bT(ix var1, int var2, int var3) {
      this.a = K.q;
      this.b = 0L;
      this.c = 0;
      this.d = 0;
      this.e = 0;
      this.G = var1;
      this.f = var1.e;
      this.g = var1.f;
      this.h = var1.g;
      this.i = var2;
      this.j = var3;
      this.k = 8192;
      this.l = 0;
      this.b();
   }

   public bT(ix var1, int var2, int var3, int var4) {
      this.a = K.q;
      this.b = 0L;
      this.c = 0;
      this.d = 0;
      this.e = 0;
      this.G = var1;
      this.f = var1.e;
      this.g = var1.f;
      this.h = var1.g;
      this.i = var2;
      this.j = var3;
      this.k = var4;
      this.l = 0;
      this.b();
   }

   public static int a(int var0, int var1) {
      return var1 < 0 ? var0 : (int)((double)var0 * Math.sqrt((double)(16384 - var1) * 1.220703125E-4) + 0.5);
   }

   public static int b(int var0, int var1) {
      return var1 < 0 ? -var0 : (int)((double)var0 * Math.sqrt((double)var1 * 1.220703125E-4) + 0.5);
   }

   public static bT a(ix var0, int var1, int var2) {
      return var0.d != null && var0.d.length != 0 ? new bT(var0, (int)((long)var0.c * 256L * (long)var1 / (long)(bo.fG * 100)), var2 << 6) : null;
   }

   public static bT a(ix var0, int var1, int var2, int var3) {
      return var0.d != null && var0.d.length != 0 ? new bT(var0, var1, var2, var3) : null;
   }

   public static int a(short[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, bT var8) {
      int var9 = var2 >> 8;
      int var10 = var7 >> 8;
      int var11 = var4 << 2;
      int var12;
      if ((var12 = var3 + var10 - var9) > var6) {
         var12 = var6;
      }

      int var13;
      for(var12 -= 3; var3 < var12; var1[var13] += var0[var9++] * var11) {
         var13 = var3++;
         var1[var13] += var0[var9++] * var11;
         var13 = var3++;
         var1[var13] += var0[var9++] * var11;
         var13 = var3++;
         var1[var13] += var0[var9++] * var11;
         var13 = var3++;
      }

      for(var12 += 3; var3 < var12; var1[var13] += var0[var9++] * var11) {
         var13 = var3++;
      }

      var8.l = var9 << 8;
      return var3;
   }

   public static int a(int var0, short[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, bT var10, boolean var11) {
      int var12 = var3 >> 8;
      int var13 = var9 >> 8;
      int var14;
      if ((var14 = var4 + var13 - var12) > var8) {
         var14 = var8;
      }

      int var15 = var4 << 1;
      int var16 = var14 << 1;
      var16 -= 6;
      int var17;
      short var18;
      short var19;
      short var20;
      short var21;
      if (var11) {
         while(var15 < var16) {
            var18 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var18 << 2;
            var17 = var15++;
            var2[var17] += var6 * var18 << 2;
            var19 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var19 << 2;
            var17 = var15++;
            var2[var17] += var6 * var19 << 2;
            var20 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var20 << 2;
            var17 = var15++;
            var2[var17] += var6 * var20 << 2;
            var21 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var21 << 2;
            var17 = var15++;
            var2[var17] += var6 * var21 << 2;
         }

         for(var16 += 6; var15 < var16; var2[var17] += var6 * var18 << 2) {
            var18 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var18 << 2;
            var17 = var15++;
         }
      } else {
         while(var15 < var16) {
            var18 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var18 >> 6;
            var17 = var15++;
            var2[var17] += var6 * var18 >> 6;
            var19 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var19 >> 6;
            var17 = var15++;
            var2[var17] += var6 * var19 >> 6;
            var20 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var20 >> 6;
            var17 = var15++;
            var2[var17] += var6 * var20 >> 6;
            var21 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var21 >> 6;
            var17 = var15++;
            var2[var17] += var6 * var21 >> 6;
         }

         for(var16 += 6; var15 < var16; var2[var17] += var6 * var18 >> 6) {
            var18 = var1[var12++];
            var17 = var15++;
            var2[var17] += var5 * var18 >> 6;
            var17 = var15++;
         }
      }

      var10.l = var12 << 8;
      return var15 >> 1;
   }

   public static int b(short[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, bT var8) {
      int var9 = var2 >> 8;
      int var10 = var7 >> 8;
      int var11 = var4 << 2;
      int var12;
      if ((var12 = var3 + var9 - (var10 - 1)) > var6) {
         var12 = var6;
      }

      int var13;
      for(var12 -= 3; var3 < var12; var1[var13] += var0[var9--] * var11) {
         var13 = var3++;
         var1[var13] += var0[var9--] * var11;
         var13 = var3++;
         var1[var13] += var0[var9--] * var11;
         var13 = var3++;
         var1[var13] += var0[var9--] * var11;
         var13 = var3++;
      }

      for(var12 += 3; var3 < var12; var1[var13] += var0[var9--] * var11) {
         var13 = var3++;
      }

      var8.l = var9 << 8;
      return var3;
   }

   public static int a(int var0, short[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, bT var10) {
      int var11 = var3 >> 8;
      int var12 = var9 >> 8;
      int var13;
      if ((var13 = var4 + var11 - (var12 - 1)) > var8) {
         var13 = var8;
      }

      int var14 = var4 << 1;
      int var15 = var13 << 1;

      int var16;
      short var17;
      short var18;
      for(var15 -= 6; var14 < var15; var2[var16] += var6 * var17 >> 6) {
         var18 = var1[var11--];
         var16 = var14++;
         var2[var16] += var5 * var18 >> 6;
         var16 = var14++;
         var2[var16] += var6 * var18 >> 6;
         short var19 = var1[var11--];
         var16 = var14++;
         var2[var16] += var5 * var19 >> 6;
         var16 = var14++;
         var2[var16] += var6 * var19 >> 6;
         short var20 = var1[var11--];
         var16 = var14++;
         var2[var16] += var5 * var20 >> 6;
         var16 = var14++;
         var2[var16] += var6 * var20 >> 6;
         var17 = var1[var11--];
         var16 = var14++;
         var2[var16] += var5 * var17 >> 6;
         var16 = var14++;
      }

      for(var15 += 6; var14 < var15; var2[var16] += var6 * var18 >> 6) {
         var18 = var1[var11--];
         var16 = var14++;
         var2[var16] += var5 * var18 >> 6;
         var16 = var14++;
      }

      var10.l = var11 << 8;
      return var14 >> 1;
   }

   public static int a(int var0, int var1, short[] var2, int[] var3, int var4, int var5, int var6, int var7, int var8, int var9, bT var10, int var11, int var12) {
      int var13;
      if (var11 == 0 || (var13 = (var9 - var4 + var11 - 257) / var11 + var5) > var8) {
         var13 = var8;
      }

      int var14;
      int var15;
      int var16;
      while(var5 < var13) {
         var15 = var4 >> 8;
         var16 = var2[var15];
         var14 = var5++;
         var3[var14] += ((var16 << 8) + (var4 & 255) * (var2[var15 + 1] - var16)) * var6 >> 6;
         var4 += var11;
      }

      if (var11 == 0 || (var15 = (var9 - var4 + var11 - 1) / var11 + var5) > var8) {
         var15 = var8;
      }

      for(var16 = var12; var5 < var15; var4 += var11) {
         short var17 = var2[var4 >> 8];
         var14 = var5++;
         var3[var14] += ((var17 << 8) + (var4 & 255) * (var16 - var17)) * var6 >> 6;
      }

      var10.l = var4;
      return var5;
   }

   public static int a(int var0, int var1, short[] var2, int[] var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, bT var11, int var12, int var13) {
      int var14;
      if (var12 == 0 || (var14 = (var10 - var4 + var12 - 257) / var12 + var5) > var9) {
         var14 = var9;
      }

      int var15 = var5 << 1;

      int var16;
      int var17;
      int var18;
      int var19;
      for(var17 = var14 << 1; var15 < var17; var4 += var12) {
         var18 = var4 >> 8;
         var19 = var2[var18];
         int var20 = ((var4 & 255) * (var2[var18 + 1] - var19) >> 8) + var19;
         var16 = var15++;
         var3[var16] += var6 * var20 >> 6;
         var16 = var15++;
         var3[var16] += var7 * var20 >> 6;
      }

      if (var12 == 0 || (var17 = (var15 >> 1) + (var10 - var4 + var12 - 1) / var12) > var9) {
         var17 = var9;
      }

      var18 = var17 << 1;

      for(var19 = var13; var15 < var18; var4 += var12) {
         short var22 = var2[var4 >> 8];
         int var21 = ((var4 & 255) * (var19 - var22) >> 8) + var22;
         var16 = var15++;
         var3[var16] += var6 * var21 >> 6;
         var16 = var15++;
         var3[var16] += var7 * var21 >> 6;
      }

      var11.l = var4;
      return var15 >> 1;
   }

   public static int b(int var0, int var1, short[] var2, int[] var3, int var4, int var5, int var6, int var7, int var8, int var9, bT var10, int var11, int var12) {
      int var13;
      if (var11 == 0 || (var13 = (var9 + 256 - var4 + var11) / var11 + var5) > var8) {
         var13 = var8;
      }

      int var14;
      int var15;
      int var16;
      while(var5 < var13) {
         var15 = var4 >> 8;
         var16 = var2[var15 - 1];
         var14 = var5++;
         var3[var14] += ((var16 << 8) + (var4 & 255) * (var2[var15] - var16)) * var6 >> 6;
         var4 += var11;
      }

      if (var11 == 0 || (var15 = (var9 - var4 + var11) / var11 + var5) > var8) {
         var15 = var8;
      }

      var16 = var12;

      for(int var17 = var11; var5 < var15; var4 += var17) {
         var14 = var5++;
         var3[var14] += ((var16 << 8) + (var4 & 255) * (var2[var4 >> 8] - var16)) * var6 >> 6;
      }

      var10.l = var4;
      return var5;
   }

   public static int b(int var0, int var1, short[] var2, int[] var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, bT var11, int var12, int var13) {
      int var14;
      if (var12 == 0 || (var14 = (var10 + 256 - var4 + var12) / var12 + var5) > var9) {
         var14 = var9;
      }

      int var15 = var5 << 1;

      int var16;
      int var17;
      int var18;
      int var19;
      int var20;
      for(var17 = var14 << 1; var15 < var17; var4 += var12) {
         var18 = var4 >> 8;
         var19 = var2[var18 - 1];
         var20 = ((var4 & 255) * (var2[var18] - var19) >> 8) + var19;
         var16 = var15++;
         var3[var16] += var6 * var20 >> 6;
         var16 = var15++;
         var3[var16] += var7 * var20 >> 6;
      }

      if (var12 == 0 || (var17 = (var15 >> 1) + (var10 - var4 + var12) / var12) > var9) {
         var17 = var9;
      }

      var18 = var17 << 1;

      for(var19 = var13; var15 < var18; var4 += var12) {
         var20 = ((var4 & 255) * (var2[var4 >> 8] - var19) >> 8) + var19;
         var16 = var15++;
         var3[var16] += var6 * var20 >> 6;
         var16 = var15++;
         var3[var16] += var7 * var20 >> 6;
      }

      var11.l = var4;
      return var15 >> 1;
   }

   public static int a(short[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, bT var9) {
      int var10 = var2 >> 8;
      int var11 = var8 >> 8;
      int var12 = var4 << 2;
      int var13 = var5 << 2;
      int var14;
      if ((var14 = var3 + var11 - var10) > var7) {
         var14 = var7;
      }

      var9.o += (var14 - var3) * var9.t;
      var9.p += (var14 - var3) * var9.q;

      int var15;
      int var16;
      for(var14 -= 3; var3 < var14; var12 = var13 + var16) {
         var15 = var3++;
         var1[var15] += var0[var10++] * var12;
         int var17 = var12 + var13;
         var15 = var3++;
         var1[var15] += var0[var10++] * var17;
         int var18 = var13 + var17;
         var15 = var3++;
         var1[var15] += var0[var10++] * var18;
         var16 = var13 + var18;
         var15 = var3++;
         var1[var15] += var0[var10++] * var16;
      }

      for(var14 += 3; var3 < var14; var12 += var13) {
         var15 = var3++;
         var1[var15] += var0[var10++] * var12;
      }

      var9.m = var12 >> 2;
      var9.l = var10 << 8;
      return var3;
   }

   public static int a(int var0, short[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, bT var12) {
      int var13 = var3 >> 8;
      int var14 = var11 >> 8;
      int var15;
      if ((var15 = var4 + var14 - var13) > var10) {
         var15 = var10;
      }

      var12.m += (var15 - var4) * var12.s;
      int var16 = var4 << 1;
      int var17 = var15 << 1;

      int var18;
      int var19;
      short var20;
      for(var17 -= 6; var16 < var17; var6 = var8 + var19) {
         var20 = var1[var13++];
         var18 = var16++;
         var2[var18] += var5 * var20 >> 6;
         int var21 = var5 + var7;
         var18 = var16++;
         var2[var18] += var6 * var20 >> 6;
         int var22 = var6 + var8;
         short var23 = var1[var13++];
         var18 = var16++;
         var2[var18] += var21 * var23 >> 6;
         int var24 = var7 + var21;
         var18 = var16++;
         var2[var18] += var22 * var23 >> 6;
         int var25 = var8 + var22;
         short var26 = var1[var13++];
         var18 = var16++;
         var2[var18] += var24 * var26 >> 6;
         int var27 = var7 + var24;
         var18 = var16++;
         var2[var18] += var25 * var26 >> 6;
         var19 = var8 + var25;
         short var28 = var1[var13++];
         var18 = var16++;
         var2[var18] += var27 * var28 >> 6;
         var5 = var7 + var27;
         var18 = var16++;
         var2[var18] += var19 * var28 >> 6;
      }

      for(var17 += 6; var16 < var17; var6 += var8) {
         var20 = var1[var13++];
         var18 = var16++;
         var2[var18] += var5 * var20 >> 6;
         var5 += var7;
         var18 = var16++;
         var2[var18] += var6 * var20 >> 6;
      }

      var12.o = var5;
      var12.p = var6;
      var12.l = var13 << 8;
      return var16 >> 1;
   }

   public static int b(short[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, bT var9) {
      int var10 = var2 >> 8;
      int var11 = var8 >> 8;
      int var12 = var4 << 2;
      int var13 = var5 << 2;
      int var14;
      if ((var14 = var3 + var10 - (var11 - 1)) > var7) {
         var14 = var7;
      }

      var9.o += (var14 - var3) * var9.t;
      var9.p += (var14 - var3) * var9.q;

      int var15;
      int var16;
      for(var14 -= 3; var3 < var14; var12 = var13 + var16) {
         var15 = var3++;
         var1[var15] += var0[var10--] * var12;
         int var17 = var12 + var13;
         var15 = var3++;
         var1[var15] += var0[var10--] * var17;
         int var18 = var13 + var17;
         var15 = var3++;
         var1[var15] += var0[var10--] * var18;
         var16 = var13 + var18;
         var15 = var3++;
         var1[var15] += var0[var10--] * var16;
      }

      for(var14 += 3; var3 < var14; var12 += var13) {
         var15 = var3++;
         var1[var15] += var0[var10--] * var12;
      }

      var9.m = var12 >> 2;
      var9.l = var10 << 8;
      return var3;
   }

   public static int b(int var0, short[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, bT var12) {
      int var13 = var3 >> 8;
      int var14 = var11 >> 8;
      int var15;
      if ((var15 = var4 + var13 - (var14 - 1)) > var10) {
         var15 = var10;
      }

      var12.m += (var15 - var4) * var12.s;
      int var16 = var4 << 1;
      int var17 = var15 << 1;

      int var18;
      int var19;
      short var20;
      for(var17 -= 6; var16 < var17; var6 = var8 + var19) {
         var20 = var1[var13--];
         var18 = var16++;
         var2[var18] += var5 * var20 >> 6;
         int var21 = var5 + var7;
         var18 = var16++;
         var2[var18] += var6 * var20 >> 6;
         int var22 = var6 + var8;
         short var23 = var1[var13--];
         var18 = var16++;
         var2[var18] += var21 * var23 >> 6;
         int var24 = var7 + var21;
         var18 = var16++;
         var2[var18] += var22 * var23 >> 6;
         int var25 = var8 + var22;
         short var26 = var1[var13--];
         var18 = var16++;
         var2[var18] += var24 * var26 >> 6;
         int var27 = var7 + var24;
         var18 = var16++;
         var2[var18] += var25 * var26 >> 6;
         var19 = var8 + var25;
         short var28 = var1[var13--];
         var18 = var16++;
         var2[var18] += var27 * var28 >> 6;
         var5 = var7 + var27;
         var18 = var16++;
         var2[var18] += var19 * var28 >> 6;
      }

      for(var17 += 6; var16 < var17; var6 += var8) {
         var20 = var1[var13--];
         var18 = var16++;
         var2[var18] += var5 * var20 >> 6;
         var5 += var7;
         var18 = var16++;
         var2[var18] += var6 * var20 >> 6;
      }

      var12.o = var5;
      var12.p = var6;
      var12.l = var13 << 8;
      return var16 >> 1;
   }

   public static int c(int var0, int var1, short[] var2, int[] var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, bT var11, int var12, int var13) {
      var11.o -= var11.t * var5;
      var11.p -= var11.q * var5;
      int var14;
      if (var12 == 0 || (var14 = (var10 - var4 + var12 - 257) / var12 + var5) > var9) {
         var14 = var9;
      }

      int var15;
      int var16;
      int var17;
      while(var5 < var14) {
         var16 = var4 >> 8;
         var17 = var2[var16];
         var15 = var5++;
         var3[var15] += ((var17 << 8) + (var4 & 255) * (var2[var16 + 1] - var17)) * var6 >> 6;
         var6 += var7;
         var4 += var12;
      }

      if (var12 == 0 || (var16 = (var10 - var4 + var12 - 1) / var12 + var5) > var9) {
         var16 = var9;
      }

      for(var17 = var13; var5 < var16; var4 += var12) {
         short var18 = var2[var4 >> 8];
         var15 = var5++;
         var3[var15] += ((var18 << 8) + (var4 & 255) * (var17 - var18)) * var6 >> 6;
         var6 += var7;
      }

      var11.o += var11.t * var5;
      var11.p += var11.q * var5;
      var11.m = var6;
      var11.l = var4;
      return var5;
   }

   public static int a(int var0, int var1, short[] var2, int[] var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, bT var13, int var14, int var15) {
      var13.m -= var13.s * var5;
      int var16;
      if (var14 == 0 || (var16 = (var12 - var4 + var14 - 257) / var14 + var5) > var11) {
         var16 = var11;
      }

      int var17 = var5 << 1;

      int var18;
      int var19;
      int var20;
      int var21;
      for(var19 = var16 << 1; var17 < var19; var4 += var14) {
         var20 = var4 >> 8;
         var21 = var2[var20];
         int var22 = ((var4 & 255) * (var2[var20 + 1] - var21) >> 8) + var21;
         var18 = var17++;
         var3[var18] += var6 * var22 >> 6;
         var6 += var8;
         var18 = var17++;
         var3[var18] += var7 * var22 >> 6;
         var7 += var9;
      }

      if (var14 == 0 || (var19 = (var17 >> 1) + (var12 - var4 + var14 - 1) / var14) > var11) {
         var19 = var11;
      }

      var20 = var19 << 1;

      for(var21 = var15; var17 < var20; var4 += var14) {
         short var24 = var2[var4 >> 8];
         int var23 = ((var4 & 255) * (var21 - var24) >> 8) + var24;
         var18 = var17++;
         var3[var18] += var6 * var23 >> 6;
         var6 += var8;
         var18 = var17++;
         var3[var18] += var7 * var23 >> 6;
         var7 += var9;
      }

      var21 = var17 >> 1;
      var13.m += var13.s * var21;
      var13.o = var6;
      var13.p = var7;
      var13.l = var4;
      return var21;
   }

   public static int d(int var0, int var1, short[] var2, int[] var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, bT var11, int var12, int var13) {
      var11.o -= var11.t * var5;
      var11.p -= var11.q * var5;
      int var14;
      if (var12 == 0 || (var14 = (var10 + 256 - var4 + var12) / var12 + var5) > var9) {
         var14 = var9;
      }

      int var15;
      int var16;
      int var17;
      while(var5 < var14) {
         var16 = var4 >> 8;
         var17 = var2[var16 - 1];
         var15 = var5++;
         var3[var15] += ((var17 << 8) + (var4 & 255) * (var2[var16] - var17)) * var6 >> 6;
         var6 += var7;
         var4 += var12;
      }

      if (var12 == 0 || (var16 = (var10 - var4 + var12) / var12 + var5) > var9) {
         var16 = var9;
      }

      var17 = var13;

      for(int var18 = var12; var5 < var16; var4 += var18) {
         var15 = var5++;
         var3[var15] += ((var17 << 8) + (var4 & 255) * (var2[var4 >> 8] - var17)) * var6 >> 6;
         var6 += var7;
      }

      var11.o += var11.t * var5;
      var11.p += var11.q * var5;
      var11.m = var6;
      var11.l = var4;
      return var5;
   }

   public static int b(int var0, int var1, short[] var2, int[] var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, bT var13, int var14, int var15) {
      var13.m -= var13.s * var5;
      int var16;
      if (var14 == 0 || (var16 = (var12 + 256 - var4 + var14) / var14 + var5) > var11) {
         var16 = var11;
      }

      int var17 = var5 << 1;

      int var18;
      int var19;
      int var20;
      int var21;
      int var22;
      for(var19 = var16 << 1; var17 < var19; var4 += var14) {
         var20 = var4 >> 8;
         var21 = var2[var20 - 1];
         var22 = ((var4 & 255) * (var2[var20] - var21) >> 8) + var21;
         var18 = var17++;
         var3[var18] += var6 * var22 >> 6;
         var6 += var8;
         var18 = var17++;
         var3[var18] += var7 * var22 >> 6;
         var7 += var9;
      }

      if (var14 == 0 || (var19 = (var17 >> 1) + (var12 - var4 + var14) / var14) > var11) {
         var19 = var11;
      }

      var20 = var19 << 1;

      for(var21 = var15; var17 < var20; var4 += var14) {
         var22 = ((var4 & 255) * (var2[var4 >> 8] - var21) >> 8) + var21;
         var18 = var17++;
         var3[var18] += var6 * var22 >> 6;
         var6 += var8;
         var18 = var17++;
         var3[var18] += var7 * var22 >> 6;
         var7 += var9;
      }

      var21 = var17 >> 1;
      var13.m += var13.s * var21;
      var13.o = var6;
      var13.p = var7;
      var13.l = var4;
      return var21;
   }

   public int a_() {
      int var1 = this.m * 3 >> 6;
      int var2 = (var1 >>> 31) + (var1 ^ var1 >> 31);
      if (this.n == 0) {
         var2 -= this.l * var2 / (((ix)this.G).d.length << 8);
      } else if (this.n >= 0) {
         var2 -= this.f * var2 / ((ix)this.G).d.length;
      }

      return var2 > 255 ? 255 : var2;
   }

   public void b() {
      this.m = this.j;
      this.o = a(this.j, this.k);
      this.p = b(this.j, this.k);
   }

   public synchronized void a(int var1) {
      this.n = var1;
   }

   public synchronized void c(int var1, int var2) {
      this.j = var1;
      this.k = var2;
      this.r = 0;
      this.b();
   }

   public synchronized int c() {
      return this.j == Integer.MIN_VALUE ? 0 : this.j + 32 >> 6;
   }

   public synchronized void b(int var1) {
      this.c(var1 << 6, this.e());
   }

   public synchronized int d() {
      return this.j == Integer.MIN_VALUE ? 0 : this.j;
   }

   public synchronized void c(int var1) {
      this.c(var1, this.e());
   }

   public synchronized int e() {
      return this.k < 0 ? -1 : this.k;
   }

   public int f() {
      ix var1 = (ix)this.G;
      return (int)((double)var1.d.length * 1000.0 / (double)var1.c);
   }

   public synchronized void d(int var1) {
      int var2 = ((ix)this.G).d.length << 8;
      if (var1 < -1) {
         var1 = -1;
      }

      if (var1 > var2) {
         var1 = var2;
      }

      this.l = var1;
   }

   public synchronized void a(boolean var1) {
      this.i = (this.i >>> 31) + (this.i ^ this.i >> 31);
      if (var1) {
         this.i = -this.i;
      }

   }

   public void g() {
      if (this.r != 0) {
         if (this.j == Integer.MIN_VALUE) {
            this.j = 0;
         }

         this.r = 0;
         this.b();
      }

   }

   public synchronized void d(int var1, int var2) {
      this.a(var1, var2, this.e());
   }

   public synchronized void a(int var1, int var2, int var3) {
      if (var1 == 0) {
         this.c(var2, var3);
      } else {
         int var4 = a(var2, var3);
         int var5 = b(var2, var3);
         if (this.o == var4 && this.p == var5) {
            this.r = 0;
         } else {
            int var6 = var2 - this.m;
            if (this.m - var2 > var6) {
               var6 = this.m - var2;
            }

            if (var4 - this.o > var6) {
               var6 = var4 - this.o;
            }

            if (this.o - var4 > var6) {
               var6 = this.o - var4;
            }

            if (var5 - this.p > var6) {
               var6 = var5 - this.p;
            }

            if (this.p - var5 > var6) {
               var6 = this.p - var5;
            }

            if (var1 > var6) {
               var1 = var6;
            }

            this.r = var1;
            this.j = var2;
            this.k = var3;
            this.s = (var2 - this.m) / var1;
            this.t = (var4 - this.o) / var1;
            this.q = (var5 - this.p) / var1;
         }
      }

   }

   public synchronized void e(int var1) {
      if (var1 == 0) {
         this.c(0);
         this.X();
      } else if (this.o == 0 && this.p == 0) {
         this.r = 0;
         this.j = 0;
         this.m = 0;
         this.X();
      } else {
         int var2 = -this.m;
         if (this.m > var2) {
            var2 = this.m;
         }

         if (-this.o > var2) {
            var2 = -this.o;
         }

         if (this.o > var2) {
            var2 = this.o;
         }

         if (-this.p > var2) {
            var2 = -this.p;
         }

         if (this.p > var2) {
            var2 = this.p;
         }

         if (var1 > var2) {
            var1 = var2;
         }

         this.r = var1;
         this.j = Integer.MIN_VALUE;
         this.s = -this.m / var1;
         this.t = -this.o / var1;
         this.q = -this.p / var1;
      }

   }

   public synchronized int h() {
      return this.i < 0 ? -this.i : this.i;
   }

   public synchronized void f(int var1) {
      if (this.i < 0) {
         this.i = -var1;
      } else {
         this.i = var1;
      }

   }

   public boolean l() {
      return this.l < 0 || this.l >= ((ix)this.G).d.length << 8;
   }

   public boolean m() {
      return this.r != 0;
   }

   public cl i() {
      return null;
   }

   public cl j() {
      return null;
   }

   public int k() {
      return this.j == 0 && this.r == 0 ? 0 : 1;
   }

   public synchronized void a(int[] var1, int var2, int var3) {
      if (this.j == 0 && this.r == 0) {
         this.h(var3);
      } else {
         ix var4 = (ix)this.G;
         int var5 = this.f << 8;
         int var6 = this.g << 8;
         int var7 = var4.d.length << 8;
         int var8 = var6 - var5;
         if (var8 <= 0) {
            this.n = 0;
         }

         int var9 = var2;
         int var10 = var2 + var3;
         if (this.l < 0) {
            if (this.i <= 0) {
               this.g();
               this.X();
               return;
            }

            this.l = 0;
         }

         if (this.l >= var7) {
            if (this.i >= 0) {
               this.g();
               this.X();
               return;
            }

            this.l = var7 - 1;
         }

         int var11;
         if (this.n < 0) {
            if (this.h) {
               if (this.i < 0) {
                  var9 = this.b(var1, var2, var5, var10, var4.d[this.f]);
                  if (this.l >= var5) {
                     return;
                  }

                  this.l = var5 + var5 - 1 - this.l;
                  this.i = -this.i;
               }

               while(true) {
                  var11 = this.a(var1, var9, var6, var10, var4.d[this.g - 1]);
                  if (this.l < var6) {
                     return;
                  }

                  this.l = var6 + var6 - 1 - this.l;
                  this.i = -this.i;
                  var9 = this.b(var1, var11, var5, var10, var4.d[this.f]);
                  if (this.l >= var5) {
                     return;
                  }

                  this.l = var5 + var5 - 1 - this.l;
                  this.i = -this.i;
               }
            }

            if (this.i < 0) {
               while(true) {
                  var9 = this.b(var1, var9, var5, var10, var4.d[this.g - 1]);
                  if (this.l >= var5) {
                     return;
                  }

                  this.l = var6 - 1 - (var6 - 1 - this.l) % var8;
               }
            }

            while(true) {
               var9 = this.a(var1, var9, var6, var10, var4.d[this.f]);
               if (this.l < var6) {
                  return;
               }

               this.l = (this.l - var5) % var8 + var5;
            }
         }

         if (this.n > 0) {
            if (this.h) {
               label118: {
                  if (this.i < 0) {
                     var9 = this.b(var1, var2, var5, var10, var4.d[this.f]);
                     if (this.l >= var5) {
                        return;
                     }

                     this.l = var5 + var5 - 1 - this.l;
                     this.i = -this.i;
                     if (--this.n == 0) {
                        break label118;
                     }
                  }

                  do {
                     var9 = this.a(var1, var9, var6, var10, var4.d[this.g - 1]);
                     if (this.l < var6) {
                        return;
                     }

                     this.l = var6 + var6 - 1 - this.l;
                     this.i = -this.i;
                     if (--this.n == 0) {
                        break;
                     }

                     var9 = this.b(var1, var9, var5, var10, var4.d[this.f]);
                     if (this.l >= var5) {
                        return;
                     }

                     this.l = var5 + var5 - 1 - this.l;
                     this.i = -this.i;
                  } while(--this.n != 0);
               }
            } else if (this.i < 0) {
               while(true) {
                  var9 = this.b(var1, var9, var5, var10, var4.d[this.g - 1]);
                  if (this.l >= var5) {
                     return;
                  }

                  var11 = (var6 - 1 - this.l) / var8;
                  if (var11 >= this.n) {
                     this.l += this.n * var8;
                     this.n = 0;
                     break;
                  }

                  this.l += var8 * var11;
                  this.n -= var11;
               }
            } else {
               while(true) {
                  var9 = this.a(var1, var9, var6, var10, var4.d[this.f]);
                  if (this.l < var6) {
                     return;
                  }

                  var11 = (this.l - var5) / var8;
                  if (var11 >= this.n) {
                     this.l -= this.n * var8;
                     this.n = 0;
                     break;
                  }

                  this.l -= var8 * var11;
                  this.n -= var11;
               }
            }
         }

         if (this.i < 0) {
            this.b(var1, var9, 0, var10, 0);
            if (this.l < 0) {
               this.l = -1;
               this.g();
               this.X();
            }
         } else {
            this.a(var1, var9, var7, var10, 0);
            if (this.l >= var7) {
               this.l = var7;
               this.g();
               this.X();
            }
         }
      }

   }

   public synchronized void h(int var1) {
      if (this.r > 0) {
         if (var1 >= this.r) {
            if (this.j == Integer.MIN_VALUE) {
               this.j = 0;
               this.p = 0;
               this.o = 0;
               this.m = 0;
               this.X();
               var1 = this.r;
            }

            this.r = 0;
            this.b();
         } else {
            this.m += this.s * var1;
            this.o += this.t * var1;
            this.p += this.q * var1;
            this.r -= var1;
         }
      }

      ix var2 = (ix)this.G;
      int var3 = this.f << 8;
      int var4 = this.g << 8;
      int var5 = var2.d.length << 8;
      int var6 = var4 - var3;
      if (var6 <= 0) {
         this.n = 0;
      }

      if (this.l < 0) {
         if (this.i <= 0) {
            this.g();
            this.X();
            return;
         }

         this.l = 0;
      }

      if (this.l >= var5) {
         if (this.i >= 0) {
            this.g();
            this.X();
            return;
         }

         this.l = var5 - 1;
      }

      this.l += this.i * var1;
      if (this.n < 0) {
         if (!this.h) {
            if (this.i < 0) {
               if (this.l >= var3) {
                  return;
               }

               this.l = var4 - 1 - (var4 - 1 - this.l) % var6;
            } else {
               if (this.l < var4) {
                  return;
               }

               this.l = (this.l - var3) % var6 + var3;
            }
         } else {
            if (this.i < 0) {
               if (this.l >= var3) {
                  return;
               }

               this.l = var3 + var3 - 1 - this.l;
               this.i = -this.i;
            }

            while(this.l >= var4) {
               this.l = var4 + var4 - 1 - this.l;
               this.i = -this.i;
               if (this.l >= var3) {
                  return;
               }

               this.l = var3 + var3 - 1 - this.l;
               this.i = -this.i;
            }
         }
      } else {
         if (this.n > 0) {
            if (this.h) {
               label117: {
                  if (this.i < 0) {
                     if (this.l >= var3) {
                        return;
                     }

                     this.l = var3 + var3 - 1 - this.l;
                     this.i = -this.i;
                     if (--this.n == 0) {
                        break label117;
                     }
                  }

                  do {
                     if (this.l < var4) {
                        return;
                     }

                     this.l = var4 + var4 - 1 - this.l;
                     this.i = -this.i;
                     if (--this.n == 0) {
                        break;
                     }

                     if (this.l >= var3) {
                        return;
                     }

                     this.l = var3 + var3 - 1 - this.l;
                     this.i = -this.i;
                  } while(--this.n != 0);
               }
            } else {
               label149: {
                  int var7;
                  if (this.i < 0) {
                     if (this.l >= var3) {
                        return;
                     }

                     var7 = (var4 - 1 - this.l) / var6;
                     if (var7 >= this.n) {
                        this.l += this.n * var6;
                        this.n = 0;
                        break label149;
                     }

                     this.l += var6 * var7;
                     this.n -= var7;
                  } else {
                     if (this.l < var4) {
                        return;
                     }

                     var7 = (this.l - var3) / var6;
                     if (var7 >= this.n) {
                        this.l -= this.n * var6;
                        this.n = 0;
                        break label149;
                     }

                     this.l -= var6 * var7;
                     this.n -= var7;
                  }

                  return;
               }
            }
         }

         if (this.i < 0) {
            if (this.l < 0) {
               this.l = -1;
               this.g();
               this.X();
            }
         } else if (this.l >= var5) {
            this.l = var5;
            this.g();
            this.X();
         }
      }

   }

   public int a(int[] var1, int var2, int var3, int var4, int var5) {
      while(true) {
         if (this.r > 0) {
            int var6 = this.r + var2;
            if (var6 > var4) {
               var6 = var4;
            }

            this.r += var2;
            if (this.i == 256 && (this.l & 255) == 0) {
               if (bo.dv) {
                  var2 = a(0, ((ix)this.G).d, var1, this.l, var2, this.o, this.p, this.t, this.q, 0, var6, var3, this);
               } else {
                  var2 = a(((ix)this.G).d, var1, this.l, var2, this.m, this.s, 0, var6, var3, this);
               }
            } else if (bo.dv) {
               var2 = a(0, 0, ((ix)this.G).d, var1, this.l, var2, this.o, this.p, this.t, this.q, 0, var6, var3, this, this.i, var5);
            } else {
               var2 = c(0, 0, ((ix)this.G).d, var1, this.l, var2, this.m, this.s, 0, var6, var3, this, this.i, var5);
            }

            this.r -= var2;
            if (this.r != 0) {
               return var2;
            }

            if (!this.n()) {
               continue;
            }

            return var4;
         }

         if (this.i == 256 && (this.l & 255) == 0) {
            if (bo.dv) {
               return a(0, ((ix)this.G).d, var1, this.l, var2, this.o, this.p, 0, var4, var3, this, ((ix)this.G).a());
            }

            return a(((ix)this.G).d, var1, this.l, var2, this.m, 0, var4, var3, this);
         }

         if (bo.dv) {
            return a(0, 0, ((ix)this.G).d, var1, this.l, var2, this.o, this.p, 0, var4, var3, this, this.i, var5);
         }

         return a(0, 0, ((ix)this.G).d, var1, this.l, var2, this.m, 0, var4, var3, this, this.i, var5);
      }
   }

   public int b(int[] var1, int var2, int var3, int var4, int var5) {
      while(true) {
         if (this.r > 0) {
            int var6 = this.r + var2;
            if (var6 > var4) {
               var6 = var4;
            }

            this.r += var2;
            if (this.i == -256 && (this.l & 255) == 0) {
               if (bo.dv) {
                  var2 = b(0, ((ix)this.G).d, var1, this.l, var2, this.o, this.p, this.t, this.q, 0, var6, var3, this);
               } else {
                  var2 = b(((ix)this.G).d, var1, this.l, var2, this.m, this.s, 0, var6, var3, this);
               }
            } else if (bo.dv) {
               var2 = b(0, 0, ((ix)this.G).d, var1, this.l, var2, this.o, this.p, this.t, this.q, 0, var6, var3, this, this.i, var5);
            } else {
               var2 = d(0, 0, ((ix)this.G).d, var1, this.l, var2, this.m, this.s, 0, var6, var3, this, this.i, var5);
            }

            this.r -= var2;
            if (this.r != 0) {
               return var2;
            }

            if (!this.n()) {
               continue;
            }

            return var4;
         }

         if (this.i == -256 && (this.l & 255) == 0) {
            if (bo.dv) {
               return a(0, ((ix)this.G).d, var1, this.l, var2, this.o, this.p, 0, var4, var3, this);
            }

            return b(((ix)this.G).d, var1, this.l, var2, this.m, 0, var4, var3, this);
         }

         if (bo.dv) {
            return b(0, 0, ((ix)this.G).d, var1, this.l, var2, this.o, this.p, 0, var4, var3, this, this.i, var5);
         }

         return b(0, 0, ((ix)this.G).d, var1, this.l, var2, this.m, 0, var4, var3, this, this.i, var5);
      }
   }

   public boolean n() {
      int var1 = this.j;
      int var2;
      int var3;
      if (var1 == Integer.MIN_VALUE) {
         var2 = 0;
         var3 = 0;
         var1 = 0;
      } else {
         var3 = a(var1, this.k);
         var2 = b(var1, this.k);
      }

      if (this.m == var1 && this.o == var3 && this.p == var2) {
         if (this.j == Integer.MIN_VALUE) {
            this.j = 0;
            this.p = 0;
            this.o = 0;
            this.m = 0;
            this.X();
            return true;
         } else {
            this.b();
            return false;
         }
      } else {
         if (this.m < var1) {
            this.s = 1;
            this.r = var1 - this.m;
         } else if (this.m > var1) {
            this.s = -1;
            this.r = this.m - var1;
         } else {
            this.s = 0;
         }

         if (this.o < var3) {
            this.t = 1;
            if (this.r == 0 || this.r > var3 - this.o) {
               this.r = var3 - this.o;
            }
         } else if (this.o > var3) {
            this.t = -1;
            if (this.r == 0 || this.r > this.o - var3) {
               this.r = this.o - var3;
            }
         } else {
            this.t = 0;
         }

         if (this.p < var2) {
            this.q = 1;
            if (this.r == 0 || this.r > var2 - this.p) {
               this.r = var2 - this.p;
            }
         } else if (this.p > var2) {
            this.q = -1;
            if (this.r == 0 || this.r > this.p - var2) {
               this.r = this.p - var2;
            }
         } else {
            this.q = 0;
         }

         return false;
      }
   }

   public String toString() {
      return super.toString();
   }

   public void g(int var1) {
      this.a(var1);
   }
}
