package net.runelite.client.events;

public final class ChatboxInput extends ChatInput {
   private final String value;
   private final int chatType;

   public ChatboxInput(String value, int chatType, Runnable resume) {
      super(resume);
      this.value = value;
      this.chatType = chatType;
   }

   public String getValue() {
      return this.value;
   }

   public int getChatType() {
      return this.chatType;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ChatboxInput)) {
         return false;
      } else {
         ChatboxInput other = (ChatboxInput)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getChatType() != other.getChatType()) {
            return false;
         } else {
            Object this$value = this.getValue();
            Object other$value = other.getValue();
            if (this$value == null) {
               if (other$value != null) {
                  return false;
               }
            } else if (!this$value.equals(other$value)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ChatboxInput;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getChatType();
      Object $value = this.getValue();
      result = result * 59 + ($value == null ? 43 : $value.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getValue();
      return "ChatboxInput(value=" + var10000 + ", chatType=" + this.getChatType() + ")";
   }
}
