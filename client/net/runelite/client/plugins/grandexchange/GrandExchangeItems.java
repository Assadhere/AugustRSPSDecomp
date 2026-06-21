package net.runelite.client.plugins.grandexchange;

import net.runelite.client.util.AsyncBufferedImage;

final class GrandExchangeItems {
   private final AsyncBufferedImage icon;
   private final String name;
   private final int itemId;
   private final int gePrice;
   private final int haPrice;
   private final int geItemLimit;

   public GrandExchangeItems(AsyncBufferedImage icon, String name, int itemId, int gePrice, int haPrice, int geItemLimit) {
      this.icon = icon;
      this.name = name;
      this.itemId = itemId;
      this.gePrice = gePrice;
      this.haPrice = haPrice;
      this.geItemLimit = geItemLimit;
   }

   public AsyncBufferedImage getIcon() {
      return this.icon;
   }

   public String getName() {
      return this.name;
   }

   public int getItemId() {
      return this.itemId;
   }

   public int getGePrice() {
      return this.gePrice;
   }

   public int getHaPrice() {
      return this.haPrice;
   }

   public int getGeItemLimit() {
      return this.geItemLimit;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GrandExchangeItems)) {
         return false;
      } else {
         GrandExchangeItems other = (GrandExchangeItems)o;
         if (this.getItemId() != other.getItemId()) {
            return false;
         } else if (this.getGePrice() != other.getGePrice()) {
            return false;
         } else if (this.getHaPrice() != other.getHaPrice()) {
            return false;
         } else if (this.getGeItemLimit() != other.getGeItemLimit()) {
            return false;
         } else {
            Object this$icon = this.getIcon();
            Object other$icon = other.getIcon();
            if (this$icon == null) {
               if (other$icon != null) {
                  return false;
               }
            } else if (!this$icon.equals(other$icon)) {
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

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getItemId();
      result = result * 59 + this.getGePrice();
      result = result * 59 + this.getHaPrice();
      result = result * 59 + this.getGeItemLimit();
      Object $icon = this.getIcon();
      result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getIcon());
      return "GrandExchangeItems(icon=" + var10000 + ", name=" + this.getName() + ", itemId=" + this.getItemId() + ", gePrice=" + this.getGePrice() + ", haPrice=" + this.getHaPrice() + ", geItemLimit=" + this.getGeItemLimit() + ")";
   }
}
