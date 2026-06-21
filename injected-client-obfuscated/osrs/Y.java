package osrs;

public class Y extends az {
   public fU a = new fU();
   public Z b;

   public Y(Z var1) {
      this.b = var1;
   }

   public void a(int var1, int var2, int var3, int var4) {
      X var5 = null;
      int var6 = 0;

      for(X var7 = (X)this.a.c(); var7 != null; var7 = (X)this.a.d()) {
         ++var6;
         if (var7.a == var1) {
            var7.a(var1, var2, var3, var4);
            return;
         }

         if (var7.a <= var1) {
            var5 = var7;
         }
      }

      if (var5 == null) {
         if (var6 < 4) {
            this.a.b(new X(var1, var2, var3, var4));
         }
      } else {
         fU.a(new X(var1, var2, var3, var4), var5);
         if (var6 >= 4) {
            this.a.c().X();
         }
      }

   }

   public X a(int var1) {
      X var2 = (X)this.a.c();
      if (var2 != null && var2.a <= var1) {
         for(X var3 = (X)this.a.d(); var3 != null && var3.a <= var1; var3 = (X)this.a.d()) {
            var2.X();
            var2 = var3;
         }

         if (this.b.h + var2.a + var2.d > var1) {
            return var2;
         } else {
            var2.X();
            return null;
         }
      } else {
         return null;
      }
   }

   public boolean a() {
      return this.a.f();
   }

   public X b(int var1) {
      return this.a(var1);
   }

   public Z b() {
      return this.b;
   }
}
