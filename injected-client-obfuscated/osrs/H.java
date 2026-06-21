package osrs;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import net.runelite.api.Preferences;
import net.runelite.api.events.VolumeChanged;
import net.runelite.api.events.VolumeChanged.Type;

public class H implements Preferences {
   public String a;
   public int b;
   public int c;
   public int d;
   public boolean e;
   public final Map f = new LinkedHashMap();
   public boolean g = false;
   public boolean h = false;
   public double i = 0.8;
   public int j = 127;
   public int k;
   public int l;
   public int m;
   public boolean n;
   public boolean o;
   public int p;

   public H() {
      this.j(-1);
      this.b = 127;
      this.i(-1);
      this.c = 127;
      this.k(-1);
      this.k = -1;
      this.a = null;
      this.d = 1;
      this.l = 25;
      this.m = 100;
      this.e = false;
      this.a(true);
      this.s();
   }

   public H(aR var1) {
      this.j(-1);
      this.b = 127;
      this.i(-1);
      this.c = 127;
      this.k(-1);
      this.k = -1;
      this.a = null;
      this.d = 1;
      this.l = 25;
      this.m = 100;
      this.e = false;
      if (var1 != null && var1.c != null && var1.c.length != 0) {
         int var2 = var1.b();
         if (var2 >= 0 && var2 <= 12) {
            if (var1.b() == 1) {
               this.n = true;
            }

            if (var2 > 1) {
               this.o = var1.b() == 1;
            }

            if (var2 > 3) {
               this.d = var1.b();
            }

            if (var2 > 2) {
               int var3 = var1.b();

               for(int var4 = 0; var4 < var3; ++var4) {
                  int var5 = var1.h();
                  int var6 = var1.h();
                  this.f.put(var5, var6);
               }
            }

            if (var2 > 4) {
               this.a = var1.l();
            }

            if (var2 > 5) {
               this.g = var1.k();
            }

            if (var2 > 6) {
               this.i = (double)var1.b() / 100.0;
               this.j = var1.b();
               this.j(-1);
               this.b = var1.b();
               this.i(-1);
               this.c = var1.b();
               this.k(-1);
            }

            if (var2 > 7) {
               this.k = var1.b();
            }

            if (var2 > 8) {
               this.h = var1.b() == 1;
            }

            if (var2 > 9) {
               this.p = var1.h();
            }

            if (var2 > 10) {
               this.l = var1.b();
            }

            if (var2 > 11) {
               var1.b();
               this.e = false;
            }
         } else {
            this.a(true);
         }
      } else {
         this.a(true);
      }

   }

   public static H a() {
      hQ var0 = null;
      H var1 = new H();

      try {
         var0 = U.a("", bo.cZ.g, false);
         byte[] var2 = new byte[(int)var0.c()];

         int var3;
         for(int var4 = 0; var4 < var2.length; var4 += var3) {
            var3 = var0.b(var2, var4, var2.length - var4);
            if (var3 == -1) {
               throw new IOException();
            }
         }

         var1 = new H(new aR(var2));
      } catch (Exception var6) {
      }

      try {
         if (var0 != null) {
            var0.b();
         }
      } catch (Exception var5) {
      }

      return var1;
   }

   public void a(boolean var1) {
   }

   public aR b() {
      aR var1 = new aR(419, true);
      var1.a(12);
      var1.a(this.n ? 1 : 0);
      var1.a(this.o ? 1 : 0);
      var1.a(this.d);
      var1.a(this.f.size());
      Iterator var2 = this.f.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.d((Integer)var3.getKey());
         var1.d((Integer)var3.getValue());
      }

