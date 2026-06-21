package osrs;

import custom.GhostRender;
import net.runelite.api.EntityOps;
import net.runelite.api.IterableHashTable;
import net.runelite.api.NPCComposition;

public class aN extends aA implements NPCComposition, jg {
   public static eI a = new eI(64);
   public static eI b = new eI(256);
   public int c;
   public String d;
   public int e;
   public int[] f;
   public int[] g;
   public int h;
   public int i;
   public int j;
   public int k;
   public int l;
   public int m;
   public int n;
   public int o;
   public int p;
   public int q;
   public int r;
   public int s;
   public int t;
   public int u;
   public int v;
   public boolean w;
   public short[] x;
   public short[] y;
   public short[] z;
   public short[] A;
   public p B;
   public boolean C;
   public int D;
   public int E;
   public int F;
   public int G;
   public int H;
   public int I;
   public int[] J;
   public int K;
   public int L;
   public boolean M;
   public boolean N;
   public boolean O;
   public boolean P;
   public int Q;
   public W R;
   public int[] S;
   public int[] T;
   public short[] U;
   public int V;
   public boolean W;
   public int X;
   public boolean Y;
   public cY Z;
   public static au aa;
   public static au ab;

   public aN() {
      this.d = bv.k;
      this.j = 1;
      this.u = -1;
      this.t = -1;
      this.q = -1;
      this.v = -1;
      this.e = -1;
      this.i = -1;
      this.p = -1;
      this.D = -1;
      this.m = -1;
      this.h = -1;
      this.n = -1;
      this.Q = -1;
      this.c = -1;
      this.I = -1;
      this.s = -1;
      this.w = false;
      this.C = true;
      this.r = -1;
      this.F = 128;
      this.K = 128;
      this.X = 0;
      this.V = 0;
      this.k = 32;
      this.H = -1;
      this.G = -1;
      this.M = true;
      this.N = true;
      this.O = false;
      this.P = false;
      this.o = -1;
      this.S = new int[]{1, 1, 1, 1, 1, 1};
      this.T = null;
      this.U = null;
      this.L = -1;
      this.W = false;
      this.E = 39188;
      this.Y = true;
      this.Z = cY.b;
      this.B = new p();
   }

   public L a() {
      return this.B;
   }

   public static void a(au var0, au var1) {
      aa = var0;
      ab = var1;
   }

