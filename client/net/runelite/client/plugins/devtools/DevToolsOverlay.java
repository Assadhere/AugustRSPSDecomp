package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Animation;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.GraphicsObject;
import net.runelite.api.ItemLayer;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Node;
import net.runelite.api.NpcOverrides;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

@Singleton
class DevToolsOverlay extends Overlay {
   private static final Font FONT = FontManager.getRunescapeFont().deriveFont(1, 16.0F);
   private static final Color RED = new Color(221, 44, 0);
   private static final Color GREEN = new Color(0, 200, 83);
   private static final Color ORANGE = new Color(255, 109, 0);
   private static final Color YELLOW = new Color(255, 214, 0);
   private static final Color CYAN = new Color(0, 184, 212);
   private static final Color BLUE = new Color(41, 98, 255);
   private static final Color DEEP_PURPLE = new Color(98, 0, 234);
   private static final Color PURPLE = new Color(170, 0, 255);
   private static final Color GRAY = new Color(158, 158, 158);
   private final Client client;
   private final DevToolsPlugin plugin;
   private final TooltipManager toolTipManager;

   @Inject
   private DevToolsOverlay(Client client, DevToolsPlugin plugin, TooltipManager toolTipManager) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.setPriority(1.0F);
      this.client = client;
      this.plugin = plugin;
      this.toolTipManager = toolTipManager;
   }

   public Dimension render(Graphics2D graphics) {
      WorldView tlwv = this.client.getTopLevelWorldView();
      WorldView playerWv = this.client.getLocalPlayer().getWorldView();
      graphics.setFont(FONT);
      if (this.plugin.getPlayers().isActive()) {
         this.renderPlayers(tlwv, graphics);
         if (playerWv != tlwv) {
            this.renderPlayers(playerWv, graphics);
         }
      }

      if (this.plugin.getNpcs().isActive()) {
         this.renderNpcs(tlwv, graphics);
         if (playerWv != tlwv) {
            this.renderNpcs(playerWv, graphics);
         }
      }

      if (this.plugin.getGroundItems().isActive() || this.plugin.getGroundObjects().isActive() || this.plugin.getGameObjects().isActive() || this.plugin.getWalls().isActive() || this.plugin.getDecorations().isActive() || this.plugin.getTileLocation().isActive() || this.plugin.getMovementFlags().isActive()) {
         this.renderTileObjects(tlwv, graphics);
         if (playerWv != tlwv) {
            this.renderTileObjects(playerWv, graphics);
         }
      }

      if (this.plugin.getProjectiles().isActive()) {
         this.renderProjectiles(graphics);
      }

      if (this.plugin.getGraphicsObjects().isActive()) {
         this.renderGraphicsObjects(tlwv, graphics);
         if (playerWv != tlwv) {
            this.renderGraphicsObjects(playerWv, graphics);
         }
      }

      if (this.plugin.getTileFlags().isActive()) {
         this.renderTileFlags(tlwv, graphics);
         if (playerWv != tlwv) {
            this.renderTileFlags(playerWv, graphics);
         }
      }

      if (this.plugin.getWorldEntities().isActive()) {
         this.renderWorldEntities(graphics);
      }

      return null;
   }

   private void renderTileFlags(WorldView wv, Graphics2D graphics) {
      byte[][][] settings = wv.getTileSettings();
      int z = wv.getPlane();

      for(int x = 0; x < 104; ++x) {
         for(int y = 0; y < 104; ++y) {
            boolean isbridge = (settings[1][x][y] & 2) != 0;
            int flag = settings[z][x][y];
            boolean isvisbelow = (flag & 8) != 0;
            boolean hasroof = (flag & 4) != 0;
            if (isbridge || isvisbelow || hasroof) {
               String s = "";
               if (isbridge) {
                  s = s + "B";
               }

               if (isvisbelow) {
                  s = s + "V";
               }

               if (hasroof) {
                  s = s + "R";
               }

               LocalPoint lp = new LocalPoint(x << 7, y << 7, wv);
               Point loc = Perspective.getCanvasTextLocation(this.client, graphics, lp, s, z);
               if (loc != null) {
                  OverlayUtil.renderTextLocation(graphics, loc, s, Color.RED);
               }
            }
         }
      }

   }

   private void renderPlayers(WorldView wv, Graphics2D graphics) {
      Player local = this.client.getLocalPlayer();
      Iterator var4 = wv.players().iterator();

      String var10000;
      while(var4.hasNext()) {
         Player p = (Player)var4.next();
         if (p != local) {
            var10000 = p.getName();
            String text = var10000 + " (A: " + p.getAnimation() + ") (P: " + p.getPoseAnimation() + ") (G: " + p.getGraphic() + ")";
            OverlayUtil.renderActorOverlay(graphics, p, text, BLUE);
         }
      }

      var10000 = local.getName();
      String text = var10000 + " (A: " + local.getAnimation() + ") (P: " + local.getPoseAnimation() + ") (G: " + local.getGraphic() + ")";
      OverlayUtil.renderActorOverlay(graphics, local, text, CYAN);
   }

   private void renderNpcs(WorldView wv, Graphics2D graphics) {
      NPC npc;
      Color color;
      String text;
      for(Iterator var3 = wv.npcs().iterator(); var3.hasNext(); OverlayUtil.renderActorOverlay(graphics, npc, text, color)) {
         npc = (NPC)var3.next();
         NPCComposition composition = npc.getComposition();
         color = composition.getCombatLevel() > 1 ? YELLOW : ORANGE;
         if (composition.getConfigs() != null) {
            NPCComposition transformedComposition = composition.transform();
            if (transformedComposition == null) {
               color = GRAY;
            } else {
               composition = transformedComposition;
            }
         }

         String var10000 = composition.getName();
         text = var10000 + " (ID:" + composition.getId() + ") (A: " + npc.getAnimation() + ") (P: " + npc.getPoseAnimation() + ") (G: " + npc.getGraphic() + ")";
         if (npc.getModelOverrides() != null) {
            NpcOverrides mo = npc.getModelOverrides();
            if (mo.getModelIds() != null) {
               text = text + " (M: " + Arrays.toString(mo.getModelIds()) + ")";
            }

            if (mo.getColorToReplaceWith() != null) {
               text = text + " (C: " + Arrays.toString(mo.getColorToReplaceWith()) + ")";
            }

            if (mo.getTextureToReplaceWith() != null) {
               text = text + " (T: " + Arrays.toString(mo.getTextureToReplaceWith()) + ")";
            }

            if (mo.useLocalPlayer()) {
               text = text + " (LocalPlayer)";
            }
         }
      }

   }

   private void renderTileObjects(WorldView wv, Graphics2D graphics) {
      Scene scene = wv.getScene();
      Tile[][][] tiles = scene.getTiles();
      int z = wv.getPlane();

      for(int x = 0; x < tiles[z].length; ++x) {
         for(int y = 0; y < tiles[z][x].length; ++y) {
            Tile tile = tiles[z][x][y];
            if (tile != null) {
               if (this.plugin.getGroundItems().isActive()) {
                  this.renderGroundItems(graphics, tile);
               }

               if (this.plugin.getGroundObjects().isActive()) {
                  this.renderTileObject(graphics, tile.getGroundObject(), PURPLE);
               }

               if (this.plugin.getGameObjects().isActive()) {
                  this.renderGameObjects(graphics, tile);
               }

               if (this.plugin.getWalls().isActive()) {
                  this.renderTileObject(graphics, tile.getWallObject(), GRAY);
               }

               if (this.plugin.getDecorations().isActive()) {
                  this.renderDecorObject(graphics, tile);
               }

               if (this.plugin.getTileLocation().isActive()) {
                  this.renderTileTooltip(graphics, tile);
               }

               if (this.plugin.getMovementFlags().isActive()) {
                  this.renderMovementInfo(graphics, tile);
               }
            }
         }
      }

   }

   private void renderTileTooltip(Graphics2D graphics, Tile tile) {
      LocalPoint tileLocalLocation = tile.getLocalLocation();
      Polygon poly = Perspective.getCanvasTilePoly(this.client, tileLocalLocation);
      if (poly != null && poly.contains(this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY())) {
         WorldPoint worldLocation = WorldPoint.fromLocalInstance(this.client, tileLocalLocation);
         byte flags = this.client.getTileSettings()[tile.getRenderLevel()][tile.getSceneLocation().getX()][tile.getSceneLocation().getY()];
         String tooltip = String.format("World location: %d, %d, %d<br>Region ID: %d location: %d, %d<br>Flags: %d", worldLocation.getX(), worldLocation.getY(), worldLocation.getPlane(), worldLocation.getRegionID(), worldLocation.getRegionX(), worldLocation.getRegionY(), flags);
         this.toolTipManager.add(new Tooltip(tooltip));
         OverlayUtil.renderPolygon(graphics, poly, GREEN);
      }

   }

   private void renderMovementInfo(Graphics2D graphics, Tile tile) {
      Polygon poly = Perspective.getCanvasTilePoly(this.client, tile.getLocalLocation());
      if (poly != null && poly.contains(this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY())) {
         if (this.client.getCollisionMaps() != null) {
            int[][] flags = this.client.getCollisionMaps()[this.client.getPlane()].getFlags();
            int data = flags[tile.getSceneLocation().getX()][tile.getSceneLocation().getY()];
            Set<MovementFlag> movementFlags = MovementFlag.getSetFlags(data);
            if (movementFlags.isEmpty()) {
               this.toolTipManager.add(new Tooltip("No movement flags"));
            } else {
               movementFlags.forEach((flag) -> {
                  this.toolTipManager.add(new Tooltip(flag.toString()));
               });
            }

            OverlayUtil.renderPolygon(graphics, poly, GREEN);
         }

      }
   }

   private void renderGroundItems(Graphics2D graphics, Tile tile) {
      ItemLayer itemLayer = tile.getItemLayer();
      if (itemLayer != null) {
         for(Node current = itemLayer.getTop(); current instanceof TileItem; current = ((Node)current).getNext()) {
            TileItem item = (TileItem)current;
            OverlayUtil.renderTileOverlay(graphics, itemLayer, "ID: " + item.getId() + " Qty:" + item.getQuantity(), RED);
         }
      }

   }

   private void renderGameObjects(Graphics2D graphics, Tile tile) {
      GameObject[] gameObjects = tile.getGameObjects();
      if (gameObjects != null) {
         GameObject[] var4 = gameObjects;
         int var5 = gameObjects.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            GameObject gameObject = var4[var6];
            if (gameObject != null && gameObject.getSceneMinLocation().equals(tile.getSceneLocation())) {
               StringBuilder stringBuilder = new StringBuilder();
               stringBuilder.append("ID: ").append(gameObject.getId());
               if (gameObject.getRenderable() instanceof DynamicObject) {
                  Animation animation = ((DynamicObject)gameObject.getRenderable()).getAnimation();
                  if (animation != null) {
                     stringBuilder.append(" A: ").append(animation.getId());
                  }
               }

               OverlayUtil.renderTileOverlay(graphics, gameObject, stringBuilder.toString(), GREEN);
            }
         }
      }

   }

   private void renderTileObject(Graphics2D graphics, TileObject tileObject, Color color) {
      if (tileObject != null) {
         OverlayUtil.renderTileOverlay(graphics, tileObject, "ID: " + tileObject.getId(), color);
      }

   }

   private void renderDecorObject(Graphics2D graphics, Tile tile) {
      DecorativeObject decorObject = tile.getDecorativeObject();
      if (decorObject != null) {
         OverlayUtil.renderTileOverlay(graphics, decorObject, "ID: " + decorObject.getId(), DEEP_PURPLE);
         Shape p = decorObject.getConvexHull();
         if (p != null) {
            graphics.draw(p);
         }

         p = decorObject.getConvexHull2();
         if (p != null) {
            graphics.draw(p);
         }
      }

   }

   private void renderProjectiles(Graphics2D graphics) {
      Iterator var2 = this.client.getProjectiles().iterator();

      while(var2.hasNext()) {
         Projectile projectile = (Projectile)var2.next();
         int projectileId = projectile.getId();
         String text = "(ID: " + projectileId + ")";
         int x = (int)projectile.getX();
         int y = (int)projectile.getY();
         LocalPoint projectilePoint = new LocalPoint(x, y);
         Point textLocation = Perspective.getCanvasTextLocation(this.client, graphics, projectilePoint, text, 0);
         if (textLocation != null) {
            OverlayUtil.renderTextLocation(graphics, textLocation, text, Color.RED);
         }
      }

   }

   private void renderGraphicsObjects(WorldView wv, Graphics2D graphics) {
      Iterator var3 = wv.getGraphicsObjects().iterator();

      while(var3.hasNext()) {
         GraphicsObject graphicsObject = (GraphicsObject)var3.next();
         LocalPoint lp = graphicsObject.getLocation();
         Polygon poly = Perspective.getCanvasTilePoly(this.client, lp);
         if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, Color.MAGENTA);
         }

         String infoString = "(ID: " + graphicsObject.getId() + ")";
         Point textLocation = Perspective.getCanvasTextLocation(this.client, graphics, lp, infoString, 0);
         if (textLocation != null) {
            OverlayUtil.renderTextLocation(graphics, textLocation, infoString, Color.WHITE);
         }
      }

   }

   private void renderWorldEntities(Graphics2D graphics) {
      WorldView toplevel = this.client.getTopLevelWorldView();
      Iterator var3 = toplevel.worldEntities().iterator();

      while(var3.hasNext()) {
         WorldEntity we = (WorldEntity)var3.next();
         if (!we.isHiddenForOverlap()) {
            LocalPoint location = we.getLocalLocation();
            int var10000 = we.getWorldView().getId();
            String text = "ID: " + var10000 + " Type: " + we.getConfig().getId();
            Point p = Perspective.getCanvasTextLocation(this.client, graphics, location, text, 0);
            if (p != null) {
               OverlayUtil.renderTextLocation(graphics, p, text, Color.BLUE);
            }
         }
      }

   }
}
