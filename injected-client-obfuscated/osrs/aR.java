package osrs;

import java.math.BigInteger;

public class aR extends az {
   public static long[] a = new long[256];
   public static int[] b = new int[256];
   public byte[] c;
   public int d;

   public aR(int var1) {
      byte[] var2 = bo.a(var1, false);
      this.c = var2;
      this.d = 0;
   }

   public aR(int var1, boolean var2) {
      this.a(var1, var2);
   }

   public aR(byte[] var1) {
      this.c = var1;
      this.d = 0;
   }

   public void a(int var1, boolean var2) {
      this.a();
      this.c = bo.a(var1, var2);
      this.d = 0;
   }

   public static int a(byte[] var0, int var1, int var2) {
      int var3 = -1;

      int var4;
      for(var4 = var1; var4 < var2; ++var4) {
         var3 = var3 >>> 8 ^ b[(var3 ^ var0[var4]) & 255];
      }

      var4 = ~var3;
      return var4;
   }

   public static int a(String var0) {
      return var0.length() + 1;
   }

   public static int b(String var0) {
      return var0.length() + 2;
   }

   public void a() {
      if (this.c != null) {
         gC.a(this.c);
      }

      this.c = null;
   }

   public void a(int var1) {
      this.c[++this.d - 1] = (byte)var1;
   }

   public void b(int var1) {
      this.c[++this.d - 1] = (byte)(var1 >> 8);
      this.c[++this.d - 1] = (byte)var1;
   }

   public void c(int var1) {
      this.c[++this.d - 1] = (byte)(var1 >> 16);
      this.c[++this.d - 1] = (byte)(var1 >> 8);
      this.c[++this.d - 1] = (byte)var1;
   }

   public void d(int var1) {
      this.c[++this.d - 1] = (byte)(var1 >> 24);
      this.c[++this.d - 1] = (byte)(var1 >> 16);
      this.c[++this.d - 1] = (byte)(var1 >> 8);
      this.c[++this.d - 1] = (byte)var1;
   }