      var1.c(this.a != null ? this.a : "");
      var1.a(this.g);
      var1.a((int)(this.i * 100.0));
      var1.a(this.j);
      var1.a(this.b);
      var1.a(this.c);
      var1.a(this.k);
      var1.a(this.h ? 1 : 0);
      var1.d(this.p);
      var1.a(this.l);
      var1.a(this.e ? 1 : 0);
      return var1;
   }

   public boolean c() {
      return this.n;
   }

   public void b(boolean var1) {
      this.n = var1;
      iu.a(1816866669);
   }

   public boolean d() {
      return this.g;
   }

   public void c(boolean var1) {
      this.g = var1;
      iu.a(-977209488);
   }

   public boolean e() {
      return this.o;
   }

   public void d(boolean var1) {
      this.o = var1;
      iu.a(-257712713);
   }

   public void f() {
      this.e(!this.h);
   }

   public boolean g() {
      return this.h;
   }

   public void e(boolean var1) {
      this.h = var1;
      iu.a(377903249);
   }

   public int h() {
      return this.p;
   }

   public void a(int var1) {
      this.p = var1;
      iu.a(1995928356);
   }

   public double i() {
      return this.i;
   }

   public void a(double var1) {
      this.i = var1;
      iu.a(-994923212);
   }

   public int j() {
      return this.j;
   }

   public void b(int var1) {
      this.j = var1;
      this.j(-1);
      iu.a(2123085821);
   }

   public int k() {
      return this.b;
   }

   public void c(int var1) {
      this.b = var1;
      this.i(-1);
      iu.a(-1503023604);
   }

   public int l() {
      return this.c;
   }

   public void d(int var1) {
      this.c = var1;
      this.k(-1);
      iu.a(1488581149);
   }

   public String m() {
      return this.a;
   }

   public void a(String var1) {
      this.a = var1;
      iu.a(199819507);
   }

   public int n() {
      return this.k;
   }

   public void e(int var1) {
      this.k = var1;
      iu.a(1946362535);
   }

   public int o() {
      return this.d;
   }

   public void f(int var1) {
      this.d = var1;
      iu.a(1747061445);
   }

   public void a(String var1, int var2) {
      int var3 = this.d(var1);
      if (this.f.size() >= 10 && !this.f.containsKey(var3)) {
         Iterator var4 = this.f.entrySet().iterator();
         var4.next();
         var4.remove();
      }

      this.f.put(var3, var2);
      iu.a(-748414219);
   }

   public boolean b(String var1) {
      int var2 = this.d(var1);
      return this.f.containsKey(var2);
   }

   public int c(String var1) {
      int var2 = this.d(var1);
      return !this.f.containsKey(var2) ? 0 : (Integer)this.f.get(var2);
   }

   public int d(String var1) {
      return br.f(var1.toLowerCase());
   }

   public void g(int var1) {
      this.l = var1;
      Client.k();
      iu.a(-1491727168);
   }

   public int p() {
      return this.l;
   }

   public float q() {
      return (float)this.m / 100.0F;
   }

   public void h(int var1) {
      this.m = var1;
      iu.a(1785599557);
   }

   public boolean r() {
      return this.e;
   }

   public void f(boolean var1) {
      this.e = var1;
      iu.a(-401100723);
   }

   public void i(int var1) {
      Client.s.getCallbacks().post(new VolumeChanged(Type.EFFECTS));
   }

   public void s() {
      this.d = 2;
   }

   public void j(int var1) {
      Client.s.getCallbacks().post(new VolumeChanged(Type.MUSIC));
   }

   public void k(int var1) {
      Client.s.getCallbacks().post(new VolumeChanged(Type.AREA));
   }

   public int getAreaSoundEffectVolume() {
      return this.c;
   }

   public String getRememberedUsername() {
      return this.a;
   }

   public void setAreaSoundEffectVolume(int var1) {
      this.c = var1;
   }

   public aR t() {
      return this.b();
   }

   public void setRememberedUsername(String var1) {
      this.a = var1;
   }

   public void setSoundEffectVolume(int var1) {
      this.b = var1;
   }

   public boolean getHideUsername() {
      return this.g;
   }

   public int getSoundEffectVolume() {
      return this.b;
   }
}
