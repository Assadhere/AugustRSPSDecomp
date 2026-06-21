package net.runelite.client.plugins.timetracking.farming;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Singleton;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.WidgetNode;
import net.runelite.api.WorldType;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneScapeProfile;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.timetracking.SummaryState;
import net.runelite.client.plugins.timetracking.Tab;
import net.runelite.client.plugins.timetracking.TimeTrackingConfig;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class FarmingTracker {
   private static final Logger log = LoggerFactory.getLogger(FarmingTracker.class);
   private final Client client;
   private final ItemManager itemManager;
   private final ConfigManager configManager;
   private final TimeTrackingConfig config;
   private final FarmingWorld farmingWorld;
   private final Notifier notifier;
   private final CompostTracker compostTracker;
   private final PaymentTracker paymentTracker;
   private final Map<Tab, SummaryState> summaries = new EnumMap(Tab.class);
   private final Map<Tab, Long> completionTimes = new EnumMap(Tab.class);
   Map<ProfilePatch, Boolean> wasNotified = new HashMap();
   private boolean newRegionLoaded;
   private Collection<FarmingRegion> lastRegions;
   private boolean firstNotifyCheck = true;

   public FarmingTabPanel createTabPanel(Tab tab, FarmingContractManager farmingContractManager) {
      return new FarmingTabPanel(this, this.compostTracker, this.paymentTracker, this.itemManager, this.configManager, this.config, (Set)this.farmingWorld.getTabs().get(tab), farmingContractManager);
   }

   public boolean updateData(WorldPoint location, int timeSinceModalClose) {
      boolean changed = false;
      Iterator var4 = this.client.getComponentTable().iterator();

      while(var4.hasNext()) {
         WidgetNode widgetNode = (WidgetNode)var4.next();
         if (widgetNode.getModalMode() != 1) {
            return false;
         }
      }

      String autoweed = Integer.toString(this.client.getVarbitValue(5557));
      if (!autoweed.equals(this.configManager.getRSProfileConfiguration("timetracking", "autoweed"))) {
         this.configManager.setRSProfileConfiguration("timetracking", "autoweed", autoweed);
         changed = true;
      }

      Collection<FarmingRegion> newRegions = this.farmingWorld.getRegionsForLocation(location);
      if (!newRegions.equals(this.lastRegions)) {
         this.newRegionLoaded = true;
         log.debug("New region loaded. {} at {} ticks", newRegions.toString(), this.client.getTickCount());
      }

      Iterator var28 = newRegions.iterator();

      while(var28.hasNext()) {
         FarmingRegion region = (FarmingRegion)var28.next();
         long unixNow = Instant.now().getEpochSecond();
         FarmingPatch[] var9 = region.getPatches();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            FarmingPatch patch = var9[var11];
            int varbit = patch.getVarbit();
            String key = patch.configKey();
            String strVarbit = Integer.toString(this.client.getVarbitValue(varbit));
            String storedValue = this.configManager.getRSProfileConfiguration("timetracking", key);
            PatchState currentPatchState = patch.getImplementation().forVarbitValue(this.client.getVarbitValue(varbit));
            if (currentPatchState != null) {
               if (storedValue != null) {
                  String[] parts = storedValue.split(":");
                  if (parts.length == 2) {
                     if (parts[0].equals(strVarbit)) {
                        long unixTime = 0L;

                        try {
                           unixTime = Long.parseLong(parts[1]);
                        } catch (NumberFormatException var25) {
                        }

                        if (unixTime + 300L > unixNow && unixNow + 30L > unixTime) {
                           continue;
                        }
                     } else if (!this.newRegionLoaded && timeSinceModalClose > 1) {
                        PatchState previousPatchState = patch.getImplementation().forVarbitValue(Integer.parseInt(parts[0]));
                        if (previousPatchState == null) {
                           continue;
                        }

                        int patchTickRate = previousPatchState.getTickRate();
                        if (this.isLeaguesWorld()) {
                           patchTickRate /= 5;
                        }

                        if (this.isObservedGrowthTick(previousPatchState, currentPatchState)) {
                           Integer storedOffsetPrecision = (Integer)this.configManager.getRSProfileConfiguration("timetracking", "farmTickOffsetPrecision", Integer.TYPE);
                           Integer storedOffsetMins = (Integer)this.configManager.getRSProfileConfiguration("timetracking", "farmTickOffset", Integer.TYPE);
                           int offsetMins = (int)Math.abs(Instant.now().getEpochSecond() / 60L % (long)patchTickRate - (long)patchTickRate);
                           log.debug("Observed an exact growth tick. Offset is: {} from a {} minute tick", offsetMins, patchTickRate);
                           if (storedOffsetMins != null && storedOffsetMins != 0 && offsetMins != storedOffsetMins % patchTickRate) {
                              WorldPoint playerLocation = this.client.getLocalPlayer().getWorldLocation();
                              log.error("Offset error! Observed new offset of {}, previous observed offset was {} ({}) Player Loc:{}", new Object[]{offsetMins, storedOffsetMins, storedOffsetMins % patchTickRate, playerLocation});
                           }

                           if (storedOffsetPrecision == null || patchTickRate >= storedOffsetPrecision) {
                              log.debug("Found a longer growth tick {}, saving new offset", patchTickRate);
                              this.configManager.setRSProfileConfiguration("timetracking", "farmTickOffsetPrecision", patchTickRate);
                              this.configManager.setRSProfileConfiguration("timetracking", "farmTickOffset", offsetMins);
                           }
                        }

                        if (currentPatchState.getTickRate() != 0 && (previousPatchState.getCropState() != CropState.GROWING || currentPatchState.getCropState() != CropState.HARVESTABLE || !currentPatchState.getProduce().getPatchImplementation().isHealthCheckRequired())) {
                           this.wasNotified.put(new ProfilePatch(patch, this.configManager.getRSProfileKey()), false);
                        }
                     } else {
                        log.debug("ignoring growth tick for offset calculation; newRegionLoaded={} timeSinceModalClose={}", this.newRegionLoaded, timeSinceModalClose);
                     }
                  }
               }

               if (currentPatchState.getCropState() == CropState.DEAD || currentPatchState.getCropState() == CropState.HARVESTABLE || currentPatchState.getCropState() == CropState.EMPTY) {
                  this.compostTracker.setCompostState(patch, (CompostState)null);
                  this.paymentTracker.setProtectedState(patch, false);
               }

               String value = strVarbit + ":" + unixNow;
               this.configManager.setRSProfileConfiguration("timetracking", key, value);
               changed = true;
            }
         }
      }

      this.newRegionLoaded = false;
      this.lastRegions = newRegions;
      if (changed) {
         this.updateCompletionTime();
      }

      return changed;
   }

   private boolean isObservedGrowthTick(PatchState previous, PatchState current) {
      int patchTickRate = previous.getTickRate();
      CropState previousCropState = previous.getCropState();
      CropState currentCropState = current.getCropState();
      Produce previousProduce = previous.getProduce();
      if (this.isLeaguesWorld()) {
         patchTickRate /= 5;
      }

      if (previousProduce != Produce.WEEDS && current.getProduce() != Produce.WEEDS && current.getProduce() == previousProduce && patchTickRate > 0) {
         if (previousCropState == CropState.GROWING) {
            if (currentCropState == CropState.GROWING && current.getStage() - previous.getStage() == 1 || currentCropState == CropState.DISEASED) {
               log.debug("Found GROWING -> GROWING or GROWING -> DISEASED");
               return true;
            }

            if (currentCropState == CropState.HARVESTABLE && !previousProduce.getPatchImplementation().isHealthCheckRequired()) {
               log.debug("Found GROWING -> HARVESTABLE");
               return true;
            }
         }

         if (previousCropState == CropState.DISEASED && currentCropState == CropState.DEAD) {
            log.debug("Found DISEASED -> DEAD");
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Nullable
   public PatchPrediction predictPatch(FarmingPatch patch) {
      return this.predictPatch(patch, this.configManager.getRSProfileKey());
   }

   @Nullable
   public PatchPrediction predictPatch(FarmingPatch patch, String profile) {
      long unixNow = Instant.now().getEpochSecond();
      boolean autoweed = Integer.toString(Autoweed.ON.ordinal()).equals(this.configManager.getConfiguration("timetracking", profile, "autoweed"));
      String key = patch.configKey();
      String storedValue = this.configManager.getConfiguration("timetracking", profile, key);
      if (storedValue == null) {
         return null;
      } else {
         long unixTime = 0L;
         int value = 0;
         String[] parts = storedValue.split(":");
         if (parts.length == 2) {
            try {
               value = Integer.parseInt(parts[0]);
               unixTime = Long.parseLong(parts[1]);
            } catch (NumberFormatException var22) {
            }
         }

         if (unixTime <= 0L) {
            return null;
         } else {
            PatchState state = patch.getImplementation().forVarbitValue(value);
            if (state == null) {
               return null;
            } else {
               int stage = state.getStage();
               int stages = state.getStages();
               int tickrate = state.getTickRate();
               if (this.isLeaguesWorld()) {
                  tickrate /= 5;
               }

               if (autoweed && state.getProduce() == Produce.WEEDS) {
                  stage = 0;
                  stages = 1;
                  tickrate = 0;
               }

               long doneEstimate = 0L;
               if (tickrate > 0) {
                  long tickNow = this.getTickTime(tickrate, 0, unixNow, profile);
                  long tickTime = this.getTickTime(tickrate, 0, unixTime, profile);
                  int delta = (int)(tickNow - tickTime) / (tickrate * 60);
                  doneEstimate = this.getTickTime(tickrate, stages - 1 - stage, tickTime, profile);
                  stage += delta;
                  if (stage >= stages) {
                     stage = stages - 1;
                  }
               }

               return new PatchPrediction(state.getProduce(), state.getCropState(), doneEstimate, stage, stages);
            }
         }
      }
   }

   public long getTickTime(int tickRate, int ticks) {
      return this.getTickTime(tickRate, ticks, Instant.now().getEpochSecond(), this.configManager.getRSProfileKey());
   }

   public long getTickTime(int tickRate, int ticks, long requestedTime, String profile) {
      Integer offsetPrecisionMins = (Integer)this.configManager.getConfiguration((String)"timetracking", profile, "farmTickOffsetPrecision", (Type)Integer.TYPE);
      Integer offsetTimeMins = (Integer)this.configManager.getConfiguration((String)"timetracking", profile, "farmTickOffset", (Type)Integer.TYPE);
      long calculatedOffsetTime = 0L;
      if (offsetPrecisionMins != null && offsetTimeMins != null && (offsetPrecisionMins >= tickRate || offsetPrecisionMins >= 40)) {
         calculatedOffsetTime = (long)(offsetTimeMins % tickRate * 60);
      }

      long unixNow = requestedTime + calculatedOffsetTime;
      long timeOfCurrentTick = unixNow - unixNow % (long)(tickRate * 60);
      long timeOfGoalTick = timeOfCurrentTick + (long)(ticks * tickRate * 60);
      return timeOfGoalTick - calculatedOffsetTime;
   }

   public void loadCompletionTimes() {
      this.summaries.clear();
      this.completionTimes.clear();
      this.lastRegions = null;
      this.updateCompletionTime();
   }

   public SummaryState getSummary(Tab patchType) {
      SummaryState summary = (SummaryState)this.summaries.get(patchType);
      return summary == null ? SummaryState.UNKNOWN : summary;
   }

   public long getCompletionTime(Tab patchType) {
      Long completionTime = (Long)this.completionTimes.get(patchType);
      return completionTime == null ? -1L : completionTime;
   }

   private void updateCompletionTime() {
      Iterator var1 = this.farmingWorld.getTabs().entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry<Tab, Set<FarmingPatch>> tab = (Map.Entry)var1.next();
         long extremumCompletionTime = this.config.preferSoonest() ? Long.MAX_VALUE : 0L;
         boolean allUnknown = true;
         boolean allEmpty = true;
         Iterator var7 = ((Set)tab.getValue()).iterator();

         while(var7.hasNext()) {
            FarmingPatch patch = (FarmingPatch)var7.next();
            PatchPrediction prediction = this.predictPatch(patch);
            if (prediction != null && prediction.getProduce().getItemID() >= 0) {
               allUnknown = false;
               if (prediction.getProduce() != Produce.WEEDS && prediction.getProduce() != Produce.SCARECROW) {
                  allEmpty = false;
                  if (this.config.preferSoonest()) {
                     extremumCompletionTime = Math.min(extremumCompletionTime, prediction.getDoneEstimate());
                  } else {
                     extremumCompletionTime = Math.max(extremumCompletionTime, prediction.getDoneEstimate());
                  }
               }
            }
         }

         SummaryState state;
         long completionTime;
         if (allUnknown) {
            state = SummaryState.UNKNOWN;
            completionTime = -1L;
         } else if (allEmpty) {
            state = SummaryState.EMPTY;
            completionTime = -1L;
         } else if (extremumCompletionTime <= Instant.now().getEpochSecond()) {
            state = SummaryState.COMPLETED;
            completionTime = 0L;
         } else {
            state = SummaryState.IN_PROGRESS;
            completionTime = extremumCompletionTime;
         }

         this.summaries.put((Tab)tab.getKey(), state);
         this.completionTimes.put((Tab)tab.getKey(), completionTime);
      }

   }

   public void checkCompletion() {
      List<RuneScapeProfile> rsProfiles = this.configManager.getRSProfiles();
      long unixNow = Instant.now().getEpochSecond();
      Iterator var4 = rsProfiles.iterator();

      while(var4.hasNext()) {
         RuneScapeProfile profile = (RuneScapeProfile)var4.next();
         Integer offsetPrecisionMins = (Integer)this.configManager.getConfiguration((String)"timetracking", profile.getKey(), "farmTickOffsetPrecision", (Type)Integer.TYPE);
         Integer offsetTimeMins = (Integer)this.configManager.getConfiguration((String)"timetracking", profile.getKey(), "farmTickOffset", (Type)Integer.TYPE);
         Iterator var8 = this.farmingWorld.getTabs().entrySet().iterator();

         label68:
         while(var8.hasNext()) {
            Map.Entry<Tab, Set<FarmingPatch>> tab = (Map.Entry)var8.next();
            Iterator var10 = ((Set)tab.getValue()).iterator();

            while(true) {
               FarmingPatch patch;
               ProfilePatch profilePatch;
               boolean patchNotified;
               boolean shouldNotify;
               PatchPrediction prediction;
               int tickRate;
               do {
                  do {
                     do {
                        do {
                           if (!var10.hasNext()) {
                              continue label68;
                           }

                           patch = (FarmingPatch)var10.next();
                           profilePatch = new ProfilePatch(patch, profile.getKey());
                           patchNotified = (Boolean)this.wasNotified.getOrDefault(profilePatch, false);
                           String configKey = patch.notifyConfigKey();
                           shouldNotify = Boolean.TRUE.equals(this.configManager.getConfiguration((String)"timetracking", profile.getKey(), configKey, (Type)Boolean.class));
                           prediction = this.predictPatch(patch, profile.getKey());
                        } while(prediction == null);

                        tickRate = prediction.getProduce().getTickrate();
                        if (this.isLeaguesWorld()) {
                           tickRate /= 5;
                        }
                     } while(offsetPrecisionMins == null);
                  } while(offsetTimeMins == null);
               } while(offsetPrecisionMins < tickRate && offsetPrecisionMins < 40);

               if (prediction.getProduce() != Produce.WEEDS && unixNow > prediction.getDoneEstimate() && !patchNotified && prediction.getCropState() != CropState.FILLING && prediction.getCropState() != CropState.EMPTY) {
                  this.wasNotified.put(profilePatch, true);
                  if (!this.firstNotifyCheck && shouldNotify) {
                     this.sendNotification(profile, prediction, patch);
                  }
               }
            }
         }
      }

      this.firstNotifyCheck = false;
   }

   private boolean isLeaguesWorld() {
      EnumSet<WorldType> worldTypes = this.client.getWorldType();
      return worldTypes.contains(WorldType.SEASONAL) && !worldTypes.contains(WorldType.DEADMAN);
   }

   @VisibleForTesting
   void sendNotification(RuneScapeProfile profile, PatchPrediction prediction, FarmingPatch patch) {
      RuneScapeProfileType profileType = profile.getType();
      StringBuilder stringBuilder = new StringBuilder();
      if (this.client.getGameState() == GameState.LOGGED_IN && profile.getDisplayName().equals(this.client.getLocalPlayer().getName())) {
         if (profileType != RuneScapeProfileType.getCurrent(this.client)) {
            stringBuilder.append('(').append(Text.titleCase(profile.getType())).append(") ");
         }
      } else if (profileType == RuneScapeProfileType.getCurrent(this.client) && this.client.getGameState() != GameState.LOGIN_SCREEN) {
         stringBuilder.append('(').append(profile.getDisplayName()).append(") ");
      } else if (this.client.getGameState() == GameState.LOGIN_SCREEN && profileType == RuneScapeProfileType.STANDARD) {
         stringBuilder.append('(').append(profile.getDisplayName()).append(") ");
      } else {
         stringBuilder.append('(').append(profile.getDisplayName()).append(" - ").append(Text.titleCase(profile.getType())).append(") ");
      }

      stringBuilder.append("Your ").append(prediction.getProduce().getName());
      switch (prediction.getCropState()) {
         case HARVESTABLE:
         case GROWING:
            if (prediction.getProduce().getName().toLowerCase(Locale.ENGLISH).contains("compost")) {
               stringBuilder.append(" is ready to collect in ");
            } else {
               stringBuilder.append(" is ready to harvest in ");
            }
            break;
         case DISEASED:
            stringBuilder.append(" has become diseased in ");
            break;
         case DEAD:
            stringBuilder.append(" has died in ");
            break;
         default:
            throw new IllegalStateException();
      }

      stringBuilder.append(patch.getRegion().isDefinite() ? "the " : "").append(patch.getRegion().getName()).append('.');
      this.notifier.notify(stringBuilder.toString());
   }

   @Inject
   private FarmingTracker(Client client, ItemManager itemManager, ConfigManager configManager, TimeTrackingConfig config, FarmingWorld farmingWorld, Notifier notifier, CompostTracker compostTracker, PaymentTracker paymentTracker) {
      this.client = client;
      this.itemManager = itemManager;
      this.configManager = configManager;
      this.config = config;
      this.farmingWorld = farmingWorld;
      this.notifier = notifier;
      this.compostTracker = compostTracker;
      this.paymentTracker = paymentTracker;
   }
}
