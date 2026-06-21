package net.runelite.client;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class RuntimeConfigRefresher {
   private static final Logger log = LoggerFactory.getLogger(RuntimeConfigRefresher.class);
   @Nullable
   private final RuntimeConfig managedRuntimeConfig;
   private final RuntimeConfigLoader configLoader;
   private Instant nextRefreshAt = this.nextRefreshTime();

   @Inject
   private RuntimeConfigRefresher(@Nullable RuntimeConfig managedRuntimeConfig, RuntimeConfigLoader configLoader, EventBus eventBus) {
      this.managedRuntimeConfig = managedRuntimeConfig;
      this.configLoader = configLoader;
      eventBus.register(this);
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOGIN_SCREEN) {
         if (Instant.now().isBefore(this.nextRefreshAt)) {
            log.debug("Skipping runtimeConfig refresh, next refresh at {}", this.nextRefreshAt);
         } else if (this.managedRuntimeConfig == null) {
            log.debug("Skipping runtimeConfig refresh, current one is null");
         } else {
            this.configLoader.fetch().thenAccept(this::refreshConfig);
            this.nextRefreshAt = this.nextRefreshTime();
         }
      }
   }

   private Instant nextRefreshTime() {
      return Instant.now().plus(10L, ChronoUnit.MINUTES);
   }

   private void refreshConfig(RuntimeConfig runtimeConfig) {
      this.managedRuntimeConfig.refresh(runtimeConfig);
      log.debug("Successfully refreshed the runtimeConfig");
   }
}
