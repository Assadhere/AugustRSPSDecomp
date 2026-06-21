package osrs;

public class bU {
   public iy a;
   public boolean b;
   public int[] c;
   public int[] d;
   public boolean[] e;

   public bU(iy var1, boolean var2, int[] var3, int[] var4, boolean[] var5) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
   }

   public void a(float[] var1, int var2) {
      int var3 = this.a.i.length;
      iy var4 = this.a;
      int var5 = iy.a[this.a.h - 1];
      boolean[] var6 = this.e;
      this.e[1] = true;
      var6[0] = true;

      int var7;
      int var8;
      int var9;
      int var10;
      int var11;
      for(var7 = 2; var7 < var3; ++var7) {
         var8 = this.a.a(this.c, var7);
         var9 = this.a.b(this.c, var7);
         var10 = this.a.a(this.c[var8], this.d[var8], this.c[var9], this.d[var9], this.c[var7]);
         var11 = this.d[var7];
         int var12 = var5 - var10;
         int var13 = (var12 < var10 ? var12 : var10) << 1;
         if (var11 != 0) {
            boolean[] var14 = this.e;
            this.e[var9] = true;
            var14[var8] = true;
            this.e[var7] = true;
            if (var11 >= var13) {
               this.d[var7] = var12 > var10 ? var11 - var10 + var10 : var10 - var11 + var12 - 1;
            } else {
               this.d[var7] = (var11 & 1) != 0 ? var10 - (var11 + 1) / 2 : var11 / 2 + var10;
            }
         } else {
            this.e[var7] = false;
            this.d[var7] = var10;
         }
      }

      this.a(0, var3 - 1);
      var7 = 0;
      var8 = this.d[0] * this.a.h;

      for(var9 = 1; var9 < var3; ++var9) {
         if (this.e[var9]) {
            var10 = this.c[var9];
            var11 = this.d[var9] * this.a.h;
            this.a.a(var7, var8, var10, var11, var1, var2);
            if (var10 >= var2) {
               return;
            }

            var7 = var10;
            var8 = var11;
         }
      }

      var4 = this.a;
      float var15 = iy.b[var8];

      for(var10 = var7; var10 < var2; ++var10) {
         var1[var10] *= var15;
      }

   }

   public boolean a() {
      return this.b;
   }

   public void a(int var1, int var2) {
      if (var1 < var2) {
         int var3 = var1;
         int var4 = this.c[var1];
         int var5 = this.d[var1];
         boolean var6 = this.e[var1];

         for(int var7 = var1 + 1; var7 <= var2; ++var7) {
            int var8 = this.c[var7];
            if (var8 < var4) {
               this.c[var3] = var8;
               this.d[var3] = this.d[var7];
               this.e[var3] = this.e[var7];
               ++var3;
               this.c[var7] = this.c[var3];
               this.d[var7] = this.d[var3];
               this.e[var7] = this.e[var3];
            }
         }

         this.c[var3] = var4;
         this.d[var3] = var5;
         this.e[var3] = var6;
         this.a(var1, var3 - 1);
         this.a(var3 + 1, var2);
      }

   }
}
