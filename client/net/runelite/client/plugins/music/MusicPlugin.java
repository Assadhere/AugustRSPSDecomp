package net.runelite.client.plugins.music;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import joptsimple.internal.Strings;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.AmbientSoundEffectCreated;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.api.events.VarClientIntChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.game.chatbox.ChatboxTextInput;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
   name = "Music",
   description = "Adds search and filter for the music list, and additional volume control",
   tags = {"sound", "volume"}
)
public class MusicPlugin extends Plugin {
   private static final Set<Integer> SOURCELESS_PLAYER_SOUNDS = ImmutableSet.of(200);
   private static final Set<Integer> PRAYER_SOUNDS = ImmutableSet.of(2690, 2688, 2664, 2685, 2670, 2684, new Integer[]{2689, 2662, 2679, 2678, 1982, 2666, 2668, 2687, 2691, 2667, 2675, 2677, 2676, 2665, 10194, 2669, 10100, 2682, 2680, 2686, 3826, 3825, 2663});
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private MusicConfig musicConfig;
   @Inject
   private ChatboxPanelManager chatboxPanelManager;
   private ChatboxTextInput searchInput;
   private Widget musicSearchButton;
   private Widget musicFilterButton;
   private String search = "";
   @Nullable
   private MusicState currentMusicFilter = null;

   protected void startUp() {
      this.clientThread.invoke(() -> {
         this.addMusicButtons();
         if (this.client.getGameState() == GameState.LOGGED_IN && this.musicConfig.muteAmbientSounds()) {
            this.client.setGameState(GameState.LOADING);
         }

      });
   }

   protected void shutDown() {
      Widget header = this.client.getWidget(15663106);
      if (header != null) {
         header.deleteAllChildren();
      }

      this.currentMusicFilter = null;
      this.search = "";
      this.clientThread.invoke(() -> {
         if (this.musicConfig.muteAmbientSounds() && this.client.getGameState() == GameState.LOGGED_IN) {
            this.client.setGameState(GameState.LOADING);
         }

      });
   }

