package osrs;

public class bD extends aA {
   public static eI a = new eI(64);
   public int b = 0;
   public static int c;
   public static au d;

   public static void a(au var0) {
      d = var0;
      c = d.b(16, (byte)71);
   }

   public static bD a(int var0) {
      bD var1 = (bD)a.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = d.b(16, (int)var0);
         bD var3 = new bD();
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
            this.a();
            return;
         }

         this.a(var1, var2);
      }
   }

   public void a(aR var1, int var2) {
      if (var2 == 5) {
         this.b = var1.d();
      }

   }

   public void a() {
   }
}
