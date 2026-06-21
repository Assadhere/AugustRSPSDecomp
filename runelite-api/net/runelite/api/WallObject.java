package net.runelite.api;

import java.awt.Shape;

public interface WallObject extends TileObject {
   int getOrientationA();

   int getOrientationB();

   int getConfig();

   Shape getConvexHull();

   Shape getConvexHull2();

   Renderable getRenderable1();

   Renderable getRenderable2();
}
