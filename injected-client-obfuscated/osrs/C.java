package osrs;

import net.runelite.api.dbtable.DBRowConfig;

public class C extends aA implements DBRowConfig {
   public static eI a = new eI(64);
   public int b = -1;
   public static au c;
   public int[][] d;
   public Object[][] e;

   public static C a(int var0) {
      C var1 = (C)a.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = c.b(38, (int)var0);
         C var3 = new C();
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

   public Object[] b(int var1) {
      return this.e == null ? null : this.e[var1];
   }

   public void a(aR var1, int var2) {
      if (var2 == 3) {
         int var3 = var1.b();
         if (this.e == null) {
            this.e = new Object[var3][];
            this.d = new int[var3][];
         }

         for(int var4 = var1.b(); var4 != 255; var4 = var1.b()) {
            int var5 = var1.b();
            int[] var6 = new int[var5];

            for(int var7 = 0; var7 < var5; ++var7) {
               var6[var7] = var1.q();
            }

            this.e[var4] = F.a(var1, var6);
            this.d[var4] = var6;
         }
      } else if (var2 == 4) {
         this.b = var1.w();
      }

   }

   public void a() {
   }

   public Object[] c(int var1) {
      return this.b(var1);
   }

   public int getTableID() {
      return this.b;
   }
}
