package osrs;

import java.util.ArrayList;
import java.util.List;
import net.runelite.api.clan.ClanMember;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.clan.ClanTitle;

public class cK implements ClanSettings {
   public int a;
   public long[] b;
   public byte[] c;
   public int[] d;
   public int[] e;
   public boolean[] f;
   public int g = -1;
   public int h = -1;
   public int i;
   public long[] j;
   public String[] k;
   public String[] l;
   public W m;
   public boolean n;
   public boolean o;
   public int[] p;
   public int q = 0;
   public String r = null;
   public int s = 0;
   public byte t;
   public byte u;
   public byte v;
   public byte w;
   public long x;
   public boolean y;

   public cK(aR var1) {
      this.a(var1);
   }

   public void a(int var1) {
      if (this.n) {
         if (this.b != null) {
            System.arraycopy(this.b, 0, this.b = new long[var1], 0, this.a);
         } else {
            this.b = new long[var1];
         }
      }

      if (this.o) {
         if (this.k != null) {
            System.arraycopy(this.k, 0, this.k = new String[var1], 0, this.a);
         } else {
            this.k = new String[var1];
         }
      }

      if (this.c != null) {
         System.arraycopy(this.c, 0, this.c = new byte[var1], 0, this.a);
      } else {
         this.c = new byte[var1];
      }

      if (this.d != null) {
         System.arraycopy(this.d, 0, this.d = new int[var1], 0, this.a);
      } else {
         this.d = new int[var1];
      }

      if (this.e != null) {
         System.arraycopy(this.e, 0, this.e = new int[var1], 0, this.a);
      } else {
         this.e = new int[var1];
      }

      if (this.f != null) {
         System.arraycopy(this.f, 0, this.f = new boolean[var1], 0, this.a);
      } else {
         this.f = new boolean[var1];
      }

   }

   public void b(int var1) {
      if (this.n) {
         if (this.j != null) {
            System.arraycopy(this.j, 0, this.j = new long[var1], 0, this.i);
         } else {
            this.j = new long[var1];
         }
      }

      if (this.o) {
         if (this.l != null) {
            System.arraycopy(this.l, 0, this.l = new String[var1], 0, this.i);
         } else {
            this.l = new String[var1];
         }
      }

   }

   public int a(int var1, int var2, int var3) {
      int var4 = var3 == 31 ? -1 : (1 << var3 + 1) - 1;
      return (this.d[var1] & var4) >>> var2;
   }

   public Integer c(int var1) {
      if (this.m == null) {
         return null;
      } else {
         az var2 = this.m.a((long)var1);
         return var2 != null && var2 instanceof aq ? new Integer(((aq)var2).a) : null;
      }
   }

   public int[] a() {
      if (this.p == null) {
         String[] var1 = new String[this.a];
         this.p = new int[this.a];

         for(int var2 = 0; var2 < this.a; this.p[var2] = var2++) {
            var1[var2] = this.k[var2];
            if (var1[var2] != null) {
               var1[var2] = var1[var2].toLowerCase();
            }
         }

         kl.a(var1, this.p);
      }

      return this.p;
   }

   public void a(long var1, String var3, int var4) {
      if (var3 != null && var3.isEmpty()) {
         var3 = null;
      }

      if (this.n != var1 > 0L) {
         throw new RuntimeException("");
      } else if (this.o != (var3 != null)) {
         throw new RuntimeException("");
      } else {
         if (var1 > 0L && (this.b == null || this.a >= this.b.length) || var3 != null && (this.k == null || this.a >= this.k.length)) {
            this.a(this.a + 5);
         }

         if (this.b != null) {
            this.b[this.a] = var1;
         }

         if (this.k != null) {
            this.k[this.a] = var3;
         }

         if (this.g == -1) {
            this.g = this.a;
            this.c[this.a] = 126;
         } else {
            this.c[this.a] = 0;
         }

         this.d[this.a] = 0;
         this.e[this.a] = var4;
         this.f[this.a] = false;
         ++this.a;
         this.p = null;
      }
   }

