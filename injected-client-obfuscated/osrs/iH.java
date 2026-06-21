package osrs;

import java.util.ArrayList;
import java.util.Iterator;
import net.runelite.api.events.PlayerDespawned;

public class iH {
   public final int[] a = new int[2048];
   public final fM[] b = new fM[2048];
   public final boolean[] c = new boolean[2048];
   public final aJ[] d = new aJ[2048];
   public final aR e = new aR(new byte[5000]);
   public final String[] f = new String[3];
   public final int[] g = new int[2048];
   public final int[] h = new int[2048];
   public final int[] i = new int[2048];
   public int j = 0;
   public int k = 0;
   public int l = 0;
   public int m = 0;

   public iH() {
      for(int var1 = 0; var1 < 2048; ++var1) {
         this.b[var1] = new fM(var1);
      }

   }

   public jR a(int var1) {
      return var1 >= 0 && var1 < this.b.length ? this.b[var1] : null;
   }

   public final void a(bu var1) {
      this.d();
      var1.am();
      int var2 = Client.P;
      fM var3 = this.b[var2];
      int var4 = var1.y(30);
      var3.a(-1);
      var3.c.c(var4);
      var3.i = 0;
      this.j = 0;
      this.a[++this.j - 1] = var2;
      this.k = 0;

      for(int var5 = 1; var5 < 2048; ++var5) {
         if (var2 != var5) {
            int var6 = var1.y(18);
            int var7 = var6 >> 16;
            int var8 = var6 >> 8 & 255;
            int var9 = var6 & 255;
            this.b[var5].a(B.a(var7, var8, var9));
            this.g[++this.k - 1] = var5;
         }
      }

      var1.an();
   }

   public final void a(bu var1, int var2) {
      this.e(var1, var2);
      int var3 = var1.d;
      this.m = 0;
      this.b(var1);
      this.a();
      this.b();
      this.d(var1);
      this.c();
      if (var1.d - var3 != var2) {
         int var10002 = var1.d - var3;
         throw new RuntimeException("" + var10002 + " " + var2);
      } else {
         this.d(var1, var2);
      }
   }

   public void b(bu var1) {
      this.l = 0;
      int var2 = 0;
      var1.am();

      fM var3;
      int var4;
      int var5;
      int var6;
      for(var4 = 0; var4 < this.j; ++var4) {
         var5 = this.a[var4];
         if ((this.b[var5].i & 1) == 0) {
            if (var2 > 0) {
               --var2;
               var3 = this.b[var5];
               var3.i = (byte)(var3.i | 2);
            } else {
               var6 = var1.y(1);
               if (var6 == 0) {
                  var2 = this.c(var1);
                  var3 = this.b[var5];
                  var3.i = (byte)(var3.i | 2);
               } else {
                  this.b(var1, var5);
               }
            }
         }
      }

      var1.an();
      if (var2 != 0) {
         throw new RuntimeException();
      } else {
         var1.am();

         for(var4 = 0; var4 < this.j; ++var4) {
            var5 = this.a[var4];
            if ((this.b[var5].i & 1) != 0) {
               if (var2 > 0) {
                  --var2;
                  var3 = this.b[var5];
                  var3.i = (byte)(var3.i | 2);
               } else {
                  var6 = var1.y(1);
                  if (var6 == 0) {
                     var2 = this.c(var1);
                     var3 = this.b[var5];
                     var3.i = (byte)(var3.i | 2);
                  } else {
                     this.b(var1, var5);
                  }
               }
            }
         }

         var1.an();
         if (var2 != 0) {
            throw new RuntimeException();
         } else {
            var1.am();

            for(var4 = 0; var4 < this.k; ++var4) {
               var5 = this.g[var4];
               if ((this.b[var5].i & 1) != 0) {
                  if (var2 > 0) {
                     --var2;
                     var3 = this.b[var5];
                     var3.i = (byte)(var3.i | 2);
                  } else {
                     var6 = var1.y(1);
                     if (var6 == 0) {
                        var2 = this.c(var1);
                        var3 = this.b[var5];
                        var3.i = (byte)(var3.i | 2);
                     } else if (this.c(var1, var5)) {
                        var3 = this.b[var5];
                        var3.i = (byte)(var3.i | 2);
                     }
                  }
               }
            }

            var1.an();
            if (var2 != 0) {
               throw new RuntimeException();
            } else {
               var1.am();

               for(var4 = 0; var4 < this.k; ++var4) {
                  var5 = this.g[var4];
                  if ((this.b[var5].i & 1) == 0) {
                     if (var2 > 0) {
                        --var2;
                        var3 = this.b[var5];
                        var3.i = (byte)(var3.i | 2);
                     } else {
                        var6 = var1.y(1);
                        if (var6 == 0) {
                           var2 = this.c(var1);
                           var3 = this.b[var5];
                           var3.i = (byte)(var3.i | 2);
                        } else if (this.c(var1, var5)) {
                           var3 = this.b[var5];
                           var3.i = (byte)(var3.i | 2);
                        }
                     }
                  }
               }

               var1.an();
               if (var2 != 0) {
                  throw new RuntimeException();
               } else {
                  this.j = 0;
                  this.k = 0;

                  for(var4 = 1; var4 < 2048; ++var4) {
                     var3 = this.b[var4];
                     var3.i = (byte)(var3.i >> 1);
                     if (this.b[var4].c()) {
                        this.a[++this.j - 1] = var4;
                     } else {
                        this.g[++this.k - 1] = var4;
                     }
                  }

               }
            }
         }
      }
   }

