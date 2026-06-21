package osrs;

import net.runelite.api.NodeCache;

public final class eI implements NodeCache {
   public float a = 0.0F;
   public aA b = new aA();
   public fZ c = new fZ();
   public int d;
   public int e;
   public W f;
   public int g;

   public eI(int var1) {
      this.d = var1;
      this.e = var1;

      int var2;
      for(var2 = 1; var2 + var2 < var1; var2 += var2) {
      }

      this.f = new W(var2);
      this.c();
   }

   public aA a(long var1) {
      aA var3 = (aA)this.f.a(var1);
      if (var3 != null) {
         this.c.a(var3);
      }

      return var3;
   }

   public void b(long var1) {
      aA var3 = (aA)this.f.a(var1);
      if (var3 != null) {
         var3.X();
         var3.ae();
         ++this.e;
      }

   }

   public void a(aA var1, long var2) {
      if (this.e == 0) {
         aA var4 = this.c.b();
         var4.X();
         var4.ae();
         if (this.b == var4) {
            aA var5 = this.c.b();
            var5.X();
            var5.ae();
         }
      } else {
         --this.e;
      }

      this.f.a(var1, var2);
      this.c.a(var1);
   }

   public void a() {
      this.c.a();
      this.f.a();
      this.b = new aA();
      this.e = this.d;
      this.d();
   }

   public static void a(String var0, eI var1) {
      synchronized(var1) {
         boolean var4 = var1.b();
         var1.a = var1.a * 0.92F + (var4 ? 0.07999998F : 0.0F);
         if (var4) {
            if (var1.a > 0.2F) {
               Client.dF.trace("cache {} is thrashing", var0);
            }

            if (var1.a > 0.9F && var1.d < var1.g * 8) {
               var1.a(var1.d * 2);
               Client.dF.info("cache {} thrashing, enlarging to {} entries", var0, var1.d);
            }
         }

         var1.c.c(var1.b);
      }
   }

   public void a(int var1) {
      if (var1 > this.d) {
         int var2;
         for(var2 = 1; var2 < var1; var2 += var2) {
         }

         this.d = var2;
         this.reset();
         this.f = new W(var2);
      }

   }

   public void b(int var1) {
      this.a(var1);
      this.g = this.d;
   }

   public boolean b() {
      return this.e <= 0 && this.b.af() == null;
   }

   public void c() {
      this.g = this.d;
   }

   public void d() {
      this.a = 0.0F;
      this.c.c(this.b);
   }

   public void b(aA var1, long var2) {
      this.a(var1, var2);
   }

   public static void e() {
      a("EnumDefinition_cached", M.h);
      a("SpotAnimationDefinition_cachedModels", bn.m);
      a("animPoseCache", bk.f);
      a("animationCache", bk.e);
      a("animayaPoses", bk.g);
      a("cachedModels2", aC.j);
      a("dbRowConfigCache", C.a);
      a("dbTableConfigCache", E.a);
      a("dbTableIndexCache", Client.bM);
      a("field3705", cC.a);
      a("field3706", cU.a);
      a("field3715", cM.a);
      a("field3763", cZ.a);
      a("field3766", cV.a);
      a("field3769", cQ.a);
      a("field3786", cL.a);
      a("field3828", bH.b);
      a("field4095", cE.a);
      a("field4100", cS.a);
      a("field4112", aE.k);
      a("field4130", bC.a);
      a("field4157", cT.a);
      a("field4244", cN.a);
      a("field4268", dj.a);
      a("field4293", ab.o);
      a("field903", Client.bN);
      a("healthBarCache", Z.b);
      a("healthBarSpriteCache", Z.c);
      a("hitsplatCompCache", ab.m);
      a("hitsplatFontCache", ab.n);
      a("identKits", af.d);
      a("inventoryCache", at.a);
      a("itemModelCache", aO.f);
      a("itemSpriteCache", aO.g);
      a("items", aO.e);
      a("npcModelCache", aN.b);
      a("npcs", aN.a);
      a("objModelDataCache", aC.e);
      a("objects", aC.d);
      a("overlays", N.d);
      a("paramCompositionCache", aT.a);
      a("playerModelCache", aZ.e);
      a("scriptCache", v.a);
      a("spotanimModelCache", bn.n);
      a("structCompositionCache", bs.a);
      a("underlays", O.a);
      a("varbits", bB.b);
      a("varplayers", bD.a);
      a("worldEntitySpriteCache", bH.u);
   }

   public void reset() {
      this.a();
   }

   public aA c(long var1) {
      return this.a(var1);
   }
}
