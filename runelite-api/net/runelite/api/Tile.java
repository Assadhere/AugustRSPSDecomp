package net.runelite.api;

import java.util.List;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public interface Tile {
   DecorativeObject getDecorativeObject();

   GameObject[] getGameObjects();

   ItemLayer getItemLayer();

   GroundObject getGroundObject();

   void setGroundObject(GroundObject var1);

   WallObject getWallObject();

   SceneTilePaint getSceneTilePaint();

   void setSceneTilePaint(SceneTilePaint var1);

   SceneTileModel getSceneTileModel();

   void setSceneTileModel(SceneTileModel var1);

   WorldPoint getWorldLocation();

   Point getSceneLocation();

   LocalPoint getLocalLocation();

   int getPlane();

   int getRenderLevel();

   List<TileItem> getGroundItems();

   Tile getBridge();
}
