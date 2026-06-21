package net.runelite.client.plugins.roofremoval;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("roofremoval")
public interface RoofRemovalConfig extends Config {
   String CONFIG_GROUP = "roofremoval";
   @ConfigSection(
      name = "Modes",
      description = "In what situations should roofs be removed.",
      position = 0
   )
   String modesSection = "modes";
   @ConfigSection(
      name = "Area overrides",
      description = "Always remove roofs in specific areas.",
      position = 1
   )
   String overridesSection = "overrides";

   @ConfigItem(
      keyName = "removePosition",
      name = "Player's position",
      description = "Remove roofs above the player's position.",
      section = "modes"
   )
   default boolean removePosition() {
      return true;
   }

   @ConfigItem(
      keyName = "removeHovered",
      name = "Hovered tile",
      description = "Remove roofs above the hovered tile.",
      section = "modes"
   )
   default boolean removeHovered() {
      return true;
   }

   @ConfigItem(
      keyName = "removeDestination",
      name = "Destination tile",
      description = "Remove roofs above the destination tile.",
      section = "modes"
   )
   default boolean removeDestination() {
      return true;
   }

   @ConfigItem(
      keyName = "removeBetween",
      name = "Between camera & player",
      description = "Remove roofs between the camera and the player at low camera angles.",
      section = "modes"
   )
   default boolean removeBetween() {
      return true;
   }

   @ConfigItem(
      keyName = "overridePOH",
      name = "Player owned house",
      description = "Always remove roofs while in the player owned house.",
      section = "overrides"
   )
   default boolean overridePOH() {
      return false;
   }
}
