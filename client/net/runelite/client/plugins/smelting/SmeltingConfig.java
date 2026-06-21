package net.runelite.client.plugins.smelting;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("smelting")
public interface SmeltingConfig extends Config {
   @ConfigItem(
      position = 1,
      keyName = "statTimeout",
      name = "Reset stats",
      description = "The time it takes for the current smelting session to be reset."
   )
   @Units(" mins")
   default int statTimeout() {
      return 5;
   }
}
