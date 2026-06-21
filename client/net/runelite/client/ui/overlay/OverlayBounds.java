package net.runelite.client.ui.overlay;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;

final class OverlayBounds {
   private final Rectangle topLeft;
   private final Rectangle topCenter;
   private final Rectangle topRight;
   private final Rectangle bottomLeft;
   private final Rectangle bottomRight;
   private final Rectangle aboveChatboxRight;
   private final Rectangle canvasTopRight;

   OverlayBounds(OverlayBounds other) {
      this.topLeft = new Rectangle(other.topLeft);
      this.topCenter = new Rectangle(other.topCenter);
      this.topRight = new Rectangle(other.topRight);
      this.bottomLeft = new Rectangle(other.bottomLeft);
      this.bottomRight = new Rectangle(other.bottomRight);
      this.aboveChatboxRight = new Rectangle(other.aboveChatboxRight);
      this.canvasTopRight = new Rectangle(other.canvasTopRight);
   }

   OverlayBounds translated(int x, int y) {
      OverlayBounds translated = new OverlayBounds(this);
      translated.getTopRight().translate(x, 0);
      translated.getTopCenter().translate(x / 2, 0);
      translated.getBottomLeft().translate(0, y);
      translated.getBottomRight().translate(x, y);
      translated.getAboveChatboxRight().translate(x, y);
      translated.getCanvasTopRight().translate(x, 0);
      return translated;
   }

   Rectangle forPosition(OverlayPosition overlayPosition) {
      switch (overlayPosition) {
         case TOP_LEFT:
            return this.topLeft;
         case TOP_CENTER:
            return this.topCenter;
         case TOP_RIGHT:
            return this.topRight;
         case BOTTOM_LEFT:
            return this.bottomLeft;
         case BOTTOM_RIGHT:
            return this.bottomRight;
         case ABOVE_CHATBOX_RIGHT:
            return this.aboveChatboxRight;
         case CANVAS_TOP_RIGHT:
            return this.canvasTopRight;
         default:
            throw new IllegalArgumentException();
      }
   }

   OverlayPosition fromBounds(Rectangle bounds) {
      if (bounds == this.topLeft) {
         return OverlayPosition.TOP_LEFT;
      } else if (bounds == this.topCenter) {
         return OverlayPosition.TOP_CENTER;
      } else if (bounds == this.topRight) {
         return OverlayPosition.TOP_RIGHT;
      } else if (bounds == this.bottomLeft) {
         return OverlayPosition.BOTTOM_LEFT;
      } else if (bounds == this.bottomRight) {
         return OverlayPosition.BOTTOM_RIGHT;
      } else if (bounds == this.aboveChatboxRight) {
         return OverlayPosition.ABOVE_CHATBOX_RIGHT;
      } else if (bounds == this.canvasTopRight) {
         return OverlayPosition.CANVAS_TOP_RIGHT;
      } else {
         throw new IllegalArgumentException();
      }
   }

   Collection<Rectangle> getBounds() {
      return Arrays.asList(this.topLeft, this.topCenter, this.topRight, this.bottomLeft, this.bottomRight, this.aboveChatboxRight, this.canvasTopRight);
   }

   public Rectangle getTopLeft() {
      return this.topLeft;
   }

   public Rectangle getTopCenter() {
      return this.topCenter;
   }

   public Rectangle getTopRight() {
      return this.topRight;
   }

   public Rectangle getBottomLeft() {
      return this.bottomLeft;
   }

   public Rectangle getBottomRight() {
      return this.bottomRight;
   }

   public Rectangle getAboveChatboxRight() {
      return this.aboveChatboxRight;
   }

