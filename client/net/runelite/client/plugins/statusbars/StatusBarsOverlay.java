package net.runelite.client.plugins.statusbars;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.AlternateSprites;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.ImageUtil;

class StatusBarsOverlay extends Overlay {
   private static final Color PRAYER_COLOR = new Color(50, 200, 200, 175);
   private static final Color ACTIVE_PRAYER_COLOR = new Color(57, 255, 186, 225);
   private static final Color HEALTH_COLOR = new Color(225, 35, 0, 125);
   private static final Color POISONED_COLOR = new Color(0, 145, 0, 150);
   private static final Color VENOMED_COLOR = new Color(0, 65, 0, 150);
   private static final Color HEAL_COLOR = new Color(255, 112, 6, 150);
   private static final Color PRAYER_HEAL_COLOR = new Color(57, 255, 186, 75);
   private static final Color ENERGY_HEAL_COLOR = new Color(199, 118, 0, 218);
   private static final Color RUN_STAMINA_COLOR = new Color(160, 124, 72, 255);
   private static final Color SPECIAL_ATTACK_COLOR = new Color(3, 153, 0, 195);
   private static final Color ENERGY_COLOR = new Color(199, 174, 0, 220);
   private static final Color DISEASE_COLOR = new Color(255, 193, 75, 181);
   private static final Color PARASITE_COLOR = new Color(196, 62, 109, 181);
   private static final int HEIGHT = 252;
   private static final int RESIZED_BOTTOM_HEIGHT = 272;
   private static final int RESIZED_BOTTOM_OFFSET_Y = 12;
   private static final int RESIZED_BOTTOM_OFFSET_X = 10;
   private static final int MAX_SPECIAL_ATTACK_VALUE = 100;
   private static final int MAX_RUN_ENERGY_VALUE = 100;
   private final Client client;
   private final StatusBarsPlugin plugin;
   private final StatusBarsConfig config;
   private final ItemStatChangesService itemStatService;
   private final SkillIconManager skillIconManager;
   private final SpriteManager spriteManager;
   private final Image heartDisease;
   private final Image heartPoison;
   private final Image heartVenom;
   private final Map<StatusBarsConfig.BarMode, BarRenderer> barRenderers = new EnumMap(StatusBarsConfig.BarMode.class);

   @Inject
   private StatusBarsOverlay(Client client, StatusBarsPlugin plugin, StatusBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.itemStatService = itemstatservice;
      this.skillIconManager = skillIconManager;
      this.spriteManager = spriteManager;
      this.heartDisease = ImageUtil.loadImageResource(AlternateSprites.class, "1067-DISEASE.png");
      this.heartPoison = ImageUtil.loadImageResource(AlternateSprites.class, "1067-POISON.png");
      this.heartVenom = ImageUtil.loadImageResource(AlternateSprites.class, "1067-VENOM.png");
      this.initRenderers();
   }

