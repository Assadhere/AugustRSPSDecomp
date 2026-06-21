package osrs;

import java.util.Iterator;

public class kx implements jo {
   public void a(int var1, int var2, int var3, int var4) {
      Client.cL = var1;
      Client.cM = var2;
      ++Client.cm;
      bo.aQ.x.b(Client.x);
      Client.C();
      bG var5 = bo.aQ;
      if (bo.cC != null && Client.cY > 0) {
         int var6 = Client.cX * 128;
         Client.a(var5, var6, bo.cC);
      }

      bG var56 = bo.aQ;
      int var7 = Client.cJ.h - 1;
      if (bo.bN != null && bo.aQ == var56 && var7 >= 0 && Client.cJ.k[var7] == 60 && (Client.cY <= 0 || Client.cX != Client.cJ.l[var7])) {
         int var8 = Client.cJ.l[var7] * 128;
         Client.a(var56, var8, bo.bN);
      }

      Client.a(bo.aQ);
      Client.b(bo.aQ);
      Client.c(bo.aQ);
      bo.a(bo.aQ, fy.c);
      bo.a(bo.aQ, fy.a);
      bo.a(bo.aQ, cY.a);
      bG var57 = bo.aQ;
      int var9 = Client.G.j;
      int[] var10 = Client.G.a;

      int var11;
      for(var11 = 0; var11 < var9; ++var11) {
         if (Client.cG != var10[var11] && Client.P != var10[var11]) {
            Client.a(var57, var10[var11], true);
         }
      }

      bo.a(bo.aQ, cY.b);
      bo.a(bo.aQ, cY.c);
      Client.D();
      Client.d(bo.aQ);
      bo.a(bo.aQ, fy.b);
      fX.a(var1, var2, var3, var4, true, (byte)80);
      var11 = Client.az;
      int var12 = Client.aA;
      int var13 = Client.aB;
      int var14 = Client.aC;
      aU.a(var11, var12, var11 + var13, var12 + var14);
      aW.k();
      aU.d();
      int var15 = Client.bq;
      if (Client.cl / 256 > var15) {
         var15 = Client.cl / 256;
      }

      if (Client.df[4] && Client.dh[4] + 128 > var15) {
         var15 = Client.dh[4] + 128;
      }

      int var16 = Client.br & 2047;
      int var17 = bo.dm;
      int var18 = bo.dA;
      int var19 = bo.o;
      int var20 = Client.b(var15);
      int var21 = var14 - 334;
      if (var21 < 0) {
         var21 = 0;
      } else if (var21 > 100) {
         var21 = 100;
      }

      int var22 = (Client.au - Client.at) * var21 / 100 + Client.at;
      int var23 = var20 * var22 / 256;
      int var24 = 2048 - var15 & 2047;
      int var25 = 2048 - var16 & 2047;
      int var26 = 0;
      int var27 = 0;
      int var28 = var23;
      int var29;
      int var30;
      int var31;
      if (var24 != 0) {
         var29 = aW.d[var24];
         var30 = aW.e[var24];
         var31 = var27 * var30 - var23 * var29 >> 16;
         var28 = var27 * var29 + var23 * var30 >> 16;
         var27 = var31;
      }

      if (var25 != 0) {
         var29 = aW.d[var25];
         var30 = aW.e[var25];
         var31 = var26 * var30 + var28 * var29 >> 16;
         var28 = var28 * var30 - var26 * var29 >> 16;
         var26 = var31;
      }

      if (Client.bJ) {
         bo.cG = var17 - var26;
         bo.I = var18 - var27;
         bo.eu = var19 - var28;
         bo.db = var15;
         bo.bZ = var16;
      } else {
         bo.bM = var17 - var26;
         bo.bD = var18 - var27;
         bo.dO = var19 - var28;
         bo.bV = var15;
         bo.cV = var16;
      }

      int var32;
      int var33;
      int var34;
      if (Client.H == 1) {
         bE var58 = Client.M();
         var30 = (int)var58.h;
         var31 = (int)var58.j;
         if (Client.by >= 2 && Client.x % 50 == 0 && (bo.dm >> 7 != var30 >> 7 || bo.o >> 7 != var31 >> 7)) {
            var32 = (int)var58.i;
            var33 = (bo.dm >> 7) + bo.aQ.B;
            var34 = (bo.o >> 7) + bo.aQ.z;
            Client.a(var33, var34, var32, true);
         }

         var58.a();
      }

      if (!Client.bJ) {
         var29 = Client.E();
      } else {
         if (Client.bL.c()) {
            var30 = bo.aQ.y;
         } else {
            var31 = bo.bM >> 7;
            var32 = bo.dO >> 7;
            if (!bo.aQ.a(var31, var32)) {
               var30 = bo.aQ.y;
            } else {
               var33 = Client.b(bo.aQ, bo.bM, bo.dO, bo.aQ.y);
               if (var33 - bo.bD < 800 && (bo.aQ.v[bo.aQ.y][var31][var32] & 4) != 0) {
                  var30 = bo.aQ.y;
               } else {
                  var30 = 3;
               }
            }
         }

         var29 = var30;
      }

      var30 = bo.bM;
      var31 = bo.bD;
      var32 = bo.dO;
      var33 = bo.bV;
      var34 = bo.cV;

      int var35;
      int var36;
      for(var35 = 0; var35 < 5; ++var35) {
         if (Client.df[var35]) {
            var36 = (int)(Math.random() * (double)(Client.dg[var35] * 2 + 1) - (double)Client.dg[var35] + Math.sin((double)Client.di[var35] / 100.0 * (double)Client.dj[var35]) * (double)Client.dh[var35]);
            if (var35 == 0) {
               bo.bM += var36;
            }

            if (var35 == 1) {
               bo.bD += var36;
            }

            if (var35 == 2) {
               bo.dO += var36;
            }

            if (var35 == 3) {
               bo.cV = bo.cV + var36 & 2047;
            }

            if (var35 == 4) {
               bo.bV += var36;
               if (bo.bV < 128) {
                  bo.bV = 128;
               }

               if (bo.bV > 383) {
                  bo.bV = 383;
               }
            }
         }
      }

      var35 = aI.h;
      var36 = aI.i;
      int var37;
      if (var35 >= var11 && var35 < var11 + var13 && var36 >= var12 && var36 < var12 + var14) {
         var37 = var35 - var11;
         int var38 = var36 - var12;
         dB.c = var37;
         dB.d = var38;
         dB.b = true;
         dB.f = 0;
         dB.g[0] = -1L;
         dB.h[0] = Integer.MAX_VALUE;
         dB.e = false;
         Iterator var39 = Client.D.iterator();

         while(var39.hasNext()) {
            bG var40 = (bG)var39.next();
            var40.x.b(var40.y, var35 - var11, var36 - var12);
         }
      } else {
         dB.b = false;
         dB.f = 0;
      }

      ej.a(-643170947);
      Client.d(var11, var12, var13, var14, 0);
      ej.a(-254228690);
      var37 = aW.h();
      aW.a(Client.s.es);
      aW.h.b = Client.aD;
      dn var59 = Client.Q == 0 ? dn.a : dn.b;
      bo.aQ.x.a(var59);
      bo.aQ.x.a(bo.bM, bo.bD, bo.dO, bo.bV, bo.cV, var29, Client.cy, Client.cA, Client.bJ);
      dB.a();
      aW.a(false);
      if (Client.bb) {
         aU.c();
      }

      aW.h.b = var37;
      ej.a(-1968565891);
      bG var60 = bo.aQ;
      int var61 = Client.x;
      jZ.k = 0;
      iH var41 = Client.G;
      int var42 = Client.cG;
      int var43 = Client.P;
      int var44 = var41.j;
      int[] var45 = var41.a;

      int var46;
      for(var46 = 0; var46 < var44 + var60.d.a(); ++var46) {
         o var47;
         if (var46 < var44) {
            var47 = (o)var60.r.a((long)var45[var46]);
            if (var45[var46] == var42 || var45[var46] == var43) {
               continue;
            }
         } else {
            var47 = (o)var60.s.a((long)var60.d.b(var46 - var44));
         }

         bo.a(var60, var47, var46, var11, var12, var13, var14, var61, 1464060867);
      }

      int var50;
      int var52;
      for(var46 = 0; var46 < var60.e.a(); ++var46) {
         gZ var63 = (gZ)var60.t.a((long)var60.e.b(var46));
         if (var63 != null) {
            bG var48 = var63.n;
            iH var49 = Client.G;
            var50 = Client.cG;
            int var51 = Client.P;
            var52 = var49.j;
            int[] var53 = var49.a;

            for(int var54 = 0; var54 < var52 + var48.d.a(); ++var54) {
               o var55;
               if (var54 < var52) {
                  var55 = (o)var48.r.a((long)var53[var54]);
                  if (var53[var54] == var50 || var53[var54] == var51) {
                     continue;
                  }
               } else {
                  var55 = (o)var48.s.a((long)var48.d.b(var54 - var52));
               }

               bo.a(var48, var55, var54, var11, var12, var13, var14, var61, 518330938);
            }
         }
      }

      boolean var62 = Client.W;
      int var64;
      io var65;
      Iterator var67;
      bG var69;
      bG var70;
      t var71;
      t var72;
      if (var62) {
         var64 = Client.P;
         if (var64 >= 0) {
            var65 = Client.D;
            var67 = var65.iterator();

            while(true) {
               if (!var67.hasNext()) {
                  var69 = var65.a();
                  break;
               }

               var70 = (bG)var67.next();
               var72 = (t)var70.r.a((long)var64);
               if (var72 != null && !var70.a()) {
                  var69 = var70;
                  break;
               }
            }

            var71 = var69.a(var64);
            if (var71 != null) {
               var52 = Client.G.b(var64);
               bo.a(var69, var71, var52, var11, var12, var13, var14, var61, 1725624763);
            }
         }
      }

      var64 = Client.cG;
      if (var64 >= 0) {
         var65 = Client.D;
         var67 = var65.iterator();

         while(true) {
            if (!var67.hasNext()) {
               var69 = var65.a();
               break;
            }

            var70 = (bG)var67.next();
            var72 = (t)var70.r.a((long)var64);
            if (var72 != null && !var70.a()) {
               var69 = var70;
               break;
            }
         }

         var71 = var69.a(var64);
         if (var71 != null) {
            var52 = Client.G.b(var64);
            bo.a(var69, var71, var52, var11, var12, var13, var14, var61, -1951800532);
         }
      }

      jZ.a(var11, var12, var13, var14, var61);
      jZ.a(Client.bR, Client.D, var11, var12, Client.x);
      fC var66 = Client.bR;
      bG var68 = bo.aQ;
      var50 = Client.x;
      int var74;
      if (var66.b() == 4) {
         gZ var73 = (gZ)var68.t.a((long)var66.c());
         if (var73 != null) {
            var52 = var73.n.p * 64;
            var74 = var73.n.q * 64;
            jZ.a(var73.n, var52, var74, var66.h() * 2, 0);
            if (jZ.i > -1 && var50 % 20 < 10) {
               bo.dK[0].b(jZ.i + var11 - 12, jZ.j + var12 - 28);
            }
         }
      }

      ((dO)aW.h.c).e(Client.cd);
      Client.N = 0;
      var71 = Client.J();
      if (var71 != null) {
         var52 = (var71.t >> 7) + bo.aQ.B;
         var74 = (var71.ai >> 7) + bo.aQ.z;
         if (var52 >= 3053 && var52 <= 3156 && var74 >= 3056 && var74 <= 3136) {
            Client.N = 1;
         }

         if (var52 >= 3072 && var52 <= 3118 && var74 >= 9492 && var74 <= 9535) {
            Client.N = 1;
         }

         if (Client.N == 1 && var52 >= 3139 && var52 <= 3199 && var74 >= 3008 && var74 <= 3062) {
            Client.N = 0;
         }
      }

      bo.bM = var30;
      bo.bD = var31;
      bo.dO = var32;
      bo.bV = var33;
      bo.cV = var34;
      if (Client.aS && bo.eg.a(true, false) == 0) {
         Client.aS = false;
      }

      if (Client.aS) {
         Client.d(var11, var12, var13, var14, 0);
         Client.a(bv.n, false);
      }

      Client.B();
   }

   public void a(ag var1, int var2, int var3) {
      jS.a(var1, var2, var3, (byte)107);
   }

   public void a(int var1, int var2, int var3, int var4, int var5, double var6) {
      Client.b().a(var1, var2, var3, var4, var5, var6);
   }

   public void b(int var1, int var2, int var3, int var4) {
      Client.b().a(var1, var2, var3, var4);
   }

   public void a(int var1, int var2) {
      bo.er.a(var1, var2);
   }
}
