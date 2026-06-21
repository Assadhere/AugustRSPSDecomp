package osrs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class bJ extends bK {
   public List a;
   public HashSet b;
   public HashSet c;

   public void a(aR var1, aR var2, int var3, boolean var4) {
      this.a(var1, var3);
      int var5 = var2.d();
      this.c = new HashSet(var5);

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         en var7 = new en();

         try {
            var7.b(var2);
         } catch (IllegalStateException var11) {
            continue;
         }

         this.c.add(var7);
      }

      var6 = var2.d();
      this.b = new HashSet(var6);

      for(int var12 = 0; var12 < var6; ++var12) {
         eA var8 = new eA();

         try {
            var8.b(var2);
         } catch (IllegalStateException var10) {
            continue;
         }

         this.b.add(var8);
      }

      this.a(var2, var4);
   }

   public void a(aR var1, boolean var2) {
      this.a = new LinkedList();
      int var3 = var1.d();

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var1.u();
         B var6 = new B(var1.h());
         boolean var7 = var1.b() == 1;
         if (var2 || !var7) {
            this.a.add(new el((B)null, var6, var5, (ex)null));
         }
      }

   }
}
