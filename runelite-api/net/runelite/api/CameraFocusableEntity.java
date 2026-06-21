package net.runelite.api;

import net.runelite.api.coords.LocalPoint;

public interface CameraFocusableEntity {
   WorldView getWorldView();

   LocalPoint getCameraFocus();
}
