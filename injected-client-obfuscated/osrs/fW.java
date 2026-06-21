package osrs;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class fW implements Iterator {
   public int a = 0;
   public gj b;
   public int c;

   public fW(gj var1) {
      this.c = this.b.a;
      this.b = var1;
   }

   public boolean hasNext() {
      return this.a < this.b.e;
   }

   public Object next() {
      if (this.b.a != this.c) {
         throw new ConcurrentModificationException();
      } else if (this.a < this.b.e) {
         Object var1 = this.b.b[this.a].a;
         ++this.a;
         return var1;
      } else {
         throw new NoSuchElementException();
      }
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
