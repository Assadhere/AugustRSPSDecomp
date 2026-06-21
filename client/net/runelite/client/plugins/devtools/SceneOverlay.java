package net.runelite.client.plugins.devtools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class SceneOverlay extends Overlay {
   private static final Color MAP_SQUARE_COLOR;
   private static final Color CHUNK_BORDER_COLOR;
   private static final Color LOCAL_VALID_MOVEMENT_COLOR;
   private static final Color VALID_MOVEMENT_COLOR;
   private static final Color LINE_OF_SIGHT_COLOR;
   private static final Color INTERACTING_COLOR;
   private static final int LOCAL_TILE_SIZE = 128;
   private static final int MAP_SQUARE_SIZE = 64;
   private static final int CULL_CHUNK_BORDERS_RANGE = 16;
   private static final int STROKE_WIDTH = 4;
   private static final int CULL_LINE_OF_SIGHT_RANGE = 10;
   private static final int INTERACTING_SHIFT = -16;
   private static final Polygon ARROW_HEAD;
   private final Client client;
   private final DevToolsPlugin plugin;

   @Inject
   public SceneOverlay(Client client, DevToolsPlugin plugin) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.client = client;
      this.plugin = plugin;
   }

   public Dimension render(Graphics2D graphics) {
      if (this.plugin.getZoneBorders().isActive()) {
         this.renderZoneBorders(graphics);
      }

      if (this.plugin.getMapSquares().isActive()) {
         this.renderMapSquares(graphics);
      }

      if (this.plugin.getLoadingLines().isActive()) {
         this.renderLoadingLines(graphics);
      }

      if (this.plugin.getLineOfSight().isActive()) {
         this.renderLineOfSight(graphics);
      }

      if (this.plugin.getValidMovement().isActive()) {
         this.renderValidMovement(graphics);
      }

      if (this.plugin.getInteracting().isActive()) {
         this.renderInteracting(graphics);
      }

      return null;
   }

   private void renderZoneBorders(Graphics2D graphics) {
      int startX = 8;
      int startZ = 8;
      int endX = 96;
      int endZ = 96;
      graphics.setStroke(new BasicStroke(4.0F));
      graphics.setColor(CHUNK_BORDER_COLOR);
      GeneralPath path = new GeneralPath();

      int z;
      boolean first;
      int x;
      LocalPoint lp;
      Point p;
      for(z = startX; z <= endX; z += 8) {
         first = true;

         for(x = startZ; x <= endZ; x += 8) {
            lp = new LocalPoint(z << 7, x << 7);
            p = Perspective.localToCanvas(this.client, lp, this.client.getPlane());
            if (p != null) {
               if (first) {
                  path.moveTo((float)p.getX(), (float)p.getY());
                  first = false;
               } else {
                  path.lineTo((float)p.getX(), (float)p.getY());
               }
            }
         }
      }

      for(z = startZ; z <= endZ; z += 8) {
         first = true;

         for(x = startX; x <= endX; x += 8) {
            lp = new LocalPoint(x << 7, z << 7);
            p = Perspective.localToCanvas(this.client, lp, this.client.getPlane());
            if (p != null) {
               if (first) {
                  path.moveTo((float)p.getX(), (float)p.getY());
                  first = false;
               } else {
                  path.lineTo((float)p.getX(), (float)p.getY());
               }
            }
         }
      }

      graphics.draw(path);
   }

   private void renderLoadingLines(Graphics2D graphics) {
      graphics.setStroke(new BasicStroke(4.0F));
      graphics.setColor(CHUNK_BORDER_COLOR);
      int off = 2048;
      int max = 13312;
      LocalPoint[] points = new LocalPoint[]{new LocalPoint(off, off), new LocalPoint(off, max - off), new LocalPoint(max - off, max - off), new LocalPoint(max - off, off)};

      for(int i = 0; i < 4; ++i) {
         LocalPoint lp0 = points[i];
         LocalPoint lp1 = points[(i + 1) % 4];
         Point p0 = Perspective.localToCanvas(this.client, lp0, this.client.getPlane());
         Point p1 = Perspective.localToCanvas(this.client, lp1, this.client.getPlane());
         if (p0 != null && p1 != null) {
            graphics.drawLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
         }
      }

   }

   private void renderMapSquares(Graphics2D graphics) {
      WorldPoint wp = this.client.getLocalPlayer().getWorldLocation();
      int startX = (wp.getX() - 16 + 64 - 1) / 64 * 64;
      int startY = (wp.getY() - 16 + 64 - 1) / 64 * 64;
      int endX = (wp.getX() + 16) / 64 * 64;
      int endY = (wp.getY() + 16) / 64 * 64;
      graphics.setStroke(new BasicStroke(4.0F));
      graphics.setColor(MAP_SQUARE_COLOR);
      GeneralPath path = new GeneralPath();

      int y;
      LocalPoint lp1;
      LocalPoint lp2;
      boolean first;
      int x;
      Point p;
      for(y = startX; y <= endX; y += 64) {
         lp1 = LocalPoint.fromWorld(this.client, y, wp.getY() - 16);
         lp2 = LocalPoint.fromWorld(this.client, y, wp.getY() + 16);
         first = true;

         for(x = lp1.getY(); x <= lp2.getY(); x += 128) {
            p = Perspective.localToCanvas(this.client, new LocalPoint(lp1.getX() - 64, x - 64), this.client.getPlane());
            if (p != null) {
               if (first) {
                  path.moveTo((float)p.getX(), (float)p.getY());
                  first = false;
               } else {
                  path.lineTo((float)p.getX(), (float)p.getY());
               }
            }
         }
      }

      for(y = startY; y <= endY; y += 64) {
         lp1 = LocalPoint.fromWorld(this.client, wp.getX() - 16, y);
         lp2 = LocalPoint.fromWorld(this.client, wp.getX() + 16, y);
         first = true;

         for(x = lp1.getX(); x <= lp2.getX(); x += 128) {
            p = Perspective.localToCanvas(this.client, new LocalPoint(x - 64, lp1.getY() - 64), this.client.getPlane());
            if (p != null) {
               if (first) {
                  path.moveTo((float)p.getX(), (float)p.getY());
                  first = false;
               } else {
                  path.lineTo((float)p.getX(), (float)p.getY());
               }
            }
         }
      }

      graphics.draw(path);
   }

   private void renderTileIfValidForMovement(Graphics2D graphics, Actor actor, int dx, int dy) {
      WorldArea area = actor.getWorldArea();
      if (area != null) {
         if (area.canTravelInDirection(this.client.getTopLevelWorldView(), dx, dy)) {
            LocalPoint lp = actor.getLocalLocation();
            if (lp == null) {
               return;
            }

            lp = new LocalPoint(lp.getX() + dx * 128 + dx * 128 * (area.getWidth() - 1) / 2, lp.getY() + dy * 128 + dy * 128 * (area.getHeight() - 1) / 2);
            Polygon poly = Perspective.getCanvasTilePoly(this.client, lp);
            if (poly == null) {
               return;
            }

            if (actor == this.client.getLocalPlayer()) {
               OverlayUtil.renderPolygon(graphics, poly, LOCAL_VALID_MOVEMENT_COLOR);
            } else {
               OverlayUtil.renderPolygon(graphics, poly, VALID_MOVEMENT_COLOR);
            }
         }

      }
   }

   private void renderValidMovement(Graphics2D graphics) {
      Player player = this.client.getLocalPlayer();
      List<NPC> npcs = this.client.getNpcs();
      Iterator var4 = npcs.iterator();

      while(true) {
         NPC npc;
         do {
            if (!var4.hasNext()) {
               for(int dx = -1; dx <= 1; ++dx) {
                  for(int dy = -1; dy <= 1; ++dy) {
                     if (dx != 0 || dy != 0) {
                        this.renderTileIfValidForMovement(graphics, player, dx, dy);
                     }
                  }
               }

               return;
            }

            npc = (NPC)var4.next();
         } while(player.getInteracting() != npc && npc.getInteracting() != player);

         for(int dx = -1; dx <= 1; ++dx) {
            for(int dy = -1; dy <= 1; ++dy) {
               if (dx != 0 || dy != 0) {
                  this.renderTileIfValidForMovement(graphics, npc, dx, dy);
               }
            }
         }
      }
   }

   private void renderTileIfHasLineOfSight(Graphics2D graphics, WorldArea start, int targetX, int targetY) {
      WorldPoint targetLocation = new WorldPoint(targetX, targetY, start.getPlane());
      if (start.hasLineOfSightTo(this.client.getTopLevelWorldView(), targetLocation)) {
         LocalPoint lp = LocalPoint.fromWorld(this.client, targetLocation);
         if (lp == null) {
            return;
         }

         Polygon poly = Perspective.getCanvasTilePoly(this.client, lp);
         if (poly == null) {
            return;
         }

         OverlayUtil.renderPolygon(graphics, poly, LINE_OF_SIGHT_COLOR);
      }

   }

   private void renderLineOfSight(Graphics2D graphics) {
      WorldArea area = this.client.getLocalPlayer().getWorldArea();

      for(int x = area.getX() - 10; x <= area.getX() + 10; ++x) {
         for(int y = area.getY() - 10; y <= area.getY() + 10; ++y) {
            if (x != area.getX() || y != area.getY()) {
               this.renderTileIfHasLineOfSight(graphics, area, x, y);
            }
         }
      }

   }

   private void renderInteracting(Graphics2D graphics) {
      WorldView tlwv = this.client.getTopLevelWorldView();
      WorldView playerWv = this.client.getLocalPlayer().getWorldView();
      this.renderInteracting(graphics, tlwv.players());
      this.renderInteracting(graphics, tlwv.npcs());
      if (playerWv != tlwv) {
         this.renderInteracting(graphics, playerWv.players());
         this.renderInteracting(graphics, playerWv.npcs());
      }

   }

   private void renderInteracting(Graphics2D graphics, IndexedObjectSet<? extends Actor> set) {
      Iterator var3 = set.iterator();

      while(var3.hasNext()) {
         Actor fa = (Actor)var3.next();
         Actor ta = fa.getInteracting();
         if (ta == null) {
            return;
         }

         LocalPoint fl = fa.getLocalLocation();
         Point fs = Perspective.localToCanvas(this.client, fl, this.client.getPlane(), fa.getLogicalHeight() / 2);
         if (fs == null) {
            return;
         }

         int fsx = fs.getX();
         int fsy = fs.getY() - -16;
         LocalPoint tl = ta.getLocalLocation();
         Point ts = Perspective.localToCanvas(this.client, tl, this.client.getPlane(), ta.getLogicalHeight() / 2);
         if (ts == null) {
            return;
         }

         int tsx = ts.getX();
         int tsy = ts.getY() - -16;
         graphics.setColor(INTERACTING_COLOR);
         graphics.drawLine(fsx, fsy, tsx, tsy);
         AffineTransform t = new AffineTransform();
         t.translate((double)tsx, (double)tsy);
         t.rotate((double)(tsx - fsx), (double)(tsy - fsy));
         t.rotate(-1.5707963267948966);
         AffineTransform ot = graphics.getTransform();
         graphics.setTransform(t);
         graphics.fill(ARROW_HEAD);
         graphics.setTransform(ot);
      }

   }

   static {
      MAP_SQUARE_COLOR = Color.GREEN;
      CHUNK_BORDER_COLOR = Color.BLUE;
      LOCAL_VALID_MOVEMENT_COLOR = new Color(141, 220, 26);
      VALID_MOVEMENT_COLOR = new Color(73, 122, 18);
      LINE_OF_SIGHT_COLOR = new Color(204, 42, 219);
      INTERACTING_COLOR = Color.CYAN;
      ARROW_HEAD = new Polygon(new int[]{0, -3, 3}, new int[]{0, -5, -5}, 3);
   }
}
