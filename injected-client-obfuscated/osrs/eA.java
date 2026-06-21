package osrs;

public class eA extends em {
   public int p;
   public int q;
   public int r;
   public int s;

   public void b(aR var1) {
      int var2 = var1.b();
      if (eq.b.c != var2) {
         throw new IllegalStateException("");
      } else {
         this.i = var1.b();
         this.j = var1.b();
         this.g = var1.d();
         this.h = var1.d();
         this.p = var1.b();
         this.s = var1.b();
         this.l = var1.d();
         this.k = var1.d();
         this.q = var1.b();
         this.r = var1.b();
         this.e = var1.u();
         this.f = var1.u();
      }
   }

   public void a(aR var1) {
      this.j = Math.min(this.j, 4);
      this.n = new short[1][64][64];
      this.o = new short[this.j][64][64];
      this.m = new byte[this.j][64][64];
      this.a = new byte[this.j][64][64];
      this.b = new ej[this.j][64][64][];
      int var2 = var1.b();
      if (ew.b.c != var2) {
         throw new IllegalStateException("");
      } else {
         int var3 = var1.b();
         int var4 = var1.b();
         int var5 = var1.b();
         int var6 = var1.b();
         if (this.l == var3 && this.k == var4 && this.q == var5 && this.r == var6) {
            for(int var7 = 0; var7 < 8; ++var7) {
               for(int var8 = 0; var8 < 8; ++var8) {
                  this.a(this.q * 8 + var7, this.r * 8 + var8, var1);
               }
            }

         } else {
            throw new IllegalStateException("");
         }
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof eA)) {
         return false;
      } else {
         eA var2 = (eA)var1;
         if (this.l == var2.l && this.k == var2.k) {
            return this.q == var2.q && this.r == var2.r;
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      return this.l | this.k << 8 | this.q << 16 | this.r << 24;
   }

   public int e() {
      return this.p;
   }

   public int f() {
      return this.s;
   }

   public int g() {
      return this.q;
   }

   public int h() {
      return this.r;
   }
}
