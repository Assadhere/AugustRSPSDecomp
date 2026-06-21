package osrs;

import java.io.IOException;
import java.net.Socket;

public abstract class gO {
   public static gO a(Socket var0, int var1, int var2) throws IOException {
      return new gF(var0, var1, var2);
   }

   public static final byte[] a(byte[] var0, int var1) {
      aR var2 = new aR(var0);
      int var3 = var2.ai();
      int var4 = var2.R();
      if (var4 >= 0) {
         if (var3 == 0) {
            byte[] var10 = new byte[var4];
            var2.d(var10, 0, var4);
            return var10;
         } else {
            int var5 = var2.R();
            if (var5 < 0) {
               throw new RuntimeException();
            } else {
               byte[] var6 = new byte[var5];
               if (var3 == 1) {
                  av.a(var6, var5, var0, var4, 9);
               } else {
                  synchronized(au.b) {
                     au.b.b(var2, var6);
                  }
               }

               return var6;
            }
         }
      } else {
         throw new RuntimeException();
      }
   }

   public abstract int b() throws IOException;

   public abstract int a(byte[] var1, int var2, int var3) throws IOException;

   public abstract void b(byte[] var1, int var2, int var3) throws IOException;

   public abstract void c();

   public abstract int a() throws IOException;

   public abstract boolean a(int var1) throws IOException;
}
