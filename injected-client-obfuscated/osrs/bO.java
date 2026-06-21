package osrs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class bO {
   public boolean a;
   public boolean b;
   public int c;
   public final HttpsURLConnection d;
   public final dk e;
   public final gE f;
   public hk g;

   public bO(URL var1, dk var2, boolean var3) throws IOException {
      this(var1, var2, new gE(), var3);
   }

   public bO(URL var1, dk var2, gE var3, boolean var4) throws IOException {
      this.a = false;
      this.b = false;
      this.c = 300000;
      if (!var2.a()) {
         throw new UnsupportedEncodingException("Unsupported request method used " + var2.c());
      } else {
         this.d = (HttpsURLConnection)var1.openConnection();
         if (!var4) {
            HttpsURLConnection var5 = this.d;
            if (bo.dy == null) {
               bo.dy = new cB();
            }

            cB var6 = bo.dy;
            var5.setSSLSocketFactory(var6);
         }

         this.e = var2;
         this.f = var3 != null ? var3 : new gE();
      }
   }

   public gE a() {
      return this.f;
   }

   public void a(hk var1) {
      if (!this.a) {
         if (var1 == null) {
            this.f.a("Content-Type");
            this.g = null;
         } else {
            this.g = var1;
            if (this.g.a() != null) {
               this.f.a(this.g.a());
            } else {
               this.f.b();
            }
         }
      }

   }

   public void b() throws ProtocolException {
      if (!this.a) {
         this.d.setRequestMethod(this.e.c());
         this.f.a(this.d);
         if (this.e.d() && this.g != null) {
            this.d.setDoOutput(true);
            ByteArrayOutputStream var1 = new ByteArrayOutputStream();

            try {
               var1.write(this.g.b());
               var1.writeTo(this.d.getOutputStream());
            } catch (IOException var11) {
               var11.printStackTrace();
            } finally {
               try {
                  var1.close();
               } catch (IOException var10) {
                  var10.printStackTrace();
               }

            }
         }

         this.d.setConnectTimeout(this.c);
         this.d.setInstanceFollowRedirects(this.b);
         this.a = true;
      }

   }

   public boolean c() throws IOException {
      if (!this.a) {
         this.b();
      }

      this.d.connect();
      return this.d.getResponseCode() == -1;
   }

   public fw d() {
      try {
         if (!this.a || this.d.getResponseCode() == -1) {
            return new fw("No REST response has been received yet.");
         }
      } catch (IOException var10) {
         this.d.disconnect();
         return new fw("Error decoding REST response code: " + var10.getMessage());
      }

      fw var1;
      try {
         fw var2 = new fw(this.d);
         fw var3 = var2;
         return var3;
      } catch (IOException var8) {
         var1 = new fw("Error decoding REST response: " + var8.getMessage());
      } finally {
         this.d.disconnect();
      }

      return var1;
   }
}
