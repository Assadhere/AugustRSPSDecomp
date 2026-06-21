package net.runelite.client.plugins.blastmine;

import com.google.common.collect.ImmutableSet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.util.ColorUtil;

public class BlastMineRockOverlay extends Overlay {
   private static final int MAX_DISTANCE = 16;
   private static final int WARNING_DISTANCE = 1;
   private static final ImmutableSet<Integer> WALL_OBJECTS = ImmutableSet.of(28570, 28571, 28572, 28573, 28574, 28575, new Integer[]{28576, 28577, 28578, 28579, 28580, 28581, 28582, 28583, 28584, 28585, 28586, 28587, 28588});
   private final Client client;
   private final BlastMinePlugin plugin;
   private final BlastMinePluginConfig config;
   private final BufferedImage chiselIcon;
   private final BufferedImage dynamiteIcon;
   private final BufferedImage tinderboxIcon;

   @Inject
   private BlastMineRockOverlay(Client client, BlastMinePlugin plugin, BlastMinePluginConfig config, ItemManager itemManager) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.chiselIcon = itemManager.getImage(1755);
      this.dynamiteIcon = itemManager.getImage(13573);
      this.tinderboxIcon = itemManager.getImage(590);
   }

   public Dimension render(Graphics2D graphics) {
      Map<WorldPoint, BlastMineRock> rocks = this.plugin.getRocks();
      if (rocks.isEmpty()) {
         return null;
      } else {
         Tile[][][] tiles = this.client.getScene().getTiles();
         Iterator var4 = rocks.values().iterator();

         while(var4.hasNext()) {
            BlastMineRock rock = (BlastMineRock)var4.next();
            if (rock.getGameObject().getCanvasLocation() != null && rock.getGameObject().getWorldLocation().distanceTo(this.client.getLocalPlayer().getWorldLocation()) <= 16) {
               switch (rock.getType()) {
                  case NORMAL:
                     this.drawIconOnRock(graphics, rock, this.chiselIcon);
                     break;
                  case CHISELED:
                     this.drawIconOnRock(graphics, rock, this.dynamiteIcon);
                     break;
                  case LOADED:
                     this.drawIconOnRock(graphics, rock, this.tinderboxIcon);
                     break;
                  case LIT:
                     this.drawTimerOnRock(graphics, rock, this.config.getTimerColor());
                     this.drawAreaWarning(graphics, rock, this.config.getWarningColor(), tiles);
               }
            }
         }

         return null;
      }
   }

   private void drawIconOnRock(Graphics2D graphics, BlastMineRock rock, BufferedImage icon) {
      if (this.config.showRockIconOverlay()) {
         Point loc = Perspective.getCanvasImageLocation(this.client, rock.getGameObject().getLocalLocation(), icon, 150);
         if (loc != null) {
            graphics.drawImage(icon, loc.getX(), loc.getY(), (ImageObserver)null);
         }

      }
   }

   private void drawTimerOnRock(Graphics2D graphics, BlastMineRock rock, Color color) {
      if (this.config.showTimerOverlay()) {
         Point loc = Perspective.localToCanvas(this.client, rock.getGameObject().getLocalLocation(), rock.getGameObject().getPlane(), 150);
         if (loc != null) {
            double timeLeft = 1.0 - rock.getRemainingFuseTimeRelative();
            ProgressPieComponent pie = new ProgressPieComponent();
            pie.setFill(color);
            pie.setBorderColor(color);
            pie.setPosition(loc);
            pie.setProgress(timeLeft);
            pie.render(graphics);
         }

      }
   }

   private void drawAreaWarning(Graphics2D graphics, BlastMineRock rock, Color color, Tile[][][] tiles) {
      if (this.config.showWarningOverlay()) {
         int z = this.client.getPlane();
         int x = rock.getGameObject().getLocalLocation().getX() / 128;
         int y = rock.getGameObject().getLocalLocation().getY() / 128;
         int orientation = tiles[z][x][y].getWallObject().getOrientationA();
         switch (orientation) {
            case 1:
               --x;
               break;
            case 4:
               ++x;
               break;
            case 8:
               --y;
               break;
            default:
               ++y;
         }

         for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
               GameObject gameObject = tiles[z][x + i][y + j].getGameObjects()[0];
               if (gameObject == null || !WALL_OBJECTS.contains(gameObject.getId())) {
                  LocalPoint localTile = new LocalPoint((x + i) * 128 + 64, (y + j) * 128 + 64);
                  Polygon poly = Perspective.getCanvasTilePoly(this.client, localTile);
                  if (poly != null) {
                     graphics.setColor(ColorUtil.colorWithAlpha(color, (int)((double)color.getAlpha() / 2.5)));
                     graphics.fillPolygon(poly);
                  }
               }
            }
         }

      }
   }
}
