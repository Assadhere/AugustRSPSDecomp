package net.runelite.api.events;

import net.runelite.api.ItemComposition;

public final class PostItemComposition {
   private final ItemComposition itemComposition;

   public PostItemComposition(ItemComposition itemComposition) {
      this.itemComposition = itemComposition;
   }

   public ItemComposition getItemComposition() {
      return this.itemComposition;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PostItemComposition)) {
         return false;
      } else {
         PostItemComposition other = (PostItemComposition)o;
         Object this$itemComposition = this.getItemComposition();
         Object other$itemComposition = other.getItemComposition();
         if (this$itemComposition == null) {
            if (other$itemComposition != null) {
               return false;
            }
         } else if (!this$itemComposition.equals(other$itemComposition)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $itemComposition = this.getItemComposition();
      result = result * 59 + ($itemComposition == null ? 43 : $itemComposition.hashCode());
      return result;
   }

   public String toString() {
      return "PostItemComposition(itemComposition=" + String.valueOf(this.getItemComposition()) + ")";
   }
}
