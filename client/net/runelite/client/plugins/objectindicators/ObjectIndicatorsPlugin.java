package net.runelite.client.plugins.objectindicators;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.events.WorldViewLoaded;
import net.runelite.api.events.WorldViewUnloaded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Object Markers",
   description = "Enable marking of objects using the Shift key",
   tags = {"overlay", "objects", "mark", "marker"},
   enabledByDefault = false
)
public class ObjectIndicatorsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(ObjectIndicatorsPlugin.class);
   private static final String CONFIG_GROUP = "objectindicators";
   private static final String MARK = "Mark object";
   private static final String UNMARK = "Unmark object";
   private final List<ColorTileObject> objects = new ArrayList();
   private final Map<Integer, Set<ObjectPoint>> points = new HashMap();
   @Inject
   private Client client;
   @Inject
   private ConfigManager configManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private ObjectIndicatorsOverlay overlay;
   @Inject
   private ObjectIndicatorsConfig config;
   @Inject
   private Gson gson;
   @Inject
   private ColorPickerManager colorPickerManager;
   @Inject
   private ClientThread clientThread;

   @Provides
   ObjectIndicatorsConfig provideConfig(ConfigManager configManager) {
      return (ObjectIndicatorsConfig)configManager.getConfig(ObjectIndicatorsConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.overlay);
      this.clientThread.invokeLater(() -> {
         this.loadPoints();
      });
   }

   protected void shutDown() {
      this.overlayManager.remove(this.overlay);
      this.points.clear();
      this.objects.clear();
   }

   @Subscribe
   public void onProfileChanged(ProfileChanged e) {
      this.clientThread.invokeLater(() -> {
         this.loadPoints();
      });
   }

   @Subscribe
   public void onWallObjectSpawned(WallObjectSpawned event) {
      this.checkObjectPoints(event.getWallObject());
   }

   @Subscribe
   public void onWallObjectDespawned(WallObjectDespawned event) {
      this.objects.removeIf((o) -> {
         return o.getTileObject() == event.getWallObject();
      });
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      this.checkObjectPoints(event.getGameObject());
   }

   @Subscribe
   public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
      this.checkObjectPoints(event.getDecorativeObject());
   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      this.objects.removeIf((o) -> {
         return o.getTileObject() == event.getGameObject();
      });
   }

   @Subscribe
   public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
      this.objects.removeIf((o) -> {
         return o.getTileObject() == event.getDecorativeObject();
      });
   }

   @Subscribe
   public void onGroundObjectSpawned(GroundObjectSpawned event) {
      this.checkObjectPoints(event.getGroundObject());
   }

   @Subscribe
   public void onGroundObjectDespawned(GroundObjectDespawned event) {
      this.objects.removeIf((o) -> {
         return o.getTileObject() == event.getGroundObject();
      });
   }

   private void loadPoints() {
      this.points.clear();
      WorldView wv = this.client.getTopLevelWorldView();
      if (wv != null) {
         this.loadPoints(wv);
         Iterator var2 = wv.worldEntities().iterator();

         while(var2.hasNext()) {
            WorldEntity we = (WorldEntity)var2.next();
            this.loadPoints(we.getWorldView());
         }

      }
   }

   private void loadPoints(WorldView wv) {
      int[] regions = wv.getMapRegions();
      if (regions != null) {
         int[] var3 = regions;
         int var4 = regions.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int regionId = var3[var5];
            Set<ObjectPoint> regionPoints = this.loadPoints(regionId);
            if (regionPoints != null) {
               this.points.put(regionId, regionPoints);
            }
         }

      }
   }

   @Subscribe
   public void onWorldViewLoaded(WorldViewLoaded event) {
      this.loadPoints(event.getWorldView());
   }

   @Subscribe
   public void onWorldViewUnloaded(WorldViewUnloaded event) {
      WorldView wv = event.getWorldView();
      this.objects.removeIf((c) -> {
         return c.getTileObject().getWorldView() == wv;
      });
      if (wv.isTopLevel()) {
         IntStream var10000 = Arrays.stream(wv.getMapRegions());
         Map var10001 = this.points;
         Objects.requireNonNull(var10001);
         var10000.forEach(var10001::remove);
      }

   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      if (event.getType() == MenuAction.EXAMINE_OBJECT.getId() && this.client.isKeyPressed(81)) {
         int worldId = event.getMenuEntry().getWorldViewId();
         WorldView wv = this.client.getWorldView(worldId);
         if (wv != null) {
            TileObject tileObject = this.findTileObject(wv, event.getActionParam0(), event.getActionParam1(), event.getIdentifier());
            if (tileObject != null) {
               int idx = -1;
               Optional<ColorTileObject> marked = this.objects.stream().filter((o) -> {
                  return o.getTileObject() == tileObject;
               }).findFirst();
               this.client.createMenuEntry(idx--).setOption(marked.isPresent() ? "Unmark object" : "Mark object").setTarget(event.getTarget()).setWorldViewId(worldId).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setIdentifier(event.getIdentifier()).setType(MenuAction.RUNELITE).onClick(this::markObject);
               if (marked.isPresent()) {
                  idx = this.createTagBorderColorMenu(idx, event.getTarget(), tileObject, (ColorTileObject)marked.get());
                  idx = this.createTagFillColorMenu(idx, event.getTarget(), tileObject, (ColorTileObject)marked.get());
                  this.createTagStyleMenu(idx, event.getTarget(), tileObject);
               }

            }
         }
      }
   }

   private int createTagBorderColorMenu(int idx, String target, TileObject object, ColorTileObject colorTileObject) {
      List<Color> colors = this.getUsedColors(ObjectPoint::getBorderColor);
      Color[] var6 = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
      int var7 = var6.length;

      Color c;
      for(int var8 = 0; var8 < var7; ++var8) {
         c = var6[var8];
         if (colors.size() < 5 && !colors.contains(c)) {
            colors.add(c);
         }
      }

      MenuEntry parent = this.client.createMenuEntry(idx--).setOption("Mark border color").setTarget(target).setType(MenuAction.RUNELITE);
      Menu submenu = parent.createSubMenu();
      Iterator var12 = colors.iterator();

      while(var12.hasNext()) {
         c = (Color)var12.next();
         submenu.createMenuEntry(0).setOption(ColorUtil.prependColorTag("Set color", c)).setType(MenuAction.RUNELITE).onClick((e) -> {
            this.updateObjectConfig(object, (p) -> {
               p.setBorderColor(c);
            });
         });
      }

      submenu.createMenuEntry(0).setOption("Pick color").setType(MenuAction.RUNELITE).onClick((e) -> {
         SwingUtilities.invokeLater(() -> {
            RuneliteColorPicker colorPicker = this.colorPickerManager.create(this.client, (Color)MoreObjects.firstNonNull(colorTileObject.getBorderColor(), this.config.markerColor()), "Mark Border Color", false);
            colorPicker.setOnClose((c) -> {
               this.clientThread.invokeLater(() -> {
                  this.updateObjectConfig(object, (p) -> {
                     p.setBorderColor(c);
                  });
               });
            });
            colorPicker.setVisible(true);
         });
      });
      return idx;
   }

   private int createTagFillColorMenu(int idx, String target, TileObject object, ColorTileObject colorTileObject) {
      List<Color> colors = this.getUsedColors(ObjectPoint::getFillColor);
      Color[] var6 = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
      int var7 = var6.length;

      Color c;
      for(int var8 = 0; var8 < var7; ++var8) {
         c = var6[var8];
         c = ColorUtil.colorWithAlpha(c, c.getAlpha() / 12);
         if (colors.size() < 5 && !colors.contains(c)) {
            colors.add(c);
         }
      }

      MenuEntry parent = this.client.createMenuEntry(idx--).setOption("Mark fill color").setTarget(target).setType(MenuAction.RUNELITE);
      Menu submenu = parent.createSubMenu();
      Iterator var12 = colors.iterator();

      while(var12.hasNext()) {
         c = (Color)var12.next();
         submenu.createMenuEntry(0).setOption(ColorUtil.prependColorTag("Set color", c)).setType(MenuAction.RUNELITE).onClick((e) -> {
            this.updateObjectConfig(object, (p) -> {
               p.setFillColor(c);
            });
         });
      }

      submenu.createMenuEntry(0).setOption("Pick color").setType(MenuAction.RUNELITE).onClick((e) -> {
         SwingUtilities.invokeLater(() -> {
            Color previousColor = (Color)MoreObjects.firstNonNull(colorTileObject.getFillColor(), new Color(0, 0, 0, 50));
            RuneliteColorPicker colorPicker = this.colorPickerManager.create(this.client, previousColor, "Mark Fill Color", false);
            colorPicker.setOnClose((c) -> {
               this.clientThread.invokeLater(() -> {
                  this.updateObjectConfig(object, (p) -> {
                     p.setFillColor(c);
                  });
               });
            });
            colorPicker.setVisible(true);
         });
      });
      submenu.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((e) -> {
         this.updateObjectConfig(object, (p) -> {
            p.setFillColor((Color)null);
         });
      });
      return idx;
   }

   private int createTagStyleMenu(int idx, String target, TileObject object) {
      MenuEntry parent = this.client.createMenuEntry(idx--).setOption("Mark style").setTarget(target).setType(MenuAction.RUNELITE);
      Menu submenu = parent.createSubMenu();
      submenu.createMenuEntry(0).setOption("Hull").setType(MenuAction.RUNELITE).onClick((e) -> {
         this.updateObjectConfig(object, (c) -> {
            c.setHull(c.getHull() != Boolean.TRUE);
         });
      });
      submenu.createMenuEntry(0).setOption("Outline").setType(MenuAction.RUNELITE).onClick((e) -> {
         this.updateObjectConfig(object, (c) -> {
            c.setOutline(c.getOutline() != Boolean.TRUE);
         });
      });
      submenu.createMenuEntry(0).setOption("Clickbox").setType(MenuAction.RUNELITE).onClick((e) -> {
         this.updateObjectConfig(object, (c) -> {
            c.setClickbox(c.getClickbox() != Boolean.TRUE);
         });
      });
      submenu.createMenuEntry(0).setOption("Tile").setType(MenuAction.RUNELITE).onClick((e) -> {
         this.updateObjectConfig(object, (c) -> {
            c.setTile(c.getTile() != Boolean.TRUE);
         });
      });
      submenu.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((e) -> {
         this.updateObjectConfig(object, (c) -> {
            c.setHull((Boolean)null);
            c.setOutline((Boolean)null);
            c.setClickbox((Boolean)null);
            c.setTile((Boolean)null);
         });
      });
      return idx;
   }

   private void markObject(MenuEntry entry) {
      WorldView wv = this.client.getWorldView(entry.getWorldViewId());
      if (wv != null) {
         TileObject object = this.findTileObject(wv, entry.getParam0(), entry.getParam1(), entry.getIdentifier());
         if (object != null) {
            ObjectComposition objectDefinition = this.getObjectComposition(object.getId());
            String name = objectDefinition.getName();
            if (!Strings.isNullOrEmpty(name) && !name.equals("null")) {
               WorldPoint worldPoint = WorldPoint.fromLocalInstance(this.client, object.getLocalLocation());
               int regionId = worldPoint.getRegionID();
               Color borderColor = this.config.markerColor();
               Color fillColor = this.config.fillColor();
               ObjectPoint point = new ObjectPoint(object.getId(), name, regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), borderColor, fillColor, (Boolean)null, (Boolean)null, (Boolean)null, (Boolean)null);
               Set<ObjectPoint> objectPoints = (Set)this.points.computeIfAbsent(regionId, (k) -> {
                  return new HashSet();
               });
               if (objectPoints.removeIf(findObjectPredicate(objectDefinition, object, worldPoint))) {
                  this.unmarkObjects(this.client.getTopLevelWorldView(), worldPoint, objectDefinition);
                  log.debug("Unmarking object: {}", point);
               } else {
                  objectPoints.add(point);
                  this.markObjects(this.client.getTopLevelWorldView(), worldPoint, objectDefinition);
                  log.debug("Marking object: {}", point);
               }

               this.savePoints(regionId, objectPoints);
            }
         }
      }
   }

   private void updateObjectConfig(TileObject object, Consumer<ObjectPoint> c) {
      WorldPoint worldPoint = WorldPoint.fromLocalInstance(this.client, object.getLocalLocation());
      int regionId = worldPoint.getRegionID();
      Set<ObjectPoint> objectPoints = (Set)this.points.get(regionId);
      if (!objectPoints.isEmpty()) {
         ObjectComposition objectComposition = this.getObjectComposition(object.getId());
         ObjectPoint objectPoint = (ObjectPoint)objectPoints.stream().filter(findObjectPredicate(objectComposition, object, worldPoint)).findFirst().orElse((Object)null);
         if (objectPoint != null) {
            c.accept(objectPoint);
            this.savePoints(regionId, objectPoints);
            Iterator var8 = (new ArrayList(this.objects)).iterator();

            while(var8.hasNext()) {
               ColorTileObject o = (ColorTileObject)var8.next();
               if (o.getTileObject().getId() == object.getId()) {
                  this.objects.remove(o);
                  this.checkObjectPoints(o.getTileObject());
               }
            }

         }
      }
   }

   private void checkObjectPoints(TileObject object) {
      if (object.getPlane() >= 0) {
         WorldPoint worldPoint = WorldPoint.fromLocalInstance(this.client, object.getLocalLocation(), object.getPlane());
         Set<ObjectPoint> objectPoints = (Set)this.points.get(worldPoint.getRegionID());
         if (objectPoints != null) {
            ObjectComposition objectComposition = this.client.getObjectDefinition(object.getId());
            if (objectComposition.getImpostorIds() == null) {
               String name = objectComposition.getName();
               if (Strings.isNullOrEmpty(name) || name.equals("null")) {
                  return;
               }
            }

            Iterator var8 = objectPoints.iterator();

            while(var8.hasNext()) {
               ObjectPoint objectPoint = (ObjectPoint)var8.next();
               if (worldPoint.getRegionX() == objectPoint.getRegionX() && worldPoint.getRegionY() == objectPoint.getRegionY() && worldPoint.getPlane() == objectPoint.getZ() && objectPoint.getId() == object.getId()) {
                  log.debug("Marking object {} due to matching {}", object, objectPoint);
                  int flags = (objectPoint.getHull() == Boolean.TRUE ? 1 : 0) | (objectPoint.getOutline() == Boolean.TRUE ? 2 : 0) | (objectPoint.getClickbox() == Boolean.TRUE ? 4 : 0) | (objectPoint.getTile() == Boolean.TRUE ? 8 : 0);
                  this.objects.add(new ColorTileObject(object, objectComposition, objectPoint.getName(), objectPoint.getBorderColor(), objectPoint.getFillColor(), (byte)flags));
                  break;
               }
            }

         }
      }
   }

   private TileObject findTileObject(WorldView wv, int x, int y, int id) {
      int level = wv.getPlane();
      Scene scene = wv.getScene();
      Tile[][][] tiles = scene.getTiles();
      Tile tile = tiles[level][x][y];
      if (tile == null) {
         return null;
      } else {
         GameObject[] tileGameObjects = tile.getGameObjects();
         DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
         WallObject tileWallObject = tile.getWallObject();
         GroundObject groundObject = tile.getGroundObject();
         if (this.objectIdEquals(tileWallObject, id)) {
            return tileWallObject;
         } else if (this.objectIdEquals(tileDecorativeObject, id)) {
            return tileDecorativeObject;
         } else if (this.objectIdEquals(groundObject, id)) {
            return groundObject;
         } else {
            GameObject[] var13 = tileGameObjects;
            int var14 = tileGameObjects.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               GameObject object = var13[var15];
               if (this.objectIdEquals(object, id)) {
                  return object;
               }
            }

            return null;
         }
      }
   }

   private boolean objectIdEquals(TileObject tileObject, int id) {
      if (tileObject == null) {
         return false;
      } else if (tileObject.getId() == id) {
         return true;
      } else {
         ObjectComposition comp = this.client.getObjectDefinition(tileObject.getId());
         if (comp.getImpostorIds() != null) {
            int[] var4 = comp.getImpostorIds();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               int impostorId = var4[var6];
               if (impostorId == id) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private void markObjects(WorldView wv, WorldPoint p, ObjectComposition objectConfig) {
      Iterator var4 = WorldPoint.toLocalInstance(wv, p).iterator();

      while(var4.hasNext()) {
         WorldPoint sp = (WorldPoint)var4.next();
         int x = sp.getX() - wv.getBaseX();
         int y = sp.getY() - wv.getBaseY();
         TileObject object = this.findTileObject(wv, x, y, objectConfig.getId());
         if (object != null) {
            this.objects.add(new ColorTileObject(object, this.client.getObjectDefinition(object.getId()), objectConfig.getName(), this.config.markerColor(), this.config.fillColor(), (byte)0));
         }
      }

      var4 = wv.worldViews().iterator();

      while(var4.hasNext()) {
         WorldView sub = (WorldView)var4.next();
         this.markObjects(sub, p, objectConfig);
      }

   }

   private void unmarkObjects(WorldView wv, WorldPoint p, ObjectComposition objectConfig) {
      Iterator var4 = WorldPoint.toLocalInstance(wv, p).iterator();

      while(var4.hasNext()) {
         WorldPoint sp = (WorldPoint)var4.next();
         int x = sp.getX() - wv.getBaseX();
         int y = sp.getY() - wv.getBaseY();
         TileObject object = this.findTileObject(wv, x, y, objectConfig.getId());
         if (object != null && !this.objects.removeIf((o) -> {
            return o.getTileObject() == object;
         })) {
            log.warn("unable to find object point for unmarked object {}", object.getId());
         }
      }

      var4 = wv.worldViews().iterator();

      while(var4.hasNext()) {
         WorldView sub = (WorldView)var4.next();
         this.unmarkObjects(sub, p, objectConfig);
      }

   }

   private static Predicate<ObjectPoint> findObjectPredicate(ObjectComposition objectComposition, TileObject object, WorldPoint worldPoint) {
      return (op) -> {
         return (op.getId() == -1 || op.getId() == object.getId() || op.getName().equals(objectComposition.getName())) && op.getRegionX() == worldPoint.getRegionX() && op.getRegionY() == worldPoint.getRegionY() && op.getZ() == worldPoint.getPlane();
      };
   }

   private void savePoints(int id, Set<ObjectPoint> points) {
      if (points.isEmpty()) {
         this.configManager.unsetConfiguration("objectindicators", "region_" + id);
      } else {
         String json = this.gson.toJson(points);
         this.configManager.setConfiguration("objectindicators", "region_" + id, json);
      }

   }

   private Set<ObjectPoint> loadPoints(int id) {
      String json = this.configManager.getConfiguration("objectindicators", "region_" + id);
      if (Strings.isNullOrEmpty(json)) {
         return null;
      } else {
         Set<ObjectPoint> points = (Set)this.gson.fromJson(json, (new TypeToken<Set<ObjectPoint>>() {
         }).getType());
         return (Set)points.stream().filter((point) -> {
            return !point.getName().equals("null");
         }).collect(Collectors.toSet());
      }
   }

   @Nullable
   private ObjectComposition getObjectComposition(int id) {
      ObjectComposition objectComposition = this.client.getObjectDefinition(id);
      return objectComposition.getImpostorIds() == null ? objectComposition : objectComposition.getImpostor();
   }

   private List<Color> getUsedColors(Function<ObjectPoint, Color> getColor) {
      List<Color> colors = new ArrayList();
      int[] var3 = this.client.getMapRegions();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int region = var3[var5];
         Set<ObjectPoint> points = (Set)this.points.get(region);
         if (points != null) {
            Iterator var8 = points.iterator();

            while(var8.hasNext()) {
               ObjectPoint p = (ObjectPoint)var8.next();
               Color c = (Color)getColor.apply(p);
               if (c != null & !colors.contains(c)) {
                  colors.add(c);
                  if (colors.size() >= 5) {
                     return colors;
                  }
               }
            }
         }
      }

      return colors;
   }

   List<ColorTileObject> getObjects() {
      return this.objects;
   }
}
