package net.runelite.api;

import java.awt.Shape;

public interface GroundObject extends TileObject {
   Renderable getRenderable();

   Shape getConvexHull();

   int getConfig();
}
