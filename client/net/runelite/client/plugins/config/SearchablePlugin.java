package net.runelite.client.plugins.config;

import java.util.List;

interface SearchablePlugin {
   String getSearchableName();

   List<String> getKeywords();

   default boolean isPinned() {
      return false;
   }

   default int installs() {
      return 0;
   }
}
