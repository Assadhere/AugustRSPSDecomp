package osrs;

import java.util.Arrays;

public class iX {
   public fX a;
   public float b;
   public static final iX c = new iX(true);
   public float d;
   public iW[] e = new iW[128];
   public fX f;
   public int g;
   public int h;
   public int i;
   public fX j;
   public aH k;
   public byte[] l;
   public float m;

   public void a(fX var1, int var2) {
      if (var2 >= this.e.length) {
         this.e = (iW[])Arrays.copyOf(this.e, var2 + 1);
      }

      iW var3 = this.e[var2];
      if (var3 == null) {
         var3 = this.e[var2] = new iW();
      }

      var3.b.e(var1);
      var3.a = var3.e = true;
   }

   public iX(boolean var1) {
      this.k = aH.o;
      this.l = aH.p;
      this.a = aH.s;
      this.f = aH.t;
      this.j = aH.u;
   }

   public iX() {
      this.k = new aH();
      this.l = new byte[1];
      this.a = new fX();
      this.f = new fX();
      this.j = new fX();
   }
}
