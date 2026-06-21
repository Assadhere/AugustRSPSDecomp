package net.runelite.client.plugins.xtea;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.http.api.RuneLiteAPI;
import net.runelite.http.api.xtea.XteaKey;
import net.runelite.http.api.xtea.XteaRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XteaClient {
   private static final Logger log = LoggerFactory.getLogger(XteaClient.class);
   private final OkHttpClient client;
   private final HttpUrl apiBase;
   private final Gson gson;

   @Inject
   private XteaClient(OkHttpClient client, @Named("runelite.api.base") HttpUrl apiBase, Gson gson) {
      this.client = client;
      this.apiBase = apiBase;
      this.gson = gson;
   }

   public void submit(XteaRequest xteaRequest) {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("xtea").build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).post(RequestBody.create(RuneLiteAPI.JSON, this.gson.toJson(xteaRequest))).url(url).build();
      this.client.newCall(request).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            XteaClient.log.warn("unable to submit xtea keys", e);
         }

         public void onResponse(Call call, Response response) {
            Response var3 = response;

            try {
               if (!response.isSuccessful()) {
                  XteaClient.log.debug("unsuccessful xtea response");
               }
            } catch (Throwable var7) {
               if (response != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (response != null) {
               response.close();
            }

         }
      });
   }

   public List<XteaKey> get() throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("xtea").build();
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         List var5;
         try {
            InputStream in = response.body().byteStream();
            var5 = (List)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), (new TypeToken<List<XteaKey>>() {
            }).getType());
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

         return var5;
      } catch (JsonParseException var8) {
         JsonParseException ex = var8;
         throw new IOException(ex);
      }
   }

   public XteaKey get(int region) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("xtea").addPathSegment(Integer.toString(region)).build();
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         XteaKey var6;
         try {
            InputStream in = response.body().byteStream();
            var6 = (XteaKey)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), XteaKey.class);
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
}
