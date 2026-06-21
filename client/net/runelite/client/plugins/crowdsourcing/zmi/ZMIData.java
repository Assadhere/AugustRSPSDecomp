package net.runelite.client.plugins.crowdsourcing.zmi;

import com.google.common.collect.Multiset;

public class ZMIData {
   private final int tickDelta;
   private final boolean ardougneMedium;
   private final int level;
   private final int xpGained;
   private final Multiset<Integer> itemsReceived;
   private final Multiset<Integer> itemsRemoved;

   public int getTickDelta() {
      return this.tickDelta;
   }

   public boolean isArdougneMedium() {
      return this.ardougneMedium;
   }

   public int getLevel() {
      return this.level;
   }

   public int getXpGained() {
      return this.xpGained;
   }

   public Multiset<Integer> getItemsReceived() {
      return this.itemsReceived;
   }

   public Multiset<Integer> getItemsRemoved() {
      return this.itemsRemoved;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ZMIData)) {
         return false;
      } else {
         ZMIData other = (ZMIData)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getTickDelta() != other.getTickDelta()) {
            return false;
         } else if (this.isArdougneMedium() != other.isArdougneMedium()) {
            return false;
         } else if (this.getLevel() != other.getLevel()) {
            return false;
         } else if (this.getXpGained() != other.getXpGained()) {
            return false;
         } else {
            Object this$itemsReceived = this.getItemsReceived();
            Object other$itemsReceived = other.getItemsReceived();
            if (this$itemsReceived == null) {
               if (other$itemsReceived != null) {
                  return false;
               }
            } else if (!this$itemsReceived.equals(other$itemsReceived)) {
               return false;
            }

            Object this$itemsRemoved = this.getItemsRemoved();
            Object other$itemsRemoved = other.getItemsRemoved();
            if (this$itemsRemoved == null) {
               if (other$itemsRemoved != null) {
                  return false;
               }
            } else if (!this$itemsRemoved.equals(other$itemsRemoved)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ZMIData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getTickDelta();
      result = result * 59 + (this.isArdougneMedium() ? 79 : 97);
      result = result * 59 + this.getLevel();
      result = result * 59 + this.getXpGained();
      Object $itemsReceived = this.getItemsReceived();
      result = result * 59 + ($itemsReceived == null ? 43 : $itemsReceived.hashCode());
      Object $itemsRemoved = this.getItemsRemoved();
      result = result * 59 + ($itemsRemoved == null ? 43 : $itemsRemoved.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getTickDelta();
      return "ZMIData(tickDelta=" + var10000 + ", ardougneMedium=" + this.isArdougneMedium() + ", level=" + this.getLevel() + ", xpGained=" + this.getXpGained() + ", itemsReceived=" + String.valueOf(this.getItemsReceived()) + ", itemsRemoved=" + String.valueOf(this.getItemsRemoved()) + ")";
   }

   public ZMIData(int tickDelta, boolean ardougneMedium, int level, int xpGained, Multiset<Integer> itemsReceived, Multiset<Integer> itemsRemoved) {
      this.tickDelta = tickDelta;
      this.ardougneMedium = ardougneMedium;
      this.level = level;
      this.xpGained = xpGained;
      this.itemsReceived = itemsReceived;
      this.itemsRemoved = itemsRemoved;
   }
}
