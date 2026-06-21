package net.runelite.client.plugins.driftnet;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class DriftNetOverlay extends Overlay {
   private final DriftNetConfig config;
   private final DriftNetPlugin plugin;

   @Inject
   private DriftNetOverlay(DriftNetConfig config, DriftNetPlugin plugin) {
      this.config = config;
      this.plugin = plugin;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.0F);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
   }

   public Dimension render(Graphics2D graphics) {
      if (!this.plugin.isInDriftNetArea()) {
         return null;
      } else {
         if (this.config.highlightUntaggedFish()) {
            this.renderFish(graphics);
         }

         if (this.config.showNetStatus()) {
            this.renderNets(graphics);
         }

         if (this.config.tagAnnetteWhenNoNets()) {
            this.renderAnnette(graphics);
         }

         return null;
      }
   }

   private void renderFish(Graphics2D graphics) {
      Iterator var2 = this.plugin.getFish().iterator();

      while(var2.hasNext()) {
         NPC fish = (NPC)var2.next();
         if (!this.plugin.getTaggedFish().containsKey(fish)) {
            OverlayUtil.renderActorOverlay(graphics, fish, "", this.config.untaggedFishColor());
         }
      }

   }

   private void renderNets(Graphics2D graphics) {
      Iterator var2 = this.plugin.getNETS().iterator();

      while(var2.hasNext()) {
         DriftNet net = (DriftNet)var2.next();
         Shape polygon = net.getNet().getConvexHull();
         if (polygon != null) {
            OverlayUtil.renderPolygon(graphics, polygon, net.getStatus().getColor());
         }

         String text = net.getFormattedCountText();
         Point textLocation = net.getNet().getCanvasTextLocation(graphics, text, 0);
         if (textLocation != null) {
            OverlayUtil.renderTextLocation(graphics, textLocation, text, this.config.countColor());
         }
      }

   }

   private void renderAnnette(Graphics2D graphics) {
      GameObject annette = this.plugin.getAnnette();
      if (annette != null && !this.plugin.isDriftNetsInInventory()) {
         OverlayUtil.renderPolygon(graphics, annette.getConvexHull(), this.config.annetteTagColor());
      }

   }
}
