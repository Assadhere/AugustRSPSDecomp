package net.runelite.api.worldmap;

import net.runelite.api.coords.WorldPoint;

public interface WorldMapIcon {
   int getType();

   WorldPoint getCoordinate();
}
