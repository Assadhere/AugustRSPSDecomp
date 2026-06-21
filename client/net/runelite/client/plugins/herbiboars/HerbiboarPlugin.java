package net.runelite.client.plugins.herbiboars;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Provides;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Herbiboar",
   description = "Highlight starting rocks, trails, and the objects to search at the end of each trail",
   tags = {"herblore", "hunter", "skilling", "overlay"}
)
public class HerbiboarPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(HerbiboarPlugin.class);
   private static final List<WorldPoint> END_LOCATIONS = ImmutableList.of(new WorldPoint(3693, 3798, 0), new WorldPoint(3702, 3808, 0), new WorldPoint(3703, 3826, 0), new WorldPoint(3710, 3881, 0), new WorldPoint(3700, 3877, 0), new WorldPoint(3715, 3840, 0), new WorldPoint(3751, 3849, 0), new WorldPoint(3685, 3869, 0), new WorldPoint(3681, 3863, 0));
   private static final Set<Integer> START_OBJECT_IDS = ImmutableSet.of(30519, 30520, 30521, 30522, 30523);
   private static final List<Integer> HERBIBOAR_REGIONS = ImmutableList.of(14652, 14651, 14908, 14907);
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private HerbiboarOverlay overlay;
   @Inject
   private HerbiboarMinimapOverlay minimapOverlay;
   private final Map<WorldPoint, TileObject> starts = new HashMap();
   private final Map<WorldPoint, TileObject> trails = new HashMap();
   private final Map<WorldPoint, TileObject> trailObjects = new HashMap();
   private final Map<WorldPoint, TileObject> tunnels = new HashMap();
   private final Set<Integer> shownTrails = new HashSet();
   private final List<HerbiboarSearchSpot> currentPath = Lists.newArrayList();
   private boolean inHerbiboarArea;
   private TrailToSpot nextTrail;
   private HerbiboarSearchSpot.Group currentGroup;
   private int finishId;
   private boolean started;
   private WorldPoint startPoint;
   private HerbiboarStart startSpot;

   @Provides
   HerbiboarConfig provideConfig(ConfigManager configManager) {
      return (HerbiboarConfig)configManager.getConfig(HerbiboarConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
      this.overlayManager.add(this.minimapOverlay);
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.clientThread.invokeLater(() -> {
            this.inHerbiboarArea = this.checkArea();
            this.updateTrailData();
         });
      }

   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
      this.overlayManager.remove(this.minimapOverlay);
      this.resetTrailData();
      this.clearCache();
      this.inHerbiboarArea = false;
   }

   private void updateTrailData() {
      if (this.isInHerbiboarArea()) {
         boolean pathActive = false;
         boolean wasStarted = this.started;
         HerbiboarSearchSpot[] var3 = HerbiboarSearchSpot.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            HerbiboarSearchSpot spot = var3[var5];
            Iterator var7 = spot.getTrails().iterator();

            while(var7.hasNext()) {
               TrailToSpot trail = (TrailToSpot)var7.next();
               int value = this.client.getVarbitValue(trail.getVarbitId());
               if (value == trail.getValue()) {
                  this.currentGroup = spot.getGroup();
                  this.nextTrail = trail;
                  if (!this.currentPath.contains(spot)) {
                     this.currentPath.add(spot);
                  }
               } else if (value > 0) {
                  this.shownTrails.addAll(trail.getFootprintIds());
                  pathActive = true;
               }
            }
         }

         this.finishId = this.client.getVarbitValue(5766);
         this.started = this.client.getVarbitValue(5767) > 0 || this.currentGroup != null;
         boolean finished = !pathActive && this.started;
         if (!wasStarted && this.started) {
            this.startSpot = HerbiboarStart.from(this.startPoint);
         }

         if (finished) {
            this.resetTrailData();
         }

      }
   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked menuOpt) {
      if (this.inHerbiboarArea && !this.started && MenuAction.GAME_OBJECT_FIRST_OPTION == menuOpt.getMenuAction()) {
         switch (Text.removeTags(menuOpt.getMenuTarget())) {
            case "Rock":
            case "Mushroom":
            case "Driftwood":
               this.startPoint = WorldPoint.fromScene(this.client, menuOpt.getParam0(), menuOpt.getParam1(), this.client.getPlane());
            default:
         }
      }
   }

   private void resetTrailData() {
      log.debug("Reset trail data");
      this.shownTrails.clear();
      this.currentPath.clear();
      this.nextTrail = null;
      this.currentGroup = null;
      this.finishId = 0;
      this.started = false;
      this.startPoint = null;
      this.startSpot = null;
   }

   private void clearCache() {
      this.starts.clear();
      this.trails.clear();
      this.trailObjects.clear();
      this.tunnels.clear();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case HOPPING:
         case LOGGING_IN:
            this.resetTrailData();
            break;
         case LOADING:
            this.clearCache();
            this.inHerbiboarArea = this.checkArea();
      }

   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      this.updateTrailData();
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      this.onTileObject((TileObject)null, event.getGameObject());
   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      this.onTileObject(event.getGameObject(), (TileObject)null);
   }

   @Subscribe
   public void onGroundObjectSpawned(GroundObjectSpawned event) {
      this.onTileObject((TileObject)null, event.getGroundObject());
   }

   @Subscribe
   public void onGroundObjectDespawned(GroundObjectDespawned event) {
      this.onTileObject(event.getGroundObject(), (TileObject)null);
   }

   private void onTileObject(TileObject oldObject, TileObject newObject) {
      if (oldObject != null) {
         WorldPoint oldLocation = oldObject.getWorldLocation();
         this.starts.remove(oldLocation);
         this.trails.remove(oldLocation);
         this.trailObjects.remove(oldLocation);
         this.tunnels.remove(oldLocation);
      }

      if (newObject != null) {
         if (START_OBJECT_IDS.contains(newObject.getId())) {
            this.starts.put(newObject.getWorldLocation(), newObject);
         } else if (HerbiboarSearchSpot.isTrail(newObject.getId())) {
            this.trails.put(newObject.getWorldLocation(), newObject);
         } else if (HerbiboarSearchSpot.isSearchSpot(newObject.getWorldLocation())) {
            this.trailObjects.put(newObject.getWorldLocation(), newObject);
         } else {
            if (END_LOCATIONS.contains(newObject.getWorldLocation())) {
               this.tunnels.put(newObject.getWorldLocation(), newObject);
            }

         }
      }
   }

   private boolean checkArea() {
      int[] mapRegions = this.client.getMapRegions();
      Iterator var2 = HERBIBOAR_REGIONS.iterator();

      int region;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         region = (Integer)var2.next();
      } while(!ArrayUtils.contains(mapRegions, region));

      return true;
   }

   List<WorldPoint> getEndLocations() {
      return END_LOCATIONS;
   }

   public Client getClient() {
      return this.client;
   }

   public ClientThread getClientThread() {
      return this.clientThread;
   }

   public OverlayManager getOverlayManager() {
      return this.overlayManager;
   }

   public HerbiboarOverlay getOverlay() {
      return this.overlay;
   }

   public HerbiboarMinimapOverlay getMinimapOverlay() {
      return this.minimapOverlay;
   }

   public Map<WorldPoint, TileObject> getStarts() {
      return this.starts;
   }

   public Map<WorldPoint, TileObject> getTrails() {
      return this.trails;
   }

   public Map<WorldPoint, TileObject> getTrailObjects() {
      return this.trailObjects;
   }

   public Map<WorldPoint, TileObject> getTunnels() {
      return this.tunnels;
   }

   public Set<Integer> getShownTrails() {
      return this.shownTrails;
   }

   public List<HerbiboarSearchSpot> getCurrentPath() {
      return this.currentPath;
   }

   public boolean isInHerbiboarArea() {
      return this.inHerbiboarArea;
   }

   public TrailToSpot getNextTrail() {
      return this.nextTrail;
   }

   public HerbiboarSearchSpot.Group getCurrentGroup() {
      return this.currentGroup;
   }

   public int getFinishId() {
      return this.finishId;
   }

   public boolean isStarted() {
      return this.started;
   }

   public WorldPoint getStartPoint() {
      return this.startPoint;
   }

   public HerbiboarStart getStartSpot() {
      return this.startSpot;
   }
}
