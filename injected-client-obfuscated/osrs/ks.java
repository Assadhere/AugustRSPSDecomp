package osrs;

public class ks {
   public static long[] a;

   public static int a(int var0, int var1) {
      int var2;
      for(var2 = 1; var1 > 1; var1 >>= 1) {
         if ((var1 & 1) != 0) {
            var2 = var0 * var2;
         }

         var0 *= var0;
      }

      if (var1 == 1) {
         return var0 * var2;
      } else {
         return var2;
      }
   }

   public static long a(int var0) {
      return a[var0];
   }

   public static int b(int var0) {
      int var1 = (var0 >>> 1 & 1431655765) + (var0 & 1431655765);
      int var2 = (var1 >>> 2 & 858993459) + (var1 & 858993459);
      int var3 = (var2 >>> 4) + var2 & 252645135;
      int var4 = (var3 >>> 8) + var3;
      int var5 = (var4 >>> 16) + var4;
      return var5 & 255;
   }

   public static int a(long var0) {
      long var2 = (var0 >>> 1 & 6148914691236517205L) + (var0 & 6148914691236517205L);
      long var4 = (var2 >>> 2 & 3689348814741910323L) + (var2 & 3689348814741910323L);
      long var6 = (var4 >>> 4) + var4 & 1085102592571150095L;
      long var8 = (var6 >>> 8) + var6;
      long var10 = (var8 >>> 16) + var8;
      long var12 = (var10 >>> 32) + var10;
      return (int)(var12 & 255L);
   }

   public static int a(int var0, int var1, int var2) {
      int var3 = (int)a(var2 - var1 + 1);
      int var4 = var3 << var1;
      return var0 & ~var4;
   }

   public static long a(long var0, int var2, int var3) {
      long var4 = a(var3 - var2 + 1);
      long var6 = var4 << var2;
      return var0 & ~var6;
   }

   public static int b(int var0, int var1, int var2) {
      int var3 = (int)a(var2 - var1 + 1);
      int var4 = var3 << var1;
      int var5 = var0 | var4;
      return var5;
   }

   public static int c(int var0) {
      --var0;
      int var1 = var0 | var0 >>> 1;
      int var2 = var1 | var1 >>> 2;
      int var3 = var2 | var2 >>> 4;
      int var4 = var3 | var3 >>> 8;
      int var5 = var4 | var4 >>> 16;
      return var5 + 1;
   }

   public static int d(int var0) {
      int var1 = 0;
      if (var0 < 0 || var0 >= 65536) {
         var0 >>>= 16;
         var1 += 16;
      }

      if (var0 >= 256) {
         var0 >>>= 8;
         var1 += 8;
      }

      if (var0 >= 16) {
         var0 >>>= 4;
         var1 += 4;
      }

      if (var0 >= 4) {
         var0 >>>= 2;
         var1 += 2;
      }

      if (var0 >= 1) {
         var0 >>>= 1;
         ++var1;
      }

      return var0 + var1;
   }

   public static int b(int var0, int var1) {
      int var2 = var0 >>> 31;
      return (var0 + var2) / var1 - var2;
   }

   public static int c(int var0, int var1, int var2) {
      if (var1 < var0) {
         throw new IllegalArgumentException("max: " + var1 + " can not be lower than min: " + var0);
      } else {
         return Math.max(var0, Math.min(var2, var1));
      }
   }

   public static float a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10) {
      float var11 = (float)((var2 - var4) * (var6 - var7) + (var4 - var3) * (var5 - var7));
      float var12 = (float)((var0 - var4) * (var6 - var7) + (var1 - var7) * (var4 - var3)) / var11;
      float var13 = (float)((var0 - var4) * (var7 - var5) + (var1 - var7) * (var2 - var4)) / var11;
      float var14 = 1.0F - var12 - var13;
      return var10 * var14 + var8 * var12 + var9 * var13;
   }

   static {
      new Object();
      a = new long[65];
      a[0] = 0L;
      long var0 = 2L;

      for(int var2 = 1; var2 < a.length; ++var2) {
         a[var2] = var0 - 1L;
         var0 += var0;
      }

   }
}
