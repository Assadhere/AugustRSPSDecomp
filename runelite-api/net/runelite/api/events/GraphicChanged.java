package net.runelite.api.events;

import net.runelite.api.Actor;

public class GraphicChanged {
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
      } else if (!(o instanceof GraphicChanged)) {
         return false;
      } else {
         GraphicChanged other = (GraphicChanged)o;
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
      return other instanceof GraphicChanged;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $actor = this.getActor();
      result = result * 59 + ($actor == null ? 43 : $actor.hashCode());
      return result;
   }

   public String toString() {
      return "GraphicChanged(actor=" + String.valueOf(this.getActor()) + ")";
   }
}
