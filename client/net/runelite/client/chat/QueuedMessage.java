package net.runelite.client.chat;

import lombok.NonNull;
import net.runelite.api.ChatMessageType;

public class QueuedMessage {
   private final @NonNull ChatMessageType type;
   private final String value;
   private String name;
   private String sender;
   private String runeLiteFormattedMessage;
   private int timestamp;

   QueuedMessage(@NonNull ChatMessageType type, String value, String name, String sender, String runeLiteFormattedMessage, int timestamp) {
      if (type == null) {
         throw new NullPointerException("type is marked non-null but is null");
      } else {
         this.type = type;
         this.value = value;
         this.name = name;
         this.sender = sender;
         this.runeLiteFormattedMessage = runeLiteFormattedMessage;
         this.timestamp = timestamp;
      }
   }

   public static QueuedMessageBuilder builder() {
      return new QueuedMessageBuilder();
   }

   public @NonNull ChatMessageType getType() {
      return this.type;
   }

   public String getValue() {
      return this.value;
   }

   public String getName() {
      return this.name;
   }

   public String getSender() {
      return this.sender;
   }

   public String getRuneLiteFormattedMessage() {
      return this.runeLiteFormattedMessage;
   }

   public int getTimestamp() {
      return this.timestamp;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSender(String sender) {
      this.sender = sender;
   }

   public void setRuneLiteFormattedMessage(String runeLiteFormattedMessage) {
      this.runeLiteFormattedMessage = runeLiteFormattedMessage;
   }

   public void setTimestamp(int timestamp) {
      this.timestamp = timestamp;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof QueuedMessage)) {
         return false;
      } else {
         QueuedMessage other = (QueuedMessage)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getTimestamp() != other.getTimestamp()) {
            return false;
         } else {
            label73: {
               Object this$type = this.getType();
               Object other$type = other.getType();
               if (this$type == null) {
                  if (other$type == null) {
                     break label73;
                  }
               } else if (this$type.equals(other$type)) {
                  break label73;
               }

               return false;
            }

            Object this$value = this.getValue();
            Object other$value = other.getValue();
            if (this$value == null) {
               if (other$value != null) {
                  return false;
               }
            } else if (!this$value.equals(other$value)) {
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

            Object this$sender = this.getSender();
            Object other$sender = other.getSender();
            if (this$sender == null) {
               if (other$sender != null) {
                  return false;
               }
            } else if (!this$sender.equals(other$sender)) {
               return false;
            }

            Object this$runeLiteFormattedMessage = this.getRuneLiteFormattedMessage();
            Object other$runeLiteFormattedMessage = other.getRuneLiteFormattedMessage();
            if (this$runeLiteFormattedMessage == null) {
               if (other$runeLiteFormattedMessage != null) {
                  return false;
               }
            } else if (!this$runeLiteFormattedMessage.equals(other$runeLiteFormattedMessage)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof QueuedMessage;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getTimestamp();
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $value = this.getValue();
      result = result * 59 + ($value == null ? 43 : $value.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $sender = this.getSender();
      result = result * 59 + ($sender == null ? 43 : $sender.hashCode());
      Object $runeLiteFormattedMessage = this.getRuneLiteFormattedMessage();
      result = result * 59 + ($runeLiteFormattedMessage == null ? 43 : $runeLiteFormattedMessage.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getType());
      return "QueuedMessage(type=" + var10000 + ", value=" + this.getValue() + ", name=" + this.getName() + ", sender=" + this.getSender() + ", runeLiteFormattedMessage=" + this.getRuneLiteFormattedMessage() + ", timestamp=" + this.getTimestamp() + ")";
   }

   public static class QueuedMessageBuilder {
      private ChatMessageType type;
      private String value;
      private String name;
      private String sender;
      private String runeLiteFormattedMessage;
      private int timestamp;

      QueuedMessageBuilder() {
      }

      public QueuedMessageBuilder type(@NonNull ChatMessageType type) {
         if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
         } else {
            this.type = type;
            return this;
         }
      }

      public QueuedMessageBuilder value(String value) {
         this.value = value;
         return this;
      }

      public QueuedMessageBuilder name(String name) {
         this.name = name;
         return this;
      }

      public QueuedMessageBuilder sender(String sender) {
         this.sender = sender;
         return this;
      }

      public QueuedMessageBuilder runeLiteFormattedMessage(String runeLiteFormattedMessage) {
         this.runeLiteFormattedMessage = runeLiteFormattedMessage;
         return this;
      }

      public QueuedMessageBuilder timestamp(int timestamp) {
         this.timestamp = timestamp;
         return this;
      }

      public QueuedMessage build() {
         return new QueuedMessage(this.type, this.value, this.name, this.sender, this.runeLiteFormattedMessage, this.timestamp);
      }

      public String toString() {
         String var10000 = String.valueOf(this.type);
         return "QueuedMessage.QueuedMessageBuilder(type=" + var10000 + ", value=" + this.value + ", name=" + this.name + ", sender=" + this.sender + ", runeLiteFormattedMessage=" + this.runeLiteFormattedMessage + ", timestamp=" + this.timestamp + ")";
      }
   }
}
