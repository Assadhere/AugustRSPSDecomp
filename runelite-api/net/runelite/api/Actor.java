package net.runelite.api;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public interface Actor extends Renderable, CameraFocusableEntity {
   WorldView getWorldView();

   int getCombatLevel();

   @Nullable
   String getName();

   boolean isInteracting();

   Actor getInteracting();

   int getHealthRatio();

   int getHealthScale();

   WorldPoint getWorldLocation();

   LocalPoint getLocalLocation();

   int getOrientation();

   int getCurrentOrientation();

   int getAnimation();

   int getPoseAnimation();

   void setPoseAnimation(int var1);

   int getPoseAnimationFrame();

   void setPoseAnimationFrame(int var1);

   int getIdlePoseAnimation();

   void setIdlePoseAnimation(int var1);

   int getIdleRotateLeft();

   void setIdleRotateLeft(int var1);

   int getIdleRotateRight();

   void setIdleRotateRight(int var1);

   int getWalkAnimation();

   void setWalkAnimation(int var1);

   int getWalkRotateLeft();

   void setWalkRotateLeft(int var1);

   int getWalkRotateRight();

   void setWalkRotateRight(int var1);

   int getWalkRotate180();

   void setWalkRotate180(int var1);

   int getRunAnimation();

   void setRunAnimation(int var1);

   void setAnimation(int var1);

   int getAnimationFrame();

   /** @deprecated */
   @Deprecated
   void setActionFrame(int var1);

   void setAnimationFrame(int var1);

   IterableHashTable<ActorSpotAnim> getSpotAnims();

   boolean hasSpotAnim(int var1);

   void createSpotAnim(int var1, int var2, int var3, int var4);

   void removeSpotAnim(int var1);

   void clearSpotAnims();

   /** @deprecated */
   @Deprecated
   int getGraphic();

   /** @deprecated */
   @Deprecated
   void setGraphic(int var1);

   /** @deprecated */
   @Deprecated
   int getGraphicHeight();

   /** @deprecated */
   @Deprecated
   void setGraphicHeight(int var1);

   /** @deprecated */
   @Deprecated
   int getSpotAnimFrame();

   /** @deprecated */
   @Deprecated
   void setSpotAnimFrame(int var1);

   Polygon getCanvasTilePoly();

   @Nullable
   Point getCanvasTextLocation(Graphics2D var1, String var2, int var3);

   Point getCanvasImageLocation(BufferedImage var1, int var2);

   Point getCanvasSpriteLocation(SpritePixels var1, int var2);

   Point getMinimapLocation();

   int getLogicalHeight();

   Shape getConvexHull();

   WorldArea getWorldArea();

   String getOverheadText();

   void setOverheadText(String var1);

   int getOverheadCycle();

   void setOverheadCycle(int var1);

   boolean isDead();

   void setDead(boolean var1);

   int getFootprintSize();

   int getAnimationHeightOffset();
}
