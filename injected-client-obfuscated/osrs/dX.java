package osrs;

import com.google.common.primitives.Doubles;
import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import net.runelite.api.Perspective;

public class dX implements Runnable {
   public dU a = null;
   public dU b = null;
   public boolean c = false;
   public Thread d;

   public dX() {
      bo.bY = "Unknown";
      bo.cd = "1.6";

      try {
         bo.bY = System.getProperty("java.vendor");
         bo.cd = System.getProperty("java.version");
      } catch (Exception var2) {
      }

      this.c = false;
      this.d = new Thread(this);
      this.d.setPriority(10);
      this.d.setDaemon(true);
      this.d.start();
   }

   public final void a() {
      synchronized(this) {
         this.c = true;
         this.notifyAll();
      }

      try {
         this.d.join();
      } catch (InterruptedException var3) {
      }

   }

   public final void run() {
      while(true) {
         dU var1;
         synchronized(this) {
            while(true) {
               if (this.c) {
                  return;
               }

               if (this.a != null) {
                  var1 = this.a;
                  this.a = this.a.d;
                  if (this.a == null) {
                     this.b = null;
                  }
                  break;
               }

               try {
                  this.wait();
               } catch (InterruptedException var7) {
               }
            }
         }

         try {
            int var2 = var1.c;
            if (var2 == 1) {
               var1.e = new Socket(InetAddress.getByName((String)var1.f), var1.b);
            } else if (var2 == 2) {
               Thread var3 = new Thread((Runnable)var1.f);
               var3.setDaemon(true);
               var3.start();
               var3.setPriority(var1.b);
               var1.e = var3;
            } else if (var2 == 4) {
               var1.e = new DataInputStream(((URL)var1.f).openStream());
            }

            var1.a = 1;
         } catch (ThreadDeath var5) {
            throw var5;
         } catch (Throwable var6) {
            var1.a = 2;
         }
      }
   }

   public final dU a(int var1, int var2, int var3, Object var4) {
      dU var5 = new dU();
      var5.c = var1;
      var5.b = var2;
      var5.f = var4;
      synchronized(this) {
         if (this.b != null) {
            this.b.d = var5;
            this.b = var5;
         } else {
            this.b = this.a = var5;
         }

         this.notify();
         return var5;
      }
   }

   public dU a(String var1, int var2) {
      return this.a(1, var2, 0, var1);
   }

   public final dU a(Runnable var1, int var2) {
      return this.a(2, var2, 0, var1);
   }

   public static final void a(ag var0, int var1, int var2, byte var3) {
      if ((eM.g == 0 || eM.g == 3) && !Client.s.isMenuOpen()) {
         int var4 = aI.k;
         if (var4 == 0) {
            int var5 = Client.bB;
            if (bo.i && var5 != 0) {
               boolean var6 = aI.h >= var1 && aI.i >= var2 && aI.h < var1 + var0.getWidth() && aI.i < var2 + var0.getHeight();
               if (var6) {
                  double var7 = (double)(-var5) * 0.25 + Client.dE;
                  Client.dE = Doubles.constrainToRange(var7, 2.0, 8.0);
               }
            }
         } else {
            ar var21 = var0.b(bo.cR, true);
            if (var21 == null) {
               return;
            }

            int var22 = aI.l - var1;
            int var23 = aI.m - var2;
            if (!var21.b(var22, var23)) {
               return;
            }

            if (var4 != 1 && (bo.cP || var4 != 4)) {
               if (var4 == 2) {
                  Client.dE = 4.0;
                  aI.k = 0;
               }
            } else {
               hf var8 = Client.D.i(-1);
               int var9;
               int var10;
               int var11;
               int var12;
               int var13;
               int var14;
               int var15;
               if (hf.b == var8) {
                  var9 = var22 - var21.a() / 2;
                  var10 = var23 - var21.b() / 2;
                  var11 = Client.br & 2047;
                  var12 = Perspective.SINE[var11];
                  var13 = Perspective.COSINE[var11];
                  var14 = var9 * var13 + var10 * var12 >> 16;
                  var15 = var10 * var13 - var9 * var12 >> 16;
                  float var16 = (float)Client.dE / 128.0F;
                  int var17 = (int)((float)var14 / var16);
                  int var18 = (int)((float)var15 / var16);
                  int var19 = Client.cy + var17 >> 7;
                  int var20 = Client.cA - var18 >> 7;
                  Client.a(var19, var20, var9, var10);
               } else if (hf.c == var8) {
                  var9 = var1 + var21.a() / 2;
                  var10 = var2 + var21.b() / 2;
                  var11 = aI.h - var9;
                  var12 = aI.i - var10;
                  var13 = Client.b(Math.atan2((double)var12, (double)var11)) - 512;
                  var14 = var13 - Client.br;
                  var15 = (var14 + 64 & 2047) / 128;
                  Client.cX = var15;
                  Client.cY = 30;
                  Client.as(var15);
               }
            }
         }
      }

   }
}
