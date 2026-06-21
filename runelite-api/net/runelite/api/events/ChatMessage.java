package net.runelite.api.events;

import net.runelite.api.ChatMessageType;
import net.runelite.api.MessageNode;

public class ChatMessage {
   private MessageNode messageNode;
   private ChatMessageType type;
   private String name;
   private String message;
   private String sender;
   private int timestamp;

   public MessageNode getMessageNode() {
      return this.messageNode;
   }

   public ChatMessageType getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public String getMessage() {
      return this.message;
   }

   public String getSender() {
      return this.sender;
   }

   public int getTimestamp() {
      return this.timestamp;
   }

   public void setMessageNode(MessageNode messageNode) {
      this.messageNode = messageNode;
   }

   public void setType(ChatMessageType type) {
      this.type = type;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public void setSender(String sender) {
      this.sender = sender;
   }

   public void setTimestamp(int timestamp) {
      this.timestamp = timestamp;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ChatMessage)) {
         return false;
      } else {
         ChatMessage other = (ChatMessage)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getTimestamp() != other.getTimestamp()) {
            return false;
         } else {
            label73: {
               Object this$messageNode = this.getMessageNode();
               Object other$messageNode = other.getMessageNode();
               if (this$messageNode == null) {
                  if (other$messageNode == null) {
                     break label73;
                  }
               } else if (this$messageNode.equals(other$messageNode)) {
                  break label73;
               }

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

            label59: {
               Object this$name = this.getName();
               Object other$name = other.getName();
               if (this$name == null) {
                  if (other$name == null) {
                     break label59;
                  }
               } else if (this$name.equals(other$name)) {
                  break label59;
               }

               return false;
            }

            Object this$message = this.getMessage();
            Object other$message = other.getMessage();
            if (this$message == null) {
               if (other$message != null) {
                  return false;
               }
            } else if (!this$message.equals(other$message)) {
               return false;
            }

            Object this$sender = this.getSender();
            Object other$sender = other.getSender();
            if (this$sender == null) {
               if (other$sender != null) {
                  return false;
               }
            } else if (!this$sender.equals(other$sender)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ChatMessage;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getTimestamp();
      Object $messageNode = this.getMessageNode();
      result = result * 59 + ($messageNode == null ? 43 : $messageNode.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      Object $sender = this.getSender();
      result = result * 59 + ($sender == null ? 43 : $sender.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getMessageNode());
      return "ChatMessage(messageNode=" + var10000 + ", type=" + String.valueOf(this.getType()) + ", name=" + this.getName() + ", message=" + this.getMessage() + ", sender=" + this.getSender() + ", timestamp=" + this.getTimestamp() + ")";
   }

   public ChatMessage(MessageNode messageNode, ChatMessageType type, String name, String message, String sender, int timestamp) {
      this.messageNode = messageNode;
      this.type = type;
      this.name = name;
      this.message = message;
      this.sender = sender;
      this.timestamp = timestamp;
   }

   public ChatMessage() {
   }
}
