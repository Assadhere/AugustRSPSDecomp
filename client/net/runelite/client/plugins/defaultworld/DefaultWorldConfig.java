package net.runelite.client.plugins.defaultworld;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("defaultworld")
public interface DefaultWorldConfig extends Config {
   String GROUP = "defaultworld";

   @ConfigItem(
      keyName = "defaultWorld",
      name = "Default world",
      description = "World to use as default one."
   )
   default int getWorld() {
      return 0;
   }

   @ConfigItem(
      keyName = "useLastWorld",
      name = "Use last world",
      description = "Use the last world you used as the default."
   )
   default boolean useLastWorld() {
      return false;
   }

   @ConfigItem(
      keyName = "lastWorld",
      name = "",
      description = "",
      hidden = true
   )
   default int lastWorld() {
      return 0;
   }

   @ConfigItem(
      keyName = "lastWorld",
      name = "",
      description = ""
   )
   void lastWorld(int var1);
}
