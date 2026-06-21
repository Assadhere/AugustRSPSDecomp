package osrs;

import java.util.Iterator;

public class fZ implements Iterable {
   public aA a = new aA();
   public aA b;

   public fZ() {
      this.a.aR = this.a;
      this.a.aS = this.a;
   }

   public static void a(aA var0, aA var1) {
      if (var0.aS != null) {
         var0.ae();
      }

      var0.aS = var1;
      var0.aR = var1.aR;
      var0.aS.aR = var0;
      var0.aR.aS = var0;
   }

   public void a() {
      while(this.a.aR != this.a) {
         this.a.aR.ae();
      }

   }

   public void a(aA var1) {
      if (var1.aS != null) {
         var1.ae();
      }

      var1.aS = this.a.aS;
      var1.aR = this.a;
      var1.aS.aR = var1;
      var1.aR.aS = var1;
   }

   public aA b() {
      aA var1 = this.a.aR;
      if (this.a == var1) {
         return null;
      } else {
         var1.ae();
         return var1;
      }
   }

   public aA c() {
      return this.b((aA)null);
   }

   public aA b(aA var1) {
      aA var2;
      if (var1 == null) {
         var2 = this.a.aR;
      } else {
         var2 = var1;
      }

      if (this.a == var2) {
         this.b = null;
         return null;
      } else {
         this.b = var2.aR;
         return var2;
      }
   }

   public aA d() {
      aA var1 = this.b;
      if (this.a == var1) {
         this.b = null;
         return null;
      } else {
         this.b = var1.aR;
         return var1;
      }
   }

   public Iterator iterator() {
      return new fV(this);
   }

   public void c(aA var1) {
      this.a(var1);
   }
}
