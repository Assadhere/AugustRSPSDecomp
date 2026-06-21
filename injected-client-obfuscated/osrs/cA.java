package osrs;

public class cA {
   public long a = -1L;
   public fU b = new fU();
   public long c;

   public cA(aR var1) {
      this.a(var1);
   }

   public void a(aR var1) {
      this.c = var1.i();
      this.a = var1.i();

      for(int var2 = var1.b(); var2 != 0; var2 = var1.b()) {
         Object var3;
         if (var2 == 1) {
            var3 = new d();
         } else if (var2 == 4) {
            var3 = new a();
         } else if (var2 == 3) {
            var3 = new e();
         } else if (var2 == 2) {
            var3 = new c();
         } else {
            if (var2 != 5) {
               throw new RuntimeException("");
            }

            var3 = new b();
         }

         ((cH)var3).a(var1);
         this.b.a((az)var3);
      }

   }

   public void a(cI var1) {
      if (this.c == var1.cm && this.a == var1.e) {
         for(cH var2 = (cH)this.b.c(); var2 != null; var2 = (cH)this.b.d()) {
            var2.a(var1);
         }

         ++var1.e;
      } else {
         throw new RuntimeException("");
      }
   }

   public class c extends cH {
      public int a = -1;
      public byte b;
      public int c;
      public String d;

      public void a(aR var1) {
         this.a = var1.d();
         this.b = var1.c();
         this.c = var1.d();
         var1.i();
         this.d = var1.m();
      }

      public void a(cI var1) {
         cr var2 = (cr)var1.f.get(this.a);
         var2.a = this.b;
         var2.b = this.c;
         var2.c = new I(this.d);
      }
   }

   public class e extends cH {
      public int a = -1;

      public void a(aR var1) {
         this.a = var1.d();
         var1.b();
         if (var1.b() != 255) {
            --var1.d;
            var1.i();
         }

      }

      public void a(cI var1) {
         var1.a(this.a);
      }
   }

   public class d extends cH {
      public String a = null;
      public byte b;
      public int c;

      public void a(aR var1) {
         if (var1.b() != 255) {
            --var1.d;
            var1.i();
         }

         this.a = var1.l();
         this.c = var1.d();
         this.b = var1.c();
         var1.i();
      }

      public void a(cI var1) {
         cr var2 = new cr();
         var2.c = new I(this.a);
         var2.b = this.c;
         var2.a = this.b;
         var1.a(var2);
      }
   }

   public class b extends cH {
      public int a = -1;
      public byte b;
      public int c;
      public String d;

      public void a(aR var1) {
         var1.b();
         this.a = var1.d();
         this.b = var1.c();
         this.c = var1.d();
         var1.i();
         this.d = var1.m();
         var1.b();
      }

      public void a(cI var1) {
         cr var2 = (cr)var1.f.get(this.a);
         var2.a = this.b;
         var2.b = this.c;
         var2.c = new I(this.d);
      }
   }

   public class a extends cH {
      public byte a;
      public byte b;
      public String c;

      public void a(aR var1) {
         this.c = var1.l();
         if (this.c != null) {
            var1.b();
            this.b = var1.c();
            this.a = var1.c();
         }

      }

      public void a(cI var1) {
         var1.a = this.c;
         if (this.c != null) {
            var1.c = this.b;
            var1.d = this.a;
         }

      }
   }
}
