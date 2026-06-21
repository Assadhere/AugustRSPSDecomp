package net.runelite.api.events;

import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;

public final class NpcChanged {
   private final NPC npc;
   private final NPCComposition old;

   public NpcChanged(NPC npc, NPCComposition old) {
      this.npc = npc;
      this.old = old;
   }

   public NPC getNpc() {
      return this.npc;
   }

   public NPCComposition getOld() {
      return this.old;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NpcChanged)) {
         return false;
      } else {
         NpcChanged other = (NpcChanged)o;
         Object this$npc = this.getNpc();
         Object other$npc = other.getNpc();
         if (this$npc == null) {
            if (other$npc != null) {
               return false;
            }
         } else if (!this$npc.equals(other$npc)) {
            return false;
         }

         Object this$old = this.getOld();
         Object other$old = other.getOld();
         if (this$old == null) {
            if (other$old != null) {
               return false;
            }
         } else if (!this$old.equals(other$old)) {
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
      Object $old = this.getOld();
      result = result * 59 + ($old == null ? 43 : $old.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getNpc());
      return "NpcChanged(npc=" + var10000 + ", old=" + String.valueOf(this.getOld()) + ")";
   }
}
