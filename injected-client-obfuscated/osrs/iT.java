package osrs;

import java.util.Comparator;

public class iT implements Comparator {
   public final dh a;

   public iT(dh var1) {
      this.a = var1;
   }

   public int a(iS var1) {
      int var2 = var1.g * 8 + 4 - this.a.s << 7;
      int var3 = var1.h * 8 + 4 - this.a.s << 7;
      return (var3 - this.a.T) * (var3 - this.a.T) + (var2 - this.a.S) * (var2 - this.a.S);
   }

   public int a(iS var1, iS var2) {
      return Integer.compare(this.a(var1), this.a(var2));
   }

   public int compare(Object var1, Object var2) {
      return this.a((iS)var1, (iS)var2);
   }
}
