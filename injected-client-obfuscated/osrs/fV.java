package osrs;

import java.util.Iterator;

public class fV implements Iterator {
   public aA a = null;
   public fZ b;
   public aA c;

   public fV(fZ var1) {
      this.b = var1;
      this.c = this.b.a.aR;
      this.a = null;
   }

   public Object next() {
      aA var1 = this.c;
      if (this.b.a == var1) {
         var1 = null;
         this.c = null;
      } else {
         this.c = var1.aR;
      }

      this.a = var1;
      return var1;
   }

   public boolean hasNext() {
      return this.b.a != this.c;
   }

   public void remove() {
      if (this.a == null) {
         throw new IllegalStateException();
      } else {
         this.a.ae();
         this.a = null;
      }
   }
}
