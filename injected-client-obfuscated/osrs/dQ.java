package osrs;

import net.runelite.api.Animation;
import net.runelite.api.GraphicsObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GraphicsObjectCreated;

public final class dQ extends dG implements GraphicsObject {
   public final gq a = new gq();
   public final bG b;
   public int c;
   public int d;
   public int e;
   public int f;
   public int g;
   public int h;

   public dQ(bG var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.b = var1;
      this.c = var2;
      this.d = var3;
      this.e = var4;
      this.f = var5;
      this.g = var6;
      this.h = var7 + var8;
      this.a.a(bn.b(this.c).b);
      this.e();
   }

   public boolean a() {
      return !this.a.b();
   }

   public boolean I() {
      if (this.a.b() && this.a.c().a(this.a.e())) {
         return true;
      } else {
         bn var1 = bn.b(this.c);
         aH var2 = var1.a();
         return var2 != null && var2.I != null;
      }
   }

   public int b() {
      return this.a.b() ? this.a.c().t : 0;
   }

   public LocalPoint getLocation() {
      return new LocalPoint(this.f(), this.g(), this.b);
   }

   public aH c() {
      aH var1;
      bn var2;
      if (this.a.c(30)) {
         var2 = null;
         var1 = (aH)var2;
      } else {
         var2 = bn.b(this.c);
         aH var3 = var2.c(this.a.e());
         if (var3 == null) {
            Object var4 = null;
            var1 = (aH)var4;
         } else {
            var1 = var3;
         }
      }

      return var1;
   }

   public void a(int var1) {
      if (!this.a()) {
         bo.cJ.a(this.b, this.e, this.f, false);
         int var2 = jM.a(this.a, var1, bo.cJ);
         bo.cJ.a();
         if ((var2 & 1) != 0) {
            this.a.a();
         }
      }

   }

   public bk d() {
      return this.a.b;
   }

   public boolean finished() {
      return this.a.b == null;
   }

   public Animation getAnimation() {
      return this.d();
   }

   public void e() {
      GraphicsObjectCreated var1 = new GraphicsObjectCreated(this);
      Client.s.getCallbacks().post(var1);
   }

   public WorldView getWorldView() {
      return this.h();
   }

   public final void a(int var1, byte var2) {
      this.a(var1);
   }

   public final aH i(int var1) {
      return this.c();
   }

   public void setFinished(boolean var1) {
      if (!var1) {
         throw new UnsupportedOperationException();
      } else {
         this.a.m();
      }
   }

   public int getAnimationFrame() {
      return this.a.c;
   }

   public int f() {
      return this.e;
   }

   public int getZ() {
      return this.g;
   }

   public int getLevel() {
      return this.d;
   }

   public int g() {
      return this.f;
   }

   public void b(int var1) {
      this.f = var1;
   }

   public bG h() {
      return this.b;
   }

   public int getStartCycle() {
      return this.h;
   }

   public int getId() {
      return this.c;
   }

   public void c(int var1) {
      this.e = var1;
   }
}
