package net.runelite.client.chat;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.http.api.RuneLiteAPI;
import net.runelite.http.api.chat.Duels;
import net.runelite.http.api.chat.LayoutRoom;
import net.runelite.http.api.chat.Task;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatClient {
   private final OkHttpClient client;
   private final HttpUrl apiBase;
   private final Gson gson;

   @Inject
   private ChatClient(OkHttpClient client, @Named("runelite.api.base") HttpUrl apiBase, Gson gson) {
      this.client = client;
      this.apiBase = apiBase;
      this.gson = gson;
   }

   public boolean submitKc(String username, String boss, int kc) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("kc").addQueryParameter("name", username).addQueryParameter("boss", boss).addQueryParameter("kc", Integer.toString(kc)).build();
      Request request = (new Request.Builder()).post(RequestBody.create((MediaType)null, new byte[0])).url(url).build();
      Response response = this.client.newCall(request).execute();

      boolean var7;
      try {
         var7 = response.isSuccessful();
      } catch (Throwable var10) {
         if (response != null) {
            try {
               response.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }
         }

         throw var10;
      }

      if (response != null) {
         response.close();
      }

      return var7;
   }

   public int getKc(String username, String boss) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("kc").addQueryParameter("name", username).addQueryParameter("boss", boss).build();
      Request request = (new Request.Builder()).url(url).build();
      Response response = this.client.newCall(request).execute();

      int var6;
      try {
         if (!response.isSuccessful()) {
            throw new IOException("Unable to look up killcount!");
         }

         var6 = Integer.parseInt(response.body().string());
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

      return var6;
   }

   public boolean submitQp(String username, int qp) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("qp").addQueryParameter("name", username).addQueryParameter("qp", Integer.toString(qp)).build();
      Request request = (new Request.Builder()).post(RequestBody.create((MediaType)null, new byte[0])).url(url).build();
      Response response = this.client.newCall(request).execute();

      boolean var6;
      try {
         var6 = response.isSuccessful();
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

      return var6;
   }

   public int getQp(String username) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("qp").addQueryParameter("name", username).build();
      Request request = (new Request.Builder()).url(url).build();
      Response response = this.client.newCall(request).execute();

      int var5;
      try {
         if (!response.isSuccessful()) {
            throw new IOException("Unable to look up quest points!");
         }

         var5 = Integer.parseInt(response.body().string());
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

      return var5;
   }

   public boolean submitTask(String username, String task, int amount, int initialAmount, String location) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("task").addQueryParameter("name", username).addQueryParameter("task", task).addQueryParameter("amount", Integer.toString(amount)).addQueryParameter("initialAmount", Integer.toString(initialAmount)).addQueryParameter("location", location).build();
      Request request = (new Request.Builder()).post(RequestBody.create((MediaType)null, new byte[0])).url(url).build();
      Response response = this.client.newCall(request).execute();

      boolean var9;
      try {
         var9 = response.isSuccessful();
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

      return var9;
   }

   public Task getTask(String username) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("task").addQueryParameter("name", username).build();
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         Task var6;
         try {
            if (!response.isSuccessful()) {
               throw new IOException("Unable to look up task!");
            }

            InputStream in = response.body().byteStream();
            var6 = (Task)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), Task.class);
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

   public boolean submitPb(String username, String boss, double pb) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("pb").addQueryParameter("name", username).addQueryParameter("boss", boss).addQueryParameter("pb", Double.toString(pb)).build();
      Request request = (new Request.Builder()).post(RequestBody.create((MediaType)null, new byte[0])).url(url).build();
      Response response = this.client.newCall(request).execute();

      boolean var8;
      try {
         var8 = response.isSuccessful();
      } catch (Throwable var11) {
         if (response != null) {
            try {
               response.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }
         }

         throw var11;
      }

      if (response != null) {
         response.close();
      }

      return var8;
   }

   public double getPb(String username, String boss) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("pb").addQueryParameter("name", username).addQueryParameter("boss", boss).build();
      Request request = (new Request.Builder()).url(url).build();
      Response response = this.client.newCall(request).execute();

      double var6;
      try {
         if (!response.isSuccessful()) {
            throw new IOException("Unable to look up personal best!");
         }

         var6 = Double.parseDouble(response.body().string());
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

      return var6;
   }

   public boolean submitGc(String username, int gc) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("gc").addQueryParameter("name", username).addQueryParameter("gc", Integer.toString(gc)).build();
      Request request = (new Request.Builder()).post(RequestBody.create((MediaType)null, new byte[0])).url(url).build();
      Response response = this.client.newCall(request).execute();

      boolean var6;
      try {
         var6 = response.isSuccessful();
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

      return var6;
   }

   public int getGc(String username) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("gc").addQueryParameter("name", username).build();
      Request request = (new Request.Builder()).url(url).build();
      Response response = this.client.newCall(request).execute();

      int var5;
      try {
         if (!response.isSuccessful()) {
            throw new IOException("Unable to look up gamble count!");
         }

         var5 = Integer.parseInt(response.body().string());
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

      return var5;
   }

   public boolean submitDuels(String username, int wins, int losses, int winningStreak, int losingStreak) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("duels").addQueryParameter("name", username).addQueryParameter("wins", Integer.toString(wins)).addQueryParameter("losses", Integer.toString(losses)).addQueryParameter("winningStreak", Integer.toString(winningStreak)).addQueryParameter("losingStreak", Integer.toString(losingStreak)).build();
      Request request = (new Request.Builder()).post(RequestBody.create((MediaType)null, new byte[0])).url(url).build();
      Response response = this.client.newCall(request).execute();

      boolean var9;
      try {
         var9 = response.isSuccessful();
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

      return var9;
   }

   public Duels getDuels(String username) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("duels").addQueryParameter("name", username).build();
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         Duels var6;
         try {
            if (!response.isSuccessful()) {
               throw new IOException("Unable to look up duels!");
            }

            InputStream in = response.body().byteStream();
            var6 = (Duels)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), Duels.class);
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

   public boolean submitLayout(String username, LayoutRoom[] rooms) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("layout").addQueryParameter("name", username).build();
      Request request = (new Request.Builder()).post(RequestBody.create(RuneLiteAPI.JSON, this.gson.toJson(rooms))).url(url).build();
      Response response = this.client.newCall(request).execute();

      boolean var6;
      try {
         var6 = response.isSuccessful();
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

      return var6;
   }

   public LayoutRoom[] getLayout(String username) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("layout").addQueryParameter("name", username).build();
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         LayoutRoom[] var6;
         try {
            if (!response.isSuccessful()) {
               throw new IOException("Unable to look up layout!");
            }

            InputStream in = response.body().byteStream();
            var6 = (LayoutRoom[])this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), LayoutRoom[].class);
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

   public boolean submitPetList(String username, Collection<Integer> petList) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("pets").addQueryParameter("name", username).build();
      Request request = (new Request.Builder()).post(RequestBody.create(RuneLiteAPI.JSON, this.gson.toJson(petList))).url(url).build();
      Response response = this.client.newCall(request).execute();

      boolean var6;
      try {
         var6 = response.isSuccessful();
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

      return var6;
   }

   public Set<Integer> getPetList(String username) throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("chat").addPathSegment("pets").addQueryParameter("name", username).build();
      Request request = (new Request.Builder()).url(url).build();

      try {
         Response response = this.client.newCall(request).execute();

         Set var6;
         try {
            if (!response.isSuccessful()) {
               throw new IOException("Unable to look up pet list!");
            }

            InputStream in = response.body().byteStream();
            var6 = (Set)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), (new TypeToken<Set<Integer>>() {
            }).getType());
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
