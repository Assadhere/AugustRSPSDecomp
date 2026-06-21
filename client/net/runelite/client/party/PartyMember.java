package net.runelite.client.party;

import java.awt.image.BufferedImage;

public class PartyMember {
   private final long memberId;
   private String displayName = "<unknown>";
   private boolean loggedIn;
   private BufferedImage avatar;

   public PartyMember(long memberId) {
      this.memberId = memberId;
   }

   public long getMemberId() {
      return this.memberId;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public boolean isLoggedIn() {
      return this.loggedIn;
   }

   public BufferedImage getAvatar() {
      return this.avatar;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   public void setLoggedIn(boolean loggedIn) {
      this.loggedIn = loggedIn;
   }

   public void setAvatar(BufferedImage avatar) {
      this.avatar = avatar;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PartyMember)) {
         return false;
      } else {
         PartyMember other = (PartyMember)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getMemberId() != other.getMemberId()) {
            return false;
         } else if (this.isLoggedIn() != other.isLoggedIn()) {
            return false;
         } else {
            label40: {
               Object this$displayName = this.getDisplayName();
               Object other$displayName = other.getDisplayName();
               if (this$displayName == null) {
                  if (other$displayName == null) {
                     break label40;
                  }
               } else if (this$displayName.equals(other$displayName)) {
                  break label40;
               }

               return false;
            }

            Object this$avatar = this.getAvatar();
            Object other$avatar = other.getAvatar();
            if (this$avatar == null) {
               if (other$avatar != null) {
                  return false;
               }
            } else if (!this$avatar.equals(other$avatar)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PartyMember;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $memberId = this.getMemberId();
      result = result * 59 + (int)($memberId >>> 32 ^ $memberId);
      result = result * 59 + (this.isLoggedIn() ? 79 : 97);
      Object $displayName = this.getDisplayName();
      result = result * 59 + ($displayName == null ? 43 : $displayName.hashCode());
      Object $avatar = this.getAvatar();
      result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
      return result;
   }

   public String toString() {
      long var10000 = this.getMemberId();
      return "PartyMember(memberId=" + var10000 + ", displayName=" + this.getDisplayName() + ", loggedIn=" + this.isLoggedIn() + ", avatar=" + String.valueOf(this.getAvatar()) + ")";
   }
}
