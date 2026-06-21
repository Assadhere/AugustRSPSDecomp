package custom;

import java.util.Arrays;
import java.util.Map;

public class UpdateItemStatInfoScript {
   private final Map<Integer, ItemStatInfo> itemStatInfo;

   public UpdateItemStatInfoScript(Map<Integer, ItemStatInfo> var1) {
      this.itemStatInfo = var1;
   }

   public Map<Integer, ItemStatInfo> getItemStatInfo() {
      return this.itemStatInfo;
   }

   public String toString() {
      return "UpdateItemStatInfoScript(itemStatInfo=" + String.valueOf(this.getItemStatInfo()) + ")";
   }

   public static class ItemStatInfo {
      private final int equipSlot;
      private final boolean isTwoHanded;
      private final int attackSpeed;
      private final double weight;
      private final int[] bonuses;

      public ItemStatInfo(int var1, boolean var2, int var3, int var4, int[] var5) {
         this.equipSlot = var1;
         this.isTwoHanded = var2;
         this.attackSpeed = var3;
         this.weight = (double)var4 / 10.0;
         this.bonuses = var5;
      }

      public int getEquipSlot() {
         return this.equipSlot;
      }

      public boolean isTwoHanded() {
         return this.isTwoHanded;
      }

      public int getAttackSpeed() {
         return this.attackSpeed;
      }

      public double getWeight() {
         return this.weight;
      }

      public int[] getBonuses() {
         return this.bonuses;
      }

      public String toString() {
         int var10000 = this.getEquipSlot();
         return "UpdateItemStatInfoScript.ItemStatInfo(equipSlot=" + var10000 + ", isTwoHanded=" + this.isTwoHanded() + ", attackSpeed=" + this.getAttackSpeed() + ", weight=" + this.getWeight() + ", bonuses=" + Arrays.toString(this.getBonuses()) + ")";
      }
   }
}