   public Rectangle getCanvasTopRight() {
      return this.canvasTopRight;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof OverlayBounds)) {
         return false;
      } else {
         OverlayBounds other;
         label92: {
            other = (OverlayBounds)o;
            Object this$topLeft = this.getTopLeft();
            Object other$topLeft = other.getTopLeft();
            if (this$topLeft == null) {
               if (other$topLeft == null) {
                  break label92;
               }
            } else if (this$topLeft.equals(other$topLeft)) {
               break label92;
            }

            return false;
         }

         Object this$topCenter = this.getTopCenter();
         Object other$topCenter = other.getTopCenter();
         if (this$topCenter == null) {
            if (other$topCenter != null) {
               return false;
            }
         } else if (!this$topCenter.equals(other$topCenter)) {
            return false;
         }

         Object this$topRight = this.getTopRight();
         Object other$topRight = other.getTopRight();
         if (this$topRight == null) {
            if (other$topRight != null) {
               return false;
            }
         } else if (!this$topRight.equals(other$topRight)) {
            return false;
         }

         label71: {
            Object this$bottomLeft = this.getBottomLeft();
            Object other$bottomLeft = other.getBottomLeft();
            if (this$bottomLeft == null) {
               if (other$bottomLeft == null) {
                  break label71;
               }
            } else if (this$bottomLeft.equals(other$bottomLeft)) {
               break label71;
            }

            return false;
         }

         label64: {
            Object this$bottomRight = this.getBottomRight();
            Object other$bottomRight = other.getBottomRight();
            if (this$bottomRight == null) {
               if (other$bottomRight == null) {
                  break label64;
               }
            } else if (this$bottomRight.equals(other$bottomRight)) {
               break label64;
            }

            return false;
         }

         Object this$aboveChatboxRight = this.getAboveChatboxRight();
         Object other$aboveChatboxRight = other.getAboveChatboxRight();
         if (this$aboveChatboxRight == null) {
            if (other$aboveChatboxRight != null) {
               return false;
            }
         } else if (!this$aboveChatboxRight.equals(other$aboveChatboxRight)) {
            return false;
         }

         Object this$canvasTopRight = this.getCanvasTopRight();
         Object other$canvasTopRight = other.getCanvasTopRight();
         if (this$canvasTopRight == null) {
            if (other$canvasTopRight != null) {
               return false;
            }
         } else if (!this$canvasTopRight.equals(other$canvasTopRight)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $topLeft = this.getTopLeft();
      result = result * 59 + ($topLeft == null ? 43 : $topLeft.hashCode());
      Object $topCenter = this.getTopCenter();
      result = result * 59 + ($topCenter == null ? 43 : $topCenter.hashCode());
      Object $topRight = this.getTopRight();
      result = result * 59 + ($topRight == null ? 43 : $topRight.hashCode());
      Object $bottomLeft = this.getBottomLeft();
      result = result * 59 + ($bottomLeft == null ? 43 : $bottomLeft.hashCode());
      Object $bottomRight = this.getBottomRight();
      result = result * 59 + ($bottomRight == null ? 43 : $bottomRight.hashCode());
      Object $aboveChatboxRight = this.getAboveChatboxRight();
      result = result * 59 + ($aboveChatboxRight == null ? 43 : $aboveChatboxRight.hashCode());
      Object $canvasTopRight = this.getCanvasTopRight();
      result = result * 59 + ($canvasTopRight == null ? 43 : $canvasTopRight.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getTopLeft());
      return "OverlayBounds(topLeft=" + var10000 + ", topCenter=" + String.valueOf(this.getTopCenter()) + ", topRight=" + String.valueOf(this.getTopRight()) + ", bottomLeft=" + String.valueOf(this.getBottomLeft()) + ", bottomRight=" + String.valueOf(this.getBottomRight()) + ", aboveChatboxRight=" + String.valueOf(this.getAboveChatboxRight()) + ", canvasTopRight=" + String.valueOf(this.getCanvasTopRight()) + ")";
   }

   public OverlayBounds(Rectangle topLeft, Rectangle topCenter, Rectangle topRight, Rectangle bottomLeft, Rectangle bottomRight, Rectangle aboveChatboxRight, Rectangle canvasTopRight) {
      this.topLeft = topLeft;
      this.topCenter = topCenter;
      this.topRight = topRight;
      this.bottomLeft = bottomLeft;
      this.bottomRight = bottomRight;
      this.aboveChatboxRight = aboveChatboxRight;
      this.canvasTopRight = canvasTopRight;
   }
}
