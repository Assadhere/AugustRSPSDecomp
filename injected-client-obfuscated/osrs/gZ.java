package osrs;

import net.runelite.api.WorldEntity;
import net.runelite.api.WorldEntityConfig;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;

public class gZ extends az implements WorldEntity, hW {
   public gq a;
   public int b;
   public boolean c = false;
   public final ee d;
   public int e;
   public gL[] f;
   public int g;
   public int h;
   public fy i;
   public int j;
   public gU k;
   public boolean l;
   public gq m;
   public bG n;
   public bH o;

   public gZ(int var1, bG var2) {
      this.j();
      this.d = new ee();
      this.e = 0;
      this.f = new gL[10];
      this.g = 0;
      this.h = 0;
      this.i = fy.a;
      this.j = 31;
      this.k = new gT();
      this.l = false;
      this.m = new gq();
      this.a = new gq();
      this.b = 0;
      this.e = var1;
      this.n = var2;
      this.g = 0;

      for(int var3 = 0; var3 < 10; ++var3) {
         this.f[var3] = new gL();
      }

   }

   public gq a() {
      return this.b == 0 && this.a.b() && this.a.c().h() ? this.a : null;
   }

   public void a(boolean var1) {
      gq var2 = this.a();
      gq var3 = var2 != null ? var2 : this.m;
      this.n.x.r.d();
      if (var3 != null && var3.d() != -1 && var3.k() && var3.c().c()) {
         cz var4 = var3.c().f();
         de var5 = var4.i.a();
         cq var6 = var5.a(0);
         if (var6 != null) {
            var5.a(var4, var3.e());
            this.n.x.r.b(var6.e(304056008), 53010330);
            this.n.x.r.p = -this.n.x.r.p;
         }
      }

      this.n.x.bA = this.n.y;
      if (var1) {
         this.n.x.u = -1200;
         this.n.x.v = 0.01F;
         int var9 = this.o.l();
         dH var10 = this.n.x.e;
         byte var11 = (byte)(var9 >> 10 & 63);
         byte var7 = hj.a(var9);
         byte var8 = (byte)(var9 & 127);
         var10.a(var11, var7, var8, (byte)127);
      } else {
         this.n.x.u = 0;
         this.n.x.v = 1.0F;
         this.n.x.e.a();
      }

   }

   public boolean b() {
      return this.n.x.v == 0.01F;
   }

   public int k() {
      return this.d.a();
   }

   public int c() {
      return this.d.b();
   }

   public int l() {
      return this.d.c();
   }

   public int m() {
      if (this.h != 0) {
         bG var1 = Client.D.b(this.h);
         if (var1 != null) {
            return var1.y;
         }
      }

      return bo.aQ.y;
   }

   public int d() {
      return this.d.f();
   }

   public ee e() {
      return this.g == 0 ? this.d : this.f[0].a;
   }

   public void a(bH var1) {
      this.o = var1;
      this.n.x.bB = var1.e();
      this.n.x.bC = var1.f();
      this.c(var1.g());
   }

   public fy f() {
      return this.i;
   }

   public fz b(boolean var1) {
      return var1 ? fz.b : this.o.i();
   }

   public void a(fy var1) {
      this.i = var1;
   }

   public void a(int var1) {
      this.j = var1;
   }

   public boolean b(int var1) {
      if (var1 >= 0 && var1 <= 4) {
         return (this.j & 1 << var1) != 0;
      } else {
         return true;
      }
   }

   public int g() {
      return this.n.p * 64 + this.o.e();
   }

   public int h() {
      return this.n.q * 64 + this.o.f();
   }

   public void c(int var1) {
      this.m.a(var1);
   }

   public int i() {
      return this.a.d();
   }

   public void a(int var1, int var2) {
      for(int var3 = 0; var3 < this.f.length; ++var3) {
         this.f[var3].a.b(var1, var2);
      }

      this.d.b(var1, var2);
      this.k.a(var1, var2);
   }

   public final void a(bG var1, ee var2) {
      int var3 = var2.d();
      int var4 = var2.e();
      if (!var1.a(var3, var4)) {
         this.a(var2);
      } else {
         int var5 = var2.a() - this.f[0].a.a();
         int var6 = var2.c() - this.f[0].a.c();
         this.a(var2, Math.abs(var5), Math.abs(var6));
      }

   }

