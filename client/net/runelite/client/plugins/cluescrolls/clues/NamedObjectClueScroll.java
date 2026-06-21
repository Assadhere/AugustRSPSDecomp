package net.runelite.client.plugins.cluescrolls.clues;

import javax.annotation.Nullable;

public interface NamedObjectClueScroll {
   String[] getObjectNames();

   @Nullable
   int[] getObjectRegions();
}
