package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class LocationOverlay extends OverlayPanel {
   private final Client client;
   private final DevToolsPlugin plugin;

   @Inject
   LocationOverlay(Client client, DevToolsPlugin plugin) {
      this.client = client;
      this.plugin = plugin;
      this.setPosition(OverlayPosition.TOP_LEFT);
   }

   public Dimension render(Graphics2D graphics) {
      if (!this.plugin.getLocation().isActive()) {
         return null;
      } else {
         WorldPoint worldPoint = this.client.getLocalPlayer().getWorldLocation();
         LocalPoint localPoint = this.client.getLocalPlayer().getLocalLocation();
         if (this.client.isInInstancedRegion()) {
            worldPoint = WorldPoint.fromLocalInstance(this.client, localPoint);
            this.panelComponent.getChildren().add(LineComponent.builder().left("Instance").build());
         }

         List var10000 = this.panelComponent.getChildren();
         LineComponent.LineComponentBuilder var10001 = LineComponent.builder().left("Local");
         int var10002 = localPoint.getX();
         var10000.add(var10001.right("" + var10002 + ", " + localPoint.getY()).build());
         var10000 = this.panelComponent.getChildren();
         var10001 = LineComponent.builder().left("World");
         var10002 = worldPoint.getX();
         var10000.add(var10001.right("" + var10002 + ", " + worldPoint.getY() + ", " + this.client.getPlane()).build());
         var10000 = this.panelComponent.getChildren();
         var10001 = LineComponent.builder().left("Scene");
         var10002 = localPoint.getSceneX();
         var10000.add(var10001.right("" + var10002 + ", " + localPoint.getSceneY()).build());
         int z;
         int cy;
         int cx;
         if (this.client.isInInstancedRegion()) {
            int[][][] instanceTemplateChunks = this.client.getInstanceTemplateChunks();
            z = this.client.getPlane();

            for(cy = 0; cy < 13; ++cy) {
               for(cx = 0; cx < 13; ++cx) {
                  int chunkData = instanceTemplateChunks[z][cx][cy];
                  if (chunkData != -1) {
                     int rotation = chunkData >> 1 & 3;
                     int chunkY = chunkData >> 3 & 2047;
                     int chunkX = chunkData >> 14 & 1023;
                     int chunkPlane = chunkData >> 24 & 3;
                     boolean myChunk = cx == localPoint.getSceneX() / 8 && cy == localPoint.getSceneY() / 8;
                     this.panelComponent.getChildren().add(LineComponent.builder().left("Chunk").right("" + chunkX + ", " + chunkY + ", " + chunkPlane).rightColor(myChunk ? Color.GREEN : Color.WHITE).build());
                  }
               }
            }
         } else {
            var10000 = this.panelComponent.getChildren();
            var10001 = LineComponent.builder().left("Base");
            var10002 = this.client.getBaseX();
            var10000.add(var10001.right("" + var10002 + ", " + this.client.getBaseY()).build());
         }

         for(int i = 0; i < this.client.getMapRegions().length; ++i) {
            z = this.client.getMapRegions()[i];
            cy = z >> 8;
            cx = z & 255;
            this.panelComponent.getChildren().add(LineComponent.builder().left(i == 0 ? "Map regions" : " ").right("" + cy + ", " + cx).rightColor(z == worldPoint.getRegionID() ? Color.GREEN : Color.WHITE).build());
         }

         return super.render(graphics);
      }
   }
}
