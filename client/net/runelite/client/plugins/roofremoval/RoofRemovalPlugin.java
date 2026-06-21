package net.runelite.client.plugins.roofremoval;

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.PreMapLoad;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Roof Removal",
   description = "Remove only the needed roofs above your player, hovered tile, or destination",
   enabledByDefault = false
)
public class RoofRemovalPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(RoofRemovalPlugin.class);
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private Gson gson;
   @Inject
   private RoofRemovalConfig config;
   private final Map<Integer, long[]> overrides = new HashMap();
   private final Set<Integer> configOverrideRegions = new HashSet();

   @Provides
   RoofRemovalConfig getConfig(ConfigManager configManager) {
      return (RoofRemovalConfig)configManager.getConfig(RoofRemovalConfig.class);
   }

   public void startUp() throws IOException {
      this.buildConfigOverrides();
      this.loadRoofOverrides();
      this.clientThread.invoke(() -> {
         Scene scene = this.client.getScene();
         if (scene == null) {
            return false;
         } else {
            scene.setRoofRemovalMode(this.buildRoofRemovalFlags());
            if (this.client.getGameState() == GameState.LOGGED_IN) {
               this.client.setGameState(GameState.LOADING);
            }

            return true;
         }
      });
   }

   public void shutDown() {
      this.overrides.clear();
      this.clientThread.invoke(() -> {
         this.client.getScene().setRoofRemovalMode(0);
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.client.setGameState(GameState.LOADING);
         }

      });
   }

   @Subscribe
   public void onPreMapLoad(PreMapLoad preMapLoad) {
      this.performRoofRemoval(preMapLoad.getScene());
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged e) {
      if (e.getGroup().equals("roofremoval")) {
         if (e.getKey().startsWith("remove")) {
            this.client.getScene().setRoofRemovalMode(this.buildRoofRemovalFlags());
         } else if (e.getKey().startsWith("override")) {
            this.buildConfigOverrides();
            this.clientThread.invoke(() -> {
               if (this.client.getGameState() == GameState.LOGGED_IN) {
                  this.client.setGameState(GameState.LOADING);
               }

            });
         }

      }
   }

   private int buildRoofRemovalFlags() {
      int roofRemovalMode = 0;
      if (this.config.removePosition()) {
         roofRemovalMode |= 1;
      }

      if (this.config.removeHovered()) {
         roofRemovalMode |= 2;
      }

      if (this.config.removeDestination()) {
         roofRemovalMode |= 4;
      }

      if (this.config.removeBetween()) {
         roofRemovalMode |= 8;
      }

      return roofRemovalMode;
   }

   private void buildConfigOverrides() {
      this.configOverrideRegions.clear();
      RoofRemovalConfigOverride[] var1 = RoofRemovalConfigOverride.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         RoofRemovalConfigOverride configOverride = var1[var3];
         if (configOverride.getEnabled().test(this.config)) {
            this.configOverrideRegions.addAll(configOverride.getRegions());
         }
      }

   }

   private void performRoofRemoval(Scene scene) {
      this.applyRoofOverrides(scene);
      Stopwatch sw = Stopwatch.createStarted();
      scene.buildRoofs();
      log.debug("Roof building duration: {}", sw.stop());
   }

   private void loadRoofOverrides() throws IOException {
      InputStream in = this.getClass().getResourceAsStream("overrides.jsonc");

      try {
         InputStreamReader data = new InputStreamReader(in, StandardCharsets.UTF_8);
         Type type = (new TypeToken<Map<Integer, List<FlaggedArea>>>() {
         }).getType();
         Map<Integer, List<FlaggedArea>> parsed = (Map)this.gson.fromJson(data, type);
         this.overrides.clear();
         Iterator var5 = parsed.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry<Integer, List<FlaggedArea>> entry = (Map.Entry)var5.next();
            Iterator var7 = ((List)entry.getValue()).iterator();

            while(var7.hasNext()) {
               FlaggedArea fla = (FlaggedArea)var7.next();

               for(int z = fla.z1; z <= fla.z2; ++z) {
                  int packedRegion = (Integer)entry.getKey() << 2 | z;
                  long[] regionData = (long[])this.overrides.computeIfAbsent(packedRegion, (k) -> {
                     return new long[64];
                  });

                  for(int y = fla.ry1; y <= fla.ry2; ++y) {
                     long row = regionData[y];

                     for(int x = fla.rx1; x <= fla.rx2; ++x) {
                        row |= 1L << x;
                     }

                     regionData[y] = row;
                  }
               }
            }
         }
      } catch (Throwable var17) {
         if (in != null) {
            try {
               in.close();
            } catch (Throwable var16) {
               var17.addSuppressed(var16);
            }
         }

         throw var17;
      }

      if (in != null) {
         in.close();
      }

   }

   private void applyRoofOverrides(Scene scene) {
      Stopwatch sw = Stopwatch.createStarted();
      boolean regionsHaveOverrides = false;
      int[] var4 = scene.getMapRegions();
      int var5 = var4.length;

      int z;
      int x;
      int y;
      label67:
      for(z = 0; z < var5; ++z) {
         x = var4[z];
         if (this.configOverrideRegions.contains(x)) {
            regionsHaveOverrides = true;
            break;
         }

         for(y = 0; y < 4; ++y) {
            if (this.overrides.containsKey(x << 2 | y)) {
               regionsHaveOverrides = true;
               break label67;
            }
         }
      }

      if (regionsHaveOverrides) {
         Tile[][][] tiles = scene.getExtendedTiles();
         byte[][][] settings = scene.getExtendedTileSettings();

         for(z = 0; z < 4; ++z) {
            for(x = 0; x < 184; ++x) {
               for(y = 0; y < 184; ++y) {
                  Tile tile = tiles[z][x][y];
                  if (tile != null) {
                     WorldPoint wp = WorldPoint.fromLocalInstance(scene, tile.getLocalLocation(), tile.getPlane());
                     int regionAndPlane = wp.getRegionID() << 2 | wp.getPlane();
                     if (this.configOverrideRegions.contains(wp.getRegionID())) {
                        settings[z][x][y] = (byte)(settings[z][x][y] | 4);
                     } else if (this.overrides.containsKey(regionAndPlane)) {
                        int rx = wp.getRegionX();
                        int ry = wp.getRegionY();
                        long[] region = (long[])this.overrides.get(regionAndPlane);
                        if ((region[ry] & 1L << rx) != 0L) {
                           settings[z][x][y] = (byte)(settings[z][x][y] | 4);
                        }
                     }
                  }
               }
            }
         }

         log.debug("Roof override duration: {}", sw.stop());
      }
   }

   private static class FlaggedArea {
      int rx1;
      int ry1;
      int rx2;
      int ry2;
      int z1;
      int z2;
   }
}
