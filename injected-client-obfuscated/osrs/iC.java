package osrs;

import net.runelite.api.TileItem;
import net.runelite.api.events.ItemQuantityChanged;

public final class iC extends dG implements TileItem {
   public int a = -1;
   public int b = -1;
   public int c;
   public int d = 31;
   public bE e;
   public int f;
   public int g;
   public int h;
   public int i;
   public int j;
   public cF k;
   public boolean l;

   public iC() {
      this.e = bE.a;
   }

   public void a(int var1) {
      this.d = var1;
   }

   public boolean b(int var1) {
      if (var1 >= 0 && var1 <= 4) {
         return (this.d & 1 << var1) != 0;
      } else {
         return true;
      }
   }

   public final aH i(int var1) {
      aO var2 = aO.a(this.f);
      aH var3;
      if (this.k == null) {
         if (var1 != -1098105585) {
            throw new IllegalStateException();
         }

         var3 = var2.c(this.h);
      } else {
         var3 = var2.a(this.k);
      }

      if (var3 == null) {
         if (var1 != -1098105585) {
            throw new IllegalStateException();
         } else {
            return null;
         }
      } else {
         aH var5 = null;
         if (!bE.d.c(this.e)) {
            bE var6 = this.e;
            bE var7 = bE.d;
            bE var8 = bE.a(var7);
            var8.g(var6);
            var8.d();
            bE var9 = bE.d;
            float var10 = var9.f(var6);
            float var11 = (float)Math.acos((double)var10);
            fY var12 = fY.a();
            var12.a(var8, var11);
            var8.a();
            var12.e();
            var5 = var3.a(false);
            var5.a(var12);
            var12.b();
         }

         if (var5 == null) {
            if (var1 != -1098105585) {
               throw new IllegalStateException();
            }

            var3 = var3;
         } else {
            var3 = var5;
         }

         return var3;
      }
   }

   public boolean I() {
      aO var1 = aO.a(this.f);
      aH var2 = this.k == null ? var1.c(this.h) : var1.a(this.k);
      return var2 != null && var2.I != null;
   }

   public cF a() {
      return this.k;
   }

   public void a(cF var1) {
      this.k = var1;
   }

   public void c() {
      this.k = null;
   }

   public void a(bE var1) {
      this.e = var1;
   }

   public void c(int var1) {
      this.b = var1;
   }

   public void d(int var1) {
      if (this.b != -1) {
         Client.dF.debug("Item quantity changed: {} ({} -> {})", new Object[]{this.getId(), this.getQuantity(), var1});
         ItemQuantityChanged var2 = new ItemQuantityChanged(this, this.f(), this.getQuantity(), var1);
         Client.s.getCallbacks().post(var2);
      }

   }

   public int getDespawnTime() {
      return this.i - Client.aT + bo.fY;
   }

   public int d() {
      return this.a;
   }

   public int getVisibleTime() {
      return this.j - Client.aT + bo.fY;
   }

   public void e(int var1) {
      this.a = var1;
   }

   public void Z() {
      if (this.b != -1 && bo.y == null) {
         bo.y = this;
      }

   }

   public int e() {
      return this.b;
   }

   public jh f() {
      int var1 = this.b;
      int var2 = this.a;
      bG var3 = bG.b(this.c);
      if (var1 != -1 && var2 != -1 && var3 != null) {
         jh[][][] var4 = var3.h().q();
         jh var5 = var4[var3.getPlane()][var1][var2];
         return var5;
      } else {
         return null;
      }
   }

   public int getQuantity() {
      return this.h;
   }

   public boolean isPrivate() {
      return this.l;
   }

   public int getOwnership() {
      return this.g;
   }

   public int getId() {
      return this.f;
   }
}
