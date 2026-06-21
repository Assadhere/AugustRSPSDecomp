package net.runelite.client.plugins.reportbutton;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("reportButton")
public interface ReportButtonConfig extends Config {
   @ConfigItem(
      keyName = "time",
      name = "Display options",
      description = "Configures what text the report button shows."
   )
   default TimeStyle time() {
      return TimeStyle.LOGIN_TIME;
   }

   @ConfigItem(
      keyName = "switchTimeFormat",
      name = "Time format",
      description = "Configures time between 12 or 24 hour time format."
   )
   default TimeFormat switchTimeFormat() {
      return TimeFormat.TIME_12H;
   }
}
