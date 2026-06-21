package osrs;

import java.util.HashMap;

public class hH {
   public au a;
   public au b;
   public HashMap c;

   public hH(au var1, au var2) {
      this.a = var1;
      this.b = var2;
      this.c = new HashMap();
   }

   public HashMap a(hv[] var1) {
      HashMap var2 = new HashMap();
      hv[] var3 = var1;

      for(int var4 = 0; var4 < var3.length; ++var4) {
         hv var5 = var3[var4];
         if (this.c.containsKey(var5)) {
            var2.put(var5, this.c.get(var5));
         } else {
            au var6 = this.a;
            au var7 = this.b;
            String var8 = var5.g;
            gg var9;
            if (!var6.a(var8, "")) {
               var9 = null;
            } else {
               int var10 = var6.a(var8);
               int var11 = var6.a(var10, "");
               var9 = aY.a(var6, var7, var10, var11);
            }

            if (var9 != null) {
               this.c.put(var5, var9);
               var2.put(var5, var9);
            }
         }
      }

      return var2;
   }
}
