package net.runelite.api;

import net.runelite.api.coords.LocalPoint;

public abstract class RuneLiteObjectController {
   private int x;
   private int y;
   private int z;
   private int worldView = -1;
   private int level;
   private int radius = 60;
   private boolean drawFrontTilesFirst = false;
   private int orientation = 0;

   public void setLocation(LocalPoint point, int level) {
      this.setX(point.getX());
      this.setY(point.getY());
      this.setWorldView(point.getWorldView());
      this.setLevel(level);
   }

   public LocalPoint getLocation() {
      return new LocalPoint(this.x, this.y, this.worldView);
   }

   public void tick(int ticksSinceLastFrame) {
   }

   public abstract Model getModel();

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public int getWorldView() {
      return this.worldView;
   }

   public int getLevel() {
      return this.level;
   }

   public int getRadius() {
      return this.radius;
   }

   public boolean isDrawFrontTilesFirst() {
      return this.drawFrontTilesFirst;
   }

   public int getOrientation() {
      return this.orientation;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public void setZ(int z) {
      this.z = z;
   }

   public void setWorldView(int worldView) {
      this.worldView = worldView;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void setRadius(int radius) {
      this.radius = radius;
   }

   public void setDrawFrontTilesFirst(boolean drawFrontTilesFirst) {
      this.drawFrontTilesFirst = drawFrontTilesFirst;
   }

   public void setOrientation(int orientation) {
      this.orientation = orientation;
   }
}
