package net.runelite.api;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public enum WorldType {
   MEMBERS(1),
   PVP(4),
   BOUNTY(32),
   PVP_ARENA(64),
   SKILL_TOTAL(128),
   QUEST_SPEEDRUNNING(256),
   HIGH_RISK(1024),
   LAST_MAN_STANDING(16384),
   BETA_WORLD(65536),
   LEGACY_ONLY(4194304),
   EOC_ONLY(8388608),
   NOSAVE_MODE(33554432),
   TOURNAMENT_WORLD(67108864),
   FRESH_START_WORLD(134217728),
   DEADMAN(536870912),
   SEASONAL(1073741824);

   private final int mask;
   private static final EnumSet<WorldType> PVP_WORLD_TYPES = EnumSet.of(DEADMAN, PVP);

   public static EnumSet<WorldType> fromMask(int mask) {
      EnumSet<WorldType> types = EnumSet.noneOf(WorldType.class);
      WorldType[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WorldType type = var2[var4];
         if ((mask & type.mask) != 0) {
            types.add(type);
         }
      }

      return types;
   }

   public static int toMask(EnumSet<WorldType> types) {
      int mask = 0;

      WorldType type;
      for(Iterator var2 = types.iterator(); var2.hasNext(); mask |= type.mask) {
         type = (WorldType)var2.next();
      }

      return mask;
   }

   public static boolean isPvpWorld(Collection<WorldType> worldTypes) {
      Stream var10000 = worldTypes.stream();
      EnumSet var10001 = PVP_WORLD_TYPES;
      Objects.requireNonNull(var10001);
      return var10000.anyMatch(var10001::contains);
   }

   private WorldType(int mask) {
      this.mask = mask;
   }
}
