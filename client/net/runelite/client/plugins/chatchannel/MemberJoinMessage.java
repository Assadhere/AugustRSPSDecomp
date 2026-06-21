package net.runelite.client.plugins.chatchannel;

import net.runelite.api.MessageNode;

final class MemberJoinMessage {
   private final MessageNode messageNode;
   private final int getMessageId;
   private final int tick;

   public MemberJoinMessage(MessageNode messageNode, int getMessageId, int tick) {
      this.messageNode = messageNode;
      this.getMessageId = getMessageId;
      this.tick = tick;
   }

   public MessageNode getMessageNode() {
      return this.messageNode;
   }

   public int getGetMessageId() {
      return this.getMessageId;
   }

   public int getTick() {
      return this.tick;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MemberJoinMessage)) {
         return false;
      } else {
         MemberJoinMessage other = (MemberJoinMessage)o;
         if (this.getGetMessageId() != other.getGetMessageId()) {
            return false;
         } else if (this.getTick() != other.getTick()) {
            return false;
         } else {
            Object this$messageNode = this.getMessageNode();
            Object other$messageNode = other.getMessageNode();
            if (this$messageNode == null) {
               if (other$messageNode != null) {
                  return false;
               }
            } else if (!this$messageNode.equals(other$messageNode)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getGetMessageId();
      result = result * 59 + this.getTick();
      Object $messageNode = this.getMessageNode();
      result = result * 59 + ($messageNode == null ? 43 : $messageNode.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getMessageNode());
      return "MemberJoinMessage(messageNode=" + var10000 + ", getMessageId=" + this.getGetMessageId() + ", tick=" + this.getTick() + ")";
   }
}
