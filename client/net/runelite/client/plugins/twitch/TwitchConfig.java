package net.runelite.client.plugins.twitch;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("twitch")
public interface TwitchConfig extends Config {
   @ConfigItem(
      keyName = "username",
      name = "Username",
      description = "Twitch username.",
      position = 0
   )
   String username();

   @ConfigItem(
      keyName = "oauth",
      name = "OAuth token",
      description = "Enter your OAuth token here.",
      secret = true,
      position = 1
   )
   String oauthToken();

   @ConfigItem(
      keyName = "channel",
      name = "Channel",
      description = "Username of Twitch chat to join.",
      position = 2
   )
   String channel();
}
