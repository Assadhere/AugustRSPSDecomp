package net.runelite.client.plugins.lowmemory;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("lowmemory")
public interface LowMemoryConfig extends Config {
   String GROUP = "lowmemory";

   @ConfigItem(
      keyName = "lowDetail",
      name = "Low detail",
      description = "Hides ground detail and simplifies textures.",
      position = 0
   )
   default boolean lowDetail() {
      return true;
   }

   @ConfigItem(
      keyName = "hideLowerPlanes",
      name = "Hide lower planes",
      description = "Only renders the current plane you are on.",
      position = 1
   )
   default boolean hideLowerPlanes() {
      return false;
   }
}
