package net.runelite.client.game;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Player;
import net.runelite.api.ScriptEvent;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.events.ServerNpcLoot;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class LootManager {
   private static final Logger log = LoggerFactory.getLogger(LootManager.class);
   private static final Map<Integer, Integer> NPC_DEATH_ANIMATIONS = ImmutableMap.of(492, 3993, 14012, 11679);
   private final EventBus eventBus;
   private final Client client;
   private final NpcUtil npcUtil;
   private final ListMultimap<Integer, TileItem> itemSpawns = ArrayListMultimap.create();
   private final Set<WorldPoint> killPoints = new HashSet();
   private WorldPoint playerLocationLastTick;
   private WorldPoint krakenPlayerLocation;
   private NPC delayedLootNpc;
   private int delayedLootTick;
   private List<WorldArea> delayedLootAreas;
   private NPCComposition scriptNpc;
   private int scriptEventId;
   private final List<ItemStack> scriptItems = new ArrayList(4);

   @Inject
   private LootManager(EventBus eventBus, Client client, NpcUtil npcUtil) {
      this.eventBus = eventBus;
      this.client = client;
      this.npcUtil = npcUtil;
      eventBus.register(this);
   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned npcDespawned) {
      NPC npc = npcDespawned.getNpc();
      if (npc == this.delayedLootNpc) {
         this.clearDelayedLootNpc();
      }

      if (!this.npcUtil.isDying(npc)) {
         int id = npc.getId();
         switch (id) {
            case 412:
            case 413:
            case 421:
            case 422:
            case 458:
            case 459:
            case 460:
            case 461:
            case 462:
            case 463:
            case 537:
            case 1024:
            case 1543:
            case 7392:
            case 7407:
            case 7408:
            case 7797:
            case 7888:
            case 7889:
               break;
            default:
               return;
         }
      }

      this.processNpcLoot(npc);
   }

   @Subscribe
   public void onPlayerDespawned(PlayerDespawned playerDespawned) {
      Player player = playerDespawned.getPlayer();
      if (player.getHealthRatio() == 0) {
         WorldPoint worldPoint = player.getWorldLocation();
         LocalPoint location = LocalPoint.fromWorld(this.client, worldPoint);
         if (location != null && !this.killPoints.contains(worldPoint)) {
            int x = location.getSceneX();
            int y = location.getSceneY();
            int packed = x << 8 | y;
            Collection<ItemStack> items = (Collection)this.itemSpawns.get(packed).stream().map((i) -> {
               return new ItemStack(i.getId(), i.getQuantity());
            }).collect(Collectors.toList());
            if (!items.isEmpty()) {
               this.killPoints.add(worldPoint);
               this.eventBus.post(new PlayerLootReceived(player, items));
            }
         }
      }
   }

   @Subscribe
   public void onItemSpawned(ItemSpawned itemSpawned) {
      TileItem item = itemSpawned.getItem();
      Tile tile = itemSpawned.getTile();
      LocalPoint location = tile.getLocalLocation();
      int packed = location.getSceneX() << 8 | location.getSceneY();
      this.itemSpawns.put(packed, item);
      log.debug("Item spawn {} ({}) location {}", new Object[]{item.getId(), item.getQuantity(), location});
   }

   @Subscribe
   public void onItemDespawned(ItemDespawned itemDespawned) {
      TileItem item = itemDespawned.getItem();
      LocalPoint location = itemDespawned.getTile().getLocalLocation();
      log.debug("Item despawn {} ({}) location {}", new Object[]{item.getId(), item.getQuantity(), location});
   }

   @Subscribe
   public void onAnimationChanged(AnimationChanged e) {
      if (e.getActor() instanceof NPC) {
         NPC npc = (NPC)e.getActor();
         int id = npc.getId();
         Integer deathAnim = (Integer)NPC_DEATH_ANIMATIONS.get(id);
         if (deathAnim != null && deathAnim == npc.getAnimation()) {
            if (id == 492) {
               this.krakenPlayerLocation = this.client.getLocalPlayer().getWorldLocation();
            } else {
               this.processNpcLoot(npc);
            }
         }

      }
   }

   @Subscribe
   public void onNpcChanged(NpcChanged npcChanged) {
      NPC npc = npcChanged.getNpc();
      if (npc.getId() != 9433 && npc.getId() != 9424) {
         if (npc.getId() == 475) {
            this.delayedLootNpc = npc;
            this.delayedLootTick = 1;
            this.delayedLootAreas = this.getDropLocations(npc);
         } else if (npc.getId() == 12192 || npc.getId() == 12196) {
            this.delayedLootNpc = npc;
            this.delayedLootTick = 5;
            this.delayedLootAreas = this.getDropLocations(npc);
         }
      } else {
         this.delayedLootNpc = npc;
         this.delayedLootTick = 10;
      }

   }

   @Subscribe
   public void onGameTick(GameTick gameTick) {
      if (this.delayedLootNpc != null && --this.delayedLootTick == 0) {
         this.processDelayedLoot();
         this.clearDelayedLootNpc();
      }

      this.playerLocationLastTick = this.client.getLocalPlayer().getWorldLocation();
      this.itemSpawns.clear();
      this.killPoints.clear();
      this.processScriptLoot();
   }

   @Subscribe
   private void onScriptPreFired(ScriptPreFired event) {
      if (event.getScriptId() == 7192) {
         ScriptEvent scriptEvent = event.getScriptEvent();
         int npcId = (Integer)scriptEvent.getArguments()[1];
         int eventId = (Integer)scriptEvent.getArguments()[2];
         int itemId = (Integer)scriptEvent.getArguments()[3];
         int qty = (Integer)scriptEvent.getArguments()[4];
         log.debug("loottracker_add_loot npc={} event={} item={} qty={}", new Object[]{npcId, eventId, itemId, qty});
         if (this.scriptEventId != eventId) {
            this.processScriptLoot();
         }

         NPCComposition npcComposition = this.client.getNpcDefinition(npcId);
         String name = Text.removeTags(npcComposition.getName());
         if (!name.isEmpty() && !"null".equals(name)) {
            this.scriptNpc = npcComposition;
         }

         this.scriptEventId = eventId;
         this.scriptItems.add(new ItemStack(itemId, qty));
      }

   }

   private void processScriptLoot() {
      if (this.scriptNpc != null && !this.scriptItems.isEmpty()) {
         this.eventBus.post(new ServerNpcLoot(this.scriptNpc, this.scriptItems));
      }

      this.scriptNpc = null;
      this.scriptItems.clear();
   }

   private void processDelayedLoot() {
      if (this.delayedLootAreas == null) {
         this.delayedLootAreas = List.of(this.getAdjacentSquareLootTile(this.delayedLootNpc).toWorldArea());
      }

      List<ItemStack> itemStacks = this.getItemStacksFromAreas(this.delayedLootAreas);
      if (!itemStacks.isEmpty()) {
         log.debug("Got delayed loot stack from {}: {}", this.delayedLootNpc.getName(), itemStacks);
         this.eventBus.post(new NpcLootReceived(this.delayedLootNpc, itemStacks));
      } else {
         log.debug("Delayed loot expired with no loot");
      }

   }

   private void processNpcLoot(NPC npc) {
      List<ItemStack> allItems = this.getItemStacksFromAreas(this.getDropLocations(npc));
      if (!allItems.isEmpty()) {
         this.eventBus.post(new NpcLootReceived(npc, allItems));
      }

   }

   private List<ItemStack> getItemStacksFromAreas(List<WorldArea> areas) {
      List<ItemStack> allItems = new ArrayList();
      Iterator var3 = areas.iterator();

      while(true) {
         WorldArea dropLocation;
         WorldPoint worldPoint;
         LocalPoint location;
         do {
            if (!var3.hasNext()) {
               return allItems;
            }

            dropLocation = (WorldArea)var3.next();
            worldPoint = dropLocation.toWorldPoint();
            location = LocalPoint.fromWorld(this.client, worldPoint);
         } while(location == null);

         int x = location.getSceneX();
         int y = location.getSceneY();

         for(int i = 0; i < dropLocation.getWidth(); ++i) {
            for(int j = 0; j < dropLocation.getHeight(); ++j) {
               WorldPoint dropPoint = new WorldPoint(worldPoint.getX() + i, worldPoint.getY() + j, worldPoint.getPlane());
               if (this.killPoints.add(dropPoint)) {
                  int packed = x + i << 8 | y + j;
                  Collection<TileItem> items = this.itemSpawns.get(packed);
                  items.forEach((item) -> {
                     allItems.add(new ItemStack(item.getId(), item.getQuantity()));
                  });
               }
            }
         }
      }
   }

   private List<WorldArea> getDropLocations(NPC npc) {
      WorldArea bossArea;
      int unpackedX;
      WorldPoint bossLocation;
      int x;
      int y;
      switch (npc.getId()) {
         case 475:
            bossArea = npc.getWorldArea();
            return List.of(new WorldArea(bossArea.getX() - 1, bossArea.getY() - 1, 3, 3, bossArea.getPlane()));
         case 492:
            return Collections.singletonList(this.krakenPlayerLocation.toWorldArea());
         case 494:
         case 6640:
         case 6656:
            return Collections.singletonList(this.playerLocationLastTick.toWorldArea());
         case 2042:
         case 2043:
         case 2044:
            Iterator var11 = this.itemSpawns.entries().iterator();

            Map.Entry entry;
            do {
               if (!var11.hasNext()) {
                  return Collections.singletonList(npc.getWorldArea());
               }

               entry = (Map.Entry)var11.next();
            } while(((TileItem)entry.getValue()).getId() != 12934);

            y = (Integer)entry.getKey();
            unpackedX = y >> 8;
            int unpackedY = y & 255;
            WorldPoint lootPoint = WorldPoint.fromScene(this.client, unpackedX, unpackedY, npc.getWorldLocation().getPlane());
            return Collections.singletonList(lootPoint.toWorldArea());
         case 6503:
         case 6504:
         case 6609:
         case 6610:
         case 6612:
         case 11992:
         case 11994:
         case 11998:
            return ImmutableList.of(npc.getWorldArea(), this.playerLocationLastTick.toWorldArea());
         case 8026:
         case 8058:
         case 8059:
         case 8060:
         case 8061:
            bossLocation = npc.getWorldLocation();
            x = bossLocation.getX() + 3;
            y = bossLocation.getY() + 3;
            if (this.playerLocationLastTick.getX() < x) {
               x -= 4;
            } else if (this.playerLocationLastTick.getX() > x) {
               x += 4;
            }

            if (this.playerLocationLastTick.getY() < y) {
               y -= 4;
            } else if (this.playerLocationLastTick.getY() > y) {
               y += 4;
            }

            return Collections.singletonList(new WorldArea(x, y, 1, 1, bossLocation.getPlane()));
         case 11278:
         case 11279:
         case 11280:
         case 11281:
         case 11282:
            LocalPoint localPoint = LocalPoint.fromWorld(this.client, this.playerLocationLastTick);
            if (localPoint != null) {
               x = localPoint.getSceneX();
               y = localPoint.getSceneY();
               unpackedX = x << 8 | y;
               if (this.itemSpawns.containsKey(unpackedX)) {
                  return Collections.singletonList(this.playerLocationLastTick.toWorldArea());
               }
            }
            break;
         case 12192:
         case 12196:
            bossLocation = npc.getWorldLocation();
            x = bossLocation.getX() + npc.getComposition().getSize() / 2;
            y = bossLocation.getY() - 1;
            return List.of((new WorldPoint(x, y, bossLocation.getPlane())).toWorldArea());
         case 12214:
         case 12215:
            bossArea = npc.getWorldArea();
            int expand = true;
            WorldArea expandedArea = new WorldArea(bossArea.getX() - 8, bossArea.getY() - 8, bossArea.getWidth() + 16, bossArea.getHeight() + 16, bossArea.getPlane());
            return List.of(expandedArea);
         case 12223:
         case 12224:
            bossArea = npc.getWorldArea();
            return List.of(new WorldArea(bossArea.getX() - 2, bossArea.getY() - 2, bossArea.getWidth() + 4, bossArea.getHeight() + 4, bossArea.getPlane()));
         case 14012:
            bossArea = npc.getWorldArea();
            return List.of(new WorldArea(bossArea.getX() - 2, bossArea.getY() - 10, 10, 10, bossArea.getPlane()));
      }

      return Collections.singletonList(npc.getWorldArea());
   }

   private WorldPoint getAdjacentSquareLootTile(NPC npc) {
      NPCComposition composition = npc.getComposition();
      WorldPoint worldLocation = npc.getWorldLocation();
      int x = worldLocation.getX();
      int y = worldLocation.getY();
      if (this.playerLocationLastTick.getX() < x) {
         --x;
      } else {
         x += Math.min(this.playerLocationLastTick.getX() - x, composition.getSize());
      }

      if (this.playerLocationLastTick.getY() < y) {
         --y;
      } else {
         y += Math.min(this.playerLocationLastTick.getY() - y, composition.getSize());
      }

      return new WorldPoint(x, y, worldLocation.getPlane());
   }

   public Collection<ItemStack> getItemSpawns(WorldPoint worldPoint) {
      LocalPoint localPoint = LocalPoint.fromWorld(this.client, worldPoint);
      if (localPoint == null) {
         return Collections.emptyList();
      } else {
         int sceneX = localPoint.getSceneX();
         int sceneY = localPoint.getSceneY();
         int packed = sceneX << 8 | sceneY;
         List<TileItem> itemStacks = this.itemSpawns.get(packed);
         return (Collection)itemStacks.stream().map((ti) -> {
            return new ItemStack(ti.getId(), ti.getQuantity());
         }).collect(Collectors.toList());
      }
   }

   private void clearDelayedLootNpc() {
      this.delayedLootNpc = null;
      this.delayedLootTick = 0;
      this.delayedLootAreas = null;
   }
}
