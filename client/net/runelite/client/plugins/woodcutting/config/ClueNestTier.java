package net.runelite.client.plugins.woodcutting.config;

import com.google.common.collect.ImmutableMap;

public enum ClueNestTier {
   BEGINNER,
   EASY,
   MEDIUM,
   HARD,
   ELITE,
   DISABLED;

   private static final ImmutableMap<Integer, ClueNestTier> CLUE_NEST_ID_TO_TIER = (new ImmutableMap.Builder()).put(19718, ELITE).put(19716, HARD).put(19714, MEDIUM).put(19712, EASY).put(23127, BEGINNER).build();

   public static ClueNestTier getTierFromItem(int itemId) {
      return (ClueNestTier)CLUE_NEST_ID_TO_TIER.get(itemId);
   }
}
