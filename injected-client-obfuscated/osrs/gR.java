package osrs;

import java.io.IOException;

public final class gR {
   public static byte[] a = new byte[520];
   public hP b = null;
   public hP c = null;
   public int d = 65000;
   public int e;

   public gR(int var1, hP var2, hP var3, int var4) {
      this.e = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   public byte[] a(int var1) {
      synchronized(this.b) {
         try {
            Object var3;
            byte[] var10000;
            if (this.c.b() < (long)(var1 * 6 + 6)) {
               var3 = null;
               var10000 = (byte[])var3;
               return var10000;
            } else {
               this.c.a((long)(var1 * 6));
               this.c.a(a, 0, 6);
               int var4 = (a[2] & 255) + ((a[0] & 255) << 16) + ((a[1] & 255) << 8);
               int var5 = (a[5] & 255) + ((a[3] & 255) << 16) + ((a[4] & 255) << 8);
               if (var4 >= 0 && var4 <= this.d) {
                  if (var5 > 0 && (long)var5 <= this.b.b() / 520L) {
                     byte[] var6 = new byte[var4];
                     int var7 = 0;
                     int var8 = 0;

                     while(var7 < var4) {
                        if (var5 == 0) {
                           var3 = null;
                           var10000 = (byte[])var3;
                           return var10000;
                        }

                        this.b.a((long)var5 * 520L);
                        int var9 = var4 - var7;
                        byte var10;
                        int var11;
                        int var12;
                        int var13;
                        int var14;
                        if (var1 > 65535) {
                           if (var9 > 510) {
                              var9 = 510;
                           }

                           var10 = 10;
                           this.b.a(a, 0, var9 + var10);
                           var11 = (a[3] & 255) + ((a[2] & 255) << 8) + ((a[0] & 255) << 24) + ((a[1] & 255) << 16);
                           var12 = ((a[4] & 255) << 8) + (a[5] & 255);
                           var13 = (a[8] & 255) + ((a[6] & 255) << 16) + ((a[7] & 255) << 8);
                           var14 = a[9] & 255;
                        } else {
                           if (var9 > 512) {
                              var9 = 512;
                           }

                           var10 = 8;
                           this.b.a(a, 0, var9 + var10);
                           var11 = ((a[0] & 255) << 8) + (a[1] & 255);
                           var12 = ((a[2] & 255) << 8) + (a[3] & 255);
                           var13 = (a[6] & 255) + ((a[4] & 255) << 16) + ((a[5] & 255) << 8);
                           var14 = a[7] & 255;
                        }

                        if (var1 == var11 && var8 == var12 && this.e == var14) {
                           if (var13 >= 0 && (long)var13 <= this.b.b() / 520L) {
                              int var15 = var9 + var10;

                              for(int var16 = var10; var16 < var15; ++var16) {
                                 var6[var7++] = a[var16];
                              }

                              var5 = var13;
                              ++var8;
                              continue;
                           }

                           var3 = null;
                           var10000 = (byte[])var3;
                           return var10000;
                        }

                        var3 = null;
                        var10000 = (byte[])var3;
                        return var10000;
                     }

                     var10000 = var6;
                     return var10000;
                  } else {
                     var3 = null;
                     return (byte[])var3;
                  }
               } else {
                  var3 = null;
                  var10000 = (byte[])var3;
                  return var10000;
               }
            }
         } catch (IOException var18) {
            return null;
         }
      }
   }

   public boolean a(int var1, byte[] var2, int var3) {
      synchronized(this.b) {
         if (var3 >= 0 && var3 <= this.d) {
            boolean var5 = this.a(var1, var2, var3, true);
            if (!var5) {
               var5 = this.a(var1, var2, var3, false);
            }

            return var5;
         } else {
            throw new IllegalArgumentException(this.e + "," + var1 + "," + var3);
         }
      }
   }

   public boolean a(int var1, byte[] var2, int var3, boolean var4) {
      synchronized(this.b) {
         try {
            int var6;
            boolean var10000;
            if (var4) {
               if (this.c.b() < (long)(var1 * 6 + 6)) {
                  var10000 = false;
                  return var10000;
               }

               this.c.a((long)(var1 * 6));
               this.c.a(a, 0, 6);
               var6 = (a[5] & 255) + ((a[3] & 255) << 16) + ((a[4] & 255) << 8);
               if (var6 <= 0 || (long)var6 > this.b.b() / 520L) {
                  var10000 = false;
                  return var10000;
               }
            } else {
               this.b.e();
               var6 = (int)((this.b.c().length() + 519L) / 520L);
               if (var6 == 0) {
                  var6 = 1;
               }
            }

            int var7 = 0;
            int var8 = 0;

            for(int var9 = var6; var7 < var3; ++var8) {
               int var10 = 0;
               int var11;
               if (var4) {
                  this.b.a((long)var9 * 520L);
                  int var12;
                  int var13;
                  if (var1 > 65535) {
                     this.b.a(a, 0, 10);
                     var11 = (a[3] & 255) + ((a[2] & 255) << 8) + ((a[0] & 255) << 24) + ((a[1] & 255) << 16);
                     var12 = ((a[4] & 255) << 8) + (a[5] & 255);
                     var10 = (a[8] & 255) + ((a[6] & 255) << 16) + ((a[7] & 255) << 8);
                     var13 = a[9] & 255;
                  } else {
                     this.b.a(a, 0, 8);
                     var11 = ((a[0] & 255) << 8) + (a[1] & 255);
                     var12 = ((a[2] & 255) << 8) + (a[3] & 255);
                     var10 = (a[6] & 255) + ((a[4] & 255) << 16) + ((a[5] & 255) << 8);
                     var13 = a[7] & 255;
                  }

                  if (var1 != var11 || var8 != var12 || this.e != var13) {
                     var10000 = false;
                     return var10000;
                  }

                  if (var10 < 0 || (long)var10 > this.b.b() / 520L) {
                     var10000 = false;
                     return var10000;
                  }
               }

               if (var10 == 0) {
                  var4 = false;
                  this.b.e();
                  var10 = (int)((this.b.c().length() + 519L) / 520L);
                  if (var10 == 0) {
                     ++var10;
                  }

                  if (var9 == var10) {
                     ++var10;
                  }
               }

               if (var1 > 65535) {
                  if (var3 - var7 <= 510) {
                     var10 = 0;
                  }

                  a[0] = (byte)(var1 >> 24);
                  a[1] = (byte)(var1 >> 16);
                  a[2] = (byte)(var1 >> 8);
                  a[3] = (byte)var1;
                  a[4] = (byte)(var8 >> 8);
                  a[5] = (byte)var8;
                  a[6] = (byte)(var10 >> 16);
                  a[7] = (byte)(var10 >> 8);
                  a[8] = (byte)var10;
                  a[9] = (byte)this.e;
                  this.b.a((long)var9 * 520L);
                  this.b.b(a, 0, 10);
                  var11 = var3 - var7;
                  if (var11 > 510) {
                     var11 = 510;
                  }

                  this.b.b(var2, var7, var11);
                  var7 += var11;
               } else {
                  if (var3 - var7 <= 512) {
                     var10 = 0;
                  }

                  a[0] = (byte)(var1 >> 8);
                  a[1] = (byte)var1;
                  a[2] = (byte)(var8 >> 8);
                  a[3] = (byte)var8;
                  a[4] = (byte)(var10 >> 16);
                  a[5] = (byte)(var10 >> 8);
                  a[6] = (byte)var10;
                  a[7] = (byte)this.e;
                  this.b.a((long)var9 * 520L);
                  this.b.b(a, 0, 8);
                  var11 = var3 - var7;
                  if (var11 > 512) {
                     var11 = 512;
                  }

                  this.b.b(var2, var7, var11);
                  var7 += var11;
               }

               var9 = var10;
            }

            a[0] = (byte)(var3 >> 16);
            a[1] = (byte)(var3 >> 8);
            a[2] = (byte)var3;
            a[3] = (byte)(var6 >> 16);
            a[4] = (byte)(var6 >> 8);
            a[5] = (byte)var6;
            this.c.a((long)(var1 * 6));
            this.c.b(a, 0, 6);
            var10000 = true;
            return var10000;
         } catch (IOException var15) {
            return false;
         }
      }
   }

   public String toString() {
      return "" + this.e;
   }
}
