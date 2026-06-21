package osrs;

public class gT extends gU {
   public static final int a;
   public final ee b = new ee();
   public final ee c = new ee();
   public int d = 0;
   public int e = 0;

   public static void a(ee var0, ee var1, float var2, ee var3) {
      float var4 = Math.max(0.0F, Math.min(var2, 1.0F));
      int var5 = var1.a() - var0.a();
      int var6 = var1.c() - var0.c();
      int var7 = (int)((float)var5 * var4);
      int var8 = (int)((float)var6 * var4);
      var3.a(var0.a() + var7, var0.c() + var8);
      int var9 = bo.b(var0.f(), var1.f());
      int var10 = (int)((float)var9 * var4);
      var3.a(var0.f() + var10);
   }

   public void a(ee var1, gL var2, int var3) {
      this.b.a(var1);
      this.c.a(var2.a);
      this.d = var3 - 1;
      this.e = a + var2.b;
   }

   public boolean a(ee var1, int var2, int var3) {
      if (this.d >= this.e) {
         var1.a(this.c);
         return true;
      } else {
         int var4 = this.d;
         int var5 = this.e;
         float var6 = (float)(var2 - var4) / (float)(var5 - var4);
         a(this.b, this.c, var6, var1);
         return var6 >= 1.0F;
      }
   }

   public void a(int var1, int var2) {
      this.b.b(var1, var2);
      this.c.b(var1, var2);
   }

   public static void a(ee var0, ee var1, double var2, ee var4) {
      if (var2 < 0.0) {
         var2 = 0.0;
      }

      if (var2 > 1.0) {
         var2 = 1.0;
      }

      int var5 = var1.b - var0.b;
      int var6 = var1.c - var0.c;
      int var7 = (int)((double)var5 * var2);
      int var8 = (int)((double)var6 * var2);
      var4.b = var0.b + var7;
      var4.c = var0.c + var8;
      int var9 = var1.a - var0.a & 2047;
      if (var9 > 1024) {
         var9 = -(2048 - var9);
      }

      int var10 = (int)((double)var9 * var2);
      var4.a = var0.a + var10 & 2047;
   }

   public boolean a(ee var1, double var2, int var4) {
      if (this.d >= this.e) {
         var1.c(this.c);
         return true;
      } else {
         double var5 = a(var2, this.d, this.e);
         a(this.b, this.c, var5, var1);
         return var5 >= 1.0;
      }
   }

   public static double a(double var0, int var2, int var3) {
      return (var0 - (double)var2) / (double)(var3 - var2);
   }

   static {
      a = Client.al + 3;
   }
}