   public static aN a(int var0) {
      aN var1 = (aN)a.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = aa.b(9, (int)var0);
         aN var3 = new aN();
         var3.l = var0;
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         var3.b();
         a.a(var3, (long)var0);
         return var3;
      }
   }

   public void b() {
      if (this.L == -1) {
         this.L = (int)((float)(this.j * 128) * 0.4F);
      }

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
      int var6;
      int var7;
      int var8;
      int var9;
      int var11;
      switch (var2) {
         case 1:
            int var3 = var1.b();
            this.f = new int[var3];

            for(var4 = 0; var4 < var3; ++var4) {
               this.f[var4] = var1.d();
            }

            return;
         case 2:
            this.d = var1.m();
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
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
         case 90:
         case 91:
         case 92:
         case 94:
         case 96:
         case 104:
         case 105:
         case 108:
         case 110:
         case 112:
         case 113:
         case 119:
         case 120:
         case 121:
         case 125:
         case 127:
         case 128:
         case 129:
         case 131:
         case 132:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 148:
         case 149:
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
         case 200:
         case 201:
         case 202:
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
         case 250:
         default:
            break;
         case 12:
            this.j = var1.b();
            break;
         case 13:
            this.u = var1.d();
            break;
         case 14:
            this.v = var1.d();
            break;
         case 15:
            this.t = var1.d();
            break;
         case 16:
            this.q = var1.d();
            break;
         case 17:
            this.v = var1.d();
            this.e = var1.d();
            this.i = var1.d();
            this.p = var1.d();
            break;
         case 18:
            var1.d();
            break;
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 251:
         case 252:
         case 253:
            this.B.a(var1, var2, 30, 34, 251, 252, 253);
            break;
         case 40:
            var4 = var1.b();
            this.x = new short[var4];
            this.A = new short[var4];

            for(var5 = 0; var5 < var4; ++var5) {
               this.x[var5] = (short)var1.d();
               this.A[var5] = (short)var1.d();
            }

            return;
         case 41:
            var5 = var1.b();
            this.z = new short[var5];
            this.y = new short[var5];

            for(var6 = 0; var6 < var5; ++var6) {
               this.z[var6] = (short)var1.d();
               this.y[var6] = (short)var1.d();
            }

            return;
         case 60:
            var6 = var1.b();
            this.g = new int[var6];

            for(var7 = 0; var7 < var6; ++var7) {
               this.g[var7] = var1.d();
            }

            return;
         case 61:
            var7 = var1.b();
            this.f = new int[var7];

            for(var8 = 0; var8 < var7; ++var8) {
               this.f[var8] = var1.a(kn.a.b);
            }

            return;
         case 62:
            var8 = var1.b();
            this.g = new int[var8];

            for(var9 = 0; var9 < var8; ++var9) {
               this.g[var9] = var1.a(kn.a.b);
            }

            return;
         case 74:
            this.S[0] = var1.d();
            break;
         case 75:
            this.S[1] = var1.d();
            break;
         case 76:
            this.S[2] = var1.d();
            break;
         case 77:
            this.S[3] = var1.d();
            break;
         case 78:
            this.S[4] = var1.d();
            break;
         case 79:
            this.S[5] = var1.d();
            break;
         case 93:
            this.C = false;
            break;
         case 95:
            this.r = var1.d();
            break;
         case 97:
            this.F = var1.d();
            break;
         case 98:
            this.K = var1.d();
            break;
         case 99:
            this.Z = cY.a;
            break;
         case 100:
            this.X = var1.c();
            break;
         case 101:
            this.V = var1.c();
            break;
         case 102:
            var9 = var1.b();
            int var10 = 0;

            for(var11 = var9; var11 != 0; var11 >>= 1) {
               ++var10;
            }

            this.T = new int[var10];
            this.U = new short[var10];

            for(var11 = 0; var11 < var10; ++var11) {
               if ((var9 & 1 << var11) == 0) {
                  this.T[var11] = -1;
                  this.U[var11] = -1;
               } else {
                  this.T[var11] = var1.u();
                  this.U[var11] = (short)var1.r();
               }
            }

            return;
         case 103:
            this.k = var1.d();
            break;
         case 106:
         case 118:
            this.H = var1.d();
            if (this.H == 65535) {
               this.H = -1;
            }

            this.G = var1.d();
            if (this.G == 65535) {
               this.G = -1;
            }

            var11 = -1;
            if (var2 == 118) {
               var11 = var1.d();
               if (var11 == 65535) {
                  var11 = -1;
               }
            }

            int var12 = var1.b();
            this.J = new int[var12 + 2];

            for(int var13 = 0; var13 <= var12; ++var13) {
               this.J[var13] = var1.d();
               if (this.J[var13] == 65535) {
                  this.J[var13] = -1;
               }
            }

            this.J[var12 + 1] = var11;
            break;
         case 107:
            this.M = false;
            break;
         case 109:
            this.N = false;
            break;
         case 111:
            this.Z = cY.c;
            break;
         case 114:
            this.D = var1.d();
            break;
         case 115:
            this.D = var1.d();
            this.m = var1.d();
            this.h = var1.d();
            this.n = var1.d();
            break;
         case 116:
            this.Q = var1.d();
            break;
         case 117:
            this.Q = var1.d();
            this.c = var1.d();
            this.I = var1.d();
            this.s = var1.d();
            break;
         case 122:
            this.O = true;
            break;
         case 123:
            this.P = true;
            break;
         case 124:
            this.o = var1.d();
            break;
         case 126:
            this.L = var1.d();
            break;
         case 130:
            this.w = true;
            break;
         case 145:
            this.W = true;
            break;
         case 146:
            this.E = var1.d();
            break;
         case 147:
            this.Y = false;
            break;
         case 249:
            this.R = jE.a(var1, this.R);
      }

   }

   public final aH a(bk var1, int var2, bk var3, int var4, aL var5) {
      if (this.J != null) {
         aN var10 = this.c();
         return var10 == null ? null : var10.a(var1, var2, var3, var4, var5);
      } else {
         long var6 = (long)this.l;
         if (var5 != null) {
            var6 |= var5.b << 16;
         }

         aH var8 = (aH)b.a(var6);
         if (var8 == null) {
            ModelUnlit var9 = this.a(this.f, var5);
            if (var9 == null) {
               return null;
            }

            var8 = var9.a(this.X + 64, this.V * 5 + 850, -30, -50, -30);
            b.a(var8, var6);
         }

         aH var11;
         if (var1 != null && var3 != null) {
            var11 = var1.b(var8, var2, var3, var4);
         } else if (var1 != null) {
            var11 = var1.a(var8, var2);
         } else if (var3 != null) {
            var11 = var3.a(var8, var4);
         } else {
            var11 = var8.a(true);
         }

         if (this.F != 128 || this.K != 128) {
            var11.d(this.F, this.K, this.F);
         }

         return var11;
      }
   }

   public ModelUnlit a(aL var1) {
      if (this.J != null) {
         aN var2 = this.c();
         return var2 == null ? null : var2.a(var1);
      } else {
         return this.a(this.g, var1);
      }
   }

   public ModelUnlit a(int[] var1, aL var2) {
      int[] var3 = var1;
      if (var2 != null && var2.c != null) {
         var3 = var2.c;
      }

      if (var3 == null) {
         return null;
      } else {
         boolean var4 = false;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5] != -1 && !ab.a(var3[var5], (int)0)) {
               var4 = true;
            }
         }

         if (var4) {
            return null;
         } else {
            ModelUnlit[] var9 = new ModelUnlit[var3.length];

            for(int var6 = 0; var6 < var3.length; ++var6) {
               var9[var6] = ModelUnlit.a(ab, var3[var6], 0);
            }

            GhostRender.applyOverridePairs(this, var2, var9);
            ModelUnlit var10;
            if (var9.length == 1) {
               var10 = var9[0];
               if (var10 == null) {
                  var10 = new ModelUnlit(var9, var9.length);
               }
            } else {
               var10 = new ModelUnlit(var9, var9.length);
            }

            short[] var7;
            int var8;
            if (this.x != null) {
               var7 = this.A;
               if (var2 != null && var2.d != null) {
                  var7 = var2.d;
               }

               for(var8 = 0; var8 < this.x.length; ++var8) {
                  var10.a(this.x[var8], var7[var8]);
               }
            }

            if (this.z != null) {
               var7 = this.y;
               if (var2 != null && var2.e != null) {
                  var7 = var2.e;
               }

               for(var8 = 0; var8 < this.z.length; ++var8) {
                  var10.b(this.z[var8], var7[var8]);
               }
            }

            return var10;
         }
      }
   }

   public final aN c() {
      int var1 = -1;
      if (this.H != -1) {
         var1 = osrs.x.a(this.H);
      } else if (this.G != -1) {
         var1 = osrs.x.c[this.G];
      }

      int var2;
      if (var1 >= 0 && var1 < this.J.length - 1) {
         var2 = this.J[var1];
      } else {
         var2 = this.J[this.J.length - 1];
      }

      return var2 != -1 ? a(var2) : null;
   }

   public boolean d() {
      if (this.J == null) {
         return true;
      } else {
         int var1 = -1;
         if (this.H != -1) {
            var1 = osrs.x.a(this.H);
         } else if (this.G != -1) {
            var1 = osrs.x.c[this.G];
         }

         if (var1 >= 0 && var1 < this.J.length) {
            return this.J[var1] != -1;
         } else {
            return this.J[this.J.length - 1] != -1;
         }
      }
   }

   public int a(int var1, int var2) {
      return jE.a(this.R, var1, var2);
   }

   public long a(int var1, long var2) {
      W var4 = this.R;
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
      return jE.a(this.R, var1, var2);
   }

   public boolean e() {
      return this.T != null && this.U != null;
   }

   public int[] f() {
      return this.T;
   }

   public int b(int var1) {
      return this.T != null && var1 < this.T.length ? this.T[var1] : -1;
   }

   public short[] g() {
      return this.U;
   }

   public short c(int var1) {
      return this.U != null && var1 < this.U.length ? this.U[var1] : -1;
   }

   public int h() {
      return this.L;
   }

   public boolean i() {
      return this.W;
   }

   public int j() {
      return this.E;
   }

   public String[] getActions() {
      String[] var1 = new String[this.B.a.size()];

      for(int var2 = 0; var2 < this.B.a.size(); ++var2) {
         p.c var3 = (p.c)this.B.a.get(var2);
         if (var3 != null) {
            var1[var2] = var3.g;
         }
      }

      return var1;
   }

   public EntityOps getOps() {
      return this.l();
   }

   public NPCComposition transform() {
      return this.k();
   }

   public aN k() {
      return this.c();
   }

   public int[] getStats() {
      return this.S;
   }

   public int getId() {
      return this.l;
   }

   public int getSize() {
      return this.j;
   }

   public int getHeightScale() {
      return this.K;
   }

   public p l() {
      return this.B;
   }

   public int getWidthScale() {
      return this.F;
   }

   public boolean isMinimapVisible() {
      return this.C;
   }

   public int getFootprintSize() {
      return this.L;
   }

   public short[] getColorToReplace() {
      return this.x;
   }

   public int[] getChatheadModels() {
      return this.g;
   }

   public boolean isInteractible() {
      return this.M;
   }

   public int[] getModels() {
      return this.f;
   }

   public int[] getConfigs() {
      return this.J;
   }

   public boolean isFollower() {
      return this.O;
   }

   public int getCombatLevel() {
      return this.r;
   }

   public IterableHashTable getParams() {
      return this.R;
   }

   public void a(W var1) {
      this.R = var1;
   }

   public short[] getColorToReplaceWith() {
      return this.A;
   }

   public String getName() {
      return this.d;
   }
}
