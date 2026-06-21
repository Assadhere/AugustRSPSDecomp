package osrs;

public class kd extends fa {
   public int o = -1;
   public fT p = new fT(512);
   public ag q = null;
   public int r = 0;
   public int s = -1;
   public ag t = null;
   public final ka u = new ka();
   public final kb v = new kb();
   public boolean w = false;
   public fT x = new fT(8);

   public kd(au var1, au var2, au var3, au var4, au var5) {
      super(var1, var2, var3, var4, var5);
   }

   public void b() {
      this.o = -1;
      this.x = new fT(8);
      this.t = null;
      this.u.a();
      this.u.b();
   }

   public kb c() {
      return this.v;
   }

   public boolean a(ag var1) {
      return var1.b() || this.i(var1) != 0 || this.d(var1) != 0 || var1 == this.u.f();
   }

   public boolean b(ag var1) {
      return var1.x;
   }

   public String a(ag var1, int var2) {
      if (!bl.a(this.d(var1), var2) && var1.br == null) {
         return null;
      } else {
         return var1.aH != null && var1.aH.length > var2 && var1.aH[var2] != null && !var1.aH[var2].trim().isEmpty() ? var1.aH[var2] : null;
      }
   }

   public String c(ag var1) {
      if (bn.a(this.i(var1)) == 0) {
         return null;
      } else {
         return var1.Q != null && !var1.Q.trim().isEmpty() ? var1.Q : null;
      }
   }

   public void e(int var1) {
      for(eO var2 = (eO)this.p.b(); var2 != null; var2 = (eO)this.p.c()) {
         if ((long)var1 == (var2.cm >> 16 & 65535L)) {
            var2.X();
         }
      }

   }

   public int d(ag var1) {
      eO var2 = (eO)this.p.a((long)var1.T);
      int var3 = var1.as;
      eO var4 = var2;

      eO var5;
      while(true) {
         if (var4 == null) {
            var5 = null;
            break;
         }

         if (var3 >= var4.a && var3 <= var4.b) {
            var5 = var4;
            break;
         }

         var4 = var4.e;
      }

      if (var5 != null) {
         return var5.b();
      } else {
         int var6 = var1.bJ;
         int var7 = var6 >> 1 & 1023;
         return var7;
      }
   }

   public ag e(ag var1) {
      int var2 = bl.a(this.i(var1));
      if (var2 == 0) {
         return null;
      } else {
         for(int var3 = 0; var3 < var2; ++var3) {
            var1 = this.a(var1.bL);
            if (var1 == null) {
               return null;
            }
         }

         return var1;
      }
   }

   public ag f(ag var1) {
      ag var2 = this.e(var1);
      if (var2 == null) {
         var2 = var1.aK;
      }

      return var2;
   }

   public ka d() {
      return this.u;
   }

   public ag e() {
      return this.u.e();
   }

   public boolean f() {
      return this.u.d();
   }

   public boolean a(ag var1, int var2, int var3) {
      if (this.u.d()) {
         return false;
      } else if (var1 != null && this.f(var1) != null) {
         this.u.a(var1, this.f(var1), var2, var3);
         return true;
      } else {
         return false;
      }
   }

   public boolean g() {
      return this.t == null;
   }

   public void g(ag var1) {
      this.t = var1;
   }

   public void h() {
      this.t = null;
   }

   public boolean h(ag var1) {
      return this.t == var1;
   }

   public int i(ag var1) {
      eO var2 = (eO)this.p.a((long)var1.T);
      int var3 = var1.as;
      eO var4 = var2;

      eO var5;
      while(true) {
         if (var4 == null) {
            var5 = null;
            break;
         }

         if (var3 >= var4.a && var3 <= var4.b) {
            var5 = var4;
            break;
         }

         var4 = var4.e;
      }

      int var6;
      int var7;
      if (var5 != null) {
         var7 = var5.a();
         var6 = var7;
      } else {
         var7 = var1.bJ;
         var6 = var7;
      }

      var7 = var6;
      if (bo.e) {
         var7 = var6 | 2097152;
      }

      return var7;
   }
}
