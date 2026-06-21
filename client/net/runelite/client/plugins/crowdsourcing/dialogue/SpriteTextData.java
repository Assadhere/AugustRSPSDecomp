package net.runelite.client.plugins.crowdsourcing.dialogue;

public class SpriteTextData {
   private String message;
   private int itemId1;

   public String getMessage() {
      return this.message;
   }

   public int getItemId1() {
      return this.itemId1;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public void setItemId1(int itemId1) {
      this.itemId1 = itemId1;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SpriteTextData)) {
         return false;
      } else {
         SpriteTextData other = (SpriteTextData)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getItemId1() != other.getItemId1()) {
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
      return other instanceof SpriteTextData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getItemId1();
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getMessage();
      return "SpriteTextData(message=" + var10000 + ", itemId1=" + this.getItemId1() + ")";
   }

   public SpriteTextData(String message, int itemId1) {
      this.message = message;
      this.itemId1 = itemId1;
   }
}
