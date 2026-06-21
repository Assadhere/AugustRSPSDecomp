package net.runelite.client.plugins.loottracker;

import java.time.Instant;
import java.util.Arrays;
import net.runelite.http.api.loottracker.LootRecordType;

class ConfigLoot {
   LootRecordType type;
   String name;
   int kills;
   Instant first = Instant.now();
   Instant last;
   int[] drops;

   ConfigLoot(LootRecordType type, String name) {
      this.type = type;
      this.name = name;
      this.drops = new int[0];
   }

   void add(int id, int qty) {
      for(int i = 0; i < this.drops.length; i += 2) {
         if (this.drops[i] == id) {
            int[] var10000 = this.drops;
            var10000[i + 1] += qty;
            if (this.drops[i + 1] < 0) {
               this.drops[i + 1] = Integer.MAX_VALUE;
            }

            return;
         }
      }

      this.drops = Arrays.copyOf(this.drops, this.drops.length + 2);
      this.drops[this.drops.length - 2] = id;
      this.drops[this.drops.length - 1] = qty;
   }

   int numDrops() {
      return this.drops.length / 2;
   }

   public LootRecordType getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public int getKills() {
      return this.kills;
   }

   public Instant getFirst() {
      return this.first;
   }

   public Instant getLast() {
      return this.last;
   }

   public int[] getDrops() {
      return this.drops;
   }

   public void setType(LootRecordType type) {
      this.type = type;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setKills(int kills) {
      this.kills = kills;
   }

   public void setFirst(Instant first) {
      this.first = first;
   }

   public void setLast(Instant last) {
      this.last = last;
   }

   public void setDrops(int[] drops) {
      this.drops = drops;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getType());
      return "ConfigLoot(type=" + var10000 + ", name=" + this.getName() + ", kills=" + this.getKills() + ", first=" + String.valueOf(this.getFirst()) + ", last=" + String.valueOf(this.getLast()) + ", drops=" + Arrays.toString(this.getDrops()) + ")";
   }

   public ConfigLoot() {
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ConfigLoot)) {
         return false;
      } else {
         ConfigLoot other = (ConfigLoot)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$type = this.getType();
            Object other$type = other.getType();
            if (this$type == null) {
               if (other$type != null) {
                  return false;
               }
            } else if (!this$type.equals(other$type)) {
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

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConfigLoot;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      return result;
   }
}
