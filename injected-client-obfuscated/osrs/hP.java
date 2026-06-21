package osrs;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;

public class hP {
   public long a = -1L;
   public long b = -1L;
   public int c = 0;
   public hQ d;
   public long e;
   public long f;
   public byte[] g;
   public byte[] h;
   public long i;
   public int j;
   public long k;

   public hP(hQ var1, int var2, int var3) throws IOException {
      this.d = var1;
      this.f = this.e = var1.c();
      this.g = new byte[var2];
      this.h = new byte[var3];
      this.i = 0L;
   }

   public void a() throws IOException {
      this.e();
      this.d.b();
   }

   public void a(long var1) throws IOException {
      if (var1 < 0L) {
         throw new IOException("");
      } else {
         this.i = var1;
      }
   }

   public long b() {
      return this.f;
   }

   public final File c() {
      return this.d.a();
   }

   public void a(byte[] var1) throws IOException {
      this.a(var1, 0, var1.length);
   }

   public void a(byte[] var1, int var2, int var3) throws IOException {
      try {
         if (var2 + var3 > var1.length) {
            throw new ArrayIndexOutOfBoundsException(var2 + var3 - var1.length);
         }

         if (this.b != -1L && this.i >= this.b && this.i + (long)var3 <= this.b + (long)this.c) {
            System.arraycopy(this.h, (int)(this.i - this.b), var1, var2, var3);
            this.i += (long)var3;
            return;
         }

         long var4 = this.i;
         int var6 = var3;
         int var7;
         if (this.i >= this.a && this.i < this.a + (long)this.j) {
            var7 = (int)((long)this.j - (this.i - this.a));
            if (var7 > var3) {
               var7 = var3;
            }

            System.arraycopy(this.g, (int)(this.i - this.a), var1, var2, var7);
            this.i += (long)var7;
            var2 += var7;
            var3 -= var7;
         }

         if (var3 > this.g.length) {
            this.d.a(this.i);

            for(this.k = this.i; var3 > 0; var3 -= var7) {
               var7 = this.d.b(var1, var2, var3);
               if (var7 == -1) {
                  break;
               }

               this.k += (long)var7;
               this.i += (long)var7;
               var2 += var7;
            }
         } else if (var3 > 0) {
            this.d();
            var7 = var3;
            if (var3 > this.j) {
               var7 = this.j;
            }

            System.arraycopy(this.g, 0, var1, var2, var7);
            var2 += var7;
            var3 -= var7;
            this.i += (long)var7;
         }

         if (this.b != -1L) {
            if (this.b > this.i && var3 > 0) {
               var7 = (int)(this.b - this.i) + var2;
               if (var7 > var2 + var3) {
                  var7 = var2 + var3;
               }

               while(var2 < var7) {
                  var1[var2++] = 0;
                  --var3;
                  ++this.i;
               }
            }

            long var13 = -1L;
            long var9 = -1L;
            if (this.b >= var4 && this.b < (long)var6 + var4) {
               var13 = this.b;
            } else if (var4 >= this.b && var4 < this.b + (long)this.c) {
               var13 = var4;
            }

            if (this.b + (long)this.c > var4 && this.b + (long)this.c <= (long)var6 + var4) {
               var9 = this.b + (long)this.c;
            } else if ((long)var6 + var4 > this.b && (long)var6 + var4 <= this.b + (long)this.c) {
               var9 = (long)var6 + var4;
            }

            if (var13 > -1L && var9 > var13) {
               int var11 = (int)(var9 - var13);
               System.arraycopy(this.h, (int)(var13 - this.b), var1, (int)(var13 - var4) + var2, var11);
               if (var9 > this.i) {
                  var3 = (int)((long)var3 - (var9 - this.i));
                  this.i = var9;
               }
            }
         }
      } catch (IOException var12) {
         this.k = -1L;
         throw var12;
      }

      if (var3 > 0) {
         throw new EOFException();
      }
   }

