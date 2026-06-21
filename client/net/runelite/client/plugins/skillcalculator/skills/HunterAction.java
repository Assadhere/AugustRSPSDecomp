package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;

public enum HunterAction implements NamedSkillAction {
   POLAR_KEBBIT("Polar Kebbit", 1, 30.0F, 9953),
   CRIMSON_SWIFT("Crimson Swift", 1, 34.0F, 9965),
   COMMON_KEBBIT("Common Kebbit", 3, 36.0F, 9954),
   GOLDEN_WARBLER("Golden Warbler", 5, 47.0F, 9968),
   REGULAR_BIRD_HOUSE("Regular Bird House", 5, 280.0F, 21512),
   FELDIP_WEASEL("Feldip Weasel", 7, 48.0F, 9955),
   COPPER_LONGTAIL("Copper Longtail", 9, 61.0F, 9966),
   CERULEAN_TWITCH("Cerulean Twitch", 11, 64.5F, 9967),
   DESERT_DEVIL("Desert Devil", 13, 66.0F, 9956),
   OAK_BIRD_HOUSE("Oak Bird House", 14, 420.0F, 21515),
   RUBY_HARVEST("Ruby Harvest", 15, 24.0F, 9970),
   BABY_IMPLING("Baby Impling", 17, 18.0F, 11238),
   TROPICAL_WAGTAIL("Tropical Wagtail", 19, 95.0F, 9969),
   MOSS_LIZARD("Moss Lizard", 20, 90.0F, 29076),
   RED_CRAB("Red Crab", 21, 64.0F, 31671),
   YOUNG_IMPLING("Young Impling", 22, 20.0F, 11240),
   WILD_KEBBIT("Wild Kebbit", 23, 128.0F, 9953),
   WILLOW_BIRD_HOUSE("Willow Bird House", 24, 560.0F, 21518),
   SAPPHIRE_GLACIALIS("Sapphire Glacialis", 25, 34.0F, 9971),
   FERRET("Ferret", 27, 115.0F, 10092),
   WHITE_RABBIT("White Rabbit", 27, 144.0F, 9975),
   GOURMET_IMPLING("Gourmet Impling", 28, 22.0F, 11242),
   SWAMP_LIZARD("Swamp Lizard", 29, 152.0F, 10149),
   SPINED_LARUPIA("Spined Larupia", 31, 180.0F, 10045),
   BARB_TAILED_KEBBIT("Barb-tailed Kebbit", 33, 168.0F, 9958),
   TEAK_BIRD_HOUSE("Teak Bird House", 34, 700.0F, 21521),
   SNOWY_KNIGHT("Snowy Knight", 35, 44.0F, 9972),
   EARTH_IMPLING("Earth Impling", 36, 25.0F, 11244),
   PRICKLY_KEBBIT("Prickly Kebbit", 37, 204.0F, 9957),
   EMBERTAILED_JERBOA("Embertailed Jerboa", 39, 137.0F, 28866),
   HORNED_GRAAHK("Horned Graahk", 41, 240.0F, 10051),
   ESSENCE_IMPLING("Essence Impling", 42, 27.0F, 11246),
   SPOTTED_KEBBIT("Spotted Kebbit", 43, 104.0F, 9960),
   MAPLE_BIRD_HOUSE("Maple Bird House", 44, 820.0F, 22192),
   BLACK_WARLOCK("Black Warlock", 45, 54.0F, 9973),
   ORANGE_SALAMANDER("Orange Salamander", 47, 224.0F, 10146),
   BLUE_CRAB("Blue Crab", 48, 136.0F, 31674),
   RAZOR_BACKED_KEBBIT("Razor-backed Kebbit", 49, 348.0F, 9961),
   MAHOGANY_BIRD_HOUSE("Mahogany Bird House", 49, 960.0F, 22195),
   ECLECTIC_IMPLING("Eclectic Impling", 50, 32.0F, 11248),
   SABRE_TOOTHED_KEBBIT("Sabre-toothed Kebbit", 51, 200.0F, 9959),
   CHINCHOMPA("Chinchompa", 53, 198.4F, 9976),
   SABRE_TOOTHED_KYATT("Sabre-toothed Kyatt", 55, 300.0F, 10039),
   DARK_KEBBIT("Dark Kebbit", 57, 132.0F, 9963),
   PYRE_FOX("Pyre Fox", 57, 222.0F, 28865),
   NATURE_IMPLING("Nature Impling", 58, 34.0F, 11250),
   RED_SALAMANDER("Red Salamander", 59, 272.0F, 10147),
   YEW_BIRD_HOUSE("Yew Bird House", 59, 1020.0F, 22198),
   MANIACAL_MONKEY("Maniacal Monkey", 60, 1000.0F, 19556),
   CARNIVOROUS_CHINCHOMPA("Carnivorous Chinchompa", 63, 265.0F, 9977),
   MAGPIE_IMPLING("Magpie Impling", 65, 44.0F, 11252),
   SUNLIGHT_MOTH("Sunlight Moth", 65, 74.0F, 28863),
   MAGPIE_IMPLING_GIELINOR("Magpie Impling (Gielinor)", 65, 216.0F, 11252),
   BLACK_SALAMANDER("Black Salamander", 67, 319.5F, 10148),
   DASHING_KEBBIT("Dashing Kebbit", 69, 156.0F, 9964),
   SUNLIGHT_ANTELOPE("Sunlight Antelope", 72, 380.0F, 28867),
   BLACK_CHINCHOMPA("Black Chinchompa", 73, 315.0F, 11959),
   NINJA_IMPLING("Ninja Impling", 74, 52.0F, 11254),
   NINJA_IMPLING_GIELINOR("Ninja Impling (Gielinor)", 74, 240.0F, 11254),
   MAGIC_BIRD_HOUSE("Magic Bird House", 74, 1140.0F, 22201),
   MOONLIGHT_MOTH("Moonlight Moth", 75, 84.0F, 28864),
   RAINBOW_CRAB("Rainbow Crab", 77, 216.0F, 31683),
   TECU_SALAMANDER("Tecu Salamander", 79, 344.0F, 28834),
   CRYSTAL_IMPLING("Crystal Impling", 80, 280.0F, 23768),
   DRAGON_IMPLING("Dragon Impling", 83, 65.0F, 11256),
   DRAGON_IMPLING_GIELINOR("Dragon Impling (Gielinor)", 83, 300.0F, 11256),
   LUCKY_IMPLING("Lucky Impling", 89, 380.0F, 19732),
   REDWOOD_BIRD_HOUSE("Redwood Bird House", 89, 1200.0F, 22204),
   MOONLIGHT_ANTELOPE("Moonlight Antelope", 91, 450.0F, 28868);

   private final String name;
   private final int level;
   private final float xp;
   private final int icon;

   public boolean isMembers(ItemManager itemManager) {
      return true;
   }

   private HunterAction(String name, int level, float xp, int icon) {
      this.name = name;
      this.level = level;
      this.xp = xp;
      this.icon = icon;
   }

   public String getName() {
      return this.name;
   }

   public int getLevel() {
      return this.level;
   }

   public float getXp() {
      return this.xp;
   }

   public int getIcon() {
      return this.icon;
   }
}
