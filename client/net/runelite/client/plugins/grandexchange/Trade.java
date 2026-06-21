package net.runelite.client.plugins.grandexchange;

import com.google.gson.annotations.SerializedName;
import java.time.Instant;

class Trade {
   @SerializedName("b")
   boolean buy;
   @SerializedName("i")
   int itemId;
   @SerializedName("q")
   int quantity;
   @SerializedName("p")
   int price;
   @SerializedName("t")
   Instant time;

   public Trade() {
   }

   public boolean isBuy() {
      return this.buy;
   }

   public int getItemId() {
      return this.itemId;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public int getPrice() {
      return this.price;
   }

   public Instant getTime() {
      return this.time;
   }

   public void setBuy(boolean buy) {
      this.buy = buy;
   }

   public void setItemId(int itemId) {
      this.itemId = itemId;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public void setPrice(int price) {
      this.price = price;
   }

   public void setTime(Instant time) {
      this.time = time;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Trade)) {
         return false;
      } else {
         Trade other = (Trade)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.isBuy() != other.isBuy()) {
            return false;
         } else if (this.getItemId() != other.getItemId()) {
            return false;
         } else if (this.getQuantity() != other.getQuantity()) {
            return false;
         } else if (this.getPrice() != other.getPrice()) {
            return false;
         } else {
            Object this$time = this.getTime();
            Object other$time = other.getTime();
            if (this$time == null) {
               if (other$time != null) {
                  return false;
               }
            } else if (!this$time.equals(other$time)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof Trade;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isBuy() ? 79 : 97);
      result = result * 59 + this.getItemId();
      result = result * 59 + this.getQuantity();
      result = result * 59 + this.getPrice();
      Object $time = this.getTime();
      result = result * 59 + ($time == null ? 43 : $time.hashCode());
      return result;
   }

   public String toString() {
      boolean var10000 = this.isBuy();
      return "Trade(buy=" + var10000 + ", itemId=" + this.getItemId() + ", quantity=" + this.getQuantity() + ", price=" + this.getPrice() + ", time=" + String.valueOf(this.getTime()) + ")";
   }
}