   public int c(bu var1) {
      int var2 = var1.y(2);
      int var3;
      if (var2 == 0) {
         var3 = 0;
      } else if (var2 == 1) {
         var3 = var1.y(5);
      } else if (var2 == 2) {
         var3 = var1.y(8);
      } else {
         var3 = var1.y(11);
      }

      return var3;
   }

   public void b(bu var1, int var2) {
      boolean var3 = var1.y(1) == 1;
      if (var3) {
         this.i[++this.m - 1] = var2;
      }

      int var4 = var1.y(2);
      fM var5 = this.b[var2];
      if (var4 == 0) {
         if (var3) {
            this.c[var2] = false;
         } else {
            if (Client.P == var2) {
               throw new RuntimeException();
            }

            var5.d();
            if (var1.y(1) != 0) {
               this.c(var1, var2);
            }

            this.h[++this.l - 1] = var2;
         }
      } else {
         int var6;
         B var7;
         if (var4 == 1) {
            var6 = var1.y(3);
            var7 = var5.c;
            if (var6 == 0) {
               --var7.b;
               --var7.c;
            } else if (var6 == 1) {
               --var7.c;
            } else if (var6 == 2) {
               ++var7.b;
               --var7.c;
            } else if (var6 == 3) {
               --var7.b;
            } else if (var6 == 4) {
               ++var7.b;
            } else if (var6 == 5) {
               --var7.b;
               ++var7.c;
            } else if (var6 == 6) {
               ++var7.c;
            } else if (var6 == 7) {
               ++var7.b;
               ++var7.c;
            }

            this.c[var2] = true;
            this.d[var2] = var5.j;
         } else if (var4 == 2) {
            var6 = var1.y(4);
            var7 = var5.c;
            if (var6 == 0) {
               var7.b -= 2;
               var7.c -= 2;
            } else if (var6 == 1) {
               --var7.b;
               var7.c -= 2;
            } else if (var6 == 2) {
               var7.c -= 2;
            } else if (var6 == 3) {
               ++var7.b;
               var7.c -= 2;
            } else if (var6 == 4) {
               var7.b += 2;
               var7.c -= 2;
            } else if (var6 == 5) {
               var7.b -= 2;
               --var7.c;
            } else if (var6 == 6) {
               var7.b += 2;
               --var7.c;
            } else if (var6 == 7) {
               var7.b -= 2;
            } else if (var6 == 8) {
               var7.b += 2;
            } else if (var6 == 9) {
               var7.b -= 2;
               ++var7.c;
            } else if (var6 == 10) {
               var7.b += 2;
               ++var7.c;
            } else if (var6 == 11) {
               var7.b -= 2;
               var7.c += 2;
            } else if (var6 == 12) {
               --var7.b;
               var7.c += 2;
            } else if (var6 == 13) {
               var7.c += 2;
            } else if (var6 == 14) {
               ++var7.b;
               var7.c += 2;
            } else if (var6 == 15) {
               var7.b += 2;
               var7.c += 2;
            }

            this.c[var2] = true;
            this.d[var2] = var5.j;
         } else {
            var6 = var1.y(1);
            int var8;
            int var9;
            int var10;
            B var11;
            int var12;
            if (var6 == 0) {
               var12 = var1.y(12);
               var8 = var12 >> 10;
               var9 = var12 >> 5 & 31;
               if (var9 > 15) {
                  var9 -= 32;
               }

               var10 = var12 & 31;
               if (var10 > 15) {
                  var10 -= 32;
               }

               var11 = var5.c;
               var11.a = (byte)(var11.a + var8 & 3);
               var11.b += var9;
               var11.c += var10;
               this.c[var2] = true;
               this.d[var2] = var5.j;
            } else {
               var12 = var1.y(30);
               var8 = bo.b(var12);
               var9 = bo.c(var12);
               var10 = bo.d(var12);
               var11 = var5.c;
               var11.a = (byte)(var11.a + var8 & 3);
               var11.b = var11.b + var9 & 16383;
               var11.c = var11.c + var10 & 16383;
               this.c[var2] = true;
               this.d[var2] = var5.j;
            }
         }
      }

   }

