package custom;

import java.util.Arrays;

public class AddToLootTrackerScript {
   private final String name;
   private final int amount;
   private final int[] itemIds;
   private final int[] itemAmounts;

   public AddToLootTrackerScript(String var1, int var2, int[] var3, int[] var4) {
      this.name = var1;
      this.amount = var2;
      this.itemIds = var3;
      this.itemAmounts = var4;
   }

   public String getName() {
      return this.name;
   }

   public int getAmount() {
      return this.amount;
   }

   public int[] getItemIds() {
      return this.itemIds;
   }

   public int[] getItemAmounts() {
      return this.itemAmounts;
   }

   public String toString() {
      String var10000 = this.getName();
      return "AddToLootTrackerScript(name=" + var10000 + ", amount=" + this.getAmount() + ", itemIds=" + Arrays.toString(this.getItemIds()) + ", itemAmounts=" + Arrays.toString(this.getItemAmounts()) + ")";
   }
}
