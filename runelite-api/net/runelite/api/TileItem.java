package net.runelite.api;

public interface TileItem extends Renderable {
   int OWNERSHIP_NONE = 0;
   int OWNERSHIP_SELF = 1;
   int OWNERSHIP_OTHER = 2;
   int OWNERSHIP_GROUP = 3;

   int getId();

   int getQuantity();

   int getVisibleTime();

   int getDespawnTime();

   int getOwnership();

   boolean isPrivate();
}
