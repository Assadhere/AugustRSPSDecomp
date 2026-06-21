package osrs;

import net.runelite.api.SpritePixels;
import net.runelite.api.worldmap.MapElementConfig;

public class aE extends aA implements MapElementConfig {
   public int[] a;
   public int b = Integer.MIN_VALUE;
   public int c = Integer.MIN_VALUE;
   public int d = -1;
   public int e = Integer.MAX_VALUE;
   public cR f;
   public cP g;
   public int[] h;
   public byte[] i;
   public int j = 0;
   public static eI k = new eI(256);
   public int l = -1;
   public boolean m = true;
   public boolean n = false;
   public String[] o = new String[5];
   public int p = Integer.MAX_VALUE;
   public int q;
   public final int r;
   public static int s;
   public int t;
   public static au u;
   public String v;
   public String w;
   public static aE[] x;

   public aE(int var1) {
      this.f = cR.b;
      this.g = cP.b;
      this.q = -1;
      this.r = var1;
   }

   public static aE a(int var0) {
      return var0 >= 0 && var0 < x.length && x[var0] != null ? x[var0] : new aE(var0);
   }

   public void a(aR var1) {
      while(true) {
         int var2 = var1.b();
         if (var2 == 0) {
            return;
         }

         this.a(var1, var2);
      }
   }

   public void a(aR var1, int var2) {
      if (var2 == 1) {
         this.l = var1.u();
      } else if (var2 == 2) {
         this.d = var1.u();
      } else if (var2 == 3) {
         this.w = var1.m();
      } else if (var2 == 4) {
         this.t = var1.f();
      } else if (var2 == 5) {
         var1.f();
      } else if (var2 == 6) {
         this.j = var1.b();
      } else {
         int var3;
         if (var2 == 7) {
            var3 = var1.b();
            if ((var3 & 1) == 0) {
               this.m = false;
            }

            if ((var3 & 2) == 2) {
               this.n = true;
            }
         } else if (var2 == 8) {
            var1.b();
         } else if (var2 >= 10 && var2 <= 14) {
            this.o[var2 - 10] = var1.m();
         } else if (var2 == 15) {
            var3 = var1.b();
            this.a = new int[var3 * 2];

            int var4;
            for(var4 = 0; var4 < var3 * 2; ++var4) {
               this.a[var4] = var1.e();
            }

            var1.h();
            var4 = var1.b();
            this.h = new int[var4];

            int var5;
            for(var5 = 0; var5 < this.h.length; ++var5) {
               this.h[var5] = var1.h();
            }

            this.i = new byte[var3];

            for(var5 = 0; var5 < var3; ++var5) {
               this.i[var5] = var1.c();
            }
         } else if (var2 != 16) {
            if (var2 == 17) {
               this.v = var1.m();
            } else if (var2 == 18) {
               var1.u();
            } else if (var2 == 19) {
               this.q = var1.d();
            } else if (var2 == 21) {
               var1.h();
            } else if (var2 == 22) {
               var1.h();
            } else if (var2 == 23) {
               var1.b();
               var1.b();
               var1.b();
            } else if (var2 == 24) {
               var1.e();
               var1.e();
            } else if (var2 == 25) {
               var1.u();
            } else if (var2 == 28) {
               var1.b();
            } else if (var2 == 29) {
               this.f = (cR)kk.a(bo.a(), var1.b());
            } else if (var2 == 30) {
               this.g = (cP)kk.a(cP.a(), var1.b());
            }
         }
      }

   }

   public void a() {
      if (this.a != null) {
         for(int var1 = 0; var1 < this.a.length; var1 += 2) {
            if (this.a[var1] < this.p) {
               this.p = this.a[var1];
            } else if (this.a[var1] > this.c) {
               this.c = this.a[var1];
            }

            if (this.a[var1 + 1] < this.e) {
               this.e = this.a[var1 + 1];
            } else if (this.a[var1 + 1] > this.b) {
               this.b = this.a[var1 + 1];
            }
         }
      }

   }

   public aV a(boolean var1) {
      int var2 = var1 ? this.d : this.l;
      return this.b(var2);
   }

   public aV b(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         aV var2 = (aV)k.a((long)var1);
         if (var2 != null) {
            return var2;
         } else {
            aV var3 = bo.a(u, var1, 0);
            if (var3 != null) {
               k.a(var3, (long)var1);
            }

            return var3;
         }
      }
   }

   public int b() {
      return this.r;
   }

   public SpritePixels getMapIcon(boolean var1) {
      return this.b(var1);
   }

   public int getCategory() {
      return this.q;
   }

   public aV b(boolean var1) {
      return this.a(var1);
   }
}
