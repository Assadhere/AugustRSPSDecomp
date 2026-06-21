package net.runelite.api.events;

import net.runelite.api.Actor;

public class AnimationChanged {
   private Actor actor;

   public Actor getActor() {
      return this.actor;
   }

   public void setActor(Actor actor) {
      this.actor = actor;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof AnimationChanged)) {
         return false;
      } else {
         AnimationChanged other = (AnimationChanged)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$actor = this.getActor();
            Object other$actor = other.getActor();
            if (this$actor == null) {
               if (other$actor != null) {
                  return false;
               }
            } else if (!this$actor.equals(other$actor)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof AnimationChanged;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $actor = this.getActor();
      result = result * 59 + ($actor == null ? 43 : $actor.hashCode());
      return result;
   }

   public String toString() {
      return "AnimationChanged(actor=" + String.valueOf(this.getActor()) + ")";
   }
}
