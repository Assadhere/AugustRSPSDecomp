package net.runelite.client.party.events;

public final class UserJoin {
   private final long partyId;
   private final long memberId;

   public UserJoin(long partyId, long memberId) {
      this.partyId = partyId;
      this.memberId = memberId;
   }

   public long getPartyId() {
      return this.partyId;
   }

   public long getMemberId() {
      return this.memberId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof UserJoin)) {
         return false;
      } else {
         UserJoin other = (UserJoin)o;
         if (this.getPartyId() != other.getPartyId()) {
            return false;
         } else {
            return this.getMemberId() == other.getMemberId();
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $partyId = this.getPartyId();
      result = result * 59 + (int)($partyId >>> 32 ^ $partyId);
      long $memberId = this.getMemberId();
      result = result * 59 + (int)($memberId >>> 32 ^ $memberId);
      return result;
   }

   public String toString() {
      long var10000 = this.getPartyId();
      return "UserJoin(partyId=" + var10000 + ", memberId=" + this.getMemberId() + ")";
   }
}
