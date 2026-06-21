package net.runelite.client.discord.events;

public final class DiscordErrored {
   private final int errorCode;
   private final String message;

   public DiscordErrored(int errorCode, String message) {
      this.errorCode = errorCode;
      this.message = message;
   }

   public int getErrorCode() {
      return this.errorCode;
   }

   public String getMessage() {
      return this.message;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DiscordErrored)) {
         return false;
      } else {
         DiscordErrored other = (DiscordErrored)o;
         if (this.getErrorCode() != other.getErrorCode()) {
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

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getErrorCode();
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getErrorCode();
      return "DiscordErrored(errorCode=" + var10000 + ", message=" + this.getMessage() + ")";
   }
}
