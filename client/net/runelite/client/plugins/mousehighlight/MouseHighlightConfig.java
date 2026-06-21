package net.runelite.client.plugins.mousehighlight;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("mousehighlight")
public interface MouseHighlightConfig extends Config {
   @ConfigItem(
      position = 0,
      keyName = "uiTooltip",
      name = "Interface tooltips",
      description = "Whether or not tooltips are shown on interfaces."
   )
   default boolean uiTooltip() {
      return true;
   }

   @ConfigItem(
      position = 1,
      keyName = "chatboxTooltip",
      name = "Chatbox tooltips",
      description = "Whether or not tooltips are shown over the chatbox."
   )
   default boolean chatboxTooltip() {
      return true;
   }

   @ConfigItem(
      position = 2,
      keyName = "disableSpellbooktooltip",
      name = "Disable spellbook tooltips",
      description = "Disable spellbook tooltips so they don't cover descriptions."
   )
   default boolean disableSpellbooktooltip() {
      return false;
   }
}