   public boolean c(bu var1, int var2) {
      fM var3 = this.b[var2];
      int var4 = var1.y(2);
      int var5;
      int var6;
      if (var4 == 0) {
         if (var1.y(1) != 0) {
            this.c(var1, var2);
         }

         var5 = var1.y(13);
         var6 = var1.y(13);
         boolean var13 = var1.y(1) == 1;
         if (var13) {
            this.i[++this.m - 1] = var2;
         }

         if (var3.c()) {
            throw new RuntimeException();
         } else {
            var3.a(var5, var6);
            this.c[var2] = false;
            return true;
         }
      } else {
         int var7;
         if (var4 == 1) {
            var5 = var1.y(2);
            var6 = var3.b;
            var7 = bo.b(var6) + var5 & 3;
            var3.b(var7);
            var3.b = (var7 << 28) + (var6 & 268435455);
            return false;
         } else {
            int var8;
            int var9;
            int var10;
            int var11;
            if (var4 == 2) {
               var5 = var1.y(5);
               var6 = var5 >> 3;
               var7 = var5 & 7;
               var8 = var3.b;
               var9 = bo.b(var8) + var6 & 3;
               var10 = var8 >> 14 & 255;
               var11 = var8 & 255;
               if (var7 == 0) {
                  --var10;
                  --var11;
               }

               if (var7 == 1) {
                  --var11;
               }

               if (var7 == 2) {
                  ++var10;
                  --var11;
               }

               if (var7 == 3) {
                  --var10;
               }

               if (var7 == 4) {
                  ++var10;
               }

               if (var7 == 5) {
                  --var10;
                  ++var11;
               }

               if (var7 == 6) {
                  ++var11;
               }

               if (var7 == 7) {
                  ++var10;
                  ++var11;
               }

               var3.b(var9);
               var3.b = B.a(var9, var10, var11);
               return false;
            } else {
               var5 = var1.y(18);
               var6 = var5 >> 16;
               var7 = var5 >> 8 & 255;
               var8 = var5 & 255;
               var9 = var3.b;
               var10 = bo.b(var9) + var6 & 3;
               var3.b(var10);
               var11 = bo.c(var9) + var7 & 255;
               int var12 = bo.d(var9) + var8 & 255;
               var3.b = B.a(var3.g(), var11, var12);
               return false;
            }
         }
      }
   }

   public void a() {
      for(int var1 = 0; var1 < this.l; ++var1) {
         int var2 = this.h[var1];
         fM var3 = this.b[var2];
         Iterator var4 = Client.D.iterator();

         while(var4.hasNext()) {
            bG var5 = (bG)var4.next();
            t var6 = (t)var5.r.a((long)var2);
            if (var6 != null) {
               var3.b(var6);
               var6.aj();
            }
         }

         var3.b();
      }

   }

   public void b() {
      Client.Q = 0;
      Iterator var1 = Client.D.iterator();

      while(var1.hasNext()) {
         bG var2 = (bG)var1.next();
         this.a(var2);
      }

   }

