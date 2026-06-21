package osrs;

import java.util.Iterator;

public class aP extends hr {
   public final eC a = new eC(64);
   public final au b;
   public final int c;

   public aP(fi var1, int var2, ax var3, au var4) {
      super(var1, var3, var4 != null ? var4.b(var2, (byte)53) : 0);
      this.b = var4;
      this.c = var2;
   }

   public hn a(int var1) {
      synchronized(this.a) {
         he var3 = (he)this.a.a((long)var1);
         if (var3 == null) {
            var3 = this.b(var1);
            this.a.a(var3, (long)var1);
         }

         return var3;
      }
   }

   public he b(int var1) {
      byte[] var2 = this.b.b(this.c, var1);
      he var3 = new he(var1);
      if (var2 != null) {
         ((hn)var3).a(new aR(var2));
      }

      return var3;
   }

   public void a() {
      synchronized(this.a) {
         this.a.a();
      }
   }

   public Iterator iterator() {
      return new a();
   }

   class a implements Iterator {
      public int a;

      public a() {
      }

      public boolean hasNext() {
         return this.a < aP.this.b();
      }

      public Object next() {
         int var1 = ++this.a - 1;
         he var2 = (he)aP.this.a.a((long)var1);
         return var2 != null ? var2 : aP.this.b(var1);
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
