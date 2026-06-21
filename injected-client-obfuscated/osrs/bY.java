package osrs;

import java.util.Comparator;

public class bY implements Comparator {
   public final boolean a;

   public bY(boolean var1) {
      this.a = var1;
   }

   public int a(gP var1, gP var2) {
      return this.a ? var1.f - var2.f : var2.f - var1.f;
   }

   public int compare(Object var1, Object var2) {
      return this.a((gP)var1, (gP)var2);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }
}
