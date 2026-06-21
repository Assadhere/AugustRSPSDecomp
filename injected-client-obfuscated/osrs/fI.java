package osrs;

import java.util.HashSet;
import java.util.Set;

public class fI implements fF {
   public static final fI a;
   public static final fI b;
   public static final fI c;
   public static final fI d;
   public static final fI e;
   public static final fI f;
   public static final fI g;
   public static final fI h;
   public static final fI i;
   public static final fI j;
   public static final fI k;
   public static final fI l;
   public static final fI m;
   public final Set n = new HashSet();
   public final int o;

   public fI(String var1, int var2) {
      this.o = var2;
   }

   public fI(String var1, int var2, kq[] var3) {
      this.o = var2;
      kq[] var4 = var3;

      for(int var5 = 0; var5 < var4.length; ++var5) {
         kq var6 = var4[var5];
         this.n.add(var6);
      }

   }

   public int b() {
      return this.o;
   }

   static {
      a = new fI("", 0, new kq[]{kq.d, kq.a});
      b = new fI("", 1, new kq[]{kq.c, kq.d, kq.a});
      c = new fI("", 2, new kq[]{kq.c, kq.b, kq.d});
      d = new fI("", 3, new kq[]{kq.c});
      e = new fI("", 4);
      f = new fI("", 5, new kq[]{kq.c, kq.d});
      g = new fI("", 6, new kq[]{kq.d});
      h = new fI("", 8, new kq[]{kq.c, kq.d});
      i = new fI("", 9, new kq[]{kq.c, kq.b});
      j = new fI("", 10, new kq[]{kq.c});
      k = new fI("", 11, new kq[]{kq.c});
      l = new fI("", 12, new kq[]{kq.c, kq.d});
      m = new fI("", 13, new kq[]{kq.c});
   }
}
