package net.runelite.client.plugins.screenmarkers;

import java.awt.Color;

public class ScreenMarker {
   private long id;
   private String name;
   private int borderThickness;
   private Color color;
   private Color fill;
   private boolean visible;
   private boolean labelled;

   public long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public int getBorderThickness() {
      return this.borderThickness;
   }

   public Color getColor() {
      return this.color;
   }

   public Color getFill() {
      return this.fill;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public boolean isLabelled() {
      return this.labelled;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setBorderThickness(int borderThickness) {
      this.borderThickness = borderThickness;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public void setFill(Color fill) {
      this.fill = fill;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public void setLabelled(boolean labelled) {
      this.labelled = labelled;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ScreenMarker)) {
         return false;
      } else {
         ScreenMarker other = (ScreenMarker)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getId() != other.getId()) {
            return false;
         } else if (this.getBorderThickness() != other.getBorderThickness()) {
            return false;
         } else if (this.isVisible() != other.isVisible()) {
            return false;
         } else if (this.isLabelled() != other.isLabelled()) {
            return false;
         } else {
            label57: {
               Object this$name = this.getName();
               Object other$name = other.getName();
               if (this$name == null) {
                  if (other$name == null) {
                     break label57;
                  }
               } else if (this$name.equals(other$name)) {
                  break label57;
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

            Object this$fill = this.getFill();
            Object other$fill = other.getFill();
            if (this$fill == null) {
               if (other$fill != null) {
                  return false;
               }
            } else if (!this$fill.equals(other$fill)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ScreenMarker;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $id = this.getId();
      result = result * 59 + (int)($id >>> 32 ^ $id);
      result = result * 59 + this.getBorderThickness();
      result = result * 59 + (this.isVisible() ? 79 : 97);
      result = result * 59 + (this.isLabelled() ? 79 : 97);
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $color = this.getColor();
      result = result * 59 + ($color == null ? 43 : $color.hashCode());
      Object $fill = this.getFill();
      result = result * 59 + ($fill == null ? 43 : $fill.hashCode());
      return result;
   }

   public String toString() {
      long var10000 = this.getId();
      return "ScreenMarker(id=" + var10000 + ", name=" + this.getName() + ", borderThickness=" + this.getBorderThickness() + ", color=" + String.valueOf(this.getColor()) + ", fill=" + String.valueOf(this.getFill()) + ", visible=" + this.isVisible() + ", labelled=" + this.isLabelled() + ")";
   }

   public ScreenMarker() {
   }

   public ScreenMarker(long id, String name, int borderThickness, Color color, Color fill, boolean visible, boolean labelled) {
      this.id = id;
      this.name = name;
      this.borderThickness = borderThickness;
      this.color = color;
      this.fill = fill;
      this.visible = visible;
      this.labelled = labelled;
   }
}
