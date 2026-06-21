package net.runelite.client.hiscore;

import java.util.Set;
import net.runelite.api.WorldType;
import okhttp3.HttpUrl;

public enum HiscoreEndpoint {
   NORMAL("Normal", "https://services.runescape.com/m=hiscore_oldschool/index_lite.json"),
   IRONMAN("Ironman", "https://services.runescape.com/m=hiscore_oldschool_ironman/index_lite.json"),
   HARDCORE_IRONMAN("Hardcore Ironman", "https://services.runescape.com/m=hiscore_oldschool_hardcore_ironman/index_lite.json"),
   ULTIMATE_IRONMAN("Ultimate Ironman", "https://services.runescape.com/m=hiscore_oldschool_ultimate/index_lite.json"),
   DEADMAN("Deadman", "https://services.runescape.com/m=hiscore_oldschool_deadman/index_lite.json"),
   SEASONAL("Leagues", "https://services.runescape.com/m=hiscore_oldschool_seasonal/index_lite.json"),
   TOURNAMENT("Tournament", "https://services.runescape.com/m=hiscore_oldschool_tournament/index_lite.json"),
   FRESH_START_WORLD("Fresh Start", "https://secure.runescape.com/m=hiscore_oldschool_fresh_start/index_lite.json"),
   PURE("1 Defence Pure", "https://secure.runescape.com/m=hiscore_oldschool_skiller_defence/index_lite.json"),
   LEVEL_3_SKILLER("Level 3 Skiller", "https://secure.runescape.com/m=hiscore_oldschool_skiller/index_lite.json");

   private final String name;
   private final HttpUrl hiscoreURL;

   private HiscoreEndpoint(String name, String hiscoreURL) {
      this.name = name;
      this.hiscoreURL = HttpUrl.get(hiscoreURL);
   }

   public static HiscoreEndpoint fromWorldTypes(Set<WorldType> worldTypes) {
      if (worldTypes.contains(WorldType.SEASONAL)) {
         return SEASONAL;
      } else if (worldTypes.contains(WorldType.TOURNAMENT_WORLD)) {
         return TOURNAMENT;
      } else if (worldTypes.contains(WorldType.DEADMAN)) {
         return DEADMAN;
      } else {
         return worldTypes.contains(WorldType.FRESH_START_WORLD) ? FRESH_START_WORLD : NORMAL;
      }
   }

   public String getName() {
      return this.name;
   }

   public HttpUrl getHiscoreURL() {
      return this.hiscoreURL;
   }
}
