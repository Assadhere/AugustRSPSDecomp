package net.runelite.client.plugins.hunter;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Notification;

@ConfigGroup("hunterplugin")
public interface HunterConfig extends Config {
   @Alpha
   @ConfigItem(
      position = 1,
      keyName = "hexColorOpenTrap",
      name = "Open trap",
      description = "Color of open trap timer."
   )
   default Color getOpenTrapColor() {
      return Color.YELLOW;
   }

   @Alpha
   @ConfigItem(
      position = 2,
      keyName = "hexColorFullTrap",
      name = "Full trap",
      description = "Color of full trap timer."
   )
   default Color getFullTrapColor() {
      return Color.GREEN;
   }

   @Alpha
   @ConfigItem(
      position = 3,
      keyName = "hexColorEmptyTrap",
      name = "Empty trap",
      description = "Color of empty trap timer."
   )
   default Color getEmptyTrapColor() {
      return Color.RED;
   }

   @Alpha
   @ConfigItem(
      position = 4,
      keyName = "hexColorTransTrap",
      name = "Transitioning trap",
      description = "Color of transitioning trap timer."
   )
   default Color getTransTrapColor() {
      return Color.ORANGE;
   }

   @ConfigItem(
      position = 5,
      keyName = "maniacalMonkeyNotify",
      name = "Maniacal monkey notification",
      description = "Send notification when maniacal monkey is caught or you fail to catch."
   )
   default Notification maniacalMonkeyNotify() {
      return Notification.OFF;
   }
}
