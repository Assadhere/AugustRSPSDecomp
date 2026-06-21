package osrs;

import java.util.ArrayList;
import java.util.Arrays;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;

public class fM implements jR {
   public final ArrayList a = new ArrayList(1);
   public int b = 0;
   public B c = new B();
   public String[] d = new String[3];
   public int e = -1;
   public int f = 0;
   public int g = -1;
   public int h = -1;
   public byte i;
   public aJ j;
   public aR k;

   public fM(int var1) {
      this.e = var1;

      for(int var2 = 0; var2 < 3; ++var2) {
         this.d[var2] = "";
      }

   }

   public void a() {
      this.h();
      this.k = null;
      this.a.clear();
   }

   public void a(int var1) {
      this.b = var1;
      this.f = 0;
      this.g = -1;
      this.h = -1;
      this.i = 0;
      this.j = aJ.c;
      this.k = null;
   }

   public void a(t var1) {
      this.d(var1);
      this.a.add(var1);
   }

   public void b(t var1) {
      this.c(var1);
      this.a.remove(var1);
   }

   public void b() {
      this.a.clear();
   }

   public t a(int var1, bG var2) {
      t var3 = new t(var1);
      if (this.k != null) {
         var3.a(this.k);
      }

      var3.x = this.f;
      var3.Z = this.h;
      ((o)var3).f(-1);
      var3.ah[0] = this.j;
      var3.au = this.c.a;
      var3.X = var2.o;
      var3.aF = this.d;
      var3.a(this.c.b - var2.B, this.c.c - var2.z);
      return var3;
   }

   public boolean c() {
      return this.b == -1;
   }

   public void d() {
      this.b = this.c.b();
   }

   public void a(int var1, int var2) {
      this.c.b(this.b, var1, var2);
      this.b = -1;
   }

   public int e() {
      return this.c.b;
   }

   public int f() {
      return this.c.c;
   }

   public int g() {
      return this.c.a;
   }

   public void b(int var1) {
      this.c.a = var1;
   }

   public void a(aJ var1) {
      for(int var2 = 0; var2 < this.a.size(); ++var2) {
         t var3 = (t)this.a.get(var2);
         bG var4 = ((o)var3).o();
         int var5 = this.c.b - var4.B;
         int var6 = this.c.c - var4.z;
         aJ var7 = var1;
         if (var4.o == 0 && Client.U == this.e && bo.E == fh.a && (var3.t < 1536 || var3.ai < 1536 || var3.t >= 11776 || var3.ai >= 11776)) {
            var7 = aJ.a;
         }

         var3.au = this.c.a;
         if (aJ.a == var7) {
            var3.a(var5, var6);
         } else {
            var3.a(var4, var5, var6, var7);
         }
      }

   }

   public void a(aR var1) {
      this.k = var1;

      for(int var2 = 0; var2 < this.a.size(); ++var2) {
         ((t)this.a.get(var2)).a(this.k);
      }

   }

   public void b(int var1, int var2) {
      for(int var3 = 0; var3 < this.a.size(); ++var3) {
         Client.a((t)this.a.get(var3), var1, var2);
      }

   }

   public void c(int var1) {
      this.h = var1;

      for(int var2 = 0; var2 < this.a.size(); ++var2) {
         ((o)this.a.get(var2)).Z = var1;
         ((o)this.a.get(var2)).f(-1);
      }

   }

   public void a(String var1) {
      boolean var2 = var1.charAt(0) == '~';
      if (var2) {
         var1 = var1.substring(1);
      }

      I var3 = null;

      for(int var4 = 0; var4 < this.a.size(); ++var4) {
         t var5 = (t)this.a.get(var4);
         var5.G = false;
         var5.E = 0;
         var5.s = 0;
         var5.aa = 150;
         var5.F = var1;
         ((o)var5).e(-1);
         var3 = var5.aP;
      }

      if (var3 != null && (var2 || Client.P == this.e)) {
         iw.a(2, var3.a(), var1);
      }

   }

   public void a(int var1, int var2, int var3, int var4) {
      for(int var5 = 0; var5 < this.a.size(); ++var5) {
         ((o)this.a.get(var5)).a(var1, var2, Client.x, var3, var4);
      }

   }

   public void a(int var1, int var2, int var3, int var4, int var5) {
      for(int var6 = 0; var6 < this.a.size(); ++var6) {
         ((o)this.a.get(var6)).a(var1, Client.x, var2, var3, var4, var5);
      }

   }

