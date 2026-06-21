package net.runelite.client.plugins.fishing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.FishingSpot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.util.ImageUtil;

class FishingSpotOverlay extends Overlay {
   private static final Duration MINNOW_MOVE = Duration.ofSeconds(15L);
   private static final Duration MINNOW_WARN = Duration.ofSeconds(3L);
   private static final int ONE_TICK_AERIAL_FISHING = 3;
   private final FishingPlugin plugin;
   private final FishingConfig config;
   private final Client client;
   private final ItemManager itemManager;
   private boolean hidden;

   @Inject
   private FishingSpotOverlay(FishingPlugin plugin, FishingConfig config, Client client, ItemManager itemManager) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.plugin = plugin;
      this.config = config;
      this.client = client;
      this.itemManager = itemManager;
   }

   public Dimension render(Graphics2D graphics) {
      if (this.hidden) {
         return null;
      } else {
         FishingSpot previousSpot = null;
         WorldPoint previousLocation = null;
         Iterator var4 = this.plugin.getFishingSpots().iterator();

         while(true) {
            NPC npc;
            FishingSpot spot;
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        return null;
                     }

                     npc = (NPC)var4.next();
                     spot = FishingSpot.findSpot(npc.getId());
                  } while(spot == null);
               } while(this.config.onlyCurrentSpot() && this.plugin.getCurrentSpot() != null && this.plugin.getCurrentSpot() != spot);
            } while(previousSpot == spot && previousLocation.equals(npc.getWorldLocation()));

            Color color;
            if (npc.getGraphic() == 1387) {
               color = this.config.getMinnowsOverlayColor();
            } else if (spot == FishingSpot.COMMON_TENCH && npc.getWorldLocation().distanceTo2D(this.client.getLocalPlayer().getWorldLocation()) <= 3) {
               color = this.config.getAerialOverlayColor();
            } else if (spot == FishingSpot.HARPOONFISH && npc.getId() == 10569) {
               color = this.config.getHarpoonfishOverlayColor();
            } else {
               color = this.config.getOverlayColor();
            }

            if (spot == FishingSpot.MINNOW && this.config.showMinnowOverlay()) {
               MinnowSpot minnowSpot = (MinnowSpot)this.plugin.getMinnowSpots().get(npc.getIndex());
               if (minnowSpot != null) {
                  long millisLeft = MINNOW_MOVE.toMillis() - Duration.between(minnowSpot.getTime(), Instant.now()).toMillis();
                  if (millisLeft < MINNOW_WARN.toMillis()) {
                     color = Color.ORANGE;
                  }

                  LocalPoint localPoint = npc.getLocalLocation();
                  Point location = Perspective.localToCanvas(this.client, localPoint, this.client.getPlane());
                  if (location != null) {
                     ProgressPieComponent pie = new ProgressPieComponent();
                     pie.setFill(color);
                     pie.setBorderColor(color);
                     pie.setPosition(location);
                     pie.setProgress((double)((float)millisLeft / (float)MINNOW_MOVE.toMillis()));
                     pie.render(graphics);
                  }
               }
            }

            if (this.config.showSpotTiles()) {
               Polygon poly = npc.getCanvasTilePoly();
               if (poly != null) {
                  OverlayUtil.renderPolygon(graphics, poly, color.darker());
               }
            }

            Point imageLocation;
            if (this.config.showSpotIcons()) {
               BufferedImage fishImage = this.itemManager.getImage(spot.getFishSpriteId());
               if (spot == FishingSpot.COMMON_TENCH && npc.getWorldLocation().distanceTo2D(this.client.getLocalPlayer().getWorldLocation()) <= 3) {
                  fishImage = ImageUtil.outlineImage(this.itemManager.getImage(spot.getFishSpriteId()), color);
               }

               if (fishImage != null) {
                  imageLocation = npc.getCanvasImageLocation((BufferedImage)fishImage, npc.getLogicalHeight());
                  if (imageLocation != null) {
                     OverlayUtil.renderImageLocation(graphics, imageLocation, (BufferedImage)fishImage);
                  }
               }
            }

            if (this.config.showSpotNames()) {
               String text = spot.getName();
               imageLocation = npc.getCanvasTextLocation(graphics, text, npc.getLogicalHeight() + 40);
               if (imageLocation != null) {
                  OverlayUtil.renderTextLocation(graphics, imageLocation, text, color.darker());
               }
            }

            previousSpot = spot;
            previousLocation = npc.getWorldLocation();
         }
      }
   }

   void setHidden(boolean hidden) {
      this.hidden = hidden;
   }
}
