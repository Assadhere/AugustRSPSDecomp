package net.runelite.client.plugins.herbiboars;

import net.runelite.api.coords.WorldPoint;

enum HerbiboarStart {
   MIDDLE(new WorldPoint(3686, 3870, 0)),
   LEPRECHAUN(new WorldPoint(3705, 3830, 0)),
   CAMP_ENTRANCE(new WorldPoint(3704, 3810, 0)),
   GHOST_MUSHROOM(new WorldPoint(3695, 3800, 0)),
   DRIFTWOOD(new WorldPoint(3751, 3850, 0));

   private final WorldPoint location;

   static HerbiboarStart from(WorldPoint location) {
      HerbiboarStart[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         HerbiboarStart start = var1[var3];
         if (start.getLocation().equals(location)) {
            return start;
         }
      }

      return null;
   }

   public WorldPoint getLocation() {
      return this.location;
   }

   private HerbiboarStart(WorldPoint location) {
      this.location = location;
   }
}
