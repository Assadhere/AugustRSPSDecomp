package net.runelite.api.coords;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.runelite.api.CollisionData;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.WorldView;

public class WorldArea {
   private int x;
   private int y;
   private int width;
   private int height;
   private int plane;

   public WorldArea(int x, int y, int width, int height, int plane) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.plane = plane;
   }

   public WorldArea(WorldPoint location, int width, int height) {
      this.x = location.getX();
      this.y = location.getY();
      this.plane = location.getPlane();
      this.width = width;
      this.height = height;
   }

   private Point getAxisDistances(WorldArea other) {
      Point p1 = this.getComparisonPoint(other);
      Point p2 = other.getComparisonPoint(this);
      return new Point(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
   }

   public int distanceTo(WorldArea other) {
      return this.getPlane() != other.getPlane() ? Integer.MAX_VALUE : this.distanceTo2D(other);
   }

   public int distanceTo(WorldPoint other) {
      return this.distanceTo(other.toWorldArea());
   }

   public int distanceTo2D(WorldArea other) {
      Point distances = this.getAxisDistances(other);
      return Math.max(distances.getX(), distances.getY());
   }

   public int distanceTo2D(WorldPoint other) {
      return this.distanceTo2D(other.toWorldArea());
   }

   public boolean contains(WorldPoint worldPoint) {
      return this.distanceTo(worldPoint) == 0;
   }

   public boolean contains2D(WorldPoint worldPoint) {
      return this.distanceTo2D(worldPoint) == 0;
   }

   public boolean isInMeleeDistance(WorldArea other) {
      if (other != null && this.getPlane() == other.getPlane()) {
         Point distances = this.getAxisDistances(other);
         return distances.getX() + distances.getY() == 1;
      } else {
         return false;
      }
   }

   public boolean isInMeleeDistance(WorldPoint other) {
      return this.isInMeleeDistance(other.toWorldArea());
   }

   public boolean intersectsWith(WorldArea other) {
      if (this.getPlane() != other.getPlane()) {
         return false;
      } else {
         Point distances = this.getAxisDistances(other);
         return distances.getX() + distances.getY() == 0;
      }
   }

   public boolean canTravelInDirection(WorldView wv, int dx, int dy) {
      return this.canTravelInDirection(wv, dx, dy, (x) -> {
         return true;
      });
   }

   public boolean canTravelInDirection(WorldView wv, int dx, int dy, Predicate<? super WorldPoint> extraCondition) {
      dx = Integer.signum(dx);
      dy = Integer.signum(dy);
      if (dx == 0 && dy == 0) {
         return true;
      } else {
         LocalPoint lp = LocalPoint.fromWorld(wv, this.x, this.y);
         int startX = lp.getSceneX() + dx;
         int startY = lp.getSceneY() + dy;
         int checkX = startX + (dx > 0 ? this.width - 1 : 0);
         int checkY = startY + (dy > 0 ? this.height - 1 : 0);
         int endX = startX + this.width - 1;
         int endY = startY + this.height - 1;
         int xFlags = 2359552;
         int yFlags = 2359552;
         int xyFlags = 2359552;
         int xWallFlagsSouth = 2359552;
         int xWallFlagsNorth = 2359552;
         int yWallFlagsWest = 2359552;
         int yWallFlagsEast = 2359552;
         if (dx < 0) {
            xFlags |= 8;
            xWallFlagsSouth |= 48;
            xWallFlagsNorth |= 6;
         }

         if (dx > 0) {
            xFlags |= 128;
            xWallFlagsSouth |= 96;
            xWallFlagsNorth |= 3;
         }

         if (dy < 0) {
            yFlags |= 2;
            yWallFlagsWest |= 129;
            yWallFlagsEast |= 12;
         }

         if (dy > 0) {
            yFlags |= 32;
            yWallFlagsWest |= 192;
            yWallFlagsEast |= 24;
         }

         if (dx < 0 && dy < 0) {
            xyFlags |= 4;
         }

         if (dx < 0 && dy > 0) {
            xyFlags |= 16;
         }

         if (dx > 0 && dy < 0) {
            xyFlags |= 1;
         }

         if (dx > 0 && dy > 0) {
            xyFlags |= 64;
         }

         CollisionData[] collisionData = wv.getCollisionMaps();
         if (collisionData == null) {
            return false;
         } else {
            int[][] collisionDataFlags = collisionData[this.plane].getFlags();
            int x;
            if (dx != 0) {
               x = startY;

               label171:
               while(true) {
                  if (x > endY) {
                     for(x = startY + 1; x <= endY; ++x) {
                        if ((collisionDataFlags[checkX][x] & xWallFlagsSouth) != 0) {
                           return false;
                        }
                     }

                     x = endY - 1;

                     while(true) {
                        if (x < startY) {
                           break label171;
                        }

                        if ((collisionDataFlags[checkX][x] & xWallFlagsNorth) != 0) {
                           return false;
                        }

                        --x;
                     }
                  }

                  if ((collisionDataFlags[checkX][x] & xFlags) != 0 || !extraCondition.test(WorldPoint.fromScene(wv, checkX, x, this.plane))) {
                     return false;
                  }

                  ++x;
               }
            }

            if (dy != 0) {
               x = startX;

               label145:
               while(true) {
                  if (x > endX) {
                     for(x = startX + 1; x <= endX; ++x) {
                        if ((collisionDataFlags[x][checkY] & yWallFlagsWest) != 0) {
                           return false;
                        }
                     }

                     x = endX - 1;

                     while(true) {
                        if (x < startX) {
                           break label145;
                        }

                        if ((collisionDataFlags[x][checkY] & yWallFlagsEast) != 0) {
                           return false;
                        }

                        --x;
                     }
                  }

                  if ((collisionDataFlags[x][checkY] & yFlags) != 0 || !extraCondition.test(WorldPoint.fromScene(wv, x, checkY, this.plane))) {
                     return false;
                  }

                  ++x;
               }
            }

            if (dx != 0 && dy != 0) {
               if ((collisionDataFlags[checkX][checkY] & xyFlags) != 0 || !extraCondition.test(WorldPoint.fromScene(wv, checkX, checkY, this.plane))) {
                  return false;
               }

               if (this.width == 1 && (collisionDataFlags[checkX][checkY - dy] & xFlags) != 0 && extraCondition.test(WorldPoint.fromScene(wv, checkX, startY, this.plane))) {
                  return false;
               }

               if (this.height == 1 && (collisionDataFlags[checkX - dx][checkY] & yFlags) != 0 && extraCondition.test(WorldPoint.fromScene(wv, startX, checkY, this.plane))) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private Point getComparisonPoint(WorldArea other) {
      int x;
      if (other.x <= this.x) {
         x = this.x;
      } else if (other.x >= this.x + this.width - 1) {
         x = this.x + this.width - 1;
      } else {
         x = other.x;
      }

      int y;
      if (other.y <= this.y) {
         y = this.y;
      } else if (other.y >= this.y + this.height - 1) {
         y = this.y + this.height - 1;
      } else {
         y = other.y;
      }

      return new Point(x, y);
   }

   public boolean hasLineOfSightTo(WorldView wv, WorldArea other) {
      if (this.plane != other.getPlane()) {
         return false;
      } else {
         List<WorldPoint> fromEdges = other.getVisibleCandidates(this);
         List<WorldPoint> toEdges = this.getVisibleCandidates(other);
         Tile[][][] tiles = wv.getScene().getTiles();
         Iterator var6 = fromEdges.iterator();

         while(var6.hasNext()) {
            WorldPoint fromPoint = (WorldPoint)var6.next();
            Iterator var8 = toEdges.iterator();

            while(var8.hasNext()) {
               WorldPoint toPoint = (WorldPoint)var8.next();
               LocalPoint fromLp = LocalPoint.fromWorld(wv, fromPoint);
               LocalPoint toLp = LocalPoint.fromWorld(wv, toPoint);
               if (fromLp != null && toLp != null) {
                  Tile sourceTile = tiles[this.plane][fromLp.getSceneX()][fromLp.getSceneY()];
                  Tile targetTile = tiles[other.getPlane()][toLp.getSceneX()][toLp.getSceneY()];
                  if (sourceTile != null && targetTile != null && hasLineOfSightTo(wv, sourceTile, targetTile)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private static boolean hasLineOfSightTo(WorldView wv, Tile from, Tile to) {
      if (from.getPlane() != to.getPlane()) {
         return false;
      } else {
         CollisionData[] collisionData = wv.getCollisionMaps();
         if (collisionData == null) {
            return false;
         } else {
            int z = from.getPlane();
            int[][] collisionDataFlags = collisionData[z].getFlags();
            Point p1 = from.getSceneLocation();
            Point p2 = to.getSceneLocation();
            if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) {
               return true;
            } else {
               int dx = p2.getX() - p1.getX();
               int dy = p2.getY() - p1.getY();
               int dxAbs = Math.abs(dx);
               int dyAbs = Math.abs(dy);
               int xFlags = 131072;
               int yFlags = 131072;
               if (dx < 0) {
                  xFlags |= 4096;
               } else {
                  xFlags |= 65536;
               }

               if (dy < 0) {
                  yFlags |= 1024;
               } else {
                  yFlags |= 16384;
               }

               int x;
               int yBig;
               int slope;
               int direction;
               int y;
               int nextY;
               if (dxAbs > dyAbs) {
                  x = p1.getX();
                  yBig = p1.getY() << 16;
                  slope = (dy << 16) / dxAbs;
                  yBig += 32768;
                  if (dy < 0) {
                     --yBig;
                  }

                  direction = dx < 0 ? -1 : 1;

                  while(x != p2.getX()) {
                     x += direction;
                     y = yBig >>> 16;
                     if ((collisionDataFlags[x][y] & xFlags) != 0) {
                        return false;
                     }

                     yBig += slope;
                     nextY = yBig >>> 16;
                     if (nextY != y && (collisionDataFlags[x][nextY] & yFlags) != 0) {
                        return false;
                     }
                  }
               } else {
                  x = p1.getY();
                  yBig = p1.getX() << 16;
                  slope = (dx << 16) / dyAbs;
                  yBig += 32768;
                  if (dx < 0) {
                     --yBig;
                  }

                  direction = dy < 0 ? -1 : 1;

                  while(x != p2.getY()) {
                     x += direction;
                     y = yBig >>> 16;
                     if ((collisionDataFlags[y][x] & yFlags) != 0) {
                        return false;
                     }

                     yBig += slope;
                     nextY = yBig >>> 16;
                     if (nextY != y && (collisionDataFlags[nextY][x] & xFlags) != 0) {
                        return false;
                     }
                  }
               }

               return true;
            }
         }
      }
   }

   public boolean hasLineOfSightTo(WorldView wv, WorldPoint other) {
      return this.hasLineOfSightTo(wv, other.toWorldArea());
   }

   private List<WorldPoint> getVisibleCandidates(WorldArea other) {
      Point compPoint = this.getComparisonPoint(other);
      Comparator<WorldPoint> byDistance = Comparator.comparingInt((p) -> {
         return p.distanceTo(new WorldPoint(compPoint.getX(), compPoint.getY(), this.getPlane()));
      });
      return (List)other.toWorldPointList().stream().filter((p) -> {
         return isEdgePoint(other, p);
      }).filter((p) -> {
         return this.isVisibleCandidate(other, p);
      }).sorted(byDistance).collect(Collectors.toList());
   }

   private static boolean isEdgePoint(WorldArea wa, WorldPoint p) {
      return p.getX() == wa.getX() || p.getX() == wa.getX() + wa.getWidth() - 1 || p.getY() == wa.getY() || p.getY() == wa.getY() + wa.getHeight() - 1;
   }

   private boolean isVisibleCandidate(WorldArea other, WorldPoint p) {
      if (this.intersectsWith(other)) {
         return false;
      } else if (this.getX() + this.getWidth() - 1 > other.getX() + other.getWidth() - 1) {
         if (this.getY() < other.getY()) {
            return p.getX() == other.getX() + other.getWidth() - 1 || p.getY() == other.getY();
         } else if (this.getY() + this.getHeight() - 1 <= other.getY() + other.getHeight() - 1) {
            return p.getX() == other.getX() + other.getWidth() - 1;
         } else {
            return p.getX() == other.getX() + other.getWidth() - 1 || p.getY() == other.getY() + other.getHeight() - 1;
         }
      } else if (this.getX() >= other.getX()) {
         if (this.getY() > other.getY() + other.getHeight() - 1) {
            return p.getY() == other.getY() + other.getHeight() - 1;
         } else if (this.getY() < other.getY()) {
            return p.getY() == other.getY();
         } else {
            return false;
         }
      } else if (this.getY() < other.getY()) {
         return p.getX() == other.getX() || p.getY() == other.getY();
      } else if (this.getY() + this.getHeight() - 1 <= other.getY() + other.getHeight() - 1) {
         return p.getX() == other.getX();
      } else {
         return p.getX() == other.getX() || p.getY() == other.getY() + other.getHeight() - 1;
      }
   }

   public WorldPoint toWorldPoint() {
      return new WorldPoint(this.x, this.y, this.plane);
   }

   public List<WorldPoint> toWorldPointList() {
      List<WorldPoint> list = new ArrayList(this.width * this.height);

      for(int x = 0; x < this.width; ++x) {
         for(int y = 0; y < this.height; ++y) {
            list.add(new WorldPoint(this.getX() + x, this.getY() + y, this.getPlane()));
         }
      }

      return list;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getPlane() {
      return this.plane;
   }
}
