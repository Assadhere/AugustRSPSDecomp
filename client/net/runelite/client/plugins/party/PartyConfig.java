package net.runelite.client.plugins.party;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup("party")
public interface PartyConfig extends Config {
   String GROUP = "party";
   @ConfigSection(
      name = "Player status overlay",
      description = "Player status such as health, prayer, and special attack energy drawn on player models.",
      position = 100
   )
   String SECTION_STATUS_OVERLAY = "statusOverlay";

   @ConfigItem(
      keyName = "pings",
      name = "Pings",
      description = "Enables party pings.<br>To ping, hold the ping hotkey down and click on the tile you want to ping.",
      position = 1
   )
   default boolean pings() {
      return true;
   }

   @ConfigItem(
      keyName = "sounds",
      name = "Sound on ping",
      description = "Enables sound notification on party ping.",
      position = 2
   )
   default boolean sounds() {
      return true;
   }

   @ConfigItem(
      keyName = "recolorNames",
      name = "Recolor names",
      description = "Recolor party members names based on unique color hash.",
      position = 3
   )
   default boolean recolorNames() {
      return true;
   }

   @ConfigItem(
      keyName = "pingHotkey",
      name = "Ping hotkey",
      description = "Key to hold to send a tile ping.<br>To ping, hold the ping hotkey down and click on the tile you want to ping.",
      position = 4
   )
   default Keybind pingHotkey() {
      return Keybind.NOT_SET;
   }

   @ConfigItem(
      keyName = "memberColor",
      name = "Self-color",
      description = "Which color you will appear as in the party panel and tile pings.",
      position = 5
   )
   Color memberColor();

   @ConfigItem(
      keyName = "memberColor",
      name = "",
      description = "",
      position = 5
   )
   void setMemberColor(Color var1);

   @ConfigItem(
      section = "statusOverlay",
      keyName = "statusOverlayHealth",
      name = "Show health",
      description = "Show health of party members on the player model.",
      position = 101
   )
   default boolean statusOverlayHealth() {
      return false;
   }

   @ConfigItem(
      section = "statusOverlay",
      keyName = "statusOverlayPrayer",
      name = "Show prayer",
      description = "Show prayer of party members on the player model.",
      position = 102
   )
   default boolean statusOverlayPrayer() {
      return false;
   }

   @ConfigItem(
      section = "statusOverlay",
      keyName = "statusOverlayStamina",
      name = "Show run energy",
      description = "Show run energy (stamina) of party members on the player model.",
      position = 103
   )
   default boolean statusOverlayStamina() {
      return false;
   }

   @ConfigItem(
      section = "statusOverlay",
      keyName = "statusOverlaySpec",
      name = "Show spec energy",
      description = "Show special attack energy of party members on the player model.",
      position = 104
   )
   default boolean statusOverlaySpec() {
      return false;
   }

   @ConfigItem(
      section = "statusOverlay",
      keyName = "statusOverlayVeng",
      name = "Show vengeance",
      description = "Show vengeance status (active/inactive) of party members on the player model.",
      position = 105
   )
   default boolean statusOverlayVeng() {
      return true;
   }

   @ConfigItem(
      section = "statusOverlay",
      keyName = "statusOverlayRenderSelf",
      name = "Show on self",
      description = "Show above activated status overlays on your local player.",
      position = 106
   )
   default boolean statusOverlayRenderSelf() {
      return true;
   }

   @ConfigItem(
      keyName = "previousPartyId",
      name = "",
      description = "",
      hidden = true
   )
   default String previousPartyId() {
      return "";
   }

   @ConfigItem(
      keyName = "previousPartyId",
      name = "",
      description = "",
      hidden = true
   )
   void setPreviousPartyId(String var1);
}
