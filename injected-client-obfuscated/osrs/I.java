package osrs;

public class I implements Comparable {
   public String a;
   public String b;

   public I(String var1) {
      this.a = var1;
      this.b = J.a(var1, aM.a);
   }

   public I(String var1, aM var2) {
      this.a = var1;
      this.b = J.a(var1, var2);
   }

   public String a() {
      return this.a;
   }

   public String b() {
      return this.b;
   }

   public boolean c() {
      return this.b != null;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof I) {
         I var2 = (I)var1;
         if (this.b == null) {
            return var2.b == null;
         } else if (var2.b == null) {
            return false;
         } else {
            return this.hashCode() != var2.hashCode() ? false : this.b.equals(var2.b);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.b == null ? 0 : this.b.hashCode();
   }

   public String toString() {
      return this.a();
   }

   public int a(I var1) {
      if (this.b == null) {
         return var1.b == null ? 0 : 1;
      } else {
         return var1.b == null ? -1 : this.b.compareTo(var1.b);
      }
   }

   public int compareTo(Object var1) {
      return this.a((I)var1);
   }

   public String d() {
      return this.a;
   }

   public String e() {
      return this.b;
   }
}
