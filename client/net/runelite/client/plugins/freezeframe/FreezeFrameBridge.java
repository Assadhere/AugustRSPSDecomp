package net.runelite.client.plugins.freezeframe;

import java.util.concurrent.ScheduledExecutorService;
import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.plugins.particles.ParticlePlugin;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageCapture;

public interface FreezeFrameBridge {
   void initialize(Deps var1);

   void cleanup();

   void onBeforeRender();

   void onClientTick();

   void onConfigChanged(ConfigChanged var1);

   void onPluginChanged(PluginChanged var1);

   void toggleFreeze();

   void capture();

   public static class Deps {
      public final Client client;
      public final ClientThread clientThread;
      public final DrawManager drawManager;
      public final ConfigManager configManager;
      public final FreezeFrameConfig config;
      public final ClientUI clientUi;
      public final Notifier notifier;
      public final ScheduledExecutorService executor;
      public final ParticlePlugin particlePlugin;
      public final ImageCapture imageCapture;
      public final OverlayManager overlayManager;

      public Deps(Client client, ClientThread clientThread, DrawManager drawManager, ConfigManager configManager, FreezeFrameConfig config, ClientUI clientUi, Notifier notifier, ScheduledExecutorService executor, ParticlePlugin particlePlugin, ImageCapture imageCapture, OverlayManager overlayManager) {
         this.client = client;
         this.clientThread = clientThread;
         this.drawManager = drawManager;
         this.configManager = configManager;
         this.config = config;
         this.clientUi = clientUi;
         this.notifier = notifier;
         this.executor = executor;
         this.particlePlugin = particlePlugin;
         this.imageCapture = imageCapture;
         this.overlayManager = overlayManager;
      }
   }
}
