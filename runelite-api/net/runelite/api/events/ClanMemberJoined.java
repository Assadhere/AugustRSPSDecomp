package net.runelite.api.events;

import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;

public final class ClanMemberJoined {
   private final ClanChannel clanChannel;
   private final ClanChannelMember clanMember;

   public ClanMemberJoined(ClanChannel clanChannel, ClanChannelMember clanMember) {
      this.clanChannel = clanChannel;
      this.clanMember = clanMember;
   }

   public ClanChannel getClanChannel() {
      return this.clanChannel;
   }

   public ClanChannelMember getClanMember() {
      return this.clanMember;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ClanMemberJoined)) {
         return false;
      } else {
         ClanMemberJoined other = (ClanMemberJoined)o;
         Object this$clanChannel = this.getClanChannel();
         Object other$clanChannel = other.getClanChannel();
         if (this$clanChannel == null) {
            if (other$clanChannel != null) {
               return false;
            }
         } else if (!this$clanChannel.equals(other$clanChannel)) {
            return false;
         }

         Object this$clanMember = this.getClanMember();
         Object other$clanMember = other.getClanMember();
         if (this$clanMember == null) {
            if (other$clanMember != null) {
               return false;
            }
         } else if (!this$clanMember.equals(other$clanMember)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $clanChannel = this.getClanChannel();
      result = result * 59 + ($clanChannel == null ? 43 : $clanChannel.hashCode());
      Object $clanMember = this.getClanMember();
      result = result * 59 + ($clanMember == null ? 43 : $clanMember.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getClanChannel());
      return "ClanMemberJoined(clanChannel=" + var10000 + ", clanMember=" + String.valueOf(this.getClanMember()) + ")";
   }
}
