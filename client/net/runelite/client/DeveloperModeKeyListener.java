package net.runelite.client;

import java.awt.event.KeyEvent;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DeveloperModeKeyListener implements KeyListener {
   private static final Logger log = LoggerFactory.getLogger(DeveloperModeKeyListener.class);
   private static final int REQUIRED_PRESSES = 3;
   private static final long TIMEOUT_MS = 3000L;
   private final PluginManager pluginManager;
   private final ScheduledExecutorService executorService;
   private int pressCount;
   private long lastPressTime;

   @Inject
   private DeveloperModeKeyListener(PluginManager pluginManager, KeyManager keyManager, ScheduledExecutorService executorService) {
      this.pluginManager = pluginManager;
      this.executorService = executorService;
      if (RuneLiteProperties.isBeta()) {
         keyManager.registerKeyListener(this);
      }

   }

   public void keyPressed(KeyEvent e) {
      if (!this.pluginManager.isDeveloperMode()) {
         switch (e.getKeyCode()) {
            case 16:
            case 17:
            case 18:
            case 157:
               return;
            default:
               if (e.getKeyCode() == 68 && e.isControlDown() && e.isShiftDown() && !e.isAltDown()) {
                  long now = System.currentTimeMillis();
                  if (now - this.lastPressTime > 3000L) {
                     this.pressCount = 0;
                  }

                  ++this.pressCount;
                  this.lastPressTime = now;
                  if (this.pressCount >= 3) {
                     this.pressCount = 0;
                     e.consume();
                     this.activateDeveloperMode();
                  }
               } else {
                  this.pressCount = 0;
               }

         }
      }
   }

   public void keyReleased(KeyEvent e) {
   }

   public void keyTyped(KeyEvent e) {
   }

   public boolean isEnabledOnLoginScreen() {
      return true;
   }

   public void focusLost() {
      this.pressCount = 0;
   }

   private void activateDeveloperMode() {
      this.executorService.execute(() -> {
         try {
            this.pluginManager.enableDeveloperMode();
            log.info("Developer plugins loaded");
         } catch (Exception var2) {
            Exception e = var2;
            log.error("Failed to enable developer mode", e);
         }

      });
   }
}
