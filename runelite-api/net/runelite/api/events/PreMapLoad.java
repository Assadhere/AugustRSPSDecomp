package net.runelite.api.events;

import net.runelite.api.Scene;
import net.runelite.api.WorldView;

public final class PreMapLoad {
   private final WorldView worldView;
   private final Scene scene;

   public PreMapLoad(WorldView worldView, Scene scene) {
      this.worldView = worldView;
      this.scene = scene;
   }

   public WorldView getWorldView() {
      return this.worldView;
   }

   public Scene getScene() {
      return this.scene;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PreMapLoad)) {
         return false;
      } else {
         PreMapLoad other = (PreMapLoad)o;
         Object this$worldView = this.getWorldView();
         Object other$worldView = other.getWorldView();
         if (this$worldView == null) {
            if (other$worldView != null) {
               return false;
            }
         } else if (!this$worldView.equals(other$worldView)) {
            return false;
         }

         Object this$scene = this.getScene();
         Object other$scene = other.getScene();
         if (this$scene == null) {
            if (other$scene != null) {
               return false;
            }
         } else if (!this$scene.equals(other$scene)) {
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
      Object $scene = this.getScene();
      result = result * 59 + ($scene == null ? 43 : $scene.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getWorldView());
      return "PreMapLoad(worldView=" + var10000 + ", scene=" + String.valueOf(this.getScene()) + ")";
   }
}
