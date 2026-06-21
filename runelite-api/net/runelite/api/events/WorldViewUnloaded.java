package net.runelite.api.events;

import net.runelite.api.WorldView;

public final class WorldViewUnloaded {
   private final WorldView worldView;

   public WorldViewUnloaded(WorldView worldView) {
      this.worldView = worldView;
   }

   public WorldView getWorldView() {
      return this.worldView;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WorldViewUnloaded)) {
         return false;
      } else {
         WorldViewUnloaded other = (WorldViewUnloaded)o;
         Object this$worldView = this.getWorldView();
         Object other$worldView = other.getWorldView();
         if (this$worldView == null) {
            if (other$worldView != null) {
               return false;
            }
         } else if (!this$worldView.equals(other$worldView)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $worldView = this.getWorldView();
      result = result * 59 + ($worldView == null ? 43 : $worldView.hashCode());
      return result;
   }

   public String toString() {
      return "WorldViewUnloaded(worldView=" + String.valueOf(this.getWorldView()) + ")";
   }
}
