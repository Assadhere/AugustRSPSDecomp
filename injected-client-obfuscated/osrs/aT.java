package osrs;

public class aT extends aA {
   public static eI a = new eI(64);
   public boolean b = true;
   public int c;
   public int d;
   public long e;
   public static au f;
   public String g;

   public static void a(au var0) {
      f = var0;
   }

   public static aT a(int var0) {
      aT var1 = (aT)a.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = f.b(11, (int)var0);
         aT var3 = new aT();
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         var3.b();
         a.a(var3, (long)var0);
         return var3;
      }
   }

   public static void a() {
      a.a();
   }

   public void b() {
   }

   public void a(aR var1) {
      while(true) {
         int var2 = var1.b();
         if (var2 == 0) {
            return;
         }

         this.a(var1, var2);
      }
   }

   public void a(aR var1, int var2) {
      if (var2 == 1) {
         char var3 = gc.a(var1.c());
         if (var3 == 's') {
            this.c = 36;
         } else {
            this.c = 0;
         }
      } else if (var2 == 8) {
         this.c = var1.b();
      } else if (var2 == 2) {
         this.d = var1.h();
      } else if (var2 == 4) {
         this.b = false;
      } else if (var2 == 5) {
         this.g = var1.m();
      } else if (var2 == 7) {
         this.e = var1.i();
      }

   }

   public boolean c() {
      return bj.b(this.c) == i.c;
   }

   public boolean d() {
      return bj.b(this.c) == i.b;
   }

   public Object e() {
      return this.c() ? this.g : this.d() ? this.e : (long)this.d;
   }

   public int f() {
      return this.d;
   }

   public String g() {
      return this.g;
   }

   public long h() {
      return this.e;
   }
}
