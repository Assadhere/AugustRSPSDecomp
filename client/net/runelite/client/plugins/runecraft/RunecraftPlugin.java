package net.runelite.client.plugins.runecraft;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Runecraft",
   description = "Show minimap icons and clickboxes for abyssal rifts",
   tags = {"abyssal", "minimap", "overlay", "rifts", "rc", "runecrafting", "essence", "pouch"}
)
public class RunecraftPlugin extends Plugin {
   private static final String POUCH_DECAYED_NOTIFICATION_MESSAGE = "Your rune pouch has decayed.";
   private static final String POUCH_DECAYED_MESSAGE = "Your pouch has decayed through use.";
   private static final List<Integer> DEGRADED_POUCHES = ImmutableList.of(5511, 5513, 5515, 26786);
   static final int ABYSS_REGION = 12107;
   private final Set<DecorativeObject> abyssObjects = new HashSet();
   private boolean degradedPouchInInventory;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private AbyssOverlay abyssOverlay;
   @Inject
   private AbyssMinimapOverlay abyssMinimapOverlay;
   @Inject
   private EssencePouchOverlay essencePouchOverlay;
   @Inject
   private RunecraftConfig config;
   @Inject
   private Notifier notifier;
   @Inject
   private NpcOverlayService npcOverlayService;
   private final Function<NPC, HighlightedNpc> highlightDarkMage = this::highlightDarkMage;

   @Provides
   RunecraftConfig getConfig(ConfigManager configManager) {
      return (RunecraftConfig)configManager.getConfig(RunecraftConfig.class);
   }

   protected void startUp() throws Exception {
      this.npcOverlayService.registerHighlighter(this.highlightDarkMage);
      this.overlayManager.add(this.abyssOverlay);
      this.overlayManager.add(this.abyssMinimapOverlay);
      this.overlayManager.add(this.essencePouchOverlay);
   }

   protected void shutDown() throws Exception {
      this.npcOverlayService.unregisterHighlighter(this.highlightDarkMage);
      this.overlayManager.remove(this.abyssOverlay);
      this.overlayManager.remove(this.abyssMinimapOverlay);
      this.overlayManager.remove(this.essencePouchOverlay);
      this.abyssObjects.clear();
      this.degradedPouchInInventory = false;
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.GAMEMESSAGE) {
         if (event.getMessage().contains("Your pouch has decayed through use.")) {
            this.notifier.notify(this.config.degradingNotification(), "Your rune pouch has decayed.");
         }

      }
   }

   @Subscribe
   public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
      DecorativeObject decorativeObject = event.getDecorativeObject();
      if (AbyssRifts.getRift(decorativeObject.getId()) != null) {
         this.abyssObjects.add(decorativeObject);
      }

   }

   @Subscribe
   public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
      DecorativeObject decorativeObject = event.getDecorativeObject();
      this.abyssObjects.remove(decorativeObject);
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      GameState gameState = event.getGameState();
      if (gameState == GameState.LOADING) {
         this.abyssObjects.clear();
      }

   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      if (event.getContainerId() == 93) {
         Item[] items = event.getItemContainer().getItems();
         this.degradedPouchInInventory = Stream.of(items).anyMatch((i) -> {
            return DEGRADED_POUCHES.contains(i.getId());
         });
      }
   }

   private HighlightedNpc highlightDarkMage(NPC npc) {
      return npc.getId() == 2583 ? HighlightedNpc.builder().npc(npc).tile(true).highlightColor(Color.GREEN).render((n) -> {
         return this.config.hightlightDarkMage() && this.degradedPouchInInventory;
      }).build() : null;
   }

   Set<DecorativeObject> getAbyssObjects() {
      return this.abyssObjects;
   }
}
