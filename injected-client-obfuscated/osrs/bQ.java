package osrs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

public class bQ extends cf {
   public final boolean a;

   public bQ(boolean var1, int var2) {
      super(var2);
      this.a = var1;
   }

   public void a(ce var1) throws IOException {
      URLConnection var2 = null;
      boolean var3 = false;
      boolean var10 = false;

      HttpURLConnection var5;
      HttpURLConnection var13;
      label175: {
         label176: {
            try {
               var10 = true;
               var3 = true;
               String var4 = var1.d.getProtocol();
               if (var4.equals("http")) {
                  var2 = this.b(var1);
               } else {
                  if (!var4.equals("https")) {
                     var1.c = ce.b;
                     var3 = false;
                     var10 = false;
                     break label175;
                  }

                  var2 = this.c(var1);
               }

               this.a(var2, var1);
               var3 = false;
               var10 = false;
               break label176;
            } catch (IOException var11) {
               var1.c = ce.b;
               var3 = false;
               var10 = false;
            } finally {
               if (var10) {
                  if (var3 && var2 != null && var2 instanceof HttpURLConnection) {
                     HttpURLConnection var7 = (HttpURLConnection)var2;
                     var7.disconnect();
                  }

               }
            }

            if (var3 && var2 != null && var2 instanceof HttpURLConnection) {
               var13 = (HttpURLConnection)var2;
               var13.disconnect();
            }

            if (var2 != null && var2 instanceof HttpURLConnection) {
               var13 = (HttpURLConnection)var2;
               var13.disconnect();
            }

            return;
         }

         if (var3 && var2 != null && var2 instanceof HttpURLConnection) {
            var5 = (HttpURLConnection)var2;
            var5.disconnect();
         }

         if (var2 != null && var2 instanceof HttpURLConnection) {
            var13 = (HttpURLConnection)var2;
            var13.disconnect();
         }

         return;
      }

      if (var3 && var2 != null && var2 instanceof HttpURLConnection) {
         var5 = (HttpURLConnection)var2;
         var5.disconnect();
      }

      if (var2 != null && var2 instanceof HttpURLConnection) {
         var13 = (HttpURLConnection)var2;
         var13.disconnect();
      }

   }

   public URLConnection b(ce var1) throws IOException {
      URLConnection var2 = var1.d.openConnection();
      this.b(var2);
      return var2;
   }

   public URLConnection c(ce var1) throws IOException {
      HttpsURLConnection var2 = (HttpsURLConnection)var1.d.openConnection();
      if (!this.a) {
         if (bo.dy == null) {
            bo.dy = new cB();
         }

         cB var3 = bo.dy;
         var2.setSSLSocketFactory(var3);
      }

      this.b(var2);
      return var2;
   }
}
