package custom;

import java.util.List;

public class UpdateGrandExchangeHistoryScript {
   private final int requestId;
   private final int rangeHours;
   private final int page;
   private final int limit;
   private final boolean hasMore;
   private final List<Transaction> transactions;

   public UpdateGrandExchangeHistoryScript(int var1, int var2, int var3, int var4, boolean var5, List<Transaction> var6) {
      this.requestId = var1;
      this.rangeHours = var2;
      this.page = var3;
      this.limit = var4;
      this.hasMore = var5;
      this.transactions = var6;
   }

   public int getRequestId() {
      return this.requestId;
   }

   public int getRangeHours() {
      return this.rangeHours;
   }

   public int getPage() {
      return this.page;
   }

   public int getLimit() {
      return this.limit;
   }

   public boolean isHasMore() {
      return this.hasMore;
   }

   public List<Transaction> getTransactions() {
      return this.transactions;
   }

   public static class Transaction {
      private final int itemId;
      private final int quantity;
      private final int price;
      private final long timestampEpochMs;

      public Transaction(int var1, int var2, int var3, long var4) {
         this.itemId = var1;
         this.quantity = var2;
         this.price = var3;
         this.timestampEpochMs = var4;
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

      public long getTimestampEpochMs() {
         return this.timestampEpochMs;
      }
   }
}