   public void d(int var1) {
      if (var1 >= 0 && var1 < this.a) {
         --this.a;
         this.p = null;
         if (this.a == 0) {
            this.b = null;
            this.k = null;
            this.c = null;
            this.d = null;
            this.e = null;
            this.f = null;
            this.g = -1;
            this.h = -1;
         } else {
            System.arraycopy(this.c, var1 + 1, this.c, var1, this.a - var1);
            System.arraycopy(this.d, var1 + 1, this.d, var1, this.a - var1);
            System.arraycopy(this.e, var1 + 1, this.e, var1, this.a - var1);
            System.arraycopy(this.f, var1 + 1, this.f, var1, this.a - var1);
            if (this.b != null) {
               System.arraycopy(this.b, var1 + 1, this.b, var1, this.a - var1);
            }

            if (this.k != null) {
               System.arraycopy(this.k, var1 + 1, this.k, var1, this.a - var1);
            }

            this.b();
         }

      } else {
         throw new RuntimeException("");
      }
   }

   public void b() {
      if (this.a == 0) {
         this.g = -1;
         this.h = -1;
      } else {
         this.g = -1;
         this.h = -1;
         int var1 = 0;
         byte var2 = this.c[0];

         for(int var3 = 1; var3 < this.a; ++var3) {
            if (this.c[var3] > var2) {
               if (var2 == 125) {
                  this.h = var1;
               }

               var1 = var3;
               var2 = this.c[var3];
            } else if (this.h == -1 && this.c[var3] == 125) {
               this.h = var3;
            }
         }

         this.g = var1;
         if (this.g != -1) {
            this.c[this.g] = 126;
         }
      }

   }

   public void a(long var1, String var3) {
      if (var3 != null && var3.isEmpty()) {
         var3 = null;
      }

      if (this.n != var1 > 0L) {
         throw new RuntimeException("");
      } else if (var3 != null != this.o) {
         throw new RuntimeException("");
      } else {
         if (var1 > 0L && (this.j == null || this.i >= this.j.length) || var3 != null && (this.l == null || this.i >= this.l.length)) {
            this.b(this.i + 5);
         }

         if (this.j != null) {
            this.j[this.i] = var1;
         }

         if (this.l != null) {
            this.l[this.i] = var3;
         }

         ++this.i;
      }
   }

   public void e(int var1) {
      --this.i;
      if (this.i == 0) {
         this.j = null;
         this.l = null;
      } else {
         if (this.j != null) {
            System.arraycopy(this.j, var1 + 1, this.j, var1, this.i - var1);
         }

         if (this.l != null) {
            System.arraycopy(this.l, var1 + 1, this.l, var1, this.i - var1);
         }
      }

   }

   public int a(int var1, byte var2) {
      if (var2 != 126 && var2 != 127) {
         if (this.g != var1 || this.h != -1 && this.c[this.h] >= 125) {
            if (this.c[var1] == var2) {
               return -1;
            } else {
               this.c[var1] = var2;
               this.b();
               return var1;
            }
         } else {
            return -1;
         }
      } else {
         return -1;
      }
   }

   public boolean f(int var1) {
      if (this.g != var1 && this.c[var1] != 126) {
         this.c[this.g] = 125;
         this.h = this.g;
         this.c[var1] = 126;
         this.g = var1;
         return true;
      } else {
         return false;
      }
   }

   public int a(int var1, boolean var2) {
      if (this.f[var1] == var2) {
         return -1;
      } else {
         this.f[var1] = var2;
         return var1;
      }
   }

   public int a(int var1, int var2, int var3, int var4) {
      int var5 = (1 << var3) - 1;
      int var6 = var4 == 31 ? -1 : (1 << var4 + 1) - 1;
      int var7 = var6 ^ var5;
      int var8 = var2 << var3;
      int var9 = var8 & var7;
      int var10 = this.d[var1];
      if ((var10 & var7) == var9) {
         return -1;
      } else {
         int var11 = var10 & ~var7;
         this.d[var1] = var11 | var9;
         return var1;
      }
   }

   public boolean a(int var1, int var2) {
      if (this.m != null) {
         az var3 = this.m.a((long)var1);
         if (var3 != null) {
            if (var3 instanceof aq) {
               aq var4 = (aq)var3;
               if (var4.a == var2) {
                  return false;
               }

               var4.a = var2;
               return true;
            }

            var3.X();
         }
      } else {
         this.m = new W(4);
      }

      this.m.a(new aq(var2), (long)var1);
      return true;
   }

   public boolean b(int var1, int var2, int var3, int var4) {
      int var5 = (1 << var3) - 1;
      int var6 = var4 == 31 ? -1 : (1 << var4 + 1) - 1;
      int var7 = var6 ^ var5;
      int var8 = var2 << var3;
      int var9 = var8 & var7;
      if (this.m != null) {
         az var10 = this.m.a((long)var1);
         if (var10 != null) {
            if (var10 instanceof aq) {
               aq var11 = (aq)var10;
               if ((var11.a & var7) == var9) {
                  return false;
               }

               var11.a &= ~var7;
               var11.a |= var9;
               return true;
            }

            var10.X();
         }
      } else {
         this.m = new W(4);
      }

      this.m.a(new aq(var9), (long)var1);
      return true;
   }

