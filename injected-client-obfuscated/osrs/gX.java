package osrs;

import java.io.IOException;
import java.io.OutputStream;

public class gX implements Runnable {
   public int a = 0;
   public int b = 0;
   public OutputStream c;
   public int d;
   public byte[] e;
   public Thread f;
   public IOException g;
   public boolean h;

   public gX(OutputStream var1, int var2) {
      this.c = var1;
      this.d = var2 + 1;
      this.e = new byte[this.d];
      this.f = new Thread(this);
      this.f.setDaemon(true);
      this.f.start();
   }

   public boolean a() {
      if (this.h) {
         try {
            this.c.close();
            if (this.g == null) {
               this.g = new IOException("");
            }
         } catch (IOException var2) {
            if (this.g == null) {
               this.g = new IOException(var2);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void run() {
      while(true) {
         synchronized(this){}

         while(true) {
            boolean var13 = false;

            int var1;
            try {
               var13 = true;
               if (this.g != null) {
                  return;
               }

               if (this.a <= this.b) {
                  var1 = this.b - this.a;
               } else {
                  var1 = this.b + (this.d - this.a);
               }

               if (var1 <= 0) {
                  try {
                     this.c.flush();
                  } catch (IOException var17) {
                     this.g = var17;
                     return;
                  }

                  if (this.a()) {
                     return;
                  }

                  try {
                     this.wait();
                  } catch (InterruptedException var18) {
                  }
                  continue;
               }

               var13 = false;
            } finally {
               if (var13) {
                  ;
               }
            }

            try {
               if (this.a + var1 <= this.d) {
                  this.c.write(this.e, this.a, var1);
               } else {
                  int var2 = this.d - this.a;
                  this.c.write(this.e, this.a, var2);
                  this.c.write(this.e, 0, var1 - var2);
               }
            } catch (IOException var16) {
               IOException var3 = var16;
               synchronized(this) {
                  this.g = var3;
                  return;
               }
            }

            synchronized(this) {
               this.a = (this.a + var1) % this.d;
            }

            if (!this.a()) {
               break;
            }

            return;
         }
      }
   }

   public void a(byte[] var1, int var2, int var3) throws IOException {
      if (var3 >= 0 && var2 >= 0 && var2 + var3 <= var1.length) {
         synchronized(this) {
            if (this.g != null) {
               throw new IOException(this.g.toString());
            } else {
               int var5;
               if (this.a <= this.b) {
                  var5 = this.a + (this.d - this.b) - 1;
               } else {
                  var5 = this.a - this.b - 1;
               }

               if (var5 < var3) {
                  throw new IOException("");
               } else {
                  if (this.b + var3 <= this.d) {
                     System.arraycopy(var1, var2, this.e, this.b, var3);
                  } else {
                     int var6 = this.d - this.b;
                     System.arraycopy(var1, var2, this.e, this.b, var6);
                     System.arraycopy(var1, var2 + var6, this.e, 0, var3 - var6);
                  }

                  this.b = (this.b + var3) % this.d;
                  this.notifyAll();
               }
            }
         }
      } else {
         throw new IOException();
      }
   }

   public void b() {
      synchronized(this) {
         this.h = true;
         this.notifyAll();
      }

      try {
         this.f.join();
      } catch (InterruptedException var3) {
      }

   }
}
