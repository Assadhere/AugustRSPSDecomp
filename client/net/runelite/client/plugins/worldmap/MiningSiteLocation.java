package net.runelite.client.plugins.worldmap;

import com.google.common.base.Joiner;
import net.runelite.api.coords.WorldPoint;

enum MiningSiteLocation {
   AGILITY_PYRAMID(new WorldPoint(3322, 2875, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.GOLD)}),
   ALDARIN(new WorldPoint(1430, 2882, 0), new Rock[]{new Rock(8, MiningSiteLocation.Ore.IRON), new Rock(5, MiningSiteLocation.Ore.SILVER), new Rock(6, MiningSiteLocation.Ore.COAL), new Rock(5, MiningSiteLocation.Ore.GOLD)}),
   AL_KHARID_MINE_NORTH(new WorldPoint(3298, 3312, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COPPER), new Rock(1, MiningSiteLocation.Ore.TIN), new Rock(7, MiningSiteLocation.Ore.IRON), new Rock(5, MiningSiteLocation.Ore.SILVER), new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   AL_KHARID_MINE_SOUTH(new WorldPoint(3298, 3282, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.GOLD)}),
   ANCIENT_CAVERN_NORTH(new WorldPoint(1847, 5414, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.MITHRIL)}),
   ANCIENT_CAVERN_SOUTH(new WorldPoint(1826, 5392, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.MITHRIL)}),
   ANCIENT_CAVERN_MIDDLE(new WorldPoint(1840, 5397, 0), new Rock[]{new Rock(8, MiningSiteLocation.Ore.MITHRIL)}),
   ARANDAR(new WorldPoint(2322, 3269, 0), new Rock[]{new Rock(8, MiningSiteLocation.Ore.LIMESTONE)}),
   ARANDAR_PRIFDDINAS_MAP(new WorldPoint(3346, 6021, 0), new Rock[]{new Rock(8, MiningSiteLocation.Ore.LIMESTONE)}),
   ARCEUUS_NORTH(new WorldPoint(1763, 3860, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.DENSE_ESSENCE)}),
   ARCEUUS_SOUTH(new WorldPoint(1763, 3844, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.DENSE_ESSENCE)}),
   ARDOUGNE_SEWERS(new WorldPoint(2670, 9680, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.IRON), new Rock(5, MiningSiteLocation.Ore.COAL)}),
   ARDOUGNE_SOUTH_EAST(new WorldPoint(2599, 3232, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.COAL)}),
   ASGARNIA_ICE_DUNGEON_EAST(new WorldPoint(3063, 9582, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.BLURITE)}),
   ASGARNIA_ICE_DUNGEON_WEST(new WorldPoint(3049, 9568, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.BLURITE)}),
   AVIUM_SAVANNAH(new WorldPoint(1618, 2991, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.IRON)}),
   BANDIT_CAMP_MINE(new WorldPoint(3086, 3763, 0), new Rock[]{new Rock(16, MiningSiteLocation.Ore.IRON), new Rock(20, MiningSiteLocation.Ore.COAL), new Rock(22, MiningSiteLocation.Ore.MITHRIL), new Rock(8, MiningSiteLocation.Ore.ADAMANTITE)}),
   BANDIT_CAMP_QUARRY(new WorldPoint(3171, 2912, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.CLAY), new Rock(2, MiningSiteLocation.Ore.COAL), new Rock(36, MiningSiteLocation.Ore.SANDSTONE), new Rock(34, MiningSiteLocation.Ore.GRANITE)}),
   BARBARIAN_VILLAGE(new WorldPoint(3078, 3421, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.TIN), new Rock(4, MiningSiteLocation.Ore.COAL)}),
   BATTLEFIELD(new WorldPoint(2471, 3255, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COPPER), new Rock(1, MiningSiteLocation.Ore.TIN)}),
   BLAST_MINE_EAST(new WorldPoint(1502, 3869, 0), new Rock[]{new Rock(20, MiningSiteLocation.Ore.HARD_ROCK)}),
   BLAST_MINE_NORTH(new WorldPoint(1485, 3882, 0), new Rock[]{new Rock(17, MiningSiteLocation.Ore.HARD_ROCK)}),
   BLAST_MINE_WEST(new WorldPoint(1471, 3865, 0), new Rock[]{new Rock(22, MiningSiteLocation.Ore.HARD_ROCK)}),
   BRIMHAVEN_NORTH(new WorldPoint(2732, 3225, 0), new Rock[]{new Rock(10, MiningSiteLocation.Ore.GOLD)}),
   BRIMHAVEN_SOUTH_(new WorldPoint(2743, 3150, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.GOLD)}),
   CAM_TORUM(new WorldPoint(1510, 9540, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.COAL), new Rock(16, MiningSiteLocation.Ore.CALCIFIED_ROCKS), new Rock(2, MiningSiteLocation.Ore.MITHRIL), new Rock(1, MiningSiteLocation.Ore.ADAMANTITE)}),
   CAMDOZAAL_MINES_EAST(new WorldPoint(2934, 5811, 0), new Rock[]{new Rock(8, MiningSiteLocation.Ore.BARRONITE), new Rock(1, MiningSiteLocation.Ore.CLAY), new Rock(2, MiningSiteLocation.Ore.TIN), new Rock(1, MiningSiteLocation.Ore.COPPER)}),
   CAMDOZAAL_MINES_WEST(new WorldPoint(2914, 5811, 0), new Rock[]{new Rock(10, MiningSiteLocation.Ore.BARRONITE), new Rock(2, MiningSiteLocation.Ore.COPPER), new Rock(2, MiningSiteLocation.Ore.CLAY), new Rock(1, MiningSiteLocation.Ore.TIN)}),
   CENTRAL_FREMENIK_ISLES(new WorldPoint(2374, 3850, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.COAL), new Rock(1, MiningSiteLocation.Ore.RUNITE)}),
   CITHAREDE_ABBEY(new WorldPoint(3400, 3170, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.COAL)}),
   COAL_TRUCKS(new WorldPoint(2580, 3484, 0), new Rock[]{new Rock(18, MiningSiteLocation.Ore.COAL)}),
   CRAFTING_GUILD(new WorldPoint(2939, 3283, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.CLAY), new Rock(6, MiningSiteLocation.Ore.SILVER), new Rock(7, MiningSiteLocation.Ore.GOLD)}),
   CRANDOR_NORTH_EAST(new WorldPoint(2860, 3287, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.GOLD)}),
   CRANDOR_NORTH_WEST(new WorldPoint(2831, 3296, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.COAL), new Rock(1, MiningSiteLocation.Ore.MITHRIL)}),
   CRANDOR_SOUTH_EAST(new WorldPoint(2835, 3245, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(3, MiningSiteLocation.Ore.ADAMANTITE)}),
   CRANDOR_SOUTH_WEST(new WorldPoint(2819, 3247, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.MITHRIL)}),
   CUSTODIA_LAKE(new WorldPoint(1281, 3412, 0), new Rock[]{new Rock(8, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(6, MiningSiteLocation.Ore.MITHRIL)}),
   DAEYALT_ESSENCE_MINE(new WorldPoint(3631, 3340, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.DAEYALT_ESSENCE)}),
   DEEPFIN_POINT_NORTH(new WorldPoint(1945, 2805, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.TIN), new Rock(4, MiningSiteLocation.Ore.COPPER), new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(5, MiningSiteLocation.Ore.MITHRIL)}),
   DEEPFIN_POINT_WEST(new WorldPoint(1932, 2790, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.SILVER), new Rock(7, MiningSiteLocation.Ore.LEAD), new Rock(6, MiningSiteLocation.Ore.GOLD), new Rock(7, MiningSiteLocation.Ore.NICKEL)}),
   DEEPFIN_MINE_NORTH(new WorldPoint(1996, 9205, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.IRON), new Rock(7, MiningSiteLocation.Ore.LEAD)}),
   DEEPFIN_MINE_NORTH_WEST(new WorldPoint(1944, 9204, 0), new Rock[]{new Rock(23, MiningSiteLocation.Ore.COAL), new Rock(4, MiningSiteLocation.Ore.ADAMANTITE)}),
   DEEPFIN_MINE_WEST(new WorldPoint(1932, 9186, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.MITHRIL)}),
   DEEPFIN_MINE_NORTH_EAST(new WorldPoint(2067, 9208, 0), new Rock[]{new Rock(12, MiningSiteLocation.Ore.NICKEL)}),
   DEEPFIN_MINE_EAST(new WorldPoint(2090, 9201, 0), new Rock[]{new Rock(21, MiningSiteLocation.Ore.COAL), new Rock(6, MiningSiteLocation.Ore.ADAMANTITE), new Rock(4, MiningSiteLocation.Ore.RUNITE)}),
   DESERT_MINING_CAMP_SURFACE(new WorldPoint(3299, 3021, 0), true, new Rock[]{new Rock(4, MiningSiteLocation.Ore.COPPER), new Rock(4, MiningSiteLocation.Ore.TIN), new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.COAL)}),
   DORGESH_KAAN_NORTH(new WorldPoint(3309, 9645, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.IRON), new Rock(9, MiningSiteLocation.Ore.SILVER)}),
   DORGESH_KAAN_SOUTH_EAST(new WorldPoint(3322, 9616, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.IRON)}),
   DORGESH_KAAN_SOUTH_WEST(new WorldPoint(3312, 9621, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.IRON)}),
   DORGESH_KAAN_WEST(new WorldPoint(3311, 9628, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.SILVER)}),
   DWARVEN_EAST_BOTTOM(new WorldPoint(3039, 9763, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.TIN), new Rock(2, MiningSiteLocation.Ore.IRON), new Rock(8, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.GOLD), new Rock(1, MiningSiteLocation.Ore.ADAMANTITE)}),
   DWARVEN_EAST_MIDDLE(new WorldPoint(3037, 9775, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.COPPER), new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   DWARVEN_EAST_TOP(new WorldPoint(3051, 9820, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.CLAY), new Rock(3, MiningSiteLocation.Ore.TIN), new Rock(2, MiningSiteLocation.Ore.IRON)}),
   DWARVEN_WEST_BOTTOM(new WorldPoint(3028, 9809, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.CLAY), new Rock(4, MiningSiteLocation.Ore.COPPER)}),
   DWARVEN_WEST_TOP(new WorldPoint(3031, 9828, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COPPER), new Rock(2, MiningSiteLocation.Ore.TIN), new Rock(2, MiningSiteLocation.Ore.IRON)}),
   FREMENIK_ISLES_EAST(new WorldPoint(2405, 3867, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COPPER), new Rock(3, MiningSiteLocation.Ore.TIN), new Rock(4, MiningSiteLocation.Ore.COAL)}),
   EAGLES_OUTPOST(new WorldPoint(3424, 3164, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.CLAY)}),
   EDGEVILLE_DUNGEON(new WorldPoint(3138, 9874, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.COPPER), new Rock(2, MiningSiteLocation.Ore.TIN), new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.SILVER), new Rock(6, MiningSiteLocation.Ore.COAL), new Rock(1, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   FALADOR_WEST(new WorldPoint(2907, 3362, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.COPPER), new Rock(6, MiningSiteLocation.Ore.TIN), new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.COAL)}),
   FELDIP_HILLS_EAST(new WorldPoint(2638, 2996, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.ROCK)}),
   FELDIP_HILLS_MIDDLE(new WorldPoint(2579, 2998, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.ROCK)}),
   FELDIP_HILLS_WEST(new WorldPoint(2567, 2961, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.ROCK)}),
   FIGHT_ARENA(new WorldPoint(2630, 3142, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.CLAY), new Rock(2, MiningSiteLocation.Ore.COPPER), new Rock(7, MiningSiteLocation.Ore.TIN), new Rock(9, MiningSiteLocation.Ore.IRON), new Rock(1, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.MITHRIL)}),
   FOSSIL_ISLAND(new WorldPoint(3770, 3815, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.IRON), new Rock(20, MiningSiteLocation.Ore.COAL), new Rock(3, MiningSiteLocation.Ore.MITHRIL), new Rock(5, MiningSiteLocation.Ore.ADAMANTITE), new Rock(2, MiningSiteLocation.Ore.RUNITE)}),
   FREMENNIK_ISLES_WEST(new WorldPoint(2310, 3853, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COPPER)}),
   FROZEN_WASTE_PLATEU_CENTER(new WorldPoint(2963, 3933, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.RUNITE)}),
   FROZEN_WASTE_PLATEU_NORTH(new WorldPoint(2975, 3937, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.RUNITE)}),
   FROZEN_WASTE_PLATEU_SOUTH(new WorldPoint(2947, 3914, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.RUNITE)}),
   GRAND_TREE(new WorldPoint(2489, 9916, 0), new Rock[]{new Rock(9, MiningSiteLocation.Ore.CLAY), new Rock(8, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.SILVER), new Rock(11, MiningSiteLocation.Ore.COAL), new Rock(4, MiningSiteLocation.Ore.GOLD), new Rock(4, MiningSiteLocation.Ore.MITHRIL), new Rock(3, MiningSiteLocation.Ore.ADAMANTITE)}),
   GREAT_CONCH_NORTH(new WorldPoint(3183, 2502, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.TIN), new Rock(3, MiningSiteLocation.Ore.COPPER), new Rock(8, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.COAL), new Rock(3, MiningSiteLocation.Ore.CLAY), new Rock(2, MiningSiteLocation.Ore.MITHRIL)}),
   GREAT_CONCH_SOUTH(new WorldPoint(3248, 2352, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.COAL), new Rock(9, MiningSiteLocation.Ore.GRANITE), new Rock(9, MiningSiteLocation.Ore.SANDSTONE)}),
   GRIMSTONE_NORTH_EAST(new WorldPoint(2911, 4084, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(3, MiningSiteLocation.Ore.ADAMANTITE), new Rock(4, MiningSiteLocation.Ore.NICKEL)}),
   GRIMSTONE_NORTH_WEST(new WorldPoint(2901, 4084, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.COAL), new Rock(3, MiningSiteLocation.Ore.RUNITE)}),
   GRIMSTONE_SOUTH(new WorldPoint(2909, 4062, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.COAL), new Rock(4, MiningSiteLocation.Ore.NICKEL)}),
   GWENITH(new WorldPoint(2163, 3415, 0), new Rock[]{new Rock(10, MiningSiteLocation.Ore.GOLD)}),
   GWENITH_PRIFDDINAS_MAP(new WorldPoint(3187, 6167, 0), new Rock[]{new Rock(10, MiningSiteLocation.Ore.GOLD)}),
   HEROES_GUILD_EAST_BOTTOM(new WorldPoint(2940, 9884, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.RUNITE)}),
   HEROES_GUILD_EAST_TOP(new WorldPoint(2939, 9898, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.COAL)}),
   HEROES_GUILD_WEST_BOTTOM(new WorldPoint(2921, 9904, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COAL)}),
   HEROES_GUILD_WEST_TOP(new WorldPoint(2914, 9916, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   HOSIDIUS_MINE(new WorldPoint(1777, 3489, 0), new Rock[]{new Rock(10, MiningSiteLocation.Ore.CLAY), new Rock(11, MiningSiteLocation.Ore.COPPER), new Rock(4, MiningSiteLocation.Ore.TIN), new Rock(9, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.SILVER)}),
   ISAFDAR(new WorldPoint(2277, 3159, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.ADAMANTITE), new Rock(2, MiningSiteLocation.Ore.RUNITE)}),
   ISLE_OF_SOULS_DUNGEON_EAST(new WorldPoint(2279, 9237, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.RUNITE)}),
   ISLE_OF_SOULS_DUNGEON_WEST(new WorldPoint(2262, 9244, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   ISLE_OF_SOULS_SOUTH(new WorldPoint(2195, 2793, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.CLAY), new Rock(3, MiningSiteLocation.Ore.TIN), new Rock(3, MiningSiteLocation.Ore.COPPER), new Rock(10, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.SILVER), new Rock(6, MiningSiteLocation.Ore.COAL), new Rock(4, MiningSiteLocation.Ore.GOLD), new Rock(2, MiningSiteLocation.Ore.MITHRIL)}),
   JATIZSO(new WorldPoint(2396, 3812, 0), new Rock[]{new Rock(11, MiningSiteLocation.Ore.TIN), new Rock(7, MiningSiteLocation.Ore.IRON), new Rock(8, MiningSiteLocation.Ore.COAL), new Rock(15, MiningSiteLocation.Ore.MITHRIL), new Rock(11, MiningSiteLocation.Ore.ADAMANTITE)}),
   KARAMJA_JUNGLE(new WorldPoint(2848, 3033, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.IRON), new Rock(1, MiningSiteLocation.Ore.SILVER), new Rock(1, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   KARAMJA_VOLCANO(new WorldPoint(2856, 9579, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.GOLD)}),
   KEBOS_LOWLANDS(new WorldPoint(1211, 3657, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.ADAMANTITE), new Rock(5, MiningSiteLocation.Ore.MITHRIL)}),
   KELDAGRIM_ENTRANCE(new WorldPoint(2724, 3693, 0), new Rock[]{new Rock(9, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.MITHRIL)}),
   KELDAGRIM_NORTH_EAST(new WorldPoint(2937, 10232, 0), new Rock[]{new Rock(9, MiningSiteLocation.Ore.COAL)}),
   KELDAGRIM_SOUTH_WEST_BOTTOM(new WorldPoint(2872, 10119, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.COPPER), new Rock(5, MiningSiteLocation.Ore.COAL)}),
   KELDAGRIM_SOUTH_WEST_MIDDLE(new WorldPoint(2818, 10156, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.IRON), new Rock(1, MiningSiteLocation.Ore.GOLD)}),
   KELDAGRIM_SOUTH_WEST_TOP(new WorldPoint(2864, 10170, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.TIN)}),
   LAVA_MAZE_DUNGEON(new WorldPoint(3045, 10263, 0), true, new Rock[]{new Rock(1, MiningSiteLocation.Ore.RUNITE)}),
   LAVA_MAZE_NORTH(new WorldPoint(3059, 3884, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.RUNITE)}),
   LEGENDS_GUILD_EAST(new WorldPoint(2709, 3331, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(8, MiningSiteLocation.Ore.COAL)}),
   LEGENDS_GUILD_WEST(new WorldPoint(2694, 3332, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.IRON), new Rock(5, MiningSiteLocation.Ore.COAL)}),
   LOVAKENGJ_SOUTH(new WorldPoint(1476, 3779, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.IRON), new Rock(6, MiningSiteLocation.Ore.COAL), new Rock(1, MiningSiteLocation.Ore.MITHRIL)}),
   LOVAKENGJ_SULPHUR_EAST(new WorldPoint(1445, 3870, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.VOLCANIC_SULPHUR)}),
   LOVAKENGJ_SULPHUR_WEST(new WorldPoint(1427, 3870, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.VOLCANIC_SULPHUR)}),
   LOVAKENGJ_WEST_1(new WorldPoint(1430, 3849, 0), new Rock[]{new Rock(33, MiningSiteLocation.Ore.COAL), new Rock(58, MiningSiteLocation.Ore.LOVAKITE)}),
   LOVAKENGJ_WEST_2(new WorldPoint(1447, 3840, 0), new Rock[]{new Rock(12, MiningSiteLocation.Ore.COAL), new Rock(22, MiningSiteLocation.Ore.LOVAKITE)}),
   LUMBRIDGE_SWAMP_EAST(new WorldPoint(3226, 3146, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.COPPER), new Rock(5, MiningSiteLocation.Ore.TIN)}),
   LUMBRIDGE_SWAMP_WEST(new WorldPoint(3148, 3149, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.COAL), new Rock(5, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   LUNAR_ISLE_1(new WorldPoint(2163, 10347, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.SILVER), new Rock(1, MiningSiteLocation.Ore.GOLD), new Rock(1, MiningSiteLocation.Ore.GEM_ROCK), new Rock(6, MiningSiteLocation.Ore.LUNAR)}),
   LUNAR_ISLE_2(new WorldPoint(2165, 10325, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.GEM_ROCK), new Rock(4, MiningSiteLocation.Ore.LUNAR)}),
   LUNAR_ISLE_3(new WorldPoint(2140, 10318, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.SILVER), new Rock(3, MiningSiteLocation.Ore.LUNAR)}),
   LUNAR_ISLE_4(new WorldPoint(2125, 10327, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.GOLD), new Rock(1, MiningSiteLocation.Ore.LUNAR)}),
   LUNAR_ISLE_5(new WorldPoint(2124, 10342, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.GOLD), new Rock(5, MiningSiteLocation.Ore.LUNAR)}),
   MINING_GUILD_AMETHYST(new WorldPoint(3022, 9704, 0), new Rock[]{new Rock(26, MiningSiteLocation.Ore.AMETHYST)}),
   MINING_GUILD_NORTH(new WorldPoint(3040, 9740, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.IRON), new Rock(37, MiningSiteLocation.Ore.COAL), new Rock(5, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   MINING_GUILD_SOUTH(new WorldPoint(3032, 9720, 0), new Rock[]{new Rock(8, MiningSiteLocation.Ore.IRON), new Rock(20, MiningSiteLocation.Ore.COAL), new Rock(10, MiningSiteLocation.Ore.MITHRIL), new Rock(8, MiningSiteLocation.Ore.ADAMANTITE), new Rock(2, MiningSiteLocation.Ore.RUNITE)}),
   MINING_GUILD_WEST(new WorldPoint(3006, 9711, 0), new Rock[]{new Rock(36, MiningSiteLocation.Ore.AMETHYST)}),
   MISCELLANIA(new WorldPoint(2526, 3891, 0), new Rock[]{new Rock(9, MiningSiteLocation.Ore.COAL)}),
   MISCELLANIA_DUNGEON(new WorldPoint(2504, 10287, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.COAL)}),
   MOR_UL_REK_NORTH(new WorldPoint(2458, 5167, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.SILVER), new Rock(3, MiningSiteLocation.Ore.GOLD)}),
   MOR_UL_REK_SOUTH_EAST(new WorldPoint(2513, 5074, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.SILVER), new Rock(7, MiningSiteLocation.Ore.GOLD)}),
   MOR_UL_REK_SOUTH_WEST(new WorldPoint(2499, 5062, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE), new Rock(3, MiningSiteLocation.Ore.RUNITE)}),
   MOUNT_KARUULM(new WorldPoint(1278, 3814, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(5, MiningSiteLocation.Ore.COAL)}),
   MYTHS_GUILD(new WorldPoint(1936, 9020, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.ADAMANTITE), new Rock(2, MiningSiteLocation.Ore.RUNITE)}),
   NECROPOLIS(new WorldPoint(3319, 2708, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.SANDSTONE), new Rock(5, MiningSiteLocation.Ore.GRANITE)}),
   OGRESS_SETTLEMENT(new WorldPoint(1977, 9041, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.MITHRIL), new Rock(1, MiningSiteLocation.Ore.ADAMANTITE)}),
   ONYX_CREST(new WorldPoint(2978, 2254, 0), new Rock[]{new Rock(8, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.SILVER), new Rock(6, MiningSiteLocation.Ore.LEAD), new Rock(4, MiningSiteLocation.Ore.GEM_ROCK), new Rock(4, MiningSiteLocation.Ore.GOLD)}),
   PANDEMONIUM(new WorldPoint(3055, 9367, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COPPER), new Rock(3, MiningSiteLocation.Ore.TIN), new Rock(5, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(8, MiningSiteLocation.Ore.LEAD)}),
   PIRATES_HIDEOUT(new WorldPoint(3056, 3945, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.COAL), new Rock(4, MiningSiteLocation.Ore.MITHRIL), new Rock(1, MiningSiteLocation.Ore.ADAMANTITE)}),
   PISCARILLIUS(new WorldPoint(1759, 3718, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.COPPER), new Rock(6, MiningSiteLocation.Ore.TIN), new Rock(5, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.SILVER), new Rock(1, MiningSiteLocation.Ore.MITHRIL)}),
   PISCATORIS(new WorldPoint(2337, 3640, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.CLAY), new Rock(2, MiningSiteLocation.Ore.COPPER), new Rock(2, MiningSiteLocation.Ore.TIN), new Rock(3, MiningSiteLocation.Ore.IRON)}),
   PORT_KHAZARD(new WorldPoint(2651, 3172, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.COPPER), new Rock(2, MiningSiteLocation.Ore.TIN), new Rock(2, MiningSiteLocation.Ore.MITHRIL)}),
   RALOS_RISE_SOUTH(new WorldPoint(1480, 3086, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.COPPER), new Rock(4, MiningSiteLocation.Ore.TIN), new Rock(4, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.ADAMANTITE)}),
   RELLEKKA(new WorldPoint(2682, 3704, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.CLAY), new Rock(3, MiningSiteLocation.Ore.SILVER), new Rock(7, MiningSiteLocation.Ore.COAL)}),
   RIMMINGTON(new WorldPoint(2977, 3240, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.CLAY), new Rock(5, MiningSiteLocation.Ore.COPPER), new Rock(2, MiningSiteLocation.Ore.TIN), new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.GOLD)}),
   RUINS_OF_ULLEK(new WorldPoint(3377, 2777, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.CLAY), new Rock(5, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(1, MiningSiteLocation.Ore.ADAMANTITE)}),
   RUINS_OF_UNKAH(new WorldPoint(3172, 2871, 0), new Rock[]{new Rock(1, MiningSiteLocation.Ore.COPPER), new Rock(1, MiningSiteLocation.Ore.TIN), new Rock(5, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.SILVER), new Rock(1, MiningSiteLocation.Ore.COAL)}),
   SALT_MINE(new WorldPoint(2835, 10334, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.BASALT), new Rock(15, MiningSiteLocation.Ore.TE_SALT), new Rock(12, MiningSiteLocation.Ore.EFH_SALT), new Rock(12, MiningSiteLocation.Ore.URT_SALT)}),
   SALVAGER_OVERLOOK_EAST(new WorldPoint(1671, 3284, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.IRON), new Rock(5, MiningSiteLocation.Ore.GOLD)}),
   SALVAGER_OVERLOOK_WEST(new WorldPoint(1631, 3277, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.COAL), new Rock(3, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   SHAYZIEN(new WorldPoint(1601, 3645, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.CLAY), new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.ADAMANTITE)}),
   SHILO_VILLAGE_SURFACE(new WorldPoint(2822, 3001, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.GEM_ROCK)}),
   SILVAREA(new WorldPoint(3371, 3498, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.LIMESTONE)}),
   SLEPE_UNDERGROUND(new WorldPoint(3888, 9749, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(14, MiningSiteLocation.Ore.COAL)}),
   STONECUTTER_OUTPOST(new WorldPoint(1747, 2955, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.CLAY), new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.SILVER), new Rock(10, MiningSiteLocation.Ore.COAL), new Rock(3, MiningSiteLocation.Ore.GOLD), new Rock(4, MiningSiteLocation.Ore.MITHRIL)}),
   STRANGLEWOOD_MINE(new WorldPoint(1169, 3323, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(3, MiningSiteLocation.Ore.MITHRIL), new Rock(3, MiningSiteLocation.Ore.ADAMANTITE), new Rock(1, MiningSiteLocation.Ore.RUNITE)}),
   SUNBLEAK_CAVE_NORTH(new WorldPoint(2221, 9003, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.COAL), new Rock(5, MiningSiteLocation.Ore.MITHRIL)}),
   SUNBLEAK_CAVE_SOUTH(new WorldPoint(2224, 8977, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.COAL), new Rock(4, MiningSiteLocation.Ore.ADAMANTITE)}),
   TLATI_RAINFOREST_EAST(new WorldPoint(1352, 3112, 0), new Rock[]{new Rock(7, MiningSiteLocation.Ore.IRON), new Rock(4, MiningSiteLocation.Ore.COAL), new Rock(5, MiningSiteLocation.Ore.GOLD), new Rock(4, MiningSiteLocation.Ore.MITHRIL), new Rock(4, MiningSiteLocation.Ore.ADAMANTITE), new Rock(1, MiningSiteLocation.Ore.GEMSTONE_CRAB)}),
   TLATI_RAINFOREST_NORTH(new WorldPoint(1272, 3173, 0), new Rock[]{new Rock(9, MiningSiteLocation.Ore.SILVER), new Rock(8, MiningSiteLocation.Ore.CLAY), new Rock(1, MiningSiteLocation.Ore.GEMSTONE_CRAB)}),
   TLATI_RAINFOREST_SOUTH(new WorldPoint(1238, 3043, 0), new Rock[]{new Rock(13, MiningSiteLocation.Ore.COPPER), new Rock(10, MiningSiteLocation.Ore.IRON), new Rock(7, MiningSiteLocation.Ore.COAL), new Rock(1, MiningSiteLocation.Ore.GEMSTONE_CRAB)}),
   TRAHEARN(new WorldPoint(3295, 12387, 0), new Rock[]{new Rock(26, MiningSiteLocation.Ore.IRON), new Rock(8, MiningSiteLocation.Ore.SILVER), new Rock(19, MiningSiteLocation.Ore.COAL), new Rock(14, MiningSiteLocation.Ore.GOLD), new Rock(7, MiningSiteLocation.Ore.MITHRIL), new Rock(10, MiningSiteLocation.Ore.SOFT_CLAY), new Rock(7, MiningSiteLocation.Ore.ADAMANTITE), new Rock(4, MiningSiteLocation.Ore.RUNITE)}),
   UZER(new WorldPoint(3415, 3160, 0), new Rock[]{new Rock(10, MiningSiteLocation.Ore.CLAY)}),
   VARROCK_SOUTH_EAST(new WorldPoint(3286, 3365, 0), new Rock[]{new Rock(9, MiningSiteLocation.Ore.COPPER), new Rock(6, MiningSiteLocation.Ore.TIN), new Rock(4, MiningSiteLocation.Ore.IRON)}),
   VARROCK_SOUTH_WEST(new WorldPoint(3176, 3370, 0), new Rock[]{new Rock(3, MiningSiteLocation.Ore.CLAY), new Rock(8, MiningSiteLocation.Ore.TIN), new Rock(3, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.SILVER)}),
   VATRACHOS_NORTH(new WorldPoint(1891, 2987, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.CLAY), new Rock(2, MiningSiteLocation.Ore.SILVER), new Rock(2, MiningSiteLocation.Ore.GOLD), new Rock(1, MiningSiteLocation.Ore.MITHRIL)}),
   VATRACHOS_SOUTH(new WorldPoint(1893, 2975, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.IRON), new Rock(2, MiningSiteLocation.Ore.COAL), new Rock(2, MiningSiteLocation.Ore.CLAY), new Rock(4, MiningSiteLocation.Ore.GOLD)}),
   VERDANT_VALLEY(new WorldPoint(3766, 3757, 0), true, new Rock[]{new Rock(3, MiningSiteLocation.Ore.IRON)}),
   WILDERNESS_RESOURCE_AREA(new WorldPoint(3192, 3930, 0), new Rock[]{new Rock(6, MiningSiteLocation.Ore.IRON), new Rock(11, MiningSiteLocation.Ore.COAL), new Rock(4, MiningSiteLocation.Ore.GOLD), new Rock(1, MiningSiteLocation.Ore.MITHRIL), new Rock(6, MiningSiteLocation.Ore.ADAMANTITE)}),
   WILDERNESS_SOUTH(new WorldPoint(3104, 3569, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.COAL)}),
   WILDERNESS_SOUTH_WEST(new WorldPoint(3013, 3589, 0), new Rock[]{new Rock(34, MiningSiteLocation.Ore.COAL)}),
   YNYSDAIL(new WorldPoint(2224, 3476, 0), new Rock[]{new Rock(4, MiningSiteLocation.Ore.IRON), new Rock(3, MiningSiteLocation.Ore.NICKEL)}),
   YNYSDAIL_CAVERN_NORTH(new WorldPoint(2267, 9892, 0), new Rock[]{new Rock(5, MiningSiteLocation.Ore.MITHRIL), new Rock(8, MiningSiteLocation.Ore.ADAMANTITE)}),
   YNYSDAIL_CAVERN_WEST(new WorldPoint(2249, 9878, 0), new Rock[]{new Rock(2, MiningSiteLocation.Ore.MITHRIL), new Rock(2, MiningSiteLocation.Ore.RUNITE)});

   private final WorldPoint location;
   private final String tooltip;
   private final boolean iconRequired;

   private MiningSiteLocation(WorldPoint location, Rock... rocks) {
      this(location, false, rocks);
   }

   private MiningSiteLocation(WorldPoint location, boolean iconRequired, Rock... rocks) {
      this.location = location;
      this.iconRequired = iconRequired;
      this.tooltip = this.createTooltip(rocks);
   }

   private String createTooltip(Rock[] rocks) {
      return Joiner.on("<br>").join(rocks);
   }

   public WorldPoint getLocation() {
      return this.location;
   }

   public String getTooltip() {
      return this.tooltip;
   }

   public boolean isIconRequired() {
      return this.iconRequired;
   }

   private static final class Rock {
      private final int count;
      private final Ore ore;

      public String toString() {
         int var10000 = this.count;
         return "" + var10000 + " " + String.valueOf(this.ore);
      }

      public Rock(int count, Ore ore) {
         this.count = count;
         this.ore = ore;
      }

      public int getCount() {
         return this.count;
      }

      public Ore getOre() {
         return this.ore;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Rock)) {
            return false;
         } else {
            Rock other = (Rock)o;
            if (this.getCount() != other.getCount()) {
               return false;
            } else {
               Object this$ore = this.getOre();
               Object other$ore = other.getOre();
               if (this$ore == null) {
                  if (other$ore != null) {
                     return false;
                  }
               } else if (!this$ore.equals(other$ore)) {
                  return false;
               }

               return true;
            }
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         result = result * 59 + this.getCount();
         Object $ore = this.getOre();
         result = result * 59 + ($ore == null ? 43 : $ore.hashCode());
         return result;
      }
   }

   private static enum Ore {
      ROCK("Rock"),
      CLAY("Clay"),
      COPPER("Copper"),
      TIN("Tin"),
      LIMESTONE("Limestone"),
      BLURITE("Blurite"),
      BARRONITE("Barronite"),
      IRON("Iron"),
      ELEMENTAL("Elemental"),
      SILVER("Silver"),
      COAL("Coal"),
      SANDSTONE("Sandstone"),
      DENSE_ESSENCE("Dense essence"),
      DAEYALT_ESSENCE("Daeyalt essence"),
      GOLD("Gold"),
      GEM_ROCK("Gem rock"),
      CALCIFIED_ROCKS("Calcified rocks"),
      HARD_ROCK("Hard rock"),
      VOLCANIC_SULPHUR("Volcanic sulphur"),
      GRANITE("Granite"),
      MITHRIL("Mithril"),
      LUNAR("Lunar"),
      LOVAKITE("Lovakite"),
      ADAMANTITE("Adamantite"),
      SOFT_CLAY("Soft clay"),
      BASALT("Basalt"),
      TE_SALT("Te salt"),
      EFH_SALT("Efh salt"),
      URT_SALT("Urt salt"),
      RUNITE("Runite"),
      AMETHYST("Amethyst"),
      GEMSTONE_CRAB("Gemstone Crab"),
      LEAD("Lead"),
      NICKEL("Nickel");

      private final String name;

      public String toString() {
         return this.name;
      }

      private Ore(String name) {
         this.name = name;
      }
   }
}
