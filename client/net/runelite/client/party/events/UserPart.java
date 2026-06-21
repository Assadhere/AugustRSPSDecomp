package net.runelite.client.party.events;

public final class UserPart {
   private final long memberId;

   public UserPart(long memberId) {
      this.memberId = memberId;
   }

   public long getMemberId() {
      return this.memberId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof UserPart)) {
         return false;
      } else {
         UserPart other = (UserPart)o;
         return this.getMemberId() == other.getMemberId();
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $memberId = this.getMemberId();
      result = result * 59 + (int)($memberId >>> 32 ^ $memberId);
      return result;
   }

   public String toString() {
      return "UserPart(memberId=" + this.getMemberId() + ")";
   }
}
