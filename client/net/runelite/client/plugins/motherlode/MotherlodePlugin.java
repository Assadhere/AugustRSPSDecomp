package net.runelite.client.plugins.motherlode;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ItemContainer;
import net.runelite.api.Perspective;
import net.runelite.api.WallObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.loottracker.PluginLootReceived;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.http.api.loottracker.LootRecordType;

@PluginDescriptor(
   name = "Motherlode Mine",
   description = "Show helpful information inside the Motherlode Mine",
   tags = {"pay", "dirt", "mining", "mlm", "skilling", "overlay"},
   enabledByDefault = false
)
public class MotherlodePlugin extends Plugin {
   private static final Set<Integer> MOTHERLODE_MAP_REGIONS = ImmutableSet.of(14679, 14680, 14681, 14935, 14936, 14937, new Integer[]{15191, 15192, 15193});
   private static final Set<Integer> MINE_SPOTS = ImmutableSet.of(26661, 26662, 26663, 26664);
   private static final Set<Integer> MLM_ORE_TYPES = ImmutableSet.of(451, 449, 447, 444, 453, 12012, new Integer[0]);
   private static final Set<Integer> ROCK_OBSTACLES = ImmutableSet.of(26679, 26680);
   private static final int SACK_LARGE_SIZE = 189;
   private static final int SACK_SIZE = 108;
   private static final int UPPER_FLOOR_HEIGHT = -490;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private MotherlodeSceneOverlay sceneOverlay;
   @Inject
   private MotherlodeConfig config;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private EventBus eventBus;
   private boolean inMlm;
   private int curSackSize;
   private boolean shouldUpdateOres;
   private Multiset<Integer> inventorySnapshot;
   private final Set<WallObject> veins = new HashSet();
   private final Set<GameObject> rocks = new HashSet();
   private final Set<GameObject> brokenStruts = new HashSet();

   @Provides
   MotherlodeConfig getConfig(ConfigManager configManager) {
      return (MotherlodeConfig)configManager.getConfig(MotherlodeConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.sceneOverlay);
      this.inMlm = this.checkInMlm();
      if (this.inMlm) {
         this.clientThread.invokeLater(this::refreshSackValues);
      }

   }