   public void d(int var1) {
      for(int var2 = 0; var2 < this.a.size(); ++var2) {
         ((o)this.a.get(var2)).b(var1);
      }

   }

   public void e(int var1) {
      this.g = var1;

      for(int var2 = 0; var2 < this.a.size(); ++var2) {
         t var3 = (t)this.a.get(var2);
         var3.v = this.g;
         if (var3.T == 0) {
            var3.x = this.g;
            ((o)var3).j();
         }
      }

   }

   public void a(int var1, j var2, boolean var3, String var4, byte[] var5) {
      boolean var6 = false;
      String var7 = null;
      String var8 = null;

      int var9;
      for(var9 = 0; var9 < this.a.size(); ++var9) {
         t var10 = (t)this.a.get(var9);
         if (var10.aP != null && var10.aO != null) {
            boolean var11 = false;
            if (var2.e && bn.l.a(var10.aP)) {
               var11 = true;
            }

            if (!var11 && Client.N == 0 && !var10.ax) {
               var6 = true;
               var7 = var10.aP.a();
               var8 = var10.aF[0];
               var10.F = var4.trim();
               ((o)var10).e(-1);
               var10.E = var1 >> 8;
               var10.s = var1 & 255;
               var10.aa = 150;
               byte[] var12 = var5;
               int[] var13;
               if (var5 != null && var5.length != 0 && var5.length <= 8) {
                  int[] var14 = new int[var5.length];
                  int var15 = 0;

                  while(true) {
                     if (var15 >= var12.length) {
                        var13 = var14;
                        break;
                     }

                     if (var12[var15] < 0 || var12[var15] >= osrs.k.a.length) {
                        var13 = null;
                        break;
                     }

                     var14[var15] = osrs.k.a[var12[var15]];
                     ++var15;
                  }
               } else {
                  var13 = null;
               }

               var10.L = var13;
               var10.G = var3;
               var10.H = Client.P != var10.j && var2.e && !Client.bE.isEmpty() && var4.toLowerCase().indexOf(Client.bE) == -1;
            }
         }
      }

      if (var6 && var7 != null) {
         if (var2.d) {
            var9 = var3 ? 91 : 1;
         } else {
            var9 = var3 ? 90 : 2;
         }

         String var16;
         if (var8 != null && !var8.isEmpty()) {
            var16 = var8;
         } else {
            var16 = var2.c != -1 ? bq.a(var2.c) : "";
         }

         iw.a(var9, var16 + var7, var4);
      }

   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      for(int var8 = 0; var8 < this.a.size(); ++var8) {
         t var9 = (t)this.a.get(var8);
         bG var10 = ((o)var9).o();
         int var11 = this.c.b - var10.B;
         int var12 = this.c.c - var10.z;
         var9.aj = var1 + var11;
         var9.c = var2 + var12;
         var9.ad = var3 + var11;
         var9.w = var4 + var12;
         var9.ak = var5;
         var9.n = var6;
         var9.r = var7;
         var9.l = 0;
         var9.b(var9.ad, var9.w);
         var9.au = this.c.a;
      }

   }

   public void a(String[] var1) {
      this.d = (String[])Arrays.copyOf(var1, var1.length);

      for(int var2 = 0; var2 < this.a.size(); ++var2) {
         ((t)this.a.get(var2)).aF = (String[])Arrays.copyOf(var1, var1.length);
      }

   }

   public void a(int var1, int var2, byte var3, byte var4, byte var5, byte var6) {
      for(int var7 = 0; var7 < this.a.size(); ++var7) {
         t var8 = (t)this.a.get(var7);
         var8.ab = var1;
         var8.M = var2;
         var8.b.a(var3, var4, var5, var6);
      }

   }

   public void b(int var1, int var2, int var3, int var4) {
      for(int var5 = 0; var5 < this.a.size(); ++var5) {
         ((o)this.a.get(var5)).a(var1, var2, var3, var4);
      }

   }

   public void c(t var1) {
      Client.s.getCallbacks().post(new PlayerDespawned(var1));
      if (bo.dZ == var1) {
         bo.dZ = null;
      }

   }

   public void d(t var1) {
      if (var1.getId() == Client.P) {
         bo.dZ = var1;
      }

      Client.s.getCallbacks().postDeferred(new PlayerSpawned(var1));
   }

   public void h() {
      bo.dZ = null;
   }
}
