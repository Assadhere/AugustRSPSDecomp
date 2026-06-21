package net.runelite.client.plugins.augustcustom.ghostrendertuner;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("ghostrendertuner")
public interface GhostRenderTunerConfig extends Config {
   String GROUP = "ghostrendertuner";

   @ConfigItem(
      keyName = "override",
      name = "Override ghost look",
      description = "When off, ghosts use the baked production look. When on, the sliders below drive them live.",
      position = 0
   )
   default boolean override() {
      return true;
   }

   @Range(
      max = 255
   )
   @ConfigItem(
      keyName = "alpha",
      name = "Transparency",
      description = "Per-face alpha (0 = fully opaque, 255 = invisible). Higher = more see-through. Baked default 120.",
      position = 1
   )
   default int alpha() {
      return 150;
   }

   @Range(
      max = 127
   )
   @ConfigItem(
      keyName = "tintAmount",
      name = "Tint strength",
      description = "How hard the spectral tint overrides the real gear colour (0 = keep full actual colour, 127 = full wash). Lower this to retain more of their real colour. Baked default 70.",
      position = 2
   )
   default int tintAmount() {
      return 40;
   }

   @Range(
      max = 63
   )
   @ConfigItem(
      keyName = "tintHue",
      name = "Tint hue",
      description = "Spectral tint hue (0-63). ~25 is a soft green. Baked default 25.",
      position = 3
   )
   default int tintHue() {
      return 0;
   }

   @Range(
      max = 7
   )
   @ConfigItem(
      keyName = "tintSaturation",
      name = "Tint saturation",
      description = "Spectral tint saturation (0-7). Low = whitish/washed, high = vivid colour cast. Baked default 1.",
      position = 4
   )
   default int tintSaturation() {
      return 0;
   }

   @Range(
      max = 127
   )
   @ConfigItem(
      keyName = "tintLuminance",
      name = "Tint luminance",
      description = "Spectral tint luminance (0-127). Higher lifts the whole model paler. Baked default 110.",
      position = 5
   )
   default int tintLuminance() {
      return 0;
   }
}
