package net.runelite.client;

import com.google.common.base.Strings;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeConfigLoader implements Supplier<RuntimeConfig> {
   private static final Logger log = LoggerFactory.getLogger(RuntimeConfigLoader.class);
   private final OkHttpClient okHttpClient;
   private final CompletableFuture<RuntimeConfig> configFuture;

   public RuntimeConfigLoader(OkHttpClient okHttpClient) {
      this.okHttpClient = okHttpClient;
      this.configFuture = this.fetch();
   }

   public RuntimeConfig get() {
      try {
         return (RuntimeConfig)this.configFuture.get();
      } catch (ExecutionException | InterruptedException var2) {
         Exception e = var2;
         log.error("error fetching runtime config", e);
         return null;
      }
   }

   @Nullable
   public RuntimeConfig tryGet() {
      try {
         return (RuntimeConfig)this.configFuture.get(0L, TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException | InterruptedException var2) {
         return null;
      }
   }

   CompletableFuture<RuntimeConfig> fetch() {
      final CompletableFuture<RuntimeConfig> future = new CompletableFuture();
      String prop = System.getProperty("runelite.rtconf");
      if (!Strings.isNullOrEmpty(prop)) {
         try {
            String strConf = new String(Files.readAllBytes(Paths.get(prop)), StandardCharsets.UTF_8);
            RuntimeConfig conf = (RuntimeConfig)RuneLiteAPI.GSON.fromJson(strConf, RuntimeConfig.class);
            log.info("Using local runtime config: {}", conf);
            future.complete(conf);
            return future;
         } catch (IOException var5) {
            IOException e = var5;
            throw new RuntimeException("failed to load override runtime config", e);
         }
      } else {
         Request request = (new Request.Builder()).url(RuneLiteProperties.getRuneLiteConfig()).build();
         this.okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
               future.completeExceptionally(e);
            }

            public void onResponse(Call call, Response response) {
               try {
                  Response var9 = response;

                  try {
                     RuntimeConfig config = (RuntimeConfig)RuneLiteAPI.GSON.fromJson(response.body().charStream(), RuntimeConfig.class);
                     future.complete(config);
                  } catch (Throwable var7) {
                     if (response != null) {
                        try {
                           var9.close();
                        } catch (Throwable var6) {
                           var7.addSuppressed(var6);
                        }
                     }

                     throw var7;
                  }

                  if (response != null) {
                     response.close();
                  }
               } catch (Throwable var8) {
                  Throwable ex = var8;
                  future.completeExceptionally(ex);
               }

            }
         });
         return future;
      }
   }
}
