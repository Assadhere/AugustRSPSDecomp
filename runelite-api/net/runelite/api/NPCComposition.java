package net.runelite.api;

import javax.annotation.Nullable;

public interface NPCComposition extends ParamHolder {
   int STAT_ATTACK = 0;
   int STAT_DEFENCE = 1;
   int STAT_STRENGTH = 2;
   int STAT_HITPOINTS = 3;
   int STAT_RANGED = 4;
   int STAT_MAGIC = 5;

   String getName();

   int[] getModels();

   @Nullable
   int[] getChatheadModels();

   EntityOps getOps();

   String[] getActions();

   boolean isInteractible();

   boolean isMinimapVisible();

   int getId();

   int getCombatLevel();

   int[] getConfigs();

   NPCComposition transform();

   int getSize();

   boolean isFollower();

   @Nullable
   short[] getColorToReplace();

   @Nullable
   short[] getColorToReplaceWith();

   int getWidthScale();

   int getHeightScale();

   int getFootprintSize();

   int[] getStats();
}
