package net.runelite.client.plugins.blastfurnace;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.Text;

@PluginDescriptor(
   name = "Blast Furnace",
   description = "Show helpful information for the Blast Furnace minigame",
   tags = {"minigame", "overlay", "skilling", "smithing"}
)
public class BlastFurnacePlugin extends Plugin {
   private static final int BAR_DISPENSER = 9092;
   private static final String FOREMAN_PERMISSION_TEXT = "Okay, you can use the furnace for ten minutes. Remember, you only need half as much coal as with a regular furnace.";
   private GameObject conveyorBelt;
   private GameObject barDispenser;
   private ForemanTimer foremanTimer;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private BlastFurnaceOverlay overlay;
   @Inject
   private BlastFurnaceCofferOverlay cofferOverlay;
   @Inject
   private BlastFurnaceClickBoxOverlay clickBoxOverlay;
   @Inject
   private Client client;
   @Inject
   private ItemManager itemManager;
   @Inject
   private InfoBoxManager infoBoxManager;

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
      this.overlayManager.add(this.cofferOverlay);
      this.overlayManager.add(this.clickBoxOverlay);
   }

   protected void shutDown() {
      InfoBoxManager var10000 = this.infoBoxManager;
      Objects.requireNonNull(ForemanTimer.class);
      var10000.removeIf(ForemanTimer.class::isInstance);
      this.overlayManager.remove(this.overlay);
      this.overlayManager.remove(this.cofferOverlay);
      this.overlayManager.remove(this.clickBoxOverlay);
      this.conveyorBelt = null;
      this.barDispenser = null;
      this.foremanTimer = null;
   }

   @Provides
   BlastFurnaceConfig provideConfig(ConfigManager configManager) {
      return (BlastFurnaceConfig)configManager.getConfig(BlastFurnaceConfig.class);
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      GameObject gameObject = event.getGameObject();
      switch (gameObject.getId()) {
         case 9092:
            this.barDispenser = gameObject;
            break;
         case 9100:
            this.conveyorBelt = gameObject;
      }

   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      GameObject gameObject = event.getGameObject();
      switch (gameObject.getId()) {
         case 9092:
            this.barDispenser = null;
            break;
         case 9100:
            this.conveyorBelt = null;
      }

   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOADING) {
         this.conveyorBelt = null;
         this.barDispenser = null;
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      Widget npcDialog = this.client.getWidget(15138822);
      if (npcDialog != null) {
         boolean shouldCheckForemanFee = this.client.getRealSkillLevel(Skill.SMITHING) < 60 && (this.foremanTimer == null || Duration.between(Instant.now(), this.foremanTimer.getEndTime()).toMinutes() <= 5L);
         if (shouldCheckForemanFee) {
            String npcText = Text.sanitizeMultilineText(npcDialog.getText());
            if (npcText.equals("Okay, you can use the furnace for ten minutes. Remember, you only need half as much coal as with a regular furnace.")) {
               InfoBoxManager var10000 = this.infoBoxManager;
               Objects.requireNonNull(ForemanTimer.class);
               var10000.removeIf(ForemanTimer.class::isInstance);
               this.foremanTimer = new ForemanTimer(this, this.itemManager);
               this.infoBoxManager.addInfoBox(this.foremanTimer);
            }
         }

      }
   }

   GameObject getConveyorBelt() {
      return this.conveyorBelt;
   }

   GameObject getBarDispenser() {
      return this.barDispenser;
   }
}
