package osrs;

import java.util.ArrayList;
import java.util.BitSet;

public class eP extends az {
   public ArrayList a = new ArrayList(8);
   public ci[] b = new ci[128];
   public short[] c = new short[128];
   public byte[] d = new byte[128];
   public byte[] e = new byte[128];
   public eW[] f = new eW[128];
   public byte[] g = new byte[128];
   public int[] h = new int[128];
   public int i;

   public eP(byte[] var1) {
      aR var2 = new aR(var1);

      int var3;
      for(var3 = 0; var2.c[var2.d + var3] != 0; ++var3) {
      }

      byte[] var4 = new byte[var3];

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         var4[var5] = var2.c();
      }

      ++var2.d;
      ++var3;
      var5 = var2.d;
      var2.d += var3;

      int var6;
      for(var6 = 0; var2.c[var2.d + var6] != 0; ++var6) {
      }

      byte[] var7 = new byte[var6];

      int var8;
      for(var8 = 0; var8 < var6; ++var8) {
         var7[var8] = var2.c();
      }

      ++var2.d;
      ++var6;
      var8 = var2.d;
      var2.d += var6;

      int var9;
      for(var9 = 0; var2.c[var2.d + var9] != 0; ++var9) {
      }

      byte[] var10 = new byte[var9];

      for(int var11 = 0; var11 < var9; ++var11) {
         var10[var11] = var2.c();
      }

      ++var2.d;
      ++var9;
      byte[] var48 = new byte[var9];
      int var12;
      int var14;
      if (var9 > 1) {
         var48[1] = 1;
         int var13 = 1;
         var12 = 2;

         for(var14 = 2; var14 < var9; ++var14) {
            int var15 = var2.b();
            if (var15 == 0) {
               var13 = var12++;
            } else {
               if (var15 <= var13) {
                  --var15;
               }

               var13 = var15;
            }

            var48[var14] = (byte)var13;
         }
      } else {
         var12 = var9;
      }

      eW[] var49 = new eW[var12];

      int var16;
      for(var14 = 0; var14 < var49.length; ++var14) {
         eW var50 = var49[var14] = new eW();
         var16 = var2.b();
         if (var16 > 0) {
            var50.h = new byte[var16 * 2];
         }

         int var17 = var2.b();
         if (var17 > 0) {
            var50.i = new byte[var17 * 2 + 2];
            var50.i[1] = 64;
         }
      }

      var14 = var2.b();
      byte[] var51 = var14 > 0 ? new byte[var14 * 2] : null;
      var16 = var2.b();
      byte[] var52 = var16 > 0 ? new byte[var16 * 2] : null;

      int var18;
      for(var18 = 0; var2.c[var2.d + var18] != 0; ++var18) {
      }

      byte[] var19 = new byte[var18];

      int var20;
      for(var20 = 0; var20 < var18; ++var20) {
         var19[var20] = var2.c();
      }

      ++var2.d;
      ++var18;
      var20 = 0;

      int var21;
      for(var21 = 0; var21 < 128; ++var21) {
         var20 += var2.b();
         this.c[var21] = (short)var20;
      }

      var21 = 0;

      short[] var22;
      int var23;
      for(var23 = 0; var23 < 128; ++var23) {
         var21 += var2.b();
         var22 = this.c;
         var22[var23] = (short)(var22[var23] + (var21 << 8));
      }

      var23 = 0;
      int var24 = 0;
      int var25 = 0;

      int var26;
      for(var26 = 0; var26 < 128; ++var26) {
         if (var23 == 0) {
            if (var24 < var19.length) {
               var23 = var19[var24++];
            } else {
               var23 = -1;
            }

            var25 = var2.v();
         }

         var22 = this.c;
         var22[var26] = (short)(var22[var26] + ((var25 - 1 & 2) << 14));
         this.h[var26] = var25;
         --var23;
      }

      var26 = 0;
      int var27 = 0;
      int var28 = 0;

