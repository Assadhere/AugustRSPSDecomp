package net.runelite.api;

import net.runelite.api.coords.LocalPoint;

public interface WorldEntity extends CameraFocusableEntity {
   int OWNER_TYPE_NOT_PLAYER = 0;
   int OWNER_TYPE_OTHER_PLAYER = 1;
   int OWNER_TYPE_SELF_PLAYER = 2;

   WorldView getWorldView();

   LocalPoint getLocalLocation();

   int getOrientation();

   LocalPoint getTargetLocation();

   int getTargetOrientation();

   LocalPoint transformToMainWorld(LocalPoint var1);

   boolean isHiddenForOverlap();

   WorldEntityConfig getConfig();

   int getOwnerType();
}
