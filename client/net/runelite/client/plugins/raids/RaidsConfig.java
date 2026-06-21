package net.runelite.client.plugins.raids;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("raids")
public interface RaidsConfig extends Config {
   @ConfigItem(
      position = 0,
      keyName = "raidsTimer",
      name = "Display elapsed raid time",
      description = "Display elapsed raid time."
   )
   default boolean raidsTimer() {
      return true;
   }

   @ConfigItem(
      position = 1,
      keyName = "pointsMessage",
      name = "Display points in chatbox",
      description = "Display a message with total points, individual points and percentage at the end of a raid."
   )
   default boolean pointsMessage() {
      return true;
   }

   @ConfigItem(
      position = 2,
      keyName = "scoutOverlay",
      name = "Show scout overlay",
      description = "Display an overlay that shows the current raid layout (when entering lobby)."
   )
   default boolean scoutOverlay() {
      return true;
   }

   @ConfigItem(
      position = 3,
      keyName = "scoutOverlayAtBank",
      name = "Show scout overlay outside",
      description = "Keep the overlay active outside of the raid starting room."
   )
   default boolean scoutOverlayAtBank() {
      return true;
   }

   @ConfigItem(
      position = 4,
      keyName = "scoutOverlayInRaid",
      name = "Show scout overlay inside raid",
      description = "Keep the overlay active while inside raid."
   )
   default boolean scoutOverlayInRaid() {
      return true;
   }

   @ConfigItem(
      position = 5,
      keyName = "ccDisplay",
      name = "FC and world in scout overlay",
      description = "Display current friends chat and world in scouting overlay."
   )
   default boolean fcDisplay() {
      return false;
   }

   @ConfigItem(
      position = 6,
      keyName = "whitelistedRooms",
      name = "Whitelisted rooms",
      description = "Display whitelisted rooms in green on the overlay. Separate with comma (full name)."
   )
   default String whitelistedRooms() {
      return "";
   }

   @ConfigItem(
      position = 7,
      keyName = "blacklistedRooms",
      name = "Blacklisted rooms",
      description = "Display blacklisted rooms in red on the overlay. Separate with comma (full name)."
   )
   default String blacklistedRooms() {
      return "";
   }

   @ConfigItem(
      position = 8,
      keyName = "enableRotationWhitelist",
      name = "Enable rotation whitelist",
      description = "Enable the rotation whitelist."
   )
   default boolean enableRotationWhitelist() {
      return false;
   }

   @ConfigItem(
      position = 9,
      keyName = "whitelistedRotations",
      name = "Whitelisted rotations",
      description = "Warn when boss rotation doesn't match a whitelisted one. Add rotations like: tekton, muttadiles, guardians - each rotation on its own line."
   )
   default String whitelistedRotations() {
      return "";
   }

   @ConfigItem(
      position = 10,
      keyName = "enableLayoutWhitelist",
      name = "Enable layout whitelist",
      description = "Enable the layout whitelist."
   )
   default boolean enableLayoutWhitelist() {
      return false;
   }

   @ConfigItem(
      position = 11,
      keyName = "whitelistedLayouts",
      name = "Whitelisted layouts",
      description = "Warn when layout doesn't match a whitelisted one. Add layouts like CFSCPPCSCF separated with comma."
   )
   default String whitelistedLayouts() {
      return "";
   }

   @ConfigItem(
      position = 12,
      keyName = "layoutMessage",
      name = "Raid layout message",
      description = "Sends a game message with the raid layout on entering a raid."
   )
   default boolean layoutMessage() {
      return false;
   }

   @ConfigItem(
      position = 13,
      keyName = "screenshotHotkey",
      name = "Screenshot hotkey",
      description = "Hotkey used to screenshot the scouting overlay."
   )
   default Keybind screenshotHotkey() {
      return Keybind.NOT_SET;
   }

   @ConfigItem(
      position = 14,
      keyName = "copyToClipboard",
      name = "Copy to clipboard",
      description = "Copies the scouting screenshot to clipboard."
   )
   default boolean copyToClipboard() {
      return true;
   }
}
