package net.runelite.client.game;

import net.runelite.api.Client;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

public enum AgilityShortcut {
   GENERIC_SHORTCUT(1, "Shortcut", (WorldPoint)null, new int[]{3790, 3791, 29993, 30938, 30939, 30940, 30941, 30606, 30198, 12982, 2830, 2831, 5948, 5949, 6673, 16115, 2618, 51, 30959, 30966, 30766, 30767, 30964, 30962, 30960, 30961, 2186, 993, 8729, 31485, 19846, 19847, 18416, 3522, 2959, 2960, 2961, 2962, 2963, 2964, 54720, 54721, 54722}),
   WEISS_BROKEN_FENCE(1, "Shortcut", (WorldPoint)null, new int[]{46815}) {
      public boolean matches(Client client, TileObject object) {
         assert object.getId() == 46815;

         int multilocId = client.getObjectDefinition(object.getId()).getImpostor().getId();
         return multilocId == 46817;
      }
   },
   BRIMHAVEN_DUNGEON_MEDIUM_PIPE_RETURN(1, "Pipe Squeeze", (WorldPoint)null, new WorldPoint(2698, 9491, 0), new int[]{21727}),
   BRIMHAVEN_DUNGEON_PIPE_RETURN(1, "Pipe Squeeze", (WorldPoint)null, new WorldPoint(2655, 9573, 0), new int[]{21728}),
   BRIMHAVEN_DUNGEON_STEPPING_STONES_RETURN(1, "Pipe Squeeze", (WorldPoint)null, new int[]{21739}),
   BRIMHAVEN_DUNGEON_LOG_BALANCE_RETURN(1, "Log Balance", (WorldPoint)null, new int[]{20884}),
   AGILITY_PYRAMID_ROCKS_WEST(1, "Rocks", (WorldPoint)null, new int[]{11948}),
   CAIRN_ISLE_CLIMBING_ROCKS(1, "Rocks", (WorldPoint)null, new int[]{2236}),
   KARAMJA_GLIDER_LOG(1, "Log Balance", new WorldPoint(2906, 3050, 0), new int[]{23644}),
   FALADOR_CRUMBLING_WALL(5, "Crumbling Wall", new WorldPoint(2936, 3357, 0), new int[]{24222}),
   YANILLE_CLIMBING_ROCKS(5, "Climbing rocks", (WorldPoint)null, new int[]{23543}),
   RIVER_LUM_GRAPPLE_WEST(8, "Grapple Broken Raft", new WorldPoint(3245, 3179, 0), new int[]{17068}),
   RIVER_LUM_GRAPPLE_EAST(8, "Grapple Broken Raft", new WorldPoint(3258, 3179, 0), new int[]{17068}),
   CORSAIR_COVE_ROCKS(10, "Rocks", new WorldPoint(2545, 2871, 0), new int[]{31757}),
   KARAMJA_MOSS_GIANT_SWING(10, "Rope", (WorldPoint)null, new int[]{23568, 23569}),
   FALADOR_GRAPPLE_WALL(11, "Grapple Wall", new WorldPoint(3031, 3391, 0), new int[]{17049, 17050}),
   BRIMHAVEN_DUNGEON_STEPPING_STONES(12, "Stepping Stones", (WorldPoint)null, new int[]{21738}),
   VARROCK_SOUTH_FENCE(13, "Fence", new WorldPoint(3239, 3334, 0), new int[]{16518}),
   GOBLIN_VILLAGE_WALL(14, "Wall", new WorldPoint(2925, 3523, 0), new int[]{16468}),
   CORSAIR_COVE_DUNGEON_PILLAR(15, "Pillar Jump", new WorldPoint(1980, 8996, 0), new int[]{31809}),
   EDGEVILLE_DUNGEON_MONKEYBARS(15, "Monkey Bars", (WorldPoint)null, new int[]{23566}),
   TROLLHEIM_ROCKS(15, "Rocks", (WorldPoint)null, new int[]{3748}),
   TROLLHEIM_CLIFF_SCRAMBLE_NORTHWEST_SOUTH(15, "Rocks", new WorldPoint(2886, 3684, 0), new int[]{3803, 3804}),
   YANILLE_UNDERWALL_TUNNEL(16, "Underwall Tunnel", new WorldPoint(2574, 3109, 0), new int[]{16520, 16519}),
   KOUREND_CATACOMBS_SOUTH_WEST_CRACK_NORTH(17, "Crack", new WorldPoint(1647, 10008, 0), new int[]{28892}),
   KOUREND_CATACOMBS_SOUTH_WEST_CRACK_SOUTH(17, "Crack", new WorldPoint(1645, 10001, 0), new int[]{28892}),
   CRABCLAW_CAVES_CREVICE(18, "Crevice", new WorldPoint(1710, 9822, 0), new int[]{31695, 31696}),
   CRABCLAW_CAVES_ROCKS(18, "Rocks", new WorldPoint(1687, 9802, 0), new int[]{31697}),
   CRABCLAW_CAVES_STEPPING_STONES(18, "Stepping Stones", new WorldPoint(1704, 9800, 0), new int[]{31699}),
   SLAYER_TOWER_GROUND_WINDOW(18, "Window", new WorldPoint(3442, 3532, 0), new int[]{57679}),
   YANILLE_WATCHTOWER_TRELLIS(18, "Trellis", (WorldPoint)null, new int[]{20056}),
   COAL_TRUCKS_LOG_BALANCE(20, "Log Balance", new WorldPoint(2598, 3475, 0), new int[]{23274}),
   GRAND_EXCHANGE_UNDERWALL_TUNNEL(21, "Underwall Tunnel", new WorldPoint(3139, 3515, 0), new int[]{16529, 16530}),
   BRIMHAVEN_DUNGEON_PIPE(22, "Pipe Squeeze", new WorldPoint(2654, 9569, 0), new int[]{21728}),
   OBSERVATORY_SCALE_CLIFF(23, "Grapple Rocks", new WorldPoint(2447, 3155, 0), new int[]{31849, 31852}),
   NEMUS_RETREAT_WALL_SOUTH_EAST(24, "Broken Wall", new WorldPoint(1386, 3302, 0), new int[]{56996}),
   NEMUS_RETREAT_WALL_SOUTH_WEST(24, "Broken Wall", new WorldPoint(1368, 3295, 0), new int[]{56996}),
   NEMUS_RETREAT_WALL_EAST(24, "Broken Wall", new WorldPoint(1389, 3309, 0), new int[]{56997}),
   EAGLES_PEAK_ROCK_CLIMB(25, "Rock Climb", new WorldPoint(2320, 3499, 0), new int[]{19849}),
   BURGH_AGILITY_SHORTCUT_FENCE(25, "Broken Fence", new WorldPoint(3470, 3219, 0), new int[]{12776}),
   FALADOR_UNDERWALL_TUNNEL(26, "Underwall Tunnel", new WorldPoint(2947, 3313, 0), new int[]{16527, 16528}),
   KOUREND_CATACOMBS_STONES_NORTH(28, "Stones", new WorldPoint(1613, 10071, 0), new int[]{28893}),
   KOUREND_CATACOMBS_STONES_SOUTH(28, "Stones", new WorldPoint(1609, 10060, 0), new int[]{28893}),
   MOUNT_KARUULM_LOWER(29, "Rocks", new WorldPoint(1324, 3782, 0), new int[]{34397}),
   CORSAIR_COVE_RESOURCE_ROCKS(30, "Rocks", new WorldPoint(2486, 2898, 0), new int[]{31758, 31759}),
   SOUTHEAST_KARAMJA_STEPPING_STONES(30, "Stepping Stones", new WorldPoint(2924, 2946, 0), new int[]{23645, 23646, 23647}),
   BRIMHAVEN_DUNGEON_LOG_BALANCE(30, "Log Balance", (WorldPoint)null, new int[]{20882}),
   AGILITY_PYRAMID_ROCKS_EAST(30, "Rocks", (WorldPoint)null, new int[]{11949}),
   DRAYNOR_MANOR_STEPPING_STONES(31, "Stepping Stones", new WorldPoint(3150, 3362, 0), new int[]{16533}),
   CATHERBY_CLIFFSIDE_GRAPPLE(32, "Grapple Rock", new WorldPoint(2868, 3429, 0), new int[]{17042}),
   CAIRN_ISLE_ROCKS(32, "Rocks", (WorldPoint)null, new int[]{2231}),
   SHILO_VILLAGE_STEPPING_STONES(32, "Stepping Stones", new WorldPoint(2863, 2974, 0), new int[]{16466}),
   ARDOUGNE_LOG_BALANCE(33, "Log Balance", new WorldPoint(2602, 3336, 0), new int[]{16546, 16547, 16548}),
   NEMUS_RETREAT_TUNNEL(33, "Tunnel", new WorldPoint(1367, 3325, 0), new int[]{56989}),
   BRIMHAVEN_DUNGEON_MEDIUM_PIPE(34, "Pipe Squeeze", (WorldPoint)null, new WorldPoint(2698, 9501, 0), new int[]{21727}),
   KOUREND_CATACOMBS_NORTH_EAST_CRACK_NORTH(34, "Crack", new WorldPoint(1715, 10057, 0), new int[]{28892}),
   KOUREND_CATACOMBS_NORTH_EAST_CRACK_SOUTH(34, "Crack", new WorldPoint(1705, 10077, 0), new int[]{28892}),
   VARROCK_CASTLE_GARDEN_TRELLIS(35, "Trellis", new WorldPoint(3227, 3471, 0), new int[]{2149}) {
      public boolean matches(Client client, TileObject object) {
         return Quest.GARDEN_OF_TRANQUILLITY.getState(client) == QuestState.FINISHED;
      }
   },
   CATHERBY_OBELISK_GRAPPLE(36, "Grapple Rock", (WorldPoint)null, new int[]{17062}),
   NEMUS_RETREAT_STEPPING_STONES(36, "Stepping Stones", new WorldPoint(1395, 3309, 0), new int[]{56988}),
   GNOME_STRONGHOLD_ROCKS(37, "Rocks", new WorldPoint(2485, 3515, 0), new int[]{16534, 16535}),
   AL_KHARID_MINING_PITCLIFF_SCRAMBLE(38, "Rocks", new WorldPoint(3305, 3315, 0), new int[]{16549, 16550}),
   YANILLE_WALL_GRAPPLE(39, "Grapple Wall", new WorldPoint(2552, 3072, 0), new int[]{17047}),
   NEITIZNOT_BRIDGE_REPAIR(0, "Bridge Repair - Quest", new WorldPoint(2315, 3828, 0), new int[]{21306, 21307}),
   NEITIZNOT_BRIDGE_SOUTHEAST(0, "Rope Bridge", (WorldPoint)null, new int[]{21308, 21309}),
   NEITIZNOT_BRIDGE_NORTHWEST(0, "Rope Bridge", (WorldPoint)null, new int[]{21310, 21311}),
   NEITIZNOT_BRIDGE_NORTH(0, "Rope Bridge", (WorldPoint)null, new int[]{21312, 21313}),
   NEITIZNOT_BRIDGE_NORTHEAST(40, "Broken Rope bridge", (WorldPoint)null, new int[]{21314, 21315}),
   KOUREND_LAKE_JUMP_EAST(40, "Stepping Stones", new WorldPoint(1612, 3570, 0), new int[]{29729, 29730}),
   KOUREND_LAKE_JUMP_WEST(40, "Stepping Stones", new WorldPoint(1604, 3572, 0), new int[]{29729, 29730}),
   TLATI_RAINFORST_BALANCE(40, "Log Balance", new WorldPoint(1283, 3144, 0), new int[]{57593}),
   YANILLE_DUNGEON_BALANCE(40, "Balancing Ledge", (WorldPoint)null, new int[]{23548}),
   AUBURNVALE_ROCK_SCRAMBLE(41, "Rocks", new WorldPoint(1393, 3322, 0), new int[]{56994}),
   TROLLHEIM_EASY_CLIFF_SCRAMBLE(41, "Rocks", new WorldPoint(2869, 3670, 0), new int[]{16521}),
   DWARVEN_MINE_NARROW_CREVICE(42, "Narrow Crevice", new WorldPoint(3034, 9806, 0), new int[]{16543}),
   DRAYNOR_UNDERWALL_TUNNEL(42, "Underwall Tunnel", new WorldPoint(3068, 3261, 0), new int[]{19032, 19036}),
   TROLLHEIM_MEDIUM_CLIFF_SCRAMBLE_SOUTHWEST(43, "Rocks", new WorldPoint(2876, 3666, 0), new int[]{16522}),
   TLATI_RAINFORST_CLIFF_SCRAMBLE(43, "Rocks", new WorldPoint(1271, 3001, 0), new int[]{57605, 57604}),
   SLAYER_DUNGEON_CHASM_JUMP(43, "Spiked Blades", new WorldPoint(2770, 10003, 0), new int[]{16544}),
   TROLLHEIM_ADVANCED_CLIFF_SCRAMBLE(44, "Rocks", new WorldPoint(2907, 3686, 0), new int[]{16523}),
   AUBURN_VALLEY_LOG_BALANCE_SOUTH(45, "Log Balance", new WorldPoint(1401, 3287, 0), new int[]{56990}),
   AUBURN_VALLEY_LOG_BALANCE_NORTH(45, "Log Balance", new WorldPoint(1453, 3332, 0), new int[]{56991}),
   PROUDSPIRE_LOWER_ROCKS(45, "Rocks", new WorldPoint(1588, 3260, 0), new int[]{56983}),
   KOUREND_RIVER_STEPPING_STONES(45, "Stepping Stones", new WorldPoint(1720, 3551, 0), new int[]{29728}),
   TIRANNWN_LOG_BALANCE(45, "Log Balance", (WorldPoint)null, new int[]{3933, 3931, 3930, 3929, 3932}),
   COSMIC_ALTAR_MEDIUM_WALKWAY(46, "Narrow Walkway", new WorldPoint(2399, 4403, 0), new int[]{17002}),
   DEEP_WILDERNESS_DUNGEON_CREVICE_NORTH(46, "Narrow Crevice", new WorldPoint(3047, 10335, 0), new int[]{19043}),
   DEEP_WILDERNESS_DUNGEON_CREVICE_SOUTH(46, "Narrow Crevice", new WorldPoint(3045, 10327, 0), new int[]{19043}),
   TONALI_CAVERN_STEPPING_STONE(46, "Stepping Stones", (WorldPoint)null, new int[]{56717}),
   TONALI_CAVERN_LOG_BALANCE(46, "Log Balance", (WorldPoint)null, new int[]{56718}),
   TROLLHEIM_HARD_CLIFF_SCRAMBLE(47, "Rocks", new WorldPoint(2902, 3680, 0), new int[]{16524}),
   RALOS_RISE_ROCK_CLIMB(47, "Rocks", new WorldPoint(1458, 3129, 0), new int[]{51931}),
   FREMENNIK_LOG_BALANCE(48, "Log Balance", new WorldPoint(2721, 3591, 0), new int[]{16540, 16541, 16542}),
   YANILLE_DUNGEON_PIPE_SQUEEZE(49, "Pipe Squeeze", (WorldPoint)null, new int[]{23140}),
   ARCEUUS_ESSENCE_MINE_BOULDER(49, "Boulder", new WorldPoint(1774, 3888, 0), new int[]{27990}),
   MORYTANIA_STEPPING_STONE(50, "Stepping Stone", new WorldPoint(3418, 3326, 0), new int[]{13504}),
   SHAMAN_CAVES_JAGGED_WALL(50, "Jagged wall", (WorldPoint)null, new int[]{2926}),
   GREAT_CONCH_CLIFF_SHORTCUT_TOWN(50, "Rock Climb", new WorldPoint(3180, 2433, 0), new int[]{57928, 57927}),
   VARROCK_SEWERS_PIPE_SQUEEZE(51, "Pipe Squeeze", new WorldPoint(3152, 9905, 0), new int[]{16511}),
   ARCEUUS_ESSENCE_MINE_EAST_SCRAMBLE(52, "Rock Climb", new WorldPoint(1770, 3851, 0), new int[]{27987, 27988}),
   ANGLERS_RETREAT_SHORTCUT(52, "Rocks", new WorldPoint(2475, 2729, 0), new int[]{60126}),
   GREAT_CONCH_CLIFF_SHORTCUT_EAST_1(52, "Rock Climb", new WorldPoint(3235, 2388, 0), new int[]{57932, 57931}),
   KARAMJA_VOLCANO_GRAPPLE_NORTH(53, "Grapple Rock", new WorldPoint(2873, 3143, 0), new int[]{17074}),
   KARAMJA_VOLCANO_GRAPPLE_SOUTH(53, "Grapple Rock", new WorldPoint(2874, 3128, 0), new int[]{17074}),
   ALDARIN_ROCKS(54, "Rocks", new WorldPoint(1340, 2916, 0), new int[]{54775, 54776}),
   MOTHERLODE_MINE_WALL_EAST(54, "Wall", new WorldPoint(3124, 9703, 0), new int[]{10047}),
   MOTHERLODE_MINE_WALL_WEST(54, "Wall", new WorldPoint(3118, 9702, 0), new int[]{10047}),
   MISCELLANIA_DOCK_STEPPING_STONE(55, "Stepping Stone", new WorldPoint(2572, 3862, 0), new int[]{11768}),
   GREAT_CONCH_CLIFF_SHORTCUT_SOUTHEAST_BOTTOM(55, "Rock Climb", new WorldPoint(3272, 2330, 0), new int[]{57930, 57929}),
   TEMPLE_OF_EYE_RUBBLE(56, "Rubble", (WorldPoint)null, new int[]{43724, 43726}),
   BRIMHAVEN_DUNGEON_EAST_STEPPING_STONES_NORTH(56, "Stepping Stones", new WorldPoint(2685, 9547, 0), new int[]{19040}),
   BRIMHAVEN_DUNGEON_EAST_STEPPING_STONES_SOUTH(56, "Stepping Stones", new WorldPoint(2693, 9529, 0), new int[]{19040}),
   ISAFDAR_FOREST_OBSTACLES(56, "Trap", (WorldPoint)null, new int[]{3938, 3939, 3998, 3999, 3937, 3923, 3924, 3925, 3922, 3920, 3921}),
   RELEKKA_EAST_FENCE(57, "Fence", new WorldPoint(2688, 3697, 0), new int[]{544}),
   YANILLE_DUNGEON_MONKEY_BARS(57, "Monkey Bars", (WorldPoint)null, new int[]{23567}),
   GREAT_CONCH_CLIFF_SHORTCUT_EAST_2(57, "Rock Climb", new WorldPoint(3256, 2397, 0), new int[]{57934, 57933}),
   PHASMATYS_ECTOPOOL_SHORTCUT(58, "Weathered Wall", (WorldPoint)null, new int[]{16525, 16526}),
   ELVEN_OVERPASS_CLIFF_SCRAMBLE(59, "Rocks", new WorldPoint(2345, 3300, 0), new int[]{16514, 16515}),
   ELVEN_OVERPASS_CLIFF_SCRAMBLE_PRIFDDINAS(59, "Rocks", new WorldPoint(3369, 6052, 0), new int[]{16514, 16515}),
   ASGARNIA_ICE_DUNGEON_TUNNEL_EAST(60, "Tunnel", new WorldPoint(2989, 9547, 0), new int[]{55988}),
   ASGARNIA_ICE_DUNGEON_TUNNEL_WEST(60, "Tunnel", new WorldPoint(2968, 9549, 0), new int[]{55989}),
   WILDERNESS_GWD_CLIMB_EAST(60, "Rocks", new WorldPoint(2943, 3770, 0), new int[]{26400, 26401, 26402, 26404, 26405, 26406}),
   WILDERNESS_GWD_CLIMB_WEST(60, "Rocks", new WorldPoint(2928, 3760, 0), new int[]{26400, 26401, 26402, 26404, 26405, 26406}),
   MOS_LEHARMLESS_STEPPING_STONE(60, "Stepping Stone", new WorldPoint(3710, 2970, 0), new int[]{19042}),
   WINTERTODT_GAP(60, "Gap", new WorldPoint(1629, 4023, 0), new int[]{29326}),
   UNGAEL_ICE(60, "Ice Chunks", new WorldPoint(2262, 4044, 0), new int[]{25337, 29868, 29869, 29870, 31822, 31823, 31990, 47324, 47325}),
   GWD_LITTLE_CRACK(60, "Little Crack", new WorldPoint(2900, 3712, 0), new int[]{26382}),
   SLAYER_TOWER_MEDIUM_CHAIN_FIRST(61, "Spikey Chain (Floor 1)", new WorldPoint(3421, 3550, 0), new int[]{16537}),
   SLAYER_TOWER_MEDIUM_CHAIN_SECOND(61, "Spikey Chain (Floor 2)", new WorldPoint(3420, 3551, 0), new int[]{16538}),
   SLAYER_DUNGEON_CREVICE(61, "Narrow Crevice", new WorldPoint(2729, 10008, 0), new int[]{16539}),
   GREAT_CONCH_STEPPING_STONE(61, "Stepping Stone", new WorldPoint(3208, 2394, 0), new int[]{57936}),
   LAGUNA_AURORAE_SHORTCUT_1(61, "Rocks", new WorldPoint(1152, 2804, 0), new int[]{60127}),
   LAGUNA_AURORAE_SHORTCUT_2(61, "Rocks", new WorldPoint(1142, 2803, 0), new int[]{60128}),
   MOUNT_KARUULM_UPPER(62, "Rocks", new WorldPoint(1322, 3791, 0), new int[]{34396}),
   NECROPOLIS_STEPPING_STONE_NORTH(62, "Stepping Stone", new WorldPoint(3293, 2706, 0), new int[]{43990}),
   NECROPOLIS_STEPPING_STONES_SOUTH(62, "Stepping Stones", new WorldPoint(3291, 2700, 0), new int[]{43989}),
   TAVERLEY_DUNGEON_RAILING(63, "Loose Railing", new WorldPoint(2935, 9811, 0), new int[]{28849}),
   FORTHOS_DUNGEON_SPIKED_BLADES(63, "Spiked Blades", new WorldPoint(1819, 9946, 0), new int[]{34834}),
   DARKMEYER_WALL(63, "Wall (Long rope)", new WorldPoint(3669, 3375, 0), new int[]{39541, 39542}),
   TROLLHEIM_WILDERNESS_ROCKS_EAST(64, "Rocks", new WorldPoint(2945, 3678, 0), new int[]{16545}),
   TROLLHEIM_WILDERNESS_ROCKS_WEST(64, "Rocks", new WorldPoint(2917, 3672, 0), new int[]{16545}),
   FOSSIL_ISLAND_VOLCANO(64, "Rope", new WorldPoint(3780, 3822, 0), new int[]{30916, 30917}),
   MORYTANIA_TEMPLE(65, "Loose Railing", new WorldPoint(3422, 3476, 0), new int[]{16998, 16999, 16552, 17000}),
   REVENANT_CAVES_GREEN_DRAGONS(65, "Jump", new WorldPoint(3220, 10086, 0), new int[]{31561}),
   COSMIC_ALTAR_ADVANCED_WALKWAY(66, "Narrow Walkway", new WorldPoint(2408, 4401, 0), new int[]{17002}),
   LUMBRIDGE_DESERT_STEPPING_STONE(66, "Stepping Stone", new WorldPoint(3210, 3135, 0), new int[]{16513}),
   TAVERLEY_WALL_CLIMBING_ROCKS(66, "Climbing rocks", new WorldPoint(2945, 3439, 0), new int[]{53255}),
   HEROES_GUILD_TUNNEL_EAST(67, "Crevice", new WorldPoint(2898, 9901, 0), new int[]{9739, 9740}),
   HEROES_GUILD_TUNNEL_WEST(67, "Crevice", new WorldPoint(2913, 9895, 0), new int[]{9739, 9740}),
   YANILLE_DUNGEON_RUBBLE_CLIMB(67, "Pile of Rubble", (WorldPoint)null, new int[]{23563, 23564}),
   ELVEN_OVERPASS_MEDIUM_CLIFF(68, "Rocks", new WorldPoint(2337, 3288, 0), new int[]{16514, 16515}),
   ICE_MOUNTAIN_WESTERN_SCRAMBLE(68, "Rocks", new WorldPoint(2998, 3484, 0), new int[]{47570, 47571}),
   ELVEN_OVERPASS_MEDIUM_CLIFF_PRIFDDINAS(68, "Rocks", new WorldPoint(3361, 6040, 0), new int[]{16514, 16515}),
   WEISS_OBSTACLES(68, "Shortcut", (WorldPoint)null, new int[]{33184, 33185, 33327, 33328, 33190, 33191, 33192}),
   WEISS_FARMING_PATCH_BOULDER(0, "Shortcut", (WorldPoint)null, new int[]{33312}),
   ARCEUUS_ESSENSE_NORTH(69, "Rock Climb", new WorldPoint(1759, 3873, 0), new int[]{34741}),
   FENKENSTRAIN_MAUSOLEUM_BRIDGE(69, "Bridge Jump", new WorldPoint(3504, 3560, 0), new int[]{57715, 57716, 57717, 57718}) {
      public boolean matches(Client client, TileObject object) {
         return client.getVarbitValue(4441) == 2 && client.getVarbitValue(5023) == 2;
      }
   },
   TAVERLEY_DUNGEON_PIPE_BLUE_DRAGON(70, "Pipe Squeeze", new WorldPoint(2886, 9798, 0), new int[]{16509}),
   TAVERLEY_DUNGEON_ROCKS_NORTH(70, "Rocks", new WorldPoint(2887, 9823, 0), new int[]{154, 14106}),
   TAVERLEY_DUNGEON_ROCKS_SOUTH(70, "Rocks", new WorldPoint(2887, 9631, 0), new int[]{154, 14106}),
   FOSSIL_ISLAND_HARDWOOD_NORTH(70, "Hole", new WorldPoint(3712, 3828, 0), new int[]{31481, 31482}),
   FOSSIL_ISLAND_HARDWOOD_SOUTH(70, "Hole", new WorldPoint(3714, 3816, 0), new int[]{31481, 31482}),
   AL_KHARID_WINDOW(70, "Window", new WorldPoint(3293, 3158, 0), new int[]{33344, 33348}) {
      public boolean matches(Client client, TileObject object) {
         return object.getId() != 33348 || object.getWorldLocation().equals(new WorldPoint(3295, 3158, 0));
      }
   },
   GWD_SARADOMIN_ROPE_NORTH(70, "Rope Descent", new WorldPoint(2912, 5300, 0), new int[]{26371, 26561}),
   GWD_SARADOMIN_ROPE_SOUTH(70, "Rope Descent", new WorldPoint(2951, 5267, 0), new int[]{26375, 26562}),
   GU_TANOTH_CRUMBLING_WALL(71, "Rocks", new WorldPoint(2545, 3032, 0), new int[]{40355, 40356}),
   POLLNIVNEACH_STEPPING_STONE(71, "Stepping stone", new WorldPoint(3371, 2958, 0), new int[]{53241}),
   SLAYER_TOWER_ADVANCED_CHAIN_FIRST(71, "Spikey Chain (Floor 2)", new WorldPoint(3447, 3578, 0), new int[]{16537}),
   SLAYER_TOWER_ADVANCED_CHAIN_SECOND(71, "Spikey Chain (Floor 3)", new WorldPoint(3446, 3576, 0), new int[]{16538}),
   PROUDSPIRE_UPPER_ROCKS(71, "Rocks", new WorldPoint(1576, 3251, 0), new int[]{56978, 56980}),
   STRONGHOLD_SLAYER_CAVE_TUNNEL(72, "Tunnel", new WorldPoint(2431, 9806, 0), new int[]{30174, 30175}),
   ASGARNIA_ICE_DUNGEON_BASIC_NORTH(72, "Tunnel", new WorldPoint(3025, 9570, 0), new int[]{42506}),
   ASGARNIA_ICE_DUNGEON_BASIC_SOUTH(72, "Tunnel", new WorldPoint(3033, 9559, 0), new int[]{42507}),
   CHAOS_TEMPLE_STEPPING_STONE(72, "Stepping stone", new WorldPoint(3267, 3628, 0), new int[]{53237}),
   BARROWS_WALL_JUMP(72, "Dry stone wall", new WorldPoint(3544, 3282, 0), new int[]{53256}),
   TROLL_STRONGHOLD_WALL_CLIMB(73, "Rocks", new WorldPoint(2841, 3694, 0), new int[]{16464}),
   COF_PLATFORM_TOP(73, "Platform Edge", new WorldPoint(1309, 10099, 0), new WorldPoint(1438, 10098, 2), new int[]{56371}),
   COF_PLATFORM_MID(73, "Jump", new WorldPoint(1314, 10002, 0), new WorldPoint(1443, 10097, 1), new int[]{56372}),
   ARCEUUS_ESSENSE_MINE_WEST(73, "Rock Climb", new WorldPoint(1742, 3853, 0), new int[]{27984, 27985}),
   LAVA_DRAGON_ISLE_JUMP(74, "Stepping Stone", new WorldPoint(3200, 3807, 0), new int[]{14918}),
   MEIYERDITCH_LAB_TUNNELS_NORTH(74, "Cave", new WorldPoint(3623, 9747, 0), new int[]{43755, 43756}),
   MEIYERDITCH_LAB_TUNNELS_SOUTH(74, "Cave", new WorldPoint(3618, 9722, 0), new int[]{43757, 43758}),
   FOSSIL_ISLAND_ZIPLINE(74, "Zipline", new WorldPoint(3764, 3883, 0), new int[]{57667}),
   MOKHAIOTL_PIT_JUMP(75, "Jump", (WorldPoint)null, new int[]{56610}),
   REVENANT_CAVES_DEMONS_JUMP(75, "Jump", new WorldPoint(3199, 10135, 0), new int[]{31561}),
   REVENANT_CAVES_ANKOU_EAST(75, "Jump", new WorldPoint(3201, 10195, 0), new int[]{31561}),
   REVENANT_CAVES_ANKOU_NORTH(75, "Jump", new WorldPoint(3180, 10209, 0), new int[]{31561}),
   DARKFROST_CLIFF_SCRAMBLE(76, "Rocks", new WorldPoint(1477, 3307, 0), new int[]{56986, 56985}),
   ZUL_ANDRA_ISLAND_CROSSING(76, "Stepping Stone", new WorldPoint(2156, 3073, 0), new int[]{10663}),
   WILDERNESS_SLAYER_CAVE_CREVICE_NORTH_EAST(77, "Crevice", new WorldPoint(3433, 10093, 0), new int[]{53259}),
   WILDERNESS_SLAYER_CAVE_CREVICE_SOUTH_EAST(77, "Crevice", new WorldPoint(3434, 10115, 0), new int[]{53259}),
   WILDERNESS_SLAYER_CAVE_CREVICE_NORTH_WEST(77, "Crevice", new WorldPoint(3341, 10149, 0), new int[]{53259}),
   WILDERNESS_SLAYER_CAVE_CREVICE_SOUTH_WEST(77, "Crevice", new WorldPoint(3333, 10119, 0), new int[]{53259}),
   IORWERTHS_DUNGEON_NORTHERN_SHORTCUT_EAST(78, "Tight Gap", new WorldPoint(3221, 12441, 0), new int[]{36692}),
   IORWERTHS_DUNGEON_NORTHERN_SHORTCUT_WEST(78, "Tight Gap", new WorldPoint(3215, 12441, 0), new int[]{36693}),
   SHILO_VILLAGE_ROCKS(79, "Rocks", new WorldPoint(2870, 3003, 0), new int[]{53240, 53238}),
   KHARAZI_JUNGLE_VINE_CLIMB(79, "Vine", new WorldPoint(2897, 2939, 0), new int[]{26884, 26886}),
   TAVERLEY_DUNGEON_SPIKED_BLADES(80, "Strange Floor", new WorldPoint(2877, 9813, 0), new int[]{16510}),
   SLAYER_TOWER_IVY(81, "Ivy", new WorldPoint(3417, 3533, 0), new int[]{57677}),
   SLAYER_TOWER_TOP_WINDOW(81, "Window", new WorldPoint(3419, 3534, 0), new int[]{57678}),
   WATERBIRTH_DUNGEON_CREVICE(81, "Crevice", new WorldPoint(2604, 10070, 0), new int[]{53258}),
   LAVA_MAZE_NORTH_JUMP(82, "Stepping Stone", new WorldPoint(3092, 3880, 0), new int[]{14917}),
   ASGARNIA_ICE_DUNGEON_ADEPT_WEST(82, "Tunnel", new WorldPoint(3012, 9549, 0), new int[]{53250}),
   ASGARNIA_ICE_DUNGEON_ADEPT_EAST(82, "Tunnel", new WorldPoint(3022, 9553, 0), new int[]{53251}),
   COF_SHORTCUT_TOP(83, "Chain", new WorldPoint(1307, 10076, 0), new WorldPoint(1436, 10075, 2), new int[]{56242}),
   GRIMSTONE_SHORTCUT_SOUTH(83, "Ledge", new WorldPoint(2901, 10454, 0), new int[]{60120}),
   IORWERTHS_DUNGEON_SOUTHERN_SHORTCUT_EAST(84, "Tight Gap", new WorldPoint(3241, 12420, 0), new int[]{36694}),
   IORWERTHS_DUNGEON_SOUTHERN_SHORTCUT_WEST(84, "Tight Gap", new WorldPoint(3231, 12420, 0), new int[]{36695}),
   CRANDOR_ROCK_CLIMB(84, "Rocks", new WorldPoint(2831, 3252, 0), new int[]{53236, 53234}),
   DEEPFIN_CAVE_SHORTCUT(84, "Crevice", new WorldPoint(2081, 9203, 0), new int[]{60146}),
   DEEPFIN_CAVE_SHORTCUT_ICON(84, "Crevice", new WorldPoint(2075, 9203, 0), new int[0]),
   ELVEN_ADVANCED_CLIFF_SCRAMBLE(85, "Rocks", new WorldPoint(2337, 3253, 0), new int[]{16514, 16515}),
   ELVEN_ADVANCED_CLIFF_SCRAMBLE_PRIFDDINAS(85, "Rocks", new WorldPoint(3361, 6005, 0), new int[]{16514, 16515}),
   WATERBIRTH_ISLAND_ROCKS(85, "Rocks", new WorldPoint(2546, 3750, 0), new int[]{53252, 53254}),
   DARKMEYER_WALL_ROCKS(86, "Rocks", new WorldPoint(3563, 3380, 0), new int[]{29044, 57666}) {
      public boolean matches(Client client, TileObject object) {
         return Quest.SINS_OF_THE_FATHER.getState(client) == QuestState.FINISHED;
      }
   },
   KALPHITE_WALL(86, "Crevice", new WorldPoint(3214, 9508, 0), new int[]{16465}),
   BRIMHAVEN_DUNGEON_VINE_EAST(87, "Vine", new WorldPoint(2672, 9582, 0), new int[]{26880, 26882}),
   BRIMHAVEN_DUNGEON_VINE_WEST(87, "Vine", new WorldPoint(2606, 9584, 0), new int[]{26880, 26882}),
   MOUNT_KARUULM_PIPE_SOUTH(88, "Pipe", new WorldPoint(1316, 10214, 0), new int[]{34655}),
   MOUNT_KARUULM_PIPE_NORTH(88, "Pipe", new WorldPoint(1345, 10230, 0), new int[]{34655}),
   REVENANT_CAVES_CHAMBER_JUMP(89, "Jump", new WorldPoint(3240, 10144, 0), new int[]{31561}),
   VIYELDI_ROCK_CLIMB(91, "Rocks", (WorldPoint)null, new int[]{40960, 40962}),
   MEIYERDITCH_LAB_ADVANCED_TUNNELS_WEST(93, "Cave", new WorldPoint(3499, 9738, 0), new int[]{43759}),
   MEIYERDITCH_LAB_ADVANCED_TUNNELS_MIDDLE(93, "Cave", new WorldPoint(3597, 9704, 0), new int[]{43840}),
   MEIYERDITCH_LAB_ADVANCED_TUNNELS_EAST(93, "Cave", new WorldPoint(3604, 9708, 0), new int[]{43762, 43763}),
   VIYELDI_CAVES_CREVICE(96, "Crevice", (WorldPoint)null, new int[]{53242});

   private final int level;
   private final String description;
   private final WorldPoint worldMapLocation;
   private final WorldPoint worldLocation;
   private final int[] obstacleIds;

   private AgilityShortcut(int level, String description, WorldPoint mapLocation, WorldPoint worldLocation, int... obstacleIds) {
      this.level = level;
      this.description = description;
      this.worldMapLocation = mapLocation;
      this.worldLocation = worldLocation;
      this.obstacleIds = obstacleIds;
   }

   private AgilityShortcut(int level, String description, WorldPoint location, int... obstacleIds) {
      this(level, description, location, location, obstacleIds);
   }

   public String getTooltip() {
      return this.description + " - Level " + this.level;
   }

   public boolean matches(Client client, TileObject object) {
      return true;
   }

   public int getLevel() {
      return this.level;
   }

   public String getDescription() {
      return this.description;
   }

   public WorldPoint getWorldMapLocation() {
      return this.worldMapLocation;
   }

   public WorldPoint getWorldLocation() {
      return this.worldLocation;
   }

   public int[] getObstacleIds() {
      return this.obstacleIds;
   }
}
