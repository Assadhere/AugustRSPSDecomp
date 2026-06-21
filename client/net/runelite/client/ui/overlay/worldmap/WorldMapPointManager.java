package net.runelite.client.ui.overlay.worldmap;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import javax.inject.Singleton;

@Singleton
public class WorldMapPointManager {
   private final List<WorldMapPoint> worldMapPoints = new CopyOnWriteArrayList();

   public void add(WorldMapPoint worldMapPoint) {
      this.worldMapPoints.add(worldMapPoint);
   }

   public void remove(WorldMapPoint worldMapPoint) {
      this.worldMapPoints.remove(worldMapPoint);
   }

   public void removeIf(Predicate<WorldMapPoint> filter) {
      this.worldMapPoints.removeIf(filter);
   }

   List<WorldMapPoint> getWorldMapPoints() {
      return this.worldMapPoints;
   }
}
