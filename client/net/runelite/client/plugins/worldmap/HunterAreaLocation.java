package net.runelite.client.plugins.worldmap;

import com.google.common.base.Joiner;
import net.runelite.api.coords.WorldPoint;

enum HunterAreaLocation {
   ALDARIN_NORTH(new WorldPoint(1357, 2977, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.COPPER_LONGTAIL}),
   ALDARIN_WEST(new WorldPoint(1342, 2934, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.RUBY_HARVEST}),
   AUBURNVALE_NORTH(new WorldPoint(1387, 3392, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.WILD_KEBBIT, HunterAreaLocation.HunterCreature.RUBY_HARVEST}),
   AUBURNVALE_WEST(new WorldPoint(1349, 3346, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.COPPER_LONGTAIL}),
   AVIUM_SAVANNAH(new WorldPoint(1616, 2999, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.PYRE_FOX}),
   AVIUM_SAVANNAH_EAST(new WorldPoint(1745, 3008, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.SUNLIGHT_ANTELOPE}),
   BONEYARD_HUNTER_AREA(new WorldPoint(3294, 3673, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.BLACK_SALAMANDER}),
   CANIFIS_HUNTER_AREA1(new WorldPoint(3553, 3438, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.SWAMP_LIZARD}),
   CANIFIS_HUNTER_AREA2(new WorldPoint(3535, 3445, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.SWAMP_LIZARD}),
   FALCONRY(new WorldPoint(2379, 3599, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.SPOTTED_KEBBIT, HunterAreaLocation.HunterCreature.DARK_KEBBIT, HunterAreaLocation.HunterCreature.DASHING_KEBBIT}),
   FELDIP_HUNTER_AREA(new WorldPoint(2557, 2912, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CRIMSON_SWIFT, HunterAreaLocation.HunterCreature.FELDIP_WEASEL, HunterAreaLocation.HunterCreature.TROPICAL_WAGTAIL, HunterAreaLocation.HunterCreature.SPINED_LARUPIA, HunterAreaLocation.HunterCreature.BARB_TAILED_KEBBIT, HunterAreaLocation.HunterCreature.BLACK_WARLOCK, HunterAreaLocation.HunterCreature.CARNIVOROUS_CHINCHOMPA}),
   FOSSIL_ISLAND1(new WorldPoint(3693, 3800, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.HERBIBOAR}),
   FOSSIL_ISLAND2(new WorldPoint(3701, 3809, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.HERBIBOAR}),
   FOSSIL_ISLAND3(new WorldPoint(3703, 3829, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.HERBIBOAR}),
   FOSSIL_ISLAND4(new WorldPoint(3749, 3850, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.HERBIBOAR}),
   FOSSIL_ISLAND5(new WorldPoint(3684, 3870, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.HERBIBOAR}),
   FOSSIL_ISLAND_UNDERWATER(new WorldPoint(3743, 10295, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.FISH_SHOAL}),
   GWENITH_HUNTER_AREA_OUTSIDE(new WorldPoint(2269, 3408, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CARNIVOROUS_CHINCHOMPA}),
   GWENITH_HUNTER_AREA_INSIDE(new WorldPoint(3293, 6160, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CARNIVOROUS_CHINCHOMPA}),
   ISLE_OF_SOULS_NORTH(new WorldPoint(2207, 2964, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.COPPER_LONGTAIL}),
   ISLE_OF_SOULS_NORTH_WEST(new WorldPoint(2127, 2950, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CHINCHOMPA}),
   ISLE_OF_SOULS_SOUTH_WEST(new WorldPoint(2158, 2822, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CRIMSON_SWIFT}),
   HUNTER_GUILD_CAVERNS(new WorldPoint(1559, 9420, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.MOONLIGHT_ANTELOPE}),
   HUNTER_GUILD_NORTH(new WorldPoint(1556, 3091, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.SUNLIGHT_MOTH}),
   HUNTER_GUILD_SOUTHEAST(new WorldPoint(1575, 3020, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.SUNLIGHT_MOTH}),
   HUNTER_GUILD_WEST(new WorldPoint(1515, 3047, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.EMBERTAILED_JERBOA}),
   KARAMJA_HUNTER_AREA(new WorldPoint(2786, 3001, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.HORNED_GRAAHK}),
   KEBOS_SWAMP(new WorldPoint(1184, 3595, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CRIMSON_SWIFT}),
   KOUREND_WOODLAND_CENTER(new WorldPoint(1512, 3478, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.RUBY_HARVEST}),
   KOUREND_WOODLAND_NORTH_WEST(new WorldPoint(1481, 3504, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CHINCHOMPA}),
   KOUREND_WOODLAND_SOUTH(new WorldPoint(1556, 3436, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.COPPER_LONGTAIL}),
   LOCUS_OASIS(new WorldPoint(1671, 3001, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.EMBERTAILED_JERBOA}),
   LAKE_MOLCH(new WorldPoint(1363, 3632, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.BLUEGILL, HunterAreaLocation.HunterCreature.COMMON_TENCH, HunterAreaLocation.HunterCreature.MOTTLED_EEL, HunterAreaLocation.HunterCreature.GREATER_SIREN}),
   MONS_GRATIA(new WorldPoint(1443, 3235, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CERULEAN_TWITCH, HunterAreaLocation.HunterCreature.SNOWY_KNIGHT, HunterAreaLocation.HunterCreature.SAPPHIRE_GLACIALIS}),
   NECROPOLIS(new WorldPoint(3285, 2739, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.ORANGE_SALAMANDER}),
   OURANIA_HUNTER_AREA_EAST(new WorldPoint(2447, 3219, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.RED_SALAMANDER}),
   OURANIA_HUNTER_AREA_SOUTH(new WorldPoint(2475, 3240, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.RED_SALAMANDER}),
   PISCATORIS_HUNTER_AREA(new WorldPoint(2335, 3584, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.COMMON_KEBBIT, HunterAreaLocation.HunterCreature.COPPER_LONGTAIL, HunterAreaLocation.HunterCreature.RUBY_HARVEST, HunterAreaLocation.HunterCreature.WILD_KEBBIT, HunterAreaLocation.HunterCreature.FERRET, HunterAreaLocation.HunterCreature.WHITE_RABBIT, HunterAreaLocation.HunterCreature.PRICKLY_KEBBIT, HunterAreaLocation.HunterCreature.RAZOR_BACKED_KEBBIT, HunterAreaLocation.HunterCreature.CHINCHOMPA}),
   PORT_PISCARILIUS_BEACH(new WorldPoint(1840, 3802, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.SANDWORMS}),
   RALOS_RISE(new WorldPoint(1475, 3096, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.TECU_SALAMANDER}),
   RELLEKA_HUNTER_AREA(new WorldPoint(2719, 3780, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.POLAR_KEBBIT, HunterAreaLocation.HunterCreature.CERULEAN_TWITCH, HunterAreaLocation.HunterCreature.SAPPHIRE_GLACIALIS, HunterAreaLocation.HunterCreature.SNOWY_KNIGHT, HunterAreaLocation.HunterCreature.SABRE_TOOTHED_KEBBIT, HunterAreaLocation.HunterCreature.SABRE_TOOTHED_KYATT}),
   SLEPE_NORTH(new WorldPoint(3677, 3405, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.SWAMP_LIZARD}),
   TLATI_RAINFOREST_EAST(new WorldPoint(1372, 3135, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CRIMSON_SWIFT}),
   TLATI_RAINFOREST_NORTH(new WorldPoint(1315, 3169, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.CARNIVOROUS_CHINCHOMPA}),
   TLATI_RAINFOREST_CENTER(new WorldPoint(1269, 3102, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.TROPICAL_WAGTAIL}),
   TLATI_RAINFOREST_SOUTH(new WorldPoint(1282, 3021, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.BLACK_WARLOCK}),
   UZER_HUNTER_AREA(new WorldPoint(3401, 3104, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.GOLDEN_WARBLER, HunterAreaLocation.HunterCreature.DESERT_DEVIL, HunterAreaLocation.HunterCreature.ORANGE_SALAMANDER}),
   WILDERNESS(new WorldPoint(3142, 3771, 0), new HunterCreature[]{HunterAreaLocation.HunterCreature.BLACK_CHINCHOMPA});

