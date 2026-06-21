package osrs;

public class jE {
   public static final W a(aR var0, W var1) {
      int var2 = var0.b();
      int var3;
      if (var1 == null) {
         var3 = ks.c(var2);
         var1 = new W(var3);
      }

      for(var3 = 0; var3 < var2; ++var3) {
         int var4 = var0.b();
         int var5 = var0.f();
         Object var6;
         if (var4 == 1) {
            var6 = new hz(var0.m());
         } else if (var4 == 2) {
            var6 = new hE(var0.i());
         } else {
            var6 = new aq(var0.h());
         }

         var1.a((az)var6, (long)var5);
      }

      return var1;
   }

   public static int a(W var0, int var1, int var2) {
      if (var0 == null) {
         return var2;
      } else {
         aq var3 = (aq)var0.a((long)var1);
         return var3 == null ? var2 : var3.a;
      }
   }

   public static String a(W var0, int var1, String var2) {
      if (var0 == null) {
         return var2;
      } else {
         hz var3 = (hz)var0.a((long)var1);
         return var3 == null ? var2 : (String)var3.a;
      }
   }
}
