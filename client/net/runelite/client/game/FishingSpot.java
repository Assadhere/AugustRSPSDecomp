package net.runelite.client.game;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public enum FishingSpot {
   SHRIMP("Shrimp, Anchovies, Sardine, Herring", "Anchovies", 317, new int[]{1514, 1517, 1518, 1521, 1523, 1524, 1525, 1528, 1530, 1544, 3913, 7155, 7459, 7462, 7467, 7469, 7947, 10513, 12778, 14038, 14040, 14041, 14524, 15066}),
   LOBSTER("Lobster, Swordfish, Tuna", "Lobster", 377, new int[]{1510, 1519, 1522, 2146, 3914, 5820, 7199, 7460, 7465, 7470, 7946, 9173, 9174, 10515, 10635, 12777, 14039, 15070, 15071, 15075, 15076, 15079, 15084, 15086}),
   SHARK("Shark, Bass", "Shark", 383, new int[]{1511, 1520, 3419, 3915, 4476, 4477, 5233, 5234, 5821, 7200, 7461, 7466, 8525, 8526, 8527, 9171, 9172, 10514, 12775, 12776, 14037, 14523, 15067, 15068, 15069, 15077, 15080, 15082, 15083, 15087}),
   MONKFISH("Monkfish", 7944, new int[]{4316}),
   SALMON("Salmon, Trout, Pike", "Salmon", 331, new int[]{394, 1506, 1507, 1508, 1509, 1513, 1515, 1516, 1526, 1527, 3417, 3418, 7463, 7464, 7468, 8524, 12774, 14036, 14525, 14526, 14527, 14528, 1512, 1529, 14521, 14522, 15072, 15073}),
   LAVA_EEL("Lava eel", 2149, new int[]{4928, 6784, 15384}),
   BARB_FISH("Sturgeon, Salmon, Trout", 11332, new int[]{1542, 7323}),
   ANGLERFISH("Anglerfish", 13439, new int[]{6825}),
   MINNOW("Minnow", 21356, new int[]{7730, 7731, 7732, 7733}),
   HARPOONFISH("Harpoonfish", 25564, new int[]{10565, 10568, 10569}),
   INFERNAL_EEL("Infernal Eel", 21293, new int[]{7676}),
   KARAMBWAN("Karambwan", 3142, new int[]{4712, 4713}),
   KARAMBWANJI("Karambwanji, Shrimp", "Karambwanji", 3150, new int[]{4710}),
   SACRED_EEL("Sacred eel", 13339, new int[]{6488}),
   CAVE_EEL("Frog spawn, Cave eel", 5001, new int[]{1497, 1498, 1499, 1500}),
   SLIMY_EEL("Slimy eel", 3379, new int[]{2653, 2654, 2655}),
   DARK_CRAB("Dark Crab", 11934, new int[]{1535, 1536}),
   COMMON_TENCH("Common tench, Bluegill, Greater siren, Mottled eel", "Greater siren", 22829, new int[]{8523}),
   CAMDOZAAL_TETRA("Guppy, Cavefish, Tetra, Catfish, Barronite shards, Barronite handle", "Tetra", 25666, new int[]{10686}),
   CAMDOZAAL_CAVE_EEL("Slimy eel, Cave eel", "Cave eel", 5003, new int[]{10653}),
   TUTORIAL_SHRIMP("Shrimp", 317, new int[]{3317}),
   ETCETERIA_LOBSTER("Lobster", "Lobster (Approval only)", 377, new int[]{3657}),
   QUEST_RUM_DEAL("Sluglings", "Rum deal (Quest)", 6715, new int[]{635}),
   QUEST_TAI_BWO_WANNAI_TRIO("Karambwan", "Tai Bwo Wannai Trio (Quest)", 3142, new int[]{4714}),
   QUEST_FISHING_CONTEST("Giant carp", "Fishing Contest (Quest)", 337, new int[]{4079, 4080, 4081, 4082}),
   CIVITAS_ILLA_FORTIS_PARK("House Keys", "House Keys", 29325, new int[]{13329}),
   SQUID("Squid", 31561, new int[]{15074, 15078, 15081, 15085});

   private static final Map<Integer, FishingSpot> SPOTS;
   private final String name;
   private final String worldMapTooltip;
   private final int fishSpriteId;
   private final int[] ids;

   private FishingSpot(String spot, int fishSpriteId, int... ids) {
      this(spot, spot, fishSpriteId, ids);
   }

   private FishingSpot(String spot, String worldMapTooltip, int fishSpriteId, int... ids) {
      this.name = spot;
      this.worldMapTooltip = worldMapTooltip;
      this.fishSpriteId = fishSpriteId;
      this.ids = ids;
   }

   public static FishingSpot findSpot(int id) {
      return (FishingSpot)SPOTS.get(id);
   }

   public String getName() {
      return this.name;
   }

   public String getWorldMapTooltip() {
      return this.worldMapTooltip;
   }

   public int getFishSpriteId() {
      return this.fishSpriteId;
   }

   public int[] getIds() {
      return this.ids;
   }

   static {
      ImmutableMap.Builder<Integer, FishingSpot> builder = new ImmutableMap.Builder();
      FishingSpot[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         FishingSpot spot = var1[var3];
         int[] var5 = spot.getIds();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int spotId = var5[var7];
            builder.put(spotId, spot);
         }
      }

      SPOTS = builder.build();
   }
}
