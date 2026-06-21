package net.runelite.api;

import java.awt.Shape;

public interface GameObject extends TileObject {
   int sizeX();

   int sizeY();

   Point getSceneMinLocation();

   Point getSceneMaxLocation();

   Shape getConvexHull();

   int getOrientation();

   Renderable getRenderable();

   int getModelOrientation();

   int getConfig();
}
