package osrs;

import net.runelite.api.HealthBarConfig;
import net.runelite.api.SpritePixels;
import net.runelite.api.events.PostHealthBarConfig;

public class Z extends aA implements HealthBarConfig {
   public int a = 0;
   public static eI b = new eI(64);
   public static eI c = new eI(64);
   public int d = 255;
   public int e = 255;
   public int f = -1;
   public int g = 1;
   public int h = 70;
   public int i = -1;
   public int j = -1;
   public int k = 30;
   public int l;
   public static au m;
   public static au n;

   public static void a(au var0, au var1) {
      n = var0;
      m = var1;
   }

   public static Z a(int var0) {
      Z var1 = (Z)b.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = n.b(33, (int)var0);
         Z var3 = new Z();
         var3.l = var0;
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         b.a(var3, (long)var0);
         return var3;
      }
   }

   public static void a() {
      b.a();
      c.a();
   }

   public void a(aR var1) {
      while(true) {
         int var2 = var1.b();
         if (var2 == 0) {
            this.b(var1);
            return;
         }

         this.a(var1, var2);
      }
   }

   public void a(aR var1, int var2) {
      if (var2 == 1) {
         var1.d();
      } else if (var2 == 2) {
         this.d = var1.b();
      } else if (var2 == 3) {
         this.e = var1.b();
      } else if (var2 == 4) {
         this.f = 0;
      } else if (var2 == 5) {
         this.h = var1.d();
      } else if (var2 == 6) {
         var1.b();
      } else if (var2 == 7) {
         this.i = var1.u();
      } else if (var2 == 8) {
         this.j = var1.u();
      } else if (var2 == 11) {
         this.f = var1.d();
      } else if (var2 == 14) {
         this.k = var1.b();
      } else if (var2 == 15) {
         this.a = var1.b();
      }

   }

   public aV b() {
      if (this.i < 0) {
         return null;
      } else {
         aV var1 = (aV)c.a((long)this.i);
         if (var1 != null) {
            return var1;
         } else {
            aV var2 = bo.a(m, this.i, 0);
            if (var2 != null) {
               c.a(var2, (long)this.i);
            }

            return var2;
         }
      }
   }

   public aV c() {
      if (this.j < 0) {
         return null;
      } else {
         aV var1 = (aV)c.a((long)this.j);
         if (var1 != null) {
            return var1;
         } else {
            aV var2 = bo.a(m, this.j, 0);
            if (var2 != null) {
               c.a(var2, (long)this.j);
            }

            return var2;
         }
      }
   }

   public SpritePixels getHealthBarFrontSprite() {
      return this.d();
   }

   public SpritePixels getHealthBarBackSprite() {
      return this.f();
   }

   public void b(aR var1) {
      PostHealthBarConfig var2 = new PostHealthBarConfig();
      var2.setHealthBarConfig(this);
      Client.s.getCallbacks().post(var2);
   }

   public aV d() {
      return this.b();
   }

   public int e() {
      return this.k;
   }

   public aV f() {
      return this.c();
   }

   public void setPadding(int var1) {
      this.a = var1;
   }

   public int getHealthBarFrontSpriteId() {
      return this.i;
   }
}
