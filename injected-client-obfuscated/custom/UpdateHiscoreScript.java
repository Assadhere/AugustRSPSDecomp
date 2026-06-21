package custom;

import java.util.List;
import java.util.Map;

public class UpdateHiscoreScript {
   private final String username;
   private final String displayName;
   private final int ironmanMode;
   private final int gameMode;
   private final int privilege;
   private final int donatorRank;
   private final long totalXp;
   private final int totalLevel;
   private final int totalPrestige;
   private final long totalPrestigeXp;
   private final int totalRank;
   private final int totalRankGlobal;
   private final long ranksUpdatedAtMillis;
   private final Map<Integer, SkillData> skills;
   private final List<CountEntry> counts;
   private final Perks perks;

   public UpdateHiscoreScript(String var1, String var2, int var3, int var4, int var5, int var6, long var7, int var9, int var10, long var11, int var13, int var14, long var15, Map<Integer, SkillData> var17, List<CountEntry> var18, Perks var19) {
      this.username = var1;
      this.displayName = var2;
      this.ironmanMode = var3;
      this.gameMode = var4;
      this.privilege = var5;
      this.donatorRank = var6;
      this.totalXp = var7;
      this.totalLevel = var9;
      this.totalPrestige = var10;
      this.totalPrestigeXp = var11;
      this.totalRank = var13;
      this.totalRankGlobal = var14;
      this.ranksUpdatedAtMillis = var15;
      this.skills = var17;
      this.counts = var18;
      this.perks = var19;
   }

   public String getUsername() {
      return this.username;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public int getIronmanMode() {
      return this.ironmanMode;
   }

   public int getGameMode() {
      return this.gameMode;
   }

   public int getPrivilege() {
      return this.privilege;
   }

   public int getDonatorRank() {
      return this.donatorRank;
   }

   public long getTotalXp() {
      return this.totalXp;
   }

   public int getTotalLevel() {
      return this.totalLevel;
   }

   public int getTotalPrestige() {
      return this.totalPrestige;
   }

   public long getTotalPrestigeXp() {
      return this.totalPrestigeXp;
   }

   public int getTotalRank() {
      return this.totalRank;
   }

   public int getTotalRankGlobal() {
      return this.totalRankGlobal;
   }

   public long getRanksUpdatedAtMillis() {
      return this.ranksUpdatedAtMillis;
   }

   public Map<Integer, SkillData> getSkills() {
      return this.skills;
   }

   public List<CountEntry> getCounts() {
      return this.counts;
   }

   public Perks getPerks() {
      return this.perks;
   }

   public static class Perks {
      private final String perk1;
      private final String perk2;
      private final String perk3;
      private final String perk4;
      private final String perk5;
      private final String perk6;

      public Perks(String var1, String var2, String var3, String var4, String var5, String var6) {
         this.perk1 = var1;
         this.perk2 = var2;
         this.perk3 = var3;
         this.perk4 = var4;
         this.perk5 = var5;
         this.perk6 = var6;
      }

      public String getPerk1() {
         return this.perk1;
      }

      public String getPerk2() {
         return this.perk2;
      }

      public String getPerk3() {
         return this.perk3;
      }

      public String getPerk4() {
         return this.perk4;
      }

      public String getPerk5() {
         return this.perk5;
      }

      public String getPerk6() {
         return this.perk6;
      }
   }

   public static class CountEntry {
      private final String id;
      private final long count;
      private final int rank;
      private final int rankGlobal;

      public CountEntry(String var1, long var2, int var4, int var5) {
         this.id = var1;
         this.count = var2;
         this.rank = var4;
         this.rankGlobal = var5;
      }

      public String getId() {
         return this.id;
      }

      public long getCount() {
         return this.count;
      }

      public int getRank() {
         return this.rank;
      }

      public int getRankGlobal() {
         return this.rankGlobal;
      }
   }

   public static class SkillData {
      private final long xp;
      private final int level;
      private final int prestige;
      private final long prestigeXp;
      private final int rank;
      private final int rankGlobal;

      public SkillData(long var1, int var3, int var4, long var5, int var7, int var8) {
         this.xp = var1;
         this.level = var3;
         this.prestige = var4;
         this.prestigeXp = var5;
         this.rank = var7;
         this.rankGlobal = var8;
      }

      public long getXp() {
         return this.xp;
      }

      public int getLevel() {
         return this.level;
      }

      public int getPrestige() {
         return this.prestige;
      }

      public long getPrestigeXp() {
         return this.prestigeXp;
      }

      public int getRank() {
         return this.rank;
      }

      public int getRankGlobal() {
         return this.rankGlobal;
      }
   }
}
