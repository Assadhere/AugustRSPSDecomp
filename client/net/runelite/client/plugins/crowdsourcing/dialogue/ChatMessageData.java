package net.runelite.client.plugins.crowdsourcing.dialogue;

import net.runelite.api.ChatMessageType;

public class ChatMessageData {
   private final String message;
   private final ChatMessageType type;

   public String getMessage() {
      return this.message;
   }

   public ChatMessageType getType() {
      return this.type;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ChatMessageData)) {
         return false;
      } else {
         ChatMessageData other = (ChatMessageData)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$message = this.getMessage();
            Object other$message = other.getMessage();
            if (this$message == null) {
               if (other$message != null) {
                  return false;
               }
            } else if (!this$message.equals(other$message)) {
               return false;
            }

            Object this$type = this.getType();
            Object other$type = other.getType();
            if (this$type == null) {
               if (other$type != null) {
                  return false;
               }
            } else if (!this$type.equals(other$type)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ChatMessageData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getMessage();
      return "ChatMessageData(message=" + var10000 + ", type=" + String.valueOf(this.getType()) + ")";
   }

   public ChatMessageData(String message, ChatMessageType type) {
      this.message = message;
      this.type = type;
   }
}
