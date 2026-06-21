package osrs;

public class bX extends gN {
   public final boolean a;

   public bX(boolean var1) {
      this.a = var1;
   }

   public int a(gP var1, gP var2) {
      if (var1.e != 0 && var2.e != 0) {
         return this.a ? var1.f - var2.f : var2.f - var1.f;
      } else {
         return this.a(var1, var2);
      }
   }

   public int compare(Object var1, Object var2) {
      return this.a((gP)var1, (gP)var2);
   }
}
