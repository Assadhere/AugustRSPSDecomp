package net.runelite.client.plugins.corp;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("corp")
public interface CorpConfig extends Config {
   String GROUP = "corp";

   @ConfigItem(
      keyName = "leftClickCore",
      name = "Left-click walk on core",
      description = "Prioritizes Walk here over Attack on the dark energy core.",
      position = 1
   )
   default boolean leftClickCore() {
      return true;
   }

   @ConfigItem(
      keyName = "showDamage",
      name = "Show damage overlay",
      description = "Show total damage overlay.",
      position = 0
   )
   default boolean showDamage() {
      return true;
   }

   @ConfigItem(
      keyName = "markDarkCore",
      name = "Mark dark core",
      description = "Marks the dark energy core.",
      position = 1
   )
   default boolean markDarkCore() {
      return true;
   }
}
