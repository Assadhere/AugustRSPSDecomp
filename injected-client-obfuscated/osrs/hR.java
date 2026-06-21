package osrs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class hR implements hC {
   public final hS a;
   public Map b;

   public hR(hS var1) {
      this.a = var1;
   }

   public int a(int var1) {
      if (this.b != null) {
         ie var2 = (ie)this.b.get(var1);
         if (var2 != null) {
            return (Integer)var2.b;
         }
      }

      return (Integer)this.a.c(var1);
   }

   public void a(int var1, Object var2) {
      if (this.b == null) {
         this.b = new HashMap();
         this.b.put(var1, new ie(var1, var2));
      } else {
         ie var3 = (ie)this.b.get(var1);
         if (var3 == null) {
            this.b.put(var1, new ie(var1, var2));
         } else {
            var3.b = var2;
         }
      }

   }

   public Iterator iterator() {
      return this.b == null ? Collections.emptyList().iterator() : this.b.values().iterator();
   }
}
