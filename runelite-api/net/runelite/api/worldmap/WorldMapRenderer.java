package net.runelite.api.worldmap;

public interface WorldMapRenderer {
   boolean isLoaded();

   WorldMapRegion[][] getMapRegions();
}
