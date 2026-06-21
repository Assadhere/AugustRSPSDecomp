package net.runelite.client.plugins.notes;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("notes")
public interface NotesConfig extends Config {
   @ConfigItem(
      keyName = "notesData",
      name = "",
      description = "",
      hidden = true
   )
   default String notesData() {
      return "";
   }

   @ConfigItem(
      keyName = "notesData",
      name = "",
      description = ""
   )
   void notesData(String var1);
}
