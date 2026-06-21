package net.runelite.api.worldmap;

import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;

public interface WorldMap {
   Point getWorldMapPosition();

   float getWorldMapZoom();

   void setWorldMapPositionTarget(WorldPoint var1);

   WorldMapRenderer getWorldMapRenderer();

   void initializeWorldMap(WorldMapData var1);

   WorldMapData getWorldMapData();
}
