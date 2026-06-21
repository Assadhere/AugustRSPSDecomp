package osrs;

import net.runelite.api.events.WidgetClosed;
import net.runelite.api.events.WidgetDrag;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.WidgetUtil;

public class jH {
   public final jI a = new jI();
   public final jz b;
   public final ju c;
   public final jK d;

   public jH(jz var1, ju var2, jK var3) {
      this.b = var1;
      this.c = var2;
      this.d = var3;
   }

   public void a(int var1, int var2, int var3, kd var4, bg var5) {
      var4.o = var1;
      jF.a(var4.o, var2, var3, false, var4, var5);
      this.b(var1, var4);
      this.a(var4.o, var4);
   }

   public bt b(int var1, int var2, int var3, kd var4, bg var5) {
      bt var6 = new bt();
      var6.c = var2;
      var6.b = var3;
      var4.x.a(var6, (long)var1);
      this.b(var2, var4);
      ag var7 = var4.a(var1);
      var4.h();
      jF.a(var4.m[var1 >> 16], var7, false, var4, var5);
      this.a(var2, var4);
      this.a(jG.b, var4);
      return var6;
   }

   public void a(bt var1, boolean var2, kd var3) {
      this.b(var1, var2, var3);
      int var4 = var1.c;
      var1.X();
      if (var2) {
         var3.c(var4);
      }

      var3.e(var4);
      this.a(jG.b, var3);
   }

