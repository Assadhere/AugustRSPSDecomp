package net.runelite.client.hiscore;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HiscoreClient {
   private static final Logger log = LoggerFactory.getLogger(HiscoreClient.class);
   private final OkHttpClient client;
   private final Gson gson;

   @Inject
   private HiscoreClient(OkHttpClient client, Gson gson) {
      this.client = client;
      this.gson = gson;
   }

   public HiscoreResult lookup(String username) throws IOException {
      return this.lookup(username, HiscoreEndpoint.NORMAL);
   }

   public HiscoreResult lookup(String username, HiscoreEndpoint endpoint) throws IOException {
      return this.lookup(username, endpoint.getHiscoreURL());
   }

   private HiscoreResult lookup(String username, HttpUrl url) throws IOException {
      Response response = this.client.newCall(buildRequest(username, url)).execute();

      HiscoreResult var4;
      try {
         var4 = this.processResponse(username, response);
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
   }

   public CompletableFuture<HiscoreResult> lookupAsync(final String username, HiscoreEndpoint endpoint) {
      final CompletableFuture<HiscoreResult> future = new CompletableFuture();
      this.client.newCall(buildRequest(username, endpoint.getHiscoreURL())).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            future.completeExceptionally(e);
         }

         public void onResponse(Call call, Response response) throws IOException {
            Response var3 = response;

            try {
               future.complete(HiscoreClient.this.processResponse(username, response));
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
      return future;
   }

   private static Request buildRequest(String username, HttpUrl hiscoreUrl) {
      HttpUrl url = hiscoreUrl.newBuilder().addQueryParameter("player", username).build();
      log.debug("Built URL {}", url);
      return (new Request.Builder()).url(url).build();
   }

   private HiscoreResult processResponse(String username, Response response) throws IOException {
      if (!response.isSuccessful()) {
         if (response.code() == 404) {
            return null;
         } else {
            throw new IOException("Error retrieving data from hiscores: " + String.valueOf(response));
         }
      } else {
         HiscoreResponse hiscoreResponse;
         try {
            hiscoreResponse = (HiscoreResponse)this.gson.fromJson(response.body().charStream(), HiscoreResponse.class);
         } catch (JsonSyntaxException var11) {
            JsonSyntaxException ex = var11;
            throw new IOException("Error deserializing hiscore response", ex);
         }

         if (hiscoreResponse == null) {
            throw new IOException("Error retrieving data from hiscores: " + String.valueOf(response));
         } else {
            Map<String, HiscoreSkill> skillMap = (Map)Arrays.stream(HiscoreSkill.values()).collect(Collectors.toMap(HiscoreSkill::getName, Function.identity()));
            ImmutableMap.Builder<HiscoreSkill, Skill> skills = ImmutableMap.builder();
            HiscoreResponse.Skill[] var6 = hiscoreResponse.skills;
            int var7 = var6.length;

            int var8;
            HiscoreSkill s;
            for(var8 = 0; var8 < var7; ++var8) {
               HiscoreResponse.Skill skill = var6[var8];
               s = (HiscoreSkill)skillMap.get(skill.name);
               if (s == null) {
                  log.debug("unknown skill in hiscore: {}", skill.name);
               } else {
                  skills.put(s, new Skill(skill.rank, skill.level, skill.xp));
               }
            }

            HiscoreResponse.Activity[] var13 = hiscoreResponse.activities;
            var7 = var13.length;

            for(var8 = 0; var8 < var7; ++var8) {
               HiscoreResponse.Activity activity = var13[var8];
               s = (HiscoreSkill)skillMap.get(activity.name);
               if (s == null) {
                  log.debug("unknown activity in hiscore: {}", activity.name);
               } else {
                  skills.put(s, new Skill(activity.rank, (int)activity.score, -1L));
               }
            }

            return new HiscoreResult(username, skills.build());
         }
      }
   }
}
