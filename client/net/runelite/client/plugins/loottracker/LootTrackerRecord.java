package net.runelite.client.plugins.loottracker;

import java.util.Arrays;
import lombok.NonNull;
import net.runelite.http.api.loottracker.LootRecordType;

class LootTrackerRecord {
   private final @NonNull String title;
   private final String subTitle;
   private final LootRecordType type;
   private LootTrackerItem[] items;
   private int kills;

   boolean matches(String id, LootRecordType type) {
      if (id == null) {
         return true;
      } else {
         return this.title.equals(id) && this.type == type;
      }
   }

   void merge(LootTrackerRecord record) {
      assert this.title.equals(record.title);

      assert this.type == record.type;

      LootTrackerItem[] var2 = record.items;
      int var3 = var2.length;

      label40:
      for(int var4 = 0; var4 < var3; ++var4) {
         LootTrackerItem item = var2[var4];

         for(int idx = 0; idx < this.items.length; ++idx) {
            LootTrackerItem r = this.items[idx];
            if (r.getId() == item.getId()) {
               int qty = r.getQuantity() + item.getQuantity();
               if (qty < 0) {
                  qty = Integer.MAX_VALUE;
               }

               this.items[idx] = new LootTrackerItem(r.getId(), r.getName(), qty, r.getGePrice(), r.getHaPrice(), r.isIgnored());
               continue label40;
            }
         }

         this.items = (LootTrackerItem[])Arrays.copyOf(this.items, this.items.length + 1);
         this.items[this.items.length - 1] = item;
      }

      this.kills += record.kills;
   }

   public @NonNull String getTitle() {
      return this.title;
   }

   public String getSubTitle() {
      return this.subTitle;
   }

   public LootRecordType getType() {
      return this.type;
   }

   public LootTrackerItem[] getItems() {
      return this.items;
   }

   public int getKills() {
      return this.kills;
   }

   public LootTrackerRecord(@NonNull String title, String subTitle, LootRecordType type, LootTrackerItem[] items, int kills) {
      if (title == null) {
         throw new NullPointerException("title is marked non-null but is null");
      } else {
         this.title = title;
         this.subTitle = subTitle;
         this.type = type;
         this.items = items;
         this.kills = kills;
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LootTrackerRecord)) {
         return false;
      } else {
         LootTrackerRecord other = (LootTrackerRecord)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$title = this.getTitle();
            Object other$title = other.getTitle();
            if (this$title == null) {
               if (other$title != null) {
                  return false;
               }
            } else if (!this$title.equals(other$title)) {
               return false;
            }

            Object this$type = this.getType();
            Object other$type = other.getType();
            if (this$type == null) {
               if (other$type != null) {
                  return false;
               }
            } else if (!this$type.equals(other$type)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof LootTrackerRecord;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $title = this.getTitle();
      result = result * 59 + ($title == null ? 43 : $title.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      return result;
   }
}
