package osrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D extends aA {
   public List a;
   public i[] b;

   public D(au var1, int var2, int var3) {
      byte[] var4 = var1.b(var2, var3 + 1);
      this.a(new aR(var4));
   }

   public D(au var1, int var2) {
      byte[] var3 = var1.b(var2, (int)0);
      this.a(new aR(var3));
   }

   public void a(aR var1) {
      int var2 = var1.w();
      this.b = new i[var2];
      this.a = new ArrayList(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.b[var3] = (i)kk.a(i.a(), var1.b());
         int var4 = var1.w();
         HashMap var5 = new HashMap(var4);

         while(var4-- > 0) {
            Object var6 = this.b[var3].a(var1);
            int var7 = var1.w();
            ArrayList var8 = new ArrayList();

            while(var7-- > 0) {
               int var9 = var1.w();
               var8.add(var9);
            }

            var5.put(var6, var8);
         }

         this.a.add(var3, var5);
      }

   }

   public List a(Object var1, int var2) {
      if (var2 < 0) {
         var2 = 0;
      }

      Map var3 = (Map)this.a.get(var2);
      return (List)var3.get(var1);
   }

   public static D a(int var0) {
      return Client.E(var0);
   }

   public static D b(int var0) {
      return bo.k(var0);
   }
}
