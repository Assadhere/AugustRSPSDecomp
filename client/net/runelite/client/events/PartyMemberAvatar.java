package net.runelite.client.events;

import java.awt.image.BufferedImage;

public final class PartyMemberAvatar {
   private final long memberId;
   private final BufferedImage image;

   public PartyMemberAvatar(long memberId, BufferedImage image) {
      this.memberId = memberId;
      this.image = image;
   }

   public long getMemberId() {
      return this.memberId;
   }

   public BufferedImage getImage() {
      return this.image;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PartyMemberAvatar)) {
         return false;
      } else {
         PartyMemberAvatar other = (PartyMemberAvatar)o;
         if (this.getMemberId() != other.getMemberId()) {
            return false;
         } else {
            Object this$image = this.getImage();
            Object other$image = other.getImage();
            if (this$image == null) {
               if (other$image != null) {
                  return false;
               }
            } else if (!this$image.equals(other$image)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $memberId = this.getMemberId();
      result = result * 59 + (int)($memberId >>> 32 ^ $memberId);
      Object $image = this.getImage();
      result = result * 59 + ($image == null ? 43 : $image.hashCode());
      return result;
   }

   public String toString() {
      long var10000 = this.getMemberId();
      return "PartyMemberAvatar(memberId=" + var10000 + ", image=" + String.valueOf(this.getImage()) + ")";
   }
}
