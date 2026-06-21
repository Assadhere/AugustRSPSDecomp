package osrs;

public class iI {
   public int a = 2;
   public int[] b = new int[2];
   public int[] c = new int[2];
   public int d;
   public int e;
   public int f;
   public int g;
   public int h;
   public int i;
   public int j;
   public int k;

   public iI() {
      this.b[0] = 0;
      this.b[1] = 65535;
      this.c[0] = 0;
      this.c[1] = 65535;
   }

   public void a(aR var1) {
      this.h = var1.b();
      this.d = var1.h();
      this.e = var1.h();
      this.b(var1);
   }

   public final void b(aR var1) {
      this.a = var1.b();
      this.b = new int[this.a];
      this.c = new int[this.a];

      for(int var2 = 0; var2 < this.a; ++var2) {
         this.b[var2] = var1.d();
         this.c[var2] = var1.d();
      }

   }

   public final void a() {
      this.g = 0;
      this.i = 0;
      this.f = 0;
      this.j = 0;
      this.k = 0;
   }

   public int a(int var1) {
      if (this.k >= this.g) {
         this.j = this.c[this.i++] << 15;
         if (this.i >= this.a) {
            this.i = this.a - 1;
         }

         this.g = (int)((double)this.b[this.i] / 65536.0 * (double)var1);
         if (this.g > this.k) {
            this.f = ((this.c[this.i] << 15) - this.j) / (this.g - this.k);
         }
      }

      this.j += this.f;
      ++this.k;
      return this.j - this.f >> 15;
   }
}
