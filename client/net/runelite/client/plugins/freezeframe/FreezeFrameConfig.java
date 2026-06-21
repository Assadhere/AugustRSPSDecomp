package net.runelite.client.plugins.freezeframe;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup("freezeframe")
public interface FreezeFrameConfig extends Config {
   String GROUP = "freezeframe";
   @ConfigSection(
      name = "Hotkeys",
      description = "Keybinds for freeze + capture.",
      position = 0
   )
   String hotkeysSection = "hotkeys";
   @ConfigSection(
      name = "Hide",
      description = "Toggle visibility of entities and UI.",
      position = 1
   )
   String hideSection = "hide";
   @ConfigSection(
      name = "Sky + Capture",
      description = "Skybox override and capture export.",
      position = 2
   )
   String captureSection = "capture";

   @ConfigItem(
      keyName = "freezeHotkey",
      name = "Freeze toggle",
      description = "Toggles animation freeze on/off.",
      position = 0,
      section = "hotkeys"
   )
   default Keybind freezeHotkey() {
      return Keybind.NOT_SET;
   }

   @ConfigItem(
      keyName = "captureHotkey",
      name = "Capture frame",
      description = "Capture one PNG with transparent skybox baked into alpha and save to export directory.",
      position = 1,
      section = "hotkeys"
   )
   default Keybind captureHotkey() {
      return Keybind.NOT_SET;
   }

   @ConfigItem(
      keyName = "freezeAnimations",
      name = "Freeze animations",
      description = "Pin actor animation frames so they stop moving.",
      position = 2,
      section = "hotkeys"
   )
   default boolean freezeAnimations() {
      return true;
   }

   @ConfigItem(
      keyName = "hideOthers",
      name = "Hide other players",
      description = "Hide players other than the local player.",
      position = 0,
      section = "hide"
   )
   default boolean hideOthers() {
      return false;
   }

   @ConfigItem(
      keyName = "hideLocalPlayer",
      name = "Hide local player",
      description = "Hide the local player.",
      position = 1,
      section = "hide"
   )
   default boolean hideLocalPlayer() {
      return false;
   }

   @ConfigItem(
      keyName = "hideNPCs",
      name = "Hide NPCs",
      description = "Hide all NPCs.",
      position = 2,
      section = "hide"
   )
   default boolean hideNPCs() {
      return false;
   }

   @ConfigItem(
      keyName = "hideProjectiles",
      name = "Hide projectiles",
      description = "Hide in-flight projectiles.",
      position = 3,
      section = "hide"
   )
   default boolean hideProjectiles() {
      return true;
   }

   @ConfigItem(
      keyName = "hideGraphicsObjects",
      name = "Hide spotanims",
      description = "Hide ground graphics objects / spotanims.",
      position = 4,
      section = "hide"
   )
   default boolean hideGraphicsObjects() {
      return true;
   }

   @ConfigItem(
      keyName = "hideWorldEntities",
      name = "Hide world entities",
      description = "Hide boats and other world entities.",
      position = 5,
      section = "hide"
   )
   default boolean hideWorldEntities() {
      return false;
   }

   @ConfigItem(
      keyName = "hideGroundItems",
      name = "Hide ground items",
      description = "Hide tile-item piles on the ground.",
      position = 6,
      section = "hide"
   )
   default boolean hideGroundItems() {
      return false;
   }

   @ConfigItem(
      keyName = "hideRuneLiteObjects",
      name = "Hide RuneLite objects",
      description = "Hide objects spawned by other plugins (markers, indicators, etc.).",
      position = 7,
      section = "hide"
   )
   default boolean hideRuneLiteObjects() {
      return false;
   }

   @ConfigItem(
      keyName = "hideScenery",
      name = "Hide scenery",
      description = "Hide static scenery and animated objects (trees, walls, mills, etc.).",
      position = 8,
      section = "hide"
   )
   default boolean hideScenery() {
      return false;
   }

   @ConfigItem(
      keyName = "hideTerrain",
      name = "Hide terrain + static scenery",
      description = "Skip zone-baked drawing — hides ground tiles, walls, and static objects (all share one VAO). Requires GPU.",
      position = 9,
      section = "hide"
   )
   default boolean hideTerrain() {
      return false;
   }

   @ConfigItem(
      keyName = "hideUI",
      name = "Hide UI",
      description = "Wipe all 2D overlays — chat, inventory, minimap, sidebar.",
      position = 10,
      section = "hide"
   )
   default boolean hideUI() {
      return false;
   }

   @ConfigItem(
      keyName = "cullRadius",
      name = "Camera cull radius (tiles)",
      description = "Hide entities + dynamic scenery within this many tiles of the camera so they don't block the shot. 0 disables. Local player is never culled.",
      position = 11,
      section = "hide"
   )
   default int cullRadius() {
      return 0;
   }

   @ConfigItem(
      keyName = "skyboxColor",
      name = "Skybox color",
      description = "Override the skybox color. Leave blank for no override.",
      position = 0,
      section = "capture"
   )
   default Color skyboxColor() {
      return null;
   }

   @ConfigItem(
      keyName = "exportDirectory",
      name = "Export directory",
      description = "Folder to save captured frames. Leave blank for default screenshots/FreezeFrame.",
      position = 1,
      section = "capture"
   )
   default String exportDirectory() {
      return "";
   }

   @ConfigItem(
      keyName = "captureIncludeFrame",
      name = "Include client frame",
      description = "Include the client window chrome in captured frames.",
      position = 2,
      section = "capture"
   )
   default boolean captureIncludeFrame() {
      return false;
   }
}
