package osrs;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import net.runelite.api.IndexedObjectSet;

public class iV implements IndexedObjectSet {
   public final bG a;

   public iV(bG var1) {
      this.a = var1;
   }

   public Iterator iterator() {
      return Iterators.transform(this.a.t.iterator(), (var0) -> {
         return var0.n;
      });
   }

   public Object byIndex(int var1) {
      return this.a(var1);
   }

   public bG a(int var1) {
      gZ var2 = (gZ)this.a.t.a(var1);
      return var2 == null ? null : var2.n;
   }
}
