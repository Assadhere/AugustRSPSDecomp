package net.runelite.client.plugins.chatchannel;

import net.runelite.api.ChatPlayer;

final class MemberActivity {
   private final ActivityType activityType;
   private final ChatType chatType;
   private final ChatPlayer member;
   private final Integer tick;

   public ActivityType getActivityType() {
      return this.activityType;
   }

   public ChatType getChatType() {
      return this.chatType;
   }

   public ChatPlayer getMember() {
      return this.member;
   }

   public Integer getTick() {
      return this.tick;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MemberActivity)) {
         return false;
      } else {
         MemberActivity other;
         label56: {
            other = (MemberActivity)o;
            Object this$tick = this.getTick();
            Object other$tick = other.getTick();
            if (this$tick == null) {
               if (other$tick == null) {
                  break label56;
               }
            } else if (this$tick.equals(other$tick)) {
               break label56;
            }

            return false;
         }

         label49: {
            Object this$activityType = this.getActivityType();
            Object other$activityType = other.getActivityType();
            if (this$activityType == null) {
               if (other$activityType == null) {
                  break label49;
               }
            } else if (this$activityType.equals(other$activityType)) {
               break label49;
            }

            return false;
         }

         Object this$chatType = this.getChatType();
         Object other$chatType = other.getChatType();
         if (this$chatType == null) {
            if (other$chatType != null) {
               return false;
            }
         } else if (!this$chatType.equals(other$chatType)) {
            return false;
         }

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
      Object $tick = this.getTick();
      result = result * 59 + ($tick == null ? 43 : $tick.hashCode());
      Object $activityType = this.getActivityType();
      result = result * 59 + ($activityType == null ? 43 : $activityType.hashCode());
      Object $chatType = this.getChatType();
      result = result * 59 + ($chatType == null ? 43 : $chatType.hashCode());
      Object $member = this.getMember();
      result = result * 59 + ($member == null ? 43 : $member.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getActivityType());
      return "MemberActivity(activityType=" + var10000 + ", chatType=" + String.valueOf(this.getChatType()) + ", member=" + String.valueOf(this.getMember()) + ", tick=" + this.getTick() + ")";
   }

   public MemberActivity(ActivityType activityType, ChatType chatType, ChatPlayer member, Integer tick) {
      this.activityType = activityType;
      this.chatType = chatType;
      this.member = member;
      this.tick = tick;
   }

   static enum ChatType {
      FRIENDS_CHAT,
      CLAN_CHAT,
      GUEST_CHAT;
   }
}
