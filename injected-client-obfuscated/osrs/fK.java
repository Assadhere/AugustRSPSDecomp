package osrs;

import java.io.IOException;
import java.util.zip.CRC32;

public class fK {
   public fH a = new fH();
   public fT b = new fT(4096);
   public int c = -1;
   public fT d = new fT(4096);
   public int e = 0;
   public boolean f;
   public fB g;
   public aR h;
   public aR i;
   public int j = 0;
   public CRC32 k = new CRC32();
   public aR l = new aR(8);
   public av[] m = new av[256];
   public int n = 0;
   public int o = 0;
   public byte p = 0;
   public int q = 0;
   public int r = 0;
   public fT s = new fT(4096);
   public fT t = new fT(32);
   public int u = 0;
   public int v = 0;
   public int w = 255;
   public long x;
   public gO y;

   public boolean a() {
      long var1 = bd.a();
      int var3 = (int)(var1 - this.x);
      this.x = var1;
      if (var3 > 200) {
         var3 = 200;
      }

      this.o += var3;
      if (this.u == 0 && this.e == 0 && this.j == 0 && this.n == 0) {
         return true;
      } else if (this.y == null) {
         return false;
      } else {
         try {
            if (this.o > 30000) {
               throw new IOException();
            } else {
               fB var4;
               aR var5;
               while(this.e < 200 && this.n > 0) {
                  var4 = (fB)this.s.b();
                  var5 = new aR(4);
                  var5.a(1);
                  var5.c((int)var4.cm);
                  this.y.b(var5.c, 0, 4);
                  this.t.a(var4, var4.cm);
                  --this.n;
                  ++this.e;
               }

               while(this.u < 200 && this.j > 0) {
                  var4 = (fB)this.a.a();
                  var5 = new aR(4);
                  var5.a(0);
                  var5.c((int)var4.cm);
                  this.y.b(var5.c, 0, 4);
                  ((aA)var4).ae();
                  this.d.a(var4, var4.cm);
                  --this.j;
                  ++this.u;
               }

               for(int var22 = 0; var22 < 100; ++var22) {
                  int var23 = this.y.a();
                  if (var23 < 0) {
                     throw new IOException();
                  }

                  if (var23 == 0) {
                     break;
                  }

                  this.o = 0;
                  byte var6 = 0;
                  if (this.g == null) {
                     var6 = 8;
                  } else if (this.v == 0) {
                     var6 = 1;
                  }

                  byte[] var7;
                  int var8;
                  aR var9;
                  int var10;
                  int var11;
                  int var12;
                  if (var6 > 0) {
                     var10 = var6 - this.l.d;
                     if (var10 > var23) {
                        var10 = var23;
                     }

                     this.y.a(this.l.c, this.l.d, var10);
                     if (this.p != 0) {
                        for(var11 = 0; var11 < var10; ++var11) {
                           var7 = this.l.c;
                           var8 = this.l.d + var11;
                           var7[var8] ^= this.p;
                        }
                     }

                     var9 = this.l;
                     var9.d += var10;
                     if (this.l.d < var6) {
                        break;
                     }

                     if (this.g == null) {
                        this.l.d = 0;
                        var11 = this.l.b();
                        var12 = this.l.d();
                        int var13 = this.l.b();
                        int var14 = this.l.h();
                        long var15 = (long)((var11 << 16) + var12);
                        fB var17 = (fB)this.t.a(var15);
                        this.f = true;
                        if (var17 == null) {
                           var17 = (fB)this.d.a(var15);
                           this.f = false;
                        }

                        if (var17 == null) {
                           throw new IOException();
                        }

                        int var18 = var13 == 0 ? 5 : 9;
                        this.g = var17;
                        this.h = new aR(var14 + var18 + this.g.a);
                        this.h.a(var13);
                        this.h.d(var14);
                        this.v = 8;
                        this.l.d = 0;
                     } else if (this.v == 0) {
                        if (this.l.c[0] == -1) {
                           this.v = 1;
                           this.l.d = 0;
                        } else {
                           this.g = null;
                        }
                     }
                  } else {
                     var10 = this.h.c.length - this.g.a;
                     var11 = 512 - this.v;
                     if (var11 > var10 - this.h.d) {
                        var11 = var10 - this.h.d;
                     }

                     if (var11 > var23) {
                        var11 = var23;
                     }

                     this.y.a(this.h.c, this.h.d, var11);
                     if (this.p != 0) {
                        for(var12 = 0; var12 < var11; ++var12) {
                           var7 = this.h.c;
                           var8 = this.h.d + var12;
                           var7[var8] ^= this.p;
                        }
                     }

                     var9 = this.h;
                     var9.d += var11;
                     this.v += var11;
                     if (this.h.d == var10) {
                        if (this.g.cm == 16711935L) {
                           this.i = this.h;

                           for(var12 = 0; var12 < 256; ++var12) {
                              av var24 = this.m[var12];
                              if (var24 != null) {
                                 this.b(var24, var12);
                              }
                           }
                        } else {
                           this.k.reset();
                           this.k.update(this.h.c, 0, var10);
                           var12 = (int)this.k.getValue();
                           if (this.g.b != var12) {
                              try {
                                 this.y.c();
                              } catch (Exception var20) {
                              }

                              ++this.q;
                              this.y = null;
                              this.p = (byte)((int)(Math.random() * 255.0 + 1.0));
                              return false;
                           }

                           this.q = 0;
                           this.r = 0;
                           this.g.c.a((int)(this.g.cm & 65535L), this.h.c, (this.g.cm & 16711680L) == 16711680L, this.f);
                        }

                        this.g.X();
                        if (this.f) {
                           --this.e;
                        } else {
                           --this.u;
                        }

                        this.v = 0;
                        this.g = null;
                        this.h = null;
                     } else {
                        if (this.v != 512) {
                           break;
                        }

                        this.v = 0;
                     }
                  }
               }

               return true;
            }
         } catch (IOException var21) {
            try {
               this.y.c();
            } catch (Exception var19) {
            }

            ++this.r;
            this.y = null;
            return false;
         }
      }
   }

