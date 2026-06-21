package net.runelite.client.plugins.examine;

import net.runelite.api.ChatMessageType;

class PendingExamine {
   private ChatMessageType responseType;
   private int id;
   private int quantity;

   public PendingExamine() {
   }

   public ChatMessageType getResponseType() {
      return this.responseType;
   }

   public int getId() {
      return this.id;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public void setResponseType(ChatMessageType responseType) {
      this.responseType = responseType;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PendingExamine)) {
         return false;
      } else {
         PendingExamine other = (PendingExamine)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getId() != other.getId()) {
            return false;
         } else if (this.getQuantity() != other.getQuantity()) {
            return false;
         } else {
            Object this$responseType = this.getResponseType();
            Object other$responseType = other.getResponseType();
            if (this$responseType == null) {
               if (other$responseType == null) {
                  return true;
               }
            } else if (this$responseType.equals(other$responseType)) {
               return true;
            }

            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PendingExamine;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getId();
      result = result * 59 + this.getQuantity();
      Object $responseType = this.getResponseType();
      result = result * 59 + ($responseType == null ? 43 : $responseType.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getResponseType());
      return "PendingExamine(responseType=" + var10000 + ", id=" + this.getId() + ", quantity=" + this.getQuantity() + ")";
   }
}
