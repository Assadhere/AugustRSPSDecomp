package net.runelite.client.plugins.mining;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

class MiningRocksOverlay extends Overlay {
   static final int DAEYALT_MAX_RESPAWN_TIME = 110;
   private static final int DAEYALT_MIN_RESPAWN_TIME = 91;
   private static final float DAEYALT_RANDOM_PERCENT_THRESHOLD = 0.8272727F;
   private static final Color DARK_GREEN = new Color(0, 100, 0);
   private static final int MOTHERLODE_UPPER_FLOOR_HEIGHT = -500;
   private final Client client;
   private final MiningPlugin plugin;

   @Inject
   private MiningRocksOverlay(Client client, MiningPlugin plugin) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.plugin = plugin;
      this.client = client;
   }

   public Dimension render(Graphics2D graphics) {
      List<RockRespawn> respawns = this.plugin.getRespawns();
      if (respawns.isEmpty()) {
         return null;
      } else {
         Instant now = Instant.now();
         Iterator var4 = respawns.iterator();

         while(true) {
            LocalPoint loc;
            float percent;
            Point point;
            Rock rock;
            LocalPoint localLocation;
            do {
               RockRespawn rockRespawn;
               do {
                  do {
                     do {
                        if (!var4.hasNext()) {
                           return null;
                        }

                        rockRespawn = (RockRespawn)var4.next();
                        loc = LocalPoint.fromWorld(this.client, rockRespawn.getWorldPoint());
                     } while(loc == null);

                     percent = (float)(now.toEpochMilli() - rockRespawn.getStartTime().toEpochMilli()) / (float)rockRespawn.getRespawnTime();
                     point = Perspective.localToCanvas(this.client, loc, this.client.getPlane(), rockRespawn.getZOffset());
                  } while(point == null);
               } while(percent > 1.0F);

               rock = rockRespawn.getRock();
               localLocation = this.client.getLocalPlayer().getLocalLocation();
            } while(rock == Rock.MLM_ORE_VEIN && this.isUpstairsMotherlode(localLocation) != this.isUpstairsMotherlode(loc));

            Color pieFillColor = Color.YELLOW;
            Color pieBorderColor = Color.ORANGE;
            if (rock == Rock.DAEYALT_ESSENCE && percent > 0.8272727F) {
               pieFillColor = Color.GREEN;
               pieBorderColor = DARK_GREEN;
            }

            ProgressPieComponent ppc = new ProgressPieComponent();
            ppc.setBorderColor(pieBorderColor);
            ppc.setFill(pieFillColor);
            ppc.setPosition(point);
            ppc.setProgress((double)percent);
            ppc.render(graphics);
         }
      }
   }

   private boolean isUpstairsMotherlode(LocalPoint localPoint) {
      return Perspective.getTileHeight(this.client, localPoint, 0) < -500;
   }
}
