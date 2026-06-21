package osrs;

import java.util.Iterator;

public class jQ {
   public static bG a(int var0, io var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         bG var3 = (bG)var2.next();
         if (var3.s.a((long)var0) != null) {
            return var3;
         }
      }

      return var1.a();
   }
}
