package net.runelite.api.events;

import net.runelite.api.GrandExchangeOffer;

public class GrandExchangeOfferChanged {
   private GrandExchangeOffer offer;
   private int slot;

   public GrandExchangeOffer getOffer() {
      return this.offer;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setOffer(GrandExchangeOffer offer) {
      this.offer = offer;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GrandExchangeOfferChanged)) {
         return false;
      } else {
         GrandExchangeOfferChanged other = (GrandExchangeOfferChanged)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getSlot() != other.getSlot()) {
            return false;
         } else {
            Object this$offer = this.getOffer();
            Object other$offer = other.getOffer();
            if (this$offer == null) {
               if (other$offer != null) {
                  return false;
               }
            } else if (!this$offer.equals(other$offer)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof GrandExchangeOfferChanged;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getSlot();
      Object $offer = this.getOffer();
      result = result * 59 + ($offer == null ? 43 : $offer.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getOffer());
      return "GrandExchangeOfferChanged(offer=" + var10000 + ", slot=" + this.getSlot() + ")";
   }
}
