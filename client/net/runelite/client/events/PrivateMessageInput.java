package net.runelite.client.events;

public final class PrivateMessageInput extends ChatInput {
   private final String target;
   private final String message;

   public PrivateMessageInput(String target, String message, Runnable resume) {
      super(resume);
      this.target = target;
      this.message = message;
   }

   public String getTarget() {
      return this.target;
   }

   public String getMessage() {
      return this.message;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PrivateMessageInput)) {
         return false;
      } else {
         PrivateMessageInput other = (PrivateMessageInput)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$target = this.getTarget();
            Object other$target = other.getTarget();
            if (this$target == null) {
               if (other$target != null) {
                  return false;
               }
            } else if (!this$target.equals(other$target)) {
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

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PrivateMessageInput;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $target = this.getTarget();
      result = result * 59 + ($target == null ? 43 : $target.hashCode());
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getTarget();
      return "PrivateMessageInput(target=" + var10000 + ", message=" + this.getMessage() + ")";
   }
}
