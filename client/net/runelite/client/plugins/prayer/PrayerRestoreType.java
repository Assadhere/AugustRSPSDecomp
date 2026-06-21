package net.runelite.client.plugins.prayer;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

enum PrayerRestoreType {
   RESTOREPOT(new int[]{3024, 3026, 3028, 3030, 24598, 24601, 24603, 24605}),
   PRAYERPOT(new int[]{2434, 139, 141, 143}),
   SANFEWPOT(new int[]{10925, 10927, 10929, 10931}),
   HOLYWRENCH(new int[]{9759, 9760, 13280, 13342, 6714, 13202});

   private static final Map<Integer, PrayerRestoreType> prayerRestores;
   private final int[] items;

   private PrayerRestoreType(int... items) {
      this.items = items;
   }

   static PrayerRestoreType getType(int itemId) {
      return (PrayerRestoreType)prayerRestores.get(itemId);
   }

   static {
      ImmutableMap.Builder<Integer, PrayerRestoreType> builder = new ImmutableMap.Builder();
      PrayerRestoreType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PrayerRestoreType prayerRestoreType = var1[var3];
         int[] var5 = prayerRestoreType.items;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int itemId = var5[var7];
            builder.put(itemId, prayerRestoreType);
         }
      }

      prayerRestores = builder.build();
   }
}
