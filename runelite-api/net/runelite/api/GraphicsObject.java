package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.coords.LocalPoint;

public interface GraphicsObject extends Renderable {
   WorldView getWorldView();

   int getId();

   LocalPoint getLocation();

   int getStartCycle();

   int getLevel();

   int getZ();

   boolean finished();

   void setFinished(boolean var1);

   @Nullable
   Animation getAnimation();

   int getAnimationFrame();
}
