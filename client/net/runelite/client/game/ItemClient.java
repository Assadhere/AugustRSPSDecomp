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
import net.runelite.http.api.item.ItemPrice;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemClient {
   private static final Logger log = LoggerFactory.getLogger(ItemClient.class);
   private final OkHttpClient client;
   private final HttpUrl staticBase;
   private final Gson gson;

   @Inject
   private ItemClient(OkHttpClient client, @Named("runelite.api.base") HttpUrl apiBase, @Named("runelite.static.base") HttpUrl staticBase, Gson gson) {
      this.client = client;
      this.staticBase = staticBase;
      this.gson = gson;
   }

   public ItemPrice[] getPrices() throws IOException {
      log.debug("Remote item price lookup disabled");
      return new ItemPrice[0];
   }

   public Map<Integer, ItemStats> getStats() throws IOException {
      HttpUrl.Builder urlBuilder = this.staticBase.newBuilder().addPathSegment("item").addPathSegment("stats.ids.min.json");
      HttpUrl url = urlBuilder.build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         Map var7;
         label50: {
            InputStream in;
            try {
               if (response.isSuccessful()) {
                  in = response.body().byteStream();
                  Type typeToken = (new TypeToken<Map<Integer, ItemStats>>() {
                  }).getType();
                  var7 = (Map)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), typeToken);
                  break label50;
               }

               log.warn("Error looking up item stats: {}", response);
               in = null;
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

            return in;
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
