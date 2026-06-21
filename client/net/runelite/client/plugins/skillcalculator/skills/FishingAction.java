package net.runelite.client.plugins.skillcalculator.skills;

import java.util.Collections;
import java.util.Set;

public enum FishingAction implements ItemSkillAction {
   RAW_SHRIMPS(317, 1, 10.0F),
   RAW_KARAMBWANJI(3150, 5, 5.0F),
   RAW_SARDINE(327, 5, 20.0F),
   RAW_GUPPY(25652, 7, 8.0F),
   RAW_HERRING(345, 10, 30.0F),
   RAW_ANCHOVIES(321, 15, 40.0F),
   RAW_MACKEREL(353, 16, 20.0F),
   RAW_CAVEFISH(25658, 20, 16.0F),
   RAW_BREAM(29216, 20, 20.0F),
   RAW_TROUT(335, 20, 50.0F),
   RAW_COD(341, 23, 45.0F),
   RAW_PIKE(349, 25, 60.0F),
   RAW_SLIMY_EEL(3379, 28, 80.0F),
   RAW_SALMON(331, 30, 70.0F),
   RAW_TETRA(25664, 33, 24.0F),
   RAW_TUNA(359, 35, 80.0F),
   RAW_CAVE_EEL(5001, 38, 80.0F),
   RAW_RAINBOW_FISH(10138, 38, 80.0F),
   RAW_LOBSTER(377, 40, 90.0F),
   RAW_CATFISH(25670, 46, 33.0F),
   RAW_BASS(363, 46, 100.0F),
   LEAPING_TROUT(11328, 48, 50.0F),
   RAW_SWORDFISH(371, 50, 100.0F),
   RAW_SWORDTIP_SQUID(31553, 52, 55.0F),
   LEAPING_SALMON(11330, 58, 70.0F),
   RAW_MONKFISH(7944, 62, 120.0F),
   RAW_KARAMBWAN(3142, 65, 50.0F),
   RAW_JUMBO_SQUID(31561, 69, 75.0F),
   RAW_GIANT_KRILL(32309, 69, 112.5F),
   LEAPING_STURGEON(11332, 70, 80.0F),
   RAW_HADDOCK(32317, 73, 128.5F),
   RAW_SHARK(383, 76, 110.0F),
   RAW_SEA_TURTLE(395, 79, 38.0F),
   RAW_YELLOWFIN(32325, 79, 155.5F),
   INFERNAL_EEL(21293, 80, 95.0F),
   RAW_MANTA_RAY(389, 81, 46.0F),
   MINNOW(21356, 82, 26.5F),
   RAW_ANGLERFISH(13439, 82, 120.0F),
   RAW_HALIBUT(32333, 83, 195.5F),
   RAW_DARK_CRAB(11934, 85, 130.0F),
   SACRED_EEL(13339, 87, 105.0F),
   RAW_BLUEFIN(32341, 87, 220.5F),
   RAW_MARLIN(32349, 91, 265.5F);

   private final int itemId;
   private final int level;
   private final float xp;

   public Set<FishingBonus> getExcludedSkillBonuses() {
      switch (this) {
         case RAW_GIANT_KRILL:
         case RAW_HADDOCK:
         case RAW_YELLOWFIN:
         case RAW_HALIBUT:
         case RAW_BLUEFIN:
         case RAW_MARLIN:
            return Set.of(FishingBonus.ANGLERS_OUTFIT);
         default:
            return Collections.emptySet();
      }
   }

   private FishingAction(int itemId, int level, float xp) {
      this.itemId = itemId;
      this.level = level;
      this.xp = xp;
   }

   public int getItemId() {
      return this.itemId;
   }

   public int getLevel() {
      return this.level;
   }

   public float getXp() {
      return this.xp;
   }
}
