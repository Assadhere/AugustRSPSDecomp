package net.runelite.client.plugins.poison;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("poison")
public interface PoisonConfig extends Config {
   String GROUP = "poison";

   @ConfigItem(
      keyName = "showInfoboxes",
      name = "Show infoboxes",
      description = "Configures whether to show the infoboxes."
   )
   default boolean showInfoboxes() {
      return false;
   }

   @ConfigItem(
      keyName = "changeHealthIcon",
      name = "Change HP orb icon",
      description = "Configures whether the HP orb icon should change color to match poison/disease."
   )
   default boolean changeHealthIcon() {
      return true;
   }
}
