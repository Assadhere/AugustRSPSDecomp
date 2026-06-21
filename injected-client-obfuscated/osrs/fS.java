package osrs;

import java.util.Comparator;

public final class fS implements Comparator {
   public int a(f var1, f var2) {
      return var1.d < var2.d ? -1 : (var1.d == var2.d ? 0 : 1);
   }

   public int compare(Object var1, Object var2) {
      return this.a((f)var1, (f)var2);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }
}
