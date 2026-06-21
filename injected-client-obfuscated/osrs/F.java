package osrs;

public class F {
   public static final int a = (int)(Math.pow(2.0, 4.0) - 1.0);
   public static final int b = (int)(Math.pow(2.0, 8.0) - 1.0);

   public static int a(int var0) {
      return var0 >>> 12;
   }

   public static int b(int var0) {
      return var0 >>> 4 & b;
   }

   public static int c(int var0) {
      return (var0 & a) - 1;
   }

   public static Object[] a(aR var0, int[] var1) {
      int var2 = var0.q();
      Object[] var3 = new Object[var1.length * var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         for(int var5 = 0; var5 < var1.length; ++var5) {
            int var6 = var1.length * var4 + var5;
            i var7 = bj.b(var1[var5]);
            var3[var6] = var7.a(var0);
         }
      }

      return var3;
   }
}
