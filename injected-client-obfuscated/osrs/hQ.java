package osrs;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.SyncFailedException;

public final class hQ {
   public RandomAccessFile a;
   public final File b;
   public final long c;
   public long d;

   public hQ(File var1, String var2, long var3) throws IOException {
      if (var3 == -1L) {
         var3 = Long.MAX_VALUE;
      }

      if (var1.length() > var3) {
         var1.delete();
      }

      this.a = new RandomAccessFile(var1, var2);
      this.b = var1;
      this.c = var3;
      this.d = 0L;
      int var5 = this.a.read();
      if (var5 != -1 && !var2.equals("r")) {
         this.a.seek(0L);
         this.a.write(var5);
      }

      this.a.seek(0L);
   }

   public final File a() {
      return this.b;
   }

   public final void a(long var1) throws IOException {
      this.a.seek(var1);
      this.d = var1;
   }

   public final void a(byte[] var1, int var2, int var3) throws IOException {
      if (this.d + (long)var3 > this.c) {
         this.a.seek(this.c);
         this.a.write(1);
         throw new EOFException();
      } else {
         this.a.write(var1, var2, var3);
         this.d += (long)var3;
      }
   }

   public final void b() throws IOException {
      this.a(false);
   }

   public final void a(boolean var1) throws IOException {
      if (this.a != null) {
         if (var1) {
            try {
               this.a.getFD().sync();
            } catch (SyncFailedException var3) {
            }
         }

         this.a.close();
         this.a = null;
      }

   }

   public long c() throws IOException {
      return this.a.length();
   }

   public final int b(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.a.read(var1, var2, var3);
      if (var4 > 0) {
         this.d += (long)var4;
      }

      return var4;
   }

   public void finalize() throws Throwable {
      if (this.a != null) {
         this.b();
      }

   }

   public void c(byte[] var1, int var2, int var3) throws IOException {
      this.a(var1, var2, var3);
   }

   public void b(boolean var1) throws IOException {
      this.a(var1);
   }
}
