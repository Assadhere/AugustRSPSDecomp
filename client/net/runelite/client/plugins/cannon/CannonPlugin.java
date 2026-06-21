package net.runelite.client.plugins.cannon;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@PluginDescriptor(
   name = "Cannon",
   description = "Show information about cannon placement and/or amount of cannonballs",
   tags = {"combat", "notifications", "ranged", "overlay"}
)
public class CannonPlugin extends Plugin {
   static final int MAX_OVERLAY_DISTANCE = 4100;
   private CannonCounter counter;
   private boolean cannonBallNotificationSent;
   private int cballsLeft;
   private boolean cannonPlaced;
   private WorldArea cannonPosition;
   private int cannonWorld = -1;
   private final List<WorldPoint> spotPoints = new ArrayList();
   @Inject
   private ItemManager itemManager;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private Notifier notifier;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private CannonOverlay cannonOverlay;
   @Inject
   private CannonSpotOverlay cannonSpotOverlay;
   @Inject
   private CannonConfig config;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;

   @Provides
   CannonConfig provideConfig(ConfigManager configManager) {
      return (CannonConfig)configManager.getConfig(CannonConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.cannonOverlay);
      this.overlayManager.add(this.cannonSpotOverlay);
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.cballsLeft = this.client.getVarpValue(3);
      }

   }

   protected void shutDown() throws Exception {
      this.cannonSpotOverlay.setHidden(true);
      this.overlayManager.remove(this.cannonOverlay);
      this.overlayManager.remove(this.cannonSpotOverlay);
      this.cannonPlaced = false;
      this.cannonWorld = -1;
      this.cannonPosition = null;
      this.cannonBallNotificationSent = false;
      this.cballsLeft = 0;
      this.removeCounter();
      this.spotPoints.clear();
   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      if (event.getItemContainer() == this.client.getItemContainer(93)) {
         boolean hasBase = false;
         boolean hasStand = false;
         boolean hasBarrels = false;
         boolean hasFurnace = false;
         boolean hasAll = false;
         if (!this.cannonPlaced) {
            Item[] var7 = event.getItemContainer().getItems();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               Item item = var7[var9];
               if (item != null) {
                  switch (item.getId()) {
                     case 6:
                     case 26520:
                        hasBase = true;
                        break;
                     case 8:
                     case 26522:
                        hasStand = true;
                        break;
                     case 10:
                     case 26524:
                        hasBarrels = true;
                        break;
                     case 12:
                     case 26526:
                        hasFurnace = true;
                  }

                  if (hasBase && hasStand && hasBarrels && hasFurnace) {
                     hasAll = true;
                     break;
                  }
               }
            }
         }

         this.cannonSpotOverlay.setHidden(!hasAll);
      }
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("cannon")) {
         if (!this.config.showInfobox()) {
            this.removeCounter();
         } else if (this.cannonPlaced) {
            this.clientThread.invoke(this::addCounter);
         }
      }

   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged gameStateChanged) {
      if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
         this.spotPoints.clear();
         Iterator var2 = CannonSpots.getCannonSpots().iterator();

         while(var2.hasNext()) {
            WorldPoint spot = (WorldPoint)var2.next();
            if (WorldPoint.isInScene(this.client, spot.getX(), spot.getY())) {
               this.spotPoints.add(spot);
            }
         }

      }
   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged varbitChanged) {
      if (varbitChanged.getVarpId() == 3) {
         int old = this.cballsLeft;
         this.cballsLeft = varbitChanged.getValue();
         if (this.cballsLeft > old) {
            this.cannonBallNotificationSent = false;
         }

         if (!this.cannonBallNotificationSent && this.cballsLeft > 0 && this.config.lowWarningThreshold() >= this.cballsLeft) {
            this.notifier.notify(this.config.showCannonNotifications(), String.format("Your cannon has %d cannon balls remaining!", this.cballsLeft));
            this.cannonBallNotificationSent = true;
         }
      } else if (varbitChanged.getVarpId() == 4) {
         WorldPoint c = WorldPoint.fromCoord(varbitChanged.getValue());
         this.cannonPosition = buildCannonWorldArea(c);
      } else if (varbitChanged.getVarpId() == 2) {
         this.cannonPlaced = varbitChanged.getValue() == 4;
         if (this.cannonPlaced) {
            this.addCounter();
         } else {
            this.removeCounter();
         }
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
         if (event.getMessage().equals("You add the furnace.")) {
            this.cannonWorld = this.client.getWorld();
         } else if (event.getMessage().contains("Your cannon is out of ammo!")) {
            this.notifier.notify(this.config.showCannonNotifications(), "Your cannon is out of ammo!");
         }

      }
   }

   Color getStateColor() {
      if (this.cballsLeft > 15) {
         return Color.green;
      } else {
         return this.cballsLeft > 5 ? Color.orange : Color.red;
      }
   }

   private void addCounter() {
      if (this.config.showInfobox() && this.counter == null) {
         this.counter = new CannonCounter(this.itemManager.getImage(2), this);
         this.counter.setTooltip("Cannonballs");
         this.infoBoxManager.addInfoBox(this.counter);
      }
   }

   private void removeCounter() {
      if (this.counter != null) {
         this.infoBoxManager.removeInfoBox(this.counter);
         this.counter = null;
      }
   }

   private static WorldArea buildCannonWorldArea(WorldPoint worldPoint) {
      return new WorldArea(worldPoint.getX(), worldPoint.getY(), 3, 3, worldPoint.getPlane());
   }

   public int getCballsLeft() {
      return this.cballsLeft;
   }

   public boolean isCannonPlaced() {
      return this.cannonPlaced;
   }

   public WorldArea getCannonPosition() {
      return this.cannonPosition;
   }

   public int getCannonWorld() {
      return this.cannonWorld;
   }

   public List<WorldPoint> getSpotPoints() {
      return this.spotPoints;
   }
}
