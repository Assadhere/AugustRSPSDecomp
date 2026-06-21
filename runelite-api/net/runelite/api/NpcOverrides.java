package net.runelite.api;

import javax.annotation.Nullable;

public interface NpcOverrides {
   @Nullable
   int[] getModelIds();

   @Nullable
   short[] getColorToReplaceWith();

   @Nullable
   short[] getTextureToReplaceWith();

   boolean useLocalPlayer();
}
