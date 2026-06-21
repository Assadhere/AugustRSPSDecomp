package osrs;

public class A {
   public static boolean a(float var0) {
      return var0 < 9.765625E-4F && var0 > -9.765625E-4F;
   }

   public static int a(int var0, int var1) {
      return (int)(Math.atan2((double)var0, (double)var1) * 325.94932345220167) & 2047;
   }

   public static boolean a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return !a(var0, var1, var2, var3, var4, var5) ? false : a(var0, var1, var4, var5, var6, var7);
   }

   public static boolean a(int var0, int var1, int var2, int var3, int var4, int var5) {
      int var6 = var4 - var2;
      int var7 = var5 - var3;
      int var8 = var0 - var2;
      int var9 = var1 - var3;
      int var10 = a(var6, var7, var8, var9);
      int var11 = var6 * var6 + var7 * var7;
      return 0 <= var10 && var10 <= var11;
   }

   public static int a(int var0, int var1, int var2, int var3) {
      return var0 * var2 + var1 * var3;
   }

   public static long b(int var0, int var1, int var2, int var3) {
      long var4 = 1073741824L;
      long var6 = -1073741824L;
      if (var0 != var2) {
         long var8 = (long)(var3 - var1);
         long var10 = (var8 << 15) / (long)(var2 - var0);
         return Math.min(1073741824L, Math.max(-1073741824L, var10));
      } else {
         return var1 > var3 ? -1073741824L : 1073741824L;
      }
   }

   public static boolean b(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      long var8 = b(var0, var1, var2, var3);
      long var10 = b(var4, var5, var6, var7);
      if (var8 == var10) {
         return false;
      } else {
         long var12 = ((long)var1 << 15) - (long)var0 * var8;
         long var14 = ((long)var5 << 15) - (long)var4 * var10;
         int var16 = -((int)((var12 - var14) / (var8 - var10)));
         if (Math.min(var0, var2) <= var16 && Math.max(var0, var2) >= var16 && Math.min(var4, var6) <= var16 && Math.max(var4, var6) >= var16) {
            long var17;
            if (Math.abs(var8) < Math.abs(var10)) {
               var17 = (long)var16 * var8 + var12 >> 15;
            } else {
               var17 = (long)var16 * var10 + var14 >> 15;
            }

            return (long)Math.min(var1, var3) <= var17 && (long)Math.max(var1, var3) >= var17 && (long)Math.min(var5, var7) <= var17 && (long)Math.max(var5, var7) >= var17;
         } else {
            return false;
         }
      }
   }

   public static int b(int var0, int var1) {
      return var0 >= 0 ? (var1 / 2 + var0) / var1 : -((var1 / 2 + -var0) / var1);
   }
}
