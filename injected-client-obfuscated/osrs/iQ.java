package osrs;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class iQ implements Iterator {
   public final gd a;
   public ht b;

   public Q a() {
      if (this.a.a == this.b) {
         throw new NoSuchElementException();
      } else {
         Q var1 = (Q)this.b;
         this.b = this.b.c();
         return var1;
      }
   }

   public void remove() {
      ht var1 = this.b.d();
      if (this.a.a == var1) {
         throw new IllegalStateException();
      } else {
         var1.e();
      }
   }

   public Object next() {
      return this.a();
   }

   public boolean hasNext() {
      return this.a.a != this.b;
   }

   public iQ(gd var1) {
      this.a = var1;
      this.b = this.a.a.c();
   }
}
