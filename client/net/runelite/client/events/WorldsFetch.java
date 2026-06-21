package net.runelite.client.events;

import net.runelite.http.api.worlds.WorldResult;

public final class WorldsFetch {
   private final WorldResult worldResult;

   public WorldsFetch(WorldResult worldResult) {
      this.worldResult = worldResult;
   }

   public WorldResult getWorldResult() {
      return this.worldResult;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WorldsFetch)) {
         return false;
      } else {
         WorldsFetch other = (WorldsFetch)o;
         Object this$worldResult = this.getWorldResult();
         Object other$worldResult = other.getWorldResult();
         if (this$worldResult == null) {
            if (other$worldResult != null) {
               return false;
            }
         } else if (!this$worldResult.equals(other$worldResult)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $worldResult = this.getWorldResult();
      result = result * 59 + ($worldResult == null ? 43 : $worldResult.hashCode());
      return result;
   }

   public String toString() {
      return "WorldsFetch(worldResult=" + String.valueOf(this.getWorldResult()) + ")";
   }
}