   public void a(bG var1) {
      for(int var2 = 0; var2 < this.j; ++var2) {
         int var3 = this.a[var2];
         fM var4 = this.b[var3];
         B var5 = var4.c;
         t var6 = (t)var1.r.a((long)var3);
         boolean var7 = var5.b > var1.B && var5.c > var1.z && var5.b < var1.B + var1.p && var5.c < var1.z + var1.q;
         if (Client.P == var3 && var7 && var1.o != 0) {
            Client.Q = var1.o;
         }

         if (var7 && var6 == null) {
            t var8 = var4.a(var3, var1);
            var4.a(var8);
            var1.r.a(var8, (long)var3);
         } else if (!var7 && var6 != null) {
            var4.b(var6);
            var6.aj();
         }
      }

   }

   public void d(bu var1) {
      for(int var2 = 0; var2 < this.m; ++var2) {
         int var3 = this.i[var2];
         int var4 = ((aR)var1).b();
         if ((var4 & 16) != 0) {
            var4 += ((aR)var1).b() << 8;
         }

         if ((var4 & 256) != 0) {
            var4 += ((aR)var1).b() << 16;
         }

         this.a(var1, var3, var4);
      }

   }

   public void c() {
      for(int var1 = 0; var1 < this.j; ++var1) {
         int var2 = this.a[var1];
         if (this.c[var2]) {
            fM var3 = this.b[var2];
            var3.a(this.d[var2]);
            this.c[var2] = false;
         }
      }

   }

