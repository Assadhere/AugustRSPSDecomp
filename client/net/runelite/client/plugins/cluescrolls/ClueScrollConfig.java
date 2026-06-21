package net.runelite.client.plugins.cluescrolls;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("cluescroll")
public interface ClueScrollConfig extends Config {
   String GROUP = "cluescroll";

   @ConfigItem(
      keyName = "displayHintArrows",
      name = "Display hint arrows",
      description = "Configures whether or not to display hint arrows for clues."
   )
   default boolean displayHintArrows() {
      return true;
   }

   @ConfigItem(
      keyName = "identify",
      name = "Identify",
      description = "Identify clue scrolls when read, picked up, or always on pickup. Does not work for beginner or master clues."
   )
   default IdentificationMode identify() {
      return ClueScrollConfig.IdentificationMode.ON_READ;
   }

   public static enum IdentificationMode {
      ON_READ,
      IF_INACTIVE,
      ON_PICKUP;
   }
}
