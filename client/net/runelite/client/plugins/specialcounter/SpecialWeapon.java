package net.runelite.client.plugins.specialcounter;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.runelite.api.NPC;

public enum SpecialWeapon {
   DRAGON_WARHAMMER("Dragon Warhammer", new int[]{13576, 28035}, false, SpecialCounterConfig::dragonWarhammerThreshold) {
      public float computeDrainPercent(int hit, @Nullable NPC target) {
         if (hit > 0) {
            return 0.7F;
         } else {
            return target != null && SpecialWeapon.TEKTON_VARIANTS.contains(target.getId()) ? 0.95F : 0.0F;
         }
      }
   },
   ARCLIGHT("Arclight", new int[]{19675}, false, SpecialCounterConfig::arclightThreshold),
   DARKLIGHT("Darklight", new int[]{6746}, false, SpecialCounterConfig::darklightThreshold),
   BANDOS_GODSWORD("Bandos Godsword", new int[]{11804, 20370}, true, SpecialCounterConfig::bandosGodswordThreshold) {
      public int computeHit(int hit, @Nullable NPC target) {
         return hit == 0 && target != null && SpecialWeapon.TEKTON_VARIANTS.contains(target.getId()) ? 10 : super.computeHit(hit, target);
      }
   },
   BARRELCHEST_ANCHOR("Barrelchest Anchor", new int[]{10887}, true, (c) -> {
      return 0;
   }),
   BONE_DAGGER("Bone Dagger", new int[]{8872, 8874, 8876, 8878}, true, (c) -> {
      return 0;
   }),
   DORGESHUUN_CROSSBOW("Dorgeshuun Crossbow", new int[]{8880}, true, (distance) -> {
      return 60 + distance * 3;
   }, (c) -> {
      return 0;
   }),
   BULWARK("Dinh's Bulwark", new int[]{21015}, false, SpecialCounterConfig::bulwarkThreshold),
   ACCURSED_SCEPTRE("Accursed Sceptre", new int[]{27665, 27679}, false, (distance) -> {
      return 46 + distance * 10;
   }, (c) -> {
      return 0;
   }),
   TONALZTICS_OF_RALOS("Tonalztics of Ralos", new int[]{28922}, true, (distance) -> {
      return 50;
   }, (c) -> {
      return 0;
   }),
   ELDER_MAUL("Elder Maul", new int[]{21003, 27100}, false, (distance) -> {
      return 50;
   }, SpecialCounterConfig::elderMaulThreshold) {
      public float computeDrainPercent(int hit, @Nullable NPC target) {
         if (hit > 0) {
            return 0.65F;
         } else {
            return target != null && SpecialWeapon.TEKTON_VARIANTS.contains(target.getId()) ? 0.95F : 0.0F;
         }
      }
   },
   SEERCULL("Seercull", new int[]{6724}, true, (d) -> {
      return 46 + d * 5;
   }, (c) -> {
      return 0;
   }),
   EMBERLIGHT("Emberlight", new int[]{29589}, false, SpecialCounterConfig::emberlightThreshold),
   EYE_OF_AYAK("Eye of Ayak", new int[]{31113}, true, (d) -> {
      return 120;
   }, SpecialCounterConfig::ayakThreshold);

   private static final Set<Integer> TEKTON_VARIANTS = ImmutableSet.of(7540, 7541, 7542, 7545, 7543, 7544, new Integer[0]);
   private final String name;
   private final int[] itemID;
   private final boolean damage;
   private final Function<Integer, Integer> clientCycleHitDelay;
   private final Function<SpecialCounterConfig, Integer> threshold;

   private SpecialWeapon(String name, int[] itemID, boolean damage, Function threshold) {
      this(name, itemID, damage, (distance) -> {
         return 0;
      }, threshold);
   }

   public int getHitDelay(int distance) {
      int serverCyclesDelay = (Integer)this.getClientCycleHitDelay().apply(distance) / 30;
      return serverCyclesDelay + 1;
   }

   public float computeDrainPercent(int hit, @Nullable NPC target) {
      return 0.0F;
   }

   public int computeHit(int hit, @Nullable NPC target) {
      return hit;
   }

   private SpecialWeapon(String name, int[] itemID, boolean damage, Function clientCycleHitDelay, Function threshold) {
      this.name = name;
      this.itemID = itemID;
      this.damage = damage;
      this.clientCycleHitDelay = clientCycleHitDelay;
      this.threshold = threshold;
   }

   public String getName() {
      return this.name;
   }

   public int[] getItemID() {
      return this.itemID;
   }

   public boolean isDamage() {
      return this.damage;
   }

   public Function<Integer, Integer> getClientCycleHitDelay() {
      return this.clientCycleHitDelay;
   }

   public Function<SpecialCounterConfig, Integer> getThreshold() {
      return this.threshold;
   }
}