   public void a(int var1, int var2, kd var3, bg var4) {
      bt var5 = (bt)var3.x.a((long)var1);
      bt var6 = (bt)var3.x.a((long)var2);
      if (var6 != null) {
         this.a(var6, var5 == null || var5.c != var6.c, var3);
      }

      if (var5 != null) {
         var5.X();
         var3.x.a(var5, (long)var2);
      }

      ag var7 = var3.a(var2);
      if (var7 != null) {
         jF.a(var3.m[var7.T >>> 16], var7, true, var3, var4);
      }

      this.a(jG.b, var3);
   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, kd var8, int var9, bg var10) {
      if (var8.b(var1)) {
         this.a(var8.m[var1], 0, var8.m[var1].length - 1, -1, -1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }

   }

   public void a(ag[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, kd var12, int var13, bg var14) {
      this.b(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14);
      boolean var15 = this.c.i();
      q var16 = this.b.d();

      for(int var17 = var2; var17 <= var3; ++var17) {
         ag var18 = var1[var17];
         if (var18 != null && var18.bL == var4 && var18.bS == var5 && var12.a(var18)) {
            if (var18.a) {
               if (var12.b(var18)) {
                  continue;
               }
            } else if (var18.B == 0 && !var12.c().a(var18) && var12.b(var18)) {
               continue;
            }

            if (var18.B == 11) {
               if (var18.c(var12, this.b.f())) {
                  if (var18.e()) {
                     jF.a(var18.bN, var18, true, var12, var14);
                     var18.j().a().a(3, var18.j().i());
                  }

                  if (var18.aU != null) {
                     ac var19 = ac.a(var18).a(var18.aU).a();
                     var14.a(var19);
                  }
               }
            } else if (var18.B == 12) {
               var18.c((fa)var12);
            }

            int var37 = var18.j + var10;
            int var20 = var18.b + var11;
            int var21;
            int var22;
            int var23;
            int var24;
            int var25;
            int var26;
            int var27;
            if (var18.B == 9) {
               var25 = var37;
               var26 = var20;
               var27 = var18.ah + var37;
               int var28 = var18.m + var20;
               if (var27 < var37) {
                  var25 = var27;
                  var27 = var37;
               }

               if (var28 < var20) {
                  var26 = var28;
                  var28 = var20;
               }

               ++var27;
               ++var28;
               var21 = var25 > var6 ? var25 : var6;
               var22 = var26 > var7 ? var26 : var7;
               var23 = var27 < var8 ? var27 : var8;
               var24 = var28 < var9 ? var28 : var9;
            } else {
               var25 = var18.ah + var37;
               var26 = var18.m + var20;
               var21 = var37 > var6 ? var37 : var6;
               var22 = var20 > var7 ? var20 : var7;
               var23 = var25 < var8 ? var25 : var8;
               var24 = var26 < var9 ? var26 : var9;
            }

            var12.d().a(var18, var37, var20);
            boolean var38 = false;
            if (var18.aB) {
               switch (var12.r) {
                  case 0:
                     var38 = true;
                  case 1:
                  default:
                     break;
                  case 2:
                     if (var18.T >>> 16 == var12.s) {
                        var38 = true;
                     }
                     break;
                  case 3:
                     if (var12.s == var18.T) {
                        var38 = true;
                     }
               }
            }

            if (var38 || !var18.a || var21 < var23 && var22 < var24) {
               if (var18.a) {
                  if (var18.bV) {
                     if (this.a.b() >= var21 && this.a.c() >= var22 && this.a.b() < var23 && this.a.c() < var24) {
                        var14.e();
                        var12.d().o();
                        if (!var15) {
                           this.d.b();
                        }
                     }
                  } else if (var18.bW && this.a.b() >= var21 && this.a.c() >= var22 && this.a.b() < var23 && this.a.c() < var24) {
                     var14.g();
                  }
               }

               var26 = this.a.b();
               var27 = this.a.c();
               if (this.a.d() != 0) {
                  var26 = this.a.e();
                  var27 = this.a.f();
               }

               boolean var39 = var26 >= var21 && var27 >= var22 && var26 < var23 && var27 < var24;
               if (var18.l == 1337) {
                  if (!this.c.h() && !var15 && var39) {
                     this.d.a(var26, var27, var21, var22);
                  }
               } else if (var18.l == 1338) {
                  dX.a(var18, var37, var20, (byte)0);
               } else {
                  if (var18.l == 1400) {
                     this.b.c().a(var26, var27, var39, var37, var20, var18.ah, var18.m);
                  }

                  if (!var15 && var39) {
                     if (var18.l == 1400) {
                        this.b.c().c(var37, var20, var18.ah, var18.m, var26, var27);
                     } else {
                        this.d.c(var18);
                     }
                  }

                  boolean var30;
                  boolean var31;
                  int var34;
                  if (var38) {
                     for(int var29 = 0; var29 < var18.aC.length; ++var29) {
                        var30 = false;
                        var31 = false;
                        int var32;
                        if (!var30 && var18.aC[var29] != null) {
                           for(var32 = 0; var32 < var18.aC[var29].length; ++var32) {
                              boolean var33 = false;
                              if (var18.bH != null) {
                                 var33 = var16.c(var18.aC[var29][var32]);
                              }

                              if (this.b.a().c(var18.aC[var29][var32]) || var33) {
                                 var30 = true;
                                 if (var18.bH != null && var18.bH[var29] > var13) {
                                    break;
                                 }

                                 var34 = var18.aD[var29][var32];
                                 if (var34 == 0 || ((var34 & 8) == 0 || !var16.c(86) && !var16.c(82) && !var16.c(81)) && ((var34 & 2) == 0 || var16.c(86)) && ((var34 & 1) == 0 || var16.c(82)) && ((var34 & 4) == 0 || var16.c(81))) {
                                    var31 = true;
                                    break;
                                 }
                              }
                           }
                        }

                        if (var31) {
                           if (var29 < 10) {
                              this.d.a(var29 + 1, var18.T, var18.as, var18.g, "");
                           } else if (var29 == 10) {
                              this.d.c();
                              String var42 = var12.c(var18);
                              if (var42 == null) {
                                 var42 = bv.k;
                              }

                              String var10000 = var18.bF;
                              String var43 = var10000 + bq.b(16777215);
                              var34 = var12.i(var18);
                              this.d.a(var18.T, var18.as, bn.a(var34), var18.g, var42, var43);
                           }

                           var32 = var18.bI[var29];
                           if (var18.bH == null) {
                              var18.bH = new int[var18.aC.length];
                           }

                           if (var18.bD == null) {
                              var18.bD = new int[var18.aC.length];
                           }

                           if (var32 != 0) {
                              if (var18.bH[var29] == 0) {
                                 var18.bH[var29] = var13 + var32 + var18.bD[var29];
                              } else {
                                 var18.bH[var29] = var13 + var32;
                              }
                           } else {
                              var18.bH[var29] = Integer.MAX_VALUE;
                           }
                        }

                        if (!var30 && var18.bH != null) {
                           var18.bH[var29] = 0;
                        }
                     }
                  }

                  if (var18.a) {
                     boolean var40 = this.a.b() >= var21 && this.a.c() >= var22 && this.a.b() < var23 && this.a.c() < var24;
                     var30 = (this.a.a() == 1 || !this.c.b() && this.a.a() == 4) && var40;
                     var31 = (this.a.d() == 1 || !this.c.b() && this.a.d() == 4) && this.a.e() >= var21 && this.a.f() >= var22 && this.a.e() < var23 && this.a.f() < var24;
                     if (var31 && !this.c.i() && var12.a(var18, this.a.e() - var37, this.a.f() - var20)) {
                        this.d.a();
                     }

                     if (var18.c()) {
                        if (var31) {
                           var14.a(new as(0, aI.h - var37, aI.i - var20, var18));
                        }

                        if (var30) {
                           var14.a(new as(1, aI.h - var37, aI.i - var20, var18));
                        }
                     }

                     if (var18.l == 1400) {
                        this.b.c().a(var26, var27, var40 & var30, var40 & var31);
                     }

                     if (var12.f() && var12.e() != var18 && var40 && bl.b(var12.i(var18))) {
                        var12.d().a(var18);
                     }

                     var12.d().b(var18, var37, var20);
                     if (var18.aP) {
                        ac var44;
                        if (var40 && this.a.g() != 0 && var18.aX != null) {
                           var44 = ac.a(var18).a(true).a(var18.aX).c(this.a.g()).a();
                           var14.a(var44);
                        }

                        if (var12.f() || this.c.i()) {
                           var31 = false;
                           var30 = false;
                           var40 = false;
                        }

                        if (!var18.bP && var31) {
                           var18.bP = true;
                           if (var18.bx != null) {
                              var44 = ac.a(var18).a(true).a(var18.bx).b(aI.l - var37).c(aI.m - var20).a();
                              var14.a(var44);
                           }
                        }

                        if (var18.bP && var30 && var18.bl != null) {
                           var44 = ac.a(var18).a(true).a(var18.bl).b(aI.h - var37).c(aI.i - var20).a();
                           var14.a(var44);
                        }

                        if (var18.bP && !var30) {
                           var18.bP = false;
                           if (var18.aW != null) {
                              var44 = ac.a(var18).a(true).a(var18.aW).b(aI.h - var37).c(aI.i - var20).a();
                              var14.c(var44);
                           }
                        }

                        if (var30 && var18.bj != null) {
                           var44 = ac.a(var18).a(true).a(var18.bj).b(aI.h - var37).c(aI.i - var20).a();
                           var14.a(var44);
                        }

                        if (!var18.bO && var40) {
                           var18.bO = true;
                           if (var18.aR != null) {
                              var44 = ac.a(var18).a(true).a(var18.aR).b(aI.h - var37).c(aI.i - var20).a();
                              var14.a(var44);
                           }
                        }

                        if (var18.bO && var40 && var18.bo != null) {
                           var44 = ac.a(var18).a(true).a(var18.bo).b(aI.h - var37).c(aI.i - var20).a();
                           var14.a(var44);
                        }

                        if (var18.bO && !var40) {
                           var18.bO = false;
                           if (var18.bq != null) {
                              var44 = ac.a(var18).a(true).a(var18.bq).b(aI.h - var37).c(aI.i - var20).a();
                              var14.c(var44);
                           }
                        }

                        if (var18.aS != null) {
                           var44 = ac.a(var18).a(var18.aS).a();
                           var14.b(var44);
                        }

                        jJ var47 = this.b.e();
                        int var35;
                        ac var36;
                        ac var45;
                        int var46;
                        if (var18.bu != null && var47.k() > var18.r) {
                           if (var18.bh != null && var47.k() - var18.r <= 32) {
                              label613:
                              for(var46 = var18.r; var46 < var47.k(); ++var46) {
                                 var34 = var47.e(var46);

                                 for(var35 = 0; var35 < var18.bh.length; ++var35) {
                                    if (var18.bh[var35] == var34) {
                                       var36 = ac.a(var18).a(var18.bu).a();
                                       var14.a(var36);
                                       break label613;
                                    }
                                 }
                              }
                           } else {
                              var45 = ac.a(var18).a(var18.bu).a();
                              var14.a(var45);
                           }

                           var18.r = var47.k();
                        }

                        if (var18.by != null && var47.l() > var18.q) {
                           if (var18.aE != null && var47.l() - var18.q <= 32) {
                              label589:
                              for(var46 = var18.q; var46 < var47.l(); ++var46) {
                                 var34 = var47.f(var46);

                                 for(var35 = 0; var35 < var18.aE.length; ++var35) {
                                    if (var18.aE[var35] == var34) {
                                       var36 = ac.a(var18).a(var18.by).a();
                                       var14.a(var36);
                                       break label589;
                                    }
                                 }
                              }
                           } else {
                              var45 = ac.a(var18).a(var18.by).a();
                              var14.a(var45);
                           }

                           var18.q = var47.l();
                        }

                        if (var18.bp != null && var47.m() > var18.ck) {
                           if (var18.aF != null && var47.m() - var18.ck <= 32) {
                              label565:
                              for(var46 = var18.ck; var46 < var47.m(); ++var46) {
                                 var34 = var47.g(var46);

                                 for(var35 = 0; var35 < var18.aF.length; ++var35) {
                                    if (var18.aF[var35] == var34) {
                                       var36 = ac.a(var18).a(var18.bp).a();
                                       var14.a(var36);
                                       break label565;
                                    }
                                 }
                              }
                           } else {
                              var45 = ac.a(var18).a(var18.bp).a();
                              var14.a(var45);
                           }

                           var18.ck = var47.m();
                        }

                        if (var47.u() > var18.ab && var18.bc != null) {
                           var45 = ac.a(var18).a(var18.bc).a();
                           var14.a(var45);
                        }

                        if (var47.n() > var18.ab && var18.bv != null) {
                           var45 = ac.a(var18).a(var18.bv).a();
                           var14.a(var45);
                        }

                        if (var47.o() > var18.ab && var18.be != null) {
                           var45 = ac.a(var18).a(var18.be).a();
                           var14.a(var45);
                        }

                        if (var47.p() > var18.ab && var18.bk != null) {
                           var45 = ac.a(var18).a(var18.bk).a();
                           var14.a(var45);
                        }

                        if (var47.q() > var18.ab && var18.aZ != null) {
                           var45 = ac.a(var18).a(var18.aZ).a();
                           var14.a(var45);
                        }

                        if (var47.r() > var18.ab && var18.bn != null) {
                           var45 = ac.a(var18).a(var18.bn).a();
                           var14.a(var45);
                        }

                        if (var47.s() > var18.ab && var18.bs != null) {
                           var45 = ac.a(var18).a(var18.bs).a();
                           var14.a(var45);
                        }

                        if (var47.t() > var18.ab && var18.bz != null) {
                           var45 = ac.a(var18).a(var18.bz).a();
                           var14.a(var45);
                        }

                        var18.ab = var47.j();
                        ac var48;
                        if (var18.bm != null) {
                           jn var49 = this.b.a();

                           for(var34 = 0; var34 < var49.b(); ++var34) {
                              var48 = ac.a(var18).a(var18.bm).f(var49.a(var34)).g(var49.b(var34)).a();
                              var14.a(var48);
                           }
                        }

                        int[] var50;
                        if (var18.bw != null) {
                           var50 = var16.f(-160450998);

                           for(var34 = 0; var34 < var50.length; ++var34) {
                              var48 = ac.a(var18).a(var18.bw).f(var50[var34]).a();
                              var14.a(var48);
                           }
                        }

                        if (var18.bA != null) {
                           var50 = var16.a((byte)34);

                           for(var34 = 0; var34 < var50.length; ++var34) {
                              var48 = ac.a(var18).a(var18.bA).f(var50[var34]).a();
                              var14.a(var48);
                           }
                        }
                     }
                  }

                  if (!var18.a) {
                     if (var12.f() || this.c.i()) {
                        continue;
                     }

                     if ((var18.cj >= 0 || var18.bM != 0) && this.a.b() >= var21 && this.a.c() >= var22 && this.a.b() < var23 && this.a.c() < var24) {
                        if (var18.cj >= 0) {
                           var12.c().b(var1[var18.cj]);
                        } else {
                           var12.c().b(var18);
                        }
                     }

                     if (var18.B == 8 && this.a.b() >= var21 && this.a.c() >= var22 && this.a.b() < var23 && this.a.c() < var24) {
                        var12.c().d(var18);
                     }

                     if (var18.w > var18.m) {
                        this.a(var18, var18.ah + var37, var20, var18.m, var18.w, this.a.b(), this.a.c(), var12);
                     }
                  }

                  if (var18.B == 0) {
                     this.a(var1, var18.ad, var18.c, var18.T, var18.as, var21, var22, var23, var24, var37 - var18.P, var20 - var18.ap, var12, var13, var14);
                     if (var18.bN != null) {
                        this.a(var18.bN, 0, var18.bN.length - 1, var18.T, -1, var21, var22, var23, var24, var37 - var18.P, var20 - var18.ap, var12, var13, var14);
                     }

                     if (var18.as == -1) {
                        bt var41 = (bt)var12.x.a((long)var18.T);
                        if (var41 != null) {
                           if (var41.b == 0 && aI.h >= var21 && aI.i >= var22 && aI.h < var23 && aI.i < var24 && !this.c.i()) {
                              var14.e();
                              var12.d().o();
                              if (!var15) {
                                 this.d.b();
                              }
                           }

                           this.a(var41.c, var21, var22, var23, var24, var37, var20, var12, var13, var14);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public void a(ag var1, int var2, int var3, int var4, int var5, int var6, int var7, kd var8) {
      byte var9;
      if (var8.w) {
         var9 = 32;
      } else {
         var9 = 0;
      }

      var8.w = false;
      int var10;
      if (this.a.a() == 1 || !this.c.b() && this.a.a() == 4) {
         if (var6 >= var2 && var6 < var2 + 16 && var7 >= var3 && var7 < var3 + 16) {
            var1.ap -= 4;
         } else if (var6 >= var2 && var6 < var2 + 16 && var7 >= var3 + var4 - 16 && var7 < var3 + var4) {
            var1.ap += 4;
         } else if (var6 >= var2 - var9 && var6 < var2 + 16 + var9 && var7 >= var3 + 16 && var7 < var3 + var4 - 16) {
            var10 = (var4 - 32) * var4 / var5;
            if (var10 < 8) {
               var10 = 8;
            }

            int var11 = var7 - var3 - 16 - var10 / 2;
            int var12 = var4 - 32 - var10;
            var1.ap = (var5 - var4) * var11 / var12;
            var8.w = true;
         }
      }

      if (this.a.g() != 0) {
         var10 = var1.ah;
         if (var6 >= var2 - var10 && var7 >= var3 && var6 < var2 + 16 && var7 <= var3 + var4) {
            var1.ap += this.a.g() * 45;
         }
      }

   }

   public void a(jG var1, kd var2) {
      if (var2.o != -1) {
         this.a(var2.o, var1, var2);
      }

   }

   public void a(int var1, jG var2, kd var3) {
      if (var3.b(var1)) {
         this.a(var3.m[var1], var2, var3);
      }

   }

   public void a(int var1, kd var2) {
      if (var1 == -1) {
         this.c(var1, var2);
      } else if (!var2.b(var1)) {
         this.c(var1, var2);
      } else {
         ag[] var3 = var2.m[var1];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            ag var5 = var3[var4];
            if (var5.bb != null) {
               ac var6 = ac.a(var5).a(var5.bb).a();
               this.d.b(var6);
            }
         }

         this.c(var1, var2);
      }

   }

   public void a(ag[] var1, jG var2, kd var3) {
      for(int var4 = 0; var4 < var1.length; ++var4) {
         ag var5 = var1[var4];
         if (var5 != null) {
            if (var5.B == 0) {
               if (var5.bN != null) {
                  this.a(var5.bN, var2, var3);
               }

               if (var5.as == -1) {
                  bt var6 = (bt)var3.x.a((long)var5.T);
                  if (var6 != null) {
                     this.a(var6.c, var2, var3);
                  }
               }
            }

            ac var7;
            if (jG.a == var2 && var5.bt != null) {
               var7 = ac.a(var5).a(var5.bt).a();
               this.d.a(var7);
            }

            if (jG.b == var2 && var5.aV != null) {
               if (var5.as >= 0) {
                  ag var8 = var3.a(var5.T);
                  if (var8 == null || var8.bN == null || var5.as >= var8.bN.length || var8.bN[var5.as] != var5) {
                     continue;
                  }
               }

               var7 = ac.a(var5).a(var5.aV).a();
               this.d.a(var7);
            }
         }
      }

   }

   public void b(int var1, kd var2) {
      if (var2.b(var1)) {
         ag[] var3 = var2.m[var1];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            ag var5 = var3[var4];
            if (var5 != null) {
               var5.ai = 0;
               var5.o = 0;
            }
         }
      }

   }

   public void a(bg var1, kd var2) {
      ac var3 = var1.b();

      while(true) {
         ac var4;
         ag var5;
         ag var6;
         do {
            if (var3 == null) {
               ac var7 = var1.c();

               while(true) {
                  ac var8;
                  ag var9;
                  ag var10;
                  do {
                     if (var7 == null) {
                        ac var11 = var1.a();

                        while(true) {
                           ac var12;
                           ag var13;
                           ag var14;
                           do {
                              if (var11 == null) {
                                 boolean var15 = false;

                                 for(as var16 = var1.d(); var16 != null; var16 = var1.d()) {
                                    if (var16.d.B == 12) {
                                       var15 = true;
                                    }

                                    this.a(var16, var2);
                                 }

                                 if (!var15 && aI.k == 1) {
                                    this.d.b((ag)null);
                                 }

                                 return;
                              }

                              var12 = var11;
                              var11 = var1.a();
                              var13 = var12.b();
                              if (var13.as < 0) {
                                 break;
                              }

                              var14 = var2.a(var13.bL);
                           } while(var14 == null || var14.bN == null || var13.as >= var14.bN.length || var14.bN[var13.as] != var13);

                           bh.a(var12);
                        }
                     }

                     var8 = var7;
                     var7 = var1.c();
                     var9 = var8.b();
                     if (var9.as < 0) {
                        break;
                     }

                     var10 = var2.a(var9.bL);
                  } while(var10 == null || var10.bN == null || var9.as >= var10.bN.length || var10.bN[var9.as] != var9);

                  bh.a(var8);
               }
            }

            var4 = var3;
            var3 = var1.b();
            var5 = var4.b();
            if (var5.as < 0) {
               break;
            }

            var6 = var2.a(var5.bL);
         } while(var6 == null || var6.bN == null || var5.as >= var6.bN.length || var6.bN[var5.as] != var5);

         bh.a(var4);
      }
   }

   public void a(as var1, kd var2) {
      if (var1 != null && var1.d != null) {
         if (var1.d.as >= 0) {
            ag var3 = var2.a(var1.d.bL);
            if (var3 == null || var3.bN == null || var3.bN.length == 0 || var1.d.as >= var3.bN.length || var3.bN[var1.d.as] != var1.d) {
               return;
            }
         }

         if (var1.d.B == 11 && var1.a == 0) {
            if (var1.d.a(var1.b, var1.c, 0, 0)) {
               this.d.a(var1.d);
            }
         } else if (var1.d.B == 12) {
            aj var4 = var1.d.l();
            if (var4 != null && var4.o()) {
               switch (var1.a) {
                  case 0:
                     this.d.b(var1.d);
                     var4.a(var1.b, var1.c, this.b.d().c(82), this.b.d().c(81));
                     break;
                  case 1:
                     var4.e(var1.b, var1.c);
               }
            }
         }
      }

   }

   public void a(kd var1) {
      this.b(var1);
      ka var2 = var1.d();
      if (var2.d()) {
         var2.c();
         int var3 = var2.h();
         int var4 = var2.i();
         if (var2.r() && var2.s()) {
            ag var5 = var2.e();
            ag var6 = var2.f();
            boolean var7 = var2.q();
            ag var8 = var2.g();
            int var9 = var2.l();
            int var10 = var2.m();
            int var11 = var2.j();
            int var12 = var2.k();
            int var13 = aI.h;
            int var14 = aI.i;
            int var15 = var13 - var3;
            int var16 = var14 - var4;
            if (var15 < var9) {
               var15 = var9;
            }

            if (var5.ah + var15 > var6.ah + var9) {
               var15 = var6.ah + var9 - var5.ah;
            }

            if (var16 < var10) {
               var16 = var10;
            }

            if (var5.m + var16 > var6.m + var10) {
               var16 = var6.m + var10 - var5.m;
            }

            int var17 = var15 - var11;
            int var18 = var16 - var12;
            int var19 = var5.au;
            if (var2.n() > var5.bE && (var17 > var19 || var17 < -var19 || var18 > var19 || var18 < -var19)) {
               var2.p();
            }

            int var20 = var6.P + (var15 - var9);
            int var21 = var6.ap + (var16 - var10);
            ac var22;
            if (var5.aT != null && var7) {
               var22 = ac.a(var5).a(var5.aT).b(var20).c(var21).a();
               bh.a(var22);
            }

            if (aI.g == 0) {
               if (var7) {
                  if (var5.aY != null) {
                     var22 = ac.a(var5).a(var5.aY).b(var20).c(var21).a(var8).a();
                     this.d.a(var22);
                  }

                  this.d.a(var5, var8);
               } else {
                  this.d.b(var3 + var11, var4 + var12);
               }

               var2.a();
            }
         } else if (var2.n() > 1) {
            if (!var2.q() && this.c.a() > 0) {
               this.d.a(var3 + var2.j(), var4 + var2.k());
            }

            var2.a();
         }
      }

   }

   public static void a(int var0, fU var1) {
      for(ac var2 = (ac)var1.h(); var2 != null; var2 = (ac)var1.i()) {
         ag var3 = var2.n();
         int var4 = WidgetUtil.componentToInterface(var3.getId());
         if (var0 == var4) {
            var2.ad();
         }
      }

   }

   public void b(ag[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, kd var12, int var13, bg var14) {
      for(int var15 = var2; var15 <= var3; ++var15) {
         ag var16 = var1[var15];
         if (var16 != null && var16.bL == var4 && var16.bS == var5 && !var16.isSelfHidden()) {
            if (var4 != -1) {
               var16.a(var4);
            }

            int var17 = var10 + var16.getRelativeX();
            int var18 = var11 + var16.getRelativeY();
            var16.b(var17);
            var16.f(var18);
            if (var16.getType() == 0 && var16.getIndex() == -1) {
               bt var19 = (bt)var12.x.b((long)var16.getId());
               if (var19 != null) {
                  int var20 = var16.getId();
                  int var21 = var19.getId();
                  ag[] var22 = bo.cR.m[var21];
                  ag[] var23 = var22;
                  int var24 = var22.length;

                  for(int var25 = 0; var25 < var24; ++var25) {
                     ag var26 = var23[var25];
                     if (var26.bL == -1) {
                        var26.a(var20);
                     }
                  }
               }
            }
         }
      }

   }

   public void b(kd var1) {
      ka var2 = var1.u;
      if (var2.a != null && var2.j && var2.g) {
         int var3 = aI.h;
         int var4 = aI.i;
         int var5 = var3 - var2.c;
         int var6 = var4 - var2.d;
         if (var5 < var2.h) {
            var5 = var2.h;
         }

         if (var2.a.ah + var5 > var2.b.ah + var2.h) {
            var5 = var2.b.ah + var2.h - var2.a.ah;
         }

         if (var6 < var2.i) {
            var6 = var2.i;
         }

         if (var2.a.m + var6 > var2.b.m + var2.i) {
            var6 = var2.b.m + var2.i - var2.a.m;
         }

         int var7 = var5 - var2.k;
         int var8 = var6 - var2.l;
         int var9 = var2.a.au;
         if (var2.m + 1 > var2.a.bE && (var7 > var9 || var7 < -var9 || var8 > var9 || var8 < -var9)) {
            WidgetDrag var10 = new WidgetDrag();
            Client.s.getCallbacks().post(var10);
            var2.e = true;
         }
      }

   }

   public void a(kd var1, int var2, int var3, int var4, int var5, bg var6, int var7) {
      if (Client.w()) {
         var5 = 0;
      }

      var1.c().a();
      var1.d().b();
      if (var1.o != -1) {
         this.a.a(aI.g, aI.h, aI.i, aI.k, aI.l, aI.m, var5);
         this.a(var1.o, 0, 0, var2, var3, 0, 0, var1, var4, var6);
      }

   }

   public void c(int var1, kd var2) {
      ag[][] var3 = var2.m;
      boolean var4 = var3 != null && var3[var1] != null;
      if (var4) {
         WidgetLoaded var5 = new WidgetLoaded();
         var5.setGroupId(var1);
         Client.s.getCallbacks().post(var5);
      }

   }

   public void b(bt var1, boolean var2, kd var3) {
      Client.s.getCallbacks().post(new WidgetClosed(var1.getId(), var1.getModalMode(), var2));
      if (var2) {
         int var4 = var1.getId();
         a(var4, Client.o.a);
         a(var4, Client.o.c);
         a(var4, Client.o.b);
      }

   }

   public void c(bt var1, boolean var2, kd var3) {
      this.a(var1, var2, var3);
   }

   public void b(ag[] var1, jG var2, kd var3) {
      this.a(var1, var2, var3);
   }

   public void d(int var1, kd var2) {
      this.a(var1, var2);
   }
}
