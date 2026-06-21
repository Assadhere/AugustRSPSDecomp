package net.runelite.client.plugins.motherlode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.WallObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class MotherlodeSceneOverlay extends Overlay {
   private static final int MAX_DISTANCE = 2350;
   private static final int IMAGE_Z_OFFSET = 20;
   private final Client client;
   private final MotherlodePlugin plugin;
   private final MotherlodeConfig config;
   private final BufferedImage miningIcon;
   private final BufferedImage hammerIcon;

   @Inject
   MotherlodeSceneOverlay(Client client, MotherlodePlugin plugin, MotherlodeConfig config, SkillIconManager iconManager, ItemManager itemManager) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.miningIcon = iconManager.getSkillImage(Skill.MINING);
      this.hammerIcon = itemManager.getImage(2347);
   }

   public Dimension render(Graphics2D graphics) {
      if ((this.config.showVeins() || this.config.showRockFalls()) && this.plugin.isInMlm()) {
         Player local = this.client.getLocalPlayer();
         this.renderTiles(graphics, local);
         return null;
      } else {
         return null;
      }
   }

   private void renderTiles(Graphics2D graphics, Player local) {
      LocalPoint localLocation = local.getLocalLocation();
      Iterator var4;
      LocalPoint location;
      if (this.config.showVeins()) {
         var4 = this.plugin.getVeins().iterator();

         while(var4.hasNext()) {
            WallObject vein = (WallObject)var4.next();
            location = vein.getLocalLocation();
            if (localLocation.distanceTo(location) <= 2350 && this.plugin.isUpstairs(localLocation) == this.plugin.isUpstairs(vein.getLocalLocation())) {
               this.renderVein(graphics, vein);
            }
         }
      }

      GameObject brokenStrut;
      if (this.config.showRockFalls()) {
         var4 = this.plugin.getRocks().iterator();

         while(var4.hasNext()) {
            brokenStrut = (GameObject)var4.next();
            location = brokenStrut.getLocalLocation();
            if (localLocation.distanceTo(location) <= 2350) {
               this.renderRock(graphics, brokenStrut);
            }
         }
      }

      if (this.config.showBrokenStruts()) {
         var4 = this.plugin.getBrokenStruts().iterator();

         while(var4.hasNext()) {
            brokenStrut = (GameObject)var4.next();
            location = brokenStrut.getLocalLocation();
            if (localLocation.distanceTo(location) <= 2350) {
               this.renderBrokenStrut(graphics, brokenStrut);
            }
         }
      }

   }

   private void renderVein(Graphics2D graphics, WallObject vein) {
      Point canvasLoc = Perspective.getCanvasImageLocation(this.client, vein.getLocalLocation(), this.miningIcon, 150);
      if (canvasLoc != null) {
         graphics.drawImage(this.miningIcon, canvasLoc.getX(), canvasLoc.getY(), (ImageObserver)null);
      }

   }

   private void renderRock(Graphics2D graphics, GameObject rock) {
      Polygon poly = Perspective.getCanvasTilePoly(this.client, rock.getLocalLocation());
      if (poly != null) {
         OverlayUtil.renderPolygon(graphics, poly, Color.red);
      }

   }

   private void renderBrokenStrut(Graphics2D graphics, GameObject brokenStrut) {
      Polygon poly = Perspective.getCanvasTilePoly(this.client, brokenStrut.getLocalLocation());
      if (poly != null) {
         OverlayUtil.renderPolygon(graphics, poly, Color.red);
         OverlayUtil.renderImageLocation(this.client, graphics, brokenStrut.getLocalLocation(), this.hammerIcon, 20);
      }

   }
}
