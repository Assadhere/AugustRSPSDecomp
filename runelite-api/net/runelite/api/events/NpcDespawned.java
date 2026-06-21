package net.runelite.api.events;

import net.runelite.api.Actor;
import net.runelite.api.NPC;

public final class NpcDespawned {
   private final NPC npc;

   public Actor getActor() {
      return this.npc;
   }

   public NpcDespawned(NPC npc) {
      this.npc = npc;
   }

   public NPC getNpc() {
      return this.npc;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NpcDespawned)) {
         return false;
      } else {
         NpcDespawned other = (NpcDespawned)o;
         Object this$npc = this.getNpc();
         Object other$npc = other.getNpc();
         if (this$npc == null) {
            if (other$npc != null) {
               return false;
            }
         } else if (!this$npc.equals(other$npc)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $npc = this.getNpc();
      result = result * 59 + ($npc == null ? 43 : $npc.hashCode());
      return result;
   }

   public String toString() {
      return "NpcDespawned(npc=" + String.valueOf(this.getNpc()) + ")";
   }
}
