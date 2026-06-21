package net.runelite.client.plugins.worldhopper;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import net.runelite.http.api.worlds.WorldType;

enum WorldTypeFilter {
   NORMAL {
      boolean matches(Set<WorldType> types) {
         EnumSet<WorldType> normal = EnumSet.of(WorldType.MEMBERS, WorldType.SKILL_TOTAL, WorldType.LAST_MAN_STANDING);
         EnumSet<WorldType> inverse = EnumSet.complementOf(normal);
         return Sets.intersection(types, inverse).isEmpty();
      }
   },
   DEADMAN {
      boolean matches(Set<WorldType> types) {
         return types.contains(WorldType.DEADMAN);
      }
   },
   SEASONAL {
      boolean matches(Set<WorldType> types) {
         return types.contains(WorldType.SEASONAL) || types.contains(WorldType.TOURNAMENT);
      }
   },
   QUEST_SPEEDRUNNING {
      boolean matches(Set<WorldType> types) {
         return types.contains(WorldType.QUEST_SPEEDRUNNING);
      }
   },
   FRESH_START_WORLD {
      boolean matches(Set<WorldType> types) {
         return types.contains(WorldType.FRESH_START_WORLD);
      }
   },
   PVP {
      boolean matches(Set<WorldType> types) {
         return types.contains(WorldType.PVP);
      }
   },
   SKILL_TOTAL {
      boolean matches(Set<WorldType> types) {
         return types.contains(WorldType.SKILL_TOTAL);
      }
   },
   HIGH_RISK {
      boolean matches(Set<WorldType> types) {
         return types.contains(WorldType.HIGH_RISK) && !types.contains(WorldType.PVP);
      }
   },
   BOUNTY_HUNTER {
      boolean matches(Set<WorldType> types) {
         return types.contains(WorldType.BOUNTY);
      }
   };

   abstract boolean matches(Set<WorldType> var1);
}
