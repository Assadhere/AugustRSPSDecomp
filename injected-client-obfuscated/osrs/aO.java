package osrs;

import net.runelite.api.ItemComposition;
import net.runelite.api.IterableHashTable;
import net.runelite.api.events.PostItemComposition;

public class aO extends aA implements ItemComposition, jg {
   public static int a;
   public static au b;
   public static au c;
   public static boolean d;
   public static eI e = new eI(64);
   public static eI f = new eI(50);
   public static eI g = new eI(200);
   public static gg h;
   public int i;
   public int j;
   public String k;
   public String l;
   public short[] m;
   public short[] n;
   public short[] o;
   public short[] p;
   public int q;
   public int r;
   public int s;
   public int t;
   public int u;
   public int v;
   public int w;
   public int x;
   public int y;
   public int z;
   public int A;
   public boolean B;
   public p C;
   public String[] D;
   public String[][] E;
   public int[] F;
   public int[] G;
   public int H;
   public int I;
   public int J;
   public int K;
   public int L;
   public int M;
   public int N;
   public int O;
   public int P;
   public W Q;
   public boolean R;
   public int S;
   public int T;
   public int U;
   public int V;
   public int W;
   public int X;
   public int Y;
   public int Z;
   public int aa;
   public int ab;
   public int ac;
   public int ad;
   public int ae;
   public int af;
   public int ag;
   public int ah;
   public int ai;
   public int aj = -2;
   public String ak;

   public aO() {
      this.k = bv.k;
      this.l = bv.k;
      this.q = 2000;
      this.r = 0;
      this.s = 0;
      this.t = 0;
      this.u = 0;
      this.v = 0;
      this.w = 0;
      this.x = 1;
      this.y = -1;
      this.z = -1;
      this.A = -1;
      this.B = false;
      this.D = new String[]{null, null, null, null, bv.e};
      this.E = (String[][])null;
      this.H = -1;
      this.I = -1;
      this.J = 128;
      this.K = 128;
      this.L = 128;
      this.M = 0;
      this.N = 0;
      this.O = 0;
      this.P = 0;
      this.R = false;
      this.S = -1;
      this.T = -1;
      this.U = -1;
      this.V = -1;
      this.W = -2;
      this.X = -1;
      this.Y = -1;
      this.Z = 0;
      this.aa = -1;
      this.ab = -1;
      this.ac = 0;
      this.ad = -1;
      this.ae = -1;
      this.af = -1;
      this.ag = -1;
      this.ah = -1;
      this.ai = -1;
      this.C = new p();
      this.C.a(2, (String)bv.d);
   }

   public L a() {
      return this.C;
   }