   public void d() throws IOException {
      this.j = 0;
      if (this.i != this.k) {
         this.d.a(this.i);
         this.k = this.i;
      }

      int var1;
      for(this.a = this.i; this.j < this.g.length; this.j += var1) {
         int var2 = this.g.length - this.j;
         if (var2 > 200000000) {
            var2 = 200000000;
         }

         var1 = this.d.b(this.g, this.j, var2);
         if (var1 == -1) {
            break;
         }

         this.k += (long)var1;
      }

   }

   public void b(byte[] var1, int var2, int var3) throws IOException {
      try {
         if (this.i + (long)var3 > this.f) {
            this.f = this.i + (long)var3;
         }

         if (this.b != -1L && (this.i < this.b || this.i > this.b + (long)this.c)) {
            this.e();
         }

         if (this.b != -1L && this.i + (long)var3 > this.b + (long)this.h.length) {
            int var4 = (int)((long)this.h.length - (this.i - this.b));
            System.arraycopy(var1, var2, this.h, (int)(this.i - this.b), var4);
            this.i += (long)var4;
            var2 += var4;
            var3 -= var4;
            this.c = this.h.length;
            this.e();
         }

         if (var3 <= this.h.length) {
            if (var3 > 0) {
               if (this.b == -1L) {
                  this.b = this.i;
               }

               System.arraycopy(var1, var2, this.h, (int)(this.i - this.b), var3);
               this.i += (long)var3;
               if (this.i - this.b > (long)this.c) {
                  this.c = (int)(this.i - this.b);
               }
            }
         } else {
            if (this.i != this.k) {
               this.d.a(this.i);
               this.k = this.i;
            }

            this.d.a(var1, var2, var3);
            this.k += (long)var3;
            if (this.k > this.e) {
               this.e = this.k;
            }

            long var10 = -1L;
            long var6 = -1L;
            if (this.i >= this.a && this.i < this.a + (long)this.j) {
               var10 = this.i;
            } else if (this.a >= this.i && this.a < this.i + (long)var3) {
               var10 = this.a;
            }

            if (this.i + (long)var3 > this.a && this.i + (long)var3 <= this.a + (long)this.j) {
               var6 = this.i + (long)var3;
            } else if (this.a + (long)this.j > this.i && this.a + (long)this.j <= this.i + (long)var3) {
               var6 = this.a + (long)this.j;
            }

            if (var10 > -1L && var6 > var10) {
               int var8 = (int)(var6 - var10);
               System.arraycopy(var1, (int)((long)var2 + var10 - this.i), this.g, (int)(var10 - this.a), var8);
            }

            this.i += (long)var3;
         }

      } catch (IOException var9) {
         this.k = -1L;
         throw var9;
      }
   }

   public void e() throws IOException {
      if (this.b != -1L) {
         if (this.b != this.k) {
            this.d.a(this.b);
            this.k = this.b;
         }

         this.d.a(this.h, 0, this.c);
         this.k += (long)(this.c * -561502235) * 1989393901L;
         if (this.k > this.e) {
            this.e = this.k;
         }

         long var1 = -1L;
         long var3 = -1L;
         if (this.b >= this.a && this.b < this.a + (long)this.j) {
            var1 = this.b;
         } else if (this.a >= this.b && this.a < this.b + (long)this.c) {
            var1 = this.a;
         }

         if (this.b + (long)this.c > this.a && this.b + (long)this.c <= this.a + (long)this.j) {
            var3 = this.b + (long)this.c;
         } else if (this.a + (long)this.j > this.b && this.a + (long)this.j <= this.b + (long)this.c) {
            var3 = this.a + (long)this.j;
         }

         if (var1 > -1L && var3 > var1) {
            int var5 = (int)(var3 - var1);
            System.arraycopy(this.h, (int)(var1 - this.b), this.g, (int)(var1 - this.a), var5);
         }

         this.b = -1L;
         this.c = 0;
      }

   }
}
