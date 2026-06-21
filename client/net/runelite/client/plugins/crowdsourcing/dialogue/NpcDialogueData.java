package net.runelite.client.plugins.crowdsourcing.dialogue;

public class NpcDialogueData {
   private final String message;
   private final String name;

   public String getMessage() {
      return this.message;
   }

   public String getName() {
      return this.name;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NpcDialogueData)) {
         return false;
      } else {
         NpcDialogueData other = (NpcDialogueData)o;
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

            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
               if (other$name != null) {
                  return false;
               }
            } else if (!this$name.equals(other$name)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof NpcDialogueData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getMessage();
      return "NpcDialogueData(message=" + var10000 + ", name=" + this.getName() + ")";
   }

   public NpcDialogueData(String message, String name) {
      this.message = message;
      this.name = name;
   }
}
