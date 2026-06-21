package net.runelite.client.plugins.tearsofguthix;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.util.ColorUtil;

@ConfigGroup("tearsofguthix")
public interface TearsOfGuthixConfig extends Config {
   @ConfigItem(
      keyName = "showGreenTearsTimer",
      name = "Enable green tears timer",
      description = "Configures whether to display a timer for green tears or not.",
      position = 1
   )
   default boolean showGreenTearsTimer() {
      return true;
   }

   @Alpha
   @ConfigItem(
      keyName = "blueTearsColor",
      name = "Blue tears color",
      description = "Color of blue tears timer.",
      position = 2
   )
   default Color getBlueTearsColor() {
      return ColorUtil.colorWithAlpha(Color.CYAN, 100);
   }

   @Alpha
   @ConfigItem(
      keyName = "greenTearsColor",
      name = "Green tears color",
      description = "Color of green tears timer.",
      position = 3
   )
   default Color getGreenTearsColor() {
      return ColorUtil.colorWithAlpha(Color.GREEN, 100);
   }
}
