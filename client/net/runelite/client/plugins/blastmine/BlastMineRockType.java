package net.runelite.client.plugins.blastmine;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public enum BlastMineRockType {
   NORMAL(new int[]{28579, 28580}),
   CHISELED(new int[]{28581, 28582}),
   LOADED(new int[]{28583, 28584}),
   LIT(new int[]{28585, 28586}),
   EXPLODED(new int[]{28587, 28588});

   private static final Map<Integer, BlastMineRockType> rockTypes;
   private final int[] objectIds;

   private BlastMineRockType(int... objectIds) {
      this.objectIds = objectIds;
   }

   public static BlastMineRockType getRockType(int objectId) {
      return (BlastMineRockType)rockTypes.get(objectId);
   }

   public int[] getObjectIds() {
      return this.objectIds;
   }

   static {
      ImmutableMap.Builder<Integer, BlastMineRockType> builder = new ImmutableMap.Builder();
      BlastMineRockType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlastMineRockType type = var1[var3];
         int[] var5 = type.getObjectIds();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int spotId = var5[var7];
            builder.put(spotId, type);
         }
      }

      rockTypes = builder.build();
   }
}
