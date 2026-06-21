package osrs;

import java.util.Comparator;

public abstract class gN implements Comparator {
   public Comparator b;

   public final void a(Comparator var1) {
      if (this.b == null) {
         this.b = var1;
      } else if (this.b instanceof gN) {
         ((gN)this.b).a(var1);
      }

   }

   public final int a(gH var1, gH var2) {
      return this.b == null ? 0 : this.b.compare(var1, var2);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }
}
