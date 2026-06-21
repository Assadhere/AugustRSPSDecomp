package osrs;

public class iJ {
   public static V a;
   public fT b;
   public au c;
   public au d;

   public iJ(au var1, au var2) {
      new fT(256);
      this.b = new fT(256);
      this.c = var1;
      this.d = var2;
   }

   public ix a(int var1, int var2, int[] var3) {
      long var4 = this.a(var1, var2, false);
      ci var6 = (ci)this.b.a(var4);
      if (var6 != null) {
         return var6.c();
      } else if (var3 != null && var3[0] <= 0) {
         return null;
      } else {
         iv var7 = iv.a(this.c, var1, var2);
         if (var7 == null) {
            return null;
         } else {
            ix var8 = var7.a();
            this.b.a(new ci(var8), var4);
            if (var3 != null) {
               var3[0] -= var8.d.length;
            }

            return var8;
         }
      }
   }

   public ci a(int var1, int var2) {
      long var3 = this.a(var1, var2, true);
      ci var5 = (ci)this.b.a(var3);
      if (var5 != null) {
         return var5;
      } else {
         bS var6 = bS.a(this.d, var1, var2);
         if (var6 == null) {
            return new ci();
         } else {
            ci var7 = new ci(var6);
            this.b.a(var7, var3);
            return var7;
         }
      }
   }

   public ix a(int var1, int[] var2) {
      if (this.c.b() == 1) {
         return this.a(0, var1, var2);
      } else if (this.c.b(var1, (byte)9) - 1 == 1) {
         return this.a(var1, 1, var2);
      } else if (this.c.b(var1, (byte)7) - 1 == 0) {
         return this.a(var1, 0, var2);
      } else {
         throw new RuntimeException();
      }
   }

   public ci a(int var1) {
      if (this.d.b() == 1) {
         return this.a(0, var1);
      } else if (this.d.b(var1, (byte)-3) == 1) {
         return this.a(var1, 0);
      } else {
         throw new RuntimeException();
      }
   }

   public ix b(int var1) {
      return this.a(var1, (int[])null);
   }

   public long a(int var1, int var2, boolean var3) {
      int var4 = var2 ^ (var1 << 4 & '\uffff' | var1 >> 12);
      int var5 = var4 | var1 << 16;
      return var3 ? (long)var5 ^ 4294967296L : (long)var5;
   }
}
