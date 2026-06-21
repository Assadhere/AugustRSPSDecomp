package net.runelite.client.plugins.stretchedmode;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("stretchedmode")
public interface StretchedModeConfig extends Config {
   @ConfigItem(
      keyName = "keepAspectRatio",
      name = "Keep aspect ratio",
      description = "Keeps the aspect ratio when stretching."
   )
   default boolean keepAspectRatio() {
      return false;
   }

   @ConfigItem(
      keyName = "increasedPerformance",
      name = "Increased performance mode",
      description = "Uses a fast algorithm when stretching, lowering quality but increasing performance."
   )
   default boolean increasedPerformance() {
      return false;
   }

   @ConfigItem(
      keyName = "integerScaling",
      name = "Integer scaling",
      description = "Forces use of a whole number scale factor when stretching."
   )
   default boolean integerScaling() {
      return false;
   }

   @ConfigItem(
      keyName = "scalingFactor",
      name = "Resizable scaling",
      description = "In resizable mode, the game is reduced in size this much before it's stretched."
   )
   @Units("%")
   default int scalingFactor() {
      return 50;
   }
}
