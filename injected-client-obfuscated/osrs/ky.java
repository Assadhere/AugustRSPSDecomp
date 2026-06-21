package osrs;

public class ky implements jK {
   public void b() {
      bn.c();
   }

   public void a(int var1, int var2, int var3, int var4) {
      bo.c(var1, var2, var3, var4);
   }

   public void c(ag var1) {
      if (var1.h == 1) {
         Client.a(var1.aG, "", 24, 0, 0, var1.T, var1.g);
      }

      if (var1.h == 2 && !Client.cN) {
         String var2 = bo.cR.c(var1);
         if (var2 != null) {
            Client.a(var2, bq.b(65280) + var1.aO, 25, 0, -1, var1.T, var1.g);
         }
      }

      if (var1.h == 3) {
         Client.a(bv.cw, "", 26, 0, 0, var1.T);
      }

      if (var1.h == 4) {
         Client.a(var1.aG, "", 28, 0, 0, var1.T);
      }

      if (var1.h == 5) {
         Client.a(var1.aG, "", 29, 0, 0, var1.T);
      }

      if (var1.h == 6 && bo.cR.g()) {
         Client.a(var1.aG, "", 30, 0, -1, var1.T);
      }

      if (var1.a) {
         if (Client.cN) {
            if (bl.c(bo.cR.i(var1)) && (bo.l & 32) == 32) {
               Client.a(Client.cQ, Client.cR + " " + bq.f + " " + var1.bF, 58, 0, var1.as, var1.T, var1.g);
            }
         } else {
            int var9;
            for(var9 = 31; var9 >= 0; --var9) {
               String var3;
               if (var1.E == var9) {
                  var3 = bo.cR.c(var1);
                  if (var3 != null) {
                     Client.a(var3, var1.bF, 25, 0, var1.as, var1.T, var1.g);
                  }
               }

               var3 = bo.cR.a(var1, var9);
               if (var3 != null) {
                  short var4;
                  int var5;
                  if (var9 > var1.E) {
                     var4 = 1007;
                     var5 = Client.a(var3, var1.bF, var4, var9 + 1, var1.as, var1.T, var1.g);
                  } else {
                     var4 = 57;
                     var5 = Client.a(var3, var1.bF, var4, var9 + 1, var1.as, var1.T, var1.g, var1.bX, 0);
                  }

                  if (var1.aI != null && var9 < var1.aI.length && var1.aI[var9] != null) {
                     String[] var6 = var1.aI[var9];

                     for(int var7 = var6.length - 1; var7 >= 0; --var7) {
                        int var8 = var7 + 1 << 16 | var9 + 1;
                        if (var6[var7] != null && !var6[var7].isEmpty()) {
                           Client.a(var5, var6[var7], "", var4, var8, var1.as, var1.T, var1.g, 0);
                        }
                     }
                  }
               }
            }

            var9 = bo.cR.i(var1);
            boolean var10 = (var9 & 1) != 0;
            if (var10) {
               Client.a(bv.i, "", 30, 0, var1.as, var1.T);
            }
         }
      }

   }

   public void a(int var1, int var2, int var3, int var4, String var5) {
      Client.a(var1, var2, var3, var4, var5);
   }

   public void a(int var1, int var2, int var3, int var4, String var5, String var6) {
      Client.a(var1, var2, var3, var4, var5, var6);
   }

   public void c() {
      Client.Q();
   }

   public void a() {
      Client.R();
   }

   public void a(int var1, int var2) {
      fq var3 = bo.ep;
      if (var3 != null) {
         bo.a(var3.b, var3.a, var3.f, var3.e, var3.c, var3.d, var3.g, var3.h, var1, var2);
      }

      bo.ep = null;
      Client.s(-1);
   }

   public void a(ac var1) {
      bh.a(var1);
   }

   public void b(ac var1) {
      bh.a(var1, 5000000, 0);
   }

   public void a(ag var1) {
      bo.a(var1);
   }

   public void b(ag var1) {
      Client.a(var1);
   }

   public void b(int var1, int var2) {
      bn.a(var1, var2);
   }

   public void a(ag var1, ag var2) {
      ka var3 = bo.cR.u;
      ag var4 = var3.f;
      ag var5 = var3.a;
      Client.a(var5, var4);
   }
}
