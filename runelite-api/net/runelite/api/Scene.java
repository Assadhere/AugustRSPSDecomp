package net.runelite.api;

public interface Scene extends Renderable {
   Tile[][][] getTiles();

   Tile[][][] getExtendedTiles();

   byte[][][] getExtendedTileSettings();

   int getDrawDistance();

   void setDrawDistance(int var1);

   int getWorldViewId();

   int getMinLevel();

   void setMinLevel(int var1);

   void removeTile(Tile var1);

   void removeGameObject(GameObject var1);

   void buildRoofs();

   int[][][] getRoofs();

   void setRoofRemovalMode(int var1);

   int getRoofRemovalMode();

   short[][][] getUnderlayIds();

   short[][][] getOverlayIds();

   byte[][][] getTileShapes();

   int[][][] getTileHeights();

   int getBaseX();

   int getBaseY();

   boolean isInstance();

   int[][][] getInstanceTemplateChunks();

   int[] getMapRegions();

   byte getOverrideAmount();

   byte getOverrideHue();

   byte getOverrideSaturation();

   byte getOverrideLuminance();
}
