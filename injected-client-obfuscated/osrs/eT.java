package osrs;

public class eT extends az {
   public fT a = new fT(16);
   public byte[] b;

   public eT(aR var1) {
      var1.d = var1.c.length - 3;
      int var2 = var1.b();
      int var3 = var1.d();
      int var4 = var2 * 10 + 14;
      var1.d = 0;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      int var11 = 0;
      int var12 = 0;

      int var13;
      int var14;
      int var15;
      for(var13 = 0; var13 < var2; ++var13) {
         var14 = -1;

         while(true) {
            var15 = var1.b();
            if (var14 != var15) {
               ++var4;
            }

            var14 = var15 & 15;
            if (var15 == 7) {
               break;
            }

            if (var15 == 23) {
               ++var5;
            } else if (var14 == 0) {
               ++var7;
            } else if (var14 == 1) {
               ++var8;
            } else if (var14 == 2) {
               ++var6;
            } else if (var14 == 3) {
               ++var9;
            } else if (var14 == 4) {
               ++var10;
            } else if (var14 == 5) {
               ++var11;
            } else {
               if (var14 != 6) {
                  throw new RuntimeException();
               }

               ++var12;
            }
         }
      }

      var13 = var5 * 5 + var4;
      var14 = (var7 + var8 + var6 + var9 + var11) * 2 + var13;
      var15 = var10 + var12 + var14;
      int var16 = var1.d;
      int var17 = var2 + var5 + var6 + var7 + var8 + var9 + var10 + var11 + var12;

      int var18;
      for(var18 = 0; var18 < var17; ++var18) {
         var1.v();
      }

      var18 = var1.d - var16 + var15;
      int var19 = var1.d;
      int var20 = 0;
      int var21 = 0;
      int var22 = 0;
      int var23 = 0;
      int var24 = 0;
      int var25 = 0;
      int var26 = 0;
      int var27 = 0;
      int var28 = 0;
      int var29 = 0;
      int var30 = 0;
      int var31 = 0;
      int var32 = 0;

      int var33;
      for(var33 = 0; var33 < var6; ++var33) {
         var32 = var32 + var1.b() & 127;
         if (var32 != 0 && var32 != 32) {
            if (var32 == 1) {
               ++var20;
            } else if (var32 == 33) {
               ++var21;
            } else if (var32 == 7) {
               ++var22;
            } else if (var32 == 39) {
               ++var23;
            } else if (var32 == 10) {
               ++var24;
            } else if (var32 == 42) {
               ++var25;
            } else if (var32 == 99) {
               ++var26;
            } else if (var32 == 98) {
               ++var27;
            } else if (var32 == 101) {
               ++var28;
            } else if (var32 == 100) {
               ++var29;
            } else if (var32 != 64 && var32 != 65 && var32 != 120 && var32 != 121 && var32 != 123) {
               ++var31;
            } else {
               ++var30;
            }
         } else {
            ++var12;
         }
      }

      var33 = 0;
      int var34 = var1.d;
      var1.d += var30;
      int var35 = var1.d;
      var1.d += var11;
      int var36 = var1.d;
      var1.d += var10;
      int var37 = var1.d;
      var1.d += var9;
      int var38 = var1.d;
      var1.d += var20;
      int var39 = var1.d;
      var1.d += var22;
      int var40 = var1.d;
      var1.d += var24;
      int var41 = var1.d;
      var1.d += var7 + var8 + var11;
      int var42 = var1.d;
      var1.d += var7;
      int var43 = var1.d;
      var1.d += var31;
      int var44 = var1.d;
      var1.d += var8;
      int var45 = var1.d;
      var1.d += var21;
      int var46 = var1.d;
      var1.d += var23;
      int var47 = var1.d;
      var1.d += var25;
      int var48 = var1.d;
      var1.d += var12;
      int var49 = var1.d;
      var1.d += var9;
      int var50 = var1.d;
      var1.d += var26;
      int var51 = var1.d;
      var1.d += var27;
      int var52 = var1.d;
      var1.d += var28;
      int var53 = var1.d;
      var1.d += var29;
      int var54 = var1.d;
      var1.d += var5 * 3;
      this.b = new byte[var18];
      aR var55 = new aR(this.b);
      var55.d(1297377380);
      var55.d(6);
      var55.b(var2 > 1 ? 1 : 0);
      var55.b(var2);
      var55.b(var3);
      var1.d = var16;
      int var56 = 0;
      int var57 = 0;
      int var58 = 0;
      int var59 = 0;
      int var60 = 0;
      int var61 = 0;
      int var62 = 0;
      int[] var63 = new int[128];
      int var64 = 0;
      int[] var65 = new int[16];
      int[] var66 = new int[16];
      var66[9] = 128;
      var65[9] = 128;

      label237:
      for(int var67 = 0; var67 < var2; ++var67) {
         var55.d(1297379947);
         var55.d += 4;
         int var68 = var55.d;
         int var69 = var68;
         int var70 = -1;

         while(true) {
            while(true) {
               int var71 = var1.v();
               var55.i(var71);
               var69 += var71;
               int var72 = var1.c[var33++] & 255;
               boolean var73 = var70 != var72;
               var70 = var72 & 15;
               if (var72 == 7) {
                  if (var73) {
                     var55.a(255);
                  }

                  var55.a(47);
                  var55.a(0);
                  var55.e(var55.d - var68);
                  continue label237;
               }

               if (var72 == 23) {
                  if (var73) {
                     var55.a(255);
                  }

                  var55.a(81);
                  var55.a(3);
                  var55.a(var1.c[var54++]);
                  var55.a(var1.c[var54++]);
                  var55.a(var1.c[var54++]);
               } else {
                  var56 ^= var72 >> 4;
                  int var74;
                  int var75;
                  int var76;
                  if (var70 == 0) {
                     if (var73) {
                        var55.a(var56 + 144);
                     }

                     var57 += var1.c[var41++];
                     var58 += var1.c[var42++];
                     var74 = var57 & 127;
                     var75 = var58 & 127;
                     var55.a(var74);
                     var55.a(var75);
                     if (var75 > 0) {
                        var76 = var66[var56];
                        eQ var77 = (eQ)this.a.a((long)var76);
                        if (var77 == null) {
                           var77 = new eQ(var69);
                           this.a.a(var77, (long)var76);
                        }

                        var77.b.set(var74);
                     }
                  } else if (var70 == 1) {
                     if (var73) {
                        var55.a(var56 + 128);
                     }

                     var57 += var1.c[var41++];
                     var59 += var1.c[var44++];
                     var55.a(var57 & 127);
                     var55.a(var59 & 127);
                  } else {
                     byte var78;
                     if (var70 == 2) {
                        if (var73) {
                           var55.a(var56 + 176);
                        }

                        var64 = var64 + var1.c[var19++] & 127;
                        var55.a(var64);
                        if (var64 != 0 && var64 != 32) {
                           if (var64 == 1) {
                              var78 = var1.c[var38++];
                           } else if (var64 == 33) {
                              var78 = var1.c[var45++];
                           } else if (var64 == 7) {
                              var78 = var1.c[var39++];
                           } else if (var64 == 39) {
                              var78 = var1.c[var46++];
                           } else if (var64 == 10) {
                              var78 = var1.c[var40++];
                           } else if (var64 == 42) {
                              var78 = var1.c[var47++];
                           } else if (var64 == 99) {
                              var78 = var1.c[var50++];
                           } else if (var64 == 98) {
                              var78 = var1.c[var51++];
                           } else if (var64 == 101) {
                              var78 = var1.c[var52++];
                           } else if (var64 == 100) {
                              var78 = var1.c[var53++];
                           } else if (var64 != 64 && var64 != 65 && var64 != 120 && var64 != 121 && var64 != 123) {
                              var78 = var1.c[var43++];
                           } else {
                              var78 = var1.c[var34++];
                           }
                        } else {
                           var78 = var1.c[var48++];
                        }

                        var75 = var63[var64] + var78;
                        var63[var64] = var75;
                        var76 = var75 & 127;
                        var55.a(var76);
                        if (var64 == 0) {
                           var65[var56] = (var76 << 14) + (var65[var56] & -2080769);
                        }

                        if (var64 == 32) {
                           var65[var56] = (var76 << 7) + (var65[var56] & -16257);
                        }
                     } else if (var70 == 3) {
                        if (var73) {
                           var55.a(var56 + 224);
                        }

                        var74 = var60 + var1.c[var49++];
                        var60 = var74 + (var1.c[var37++] << 7);
                        var55.a(var60 & 127);
                        var55.a(var60 >> 7 & 127);
                     } else if (var70 == 4) {
                        if (var73) {
                           var55.a(var56 + 208);
                        }

                        var61 += var1.c[var36++];
                        var55.a(var61 & 127);
                     } else if (var70 == 5) {
                        if (var73) {
                           var55.a(var56 + 160);
                        }

                        var57 += var1.c[var41++];
                        var62 += var1.c[var35++];
                        var55.a(var57 & 127);
                        var55.a(var62 & 127);
                     } else {
                        if (var70 != 6) {
                           throw new RuntimeException();
                        }

                        if (var73) {
                           var55.a(var56 + 192);
                        }

                        var78 = var1.c[var48++];
                        var66[var56] = var65[var56] + var78;
                        var55.a(var78);
                     }
                  }
               }
            }
         }
      }

   }

   public static eT a(au var0, int var1, int var2) {
      byte[] var3 = var0.b(var1, var2);
      return var3 == null ? null : new eT(new aR(var3));
   }
}
