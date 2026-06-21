package net.runelite.client.plugins.timetracking.farming;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class CompostTracker {
   private static final Logger log = LoggerFactory.getLogger(CompostTracker.class);
   private static final Duration COMPOST_ACTION_TIMEOUT = Duration.ofSeconds(30L);
   private static final Pattern COMPOST_USED_ON_PATCH = Pattern.compile("You treat the .+ with (?<compostType>ultra|super|)compost\\.");
   private static final Pattern FERTILE_SOIL_CAST = Pattern.compile("^The .+ has been treated with (?<compostType>ultra|super|)compost");
   private static final Pattern ALREADY_TREATED = Pattern.compile("This .+ has already been (treated|fertilised) with (?<compostType>ultra|super|)compost(?: - the spell can't make it any more fertile)?\\.");
   private static final Pattern INSPECT_PATCH = Pattern.compile("This is an? .+\\. The soil has been treated with (?<compostType>ultra|super|)compost\\..*");
   private static final ImmutableSet<Integer> COMPOST_ITEMS = ImmutableSet.of(6032, 6034, 21483, 22997);
   private final Client client;
   private final FarmingWorld farmingWorld;
   private final ConfigManager configManager;
   @VisibleForTesting
   final Map<FarmingPatch, PendingCompost> pendingCompostActions = new HashMap();

   private static String configKey(FarmingPatch fp) {
      return fp.configKey() + ".compost";
   }

   public void setCompostState(FarmingPatch fp, CompostState state) {
      log.debug("Storing compost state [{}] for patch [{}]", state, fp);
      if (state == null) {
         this.configManager.unsetRSProfileConfiguration("timetracking", configKey(fp));
      } else {
         this.configManager.setRSProfileConfiguration("timetracking", configKey(fp), state);
      }

   }

   public CompostState getCompostState(FarmingPatch fp) {
      return (CompostState)this.configManager.getRSProfileConfiguration("timetracking", configKey(fp), CompostState.class);
   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked e) {
      if (this.isCompostAction(e)) {
         ObjectComposition patchDef = this.client.getObjectDefinition(e.getId());
         WorldPoint actionLocation = WorldPoint.fromScene(this.client, e.getParam0(), e.getParam1(), this.client.getPlane());
         FarmingPatch targetPatch = (FarmingPatch)this.farmingWorld.getRegionsForLocation(actionLocation).stream().flatMap((fr) -> {
            return Arrays.stream(fr.getPatches());
         }).filter((fp) -> {
            return fp.getVarbit() == patchDef.getVarbitId();
         }).filter((fp) -> {
            return fp.getImplementation() != PatchImplementation.COMPOST && fp.getImplementation() != PatchImplementation.BIG_COMPOST;
         }).findFirst().orElse((Object)null);
         if (targetPatch != null) {
            log.debug("Storing pending compost action for patch [{}]", targetPatch);
            PendingCompost pc = new PendingCompost(Instant.now().plus(COMPOST_ACTION_TIMEOUT), actionLocation, targetPatch);
            this.pendingCompostActions.put(targetPatch, pc);
         }
      }
   }

   private boolean isCompostAction(MenuOptionClicked e) {
      switch (e.getMenuAction()) {
         case WIDGET_TARGET_ON_GAME_OBJECT:
            Widget w = this.client.getSelectedWidget();
            return w != null && (COMPOST_ITEMS.contains(w.getItemId()) || w.getId() == 14286983);
         case GAME_OBJECT_FIRST_OPTION:
         case GAME_OBJECT_SECOND_OPTION:
         case GAME_OBJECT_THIRD_OPTION:
         case GAME_OBJECT_FOURTH_OPTION:
         case GAME_OBJECT_FIFTH_OPTION:
            return "Inspect".equals(e.getMenuOption());
         default:
            return false;
      }
   }

   @Subscribe
   public void onChatMessage(ChatMessage e) {
      if (e.getType() == ChatMessageType.GAMEMESSAGE || e.getType() == ChatMessageType.SPAM) {
         CompostState compostUsed = determineCompostUsed(e.getMessage());
         if (compostUsed != null) {
            this.expirePendingActions();
            this.pendingCompostActions.values().stream().filter(this::playerIsBesidePatch).findFirst().ifPresent((pc) -> {
               this.setCompostState(pc.getFarmingPatch(), compostUsed);
               this.pendingCompostActions.remove(pc.getFarmingPatch());
            });
         }
      }
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged e) {
      switch (e.getGameState()) {
         case LOGGED_IN:
         case LOADING:
            return;
         default:
            this.pendingCompostActions.clear();
      }
   }

   private boolean playerIsBesidePatch(PendingCompost pendingCompost) {
      LocalPoint localPatchLocation = LocalPoint.fromWorld(this.client, pendingCompost.getPatchLocation());
      if (localPatchLocation == null) {
         return false;
      } else {
         int patchVarb = pendingCompost.getFarmingPatch().getVarbit();
         Tile patchTile = this.client.getScene().getTiles()[this.client.getPlane()][localPatchLocation.getSceneX()][localPatchLocation.getSceneY()];
         GameObject patchObject = null;
         GameObject[] var6 = patchTile.getGameObjects();
         int playerX = var6.length;

         int playerY;
         for(playerY = 0; playerY < playerX; ++playerY) {
            GameObject go = var6[playerY];
            if (go != null && this.client.getObjectDefinition(go.getId()).getVarbitId() == patchVarb) {
               patchObject = go;
               break;
            }
         }

         assert patchObject != null;

         WorldPoint playerPos = this.client.getLocalPlayer().getWorldLocation();
         playerX = playerPos.getX();
         playerY = playerPos.getY();
         WorldPoint patchBase = pendingCompost.getPatchLocation();
         int minX = patchBase.getX();
         int minY = patchBase.getY();
         int maxX = minX + patchObject.sizeX() - 1;
         int maxY = minY + patchObject.sizeY() - 1;
         return playerX >= minX - 1 && playerX <= maxX + 1 && playerY >= minY - 1 && playerY <= maxY + 1;
      }
   }

   private void expirePendingActions() {
      this.pendingCompostActions.values().removeIf((e) -> {
         return Instant.now().isAfter(e.getTimeout());
      });
   }

   @VisibleForTesting
   static CompostState determineCompostUsed(String chatMessage) {
      if (!chatMessage.contains("compost")) {
         return null;
      } else {
         Matcher matcher;
         if (!(matcher = COMPOST_USED_ON_PATCH.matcher(chatMessage)).matches() && !(matcher = FERTILE_SOIL_CAST.matcher(chatMessage)).find() && !(matcher = ALREADY_TREATED.matcher(chatMessage)).matches() && !(matcher = INSPECT_PATCH.matcher(chatMessage)).matches()) {
            return null;
         } else {
            switch (matcher.group("compostType")) {
               case "ultra":
                  return CompostState.ULTRACOMPOST;
               case "super":
                  return CompostState.SUPERCOMPOST;
               default:
                  return CompostState.COMPOST;
            }
         }
      }
   }

   @Inject
   public CompostTracker(Client client, FarmingWorld farmingWorld, ConfigManager configManager) {
      this.client = client;
      this.farmingWorld = farmingWorld;
      this.configManager = configManager;
   }

   @VisibleForTesting
   static final class PendingCompost {
      private final Instant timeout;
      private final WorldPoint patchLocation;
      private final FarmingPatch farmingPatch;

      public PendingCompost(Instant timeout, WorldPoint patchLocation, FarmingPatch farmingPatch) {
         this.timeout = timeout;
         this.patchLocation = patchLocation;
         this.farmingPatch = farmingPatch;
      }

      public Instant getTimeout() {
         return this.timeout;
      }

      public WorldPoint getPatchLocation() {
         return this.patchLocation;
      }

      public FarmingPatch getFarmingPatch() {
         return this.farmingPatch;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof PendingCompost)) {
            return false;
         } else {
            PendingCompost other;
            label44: {
               other = (PendingCompost)o;
               Object this$timeout = this.getTimeout();
               Object other$timeout = other.getTimeout();
               if (this$timeout == null) {
                  if (other$timeout == null) {
                     break label44;
                  }
               } else if (this$timeout.equals(other$timeout)) {
                  break label44;
               }

               return false;
            }

            Object this$patchLocation = this.getPatchLocation();
            Object other$patchLocation = other.getPatchLocation();
            if (this$patchLocation == null) {
               if (other$patchLocation != null) {
                  return false;
               }
            } else if (!this$patchLocation.equals(other$patchLocation)) {
               return false;
            }

            Object this$farmingPatch = this.getFarmingPatch();
            Object other$farmingPatch = other.getFarmingPatch();
            if (this$farmingPatch == null) {
               if (other$farmingPatch != null) {
                  return false;
               }
            } else if (!this$farmingPatch.equals(other$farmingPatch)) {
               return false;
            }

            return true;
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $timeout = this.getTimeout();
         result = result * 59 + ($timeout == null ? 43 : $timeout.hashCode());
         Object $patchLocation = this.getPatchLocation();
         result = result * 59 + ($patchLocation == null ? 43 : $patchLocation.hashCode());
         Object $farmingPatch = this.getFarmingPatch();
         result = result * 59 + ($farmingPatch == null ? 43 : $farmingPatch.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = String.valueOf(this.getTimeout());
         return "CompostTracker.PendingCompost(timeout=" + var10000 + ", patchLocation=" + String.valueOf(this.getPatchLocation()) + ", farmingPatch=" + String.valueOf(this.getFarmingPatch()) + ")";
      }
   }
}
