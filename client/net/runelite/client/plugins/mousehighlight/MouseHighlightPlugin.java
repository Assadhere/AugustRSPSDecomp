package net.runelite.client.plugins.mousehighlight;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Mouse Tooltips",
   description = "Render default actions as a tooltip",
   tags = {"actions", "overlay"}
)
public class MouseHighlightPlugin extends Plugin {
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private MouseHighlightOverlay overlay;

   @Provides
   MouseHighlightConfig provideConfig(ConfigManager configManager) {
      return (MouseHighlightConfig)configManager.getConfig(MouseHighlightConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
   }
}
