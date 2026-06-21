package osrs;

public class fC {
   public int a;
   public int b;
   public int c;
   public int d;
   public int e;
   public int f;
   public int g;
   public int h;

   public void a() {
      this.g = 0;
   }

   public int b() {
      return this.g;
   }

   public int c() {
      return this.h;
   }

   public int d() {
      return this.a;
   }

   public int e() {
      return this.b;
   }

   public int f() {
      return this.c;
   }

   public int g() {
      return this.d;
   }

   public int h() {
      return this.e;
   }

   public void a(aR var1) {
      this.b(var1);
      int var2 = var1.b();
      switch (var2) {
         case 1:
            this.g = 1;
            break;
         case 2:
            this.c = 64;
            this.d = 64;
            this.g = 2;
            break;
         case 3:
            this.c = 0;
            this.d = 64;
            this.g = 2;
            break;
         case 4:
            this.c = 128;
            this.d = 64;
            this.g = 2;
            break;
         case 5:
            this.c = 64;
            this.d = 0;
            this.g = 2;
            break;
         case 6:
            this.c = 64;
            this.d = 128;
            this.g = 2;
         case 7:
         case 8:
         case 9:
         default:
            break;
         case 10:
            this.g = 3;
            break;
         case 11:
            this.g = 4;
      }

      switch (this.g) {
         case 0:
            break;
         case 1:
         case 3:
            this.h = var1.d();
            break;
         case 2:
            this.a = var1.d();
            this.b = var1.d();
            this.e = var1.b();
            break;
         case 4:
            this.h = var1.d();
            this.e = var1.f();
            break;
         default:
            throw new RuntimeException("");
      }

   }

   public void b(aR var1) {
      this.f = Client.s.getPlane();
   }
}
