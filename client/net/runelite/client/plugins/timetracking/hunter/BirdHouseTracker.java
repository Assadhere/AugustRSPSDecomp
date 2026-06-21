package net.runelite.client.plugins.timetracking.hunter;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.timetracking.SummaryState;
import net.runelite.client.plugins.timetracking.TimeTrackingConfig;

@Singleton
public class BirdHouseTracker {
   static final int BIRD_HOUSE_DURATION = (int)Duration.ofMinutes(50L).getSeconds();
   private static ImmutableSet<Integer> FOSSIL_ISLAND_REGIONS = ImmutableSet.of(14650, 14651, 14652, 14906, 14907, 15162, new Integer[]{15163});
   private final Client client;
   private final ItemManager itemManager;
   private final ConfigManager configManager;
   private final TimeTrackingConfig config;
   private final Notifier notifier;
   private final ConcurrentMap<BirdHouseSpace, BirdHouseData> birdHouseData = new ConcurrentHashMap();
   private SummaryState summary;
   private long completionTime;

   @Inject
   private BirdHouseTracker(Client client, ItemManager itemManager, ConfigManager configManager, TimeTrackingConfig config, Notifier notifier) {
      this.summary = SummaryState.UNKNOWN;
      this.completionTime = -1L;
      this.client = client;
      this.itemManager = itemManager;
      this.configManager = configManager;
      this.config = config;
      this.notifier = notifier;
   }

   public BirdHouseTabPanel createBirdHouseTabPanel() {
      return new BirdHouseTabPanel(this.configManager, this.itemManager, this, this.config);
   }

   public void loadFromConfig() {
      this.birdHouseData.clear();
      BirdHouseSpace[] var1 = BirdHouseSpace.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BirdHouseSpace space = var1[var3];
         String key = "birdhouse." + space.getVarp();
         String storedValue = this.configManager.getRSProfileConfiguration("timetracking", key);
         if (storedValue != null) {
            String[] parts = storedValue.split(":");
            if (parts.length == 2) {
               try {
                  int varp = Integer.parseInt(parts[0]);
                  long timestamp = Long.parseLong(parts[1]);
                  this.birdHouseData.put(space, new BirdHouseData(space, varp, timestamp));
               } catch (NumberFormatException var11) {
               }
            }
         }
      }

      this.updateCompletionTime();
   }

   public boolean updateData(WorldPoint location) {
      boolean changed = false;
      if (FOSSIL_ISLAND_REGIONS.contains(location.getRegionID()) && location.getPlane() == 0) {
         Map<BirdHouseSpace, BirdHouseData> newData = new HashMap();
         long currentTime = Instant.now().getEpochSecond();
         int removalCount = 0;
         BirdHouseSpace[] var7 = BirdHouseSpace.values();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BirdHouseSpace space = var7[var9];
            int varp = this.client.getVarpValue(space.getVarp());
            BirdHouseData oldData = (BirdHouseData)this.birdHouseData.get(space);
            int oldVarp = oldData == null ? -1 : oldData.getVarp();
            if (varp != oldVarp) {
               newData.put(space, new BirdHouseData(space, varp, currentTime));
               changed = true;
            }

            if (varp <= 0 && oldVarp > 0) {
               ++removalCount;
            }
         }

         if (removalCount > 2) {
            return false;
         }

         if (changed) {
            this.birdHouseData.putAll(newData);
            this.updateCompletionTime();
            this.saveToConfig(newData);
         }
      }

      return changed;
   }

   public boolean checkCompletion() {
      if (this.summary == SummaryState.IN_PROGRESS && this.completionTime < Instant.now().getEpochSecond()) {
         this.summary = SummaryState.COMPLETED;
         this.completionTime = 0L;
         if (Boolean.TRUE.equals(this.configManager.getRSProfileConfiguration("timetracking", "birdHouseNotification", Boolean.TYPE))) {
            this.notifier.notify("Your bird houses are ready to be dismantled.");
         }

         return true;
      } else {
         return false;
      }
   }

   private void updateCompletionTime() {
      if (this.birdHouseData.isEmpty()) {
         this.summary = SummaryState.UNKNOWN;
         this.completionTime = -1L;
      } else {
         boolean allEmpty = true;
         long maxCompletionTime = 0L;
         Iterator var4 = this.birdHouseData.values().iterator();

         while(var4.hasNext()) {
            BirdHouseData data = (BirdHouseData)var4.next();
            BirdHouseState state = BirdHouseState.fromVarpValue(data.getVarp());
            if (state != BirdHouseState.EMPTY) {
               allEmpty = false;
            }

            if (state == BirdHouseState.SEEDED) {
               maxCompletionTime = Math.max(maxCompletionTime, data.getTimestamp() + (long)BIRD_HOUSE_DURATION);
            }
         }

         if (allEmpty) {
            this.summary = SummaryState.EMPTY;
            this.completionTime = 0L;
         } else if (maxCompletionTime <= Instant.now().getEpochSecond()) {
            this.summary = SummaryState.COMPLETED;
            this.completionTime = 0L;
         } else {
            this.summary = SummaryState.IN_PROGRESS;
            this.completionTime = maxCompletionTime;
         }

      }
   }

   private void saveToConfig(Map<BirdHouseSpace, BirdHouseData> updatedData) {
      Iterator var2 = updatedData.values().iterator();

      while(var2.hasNext()) {
         BirdHouseData data = (BirdHouseData)var2.next();
         String key = "birdhouse." + data.getSpace().getVarp();
         ConfigManager var10000 = this.configManager;
         int var10003 = data.getVarp();
         var10000.setRSProfileConfiguration("timetracking", key, "" + var10003 + ":" + data.getTimestamp());
      }

   }

   ConcurrentMap<BirdHouseSpace, BirdHouseData> getBirdHouseData() {
      return this.birdHouseData;
   }

   public SummaryState getSummary() {
      return this.summary;
   }

   public long getCompletionTime() {
      return this.completionTime;
   }
}
