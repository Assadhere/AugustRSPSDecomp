package net.runelite.client.plugins.crowdsourcing.dialogue;

public class DoubleSpriteTextData {
   private String message;
   private int itemId1;
   private int itemId2;

   public String getMessage() {
      return this.message;
   }

   public int getItemId1() {
      return this.itemId1;
   }

   public int getItemId2() {
      return this.itemId2;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public void setItemId1(int itemId1) {
      this.itemId1 = itemId1;
   }

   public void setItemId2(int itemId2) {
      this.itemId2 = itemId2;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DoubleSpriteTextData)) {
         return false;
      } else {
         DoubleSpriteTextData other = (DoubleSpriteTextData)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getItemId1() != other.getItemId1()) {
            return false;
         } else if (this.getItemId2() != other.getItemId2()) {
            return false;
         } else {
            Object this$message = this.getMessage();
            Object other$message = other.getMessage();
            if (this$message == null) {
               if (other$message == null) {
                  return true;
               }
            } else if (this$message.equals(other$message)) {
               return true;
            }

            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof DoubleSpriteTextData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getItemId1();
      result = result * 59 + this.getItemId2();
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getMessage();
      return "DoubleSpriteTextData(message=" + var10000 + ", itemId1=" + this.getItemId1() + ", itemId2=" + this.getItemId2() + ")";
   }

   public DoubleSpriteTextData(String message, int itemId1, int itemId2) {
      this.message = message;
      this.itemId1 = itemId1;
      this.itemId2 = itemId2;
   }
}
