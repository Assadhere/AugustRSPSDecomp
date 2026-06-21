package net.runelite.api.events;

import net.runelite.api.Actor;

public final class InteractingChanged {
   private final Actor source;
   private final Actor target;

   public InteractingChanged(Actor source, Actor target) {
      this.source = source;
      this.target = target;
   }

   public Actor getSource() {
      return this.source;
   }

   public Actor getTarget() {
      return this.target;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof InteractingChanged)) {
         return false;
      } else {
         InteractingChanged other = (InteractingChanged)o;
         Object this$source = this.getSource();
         Object other$source = other.getSource();
         if (this$source == null) {
            if (other$source != null) {
               return false;
            }
         } else if (!this$source.equals(other$source)) {
            return false;
         }

         Object this$target = this.getTarget();
         Object other$target = other.getTarget();
         if (this$target == null) {
            if (other$target != null) {
               return false;
            }
         } else if (!this$target.equals(other$target)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $source = this.getSource();
      result = result * 59 + ($source == null ? 43 : $source.hashCode());
      Object $target = this.getTarget();
      result = result * 59 + ($target == null ? 43 : $target.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getSource());
      return "InteractingChanged(source=" + var10000 + ", target=" + String.valueOf(this.getTarget()) + ")";
   }
}
