package osrs;

import java.util.Comparator;

public final class fQ implements Comparator {
   public int a(f var1, f var2) {
      return var1.e.d < var2.e.d ? -1 : (var1.e.d == var2.e.d ? 0 : 1);
   }

   public int compare(Object var1, Object var2) {
      return this.a((f)var1, (f)var2);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }
}
