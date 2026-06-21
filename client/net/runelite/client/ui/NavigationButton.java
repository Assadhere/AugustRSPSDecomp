package net.runelite.client.ui;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Map;

public final class NavigationButton {
   static final Comparator<NavigationButton> COMPARATOR = Comparator.comparing(NavigationButton::getPriority).thenComparing(NavigationButton::getTooltip);
   private final BufferedImage icon;
   private final String tooltip;
   private final Runnable onClick;
   private final PluginPanel panel;
   private final int priority;
   private final Map<String, Runnable> popup;

   private static String $default$tooltip() {
      return "";
   }

   NavigationButton(BufferedImage icon, String tooltip, Runnable onClick, PluginPanel panel, int priority, Map<String, Runnable> popup) {
      this.icon = icon;
      this.tooltip = tooltip;
      this.onClick = onClick;
      this.panel = panel;
      this.priority = priority;
      this.popup = popup;
   }

   public static NavigationButtonBuilder builder() {
      return new NavigationButtonBuilder();
   }

   public BufferedImage getIcon() {
      return this.icon;
   }

   public String getTooltip() {
      return this.tooltip;
   }

   public Runnable getOnClick() {
      return this.onClick;
   }

   public PluginPanel getPanel() {
      return this.panel;
   }

   public int getPriority() {
      return this.priority;
   }

   public Map<String, Runnable> getPopup() {
      return this.popup;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NavigationButton)) {
         return false;
      } else {
         NavigationButton other = (NavigationButton)o;
         if (this.getPriority() != other.getPriority()) {
            return false;
         } else {
            label71: {
               Object this$icon = this.getIcon();
               Object other$icon = other.getIcon();
               if (this$icon == null) {
                  if (other$icon == null) {
                     break label71;
                  }
               } else if (this$icon.equals(other$icon)) {
                  break label71;
               }

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

            label57: {
               Object this$onClick = this.getOnClick();
               Object other$onClick = other.getOnClick();
               if (this$onClick == null) {
                  if (other$onClick == null) {
                     break label57;
                  }
               } else if (this$onClick.equals(other$onClick)) {
                  break label57;
               }

               return false;
            }

            Object this$panel = this.getPanel();
            Object other$panel = other.getPanel();
            if (this$panel == null) {
               if (other$panel != null) {
                  return false;
               }
            } else if (!this$panel.equals(other$panel)) {
               return false;
            }

            Object this$popup = this.getPopup();
            Object other$popup = other.getPopup();
            if (this$popup == null) {
               if (other$popup == null) {
                  return true;
               }
            } else if (this$popup.equals(other$popup)) {
               return true;
            }

            return false;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getPriority();
      Object $icon = this.getIcon();
      result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
      Object $tooltip = this.getTooltip();
      result = result * 59 + ($tooltip == null ? 43 : $tooltip.hashCode());
      Object $onClick = this.getOnClick();
      result = result * 59 + ($onClick == null ? 43 : $onClick.hashCode());
      Object $panel = this.getPanel();
      result = result * 59 + ($panel == null ? 43 : $panel.hashCode());
      Object $popup = this.getPopup();
      result = result * 59 + ($popup == null ? 43 : $popup.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getIcon());
      return "NavigationButton(icon=" + var10000 + ", tooltip=" + this.getTooltip() + ", onClick=" + String.valueOf(this.getOnClick()) + ", panel=" + String.valueOf(this.getPanel()) + ", priority=" + this.getPriority() + ", popup=" + String.valueOf(this.getPopup()) + ")";
   }

   public static class NavigationButtonBuilder {
      private BufferedImage icon;
      private boolean tooltip$set;
      private String tooltip$value;
      private Runnable onClick;
      private PluginPanel panel;
      private int priority;
      private Map<String, Runnable> popup;

      NavigationButtonBuilder() {
      }

      public NavigationButtonBuilder icon(BufferedImage icon) {
         this.icon = icon;
         return this;
      }

      public NavigationButtonBuilder tooltip(String tooltip) {
         this.tooltip$value = tooltip;
         this.tooltip$set = true;
         return this;
      }

      public NavigationButtonBuilder onClick(Runnable onClick) {
         this.onClick = onClick;
         return this;
      }

      public NavigationButtonBuilder panel(PluginPanel panel) {
         this.panel = panel;
         return this;
      }

      public NavigationButtonBuilder priority(int priority) {
         this.priority = priority;
         return this;
      }

      public NavigationButtonBuilder popup(Map<String, Runnable> popup) {
         this.popup = popup;
         return this;
      }

      public NavigationButton build() {
         String tooltip$value = this.tooltip$value;
         if (!this.tooltip$set) {
            tooltip$value = NavigationButton.$default$tooltip();
         }

         return new NavigationButton(this.icon, tooltip$value, this.onClick, this.panel, this.priority, this.popup);
      }

      public String toString() {
         String var10000 = String.valueOf(this.icon);
         return "NavigationButton.NavigationButtonBuilder(icon=" + var10000 + ", tooltip$value=" + this.tooltip$value + ", onClick=" + String.valueOf(this.onClick) + ", panel=" + String.valueOf(this.panel) + ", priority=" + this.priority + ", popup=" + String.valueOf(this.popup) + ")";
      }
   }
}
