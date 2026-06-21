package osrs;

public final class hZ {
   public int[] a = new int[256];
   public int[] b = new int[256];
   public int c;
   public int d;
   public int e;
   public int f;

   public hZ(int[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.b[var2] = var1[var2];
      }

      this.d();
   }

   public final int a() {
      if (this.d == 0) {
         this.c();
         this.d = 256;
      }

      return this.b[--this.d];
   }

   public final int b() {
      if (this.d == 0) {
         this.c();
         this.d = 256;
      }

      return this.b[this.d - 1];
   }

   public void c() {
      this.e += ++this.f;

      for(int var1 = 0; var1 < 256; ++var1) {
         int var2 = this.a[var1];
         if ((var1 & 2) == 0) {
            if ((var1 & 1) == 0) {
               this.c ^= this.c << 13;
            } else {
               this.c ^= this.c >>> 6;
            }
         } else if ((var1 & 1) == 0) {
            this.c ^= this.c << 2;
         } else {
            this.c ^= this.c >>> 16;
         }

         this.c += this.a[var1 + 128 & 255];
         int var3;
         this.a[var1] = var3 = this.e + this.c + this.a[(var2 & 1020) >> 2];
         this.b[var1] = this.e = this.a[(var3 >> 8 & 1020) >> 2] + var2;
      }

   }

   public final void d() {
      int var1 = -1640531527;
      int var2 = -1640531527;
      int var3 = -1640531527;
      int var4 = -1640531527;
      int var5 = -1640531527;
      int var6 = -1640531527;
      int var7 = -1640531527;
      int var8 = -1640531527;

      int var9;
      int var10;
      int var11;
      int var12;
      int var13;
      int var14;
      int var15;
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
      for(var9 = 0; var9 < 4; ++var9) {
         var10 = var8 ^ var7 << 11;
         var11 = var5 + var10;
         var12 = var6 + var7;
         var13 = var12 ^ var6 >>> 2;
         var14 = var4 + var13;
         var15 = var6 + var11;
         var16 = var15 ^ var11 << 8;
         var17 = var3 + var16;
         var18 = var11 + var14;
         var5 = var18 ^ var14 >>> 16;
         var19 = var2 + var5;
         var20 = var14 + var17;
         var4 = var20 ^ var17 << 10;
         var21 = var1 + var4;
         var22 = var17 + var19;
         var3 = var22 ^ var19 >>> 4;
         var23 = var3 + var10;
         var24 = var19 + var21;
         var2 = var24 ^ var21 << 8;
         var7 = var2 + var13;
         var25 = var21 + var23;
         var1 = var25 ^ var23 >>> 9;
         var6 = var1 + var16;
         var8 = var7 + var23;
      }

      int var26;
      int var27;
      int var28;
      int var29;
      int var30;
      int var31;
      int var32;
      int var33;
      for(var9 = 0; var9 < 256; var9 += 8) {
         var10 = this.b[var9] + var8;
         var11 = this.b[var9 + 1] + var7;
         var12 = this.b[var9 + 2] + var6;
         var13 = this.b[var9 + 3] + var5;
         var14 = this.b[var9 + 4] + var4;
         var15 = this.b[var9 + 5] + var3;
         var16 = this.b[var9 + 6] + var2;
         var17 = this.b[var9 + 7] + var1;
         var18 = var10 ^ var11 << 11;
         var19 = var13 + var18;
         var20 = var11 + var12;
         var21 = var20 ^ var12 >>> 2;
         var22 = var14 + var21;
         var23 = var12 + var19;
         var24 = var23 ^ var19 << 8;
         var25 = var15 + var24;
         var26 = var19 + var22;
         var5 = var26 ^ var22 >>> 16;
         var27 = var5 + var16;
         var28 = var22 + var25;
         var4 = var28 ^ var25 << 10;
         var29 = var4 + var17;
         var30 = var25 + var27;
         var3 = var30 ^ var27 >>> 4;
         var31 = var3 + var18;
         var32 = var27 + var29;
         var2 = var32 ^ var29 << 8;
         var7 = var2 + var21;
         var33 = var29 + var31;
         var1 = var33 ^ var31 >>> 9;
         var6 = var1 + var24;
         var8 = var7 + var31;
         this.a[var9] = var8;
         this.a[var9 + 1] = var7;
         this.a[var9 + 2] = var6;
         this.a[var9 + 3] = var5;
         this.a[var9 + 4] = var4;
         this.a[var9 + 5] = var3;
         this.a[var9 + 6] = var2;
         this.a[var9 + 7] = var1;
      }

      for(var9 = 0; var9 < 256; var9 += 8) {
         var10 = this.a[var9] + var8;
         var11 = this.a[var9 + 1] + var7;
         var12 = this.a[var9 + 2] + var6;
         var13 = this.a[var9 + 3] + var5;
         var14 = this.a[var9 + 4] + var4;
         var15 = this.a[var9 + 5] + var3;
         var16 = this.a[var9 + 6] + var2;
         var17 = this.a[var9 + 7] + var1;
         var18 = var10 ^ var11 << 11;
         var19 = var13 + var18;
         var20 = var11 + var12;
         var21 = var20 ^ var12 >>> 2;
         var22 = var14 + var21;
         var23 = var12 + var19;
         var24 = var23 ^ var19 << 8;
         var25 = var15 + var24;
         var26 = var19 + var22;
         var5 = var26 ^ var22 >>> 16;
         var27 = var5 + var16;
         var28 = var22 + var25;
         var4 = var28 ^ var25 << 10;
         var29 = var4 + var17;
         var30 = var25 + var27;
         var3 = var30 ^ var27 >>> 4;
         var31 = var3 + var18;
         var32 = var27 + var29;
         var2 = var32 ^ var29 << 8;
         var7 = var2 + var21;
         var33 = var29 + var31;
         var1 = var33 ^ var31 >>> 9;
         var6 = var1 + var24;
         var8 = var7 + var31;
         this.a[var9] = var8;
         this.a[var9 + 1] = var7;
         this.a[var9 + 2] = var6;
         this.a[var9 + 3] = var5;
         this.a[var9 + 4] = var4;
         this.a[var9 + 5] = var3;
         this.a[var9 + 6] = var2;
         this.a[var9 + 7] = var1;
      }

      this.c();
      this.d = 256;
   }
}
