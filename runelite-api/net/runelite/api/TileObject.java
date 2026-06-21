package net.runelite.api;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public interface TileObject {
   int HASH_PLANE_SHIFT = 14;

   long getHash();

   int getX();

   int getY();

   int getZ();

   int getPlane();

   WorldView getWorldView();

   int getId();

   @Nonnull
   WorldPoint getWorldLocation();

   @Nonnull
   LocalPoint getLocalLocation();

   @Nullable
   Point getCanvasLocation();

   @Nullable
   Point getCanvasLocation(int var1);

   @Nullable
   Polygon getCanvasTilePoly();

   @Nullable
   Point getCanvasTextLocation(Graphics2D var1, String var2, int var3);

   @Nullable
   Point getMinimapLocation();

   @Nullable
   Shape getClickbox();

   @Nullable
   String getOpOverride(int var1);

   boolean isOpShown(int var1);
}
