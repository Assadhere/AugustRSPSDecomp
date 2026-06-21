package net.runelite.client.plugins.itemstats.potions;

import com.google.common.collect.ImmutableMap;
import java.time.Duration;
import java.util.Map;

public enum PotionDuration {
   ANTIPOISON(Duration.ofSeconds(90L), new int[]{2446, 175, 177, 179}),
   SUPERANTIPOISON(Duration.ofMinutes(6L), new int[]{2448, 181, 183, 185}),
   ANTIDOTE_P(Duration.ofMinutes(9L), new int[]{5943, 5945, 5947, 5949}),
   ANTIDOTE_PP(Duration.ofMinutes(12L), new int[]{5952, 5954, 5956, 5958}),
   ANTIVENOM(new PotionDurationRange[]{new PotionDurationRange("Anti-venom", Duration.ofSeconds(18L), Duration.ofSeconds(36L)), new PotionDurationRange("Anti-poison", Duration.ofMinutes(12L))}, new int[]{12905, 12907, 12909, 12911}),
   ANTIVENOM_P(new PotionDurationRange[]{new PotionDurationRange("Anti-venom", Duration.ofMinutes(3L)), new PotionDurationRange("Anti-poison", Duration.ofMinutes(15L))}, new int[]{12913, 12915, 12917, 12919}),
   EXTENDED_ANTIVENOM_P(new PotionDurationRange[]{new PotionDurationRange("Anti-venom", Duration.ofMinutes(6L)), new PotionDurationRange("Anti-poison", Duration.ofMinutes(17L))}, new int[]{29824, 29827, 29830, 29833}),
   ANTIFIRE(Duration.ofMinutes(6L), new int[]{2452, 2454, 2456, 2458}),
   EXTENDED_ANTIFIRE(Duration.ofMinutes(12L), new int[]{11951, 11953, 11955, 11957}),
   SUPER_ANTIFIRE(Duration.ofMinutes(3L), new int[]{21978, 21981, 21984, 21987}),
   EXTENDED_SUPER_ANTIFIRE(Duration.ofMinutes(6L), new int[]{22209, 22212, 22215, 22218}),
   ANTIPOISON_MIX(Duration.ofSeconds(90L), new int[]{11435, 11433}),
   ANTIPOISON_SUPERMIX(Duration.ofMinutes(6L), new int[]{11475, 11473}),
   ANTIDOTE_PLUS_MIX(Duration.ofMinutes(9L), new int[]{11503, 11501}),
   EXTENDED_ANTIFIRE_MIX(Duration.ofMinutes(12L), new int[]{11962, 11960}),
   SUPER_ANTIFIRE_MIX(Duration.ofMinutes(3L), new int[]{21997, 21994}),
   EXTENDED_SUPER_ANTIFIRE_MIX(Duration.ofMinutes(6L), new int[]{22224, 22221});

   private final PotionDurationRange[] durationRanges;
   private final int[] itemIds;
   private static final Map<Integer, PotionDuration> potions;

   private PotionDuration(Duration duration, int... itemIds) {
      PotionDurationRange[] ranges = new PotionDurationRange[]{new PotionDurationRange("", duration)};
      this.durationRanges = ranges;
      this.itemIds = itemIds;
   }

   private PotionDuration(PotionDurationRange[] durationRanges, int... itemIds) {
      this.durationRanges = durationRanges;
      this.itemIds = itemIds;
   }

   public static PotionDuration get(int id) {
      return (PotionDuration)potions.get(id);
   }

   public PotionDurationRange[] getDurationRanges() {
      return this.durationRanges;
   }

   static {
      ImmutableMap.Builder<Integer, PotionDuration> builder = new ImmutableMap.Builder();
      PotionDuration[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PotionDuration potion = var1[var3];
         int[] var5 = potion.itemIds;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int id = var5[var7];
            builder.put(id, potion);
         }
      }

      potions = builder.build();
   }

   public static class PotionDurationRange {
      private final String potionName;
      private final Duration lowestDuration;
      private final Duration highestDuration;

      public PotionDurationRange(String potionName, Duration duration) {
         this.potionName = potionName;
         this.lowestDuration = this.highestDuration = duration;
      }

      public PotionDurationRange(String potionName, Duration lowestDuration, Duration highestDuration) {
         this.potionName = potionName;
         this.lowestDuration = lowestDuration;
         this.highestDuration = highestDuration;
      }

      public String getPotionName() {
         return this.potionName;
      }

      public Duration getLowestDuration() {
         return this.lowestDuration;
      }

      public Duration getHighestDuration() {
         return this.highestDuration;
      }
   }
}
