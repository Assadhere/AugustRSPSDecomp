package net.runelite.client.ui.overlay.worldmap;

import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;

public class WorldMapPoint {
   private BufferedImage image;
   private WorldPoint worldPoint;
   @Nullable
   private WorldPoint target;
   private Point imagePoint;
   private boolean snapToEdge;
   private boolean currentlyEdgeSnapped;
   private boolean jumpOnClick;
   private String name;
   private String tooltip;

   public WorldMapPoint(WorldPoint worldPoint, BufferedImage image) {
      this.worldPoint = worldPoint;
      this.image = image;
   }

   public void onEdgeSnap() {
   }

   public void onEdgeUnsnap() {
   }

   protected WorldMapPoint(WorldMapPointBuilder<?, ?> b) {
      this.image = b.image;
      this.worldPoint = b.worldPoint;
      this.target = b.target;
      this.imagePoint = b.imagePoint;
      this.snapToEdge = b.snapToEdge;
      this.currentlyEdgeSnapped = b.currentlyEdgeSnapped;
      this.jumpOnClick = b.jumpOnClick;
      this.name = b.name;
      this.tooltip = b.tooltip;
   }

   public static WorldMapPointBuilder<?, ?> builder() {
      return new WorldMapPointBuilderImpl();
   }

   public BufferedImage getImage() {
      return this.image;
   }

   public WorldPoint getWorldPoint() {
      return this.worldPoint;
   }

   @Nullable
   public WorldPoint getTarget() {
      return this.target;
   }

   public Point getImagePoint() {
      return this.imagePoint;
   }

   public boolean isSnapToEdge() {
      return this.snapToEdge;
   }

   public boolean isCurrentlyEdgeSnapped() {
      return this.currentlyEdgeSnapped;
   }

   public boolean isJumpOnClick() {
      return this.jumpOnClick;
   }

   public String getName() {
      return this.name;
   }

   public String getTooltip() {
      return this.tooltip;
   }

   public void setImage(BufferedImage image) {
      this.image = image;
   }

   public void setWorldPoint(WorldPoint worldPoint) {
      this.worldPoint = worldPoint;
   }

   public void setTarget(@Nullable WorldPoint target) {
      this.target = target;
   }

   public void setImagePoint(Point imagePoint) {
      this.imagePoint = imagePoint;
   }

   public void setSnapToEdge(boolean snapToEdge) {
      this.snapToEdge = snapToEdge;
   }

   public void setCurrentlyEdgeSnapped(boolean currentlyEdgeSnapped) {
      this.currentlyEdgeSnapped = currentlyEdgeSnapped;
   }

   public void setJumpOnClick(boolean jumpOnClick) {
      this.jumpOnClick = jumpOnClick;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setTooltip(String tooltip) {
      this.tooltip = tooltip;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WorldMapPoint)) {
         return false;
      } else {
         WorldMapPoint other = (WorldMapPoint)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.isSnapToEdge() != other.isSnapToEdge()) {
            return false;
         } else if (this.isCurrentlyEdgeSnapped() != other.isCurrentlyEdgeSnapped()) {
            return false;
         } else if (this.isJumpOnClick() != other.isJumpOnClick()) {
            return false;
         } else {
            Object this$image = this.getImage();
            Object other$image = other.getImage();
            if (this$image == null) {
               if (other$image != null) {
                  return false;
               }
            } else if (!this$image.equals(other$image)) {
               return false;
            }

            Object this$worldPoint = this.getWorldPoint();
            Object other$worldPoint = other.getWorldPoint();
            if (this$worldPoint == null) {
               if (other$worldPoint != null) {
                  return false;
               }
            } else if (!this$worldPoint.equals(other$worldPoint)) {
               return false;
            }

            label76: {
               Object this$target = this.getTarget();
               Object other$target = other.getTarget();
               if (this$target == null) {
                  if (other$target == null) {
                     break label76;
                  }
               } else if (this$target.equals(other$target)) {
                  break label76;
               }

               return false;
            }

            Object this$imagePoint = this.getImagePoint();
            Object other$imagePoint = other.getImagePoint();
            if (this$imagePoint == null) {
               if (other$imagePoint != null) {
                  return false;
               }
            } else if (!this$imagePoint.equals(other$imagePoint)) {
               return false;
            }

            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
               if (other$name != null) {
                  return false;
               }
            } else if (!this$name.equals(other$name)) {
               return false;
            }

