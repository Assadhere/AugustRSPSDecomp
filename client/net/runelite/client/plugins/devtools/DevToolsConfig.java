package net.runelite.client.plugins.devtools;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Notification;

@ConfigGroup("devtools")
public interface DevToolsConfig extends Config {
   @ConfigItem(
      keyName = "inspectorAlwaysOnTop",
      name = "",
      description = "",
      hidden = true
   )
   default boolean inspectorAlwaysOnTop() {
      return false;
   }

   @ConfigItem(
      keyName = "inspectorAlwaysOnTop",
      name = "",
      description = ""
   )
   void inspectorAlwaysOnTop(boolean var1);

   @ConfigItem(
      keyName = "swingInspectorHotkey",
      name = "Swing inspector",
      description = "Hotkey to open the Swing inspector, if available."
   )
   default Keybind swingInspectorHotkey() {
      return Keybind.NOT_SET;
   }

   @ConfigItem(
      keyName = "notification",
      name = "Notification",
      description = ""
   )
   default Notification notification() {
      return Notification.ON;
   }
}
