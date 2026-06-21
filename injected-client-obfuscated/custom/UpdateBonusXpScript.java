package custom;

import java.util.Map;

public class UpdateBonusXpScript {
   private final Map<Integer, BonusXpState> mapping;

   public UpdateBonusXpScript(Map<Integer, BonusXpState> var1) {
      this.mapping = var1;
   }

   public Map<Integer, BonusXpState> getMapping() {
      return this.mapping;
   }

   public String toString() {
      return "UpdateBonusXpScript(mapping=" + String.valueOf(this.getMapping()) + ")";
   }

   public static class BonusXpState {
      private final int sourceWorldId;
      private final int spriteId;
      private final long endTimeMillis;

      public BonusXpState(int var1, int var2, long var3) {
         this.sourceWorldId = var1;
         this.spriteId = var2;
         this.endTimeMillis = var3;
      }

      public int getSourceWorldId() {
         return this.sourceWorldId;
      }

      public int getSpriteId() {
         return this.spriteId;
      }

      public long getEndTimeMillis() {
         return this.endTimeMillis;
      }

      public String toString() {
         int var10000 = this.getSourceWorldId();
         return "UpdateBonusXpScript.BonusXpState(sourceWorldId=" + var10000 + ", spriteId=" + this.getSpriteId() + ", endTimeMillis=" + this.getEndTimeMillis() + ")";
      }
   }
}