      int var29;
      for(var29 = 0; var29 < 128; ++var29) {
         if (this.h[var29] != 0) {
            if (var26 == 0) {
               if (var27 < var4.length) {
                  var26 = var4[var27++];
               } else {
                  var26 = -1;
               }

               var28 = var2.c[var5++] - 1;
            }

            this.g[var29] = (byte)var28;
            --var26;
         }
      }

      var29 = 0;
      int var30 = 0;
      int var31 = 0;

      int var32;
      for(var32 = 0; var32 < 128; ++var32) {
         if (this.h[var32] != 0) {
            if (var29 == 0) {
               if (var30 < var7.length) {
                  var29 = var7[var30++];
               } else {
                  var29 = -1;
               }

               var31 = var2.c[var8++] + 16 << 2;
            }

            this.e[var32] = (byte)var31;
            --var29;
         }
      }

      var32 = 0;
      int var33 = 0;
      eW var34 = null;

      int var35;
      for(var35 = 0; var35 < 128; ++var35) {
         if (this.h[var35] != 0) {
            if (var32 == 0) {
               var34 = var49[var48[var33]];
               if (var33 < var10.length) {
                  var32 = var10[var33++];
               } else {
                  var32 = -1;
               }
            }

            this.f[var35] = var34;
            --var32;
         }
      }

      var35 = 0;
      int var36 = 0;
      int var37 = 0;

      int var38;
      for(var38 = 0; var38 < 128; ++var38) {
         if (var35 == 0) {
            if (var36 < var19.length) {
               var35 = var19[var36++];
            } else {
               var35 = -1;
            }

            if (this.h[var38] > 0) {
               var37 = var2.b() + 1;
            }
         }

         this.d[var38] = (byte)var37;
         --var35;
      }

      this.i = var2.b() + 1;

      eW var39;
      int var40;
      for(var38 = 0; var38 < var12; ++var38) {
         var39 = var49[var38];
         if (var39.h != null) {
            for(var40 = 1; var40 < var39.h.length; var40 += 2) {
               var39.h[var40] = var2.c();
            }
         }

         if (var39.i != null) {
            for(var40 = 3; var40 < var39.i.length - 2; var40 += 2) {
               var39.i[var40] = var2.c();
            }
         }
      }

      if (var51 != null) {
         for(var38 = 1; var38 < var51.length; var38 += 2) {
            var51[var38] = var2.c();
         }
      }

      if (var52 != null) {
         for(var38 = 1; var38 < var52.length; var38 += 2) {
            var52[var38] = var2.c();
         }
      }

      int var41;
      for(var38 = 0; var38 < var12; ++var38) {
         var39 = var49[var38];
         if (var39.i != null) {
            var40 = 0;

            for(var41 = 2; var41 < var39.i.length; var41 += 2) {
               var40 = var40 + 1 + var2.b();
               var39.i[var41] = (byte)var40;
            }
         }
      }

      for(var38 = 0; var38 < var12; ++var38) {
         var39 = var49[var38];
         if (var39.h != null) {
            var40 = 0;

            for(var41 = 2; var41 < var39.h.length; var41 += 2) {
               var40 = var40 + 1 + var2.b();
               var39.h[var41] = (byte)var40;
            }
         }
      }

      int var42;
      int var43;
      int var44;
      int var45;
      int var46;
      int var53;
      byte var54;
      Object var57;
      if (var51 != null) {
         var38 = var2.b();
         var51[0] = (byte)var38;

         for(var53 = 2; var53 < var51.length; var53 += 2) {
            var38 = var38 + 1 + var2.b();
            var51[var53] = (byte)var38;
         }

         var54 = var51[0];
         byte var55 = var51[1];

         for(var41 = 0; var41 < var54; ++var41) {
            this.d[var41] = (byte)(this.d[var41] * var55 + 32 >> 6);
         }

         for(var41 = 2; var41 < var51.length; var41 += 2) {
            var42 = var51[var41];
            var43 = var51[var41 + 1];
            var44 = (var42 - var54) / 2 + (var42 - var54) * var55;

            for(var45 = var54; var45 < var42; ++var45) {
               var46 = ks.b(var44, var42 - var54);
               this.d[var45] = (byte)(this.d[var45] * var46 + 32 >> 6);
               var44 += var43 - var55;
            }

            var54 = (byte)var42;
            var55 = (byte)var43;
         }

         for(var41 = var54; var41 < 128; ++var41) {
            this.d[var41] = (byte)(this.d[var41] * var55 + 32 >> 6);
         }

         var57 = null;
      }

