package net.runelite.client.plugins.particles;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
import net.runelite.client.plugins.particles.config.ParticleEffectorConfig;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.core.ParticleEffector;
import net.runelite.client.plugins.particles.core.ParticleSystem;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.overlay.OverlayManager;

public interface ParticleEditorBridge {
   boolean isPlacementMode();

   boolean isPlacingEmitter();

   ParticleEmitterConfig getEditingEmitterConfig();

   ParticleEffectorConfig getEditingEffectorConfig();

   boolean handlePlacementClick(MouseEvent var1);

   boolean handlePlacementMouseMoved(MouseEvent var1);

   boolean isModelEditorActive();

   boolean handleModelEditorMousePressed(MouseEvent var1);

   boolean handleModelEditorMouseClick(MouseEvent var1);

   boolean handleModelEditorMouseReleased(MouseEvent var1);

   boolean handleModelEditorMouseDragged(MouseEvent var1);

   void handleModelEditorMouseMoved(MouseEvent var1);

   boolean handleModelEditorKeyPressed(KeyEvent var1);

   void syncModelEditorModeButtons();

   ParticleEffector findEffectorAtScreen(int var1, int var2);

   void inspectEffectorConfig(ParticleEffectorConfig var1);

   void onConfigsReloaded();

   void updatePlacementButtonState();

   void clearModelViewer();

   void repositionEditorObjects(int var1, int var2);

   void revalidateEditorObjects();

   void resetAll();

   void initialize(EditorDependencies var1);

   void cleanup();

   PluginPanel getEditorPanel();

   public static class EditorDependencies {
      public final Client client;
      public final ClientThread clientThread;
      public final Hooks hooks;
      public final ParticleSystem particleSystem;
      public final OverlayManager overlayManager;
      public final ParticlePlugin plugin;

      public EditorDependencies(Client client, ClientThread clientThread, Hooks hooks, ParticleSystem particleSystem, OverlayManager overlayManager, ParticlePlugin plugin) {
         this.client = client;
         this.clientThread = clientThread;
         this.hooks = hooks;
         this.particleSystem = particleSystem;
         this.overlayManager = overlayManager;
         this.plugin = plugin;
      }
   }
}
