package osrs;

import net.runelite.api.VarbitComposition;

public class bB extends aA implements VarbitComposition {
   public static final int[] a = new int[32];
   public static eI b = new eI(64);
   public int c;
   public int d;
   public int e;
   public static au f;

   public static void a(au var0) {
      f = var0;
   }

   public static bB a(int var0) {
      bB var1 = (bB)b.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = f.b(14, (int)var0);
         bB var3 = new bB();
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         b.a(var3, (long)var0);
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
         this.d = var1.d();
         this.c = var1.b();
         this.e = var1.b();
      }

   }

   public int b(int var1) {
      int var2 = a[this.e - this.c];
      return var1 >> this.c & var2;
   }

   public int getLeastSignificantBit() {
      return this.c;
   }

   public int getIndex() {
      return this.d;
   }

   public int getMostSignificantBit() {
      return this.e;
   }

   static {
      int var0 = 2;

      for(int var1 = 0; var1 < 32; ++var1) {
         a[var1] = var0 - 1;
         var0 += var0;
      }

   }
}
