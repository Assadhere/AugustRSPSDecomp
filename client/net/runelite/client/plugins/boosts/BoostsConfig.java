package net.runelite.client.plugins.boosts;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Notification;

@ConfigGroup("boosts")
public interface BoostsConfig extends Config {
   @ConfigItem(
      keyName = "displayBoosts",
      name = "Display boosts",
      description = "Configures which skill boosts to display.",
      position = 1
   )
   default DisplayBoosts displayBoosts() {
      return BoostsConfig.DisplayBoosts.COMBAT;
   }

   @ConfigItem(
      keyName = "relativeBoost",
      name = "Show relative boosts",
      description = "Configures whether or not relative boost is used.",
      position = 2
   )
   default boolean useRelativeBoost() {
      return false;
   }

   @ConfigItem(
      keyName = "displayIndicators",
      name = "Display infoboxes",
      description = "Configures whether to display boost infoboxes.",
      position = 3
   )
   default boolean displayInfoboxes() {
      return true;
   }

   @ConfigItem(
      keyName = "displayPanel",
      name = "Display panel",
      description = "Configures whether to display the boost panel.",
      position = 3
   )
   default boolean displayPanel() {
      return false;
   }

   @ConfigItem(
      keyName = "compactDisplay",
      name = "Compact display",
      description = "Displays skill boosts in a more compact panel.",
      position = 4
   )
   default boolean compactDisplay() {
      return false;
   }

   @ConfigItem(
      keyName = "displayNextBuffChange",
      name = "Next buff change",
      description = "Configures whether or not to display when the next buffed stat change will be.",
      position = 10
   )
   default DisplayChangeMode displayNextBuffChange() {
      return BoostsConfig.DisplayChangeMode.BOOSTED;
   }

   @ConfigItem(
      keyName = "displayNextDebuffChange",
      name = "Next debuff change",
      description = "Configures whether or not to display when the next debuffed stat change will be.",
      position = 11
   )
   default DisplayChangeMode displayNextDebuffChange() {
      return BoostsConfig.DisplayChangeMode.NEVER;
   }

   @ConfigItem(
      keyName = "boostThreshold",
      name = "Boost threshold",
      description = "Number of levels above your base level at which boosted levels will be displayed in a different color.",
      position = 12
   )
   default int boostThreshold() {
      return 0;
   }

   @ConfigItem(
      keyName = "notifyOnBoost",
      name = "Notify on boost threshold",
      description = "Configures whether or not a notification will be sent when boosted stats drain to the boost threshold.",
      position = 13
   )
   default Notification notifyOnBoost() {
      return Notification.OFF;
   }

   public static enum DisplayBoosts {
      NONE,
      COMBAT,
      NON_COMBAT,
      BOTH;
   }

   public static enum DisplayChangeMode {
      ALWAYS,
      BOOSTED,
      NEVER;
   }
}
