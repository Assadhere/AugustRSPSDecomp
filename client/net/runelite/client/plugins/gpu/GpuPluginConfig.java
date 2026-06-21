package net.runelite.client.plugins.gpu;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.plugins.gpu.config.AntiAliasingMode;
import net.runelite.client.plugins.gpu.config.ColorBlindMode;
import net.runelite.client.plugins.gpu.config.UIScalingMode;

@ConfigGroup("gpu")
public interface GpuPluginConfig extends Config {
   String GROUP = "gpu";

   @Range(
      max = 184
   )
   @ConfigItem(
      keyName = "drawDistance",
      name = "Draw distance",
      description = "Draw distance.",
      position = 1
   )
   default int drawDistance() {
      return 50;
   }

   @ConfigItem(
      keyName = "hideUnrelatedMaps",
      name = "Hide unrelated maps",
      description = "Hide unrelated map areas you shouldn't see.",
      position = 2
   )
   default boolean hideUnrelatedMaps() {
      return true;
   }

   @Range(
      max = 5
   )
   @ConfigItem(
      keyName = "expandedMapLoadingChunks",
      name = "Extended map loading",
      description = "Extra map area to load, in 8 tile chunks.",
      position = 1
   )
   default int expandedMapLoadingZones() {
      return 3;
   }

   @ConfigItem(
      keyName = "smoothBanding",
      name = "Remove color banding",
      description = "Smooths out the color banding that is present in the CPU renderer.",
      position = 2
   )
   default boolean smoothBanding() {
      return true;
   }

   @ConfigItem(
      keyName = "antiAliasingMode",
      name = "Anti aliasing",
      description = "Configures the anti-aliasing mode.",
      position = 3
   )
   default AntiAliasingMode antiAliasingMode() {
      return AntiAliasingMode.MSAA_2;
   }

   @ConfigItem(
      keyName = "uiScalingMode",
      name = "UI scaling mode",
      description = "Sampling function to use for the UI in stretched mode.",
      position = 4
   )
   default UIScalingMode uiScalingMode() {
      return UIScalingMode.HYBRID;
   }

   @Range(
      max = 100
   )
   @ConfigItem(
      keyName = "fogDepth",
      name = "Fog depth",
      description = "Distance from the scene edge the fog starts.",
      position = 5
   )
   default int fogDepth() {
      return 0;
   }

   @Range(
      min = 0,
      max = 16
   )
   @ConfigItem(
      keyName = "anisotropicFilteringLevel",
      name = "Anisotropic filtering",
      description = "Configures the anisotropic filtering level.",
      position = 7
   )
   default int anisotropicFilteringLevel() {
      return 1;
   }

   @ConfigItem(
      keyName = "colorBlindMode",
      name = "Colorblindness correction",
      description = "Adjusts colors to account for colorblindness.",
      position = 8
   )
   default ColorBlindMode colorBlindMode() {
      return ColorBlindMode.NONE;
   }

   @Range(
      min = 0,
      max = 100
   )
   @ConfigItem(
      keyName = "colorBlindIntensity",
      name = "Colorblindness intensity",
      description = "Strength of the colorblindness correction effect.",
      position = 9
   )
   default int colorBlindIntensity() {
      return 100;
   }

   @ConfigItem(
      keyName = "brightTextures",
      name = "Bright textures",
      description = "Use old texture lighting method which results in brighter game textures.",
      position = 10
   )
   default boolean brightTextures() {
      return false;
   }

   @ConfigItem(
      keyName = "unlockFps",
      name = "Unlock FPS",
      description = "Removes the 50 FPS cap for camera movement.",
      position = 11
   )
   default boolean unlockFps() {
      return true;
   }

   @ConfigItem(
      keyName = "vsyncMode",
      name = "Vsync mode",
      description = "Method to synchronize frame rate with refresh rate.",
      position = 12
   )
   default SyncMode syncMode() {
      return GpuPluginConfig.SyncMode.OFF;
   }

   @ConfigItem(
      keyName = "fpsTarget",
      name = "FPS target",
      description = "Target FPS when 'Unlock FPS' is enabled and 'Vsync mode' is off.",
      position = 13
   )
   @Range(
      min = 1,
      max = 999
   )
   default int fpsTarget() {
      return 60;
   }

   @ConfigItem(
      keyName = "removeVertexSnapping",
      name = "Remove vertex snapping",
      description = "Removes vertex snapping from most animations.",
      position = 14
   )
   default boolean removeVertexSnapping() {
      return true;
   }

   public static enum SyncMode {
      OFF,
      ON,
      ADAPTIVE;
   }
}