   public boolean a(int var1, long var2) {
      if (this.m != null) {
         az var4 = this.m.a((long)var1);
         if (var4 != null) {
            if (var4 instanceof hE) {
               hE var5 = (hE)var4;
               if (var5.a == var2) {
                  return false;
               }

               var5.a = var2;
               return true;
            }

            var4.X();
         }
      } else {
         this.m = new W(4);
      }

      this.m.a(new hE(var2), (long)var1);
      return true;
   }

   public boolean a(int var1, String var2) {
      if (var2 == null) {
         var2 = "";
      } else if (var2.length() > 80) {
         var2 = var2.substring(0, 80);
      }

      if (this.m != null) {
         az var3 = this.m.a((long)var1);
         if (var3 != null) {
            if (var3 instanceof hz) {
               hz var4 = (hz)var3;
               if (var4.a instanceof String) {
                  if (var2.equals(var4.a)) {
                     return false;
                  }

                  var4.X();
                  this.m.a(new hz(var2), var4.cm);
                  return true;
               }
            }

            var3.X();
         }
      } else {
         this.m = new W(4);
      }

      this.m.a(new hz(var2), (long)var1);
      return true;
   }

   public void a(aR var1) {
      int var2 = var1.b();
      if (var2 >= 1 && var2 <= 6) {
         int var3 = var1.b();
         if ((var3 & 1) != 0) {
            this.n = true;
         }

         if ((var3 & 2) != 0) {
            this.o = true;
         }

         if (!this.n) {
            this.b = null;
            this.j = null;
         }

         if (!this.o) {
            this.k = null;
            this.l = null;
         }

         this.q = var1.h();
         this.s = var1.h();
         if (var2 <= 3 && this.s != 0) {
            this.s += 16912800;
         }

         this.a = var1.d();
         this.i = var1.b();
         this.r = var1.m();
         if (var2 >= 4) {
            var1.h();
         }

         this.y = var1.b() == 1;
         this.t = var1.c();
         this.u = var1.c();
         this.v = var1.c();
         this.w = var1.c();
         int var4;
         if (this.a > 0) {
            if (this.n && (this.b == null || this.b.length < this.a)) {
               this.b = new long[this.a];
            }

            if (this.o && (this.k == null || this.k.length < this.a)) {
               this.k = new String[this.a];
            }

            if (this.c == null || this.c.length < this.a) {
               this.c = new byte[this.a];
            }

            if (this.d == null || this.d.length < this.a) {
               this.d = new int[this.a];
            }

            if (this.e == null || this.e.length < this.a) {
               this.e = new int[this.a];
            }

            if (this.f == null || this.f.length < this.a) {
               this.f = new boolean[this.a];
            }

            for(var4 = 0; var4 < this.a; ++var4) {
               if (this.n) {
                  this.b[var4] = var1.i();
               }

               if (this.o) {
                  this.k[var4] = var1.l();
               }

               this.c[var4] = var1.c();
               if (var2 >= 2) {
                  this.d[var4] = var1.h();
               }

               if (var2 >= 5) {
                  this.e[var4] = var1.d();
               } else {
                  this.e[var4] = 0;
               }

               if (var2 >= 6) {
                  this.f[var4] = var1.b() == 1;
               } else {
                  this.f[var4] = false;
               }
            }

            this.b();
         }

         if (this.i > 0) {
            if (this.n && (this.j == null || this.j.length < this.i)) {
               this.j = new long[this.i];
            }

            if (this.o && (this.l == null || this.l.length < this.i)) {
               this.l = new String[this.i];
            }

            for(var4 = 0; var4 < this.i; ++var4) {
               if (this.n) {
                  this.j[var4] = var1.i();
               }

               if (this.o) {
                  this.l[var4] = var1.l();
               }
            }
         }

         if (var2 >= 3) {
            var4 = var1.d();
            if (var4 > 0) {
               this.m = new W(var4 < 16 ? ks.c(var4) : 16);

               while(var4-- > 0) {
                  int var5 = var1.h();
                  int var6 = var5 & 1073741823;
                  int var7 = var5 >>> 30;
                  if (var7 == 0) {
                     int var8 = var1.h();
                     this.m.a(new aq(var8), (long)var6);
                  } else if (var7 == 1) {
                     long var10 = var1.i();
                     this.m.a(new hE(var10), (long)var6);
                  } else if (var7 == 2) {
                     String var11 = var1.m();
                     this.m.a(new hz(var11), (long)var6);
                  }
               }
            }
         }

      } else {
         throw new RuntimeException("" + var2);
      }
   }