   private void initRenderers() {
      this.barRenderers.put(StatusBarsConfig.BarMode.HITPOINTS, new BarRenderer(() -> {
         return this.inLms() ? 99 : this.client.getRealSkillLevel(Skill.HITPOINTS);
      }, () -> {
         return this.client.getBoostedSkillLevel(Skill.HITPOINTS);
      }, () -> {
         return this.getRestoreValue(Skill.HITPOINTS.getName());
      }, () -> {
         int poisonState = this.client.getVarpValue(102);
         if (poisonState >= 1000000) {
            return VENOMED_COLOR;
         } else if (poisonState > 0) {
            return POISONED_COLOR;
         } else if (this.client.getVarpValue(456) > 0) {
            return DISEASE_COLOR;
         } else {
            return this.client.getVarbitValue(10151) >= 1 ? PARASITE_COLOR : HEALTH_COLOR;
         }
      }, () -> {
         return HEAL_COLOR;
      }, () -> {
         int poisonState = this.client.getVarpValue(102);
         if (poisonState > 0 && poisonState < 50) {
            return this.heartPoison;
         } else if (poisonState >= 1000000) {
            return this.heartVenom;
         } else {
            return (Image)(this.client.getVarpValue(456) > 0 ? this.heartDisease : this.loadSprite(1067));
         }
      }));
      this.barRenderers.put(StatusBarsConfig.BarMode.PRAYER, new BarRenderer(() -> {
         return this.inLms() ? 99 : this.client.getRealSkillLevel(Skill.PRAYER);
      }, () -> {
         return this.client.getBoostedSkillLevel(Skill.PRAYER);
      }, () -> {
         return this.getRestoreValue(Skill.PRAYER.getName());
      }, () -> {
         Color prayerColor = PRAYER_COLOR;
         Prayer[] var2 = Prayer.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Prayer pray = var2[var4];
            if (this.client.isPrayerActive(pray)) {
               prayerColor = ACTIVE_PRAYER_COLOR;
               break;
            }
         }

         return prayerColor;
      }, () -> {
         return PRAYER_HEAL_COLOR;
      }, () -> {
         return this.skillIconManager.getSkillImage(Skill.PRAYER, true);
      }));
      this.barRenderers.put(StatusBarsConfig.BarMode.RUN_ENERGY, new BarRenderer(() -> {
         return 100;
      }, () -> {
         return this.client.getEnergy() / 100;
      }, () -> {
         return this.getRestoreValue("Run Energy");
      }, () -> {
         return this.client.getVarbitValue(25) != 0 ? RUN_STAMINA_COLOR : ENERGY_COLOR;
      }, () -> {
         return ENERGY_HEAL_COLOR;
      }, () -> {
         return this.loadSprite(1069);
      }));
      this.barRenderers.put(StatusBarsConfig.BarMode.SPECIAL_ATTACK, new BarRenderer(() -> {
         return 100;
      }, () -> {
         return this.client.getVarpValue(300) / 10;
      }, () -> {
         return 0;
      }, () -> {
         return SPECIAL_ATTACK_COLOR;
      }, () -> {
         return null;
      }, () -> {
         return this.loadSprite(1610);
      }));
      this.barRenderers.put(StatusBarsConfig.BarMode.WARMTH, new BarRenderer(() -> {
         return 100;
      }, () -> {
         return this.client.getVarbitValue(11434) / 10;
      }, () -> {
         return 0;
      }, () -> {
         return new Color(244, 97, 0);
      }, () -> {
         return null;
      }, () -> {
         return this.skillIconManager.getSkillImage(Skill.FIREMAKING, true);
      }));
   }

   public Dimension render(Graphics2D g) {
      if (!this.plugin.isBarsDisplayed()) {
         return null;
      } else {
         Viewport curViewport = null;
         Widget curWidget = null;
         Viewport[] var4 = Viewport.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Viewport viewport = var4[var6];
            Widget viewportWidget = this.client.getWidget(viewport.getViewport());
            if (viewportWidget != null && !viewportWidget.isHidden()) {
               curViewport = viewport;
               curWidget = viewportWidget;
               break;
            }
         }

         if (curViewport == null) {
            return null;
         } else {
            Point offsetLeft = curViewport.getOffsetLeft();
            Point offsetRight = curViewport.getOffsetRight();
            Point location = curWidget.getCanvasLocation();
            int offsetLeftBarX;
            int offsetLeftBarY;
            int offsetRightBarX;
            int offsetRightBarY;
            int width;
            short height;
            if (curViewport == Viewport.RESIZED_BOTTOM) {
               width = this.config.barWidth();
               height = 272;
               int barWidthOffset = width - 20;
               offsetLeftBarX = location.getX() + 10 - offsetLeft.getX() - 2 * barWidthOffset;
               offsetLeftBarY = location.getY() - 12 - offsetLeft.getY();
               offsetRightBarX = location.getX() + 10 - offsetRight.getX() - barWidthOffset;
               offsetRightBarY = location.getY() - 12 - offsetRight.getY();
            } else {
               width = 20;
               height = 252;
               offsetLeftBarX = location.getX() - offsetLeft.getX();
               offsetLeftBarY = location.getY() - offsetLeft.getY();
               offsetRightBarX = location.getX() - offsetRight.getX() + curWidget.getWidth();
               offsetRightBarY = location.getY() - offsetRight.getY();
            }

            BarRenderer left = (BarRenderer)this.barRenderers.get(this.config.leftBarMode());
            BarRenderer right = (BarRenderer)this.barRenderers.get(this.config.rightBarMode());
            if (left != null) {
               left.renderBar(this.config, g, offsetLeftBarX, offsetLeftBarY, width, height);
            }

            if (right != null) {
               right.renderBar(this.config, g, offsetRightBarX, offsetRightBarY, width, height);
            }

            return null;
         }
      }
   }

   private int getRestoreValue(String skill) {
      MenuEntry[] menu = this.client.getMenuEntries();
      int menuSize = menu.length;
      if (menuSize == 0) {
         return 0;
      } else {
         MenuEntry entry = menu[menuSize - 1];
         Widget widget = entry.getWidget();
         int restoreValue = 0;
         if (widget != null && widget.getId() == 9764864) {
            Effect change = this.itemStatService.getItemStatChanges(widget.getItemId());
            if (change != null) {
               StatChange[] var8 = change.calculate(this.client).getStatChanges();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  StatChange c = var8[var10];
                  int value = c.getTheoretical();
                  if (value != 0 && c.getStat().getName().equals(skill)) {
                     restoreValue = value;
                  }
               }
            }
         }

         return restoreValue;
      }
   }

   private BufferedImage loadSprite(int spriteId) {
      return this.spriteManager.getSprite(spriteId, 0);
   }

   private boolean inLms() {
      return this.client.getWidget(21495812) != null;
   }
}
