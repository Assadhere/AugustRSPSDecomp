package net.runelite.client.plugins.groundmarkers;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.util.concurrent.Runnables;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Tile;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.WorldViewLoaded;
import net.runelite.api.events.WorldViewUnloaded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Ground Markers",
   description = "Enable marking of tiles using the Shift key",
   tags = {"overlay", "tiles"}
)
public class GroundMarkerPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(GroundMarkerPlugin.class);
   private static final String CONFIG_GROUP = "groundMarker";
   private static final String REGION_PREFIX = "region_";
   private final ListMultimap<WorldView, ColorTileMarker> points = ArrayListMultimap.create();
   @Inject
   private Client client;
   @Inject
   private GroundMarkerConfig config;
   @Inject
   private ConfigManager configManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private GroundMarkerOverlay overlay;
   @Inject
   private GroundMarkerMinimapOverlay minimapOverlay;
   @Inject
   private ChatboxPanelManager chatboxPanelManager;
   @Inject
   private EventBus eventBus;
   @Inject
   private GroundMarkerSharingManager sharingManager;
   @Inject
   private Gson gson;
   @Inject
   private ColorPickerManager colorPickerManager;

   void savePoints(int regionId, Collection<GroundMarkerPoint> points) {
      if (points != null && !points.isEmpty()) {
         String json = this.gson.toJson(points);
         this.configManager.setConfiguration("groundMarker", "region_" + regionId, json);
      } else {
         this.configManager.unsetConfiguration("groundMarker", "region_" + regionId);
      }
   }

   Collection<GroundMarkerPoint> getPoints(int regionId) {
      String json = this.configManager.getConfiguration("groundMarker", "region_" + regionId);
      return (Collection)(Strings.isNullOrEmpty(json) ? Collections.emptyList() : (Collection)this.gson.fromJson(json, (new TypeToken<List<GroundMarkerPoint>>() {
      }).getType()));
   }

   @Provides
   GroundMarkerConfig provideConfig(ConfigManager configManager) {
      return (GroundMarkerConfig)configManager.getConfig(GroundMarkerConfig.class);
   }

   void loadPoints() {
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

   void loadPoints(WorldView wv) {
      this.points.removeAll(wv);
      int[] regions = wv.getMapRegions();
      if (regions != null) {
         int[] var3 = regions;
         int var4 = regions.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int regionId = var3[var5];
            log.debug("Loading points for region {}", regionId);
            Collection<GroundMarkerPoint> regionPoints = this.getPoints(regionId);
            Collection<ColorTileMarker> colorTileMarkers = this.translateToColorTileMarker(wv, regionPoints);
            this.points.putAll(wv, colorTileMarkers);
         }

      }
   }

   private Collection<ColorTileMarker> translateToColorTileMarker(WorldView wv, Collection<GroundMarkerPoint> points) {
      return (Collection)(points.isEmpty() ? Collections.emptyList() : (Collection)points.stream().map((point) -> {
         return new ColorTileMarker(WorldPoint.fromRegion(point.getRegionId(), point.getRegionX(), point.getRegionY(), point.getZ()), point.getColor(), point.getLabel());
      }).flatMap((colorTile) -> {
         Collection<WorldPoint> localWorldPoints = WorldPoint.toLocalInstance(wv, colorTile.getWorldPoint());
         return localWorldPoints.stream().map((wp) -> {
            return new ColorTileMarker(wp, colorTile.getColor(), colorTile.getLabel());
         });
      }).collect(Collectors.toList()));
   }

   public void startUp() {
      this.overlayManager.add(this.overlay);
      this.overlayManager.add(this.minimapOverlay);
      if (this.config.showImportExport()) {
         this.sharingManager.addImportExportMenuOptions();
         this.sharingManager.addClearMenuOption();
      }

      this.loadPoints();
      this.eventBus.register(this.sharingManager);
   }

   public void shutDown() {
      this.eventBus.unregister((Object)this.sharingManager);
      this.overlayManager.remove(this.overlay);
      this.overlayManager.remove(this.minimapOverlay);
      this.sharingManager.removeMenuOptions();
      this.points.clear();
   }

   @Subscribe
   public void onProfileChanged(ProfileChanged profileChanged) {
      this.loadPoints();
   }

   @Subscribe
   public void onWorldViewLoaded(WorldViewLoaded event) {
      this.loadPoints(event.getWorldView());
   }

   @Subscribe
   public void onWorldViewUnloaded(WorldViewUnloaded event) {
      this.points.removeAll(event.getWorldView());
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      MenuAction menuAction = event.getMenuEntry().getType();
      boolean hotKeyPressed = this.client.isKeyPressed(81);
      if (hotKeyPressed && (menuAction == MenuAction.WALK || menuAction == MenuAction.SET_HEADING)) {
         int worldId = event.getMenuEntry().getWorldViewId();
         WorldView wv = this.client.getWorldView(worldId);
         if (wv == null) {
            return;
         }

         Tile selectedSceneTile = wv.getSelectedSceneTile();
         if (selectedSceneTile == null) {
            return;
         }

         WorldPoint worldPoint = WorldPoint.fromLocalInstance(this.client, selectedSceneTile.getLocalLocation());
         int regionId = worldPoint.getRegionID();
         Collection<GroundMarkerPoint> regionPoints = this.getPoints(regionId);
         Optional<GroundMarkerPoint> existingOpt = regionPoints.stream().filter((p) -> {
            return p.getRegionX() == worldPoint.getRegionX() && p.getRegionY() == worldPoint.getRegionY() && p.getZ() == worldPoint.getPlane();
         }).findFirst();
         this.client.createMenuEntry(-1).setOption(existingOpt.isPresent() ? "Unmark" : "Mark").setTarget("Tile").setType(MenuAction.RUNELITE).onClick((e) -> {
            this.markTile(worldPoint);
         });
         if (existingOpt.isPresent()) {
            GroundMarkerPoint existing = (GroundMarkerPoint)existingOpt.get();
            this.client.createMenuEntry(-2).setOption("Label").setTarget("Tile").setType(MenuAction.RUNELITE).onClick((e) -> {
               this.labelTile(existing);
            });
            MenuEntry menuColor = this.client.createMenuEntry(-3).setOption("Color").setTarget("Tile").setType(MenuAction.RUNELITE);
            Menu submenu = menuColor.createSubMenu();
            if (regionPoints.size() > 1) {
               submenu.createMenuEntry(-1).setOption("Reset all").setType(MenuAction.RUNELITE).onClick((e) -> {
                  this.chatboxPanelManager.openTextMenuInput("Are you sure you want to reset the color of " + regionPoints.size() + " tiles?").option("Yes", () -> {
                     List<GroundMarkerPoint> newPoints = (List)regionPoints.stream().map((p) -> {
                        return new GroundMarkerPoint(p.getRegionId(), p.getRegionX(), p.getRegionY(), p.getZ(), this.config.markerColor(), p.getLabel());
                     }).collect(Collectors.toList());
                     this.savePoints(regionId, newPoints);
                     this.loadPoints();
                  }).option("No", Runnables.doNothing()).build();
               });
            }

            submenu.createMenuEntry(-1).setOption("Pick").setType(MenuAction.RUNELITE).onClick((e) -> {
               Color color = existing.getColor();
               SwingUtilities.invokeLater(() -> {
                  RuneliteColorPicker colorPicker = this.colorPickerManager.create(this.client, color, "Tile marker color", false);
                  colorPicker.setOnClose((c) -> {
                     this.colorTile(existing, c);
                  });
                  colorPicker.setVisible(true);
               });
            });
            List<Color> existingColors = (List)this.points.values().stream().map(ColorTileMarker::getColor).distinct().collect(Collectors.toList());
            Iterator var15 = existingColors.iterator();

            while(var15.hasNext()) {
               Color color = (Color)var15.next();
               if (!color.equals(existing.getColor())) {
                  submenu.createMenuEntry(-1).setOption(ColorUtil.prependColorTag("Color", color)).setType(MenuAction.RUNELITE).onClick((e) -> {
                     this.colorTile(existing, color);
                  });
               }
            }
         }
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("groundMarker") && event.getKey().equals("showImportExport")) {
         this.sharingManager.removeMenuOptions();
         if (this.config.showImportExport()) {
            this.sharingManager.addImportExportMenuOptions();
            this.sharingManager.addClearMenuOption();
         }
      }

   }

   private void markTile(WorldPoint worldPoint) {
      int regionId = worldPoint.getRegionID();
      GroundMarkerPoint point = new GroundMarkerPoint(regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), this.config.markerColor(), (String)null);
      log.debug("Updating point: {} - {}", point, worldPoint);
      List<GroundMarkerPoint> groundMarkerPoints = new ArrayList(this.getPoints(regionId));
      if (groundMarkerPoints.contains(point)) {
         groundMarkerPoints.remove(point);
      } else {
         groundMarkerPoints.add(point);
      }

      this.savePoints(regionId, groundMarkerPoints);
      this.loadPoints();
   }

   private void labelTile(GroundMarkerPoint existing) {
      this.chatboxPanelManager.openTextInput("Tile label").value((String)Optional.ofNullable(existing.getLabel()).orElse("")).onDone((input) -> {
         input = Strings.emptyToNull(input);
         GroundMarkerPoint newPoint = new GroundMarkerPoint(existing.getRegionId(), existing.getRegionX(), existing.getRegionY(), existing.getZ(), existing.getColor(), input);
         Collection<GroundMarkerPoint> points = new ArrayList(this.getPoints(existing.getRegionId()));
         points.remove(existing);
         points.add(newPoint);
         this.savePoints(existing.getRegionId(), points);
         this.loadPoints();
      }).build();
   }

   private void colorTile(GroundMarkerPoint existing, Color newColor) {
      GroundMarkerPoint newPoint = new GroundMarkerPoint(existing.getRegionId(), existing.getRegionX(), existing.getRegionY(), existing.getZ(), newColor, existing.getLabel());
      Collection<GroundMarkerPoint> points = new ArrayList(this.getPoints(existing.getRegionId()));
      points.remove(newPoint);
      points.add(newPoint);
      this.savePoints(existing.getRegionId(), points);
      this.loadPoints();
   }

   ListMultimap<WorldView, ColorTileMarker> getPoints() {
      return this.points;
   }
}
