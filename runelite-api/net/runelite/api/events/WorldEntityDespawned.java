package net.runelite.api.events;

import net.runelite.api.WorldEntity;

public final class WorldEntityDespawned {
   private final WorldEntity worldEntity;

   public WorldEntityDespawned(WorldEntity worldEntity) {
      this.worldEntity = worldEntity;
   }

   public WorldEntity getWorldEntity() {
      return this.worldEntity;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WorldEntityDespawned)) {
         return false;
      } else {
         WorldEntityDespawned other = (WorldEntityDespawned)o;
         Object this$worldEntity = this.getWorldEntity();
         Object other$worldEntity = other.getWorldEntity();
         if (this$worldEntity == null) {
            if (other$worldEntity != null) {
               return false;
            }
         } else if (!this$worldEntity.equals(other$worldEntity)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $worldEntity = this.getWorldEntity();
      result = result * 59 + ($worldEntity == null ? 43 : $worldEntity.hashCode());
      return result;
   }

   public String toString() {
      return "WorldEntityDespawned(worldEntity=" + String.valueOf(this.getWorldEntity()) + ")";
   }
}
