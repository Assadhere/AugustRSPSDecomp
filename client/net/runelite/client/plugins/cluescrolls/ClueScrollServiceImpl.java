package net.runelite.client.plugins.cluescrolls;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;

@Singleton
class ClueScrollServiceImpl implements ClueScrollService {
   private final ClueScrollPlugin plugin;

   @Inject
   private ClueScrollServiceImpl(ClueScrollPlugin plugin) {
      this.plugin = plugin;
   }

   public ClueScroll getClue() {
      return this.plugin.getClue();
   }
}
