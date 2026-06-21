package net.runelite.client.plugins.discord;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("discord")
public interface DiscordConfig extends Config {
   @ConfigItem(
      keyName = "elapsedTime",
      name = "Elapsed time",
      description = "Configures the elapsed time shown.",
      position = 1
   )
   default ElapsedTimeType elapsedTimeType() {
      return DiscordConfig.ElapsedTimeType.TOTAL;
   }

   @ConfigItem(
      keyName = "showMainMenu",
      name = "Login screen",
      description = "Show your status while at the login screen.",
      position = 2
   )
   default boolean showMainMenu() {
      return true;
   }

   @ConfigItem(
      keyName = "showActivity",
      name = "World & activity",
      description = "Show your current world and activity, as reported by the server.",
      position = 3
   )
   default boolean showActivity() {
      return true;
   }

   @ConfigItem(
      keyName = "showParty",
      name = "Party size",
      description = "Show the size of your current party or group instance.",
      position = 4
   )
   default boolean showParty() {
      return true;
   }

   public static enum ElapsedTimeType {
      TOTAL("Since login"),
      ACTIVITY("Per activity"),
      HIDDEN("Hide elapsed time");

      private final String value;

      public String toString() {
         return this.value;
      }

      private ElapsedTimeType(String value) {
         this.value = value;
      }
   }
}
