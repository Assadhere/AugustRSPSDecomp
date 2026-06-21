package osrs;

import custom.GhostRender;
import custom.SoftHidePriority;
import java.util.Comparator;
import java.util.Set;

public class iR implements Comparator {
   public static int a(dA var0) {
      int var1 = (int)bo.dD.project((float)var0.g, (float)var0.f, (float)var0.h)[2];
      if (bo.dZ == var0.n) {
         var1 -= 12;
      }

      if (var0.n instanceof s) {
         int var2 = ((s)var0.n).getId();
         Set var3 = SoftHidePriority.getNpcIds();
         if (!var3.isEmpty() && var3.contains(var2) || GhostRender.isGhost(var2)) {
            var1 += 100000;
         }
      }

      return var1;
   }

   public int a(dA var1, dA var2) {
      return Integer.compare(a(var2), a(var1));
   }

   public int compare(Object var1, Object var2) {
      return this.a((dA)var1, (dA)var2);
   }
}
