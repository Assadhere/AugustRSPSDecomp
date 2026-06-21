package net.runelite.client.plugins.specialcounter;

import net.runelite.client.party.messages.PartyMemberMessage;

public final class SpecialCounterUpdate extends PartyMemberMessage {
   private final int npcIndex;
   private final SpecialWeapon weapon;
   private final int hit;
   private final int world;
   private final int playerId;

   public SpecialCounterUpdate(int npcIndex, SpecialWeapon weapon, int hit, int world, int playerId) {
      this.npcIndex = npcIndex;
      this.weapon = weapon;
      this.hit = hit;
      this.world = world;
      this.playerId = playerId;
   }

   public int getNpcIndex() {
      return this.npcIndex;
   }

   public SpecialWeapon getWeapon() {
      return this.weapon;
   }

   public int getHit() {
      return this.hit;
   }

   public int getWorld() {
      return this.world;
   }

   public int getPlayerId() {
      return this.playerId;
   }

   public String toString() {
      int var10000 = this.getNpcIndex();
      return "SpecialCounterUpdate(npcIndex=" + var10000 + ", weapon=" + String.valueOf(this.getWeapon()) + ", hit=" + this.getHit() + ", world=" + this.getWorld() + ", playerId=" + this.getPlayerId() + ")";
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SpecialCounterUpdate)) {
         return false;
      } else {
         SpecialCounterUpdate other = (SpecialCounterUpdate)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else if (this.getNpcIndex() != other.getNpcIndex()) {
            return false;
         } else if (this.getHit() != other.getHit()) {
            return false;
         } else if (this.getWorld() != other.getWorld()) {
            return false;
         } else if (this.getPlayerId() != other.getPlayerId()) {
            return false;
         } else {
            Object this$weapon = this.getWeapon();
            Object other$weapon = other.getWeapon();
            if (this$weapon == null) {
               if (other$weapon != null) {
                  return false;
               }
            } else if (!this$weapon.equals(other$weapon)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SpecialCounterUpdate;
   }

   public int hashCode() {
      int PRIME = true;
      int result = super.hashCode();
      result = result * 59 + this.getNpcIndex();
      result = result * 59 + this.getHit();
      result = result * 59 + this.getWorld();
      result = result * 59 + this.getPlayerId();
      Object $weapon = this.getWeapon();
      result = result * 59 + ($weapon == null ? 43 : $weapon.hashCode());
      return result;
   }
}