      if (var52 != null) {
         var38 = var2.b();
         var52[0] = (byte)var38;

         for(var53 = 2; var53 < var52.length; var53 += 2) {
            var38 = var38 + 1 + var2.b();
            var52[var53] = (byte)var38;
         }

         var54 = var52[0];
         var40 = var52[1] << 1;

         for(var41 = 0; var41 < var54; ++var41) {
            var42 = (this.e[var41] & 255) + var40;
            if (var42 < 0) {
               var42 = 0;
            }

            if (var42 > 128) {
               var42 = 128;
            }

            this.e[var41] = (byte)var42;
         }

         for(var41 = 2; var41 < var52.length; var41 += 2) {
            byte var56 = var52[var41];
            var43 = var52[var41 + 1] << 1;
            var44 = (var56 - var54) / 2 + (var56 - var54) * var40;

            for(var45 = var54; var45 < var56; ++var45) {
               var46 = ks.b(var44, var56 - var54);
               int var47 = (this.e[var45] & 255) + var46;
               if (var47 < 0) {
                  var47 = 0;
               }

               if (var47 > 128) {
                  var47 = 128;
               }

               this.e[var45] = (byte)var47;
               var44 += var43 - var40;
            }

            var54 = var56;
            var40 = var43;
         }

         for(var41 = var54; var41 < 128; ++var41) {
            var42 = (this.e[var41] & 255) + var40;
            if (var42 < 0) {
               var42 = 0;
            }

            if (var42 > 128) {
               var42 = 128;
            }

            this.e[var41] = (byte)var42;
         }

         var57 = null;
      }

      for(var38 = 0; var38 < var12; ++var38) {
         var49[var38].b = var2.b();
      }

      for(var38 = 0; var38 < var12; ++var38) {
         var39 = var49[var38];
         if (var39.h != null) {
            var39.c = var2.b();
         }

         if (var39.i != null) {
            var39.d = var2.b();
         }

         if (var39.b > 0) {
            var39.a = var2.b();
         }
      }

      for(var38 = 0; var38 < var12; ++var38) {
         var49[var38].f = var2.b();
      }

      for(var38 = 0; var38 < var12; ++var38) {
         var39 = var49[var38];
         if (var39.f > 0) {
            var39.g = var2.b();
         }
      }

      for(var38 = 0; var38 < var12; ++var38) {
         var39 = var49[var38];
         if (var39.g > 0) {
            var39.e = var2.b();
         }
      }

   }

   public static eP a(au var0, int var1) {
      byte[] var2 = var0.d(var1);
      return var2 == null ? null : new eP(var2);
   }

   public boolean a(iJ var1, BitSet var2) {
      boolean var3 = true;
      int var4 = 0;
      ci var5 = new ci();

      int var6;
      for(int var7 = var2.nextSetBit(0); var7 != -1; var7 = var2.nextSetBit(var6)) {
         var6 = var2.nextClearBit(var7);

         for(int var8 = var7; var8 < var6; ++var8) {
            if (var2.get(var8)) {
               int var9 = this.h[var8];
               if (var9 != 0) {
                  if (var4 != var9) {
                     var4 = var9--;
                     if ((var9 & 1) == 0) {
                        var5 = new ci(var1.b(var9 >> 2));
                     } else {
                        var5 = var1.a(var9 >> 2);
                     }

                     if (var5.a()) {
                        var3 = false;
                     } else {
                        this.a.add(this.a.size(), var5);
                     }
                  }

                  if (!var5.a()) {
                     this.b[var8] = var5;
                     this.h[var8] = 0;
                  }
               }
            }
         }
      }

      return var3;
   }

   public void a() {
      this.h = null;
   }
}
