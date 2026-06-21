package net.runelite.client.plugins.logouttimer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup("logouttimer")
public interface LogoutTimerConfig extends Config {
   String GROUP = "logouttimer";

   @ConfigItem(
      keyName = "idleTimeout",
      name = "Idle timeout",
      description = "Amount of time before you are logged out for being idle."
   )
   @Units(" mins")
   @Range(
      min = 5,
      max = 30
   )
   default int getIdleTimeout() {
      return 5;
   }
}
