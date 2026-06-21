package net.runelite.client.plugins.interfacestyles;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("interfaceStyles")
public interface InterfaceStylesConfig extends Config {
   @ConfigItem(
      keyName = "gameframe",
      name = "Gameframe",
      description = "The gameframe to use for the interface."
   )
   default Skin skin() {
      return Skin.AROUND_2010;
   }

   @ConfigItem(
      keyName = "hdHealthBars",
      name = "High detail health bars",
      description = "Replaces health bars with the RuneScape high detail mode design."
   )
   default boolean hdHealthBars() {
      return false;
   }

   @ConfigItem(
      keyName = "hdMenu",
      name = "High detail menu",
      description = "Replaces game menu with the RuneScape high detail mode design."
   )
   default boolean hdMenu() {
      return false;
   }

   @ConfigItem(
      keyName = "rsCrossSprites",
      name = "RuneScape cross sprites",
      description = "Replaces left-click cross sprites with the ones in RuneScape."
   )
   default boolean rsCrossSprites() {
      return false;
   }

   @ConfigItem(
      keyName = "alwaysStack",
      name = "Always stack bottom bar",
      description = "Always stack the bottom bar in resizable."
   )
   default boolean alwaysStack() {
      return false;
   }

   @Range(
      max = 255
   )
   @ConfigItem(
      keyName = "menuAlpha",
      name = "Menu alpha",
      description = "Configures the transparency of the right-click menu."
   )
   default int menuAlpha() {
      return 255;
   }

   @ConfigItem(
      keyName = "condensePlayerOptions",
      name = "Condense player options",
      description = "Move player options like Follow and Trade with to submenus."
   )
   default boolean condensePlayerOptions() {
      return false;
   }
}
