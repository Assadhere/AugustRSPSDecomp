package net.runelite.client.plugins.tithefarm;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public enum TitheFarmPlantType {
   EMPTY("Empty", 27383, new int[]{27383}),
   GOLOVANOVA("Golovanova", 27393, new int[]{27384, 27385, 27386, 27387, 27388, 27389, 27390, 27391, 27392, 27393, 27394}),
   BOLOGANO("Bologano", 27404, new int[]{27395, 27396, 27397, 27398, 27399, 27400, 27401, 27402, 27403, 27404, 27405}),
   LOGAVANO("Logavano", 27415, new int[]{27406, 27407, 27408, 27409, 27410, 27411, 27412, 27413, 27414, 27415, 27416});

   private final String name;
   private final int baseId;
   private final int[] objectIds;
   private static final Map<Integer, TitheFarmPlantType> plantTypes;

   private TitheFarmPlantType(String name, int baseId, int... objectIds) {
      this.name = name;
      this.baseId = baseId;
      this.objectIds = objectIds;
   }

   public static TitheFarmPlantType getPlantType(int objectId) {
      return (TitheFarmPlantType)plantTypes.get(objectId);
   }

   public String getName() {
      return this.name;
   }

   public int getBaseId() {
      return this.baseId;
   }

   public int[] getObjectIds() {
      return this.objectIds;
   }

   static {
      ImmutableMap.Builder<Integer, TitheFarmPlantType> builder = new ImmutableMap.Builder();
      TitheFarmPlantType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         TitheFarmPlantType type = var1[var3];
         int[] var5 = type.getObjectIds();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int spotId = var5[var7];
            builder.put(spotId, type);
         }
      }

      plantTypes = builder.build();
   }
}
