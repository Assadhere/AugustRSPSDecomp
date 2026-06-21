package net.runelite.client.plugins.friendnotes;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("friendNotes")
public interface FriendNotesConfig extends Config {
   @ConfigItem(
      keyName = "showIcons",
      name = "Show icons",
      description = "Show icons on friend or ignore list.",
      position = 1
   )
   default boolean showIcons() {
      return true;
   }
}
