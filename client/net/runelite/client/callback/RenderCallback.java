package net.runelite.client.callback;

import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;

public interface RenderCallback {
   default boolean addEntity(Renderable renderable, boolean ui) {
      return true;
   }

   default void onDraw(Renderable renderable) {
   }

   default boolean drawTile(Scene scene, Tile tile) {
      return true;
   }

   default boolean drawObject(Scene scene, TileObject object) {
      return true;
   }
}
