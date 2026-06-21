package net.runelite.client.plugins.interacthighlight;

import com.google.inject.Provides;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.GroundObject;
import net.runelite.api.ItemLayer;
import net.runelite.api.MenuAction;
import net.runelite.api.Node;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.WorldView;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Interact Highlight",
   description = "Outlines entities you interact with or hover over",
   enabledByDefault = false
)
public class InteractHighlightPlugin extends Plugin {
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private InteractHighlightOverlay interactHighlightOverlay;
   @Inject
   private Client client;
   private TileObject interactedObject;
   private TileItem interactedItem;
   private Actor interactedActor;
   boolean attacked;
   private int clickTick;
   private int gameCycle;

   @Provides
   InteractHighlightConfig provideConfig(ConfigManager configManager) {
      return (InteractHighlightConfig)configManager.getConfig(InteractHighlightConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.interactHighlightOverlay);
   }

   protected void shutDown() {
      this.overlayManager.remove(this.interactHighlightOverlay);
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged gameStateChanged) {
      if (gameStateChanged.getGameState() == GameState.LOADING) {
         this.interactedObject = null;
      }

   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned npcDespawned) {
      if (npcDespawned.getNpc() == this.interactedActor) {
         this.interactedActor = null;
      }

   }

   @Subscribe
   public void onPlayerDespawned(PlayerDespawned event) {
      if (event.getPlayer() == this.interactedActor) {
         this.interactedActor = null;
      }

   }

   @Subscribe
   public void onItemDespawned(ItemDespawned event) {
      if (event.getItem() == this.interactedItem) {
         this.interactedObject = null;
         this.interactedItem = null;
      }

   }

   @Subscribe
   public void onGameTick(GameTick gameTick) {
      if (this.client.getTickCount() > this.clickTick && this.client.getLocalDestinationLocation() == null) {
         this.interactedObject = null;
         this.interactedItem = null;
         this.interactedActor = null;
      }

   }

   @Subscribe
   public void onInteractingChanged(InteractingChanged interactingChanged) {
      if (interactingChanged.getSource() == this.client.getLocalPlayer() && this.client.getTickCount() > this.clickTick && interactingChanged.getTarget() != this.interactedActor) {
         this.interactedActor = null;
         this.attacked = interactingChanged.getTarget() != null && interactingChanged.getTarget().getCombatLevel() > 0;
      }

   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
      int worldId;
      int x;
      int y;
      int id;
      switch (menuOptionClicked.getMenuAction()) {
         case WIDGET_TARGET_ON_GAME_OBJECT:
         case GAME_OBJECT_FIRST_OPTION:
         case GAME_OBJECT_SECOND_OPTION:
         case GAME_OBJECT_THIRD_OPTION:
         case GAME_OBJECT_FOURTH_OPTION:
         case GAME_OBJECT_FIFTH_OPTION:
            worldId = menuOptionClicked.getMenuEntry().getWorldViewId();
            x = menuOptionClicked.getParam0();
            y = menuOptionClicked.getParam1();
            id = menuOptionClicked.getId();
            this.interactedObject = this.findTileObject(worldId, x, y, id);
            this.interactedItem = null;
            this.interactedActor = null;
            this.clickTick = this.client.getTickCount();
            this.gameCycle = this.client.getGameCycle();
            break;
         case WIDGET_TARGET_ON_NPC:
         case NPC_FIRST_OPTION:
         case NPC_SECOND_OPTION:
         case NPC_THIRD_OPTION:
         case NPC_FOURTH_OPTION:
         case NPC_FIFTH_OPTION:
            this.interactedObject = null;
            this.interactedItem = null;
            this.interactedActor = menuOptionClicked.getMenuEntry().getNpc();
            this.attacked = menuOptionClicked.getMenuAction() == MenuAction.NPC_SECOND_OPTION || menuOptionClicked.getMenuAction() == MenuAction.WIDGET_TARGET_ON_NPC && this.client.getSelectedWidget() != null && WidgetUtil.componentToInterface(this.client.getSelectedWidget().getId()) == 218;
            this.clickTick = this.client.getTickCount();
            this.gameCycle = this.client.getGameCycle();
            break;
         case WIDGET_TARGET_ON_PLAYER:
         case PLAYER_FIRST_OPTION:
         case PLAYER_SECOND_OPTION:
         case PLAYER_THIRD_OPTION:
         case PLAYER_FOURTH_OPTION:
         case PLAYER_FIFTH_OPTION:
         case PLAYER_SIXTH_OPTION:
         case PLAYER_SEVENTH_OPTION:
         case PLAYER_EIGHTH_OPTION:
            this.interactedObject = null;
            this.interactedItem = null;
            this.interactedActor = menuOptionClicked.getMenuEntry().getPlayer();
            this.attacked = false;
            this.clickTick = this.client.getTickCount();
            this.gameCycle = this.client.getGameCycle();
            break;
         case WIDGET_TARGET_ON_GROUND_ITEM:
         case GROUND_ITEM_FIRST_OPTION:
         case GROUND_ITEM_SECOND_OPTION:
         case GROUND_ITEM_THIRD_OPTION:
         case GROUND_ITEM_FOURTH_OPTION:
         case GROUND_ITEM_FIFTH_OPTION:
            worldId = menuOptionClicked.getMenuEntry().getWorldViewId();
            x = menuOptionClicked.getParam0();
            y = menuOptionClicked.getParam1();
            id = menuOptionClicked.getId();
            this.interactedObject = this.findItemLayer(worldId, x, y);
            this.interactedItem = this.findItem((ItemLayer)this.interactedObject, id);
            this.interactedActor = null;
            this.clickTick = this.client.getTickCount();
            this.gameCycle = this.client.getGameCycle();
            break;
         case WIDGET_TARGET_ON_WIDGET:
         case WALK:
            this.interactedObject = null;
            this.interactedActor = null;
            break;
         default:
            if (menuOptionClicked.isItemOp()) {
               this.interactedObject = null;
               this.interactedActor = null;
            }
      }

   }

