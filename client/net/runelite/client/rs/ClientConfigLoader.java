package net.runelite.client.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class ClientConfigLoader {
   private final OkHttpClient okHttpClient;

   RSConfig fetch(HttpUrl url) throws IOException {
      Request request = (new Request.Builder()).url(url).build();
      RSConfig config = new RSConfig();
      Response response = this.okHttpClient.newCall(request).execute();

      try {
         if (!response.isSuccessful()) {
            throw new IOException("Unsuccessful response: " + response.message());
         }

         BufferedReader in = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));

         String str;
         while((str = in.readLine()) != null) {
            int idx = str.indexOf(61);
            if (idx != -1) {
               switch (str.substring(0, idx)) {
                  case "param":
                     str = str.substring(idx + 1);
                     idx = str.indexOf(61);
                     s = str.substring(0, idx);
                     config.getAppletProperties().put(s, str.substring(idx + 1));
                  case "msg":
                     break;
                  default:
                     config.getClassLoaderProperties().put(s, str.substring(idx + 1));
               }
            }
         }
      } catch (Throwable var12) {
         if (response != null) {
            try {
               response.close();
            } catch (Throwable var11) {
               var12.addSuppressed(var11);
            }
         }

         throw var12;
      }

      if (response != null) {
         response.close();
      }

      return config;
   }

   public ClientConfigLoader(OkHttpClient okHttpClient) {
      this.okHttpClient = okHttpClient;
   }
}
