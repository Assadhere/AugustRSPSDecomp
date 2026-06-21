package net.runelite.client;

import java.io.IOException;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class SessionClient {
   private final OkHttpClient client;
   private final HttpUrl sessionUrl;

   @Inject
   private SessionClient(OkHttpClient client, @Named("runelite.session") HttpUrl sessionUrl) {
      this.client = client;
      this.sessionUrl = sessionUrl;
   }

   void ping(UUID uuid, boolean loggedIn) throws IOException {
      HttpUrl url = this.sessionUrl.newBuilder().addPathSegment("ping").addQueryParameter("session", uuid.toString()).addQueryParameter("logged-in", String.valueOf(loggedIn)).build();
      Request request = (new Request.Builder()).post(RequestBody.create((MediaType)null, new byte[0])).url(url).build();
      Response response = this.client.newCall(request).execute();

      try {
         if (!response.isSuccessful()) {
            throw new IOException("Unsuccessful ping");
         }
      } catch (Throwable var9) {
         if (response != null) {
            try {
               response.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (response != null) {
         response.close();
      }

   }
}
