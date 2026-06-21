package custom.model;

public class SkillPrestigeInfo {
   private final int skillId;
   private final int rank;
   private final int nextRankAt;
   private final PrestigeState state;

   public SkillPrestigeInfo(int var1, int var2, int var3, int var4) {
      this.skillId = var1;
      this.rank = var2;
      this.nextRankAt = var3;
      if (var4 == PrestigeState.UnderLevel.value) {
         this.state = PrestigeState.UnderLevel;
      } else if (var4 == PrestigeState.CanRankUp.value) {
         this.state = PrestigeState.CanRankUp;
      } else if (var4 == PrestigeState.Maxed.value) {
         this.state = PrestigeState.Maxed;
      } else {
         this.state = PrestigeState.UnderLevel;
      }

   }

   public int getSkillId() {
      return this.skillId;
   }

   public int getRank() {
      return this.rank;
   }

   public int getNextRankAt() {
      return this.nextRankAt;
   }

   public PrestigeState getState() {
      return this.state;
   }

   public String toString() {
      int var10000 = this.getSkillId();
      return "SkillPrestigeInfo(skillId=" + var10000 + ", rank=" + this.getRank() + ", nextRankAt=" + this.getNextRankAt() + ", state=" + String.valueOf(this.getState()) + ")";
   }
}
