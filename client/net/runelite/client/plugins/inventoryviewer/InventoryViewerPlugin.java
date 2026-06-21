package net.runelite.client.plugins.inventoryviewer;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
   name = "Inventory Viewer",
   description = "Add an overlay showing the contents of your inventory",
   tags = {"alternate", "items", "overlay", "second"},
   enabledByDefault = false
)
public class InventoryViewerPlugin extends Plugin {
   @Inject
   private InventoryViewerConfig config;
   @Inject
   private InventoryViewerOverlay overlay;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private KeyManager keyManager;
   private final HotkeyListener hotkeyListener = new HotkeyListener(() -> {
      return this.config.toggleKeybind();
   }) {
      public void hotkeyPressed() {
         InventoryViewerPlugin.this.overlay.toggle();
      }
   };

   @Provides
   InventoryViewerConfig getConfig(ConfigManager configManager) {
      return (InventoryViewerConfig)configManager.getConfig(InventoryViewerConfig.class);
   }

   public void startUp() {
      this.overlayManager.add(this.overlay);
      this.keyManager.registerKeyListener(this.hotkeyListener);
   }

   public void shutDown() {
      this.overlayManager.remove(this.overlay);
      this.keyManager.unregisterKeyListener(this.hotkeyListener);
   }
}
