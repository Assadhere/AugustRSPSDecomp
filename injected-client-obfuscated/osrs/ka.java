package osrs;

public class ka {
   public ag a = null;
   public ag b = null;
   public int c = 0;
   public int d = 0;
   public boolean e = false;
   public ag f = null;
   public boolean g = false;
   public int h = -1;
   public int i = -1;
   public boolean j = false;
   public int k = -1;
   public int l = -1;
   public int m;

   public void a(ag var1, ag var2, int var3, int var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.m = 0;
      this.e = false;
   }

   public void a() {
      this.a = null;
      this.b = null;
   }

   public void b() {
      this.f = null;
      this.g = false;
      this.h = -1;
      this.i = -1;
      this.j = false;
      this.k = -1;
      this.l = -1;
   }

   public boolean a(ag var1, int var2, int var3) {
      if (this.a == var1) {
         this.j = true;
         this.k = var2;
         this.l = var3;
         return true;
      } else {
         return false;
      }
   }

   public boolean b(ag var1, int var2, int var3) {
      if (this.b == var1) {
         this.g = true;
         this.h = var2;
         this.i = var3;
         return true;
      } else {
         return false;
      }
   }

   public void a(ag var1) {
      this.f = var1;
   }

   public void c() {
      ++this.m;
   }

   public boolean d() {
      return this.a != null;
   }

   public ag e() {
      return this.a;
   }

   public ag f() {
      return this.b;
   }

   public ag g() {
      return this.f;
   }

   public int h() {
      return this.c;
   }

   public int i() {
      return this.d;
   }

   public int j() {
      return this.k;
   }

   public int k() {
      return this.l;
   }

   public int l() {
      return this.h;
   }

   public int m() {
      return this.i;
   }

   public int n() {
      return this.m;
   }

   public void o() {
      if (this.m == 0) {
         this.a();
      }

   }

   public void p() {
      this.e = true;
   }

   public boolean q() {
      return this.e;
   }

   public boolean r() {
      return this.j;
   }

   public boolean s() {
      return this.g;
   }
}
