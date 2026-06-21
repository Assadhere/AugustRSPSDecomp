package osrs;

import java.util.Iterator;
import net.runelite.api.IndexedObjectSet;

public final class fO implements Iterable, IndexedObjectSet {
   public int a;
   public hD[] b;
   public final fD c;
   public hD d;

   public fO(int var1) {
      this.a = var1;
      this.b = new hD[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         hD var3 = this.b[var2] = new hD();
         var3.cf = var3;
         var3.cg = var3;
      }

      this.c = new fD(this);
   }

   public void a(hD var1, long var2) {
      if (var1.cg != null) {
         var1.aj();
      }

      hD var4 = this.b[(int)(var2 & (long)(this.a - 1))];
      var1.cg = var4.cg;
      var1.cf = var4;
      var1.cg.cf = var1;
      var1.cf.cg = var1;
      var1.ce = var2;
   }

   public void a() {
      for(int var1 = 0; var1 < this.a; ++var1) {
         hD var2 = this.b[var1];

         while(true) {
            hD var3 = var2.cf;
            if (var2 == var3) {
               break;
            }

            var3.aj();
         }
      }

      this.d = null;
   }

   public hD a(long var1) {
      hD var3 = this.b[(int)(var1 & (long)(this.a - 1))];

      for(hD var4 = var3.cf; var3 != var4; var4 = var4.cf) {
         if (var4.ce == var1) {
            return var4;
         }
      }

      return null;
   }

   public Iterator iterator() {
      return new fD(this);
   }

   public Object byIndex(int var1) {
      return this.a(var1);
   }

   public hD a(int var1) {
      return this.b((long)var1);
   }

   public hD b(long var1) {
      return this.a(var1);
   }
}
