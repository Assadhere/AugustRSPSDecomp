package osrs;

public class eK {
   public static final eK a = new eK(14, 0);
   public static final eK b = new eK(15, 20);
   public static final eK c = new eK(16, -2);
   public static final eK d = new eK(18, -2);
   public static final eK e = new eK(19, -2);
   public static final eK f = new eK(21, 37);
   public static final eK g = new eK(27, 0);
   public static final eK h = new eK(32, 66);
   public static final eK[] i = new eK[33];
   public final int j;
   public final int k;

   public static eK[] a() {
      return new eK[]{c, b, f, g, h, d, a, e};
   }

   public eK(int var1, int var2) {
      this.j = var1;
      this.k = var2;
   }

   static {
      eK[] var0 = a();

      for(int var1 = 0; var1 < var0.length; ++var1) {
         i[var0[var1].j] = var0[var1];
      }

   }
}
