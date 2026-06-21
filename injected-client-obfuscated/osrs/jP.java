package osrs;

public class jP implements jj {
   public aH c(ag var1) {
      aO var2 = aO.a(var1.g);
      if (var2 != null) {
         aO var3 = var2.d(var1.aL);
         return var3.c(1);
      } else {
         return null;
      }
   }

   public aH a(ag var1) {
      if (var1.aM == 0) {
         return Client.n.a((bk)null, -1, (bk)null, -1);
      } else {
         t var2 = Client.J();
         return var2 != null ? var2.i(-1098105585) : null;
      }
   }

   public aH b(ag var1) {
      t var2 = Client.J();
      return var2 != null ? var1.al.a((bk)null, -1, bk.b(var2.W), var2.O.e()) : null;
   }

   public aH a(ag var1, bk var2, int var3) {
      if (var1.aM != -1) {
         aC var4 = aC.a(var1.aM);
         return var4.a(10, 0, (int[][])null, 0, 0, 0, var2, var3, -1050875960);
      } else {
         return null;
      }
   }

   public aH b(ag var1, bk var2, int var3) {
      if (var1.aM != -1) {
         aN var4 = aN.a(var1.aM);
         return var4.a(var2, var3, (bk)null, -1, (aL)null);
      } else {
         return null;
      }
   }

   public aH c(ag var1, bk var2, int var3, boolean var4) {
      aN var5 = null;
      aL var6 = null;
      int var7 = var1.aM;
      if (var7 >= 0) {
         s var8 = (s)bo.aQ.s.a((long)var7);
         if (var8 != null && var8.aw != null) {
            var5 = var8.aw;
            if (var5.J != null) {
               var5 = var5.c();
            }

            var6 = var8.M();
         }
      }

      t var10 = Client.J();
      aZ var9 = var10 == null ? null : var10.aO;
      return var1.a(bo.cR, var2, var3, var4, var9, var5, var6);
   }

   public aH a(ag var1, bk var2, int var3, boolean var4) {
      int var5 = var1.aM;
      aN var6 = aN.a(var5);
      if (var6.J != null) {
         var6 = var6.c();
      }

      t var7 = Client.J();
      aZ var8 = var7 == null ? null : var7.aO;
      return var1.a(bo.cR, var2, var3, var4, var8, var6, (aL)null);
   }

   public aH b(ag var1, bk var2, int var3, boolean var4) {
      t var5 = Client.J();
      aZ var6 = var5 == null ? null : var5.aO;
      return var1.a(bo.cR, var2, var3, var4, var6, (aN)null, (aL)null);
   }
}
