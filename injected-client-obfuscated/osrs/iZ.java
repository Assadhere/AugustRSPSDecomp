package osrs;

import net.runelite.api.TileObject;

public class iZ extends Thread {
   public final int a;
   public static final fJ b = new fJ();
   public final iX c = new iX();
   public static final fJ d = new fJ();
   public static final Object e = new Object();
   public volatile boolean f = true;

   public void run() {
      Client.dF.trace("{} start", this.getName());

      while(this.f) {
         iY var1;
         synchronized(d) {
            var1 = (iY)d.k();
            if (var1 == null) {
               try {
                  d.wait();
               } catch (InterruptedException var5) {
               }
               continue;
            }
         }

         a(this.a, this.c, var1);
         a(var1);
      }

      Client.dF.trace("{} stop", this.getName());
   }

   public static void a() {
      while(true) {
         iY var0;
         synchronized(d) {
            var0 = (iY)d.k();
         }

         if (var0 == null) {
            while(true) {
               synchronized(e) {
                  if (bo.dH == 0) {
                     return;
                  }

                  try {
                     e.wait();
                  } catch (InterruptedException var4) {
                  }
               }
            }
         }

         a(-1, iX.c, var0);
         a(var0);
      }
   }

   public static void a(int var0, iX var1, iY var2) {
      il var3 = var2.f;
      TileObject var4 = var2.b;
      int var5 = var2.d;
      int var6 = var2.a;
      int var7 = var2.c;
      int var8 = var2.e;

      aH var9;
      try {
         var9 = var3.a(var1);
      } catch (Exception var11) {
         Client.dF.debug("exception drawing dynamic game entity", var11);
         return;
      }

      if (var9 != null) {
         var3.setModelHeight(var9.getModelHeight());
         int var10 = var3.getAnimationHeightOffset();
         Client.ef.drawDynamic(var0, bo.dE, bo.dF, var4, var3, var9, var5, var6, var7 - var10, var8);
      }

   }

   public static void a(iY var0) {
      synchronized(b) {
         b.c(var0);
      }

      synchronized(e) {
         --bo.dH;
         if (bo.dH == 0) {
            e.notify();
         }

      }
   }

   public static void b() {
      bo.dE = null;
      bo.dF = null;
   }

   public static void a(int var0) {
      if (bo.dG != null) {
         if (bo.dG.length == var0) {
            return;
         }

         iZ[] var1 = bo.dG;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            iZ var4 = var1[var3];
            var4.f = false;
         }

         synchronized(d) {
            d.notifyAll();
         }
      }

      bo.dG = new iZ[var0];

      for(int var7 = 0; var7 < var0; ++var7) {
         bo.dG[var7] = new iZ(var7, "RenderThread" + var7);
         bo.dG[var7].start();
      }

   }

   public static void a(TileObject var0, il var1, int var2, int var3, int var4, int var5) {
      assert bo.dF != null;

      iY var6;
      synchronized(b) {
         var6 = (iY)b.k();
      }

      if (var6 == null) {
         var6 = new iY();
      }

      var6.b = var0;
      var6.f = var1;
      var6.d = var2;
      var6.a = var3;
      var6.c = var4;
      var6.e = var5;
      synchronized(d) {
         d.c(var6);
         d.notify();
      }

      synchronized(e) {
         ++bo.dH;
      }
   }

   public static void a(dE var0, dh var1) {
      bo.dE = var0;
      bo.dF = var1;
   }

   public iZ(int var1, String var2) {
      super(var2);
      this.a = var1;
   }
}
