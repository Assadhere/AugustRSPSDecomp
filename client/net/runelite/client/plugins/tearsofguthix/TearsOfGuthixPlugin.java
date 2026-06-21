package net.runelite.client.plugins.tearsofguthix;

import com.google.inject.Provides;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Tears Of Guthix",
   description = "Show timers for the Tears Of Guthix streams",
   tags = {"minigame", "overlay", "skilling", "timers", "tog"}
)
public class TearsOfGuthixPlugin extends Plugin {
   private static final int TOG_REGION = 12948;
   @Inject
   private Client client;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private TearsOfGuthixOverlay overlay;
   private final Map<DecorativeObject, Instant> streams = new HashMap();

   @Provides
   TearsOfGuthixConfig provideConfig(ConfigManager configManager) {
      return (TearsOfGuthixConfig)configManager.getConfig(TearsOfGuthixConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() {
      this.overlayManager.remove(this.overlay);
      this.streams.clear();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case LOADING:
         case LOGIN_SCREEN:
         case HOPPING:
            this.streams.clear();
         default:
      }
   }

   @Subscribe
   public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
      DecorativeObject object = event.getDecorativeObject();
      if ((object.getId() == 6661 || object.getId() == 6665 || object.getId() == 6662 || object.getId() == 6666) && this.client.getLocalPlayer().getWorldLocation().getRegionID() == 12948) {
         this.streams.put(event.getDecorativeObject(), Instant.now());
      }

   }

   @Subscribe
   public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
      if (!this.streams.isEmpty()) {
         DecorativeObject object = event.getDecorativeObject();
         this.streams.remove(object);
      }
   }

   public Map<DecorativeObject, Instant> getStreams() {
      return this.streams;
   }
}
