package net.runelite.api;

import java.awt.Shape;

public interface DecorativeObject extends TileObject {
   Shape getConvexHull();

   Shape getConvexHull2();

   Renderable getRenderable();

   Renderable getRenderable2();

   int getXOffset();

   int getYOffset();

   int getXOffset2();

   int getYOffset2();

   int getConfig();
}
