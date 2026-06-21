package osrs;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class iM implements Iterator {
   public final fJ a;
   public az b;

   public boolean hasNext() {
      return this.a.a != this.b;
   }

   public iM(fJ var1) {
      this.a = var1;
      this.b = this.a.a.ab();
   }

   public void remove() {
      az var1 = this.b.ac();
      if (this.a.a == var1) {
         throw new IllegalStateException();
      } else {
         var1.ad();
      }
   }

   public az a() {
      if (this.a.a == this.b) {
         throw new NoSuchElementException();
      } else {
         az var1 = this.b;
         this.b = this.b.ab();
         return var1;
      }
   }

   public Object next() {
      return this.a();
   }
}
