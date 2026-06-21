package net.runelite.client.plugins.loottracker;

import java.util.Collection;
import javax.annotation.Nullable;
import lombok.NonNull;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.Plugin;
import net.runelite.http.api.loottracker.LootRecordType;

public final class PluginLootReceived {
   private final @NonNull Plugin source;
   private final @NonNull String name;
   private final int combatLevel;
   private final @NonNull LootRecordType type;
   private final @NonNull Collection<ItemStack> items;
   private final int amount;
   @Nullable
   private final Object metadata;

   private static int $default$combatLevel() {
      return -1;
   }

   private static int $default$amount() {
      return 1;
   }

   PluginLootReceived(@NonNull Plugin source, @NonNull String name, int combatLevel, @NonNull LootRecordType type, @NonNull Collection<ItemStack> items, int amount, @Nullable Object metadata) {
      if (source == null) {
         throw new NullPointerException("source is marked non-null but is null");
      } else if (name == null) {
         throw new NullPointerException("name is marked non-null but is null");
      } else if (type == null) {
         throw new NullPointerException("type is marked non-null but is null");
      } else if (items == null) {
         throw new NullPointerException("items is marked non-null but is null");
      } else {
         this.source = source;
         this.name = name;
         this.combatLevel = combatLevel;
         this.type = type;
         this.items = items;
         this.amount = amount;
         this.metadata = metadata;
      }
   }

   public static PluginLootReceivedBuilder builder() {
      return new PluginLootReceivedBuilder();
   }

   public @NonNull Plugin getSource() {
      return this.source;
   }

   public @NonNull String getName() {
      return this.name;
   }

   public int getCombatLevel() {
      return this.combatLevel;
   }

   public @NonNull LootRecordType getType() {
      return this.type;
   }

   public @NonNull Collection<ItemStack> getItems() {
      return this.items;
   }

   public int getAmount() {
      return this.amount;
   }

   @Nullable
   public Object getMetadata() {
      return this.metadata;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PluginLootReceived)) {
         return false;
      } else {
         PluginLootReceived other = (PluginLootReceived)o;
         if (this.getCombatLevel() != other.getCombatLevel()) {
            return false;
         } else if (this.getAmount() != other.getAmount()) {
            return false;
         } else {
            label73: {
               Object this$source = this.getSource();
               Object other$source = other.getSource();
               if (this$source == null) {
                  if (other$source == null) {
                     break label73;
                  }
               } else if (this$source.equals(other$source)) {
                  break label73;
               }

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

            label59: {
               Object this$type = this.getType();
               Object other$type = other.getType();
               if (this$type == null) {
                  if (other$type == null) {
                     break label59;
                  }
               } else if (this$type.equals(other$type)) {
                  break label59;
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

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getCombatLevel();
      result = result * 59 + this.getAmount();
      Object $source = this.getSource();
      result = result * 59 + ($source == null ? 43 : $source.hashCode());
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
      String var10000 = String.valueOf(this.getSource());
      return "PluginLootReceived(source=" + var10000 + ", name=" + this.getName() + ", combatLevel=" + this.getCombatLevel() + ", type=" + String.valueOf(this.getType()) + ", items=" + String.valueOf(this.getItems()) + ", amount=" + this.getAmount() + ", metadata=" + String.valueOf(this.getMetadata()) + ")";
   }

   public static class PluginLootReceivedBuilder {
      private Plugin source;
      private String name;
      private boolean combatLevel$set;
      private int combatLevel$value;
      private LootRecordType type;
      private Collection<ItemStack> items;
      private boolean amount$set;
      private int amount$value;
      private Object metadata;

      PluginLootReceivedBuilder() {
      }

      public PluginLootReceivedBuilder source(@NonNull Plugin source) {
         if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
         } else {
            this.source = source;
            return this;
         }
      }

      public PluginLootReceivedBuilder name(@NonNull String name) {
         if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
         } else {
            this.name = name;
            return this;
         }
      }

      public PluginLootReceivedBuilder combatLevel(int combatLevel) {
         this.combatLevel$value = combatLevel;
         this.combatLevel$set = true;
         return this;
      }

      public PluginLootReceivedBuilder type(@NonNull LootRecordType type) {
         if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
         } else {
            this.type = type;
            return this;
         }
      }

      public PluginLootReceivedBuilder items(@NonNull Collection<ItemStack> items) {
         if (items == null) {
            throw new NullPointerException("items is marked non-null but is null");
         } else {
            this.items = items;
            return this;
         }
      }

      public PluginLootReceivedBuilder amount(int amount) {
         this.amount$value = amount;
         this.amount$set = true;
         return this;
      }

      public PluginLootReceivedBuilder metadata(@Nullable Object metadata) {
         this.metadata = metadata;
         return this;
      }

      public PluginLootReceived build() {
         int combatLevel$value = this.combatLevel$value;
         if (!this.combatLevel$set) {
            combatLevel$value = PluginLootReceived.$default$combatLevel();
         }

         int amount$value = this.amount$value;
         if (!this.amount$set) {
            amount$value = PluginLootReceived.$default$amount();
         }

         return new PluginLootReceived(this.source, this.name, combatLevel$value, this.type, this.items, amount$value, this.metadata);
      }

      public String toString() {
         String var10000 = String.valueOf(this.source);
         return "PluginLootReceived.PluginLootReceivedBuilder(source=" + var10000 + ", name=" + this.name + ", combatLevel$value=" + this.combatLevel$value + ", type=" + String.valueOf(this.type) + ", items=" + String.valueOf(this.items) + ", amount$value=" + this.amount$value + ", metadata=" + String.valueOf(this.metadata) + ")";
      }
   }
}
