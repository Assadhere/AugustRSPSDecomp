package osrs;

public final class eC {
   public fZ a;
   public int b;
   public int c;
   public W d;
   public eH e;

   public eC(int var1) {
      this(var1, var1);
   }

   public eC(int var1, int var2) {
      this.a = new fZ();
      this.b = var1;
      this.c = var1;

      int var3;
      for(var3 = 1; var3 + var3 < var1 && var3 < var2; var3 += var3) {
      }

      this.d = new W(var3);
   }

   public Object a(long var1) {
      eG var3 = (eG)this.d.a(var1);
      if (var3 == null) {
         return null;
      } else {
         Object var4 = var3.a();
         if (var4 == null) {
            var3.X();
            ((aA)var3).ae();
            this.c += var3.b;
            return null;
         } else {
            if (var3.b()) {
               eF var5 = new eF(var4, var3.b);
               this.d.a(var5, var3.cm);
               this.a.a(var5);
               var5.aQ = 0L;
               var3.X();
               ((aA)var3).ae();
            } else {
               this.a.a(var3);
               var3.aQ = 0L;
            }

            return var4;
         }
      }
   }

   public void b(long var1) {
      eG var3 = (eG)this.d.a(var1);
      this.a(var3);
   }

   public void a(eG var1) {
      if (var1 != null) {
         var1.X();
         ((aA)var1).ae();
         this.c += var1.b;
      }

   }

   public void a(Object var1, long var2) {
      this.a(var1, var2, 1);
   }

   public void a(Object var1, long var2, int var4) {
      if (var4 > this.b) {
         throw new IllegalStateException();
      } else {
         this.b(var2);
         this.c -= var4;

         while(this.c < 0) {
            eG var5 = (eG)this.a.b();
            if (var5 == null) {
               throw new RuntimeException("");
            }

            if (!var5.b()) {
            }

            this.a(var5);
            if (this.e != null) {
               this.e.a(var5.a());
            }
         }

         eF var6 = new eF(var1, var4);
         this.d.a(var6, var2);
         this.a.a(var6);
         var6.aQ = 0L;
      }
   }

   public void a(int var1) {
      for(eG var2 = (eG)this.a.c(); var2 != null; var2 = (eG)this.a.d()) {
         if (var2.b()) {
            if (var2.a() == null) {
               var2.X();
               ((aA)var2).ae();
               this.c += var2.b;
            }
         } else if (++var2.aQ > (long)var1) {
            eJ var3 = new eJ(var2.a(), var2.b);
            this.d.a(var3, var2.cm);
            fZ.a(var3, var2);
            var2.X();
            ((aA)var2).ae();
         }
      }

   }

   public void a() {
      this.a.a();
      this.d.a();
      this.c = this.b;
   }
}
