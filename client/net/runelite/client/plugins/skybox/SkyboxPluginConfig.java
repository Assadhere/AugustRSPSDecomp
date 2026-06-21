package net.runelite.client.plugins.skybox;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("skybox")
public interface SkyboxPluginConfig extends Config {
   @ConfigItem(
      keyName = "customOverworldColor",
      name = "Overworld sky color",
      description = "Sets the color to use for the sky in overworld areas.",
      position = 1
   )
   Color customOverworldColor();

   @ConfigItem(
      keyName = "customOtherColor",
      name = "Cave sky color",
      description = "Sets the color to use for the sky in non-overworld areas.",
      position = 2
   )
   Color customOtherColor();
}
