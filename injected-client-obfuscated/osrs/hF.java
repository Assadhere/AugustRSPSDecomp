package osrs;

public final class hF {
   public static int a = ((int)(Math.random() * 17.0) - 8) * -107570457;
   public static int b = ((int)(Math.random() * 33.0) - 16) * -1749824153;
   public static final int[] c = new int[]{1, 2, 4, 8};
   public static final int[] d = new int[]{16, 32, 64, 128};
   public static final int[] e = new int[]{1, 0, -1, 0};
   public static final int[] f = new int[]{0, -1, 0, 1};
   public static final int[] g = new int[]{1, -1, -1, 1};
   public static final int[] h = new int[]{-1, -1, 1, 1};
   public static int i = 99;

   public static final int a(int var0, int var1, int var2) {
      int var3 = var0 / var2;
      int var4 = var0 & var2 - 1;
      int var5 = var1 / var2;
      int var6 = var1 & var2 - 1;
      int var7 = a(var3, var5);
      int var8 = a(var3 + 1, var5);
      int var9 = a(var3, var5 + 1);
      int var10 = a(var3 + 1, var5 + 1);
      int var11 = a(var7, var8, var4, var2);
      int var12 = a(var9, var10, var4, var2);
      return a(var11, var12, var6, var2);
   }

   public static final int a(int var0, int var1, int var2, int var3) {
      int var4 = 65536 - aW.e[var2 * 1024 / var3] >> 1;
      return ((65536 - var4) * var0 >> 16) + (var1 * var4 >> 16);
   }

   public static final int a(int var0, int var1) {
      int var2 = b(var0 - 1, var1 - 1) + b(var0 + 1, var1 - 1) + b(var0 - 1, var1 + 1) + b(var0 + 1, var1 + 1);
      int var3 = b(var0 - 1, var1) + b(var0 + 1, var1) + b(var0, var1 - 1) + b(var0, var1 + 1);
      int var4 = b(var0, var1);
      return var4 / 4 + var2 / 16 + var3 / 8;
   }

   public static final int b(int var0, int var1) {
      int var2 = var1 * 57 + var0;
      int var3 = var2 << 13 ^ var2;
      int var4 = (var3 * var3 * 15731 + 789221) * var3 + 1376312589 & Integer.MAX_VALUE;
      return var4 >> 19 & 255;
   }
}
