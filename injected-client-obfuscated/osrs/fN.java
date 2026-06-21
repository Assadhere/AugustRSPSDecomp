package osrs;

import java.util.Iterator;

public class fN implements Iterator {
   public az a = null;
   public W b;
   public int c;
   public az d;

   public fN(W var1) {
      this.b = var1;
      this.a();
   }

   public void a() {
      this.d = this.b.c[0].cn;
      this.c = 1;
      this.a = null;
   }

   public az b() {
      this.a();
      return (az)this.next();
   }

   public Object next() {
      az var1;
      if (this.b.c[this.c - 1] != this.d) {
         var1 = this.d;
         this.d = var1.cn;
         this.a = var1;
         return var1;
      } else {
         while(this.c < this.b.b) {
            var1 = this.b.c[this.c++].cn;
            if (this.b.c[this.c - 1] != var1) {
               this.d = var1.cn;
               this.a = var1;
               return var1;
            }
         }

         return null;
      }
   }

   public boolean hasNext() {
      if (this.b.c[this.c - 1] != this.d) {
         return true;
      } else {
         while(this.c < this.b.b) {
            if (this.b.c[this.c++].cn != this.b.c[this.c - 1]) {
               this.d = this.b.c[this.c - 1].cn;
               return true;
            }

            this.d = this.b.c[this.c - 1];
         }

         return false;
      }
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
