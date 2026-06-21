package osrs;

import java.util.Iterator;

public class fR implements Iterator {
   public az a = null;
   public fU b;
   public az c;

   public fR(fU var1) {
      this.a(var1);
   }

   public void a(fU var1) {
      this.b = var1;
      this.a();
   }

   public void a() {
      this.c = this.b != null ? this.b.a.cn : null;
      this.a = null;
   }

   public Object next() {
      az var1 = this.c;
      if (this.b.a == var1) {
         var1 = null;
         this.c = null;
      } else {
         this.c = var1.cn;
      }

      this.a = var1;
      return var1;
   }

   public boolean hasNext() {
      return this.b.a != this.c && this.c != null;
   }

   public void remove() {
      if (this.a == null) {
         throw new IllegalStateException();
      } else {
         this.a.X();
         this.a = null;
      }
   }
}
