package custom;

import java.util.Map;

public class UpdateNpcInfoScript {
   private final Map<Integer, NpcInfoMessage> npcInfo;

   public UpdateNpcInfoScript(Map<Integer, NpcInfoMessage> var1) {
      this.npcInfo = var1;
   }

   public Map<Integer, NpcInfoMessage> getNpcInfo() {
      return this.npcInfo;
   }

   public String toString() {
      return "UpdateNpcInfoScript(npcInfo=" + String.valueOf(this.getNpcInfo()) + ")";
   }

   public static class NpcInfoMessage {
      private final int hitpoints;

      public NpcInfoMessage(int var1) {
         this.hitpoints = var1;
      }

      public int getHitpoints() {
         return this.hitpoints;
      }

      public String toString() {
         return "UpdateNpcInfoScript.NpcInfoMessage(hitpoints=" + this.getHitpoints() + ")";
      }
   }
}
