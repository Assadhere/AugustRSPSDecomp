package net.runelite.client.game;

import com.google.gson.annotations.SerializedName;

public final class ItemStats {
   private final boolean equipable;
   private final double weight;
   @SerializedName("ge_limit")
   private final int geLimit;
   private final ItemEquipmentStats equipment;

   net.runelite.http.api.item.ItemStats toHttpApiFormat() {
      net.runelite.http.api.item.ItemEquipmentStats equipment = this.equipment == null ? null : this.equipment.toHttpApiFormat();
      return new net.runelite.http.api.item.ItemStats(this.equipable, this.weight, this.geLimit, equipment);
   }

   public ItemStats(boolean equipable, double weight, int geLimit, ItemEquipmentStats equipment) {
      this.equipable = equipable;
      this.weight = weight;
      this.geLimit = geLimit;
      this.equipment = equipment;
   }

   public boolean isEquipable() {
      return this.equipable;
   }

   public double getWeight() {
      return this.weight;
   }

   public int getGeLimit() {
      return this.geLimit;
   }

   public ItemEquipmentStats getEquipment() {
      return this.equipment;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ItemStats)) {
         return false;
      } else {
         ItemStats other = (ItemStats)o;
         if (this.isEquipable() != other.isEquipable()) {
            return false;
         } else if (Double.compare(this.getWeight(), other.getWeight()) != 0) {
            return false;
         } else if (this.getGeLimit() != other.getGeLimit()) {
            return false;
         } else {
            Object this$equipment = this.getEquipment();
            Object other$equipment = other.getEquipment();
            if (this$equipment == null) {
               if (other$equipment == null) {
                  return true;
               }
            } else if (this$equipment.equals(other$equipment)) {
               return true;
            }

            return false;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isEquipable() ? 79 : 97);
      long $weight = Double.doubleToLongBits(this.getWeight());
      result = result * 59 + (int)($weight >>> 32 ^ $weight);
      result = result * 59 + this.getGeLimit();
      Object $equipment = this.getEquipment();
      result = result * 59 + ($equipment == null ? 43 : $equipment.hashCode());
      return result;
   }

   public String toString() {
      boolean var10000 = this.isEquipable();
      return "ItemStats(equipable=" + var10000 + ", weight=" + this.getWeight() + ", geLimit=" + this.getGeLimit() + ", equipment=" + String.valueOf(this.getEquipment()) + ")";
   }
}
