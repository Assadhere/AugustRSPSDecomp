package osrs;

public final class gI implements Comparable {
   public Object a;
   public Object b;
   public long c;
   public long d;

   public gI(Object var1, Object var2) {
      this.a = var1;
      this.b = var2;
   }

   public int a(gI var1) {
      if (this.c < var1.c) {
         return -1;
      } else {
         return this.c > var1.c ? 1 : 0;
      }
   }

   public boolean equals(Object var1) {
      if (var1 instanceof gI) {
         return this.b.equals(((gI)var1).b);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int hashCode() {
      return this.b.hashCode();
   }

   public int compareTo(Object var1) {
      return this.a((gI)var1);
   }
}
