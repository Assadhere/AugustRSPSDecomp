package net.runelite.client.plugins.modeltools;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.overlay.OverlayManager;

public interface ModelToolsBridge {
   default void initialize(EditorDependencies dependencies) {
   }

   default PluginPanel getEditorPanel() {
      return null;
   }

   default boolean handleMousePressed(MouseEvent event) {
      return false;
   }

   default boolean handleMouseReleased(MouseEvent event) {
      return false;
   }

   default boolean handleMouseDragged(MouseEvent event) {
      return false;
   }

   default boolean handleKeyPressed(KeyEvent event) {
      return false;
   }

   default void cleanup() {
   }

   public static class EditorDependencies {
      public final Client client;
      public final ClientThread clientThread;
      public final Hooks hooks;
      public final OverlayManager overlayManager;
      public final MouseManager mouseManager;
      public final ConfigManager configManager;

      public EditorDependencies(Client client, ClientThread clientThread, Hooks hooks, OverlayManager overlayManager, MouseManager mouseManager, ConfigManager configManager) {
         this.client = client;
         this.clientThread = clientThread;
         this.hooks = hooks;
         this.overlayManager = overlayManager;
         this.mouseManager = mouseManager;
         this.configManager = configManager;
      }
   }
}
