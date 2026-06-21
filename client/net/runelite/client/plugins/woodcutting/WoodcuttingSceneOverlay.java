package net.runelite.client.plugins.woodcutting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.util.ColorUtil;

class WoodcuttingSceneOverlay extends Overlay {
   private static final Color ROTTING_LEAVES = new Color(179, 0, 0);
   private static final Color GREEN_LEAVES = new Color(0, 179, 0);
   private static final Color DROPPINGS = new Color(120, 88, 76);
   private static final Color WILD_MUSHROOMS = new Color(220, 220, 220);
   private static final Color SPLINTERED_BARK = new Color(0, 0, 179);
   private final Client client;
   private final WoodcuttingConfig config;
   private final ItemManager itemManager;
   private final WoodcuttingPlugin plugin;

   @Inject
   private WoodcuttingSceneOverlay(Client client, WoodcuttingConfig config, ItemManager itemManager, WoodcuttingPlugin plugin) {
      this.client = client;
      this.config = config;
      this.itemManager = itemManager;
      this.plugin = plugin;
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.setPosition(OverlayPosition.DYNAMIC);
   }

   public Dimension render(Graphics2D graphics) {
      this.renderRedwoods(graphics);
      this.renderTimers(graphics);
      this.renderForestryRoots(graphics);
      this.renderForestrySapling(graphics);
      this.renderForestryFlowers(graphics);
      this.renderForestryPoachers(graphics);
      this.renderForestryPheasants(graphics);
      this.renderForestryBeeHive(graphics);
      this.renderEnchantmentRitual(graphics);
      this.renderLeprechaun(graphics);
      return null;
   }

   private void renderForestryRoots(Graphics2D graphics) {
      if (!this.plugin.getRoots().isEmpty() && this.config.highlightGlowingRoots()) {
         Iterator var2 = this.plugin.getRoots().iterator();

         while(var2.hasNext()) {
            GameObject treeRoot = (GameObject)var2.next();
            if (treeRoot.getId() == 47483) {
               Shape clickbox = treeRoot.getClickbox();
               if (clickbox != null) {
                  Color color = Color.GREEN;
                  graphics.setColor(color);
                  graphics.draw(clickbox);
                  graphics.setColor(ColorUtil.colorWithAlpha(color, color.getAlpha() / 5));
                  graphics.fill(clickbox);
               }
            }
         }
      }

   }

   private void renderForestrySapling(Graphics2D graphics) {
      if (!this.plugin.getSaplingIngredients().isEmpty() && this.config.highlightMulch()) {
         GameObject[] order = this.plugin.getSaplingOrder();
         StringBuilder sb = new StringBuilder();
         graphics.setFont(FontManager.getRunescapeBoldFont().deriveFont(16.0F));
         Iterator var4 = this.plugin.getSaplingIngredients().iterator();

         while(true) {
            GameObject ingredient;
            Color color;
            int numSolved;
            boolean found;
            do {
               char letter;
               label49:
               while(true) {
                  if (!var4.hasNext()) {
                     return;
                  }

                  ingredient = (GameObject)var4.next();
                  switch (ingredient.getId()) {
                     case 47493:
                        letter = 'R';
                        color = ROTTING_LEAVES;
                        break label49;
                     case 47494:
                        letter = 'G';
                        color = GREEN_LEAVES;
                        break label49;
                     case 47495:
                        letter = 'D';
                        color = DROPPINGS;
                        break label49;
                     case 47496:
                     case 47497:
                     case 47498:
                        letter = 'M';
                        color = WILD_MUSHROOMS;
                        break label49;
                     case 47499:
                        letter = 'B';
                        color = SPLINTERED_BARK;
                        break label49;
                  }
               }

               sb.setLength(0);
               sb.append(letter);
               numSolved = 0;
               found = false;

               for(int i = 0; i < order.length; ++i) {
                  if (order[i] == ingredient) {
                     found = true;
                     if (sb.length() == 1) {
                        sb.append(" -");
                     }

                     sb.append(' ').append(i + 1);
                  }

                  if (order[i] != null) {
                     ++numSolved;
                  }
               }
            } while(numSolved >= 3 && !found);

            Polygon poly = ingredient.getCanvasTilePoly();
            if (poly != null) {
               OverlayUtil.renderPolygon(graphics, poly, color);
            }

            String text = sb.toString();
            Point textLocation = ingredient.getCanvasTextLocation(graphics, text, 0);
            if (textLocation != null) {
               OverlayUtil.renderTextLocation(graphics, textLocation, text, Color.WHITE);
            }
         }
      }
   }

