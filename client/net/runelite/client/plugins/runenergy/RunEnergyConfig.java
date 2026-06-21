package net.runelite.client.plugins.runenergy;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("runenergy")
public interface RunEnergyConfig extends Config {
   String GROUP_NAME = "runenergy";

   @ConfigItem(
      keyName = "ringOfEnduranceChargeMessage",
      name = "Ring of endurance charge message",
      description = "Sends a message asking you to charge your equipped ring of endurance when it has less than 500 charges."
   )
   default boolean ringOfEnduranceChargeMessage() {
      return true;
   }

   @ConfigItem(
      keyName = "replaceOrbText",
      name = "Replace orb text with run time left",
      description = "Show the remaining run time (in seconds) next in the energy orb."
   )
   default boolean replaceOrbText() {
      return false;
   }
}
