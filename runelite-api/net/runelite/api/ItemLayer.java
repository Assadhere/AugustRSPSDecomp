package net.runelite.api;

public interface ItemLayer extends TileObject {
   int getHeight();

   Renderable getBottom();

   Renderable getMiddle();

   Renderable getTop();
}
