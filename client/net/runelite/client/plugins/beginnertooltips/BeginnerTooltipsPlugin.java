package net.runelite.client.plugins.beginnertooltips;

import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Beginner Tooltips",
   description = "Show extra info tooltips for new players when hovering game objects",
   tags = {"beginner", "tooltips", "overlay", "objects", "help"}
)
public class BeginnerTooltipsPlugin extends Plugin {
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private BeginnerTooltipsOverlay overlay;

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
   }
}
