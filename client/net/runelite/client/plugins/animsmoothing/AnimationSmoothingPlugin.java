package net.runelite.client.plugins.animsmoothing;

import java.util.function.IntPredicate;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
   name = "Animation Smoothing",
   description = "Show smoother player, NPC, and object animations",
   tags = {"npcs", "objects", "players"},
   enabledByDefault = true
)
public class AnimationSmoothingPlugin extends Plugin {
   @Inject
   private Client client;

   protected void startUp() throws Exception {
      this.update();
   }

   protected void shutDown() throws Exception {
      this.client.setAnimationInterpolationFilter((IntPredicate)null);
   }

   private void update() {
      this.client.setAnimationInterpolationFilter(AnimationSmoothingPlugin::isAnimationInterpolatable);
   }

   private static boolean isAnimationInterpolatable(int animId) {
      switch (animId) {
         case 244:
         case 367:
         case 1051:
         case 3558:
         case 4519:
         case 4652:
         case 5530:
         case 5531:
         case 5583:
         case 5857:
         case 6495:
         case 6566:
         case 6818:
         case 7898:
         case 8266:
         case 8267:
         case 8270:
         case 8271:
         case 8499:
         case 8977:
         case 9450:
         case 9493:
         case 11291:
            return false;
         default:
            return true;
      }
   }
}
