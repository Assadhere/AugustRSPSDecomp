package osrs;

public class B {
   public int a;
   public int b;
   public int c;

   public B() {
      this.a = -1;
   }

   public B(int var1, int var2, int var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public B(B var1) {
      this.a = var1.a;
      this.b = var1.b;
      this.c = var1.c;
   }

   public B(int var1) {
      if (var1 == -1) {
         this.a = -1;
      } else {
         this.a = var1 >> 28 & 3;
         this.b = var1 >> 14 & 16383;
         this.c = var1 & 16383;
      }

   }

   public static int a(int var0, int var1, int var2) {
      return var0 << 28 | var1 << 14 | var2;
   }

   public static int a(int var0) {
      return var0 >> 3;
   }

   public static int b(int var0) {
      return var0 << 7;
   }

   public void c(int var1) {
      if (var1 == -1) {
         this.a = -1;
      } else {
         this.a = var1 >> 28 & 3;
         this.b = var1 >> 14 & 16383;
         this.c = var1 & 16383;
      }

   }

   public int a() {
      return !this.c() ? -1 : a(this.a, this.b, this.c);
   }

   public int b() {
      return a(this.a, this.b >> 13, this.c >> 13);
   }

   public void b(int var1, int var2, int var3) {
      int var4 = var1 >> 14 & 255;
      int var5 = var1 & 255;
      this.a = var1 >> 28;
      this.b = (var4 << 13) + var2;
      this.c = (var5 << 13) + var3;
   }

   public boolean c() {
      return this.a != -1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return !(var1 instanceof B) ? false : this.a((B)var1);
      }
   }

   public boolean a(B var1) {
      if (this.a != var1.a) {
         return false;
      } else if (this.b != var1.b) {
         return false;
      } else {
         return this.c == var1.c;
      }
   }

   public int hashCode() {
      return this.a();
   }

   public String toString() {
      return this.a(",");
   }

   public String a(String var1) {
      return this.a + var1 + (this.b >> 6) + var1 + (this.c >> 6) + var1 + (this.b & 63) + var1 + (this.c & 63);
   }
}
