package osrs;

import net.runelite.api.EntityOps;
import net.runelite.api.IterableHashTable;
import net.runelite.api.ObjectComposition;
import net.runelite.api.events.PostObjectComposition;

public class aC extends aA implements ObjectComposition, jg {
   public static final K a;
   public static final K b;
   public static final K c;
   public static eI d;
   public static eI e;
   public static boolean f;
   public static au g;
   public static au h;
   public static eI i;
   public static eI j;
   public static ModelUnlit[] k;
   public int l;
   public int[] m;
   public int[] n;
   public String o;
   public short[] p;
   public short[] q;
   public short[] r;
   public short[] s;
   public int t;
   public int u;
   public int v;
   public boolean w;
   public int x;
   public int y;
   public boolean z;
   public boolean A;
   public int B;
   public int C;
   public int D;
   public int E;
   public p F;
   public int G;
   public int H;
   public boolean I;
   public boolean J;
   public int K;
   public int L;
   public int M;
   public int N;
   public int O;
   public int P;
   public boolean Q;
   public boolean R;
   public int S;
   public int T;
   public int[] U;
   public int V;
   public int W;
   public int X;
   public int Y;
   public int Z;
   public K aa;
   public int ab;
   public int ac;
   public K ad;
   public K ae;
   public fs af;
   public int ag;
   public int ah;
   public int[] ai;
   public boolean aj;
   public boolean ak;
   public W al;
   public static final boolean am;

   public static aC a(int var0) {
      synchronized(d) {
         aC var2 = (aC)d.a((long)var0);
         aC var3;
         if (var2 != null) {
            var3 = var2;
         } else {
            byte[] var4 = g.b(6, (int)var0);
            aC var5 = new aC();
            var5.l = var0;
            if (var4 != null) {
               var5.a(new aR(var4));
            }

            var5.c();
            if (var5.R) {
               var5.v = 0;
               var5.w = false;
            }

            d.a(var5, (long)var0);
            var3 = var5;
         }

         return var3;
      }
   }

   public aC() {
      this.o = bv.k;
      this.t = 1;
      this.u = 1;
      this.v = 2;
      this.w = true;
      this.x = -1;
      this.y = -1;
      this.z = false;
      this.A = false;
      this.B = -1;
      this.C = 16;
      this.D = 0;
      this.E = 0;
      this.G = -1;
      this.H = -1;
      this.I = false;
      this.J = true;
      this.K = 128;
      this.L = 128;
      this.M = 128;
      this.N = 0;
      this.O = 0;
      this.P = 0;
      this.Q = false;
      this.R = false;
      this.S = -1;
      this.T = 0;
      this.V = -1;
      this.W = -1;
      this.X = -1;
      this.Y = 0;
      this.Z = 0;
      this.ae = a;
      this.ab = 300;
      this.ac = 300;
      this.aa = b;
      this.ad = c;
      this.af = fs.c;
      this.ag = 0;
      this.ah = 0;
      this.aj = true;
      this.ak = false;
      this.F = new p();
   }

   public L a() {
      return this.F;
   }

   public static void a(au var0, au var1, boolean var2) {
      g = var0;
      h = var1;
      f = var2;
   }

   public static void b() {
      d.a();
      e.a();
      i.a();
      j.a();
   }

