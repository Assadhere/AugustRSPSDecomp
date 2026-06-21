package net.runelite.client.plugins.objectindicators;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("objectindicators")
public interface ObjectIndicatorsConfig extends Config {
   @ConfigSection(
      name = "Render style",
      description = "The render style of object highlighting.",
      position = 0
   )
   String renderStyleSection = "renderStyleSection";

   @ConfigItem(
      position = 0,
      keyName = "highlightHull",
      name = "Highlight hull",
      description = "Configures whether or not object should be highlighted by hull.",
      section = "renderStyleSection"
   )
   default boolean highlightHull() {
      return true;
   }

   @ConfigItem(
      position = 1,
      keyName = "highlightOutline",
      name = "Highlight outline",
      description = "Configures whether or not the model of the object should be highlighted by outline.",
      section = "renderStyleSection"
   )
   default boolean highlightOutline() {
      return false;
   }

   @ConfigItem(
      position = 2,
      keyName = "highlightClickbox",
      name = "Highlight clickbox",
      description = "Configures whether the object's clickbox should be highlighted.",
      section = "renderStyleSection"
   )
   default boolean highlightClickbox() {
      return false;
   }

   @ConfigItem(
      position = 3,
      keyName = "highlightTile",
      name = "Highlight tile",
      description = "Configures whether the object's tile should be highlighted.",
      section = "renderStyleSection"
   )
   default boolean highlightTile() {
      return false;
   }

   @Alpha
   @ConfigItem(
      position = 4,
      keyName = "markerColor",
      name = "Marker color",
      description = "Configures the color of newly created object markers.",
      section = "renderStyleSection"
   )
   default Color markerColor() {
      return Color.YELLOW;
   }

   @Alpha
   @ConfigItem(
      position = 5,
      keyName = "fillColor",
      name = "Fill color",
      description = "Configures the fill color of newly created object markers.",
      section = "renderStyleSection"
   )
   Color fillColor();

   @ConfigItem(
      position = 6,
      keyName = "borderWidth",
      name = "Border width",
      description = "Width of the marked object border.",
      section = "renderStyleSection"
   )
   default double borderWidth() {
      return 2.0;
   }

   @ConfigItem(
      position = 7,
      keyName = "outlineFeather",
      name = "Outline feather",
      description = "Specify between 0-4 how much of the model outline should be faded.",
      section = "renderStyleSection"
   )
   @Range(
      min = 0,
      max = 4
   )
   default int outlineFeather() {
      return 0;
   }
}