   public void a(ee var1) {
      this.b(var1);
      this.d.a(var1);
      this.f[0].a.a(var1);
      this.g = 0;
      this.l = false;
   }

   public void a(ee var1, int var2, int var3) {
      if (this.g < 9) {
         ++this.g;
      }

      for(int var4 = this.g; var4 > 0; --var4) {
         gL var5 = this.f[var4];
         this.f[var4] = this.f[var4 - 1];
         this.f[var4 - 1] = var5;
      }

      this.f[0].a.a(var1);
      this.f[0].b = Client.x;
   }

   public bE b(int var1, int var2) {
      ge var3 = ge.a();
      fX var4 = fX.a();
      var3.c.a(hg.a(this.d.f()), 0.0F, 0.0F);
      var3.d.b((float)this.d.a(), 0.0F, (float)this.d.c());
      int var5 = var1 - this.g();
      int var6 = var2 - this.h();
      var4.a((ge)var3, (byte)79);
      var3.b();
      bE var7 = var4.b((float)var5, 0.0F, (float)var6);
      var4.b();
      return var7;
   }

   public bE c(int var1, int var2) {
      ge var3 = ge.a();
      fX var4 = fX.a();
      var3.c.a(hg.a(this.d.f()), 0.0F, 0.0F);
      var3.d.b((float)this.d.a(), 0.0F, (float)this.d.c());
      var4.a((ge)var3, (byte)7);
      var4.g();
      var3.b();
      bE var5 = var4.b((float)var1, 0.0F, (float)var2);
      var5.c((float)this.g(), 0.0F, (float)this.h());
      var4.b();
      return var5;
   }

   public final void d(int var1, int var2) {
   }

   public float v() {
      return (float)this.d.c;
   }

   public void d(int var1) {
      if (this.g == 0) {
         this.c(this.f[0].a);
      } else {
         if (!this.l) {
            if (this.c) {
               this.k.a(this.d, (double)(var1 - 1), this.g);
            }

            this.k.b(this.d, this.f[0], var1);
            this.l = true;
         }

         if (this.k.a(this.d, (double)var1 + bo.ce, this.g)) {
            --this.g;
            this.l = false;
         }

         this.c = true;
      }

   }

   public int getTargetOrientation() {
      return this.g > 0 ? this.f[0].a.a : this.getOrientation();
   }

   public WorldView getWorldView() {
      return this.q();
   }

   public WorldEntityConfig getConfig() {
      return this.n();
   }

   public int getOwnerType() {
      return this.i.e();
   }

   public LocalPoint getTargetLocation() {
      return this.g > 0 ? new LocalPoint(this.f[0].a.b, this.f[0].a.c, 0) : this.getLocalLocation();
   }

   public LocalPoint getLocalLocation() {
      return new LocalPoint(this.d.b, this.d.c, 0);
   }

   public void j() {
      Client.dM.add(this);
   }

   public int getOrientation() {
      return this.d.a;
   }

   public LocalPoint getCameraFocus() {
      return this.getLocalLocation();
   }

   public float w() {
      return (float)this.d.b;
   }

   public LocalPoint transformToMainWorld(LocalPoint var1) {
      if (var1.getWorldView() != this.e) {
         throw new IllegalArgumentException("LocalPoint doesn't belong do this WorldEntity");
      } else {
         bE var2 = this.f(var1.getX(), var1.getY());
         LocalPoint var3 = new LocalPoint((int)var2.h, (int)var2.j, 0);
         var2.f();
         return var3;
      }
   }

   public void b(ee var1) {
      this.c = false;
   }

   public bH n() {
      return this.o;
   }

   public void e(int var1, int var2) {
      this.a(var1, var2);
   }

   public boolean isHiddenForOverlap() {
      return this.b();
   }

   public bE f(int var1, int var2) {
      return this.b(var1, var2);
   }

   public void c(ee var1) {
      this.a(var1);
   }

   public int o() {
      return this.k();
   }

   public int p() {
      return this.l();
   }

   public int D() {
      return this.m();
   }

   public bG q() {
      return this.n;
   }
}
