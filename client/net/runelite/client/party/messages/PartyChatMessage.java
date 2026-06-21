package net.runelite.client.party.messages;

public final class PartyChatMessage extends PartyMemberMessage {
   private final String value;

   public PartyChatMessage(String value) {
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PartyChatMessage)) {
         return false;
      } else {
         PartyChatMessage other = (PartyChatMessage)o;
         if (!other.canEqual(this)) {
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
      return other instanceof PartyChatMessage;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $value = this.getValue();
      result = result * 59 + ($value == null ? 43 : $value.hashCode());
      return result;
   }

   public String toString() {
      return "PartyChatMessage(value=" + this.getValue() + ")";
   }
}
