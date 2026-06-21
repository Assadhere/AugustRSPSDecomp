package net.runelite.client.plugins.perkgrapheditor;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("perkgrapheditor")
public interface PerkGraphEditorConfig extends Config {
   String GROUP = "perkgrapheditor";
   @ConfigSection(
      name = "Developer",
      description = "Developer-only settings for the Perk Graph Editor",
      position = 0
   )
   String DEVELOPER_SECTION = "developer";

   @ConfigItem(
      keyName = "augustServerPath",
      name = "august-server path",
      description = "Absolute path to a local checkout of august-server. The editor writes perk_graph_node/<variant>.toml under this directory when you save.",
      position = 1,
      section = "developer"
   )
   default String augustServerPath() {
      return "";
   }

   @ConfigItem(
      keyName = "augustServerPath",
      name = "",
      description = ""
   )
   void augustServerPath(String var1);

   @ConfigItem(
      keyName = "lastVariant",
      name = "",
      description = "",
      hidden = true
   )
   default String lastVariant() {
      return "";
   }

   @ConfigItem(
      keyName = "lastVariant",
      name = "",
      description = ""
   )
   void lastVariant(String var1);

   @ConfigItem(
      keyName = "gridSize.default",
      name = "",
      description = "",
      hidden = true
   )
   default int defaultGridSize() {
      return 10;
   }

   @ConfigItem(
      keyName = "zoom.default",
      name = "",
      description = "",
      hidden = true
   )
   default int defaultZoomPct() {
      return 50;
   }
}
