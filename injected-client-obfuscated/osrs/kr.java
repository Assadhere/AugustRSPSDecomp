package osrs;

import java.util.Random;

public class kr {
   public static void a(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      int var5;
      if (var0 == var2) {
         if (var1 == var3) {
            return;
         }

         if (var3 > var1 && var3 < var1 + var4) {
            --var4;
            var5 = var1 + var4;
            int var6 = var3 + var4;
            int var7 = var5 - var4;

            for(var7 += 7; var5 >= var7; var2[var6--] = var0[var5--]) {
               var2[var6--] = var0[var5--];
               var2[var6--] = var0[var5--];
               var2[var6--] = var0[var5--];
               var2[var6--] = var0[var5--];
               var2[var6--] = var0[var5--];
               var2[var6--] = var0[var5--];
               var2[var6--] = var0[var5--];
            }

            for(var7 -= 7; var5 >= var7; var2[var6--] = var0[var5--]) {
            }

            return;
         }
      }

      var5 = var1 + var4;

      for(var5 -= 7; var1 < var5; var2[var3++] = var0[var1++]) {
         var2[var3++] = var0[var1++];
         var2[var3++] = var0[var1++];
         var2[var3++] = var0[var1++];
         var2[var3++] = var0[var1++];
         var2[var3++] = var0[var1++];
         var2[var3++] = var0[var1++];
         var2[var3++] = var0[var1++];
      }

      for(var5 += 7; var1 < var5; var2[var3++] = var0[var1++]) {
      }

   }

   public static void a(int[] var0, int var1, int var2) {
      int var3;
      for(var3 = var1 + var2 - 7; var1 < var3; var0[var1++] = 0) {
         var0[var1++] = 0;
         var0[var1++] = 0;
         var0[var1++] = 0;
         var0[var1++] = 0;
         var0[var1++] = 0;
         var0[var1++] = 0;
         var0[var1++] = 0;
      }

      for(var3 += 7; var1 < var3; var0[var1++] = 0) {
      }

   }

   public static Random a(int var0, int var1) {
      if (var0 == 0 && var1 == 0) {
         var0 = (int)(Math.random() * 2.147483647E9);
         var1 = (int)(Math.random() * 2.147483647E9);
      }

      long var2 = (long)var0 << 32 | (long)var1;
      return new Random(var2);
   }

   public static void a(int[] var0, int var1, int var2, int var3) {
      Random var4 = a(var2, var3);

      for(int var5 = var1 - 1; var5 > 0; --var5) {
         int var6 = var4.nextInt(var5 + 1);
         if (var5 != var6) {
            int var7 = var0[var5];
            var0[var5] = var0[var6];
            var0[var6] = var7;
         }
      }

   }

   public static void a(long[] var0, int var1, int var2, int var3) {
      Random var4 = a(var2, var3);

      for(int var5 = var1 - 1; var5 > 0; --var5) {
         int var6 = var4.nextInt(var5 + 1);
         if (var5 != var6) {
            long var7 = var0[var5];
            var0[var5] = var0[var6];
            var0[var6] = var7;
         }
      }

   }

   public static void a(Object[] var0, int var1, int var2, int var3) {
      Random var4 = a(var2, var3);

      for(int var5 = var1 - 1; var5 > 0; --var5) {
         int var6 = var4.nextInt(var5 + 1);
         if (var5 != var6) {
            Object var7 = var0[var5];
            var0[var5] = var0[var6];
            var0[var6] = var7;
         }
      }

   }
}
