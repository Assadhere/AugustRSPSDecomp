package net.runelite.client.plugins.tithefarm;

import com.google.inject.Provides;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Tithe Farm",
   description = "Show timers for the farming patches within the Tithe Farm minigame",
   tags = {"farming", "minigame", "overlay", "skilling", "timers"}
)
public class TitheFarmPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(TitheFarmPlugin.class);
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private TitheFarmPlantOverlay titheFarmOverlay;
   private final Set<TitheFarmPlant> plants = new HashSet();

   @Provides
   TitheFarmPluginConfig getConfig(ConfigManager configManager) {
      return (TitheFarmPluginConfig)configManager.getConfig(TitheFarmPluginConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.titheFarmOverlay);
      this.titheFarmOverlay.updateConfig();
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.titheFarmOverlay);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("tithefarmplugin")) {
         this.titheFarmOverlay.updateConfig();
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      this.plants.removeIf((plant) -> {
         return plant.getPlantTimeRelative() == 1.0;
      });
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      GameObject gameObject = event.getGameObject();
      TitheFarmPlantType type = TitheFarmPlantType.getPlantType(gameObject.getId());
      if (type != null) {
         TitheFarmPlantState state = TitheFarmPlantState.getState(gameObject.getId());
         TitheFarmPlant newPlant = new TitheFarmPlant(state, type, gameObject);
         TitheFarmPlant oldPlant = this.getPlantFromCollection(gameObject);
         if (oldPlant == null && newPlant.getType() != TitheFarmPlantType.EMPTY) {
            log.debug("Added plant {}", newPlant);
            this.plants.add(newPlant);
         } else {
            if (oldPlant == null) {
               return;
            }

            if (newPlant.getType() == TitheFarmPlantType.EMPTY) {
               log.debug("Removed plant {}", oldPlant);
               this.plants.remove(oldPlant);
            } else if (oldPlant.getGameObject().getId() != newPlant.getGameObject().getId()) {
               if (oldPlant.getState() != TitheFarmPlantState.WATERED && newPlant.getState() == TitheFarmPlantState.WATERED) {
                  log.debug("Updated plant (watered)");
                  newPlant.setPlanted(oldPlant.getPlanted());
                  this.plants.remove(oldPlant);
                  this.plants.add(newPlant);
               } else {
                  log.debug("Updated plant");
                  this.plants.remove(oldPlant);
                  this.plants.add(newPlant);
               }
            }
         }

      }
   }

   private TitheFarmPlant getPlantFromCollection(GameObject gameObject) {
      WorldPoint gameObjectLocation = gameObject.getWorldLocation();
      Iterator var3 = this.plants.iterator();

      TitheFarmPlant plant;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         plant = (TitheFarmPlant)var3.next();
      } while(!gameObjectLocation.equals(plant.getWorldLocation()));

      return plant;
   }

   public Set<TitheFarmPlant> getPlants() {
      return this.plants;
   }
}