   public void a(boolean var1) {
      if (this.y != null) {
         try {
            aR var2 = new aR(4);
            var2.a(var1 ? 2 : 3);
            var2.c(0);
            this.y.b(var2.c, 0, 4);
         } catch (IOException var5) {
            try {
               this.y.c();
            } catch (Exception var4) {
            }

            ++this.r;
            this.y = null;
         }
      }

   }

   public void a(gO var1, boolean var2) {
      if (this.y != null) {
         try {
            this.y.c();
         } catch (Exception var9) {
         }

         this.y = null;
      }

      this.y = var1;
      this.a(var2);
      this.l.d = 0;
      this.g = null;
      this.h = null;
      this.v = 0;

      while(true) {
         fB var3 = (fB)this.t.b();
         if (var3 == null) {
            while(true) {
               fB var4 = (fB)this.d.b();
               if (var4 == null) {
                  if (this.p != 0) {
                     try {
                        aR var5 = new aR(4);
                        var5.a(4);
                        var5.a(this.p);
                        var5.b(0);
                        this.y.b(var5.c, 0, 4);
                     } catch (IOException var8) {
                        try {
                           this.y.c();
                        } catch (Exception var7) {
                        }

                        ++this.r;
                        this.y = null;
                     }
                  }

                  this.o = 0;
                  this.x = bd.a();
                  return;
               }

               this.a.b(var4);
               this.b.a(var4, var4.cm);
               ++this.j;
               --this.u;
            }
         }

         this.s.a(var3, var3.cm);
         ++this.n;
         --this.e;
      }
   }

   public void a(av var1, int var2) {
      if (var1.C) {
         if (!var1.B) {
            if (var2 <= this.c) {
               throw new RuntimeException("");
            }

            if (var2 < this.w) {
               this.w = var2;
            }
         }
      } else {
         if (var2 >= this.w) {
            throw new RuntimeException("");
         }

         if (var2 > this.c) {
            this.c = var2;
         }
      }

      if (this.i != null) {
         this.b(var1, var2);
      } else {
         this.a((av)null, fA.m.l, 255, 0, (byte)0, true);
         this.m[var2] = var1;
      }

   }

   public void b(av var1, int var2) {
      int var3 = var2 * 8 + 5;
      int var4 = 0;
      int var5 = 0;
      if (var3 <= this.i.c.length - 8) {
         this.i.d = var3;
         var4 = this.i.h();
         var5 = this.i.h();
      }

      if (var4 == 0 && var5 == 0) {
         if (!var1.C) {
            throw new RuntimeException("");
         }

         var1.e();
      } else {
         var1.d(var4, var5);
      }

   }

   public int a(int var1, int var2) {
      long var3 = (long)((var1 << 16) + var2);
      return this.g != null && this.g.cm == var3 ? this.h.d * 99 / (this.h.c.length - this.g.a) + 1 : 0;
   }

   public int a(boolean var1, boolean var2) {
      int var3 = 0;
      if (var1) {
         var3 += this.e + this.n;
      }

      if (var2) {
         var3 += this.u + this.j;
      }

      return var3;
   }

   public void b() {
      if (this.y != null) {
         try {
            this.y.c();
         } catch (Exception var2) {
         }

         this.y = null;
      }

   }

   public void a(av var1, int var2, int var3, int var4, byte var5, boolean var6) {
      synchronized(this) {
         long var8 = (long)((var2 << 16) + var3);
         fB var10 = (fB)this.s.a(var8);
         if (var10 == null) {
            fB var11 = (fB)this.t.a(var8);
            if (var11 == null) {
               fB var12 = (fB)this.b.a(var8);
               if (var12 != null) {
                  if (var6) {
                     ((aA)var12).ae();
                     this.s.a(var12, var8);
                     --this.j;
                     ++this.n;
                  }
               } else {
                  fB var13;
                  if (!var6) {
                     var13 = (fB)this.d.a(var8);
                     if (var13 != null) {
                        return;
                     }
                  }

                  var13 = new fB();
                  var13.c = var1;
                  var13.b = var4;
                  var13.a = var5;
                  if (var6) {
                     this.s.a(var13, var8);
                     ++this.n;
                  } else {
                     this.a.a(var13);
                     this.b.a(var13, var8);
                     ++this.j;
                  }
               }
            }
         }

      }
   }

   public void b(int var1, int var2) {
      synchronized(this) {
         long var4 = (long)((var1 << 16) + var2);
         fB var6 = (fB)this.b.a(var4);
         if (var6 != null) {
            this.a.b(var6);
         }

      }
   }

   public void b(av var1, int var2, int var3, int var4, byte var5, boolean var6) {
      this.a(var1, var2, var3, var4, var5, var6);
   }
}
