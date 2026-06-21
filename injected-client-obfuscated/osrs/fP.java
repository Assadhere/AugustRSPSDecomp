package osrs;

import java.util.Comparator;

public final class fP implements Comparator {
   public int a(f var1, f var2) {
      return var1.a().compareTo(var2.a());
   }

   public int compare(Object var1, Object var2) {
      return this.a((f)var1, (f)var2);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }
}
