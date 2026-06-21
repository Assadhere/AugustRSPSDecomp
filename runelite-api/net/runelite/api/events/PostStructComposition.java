package net.runelite.api.events;

import net.runelite.api.StructComposition;

public class PostStructComposition {
   private StructComposition structComposition;

   public StructComposition getStructComposition() {
      return this.structComposition;
   }

   public void setStructComposition(StructComposition structComposition) {
      this.structComposition = structComposition;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PostStructComposition)) {
         return false;
      } else {
         PostStructComposition other = (PostStructComposition)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$structComposition = this.getStructComposition();
            Object other$structComposition = other.getStructComposition();
            if (this$structComposition == null) {
               if (other$structComposition != null) {
                  return false;
               }
            } else if (!this$structComposition.equals(other$structComposition)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PostStructComposition;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $structComposition = this.getStructComposition();
      result = result * 59 + ($structComposition == null ? 43 : $structComposition.hashCode());
      return result;
   }

   public String toString() {
      return "PostStructComposition(structComposition=" + String.valueOf(this.getStructComposition()) + ")";
   }
}
