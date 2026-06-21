package net.runelite.client.hiscore;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HiscoreLoader extends CacheLoader<HiscoreManager.HiscoreKey, HiscoreResult> {
   private static final Logger log = LoggerFactory.getLogger(HiscoreLoader.class);
   private final ListeningExecutorService executorService;
   private final HiscoreClient hiscoreClient;

   HiscoreLoader(ScheduledExecutorService executor, HiscoreClient client) {
      this.executorService = MoreExecutors.listeningDecorator(executor);
      this.hiscoreClient = client;
   }

   public HiscoreResult load(HiscoreManager.HiscoreKey hiscoreKey) throws Exception {
      return HiscoreManager.EMPTY;
   }

   public ListenableFuture<HiscoreResult> reload(HiscoreManager.HiscoreKey hiscoreKey, HiscoreResult oldValue) {
      log.debug("Submitting hiscore lookup for {} type {}", hiscoreKey.getUsername(), hiscoreKey.getType());
      return this.executorService.submit(() -> {
         return this.fetch(hiscoreKey);
      });
   }

   private HiscoreResult fetch(HiscoreManager.HiscoreKey hiscoreKey) {
      String username = hiscoreKey.getUsername();
      HiscoreEndpoint endpoint = hiscoreKey.getType();

      try {
         HiscoreResult result = this.hiscoreClient.lookup(username, endpoint);
         return result == null ? HiscoreManager.NONE : result;
      } catch (IOException var5) {
         IOException ex = var5;
         log.warn("Unable to look up hiscore!", ex);
         return HiscoreManager.NONE;
      }
   }
}
