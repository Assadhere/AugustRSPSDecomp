package osrs;

import java.util.Random;
import net.runelite.api.FontTypeFace;

public abstract class aX extends aU implements FontTypeFace {
   public static int i = 0;
   public static Random j = new Random();
   public static String[] k = new String[100];
   public int l;
   public int m = 0;
   public int n;
   public byte[][] o = new byte[256][];
   public int[] p;
   public int[] q;
   public int[] r;
   public int[] s;
   public int[] t;
   public byte[] u;
   public static final StringBuilder v = new StringBuilder(100);
   public static int w = -1;
   public static int x = -1;
   public static int y = -1;
   public static int z = -1;
   public static int A = 0;
   public static int B = 0;
   public static int C = 256;
   public static int D = 0;
   public static hX[] E;

   public aX(byte[] var1, int[] var2, int[] var3, int[] var4, int[] var5, byte[][] var6) {
      this.r = var2;
      this.q = var3;
      this.s = var4;
      this.p = var5;
      this.a(var1);
      this.o = var6;
      int var7 = Integer.MAX_VALUE;
      int var8 = Integer.MIN_VALUE;

      for(int var9 = 0; var9 < 256; ++var9) {
         if (this.q[var9] < var7 && this.p[var9] != 0) {
            var7 = this.q[var9];
         }

         if (this.q[var9] + this.p[var9] > var8) {
            var8 = this.q[var9] + this.p[var9];
         }
      }

      this.n = this.m - var7;
      this.l = var8 - this.m;
   }

   public aX(byte[] var1) {
      this.a(var1);
   }

   public static int a(byte[][] var0, byte[][] var1, int[] var2, int[] var3, int[] var4, int var5, int var6) {
      int var7 = var2[var5];
      int var8 = var4[var5] + var7;
      int var9 = var2[var6];
      int var10 = var4[var6] + var9;
      int var11 = var7;
      if (var9 > var7) {
         var11 = var9;
      }

      int var12 = var8;
      if (var10 < var8) {
         var12 = var10;
      }

      int var13 = var3[var5];
      if (var3[var6] < var13) {
         var13 = var3[var6];
      }

      byte[] var14 = var1[var5];
      byte[] var15 = var0[var6];
      int var16 = var11 - var7;
      int var17 = var11 - var9;

      for(int var18 = var11; var18 < var12; ++var18) {
         int var19 = var14[var16++] + var15[var17++];
         if (var19 < var13) {
            var13 = var19;
         }
      }

      return -var13;
   }

   public static String a(String var0) {
      int var1 = var0.length();
      int var2 = 0;

      int var4;
      for(int var3 = 0; var3 < var1; ++var3) {
         var4 = var0.charAt(var3);
         if (var4 == 60 || var4 == 62) {
            var2 += 3;
         }
      }

      StringBuilder var6 = new StringBuilder(var1 + var2);

      for(var4 = 0; var4 < var1; ++var4) {
         char var5 = var0.charAt(var4);
         if (var5 == '<') {
            var6.append("<lt>");
         } else if (var5 == '>') {
            var6.append("<gt>");
         } else {
            var6.append(var5);
         }
      }

      return var6.toString();
   }

   public static void a(byte[] var0, int var1, int var2, int var3, int var4, int var5) {
      int var6 = aU.f * var2 + var1;
      int var7 = aU.f - var3;
      int var8 = 0;
      int var9 = 0;
      int var10;
      if (var2 < a) {
         var10 = a - var2;
         var4 -= var10;
         var2 = a;
         var9 += var3 * var10;
         var6 += aU.f * var10;
      }

      if (var2 + var4 > b) {
         var4 -= var2 + var4 - b;
      }

      if (var1 < c) {
         var10 = c - var1;
         var3 -= var10;
         var1 = c;
         var9 += var10;
         var6 += var10;
         var8 += var10;
         var7 += var10;
      }

      if (var1 + var3 > d) {
         var10 = var1 + var3 - d;
         var3 -= var10;
         var8 += var10;
         var7 += var10;
      }

      if (var3 > 0 && var4 > 0) {
         a(aU.h, var0, var5, var9, var6, var3, var4, var7, var8);
      }

   }

