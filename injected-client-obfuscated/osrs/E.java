package osrs;

public class E extends aA {
   public static eI a = new eI(64);
   public static au b;
   public int[][] c;
   public Object[][] d;

   public static void a(au var0) {
      b = var0;
   }

   public static E a(int var0) {
      E var1 = (E)a.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = b.b(39, (int)var0);
         E var3 = new E();
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         var3.a();
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
      if (var2 == 1) {
         int var3 = var1.b();
         if (this.c == null) {
            this.c = new int[var3][];
         }

         for(int var4 = var1.b(); var4 != 255; var4 = var1.b()) {
            int var5 = var4 & 127;
            boolean var6 = (var4 & 128) != 0;
            int[] var7 = new int[var1.b()];

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var7[var8] = var1.q();
            }

            this.c[var5] = var7;
            if (var6) {
               if (this.d == null) {
                  this.d = new Object[this.c.length][];
               }

               this.d[var5] = F.a(var1, var7);
            }
         }
      }

   }

   public void a() {
   }

   public Object[][] b() {
      return this.d;
   }

   public int[][] c() {
      return this.c;
   }
}
