package osrs;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketImpl;

public class gF extends gO {
   public Socket a;
   public ha b;
   public gX c;

   public gF(Socket var1, int var2, int var3) throws IOException {
      this.a = var1;
      this.a.setSoTimeout(30000);
      this.a.setTcpNoDelay(true);
      this.a.setReceiveBufferSize(65536);
      this.a.setSendBufferSize(65536);
      this.b = new ha(this.a.getInputStream(), var2);
      this.c = new gX(this.a.getOutputStream(), var3);
   }

   public boolean a(int var1) throws IOException {
      return this.b.a(var1);
   }

   public int a() throws IOException {
      return this.b.a();
   }

   public int b() throws IOException {
      return this.b.b();
   }

   public int a(byte[] var1, int var2, int var3) throws IOException {
      return this.b.a(var1, var2, var3);
   }

   public void b(byte[] var1, int var2, int var3) throws IOException {
      this.c.a(var1, var2, var3);
   }

   public void c() {
      this.c.b();

      try {
         this.a.close();
      } catch (IOException var2) {
      }

      this.b.c();
   }

   public void finalize() {
      this.c();
   }

   public FileDescriptor d() {
      if (this.b.c instanceof FileInputStream) {
         FileInputStream var6 = (FileInputStream)this.b.c;

         try {
            return var6.getFD();
         } catch (IOException var4) {
            return null;
         }
      } else {
         try {
            Field var1 = Socket.class.getDeclaredField("impl");
            var1.setAccessible(true);
            SocketImpl var2 = (SocketImpl)var1.get(this.a);
            Method var3 = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
            var3.setAccessible(true);
            return (FileDescriptor)var3.invoke(var2);
         } catch (ReflectiveOperationException | InaccessibleObjectException var5) {
            return null;
         }
      }
   }
}
