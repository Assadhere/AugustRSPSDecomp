package net.runelite.client.plugins.gpu;

import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.client.plugins.gpu.regions.Regions;

@Singleton
class RegionManager {
   private final GpuPluginConfig gpuConfig;
   private final Regions regions;

   @Inject
   RegionManager(GpuPluginConfig config) {
      this.gpuConfig = config;

      try {
         InputStream in = SceneUploader.class.getResourceAsStream("regions/regions.txt");

         try {
            this.regions = new Regions(in, "regions.txt");
         } catch (Throwable var6) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (in != null) {
            in.close();
         }

      } catch (IOException var7) {
         IOException ex = var7;
         throw new RuntimeException(ex);
      }
   }

   void prepare(Scene scene) {
      if (!scene.isInstance() && this.gpuConfig.hideUnrelatedMaps()) {
         int baseX = scene.getBaseX() / 8;
         int baseY = scene.getBaseY() / 8;
         int centerX = baseX + 6;
         int centerY = baseY + 6;
         int centerId = this.regions.getRegionId(centerX, centerY);
         int r = 11;

         for(int offx = -r; offx <= r; ++offx) {
            for(int offy = -r; offy <= r; ++offy) {
               int cx = centerX + offx;
               int cy = centerY + offy;
               int id = this.regions.getRegionId(cx, cy);
               if (id != centerId) {
                  removeZone(scene, cx, cy);
               }
            }
         }

      }
   }

   private static void removeZone(Scene scene, int cx, int cy) {
      int wx = cx * 8;
      int wy = cy * 8;
      int sx = wx - scene.getBaseX();
      int sy = wy - scene.getBaseY();
      int cmsx = sx + 40;
      int cmsy = sy + 40;
      Tile[][][] tiles = scene.getExtendedTiles();

      for(int x = 0; x < 8; ++x) {
         for(int y = 0; y < 8; ++y) {
            int msx = cmsx + x;
            int msy = cmsy + y;
            if (msx >= 0 && msx < 184 && msy >= 0 && msy < 184) {
               for(int z = 0; z < 4; ++z) {
                  Tile tile = tiles[z][msx][msy];
                  if (tile != null) {
                     scene.removeTile(tile);
                  }
               }
            }
         }
      }

   }
}
