package net.runelite.client.plugins.driftnet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Drift Net",
   description = "Display information about drift nets",
   tags = {"hunter", "fishing", "drift", "net"},
   enabledByDefault = false
)
public class DriftNetPlugin extends Plugin {
   static final String CONFIG_GROUP = "driftnet";
   private static final int UNDERWATER_REGION = 15008;
   private static final String CHAT_PRODDING_FISH = "You prod at the shoal of fish to scare it.";
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private DriftNetConfig config;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private DriftNetOverlay overlay;
   private final Set<NPC> fish = new HashSet();
   private final Map<NPC, Integer> taggedFish = new HashMap();
   private final List<DriftNet> NETS = ImmutableList.of(new DriftNet(31433, 5812, 5813, ImmutableSet.of(new WorldPoint(3746, 10297, 1), new WorldPoint(3747, 10297, 1), new WorldPoint(3748, 10297, 1), new WorldPoint(3749, 10297, 1))), new DriftNet(31434, 5814, 5815, ImmutableSet.of(new WorldPoint(3742, 10288, 1), new WorldPoint(3742, 10289, 1), new WorldPoint(3742, 10290, 1), new WorldPoint(3742, 10291, 1), new WorldPoint(3742, 10292, 1))));
   private boolean inDriftNetArea;
   private boolean armInteraction;
   private boolean driftNetsInInventory;
   private GameObject annette;

   @Provides
   DriftNetConfig provideConfig(ConfigManager configManager) {
      return (DriftNetConfig)configManager.getConfig(DriftNetConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.overlay);
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.clientThread.invokeLater(() -> {
            this.inDriftNetArea = this.checkArea();
            this.updateDriftNetVarbits();
         });
      }

   }

   protected void shutDown() {
      this.overlayManager.remove(this.overlay);
      this.reset();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() != GameState.LOGGED_IN) {
         this.annette = null;
      }

      switch (event.getGameState()) {
         case LOGIN_SCREEN:
         case HOPPING:
         case LOADING:
            this.reset();
            break;
         case LOGGED_IN:
            this.inDriftNetArea = this.checkArea();
            this.updateDriftNetVarbits();
      }

   }

   private void reset() {
      this.fish.clear();
      this.taggedFish.clear();
      this.armInteraction = false;
      this.inDriftNetArea = false;
   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      this.updateDriftNetVarbits();
   }

   private void updateDriftNetVarbits() {
      if (this.inDriftNetArea) {
         Iterator var1 = this.NETS.iterator();

         while(var1.hasNext()) {
            DriftNet net = (DriftNet)var1.next();
            DriftNetStatus status = DriftNetStatus.of(this.client.getVarbitValue(net.getStatusVarbit()));
            int count = this.client.getVarbitValue(net.getCountVarbit());
            net.setStatus(status);
            net.setCount(count);
         }

      }
   }

   @Subscribe
   public void onInteractingChanged(InteractingChanged event) {
      if (this.armInteraction && event.getSource() == this.client.getLocalPlayer() && event.getTarget() instanceof NPC && ((NPC)event.getTarget()).getId() == 7782) {
         this.tagFish(event.getTarget());
         this.armInteraction = false;
      }

   }

   private boolean isFishNextToNet(NPC fish, Collection<DriftNet> nets) {
      WorldPoint fishTile = WorldPoint.fromLocalInstance(this.client, fish.getLocalLocation());
      return nets.stream().anyMatch((net) -> {
         return net.getAdjacentTiles().contains(fishTile);
      });
   }

   private boolean isTagExpired(Integer tick) {
      return tick + this.config.timeoutDelay() < this.client.getTickCount();
   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      if (this.inDriftNetArea) {
         List<DriftNet> closedNets = (List)this.NETS.stream().filter(DriftNet::isNotAcceptingFish).collect(Collectors.toList());
         this.taggedFish.entrySet().removeIf((entry) -> {
            return this.isTagExpired((Integer)entry.getValue()) || this.isFishNextToNet((NPC)entry.getKey(), closedNets);
         });
         this.NETS.forEach((net) -> {
            net.setPrevTickStatus(net.getStatus());
         });
         this.armInteraction = false;
      }
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (this.inDriftNetArea) {
         if (event.getType() == ChatMessageType.SPAM && event.getMessage().equals("You prod at the shoal of fish to scare it.")) {
            Actor target = this.client.getLocalPlayer().getInteracting();
            if (target instanceof NPC && ((NPC)target).getId() == 7782) {
               this.tagFish(target);
            } else {
               this.armInteraction = true;
            }
         }

      }
   }

   private void tagFish(Actor fish) {
      NPC fishTarget = (NPC)fish;
      this.taggedFish.put(fishTarget, this.client.getTickCount());
   }

   @Subscribe
   public void onNpcSpawned(NpcSpawned event) {
      NPC npc = event.getNpc();
      if (npc.getId() == 7782) {
         this.fish.add(npc);
      }

   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned event) {
      NPC npc = event.getNpc();
      this.fish.remove(npc);
      this.taggedFish.remove(npc);
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      GameObject object = event.getGameObject();
      if (object.getId() == 31843) {
         this.annette = object;
      }

      Iterator var3 = this.NETS.iterator();

      while(var3.hasNext()) {
         DriftNet net = (DriftNet)var3.next();
         if (net.getObjectId() == object.getId()) {
            net.setNet(object);
         }
      }

   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      GameObject object = event.getGameObject();
      if (object == this.annette) {
         this.annette = null;
      }

      Iterator var3 = this.NETS.iterator();

      while(var3.hasNext()) {
         DriftNet net = (DriftNet)var3.next();
         if (net.getObjectId() == object.getId()) {
            net.setNet((GameObject)null);
         }
      }

   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      ItemContainer itemContainer = event.getItemContainer();
      if (itemContainer == this.client.getItemContainer(93)) {
         this.driftNetsInInventory = itemContainer.contains(21652);
      }
   }

   private boolean checkArea() {
      Player localPlayer = this.client.getLocalPlayer();
      if (localPlayer != null && this.client.isInInstancedRegion()) {
         WorldPoint point = WorldPoint.fromLocalInstance(this.client, localPlayer.getLocalLocation());
         return point.getRegionID() == 15008;
      } else {
         return false;
      }
   }

   public Set<NPC> getFish() {
      return this.fish;
   }

   public Map<NPC, Integer> getTaggedFish() {
      return this.taggedFish;
   }

   public List<DriftNet> getNETS() {
      return this.NETS;
   }

   public boolean isInDriftNetArea() {
      return this.inDriftNetArea;
   }

   public boolean isDriftNetsInInventory() {
      return this.driftNetsInInventory;
   }

   public GameObject getAnnette() {
      return this.annette;
   }
}
