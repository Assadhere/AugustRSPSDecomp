package net.runelite.client.plugins.mta.telekinetic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.WallObject;
import net.runelite.api.coords.Angle;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.mta.MTAConfig;
import net.runelite.client.plugins.mta.MTARoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelekineticRoom extends MTARoom {
   private static final Logger log = LoggerFactory.getLogger(TelekineticRoom.class);
   private static final int MAZE_GUARDIAN_MOVING = 6778;
   private static final int TELEKINETIC_WALL = 10755;
   private static final int TELEKINETIC_FINISH = 23672;
   private final Client client;
   private final List<WallObject> telekineticWalls = new ArrayList();
   private Stack<Direction> moves = new Stack();
   private LocalPoint destination;
   private WorldPoint location;
   private WorldPoint finishLocation;
   private Rectangle bounds;
   private NPC guardian;
   private int numMazeWalls;

   @Inject
   private TelekineticRoom(MTAConfig config, Client client) {
      super(config);
      this.client = client;
   }

   public void resetRoom() {
      this.finishLocation = null;
      this.telekineticWalls.clear();
   }

   @Subscribe
   public void onWallObjectSpawned(WallObjectSpawned event) {
      WallObject wall = event.getWallObject();
      if (wall.getId() == 10755) {
         this.telekineticWalls.add(wall);
      }
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOADING) {
         this.resetRoom();
      }

   }

   @Subscribe
   public void onGroundObjectSpawned(GroundObjectSpawned event) {
      GroundObject object = event.getGroundObject();
      if (object.getId() == 23672) {
         this.finishLocation = object.getWorldLocation();
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      if (this.inside() && this.config.telekinetic()) {
         if (this.telekineticWalls.size() != this.numMazeWalls) {
            this.bounds = this.getBounds((WallObject[])this.telekineticWalls.toArray(new WallObject[0]));
            this.numMazeWalls = this.telekineticWalls.size();
            this.client.clearHintArrow();
         } else if (this.guardian != null) {
            WorldPoint current;
            if (this.guardian.getId() == 6778) {
               this.destination = this.getGuardianDestination();
               current = WorldPoint.fromLocal(this.client, this.destination);
            } else {
               this.destination = null;
               current = this.guardian.getWorldLocation();
            }

            if (current.equals(this.location)) {
               return;
            }

            log.debug("Updating guarding location {} -> {}", this.location, current);
            this.location = current;
            if (this.location.equals(this.finishLocation)) {
               this.client.clearHintArrow();
            } else {
               log.debug("Rebuilding moves due to guardian move");
               this.moves = this.build();
            }
         } else {
            this.client.clearHintArrow();
            this.moves.clear();
         }

      } else {
         this.numMazeWalls = 0;
         this.moves.clear();
      }
   }

   @Subscribe
   public void onNpcSpawned(NpcSpawned event) {
      NPC npc = event.getNpc();
      if (npc.getId() == 6777 || npc.getId() == 6778) {
         this.guardian = npc;
      }

   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned event) {
      NPC npc = event.getNpc();
      if (npc == this.guardian) {
         this.guardian = null;
      }

   }

   public boolean inside() {
      return this.client.getWidget(198, 0) != null;
   }

   public void under(Graphics2D graphics2D) {
      if (this.inside() && this.numMazeWalls > 0 && this.guardian != null) {
         if (this.destination != null) {
            graphics2D.setColor(Color.ORANGE);
            this.renderLocalPoint(graphics2D, this.destination);
         }

         if (!this.moves.isEmpty()) {
            if (this.guardian.getId() == 6778) {
               graphics2D.setColor(Color.YELLOW);
            } else if (this.moves.peek() == this.getPosition()) {
               graphics2D.setColor(Color.GREEN);
            } else {
               graphics2D.setColor(Color.RED);
            }

            Polygon tile = Perspective.getCanvasTilePoly(this.client, this.guardian.getLocalLocation());
            if (tile != null) {
               graphics2D.drawPolygon(tile);
            }

            WorldPoint optimal = this.optimal(0);
            if (optimal != null) {
               this.client.setHintArrow(optimal);
               this.renderWorldPoint(graphics2D, optimal);
            }
         }

         if (this.moves.size() >= 2) {
            WorldPoint optimal = this.optimal(1);
            if (optimal != null) {
               graphics2D.setColor(Color.CYAN);
               this.renderWorldPoint(graphics2D, optimal);
            }
         }
      }

   }

   private WorldPoint optimal(int index) {
      WorldPoint current = this.client.getLocalPlayer().getWorldLocation();
      Direction next = (Direction)this.moves.get(this.moves.size() - 1 - index);
      WorldArea areaNext = this.getIndicatorLine(next);
      WorldPoint nearestNext = this.nearest(areaNext, current);
      if (this.moves.size() <= 1 + index) {
         return nearestNext;
      } else {
         Direction after = (Direction)this.moves.get(this.moves.size() - 2 - index);
         WorldArea areaAfter = this.getIndicatorLine(after);
         WorldPoint nearestAfter = this.nearest(areaAfter, nearestNext);
         return this.nearest(areaNext, nearestAfter);
      }
   }

   private static int manhattan(WorldPoint point1, WorldPoint point2) {
      return Math.abs(point1.getX() - point2.getX()) + Math.abs(point2.getY() - point1.getY());
   }

   private WorldPoint nearest(WorldArea area, WorldPoint worldPoint) {
      int dist = Integer.MAX_VALUE;
      WorldPoint nearest = null;
      Iterator var5 = area.toWorldPointList().iterator();

      while(true) {
         WorldPoint areaPoint;
         int currDist;
         do {
            if (!var5.hasNext()) {
               return nearest;
            }

            areaPoint = (WorldPoint)var5.next();
            currDist = manhattan(areaPoint, worldPoint);
         } while(nearest != null && dist <= currDist);

         nearest = areaPoint;
         dist = currDist;
      }
   }

   private void renderWorldPoint(Graphics2D graphics, WorldPoint worldPoint) {
      this.renderLocalPoint(graphics, LocalPoint.fromWorld(this.client, worldPoint));
   }

   private void renderLocalPoint(Graphics2D graphics, LocalPoint local) {
      if (local != null) {
         Polygon canvasTilePoly = Perspective.getCanvasTilePoly(this.client, local);
         if (canvasTilePoly != null) {
            graphics.drawPolygon(canvasTilePoly);
         }
      }

   }

   private Stack<Direction> build() {
      if (this.guardian.getId() == 6778) {
         WorldPoint converted = WorldPoint.fromLocal(this.client, this.getGuardianDestination());
         return this.build(converted);
      } else {
         return this.build(this.guardian.getWorldLocation());
      }
   }

   private LocalPoint getGuardianDestination() {
      Angle angle = new Angle(this.guardian.getOrientation());
      Direction facing = angle.getNearestDirection();
      return this.neighbour(this.guardian.getLocalLocation(), facing);
   }

   private Stack<Direction> build(WorldPoint start) {
      Queue<WorldPoint> visit = new LinkedList();
      Set<WorldPoint> closed = new HashSet();
      Map<WorldPoint, Integer> scores = new HashMap();
      Map<WorldPoint, WorldPoint> edges = new HashMap();
      scores.put(start, 0);
      visit.add(start);

      while(!visit.isEmpty()) {
         WorldPoint next = (WorldPoint)visit.poll();
         closed.add(next);
         LocalPoint localNext = LocalPoint.fromWorld(this.client, next);
         LocalPoint[] neighbours = this.neighbours(localNext);
         LocalPoint[] var9 = neighbours;
         int var10 = neighbours.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            LocalPoint neighbour = var9[var11];
            if (neighbour != null) {
               WorldPoint nghbWorld = WorldPoint.fromLocal(this.client, neighbour);
               if (!nghbWorld.equals(next) && !closed.contains(nghbWorld)) {
                  int score = (Integer)scores.get(next) + 1;
                  if (!scores.containsKey(nghbWorld) || (Integer)scores.get(nghbWorld) > score) {
                     scores.put(nghbWorld, score);
                     edges.put(nghbWorld, next);
                     visit.add(nghbWorld);
                  }
               }
            }
         }
      }

      return this.build(edges, this.finishLocation);
   }

   private Stack<Direction> build(Map<WorldPoint, WorldPoint> edges, WorldPoint finish) {
      Stack<Direction> path = new Stack();

      WorldPoint next;
      for(WorldPoint current = finish; edges.containsKey(current); current = next) {
         next = (WorldPoint)edges.get(current);
         if (next.getX() > current.getX()) {
            path.add(Direction.WEST);
         } else if (next.getX() < current.getX()) {
            path.add(Direction.EAST);
         } else if (next.getY() > current.getY()) {
            path.add(Direction.SOUTH);
         } else {
            path.add(Direction.NORTH);
         }
      }

      return path;
   }

   private LocalPoint[] neighbours(LocalPoint point) {
      return new LocalPoint[]{this.neighbour(point, Direction.NORTH), this.neighbour(point, Direction.SOUTH), this.neighbour(point, Direction.EAST), this.neighbour(point, Direction.WEST)};
   }

   private LocalPoint neighbour(LocalPoint point, Direction direction) {
      WorldPoint worldPoint = WorldPoint.fromLocal(this.client, point);
      WorldArea area = worldPoint.toWorldArea();
      byte dx;
      byte dy;
      switch (direction) {
         case NORTH:
            dx = 0;
            dy = 1;
            break;
         case SOUTH:
            dx = 0;
            dy = -1;
            break;
         case EAST:
            dx = 1;
            dy = 0;
            break;
         case WEST:
            dx = -1;
            dy = 0;
            break;
         default:
            throw new IllegalStateException();
      }

      while(area.canTravelInDirection(this.client.getTopLevelWorldView(), dx, dy)) {
         worldPoint = area.toWorldPoint().dx(dx).dy(dy);
         area = worldPoint.toWorldArea();
      }

      return LocalPoint.fromWorld(this.client, worldPoint);
   }

   private Rectangle getBounds(WallObject[] walls) {
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      WallObject[] var6 = walls;
      int var7 = walls.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         WallObject wall = var6[var8];
         WorldPoint point = wall.getWorldLocation();
         minX = Math.min(minX, point.getX());
         minY = Math.min(minY, point.getY());
         maxX = Math.max(maxX, point.getX());
         maxY = Math.max(maxY, point.getY());
      }

      return new Rectangle(minX, minY, maxX - minX, maxY - minY);
   }

   private Direction getPosition() {
      WorldPoint mine = this.client.getLocalPlayer().getWorldLocation();
      if ((double)mine.getY() >= this.bounds.getMaxY() && (double)mine.getX() < this.bounds.getMaxX() && (double)mine.getX() > this.bounds.getX()) {
         return Direction.NORTH;
      } else if ((double)mine.getY() <= this.bounds.getY() && (double)mine.getX() < this.bounds.getMaxX() && (double)mine.getX() > this.bounds.getX()) {
         return Direction.SOUTH;
      } else if ((double)mine.getX() >= this.bounds.getMaxX() && (double)mine.getY() < this.bounds.getMaxY() && (double)mine.getY() > this.bounds.getY()) {
         return Direction.EAST;
      } else {
         return (double)mine.getX() <= this.bounds.getX() && (double)mine.getY() < this.bounds.getMaxY() && (double)mine.getY() > this.bounds.getY() ? Direction.WEST : null;
      }
   }

   private WorldArea getIndicatorLine(Direction direction) {
      switch (direction) {
         case NORTH:
            return new WorldArea(this.bounds.x + 1, (int)this.bounds.getMaxY(), this.bounds.width - 1, 1, 0);
         case SOUTH:
            return new WorldArea(this.bounds.x + 1, this.bounds.y, this.bounds.width - 1, 1, 0);
         case EAST:
            return new WorldArea((int)this.bounds.getMaxX(), this.bounds.y + 1, 1, this.bounds.height - 1, 0);
         case WEST:
            return new WorldArea(this.bounds.x, this.bounds.y + 1, 1, this.bounds.height - 1, 0);
         default:
            return null;
      }
   }
}
