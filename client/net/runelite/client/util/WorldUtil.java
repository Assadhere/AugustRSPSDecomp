package net.runelite.client.util;

import java.util.EnumSet;
import java.util.Iterator;
import net.runelite.api.WorldType;

public class WorldUtil {
   public static EnumSet<WorldType> toWorldTypes(EnumSet<net.runelite.http.api.worlds.WorldType> apiTypes) {
      EnumSet<WorldType> types = EnumSet.noneOf(WorldType.class);
      Iterator var2 = apiTypes.iterator();

      while(var2.hasNext()) {
         net.runelite.http.api.worlds.WorldType apiType = (net.runelite.http.api.worlds.WorldType)var2.next();
         types.add(fromApiWorldType(apiType));
      }

      return types;
   }

   private static WorldType fromApiWorldType(net.runelite.http.api.worlds.WorldType apiType) {
      switch (apiType) {
         case MEMBERS:
            return WorldType.MEMBERS;
         case PVP:
            return WorldType.PVP;
         case BOUNTY:
            return WorldType.BOUNTY;
         case PVP_ARENA:
            return WorldType.PVP_ARENA;
         case SKILL_TOTAL:
            return WorldType.SKILL_TOTAL;
         case QUEST_SPEEDRUNNING:
            return WorldType.QUEST_SPEEDRUNNING;
         case HIGH_RISK:
            return WorldType.HIGH_RISK;
         case LAST_MAN_STANDING:
            return WorldType.LAST_MAN_STANDING;
         case BETA_WORLD:
            return WorldType.BETA_WORLD;
         case LEGACY_ONLY:
            return WorldType.LEGACY_ONLY;
         case EOC_ONLY:
            return WorldType.EOC_ONLY;
         case NOSAVE_MODE:
            return WorldType.NOSAVE_MODE;
         case TOURNAMENT:
            return WorldType.TOURNAMENT_WORLD;
         case FRESH_START_WORLD:
            return WorldType.FRESH_START_WORLD;
         case DEADMAN:
            return WorldType.DEADMAN;
         case SEASONAL:
            return WorldType.SEASONAL;
         default:
            throw new IllegalArgumentException("unknown world type " + String.valueOf(apiType));
      }
   }
}
