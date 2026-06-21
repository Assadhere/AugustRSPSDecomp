package net.runelite.client.plugins.agility;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.AgilityShortcut;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Agility",
   description = "Show helpful information about agility courses and obstacles",
   tags = {"grace", "marks", "overlay", "shortcuts", "skilling", "traps", "sepulchre"}
)
@PluginDependency(XpTrackerPlugin.class)
public class AgilityPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(AgilityPlugin.class);
   private static final int AGILITY_ARENA_REGION_ID = 11157;
   private static final Set<Integer> SEPULCHRE_NPCS = ImmutableSet.of(9672, 9673, 9674, 9669, 9670, 9671, new Integer[0]);
   private final Map<TileObject, Obstacle> obstacles = new HashMap();
   private final List<Tile> marksOfGrace = new ArrayList();
   private final Set<NPC> npcs = new HashSet();
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private AgilityOverlay agilityOverlay;
   @Inject
   private LapCounterOverlay lapCounterOverlay;
   @Inject
   private Notifier notifier;
   @Inject
   private Client client;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private AgilityConfig config;
   @Inject
   private ItemManager itemManager;
   @Inject
   private XpTrackerService xpTrackerService;
   private AgilitySession session;
   private int lastAgilityXp;
   private WorldPoint lastArenaTicketPosition;
   private int agilityLevel;
   private Tile stickTile;

   @Provides
   AgilityConfig getConfig(ConfigManager configManager) {
      return (AgilityConfig)configManager.getConfig(AgilityConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.agilityOverlay);
      this.overlayManager.add(this.lapCounterOverlay);
      this.agilityLevel = this.client.getBoostedSkillLevel(Skill.AGILITY);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.agilityOverlay);
      this.overlayManager.remove(this.lapCounterOverlay);
      this.marksOfGrace.clear();
      this.obstacles.clear();
      this.session = null;
      this.agilityLevel = 0;
      this.stickTile = null;
      this.npcs.clear();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case HOPPING:
         case LOGIN_SCREEN:
            this.session = null;
            this.lastArenaTicketPosition = null;
            this.removeAgilityArenaTimer();
            this.npcs.clear();
            break;
         case LOADING:
            this.marksOfGrace.clear();
            this.obstacles.clear();
            this.stickTile = null;
            break;
         case LOGGED_IN:
            if (!this.isInAgilityArena()) {
               this.lastArenaTicketPosition = null;
               this.removeAgilityArenaTimer();
            }
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (!this.config.showAgilityArenaTimer()) {
         this.removeAgilityArenaTimer();
      }

   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      if (event.getVarbitId() == 11293 && event.getValue() == 6) {
         this.trackSession(Courses.COLOSSAL_WYRM_ADVANCED);
      } else if (event.getVarbitId() == 11292 && event.getValue() == 6) {
         this.trackSession(Courses.COLOSSAL_WYRM_BASIC);
      }

   }

   @Subscribe
   public void onStatChanged(StatChanged statChanged) {
      if (statChanged.getSkill() == Skill.AGILITY) {
         this.agilityLevel = statChanged.getBoostedLevel();
         int previousLevel = Experience.getLevelForXp(this.lastAgilityXp);
         int agilityXp = statChanged.getXp();
         int skillGained = agilityXp - this.lastAgilityXp;
         this.lastAgilityXp = agilityXp;
         log.debug("Gained {} xp at {}", skillGained, this.client.getLocalPlayer().getWorldLocation());
         Courses course = Courses.getCourse(this.client.getLocalPlayer().getWorldLocation().getRegionID());
         if (course != null && this.config.showLapCount()) {
            if (Arrays.stream(course.getCourseEndWorldPoints()).noneMatch((wp) -> {
               return wp.equals(this.client.getLocalPlayer().getWorldLocation());
            })) {
               if (this.session != null && previousLevel != statChanged.getLevel()) {
                  this.session.recalculateLapsTillGoal(this.client, this.xpTrackerService);
               }

            } else {
               this.trackSession(course);
            }
         }
      }
   }

   @Subscribe
   public void onItemSpawned(ItemSpawned itemSpawned) {
      if (!this.obstacles.isEmpty()) {
         TileItem item = itemSpawned.getItem();
         Tile tile = itemSpawned.getTile();
         if (item.getId() == 11849) {
            this.marksOfGrace.add(tile);
         }

         if (item.getId() == 4179) {
            this.stickTile = tile;
         }

      }
   }

   @Subscribe
   public void onItemDespawned(ItemDespawned itemDespawned) {
      TileItem item = itemDespawned.getItem();
      Tile tile = itemDespawned.getTile();
      this.marksOfGrace.remove(tile);
      if (item.getId() == 4179 && this.stickTile == tile) {
         this.stickTile = null;
      }

   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      if (this.isInAgilityArena()) {
         WorldPoint newTicketPosition = this.client.getHintArrowPoint();
         WorldPoint oldTickPosition = this.lastArenaTicketPosition;
         this.lastArenaTicketPosition = newTicketPosition;
         if (oldTickPosition != null && newTicketPosition != null && (oldTickPosition.getX() != newTicketPosition.getX() || oldTickPosition.getY() != newTicketPosition.getY())) {
            log.debug("Ticked position moved from {} to {}", oldTickPosition, newTicketPosition);
            this.notifier.notify(this.config.notifyAgilityArena(), "Ticket location changed");
            if (this.config.showAgilityArenaTimer()) {
               this.showNewAgilityArenaTimer();
            }
         }
      }

   }

   private void trackSession(Courses course) {
      if (this.session == null || this.session.getCourse() != course) {
         this.session = new AgilitySession(course);
         log.debug("Started new agility session for course: {}", course);
      }

      this.session.incrementLapCount(this.client, this.xpTrackerService);
   }

   private boolean isInAgilityArena() {
      Player local = this.client.getLocalPlayer();
      if (local == null) {
         return false;
      } else {
         WorldPoint location = local.getWorldLocation();
         return location.getRegionID() == 11157;
      }
   }

   private void removeAgilityArenaTimer() {
      this.infoBoxManager.removeIf((infoBox) -> {
         return infoBox instanceof AgilityArenaTimer;
      });
   }

   private void showNewAgilityArenaTimer() {
      this.removeAgilityArenaTimer();
      this.infoBoxManager.addInfoBox(new AgilityArenaTimer(this, this.itemManager.getImage(2996)));
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      this.onTileObject(event.getTile(), (TileObject)null, event.getGameObject());
   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      this.onTileObject(event.getTile(), event.getGameObject(), (TileObject)null);
   }

   @Subscribe
   public void onGroundObjectSpawned(GroundObjectSpawned event) {
      this.onTileObject(event.getTile(), (TileObject)null, event.getGroundObject());
   }

   @Subscribe
   public void onGroundObjectDespawned(GroundObjectDespawned event) {
      this.onTileObject(event.getTile(), event.getGroundObject(), (TileObject)null);
   }

   @Subscribe
   public void onWallObjectSpawned(WallObjectSpawned event) {
      this.onTileObject(event.getTile(), (TileObject)null, event.getWallObject());
   }

   @Subscribe
   public void onWallObjectDespawned(WallObjectDespawned event) {
      this.onTileObject(event.getTile(), event.getWallObject(), (TileObject)null);
   }

   @Subscribe
   public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
      this.onTileObject(event.getTile(), (TileObject)null, event.getDecorativeObject());
   }

   @Subscribe
   public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
      this.onTileObject(event.getTile(), event.getDecorativeObject(), (TileObject)null);
   }

   private void onTileObject(Tile tile, TileObject oldObject, TileObject newObject) {
      this.obstacles.remove(oldObject);
      if (newObject != null) {
         if (Obstacles.OBSTACLE_IDS.contains(newObject.getId()) || Obstacles.PORTAL_OBSTACLE_IDS.contains(newObject.getId()) || Obstacles.TRAP_OBSTACLE_IDS.contains(newObject.getId()) && Obstacles.TRAP_OBSTACLE_REGIONS.contains(newObject.getWorldLocation().getRegionID()) || Obstacles.SEPULCHRE_OBSTACLE_IDS.contains(newObject.getId()) || Obstacles.SEPULCHRE_SKILL_OBSTACLE_IDS.contains(newObject.getId())) {
            this.obstacles.put(newObject, new Obstacle(tile, (AgilityShortcut)null));
         }

         if (Obstacles.SHORTCUT_OBSTACLE_IDS.containsKey(newObject.getId())) {
            AgilityShortcut closestShortcut = null;
            int distance = -1;
            Iterator var6 = Obstacles.SHORTCUT_OBSTACLE_IDS.get(newObject.getId()).iterator();

            label44:
            while(true) {
               AgilityShortcut shortcut;
               int newDistance;
               do {
                  do {
                     if (!var6.hasNext()) {
                        break label44;
                     }

                     shortcut = (AgilityShortcut)var6.next();
                  } while(!shortcut.matches(this.client, newObject));

                  if (shortcut.getWorldLocation() == null) {
                     closestShortcut = shortcut;
                     break label44;
                  }

                  newDistance = shortcut.getWorldLocation().distanceTo2D(newObject.getWorldLocation());
               } while(closestShortcut != null && newDistance >= distance);

               closestShortcut = shortcut;
               distance = newDistance;
            }

            if (closestShortcut != null) {
               this.obstacles.put(newObject, new Obstacle(tile, closestShortcut));
            }
         }

      }
   }

   @Subscribe
   public void onNpcSpawned(NpcSpawned npcSpawned) {
      NPC npc = npcSpawned.getNpc();
      if (SEPULCHRE_NPCS.contains(npc.getId())) {
         this.npcs.add(npc);
      }

   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned npcDespawned) {
      NPC npc = npcDespawned.getNpc();
      this.npcs.remove(npc);
   }

   public Map<TileObject, Obstacle> getObstacles() {
      return this.obstacles;
   }

   public List<Tile> getMarksOfGrace() {
      return this.marksOfGrace;
   }

   public Set<NPC> getNpcs() {
      return this.npcs;
   }

   public AgilitySession getSession() {
      return this.session;
   }

   void setSession(AgilitySession session) {
      this.session = session;
   }

   public int getAgilityLevel() {
      return this.agilityLevel;
   }

   Tile getStickTile() {
      return this.stickTile;
   }
}
