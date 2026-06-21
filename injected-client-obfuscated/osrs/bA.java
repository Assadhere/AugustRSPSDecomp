package osrs;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class bA {
   public static int a;
   public static int b;
   public static String c;
   public static int d;
   public static int e;
   public static String f;
   public static ba g;
   public static ba h;
   public static String i;
   public static String j;
   public static int k;
   public static int l;
   public static String[] m;
   public static boolean n;
   public static hX o;
   public static hX p;
   public static hX q;
   public static hX r;
   public static hX[] s;
   public static hX t;
   public static hX u;
   public static int v;
   public static int w;
   public static int x;
   public static String y;
   public static String z;
   public static String A;
   public static String B;
   public static String C;
   public static boolean D;
   public static boolean E;
   public static int F;
   public static String G;
   public static String H;
   public static boolean I;
   public static hX J;
   public static int K;
   public static int L;
   public static int M;
   public static long N;
   public static long O;
   public static String[] P;
   public static String[] Q;
   public static String[] R;

   public static int a(au var0, au var1) {
      int var2 = 0;
      String[] var3 = P;

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4];
         if (var0.c(var5, "")) {
            ++var2;
         }
      }

      String[] var8 = Q;

      for(int var9 = 0; var9 < var8.length; ++var9) {
         String var6 = var8[var9];
         if (var1.c(var6, "")) {
            ++var2;
         }
      }

      String[] var10 = R;

      for(int var11 = 0; var11 < var10.length; ++var11) {
         String var7 = var10[var11];
         if (var1.a(var7) != -1 && var1.c(var7, "")) {
            ++var2;
         }
      }

      return var2;
   }

   public static int a(au var0) {
      int var1 = Q.length + P.length;
      String[] var2 = R;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3];
         if (var0.a(var4) != -1) {
            ++var1;
         }
      }

      return var1;
   }

   public static void a(au var0, au var1, V var2, boolean var3, int var4) {
      Client.b(var0, var1, var2, var3, var4);
      if (bo.dw) {
         if (var4 == 4) {
            b((int)4);
         }

         Client.a(var0, var1, var2, var3, var4);
      } else {
         if (var4 == 0) {
            bo.a(var3);
         } else {
            b(var4);
         }

         aU.b();
         byte[] var5 = var0.b("title.jpg", "");
         bo.cI = jV.a(var5);
         bo.dX = bo.cI.e();
         int var6 = Client.aK;
         if ((var6 & hO.o.b()) != 0) {
            bo.dk = aY.b(var1, "logo_deadman_mode", "");
         } else if ((var6 & hO.p.b()) != 0) {
            bo.dk = aY.b(var1, "logo_seasonal_mode", "");
         } else if ((var6 & hO.A.b()) != 0) {
            bo.dk = aY.b(var1, "logo_speedrunning", "");
         } else if ((var6 & hO.D.b()) != 0) {
            bo.dk = aY.b(var1, "logo_ugc_world", "");
         } else {
            bo.dk = aY.b(var1, "logo", "");
         }

         o = aY.b(var1, "titlebox", "");
         p = aY.b(var1, "titlebutton", "");
         q = aY.b(var1, "titlebutton_large", "");
         r = aY.b(var1, "play_now_text", "");
         aY.b(var1, "titlebutton_wide42,1", "");
         bo.aO = aY.a(var1, "runes", "");
         s = aY.a(var1, "title_mute", "");
         t = aY.b(var1, "options_radio_buttons,0", "");
         bo.M = aY.b(var1, "options_radio_buttons,4", "");
         u = aY.b(var1, "options_radio_buttons,2", "");
         bo.aX = aY.b(var1, "options_radio_buttons,6", "");
         bo.J = t.i;
         bo.bc = t.k;
         bo.er = new bz(bo.aO, var2.n);
         if (var3) {
            i = "";
            Client.ah(-1);
            j = "";
            m = new String[8];
            l = 0;
         }

         bo.G = 0;
         bo.cD = "";
         n = true;
         I = false;
         k();
         eZ.b();
         Client.ap().a(false);
         bo.dw = true;
         Client.ad(-1);
         a = (T.eP - 765) / 2;
         v = a + 202;
         w = v + 180;
         bo.cI.a(a, 0);
         bo.dX.a(a + 382, 0);
         bo.dk.a(a + 382 - bo.dk.i / 2, 18);
         Client.a(var0, var1, var2, var3, var4);
      }

   }

   public static String a() {
      return Client.bL.d() ? br.g(i) : i;
   }

   public static void b() {
      if (Client.A && i != null && !i.isEmpty()) {
         F = 1;
      } else {
         F = 0;
      }

   }

   public static void a(T var0, gg var1, gg var2) {
      if (I) {
         a(var0);
      } else {
         if ((aI.k == 1 || !bo.cP && aI.k == 4) && aI.l >= a + 765 - 50 && aI.m >= 453 && aI.l < a + 765 && aI.m < 503) {
            Client.bL.d(!Client.bL.e());
            k();
            eZ.b();
         }

         if (Client.w != 5) {
            if (N == -1L) {
               N = bd.a() + 1000L;
            }

            long var3 = bd.a();
            boolean var5;
            if (Client.dm != null && Client.dn < Client.dm.size()) {
               label1127: {
                  while(Client.dn < Client.dm.size()) {
                     iq var6 = (iq)Client.dm.get(Client.dn);
                     if (!var6.a()) {
                        var5 = false;
                        break label1127;
                     }

                     ++Client.dn;
                  }

                  var5 = true;
               }
            } else {
               var5 = true;
            }

            if (var5 && O == -1L) {
               O = var3;
               if (O > N) {
                  N = O;
               }
            }

            if (Client.w == 10 || Client.w == 11) {
               int var7;
               int var8;
               int var31;
               if (bo.el == ax.a) {
                  if (aI.k == 1 || !bo.cP && aI.k == 4) {
                     var31 = a + 5;
                     var7 = 463;
                     var8 = 100;
                     byte var9 = 35;
                     if (aI.l >= var31 && aI.l <= var31 + var8 && aI.m >= var7 && aI.m <= var7 + var9) {
                        h();
                        return;
                     }
                  }

                  if (bo.dg != null) {
                     h();
                  }
               }

               var31 = aI.k;
               var7 = aI.l;
               var8 = aI.m;
               if (var31 == 0) {
                  var7 = aI.h;
                  var8 = aI.i;
               }

               if (!bo.cP && var31 == 4) {
                  var31 = 1;
               }

               q var32 = Client.c();
               boolean var10;
               int var11;
               int var12;
               int var13;
               if (x == 0) {
                  var10 = false;

                  while(var32.b()) {
                     if (var32.p == 84) {
                        var10 = true;
                     }
                  }

                  var11 = w - 80;
                  var12 = 291;
                  if (var31 == 1 && var7 >= var11 - 75 && var7 <= var11 + 75 && var8 >= var12 - 20 && var8 <= var12 + 20) {
                     dG.a("https://august-rsps.com/password-reset", 465024219);
                  }

                  var13 = w + 80;
                  if (var31 == 1 && var7 >= var13 - 75 && var7 <= var13 + 75 && var8 >= var12 - 20 && var8 <= var12 + 20 || var10) {
                     f();
                  }
               } else if (x == 1) {
                  var10 = Client.s.at() || Client.s.av() || Client.s.au();

                  while(var32.b()) {
                     if (var32.p == 84) {
                        if (var10) {
                           a(bv.dy, bv.dz, bv.dA);
                           Client.z = hY.b;
                           bo.b(false);
                           Client.a((int)20);
                        } else {
                           a(false);
                        }

                        return;
                     }

                     if (var32.p == 13) {
                        bo.a(true);
                        return;
                     }
                  }

                  var11 = w - 80;
                  var12 = 321;
                  if (var31 == 1 && var7 >= var11 - 75 && var7 <= var11 + 75 && var8 >= var12 - 20 && var8 <= var12 + 20) {
                     if (var10) {
                        d();
                     } else {
                        a(false);
                     }

                     return;
                  }

                  var13 = w + 80;
                  if (var31 == 1 && var7 >= var13 - 75 && var7 <= var13 + 75 && var8 >= var12 - 20 && var8 <= var12 + 20) {
                     bo.a(true);
                     return;
                  }
               } else {
                  int var14;
                  int var15;
                  int var33;
                  short var34;
                  short var40;
                  short var49;
                  int var51;
                  String var60;
                  if (x == 2) {
                     var49 = 201;
                     var33 = var49 + 52;
                     if (var31 == 1 && var8 >= var33 - 12 && var8 < var33 + 2) {
                        F = 0;
                     }

                     var33 += 15;
                     if (var31 == 1 && var8 >= var33 - 12 && var8 < var33 + 2) {
                        F = 1;
                     }

                     var33 += 15;
                     var34 = 361;
                     if (g != null) {
                        var12 = g.d / 2;
                        if (var31 == 1 && var7 >= g.c - var12 && var7 <= g.c + var12 && var8 >= var34 - 15 && var8 < var34) {
                           switch (e) {
                              case 1:
                                 dG.a(bv.fe, 465024219);
                                 return;
                              case 2:
                                 dG.a("https://support.runescape.com/hc/en-gb", 465024219);
                           }
                        }
                     }

                     var12 = w - 80;
                     var40 = 321;
                     if (var31 == 1 && var7 >= var12 - 75 && var7 <= var12 + 75 && var8 >= var40 - 20 && var8 <= var40 + 20) {
                        bo.l();
                        return;
                     }

                     var14 = v + 180 + 80;
                     if (var31 == 1 && var7 >= var14 - 75 && var7 <= var14 + 75 && var8 >= var40 - 20 && var8 <= var40 + 20) {
                        b((int)0);
                        i = "";
                        Client.ah(-1);
                        j = "";
                        bo.G = 0;
                        bo.cD = "";
                        n = true;
                     }

                     var15 = w + -117;
                     short var48 = 277;
                     D = var7 >= var15 && var7 < bo.J + var15 && var8 >= var48 && var8 < bo.bc + var48;
                     if (var31 == 1 && D) {
                        Client.A = !Client.A;
                        if (!Client.A && Client.bL.m() != null) {
                           Client.bL.a((String)null);
                        }
                     }

                     var51 = w + 24;
                     short var54 = 277;
                     E = var7 >= var51 && var7 < bo.J + var51 && var8 >= var54 && var8 < bo.bc + var54;
                     if (var31 == 1 && E) {
                        Client.bL.c(!Client.bL.d());
                        if (!Client.bL.d()) {
                           i = "";
                           Client.ah(-1);
                           Client.bL.a((String)null);
                           b();
                        }
                     }

                     while(true) {
                        Transferable var59;
                        int var65;
                        do {
                           while(true) {
                              label879:
                              do {
                                 while(true) {
                                    while(var32.b()) {
                                       if (var32.p != 13) {
                                          if (F != 0) {
                                             continue label879;
                                          }

                                          a(var32.n);
                                          if (var32.p == 85 && !i.isEmpty()) {
                                             i = i.substring(0, i.length() - 1);
                                             Client.ah(-1);
                                          }

                                          if (var32.p == 84 || var32.p == 80) {
                                             F = 1;
                                          }

                                          if (b(var32.n) && i.length() < 320) {
                                             i = i + var32.n;
                                             Client.ah(-1);
                                          }
                                       } else {
                                          b((int)0);
                                          i = "";
                                          Client.ah(-1);
                                          j = "";
                                          bo.G = 0;
                                          bo.cD = "";
                                          n = true;
                                       }
                                    }

                                    return;
                                 }
                              } while(F != 1);

                              if (var32.p == 85 && !j.isEmpty()) {
                                 j = j.substring(0, j.length() - 1);
                              } else if (var32.p == 84 || var32.p == 80) {
                                 F = 0;
                                 if (var32.p == 84) {
                                    i = i.trim();
                                    Client.ah(-1);
                                    if (i.isEmpty()) {
                                       a(bv.bs, bv.bt, bv.bu);
                                    } else if (j.isEmpty()) {
                                       a(bv.bv, bv.bw, bv.bx);
                                    } else {
                                       a(bv.dy, bv.dz, bv.dA);
                                       bo.b(false);
                                       Client.a((int)20);
                                    }

                                    return;
                                 }
                              }

                              if ((var32.c(82) || var32.c(87)) && var32.p == 67) {
                                 Clipboard var56 = Toolkit.getDefaultToolkit().getSystemClipboard();
                                 var59 = var56.getContents(Client.s);
                                 var65 = 20 - j.length();
                                 break;
                              }

                              if (gc.c(var32.n) && b(var32.n) && j.length() < 20) {
                                 j = j + var32.n;
                              }
                           }
                        } while(var65 <= 0);

                        try {
                           var60 = (String)var59.getTransferData(DataFlavor.stringFlavor);
                           int var64 = Math.min(var65, var60.length());

                           for(int var67 = 0; var67 < var64; ++var67) {
                              if (!gc.c(var60.charAt(var67)) || !b(var60.charAt(var67))) {
                                 b((int)3);
                                 return;
                              }
                           }

                           String var10000 = j;
                           j = var10000 + var60.substring(0, var64);
                        } catch (UnsupportedFlavorException var29) {
                        } catch (IOException var30) {
                        }
                     }
                  }

                  int var16;
                  ba var35;
                  if (x == 3) {
                     var33 = v + 180;
                     var34 = 241;
                     var35 = var1.a(25, bv.dT.length() - 34, bv.dT, var33, var34);
                     if (var31 == 1 && var35.c(var7, var8)) {
                        dG.a(bv.fd, 465024219);
                     }

                     var13 = v + 180;
                     var14 = 276;
                     if (var31 == 1 && var7 >= var13 - 75 && var7 <= var13 + 75 && var8 >= var14 - 20 && var8 <= var14 + 20) {
                        bo.a(false);
                     }

                     var15 = v + 180;
                     var16 = 326;
                     if (var31 == 1 && var7 >= var15 - 75 && var7 <= var15 + 75 && var8 >= var16 - 20 && var8 <= var16 + 20) {
                        dG.a(bv.fe, 465024219);
                        return;
                     }
                  } else if (x == 4) {
                     var33 = v + 180 - 80;
                     var34 = 321;
                     if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                        c();
                        return;
                     }

                     if (var31 == 1 && var7 >= v + 180 - 9 && var7 <= v + 180 + 130 && var8 >= 263 && var8 <= 296) {
                        n = !n;
                     }

                     if (var31 == 1 && var7 >= v + 180 - 34 && var7 <= v + 180 + 34 && var8 >= 351 && var8 <= 363) {
                        dG.a(bv.fe, 465024219);
                     }

                     var12 = v + 180 + 80;
                     if (var31 == 1 && var7 >= var12 - 75 && var7 <= var12 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                        b((int)0);
                        i = "";
                        Client.ah(-1);
                        j = "";
                        bo.G = 0;
                        bo.cD = "";
                     }

                     while(var32.b()) {
                        boolean var37 = false;

                        for(var14 = 0; var14 < H.length(); ++var14) {
                           if (var32.n == H.charAt(var14)) {
                              var37 = true;
                              break;
                           }
                        }

                        if (var32.p == 13) {
                           b((int)0);
                           i = "";
                           Client.ah(-1);
                           j = "";
                           bo.G = 0;
                           bo.cD = "";
                        } else {
                           if (var32.p == 85 && !bo.cD.isEmpty()) {
                              bo.cD = bo.cD.substring(0, bo.cD.length() - 1);
                           }

                           if (var32.p == 84) {
                              c();
                              return;
                           }

                           if (var37 && bo.cD.length() < 6) {
                              bo.cD = bo.cD + var32.n;
                           }
                        }
                     }
                  } else if (x == 5) {
                     var33 = v + 180 - 80;
                     var34 = 321;
                     if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                        bo.m();
                        return;
                     }

                     var12 = v + 180 + 80;
                     if (var31 == 1 && var7 >= var12 - 75 && var7 <= var12 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                        a(true);
                     }

                     var40 = 361;
                     if (h != null) {
                        var14 = h.d / 2;
                        if (var31 == 1 && var7 >= h.c - var14 && var7 <= h.c + var14 && var8 >= var40 - 15 && var8 < var40) {
                           dG.a(Client.b("secure", true) + "m=weblogin/g=oldscape/cant_log_in", 465024219);
                        }
                     }

                     while(var32.b()) {
                        boolean var41 = false;

                        for(var15 = 0; var15 < G.length(); ++var15) {
                           if (var32.n == G.charAt(var15)) {
                              var41 = true;
                              break;
                           }
                        }

                        if (var32.p == 13) {
                           a(true);
                        } else {
                           if (var32.p == 85 && !i.isEmpty()) {
                              i = i.substring(0, i.length() - 1);
                              Client.ah(-1);
                           }

                           if (var32.p == 84) {
                              bo.m();
                              return;
                           }

                           if (var41 && i.length() < 320) {
                              i = i + var32.n;
                              Client.ah(-1);
                           }
                        }
                     }
                  } else {
                     if (x == 6) {
                        while(true) {
                           do {
                              if (!var32.b()) {
                                 var49 = 321;
                                 if (var31 == 1 && var8 >= var49 - 20 && var8 <= var49 + 20) {
                                    a(true);
                                 }

                                 return;
                              }
                           } while(var32.p != 84 && var32.p != 13);

                           a(true);
                        }
                     }

                     short var36;
                     if (x == 7) {
                        if (bo.bo && !Client.aR) {
                           var33 = w - 150;
                           var11 = var33 + 240 + 25 + 40;
                           var36 = 231;
                           var13 = var36 + 40;
                           if (var31 == 1 && var7 >= var33 && var7 <= var11 && var8 >= var36 && var8 <= var13) {
                              l = a(var33, var7);
                           }

                           var14 = v + 180 - 80;
                           short var43 = 321;
                           boolean var25;
                           if (var31 == 1 && var7 >= var14 - 75 && var7 <= var14 + 75 && var8 >= var43 - 20 && var8 <= var43 + 20) {
                              SimpleDateFormat var44 = new SimpleDateFormat("ddMMyyyyHH", Locale.ENGLISH);
                              var44.setLenient(false);
                              StringBuilder var17 = new StringBuilder();
                              String[] var18 = m;
                              int var19 = 0;

                              Date var20;
                              while(true) {
                                 if (var19 >= var18.length) {
                                    var17.append("12");

                                    try {
                                       var20 = var44.parse(var17.toString());
                                    } catch (ParseException var28) {
                                       b((int)7);
                                       a("Date not valid.", "Please ensure date follows the format", "DD/MM/YYYY and is after 01/01/1900");
                                       var20 = null;
                                    }
                                    break;
                                 }

                                 String var21 = var18[var19];
                                 if (var21 == null) {
                                    b((int)7);
                                    a("Date not valid.", "Please ensure all characters are populated.", "");
                                    var20 = null;
                                    break;
                                 }

                                 var17.append(var21);
                                 ++var19;
                              }

                              boolean var57;
                              if (var20 == null) {
                                 var57 = false;
                              } else {
                                 Calendar var22 = Calendar.getInstance();
                                 var22.set(1, var22.get(1) - 13);
                                 var22.set(5, var22.get(5) + 1);
                                 var22.set(11, 0);
                                 var22.set(12, 0);
                                 var22.set(13, 0);
                                 var22.set(14, 0);
                                 Date var23 = var22.getTime();
                                 boolean var24 = var20.before(var23);
                                 var25 = a(var20);
                                 if (!var25) {
                                    b((int)7);
                                    a("Date not valid.", "Please ensure date follows the format", "DD/MM/YYYY and is after 01/01/1900");
                                    var57 = false;
                                 } else {
                                    if (!var24) {
                                       k = 8388607;
                                    } else {
                                       k = (int)(var20.getTime() / 86400000L - 11745L);
                                    }

                                    var57 = true;
                                 }
                              }

                              if (var57) {
                                 Client.a((int)50);
                                 return;
                              }
                           }

                           var16 = v + 180 + 80;
                           if (var31 == 1 && var7 >= var16 - 75 && var7 <= var16 + 75 && var8 >= var43 - 20 && var8 <= var43 + 20) {
                              m = new String[8];
                              a(true);
                           }

                           while(var32.b()) {
                              if (var32.p == 101) {
                                 m[l] = null;
                              }

                              if (var32.p == 85) {
                                 if (m[l] == null && l > 0) {
                                    --l;
                                 }

                                 m[l] = null;
                              }

                              if (var32.n >= '0' && var32.n <= '9') {
                                 m[l] = "" + var32.n;
                                 if (l < 7) {
                                    ++l;
                                 }
                              }

                              if (var32.p == 84) {
                                 SimpleDateFormat var50 = new SimpleDateFormat("ddMMyyyyHH", Locale.ENGLISH);
                                 var50.setLenient(false);
                                 StringBuilder var52 = new StringBuilder();
                                 String[] var55 = m;
                                 int var58 = 0;

                                 Date var61;
                                 while(true) {
                                    if (var58 >= var55.length) {
                                       var52.append("12");

                                       try {
                                          var61 = var50.parse(var52.toString());
                                       } catch (ParseException var27) {
                                          b((int)7);
                                          a("Date not valid.", "Please ensure date follows the format", "DD/MM/YYYY and is after 01/01/1900");
                                          var61 = null;
                                       }
                                       break;
                                    }

                                    var60 = var55[var58];
                                    if (var60 == null) {
                                       b((int)7);
                                       a("Date not valid.", "Please ensure all characters are populated.", "");
                                       var61 = null;
                                       break;
                                    }

                                    var52.append(var60);
                                    ++var58;
                                 }

                                 boolean var62;
                                 if (var61 == null) {
                                    var62 = false;
                                 } else {
                                    Calendar var63 = Calendar.getInstance();
                                    var63.set(1, var63.get(1) - 13);
                                    var63.set(5, var63.get(5) + 1);
                                    var63.set(11, 0);
                                    var63.set(12, 0);
                                    var63.set(13, 0);
                                    var63.set(14, 0);
                                    Date var66 = var63.getTime();
                                    var25 = var61.before(var66);
                                    boolean var26 = a(var61);
                                    if (!var26) {
                                       b((int)7);
                                       a("Date not valid.", "Please ensure date follows the format", "DD/MM/YYYY and is after 01/01/1900");
                                       var62 = false;
                                    } else {
                                       if (!var25) {
                                          k = 8388607;
                                       } else {
                                          k = (int)(var61.getTime() / 86400000L - 11745L);
                                       }

                                       var62 = true;
                                    }
                                 }

                                 if (var62) {
                                    Client.a((int)50);
                                 }

                                 return;
                              }
                           }
                        } else {
                           var33 = v + 180 - 80;
                           var34 = 321;
                           if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                              dG.a(Client.b("secure", true) + "m=dob/set_dob.ws", 465024219);
                              a(bv.ed, bv.ee, bv.ef);
                              b((int)6);
                              return;
                           }

                           var12 = v + 180 + 80;
                           if (var31 == 1 && var7 >= var12 - 75 && var7 <= var12 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                              a(true);
                           }
                        }
                     } else if (x == 8) {
                        var33 = v + 180 - 80;
                        var34 = 321;
                        if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                           dG.a("https://legal.jagex.com/docs/policies/privacy", 465024219);
                           a(bv.ed, bv.ee, bv.ef);
                           b((int)6);
                           return;
                        }

                        var12 = v + 180 + 80;
                        if (var31 == 1 && var7 >= var12 - 75 && var7 <= var12 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                           a(true);
                        }
                     } else {
                        boolean var39;
                        if (x == 9) {
                           var33 = v + 180;
                           var34 = 311;
                           var39 = var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20;

                           while(true) {
                              do {
                                 if (!var32.b()) {
                                    if (var39) {
                                       bo.a(false);
                                    }

                                    return;
                                 }
                              } while(var32.p != 84 && var32.p != 13);

                              var39 = true;
                           }
                        }

                        if (x == 10) {
                           var33 = v + 180;
                           var34 = 209;
                           var39 = var31 == 1 && var7 >= var33 - 109 && var7 <= var33 + 109 && var8 >= var34 && var8 <= var34 + 68;

                           while(var32.b()) {
                              if (var32.p == 84) {
                                 var39 = true;
                              }
                           }

                           if (var39) {
                              if (e()) {
                                 return;
                              }

                              d();
                           }
                        } else if (x == 12) {
                           var33 = w;
                           var34 = 233;
                           var35 = var2.a(0, 30, bv.eJ, var33, var34);
                           ba var42 = var2.a(32, 32, bv.eJ, var33, var34);
                           ba var46 = var2.a(70, 34, bv.eJ, var33, var34);
                           var11 = var34 + 17;
                           ba var45 = var2.a(0, 34, bv.eK, var33, var11);
                           if (var31 == 1) {
                              if (var35.c(var7, var8)) {
                                 dG.a("https://legal.jagex.com/docs/terms", 465024219);
                              } else if (var42.c(var7, var8)) {
                                 dG.a("https://legal.jagex.com/docs/policies/privacy", 465024219);
                              } else if (var46.c(var7, var8) || var45.c(var7, var8)) {
                                 dG.a("https://legal.jagex.com/docs/terms/eula", 465024219);
                              }
                           }

                           var16 = w - 80;
                           var51 = 311;
                           if (var31 == 1 && var7 >= var16 - 75 && var7 <= var16 + 75 && var8 >= var51 - 20 && var8 <= var51 + 20) {
                              Client.am();
                              bo.a(true);
                           }

                           int var53 = w + 80;
                           if (var31 == 1 && var7 >= var53 - 75 && var7 <= var53 + 75 && var8 >= var51 - 20 && var8 <= var51 + 20) {
                              x = 13;
                           }
                        } else if (x == 13) {
                           var33 = w;
                           var34 = 321;
                           if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                              bo.a(true);
                           }
                        } else if (x == 14) {
                           String var38 = "";
                           switch (d) {
                              case 0:
                                 var38 = "https://secure.runescape.com/m=offence-appeal/account-history";
                                 break;
                              case 1:
                                 var38 = "https://secure.runescape.com/m=accountappeal/passwordrecovery";
                                 break;
                              case 2:
                                 var38 = "https://support.runescape.com/hc/en-gb/articles/207256855-Settle-an-Unpaid-Balance";
                                 break;
                              default:
                                 a(false);
                           }

                           var11 = v + 180;
                           var36 = 276;
                           if (var31 == 1 && var7 >= var11 - 75 && var7 <= var11 + 75 && var8 >= var36 - 20 && var8 <= var36 + 20) {
                              dG.a(var38, 465024219);
                              a(bv.ed, bv.ee, bv.ef);
                              b((int)6);
                              return;
                           }

                           var13 = v + 180;
                           short var47 = 326;
                           if (var31 == 1 && var7 >= var13 - 75 && var7 <= var13 + 75 && var8 >= var47 - 20 && var8 <= var47 + 20) {
                              a(false);
                           }
                        } else if (x == 24) {
                           var33 = v + 180;
                           var34 = 301;
                           if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                              bo.a(false);
                           }
                        } else if (x == 32) {
                           var33 = v + 180 - 80;
                           var34 = 321;
                           if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                              dG.a(Client.b("secure", true) + "m=dob/set_dob.ws", 465024219);
                              a(bv.ed, bv.ee, bv.ef);
                              b((int)6);
                              return;
                           }

                           var12 = v + 180 + 80;
                           if (var31 == 1 && var7 >= var12 - 75 && var7 <= var12 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                              a(true);
                           }
                        } else if (x == 33) {
                           var33 = v + 180;
                           var34 = 276;
                           if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                              dG.a(bv.fd, 465024219);
                           }

                           var12 = v + 180;
                           var40 = 326;
                           if (var31 == 1 && var7 >= var12 - 75 && var7 <= var12 + 75 && var8 >= var40 - 20 && var8 <= var40 + 20) {
                              a(true);
                           }
                        } else if (x == 34) {
                           var33 = v + 180;
                           var34 = 276;
                           if (var31 == 1 && var7 >= var33 - 75 && var7 <= var33 + 75 && var8 >= var34 - 20 && var8 <= var34 + 20) {
                              h();
                              return;
                           }

                           if (bo.dg != null) {
                              h();
                           }

                           var12 = v + 180;
                           var40 = 326;
                           if (var31 == 1 && var7 >= var12 - 75 && var7 <= var12 + 75 && var8 >= var40 - 20 && var8 <= var40 + 20) {
                              a(false);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public static void c() {
      bo.cD.trim();
      if (bo.cD.length() != 6) {
         a(bv.P, bv.Q, bv.R);
      } else {
         bo.G = Integer.parseInt(bo.cD);
         bo.cD = "";
         bo.b(true);
         a(bv.dy, bv.dz, bv.dA);
         Client.a((int)20);
      }

   }

   public static void d() {
      a(bv.dy, bv.dz, bv.dA);
      Client.z = hY.b;
      bo.b(false);
      Client.a((int)20);
   }

   public static boolean e() {
      if ((Client.aK & hO.k.b()) != 0) {
         y = "";
         z = bv.du;
         A = bv.dv;
         B = bv.dw;
         b((int)1);
         return true;
      } else if ((Client.aK & hO.u.b()) != 0) {
         if ((Client.aK & hO.C.b()) != 0) {
            z = bv.dr;
            A = bv.ds;
            B = bv.dt;
         } else {
            z = bv.dl;
            A = bv.dm;
            B = bv.dn;
         }

         y = bv.dk;
         b((int)1);
         return true;
      } else if ((Client.aK & hO.C.b()) != 0) {
         z = bv.do;
         A = bv.dp;
         B = bv.dq;
         y = bv.dk;
         b((int)1);
         return true;
      } else {
         return false;
      }
   }

   public static void f() {
      if (e()) {
         b();
      } else {
         a(false);
      }

   }

   public static boolean a(Date var0) {
      Date var1 = g();
      return var0.after(var1);
   }

   public static Date g() {
      Calendar var0 = Calendar.getInstance();
      var0.set(2, 0);
      var0.set(5, 1);
      var0.set(1, 1900);
      return var0.getTime();
   }

   public static int a(int var0, int var1) {
      for(int var2 = 0; var2 < 8; ++var2) {
         if (var1 <= var0 + 30) {
            return var2;
         }

         var0 += 30;
         var0 += var2 != 1 && var2 != 3 ? 5 : 20;
      }

      return 0;
   }

   public static boolean a(char var0) {
      for(int var1 = 0; var1 < G.length(); ++var1) {
         if (var0 == G.charAt(var1)) {
            return true;
         }
      }

      return false;
   }

   public static boolean b(char var0) {
      return G.indexOf(var0) != -1;
   }

   public static void a(boolean var0) {
      if (!Client.s.at() && !Client.s.av() && !Client.s.au()) {
         z = bv.dh;
         A = bv.di;
         B = bv.dj;
         b((int)2);
         if (var0) {
            j = "";
         }

         bo.i();
         b();
      } else {
         b((int)10);
      }

   }

   public static void a(gg var0, gg var1, gg var2) {
      Client.a(var0, var1, var2);
      a = (T.eP - 765) / 2;
      v = a + 202;
      w = v + 180;
      if (I) {
         a(var0, var1);
      } else {
         bo.cI.a(a, 0);
         bo.dX.a(a + 382, 0);
         bo.dk.a(a + 382 - bo.dk.i / 2, 18);
         int var3;
         int var4;
         if (Client.w == 0 || Client.w == 5) {
            var3 = 20;
            var0.c(bv.dg, v + 180, 245 - var3, 16777215, -1);
            var4 = 253 - var3;
            aU.c(v + 180 - 152, var4, 304, 34, 9179409);
            aU.c(v + 180 - 151, var4 + 1, 302, 32, 0);
            aU.b(v + 180 - 150, var4 + 2, b * 3, 30, 9179409);
            aU.b(b * 3 + (v + 180 - 150), var4 + 2, 300 - b * 3, 30, 0);
            var0.c(c, v + 180, 276 - var3, 16777215, -1);
         }

         String var5;
         String var6;
         short var22;
         if (Client.w == 20) {
            o.a(v + 180 - o.i / 2, 271 - o.k / 2);
            var3 = 201;
            var0.c(z, v + 180, var3, 16776960, 0);
            var3 += 15;
            var0.c(A, v + 180, var3, 16776960, 0);
            var3 += 15;
            var0.c(B, v + 180, var3, 16776960, 0);
            var3 += 15;
            var3 += 7;
            if (x != 4 && x != 10 && x != 1) {
               ((aX)var0).a(bv.dH, v + 180 - 110, var3, 16777215, 0);
               var22 = 200;

               for(var5 = a(); var0.b(var5) > var22; var5 = var5.substring(0, var5.length() - 1)) {
               }

               ((aX)var0).a(aX.a(var5), v + 180 - 70, var3, 16777215, 0);
               var3 += 15;

               for(var6 = br.g(j); var0.b(var6) > var22; var6 = var6.substring(1)) {
               }

               ((aX)var0).a(bv.dI + var6, v + 180 - 108, var3, 16777215, 0);
               var3 += 15;
            }
         }

         if (Client.w == 10 || Client.w == 11 || Client.w == 50) {
            o.a(v, 171);
            short var23;
            int var25;
            short var26;
            if (x == 0) {
               var23 = 251;
               var0.c(bv.dK, v + 180, var23, 16776960, 0);
               var3 = var23 + 30;
               var4 = v + 180 - 80;
               var26 = 291;
               p.a(var4 - 73, var26 - 20);
               var0.a(bv.dL, var4 - 73, var26 - 20, 144, 40, 16777215, 0, 1, 1, 0);
               var25 = v + 180 + 80;
               p.a(var25 - 73, var26 - 20);
               var0.a(bv.dM, var25 - 73, var26 - 20, 144, 40, 16777215, 0, 1, 1, 0);
            } else if (x == 1) {
               var0.c(y, v + 180, 201, 16776960, 0);
               var23 = 236;
               var0.c(z, v + 180, var23, 16777215, 0);
               var3 = var23 + 15;
               var0.c(A, v + 180, var3, 16777215, 0);
               var3 += 15;
               var0.c(B, v + 180, var3, 16777215, 0);
               var3 += 15;
               var4 = v + 180 - 80;
               var26 = 321;
               p.a(var4 - 73, var26 - 20);
               var0.c(bv.i, var4, var26 + 5, 16777215, 0);
               var25 = v + 180 + 80;
               p.a(var25 - 73, var26 - 20);
               var0.c(bv.dO, var25, var26 + 5, 16777215, 0);
            } else {
               int var7;
               int var8;
               String var10001;
               short var29;
               if (x == 2) {
                  var23 = 201;
                  var0.c(z, w, var23, 16776960, 0);
                  var3 = var23 + 15;
                  var0.c(A, w, var3, 16776960, 0);
                  var3 += 15;
                  var0.c(B, w, var3, 16776960, 0);
                  var3 += 15;
                  var3 += 7;
                  ((aX)var0).a(bv.dH, w - 110, var3, 16777215, 0);
                  var22 = 200;

                  for(var5 = a(); var0.b(var5) > var22; var5 = var5.substring(1)) {
                  }

                  var10001 = aX.a(var5);
                  ((aX)var0).a(var10001 + (F == 0 & Client.x % 40 < 20 ? bq.b(16776960) + bq.c : ""), w - 70, var3, 16777215, 0);
                  var3 += 15;

                  for(var6 = br.g(j); var0.b(var6) > var22; var6 = var6.substring(1)) {
                  }

                  var10001 = bv.dI;
                  ((aX)var0).a(var10001 + var6 + (F == 1 & Client.x % 40 < 20 ? bq.b(16776960) + bq.c : ""), w - 108, var3, 16777215, 0);
                  var3 += 15;
                  var29 = 277;
                  var8 = w + -117;
                  boolean var31 = Client.A;
                  boolean var32 = D;
                  hX var11 = var31 ? (var32 ? bo.aX : u) : (var32 ? bo.M : t);
                  var11.a(var8, var29);
                  int var12 = var11.i + 5 + var8;
                  ((aX)var1).a(bv.dP, var12, var29 + 13, 16776960, 0);
                  int var13 = w + 24;
                  boolean var14 = Client.bL.d();
                  boolean var15 = E;
                  hX var16 = var14 ? (var15 ? bo.aX : u) : (var15 ? bo.M : t);
                  var16.a(var13, var29);
                  int var17 = var16.i + 5 + var13;
                  ((aX)var1).a(bv.dQ, var17, var29 + 13, 16776960, 0);
                  var7 = var29 + 15;
                  int var18 = w - 80;
                  short var19 = 321;
                  p.a(var18 - 73, var19 - 20);
                  var0.c(bv.dN, var18, var19 + 5, 16777215, 0);
                  int var20 = w + 80;
                  p.a(var20 - 73, var19 - 20);
                  var0.c(bv.dO, var20, var19 + 5, 16777215, 0);
                  short var21 = 357;
                  switch (e) {
                     case 2:
                        f = bv.ec;
                        break;
                     default:
                        f = bv.ea;
                  }

                  g = new ba(w, var21, var1.b(f), 11);
                  h = new ba(w, var21, var1.b(bv.eb), 11);
                  var1.c(f, w, var21, 16777215, 0);
               } else if (x == 3) {
                  var23 = 201;
                  var0.c(bv.dR, v + 180, var23, 16776960, 0);
                  var3 = var23 + 20;
                  var1.c(bv.dS, v + 180, var3, 16776960, 0);
                  var3 += 20;
                  var1.c(bv.dT, v + 180, var3, 16776960, 0);
                  var3 += 15;
                  var4 = v + 180;
                  var26 = 276;
                  p.a(var4 - 73, var26 - 20);
                  var2.c(bv.dU, var4, var26 + 5, 16777215, 0);
                  var25 = v + 180;
                  var29 = 326;
                  p.a(var25 - 73, var29 - 20);
                  var2.c(bv.dV, var25, var29 + 5, 16777215, 0);
               } else {
                  int var9;
                  int var24;
                  if (x == 4) {
                     var0.c(bv.dx, v + 180, 201, 16776960, 0);
                     var23 = 236;
                     var0.c(z, v + 180, var23, 16777215, 0);
                     var3 = var23 + 15;
                     var0.c(A, v + 180, var3, 16777215, 0);
                     var3 += 15;
                     var0.c(B, v + 180, var3, 16777215, 0);
                     var3 += 15;
                     var10001 = bv.dJ;
                     ((aX)var0).a(var10001 + br.g(bo.cD) + (Client.x % 40 < 20 ? bq.b(16776960) + bq.c : ""), v + 180 - 108, var3, 16777215, 0);
                     var3 -= 8;
                     ((aX)var0).a(bv.l, v + 180 - 9, var3, 16776960, 0);
                     var3 += 15;
                     ((aX)var0).a(bv.m, v + 180 - 9, var3, 16776960, 0);
                     var4 = v + 180 - 9 + var0.b(bv.m) + 15;
                     var24 = var3 - var0.m;
                     hX var33;
                     if (n) {
                        var33 = u;
                     } else {
                        var33 = t;
                     }

                     var33.a(var4, var24);
                     var3 += 15;
                     var7 = v + 180 - 80;
                     var8 = 321;
                     p.a(var7 - 73, var8 - 20);
                     var0.c(bv.i, var7, var8 + 5, 16777215, 0);
                     var9 = v + 180 + 80;
                     p.a(var9 - 73, var8 - 20);
                     var0.c(bv.dO, var9, var8 + 5, 16777215, 0);
                     var1.c(bv.ea, v + 180, var8 + 36, 16777215, 0);
                  } else {
                     short var10;
                     if (x == 5) {
                        var0.c(bv.dW, v + 180, 201, 16776960, 0);
                        var23 = 221;
                        var2.c(z, v + 180, var23, 16776960, 0);
                        var3 = var23 + 15;
                        var2.c(A, v + 180, var3, 16776960, 0);
                        var3 += 15;
                        var2.c(B, v + 180, var3, 16776960, 0);
                        var3 += 15;
                        var3 += 14;
                        ((aX)var0).a(bv.dX, v + 180 - 145, var3, 16777215, 0);
                        var22 = 174;
                        var5 = Client.bL.d() ? br.g(i) : i;

                        for(var6 = var5; var0.b(var6) > var22; var6 = var6.substring(1)) {
                        }

                        var10001 = aX.a(var6);
                        ((aX)var0).a(var10001 + (Client.x % 40 < 20 ? bq.b(16776960) + bq.c : ""), v + 180 - 34, var3, 16777215, 0);
                        var3 += 15;
                        var7 = v + 180 - 80;
                        var8 = 321;
                        p.a(var7 - 73, var8 - 20);
                        var0.c(bv.dY, var7, var8 + 5, 16777215, 0);
                        var9 = v + 180 + 80;
                        p.a(var9 - 73, var8 - 20);
                        var0.c(bv.dZ, var9, var8 + 5, 16777215, 0);
                        var10 = 356;
                        var1.c(bv.eb, w, var10, 268435455, 0);
                     } else if (x == 6) {
                        var23 = 201;
                        var0.c(z, v + 180, var23, 16776960, 0);
                        var3 = var23 + 15;
                        var0.c(A, v + 180, var3, 16776960, 0);
                        var3 += 15;
                        var0.c(B, v + 180, var3, 16776960, 0);
                        var3 += 15;
                        var4 = v + 180;
                        var26 = 321;
                        p.a(var4 - 73, var26 - 20);
                        var0.c(bv.dZ, var4, var26 + 5, 16777215, 0);
                     } else {
                        short var28;
                        if (x != 7) {
                           if (x == 8) {
                              var23 = 216;
                              var0.c(bv.bN, v + 180, var23, 16776960, 0);
                              var3 = var23 + 15;
                              var2.c(bv.bO, v + 180, var3, 16776960, 0);
                              var3 += 15;
                              var2.c(bv.bP, v + 180, var3, 16776960, 0);
                              var3 += 15;
                              var4 = v + 180 - 80;
                              var26 = 321;
                              p.a(var4 - 73, var26 - 20);
                              var0.c(bv.bQ, var4, var26 + 5, 16777215, 0);
                              var25 = v + 180 + 80;
                              p.a(var25 - 73, var26 - 20);
                              var0.c(bv.dZ, var25, var26 + 5, 16777215, 0);
                           } else if (x == 9) {
                              var23 = 221;
                              var0.c(z, v + 180, var23, 16776960, 0);
                              var3 = var23 + 25;
                              var0.c(A, v + 180, var3, 16776960, 0);
                              var3 += 25;
                              var0.c(B, v + 180, var3, 16776960, 0);
                              var4 = v + 180;
                              var26 = 311;
                              p.a(var4 - 73, var26 - 20);
                              var0.c(bv.dU, var4, var26 + 5, 16777215, 0);
                           } else if (x == 10) {
                              var3 = v + 180;
                              var22 = 209;
                              var0.c(bv.dK, v + 180, var22, 16776960, 0);
                              var4 = var22 + 20;
                              q.a(var3 - 109, var4);
                              if (C.isEmpty()) {
                                 r.a(var3 - 48, var4 + 18);
                              } else {
                                 r.a(var3 - 48, var4 + 5);
                                 var0.c(C, var3, var4 + 68 - 15, 16776960, 0);
                              }
                           } else if (x == 12) {
                              var3 = w;
                              var22 = 216;
                              var2.c(bv.eI, var3, var22, 16777215, 0);
                              var4 = var22 + 17;
                              var2.c(bv.eJ, var3, var4, 16777215, 0);
                              var4 += 17;
                              var2.c(bv.eK, var3, var4, 16777215, 0);
                              var4 += 17;
                              var2.c(bv.eL, var3, var4, 16777215, 0);
                              var24 = w - 80;
                              var28 = 311;
                              p.a(var24 - 73, var28 - 20);
                              var0.c(bv.eO, var24, var28 + 5, 16777215, 0);
                              var7 = w + 80;
                              p.a(var7 - 73, var28 - 20);
                              var0.c(bv.eP, var7, var28 + 5, 16777215, 0);
                           } else if (x == 13) {
                              var23 = 231;
                              var2.c(bv.eM, v + 180, var23, 16777215, 0);
                              var3 = var23 + 20;
                              var2.c(bv.eN, v + 180, var3, 16777215, 0);
                              var4 = v + 180;
                              var26 = 311;
                              p.a(var4 - 73, var26 - 20);
                              var0.c(bv.dZ, var4, var26 + 5, 16777215, 0);
                           } else if (x == 14) {
                              var23 = 201;
                              String var30 = "";
                              var5 = "";
                              var6 = "";
                              switch (d) {
                                 case 0:
                                    var30 = bv.ae;
                                    var5 = bv.af;
                                    var6 = bv.ag;
                                    break;
                                 case 1:
                                    var30 = bv.aR;
                                    var5 = bv.aS;
                                    var6 = bv.aT;
                                    break;
                                 case 2:
                                    var30 = bv.bB;
                                    var5 = bv.bC;
                                    var6 = bv.bD;
                                    break;
                                 default:
                                    a(false);
                              }

                              var0.c(var30, v + 180, var23, 16776960, 0);
                              var3 = var23 + 20;
                              var0.c(var5, v + 180, var3, 16776960, 0);
                              var3 += 20;
                              var0.c(var6, v + 180, var3, 16776960, 0);
                              var7 = v + 180;
                              var8 = 276;
                              p.a(var7 - 73, var8 - 20);
                              if (d == 1) {
                                 var0.c(bv.O, var7, var8 + 5, 16777215, 0);
                              } else {
                                 var0.c(bv.N, var7, var8 + 5, 16777215, 0);
                              }

                              var9 = v + 180;
                              var10 = 326;
                              p.a(var9 - 73, var10 - 20);
                              var0.c(bv.dZ, var9, var10 + 5, 16777215, 0);
                           } else if (x == 24) {
                              var23 = 221;
                              var0.c(z, v + 180, var23, 16777215, 0);
                              var3 = var23 + 15;
                              var0.c(A, v + 180, var3, 16777215, 0);
                              var3 += 15;
                              var0.c(B, v + 180, var3, 16777215, 0);
                              var3 += 15;
                              var4 = v + 180;
                              var26 = 301;
                              p.a(var4 - 73, var26 - 20);
                              var0.c(bv.g, var4, var26 + 5, 16777215, 0);
                           } else if (x == 32) {
                              var23 = 216;
                              var0.c(bv.eB, v + 180, var23, 16776960, 0);
                              var3 = var23 + 15;
                              var2.c(bv.eC, v + 180, var3, 16776960, 0);
                              var3 += 15;
                              var2.c(bv.eD, v + 180, var3, 16776960, 0);
                              var3 += 15;
                              var4 = v + 180 - 80;
                              var26 = 321;
                              p.a(var4 - 73, var26 - 20);
                              var0.c(bv.eE, var4, var26 + 5, 16777215, 0);
                              var25 = v + 180 + 80;
                              p.a(var25 - 73, var26 - 20);
                              var0.c(bv.dZ, var25, var26 + 5, 16777215, 0);
                           } else if (x == 33) {
                              var23 = 201;
                              var0.c(z, v + 180, var23, 16776960, 0);
                              var3 = var23 + 20;
                              var1.c(A, v + 180, var3, 16776960, 0);
                              var3 += 20;
                              var1.c(B, v + 180, var3, 16776960, 0);
                              var3 += 15;
                              var4 = v + 180;
                              var26 = 276;
                              p.a(var4 - 73, var26 - 20);
                              var2.c(bv.fc, var4, var26 + 5, 16777215, 0);
                              var25 = v + 180;
                              var29 = 326;
                              p.a(var25 - 73, var29 - 20);
                              var2.c(bv.dZ, var25, var29 + 5, 16777215, 0);
                           } else if (x == 34) {
                              var23 = 201;
                              var0.c(z, v + 180, var23, 16776960, 0);
                              var3 = var23 + 20;
                              var1.c(A, v + 180, var3, 16776960, 0);
                              var3 += 20;
                              var1.c(B, v + 180, var3, 16776960, 0);
                              var4 = v + 180;
                              var26 = 276;
                              p.a(var4 - 73, var26 - 20);
                              var6 = bo.dg != null ? bv.eZ : bv.fb;
                              var2.c(var6, var4, var26 + 5, 16777215, 0);
                              var7 = v + 180;
                              var8 = 326;
                              p.a(var7 - 73, var8 - 20);
                              var2.c(bv.dZ, var7, var8 + 5, 16777215, 0);
                           }
                        } else if (bo.bo && !Client.aR) {
                           var23 = 201;
                           var0.c(z, w, var23, 16776960, 0);
                           var3 = var23 + 15;
                           var0.c(A, w, var3, 16776960, 0);
                           var3 += 15;
                           var0.c(B, w, var3, 16776960, 0);
                           var4 = w - 150;
                           var3 += 10;

                           for(var24 = 0; var24 < 8; ++var24) {
                              p.f(var4, var3, 30, 40);
                              boolean var27 = l == var24 & Client.x % 40 < 20;
                              ((aX)var0).a((m[var24] == null ? "" : m[var24]) + (var27 ? bq.b(16776960) + bq.c : ""), var4 + 10, var3 + 27, 16777215, 0);
                              if (var24 != 1 && var24 != 3) {
                                 var4 += 35;
                              } else {
                                 var4 += 50;
                                 ((aX)var0).a(aX.a("/"), var4 - 13, var3 + 27, 16777215, 0);
                              }
                           }

                           var24 = w - 80;
                           var28 = 321;
                           p.a(var24 - 73, var28 - 20);
                           var0.c("Submit", var24, var28 + 5, 16777215, 0);
                           var7 = w + 80;
                           p.a(var7 - 73, var28 - 20);
                           var0.c(bv.dO, var7, var28 + 5, 16777215, 0);
                        } else {
                           var23 = 216;
                           var0.c(bv.eB, v + 180, var23, 16776960, 0);
                           var3 = var23 + 15;
                           var2.c(bv.eC, v + 180, var3, 16776960, 0);
                           var3 += 15;
                           var2.c(bv.eD, v + 180, var3, 16776960, 0);
                           var3 += 15;
                           var4 = v + 180 - 80;
                           var24 = 321;
                           p.a(var4 - 73, var24 - 20);
                           var0.c(bv.eE, var4, var24 + 5, 16777215, 0);
                           var25 = v + 180 + 80;
                           p.a(var25 - 73, var24 - 20);
                           var0.c(bv.dZ, var25, var24 + 5, 16777215, 0);
                        }
                     }
                  }
               }
            }
         }

         if (Client.w >= 10) {
            int[] var36 = new int[4];
            aU.a(var36);
            aU.a(a, 0, a + 765, T.eQ);
            bo.er.a(a - 22, Client.x);
            bo.er.a(a + 765 + 22 - 128, Client.x);
            aU.b(var36);
         }

         s[Client.bL.e() ? 1 : 0].a(a + 765 - 40, 463);
         if (Client.w > 5 && bo.el == ax.a) {
            if (J != null) {
               var3 = a + 5;
               var22 = 463;
               byte var34 = 100;
               byte var35 = 35;
               J.a(var3, var22);
               var0.c(bv.cC + " " + Client.u, var34 / 2 + var3, var35 / 2 + var22 - 2, 16777215, 0);
               if (bo.dg != null) {
                  var1.c(bv.eZ, var34 / 2 + var3, var35 / 2 + var22 + 12, 16777215, 0);
               } else {
                  var1.c(bv.fa, var34 / 2 + var3, var35 / 2 + var22 + 12, 16777215, 0);
               }
            } else {
               J = aY.b(bo.dM, "sl_button", "");
            }
         }
      }

   }

   public static void a(String var0, String var1, String var2) {
      z = var0;
      A = var1;
      B = var2;
   }

   public static void a(gg var0, gg var1) {
      Client.a(var0, var1);
      if (bo.cm == null) {
         bo.cm = aY.c(bo.dM, "sl_back", "");
      }

      if (bo.dd == null) {
         bo.dd = aY.a(bo.dM, "sl_flags", "");
      }

      if (bo.bK == null) {
         bo.bK = aY.a(bo.dM, "sl_arrows", "");
      }

      if (bo.r == null) {
         bo.r = aY.a(bo.dM, "sl_stars", "");
      }

      if (bo.ae == null) {
         bo.ae = aY.b(bo.dM, "leftarrow", "");
      }

      if (bo.W == null) {
         bo.W = aY.b(bo.dM, "rightarrow", "");
      }

      aU.b(a, 23, 765, 480, 0);
      aU.b(a, 0, 125, 23, 12425273, 9135624);
      aU.b(a + 125, 0, 640, 23, 5197647, 2697513);
      var0.c(bv.eQ, a + 62, 15, 0, -1);
      if (bo.r != null) {
         bo.r[1].a(a + 140, 1);
         ((aX)var1).a(bv.eR, a + 152, 10, 16777215, -1);
         bo.r[0].a(a + 140, 12);
         ((aX)var1).a(bv.eS, a + 152, 21, 16777215, -1);
      }

      int var4;
      int var5;
      if (bo.bK != null) {
         int var2 = a + 280;
         if (ey.g[0] == 0 && ey.f[0] == 0) {
            bo.bK[2].a(var2, 4);
         } else {
            bo.bK[0].a(var2, 4);
         }

         if (ey.g[0] == 0 && ey.f[0] == 1) {
            bo.bK[3].a(var2 + 15, 4);
         } else {
            bo.bK[1].a(var2 + 15, 4);
         }

         ((aX)var0).a(bv.eT, var2 + 32, 17, 16777215, -1);
         int var3 = a + 390;
         if (ey.g[0] == 1 && ey.f[0] == 0) {
            bo.bK[2].a(var3, 4);
         } else {
            bo.bK[0].a(var3, 4);
         }

         if (ey.g[0] == 1 && ey.f[0] == 1) {
            bo.bK[3].a(var3 + 15, 4);
         } else {
            bo.bK[1].a(var3 + 15, 4);
         }

         ((aX)var0).a(bv.eU, var3 + 32, 17, 16777215, -1);
         var4 = a + 500;
         if (ey.g[0] == 2 && ey.f[0] == 0) {
            bo.bK[2].a(var4, 4);
         } else {
            bo.bK[0].a(var4, 4);
         }

         if (ey.g[0] == 2 && ey.f[0] == 1) {
            bo.bK[3].a(var4 + 15, 4);
         } else {
            bo.bK[1].a(var4 + 15, 4);
         }

         ((aX)var0).a(bv.eV, var4 + 32, 17, 16777215, -1);
         var5 = a + 610;
         if (ey.g[0] == 3 && ey.f[0] == 0) {
            bo.bK[2].a(var5, 4);
         } else {
            bo.bK[0].a(var5, 4);
         }

         if (ey.g[0] == 3 && ey.f[0] == 1) {
            bo.bK[3].a(var5 + 15, 4);
         } else {
            bo.bK[1].a(var5 + 15, 4);
         }

         ((aX)var0).a(bv.eW, var5 + 32, 17, 16777215, -1);
      }

      aU.b(a + 708, 4, 50, 16, 0);
      var1.c(bv.dO, a + 708 + 25, 16, 16777215, -1);
      K = -1;
      if (bo.cm != null) {
         byte var24 = 88;
         byte var25 = 19;
         var4 = 765 / (var24 + 1) - 1;
         var5 = 480 / (var25 + 1);

         int var6;
         int var7;
         do {
            var6 = var5;
            var7 = var4;
            if ((var4 - 1) * var5 >= ey.d) {
               --var4;
            }

            if ((var5 - 1) * var4 >= ey.d) {
               --var5;
            }

            if ((var5 - 1) * var4 >= ey.d) {
               --var5;
            }
         } while(var5 != var6 || var4 != var7);

         int var8 = (765 - var24 * var4) / (var4 + 1);
         if (var8 > 5) {
            var8 = 5;
         }

         int var9 = (480 - var25 * var5) / (var5 + 1);
         if (var9 > 5) {
            var9 = 5;
         }

         int var10 = (765 - var24 * var4 - (var4 - 1) * var8) / 2;
         int var11 = (480 - var25 * var5 - (var5 - 1) * var9) / 2;
         int var12 = (ey.d + var5 - 1) / var5;
         M = var12 - var4;
         if (bo.ae != null && L > 0) {
            bo.ae.a(8, T.eQ / 2 - bo.ae.k / 2);
         }

         if (bo.W != null && L < M) {
            bo.W.a(T.eP - bo.W.i - 8, T.eQ / 2 - bo.W.k / 2);
         }

         int var13 = var11 + 23;
         int var14 = a + var10;
         int var15 = 0;
         boolean var16 = false;
         int var17 = L;

         int var18;
         for(var18 = var5 * var17; var18 < ey.d && var17 - L < var4; ++var18) {
            ey var19 = bo.ed[var18];
            boolean var20 = true;
            String var21 = Integer.toString(var19.j);
            if (var19.j == -1) {
               var21 = bv.eX;
               var20 = false;
            } else if (var19.j > 1980) {
               var21 = bv.eY;
               var20 = false;
            }

            kw var22 = null;
            int var23 = 0;
            if (var19.i()) {
               var22 = var19.c() ? kw.j : kw.i;
            } else if (var19.d()) {
               var22 = var19.c() ? kw.a : kw.q;
            } else if (var19.h()) {
               var23 = 16711680;
               var22 = var19.c() ? kw.h : kw.g;
            } else if (var19.j()) {
               var22 = var19.c() ? kw.l : kw.k;
            } else if (var19.f()) {
               var22 = var19.c() ? kw.f : kw.e;
            } else if (var19.k()) {
               var22 = var19.c() ? kw.n : kw.m;
            } else if (var19.l()) {
               var22 = var19.c() ? kw.p : kw.o;
            }

            if (var22 == null || var22.b >= bo.cm.length) {
               var22 = var19.c() ? kw.d : kw.c;
            }

            if (aI.h >= var14 && aI.i >= var13 && aI.h < var24 + var14 && aI.i < var25 + var13 && var20) {
               K = var18;
               bo.cm[var22.b].g(var14, var13, 128, 16777215);
               var16 = true;
            } else {
               bo.cm[var22.b].a(var14, var13);
            }

            if (bo.dd != null) {
               bo.dd[(var19.c() ? bo.dd.length / 2 : 0) + var19.a].a(var14 + 29, var13);
            }

            var0.c(Integer.toString(var19.h), var14 + 15, var25 / 2 + var13 + 5, var23, -1);
            var1.c(var21, var14 + 60, var25 / 2 + var13 + 5, 268435455, -1);
            var13 += var25 + var9;
            ++var15;
            if (var15 >= var5) {
               var13 = var11 + 23;
               var14 += var24 + var8;
               var15 = 0;
               ++var17;
            }
         }

         if (var16) {
            var18 = var1.b(bo.ed[K].k) + 6;
            int var26 = var1.m + 8;
            int var27 = aI.i + 25;
            if (var26 + var27 > 480) {
               var27 = aI.i - 25 - var26;
            }

            aU.b(aI.h - var18 / 2, var27, var18, var26, 16777120);
            aU.c(aI.h - var18 / 2, var27, var18, var26, 0);
            var1.c(bo.ed[K].k, aI.h, var1.m + var27 + 4, 0, -1);
         }
      }

   }

   public static void a(T var0) {
      q var1 = Client.c();

      while(var1.b()) {
         if (var1.p == 13) {
            i();
            return;
         }

         if (var1.p == 96) {
            if (L > 0 && bo.ae != null) {
               --L;
            }
         } else if (var1.p == 97 && L < M && bo.W != null) {
            ++L;
         }
      }

      if (aI.k == 1 || !bo.cP && aI.k == 4) {
         int var2 = a + 280;
         if (aI.l >= var2 && aI.l <= var2 + 14 && aI.m >= 4 && aI.m <= 18) {
            ey.a(0, 0);
            return;
         }

         if (aI.l >= var2 + 15 && aI.l <= var2 + 80 && aI.m >= 4 && aI.m <= 18) {
            ey.a(0, 1);
            return;
         }

         int var3 = a + 390;
         if (aI.l >= var3 && aI.l <= var3 + 14 && aI.m >= 4 && aI.m <= 18) {
            ey.a(1, 0);
            return;
         }

         if (aI.l >= var3 + 15 && aI.l <= var3 + 80 && aI.m >= 4 && aI.m <= 18) {
            ey.a(1, 1);
            return;
         }

         int var4 = a + 500;
         if (aI.l >= var4 && aI.l <= var4 + 14 && aI.m >= 4 && aI.m <= 18) {
            ey.a(2, 0);
            return;
         }

         if (aI.l >= var4 + 15 && aI.l <= var4 + 80 && aI.m >= 4 && aI.m <= 18) {
            ey.a(2, 1);
            return;
         }

         int var5 = a + 610;
         if (aI.l >= var5 && aI.l <= var5 + 14 && aI.m >= 4 && aI.m <= 18) {
            ey.a(3, 0);
            return;
         }

         if (aI.l >= var5 + 15 && aI.l <= var5 + 80 && aI.m >= 4 && aI.m <= 18) {
            ey.a(3, 1);
            return;
         }

         if (aI.l >= a + 708 && aI.m >= 4 && aI.l <= a + 708 + 50 && aI.m <= 20) {
            i();
            return;
         }

         if (K != -1) {
            ey var6 = bo.ed[K];
            boolean var7 = kh.a(Client.aK, hO.b);
            boolean var8 = var6.d();
            bo.q = var8;
            var6.c = var8 ? "beta" : var6.c;
            bo.a(var6);
            i();
            if (var7 != var8) {
               bo.k();
            }

            return;
         }

         if (L > 0 && bo.ae != null && aI.l >= 0 && aI.l <= bo.ae.i && aI.m >= T.eQ / 2 - 50 && aI.m <= T.eQ / 2 + 50) {
            --L;
         }

         if (L < M && bo.W != null && aI.l >= T.eP - bo.W.i - 5 && aI.l <= T.eP && aI.m >= T.eQ / 2 - 50 && aI.m <= T.eQ / 2 + 50) {
            ++L;
         }
      }

   }

   public static void a(int var0) {
      b((int)14);
      d = var0;
   }

   public static void h() {
      if (bo.d()) {
         I = true;
         L = 0;
         M = 0;
      }

   }

   public static void i() {
      if (x == 34) {
         a(false);
      }

      I = false;
      aU.b(0, 0, aU.f, aU.e, 0);
      bo.cI.a(a, 0);
      bo.dX.a(a + 382, 0);
      bo.dk.a(a + 382 - bo.dk.i / 2, 18);
   }

   public static void j() {
      b((int)24);
      a(bv.dB, bv.dC, bv.dD);
   }

   public static void b(int var0) {
      if (x != var0) {
         x = var0;
      }

   }

   public static void a(String var0) {
      C = osrs.J.a(var0);
   }

   public static void k() {
      if (Client.bL.e()) {
         eZ.a(0, 0);
      } else {
         eZ.a(0, 0);
         ArrayList var0 = new ArrayList();
         var0.add(new eX(bo.cL, "scape main", "", 255, false));
         eZ.a(var0, 0, 0, 0, 100, false);
      }

   }

   public static void a(int var0, String var1) {
      c = var1;
      b = var0;
   }

   static {
      new DecimalFormat("##0.00");
      new ct();
      a = 0;
      b = 10;
      c = "";
      d = -1;
      e = 1;
      i = "";
      j = "";
      l = 0;
      m = new String[8];
      n = true;
      v = a + 202;
      x = 0;
      y = "";
      z = "";
      A = "";
      B = "";
      C = "";
      D = false;
      E = false;
      F = 0;
      G = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"ï¿½$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
      H = "1234567890";
      I = false;
      K = -1;
      L = 0;
      M = 0;
      N = -1L;
      O = -1L;
      P = new String[]{"title.jpg"};
      Q = new String[]{"logo", "logo_deadman_mode", "logo_seasonal_mode", "titlebox", "titlebutton", "titlebutton_large", "play_now_text", "titlebutton_wide42,1", "runes", "title_mute", "options_radio_buttons,0", "options_radio_buttons,2", "options_radio_buttons,4", "options_radio_buttons,6", "sl_back", "sl_flags", "sl_arrows", "sl_stars", "sl_button"};
      R = new String[]{"logo_speedrunning", "logo_ugc_world"};
   }
}
