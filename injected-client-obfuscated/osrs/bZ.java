package osrs;

public class bZ extends gN {
   public final boolean a;

   public bZ(boolean var1) {
      this.a = var1;
   }

   public int a(gP var1, gP var2) {
      if (var1.e != var2.e) {
         return this.a ? var1.e - var2.e : var2.e - var1.e;
      } else {
         return this.a(var1, var2);
      }
   }

   public int compare(Object var1, Object var2) {
      return this.a((gP)var1, (gP)var2);
   }
}
