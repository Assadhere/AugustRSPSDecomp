package net.runelite.client.plugins.crowdsourcing.dialogue;

public class PlayerDialogueData {
   private final String message;

   public String getMessage() {
      return this.message;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PlayerDialogueData)) {
         return false;
      } else {
         PlayerDialogueData other = (PlayerDialogueData)o;
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

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PlayerDialogueData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      return result;
   }

   public String toString() {
      return "PlayerDialogueData(message=" + this.getMessage() + ")";
   }

   public PlayerDialogueData(String message) {
      this.message = message;
   }
}
