package net.runelite.api.events;

import net.runelite.api.FriendsChatMember;

public final class FriendsChatMemberJoined {
   private final FriendsChatMember member;

   public FriendsChatMemberJoined(FriendsChatMember member) {
      this.member = member;
   }

   public FriendsChatMember getMember() {
      return this.member;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof FriendsChatMemberJoined)) {
         return false;
      } else {
         FriendsChatMemberJoined other = (FriendsChatMemberJoined)o;
         Object this$member = this.getMember();
         Object other$member = other.getMember();
         if (this$member == null) {
            if (other$member != null) {
               return false;
            }
         } else if (!this$member.equals(other$member)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $member = this.getMember();
      result = result * 59 + ($member == null ? 43 : $member.hashCode());
      return result;
   }

   public String toString() {
      return "FriendsChatMemberJoined(member=" + String.valueOf(this.getMember()) + ")";
   }
}
