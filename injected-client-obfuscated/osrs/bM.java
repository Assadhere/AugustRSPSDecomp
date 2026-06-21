package osrs;

public class bM {
   public int a = 0;
   public boolean b = false;
   public au c;
   public String d;

   public bM(au var1) {
      this.c = var1;
   }

   public void a(String var1) {
      if (var1 != null && !var1.isEmpty() && this.d != var1) {
         this.d = var1;
         this.a = 0;
         this.b = false;
         this.a();
      }

   }

   public int a() {
      if (this.d == null) {
         this.a = 100;
         this.b = true;
      } else {
         if (this.a < 33) {
            if (!this.c.c(bL.b.f, this.d)) {
               return this.a;
            }

            this.a = 33;
         }

         if (this.a == 33) {
            if (this.c.a(bL.c.f, this.d) && !this.c.c(bL.c.f, this.d)) {
               return this.a;
            }

            this.a = 66;
         }

         if (this.a == 66) {
            if (!this.c.c(this.d, bL.e.f)) {
               return this.a;
            }

            this.a = 100;
            this.b = true;
         }
      }

      return this.a;
   }

   public boolean b() {
      return this.b;
   }

   public int c() {
      return this.a;
   }
}
