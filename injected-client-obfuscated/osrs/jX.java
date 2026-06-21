package osrs;

public class jX {
   public static double[] a(double var0, double var2, int var4) {
      int var5 = var4 * 2 + 1;
      double[] var6 = new double[var5];
      int var7 = -var4;

      for(int var8 = 0; var7 <= var4; ++var8) {
         double var9 = ((double)var7 - var0) / var2;
         double var11 = Math.exp(-var9 * var9 / 2.0) / Math.sqrt(6.283185307179586);
         double var13 = var11 / var2;
         var6[var8] = var13;
         ++var7;
      }

      return var6;
   }
}
