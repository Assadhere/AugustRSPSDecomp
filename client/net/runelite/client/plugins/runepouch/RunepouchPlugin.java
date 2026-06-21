package net.runelite.client.plugins.runepouch;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Rune Pouch",
   description = "Show the contents of your rune pouch",
   tags = {"combat", "magic", "overlay"}
)
public class RunepouchPlugin extends Plugin {
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private RunepouchOverlay overlay;

   @Provides
   RunepouchConfig getConfig(ConfigManager configManager) {
      return (RunepouchConfig)configManager.getConfig(RunepouchConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
   }
}
