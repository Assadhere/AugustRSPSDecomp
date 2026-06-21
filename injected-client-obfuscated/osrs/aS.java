package osrs;

public class aS {
   public final Object a;
   public final Object b;

   public aS(Object var1, Object var2) {
      this.a = var1;
      this.b = var2;
   }

   public String toString() {
      String var10000 = String.valueOf(this.a);
      return var10000 + ", " + String.valueOf(this.b);
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof aS) {
         aS var2 = (aS)var1;
         if (this.a == null) {
            if (var2.a != null) {
               return false;
            }
         } else if (!this.a.equals(var2.a)) {
            return false;
         }

         if (this.b == null) {
            if (var2.b != null) {
               return false;
            }
         } else if (!this.b.equals(var2.b)) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = 0;
      if (this.a != null) {
         var1 += this.a.hashCode();
      }

      if (this.b != null) {
         var1 += 31 * this.b.hashCode();
      }

      return var1;
   }
}
