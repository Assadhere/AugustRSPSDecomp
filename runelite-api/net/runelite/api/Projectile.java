package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public interface Projectile extends Renderable {
   int getId();

   int getSourceLevel();

   WorldPoint getSourcePoint();

   @Nullable
   Actor getSourceActor();

   int getTargetLevel();

   WorldPoint getTargetPoint();

   @Nullable
   Actor getTargetActor();

   /** @deprecated */
   @Deprecated
   default Actor getInteracting() {
      return this.getTargetActor();
   }

   /** @deprecated */
   @Deprecated
   LocalPoint getTarget();

   /** @deprecated */
   @Deprecated
   int getX1();

   /** @deprecated */
   @Deprecated
   int getY1();

   /** @deprecated */
   @Deprecated
   int getFloor();

   /** @deprecated */
   @Deprecated
   int getHeight();

   int getEndHeight();

   int getStartCycle();

   int getEndCycle();

   void setEndCycle(int var1);

   int getRemainingCycles();

   int getSlope();

   int getStartPos();

   int getStartHeight();

   double getX();

   double getY();

   double getZ();

   int getOrientation();

   @Nullable
   Animation getAnimation();

   int getAnimationFrame();
}
