package net.runelite.client.plugins.hiscore;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("hiscore")
public interface HiscoreConfig extends Config {
   @ConfigItem(
      position = 1,
      keyName = "virtualLevels",
      name = "Display virtual levels",
      description = "Display levels over 99 in the hiscore panel."
   )
   default boolean virtualLevels() {
      return true;
   }
}
