package net.runelite.client.plugins.minimap;

import com.google.inject.Provides;
import java.awt.Color;
import java.lang.reflect.Type;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.SpritePixels;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;

@PluginDescriptor(
   name = "Minimap",
   description = "Customize the color of minimap dots, hide the minimap, and zoom",
   tags = {"items", "npcs", "players", "zoom"}
)
public class MinimapPlugin extends Plugin {
   private static final int DOT_ITEM = 0;
   private static final int DOT_NPC = 1;
   private static final int DOT_PLAYER = 2;
   private static final int DOT_FRIEND = 3;
   private static final int DOT_TEAM = 4;
   private static final int DOT_FRIENDSCHAT = 5;
   private static final int DOT_CLAN = 6;
   @Inject
   private Client client;
   @Inject
   private MinimapConfig config;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ConfigManager configManager;
   private SpritePixels[] originalDotSprites;

   @Provides
   private MinimapConfig provideConfig(ConfigManager configManager) {
      return (MinimapConfig)configManager.getConfig(MinimapConfig.class);
   }

   protected void startUp() {
      this.clientThread.invokeLater(() -> {
         this.updateMinimapWidgetVisibility(this.config.hideMinimap());
      });
      this.storeOriginalDots();
      this.replaceMapDots();
      this.client.setMinimapZoom(this.config.zoom());
      Double zoomLevel = (Double)this.configManager.getConfiguration("minimap", "zoomLevel", (Type)Double.TYPE);
      if (zoomLevel != null && zoomLevel > 0.0) {
         this.client.setMinimapZoom(zoomLevel);
      }

   }

   protected void shutDown() {
      this.clientThread.invokeLater(() -> {
         this.updateMinimapWidgetVisibility(false);
      });
      this.restoreOriginalDots();
      this.client.setMinimapZoom(false);
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      GameState state = event.getGameState();
      if (state == GameState.STARTING) {
         this.originalDotSprites = null;
      } else if (state == GameState.LOGIN_SCREEN && this.originalDotSprites == null) {
         this.storeOriginalDots();
         this.replaceMapDots();
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("minimap")) {
         if (event.getKey().equals("hideMinimap")) {
            this.clientThread.invokeLater(() -> {
               this.updateMinimapWidgetVisibility(this.config.hideMinimap());
            });
         } else if (event.getKey().equals("zoom")) {
            this.client.setMinimapZoom(this.config.zoom());
         } else {
            this.restoreOriginalDots();
            this.replaceMapDots();
         }
      }
   }

   @Schedule(
      period = 11L,
      unit = ChronoUnit.SECONDS,
      asynchronous = true
   )
   public void saveZoom() {
      double zoom = this.client.getMinimapZoom();
      this.configManager.setConfiguration("minimap", "zoomLevel", (Object)zoom);
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired scriptPostFired) {
      if (scriptPostFired.getScriptId() == 907 || scriptPostFired.getScriptId() == 920) {
         this.updateMinimapWidgetVisibility(this.config.hideMinimap());
      }

   }

   private void updateMinimapWidgetVisibility(boolean hide) {
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         boolean vanillaHideMinimap = this.client.getVarbitValue(12986) == 1;
         this.setHidden(10551318, hide || vanillaHideMinimap);
         this.setHidden(10551329, hide);
         this.setHidden(10747926, hide || vanillaHideMinimap);
         this.setHidden(10747937, hide);
      }
   }

   private void setHidden(int widget, boolean hide) {
      Widget w = this.client.getWidget(widget);
      if (w != null) {
         w.setHidden(hide);
      }

   }

   private void replaceMapDots() {
      SpritePixels[] mapDots = this.client.getMapDots();
      if (mapDots != null) {
         this.applyDot(mapDots, 0, this.config.itemColor());
         this.applyDot(mapDots, 1, this.config.npcColor());
         this.applyDot(mapDots, 2, this.config.playerColor());
         this.applyDot(mapDots, 3, this.config.friendColor());
         this.applyDot(mapDots, 4, this.config.teamColor());
         this.applyDot(mapDots, 5, this.config.friendsChatColor());
         this.applyDot(mapDots, 6, this.config.clanChatColor());
      }
   }

   private void applyDot(SpritePixels[] mapDots, int id, Color color) {
      if (id < mapDots.length && color != null) {
         mapDots[id] = MinimapDot.create(this.client, color);
      }

   }

   private void storeOriginalDots() {
      SpritePixels[] originalDots = this.client.getMapDots();
      if (originalDots != null) {
         this.originalDotSprites = (SpritePixels[])Arrays.copyOf(originalDots, originalDots.length);
      }
   }

   private void restoreOriginalDots() {
      SpritePixels[] mapDots = this.client.getMapDots();
      if (this.originalDotSprites != null && mapDots != null) {
         System.arraycopy(this.originalDotSprites, 0, mapDots, 0, mapDots.length);
      }
   }
}
