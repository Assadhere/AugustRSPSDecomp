package osrs;

public class cD {
   public long a;
   public int b = -1;
   public fU c = new fU();

   public cD(aR var1) {
      this.a(var1);
   }

   public void a(aR var1) {
      this.a = var1.i();
      this.b = var1.h();

      for(int var2 = var1.b(); var2 != 0; var2 = var1.b()) {
         Object var3;
         if (var2 == 3) {
            var3 = new k();
         } else if (var2 == 1) {
            var3 = new b();
         } else if (var2 == 13) {
            var3 = new m();
         } else if (var2 == 4) {
            var3 = new l();
         } else if (var2 == 6) {
            var3 = new h();
         } else if (var2 == 5) {
            var3 = new a();
         } else if (var2 == 2) {
            var3 = new g();
         } else if (var2 == 7) {
            var3 = new c();
         } else if (var2 == 14) {
            var3 = new d();
         } else if (var2 == 8) {
            var3 = new o();
         } else if (var2 == 9) {
            var3 = new i();
         } else if (var2 == 10) {
            var3 = new j();
         } else if (var2 == 11) {
            var3 = new e();
         } else if (var2 == 12) {
            var3 = new n();
         } else {
            if (var2 != 15) {
               throw new RuntimeException("");
            }

            var3 = new f();
         }

         ((cv)var3).a(var1);
         this.c.a((az)var3);
      }

   }

   public void a(cK var1) {
      if (this.a == var1.x && this.b == var1.q) {
         for(cv var2 = (cv)this.c.c(); var2 != null; var2 = (cv)this.c.d()) {
            var2.a(var1);
         }

         ++var1.q;
      } else {
         throw new RuntimeException("");
      }
   }

   public class j extends cv {
      public int a;
      public String b;

      public void a(aR var1) {
         this.a = var1.h();
         this.b = var1.m();
      }

      public void a(cK var1) {
         var1.a(this.a, this.b);
      }
   }

   public class o extends cv {
      public int a;
      public int b;

      public void a(aR var1) {
         this.b = var1.h();
         this.a = var1.h();
      }

      public void a(cK var1) {
         var1.a(this.b, this.a);
      }
   }

   public class h extends cv {
      public int a = -1;

      public void a(aR var1) {
         this.a = var1.d();
      }

      public void a(cK var1) {
         var1.e(this.a);
      }
   }

   public class n extends cv {
      public String a;

      public void a(aR var1) {
         this.a = var1.m();
         var1.h();
      }

      public void a(cK var1) {
         var1.r = this.a;
      }
   }

   public class d extends cv {
      public int a = -1;
      public boolean b;

      public void a(aR var1) {
         this.a = var1.d();
         this.b = var1.b() == 1;
      }

      public void a(cK var1) {
         var1.a(this.a, this.b);
      }
   }

   public class e extends cv {
      public int a;
      public int b;
      public int c;
      public int d;

      public void a(aR var1) {
         this.b = var1.h();
         this.c = var1.h();
         this.d = var1.b();
         this.a = var1.b();
      }

      public void a(cK var1) {
         var1.b(this.b, this.c, this.d, this.a);
      }
   }

   public class a extends cv {
      public int a = -1;

      public void a(aR var1) {
         this.a = var1.d();
      }

      public void a(cK var1) {
         var1.d(this.a);
      }
   }

   public class f extends cv {
      public int a = -1;

      public void a(aR var1) {
         this.a = var1.d();
      }

      public void a(cK var1) {
         var1.f(this.a);
      }
   }

   public class m extends cv {
      public long a = -1L;
      public String b = null;
      public int c = 0;

      public void a(aR var1) {
         if (var1.b() != 255) {
            --var1.d;
            this.a = var1.i();
         }

         this.b = var1.l();
         this.c = var1.d();
      }

      public void a(cK var1) {
         var1.a(this.a, this.b, this.c);
      }
   }

   public class g extends cv {
      public int a = -1;
      public byte b;

      public void a(aR var1) {
         this.a = var1.d();
         this.b = var1.c();
      }

      public void a(cK var1) {
         var1.a(this.a, this.b);
      }
   }

   public class k extends cv {
      public long a = -1L;
      public String b = null;

      public void a(aR var1) {
         if (var1.b() != 255) {
            --var1.d;
            this.a = var1.i();
         }

         this.b = var1.l();
      }

      public void a(cK var1) {
         var1.a(this.a, this.b);
      }
   }

   public class i extends cv {
      public int a;
      public long b;

      public void a(aR var1) {
         this.a = var1.h();
         this.b = var1.i();
      }

      public void a(cK var1) {
         var1.a(this.a, this.b);
      }
   }

   public class b extends cv {
      public long a = -1L;
      public String b = null;

      public void a(aR var1) {
         if (var1.b() != 255) {
            --var1.d;
            this.a = var1.i();
         }

         this.b = var1.l();
      }

      public void a(cK var1) {
         var1.a(this.a, this.b, 0);
      }
   }

   public class l extends cv {
      public byte a;
      public byte b;
      public byte c;
      public byte d;
      public boolean e;

      public void a(aR var1) {
         this.e = var1.b() == 1;
         this.a = var1.c();
         this.b = var1.c();
         this.c = var1.c();
         this.d = var1.c();
      }

      public void a(cK var1) {
         var1.y = this.e;
         var1.t = this.a;
         var1.u = this.b;
         var1.v = this.c;
         var1.w = this.d;
      }
   }

   public class c extends cv {
      public int a = -1;
      public int b;
      public int c;
      public int d;

      public void a(aR var1) {
         this.a = var1.d();
         this.b = var1.h();
         this.c = var1.b();
         this.d = var1.b();
      }

      public void a(cK var1) {
         var1.a(this.a, this.b, this.c, this.d);
      }
   }
}
