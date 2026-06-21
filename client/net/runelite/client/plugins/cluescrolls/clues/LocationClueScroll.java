package net.runelite.client.plugins.cluescrolls.clues;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;

public interface LocationClueScroll {
   WorldPoint getLocation(ClueScrollPlugin var1);

   default WorldPoint[] getLocations(ClueScrollPlugin plugin) {
      WorldPoint location = this.getLocation(plugin);
      return location == null ? new WorldPoint[0] : new WorldPoint[]{location};
   }
}
