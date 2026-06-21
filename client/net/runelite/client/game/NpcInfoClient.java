package net.runelite.client.game;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NpcInfoClient {
   private static final Logger log = LoggerFactory.getLogger(NpcInfoClient.class);
   private final OkHttpClient client;
   private final HttpUrl staticBase;
   private final Gson gson;

   @Inject
   private NpcInfoClient(OkHttpClient client, @Named("runelite.static.base") HttpUrl staticBase, Gson gson) {
      this.client = client;
      this.staticBase = staticBase;
      this.gson = gson;
   }

   public Map<Integer, NpcInfo> getNpcs() throws IOException {
      HttpUrl.Builder urlBuilder = this.staticBase.newBuilder().addPathSegment("npcs").addPathSegment("npcs.min.json");
      HttpUrl url = urlBuilder.build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         Map var7;
         try {
            if (!response.isSuccessful()) {
               throw new IOException(response.toString());
            }

            InputStream in = response.body().byteStream();
            Type typeToken = (new TypeToken<Map<Integer, NpcInfo>>() {
            }).getType();
            var7 = (Map)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), typeToken);
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

         return var7;
      } catch (JsonParseException var10) {
         JsonParseException ex = var10;
         throw new IOException(ex);
      }
   }
}
