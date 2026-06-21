package net.runelite.client.plugins.loottracker;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import net.runelite.http.api.loottracker.LootRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LootTrackerClient {
   private static final Logger log = LoggerFactory.getLogger(LootTrackerClient.class);
   private UUID uuid;

   @Inject
   private LootTrackerClient() {
   }

   public CompletableFuture<Void> submit(Collection<LootRecord> lootRecords) {
      log.debug("Remote loot submission disabled; kept {} loot records locally", lootRecords.size());
      return CompletableFuture.completedFuture((Object)null);
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }
}
