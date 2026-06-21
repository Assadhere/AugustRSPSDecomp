package net.runelite.client.util;

import java.util.Iterator;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.GroundObject;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemLayer;
import net.runelite.api.NPC;
import net.runelite.api.Node;
import net.runelite.api.Player;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.WallObject;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.events.WorldEntitySpawned;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;

@Singleton
public class GameEventManager {
   private final EventBus eventBus = new EventBus();
   private final Client client;
   private final ClientThread clientThread;

   @Inject
   private GameEventManager(Client client, ClientThread clientThread) {
      this.client = client;
      this.clientThread = clientThread;
   }

   private void forEachTile(WorldView wv, Consumer<Tile> consumer) {
      Scene scene = wv.getScene();
      Tile[][][] tiles = scene.getTiles();

      for(int z = 0; z < 4; ++z) {
         for(int x = 0; x < wv.getSizeX(); ++x) {
            for(int y = 0; y < wv.getSizeY(); ++y) {
               Tile tile = tiles[z][x][y];
               if (tile != null) {
                  consumer.accept(tile);
                  if (tile.getBridge() != null) {
                     consumer.accept(tile.getBridge());
                  }
               }
            }
         }
      }

   }

   public void simulateGameEvents(Object subscriber) {
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.clientThread.invoke(() -> {
            this.eventBus.register(subscriber);
            Iterator var2 = this.client.getItemContainers().iterator();

            while(var2.hasNext()) {
               ItemContainer itemContainer = (ItemContainer)var2.next();
               this.eventBus.post(new ItemContainerChanged(itemContainer.getId(), itemContainer));
            }

            this.simulateGameEvents(this.client.getTopLevelWorldView());
            this.eventBus.unregister(subscriber);
         });
      }
   }

   private void simulateGameEvents(WorldView wv) {
      Iterator var2 = wv.npcs().iterator();

      while(var2.hasNext()) {
         NPC npc = (NPC)var2.next();
         if (npc != null) {
            NpcSpawned npcSpawned = new NpcSpawned(npc);
            this.eventBus.post(npcSpawned);
         }
      }

      var2 = wv.players().iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         if (player != null) {
            PlayerSpawned playerSpawned = new PlayerSpawned(player);
            this.eventBus.post(playerSpawned);
         }
      }

      this.forEachTile(wv, (tile) -> {
         WallObject wallObject = tile.getWallObject();
         if (wallObject != null) {
            WallObjectSpawned objectSpawned = new WallObjectSpawned();
            objectSpawned.setTile(tile);
            objectSpawned.setWallObject(wallObject);
            this.eventBus.post(objectSpawned);
         }

         DecorativeObject decorativeObject = tile.getDecorativeObject();
         if (decorativeObject != null) {
            DecorativeObjectSpawned objectSpawnedx = new DecorativeObjectSpawned();
            objectSpawnedx.setTile(tile);
            objectSpawnedx.setDecorativeObject(decorativeObject);
            this.eventBus.post(objectSpawnedx);
         }

         GroundObject groundObject = tile.getGroundObject();
         if (groundObject != null) {
            GroundObjectSpawned objectSpawnedxx = new GroundObjectSpawned();
            objectSpawnedxx.setTile(tile);
            objectSpawnedxx.setGroundObject(groundObject);
            this.eventBus.post(objectSpawnedxx);
         }

         GameObject[] var12 = tile.getGameObjects();
         int var6 = var12.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            GameObject object = var12[var7];
            if (object != null && object.getSceneMinLocation().equals(tile.getSceneLocation())) {
               GameObjectSpawned objectSpawnedxxx = new GameObjectSpawned();
               objectSpawnedxxx.setTile(tile);
               objectSpawnedxxx.setGameObject(object);
               this.eventBus.post(objectSpawnedxxx);
            }
         }

         ItemLayer itemLayer = tile.getItemLayer();
         if (itemLayer != null) {
            Node current = itemLayer.getTop();

            while(current instanceof TileItem) {
               TileItem item = (TileItem)current;
               current = ((Node)current).getNext();
               ItemSpawned itemSpawned = new ItemSpawned(tile, item);
               this.eventBus.post(itemSpawned);
            }
         }

      });
      var2 = wv.worldEntities().iterator();

      while(var2.hasNext()) {
         WorldEntity we = (WorldEntity)var2.next();
         this.eventBus.post(new WorldEntitySpawned(we));
         this.simulateGameEvents(we.getWorldView());
      }

   }
}
