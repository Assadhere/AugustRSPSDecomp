package osrs;

public class bC extends aA {
   public static eI a = new eI(64);
   public boolean b = false;
   public static au c;

   public static void a(au var0) {
      c = var0;
   }

   public static bC a(int var0) {
      bC var1 = (bC)a.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = c.b(19, (int)var0);
         bC var3 = new bC();
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         a.a(var3, (long)var0);
         return var3;
      }
   }

   public void a(aR var1) {
      while(true) {
         int var2 = var1.b();
         if (var2 == 0) {
            return;
         }

         this.a(var1, var2);
      }
   }

   public void a(aR var1, int var2) {
      if (var2 == 2) {
         this.b = true;
      }

   }
}
