package net.runelite.client.plugins.loottracker;

import java.util.Collection;
import net.runelite.client.game.ItemStack;
import net.runelite.http.api.loottracker.LootRecordType;

public class LootReceived {
   private String name;
   private int combatLevel;
   private LootRecordType type;
   private Collection<ItemStack> items;
   private int amount;
   private Object metadata;

   public String getName() {
      return this.name;
   }

   public int getCombatLevel() {
      return this.combatLevel;
   }

   public LootRecordType getType() {
      return this.type;
   }

   public Collection<ItemStack> getItems() {
      return this.items;
   }

   public int getAmount() {
      return this.amount;
   }

   public Object getMetadata() {
      return this.metadata;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setCombatLevel(int combatLevel) {
      this.combatLevel = combatLevel;
   }

   public void setType(LootRecordType type) {
      this.type = type;
   }

   public void setItems(Collection<ItemStack> items) {
      this.items = items;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   public void setMetadata(Object metadata) {
      this.metadata = metadata;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LootReceived)) {
         return false;
      } else {
         LootReceived other = (LootReceived)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getCombatLevel() != other.getCombatLevel()) {
            return false;
         } else if (this.getAmount() != other.getAmount()) {
            return false;
         } else {
            label64: {
               Object this$name = this.getName();
               Object other$name = other.getName();
               if (this$name == null) {
                  if (other$name == null) {
                     break label64;
                  }
               } else if (this$name.equals(other$name)) {
                  break label64;
               }

               return false;
            }

            label57: {
               Object this$type = this.getType();
               Object other$type = other.getType();
               if (this$type == null) {
                  if (other$type == null) {
                     break label57;
                  }
               } else if (this$type.equals(other$type)) {
                  break label57;
               }

               return false;
            }

            Object this$items = this.getItems();
            Object other$items = other.getItems();
            if (this$items == null) {
               if (other$items != null) {
                  return false;
               }
            } else if (!this$items.equals(other$items)) {
               return false;
            }

            Object this$metadata = this.getMetadata();
            Object other$metadata = other.getMetadata();
            if (this$metadata == null) {
               if (other$metadata != null) {
                  return false;
               }
            } else if (!this$metadata.equals(other$metadata)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof LootReceived;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getCombatLevel();
      result = result * 59 + this.getAmount();
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $items = this.getItems();
      result = result * 59 + ($items == null ? 43 : $items.hashCode());
      Object $metadata = this.getMetadata();
      result = result * 59 + ($metadata == null ? 43 : $metadata.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getName();
      return "LootReceived(name=" + var10000 + ", combatLevel=" + this.getCombatLevel() + ", type=" + String.valueOf(this.getType()) + ", items=" + String.valueOf(this.getItems()) + ", amount=" + this.getAmount() + ", metadata=" + String.valueOf(this.getMetadata()) + ")";
   }

   public LootReceived(String name, int combatLevel, LootRecordType type, Collection<ItemStack> items, int amount, Object metadata) {
      this.name = name;
      this.combatLevel = combatLevel;
      this.type = type;
      this.items = items;
      this.amount = amount;
      this.metadata = metadata;
   }
}