   private void renderForestryFlowers(Graphics2D graphics) {
      if (!this.plugin.getFlowers().isEmpty() && this.config.highlightFlowers()) {
         List<NPC> activeFlowers = this.plugin.getActiveFlowers();
         Iterator var3 = this.plugin.getFlowers().iterator();

         while(true) {
            NPC flower;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               flower = (NPC)var3.next();
            } while(activeFlowers.size() == 2 && !activeFlowers.contains(flower));

            Polygon poly = flower.getCanvasTilePoly();
            if (poly != null) {
               OverlayUtil.renderPolygon(graphics, poly, Color.YELLOW);
            }
         }
      }
   }

   private void renderForestryPoachers(Graphics2D graphics) {
      if (this.plugin.getFoxTrap() != null && this.config.highlightFoxTrap()) {
         NPC foxTrap = this.plugin.getFoxTrap();
         Polygon poly = foxTrap.getCanvasTilePoly();
         if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, Color.RED);
         }
      }

   }

   private void renderForestryPheasants(Graphics2D graphics) {
      if (!this.plugin.getPheasantNests().isEmpty() && this.config.highlightPheasantNest()) {
         Iterator var2 = this.plugin.getPheasantNests().iterator();

         while(var2.hasNext()) {
            GameObject nest = (GameObject)var2.next();
            if (nest.getId() == 49937) {
               Polygon poly = nest.getCanvasTilePoly();
               if (poly != null) {
                  OverlayUtil.renderPolygon(graphics, poly, Color.GREEN);
               }
            }
         }

         NPC forester = this.plugin.getFreakyForester();
         if (forester != null) {
            Polygon poly = forester.getCanvasTilePoly();
            if (poly != null) {
               OverlayUtil.renderPolygon(graphics, poly, Color.GREEN);
            }
         }
      }

   }

   private void renderForestryBeeHive(Graphics2D graphics) {
      if (this.plugin.getUnfinishedBeeHive() != null && this.config.highlightBeeHive()) {
         NPC beehive = this.plugin.getUnfinishedBeeHive();
         Polygon poly = beehive.getCanvasTilePoly();
         if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, Color.ORANGE);
         }
      }

   }

   private void renderEnchantmentRitual(Graphics2D graphics) {
      NPC solution = this.plugin.solveCircles();
      if (solution != null && this.config.highlightRitualCircle()) {
         Polygon poly = solution.getCanvasTilePoly();
         if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, Color.GREEN);
         }
      }

   }

   private void renderLeprechaun(Graphics2D graphics) {
      if (!this.plugin.getEndOfRainbows().isEmpty() && this.config.highlightLeprechaunRainbow()) {
         Iterator var2 = this.plugin.getEndOfRainbows().iterator();

         while(var2.hasNext()) {
            GameObject rainbow = (GameObject)var2.next();
            Polygon poly = rainbow.getCanvasTilePoly();
            if (poly != null) {
               OverlayUtil.renderPolygon(graphics, poly, Color.GREEN);
            }
         }
      }

   }

   private void renderRedwoods(Graphics2D graphics) {
      if (this.plugin.getSession() != null && this.config.showRedwoodTrees()) {
         Iterator var2 = this.plugin.getRedwoods().iterator();

         while(var2.hasNext()) {
            GameObject treeObject = (GameObject)var2.next();
            if (treeObject.getWorldLocation().distanceTo(this.client.getLocalPlayer().getWorldLocation()) <= 12) {
               OverlayUtil.renderImageLocation(this.client, graphics, treeObject.getLocalLocation(), this.itemManager.getImage(19669), 120);
            }
         }

      }
   }

   private void renderTimers(Graphics2D graphics) {
      List<TreeRespawn> respawns = this.plugin.getRespawns();
      if (!respawns.isEmpty() && this.config.showRespawnTimers()) {
         Instant now = Instant.now();
         Iterator var4 = respawns.iterator();

         while(var4.hasNext()) {
            TreeRespawn treeRespawn = (TreeRespawn)var4.next();
            LocalPoint minLocation = LocalPoint.fromWorld(this.client, treeRespawn.getWorldLocation());
            if (minLocation != null) {
               LocalPoint centeredLocation = new LocalPoint(minLocation.getX() + treeRespawn.getLenX() * 64, minLocation.getY() + treeRespawn.getLenY() * 64);
               float percent = (float)(now.toEpochMilli() - treeRespawn.getStartTime().toEpochMilli()) / (float)treeRespawn.getRespawnTime();
               Point point = Perspective.localToCanvas(this.client, centeredLocation, this.client.getPlane());
               if (point != null && !(percent > 1.0F)) {
                  ProgressPieComponent ppc = new ProgressPieComponent();
                  ppc.setBorderColor(Color.ORANGE);
                  ppc.setFill(Color.YELLOW);
                  ppc.setPosition(point);
                  ppc.setProgress((double)percent);
                  ppc.render(graphics);
               }
            }
         }

      }
   }
}
