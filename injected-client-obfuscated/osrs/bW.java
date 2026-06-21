package osrs;

public class bW extends gN {
   public final boolean a;

   public bW(boolean var1) {
      this.a = var1;
   }

   public int a(gP var1, gP var2) {
      if (Client.u == var1.e && Client.u == var2.e) {
         return this.a ? var1.a().a(var2.a()) : var2.a().a(var1.a());
      } else {
         return this.a(var1, var2);
      }
   }

   public int compare(Object var1, Object var2) {
      return this.a((gP)var1, (gP)var2);
   }
}
