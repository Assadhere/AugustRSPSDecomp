package net.runelite.client.plugins.friendlist;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("friendlist")
public interface FriendListConfig extends Config {
   String GROUP = "friendlist";

   @ConfigItem(
      keyName = "showWorldOnLogin",
      name = "Show world on login",
      description = "Shows world number on friend login notifications."
   )
   default boolean showWorldOnLogin() {
      return false;
   }
}
