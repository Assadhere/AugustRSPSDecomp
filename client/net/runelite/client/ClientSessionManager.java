package net.runelite.client;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
import net.runelite.client.util.RunnableExceptionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ClientSessionManager {
   private static final Logger log = LoggerFactory.getLogger(ClientSessionManager.class);
   private final ScheduledExecutorService executorService;
   private final Client client;
   private final SessionClient sessionClient;
   private static final UUID sessionId = UUID.randomUUID();
   private ScheduledFuture<?> scheduledFuture;

   @Inject
   ClientSessionManager(ScheduledExecutorService executorService, Client client, SessionClient sessionClient) {
      this.executorService = executorService;
      this.client = client;
      this.sessionClient = sessionClient;
   }

   public void start() {
      this.scheduledFuture = this.executorService.scheduleWithFixedDelay(RunnableExceptionLogger.wrap(this::ping), (long)((int)(300.0 * Math.random())), 600L, TimeUnit.SECONDS);
   }

   @Subscribe
   private void onClientShutdown(ClientShutdown e) {
      this.scheduledFuture.cancel(true);
   }

   private void ping() {
      if (this.isWorldHostValid()) {
         GameState gameState = this.client.getGameState();
         boolean loggedIn = gameState.getState() >= GameState.LOADING.getState();

         try {
            this.sessionClient.ping(sessionId, loggedIn);
         } catch (IOException var4) {
            IOException ex = var4;
            log.warn("Unable to ping session service", ex);
         }

      }
   }

   private boolean isWorldHostValid() {
      String host = this.client.getWorldHost();
      return host != null && host.endsWith(".runescape.com");
   }
}
