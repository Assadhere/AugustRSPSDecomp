package osrs;

public class dD extends aA {
   public dq[] a;

   public dD(au var1, au var2, int var3) {
      int var4 = var1.b(var3, (byte)-60);
      this.a = new dq[var4];
      db var5 = null;
      int[] var6 = var1.g(var3);

      for(int var7 = 0; var7 < var6.length; ++var7) {
         byte[] var8 = var1.b(var3, var6[var7]);
         if (var5 == null) {
            int var9 = (var8[0] & 255) << 8 | var8[1] & 255;
            byte[] var10 = var2.a(var9, 0, (byte)0);
            var5 = new db(var9, var10);
         }

         this.a[var6[var7]] = new dq(var8, var5);
      }

   }

   public static dD a(au var0, au var1, int var2) {
      boolean var3 = true;
      int var4 = -1;
      int[] var5 = var0.g(var2);

      for(int var6 = 0; var6 < var5.length; ++var6) {
         byte[] var7 = var0.a(var2, var5[var6], (byte)0);
         if (var7 == null) {
            var3 = false;
         } else if (var4 == -1) {
            var4 = (var7[0] & 255) << 8 | var7[1] & 255;
         }
      }

      if (var4 != -1) {
         byte[] var9 = var1.a(var4, 0, (byte)0);
         if (var9 == null) {
            var3 = false;
         }
      } else {
         var3 = false;
      }

      if (!var3) {
         return null;
      } else {
         try {
            return new dD(var0, var1, var2);
         } catch (Exception var8) {
            return null;
         }
      }
   }

   public boolean a(int var1) {
      return this.a[var1].g;
   }

   public static dD b(int var0) {
      return bo.g(var0);
   }

   public boolean c(int var1) {
      return this.a(var1);
   }
}
