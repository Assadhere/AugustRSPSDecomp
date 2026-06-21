package net.runelite.client.hiscore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HiscoreResult {
   private final String player;
   private final String displayName;
   private final int ironmanMode;
   private final int gameMode;
   private final int privilege;
   private final int donatorRank;
   private final Map<HiscoreSkill, Skill> skills;
   private final Map<String, Long> counts;
   private final Map<String, Integer> countRanks;
   private final Map<String, Integer> countRanksGlobal;
   private final List<String> perks;
   private final long ranksUpdatedAtMillis;

   public HiscoreResult(String player, String displayName, int ironmanMode, int gameMode, int privilege, int donatorRank, Map<HiscoreSkill, Skill> skills, Map<String, Long> counts, Map<String, Integer> countRanks, Map<String, Integer> countRanksGlobal, List<String> perks, long ranksUpdatedAtMillis) {
      this.player = player;
      this.displayName = displayName;
      this.ironmanMode = ironmanMode;
      this.gameMode = gameMode;
      this.privilege = privilege;
      this.donatorRank = donatorRank;
      this.skills = skills;
      this.counts = counts;
      this.countRanks = countRanks;
      this.countRanksGlobal = countRanksGlobal;
      this.perks = perks;
      this.ranksUpdatedAtMillis = ranksUpdatedAtMillis;
   }

   public HiscoreResult(String player, Map<HiscoreSkill, Skill> skills) {
      this(player, player, -1, -1, -1, 0, skills, Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap(), Collections.emptyList(), -1L);
   }

   public int getCountRank(String id) {
      Integer rank = (Integer)this.countRanks.get(id);
      return rank != null ? rank : -1;
   }

   public int getCountRankGlobal(String id) {
      Integer rank = (Integer)this.countRanksGlobal.get(id);
      return rank != null ? rank : -1;
   }

   public Skill getSkill(HiscoreSkill skill) {
      return (Skill)this.skills.get(skill);
   }

   public long getCount(String id) {
      Long count = (Long)this.counts.get(id);
      return count != null ? count : 0L;
   }

   public long getCount(CountId id) {
      return this.getCount(id.getId());
   }

   public String getPlayer() {
      return this.player;
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

   public Map<HiscoreSkill, Skill> getSkills() {
      return this.skills;
   }

   public Map<String, Long> getCounts() {
      return this.counts;
   }

   public Map<String, Integer> getCountRanks() {
      return this.countRanks;
   }

   public Map<String, Integer> getCountRanksGlobal() {
      return this.countRanksGlobal;
   }

   public List<String> getPerks() {
      return this.perks;
   }

   public long getRanksUpdatedAtMillis() {
      return this.ranksUpdatedAtMillis;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HiscoreResult)) {
         return false;
      } else {
         HiscoreResult other = (HiscoreResult)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getIronmanMode() != other.getIronmanMode()) {
            return false;
         } else if (this.getGameMode() != other.getGameMode()) {
            return false;
         } else if (this.getPrivilege() != other.getPrivilege()) {
            return false;
         } else if (this.getDonatorRank() != other.getDonatorRank()) {
            return false;
         } else if (this.getRanksUpdatedAtMillis() != other.getRanksUpdatedAtMillis()) {
            return false;
         } else {
            Object this$player = this.getPlayer();
            Object other$player = other.getPlayer();
            if (this$player == null) {
               if (other$player != null) {
                  return false;
               }
            } else if (!this$player.equals(other$player)) {
               return false;
            }

            Object this$displayName = this.getDisplayName();
            Object other$displayName = other.getDisplayName();
            if (this$displayName == null) {
               if (other$displayName != null) {
                  return false;
               }
            } else if (!this$displayName.equals(other$displayName)) {
               return false;
            }

            label93: {
               Object this$skills = this.getSkills();
               Object other$skills = other.getSkills();
               if (this$skills == null) {
                  if (other$skills == null) {
                     break label93;
                  }
               } else if (this$skills.equals(other$skills)) {
                  break label93;
               }

               return false;
            }

            label86: {
               Object this$counts = this.getCounts();
               Object other$counts = other.getCounts();
               if (this$counts == null) {
                  if (other$counts == null) {
                     break label86;
                  }
               } else if (this$counts.equals(other$counts)) {
                  break label86;
               }

               return false;
            }

            label79: {
               Object this$countRanks = this.getCountRanks();
               Object other$countRanks = other.getCountRanks();
               if (this$countRanks == null) {
                  if (other$countRanks == null) {
                     break label79;
                  }
               } else if (this$countRanks.equals(other$countRanks)) {
                  break label79;
               }

               return false;
            }

            label72: {
               Object this$countRanksGlobal = this.getCountRanksGlobal();
               Object other$countRanksGlobal = other.getCountRanksGlobal();
               if (this$countRanksGlobal == null) {
                  if (other$countRanksGlobal == null) {
                     break label72;
                  }
               } else if (this$countRanksGlobal.equals(other$countRanksGlobal)) {
                  break label72;
               }

               return false;
            }

            Object this$perks = this.getPerks();
            Object other$perks = other.getPerks();
            if (this$perks == null) {
               if (other$perks != null) {
                  return false;
               }
            } else if (!this$perks.equals(other$perks)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof HiscoreResult;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getIronmanMode();
      result = result * 59 + this.getGameMode();
      result = result * 59 + this.getPrivilege();
      result = result * 59 + this.getDonatorRank();
      long $ranksUpdatedAtMillis = this.getRanksUpdatedAtMillis();
      result = result * 59 + (int)($ranksUpdatedAtMillis >>> 32 ^ $ranksUpdatedAtMillis);
      Object $player = this.getPlayer();
      result = result * 59 + ($player == null ? 43 : $player.hashCode());
      Object $displayName = this.getDisplayName();
      result = result * 59 + ($displayName == null ? 43 : $displayName.hashCode());
      Object $skills = this.getSkills();
      result = result * 59 + ($skills == null ? 43 : $skills.hashCode());
      Object $counts = this.getCounts();
      result = result * 59 + ($counts == null ? 43 : $counts.hashCode());
      Object $countRanks = this.getCountRanks();
      result = result * 59 + ($countRanks == null ? 43 : $countRanks.hashCode());
      Object $countRanksGlobal = this.getCountRanksGlobal();
      result = result * 59 + ($countRanksGlobal == null ? 43 : $countRanksGlobal.hashCode());
      Object $perks = this.getPerks();
      result = result * 59 + ($perks == null ? 43 : $perks.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getPlayer();
      return "HiscoreResult(player=" + var10000 + ", displayName=" + this.getDisplayName() + ", ironmanMode=" + this.getIronmanMode() + ", gameMode=" + this.getGameMode() + ", privilege=" + this.getPrivilege() + ", donatorRank=" + this.getDonatorRank() + ", skills=" + String.valueOf(this.getSkills()) + ", counts=" + String.valueOf(this.getCounts()) + ", countRanks=" + String.valueOf(this.getCountRanks()) + ", countRanksGlobal=" + String.valueOf(this.getCountRanksGlobal()) + ", perks=" + String.valueOf(this.getPerks()) + ", ranksUpdatedAtMillis=" + this.getRanksUpdatedAtMillis() + ")";
   }
}