   public void a(long var1) {
      this.c[++this.d - 1] = (byte)((int)(var1 >> 40));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 32));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 24));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 16));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 8));
      this.c[++this.d - 1] = (byte)((int)var1);
   }

   public void b(long var1) {
      this.c[++this.d - 1] = (byte)((int)(var1 >> 56));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 48));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 40));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 32));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 24));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 16));
      this.c[++this.d - 1] = (byte)((int)(var1 >> 8));
      this.c[++this.d - 1] = (byte)((int)var1);
   }

   public void a(boolean var1) {
      this.a(var1 ? 1 : 0);
   }

   public void c(String var1) {
      int var2 = var1.indexOf(0);
      if (var2 >= 0) {
         throw new IllegalArgumentException("");
      } else {
         this.d += gc.a(var1, 0, var1.length(), this.c, this.d);
         this.c[++this.d - 1] = 0;
      }
   }

   public void d(String var1) {
      int var2 = var1.indexOf(0);
      if (var2 >= 0) {
         throw new IllegalArgumentException("");
      } else {
         this.c[++this.d - 1] = 0;
         this.d += gc.a(var1, 0, var1.length(), this.c, this.d);
         this.c[++this.d - 1] = 0;
      }
   }

   public void a(CharSequence var1) {
      int var2 = km.a(var1);
      this.c[++this.d - 1] = 0;
      this.i(var2);
      int var3 = this.d;
      byte[] var4 = this.c;
      int var5 = this.d;
      int var6 = var1.length();
      int var7 = var5;

      int var8;
      for(var8 = 0; var8 < var6; ++var8) {
         char var9 = var1.charAt(var8);
         if (var9 <= 127) {
            var4[var7++] = (byte)var9;
         } else if (var9 <= 2047) {
            var4[var7++] = (byte)(192 | var9 >> 6);
            var4[var7++] = (byte)(128 | var9 & 63);
         } else {
            var4[var7++] = (byte)(224 | var9 >> 12);
            var4[var7++] = (byte)(128 | var9 >> 6 & 63);
            var4[var7++] = (byte)(128 | var9 & 63);
         }
      }

      var8 = var7 - var5;
      this.d = var8 + var3;
   }

   public void b(byte[] var1, int var2, int var3) {
      for(int var4 = var2; var4 < var2 + var3; ++var4) {
         this.c[++this.d - 1] = var1[var4];
      }

   }

   public void a(aR var1) {
      this.b((byte[])var1.c, 0, var1.d);
   }

   public void e(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         this.c[this.d - var1 - 4] = (byte)(var1 >> 24);
         this.c[this.d - var1 - 3] = (byte)(var1 >> 16);
         this.c[this.d - var1 - 2] = (byte)(var1 >> 8);
         this.c[this.d - var1 - 1] = (byte)var1;
      }
   }

   public void f(int var1) {
      if (var1 >= 0 && var1 <= 65535) {
         this.c[this.d - var1 - 2] = (byte)(var1 >> 8);
         this.c[this.d - var1 - 1] = (byte)var1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void g(int var1) {
      if (var1 >= 0 && var1 <= 255) {
         this.c[this.d - var1 - 1] = (byte)var1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void h(int var1) {
      if (var1 >= 0 && var1 < 128) {
         this.a(var1);
      } else {
         if (var1 < 0 || var1 >= 32768) {
            throw new IllegalArgumentException();
         }

         this.b(var1 + '耀');
      }

   }

   public void i(int var1) {
      if ((var1 & -128) != 0) {
         if ((var1 & -16384) != 0) {
            if ((var1 & -2097152) != 0) {
               if ((var1 & -268435456) != 0) {
                  this.a(var1 >>> 28 | 128);
               }

               this.a(var1 >>> 21 | 128);
            }

            this.a(var1 >>> 14 | 128);
         }

         this.a(var1 >>> 7 | 128);
      }

      this.a(var1 & 127);
   }

   public void j(int var1) {
      while(var1 < 0 || var1 > 127) {
         this.a(128 | var1 & 127);
         var1 >>>= 7;
      }

      this.a(var1);
   }

   public void k(int var1) {
      int var2 = var1 << 1 ^ var1 >> 31;
      this.j(var2);
   }

   public int b() {
      return this.c[++this.d - 1] & 255;
   }

   public byte c() {
      return this.c[++this.d - 1];
   }

   public int d() {
      this.d += 2;
      return ((this.c[this.d - 2] & 255) << 8) + (this.c[this.d - 1] & 255);
   }

   public int e() {
      this.d += 2;
      int var1 = ((this.c[this.d - 2] & 255) << 8) + (this.c[this.d - 1] & 255);
      if (var1 > 32767) {
         var1 -= 65536;
      }

      return var1;
   }

   public int f() {
      this.d += 3;
      return (this.c[this.d - 1] & 255) + ((this.c[this.d - 2] & 255) << 8) + ((this.c[this.d - 3] & 255) << 16);
   }

   public int g() {
      this.d += 3;
      int var1 = (this.c[this.d - 1] & 255) + ((this.c[this.d - 3] & 255) << 16) + ((this.c[this.d - 2] & 255) << 8);
      if (var1 > 8388607) {
         var1 -= 16777216;
      }

      return var1;
   }

   public int h() {
      this.d += 4;
      return (this.c[this.d - 1] & 255) + ((this.c[this.d - 2] & 255) << 8) + ((this.c[this.d - 3] & 255) << 16) + ((this.c[this.d - 4] & 255) << 24);
   }

   public long i() {
      long var1 = (long)this.h() & 4294967295L;
      long var3 = (long)this.h() & 4294967295L;
      return (var1 << 32) + var3;
   }

   public float j() {
      return Float.intBitsToFloat(this.h());
   }

   public boolean k() {
      return (this.b() & 1) == 1;
   }

   public String l() {
      if (this.c[this.d] == 0) {
         ++this.d;
         return null;
      } else {
         return this.m();
      }
   }

   public String m() {
      int var1 = this.d;

      while(this.c[++this.d - 1] != 0) {
      }

      int var2 = this.d - var1 - 1;
      return var2 == 0 ? "" : gc.a(this.c, var1, var2);
   }

   public String n() {
      byte var1 = this.c[++this.d - 1];
      if (var1 != 0) {
         throw new IllegalStateException("");
      } else {
         int var2 = this.d;

         while(this.c[++this.d - 1] != 0) {
         }

         int var3 = this.d - var2 - 1;
         return var3 == 0 ? "" : gc.a(this.c, var2, var3);
      }
   }

   public String o() {
      byte var1 = this.c[++this.d - 1];
      if (var1 != 0) {
         throw new IllegalStateException("");
      } else {
         int var2 = this.v();
         if (this.d + var2 > this.c.length) {
            throw new IllegalStateException("");
         } else {
            String var3 = km.a(this.c, this.d, var2);
            this.d += var2;
            return var3;
         }
      }
   }

   public void c(byte[] var1, int var2, int var3) {
      for(int var4 = var2; var4 < var2 + var3; ++var4) {
         var1[var4] = this.c[++this.d - 1];
      }

   }

   public int p() {
      int var1 = this.c[this.d] & 255;
      return var1 < 128 ? this.b() - 64 : this.d() - '쀀';
   }

   public int q() {
      int var1 = this.c[this.d] & 255;
      return var1 < 128 ? this.b() : this.d() - '耀';
   }

   public int r() {
      int var1 = this.c[this.d] & 255;
      return var1 < 128 ? this.b() - 1 : this.d() - '老';
   }

   public int s() {
      int var1 = 0;

      int var2;
      for(var2 = this.q(); var2 == 32767; var2 = this.q()) {
         var1 += 32767;
      }

      int var3 = var1 + var2;
      return var3;
   }

   public int t() {
      return this.c[this.d] < 0 ? this.h() & Integer.MAX_VALUE : this.d();
   }

   public int u() {
      if (this.c[this.d] < 0) {
         return this.h() & Integer.MAX_VALUE;
      } else {
         int var1 = this.d();
         return var1 == 32767 ? -1 : var1;
      }
   }

   public int v() {
      byte var1 = this.c[++this.d - 1];

      int var2;
      for(var2 = 0; var1 < 0; var1 = this.c[++this.d - 1]) {
         var2 = (var2 | var1 & 127) << 7;
      }

      return var2 | var1;
   }

   public int a(kp var1) {
      switch (var1.a().h) {
         case 0:
            return this.c();
         case 1:
            return this.b();
         case 2:
            return this.e();
         case 3:
            return this.d();
         case 4:
            return this.g();
         case 5:
            return this.f();
         case 6:
            return this.h();
         default:
            throw new IllegalArgumentException();
      }
   }

   public int w() {
      int var1 = 0;
      int var2 = 0;

      int var3;
      do {
         var3 = this.b();
         var1 |= (var3 & 127) << var2;
         var2 += 7;
      } while(var3 > 127);

      return var1;
   }

   public int x() {
      int var1 = this.w();
      return var1 >>> 1 ^ -(var1 & 1);
   }

   public void a(int[] var1) {
      int var2 = this.d / 8;
      this.d = 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = this.h();
         int var5 = this.h();
         int var6 = 0;
         int var7 = -1640531527;

         for(int var8 = 32; var8-- > 0; var5 += (var4 << 4 ^ var4 >>> 5) + var4 ^ var1[var6 >>> 11 & 3] + var6) {
            var4 += (var5 << 4 ^ var5 >>> 5) + var5 ^ var1[var6 & 3] + var6;
            var6 += var7;
         }

         this.d -= 8;
         this.d(var4);
         this.d(var5);
      }

   }

   public void b(int[] var1) {
      int var2 = this.d / 8;
      this.d = 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = this.h();
         int var5 = this.h();
         int var6 = -957401312;
         int var7 = -1640531527;

         for(int var8 = 32; var8-- > 0; var4 -= (var5 << 4 ^ var5 >>> 5) + var5 ^ var1[var6 & 3] + var6) {
            var5 -= (var4 << 4 ^ var4 >>> 5) + var4 ^ var1[var6 >>> 11 & 3] + var6;
            var6 -= var7;
         }

         this.d -= 8;
         this.d(var4);
         this.d(var5);
      }

   }

   public void a(int[] var1, int var2, int var3) {
      int var4 = this.d;
      this.d = var2;
      int var5 = (var3 - var2) / 8;

      for(int var6 = 0; var6 < var5; ++var6) {
         int var7 = this.h();
         int var8 = this.h();
         int var9 = 0;
         int var10 = -1640531527;

         for(int var11 = 32; var11-- > 0; var8 += (var7 << 4 ^ var7 >>> 5) + var7 ^ var1[var9 >>> 11 & 3] + var9) {
            var7 += (var8 << 4 ^ var8 >>> 5) + var8 ^ var1[var9 & 3] + var9;
            var9 += var10;
         }

         this.d -= 8;
         this.d(var7);
         this.d(var8);
      }

      this.d = var4;
   }

   public void b(int[] var1, int var2, int var3) {
      int var4 = this.d;
      this.d = var2;
      int var5 = (var3 - var2) / 8;

      for(int var6 = 0; var6 < var5; ++var6) {
         int var7 = this.h();
         int var8 = this.h();
         int var9 = -957401312;
         int var10 = -1640531527;

         for(int var11 = 32; var11-- > 0; var7 -= (var8 << 4 ^ var8 >>> 5) + var8 ^ var1[var9 & 3] + var9) {
            var8 -= (var7 << 4 ^ var7 >>> 5) + var7 ^ var1[var9 >>> 11 & 3] + var9;
            var9 -= var10;
         }

         this.d -= 8;
         this.d(var7);
         this.d(var8);
      }

      this.d = var4;
   }

   public void a(BigInteger var1, BigInteger var2) {
      int var3 = this.d;
      this.d = 0;
      byte[] var4 = new byte[var3];
      this.c(var4, 0, var3);
      BigInteger var5 = new BigInteger(var4);
      BigInteger var6 = var5.modPow(var1, var2);
      byte[] var7 = var6.toByteArray();
      this.d = 0;
      this.b(var7.length);
      this.b((byte[])var7, 0, var7.length);
   }

   public int l(int var1) {
      int var2 = a(this.c, var1, this.d);
      this.d(var2);
      return var2;
   }

   public boolean y() {
      this.d -= 4;
      int var1 = a((byte[])this.c, 0, this.d);
      int var2 = this.h();
      return var1 == var2;
   }

   public void m(int var1) {
      this.c[++this.d - 1] = (byte)(var1 + 128);
   }

   public void n(int var1) {
      this.c[++this.d - 1] = (byte)(0 - var1);
   }

   public void o(int var1) {
      this.c[++this.d - 1] = (byte)(128 - var1);
   }

   public int z() {
      return this.c[++this.d - 1] - 128 & 255;
   }

   public int A() {
      return 0 - this.c[++this.d - 1] & 255;
   }

   public int B() {
      return 128 - this.c[++this.d - 1] & 255;
   }

   public byte C() {
      return (byte)(this.c[++this.d - 1] - 128);
   }

   public byte D() {
      return (byte)(0 - this.c[++this.d - 1]);
   }

   public byte E() {
      return (byte)(128 - this.c[++this.d - 1]);
   }

   public void p(int var1) {
      this.c[++this.d - 1] = (byte)var1;
      this.c[++this.d - 1] = (byte)(var1 >> 8);
   }

   public void q(int var1) {
      this.c[++this.d - 1] = (byte)(var1 >> 8);
      this.c[++this.d - 1] = (byte)(var1 + 128);
   }

   public void r(int var1) {
      this.c[++this.d - 1] = (byte)(var1 + 128);
      this.c[++this.d - 1] = (byte)(var1 >> 8);
   }

   public int F() {
      this.d += 2;
      return ((this.c[this.d - 1] & 255) << 8) + (this.c[this.d - 2] & 255);
   }

   public int G() {
      this.d += 2;
      return ((this.c[this.d - 2] & 255) << 8) + (this.c[this.d - 1] - 128 & 255);
   }

   public int H() {
      this.d += 2;
      return ((this.c[this.d - 1] & 255) << 8) + (this.c[this.d - 2] - 128 & 255);
   }

   public int I() {
      this.d += 2;
      int var1 = ((this.c[this.d - 1] & 255) << 8) + (this.c[this.d - 2] & 255);
      if (var1 > 32767) {
         var1 -= 65536;
      }

      return var1;
   }

   public int J() {
      this.d += 2;
      int var1 = ((this.c[this.d - 2] & 255) << 8) + (this.c[this.d - 1] - 128 & 255);
      if (var1 > 32767) {
         var1 -= 65536;
      }

      return var1;
   }

   public int K() {
      this.d += 2;
      int var1 = ((this.c[this.d - 1] & 255) << 8) + (this.c[this.d - 2] - 128 & 255);
      if (var1 > 32767) {
         var1 -= 65536;
      }

      return var1;
   }

   public void s(int var1) {
      this.c[++this.d - 1] = (byte)var1;
      this.c[++this.d - 1] = (byte)(var1 >> 8);
      this.c[++this.d - 1] = (byte)(var1 >> 16);
   }

   public int L() {
      this.d += 3;
      int var1 = (this.c[this.d - 3] & 255) + ((this.c[this.d - 1] & 255) << 16) + ((this.c[this.d - 2] & 255) << 8);
      if (var1 > 8388607) {
         var1 -= 16777216;
      }

      return var1;
   }

   public int M() {
      this.d += 3;
      int var1 = (this.c[this.d - 2] & 255) + ((this.c[this.d - 1] & 255) << 8) + ((this.c[this.d - 3] & 255) << 16);
      if (var1 > 8388607) {
         var1 -= 16777216;
      }

      return var1;
   }

   public int N() {
      this.d += 3;
      int var1 = (this.c[this.d - 1] & 255) + ((this.c[this.d - 3] & 255) << 8) + ((this.c[this.d - 2] & 255) << 16);
      if (var1 > 8388607) {
         var1 -= 16777216;
      }

      return var1;
   }

   public void t(int var1) {
      this.c[++this.d - 1] = (byte)var1;
      this.c[++this.d - 1] = (byte)(var1 >> 8);
      this.c[++this.d - 1] = (byte)(var1 >> 16);
      this.c[++this.d - 1] = (byte)(var1 >> 24);
   }

   public void u(int var1) {
      this.c[++this.d - 1] = (byte)(var1 >> 8);
      this.c[++this.d - 1] = (byte)var1;
      this.c[++this.d - 1] = (byte)(var1 >> 24);
      this.c[++this.d - 1] = (byte)(var1 >> 16);
   }

   public void v(int var1) {
      this.c[++this.d - 1] = (byte)(var1 >> 16);
      this.c[++this.d - 1] = (byte)(var1 >> 24);
      this.c[++this.d - 1] = (byte)var1;
      this.c[++this.d - 1] = (byte)(var1 >> 8);
   }

   public int O() {
      this.d += 4;
      return (this.c[this.d - 4] & 255) + ((this.c[this.d - 3] & 255) << 8) + ((this.c[this.d - 1] & 255) << 24) + ((this.c[this.d - 2] & 255) << 16);
   }

   public int P() {
      this.d += 4;
      return (this.c[this.d - 3] & 255) + ((this.c[this.d - 4] & 255) << 8) + ((this.c[this.d - 1] & 255) << 16) + ((this.c[this.d - 2] & 255) << 24);
   }

   public int Q() {
      this.d += 4;
      return (this.c[this.d - 2] & 255) + ((this.c[this.d - 1] & 255) << 8) + ((this.c[this.d - 3] & 255) << 24) + ((this.c[this.d - 4] & 255) << 16);
   }

   public int R() {
      return this.h();
   }

   public int S() {
      return this.p();
   }

   public byte[] T() {
      return this.c;
   }

   public int U() {
      return this.q();
   }

   public void d(byte[] var1, int var2, int var3) {
      this.c(var1, var2, var3);
   }

   public float V() {
      return this.j();
   }

   public int W() {
      return this.d();
   }

   public int ae() {
      return this.s();
   }

   public byte af() {
      return this.c();
   }

   public int ag() {
      return this.d;
   }

   public int ah() {
      return this.e();
   }

   public void w(int var1) {
      this.d = var1;
   }

   public int ai() {
      return this.b();
   }

   static {
      int var0;
      for(var0 = 0; var0 < 256; ++var0) {
         int var1 = var0;

         for(int var2 = 0; var2 < 8; ++var2) {
            if ((var1 & 1) == 1) {
               var1 = var1 >>> 1 ^ -306674912;
            } else {
               var1 >>>= 1;
            }
         }

         b[var0] = var1;
      }

      for(var0 = 0; var0 < 256; ++var0) {
         long var4 = (long)var0;

         for(int var3 = 0; var3 < 8; ++var3) {
            if ((var4 & 1L) == 1L) {
               var4 = var4 >>> 1 ^ -3932672073523589310L;
            } else {
               var4 >>>= 1;
            }
         }

         a[var0] = var4;
      }

   }
}
