package net.runelite.client.plugins.inventorygrid;

import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Inventory Grid",
   description = "Shows a grid over the inventory and a preview of where items will be dragged",
   tags = {"items", "overlay"},
   enabledByDefault = false
)
public class InventoryGridPlugin extends Plugin {
   @Inject
   private InventoryGridOverlay overlay;
   @Inject
   private OverlayManager overlayManager;

   public void startUp() {
      this.overlayManager.add(this.overlay);
   }

   public void shutDown() {
      this.overlayManager.remove(this.overlay);
   }

   @Provides
   InventoryGridConfig getConfig(ConfigManager configManager) {
      return (InventoryGridConfig)configManager.getConfig(InventoryGridConfig.class);
   }
}
