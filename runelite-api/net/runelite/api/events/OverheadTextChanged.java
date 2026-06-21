package net.runelite.api.events;

import net.runelite.api.Actor;

public final class OverheadTextChanged {
   private final Actor actor;
   private final String overheadText;

   public OverheadTextChanged(Actor actor, String overheadText) {
      this.actor = actor;
      this.overheadText = overheadText;
   }

   public Actor getActor() {
      return this.actor;
   }

   public String getOverheadText() {
      return this.overheadText;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof OverheadTextChanged)) {
         return false;
      } else {
         OverheadTextChanged other = (OverheadTextChanged)o;
         Object this$actor = this.getActor();
         Object other$actor = other.getActor();
         if (this$actor == null) {
            if (other$actor != null) {
               return false;
            }
         } else if (!this$actor.equals(other$actor)) {
            return false;
         }

         Object this$overheadText = this.getOverheadText();
         Object other$overheadText = other.getOverheadText();
         if (this$overheadText == null) {
            if (other$overheadText != null) {
               return false;
            }
         } else if (!this$overheadText.equals(other$overheadText)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $actor = this.getActor();
      result = result * 59 + ($actor == null ? 43 : $actor.hashCode());
      Object $overheadText = this.getOverheadText();
      result = result * 59 + ($overheadText == null ? 43 : $overheadText.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getActor());
      return "OverheadTextChanged(actor=" + var10000 + ", overheadText=" + this.getOverheadText() + ")";
   }
}
