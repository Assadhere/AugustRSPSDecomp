package osrs;

public class iG extends cl {
   public fJ a = new fJ();
   public fJ b = new fJ();
   public int c = 0;
   public int d = -1;

   public final synchronized void a(cl var1) {
      this.a.b(var1);
   }

   public final synchronized void b(cl var1) {
      var1.X();
   }

   public void b() {
      if (this.c > 0) {
         for(cj var1 = (cj)this.b.d(); var1 != null; var1 = (cj)this.b.f()) {
            var1.a -= this.c;
         }

         this.d -= this.c;
         this.c = 0;
      }

   }

   public void a(az var1, cj var2) {
      while(this.b.a != var1 && ((cj)var1).a <= var2.a) {
         var1 = var1.cn;
      }

      fJ.a(var2, var1);
      this.d = ((cj)this.b.a.cn).a;
   }

   public void a(cj var1) {
      var1.X();
      var1.a();
      az var2 = this.b.a.cn;
      if (this.b.a == var2) {
         this.d = -1;
      } else {
         this.d = ((cj)var2).a;
      }

   }

   public cl i() {
      return (cl)this.a.d();
   }

   public cl j() {
      return (cl)this.a.f();
   }

   public int k() {
      return 0;
   }

   public final synchronized void a(int[] var1, int var2, int var3) {
      do {
         if (this.d < 0) {
            this.c(var1, var2, var3);
            return;
         }

         if (this.c + var3 < this.d) {
            this.c += var3;
            this.c(var1, var2, var3);
            return;
         }

         int var4 = this.d - this.c;
         this.c(var1, var2, var4);
         var2 += var4;
         var3 -= var4;
         this.c += var4;
         this.b();
         cj var5 = (cj)this.b.d();
         synchronized(var5) {
            int var7 = var5.a(this);
            if (var7 < 0) {
               var5.a = 0;
               this.a(var5);
            } else {
               var5.a = var7;
               this.a(var5.cn, var5);
            }
         }
      } while(var3 != 0);

   }

   public void c(int[] var1, int var2, int var3) {
      for(cl var4 = (cl)this.a.d(); var4 != null; var4 = (cl)this.a.f()) {
         var4.b(var1, var2, var3);
      }

   }

   public final synchronized void h(int var1) {
      do {
         if (this.d < 0) {
            this.a(var1);
            return;
         }

         if (this.c + var1 < this.d) {
            this.c += var1;
            this.a(var1);
            return;
         }

         int var2 = this.d - this.c;
         this.a(var2);
         var1 -= var2;
         this.c += var2;
         this.b();
         cj var3 = (cj)this.b.d();
         synchronized(var3) {
            int var5 = var3.a(this);
            if (var5 < 0) {
               var3.a = 0;
               this.a(var3);
            } else {
               var3.a = var5;
               this.a(var3.cn, var3);
            }
         }
      } while(var1 != 0);

   }

   public void a(int var1) {
      for(cl var2 = (cl)this.a.d(); var2 != null; var2 = (cl)this.a.f()) {
         var2.h(var1);
      }

   }

   public void c(cl var1) {
      this.a(var1);
   }

   public fJ c() {
      return this.a;
   }
}