   public static aO a(int var0) {
      aO var1 = (aO)e.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = b.b(10, (int)var0);
         aO var3 = new aO();
         var3.i = var0;
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         var3.c();
         if (var3.I != -1) {
            var3.a(a(var3.I), a(var3.H));
         }

         if (var3.T != -1) {
            var3.b(a(var3.T), a(var3.S));
         }

         if (var3.V != -1) {
            var3.c(a(var3.V), a(var3.U));
         }

         if (!d && var3.B) {
            if (var3.I == -1 && var3.T == -1 && var3.V == -1) {
               var3.k = var3.k + bv.a;
            }

            var3.l = bv.b;
            var3.R = false;
            var3.C.a();
            var3.C.a(2, (String)bv.d);

            for(int var4 = 0; var4 < var3.D.length; ++var4) {
               if (var4 != 4) {
                  if (var3.E != null) {
                     var3.E[var4] = null;
                  }

                  var3.D[var4] = null;
               }
            }

            var3.W = -2;
            var3.O = 0;
            if (var3.Q != null) {
               boolean var7 = false;

               for(az var5 = var3.Q.b(); var5 != null; var5 = var3.Q.c()) {
                  aT var6 = aT.a((int)var5.cm);
                  if (var6.b) {
                     var5.X();
                  } else {
                     var7 = true;
                  }
               }

               if (!var7) {
                  var3.Q = null;
               }
            }
         }

         e.a(var3, (long)var0);
         return var3;
      }
   }

   public static final aV a(int var0, int var1, int var2, int var3, int var4, boolean var5, int var6, int var7) {
      if (var6 == 36 && var7 == 32) {
         if (var1 == -1) {
            var4 = 0;
         } else if (var4 == 2 && var1 != 1) {
            var4 = 1;
         }
      } else {
         var4 = 0;
      }

      long var8 = ((long)var3 << 42) + ((long)var4 << 40) + ((long)var2 << 38) + ((long)var1 << 16) + (long)var0;
      if (!var5 && var6 == 36 && var7 == 32) {
         aV var10 = (aV)g.a(var8);
         if (var10 != null) {
            return var10;
         }
      }

      aO var25 = a(var0);
      if (var1 > 1 && var25.G != null) {
         int var11 = -1;

         for(int var12 = 0; var12 < 10; ++var12) {
            if (var1 >= var25.F[var12] && var25.F[var12] != 0) {
               var11 = var25.G[var12];
            }
         }

         if (var11 != -1) {
            var25 = a(var11);
         }
      }

      aH var26 = var25.c(1);
      if (var26 == null) {
         return null;
      } else {
         aV var27 = null;
         if (var25.I != -1) {
            var27 = a(var25.H, 10, 1, 0, 0, true, var6, var7);
            if (var27 == null) {
               return null;
            }
         } else if (var25.T != -1) {
            var27 = a(var25.S, var1, var2, var3, 0, false, var6, var7);
            if (var27 == null) {
               return null;
            }
         } else if (var25.V != -1) {
            var27 = a(var25.U, var1, 0, 0, 0, false, var6, var7);
            if (var27 == null) {
               return null;
            }
         }

         int[] var13 = aU.h;
         int var14 = aU.f;
         int var15 = aU.e;
         float[] var16 = aU.g;
         int[] var17 = new int[4];
         aU.a(var17);
         aV var18 = new aV(var6, var7);
         aW.a(var18.k, var6, var7, (float[])null);
         aU.b();
         aW.k();
         int var19 = var7 >> 1;
         aW.a(var19, var19);
         aW.h.r = false;
         if (var25.V != -1) {
            var27.b(0, 0);
         }

         int var20 = var25.q;
         if (var6 != 36) {
            var20 = var20 * 32 / var6;
         }

         if (var5) {
            var20 = (int)((double)var20 * 1.5);
         } else if (var2 == 2) {
            var20 = (int)((double)var20 * 1.04);
         }

         int var21 = aW.d[var25.r] * var20 >> 16;
         int var22 = aW.e[var25.r] * var20 >> 16;
         var26.h();
         var26.a(0, var25.s, var25.t, var25.r, var25.u, var25.v + var26.cd / 2 + var21, var25.v + var22);
         if (var25.T != -1) {
            var27.b(0, 0);
         }

         if (var2 >= 1) {
            var18.b(1);
         }

         if (var2 >= 2) {
            var18.b(16777215);
         }

         if (var3 != 0) {
            var18.c(var3);
         }

         aW.a(var18.k, var6, var7, (float[])null);
         if (var25.I != -1) {
            var27.b(0, 0);
         }

         if (var4 == 1 || var4 == 2 && var25.w == 1) {
            gg var23 = h;
            String var24;
            if (var1 < 100000) {
               var24 = "<col=ffff00>" + var1 + "</col>";
            } else if (var1 < 10000000) {
               var24 = "<col=ffffff>" + var1 / 1000 + bv.cB + "</col>";
            } else {
               var24 = "<col=00ff80>" + var1 / 1000000 + bv.cz + "</col>";
            }

            ((aX)var23).a(var24, 0, 9, 16776960, 1);
         }

         if (!var5 && var6 == 36 && var7 == 32) {
            g.a(var18, var8);
         }

         aW.a(var13, var14, var15, var16);
         aU.b(var17);
         aW.k();
         aW.h.r = true;
         return var18;
      }
   }

   public static void b() {
      g.a();
   }

   public static void a(boolean var0) {
      if (d != var0) {
         e.a();
         f.a();
         g.a();
         d = var0;
      }

   }

   public void c() {
      if (this.w == 1) {
         this.P = 0;
      }

      this.f();
   }

   public void a(aR var1) {
      while(true) {
         int var2 = var1.b();
         if (var2 == 0) {
            return;
         }

         this.a(var1, var2);
      }
   }

   public void a(aR var1, int var2) {
      int var4;
      int var5;
      switch (var2) {
         case 1:
            this.j = var1.d();
            break;
         case 2:
            this.k = var1.m();
            break;
         case 3:
            this.l = var1.m();
            break;
         case 4:
            this.q = var1.d();
            break;
         case 5:
            this.r = var1.d();
            break;
         case 6:
            this.s = var1.d();
            break;
         case 7:
            this.u = var1.d();
            if (this.u > 32767) {
               this.u -= 65536;
            }
            break;
         case 8:
            this.v = var1.d();
            if (this.v > 32767) {
               this.v -= 65536;
            }
            break;
         case 9:
            var1.m();
         case 10:
         case 15:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 28:
         case 29:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 76:
         case 77:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 96:
         case 99:
         case 116:
         case 117:
         case 118:
         case 119:
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 128:
         case 129:
         case 130:
         case 131:
         case 132:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 150:
         case 151:
         case 152:
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 167:
         case 168:
         case 169:
         case 170:
         case 171:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 191:
         case 192:
         case 193:
         case 194:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 203:
         case 204:
         case 205:
         case 206:
         case 207:
         case 208:
         case 209:
         case 210:
         case 211:
         case 212:
         case 213:
         case 214:
         case 215:
         case 216:
         case 217:
         case 218:
         case 219:
         case 220:
         case 221:
         case 222:
         case 223:
         case 224:
         case 225:
         case 226:
         case 227:
         case 228:
         case 229:
         case 230:
         case 231:
         case 232:
         case 233:
         case 234:
         case 235:
         case 236:
         case 237:
         case 238:
         case 239:
         case 240:
         case 241:
         case 242:
         case 243:
         case 244:
         case 245:
         case 246:
         case 247:
         case 248:
         default:
            break;
         case 11:
            this.w = 1;
            break;
         case 12:
            this.x = var1.h();
            break;
         case 13:
            this.y = var1.b();
            break;
         case 14:
            this.z = var1.b();
            break;
         case 16:
            this.B = true;
            break;
         case 23:
            this.X = var1.d();
            this.Z = var1.b();
            break;
         case 24:
            this.Y = var1.d();
            break;
         case 25:
            this.aa = var1.d();
            this.ac = var1.b();
            break;
         case 26:
            this.ab = var1.d();
            break;
         case 27:
            this.A = var1.b();
            break;
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 200:
         case 201:
         case 202:
            this.C.a(var1, var2, 30, 34, 200, 201, 202);
            break;
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
            this.D[var2 - 35] = var1.m();
            break;
         case 40:
            int var3 = var1.b();
            this.m = new short[var3];
            this.n = new short[var3];

            for(var4 = 0; var4 < var3; ++var4) {
               this.m[var4] = (short)var1.d();
               this.n[var4] = (short)var1.d();
            }

            return;
         case 41:
            var4 = var1.b();
            this.o = new short[var4];
            this.p = new short[var4];

            for(var5 = 0; var5 < var4; ++var5) {
               this.o[var5] = (short)var1.d();
               this.p[var5] = (short)var1.d();
            }

            return;
         case 42:
            this.W = var1.c();
            break;
         case 43:
            var5 = var1.b();
            if (this.E == null) {
               this.E = new String[5][];
            }

            boolean var6 = var5 >= 0 && var5 < 5;
            if (var6 && this.E[var5] == null) {
               this.E[var5] = new String[20];
            }

            while(true) {
               int var7 = var1.b() - 1;
               if (var7 == -1) {
                  return;
               }

               String var8 = var1.m();
               if (var6 && var7 >= 0 && var7 < 20) {
                  this.E[var5][var7] = var8;
               }
            }
         case 44:
            this.j = var1.a(kn.a.b);
            break;
         case 45:
            this.X = var1.a(kn.a.b);
            this.Z = var1.b();
            break;
         case 46:
            this.Y = var1.a(kn.a.b);
            break;
         case 47:
            this.ad = var1.a(kn.a.b);
            break;
         case 48:
            this.aa = var1.a(kn.a.b);
            this.ac = var1.b();
            break;
         case 49:
            this.ab = var1.a(kn.a.b);
            break;
         case 50:
            this.ae = var1.a(kn.a.b);
            break;
         case 51:
            this.af = var1.a(kn.a.b);
            break;
         case 52:
            this.ag = var1.a(kn.a.b);
            break;
         case 53:
            this.ah = var1.a(kn.a.b);
            break;
         case 54:
            this.ai = var1.a(kn.a.b);
            break;
         case 65:
            this.R = true;
            break;
         case 75:
            this.P = var1.e();
            break;
         case 78:
            this.ad = var1.d();
            break;
         case 79:
            this.ae = var1.d();
            break;
         case 90:
            this.af = var1.d();
            break;
         case 91:
            this.ah = var1.d();
            break;
         case 92:
            this.ag = var1.d();
            break;
         case 93:
            this.ai = var1.d();
            break;
         case 94:
            var1.d();
            break;
         case 95:
            this.t = var1.d();
            break;
         case 97:
            this.H = var1.d();
            break;
         case 98:
            this.I = var1.d();
            break;
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
            if (this.G == null) {
               this.G = new int[10];
               this.F = new int[10];
            }

            this.G[var2 - 100] = var1.d();
            this.F[var2 - 100] = var1.d();
            break;
         case 110:
            this.J = var1.d();
            break;
         case 111:
            this.K = var1.d();
            break;
         case 112:
            this.L = var1.d();
            break;
         case 113:
            this.M = var1.c();
            break;
         case 114:
            this.N = var1.c() * 5;
            break;
         case 115:
            this.O = var1.b();
            break;
         case 139:
            this.S = var1.d();
            break;
         case 140:
            this.T = var1.d();
            break;
         case 148:
            this.U = var1.d();
            break;
         case 149:
            this.V = var1.d();
            break;
         case 249:
            this.Q = jE.a(var1, this.Q);
      }

   }

   public void a(aO var1, aO var2) {
      this.j = var1.j;
      this.q = var1.q;
      this.r = var1.r;
      this.s = var1.s;
      this.t = var1.t;
      this.u = var1.u;
      this.v = var1.v;
      this.m = var1.m;
      this.n = var1.n;
      this.o = var1.o;
      this.p = var1.p;
      this.k = var2.k;
      this.l = bv.c;
      this.B = var2.B;
      this.x = var2.x;
      this.w = 1;
      this.d(var1, var2);
   }

   public void b(aO var1, aO var2) {
      this.j = var1.j;
      this.q = var1.q;
      this.r = var1.r;
      this.s = var1.s;
      this.t = var1.t;
      this.u = var1.u;
      this.v = var1.v;
      this.m = var2.m;
      this.n = var2.n;
      this.o = var2.o;
      this.p = var2.p;
      this.k = var2.k;
      this.l = var2.l;
      this.B = var2.B;
      this.w = var2.w;
      this.y = var2.y;
      this.z = var2.z;
      this.A = var2.A;
      this.X = var2.X;
      this.Y = var2.Y;
      this.ad = var2.ad;
      this.aa = var2.aa;
      this.ab = var2.ab;
      this.ae = var2.ae;
      this.af = var2.af;
      this.ag = var2.ag;
      this.ah = var2.ah;
      this.ai = var2.ai;
      this.O = var2.O;
      this.C = var2.C;
      this.P = var2.P;
      this.D = new String[5];
      if (var2.D != null) {
         for(int var3 = 0; var3 < 4; ++var3) {
            this.D[var3] = var2.D[var3];
         }
      }

      this.D[4] = bv.j;
      if (var2.E != null) {
         this.E = new String[5][];
         System.arraycopy(var2.E, 0, this.E, 0, 4);
      } else {
         this.E = (String[][])null;
      }

      this.x = 0;
      this.f(var1, var2);
   }

   public void c(aO var1, aO var2) {
      this.j = var1.j;
      this.q = var1.q;
      this.r = var1.r;
      this.s = var1.s;
      this.t = var1.t;
      this.u = var1.u;
      this.v = var1.v;
      this.m = var1.m;
      this.n = var1.n;
      this.o = var1.o;
      this.p = var1.p;
      this.w = var1.w;
      this.k = var2.k;
      this.l = var2.l;
      this.x = 0;
      this.B = false;
      this.R = false;
      this.e(var1, var2);
   }

   public final ModelUnlit b(int var1) {
      if (this.G != null && var1 > 1) {
         int var2 = -1;

         for(int var3 = 0; var3 < 10; ++var3) {
            if (var1 >= this.F[var3] && this.F[var3] != 0) {
               var2 = this.G[var3];
            }
         }

         if (var2 != -1) {
            return a(var2).b(1);
         }
      }

      ModelUnlit var4 = ModelUnlit.a(c, this.j, 0);
      if (var4 == null) {
         return null;
      } else {
         if (this.J != 128 || this.K != 128 || this.L != 128) {
            var4.c(this.J, this.K, this.L);
         }

         this.a(var4, (cF)null);
         return var4;
      }
   }

   public final aH c(int var1) {
      if (this.G != null && var1 > 1) {
         int var2 = -1;

         for(int var3 = 0; var3 < 10; ++var3) {
            if (var1 >= this.F[var3] && this.F[var3] != 0) {
               var2 = this.G[var3];
            }
         }

         if (var2 != -1) {
            return a(var2).c(1);
         }
      }

      aH var5 = (aH)f.a((long)this.i);
      if (var5 != null) {
         return var5;
      } else {
         ModelUnlit var6 = ModelUnlit.a(c, this.j, 0);
         if (var6 == null) {
            return null;
         } else {
            if (this.J != 128 || this.K != 128 || this.L != 128) {
               var6.c(this.J, this.K, this.L);
            }

            this.a(var6, (cF)null);
            aH var4 = var6.a(this.M + 64, this.N + 768, -50, -10, -50);
            var4.T = true;
            f.a(var4, (long)this.i);
            return var4;
         }
      }
   }

   public final aH a(cF var1) {
      aH var2 = var1.h;
      if (var2 != null) {
         return var2;
      } else {
         ModelUnlit var3 = ModelUnlit.a(c, var1.g, 0);
         if (var3 == null) {
            return null;
         } else {
            if (this.J != 128 || this.K != 128 || this.L != 128) {
               var3.c(this.J, this.K, this.L);
            }

            this.a(var3, var1);
            aH var4 = var3.a(this.M + 64, this.N + 768, -50, -10, -50);
            var4.T = true;
            var1.h = var4;
            return var4;
         }
      }
   }

   public aO d(int var1) {
      if (this.G != null && var1 > 1) {
         int var2 = -1;

         for(int var3 = 0; var3 < 10; ++var3) {
            if (var1 >= this.F[var3] && this.F[var3] != 0) {
               var2 = this.G[var3];
            }
         }

         if (var2 != -1) {
            return a(var2);
         }
      }

      return this;
   }

   public final boolean a(int var1, cF var2) {
      int var3 = this.X;
      int var4 = this.Y;
      int var5 = this.ad;
      if (var1 == 1) {
         var3 = this.aa;
         var4 = this.ab;
         var5 = this.ae;
      }

      if (var2 != null && var2.a(var1)) {
         var3 = var2.c(var1);
      }

      if (var3 == -1) {
         return true;
      } else {
         boolean var6 = true;
         if (!c.a(var3, (int)0)) {
            var6 = false;
         }

         if (var4 != -1 && !c.a(var4, (int)0)) {
            var6 = false;
         }

         if (var5 != -1 && !c.a(var5, (int)0)) {
            var6 = false;
         }

         return var6;
      }
   }

   public final ModelUnlit b(int var1, cF var2) {
      int var3 = this.X;
      int var4 = this.Y;
      int var5 = this.ad;
      if (var1 == 1) {
         var3 = this.aa;
         var4 = this.ab;
         var5 = this.ae;
      }

      if (var2 != null && var2.a(var1)) {
         var3 = var2.c(var1);
      }

      if (var3 == -1) {
         return null;
      } else {
         ModelUnlit var6 = ModelUnlit.a(c, var3, 0);
         if (var4 != -1) {
            ModelUnlit var7 = ModelUnlit.a(c, var4, 0);
            if (var5 != -1) {
               ModelUnlit var8 = ModelUnlit.a(c, var5, 0);
               ModelUnlit[] var9 = new ModelUnlit[]{var6, var7, var8};
               var6 = new ModelUnlit(var9, 3);
            } else {
               ModelUnlit[] var10 = new ModelUnlit[]{var6, var7};
               var6 = new ModelUnlit(var10, 2);
            }
         }

         if (var1 == 0 && this.Z != 0) {
            var6.a(0, this.Z, 0);
         }

         if (var1 == 1 && this.ac != 0) {
            var6.a(0, this.ac, 0);
         }

         this.a(var6, var2);
         return var6;
      }
   }

   public final boolean c(int var1, cF var2) {
      int var3 = this.af;
      int var4 = this.ag;
      if (var1 == 1) {
         var3 = this.ah;
         var4 = this.ai;
      }

      if (var2 != null && var2.b(var1)) {
         var3 = var2.d(var1);
      }

      if (var3 == -1) {
         return true;
      } else {
         boolean var5 = true;
         if (!c.a(var3, (int)0)) {
            var5 = false;
         }

         if (var4 != -1 && !c.a(var4, (int)0)) {
            var5 = false;
         }

         return var5;
      }
   }

   public ModelUnlit d(int var1, cF var2) {
      int var3 = this.af;
      int var4 = this.ag;
      if (var1 == 1) {
         var3 = this.ah;
         var4 = this.ai;
      }

      if (var2 != null && var2.b(var1)) {
         var3 = var2.d(var1);
      }

      if (var3 == -1) {
         return null;
      } else {
         ModelUnlit var5 = ModelUnlit.a(c, var3, 0);
         if (var4 != -1) {
            ModelUnlit var6 = ModelUnlit.a(c, var4, 0);
            ModelUnlit[] var7 = new ModelUnlit[]{var5, var6};
            var5 = new ModelUnlit(var7, 2);
         }

         this.a(var5, var2);
         return var5;
      }
   }

   public void a(ModelUnlit var1, cF var2) {
      short[] var3;
      short[] var4;
      int var5;
      if (this.m != null) {
         var3 = this.m;
         var4 = this.n;
         if (var2 != null && var2.e != null && this.n.length == var2.e.length) {
            var4 = var2.e;
         }

         for(var5 = 0; var5 < this.m.length; ++var5) {
            var1.a(var3[var5], var4[var5]);
         }
      }

      if (this.o != null) {
         var3 = this.o;
         var4 = this.p;
         if (var2 != null && var2.f != null && this.p.length == var2.f.length) {
            var4 = var2.f;
         }

         for(var5 = 0; var5 < this.o.length; ++var5) {
            var1.b(var3[var5], var4[var5]);
         }
      }

   }

   public int a(int var1, int var2) {
      return jE.a(this.Q, var1, var2);
   }

   public long a(int var1, long var2) {
      W var4 = this.Q;
      long var5;
      if (var4 == null) {
         var5 = var2;
      } else {
         hE var7 = (hE)var4.a((long)var1);
         if (var7 == null) {
            var5 = var2;
         } else {
            var5 = var7.a;
         }
      }

      return var5;
   }

   public String a(int var1, String var2) {
      return jE.a(this.Q, var1, var2);
   }

   public boolean d() {
      return this.n != null;
   }

   public boolean e() {
      return this.p != null;
   }

   public void d(aO var1, aO var2) {
      this.ak = var2.getMembersName();
   }

   public int getHaPrice() {
      int var1 = this.getPrice();
      return (int)((float)var1 * 0.6F);
   }

   public void setShiftClickActionIndex(int var1) {
      this.aj = var1;
   }

   public void e(aO var1, aO var2) {
      this.ak = var2.getMembersName();
   }

   public int a(byte var1) {
      int var2;
      if (this.aj == -2) {
         int var3;
         if (this.W != -1 && this.D != null) {
            if (this.W >= 0) {
               var3 = this.D[this.W] != null ? this.W : -1;
               var2 = var3;
            } else {
               var3 = bv.e.equalsIgnoreCase(this.D[4]) ? 4 : -1;
               var2 = var3;
            }
         } else {
            var3 = -1;
            var2 = var3;
         }
      } else {
         var2 = this.aj;
      }

      return var2;
   }

   public void f() {
      this.ak = this.getName();
      PostItemComposition var1 = new PostItemComposition(this);
      Client.s.getCallbacks().post(var1);
   }

   public void f(aO var1, aO var2) {
      this.ak = var2.getMembersName();
   }

   public boolean isStackable() {
      return this.g() != 0;
   }

   public String getMembersName() {
      return this.ak;
   }

   public int getNote() {
      return this.I;
   }

   public IterableHashTable getParams() {
      return this.Q;
   }

   public int getZan2d() {
      return this.t;
   }

   public void setTextureToReplace(short[] var1) {
      this.o = var1;
   }

   public int getXan2d() {
      return this.r;
   }

   public String getName() {
      return this.k;
   }

   public int getAmbient() {
      return this.M;
   }

   public void setAmbient(int var1) {
      this.M = var1;
   }

   public void setXan2d(int var1) {
      this.r = var1;
   }

   public void setYan2d(int var1) {
      this.s = var1;
   }

   public int getPlaceholderId() {
      return this.U;
   }

   public void setInventoryModel(int var1) {
      this.j = var1;
   }

   public void setTextureToReplaceWith(short[] var1) {
      this.p = var1;
   }

   public short[] getColorToReplace() {
      return this.m;
   }

   public String[] getInventoryActions() {
      return this.D;
   }

   public int getContrast() {
      return this.N;
   }

   public void setContrast(int var1) {
      this.N = var1;
   }

   public int getId() {
      return this.i;
   }

   public String[][] getSubops() {
      return this.E;
   }

   public int getShiftClickActionIndex() {
      return this.a((byte)97);
   }

   public short[] getTextureToReplaceWith() {
      return this.p;
   }

   public short[] getTextureToReplace() {
      return this.o;
   }

   public int getYan2d() {
      return this.s;
   }

   public void setColorToReplaceWith(short[] var1) {
      this.n = var1;
   }

   public void setName(String var1) {
      this.k = var1;
   }

   public int getPlaceholderTemplateId() {
      return this.V;
   }

   public int getPrice() {
      return this.x;
   }

   public boolean isMembers() {
      return this.B;
   }

   public void setColorToReplace(short[] var1) {
      this.m = var1;
   }

   public short[] getColorToReplaceWith() {
      return this.n;
   }

   public boolean isTradeable() {
      return this.R;
   }

   public void a(W var1) {
      this.Q = var1;
   }

   public int g() {
      return this.w;
   }

   public void setZan2d(int var1) {
      this.t = var1;
   }

   public int getLinkedNoteId() {
      return this.H;
   }

   public int getInventoryModel() {
      return this.j;
   }

   public int getEquipmentSlot() {
      return this.y;
   }

   public int getEquipmentSlot2() {
      return this.z;
   }

   public int getMaleModel() {
      return this.X;
   }

   public int getMaleModel1() {
      return this.Y;
   }

   public int getFemaleModel() {
      return this.aa;
   }

   public int getFemaleModel1() {
      return this.ab;
   }

   public int getZoom2d() {
      return this.q;
   }

   public void setZoom2d(int var1) {
      this.q = var1;
   }

   public int getXOffset2d() {
      return this.u;
   }

   public void setXOffset2d(int var1) {
      this.u = var1;
   }

   public int getYOffset2d() {
      return this.v;
   }

   public void setYOffset2d(int var1) {
      this.v = var1;
   }

   static {
      e.b(1024);
   }
}