   public static void a(int[] var0, byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = -(var5 >> 2);
      int var10 = -(var5 & 3);

      for(int var11 = -var6; var11 < 0; ++var11) {
         int var12;
         for(var12 = var9; var12 < 0; ++var12) {
            if (var1[var3++] != 0) {
               var0[var4++] = var2 | -16777216;
            } else {
               ++var4;
            }

            if (var1[var3++] != 0) {
               var0[var4++] = var2 | -16777216;
            } else {
               ++var4;
            }

            if (var1[var3++] != 0) {
               var0[var4++] = var2 | -16777216;
            } else {
               ++var4;
            }

            if (var1[var3++] != 0) {
               var0[var4++] = var2 | -16777216;
            } else {
               ++var4;
            }
         }

         for(var12 = var10; var12 < 0; ++var12) {
            if (var1[var3++] != 0) {
               var0[var4++] = var2 | -16777216;
            } else {
               ++var4;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   public static void a(byte[] var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = aU.f * var2 + var1;
      int var8 = aU.f - var3;
      int var9 = 0;
      int var10 = 0;
      int var11;
      if (var2 < a) {
         var11 = a - var2;
         var4 -= var11;
         var2 = a;
         var10 += var3 * var11;
         var7 += aU.f * var11;
      }

      if (var2 + var4 > b) {
         var4 -= var2 + var4 - b;
      }

      if (var1 < c) {
         var11 = c - var1;
         var3 -= var11;
         var1 = c;
         var10 += var11;
         var7 += var11;
         var9 += var11;
         var8 += var11;
      }

      if (var1 + var3 > d) {
         var11 = var1 + var3 - d;
         var3 -= var11;
         var9 += var11;
         var8 += var11;
      }

      if (var3 > 0 && var4 > 0) {
         a(aU.h, var0, var5, var10, var7, var3, var4, var8, var9, var6);
      }

   }

   public static void a(int[] var0, byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      int var10 = ((var2 & '\uff00') * var9 & 16711680) + ((var2 & 16711935) * var9 & -16711936) >> 8;
      int var11 = 256 - var9;

      for(int var12 = -var6; var12 < 0; ++var12) {
         for(int var13 = -var5; var13 < 0; ++var13) {
            if (var1[var3++] != 0) {
               int var14 = var0[var4];
               Client.a(var0, var4++, (((var14 & '\uff00') * var11 & 16711680) + ((var14 & 16711935) * var11 & -16711936) >> 8) + var10, 256 - var11);
            } else {
               ++var4;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   public void a(byte[] var1) {
      this.t = new int[256];
      int var2;
      if (var1.length == 257) {
         for(var2 = 0; var2 < this.t.length; ++var2) {
            this.t[var2] = var1[var2] & 255;
         }

         this.m = var1[256] & 255;
      } else {
         var2 = 0;

         for(int var3 = 0; var3 < 256; ++var3) {
            this.t[var3] = var1[var2++] & 255;
         }

         int[] var10 = new int[256];
         int[] var4 = new int[256];

         int var5;
         for(var5 = 0; var5 < 256; ++var5) {
            var10[var5] = var1[var2++] & 255;
         }

         for(var5 = 0; var5 < 256; ++var5) {
            var4[var5] = var1[var2++] & 255;
         }

         byte[][] var11 = new byte[256][];

         int var7;
         int var8;
         for(int var6 = 0; var6 < 256; ++var6) {
            var11[var6] = new byte[var10[var6]];
            var7 = 0;

            for(var8 = 0; var8 < var11[var6].length; ++var8) {
               var7 += var1[var2++];
               var11[var6][var8] = (byte)var7;
            }
         }

         byte[][] var12 = new byte[256][];

         for(var7 = 0; var7 < 256; ++var7) {
            var12[var7] = new byte[var10[var7]];
            byte var13 = 0;

            for(int var9 = 0; var9 < var12[var7].length; ++var9) {
               var13 += var1[var2++];
               var12[var7][var9] = var13;
            }
         }

         this.u = new byte[65536];

         for(var7 = 0; var7 < 256; ++var7) {
            if (var7 != 32 && var7 != 160) {
               for(var8 = 0; var8 < 256; ++var8) {
                  if (var8 != 32 && var8 != 160) {
                     this.u[(var7 << 8) + var8] = (byte)a(var11, var12, var4, this.t, var10, var7, var8);
                  }
               }
            }
         }

         this.m = var10[32] + var4[32];
      }

   }

   public int a(char var1) {
      if (var1 == 160) {
         var1 = ' ';
      }

      return this.t[gc.a(var1) & 255];
   }

   public int b(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = -1;
         int var3 = -1;
         int var4 = 0;

         for(int var5 = 0; var5 < var1.length(); ++var5) {
            char var6 = var1.charAt(var5);
            if (var6 == '<') {
               var2 = var5;
            } else {
               if (var6 == '>' && var2 != -1) {
                  String var7 = var1.substring(var2 + 1, var5);
                  var2 = -1;
                  if (var7.equals("lt")) {
                     var6 = '<';
                  } else {
                     if (!var7.equals("gt")) {
                        if (var7.startsWith("img=")) {
                           try {
                              int var8 = br.c(var7.substring(4));
                              var4 += E[var8].j;
                              var3 = -1;
                           } catch (Exception var9) {
                           }
                        }
                        continue;
                     }

                     var6 = '>';
                  }
               }

               if (var6 == 160) {
                  var6 = ' ';
               }

               if (var2 == -1) {
                  var4 += this.t[(char)(gc.a(var6) & 255)];
                  if (this.u != null && var3 != -1) {
                     var4 += this.u[(var3 << 8) + var6];
                  }

                  var3 = var6;
               }
            }
         }

         return var4;
      }
   }

   public int a(String var1, int var2) {
      int var3 = this.a(var1, new int[]{var2}, k);
      int var4 = 0;

      for(int var5 = 0; var5 < var3; ++var5) {
         int var6 = this.b(k[var5]);
         if (var6 > var4) {
            var4 = var6;
         }
      }

      return var4;
   }

   public int b(String var1, int var2) {
      return this.a(var1, new int[]{var2}, k);
   }

   public ba a(int var1, int var2, String var3, int var4, int var5) {
      if (var3 != null && var3.length() >= var1 + var2) {
         int var6 = var4 - this.b(var3) / 2;
         int var7 = var6 + this.b(var3.substring(0, var1));
         int var8 = var5 - this.n;
         int var9 = this.b(var3.substring(var1, var1 + var2));
         int var10 = this.l + this.n;
         return new ba(var7, var8, var9, var10);
      } else {
         return new ba(var4, var5, 0, 0);
      }
   }

   public void a(String var1, int var2, int var3, int var4, int var5) {
      if (var1 != null) {
         this.a(var4, var5);
         this.a(var1, var2, var3);
      }

   }

   public void a(String var1, int var2, int var3, int var4, int var5, int var6) {
      if (var1 != null) {
         this.a(var4, var5);
         C = var6;
         this.a(var1, var2, var3);
      }

   }

   public void b(String var1, int var2, int var3, int var4, int var5) {
      if (var1 != null) {
         this.a(var4, var5);
         this.a(var1, var2 - this.b(var1), var3);
      }

   }

   public void c(String var1, int var2, int var3, int var4, int var5) {
      if (var1 != null) {
         this.a(var4, var5);
         this.a(var1, var2 - this.b(var1) / 2, var3);
      }

   }

   public int a(String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      return this.a(var1, var2, var3, var4, var5, var6, var7, 256, var8, var9, var10);
   }

   public int a(String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      if (var1 == null) {
         return 0;
      } else {
         if (var8 == 255) {
            var8 = 256;
         }

         this.a(var6, var7);
         C = var8;
         if (var11 == 0) {
            var11 = this.m;
         }

         int[] var12 = new int[]{var4};
         if (var5 < this.l + this.n + var11 && var5 < var11 + var11) {
            var12 = null;
         }

         int var13 = this.a(var1, var12, k);
         if (var10 == 3 && var13 == 1) {
            var10 = 1;
         }

         int var14;
         int var15;
         if (var10 == 0) {
            var14 = this.n + var3;
         } else if (var10 == 1) {
            var14 = (var5 - this.n - this.l - (var13 - 1) * var11) / 2 + this.n + var3;
         } else if (var10 == 2) {
            var14 = var3 + var5 - this.l - (var13 - 1) * var11;
         } else {
            var15 = (var5 - this.n - this.l - (var13 - 1) * var11) / (var13 + 1);
            if (var15 < 0) {
               var15 = 0;
            }

            var14 = this.n + var3 + var15;
            var11 += var15;
         }

         for(var15 = 0; var15 < var13; ++var15) {
            if (var9 == 0) {
               this.a(k[var15], var2, var14);
            } else if (var9 == 1) {
               this.a(k[var15], var2 + (var4 - this.b(k[var15])) / 2, var14);
            } else if (var9 == 2) {
               this.a(k[var15], var2 + var4 - this.b(k[var15]), var14);
            } else if (var13 - 1 == var15) {
               this.a(k[var15], var2, var14);
            } else {
               this.c(k[var15], var4);
               this.a(k[var15], var2, var14);
               D = 0;
            }

            var14 += var11;
         }

         return var13;
      }
   }

   public aS a(by var1, int var2, int var3, int var4, int var5, int var6) {
      if (!var1.b()) {
         this.a(var4, var5);
         int var7 = var3 - this.m;

         for(int var8 = 0; var8 < var1.c(); ++var8) {
            bx var9 = var1.b(var8);
            if (var6 != -1 && var9.b > var6) {
               return new aS(var9.a, var9.b);
            }

            char var10 = var9.c;
            if (var10 != '\n') {
               if (var1.a(var8)) {
                  var10 = '*';
               }

               if (var10 != '\t') {
                  if (var10 == 160) {
                     var10 = ' ';
                  }

                  int var11 = var9.a + var2;
                  int var12 = var9.b + var7;
                  int var13 = this.s[var10];
                  int var14 = this.p[var10];
                  if (z != -1) {
                     this.b(this.o[var10], this.r[var10] + var11 + 1, this.q[var10] + var12 + 1, var13, var14, z);
                  }

                  this.b(this.o[var10], this.r[var10] + var11, this.q[var10] + var12, var13, var14, B);
               }
            }
         }
      }

      return var1.e();
   }

   public void a(String var1, int var2, int var3, int var4, int var5, int var6, int[] var7) {
      if (var1 != null) {
         this.a(var4, var5);
         int[] var8 = null;
         if (var7 != null) {
            var8 = this.a(var7, var1.length());
         }

         int[] var9 = new int[var1.length()];

         for(int var10 = 0; var10 < var1.length(); ++var10) {
            var9[var10] = (int)(Math.sin((double)var6 / 5.0 + (double)var10 / 2.0) * 5.0);
         }

         this.a(var1, var2 - this.b(var1) / 2, var3, var8, (int[])null, var9);
      }

   }

   public void b(String var1, int var2, int var3, int var4, int var5, int var6, int[] var7) {
      if (var1 != null) {
         this.a(var4, var5);
         int[] var8 = null;
         if (var7 != null) {
            var8 = this.a(var7, var1.length());
         }

         int[] var9 = new int[var1.length()];
         int[] var10 = new int[var1.length()];

         for(int var11 = 0; var11 < var1.length(); ++var11) {
            var9[var11] = (int)(Math.sin((double)var6 / 5.0 + (double)var11 / 5.0) * 5.0);
            var10[var11] = (int)(Math.sin((double)var6 / 5.0 + (double)var11 / 3.0) * 5.0);
         }

         this.a(var1, var2 - this.b(var1) / 2, var3, var8, var9, var10);
      }

   }

   public void a(String var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var8) {
      if (var1 != null) {
         this.a(var4, var5);
         int[] var9 = null;
         if (var8 != null) {
            var9 = this.a(var8, var1.length());
         }

         double var10 = 7.0 - (double)var7 / 8.0;
         if (var10 < 0.0) {
            var10 = 0.0;
         }

         int[] var12 = new int[var1.length()];

         for(int var13 = 0; var13 < var1.length(); ++var13) {
            var12[var13] = (int)(Math.sin((double)var6 / 1.0 + (double)var13 / 1.5) * var10);
         }

         this.a(var1, var2 - this.b(var1) / 2, var3, var9, (int[])null, var12);
      }

   }

   public void a(String var1, int var2, int var3, int var4, int var5, int[] var6) {
      if (var1 != null) {
         this.a(var4, var5);
         int[] var7 = null;
         if (var6 != null) {
            var7 = this.a(var6, var1.length());
         }

         this.a(var1, var2 - this.b(var1) / 2, var3, var7, (int[])null, (int[])null);
      }

   }

   public void b(String var1, int var2, int var3, int var4, int var5, int[] var6) {
      if (var1 != null) {
         this.a(var4, var5);
         int[] var7 = null;
         if (var6 != null) {
            var7 = this.a(var6, var1.length());
         }

         this.a(var1, var2, var3, var7, (int[])null, (int[])null);
      }

   }

   public int[] a(int[] var1, int var2) {
      if (var2 == 0) {
         return null;
      } else {
         int[] var3 = new int[var2];
         float var4 = (float)var1.length / (float)var2;

         for(int var5 = 0; var5 < var2; ++var5) {
            var3[var5] = var1[(int)((float)var5 * var4)];
         }

         return var3;
      }
   }

   public void b(String var1, int var2, int var3, int var4, int var5, int var6) {
      if (var1 != null) {
         this.a(var4, var5);
         j.setSeed((long)var6);
         C = 192 + (j.nextInt() & 31);
         int[] var7 = new int[var1.length()];
         int var8 = 0;

         for(int var9 = 0; var9 < var1.length(); ++var9) {
            var7[var9] = var8;
            if ((j.nextInt() & 3) == 0) {
               ++var8;
            }
         }

         this.a(var1, var2, var3, (int[])null, var7, (int[])null);
      }

   }

   public void a(int var1, int var2) {
      w = -1;
      x = -1;
      y = var2;
      z = var2;
      A = var1;
      B = var1;
      C = 256;
      D = 0;
      i = 0;
   }

   public void c(String var1) {
      try {
         if (var1.startsWith("col=")) {
            B = br.a(var1.substring(4), 16);
         } else if (var1.equals("/col")) {
            B = A;
         } else if (var1.startsWith("str=")) {
            w = br.a(var1.substring(4), 16);
         } else if (var1.equals("str")) {
            w = 8388608;
         } else if (var1.equals("/str")) {
            w = -1;
         } else if (var1.startsWith("u=")) {
            x = br.a(var1.substring(2), 16);
         } else if (var1.equals("u")) {
            x = 0;
         } else if (var1.equals("/u")) {
            x = -1;
         } else if (var1.startsWith("shad=")) {
            z = br.a(var1.substring(5), 16);
         } else if (var1.equals("shad")) {
            z = 0;
         } else if (var1.equals("/shad")) {
            z = y;
         } else if (var1.equals("br")) {
            this.a(A, y);
         }
      } catch (Exception var3) {
      }

   }

   public void c(String var1, int var2) {
      int var3 = 0;
      boolean var4 = false;

      for(int var5 = 0; var5 < var1.length(); ++var5) {
         char var6 = var1.charAt(var5);
         if (var6 == '<') {
            var4 = true;
         } else if (var6 == '>') {
            var4 = false;
         } else if (!var4 && var6 == ' ') {
            ++var3;
         }
      }

      if (var3 > 0) {
         D = (var2 - this.b(var1) << 8) / var3;
      }

   }

   public void a(String var1, int var2, int var3) {
      int var4 = var3 - this.m;
      int var5 = -1;
      int var6 = -1;

      for(int var7 = 0; var7 < var1.length(); ++var7) {
         if (var1.charAt(var7) != 0) {
            char var8 = (char)(gc.a(var1.charAt(var7)) & 255);
            if (var8 == '<') {
               var5 = var7;
            } else {
               int var10;
               if (var8 == '>' && var5 != -1) {
                  String var9 = var1.substring(var5 + 1, var7);
                  var5 = -1;
                  if (var9.equals("lt")) {
                     var8 = '<';
                  } else {
                     if (!var9.equals("gt")) {
                        if (var9.startsWith("img=")) {
                           try {
                              var10 = br.c(var9.substring(4));
                              hX var14 = E[var10];
                              var14.a(var2, this.m + var4 - var14.n);
                              var2 += var14.j;
                              var6 = -1;
                           } catch (Exception var12) {
                           }
                        } else {
                           this.c(var9);
                        }
                        continue;
                     }

                     var8 = '>';
                  }
               }

               if (var8 == 160) {
                  var8 = ' ';
               }

               if (var5 == -1) {
                  if (this.u != null && var6 != -1) {
                     var2 += this.u[(var6 << 8) + var8];
                  }

                  int var13 = this.s[var8];
                  var10 = this.p[var8];
                  if (var8 != ' ') {
                     if (C == 256) {
                        if (z != -1) {
                           a(this.o[var8], this.r[var8] + var2 + 1, this.q[var8] + var4 + 1, var13, var10, z);
                        }

                        this.b(this.o[var8], this.r[var8] + var2, this.q[var8] + var4, var13, var10, B);
                     } else {
                        if (z != -1) {
                           a(this.o[var8], this.r[var8] + var2 + 1, this.q[var8] + var4 + 1, var13, var10, z, C);
                        }

                        this.b(this.o[var8], this.r[var8] + var2, this.q[var8] + var4, var13, var10, B, C);
                     }
                  } else if (D > 0) {
                     i += D;
                     var2 += i >> 8;
                     i &= 255;
                  }

                  int var11 = this.t[var8];
                  if (w != -1) {
                     d(var2, (int)((double)this.m * 0.7) + var4, var11, w);
                  }

                  if (x != -1) {
                     d(var2, this.m + var4 + 1, var11, x);
                  }

                  var2 += var11;
                  var6 = var8;
               }
            }
         }
      }

   }

   public void a(String var1, int var2, int var3, int[] var4, int[] var5, int[] var6) {
      int var7 = var3 - this.m;
      int var8 = -1;
      int var9 = -1;
      int var10 = 0;

      for(int var11 = 0; var11 < var1.length(); ++var11) {
         if (var1.charAt(var11) != 0) {
            char var12 = (char)(gc.a(var1.charAt(var11)) & 255);
            if (var12 == '<') {
               var8 = var11;
            } else {
               int var14;
               int var15;
               int var16;
               if (var12 == '>' && var8 != -1) {
                  String var13 = var1.substring(var8 + 1, var11);
                  var8 = -1;
                  if (var13.equals("lt")) {
                     var12 = '<';
                  } else {
                     if (!var13.equals("gt")) {
                        if (var13.startsWith("img=")) {
                           try {
                              if (var5 != null) {
                                 var14 = var5[var10];
                              } else {
                                 var14 = 0;
                              }

                              if (var6 != null) {
                                 var15 = var6[var10];
                              } else {
                                 var15 = 0;
                              }

                              ++var10;
                              var16 = br.c(var13.substring(4));
                              hX var21 = E[var16];
                              var21.a(var2 + var14, this.m + var7 - var21.n + var15);
                              var2 += var21.j;
                              var9 = -1;
                           } catch (Exception var19) {
                           }
                        } else {
                           this.c(var13);
                        }
                        continue;
                     }

                     var12 = '>';
                  }
               }

               if (var12 == 160) {
                  var12 = ' ';
               }

               if (var8 == -1) {
                  if (this.u != null && var9 != -1) {
                     var2 += this.u[(var9 << 8) + var12];
                  }

                  int var20 = this.s[var12];
                  var14 = this.p[var12];
                  if (var5 != null) {
                     var15 = var5[var10];
                  } else {
                     var15 = 0;
                  }

                  if (var6 != null) {
                     var16 = var6[var10];
                  } else {
                     var16 = 0;
                  }

                  int var17;
                  if (var4 != null) {
                     var17 = var4[var10];
                  } else {
                     var17 = B;
                  }

                  ++var10;
                  if (var12 != ' ') {
                     if (C == 256) {
                        if (z != -1) {
                           a(this.o[var12], this.r[var12] + var2 + 1 + var15, this.q[var12] + var7 + 1 + var16, var20, var14, z);
                        }

                        this.b(this.o[var12], this.r[var12] + var2 + var15, this.q[var12] + var7 + var16, var20, var14, var17);
                     } else {
                        if (z != -1) {
                           a(this.o[var12], this.r[var12] + var2 + 1 + var15, this.q[var12] + var7 + 1 + var16, var20, var14, z, C);
                        }

                        this.b(this.o[var12], this.r[var12] + var2 + var15, this.q[var12] + var7 + var16, var20, var14, var17, C);
                     }
                  } else if (D > 0) {
                     i += D;
                     var2 += i >> 8;
                     i &= 255;
                  }

                  int var18 = this.t[var12];
                  if (w != -1) {
                     d(var2, (int)((double)this.m * 0.7) + var7, var18, w);
                  }

                  if (x != -1) {
                     d(var2, this.m + var7, var18, x);
                  }

                  var2 += var18;
                  var9 = var12;
               }
            }
         }
      }

   }

   public int a(String var1, int[] var2, String[] var3) {
      if (var1 == null) {
         return 0;
      } else {
         int var4 = 0;
         int var5 = 0;
         int var6 = -1;
         int var7 = 0;
         byte var8 = 0;
         int var9 = -1;
         char var10 = 0;
         int var11 = 0;
         int var12 = var1.length();
         v.setLength(0);

         for(int var13 = 0; var13 < var12; ++var13) {
            char var14 = var1.charAt(var13);
            if (var14 == '<') {
               var9 = var13;
            } else {
               if (var14 == '>' && var9 != -1) {
                  String var15 = var1.substring(var9 + 1, var13);
                  var9 = -1;
                  v.append('<');
                  v.append(var15);
                  v.append('>');
                  if (var15.equals("br")) {
                     var3[var11++] = v.substring(var5);
                     v.setLength(0);
                     var5 = 0;
                     var4 = 0;
                     var6 = -1;
                     var10 = 0;
                  } else if (var15.equals("lt")) {
                     var4 += this.b('<');
                     if (this.u != null && var10 != -1) {
                        var4 += this.u[(var10 << 8) + 60];
                     }

                     var10 = '<';
                  } else if (var15.equals("gt")) {
                     var4 += this.b('>');
                     if (this.u != null && var10 != -1) {
                        var4 += this.u[(var10 << 8) + 62];
                     }

                     var10 = '>';
                  } else if (var15.startsWith("img=")) {
                     try {
                        hX[] var16 = Client.s.bp();
                        int var17 = Integer.parseInt(var15.substring(4));
                        var4 += var16[var17].getOriginalWidth();
                        var10 = 0;
                     } catch (Exception var18) {
                     }
                  }

                  var14 = 0;
               }

               if (var9 == -1) {
                  if (var14 != 0) {
                     v.append(var14);
                     var4 += this.b(var14);
                     if (this.u != null && var10 != -1) {
                        var4 += this.u[(var10 << 8) + var14];
                     }

                     var10 = var14;
                  }

                  if (var14 == ' ') {
                     var6 = v.length();
                     var7 = var4;
                     var8 = 1;
                  }

                  if (var2 != null && var4 > var2[var11 < var2.length ? var11 : var2.length - 1] && var6 >= 0) {
                     var3[var11++] = v.substring(var5, var6 - var8);
                     var5 = var6;
                     var6 = -1;
                     var4 -= var7;
                     var10 = 0;
                  }

                  if (var14 == '-') {
                     var6 = v.length();
                     var7 = var4;
                     var8 = 0;
                  }
               }
            }
         }

         if (v.length() > var5) {
            var3[var11++] = v.substring(var5);
         }

         return var11;
      }
   }

   public int b(char var1) {
      return this.a(var1);
   }

   public abstract void b(byte[] var1, int var2, int var3, int var4, int var5, int var6);

   public abstract void b(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

   public void drawWidgetText(String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      this.a(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
   }

   public int getBaseline() {
      return this.m;
   }

   public void d(String var1, int var2, int var3, int var4, int var5) {
      this.a(var1, var2, var3, var4, var5);
   }

   public int getTextWidth(String var1) {
      return this.b(var1);
   }
}
