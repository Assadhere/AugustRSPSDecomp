package osrs;

import net.runelite.api.WorldEntityConfig;

public class bH extends aA implements WorldEntityConfig {
   public static au a;
   public static eI b = new eI(64);
   public String c;
   public String[] d;
   public boolean e;
   public int f;
   public int g;
   public int h;
   public int i;
   public int j;
   public int k;
   public int l;
   public hx m;
   public hx[] n;
   public int o;
   public int p;
   public fz q;
   public fk r;
   public int s;
   public int t;
   public static eI u = new eI(64);
   public static au v;
   public int w;

   public bH() {
      this.c = bv.k;
      this.d = new String[5];
      this.e = false;
      this.f = 0;
      this.g = 0;
      this.h = 0;
      this.i = 0;
      this.j = 0;
      this.k = 0;
      this.l = -1;
      this.n = new hx[4];
      this.o = -1;
      this.p = 0;
      this.q = fz.e;
      this.r = fk.d;
      this.s = -1;
      this.t = 39188;
   }

   public static void a(au var0, au var1) {
      a = var0;
      v = var1;
   }

   public static bH a(int var0) {
      bH var1 = (bH)b.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = a.b(72, (int)var0);
         bH var3 = new bH();
         var3.o = var0;
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         var3.b();
         b.a(var3, (long)var0);
         return var3;
      }
   }

   public int a() {
      return this.o;
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
      this.b(var1, var2);
      switch (var2) {
         case 2:
            this.p = var1.b();
         case 3:
         case 10:
         case 11:
         case 13:
         case 21:
         case 22:
         default:
            break;
         case 4:
            this.f = var1.e();
            break;
         case 5:
            this.g = var1.e();
            break;
         case 6:
            this.h = var1.e();
            break;
         case 7:
            this.i = var1.e();
            break;
         case 8:
            this.j = var1.d();
            break;
         case 9:
            this.k = var1.d();
            break;
         case 12:
            this.c = var1.m();
            break;
         case 14:
            this.e = true;
            break;
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
            int var3 = var2 - 15;
            this.d[var3] = var1.m();
            if (this.d[var3].equalsIgnoreCase(bv.f)) {
               this.d[var3] = null;
            }

            this.e = true;
            break;
         case 20:
            var1.d();
            break;
         case 23:
            this.q = (fz)kk.a(fz.a(), var1.b());
            break;
         case 24:
            fk[] var4 = new fk[]{fk.a, fk.b, fk.c};
            this.r = (fk)kk.a(var4, var1.b());
            break;
         case 25:
            this.l = var1.d();
            break;
         case 26:
            this.s = var1.u();
            break;
         case 27:
            this.t = var1.d();
      }

   }

   public void b() {
      this.m = new hx(this.j, this.k, this.h, this.i);
      this.c();
   }

   public void c() {
      short var1 = 256;
      this.n[0] = new hx(this.j + var1, this.k + var1, this.h, this.i);
      short var2 = 362;
      this.n[2] = new hx(this.j + var2, this.k + var2, this.h, this.i);
      short var3 = 334;
      this.n[1] = new hx(this.j + var3, this.k + var3, this.h, this.i);
      this.n[3] = this.n[1];
   }

   public int d() {
      return this.p;
   }

   public int e() {
      return this.f;
   }

   public int f() {
      return this.g;
   }

   public int g() {
      return this.l;
   }

   public hx h() {
      return this.m;
   }

   public fz i() {
      return this.q;
   }

   public fk j() {
      return this.r;
   }

   public aV k() {
      if (this.s < 0) {
         return null;
      } else {
         aV var1 = (aV)u.a((long)this.s);
         if (var1 != null) {
            return var1;
         } else {
            aV var2 = bo.a(v, this.s, 0);
            if (var2 != null) {
               u.a(var2, (long)this.s);
            }

            return var2;
         }
      }
   }

   public int l() {
      return this.t;
   }

   public void b(aR var1, int var2) {
      if (var2 == 20) {
         int var3 = var1.ag();
         this.w = var1.W();
         var1.w(var3);
      }

   }

   public int getCategory() {
      return this.w;
   }

   public int getBoundsHeight() {
      return this.k;
   }

   public aV m() {
      return this.k();
   }

   public int getBoundsY() {
      return this.i;
   }

   public int getBoundsX() {
      return this.h;
   }

   public int getBoundsWidth() {
      return this.j;
   }

   public int getId() {
      return this.o;
   }
}