   private final WorldPoint location;
   private final String tooltip;

   private HunterAreaLocation(WorldPoint location, HunterCreature... creatures) {
      this.location = location;
      this.tooltip = Joiner.on("<br>").join(creatures);
   }

   public WorldPoint getLocation() {
      return this.location;
   }

   public String getTooltip() {
      return this.tooltip;
   }

   private static enum HunterCreature {
      BARB_TAILED_KEBBIT("Barb-tailed kebbit", 33),
      BLACK_CHINCHOMPA("Black chinchompa", 73),
      BLACK_SALAMANDER("Black salamander", 67),
      BLACK_WARLOCK("Black warlock", 45),
      BLUEGILL("Bluegill", 35),
      CARNIVOROUS_CHINCHOMPA("Carnivorous chinchompa", 63),
      CERULEAN_TWITCH("Cerulean twitch", 11),
      CHINCHOMPA("Chinchompa", 53),
      COMMON_KEBBIT("Common kebbit", 3),
      COMMON_TENCH("Common tench", 51),
      COPPER_LONGTAIL("Copper longtail", 9),
      CRIMSON_SWIFT("Crimson swift", 1),
      DARK_KEBBIT("Dark kebbit", 57),
      DASHING_KEBBIT("Dashing kebbit", 69),
      DESERT_DEVIL("Desert devil", 13),
      EMBERTAILED_JERBOA("Embertailed Jerboa", 39),
      FELDIP_WEASEL("Feldip Weasel", 7),
      FERRET("Ferret", 27),
      FISH_SHOAL("Fish shoal", 44),
      GOLDEN_WARBLER("Golden warbler", 5),
      GREATER_SIREN("Greater siren", 87),
      HERBIBOAR("Herbiboar", 80),
      HORNED_GRAAHK("Horned graahk", 41),
      MOONLIGHT_ANTELOPE("Moonlight Antelope", 91),
      MOTTLED_EEL("Mottled eel", 68),
      ORANGE_SALAMANDER("Orange salamander", 47),
      POLAR_KEBBIT("Polar kebbit", 1),
      PRICKLY_KEBBIT("Prickly kebbit", 37),
      PYRE_FOX("Pyre Fox", 57),
      RAZOR_BACKED_KEBBIT("Razor-backed kebbit", 49),
      RED_SALAMANDER("Red salamander", 59),
      RUBY_HARVEST("Ruby harvest", 15),
      SABRE_TOOTHED_KEBBIT("Sabre-toothed kebbit", 51),
      SABRE_TOOTHED_KYATT("Sabre-toothed kyatt", 55),
      SANDWORMS("Sandworms", 15),
      SAPPHIRE_GLACIALIS("Sapphire glacialis", 25),
      SNOWY_KNIGHT("Snowy knight", 35),
      SPINED_LARUPIA("Spined larupia", 31),
      SPOTTED_KEBBIT("Spotted kebbit", 43),
      SUNLIGHT_ANTELOPE("Sunlight Antelope", 72),
      SUNLIGHT_MOTH("Sunlight Moth", 65),
      SWAMP_LIZARD("Swamp lizard", 29),
      TECU_SALAMANDER("Tecu Salamander", 79),
      TROPICAL_WAGTAIL("Tropical wagtail", 19),
      WHITE_RABBIT("White rabbit", 27),
      WILD_KEBBIT("Wild kebbit", 23);

      private String name;
      private int level;

      public String toString() {
         return this.name + " (" + this.level + ")";
      }

      private HunterCreature(String name, int level) {
         this.name = name;
         this.level = level;
      }
   }
}
