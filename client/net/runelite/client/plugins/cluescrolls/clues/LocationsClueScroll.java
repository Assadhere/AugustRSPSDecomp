package net.runelite.client.plugins.cluescrolls.clues;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;

public interface LocationsClueScroll {
   void reset();

   WorldPoint[] getLocations(ClueScrollPlugin var1);
}
