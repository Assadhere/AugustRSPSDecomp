package osrs;

import java.util.HashMap;

public class fL {
   public final HashMap a = new HashMap();
   public ba b = new ba(0, 0);
   public int[] c = new int[2048];
   public int[] d = new int[2048];
   public int e = 0;

   public fL() {
      bo.eE = new int[2000];
      int var1 = 0;
      int var2 = 240;

      int var3;
      int var4;
      for(var3 = 12; var1 < 16; var2 -= var3) {
         var4 = jr.a((double)((float)var2 / 360.0F), 0.9998999834060669, (double)((float)var1 * 0.425F / 16.0F + 0.075F));
         bo.eE[var1] = var4;
         ++var1;
      }

      var3 = 48;

      for(var4 = var3 / 6; var1 < bo.eE.length; var3 -= var4) {
         int var5 = var1 * 2;

         for(int var6 = jr.a((double)((float)var3 / 360.0F), 0.9998999834060669, 0.5); var1 < var5 && var1 < bo.eE.length; ++var1) {
            bo.eE[var1] = var6;
         }
      }

   }

   public void a(int var1) {
      int var2 = var1 * 2 + 1;
      double[] var3 = jX.a(0.0, (double)((float)var1 / 3.0F), var1);
      double var4 = var3[var1] * var3[var1];
      int[] var6 = new int[var2 * var2];
      boolean var7 = false;

      for(int var8 = 0; var8 < var2; ++var8) {
         for(int var9 = 0; var9 < var2; ++var9) {
            int var10 = var6[var2 * var8 + var9] = (int)(var3[var8] * var3[var9] / var4 * 256.0);
            if (!var7 && var10 > 0) {
               var7 = true;
            }
         }
      }

      aV var11 = new aV(var6, var2, var2);
      this.a.put(var1, var11);
   }

   public aV b(int var1) {
      if (!this.a.containsKey(var1)) {
         this.a(var1);
      }

      return (aV)this.a.get(var1);
   }

   public final void a(int var1, int var2) {
      if (this.e < this.c.length) {
         this.c[this.e] = var1;
         this.d[this.e] = var2;
         ++this.e;
      }

   }

   public void a() {
      this.e = 0;
   }

   public final void a(int var1, int var2, aV var3, float var4) {
      int var5 = (int)(var4 * 18.0F);
      aV var6 = this.b(var5);
      int var7 = var5 * 2 + 1;
      ba var8 = new ba(0, 0, var3.l, var3.m);
      ba var9 = new ba(0, 0);
      this.b.b(var7, var7);
      System.nanoTime();

      int var10;
      int var11;
      int var12;
      for(var10 = 0; var10 < this.e; ++var10) {
         var11 = this.c[var10];
         var12 = this.d[var10];
         int var13 = (int)((float)(var11 - var1) * var4) - var5;
         int var14 = (int)((float)var3.m - (float)(var12 - var2) * var4) - var5;
         this.b.a(var13, var14);
         this.b.a(var8, var9);
         this.a(var6, var3, var9);
      }

      System.nanoTime();
      System.nanoTime();

      for(var10 = 0; var10 < var3.k.length; ++var10) {
         if (var3.k[var10] == 0) {
            var3.k[var10] = -16777216;
         } else {
            var11 = (var3.k[var10] + 64 - 1) / 256;
            if (var11 <= 0) {
               var3.k[var10] = -16777216;
            } else {
               if (var11 > bo.eE.length) {
                  var11 = bo.eE.length;
               }

               var12 = bo.eE[var11 - 1];
               var3.k[var10] = -16777216 | var12;
            }
         }
      }

      System.nanoTime();
   }

   public void a(aV var1, aV var2, ba var3) {
      if (var3.d != 0 && var3.f != 0) {
         int var4 = 0;
         int var5 = 0;
         if (var3.c == 0) {
            var4 = var1.l - var3.d;
         }

         if (var3.e == 0) {
            var5 = var1.m - var3.f;
         }

         int var6 = var1.l * var5 + var4;
         int var7 = var3.e * var2.l + var3.c;

         for(int var8 = 0; var8 < var3.f; ++var8) {
            for(int var9 = 0; var9 < var3.d; ++var9) {
               int[] var10 = var2.k;
               int var11 = var7++;
               var10[var11] += var1.k[var6++];
            }

            var6 += var1.l - var3.d;
            var7 += var2.l - var3.d;
         }
      }

   }
}
