package net.runelite.client.plugins.music;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("music")
public interface MusicConfig extends Config {
   String GROUP = "music";
   String GRANULAR_SLIDERS = "granularSliders";
   String MUTE_AMBIENT_SOUNDS = "muteAmbientSounds";

   @ConfigItem(
      keyName = "muteOwnAreaSounds",
      name = "Mute player area sounds",
      description = "Mute area sounds caused by yourself.",
      position = 0
   )
   default boolean muteOwnAreaSounds() {
      return false;
   }

   @ConfigItem(
      keyName = "muteOtherAreaSounds",
      name = "Mute other players' area sounds",
      description = "Mute area sounds caused by other players.",
      position = 1
   )
   default boolean muteOtherAreaSounds() {
      return false;
   }

   @ConfigItem(
      keyName = "muteOtherAreaNPCSounds",
      name = "Mute NPCs' area sounds",
      description = "Mute area sounds caused by NPCs.",
      position = 2
   )
   default boolean muteNpcAreaSounds() {
      return false;
   }

   @ConfigItem(
      keyName = "muteOtherAreaEnvironmentSounds",
      name = "Mute environment area sounds",
      description = "Mute area sounds caused by neither NPCs nor players.",
      position = 3
   )
   default boolean muteEnvironmentAreaSounds() {
      return false;
   }

   @ConfigItem(
      keyName = "muteAmbientSounds",
      name = "Mute ambient sounds",
      description = "Mute background noise such as magic trees and furnaces.",
      position = 4
   )
   default boolean muteAmbientSounds() {
      return false;
   }

   @ConfigItem(
      keyName = "mutePrayerSounds",
      name = "Mute prayer sounds",
      description = "Mute prayer activation and deactivation sounds.",
      position = 5
   )
   default boolean mutePrayerSounds() {
      return false;
   }
}
