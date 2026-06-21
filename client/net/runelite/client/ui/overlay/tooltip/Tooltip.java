package net.runelite.client.ui.overlay.tooltip;

import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

public class Tooltip {
   private String text;
   private LayoutableRenderableEntity component;

   public Tooltip(String text) {
      this.text = text;
   }

   public Tooltip(LayoutableRenderableEntity component) {
      this.component = component;
   }

   public String getText() {
      return this.text;
   }

   public LayoutableRenderableEntity getComponent() {
      return this.component;
   }

   public void setText(String text) {
      this.text = text;
   }

   public void setComponent(LayoutableRenderableEntity component) {
      this.component = component;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Tooltip)) {
         return false;
      } else {
         Tooltip other = (Tooltip)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$text = this.getText();
            Object other$text = other.getText();
            if (this$text == null) {
               if (other$text != null) {
                  return false;
               }
            } else if (!this$text.equals(other$text)) {
               return false;
            }

            Object this$component = this.getComponent();
            Object other$component = other.getComponent();
            if (this$component == null) {
               if (other$component != null) {
                  return false;
               }
            } else if (!this$component.equals(other$component)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof Tooltip;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $text = this.getText();
      result = result * 59 + ($text == null ? 43 : $text.hashCode());
      Object $component = this.getComponent();
      result = result * 59 + ($component == null ? 43 : $component.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getText();
      return "Tooltip(text=" + var10000 + ", component=" + String.valueOf(this.getComponent()) + ")";
   }
}
