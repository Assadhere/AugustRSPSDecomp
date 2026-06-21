package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.coords.LocalPoint;

public interface AmbientSoundEffect {
   int getSoundEffectId();

   @Nullable
   int[] getBackgroundSoundEffectIds();

   int getPlane();

   LocalPoint getMinPosition();

   LocalPoint getMaxPosition();
}
