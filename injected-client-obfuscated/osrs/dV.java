package osrs;

import java.util.Comparator;

public class dV implements Comparator {
   public int a(ed var1, ed var2) {
      return var1.a() - var2.a();
   }

   public int compare(Object var1, Object var2) {
      return this.a((ed)var1, (ed)var2);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }
}
