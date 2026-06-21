package net.runelite.api.events;

import net.runelite.api.ObjectComposition;

public final class PostObjectComposition {
   private final ObjectComposition objectComposition;

   public PostObjectComposition(ObjectComposition objectComposition) {
      this.objectComposition = objectComposition;
   }

   public ObjectComposition getObjectComposition() {
      return this.objectComposition;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PostObjectComposition)) {
         return false;
      } else {
         PostObjectComposition other = (PostObjectComposition)o;
         Object this$objectComposition = this.getObjectComposition();
         Object other$objectComposition = other.getObjectComposition();
         if (this$objectComposition == null) {
            if (other$objectComposition != null) {
               return false;
            }
         } else if (!this$objectComposition.equals(other$objectComposition)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $objectComposition = this.getObjectComposition();
      result = result * 59 + ($objectComposition == null ? 43 : $objectComposition.hashCode());
      return result;
   }

   public String toString() {
      return "PostObjectComposition(objectComposition=" + String.valueOf(this.getObjectComposition()) + ")";
   }
}
