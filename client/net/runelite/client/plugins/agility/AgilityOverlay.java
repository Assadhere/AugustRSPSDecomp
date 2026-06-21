package net.runelite.client.plugins.agility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.AgilityShortcut;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;

class AgilityOverlay extends Overlay {
   private static final int MAX_DISTANCE = 2350;
   private static final Color SHORTCUT_HIGH_LEVEL_COLOR;
   private final Client client;
   private final AgilityPlugin plugin;
   private final AgilityConfig config;

   @Inject
   private AgilityOverlay(Client client, AgilityPlugin plugin, AgilityConfig config) {
      super(plugin);
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D graphics) {
      LocalPoint playerLocation = this.client.getLocalPlayer().getLocalLocation();
      Point mousePosition = this.client.getMouseCanvasPosition();
      List<Tile> marksOfGrace = this.plugin.getMarksOfGrace();
      Tile stickTile = this.plugin.getStickTile();
      this.plugin.getObstacles().forEach((object, obstacle) -> {
         if ((!Obstacles.SHORTCUT_OBSTACLE_IDS.containsKey(object.getId()) || this.config.highlightShortcuts()) && (!Obstacles.TRAP_OBSTACLE_IDS.contains(object.getId()) || this.config.showTrapOverlay()) && (!Obstacles.OBSTACLE_IDS.contains(object.getId()) || this.config.showClickboxes()) && (!Obstacles.SEPULCHRE_OBSTACLE_IDS.contains(object.getId()) || this.config.highlightSepulchreObstacles()) && (!Obstacles.SEPULCHRE_SKILL_OBSTACLE_IDS.contains(object.getId()) || this.config.highlightSepulchreSkilling())) {
            Tile tile = obstacle.getTile();
            if (tile.getPlane() == this.client.getPlane() && object.getLocalLocation().distanceTo(playerLocation) < 2350) {
               if (Obstacles.TRAP_OBSTACLE_IDS.contains(object.getId())) {
                  Polygon polygon = object.getCanvasTilePoly();
                  if (polygon != null) {
                     OverlayUtil.renderPolygon(graphics, polygon, this.config.getTrapColor());
                  }

                  return;
               }

               Shape objectClickbox = object.getClickbox();
               if (objectClickbox != null) {
                  AgilityShortcut agilityShortcut = obstacle.getShortcut();
                  Color configColor = agilityShortcut != null && agilityShortcut.getLevel() > this.plugin.getAgilityLevel() ? SHORTCUT_HIGH_LEVEL_COLOR : this.config.getOverlayColor();
                  if (this.config.highlightMarks() && !marksOfGrace.isEmpty()) {
                     configColor = this.config.getMarkColor();
                  }

                  if (Obstacles.PORTAL_OBSTACLE_IDS.contains(object.getId())) {
                     if (!this.config.highlightPortals()) {
                        return;
                     }

                     configColor = this.config.getPortalsColor();
                  }

                  if (objectClickbox.contains((double)mousePosition.getX(), (double)mousePosition.getY())) {
                     graphics.setColor(configColor.darker());
                  } else {
                     graphics.setColor(configColor);
                  }

                  graphics.draw(objectClickbox);
                  graphics.setColor(ColorUtil.colorWithAlpha(configColor, configColor.getAlpha() / 5));
                  graphics.fill(objectClickbox);
               }
            }

         }
      });
      if (this.config.highlightMarks() && !marksOfGrace.isEmpty()) {
         Iterator var6 = marksOfGrace.iterator();

         while(var6.hasNext()) {
            Tile markOfGraceTile = (Tile)var6.next();
            this.highlightTile(graphics, playerLocation, markOfGraceTile, this.config.getMarkColor());
         }
      }

      if (stickTile != null && this.config.highlightStick()) {
         this.highlightTile(graphics, playerLocation, stickTile, this.config.stickHighlightColor());
      }

      Set<NPC> npcs = this.plugin.getNpcs();
      if (!npcs.isEmpty() && this.config.highlightSepulchreNpcs()) {
         Color color = this.config.sepulchreHighlightColor();
         Iterator var8 = npcs.iterator();

         while(var8.hasNext()) {
            NPC npc = (NPC)var8.next();
            Polygon tilePoly = npc.getCanvasTilePoly();
            if (tilePoly != null) {
               OverlayUtil.renderPolygon(graphics, tilePoly, color);
            }
         }
      }

      return null;
   }

   private void highlightTile(Graphics2D graphics, LocalPoint playerLocation, Tile tile, Color color) {
      if (tile.getPlane() == this.client.getPlane() && tile.getItemLayer() != null && tile.getLocalLocation().distanceTo(playerLocation) < 2350) {
         Polygon poly = tile.getItemLayer().getCanvasTilePoly();
         if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, color);
         }
      }

   }

   static {
      SHORTCUT_HIGH_LEVEL_COLOR = Color.ORANGE;
   }
}
