package osrs;

public class cJ {
   public final int a;
   public final int b;
   public final String c;

   public cJ(int var1, int var2, String var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public cJ(aR var1) {
      this(var1.b(), var1.b(), var1.m());
   }

   public String a() {
      String var10000 = Integer.toHexString(this.a);
      return var10000 + Integer.toHexString(this.b) + this.c;
   }

   public int b() {
      return this.b;
   }
}
