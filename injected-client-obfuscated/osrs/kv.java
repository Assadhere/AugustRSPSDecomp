package osrs;

public class kv {
   public static void a(gZ var0, bu var1, int var2) {
      int var3;
      if ((var2 & 2) != 0) {
         var3 = ((aR)var1).A();
         var0.a(var3);
      }

      if ((var2 & 1) != 0) {
         var3 = var1.F();
         int var4 = var1.B();
         if (var3 == 65535) {
            byte var5 = -1;
            var0.a.a(var5);
            var0.a.h();
            var0.b = var4;
         } else {
            bk var7 = var0.a.c();
            if (var7 != null) {
               if (var3 == var0.i()) {
                  int var6 = var7.B;
                  if (var6 == 1) {
                     var0.a.h();
                     var0.b = var4;
                  }

                  if (var6 == 2) {
                     var0.a.i();
                  }

                  return;
               }

               if (bk.b(var3).v < var7.v) {
                  return;
               }
            }

            var0.a.a(var3);
            var0.a.h();
            var0.b = var4;
         }
      }

   }
}
