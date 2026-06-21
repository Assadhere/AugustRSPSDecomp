package osrs;

import java.util.Iterator;

public class fD implements Iterator {
   public hD a = null;
   public fO b;
   public int c;
   public hD d;

   public fD(fO var1) {
      this.b = var1;
      this.a();
   }

   public void a() {
      this.d = this.b.b[0].cf;
      this.c = 1;
      this.a = null;
   }

   public Object next() {
      hD var1;
      if (this.b.b[this.c - 1] != this.d) {
         var1 = this.d;
         this.d = var1.cf;
         this.a = var1;
         return var1;
      } else {
         while(this.c < this.b.a) {
            var1 = this.b.b[this.c++].cf;
            if (this.b.b[this.c - 1] != var1) {
               this.d = var1.cf;
               this.a = var1;
               return var1;
            }
         }

         return null;
      }
   }

   public boolean hasNext() {
      if (this.b.b[this.c - 1] != this.d) {
         return true;
      } else {
         while(this.c < this.b.a) {
            if (this.b.b[this.c++].cf != this.b.b[this.c - 1]) {
               this.d = this.b.b[this.c - 1].cf;
               return true;
            }

            this.d = this.b.b[this.c - 1];
         }

         return false;
      }
   }

   public void remove() {
      if (this.a == null) {
         throw new IllegalStateException();
      } else {
         this.a.aj();
         this.a = null;
      }
   }
}
