package net.runelite.client.plugins.pyramidplunder;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.RSTimeUnit;

@PluginDescriptor(
   name = "Pyramid Plunder",
   description = "Show custom overlay for Pyramid Plunder",
   tags = {"minigame", "thieving", "pp"},
   enabledByDefault = false
)
public class PyramidPlunderPlugin extends Plugin {
   private static final Duration PYRAMID_PLUNDER_DURATION;
   private static final int PYRAMID_PLUNDER_REGION = 7749;
   static final Set<Integer> TOMB_DOOR_WALL_IDS;
   static final int TOMB_DOOR_CLOSED_ID = 20948;
   static final int SPEARTRAP_ID = 21280;
   static final Set<Integer> URN_IDS;
   static final Set<Integer> URN_CLOSED_IDS;
   static final int GRAND_GOLD_CHEST_ID = 26616;
   static final int GRAND_GOLD_CHEST_CLOSED_ID = 20946;
   static final int SARCOPHAGUS_ID = 26626;
   static final int SARCOPHAGUS_CLOSED_ID = 21255;
   @Inject
   private Client client;
   @Inject
   private PyramidPlunderConfig config;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private PyramidPlunderOverlay overlay;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private ClientThread clientThread;
   private final Map<TileObject, Tile> tilesToHighlight = new HashMap();
   private final List<GameObject> objectsToHighlight = new ArrayList();
   private PyramidPlunderTimer timer;

   @Provides
   PyramidPlunderConfig getConfig(ConfigManager configManager) {
      return (PyramidPlunderConfig)configManager.getConfig(PyramidPlunderConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      this.tilesToHighlight.clear();
      this.objectsToHighlight.clear();
      this.overlayManager.remove(this.overlay);
      this.timer = null;
      InfoBoxManager var10000 = this.infoBoxManager;
      Objects.requireNonNull(PyramidPlunderTimer.class);
      var10000.removeIf(PyramidPlunderTimer.class::isInstance);
      this.clientThread.invoke(() -> {
         Widget ppWidget = this.client.getWidget(28049410);
         if (ppWidget != null) {
            ppWidget.setHidden(false);
         }

      });
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOADING) {
         this.tilesToHighlight.clear();
         this.objectsToHighlight.clear();
      }

   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      if (this.isInPyramidPlunder()) {
         if (this.timer == null) {
            int ppTimer = this.client.getVarbitValue(2375);
            Duration remaining = PYRAMID_PLUNDER_DURATION.minus((long)ppTimer, RSTimeUnit.GAME_TICKS);
            this.timer = new PyramidPlunderTimer(remaining, this.itemManager.getImage(9044), this, this.config, this.client);
            this.infoBoxManager.addInfoBox(this.timer);
         }
      } else if (this.timer != null) {
         this.infoBoxManager.removeInfoBox(this.timer);
         this.timer = null;
      }

   }

   @Subscribe
   public void onWallObjectSpawned(WallObjectSpawned event) {
      WallObject object = event.getWallObject();
      if (TOMB_DOOR_WALL_IDS.contains(object.getId())) {
         this.tilesToHighlight.put(object, event.getTile());
      }

   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      GameObject object = event.getGameObject();
      if (21280 == object.getId()) {
         this.tilesToHighlight.put(object, event.getTile());
      } else if (URN_IDS.contains(object.getId()) || 26616 == object.getId() || 26626 == object.getId()) {
         this.objectsToHighlight.add(object);
      }

   }

   public boolean isInPyramidPlunder() {
      return this.client.getLocalPlayer() != null && 7749 == this.client.getLocalPlayer().getWorldLocation().getRegionID() && this.client.getVarbitValue(2375) > 0;
   }

   public Map<TileObject, Tile> getTilesToHighlight() {
      return this.tilesToHighlight;
   }

   public List<GameObject> getObjectsToHighlight() {
      return this.objectsToHighlight;
   }

   static {
      PYRAMID_PLUNDER_DURATION = Duration.of(501L, RSTimeUnit.GAME_TICKS);
      TOMB_DOOR_WALL_IDS = ImmutableSet.of(26618, 26619, 26620, 26621);
      URN_IDS = ImmutableSet.of(26580, 26600, 26601, 26602, 26603, 26604, new Integer[]{26605, 26606, 26607, 26608, 26609, 26610, 26611, 26612, 26613});
      URN_CLOSED_IDS = ImmutableSet.of(21261, 21262, 21263);
   }
}
