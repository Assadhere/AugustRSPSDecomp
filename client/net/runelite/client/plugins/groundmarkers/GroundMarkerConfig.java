package net.runelite.client.plugins.groundmarkers;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("groundMarker")
public interface GroundMarkerConfig extends Config {
   String GROUND_MARKER_CONFIG_GROUP = "groundMarker";
   String SHOW_IMPORT_EXPORT_KEY_NAME = "showImportExport";

   @Alpha
   @ConfigItem(
      keyName = "markerColor",
      name = "Tile color",
      description = "The default color for marked tiles."
   )
   default Color markerColor() {
      return Color.YELLOW;
   }

   @ConfigItem(
      keyName = "drawOnMinimap",
      name = "Draw tiles on minimap",
      description = "Configures whether marked tiles should be drawn on minimap."
   )
   default boolean drawTileOnMinimmap() {
      return false;
   }

   @ConfigItem(
      keyName = "showImportExport",
      name = "Show import/export/clear options",
      description = "Show the Import, Export, and Clear options on the world map orb right-click menu."
   )
   default boolean showImportExport() {
      return true;
   }

   @ConfigItem(
      keyName = "borderWidth",
      name = "Border width",
      description = "Width of the marked tile border."
   )
   default double borderWidth() {
      return 2.0;
   }

   @ConfigItem(
      keyName = "fillOpacity",
      name = "Fill opacity",
      description = "Opacity of the tile fill color."
   )
   @Range(
      max = 255
   )
   default int fillOpacity() {
      return 50;
   }
}
