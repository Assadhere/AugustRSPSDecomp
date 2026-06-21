package net.runelite.client.plugins.perkgrapheditor;

import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.PluginPanel;

public interface PerkGraphEditorBridge {
   default void initialize(EditorDependencies dependencies) {
   }

   default PluginPanel getEditorPanel() {
      return null;
   }

   default void cleanup() {
   }

   public static class EditorDependencies {
      public final Client client;
      public final ClientThread clientThread;
      public final ConfigManager configManager;
      public final PerkGraphEditorConfig config;

      public EditorDependencies(Client client, ClientThread clientThread, ConfigManager configManager, PerkGraphEditorConfig config) {
         this.client = client;
         this.clientThread = clientThread;
         this.configManager = configManager;
         this.config = config;
      }
   }
}
