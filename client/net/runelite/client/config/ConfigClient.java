package net.runelite.client.config;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.http.api.RuneLiteAPI;
import net.runelite.http.api.config.ConfigPatch;
import net.runelite.http.api.config.ConfigPatchResult;
import net.runelite.http.api.config.Configuration;
import net.runelite.http.api.config.Profile;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigClient {
   private static final Logger log = LoggerFactory.getLogger(ConfigClient.class);
   private final OkHttpClient client;
   private final HttpUrl apiBase;
   private final Gson gson;
   private UUID uuid;

   @Inject
   private ConfigClient(OkHttpClient client, @Named("runelite.api.base") HttpUrl apiBase, Gson gson) {
      this.client = client;
      this.apiBase = apiBase;
      this.gson = gson;
   }

   public List<Profile> profiles() throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("config").addPathSegment("v3").addPathSegment("list").build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         List var6;
         label50: {
            InputStream in;
            try {
               if (response.isSuccessful()) {
                  in = response.body().byteStream();
                  Type type = (new TypeToken<List<Profile>>() {
                  }).getType();
                  var6 = (List)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), type);
                  break label50;
               }

               log.error("non-successful response loading profiles: {}", response.code());
               in = null;
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

            return in;
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

   public Configuration get(long profile) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("config").addPathSegment("v3").addPathSegment(Long.toString(profile)).build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         Configuration var7;
         try {
            InputStream in = response.body().byteStream();
            var7 = (Configuration)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), Configuration.class);
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

   public CompletableFuture<ConfigPatchResult> patch(final ConfigPatch patch, long profile) {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("config").addPathSegment("v3").addPathSegment(Long.toString(profile)).build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).patch(RequestBody.create(RuneLiteAPI.JSON, this.gson.toJson(patch))).header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();
      final CompletableFuture<ConfigPatchResult> future = new CompletableFuture();
      this.client.newCall(request).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            ConfigClient.log.warn("Unable to synchronize configuration item", e);
            future.completeExceptionally(e);
         }

         public void onResponse(Call call, Response response) {
            try {
               Response var10 = response;

               try {
                  if (response.code() != 200) {
                     String body = "bad response";

                     try {
                        body = response.body().string();
                     } catch (IOException var7) {
                     }

                     ConfigClient.log.warn("failed to synchronize some of {}/{} configuration values: {}", new Object[]{patch.getEdit().size(), patch.getUnset().size(), body});
                     future.complete((Object)null);
                  } else {
                     ConfigClient.log.debug("Synchronized {}/{} configuration values", patch.getEdit().size(), patch.getUnset().size());
                     future.complete((ConfigPatchResult)ConfigClient.this.gson.fromJson(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8), ConfigPatchResult.class));
                  }
               } catch (Throwable var8) {
                  if (response != null) {
                     try {
                        var10.close();
                     } catch (Throwable var6) {
                        var8.addSuppressed(var6);
                     }
                  }

                  throw var8;
               }

               if (response != null) {
                  response.close();
               }
            } catch (Exception var9) {
               Exception ex = var9;
               future.completeExceptionally(ex);
            }

         }
      });
      return future;
   }

   public void delete(final long profile) {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("config").addPathSegment("v3").addPathSegment(Long.toString(profile)).build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).delete().header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();
      this.client.newCall(request).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            ConfigClient.log.warn("error deleting profile {}", profile, e);
         }

         public void onResponse(Call call, Response response) {
            ConfigClient.log.debug("deleted profile {}", profile);
            response.close();
         }
      });
   }

   public void rename(final long profile, final String name) {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("config").addPathSegment("v3").addPathSegment(Long.toString(profile)).addPathSegment("name").build();
      log.debug("Built URI: {}", url);
      Request request = (new Request.Builder()).post(RequestBody.create((MediaType)null, name)).header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();
      this.client.newCall(request).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            ConfigClient.log.warn("error renaming profile {}", profile, e);
         }

         public void onResponse(Call call, Response response) {
            if (!response.isSuccessful()) {
               ConfigClient.log.debug("unable to rename profile {} to {}", profile, name);
            } else {
               ConfigClient.log.debug("renamed profile {} to {}", profile, name);
            }

            response.close();
         }
      });
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }
}
