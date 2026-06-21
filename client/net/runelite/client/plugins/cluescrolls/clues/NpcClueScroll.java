package net.runelite.client.plugins.cluescrolls.clues;

import java.util.Collection;
import java.util.Collections;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;

public interface NpcClueScroll {
   String[] getNpcs(ClueScrollPlugin var1);

   default Collection<Integer> getNpcRegions() {
      return Collections.emptyList();
   }
}
