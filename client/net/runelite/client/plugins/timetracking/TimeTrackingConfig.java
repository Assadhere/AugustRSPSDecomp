package net.runelite.client.plugins.timetracking;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Notification;
import net.runelite.client.config.Units;

@ConfigGroup("timetracking")
public interface TimeTrackingConfig extends Config {
   String CONFIG_GROUP = "timetracking";
   String FARM_TICK_OFFSET = "farmTickOffset";
   String FARM_TICK_OFFSET_PRECISION = "farmTickOffsetPrecision";
   String AUTOWEED = "autoweed";
   String BIRD_HOUSE = "birdhouse";
   String TIMERS = "timers";
   String STOPWATCHES = "stopwatches";
   String PREFER_SOONEST = "preferSoonest";
   String NOTIFY = "notify";
   String BIRDHOUSE_NOTIFY = "birdHouseNotification";
   String COMPOST = "compost";
   String PROTECTED = "protected";

   @ConfigItem(
      keyName = "timeFormatMode",
      name = "Time format",
      description = "What format to display times in.",
      position = 1
   )
   default TimeFormatMode timeFormatMode() {
      return TimeFormatMode.ABSOLUTE_24H;
   }

   @ConfigItem(
      keyName = "timerNotification",
      name = "Timer notification",
      description = "Notify you whenever a timer has finished counting down.",
      position = 2
   )
   default Notification timerNotification() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "farmingContractInfoBox",
      name = "Show farming contract infobox",
      description = "Show an infobox of your current farming contract when inside the farming guild.",
      position = 4
   )
   default boolean farmingContractInfoBox() {
      return true;
   }

   @ConfigItem(
      keyName = "defaultTimerMinutes",
      name = "Default time",
      description = "The default time for the timer in minutes.",
      position = 5
   )
   @Units(" mins")
   default int defaultTimerMinutes() {
      return 5;
   }

   @ConfigItem(
      keyName = "sortOrder",
      name = "Sort order",
      description = "The order in which to sort the timers.",
      position = 6
   )
   default SortOrder sortOrder() {
      return SortOrder.NONE;
   }

   @ConfigItem(
      keyName = "timerWarningThreshold",
      name = "Warning threshold",
      description = "The time at which to change the timer color to the warning color.",
      position = 6
   )
   @Units("s")
   default int timerWarningThreshold() {
      return 10;
   }

   @ConfigItem(
      keyName = "preferSoonest",
      name = "Prefer soonest completion",
      description = "When displaying completion times on the overview, prefer showing the soonest any patch will complete.",
      position = 7
   )
   default boolean preferSoonest() {
      return false;
   }

   @ConfigItem(
      keyName = "activeTab",
      name = "Active tab",
      description = "The currently selected tab.",
      hidden = true
   )
   default Tab activeTab() {
      return Tab.CLOCK;
   }

   @ConfigItem(
      keyName = "activeTab",
      name = "",
      description = "",
      hidden = true
   )
   void setActiveTab(Tab var1);
}
