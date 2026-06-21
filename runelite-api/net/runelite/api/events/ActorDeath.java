package net.runelite.api.events;

import net.runelite.api.Actor;

public final class ActorDeath {
   private final Actor actor;

   public ActorDeath(Actor actor) {
      this.actor = actor;
   }

   public Actor getActor() {
      return this.actor;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ActorDeath)) {
         return false;
      } else {
         ActorDeath other = (ActorDeath)o;
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

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $actor = this.getActor();
      result = result * 59 + ($actor == null ? 43 : $actor.hashCode());
      return result;
   }

   public String toString() {
      return "ActorDeath(actor=" + String.valueOf(this.getActor()) + ")";
   }
}
