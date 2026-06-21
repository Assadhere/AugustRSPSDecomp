package net.runelite.api.worldmap;

import net.runelite.api.SpritePixels;

public interface MapElementConfig {
   SpritePixels getMapIcon(boolean var1);

   int getCategory();
}
