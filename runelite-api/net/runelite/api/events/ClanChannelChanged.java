package net.runelite.api.events;

import javax.annotation.Nullable;
import net.runelite.api.clan.ClanChannel;

public final class ClanChannelChanged {
   @Nullable
   private final ClanChannel clanChannel;
   private final int clanId;
   private final boolean guest;

   public ClanChannelChanged(@Nullable ClanChannel clanChannel, int clanId, boolean guest) {
      this.clanChannel = clanChannel;
      this.clanId = clanId;
      this.guest = guest;
   }

   @Nullable
   public ClanChannel getClanChannel() {
      return this.clanChannel;
   }

   public int getClanId() {
      return this.clanId;
   }

   public boolean isGuest() {
      return this.guest;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ClanChannelChanged)) {
         return false;
      } else {
         ClanChannelChanged other = (ClanChannelChanged)o;
         if (this.getClanId() != other.getClanId()) {
            return false;
         } else if (this.isGuest() != other.isGuest()) {
            return false;
         } else {
            Object this$clanChannel = this.getClanChannel();
            Object other$clanChannel = other.getClanChannel();
            if (this$clanChannel == null) {
               if (other$clanChannel != null) {
                  return false;
               }
            } else if (!this$clanChannel.equals(other$clanChannel)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getClanId();
      result = result * 59 + (this.isGuest() ? 79 : 97);
      Object $clanChannel = this.getClanChannel();
      result = result * 59 + ($clanChannel == null ? 43 : $clanChannel.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getClanChannel());
      return "ClanChannelChanged(clanChannel=" + var10000 + ", clanId=" + this.getClanId() + ", guest=" + this.isGuest() + ")";
   }
}
