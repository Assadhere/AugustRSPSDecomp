package net.runelite.api.events;

import net.runelite.api.Actor;
import net.runelite.api.NPC;

public final class NpcSpawned {
   private final NPC npc;

   public Actor getActor() {
      return this.npc;
   }

   public NpcSpawned(NPC npc) {
      this.npc = npc;
   }

   public NPC getNpc() {
      return this.npc;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NpcSpawned)) {
         return false;
      } else {
         NpcSpawned other = (NpcSpawned)o;
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
      return "NpcSpawned(npc=" + String.valueOf(this.getNpc()) + ")";
   }
}
