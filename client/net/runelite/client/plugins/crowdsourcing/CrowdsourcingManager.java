package net.runelite.client.plugins.crowdsourcing;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class CrowdsourcingManager {
   private static final Logger log = LoggerFactory.getLogger(CrowdsourcingManager.class);
   private static final String CROWDSOURCING_BASE = "https://crowdsource.runescape.wiki/runelite";
   private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
   @Inject
   private OkHttpClient okHttpClient;
   @Inject
   private Gson gson;
   private List<Object> data = new ArrayList();

   public void storeEvent(Object event) {
      synchronized(this) {
         this.data.add(event);
      }
   }

   protected void submitToAPI() {
      List temp;
      synchronized(this) {
         if (this.data.isEmpty()) {
            return;
         }

         temp = this.data;
         this.data = new ArrayList();
      }

      Request r = (new Request.Builder()).url("https://crowdsource.runescape.wiki/runelite").post(RequestBody.create(JSON, this.gson.toJson(temp))).build();
      this.okHttpClient.newCall(r).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            CrowdsourcingManager.log.debug("Error sending crowdsourcing data", e);
         }

         public void onResponse(Call call, Response response) {
            CrowdsourcingManager.log.debug("Successfully sent crowdsourcing data");
            response.close();
         }
      });
   }
}
