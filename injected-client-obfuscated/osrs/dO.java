package osrs;

import net.runelite.api.Texture;
import net.runelite.api.TextureProvider;

public class dO implements TextureProvider, dM {
   public fJ a = new fJ();
   public int b = 0;
   public double c = 1.0;
   public int d = 128;
   public au e;
   public int f;
   public do[] g;

   public dO(au var1, au var2, int var3, double var4, int var6) {
      this.e = var2;
      this.f = var3;
      this.b = this.f;
      this.c = var4;
      this.d = var6;
      int[] var7 = var1.g(0);
      if (var7 != null) {
         int var8 = var7.length;
         this.g = new do[var1.b(0, (byte)23)];

         for(int var9 = 0; var9 < var8; ++var9) {
            aR var10 = new aR(var1.b(0, (int)var7[var9]));
            this.g[var7[var9]] = new do(var10);
         }
      } else {
         this.g = new do[0];
      }

      this.a(var1, var2, var3, var4, var6);
   }

   public int a() {
      if (this.g.length == 0) {
         return 100;
      } else {
         int var1 = 0;
         int var2 = 0;
         do[] var3 = this.g;

         for(int var4 = 0; var4 < var3.length; ++var4) {
            do var5 = var3[var4];
            if (var5 != null && var5.b != -1) {
               ++var1;
               if (this.e.b(var5.b)) {
                  ++var2;
               }
            }
         }

         if (var1 == 0) {
            return 0;
         } else {
            return var2 * 100 / var1;
         }
      }
   }

   public void a(double var1) {
      this.c = var1;
      this.b();
   }

   public int[] d(int var1) {
      do var2 = this.g[var1];
      if (var2 != null) {
         if (var2.g != null) {
            this.a.b(var2);
            var2.a = true;
            return var2.g;
         }

         boolean var3 = var2.a(this.c, this.d, this.e);
         if (var3) {
            if (this.b == 0) {
               do var4 = (do)this.a.c();
               var4.a();
            } else {
               --this.b;
            }

            this.a.b(var2);
            var2.a = true;
            return var2.g;
         }
      }

      return null;
   }

   public int b(int var1) {
      return this.g[var1] != null ? this.g[var1].c : 0;
   }

   public boolean a(int var1) {
      return this.g[var1].d;
   }

   public void b() {
      for(int var1 = 0; var1 < this.g.length; ++var1) {
         if (this.g[var1] != null) {
            this.g[var1].a();
         }
      }

      this.a = new fJ();
      this.b = this.f;
   }

   public void e(int var1) {
      for(int var2 = 0; var2 < this.g.length; ++var2) {
         do var3 = this.g[var2];
         if (var3 != null && var3.e != 0 && var3.a) {
            var3.a(var1);
            var3.a = false;
         }
      }

      this.f(var1);
   }

   public Texture[] getTextures() {
      return this.c();
   }

   public void a(au var1, au var2, int var3, double var4, int var6) {
      this.h(128);
      this.g(128);
   }

   public void f(int var1) {
      Client.s.getCallbacks().drawAboveOverheads();
   }

   public void setBrightness(double var1) {
      this.a(var1);
   }

   public void g(int var1) {
      this.b = var1;
   }

   public double getBrightness() {
      return this.c;
   }

   public int getDefaultColor(int var1) {
      return this.b(var1);
   }

   public void h(int var1) {
      this.f = var1;
   }

   public int[] load(int var1) {
      return this.d(var1);
   }

   public do[] c() {
      return this.g;
   }
}
