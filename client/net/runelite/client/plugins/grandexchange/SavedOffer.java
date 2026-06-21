package net.runelite.client.plugins.grandexchange;

import net.runelite.api.GrandExchangeOfferState;

class SavedOffer {
   private int itemId;
   private int quantitySold;
   private int totalQuantity;
   private int price;
   private int spent;
   private GrandExchangeOfferState state;

   public SavedOffer() {
   }

   public int getItemId() {
      return this.itemId;
   }

   public int getQuantitySold() {
      return this.quantitySold;
   }

   public int getTotalQuantity() {
      return this.totalQuantity;
   }

   public int getPrice() {
      return this.price;
   }

   public int getSpent() {
      return this.spent;
   }

   public GrandExchangeOfferState getState() {
      return this.state;
   }

   public void setItemId(int itemId) {
      this.itemId = itemId;
   }

   public void setQuantitySold(int quantitySold) {
      this.quantitySold = quantitySold;
   }

   public void setTotalQuantity(int totalQuantity) {
      this.totalQuantity = totalQuantity;
   }

   public void setPrice(int price) {
      this.price = price;
   }

   public void setSpent(int spent) {
      this.spent = spent;
   }

   public void setState(GrandExchangeOfferState state) {
      this.state = state;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SavedOffer)) {
         return false;
      } else {
         SavedOffer other = (SavedOffer)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getItemId() != other.getItemId()) {
            return false;
         } else if (this.getQuantitySold() != other.getQuantitySold()) {
            return false;
         } else if (this.getTotalQuantity() != other.getTotalQuantity()) {
            return false;
         } else if (this.getPrice() != other.getPrice()) {
            return false;
         } else if (this.getSpent() != other.getSpent()) {
            return false;
         } else {
            Object this$state = this.getState();
            Object other$state = other.getState();
            if (this$state == null) {
               if (other$state != null) {
                  return false;
               }
            } else if (!this$state.equals(other$state)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SavedOffer;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getItemId();
      result = result * 59 + this.getQuantitySold();
      result = result * 59 + this.getTotalQuantity();
      result = result * 59 + this.getPrice();
      result = result * 59 + this.getSpent();
      Object $state = this.getState();
      result = result * 59 + ($state == null ? 43 : $state.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getItemId();
      return "SavedOffer(itemId=" + var10000 + ", quantitySold=" + this.getQuantitySold() + ", totalQuantity=" + this.getTotalQuantity() + ", price=" + this.getPrice() + ", spent=" + this.getSpent() + ", state=" + String.valueOf(this.getState()) + ")";
   }
}