            Object this$tooltip = this.getTooltip();
            Object other$tooltip = other.getTooltip();
            if (this$tooltip == null) {
               if (other$tooltip != null) {
                  return false;
               }
            } else if (!this$tooltip.equals(other$tooltip)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof WorldMapPoint;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isSnapToEdge() ? 79 : 97);
      result = result * 59 + (this.isCurrentlyEdgeSnapped() ? 79 : 97);
      result = result * 59 + (this.isJumpOnClick() ? 79 : 97);
      Object $image = this.getImage();
      result = result * 59 + ($image == null ? 43 : $image.hashCode());
      Object $worldPoint = this.getWorldPoint();
      result = result * 59 + ($worldPoint == null ? 43 : $worldPoint.hashCode());
      Object $target = this.getTarget();
      result = result * 59 + ($target == null ? 43 : $target.hashCode());
      Object $imagePoint = this.getImagePoint();
      result = result * 59 + ($imagePoint == null ? 43 : $imagePoint.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $tooltip = this.getTooltip();
      result = result * 59 + ($tooltip == null ? 43 : $tooltip.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getImage());
      return "WorldMapPoint(image=" + var10000 + ", worldPoint=" + String.valueOf(this.getWorldPoint()) + ", target=" + String.valueOf(this.getTarget()) + ", imagePoint=" + String.valueOf(this.getImagePoint()) + ", snapToEdge=" + this.isSnapToEdge() + ", currentlyEdgeSnapped=" + this.isCurrentlyEdgeSnapped() + ", jumpOnClick=" + this.isJumpOnClick() + ", name=" + this.getName() + ", tooltip=" + this.getTooltip() + ")";
   }

   private static final class WorldMapPointBuilderImpl extends WorldMapPointBuilder<WorldMapPoint, WorldMapPointBuilderImpl> {
      protected WorldMapPointBuilderImpl self() {
         return this;
      }

      public WorldMapPoint build() {
         return new WorldMapPoint(this);
      }
   }

   public abstract static class WorldMapPointBuilder<C extends WorldMapPoint, B extends WorldMapPointBuilder<C, B>> {
      private BufferedImage image;
      private WorldPoint worldPoint;
      private WorldPoint target;
      private Point imagePoint;
      private boolean snapToEdge;
      private boolean currentlyEdgeSnapped;
      private boolean jumpOnClick;
      private String name;
      private String tooltip;

      public B image(BufferedImage image) {
         this.image = image;
         return this.self();
      }

      public B worldPoint(WorldPoint worldPoint) {
         this.worldPoint = worldPoint;
         return this.self();
      }

      public B target(@Nullable WorldPoint target) {
         this.target = target;
         return this.self();
      }

      public B imagePoint(Point imagePoint) {
         this.imagePoint = imagePoint;
         return this.self();
      }

      public B snapToEdge(boolean snapToEdge) {
         this.snapToEdge = snapToEdge;
         return this.self();
      }

      public B currentlyEdgeSnapped(boolean currentlyEdgeSnapped) {
         this.currentlyEdgeSnapped = currentlyEdgeSnapped;
         return this.self();
      }

      public B jumpOnClick(boolean jumpOnClick) {
         this.jumpOnClick = jumpOnClick;
         return this.self();
      }

      public B name(String name) {
         this.name = name;
         return this.self();
      }

      public B tooltip(String tooltip) {
         this.tooltip = tooltip;
         return this.self();
      }

      protected abstract B self();

      public abstract C build();

      public String toString() {
         String var10000 = String.valueOf(this.image);
         return "WorldMapPoint.WorldMapPointBuilder(image=" + var10000 + ", worldPoint=" + String.valueOf(this.worldPoint) + ", target=" + String.valueOf(this.target) + ", imagePoint=" + String.valueOf(this.imagePoint) + ", snapToEdge=" + this.snapToEdge + ", currentlyEdgeSnapped=" + this.currentlyEdgeSnapped + ", jumpOnClick=" + this.jumpOnClick + ", name=" + this.name + ", tooltip=" + this.tooltip + ")";
      }
   }
}
