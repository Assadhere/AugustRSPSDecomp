package net.runelite.client.plugins.customcursor;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("customcursor")
public interface CustomCursorConfig extends Config {
   @ConfigItem(
      keyName = "cursorStyle",
      name = "Cursor",
      description = "Select which cursor you wish to use."
   )
   default CustomCursor selectedCursor() {
      return CustomCursor.RS3_GOLD;
   }
}
