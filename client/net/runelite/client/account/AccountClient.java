package net.runelite.client.account;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.http.api.account.OAuthResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountClient {
   private static final Logger log = LoggerFactory.getLogger(AccountClient.class);
   private final OkHttpClient client;
   private final HttpUrl apiBase;
   private final Gson gson;
   private UUID uuid;

   @Inject
   private AccountClient(OkHttpClient client, @Named("runelite.api.base") HttpUrl apiBase, Gson gson) {
      this.client = client;
      this.apiBase = apiBase;
      this.gson = gson;
   }

   public OAuthResponse login(int port) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("account").addPathSegment("login").addQueryParameter("port", Integer.toString(port)).build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         OAuthResponse var6;
         try {
            InputStream in = response.body().byteStream();
            var6 = (OAuthResponse)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), OAuthResponse.class);
         } catch (Throwable var8) {
            if (response != null) {
               try {
                  response.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (response != null) {
            response.close();
         }

         return var6;
      } catch (JsonParseException var9) {
         JsonParseException ex = var9;
         throw new IOException(ex);
      }
   }

   public void logout() throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("account").addPathSegment("logout").build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();
      Response ignored = this.client.newCall(request).execute();

      try {
         log.debug("Sent logout request");
      } catch (Throwable var7) {
         if (ignored != null) {
            try {
               ignored.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignored != null) {
         ignored.close();
      }

   }

   public boolean sessionCheck() {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("account").addPathSegment("session-check").build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         boolean var4;
         try {
            var4 = response.isSuccessful();
         } catch (Throwable var7) {
            if (response != null) {
               try {
                  response.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (response != null) {
            response.close();
         }

         return var4;
      } catch (IOException var8) {
         IOException ex = var8;
         log.debug("Unable to verify session", ex);
         return true;
      }
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }
}
