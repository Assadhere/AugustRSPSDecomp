package osrs;

import java.util.Arrays;

public class hB {
   public static int a = 0;
   public static final hB[] b = new hB[16];
   public final int[] c = new int[4];
   public final int[] d = new int[4];
   public int e;
   public int f;
   public int g;

   public static hB a() {
      synchronized(b) {
         if (a == 0) {
            return new hB();
         } else {
            b[--a].c();
            return b[a];
         }
      }
   }

   public void b() {
      synchronized(b) {
         if (a < 16) {
            b[++a - 1] = this;
         }

      }
   }

   public void c() {
      this.f = 0;
      this.e = 0;
      this.g = 0;
      Arrays.fill(this.c, 0);
      Arrays.fill(this.d, 0);
   }

   public int a(int var1) {
      return this.c[var1];
   }

   public int b(int var1) {
      return this.d[var1];
   }

   public int d() {
      return this.f;
   }

   public int e() {
      return this.e;
   }

   public int f() {
      return this.g;
   }

   public void a(hx var1, int var2, int var3, int var4) {
      this.g = var1.a;
      int var5 = var4 + 64;
      int var6 = (var5 & 2047) / 128;
      this.f = var2 + var1.a(var6);
      this.e = var3 + var1.b(var6);
      this.c[0] = var2 + var1.a(var6, 0);
      this.d[0] = var3 + var1.b(var6, 0);
      this.c[1] = var2 + var1.a(var6, 1);
      this.d[1] = var3 + var1.b(var6, 1);
      this.c[2] = var2 + var1.a(var6, 2);
      this.d[2] = var3 + var1.b(var6, 2);
      this.c[3] = var2 + var1.a(var6, 3);
      this.d[3] = var3 + var1.b(var6, 3);
   }
}
