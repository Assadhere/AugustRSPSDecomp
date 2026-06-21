package net.runelite.client.plugins.groundmarkers;

import java.awt.Color;
import javax.annotation.Nullable;
import net.runelite.api.coords.WorldPoint;

final class ColorTileMarker {
   private final WorldPoint worldPoint;
   @Nullable
   private final Color color;
   @Nullable
   private final String label;

   public ColorTileMarker(WorldPoint worldPoint, @Nullable Color color, @Nullable String label) {
      this.worldPoint = worldPoint;
      this.color = color;
      this.label = label;
   }

   public WorldPoint getWorldPoint() {
      return this.worldPoint;
   }

   @Nullable
   public Color getColor() {
      return this.color;
   }

   @Nullable
   public String getLabel() {
      return this.label;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ColorTileMarker)) {
         return false;
      } else {
         ColorTileMarker other;
         label44: {
            other = (ColorTileMarker)o;
            Object this$worldPoint = this.getWorldPoint();
            Object other$worldPoint = other.getWorldPoint();
            if (this$worldPoint == null) {
               if (other$worldPoint == null) {
                  break label44;
               }
            } else if (this$worldPoint.equals(other$worldPoint)) {
               break label44;
            }

            return false;
         }

         Object this$color = this.getColor();
         Object other$color = other.getColor();
         if (this$color == null) {
            if (other$color != null) {
               return false;
            }
         } else if (!this$color.equals(other$color)) {
            return false;
         }

         Object this$label = this.getLabel();
         Object other$label = other.getLabel();
         if (this$label == null) {
            if (other$label != null) {
               return false;
            }
         } else if (!this$label.equals(other$label)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $worldPoint = this.getWorldPoint();
      result = result * 59 + ($worldPoint == null ? 43 : $worldPoint.hashCode());
      Object $color = this.getColor();
      result = result * 59 + ($color == null ? 43 : $color.hashCode());
      Object $label = this.getLabel();
      result = result * 59 + ($label == null ? 43 : $label.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getWorldPoint());
      return "ColorTileMarker(worldPoint=" + var10000 + ", color=" + String.valueOf(this.getColor()) + ", label=" + this.getLabel() + ")";
   }
}
