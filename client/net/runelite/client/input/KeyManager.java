package net.runelite.client.input;

import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.FocusChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class KeyManager {
   private static final Logger log = LoggerFactory.getLogger(KeyManager.class);
   private final Client client;
   private final List<KeyListener> keyListeners = new CopyOnWriteArrayList();

   @Inject
   private KeyManager(@Nullable Client client, EventBus eventBus) {
      this.client = client;
      eventBus.register(this);
   }

   public void registerKeyListener(KeyListener keyListener) {
      if (!this.keyListeners.contains(keyListener)) {
         log.debug("Registering key listener: {}", keyListener);
         this.keyListeners.add(keyListener);
      }

   }

   public void unregisterKeyListener(KeyListener keyListener) {
      boolean unregistered = this.keyListeners.remove(keyListener);
      if (unregistered) {
         log.debug("Unregistered key listener: {}", keyListener);
      }

   }

   public void processKeyPressed(KeyEvent keyEvent) {
      if (!keyEvent.isConsumed()) {
         Iterator var2 = this.keyListeners.iterator();

         while(var2.hasNext()) {
            KeyListener keyListener = (KeyListener)var2.next();
            if (this.shouldProcess(keyListener)) {
               log.trace("Processing key pressed {} for key listener {}", keyEvent.paramString(), keyListener);
               keyListener.keyPressed(keyEvent);
               if (keyEvent.isConsumed()) {
                  log.debug("Consuming key pressed {} for key listener {}", keyEvent.paramString(), keyListener);
                  break;
               }
            }
         }

      }
   }

   public void processKeyReleased(KeyEvent keyEvent) {
      if (!keyEvent.isConsumed()) {
         Iterator var2 = this.keyListeners.iterator();

         while(var2.hasNext()) {
            KeyListener keyListener = (KeyListener)var2.next();
            if (this.shouldProcess(keyListener)) {
               log.trace("Processing key released {} for key listener {}", keyEvent.paramString(), keyListener);
               keyListener.keyReleased(keyEvent);
               if (keyEvent.isConsumed()) {
                  log.debug("Consuming key released {} for listener {}", keyEvent.paramString(), keyListener);
                  break;
               }
            }
         }

      }
   }

   public void processKeyTyped(KeyEvent keyEvent) {
      if (!keyEvent.isConsumed()) {
         Iterator var2 = this.keyListeners.iterator();

         while(var2.hasNext()) {
            KeyListener keyListener = (KeyListener)var2.next();
            if (this.shouldProcess(keyListener)) {
               log.trace("Processing key typed {} for key listener {}", keyEvent.paramString(), keyListener);
               keyListener.keyTyped(keyEvent);
               if (keyEvent.isConsumed()) {
                  log.debug("Consuming key typed {} for key listener {}", keyEvent.paramString(), keyListener);
                  break;
               }
            }
         }

      }
   }

   private boolean shouldProcess(KeyListener keyListener) {
      if (this.client == null) {
         return true;
      } else {
         GameState gameState = this.client.getGameState();
         return gameState != GameState.LOGIN_SCREEN && gameState != GameState.LOGIN_SCREEN_AUTHENTICATOR ? true : keyListener.isEnabledOnLoginScreen();
      }
   }

   @Subscribe
   private void onFocusChanged(FocusChanged event) {
      if (!event.isFocused()) {
         Iterator var2 = this.keyListeners.iterator();

         while(var2.hasNext()) {
            KeyListener keyListener = (KeyListener)var2.next();
            keyListener.focusLost();
         }
      }

   }
}
