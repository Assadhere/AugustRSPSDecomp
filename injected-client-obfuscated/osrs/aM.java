package osrs;

public class aM {
   public static final aM a = new aM(6, 0, "", "");
   public static final aM b = new aM(1, 1, "", "");
   public static final aM c = new aM(2, 2, "", "");
   public static final aM d = new aM(5, 3, "", "");
   public static final aM e = new aM(3, 4, "", "");
   public static final aM f = new aM(7, 5, "", "");
   public static final aM g = new aM(4, 6, "", "");
   public static final aM h = new aM(0, 7, "", "");
   public static final aM i;
   public final int j;
   public final String k;

   public aM(int var1, int var2, String var3, String var4) {
      this.j = var1;
      this.k = var4;
   }

   public aM(int var1, int var2, String var3, String var4, boolean var5, aM[] var6) {
      this.j = var1;
      this.k = var4;
   }

   public String toString() {
      return this.k;
   }

   static {
      i = new aM(8, -1, "", "", true, new aM[]{a, b, c, e, d});
   }
}
