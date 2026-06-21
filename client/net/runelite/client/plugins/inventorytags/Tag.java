package net.runelite.client.plugins.inventorytags;

import java.awt.Color;

class Tag {
   Color color;

   public Tag() {
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Tag)) {
         return false;
      } else {
         Tag other = (Tag)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$color = this.getColor();
            Object other$color = other.getColor();
            if (this$color == null) {
               if (other$color != null) {
                  return false;
               }
            } else if (!this$color.equals(other$color)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof Tag;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $color = this.getColor();
      result = result * 59 + ($color == null ? 43 : $color.hashCode());
      return result;
   }

   public String toString() {
      return "Tag(color=" + String.valueOf(this.getColor()) + ")";
   }
}