   public void a(bu var1, int var2, int var3) {
      fM var4 = this.b[var2];
      int var5 = Integer.MAX_VALUE;
      int var6;
      if ((var3 & 2) != 0) {
         var6 = ((aR)var1).b();
         byte[] var7 = new byte[var6];
         aR var8 = new aR(var7);
         ((aR)var1).c(var7, 0, var6);
         var4.a(var8);
      }

      int var9;
      int var10;
      int var11;
      int var12;
      int var17;
      int var19;
      if ((var3 & 8) != 0) {
         var6 = ((aR)var1).A();
         if (var6 > 0) {
            for(var17 = 0; var17 < var6; ++var17) {
               var19 = -1;
               var9 = var1.q();
               if (var9 == 32767) {
                  var9 = var1.q();
                  var19 = var1.q();
                  var1.q();
                  var1.q();
               } else if (var9 != 32766) {
                  var19 = var1.q();
               } else {
                  var9 = -1;
               }

               var10 = var1.q();
               var4.a(var9, var19, var10, 4);
            }
         }

         var17 = ((aR)var1).A();
         if (var17 > 0) {
            for(var19 = 0; var19 < var17; ++var19) {
               var9 = var1.q();
               var10 = var1.q();
               if (var10 != 32767) {
                  var11 = var1.q();
                  var12 = var1.B();
                  int var13 = var10 > 0 ? ((aR)var1).A() : var12;
                  var4.a(var9, var10, var11, var12, var13);
               } else {
                  var4.d(var9);
               }
            }
         }
      }

      aJ[] var15;
      if ((var3 & 2048) != 0) {
         var5 = var1.c();
         if (var5 == 127) {
            this.d[var2] = aJ.a;
         } else {
            var15 = this.d;
            aJ[] var18 = new aJ[]{aJ.d, aJ.c, aJ.b, aJ.a};
            var15[var2] = (aJ)kk.a(var18, var5);
         }
      }

      byte var21;
      byte var22;
      if ((var3 & 512) != 0) {
         byte var16 = var1.C();
         byte var20 = var1.c();
         var21 = var1.c();
         var22 = var1.D();
         var10 = var1.d() + Client.x;
         var11 = var1.G() + Client.x;
         var12 = var1.H();
         var4.a(var16, var20, var21, var22, var10, var11, var12);
         this.c[var2] = false;
      }

      if ((var3 & 64) != 0) {
         var6 = var1.F();
         if (var6 == 65535) {
            var6 = -1;
         }

         var17 = var1.z();
         var4.b(var6, var17);
      }

      if ((var3 & 65536) != 0) {
         var6 = ((aR)var1).b();
         if (var6 > 0) {
            for(var17 = 0; var17 < var6; ++var17) {
               var19 = var1.q();
               var9 = var1.q();
               if (var9 != 32767) {
                  var10 = var1.q();
                  var11 = var1.z();
                  var12 = var9 > 0 ? ((aR)var1).b() : var11;
                  var4.a(var19, var9, var10, var11, var12);
               } else {
                  var4.d(var19);
               }
            }
         }
      }

      if ((var3 & 1) != 0) {
         var1.H();
         ((aR)var1).b();
         var1.B();
         var6 = ((aR)var1).b();
         this.e.d = 0;
         ((aR)var1).c(this.e.c, 0, var6);
         this.e.d = 0;
      }

      if ((var3 & 131072) != 0) {
         var6 = ((aR)var1).A();
         if (var6 > 0) {
            for(var17 = 0; var17 < var6; ++var17) {
               var19 = var1.q();
               var9 = var1.q();
               var10 = var1.q();
               var11 = var1.q();
               var4.a(var19, var9, var10, var11);
            }
         }
      }

      if ((var3 & 128) != 0) {
         var6 = var1.H();
         var4.e(var6);
      }

      if ((var3 & 8192) != 0) {
         var6 = Client.x + var1.d();
         var17 = Client.x + var1.F();
         var21 = var1.E();
         var22 = var1.c();
         byte var24 = var1.c();
         byte var25 = (byte)var1.z();
         var4.a(var6, var17, var21, var22, var24, var25);
      }

      if ((var3 & '耀') != 0) {
         for(var6 = 0; var6 < 3; ++var6) {
            this.f[var6] = ((aR)var1).m();
         }

         var4.a(this.f);
      }

      if ((var3 & 4) != 0) {
         var6 = var1.d();
         var17 = var6 + (var1.B() << 16);
         var19 = 16777215;
         if (var17 == var19) {
            var17 = -1;
         }

         var4.c(var17);
      }

      if ((var3 & 4096) != 0) {
         var15 = new aJ[]{aJ.d, aJ.c, aJ.b, aJ.a};
         var4.j = (aJ)kk.a(var15, var1.D());
         if (var5 == Integer.MAX_VALUE) {
            this.d[var2] = var4.j;
         }
      }

      if ((var3 & 32) != 0) {
         String var23 = ((aR)var1).m();
         var4.a(var23);
      }

      if ((var3 & 262144) != 0) {
         var6 = ((aR)var1).A();

         for(var17 = 0; var17 < var6; ++var17) {
            var19 = ((aR)var1).b();
            var9 = var1.F();
            var10 = var1.h();
            var4.b(var19, var9, var10 >> 16, var10 & '\uffff');
         }
      }

      if ((var3 & 16384) != 0) {
         var6 = var1.H();
         var17 = var6 >> 8;
         var19 = var17 >= 13 && var17 <= 20 ? var17 - 12 : 0;
         j var26 = (j)kk.a(osrs.j.a(), var1.z());
         boolean var27 = ((aR)var1).A() == 1;
         var11 = var1.z();
         this.e.d = 0;
         ((aR)var1).c(this.e.c, 0, var11);
         this.e.d = 0;
         String var28 = aX.a(bo.b(jy.a(this.e)));
         byte[] var29 = null;
         if (var19 > 0 && var19 <= 8) {
            var29 = new byte[var19];

            for(int var14 = 0; var14 < var19; ++var14) {
               var29[var14] = var1.c();
            }
         }

         var4.a(var6, var26, var27, var28, var29);
      }

   }

   public void a(t var1) {
      this.b(var1);
      if (var1 != null) {
         this.b[var1.j].b(var1);
      }

   }

   public void d() {
      this.j = 0;

      for(int var1 = 0; var1 < 2048; ++var1) {
         this.b[var1].a();
      }

   }

   public int b(int var1) {
      for(int var2 = 0; var2 < this.j; ++var2) {
         if (this.a[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public void d(bu var1, int var2) {
      bo.C = false;

      for(int var3 = 0; var3 < this.j; ++var3) {
         ArrayList var4 = this.b[this.a[var3]].a;

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            ((t)var4.get(var5)).t();
         }
      }

   }

   public void b(t var1) {
      if (var1 != null) {
         Client.s.getCallbacks().post(new PlayerDespawned(var1));
      }

   }

   public void e(bu var1, int var2) {
      bo.C = true;
   }
}
