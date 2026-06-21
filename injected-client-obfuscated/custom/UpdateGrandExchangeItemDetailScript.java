package custom;

import java.util.List;

public class UpdateGrandExchangeItemDetailScript {
   private final int requestId;
   private final int itemId;
   private final int rangeHours;
   private final long activeSellQuantity;
   private final long activeBuyQuantity;
   private final long transactionCount;
   private final long transactionVolume;
   private final long transactionTotalGp;
   private final long averagePrice;
   private final long minPrice;
   private final long maxPrice;
   private final long lastPrice;
   private final List<Order> orders;

   public UpdateGrandExchangeItemDetailScript(int var1, int var2, int var3, long var4, long var6, long var8, long var10, long var12, long var14, long var16, long var18, long var20, List<Order> var22) {
      this.requestId = var1;
      this.itemId = var2;
      this.rangeHours = var3;
      this.activeSellQuantity = var4;
      this.activeBuyQuantity = var6;
      this.transactionCount = var8;
      this.transactionVolume = var10;
      this.transactionTotalGp = var12;
      this.averagePrice = var14;
      this.minPrice = var16;
      this.maxPrice = var18;
      this.lastPrice = var20;
      this.orders = var22;
   }

   public int getRequestId() {
      return this.requestId;
   }

   public int getItemId() {
      return this.itemId;
   }

   public int getRangeHours() {
      return this.rangeHours;
   }

   public long getActiveSellQuantity() {
      return this.activeSellQuantity;
   }

   public long getActiveBuyQuantity() {
      return this.activeBuyQuantity;
   }

   public long getTransactionCount() {
      return this.transactionCount;
   }

   public long getTransactionVolume() {
      return this.transactionVolume;
   }

   public long getTransactionTotalGp() {
      return this.transactionTotalGp;
   }

   public long getAveragePrice() {
      return this.averagePrice;
   }

   public long getMinPrice() {
      return this.minPrice;
   }

   public long getMaxPrice() {
      return this.maxPrice;
   }

   public long getLastPrice() {
      return this.lastPrice;
   }

   public List<Order> getOrders() {
      return this.orders;
   }

   public static class Order {
      private final String type;
      private final int itemQuantity;
      private final int price;
      private final String status;
      private final long fulfilledQuantity;
      private final long createdEpochMs;

      public Order(String var1, int var2, int var3, String var4, long var5, long var7) {
         this.type = var1;
         this.itemQuantity = var2;
         this.price = var3;
         this.status = var4;
         this.fulfilledQuantity = var5;
         this.createdEpochMs = var7;
      }

      public String getType() {
         return this.type;
      }

      public int getItemQuantity() {
         return this.itemQuantity;
      }

      public int getPrice() {
         return this.price;
      }

      public String getStatus() {
         return this.status;
      }

      public long getFulfilledQuantity() {
         return this.fulfilledQuantity;
      }

      public long getCreatedEpochMs() {
         return this.createdEpochMs;
      }
   }
}
