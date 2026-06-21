package osrs;

public class kl {
   public static void a(String[] var0, short[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         String var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         short var7 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var7;

         for(int var8 = var2; var8 < var3; ++var8) {
            if (var6 == null || var0[var8] != null && var0[var8].compareTo(var6) < (var8 & 1)) {
               String var9 = var0[var8];
               var0[var8] = var0[var5];
               var0[var5] = var9;
               short var10 = var1[var8];
               var1[var8] = var1[var5];
               var1[var5++] = var10;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var7;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(String[] var0, int[] var1) {
      a((String[])var0, (int[])var1, 0, var0.length - 1);
   }

   public static void a(String[] var0, int[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         String var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         int var7 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var7;

         for(int var8 = var2; var8 < var3; ++var8) {
            if (var6 == null || var0[var8] != null && var0[var8].compareTo(var6) < (var8 & 1)) {
               String var9 = var0[var8];
               var0[var8] = var0[var5];
               var0[var5] = var9;
               int var10 = var1[var8];
               var1[var8] = var1[var5];
               var1[var5++] = var10;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var7;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(String[] var0, long[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         String var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         long var7 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var7;

         for(int var9 = var2; var9 < var3; ++var9) {
            if (var6 == null || var0[var9] != null && var0[var9].compareTo(var6) < (var9 & 1)) {
               String var10 = var0[var9];
               var0[var9] = var0[var5];
               var0[var5] = var10;
               long var11 = var1[var9];
               var1[var9] = var1[var5];
               var1[var5++] = var11;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var7;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(String[] var0, Object[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         String var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         Object var7 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var7;

         for(int var8 = var2; var8 < var3; ++var8) {
            if (var6 == null || var0[var8] != null && var0[var8].compareTo(var6) < (var8 & 1)) {
               String var9 = var0[var8];
               var0[var8] = var0[var5];
               var0[var5] = var9;
               Object var10 = var1[var8];
               var1[var8] = var1[var5];
               var1[var5++] = var10;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var7;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(int[] var0, int[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         int var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         int var7 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var7;
         int var8 = var6 == Integer.MAX_VALUE ? 0 : 1;

         for(int var9 = var2; var9 < var3; ++var9) {
            if (var0[var9] < (var9 & var8) + var6) {
               int var10 = var0[var9];
               var0[var9] = var0[var5];
               var0[var5] = var10;
               int var11 = var1[var9];
               var1[var9] = var1[var5];
               var1[var5++] = var11;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var7;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(int[] var0, long[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         int var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         long var7 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var7;
         int var9 = var6 == Integer.MAX_VALUE ? 0 : 1;

         for(int var10 = var2; var10 < var3; ++var10) {
            if (var0[var10] < (var10 & var9) + var6) {
               int var11 = var0[var10];
               var0[var10] = var0[var5];
               var0[var5] = var11;
               long var12 = var1[var10];
               var1[var10] = var1[var5];
               var1[var5++] = var12;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var7;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(long[] var0, int[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         long var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         int var8 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var8;
         int var9 = var6 == Long.MAX_VALUE ? 0 : 1;

         for(int var10 = var2; var10 < var3; ++var10) {
            if (var0[var10] < (long)(var10 & var9) + var6) {
               long var11 = var0[var10];
               var0[var10] = var0[var5];
               var0[var5] = var11;
               int var13 = var1[var10];
               var1[var10] = var1[var5];
               var1[var5++] = var13;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var8;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(long[] var0, long[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         long var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         long var8 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var8;
         long var10 = var6 == Long.MAX_VALUE ? 0L : 1L;

         for(int var12 = var2; var12 < var3; ++var12) {
            if (var0[var12] < ((long)var12 & var10) + var6) {
               long var13 = var0[var12];
               var0[var12] = var0[var5];
               var0[var5] = var13;
               long var15 = var1[var12];
               var1[var12] = var1[var5];
               var1[var5++] = var15;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var8;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(float[] var0, int[] var1) {
      a((float[])var0, (int[])var1, 0, var0.length - 1);
   }

   public static void a(float[] var0, int[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         float var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         int var7 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var7;

         for(int var8 = var2; var8 < var3; ++var8) {
            if (var0[var8] < var6) {
               float var9 = var0[var8];
               var0[var8] = var0[var5];
               var0[var5] = var9;
               int var10 = var1[var8];
               var1[var8] = var1[var5];
               var1[var5++] = var10;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var7;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(int[] var0, Object[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         int var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         Object var7 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var7;
         int var8 = var6 == Integer.MAX_VALUE ? 0 : 1;

         for(int var9 = var2; var9 < var3; ++var9) {
            if (var0[var9] < (var9 & var8) + var6) {
               int var10 = var0[var9];
               var0[var9] = var0[var5];
               var0[var5] = var10;
               Object var11 = var1[var9];
               var1[var9] = var1[var5];
               var1[var5++] = var11;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var7;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }

   public static void a(long[] var0, Object[] var1, int var2, int var3) {
      if (var2 < var3) {
         int var4 = (var2 + var3) / 2;
         int var5 = var2;
         long var6 = var0[var4];
         var0[var4] = var0[var3];
         var0[var3] = var6;
         Object var8 = var1[var4];
         var1[var4] = var1[var3];
         var1[var3] = var8;
         int var9 = var6 == Long.MAX_VALUE ? 0 : 1;

         for(int var10 = var2; var10 < var3; ++var10) {
            if (var0[var10] < (long)(var10 & var9) + var6) {
               long var11 = var0[var10];
               var0[var10] = var0[var5];
               var0[var5] = var11;
               Object var13 = var1[var10];
               var1[var10] = var1[var5];
               var1[var5++] = var13;
            }
         }

         var0[var3] = var0[var5];
         var0[var5] = var6;
         var1[var3] = var1[var5];
         var1[var5] = var8;
         a(var0, var1, var2, var5 - 1);
         a(var0, var1, var5 + 1, var3);
      }

   }
}
