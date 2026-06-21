package net.runelite.api;

import javax.annotation.Nullable;

public interface NPC extends Actor {
   int getId();

   String getName();

   int getCombatLevel();

   int getIndex();

   NPCComposition getComposition();

   @Nullable
   NPCComposition getTransformedComposition();

   @Nullable
   NpcOverrides getModelOverrides();

   @Nullable
   NpcOverrides getChatheadOverrides();

   @Nullable
   int[] getOverheadArchiveIds();

   @Nullable
   short[] getOverheadSpriteIds();
}
