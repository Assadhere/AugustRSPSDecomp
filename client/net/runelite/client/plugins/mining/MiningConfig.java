package net.runelite.client.plugins.mining;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("mining")
public interface MiningConfig extends Config {
   @ConfigItem(
      keyName = "statTimeout",
      name = "Reset stats",
      description = "Duration the mining indicator and session stats are displayed before being reset."
   )
   @Units(" mins")
   default int statTimeout() {
      return 5;
   }

   @ConfigItem(
      keyName = "showMiningStats",
      name = "Show session stats",
      description = "Configures whether to display mining session stats."
   )
   default boolean showMiningStats() {
      return true;
   }
}
