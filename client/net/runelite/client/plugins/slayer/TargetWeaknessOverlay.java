package net.runelite.client.plugins.slayer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.NPCManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class TargetWeaknessOverlay extends Overlay {
   private final Client client;
   private final SlayerConfig config;
   private final SlayerPlugin plugin;
   private final ItemManager itemManager;
   private final NPCManager npcManager;

   @Inject
   private TargetWeaknessOverlay(Client client, SlayerConfig config, SlayerPlugin plugin, ItemManager itemManager, NPCManager npcManager) {
      this.client = client;
      this.config = config;
      this.plugin = plugin;
      this.itemManager = itemManager;
      this.npcManager = npcManager;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.UNDER_WIDGETS);
   }

   public Dimension render(Graphics2D graphics) {
      List<NPC> targets = this.plugin.getTargets();
      if (!targets.isEmpty() && this.config.weaknessPrompt()) {
         Task curTask = Task.getTask(this.plugin.getTaskName());
         if (curTask != null && curTask.getWeaknessThreshold() >= 0 && curTask.getWeaknessItem() >= 0) {
            int threshold = curTask.getWeaknessThreshold();
            BufferedImage image = this.itemManager.getImage(curTask.getWeaknessItem());
            if (image == null) {
               return null;
            } else {
               Iterator var6 = targets.iterator();

               while(var6.hasNext()) {
                  NPC target = (NPC)var6.next();
                  int currentHealth = this.calculateHealth(target);
                  if (currentHealth >= 0 && currentHealth <= threshold) {
                     this.renderTargetItem(graphics, target, image);
                  }
               }

               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private int calculateHealth(NPC target) {
      if (target != null && target.getName() != null) {
         int healthScale = target.getHealthScale();
         int healthRatio = target.getHealthRatio();
         Integer maxHealth = this.npcManager.getHealth(target.getId());
         return healthRatio >= 0 && healthScale > 0 && maxHealth != null ? (int)((float)(maxHealth * healthRatio / healthScale) + 0.5F) : -1;
      } else {
         return -1;
      }
   }

   private void renderTargetItem(Graphics2D graphics, NPC actor, BufferedImage image) {
      LocalPoint actorPosition = actor.getLocalLocation();
      int offset = actor.getLogicalHeight() + 40;
      if (actorPosition != null && image != null) {
         Point imageLoc = Perspective.getCanvasImageLocation(this.client, actorPosition, image, offset);
         if (imageLoc != null) {
            OverlayUtil.renderImageLocation(graphics, imageLoc, image);
         }

      }
   }
}
