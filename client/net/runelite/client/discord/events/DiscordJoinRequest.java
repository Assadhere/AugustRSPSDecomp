package net.runelite.client.discord.events;

public final class DiscordJoinRequest {
   private final String userId;
   private final String username;
   private final String discriminator;
   private final String avatar;

   public DiscordJoinRequest(String userId, String username, String discriminator, String avatar) {
      this.userId = userId;
      this.username = username;
      this.discriminator = discriminator;
      this.avatar = avatar;
   }

   public String getUserId() {
      return this.userId;
   }

   public String getUsername() {
      return this.username;
   }

   public String getDiscriminator() {
      return this.discriminator;
   }

   public String getAvatar() {
      return this.avatar;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DiscordJoinRequest)) {
         return false;
      } else {
         DiscordJoinRequest other;
         label56: {
            other = (DiscordJoinRequest)o;
            Object this$userId = this.getUserId();
            Object other$userId = other.getUserId();
            if (this$userId == null) {
               if (other$userId == null) {
                  break label56;
               }
            } else if (this$userId.equals(other$userId)) {
               break label56;
            }

            return false;
         }

         label49: {
            Object this$username = this.getUsername();
            Object other$username = other.getUsername();
            if (this$username == null) {
               if (other$username == null) {
                  break label49;
               }
            } else if (this$username.equals(other$username)) {
               break label49;
            }

            return false;
         }

         Object this$discriminator = this.getDiscriminator();
         Object other$discriminator = other.getDiscriminator();
         if (this$discriminator == null) {
            if (other$discriminator != null) {
               return false;
            }
         } else if (!this$discriminator.equals(other$discriminator)) {
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

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $userId = this.getUserId();
      result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
      Object $username = this.getUsername();
      result = result * 59 + ($username == null ? 43 : $username.hashCode());
      Object $discriminator = this.getDiscriminator();
      result = result * 59 + ($discriminator == null ? 43 : $discriminator.hashCode());
      Object $avatar = this.getAvatar();
      result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getUserId();
      return "DiscordJoinRequest(userId=" + var10000 + ", username=" + this.getUsername() + ", discriminator=" + this.getDiscriminator() + ", avatar=" + this.getAvatar() + ")";
   }
}
