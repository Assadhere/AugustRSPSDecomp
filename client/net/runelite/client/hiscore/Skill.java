package net.runelite.client.hiscore;

public final class Skill {
   private final int rank;
   private final int rankGlobal;
   private final int level;
   private final long experience;
   private final int prestige;
   private final long prestigeXp;

   public Skill(int rank, int rankGlobal, int level, long experience, int prestige, long prestigeXp) {
      this.rank = rank;
      this.rankGlobal = rankGlobal;
      this.level = level;
      this.experience = experience;
      this.prestige = prestige;
      this.prestigeXp = prestigeXp;
   }

   public Skill(int rank, int level, long experience, int prestige, long prestigeXp) {
      this(rank, -1, level, experience, prestige, prestigeXp);
   }

   public Skill(int rank, int level, long experience) {
      this(rank, -1, level, experience, 0, 0L);
   }

   public int getRank() {
      return this.rank;
   }

   public int getRankGlobal() {
      return this.rankGlobal;
   }

   public int getLevel() {
      return this.level;
   }

   public long getExperience() {
      return this.experience;
   }

   public int getPrestige() {
      return this.prestige;
   }

   public long getPrestigeXp() {
      return this.prestigeXp;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Skill)) {
         return false;
      } else {
         Skill other = (Skill)o;
         if (this.getRank() != other.getRank()) {
            return false;
         } else if (this.getRankGlobal() != other.getRankGlobal()) {
            return false;
         } else if (this.getLevel() != other.getLevel()) {
            return false;
         } else if (this.getExperience() != other.getExperience()) {
            return false;
         } else if (this.getPrestige() != other.getPrestige()) {
            return false;
         } else {
            return this.getPrestigeXp() == other.getPrestigeXp();
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getRank();
      result = result * 59 + this.getRankGlobal();
      result = result * 59 + this.getLevel();
      long $experience = this.getExperience();
      result = result * 59 + (int)($experience >>> 32 ^ $experience);
      result = result * 59 + this.getPrestige();
      long $prestigeXp = this.getPrestigeXp();
      result = result * 59 + (int)($prestigeXp >>> 32 ^ $prestigeXp);
      return result;
   }

   public String toString() {
      int var10000 = this.getRank();
      return "Skill(rank=" + var10000 + ", rankGlobal=" + this.getRankGlobal() + ", level=" + this.getLevel() + ", experience=" + this.getExperience() + ", prestige=" + this.getPrestige() + ", prestigeXp=" + this.getPrestigeXp() + ")";
   }
}