   public void c() {
      if (this.x == -1) {
         this.x = 0;
         if (this.m != null && (this.n == null || this.n[0] == 10)) {
            this.x = 1;
         }

         if (this.F.b()) {
            this.x = 1;
         }
      }

      if (this.S == -1) {
         this.S = this.v != 0 ? 1 : 0;
      }

      this.g();
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
            if (var3 > 0) {
               if (this.m != null && !f) {
                  var1.d += var3 * 3;
               } else {
                  this.n = new int[var3];
                  this.m = new int[var3];

                  for(var4 = 0; var4 < var3; ++var4) {
                     this.m[var4] = var1.d();
                     this.n[var4] = var1.b();
                  }
               }
            }
            break;
         case 2:
            this.o = var1.m();
         case 3:
         case 4:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 16:
         case 20:
         case 25:
         case 26:
         case 35:
         case 36:
         case 37:
         case 38:
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
         case 60:
         case 63:
         case 76:
         case 80:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 94:
         case 97:
         case 98:
         case 99:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
         case 110:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
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
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
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
         default:
            break;
         case 5:
            var4 = var1.b();
            if (var4 > 0) {
               if (this.m != null && !f) {
                  var1.d += var4 * 2;
               } else {
                  this.n = null;
                  this.m = new int[var4];

                  for(var5 = 0; var5 < var4; ++var5) {
                     this.m[var5] = var1.d();
                  }
               }
            }
            break;
         case 6:
            var5 = var1.b();
            if (var5 > 0) {
               if (this.m != null && !f) {
                  var1.d += var5 * (kn.a.a() + 1);
               } else {
                  this.n = new int[var5];
                  this.m = new int[var5];

                  for(var6 = 0; var6 < var5; ++var6) {
                     this.m[var6] = var1.a(kn.a.b);
                     this.n[var6] = var1.b();
                  }
               }
            }
            break;
         case 7:
            var6 = var1.b();
            if (var6 > 0) {
               if (this.m != null && !f) {
                  var1.d += var6 * kn.a.a();
               } else {
                  this.n = null;
                  this.m = new int[var6];

                  for(var7 = 0; var7 < var6; ++var7) {
                     this.m[var7] = var1.a(kn.a.b);
                  }
               }
            }
            break;
         case 14:
            this.t = var1.b();
            break;
         case 15:
            this.u = var1.b();
            break;
         case 17:
            this.v = 0;
            this.w = false;
            break;
         case 18:
            this.w = false;
            break;
         case 19:
            this.x = var1.b();
            break;
         case 21:
            this.y = 0;
            break;
         case 22:
            this.z = true;
            break;
         case 23:
            this.A = true;
            break;
         case 24:
            this.B = var1.d();
            if (this.B == 65535) {
               this.B = -1;
            }
            break;
         case 27:
            this.v = 1;
            break;
         case 28:
            this.C = var1.b();
            break;
         case 29:
            this.D = var1.c();
            break;
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 100:
         case 101:
         case 102:
            this.F.a(var1, var2, 30, 34, 100, 101, 102);
            break;
         case 39:
            this.E = var1.c() * 25;
            break;
         case 40:
            var7 = var1.b();
            this.p = new short[var7];
            this.q = new short[var7];

            for(var8 = 0; var8 < var7; ++var8) {
               this.p[var8] = (short)var1.d();
               this.q[var8] = (short)var1.d();
            }

            return;
         case 41:
            var8 = var1.b();
            this.r = new short[var8];
            this.s = new short[var8];

            for(var9 = 0; var9 < var8; ++var9) {
               this.r[var9] = (short)var1.d();
               this.s[var9] = (short)var1.d();
            }

            return;
         case 61:
            var1.d();
            break;
         case 62:
            this.I = true;
            break;
         case 64:
            this.J = false;
            break;
         case 65:
            this.K = var1.d();
            break;
         case 66:
            this.L = var1.d();
            break;
         case 67:
            this.M = var1.d();
            break;
         case 68:
            this.H = var1.d();
            break;
         case 69:
            var1.b();
            break;
         case 70:
            this.N = var1.e();
            break;
         case 71:
            this.O = var1.e();
            break;
         case 72:
            this.P = var1.e();
            break;
         case 73:
            this.Q = true;
            break;
         case 74:
            this.R = true;
            break;
         case 75:
            this.S = var1.b();
            break;
         case 77:
         case 92:
            this.V = var1.d();
            if (this.V == 65535) {
               this.V = -1;
            }

            this.W = var1.d();
            if (this.W == 65535) {
               this.W = -1;
            }

            var9 = -1;
            if (var2 == 92) {
               var9 = var1.d();
               if (var9 == 65535) {
                  var9 = -1;
               }
            }

            int var10 = var1.b();
            this.U = new int[var10 + 2];

            for(var11 = 0; var11 <= var10; ++var11) {
               this.U[var11] = var1.d();
               if (this.U[var11] == 65535) {
                  this.U[var11] = -1;
               }
            }

            this.U[var10 + 1] = var9;
            break;
         case 78:
            this.X = var1.d();
            this.Y = var1.b();
            this.Z = var1.b();
            break;
         case 79:
            this.ag = var1.d();
            this.ah = var1.d();
            this.Y = var1.b();
            this.Z = var1.b();
            var11 = var1.b();
            this.ai = new int[var11];

            for(int var12 = 0; var12 < var11; ++var12) {
               this.ai[var12] = var1.d();
            }

            return;
         case 81:
            this.y = var1.b() * 256;
            break;
         case 82:
            this.G = var1.d();
            break;
         case 89:
            this.aj = false;
            break;
         case 90:
            this.ak = true;
            break;
         case 91:
            this.ae = osrs.K.a(var1.b());
            break;
         case 93:
            this.aa = osrs.K.a(var1.b());
            this.ab = var1.d();
            this.ad = osrs.K.a(var1.b());
            this.ac = var1.d();
            break;
         case 95:
            this.af = (fs)kk.a(fs.a(), var1.b());
            break;
         case 96:
            this.T = var1.b();
            break;
         case 249:
            this.al = jE.a(var1, this.al);
      }

   }

   public final ModelUnlit a(int var1, int var2) {
      synchronized(e) {
         int var4 = var1;
         aC var5 = this;
         ModelUnlit var6 = null;
         ModelUnlit var7;
         boolean var8;
         int var9;
         int var10;
         int var11;
         int var12;
         if (this.n == null) {
            Object var16;
            if (var1 != 10) {
               var16 = null;
               var7 = (ModelUnlit)var16;
               return var7;
            }

            if (this.m == null) {
               var16 = null;
               var7 = (ModelUnlit)var16;
               return var7;
            }

            var8 = this.I;
            if (var1 == 2 && var2 > 3) {
               var8 = !var8;
            }

            var9 = this.m.length;

            for(var10 = 0; var10 < var9; ++var10) {
               var11 = var5.m[var10];
               var12 = var11;
               if (var8) {
                  var12 = -var11;
               }

               var6 = (ModelUnlit)e.a((long)var12);
               if (var6 == null) {
                  var6 = ModelUnlit.a(h, var11, 0);
                  if (var6 == null) {
                     Object var13 = null;
                     var7 = (ModelUnlit)var13;
                     return var7;
                  }

                  if (var8) {
                     var6.d();
                  }

                  e.a(var6, (long)var12);
               }

               if (var9 > 1) {
                  k[var10] = var6;
               }
            }

            if (var9 > 1) {
               var6 = new ModelUnlit(k, var9);
            }
         } else {
            int var17 = -1;

            for(var9 = 0; var9 < var5.n.length; ++var9) {
               if (var5.n[var9] == var4) {
                  var17 = var9;
                  break;
               }
            }

            if (var17 == -1) {
               Object var20 = null;
               var7 = (ModelUnlit)var20;
               return var7;
            }

            var9 = var5.m[var17];
            var10 = var9;
            boolean var19 = var5.I ^ var2 > 3;
            if (var19) {
               var10 = var9 + 65536;
            }

            var6 = (ModelUnlit)e.a((long)var10);
            if (var6 == null) {
               var6 = ModelUnlit.a(h, var9, 0);
               if (var6 == null) {
                  Object var22 = null;
                  var7 = (ModelUnlit)var22;
                  return var7;
               }

               if (var19) {
                  var6.d();
               }

               e.a(var6, (long)var10);
            }
         }

         if (var5.K == 128 && var5.L == 128 && var5.M == 128) {
            var8 = false;
         } else {
            var8 = true;
         }

         boolean var18;
         if (var5.N == 0 && var5.O == 0 && var5.P == 0) {
            var18 = false;
         } else {
            var18 = true;
         }

         ModelUnlit var21 = new ModelUnlit(var6, var2 == 0 && !var8 && !var18, var5.p == null, var5.r == null, true);
         if (var4 == 4 && var2 > 3) {
            var21.b(256);
            var21.a(45, 0, -45);
         }

         var11 = var2 & 3;
         if (var11 == 1) {
            var21.q();
         } else if (var11 == 2) {
            var21.p();
         } else if (var11 == 3) {
            var21.k();
         }

         if (var5.p != null) {
            for(var12 = 0; var12 < var5.p.length; ++var12) {
               var21.a(var5.p[var12], var5.q[var12]);
            }
         }

         if (var5.r != null) {
            for(var12 = 0; var12 < var5.r.length; ++var12) {
               var21.b(var5.r[var12], var5.s[var12]);
            }
         }

         if (var8) {
            var21.c(var5.K, var5.L, var5.M);
         }

         if (var18) {
            var21.a(var5.N, var5.O, var5.P);
         }

         return var21;
      }
   }

   public final boolean b(int var1) {
      if (this.n != null) {
         for(int var4 = 0; var4 < this.n.length; ++var4) {
            if (this.n[var4] == var1) {
               return h.a(this.m[var4], (int)0);
            }
         }

         return true;
      } else if (this.m == null) {
         return true;
      } else if (var1 != 10) {
         return true;
      } else {
         boolean var2 = true;

         for(int var3 = 0; var3 < this.m.length; ++var3) {
            var2 &= h.a(this.m[var3], (int)0);
         }

         return var2;
      }
   }

   public boolean d() {
      if (this.m == null) {
         return true;
      } else {
         boolean var1 = true;

         for(int var2 = 0; var2 < this.m.length; ++var2) {
            var1 &= h.a(this.m[var2], (int)0);
         }

         return var1;
      }
   }

   public final dG a(int var1, int var2, int[][] var3, int var4, int var5, int var6) {
      long var7;
      if (this.n == null) {
         var7 = (long)((this.l << 10) + var2);
      } else {
         var7 = (long)((this.l << 10) + (var1 << 3) + var2);
      }

      Object var9 = (dG)i.a(var7);
      if (var9 == null) {
         ModelUnlit var10 = this.a(var1, var2);
         if (var10 == null) {
            return null;
         }

         if (!this.z) {
            var9 = var10.a(this.D + 64, this.E + 768, -50, -10, -50);
         } else {
            var10.q = (short)(this.D + 64);
            var10.r = (short)(this.E + 768);
            var10.e();
            var9 = var10;
         }

         i.a((aA)var9, var7);
      }

      if (this.z) {
         var9 = ((ModelUnlit)var9).a();
      }

      if (this.y >= 0) {
         if (var9 instanceof aH) {
            var9 = ((aH)var9).a(var3, var4, var5, var6, true, this.y);
         } else if (var9 instanceof ModelUnlit) {
            var9 = ((ModelUnlit)var9).a(var3, var4, var5, var6, true, this.y);
         }
      }

      return (dG)var9;
   }

   public final aH b(int var1, int var2, int[][] var3, int var4, int var5, int var6) {
      long var7;
      if (this.n == null) {
         var7 = (long)((this.l << 10) + var2);
      } else {
         var7 = (long)((this.l << 10) + (var1 << 3) + var2);
      }

      aH var9 = (aH)j.a(var7);
      if (var9 == null) {
         ModelUnlit var10 = this.a(var1, var2);
         if (var10 == null) {
            return null;
         }

         var9 = var10.a(this.D + 64, this.E + 768, -50, -10, -50);
         j.a(var9, var7);
      }

      if (this.y >= 0) {
         var9 = var9.a(var3, var4, var5, var6, true, this.y);
      }

      return var9;
   }

   public aC e() {
      int var1 = -1;
      if (this.V != -1) {
         var1 = osrs.x.a(this.V);
      } else if (this.W != -1) {
         var1 = osrs.x.c[this.W];
      }

      int var2;
      if (var1 >= 0 && var1 < this.U.length - 1) {
         var2 = this.U[var1];
      } else {
         var2 = this.U[this.U.length - 1];
      }

      return var2 != -1 ? a(var2) : null;
   }

   public int b(int var1, int var2) {
      return jE.a(this.al, var1, var2);
   }

   public long a(int var1, long var2) {
      W var4 = this.al;
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
      return jE.a(this.al, var1, var2);
   }

   public boolean f() {
      if (this.U == null) {
         return this.X != -1 || this.ai != null;
      } else {
         for(int var1 = 0; var1 < this.U.length; ++var1) {
            if (this.U[var1] != -1) {
               aC var2 = a(this.U[var1]);
               if (var2.X != -1 || var2.ai != null) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public aH a(iX var1, int var2, int var3, int[][] var4, int var5, int var6, int var7, bk var8, int var9) {
      long var10;
      if (this.n == null) {
         var10 = (long)((this.l << 10) + var3);
      } else {
         var10 = (long)((this.l << 10) + (var2 << 3) + var3);
      }

      aH var12;
      synchronized(j) {
         var12 = (aH)j.c(var10);
      }

      if (var12 == null) {
         ModelUnlit var13 = this.c(var2, var3);
         if (var13 == null) {
            return null;
         }

         var12 = var13.b(this.D + 64, this.E + 768, -50, -10, -50);
         synchronized(j) {
            j.b(var12, var10);
         }
      }

      if (var8 == null && this.y == -1) {
         return var12;
      } else {
         if (var8 != null) {
            if (!am && var1 == null) {
               throw new AssertionError();
            }

            var12 = var8.a(var1, var12, var9, var3);
         } else if (var1 != null) {
            var12 = var12.a(var1, true);
         }

         if (this.y >= 0 && var4 != null) {
            var12 = var12.b(var4, var5, var6, var7, var1 == null, this.y);
         }

         return var12;
      }
   }

   public void g() {
      if (this.y > 0 && (bo.fe & 16) == 0) {
         this.y = -1;
      }

      PostObjectComposition var1 = new PostObjectComposition(this);
      Client.s.getCallbacks().post(var1);
   }

   public String[] getActions() {
      String[] var1 = new String[this.F.a.size()];

      for(int var2 = 0; var2 < this.F.a.size(); ++var2) {
         p.c var3 = (p.c)this.F.a.get(var2);
         if (var3 != null) {
            var1[var2] = var3.g;
         }
      }

      return var1;
   }

   public EntityOps getOps() {
      return this.h();
   }

   public final aH a(int var1, int var2, int[][] var3, int var4, int var5, int var6, bk var7, int var8, int var9) {
      return this.a(iX.c, var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public ObjectComposition getImpostor() {
      return this.l();
   }

   public int getVarbitId() {
      return this.V;
   }

   public p h() {
      return this.F;
   }

   public boolean i() {
      return this.d();
   }

   public dG c(int var1, int var2, int[][] var3, int var4, int var5, int var6) {
      return this.a(var1, var2, var3, var4, var5, var6);
   }

   public static aC c(int var0) {
      return a(var0);
   }

   public int[] getImpostorIds() {
      return this.U;
   }

   public int[] getObjectModels() {
      return this.m;
   }

   public boolean j() {
      return this.f();
   }

   public void setMapSceneId(int var1) {
      this.H = var1;
   }

   public int getMapIconId() {
      return this.G;
   }

   public int getSizeY() {
      return this.u;
   }

   public int k() {
      return this.t * 1546111625 * 1401211833;
   }

   public int getMapSceneId() {
      return this.H;
   }

   public int getId() {
      return this.l;
   }

   public int getVarPlayerId() {
      return this.W;
   }

   public aC l() {
      return this.e();
   }

   public IterableHashTable getParams() {
      return this.al;
   }

   public int getSizeX() {
      return this.t;
   }

   public void setMapIconId(int var1) {
      this.G = var1;
   }

   public ModelUnlit c(int var1, int var2) {
      return this.a(var1, var2);
   }

   public int m() {
      return this.u * 1586768979 * 2120153051;
   }

   public String getName() {
      return this.o;
   }

   public void a(W var1) {
      this.al = var1;
   }

   static {
      a = osrs.K.q;
      b = osrs.K.q;
      c = osrs.K.q;
      d = new eI(4096);
      e = new eI(500);
      f = false;
      i = new eI(256);
      j = new eI(256);
      k = new ModelUnlit[4];
      am = !aC.class.desiredAssertionStatus();
      j.b(256);
   }
}
