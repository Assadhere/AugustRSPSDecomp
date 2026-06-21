package net.runelite.client.plugins.worldmap;

import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

class MapPoint extends WorldMapPoint {
   private final Type type;

   protected MapPoint(MapPointBuilder<?, ?> b) {
      super(b);
      this.type = b.type;
   }

   public static MapPointBuilder<?, ?> builder() {
      return new MapPointBuilderImpl();
   }

   public Type getType() {
      return this.type;
   }

   private static final class MapPointBuilderImpl extends MapPointBuilder<MapPoint, MapPointBuilderImpl> {
      protected MapPointBuilderImpl self() {
         return this;
      }

      public MapPoint build() {
         return new MapPoint(this);
      }
   }

   public abstract static class MapPointBuilder<C extends MapPoint, B extends MapPointBuilder<C, B>> extends WorldMapPoint.WorldMapPointBuilder<C, B> {
      private Type type;

      public B type(Type type) {
         this.type = type;
         return this.self();
      }

      protected abstract B self();

      public abstract C build();

      public String toString() {
         String var10000 = super.toString();
         return "MapPoint.MapPointBuilder(super=" + var10000 + ", type=" + String.valueOf(this.type) + ")";
      }
   }

   static enum Type {
      TELEPORT,
      RUNECRAFT_ALTAR,
      MINING_SITE,
      DUNGEON,
      HUNTER,
      FISHING,
      KOUREND_TASK,
      FARMING_PATCH,
      TRANSPORTATION,
      MINIGAME,
      FAIRY_RING,
      AGILITY_COURSE,
      AGILITY_SHORTCUT,
      QUEST,
      RARE_TREE,
      MOORING_POINT;
   }
}