   @Provides
   MusicConfig getConfig(ConfigManager configManager) {
      return (MusicConfig)configManager.getConfig(MusicConfig.class);
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged gameStateChanged) {
      GameState gameState = gameStateChanged.getGameState();
      if (gameState == GameState.LOGIN_SCREEN) {
         this.currentMusicFilter = null;
      } else if (gameState == GameState.LOGGED_IN && this.musicConfig.muteAmbientSounds()) {
         this.client.getAmbientSoundEffects().clear();
      }

   }

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
      if (widgetLoaded.getGroupId() == 239) {
         this.currentMusicFilter = null;
         this.addMusicButtons();
      }

   }

   private void addMusicButtons() {
      Widget header = this.client.getWidget(15663106);
      if (header != null) {
         header.deleteAllChildren();
         this.musicSearchButton = header.createChild(-1, 5).setSpriteId(1113).setSize(18, 17).setPos(5, 6, 2, 2).setName("Search").setHasListener(true);
         this.musicSearchButton.setAction(1, "Open");
         this.musicSearchButton.setOnOpListener(new Object[]{(e) -> {
            this.openSearch();
         }});
         this.musicSearchButton.revalidate();
         this.musicFilterButton = header.createChild(-1, 5).setSpriteId(1063).setSize(15, 15).setPos(25, 6, 2, 2).setName("All").setHasListener(true);
         this.musicFilterButton.setAction(1, "Toggle");
         this.musicFilterButton.setOnOpListener(new Object[]{(e) -> {
            this.toggleStatus();
         }});
         this.musicFilterButton.revalidate();
         this.updateFilterButton();
      }
   }

   @Subscribe
   public void onVarClientIntChanged(VarClientIntChanged varClientIntChanged) {
      if (this.isChatboxOpen() && !this.isOnMusicTab()) {
         this.chatboxPanelManager.close();
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged configChanged) {
      if (configChanged.getGroup().equals("music")) {
         this.clientThread.invoke(() -> {
            if ("muteAmbientSounds".equals(configChanged.getKey()) && this.client.getGameState() == GameState.LOGGED_IN) {
               this.client.setGameState(GameState.LOADING);
            }

         });
      }

   }

   @Subscribe
   private void onVarbitChanged(VarbitChanged ev) {
      if (ev.getVarbitId() == 19733) {
         this.currentMusicFilter = null;
         this.updateFilterButton();
      }

   }

   @Subscribe
   private void onScriptCallbackEvent(ScriptCallbackEvent ev) {
      if ("musicTrackFilter".equals(ev.getEventName())) {
         int override = -1;
         if (this.currentMusicFilter != null) {
            boolean unlocked = this.client.getIntStack()[this.client.getIntStackSize() - 1] != 0;
            switch (this.currentMusicFilter) {
               case ALL:
                  override = 1;
                  break;
               case LOCKED:
                  override = unlocked ? 0 : 1;
                  break;
               case UNLOCKED:
                  override = unlocked ? 1 : 0;
            }
         }

         if (!Strings.isNullOrEmpty(this.search)) {
            int dbrow = this.client.getIntStack()[this.client.getIntStackSize() - 2];
            Object[] displayName = this.client.getDBTableField(dbrow, 1, 0);
            if (displayName.length > 0 && !((String)displayName[0]).toLowerCase().contains(this.search)) {
               override = 0;
            }
         }

         this.client.getIntStack()[this.client.getIntStackSize() - 3] = override;
      }

   }

   private boolean isOnMusicTab() {
      return this.client.getVarcIntValue(171) == 13;
   }

   private boolean isChatboxOpen() {
      return this.searchInput != null && this.chatboxPanelManager.getCurrentInput() == this.searchInput;
   }

   private void toggleStatus() {
      MusicState[] states = MusicPlugin.MusicState.values();
      if (this.currentMusicFilter == null) {
         int state = this.client.getVarbitValue(19733);
         this.currentMusicFilter = states[state];
      }

      this.currentMusicFilter = states[(this.currentMusicFilter.ordinal() + 1) % states.length];
      this.updateFilterButton();
      this.client.playSoundEffect(2266);
      this.redrawList();
   }

   private void updateFilterButton() {
      MusicState filter = this.currentMusicFilter;
      if (filter == null) {
         int state = this.client.getVarbitValue(19733);
         filter = MusicPlugin.MusicState.values()[state];
      }

      this.musicFilterButton.setSpriteId(filter.getSpriteID());
      this.musicFilterButton.setName(filter.getName());
   }

   private void redrawList() {
      Widget transmitListenerWidget = this.client.getWidget(15663112);
      if (transmitListenerWidget != null && transmitListenerWidget.getOnVarTransmitListener() != null) {
         this.client.runScript(transmitListenerWidget.getOnVarTransmitListener());
      }

   }

   private void openSearch() {
      this.updateFilter("");
      this.client.playSoundEffect(2266);
      this.musicSearchButton.setAction(1, "Close");
      this.musicSearchButton.setOnOpListener(new Object[]{(e) -> {
         this.closeSearch();
      }});
      this.searchInput = this.chatboxPanelManager.openTextInput("Search music list").onChanged((s) -> {
         this.clientThread.invokeLater(() -> {
            this.updateFilter(s.trim());
         });
      }).onDone((s) -> {
         return false;
      }).onClose(() -> {
         this.clientThread.invokeLater(() -> {
            this.updateFilter("");
         });
         this.musicSearchButton.setOnOpListener(new Object[]{(e) -> {
            this.openSearch();
         }});
         this.musicSearchButton.setAction(1, "Open");
      }).build();
   }

   private void closeSearch() {
      this.updateFilter("");
      this.chatboxPanelManager.close();
      this.client.playSoundEffect(2266);
   }

   private void updateFilter(String input) {
      this.search = input.toLowerCase();
      this.redrawList();
   }

   @Subscribe
   public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed areaSoundEffectPlayed) {
      Actor source = areaSoundEffectPlayed.getSource();
      int soundId = areaSoundEffectPlayed.getSoundId();
      if (source == this.client.getLocalPlayer() && this.musicConfig.muteOwnAreaSounds()) {
         areaSoundEffectPlayed.consume();
      } else if (source != this.client.getLocalPlayer() && (source instanceof Player || source == null && SOURCELESS_PLAYER_SOUNDS.contains(soundId)) && this.musicConfig.muteOtherAreaSounds()) {
         areaSoundEffectPlayed.consume();
      } else if (source instanceof NPC && this.musicConfig.muteNpcAreaSounds()) {
         areaSoundEffectPlayed.consume();
      } else if (source == null && !SOURCELESS_PLAYER_SOUNDS.contains(soundId) && this.musicConfig.muteEnvironmentAreaSounds()) {
         areaSoundEffectPlayed.consume();
      }

   }

   @Subscribe
   public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed) {
      if (this.musicConfig.mutePrayerSounds() && PRAYER_SOUNDS.contains(soundEffectPlayed.getSoundId())) {
         soundEffectPlayed.consume();
      }

   }

   @Subscribe
   public void onAmbientSoundEffectCreated(AmbientSoundEffectCreated ev) {
      if (this.musicConfig.muteAmbientSounds()) {
         this.client.getAmbientSoundEffects().clear();
      }

   }

   private static enum MusicState {
      ALL("All", 1063),
      UNLOCKED("Unlocked", 1061),
      LOCKED("Locked", 1060);

      private final String name;
      private final int spriteID;

      private MusicState(String name, int spriteID) {
         this.name = name;
         this.spriteID = spriteID;
      }

      public String getName() {
         return this.name;
      }

      public int getSpriteID() {
         return this.spriteID;
      }
   }
}
