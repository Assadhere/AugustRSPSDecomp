package net.runelite.client.plugins.bank;

final class ContainerPrices {
   private final long gePrice;
   private final long highAlchPrice;

   public ContainerPrices(long gePrice, long highAlchPrice) {
      this.gePrice = gePrice;
      this.highAlchPrice = highAlchPrice;
   }

   public long getGePrice() {
      return this.gePrice;
   }

   public long getHighAlchPrice() {
      return this.highAlchPrice;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ContainerPrices)) {
         return false;
      } else {
         ContainerPrices other = (ContainerPrices)o;
         if (this.getGePrice() != other.getGePrice()) {
            return false;
         } else {
            return this.getHighAlchPrice() == other.getHighAlchPrice();
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $gePrice = this.getGePrice();
      result = result * 59 + (int)($gePrice >>> 32 ^ $gePrice);
      long $highAlchPrice = this.getHighAlchPrice();
      result = result * 59 + (int)($highAlchPrice >>> 32 ^ $highAlchPrice);
      return result;
   }

   public String toString() {
      long var10000 = this.getGePrice();
      return "ContainerPrices(gePrice=" + var10000 + ", highAlchPrice=" + this.getHighAlchPrice() + ")";
   }
}