   public int a(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         String var2 = var1.toLowerCase();
         String[] var3 = this.d();
         int[] var4 = this.f();
         int var5 = 0;
         int var6 = var4.length - 1;

         while(var5 <= var6) {
            int var7 = var5 + var6 >>> 1;
            int var8 = var4[var7];
            String var9 = var3[var8];
            int var10 = var9.toLowerCase().compareTo(var2);
            if (var10 < 0) {
               var5 = var7 + 1;
            } else {
               if (var10 <= 0) {
                  return var8;
               }

               var6 = var7 - 1;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public ClanMember findMember(String var1) {
      int var2 = this.a(var1.replace(' ', ' '));
      return var2 == -1 ? null : new iL(this, var2);
   }

   public ClanTitle titleForRank(ClanRank var1) {
      if (!Client.s.isClientThread()) {
         if (!T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         int var2;
         switch (var1.getRank()) {
            case -1:
               var2 = -1;
               break;
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 91:
            case 92:
            case 93:
            case 94:
            case 106:
            case 107:
            case 108:
            case 109:
            case 111:
            case 112:
            case 113:
            case 114:
            case 116:
            case 117:
            case 118:
            case 119:
            case 121:
            case 122:
            case 123:
            default:
               var2 = this.b(1, 0, 9);
               break;
            case 10:
               var2 = this.b(1, 10, 19);
               break;
            case 20:
               var2 = this.b(1, 20, 29);
               break;
            case 30:
               var2 = this.b(2, 0, 9);
               break;
            case 40:
               var2 = this.b(2, 10, 19);
               break;
            case 50:
               var2 = this.b(2, 20, 29);
               break;
            case 60:
               var2 = this.b(3, 0, 9);
               break;
            case 70:
               var2 = this.b(3, 10, 19);
               break;
            case 80:
               var2 = this.b(3, 20, 29);
               break;
            case 90:
               var2 = this.b(4, 0, 9);
               break;
            case 95:
               var2 = this.b(5, 20, 29);
               break;
            case 96:
               var2 = this.b(102, 0, 9);
               break;
            case 97:
               var2 = this.b(102, 10, 19);
               break;
            case 98:
               var2 = this.b(102, 20, 29);
               break;
            case 99:
               var2 = this.b(103, 0, 9);
               break;
            case 100:
               var2 = -2;
               break;
            case 101:
               var2 = this.b(103, 10, 19);
               break;
            case 102:
               var2 = this.b(103, 20, 29);
               break;
            case 103:
               var2 = this.b(104, 0, 9);
               break;
            case 104:
               var2 = this.b(104, 10, 19);
               break;
            case 105:
               var2 = this.b(4, 10, 19);
               break;
            case 110:
               var2 = this.b(4, 20, 29);
               break;
            case 115:
               var2 = this.b(5, 0, 9);
               break;
            case 120:
               var2 = this.b(5, 10, 19);
               break;
            case 124:
               var2 = this.b(104, 20, 29);
               break;
            case 125:
               var2 = -3;
               break;
            case 126:
               var2 = -4;
               break;
            case 127:
               var2 = -5;
         }

         if (var2 == 1023) {
            return null;
         } else {
            M var3 = Client.s.k(3797);
            String var4 = var3.getStringValue(var2);
            return !var4.isEmpty() ? new ClanTitle(var2, var4) : null;
         }
      }
   }

   public List getMembers() {
      int var1 = this.c();
      ArrayList var2 = new ArrayList(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.add(new iL(this, var3));
      }

      return var2;
   }

   public int b(int var1, int var2, int var3) {
      Integer var4 = this.g(var1);
      int var5 = var4 == null ? -1 : var4;
      int var6 = 31 - var3;
      return var5 << var6 >>> var2 + var6;
   }

   public int a(String var1, byte var2) {
      return this.a(var1);
   }

   public String getName() {
      return this.r;
   }

   public int c() {
      return this.a;
   }

   public String[] d() {
      return this.k;
   }

   public byte[] e() {
      return this.c;
   }

   public int[] f() {
      return this.a();
   }

   public Integer g(int var1) {
      return this.c(var1);
   }
}
