package osrs;

public class aQ {
   public int a = 32;
   public int[] b;
   public cl c;
   public long d = 0L;
   public int e;
   public int f;
   public int g;
   public long h = bd.a();
   public int i = 0;
   public int j = 0;
   public int k = 0;
   public long l = 0L;
   public boolean m = true;
   public int n = 0;
   public cl[] o = new cl[8];
   public cl[] p = new cl[8];

   public static final void a(cl var0) {
      var0.H = false;
      if (var0.G != null) {
         var0.G.a = 0;
      }

      for(cl var1 = var0.i(); var1 != null; var1 = var0.j()) {
         a(var1);
      }

   }

   public final synchronized void b(cl var1) {
      this.c = var1;
   }

   public final synchronized void a() {
      if (this.b != null) {
         long var1 = bd.a();

         try {
            if (this.l != 0L) {
               if (var1 < this.l) {
                  return;
               }

               this.b(this.e);
               this.l = 0L;
               this.m = true;
            }

            int var3 = this.f();
            if (this.k - var3 > this.i) {
               this.i = this.k - var3;
            }

            int var4 = this.f + this.g;
            if (var4 + 512 > 32768) {
               var4 = 32256;
            }

            if (var4 + 512 > this.e) {
               this.e += 1024;
               if (this.e > 32768) {
                  this.e = 32768;
               }

               this.h();
               this.b(this.e);
               var3 = 0;
               this.m = true;
               if (var4 + 512 > this.e) {
                  var4 = this.e - 512;
                  this.g = var4 - this.f;
               }
            }

            while(var3 < var4) {
               this.a((int[])this.b, 512);
               this.g();
               var3 += 512;
            }

            if (var1 > this.d) {
               if (!this.m) {
                  if (this.i == 0 && this.j == 0) {
                     this.h();
                     this.l = var1 + 2000L;
                     return;
                  }

                  this.g = Math.min(this.j, this.i);
                  this.j = this.i;
               } else {
                  this.m = false;
               }

               this.i = 0;
               this.d = var1 + 2000L;
            }

            this.k = var3;
         } catch (Exception var6) {
            this.h();
            this.l = var1 + 2000L;
         }

         try {
            if (var1 > this.h + 500000L) {
               var1 = this.h;
            }

            while(var1 > this.h + 5000L) {
               this.a(512);
               this.h += (long)(512000 / bo.fG);
            }
         } catch (Exception var5) {
            this.h = var1;
         }
      }

   }

   public final void b() {
      this.m = true;
   }

   public final synchronized void c() {
      this.m = true;

      try {
         this.i();
      } catch (Exception var2) {
         this.h();
         this.l = bd.a() + 2000L;
      }

   }

   public final synchronized void d() {
      if (bo.dC != null) {
         boolean var1 = true;

         for(int var2 = 0; var2 < 2; ++var2) {
            if (bo.dC.a[var2] == this) {
               bo.dC.a[var2] = null;
            }

            if (bo.dC.a[var2] != null) {
               var1 = false;
            }
         }

         if (var1) {
            bo.fI.shutdownNow();
            bo.fI = null;
            bo.dC = null;
         }
      }

      this.h();
      this.b = null;
   }

   public final void a(int var1) {
      this.n -= var1;
      if (this.n < 0) {
         this.n = 0;
      }

      if (this.c != null) {
         this.c.h(var1);
      }

   }

   public final void a(int[] var1, int var2) {
      int var3 = var2;
      if (bo.dv) {
         var3 = var2 << 1;
      }

      kr.a(var1, 0, var3);
      this.n -= var2;
      if (this.c != null && this.n <= 0) {
         this.n += bo.fG >> 4;
         a(this.c);
         this.a(this.c, this.c.a_());
         int var4 = 0;
         int var5 = 255;

         int var6;
         label106:
         for(var6 = 7; var5 != 0; --var6) {
            int var7;
            int var8;
            if (var6 < 0) {
               var7 = var6 & 3;
               var8 = -(var6 >> 2);
            } else {
               var7 = var6;
               var8 = 0;
            }

            for(int var9 = var5 >>> var7 & 286331153; var9 != 0; var9 >>>= 4) {
               if ((var9 & 1) != 0) {
                  var5 &= ~(1 << var7);
                  cl var10 = null;
                  cl var11 = this.o[var7];

                  label100:
                  while(true) {
                     while(true) {
                        if (var11 == null) {
                           break label100;
                        }

                        cm var12 = var11.G;
                        if (var12 != null && var12.a > var8) {
                           var5 |= 1 << var7;
                           var10 = var11;
                           var11 = var11.I;
                        } else {
                           var11.H = true;
                           int var13 = var11.k();
                           var4 += var13;
                           if (var12 != null) {
                              var12.a += var13;
                           }

                           if (var4 >= this.a) {
                              break label106;
                           }

                           cl var14 = var11.i();
                           if (var14 != null) {
                              for(int var15 = var11.J; var14 != null; var14 = var11.j()) {
                                 this.a(var14, var15 * var14.a_() >> 8);
                              }
                           }

                           cl var19 = var11.I;
                           var11.I = null;
                           if (var10 == null) {
                              this.o[var7] = var19;
                           } else {
                              var10.I = var19;
                           }

                           if (var19 == null) {
                              this.p[var7] = var10;
                           }

                           var11 = var19;
                        }
                     }
                  }
               }

               var7 += 4;
               ++var8;
            }
         }

         for(var6 = 0; var6 < 8; ++var6) {
            cl var16 = this.o[var6];
            cl[] var17 = this.o;
            this.p[var6] = null;

            cl var18;
            for(var17[var6] = null; var16 != null; var16 = var18) {
               var18 = var16.I;
               var16.I = null;
            }
         }
      }

      if (this.n < 0) {
         this.n = 0;
      }

      if (this.c != null) {
         this.c.a(var1, 0, var2);
      }

      this.h = bd.a();
   }

   public final void a(cl var1, int var2) {
      int var3 = var2 >> 5;
      cl var4 = this.p[var3];
      if (var4 == null) {
         this.o[var3] = var1;
      } else {
         var4.I = var1;
      }

      this.p[var3] = var1;
      var1.J = var2;
   }

   public void e() throws Exception {
   }

   public void b(int var1) throws Exception {
   }

   public int f() throws Exception {
      return this.e;
   }

   public void g() throws Exception {
   }

   public void h() {
   }

   public void i() throws Exception {
   }

   public void j() {
      if (this.b == null) {
         iG var1 = Client.s.bx();
         synchronized(var1) {
            var1.c().clear();
         }
      }

   }
}