   protected void shutDown() {
      this.overlayManager.remove(this.sceneOverlay);
      this.veins.clear();
      this.rocks.clear();
      this.brokenStruts.clear();
   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      if (this.inMlm && event.getVarbitId() == 5558) {
         int lastSackValue = this.curSackSize;
         this.refreshSackValues();
         this.shouldUpdateOres = this.curSackSize < lastSackValue;
         if (this.shouldUpdateOres) {
            ItemContainer itemContainer = this.client.getItemContainer(93);
            if (itemContainer != null) {
               this.inventorySnapshot = HashMultiset.create();
               Arrays.stream(itemContainer.getItems()).filter((item) -> {
                  return MLM_ORE_TYPES.contains(item.getId());
               }).forEach((item) -> {
                  this.inventorySnapshot.add(item.getId(), item.getQuantity());
               });
            }
         }
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (this.inMlm && event.getType() == ChatMessageType.SPAM) {
         switch (event.getMessage()) {
            case "You just found a Diamond!":
               if (this.config.trackGemsFound()) {
                  this.broadcastLootItem(1617);
               }
               break;
            case "You just found a Ruby!":
               if (this.config.trackGemsFound()) {
                  this.broadcastLootItem(1619);
               }
               break;
            case "You just found an Emerald!":
               if (this.config.trackGemsFound()) {
                  this.broadcastLootItem(1621);
               }
               break;
            case "You just found a Sapphire!":
               if (this.config.trackGemsFound()) {
                  this.broadcastLootItem(1623);
               }
         }

      }
   }

   private void broadcastLootItem(int itemId) {
      PluginLootReceived lootEvent = PluginLootReceived.builder().source(this).name("Motherlode Mine").type(LootRecordType.EVENT).items(Collections.singleton(new ItemStack(itemId, 1, this.client.getLocalPlayer().getLocalLocation()))).build();
      this.eventBus.post(lootEvent);
   }

   @Subscribe
   private void onScriptPostFired(ScriptPostFired event) {
      if (event.getScriptId() == 1634) {
         this.recolorSackOverlay();
      }

   }

   private void recolorSackOverlay() {
      ItemContainer inv = this.client.getItemContainer(93);
      if (inv != null) {
         int sackSize = this.client.getVarbitValue(5558);
         boolean sackUpgraded = this.client.getVarbitValue(5556) == 1;
         int sackCapacity = sackUpgraded ? 189 : 108;
         int payDir = inv.count(12011);
         Widget sackSizeWidget = this.client.getWidget(25034757);
         Widget spaceTextWidget = this.client.getWidget(25034758);
         if (sackSizeWidget != null && spaceTextWidget != null) {
            if (payDir >= sackCapacity - sackSize) {
               sackSizeWidget.setTextColor(16711680);
               spaceTextWidget.setTextColor(16711680);
            } else {
               sackSizeWidget.setTextColor(13158600);
               spaceTextWidget.setTextColor(16777215);
            }
         }

      }
   }

   @Subscribe
   public void onWallObjectSpawned(WallObjectSpawned event) {
      if (this.inMlm) {
         WallObject wallObject = event.getWallObject();
         if (MINE_SPOTS.contains(wallObject.getId())) {
            this.veins.add(wallObject);
         }

      }
   }

   @Subscribe
   public void onWallObjectDespawned(WallObjectDespawned event) {
      if (this.inMlm) {
         WallObject wallObject = event.getWallObject();
         this.veins.remove(wallObject);
      }
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      if (this.inMlm) {
         this.addGameObject(event.getGameObject());
      }
   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      if (this.inMlm) {
         this.removeGameObject(event.getGameObject());
      }
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOADING) {
         this.veins.clear();
         this.rocks.clear();
         this.brokenStruts.clear();
         this.inMlm = this.checkInMlm();
      } else if (event.getGameState() == GameState.LOGIN_SCREEN) {
         this.inMlm = false;
      }

   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      if (this.inMlm) {
         this.recolorSackOverlay();
         if (this.shouldUpdateOres && this.inventorySnapshot != null && event.getContainerId() == 93) {
            ItemContainer container = event.getItemContainer();
            Multiset<Integer> current = HashMultiset.create();
            Arrays.stream(container.getItems()).filter((item) -> {
               return MLM_ORE_TYPES.contains(item.getId());
            }).forEach((item) -> {
               current.add(item.getId(), item.getQuantity());
            });
            Multiset<Integer> delta = Multisets.difference(current, this.inventorySnapshot);
            PluginLootReceived lootEvent = PluginLootReceived.builder().source(this).name("Motherlode Mine").type(LootRecordType.EVENT).items((Collection)delta.entrySet().stream().map((e) -> {
               return new ItemStack((Integer)e.getElement(), e.getCount());
            }).collect(Collectors.toList())).build();
            if (this.config.trackOresFound()) {
               this.eventBus.post(lootEvent);
            }

            this.inventorySnapshot = null;
            this.shouldUpdateOres = false;
         }
      }
   }

   private boolean checkInMlm() {
      GameState gameState = this.client.getGameState();
      if (gameState != GameState.LOGGED_IN && gameState != GameState.LOADING) {
         return false;
      } else {
         int[] currentMapRegions = this.client.getMapRegions();
         int[] var3 = currentMapRegions;
         int var4 = currentMapRegions.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int region = var3[var5];
            if (!MOTHERLODE_MAP_REGIONS.contains(region)) {
               return false;
            }
         }

         return true;
      }
   }

   private void refreshSackValues() {
      this.curSackSize = this.client.getVarbitValue(5558);
   }

   boolean isUpstairs(LocalPoint localPoint) {
      return Perspective.getTileHeight(this.client, localPoint, 0) < -490;
   }

   private void addGameObject(GameObject gameObject) {
      if (ROCK_OBSTACLES.contains(gameObject.getId())) {
         this.rocks.add(gameObject);
      }

      if (26670 == gameObject.getId()) {
         this.brokenStruts.add(gameObject);
      }

   }

   private void removeGameObject(GameObject gameObject) {
      this.rocks.remove(gameObject);
      this.brokenStruts.remove(gameObject);
   }

   boolean isInMlm() {
      return this.inMlm;
   }

   Set<WallObject> getVeins() {
      return this.veins;
   }

   Set<GameObject> getRocks() {
      return this.rocks;
   }

   Set<GameObject> getBrokenStruts() {
      return this.brokenStruts;
   }
}
