package osrs;

import net.runelite.api.Model;
import net.runelite.api.Renderable;

public abstract class dG extends hD implements Renderable {
   public int cd = 1000;

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, int var12, int var13, boolean var14) {
      if (aW.h.p != 1 || this.I()) {
         aH var15 = this.i(-1098105585);
         if (var15 != null) {
            int var16 = var5 - this.b();
            this.cd = var15.cd;
            var15.a(this.P());
            var15.a(var1, var2, var3, var4 - var7, var16 - var8, var6 - var9, var10);
            var15.a(dg.a);
         }
      }

   }

   public void a(int var1, fX var2, int var3, int var4, int var5, long var6) {
      if (aW.h.p != 1 || this.I()) {
         aH var8 = this.i(-1098105585);
         if (var8 != null) {
            int var9 = var4 - this.b();
            this.cd = var8.cd;
            var8.a(this.P());
            var8.a(var1, var2, var3, var9, var5, var6);
            var8.a(dg.a);
         }
      }

   }

   public aH i(int var1) {
      return null;
   }

   public boolean I() {
      return true;
   }

   public dg P() {
      return dg.a;
   }

   public int b() {
      return 0;
   }

   public static void a(String var0, int var1) {
      try {
         Client.s.getCallbacks().openUrl(var0);
      } catch (Exception var3) {
         Client.dF.error("unable to open url {}", var0, var3);
      }

   }

   public int getRenderMode() {
      return this.ah().e;
   }

   public Model getModel() {
      return this.ai();
   }

   public int getAnimationHeightOffset() {
      return this.b();
   }

   public void setModelHeight(int var1) {
      this.cd = var1;
   }

   public dg ah() {
      return this.P();
   }

   public void b(int var1, fX var2, int var3, int var4, int var5, long var6) {
      this.a(var1, var2, var3, var4, var5, var6);
   }

   public void b(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, int var12, int var13, boolean var14) {
      this.a(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var12, var13, var14);
   }

   public int getModelHeight() {
      return this.cd;
   }

   public aH ai() {
      return this.i(-1098105585);
   }
}
