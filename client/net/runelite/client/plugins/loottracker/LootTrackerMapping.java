package net.runelite.client.plugins.loottracker;

import com.google.common.collect.ImmutableMap;

enum LootTrackerMapping {
   CLUE_SCROLL_BEGINNER("Clue scroll (beginner)", 23182),
   CLUE_SCROLL_EASY("Clue scroll (easy)", 2677),
   CLUE_SCROLL_MEDIUM("Clue scroll (medium)", 2801),
   CLUE_SCROLL_HARD("Clue scroll (hard)", 2722),
   CLUE_SCROLL_ELITE("Clue scroll (elite)", 12073),
   CLUE_SCROLL_MASTER("Clue scroll (master)", 19835);

   private final String name;
   private final int baseId;
   private static final ImmutableMap<String, Integer> MAPPINGS;

   static int map(int itemId, String name) {
      return (Integer)MAPPINGS.getOrDefault(name, itemId);
   }

   private LootTrackerMapping(String name, int baseId) {
      this.name = name;
      this.baseId = baseId;
   }

   static {
      ImmutableMap.Builder<String, Integer> map = ImmutableMap.builder();
      LootTrackerMapping[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         LootTrackerMapping mapping = var1[var3];
         map.put(mapping.name, mapping.baseId);
      }

      MAPPINGS = map.build();
   }
}
