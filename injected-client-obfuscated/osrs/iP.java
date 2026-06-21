package osrs;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class iP implements Iterator {
   public int a;
   public final fT b;
   public az c;

   public Object next() {
      return this.a();
   }

   public az a() {
      az var1;
      if (this.a > 0 && this.b.c[this.a - 1] != this.c) {
         var1 = this.c;
         this.c = var1.ab();
         return var1;
      } else {
         while(this.a < this.b.b) {
            var1 = this.b.c[this.a++];
            az var2 = var1.ab();
            if (var1 != var2) {
               this.c = var2.ab();
               return var2;
            }
         }

         throw new NoSuchElementException();
      }
   }

   public boolean hasNext() {
      if (this.a > 0 && this.b.c[this.a - 1] != this.c) {
         return true;
      } else {
         for(int var1 = this.a; var1 < this.b.b; ++var1) {
            az var2 = this.b.c[var1];
            az var3 = var2.ab();
            if (var2 != var3) {
               return true;
            }
         }

         return false;
      }
   }

   public iP(fT var1) {
      this.b = var1;
   }
}
