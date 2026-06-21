package net.runelite.client.plugins.interfacestyles;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.HealthBarConfig;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.SpritePixels;
import net.runelite.api.events.BeforeMenuRender;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.PostClientTick;
import net.runelite.api.events.PostHealthBarConfig;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Interface Styles",
   description = "Change the interface style to the 2005/2010 interface",
   tags = {"2005", "2010", "skin", "theme", "ui", "hp", "bar"},
   enabledByDefault = false
)
public class InterfaceStylesPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(InterfaceStylesPlugin.class);
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private InterfaceStylesConfig config;
   @Inject
   private SpriteManager spriteManager;
   private SpritePixels[] defaultCrossSprites;

   @Provides
   InterfaceStylesConfig provideConfig(ConfigManager configManager) {
      return (InterfaceStylesConfig)configManager.getConfig(InterfaceStylesConfig.class);
   }

   protected void startUp() throws Exception {
      this.queueUpdateAllOverrides();
   }

   protected void shutDown() throws Exception {
      this.clientThread.invoke(() -> {
         this.restoreWidgetDimensions();
         this.removeGameframe();
         this.restoreHealthBars();
         this.restoreCrossSprites();
      });
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged gameStateChanged) {
      if (gameStateChanged.getGameState() == GameState.STARTING) {
         this.queueUpdateAllOverrides();
      }

   }

   private void queueUpdateAllOverrides() {
      this.clientThread.invoke(() -> {
         if (this.client.getGameState().getState() < GameState.LOGIN_SCREEN.getState()) {
            return false;
         } else {
            this.updateAllOverrides();
            return true;
         }
      });
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged config) {
      if (config.getGroup().equals("interfaceStyles")) {
         this.clientThread.invoke(this::updateAllOverrides);
      }

   }

   @Subscribe(
      priority = 1.0F
   )
   public void onMenuOpened(MenuOpened event) {
      if (this.config.condensePlayerOptions()) {
         this.condensePlayerOptions();
      }

   }

   private void condensePlayerOptions() {
      MenuEntry[] menuEntries = this.client.getMenuEntries();
      MenuEntry[] newMenus = new MenuEntry[menuEntries.length];
      int newIdx = 0;
      Menu submenu = null;
      Player prev = null;
      boolean changed = false;
      MenuEntry[] var7 = menuEntries;
      int var8 = menuEntries.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         MenuEntry menuEntry = var7[var9];
         MenuAction type = menuEntry.getType();
         Player player = menuEntry.getPlayer();
         if (player != null && type != MenuAction.ITEM_USE_ON_PLAYER && type != MenuAction.WIDGET_TARGET_ON_PLAYER && type != MenuAction.WALK) {
            String option = menuEntry.getOption();
            boolean deprioritized = menuEntry.isDeprioritized();
            if (prev != player) {
               menuEntry.setOption("");
               menuEntry.setType(MenuAction.RUNELITE);
               menuEntry.setDeprioritized(false);
               submenu = menuEntry.createSubMenu();
               newMenus[newIdx++] = menuEntry;
            }

            assert submenu != null;

            submenu.createMenuEntry(-1).setIdentifier(menuEntry.getIdentifier()).setOption(option).setTarget(menuEntry.getTarget()).setType(type).setParam0(menuEntry.getParam0()).setParam1(menuEntry.getParam1()).setWorldViewId(menuEntry.getWorldViewId()).setDeprioritized(deprioritized);
            changed = true;
         } else {
            newMenus[newIdx++] = menuEntry;
         }

         prev = player;
      }

      if (changed) {
         this.client.setMenuEntries((MenuEntry[])Arrays.copyOf(newMenus, newIdx));
      }

   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent event) {
      if ("forceStackStones".equals(event.getEventName()) && this.config.alwaysStack()) {
         int[] intStack = this.client.getIntStack();
         int intStackSize = this.client.getIntStackSize();
         intStack[intStackSize - 1] = 1;
      }

   }

   @Subscribe
   public void onPostClientTick(PostClientTick event) {
      this.adjustWidgetDimensions();
   }

   @Subscribe
   public void onPostHealthBarConfig(PostHealthBarConfig postHealthBar) {
      if (this.config.hdHealthBars()) {
         HealthBarConfig healthBar = postHealthBar.getHealthBarConfig();
         HealthbarOverride override = HealthbarOverride.get(healthBar.getHealthBarFrontSpriteId());
         if (override != null) {
            healthBar.setPadding(override.getPadding());
         }

      }
   }

   private void updateAllOverrides() {
      this.removeGameframe();
      this.overrideSprites();
      this.overrideWidgetSprites();
      this.restoreWidgetDimensions();
      this.adjustWidgetDimensions();
      this.overrideHealthBars();
      this.overrideCrossSprites();
   }

   @Subscribe
   public void onBeforeMenuRender(BeforeMenuRender event) {
      if (this.config.hdMenu()) {
         this.client.draw2010Menu(this.config.menuAlpha());
         event.consume();
      } else if (this.config.menuAlpha() != 255) {
         this.client.drawOriginalMenu(this.config.menuAlpha());
         event.consume();
      }

   }

   private void overrideSprites() {
      Skin configuredSkin = this.config.skin();
      if (configuredSkin != Skin.DEFAULT) {
         SpriteOverride[] var2 = SpriteOverride.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SpriteOverride spriteOverride = var2[var4];
            Skin[] var6 = spriteOverride.getSkin();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Skin skin = var6[var8];
               if (skin == configuredSkin) {
                  String configSkin = skin.getExtendSkin() != null ? skin.getExtendSkin().toString() : skin.toString();
                  String file = configSkin + "/" + spriteOverride.getSpriteID() + ".png";
                  SpritePixels spritePixels = this.getFileSpritePixels(file);
                  if (spriteOverride.getSpriteID() == 169) {
                     this.client.setCompass(spritePixels);
                  } else {
                     this.client.getSpriteOverrides().put(spriteOverride.getSpriteID(), spritePixels);
                  }
               }
            }
         }

      }
   }

   private void restoreSprites() {
      this.client.getWidgetSpriteCache().reset();
      SpriteOverride[] var1 = SpriteOverride.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SpriteOverride spriteOverride = var1[var3];
         this.client.getSpriteOverrides().remove(spriteOverride.getSpriteID());
      }

   }

   private void overrideWidgetSprites() {
      Skin configuredSkin = this.config.skin();
      if (configuredSkin != Skin.DEFAULT) {
         WidgetOverride[] var2 = WidgetOverride.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WidgetOverride widgetOverride = var2[var4];
            if (widgetOverride.getSkin() == configuredSkin || widgetOverride.getSkin() == configuredSkin.getExtendSkin()) {
               String configSkin = configuredSkin.getExtendSkin() != null ? configuredSkin.getExtendSkin().toString() : configuredSkin.toString();
               String file = configSkin + "/widget/" + widgetOverride.getName() + ".png";
               SpritePixels spritePixels = this.getFileSpritePixels(file);
               if (spritePixels != null) {
                  int[] var9 = widgetOverride.getWidgetInfo();
                  int var10 = var9.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     int widgetInfo = var9[var11];
                     this.client.getWidgetSpriteOverrides().put(widgetInfo, spritePixels);
                  }
               }
            }
         }

      }
   }

   private void restoreWidgetSprites() {
      WidgetOverride[] var1 = WidgetOverride.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         WidgetOverride widgetOverride = var1[var3];
         int[] var5 = widgetOverride.getWidgetInfo();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int widgetInfo = var5[var7];
            this.client.getWidgetSpriteOverrides().remove(widgetInfo);
         }
      }

   }

   private SpritePixels getFileSpritePixels(String file) {
      try {
         log.debug("Loading: {}", file);
         BufferedImage image = ImageUtil.loadImageResource(this.getClass(), file);
         return ImageUtil.getImageSpritePixels(image, this.client);
      } catch (RuntimeException var3) {
         RuntimeException ex = var3;
         log.debug("Unable to load image: ", ex);
         return null;
      }
   }

   private void adjustWidgetDimensions() {
      Skin skin = this.config.skin();
      if (skin != Skin.DEFAULT) {
         WidgetOffset[] var2 = WidgetOffset.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WidgetOffset widgetOffset = var2[var4];
            if (widgetOffset.getSkin() == skin) {
               Widget widget = this.client.getWidget(widgetOffset.getComponent());
               if (widget != null) {
                  if (widgetOffset.getOffsetX() != null) {
                     widget.setRelativeX(widgetOffset.getOffsetX());
                  }

                  if (widgetOffset.getOffsetY() != null) {
                     widget.setRelativeY(widgetOffset.getOffsetY());
                  }

                  if (widgetOffset.getWidth() != null) {
                     widget.setWidth(widgetOffset.getWidth());
                  }

                  if (widgetOffset.getHeight() != null) {
                     widget.setHeight(widgetOffset.getHeight());
                  }
               }
            }
         }

      }
   }

   private void overrideHealthBars() {
      if (this.config.hdHealthBars()) {
         this.spriteManager.addSpriteOverrides(HealthbarOverride.values());
         ClientThread var10000 = this.clientThread;
         Client var10001 = this.client;
         Objects.requireNonNull(var10001);
         var10000.invokeLater(var10001::resetHealthBarCaches);
      } else {
         this.restoreHealthBars();
      }

   }

   private void restoreHealthBars() {
      this.spriteManager.removeSpriteOverrides(HealthbarOverride.values());
      ClientThread var10000 = this.clientThread;
      Client var10001 = this.client;
      Objects.requireNonNull(var10001);
      var10000.invokeLater(var10001::resetHealthBarCaches);
   }

   private void overrideCrossSprites() {
      if (this.config.rsCrossSprites()) {
         if (this.defaultCrossSprites != null) {
            return;
         }

         SpritePixels[] crossSprites = this.client.getCrossSprites();
         if (crossSprites == null) {
            return;
         }

         this.defaultCrossSprites = new SpritePixels[crossSprites.length];
         System.arraycopy(crossSprites, 0, this.defaultCrossSprites, 0, this.defaultCrossSprites.length);

         for(int i = 0; i < crossSprites.length; ++i) {
            SpritePixels newSprite = this.getFileSpritePixels("rs3/cross_sprites/" + i + ".png");
            if (newSprite != null) {
               crossSprites[i] = newSprite;
            }
         }
      } else {
         this.restoreCrossSprites();
      }

   }

   private void restoreCrossSprites() {
      if (this.defaultCrossSprites != null) {
         SpritePixels[] crossSprites = this.client.getCrossSprites();
         if (crossSprites != null && this.defaultCrossSprites.length == crossSprites.length) {
            System.arraycopy(this.defaultCrossSprites, 0, crossSprites, 0, this.defaultCrossSprites.length);
         }

         this.defaultCrossSprites = null;
      }
   }

   private void restoreWidgetDimensions() {
      WidgetOffset[] var1 = WidgetOffset.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         WidgetOffset widgetOffset = var1[var3];
         Widget widget = this.client.getWidget(widgetOffset.getComponent());
         if (widget != null) {
            widget.revalidate();
         }
      }

   }

   private void removeGameframe() {
      this.restoreSprites();
      this.restoreWidgetSprites();
      BufferedImage compassImage = this.spriteManager.getSprite(169, 0);
      if (compassImage != null) {
         SpritePixels compass = ImageUtil.getImageSpritePixels(compassImage, this.client);
         this.client.setCompass(compass);
      }

   }
}
