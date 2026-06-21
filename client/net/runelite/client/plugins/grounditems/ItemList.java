package net.runelite.client.plugins.grounditems;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.runelite.client.util.WildcardMatcher;

final class ItemList {
   static final int NONE = 0;
   static final int WILDCARD = 1;
   static final int EXACT = 2;
   private final List<ItemThreshold> items;

   ItemList(List<String> items) {
      this.items = (List)items.stream().map(ItemThreshold::fromName).filter(Objects::nonNull).collect(Collectors.toList());
   }

   int matches(GroundItem item) {
      Iterator var2 = this.items.iterator();

      ItemThreshold it;
      do {
         if (!var2.hasNext()) {
            var2 = this.items.iterator();

            do {
               if (!var2.hasNext()) {
                  return 0;
               }

               it = (ItemThreshold)var2.next();
            } while(!it.isWildcard() || !WildcardMatcher.matches(it.getName(), item.getName()) || !it.quantityHolds(item.getQuantity()));

            return 1;
         }

         it = (ItemThreshold)var2.next();
      } while(it.isWildcard() || !it.getName().equalsIgnoreCase(item.getName()) || !it.quantityHolds(item.getQuantity()));

      return 2;
   }

   public List<ItemThreshold> getItems() {
      return this.items;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ItemList)) {
         return false;
      } else {
         ItemList other = (ItemList)o;
         Object this$items = this.getItems();
         Object other$items = other.getItems();
         if (this$items == null) {
            if (other$items != null) {
               return false;
            }
         } else if (!this$items.equals(other$items)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $items = this.getItems();
      result = result * 59 + ($items == null ? 43 : $items.hashCode());
      return result;
   }

   public String toString() {
      return "ItemList(items=" + String.valueOf(this.getItems()) + ")";
   }
}
