package net.runelite.client.plugins.cooking;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("cooking")
public interface CookingConfig extends Config {
   @ConfigItem(
      position = 1,
      keyName = "statTimeout",
      name = "Reset stats",
      description = "Configures the time until the session resets and the overlay is hidden (0 = disable feature)."
   )
   @Units(" mins")
   default int statTimeout() {
      return 5;
   }

   @ConfigItem(
      position = 2,
      keyName = "fermentTimer",
      name = "Show wine ferment timer",
      description = "Configures if the timer before wines are fermented is shown."
   )
   default boolean fermentTimer() {
      return true;
   }
}
