package osrs;

import net.runelite.api.Projection;

public abstract class dE implements Projection {
   public void a(dh var1, du var2, int var3, int var4, byte var5) {
      try {
         dN var6 = aW.h;
         boolean var7 = Client.s.isGpu();
         if (Client.ef != null) {
            Client.ef.drawSceneTileModel(var1, var2, var3, var4);
         }

         jh var8 = var1.bh[var1.aZ.y][var1.s + var3][var1.s + var4];
         var6.a = 0;
         int var9 = var2.t.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            int var11 = var2.t[var10];
            int var12 = var2.a[var10];
            int var13 = var2.b[var10];
            float var14 = du.k[var11];
            float var15 = du.k[var12];
            float var16 = du.k[var13];
            float var17 = du.l[var11];
            float var18 = du.l[var12];
            float var19 = du.l[var13];
            float var20 = du.m[var11];
            float var21 = du.m[var12];
            float var22 = du.m[var13];
            if ((var14 - var15) * (var19 - var18) - (var16 - var15) * (var17 - var18) > 0.0F) {
               if (dh.b(var1.af, var1.ag, (int)var17, (int)var18, (int)var19, (int)var14, (int)var15, (int)var16)) {
                  float var23 = a(var1.af, var1.ag, (int)var14, (int)var15, (int)var16, (int)var17, (int)var18, (int)var19, var20, var21, var22);
                  if (var23 > var1.y && bo.aI <= var1.aZ.y) {
                     var1.a(var3, var4, var23);
                  }

                  if (var8 != null && var2 == var8.e() && var1.af >= Client.s.getViewportXOffset() && var1.af < Client.s.getViewportXOffset() + Client.s.getViewportWidth() && var1.ag >= Client.s.getViewportYOffset() && var1.ag < Client.s.getViewportYOffset() + Client.s.getViewportHeight()) {
                     var1.s(var3, var4, var8.f());
                  }
               }

               if (!var7) {
                  var6.q = false;
                  int var26 = var6.f;
                  if (var14 < 0.0F || var15 < 0.0F || var16 < 0.0F || var14 > (float)var26 || var15 > (float)var26 || var16 > (float)var26) {
                     var6.q = true;
                  }

                  if (var2.c != null && var2.c[var10] != -1) {
                     if (!dh.c) {
                        if (var2.d) {
                           df.a(var17, var18, var19, var14, var15, var16, var20, var21, var22, var2.u[var10], var2.v[var10], var2.w[var10], du.n[0], du.n[1], du.n[3], du.o[0], du.o[1], du.o[3], du.p[0], du.p[1], du.p[3], var2.c[var10]);
                        } else {
                           df.a(var17, var18, var19, var14, var15, var16, var20, var21, var22, var2.u[var10], var2.v[var10], var2.w[var10], du.n[var11], du.n[var12], du.n[var13], du.o[var11], du.o[var12], du.o[var13], du.p[var11], du.p[var12], du.p[var13], var2.c[var10]);
                        }
                     } else {
                        int var24 = var6.c.c(var2.c[var10]);
                        df.c(var17, var18, var19, var14, var15, var16, var20, var21, var22, dh.d(var24, var2.u[var10]), dh.d(var24, var2.v[var10]), dh.d(var24, var2.w[var10]));
                     }
                  } else if (var2.u[var10] != 12345678) {
                     df.c(var17, var18, var19, var14, var15, var16, var20, var21, var22, var2.u[var10], var2.v[var10], var2.w[var10]);
                  }
               }
            }
         }
      } catch (Exception var25) {
         Client.dF.warn("error during overlay rendering", var25);
      }

   }

   public void a(dh var1, dv var2, int var3, int var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, byte var17) {
      try {
         dN var18 = aW.h;
         int var19 = var18.b;
         int var20 = var18.d;
         int var21 = var18.e;
         float var22 = (float)var19 * var5 / var13 + (float)var20;
         float var23 = (float)var19 * var9 / var13 + (float)var21;
         float var24 = (float)var19 * var6 / var14 + (float)var20;
         float var25 = (float)var19 * var10 / var14 + (float)var21;
         float var26 = (float)var19 * var7 / var15 + (float)var20;
         float var27 = (float)var19 * var11 / var15 + (float)var21;
         float var28 = (float)var19 * var8 / var16 + (float)var20;
         float var29 = (float)var19 * var12 / var16 + (float)var21;
         float var30 = df.a(var13);
         float var31 = df.a(var14);
         float var32 = df.a(var15);
         float var33 = df.a(var16);
         var18.a = 0;
         jh var34 = var1.bh[var1.aZ.y][var1.s + var3][var1.s + var4];
         float var35;
         int var36;
         int var38;
         if ((var25 - var29) * (var26 - var28) - (var24 - var28) * (var27 - var29) > 0.0F) {
            if (dh.b(var1.af, var1.ag, (int)var27, (int)var29, (int)var25, (int)var26, (int)var28, (int)var24)) {
               var35 = a(var1.af, var1.ag, (int)var26, (int)var28, (int)var24, (int)var27, (int)var29, (int)var25, var32, var33, var31);
               if (var35 > var1.y && bo.aI <= var1.aZ.y) {
                  var1.a(var3, var4, var35);
               }

               if (var1.af >= Client.s.getViewportXOffset() && var1.af < Client.s.getViewportXOffset() + Client.s.getViewportWidth() && var1.ag >= Client.s.getViewportYOffset() && var1.ag < Client.s.getViewportYOffset() + Client.s.getViewportHeight() && var34 != null && var2 == var34.i()) {
                  var1.s(var3, var4, var34.f());
               }
            }

            if (!Client.s.isGpu()) {
               var18.q = false;
               var38 = var18.f;
               if (var26 < 0.0F || var28 < 0.0F || var24 < 0.0F || var26 > (float)var38 || var28 > (float)var38 || var24 > (float)var38) {
                  var18.q = true;
               }

               if (var2.j == -1) {
                  if (var2.h != 12345678) {
                     df.c(var27, var29, var25, var26, var28, var24, var32, var33, var31, var2.h, var2.i, var2.g);
                  }
               } else if (!dh.c) {
                  if (var2.e) {
                     df.a(var27, var29, var25, var26, var28, var24, var32, var33, var31, var2.h, var2.i, var2.g, (int)var5, (int)var6, (int)var8, (int)var9, (int)var10, (int)var12, (int)var13, (int)var14, (int)var16, var2.j);
                  } else {
                     df.a(var27, var29, var25, var26, var28, var24, var32, var33, var31, var2.h, var2.i, var2.g, (int)var7, (int)var8, (int)var6, (int)var11, (int)var12, (int)var10, (int)var15, (int)var16, (int)var14, var2.j);
                  }
               } else {
                  var36 = var18.c.c(var2.j);
                  df.c(var27, var29, var25, var26, var28, var24, var32, var33, var31, dh.d(var36, var2.h), dh.d(var36, var2.i), dh.d(var36, var2.g));
               }
            }
         }

         if ((var22 - var24) * (var29 - var25) - (var23 - var25) * (var28 - var24) > 0.0F) {
            if (dh.b(var1.af, var1.ag, (int)var23, (int)var25, (int)var29, (int)var22, (int)var24, (int)var28)) {
               var35 = a(var1.af, var1.ag, (int)var22, (int)var24, (int)var28, (int)var23, (int)var25, (int)var29, var30, var31, var33);
               if (var35 > var1.y && bo.aI <= var1.aZ.y) {
                  var1.a(var3, var4, var35);
               }

               if (var1.af >= Client.s.getViewportXOffset() && var1.af < Client.s.getViewportXOffset() + Client.s.getViewportWidth() && var1.ag >= Client.s.getViewportYOffset() && var1.ag < Client.s.getViewportYOffset() + Client.s.getViewportHeight() && var34 != null && var2 == var34.i()) {
                  var1.s(var3, var4, var34.f());
               }
            }

            if (!Client.s.isGpu()) {
               var18.q = false;
               var38 = var18.f;
               if (var22 < 0.0F || var24 < 0.0F || var28 < 0.0F || var22 > (float)var38 || var24 > (float)var38 || var28 > (float)var38) {
                  var18.q = true;
               }

               if (var2.j == -1) {
                  if (var2.f != 12345678) {
                     df.c(var23, var25, var29, var22, var24, var28, var30, var31, var33, var2.f, var2.g, var2.i);
                  }
               } else if (!dh.c) {
                  df.a(var23, var25, var29, var22, var24, var28, var30, var31, var33, var2.f, var2.g, var2.i, (int)var5, (int)var6, (int)var8, (int)var9, (int)var10, (int)var12, (int)var13, (int)var14, (int)var16, var2.j);
               } else {
                  var36 = var18.c.c(var2.j);
                  df.c(var23, var25, var29, var22, var24, var28, var30, var31, var33, dh.d(var36, var2.f), dh.d(var36, var2.g), dh.d(var36, var2.i));
               }
            }
         }
      } catch (Exception var37) {
         Client.dF.warn("error during underlay rendering", var37);
      }

   }

   public void a(dh var1, du var2, int var3, int var4, int var5) {
      bo.aI = var3;
      this.b(var1, var2, var4, var5);
   }

   public void a(dh var1, dv var2, int var3, int var4, int var5, int var6) {
      bo.aI = var3;
      this.b(var1, var2, var4, var5, var6);
   }

   public abstract void a(dG var1, int var2, int var3, int var4, int var5, long var6);

   public void b(dh var1, dv var2, int var3, int var4, int var5) {
      this.a(var1, var2, var3, var4, var5);
   }

   public abstract void a(dh var1, du var2, int var3, int var4);

   public static float a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10) {
      return ks.a(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public abstract void a(dh var1, dv var2, int var3, int var4, int var5);

   public void b(dG var1, int var2, int var3, int var4, int var5, long var6) {
      this.a(var1, var2, var3, var4, var5, var6);
   }

   public void a(dh var1, dv var2, int var3, int var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16) {
      this.a(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, (byte)44);
   }

   public void b(dh var1, du var2, int var3, int var4) {
      this.a(var1, var2, var3, var4);
   }
}
