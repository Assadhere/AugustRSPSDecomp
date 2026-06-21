package net.runelite.client.plugins.devtools;

import java.util.Arrays;
import javax.annotation.Nullable;
import net.runelite.api.Item;

final class InventoryLog {
   private final int containerId;
   @Nullable
   private final String containerName;
   private final Item[] items;
   private final int tick;

   public InventoryLog(int containerId, @Nullable String containerName, Item[] items, int tick) {
      this.containerId = containerId;
      this.containerName = containerName;
      this.items = items;
      this.tick = tick;
   }

   public int getContainerId() {
      return this.containerId;
   }

   @Nullable
   public String getContainerName() {
      return this.containerName;
   }

   public Item[] getItems() {
      return this.items;
   }

   public int getTick() {
      return this.tick;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof InventoryLog)) {
         return false;
      } else {
         InventoryLog other = (InventoryLog)o;
         if (this.getContainerId() != other.getContainerId()) {
            return false;
         } else if (this.getTick() != other.getTick()) {
            return false;
         } else {
            Object this$containerName = this.getContainerName();
            Object other$containerName = other.getContainerName();
            if (this$containerName == null) {
               if (other$containerName == null) {
                  return Arrays.deepEquals(this.getItems(), other.getItems());
               }
            } else if (this$containerName.equals(other$containerName)) {
               return Arrays.deepEquals(this.getItems(), other.getItems());
            }

            return false;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getContainerId();
      result = result * 59 + this.getTick();
      Object $containerName = this.getContainerName();
      result = result * 59 + ($containerName == null ? 43 : $containerName.hashCode());
      result = result * 59 + Arrays.deepHashCode(this.getItems());
      return result;
   }

   public String toString() {
      int var10000 = this.getContainerId();
      return "InventoryLog(containerId=" + var10000 + ", containerName=" + this.getContainerName() + ", items=" + Arrays.deepToString(this.getItems()) + ", tick=" + this.getTick() + ")";
   }
}