   TileObject findTileObject(int worldId, int x, int y, int id) {
      WorldView wv = this.client.getWorldView(worldId);
      int offset = worldId == 0 ? 40 : 0;
      x += offset;
      y += offset;
      Scene scene = wv.getScene();
      Tile[][][] tiles = scene.getExtendedTiles();
      Tile tile = tiles[wv.getPlane()][x][y];
      if (tile != null) {
         GameObject[] var10 = tile.getGameObjects();
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            GameObject gameObject = var10[var12];
            if (gameObject != null && gameObject.getId() == id) {
               return gameObject;
            }
         }

         WallObject wallObject = tile.getWallObject();
         if (wallObject != null && wallObject.getId() == id) {
            return wallObject;
         }

         DecorativeObject decorativeObject = tile.getDecorativeObject();
         if (decorativeObject != null && decorativeObject.getId() == id) {
            return decorativeObject;
         }

         GroundObject groundObject = tile.getGroundObject();
         if (groundObject != null && groundObject.getId() == id) {
            return groundObject;
         }
      }

      return null;
   }

   ItemLayer findItemLayer(int worldId, int x, int y) {
      WorldView wv = this.client.getWorldView(worldId);
      int offset = worldId == 0 ? 40 : 0;
      x += offset;
      y += offset;
      Scene scene = wv.getScene();
      Tile[][][] tiles = scene.getExtendedTiles();
      Tile tile = tiles[wv.getPlane()][x][y];
      return tile != null ? tile.getItemLayer() : null;
   }

   TileItem findItem(ItemLayer layer, int id) {
      if (layer == null) {
         return null;
      } else {
         Node current = layer.getTop();

         TileItem item;
         do {
            if (!(current instanceof TileItem)) {
               return null;
            }

            item = (TileItem)current;
            current = ((Node)current).getNext();
         } while(item.getId() != id);

         return item;
      }
   }

   @Nullable
   Actor getInteractedTarget() {
      return this.interactedActor != null ? this.interactedActor : this.client.getLocalPlayer().getInteracting();
   }

   TileObject getInteractedObject() {
      return this.interactedObject;
   }

   TileItem getInteractedItem() {
      return this.interactedItem;
   }

   boolean isAttacked() {
      return this.attacked;
   }

   int getGameCycle() {
      return this.gameCycle;
   }
}
