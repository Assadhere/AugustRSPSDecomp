package net.runelite.client.ui.overlay;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;

public class OverlayMenuEntry {
   private final MenuAction menuAction;
   private final String option;
   private final String target;
   @Nullable
   Consumer<MenuEntry> callback;

   public OverlayMenuEntry(MenuAction menuAction, String option, String target) {
      this.menuAction = menuAction;
      this.option = option;
      this.target = target;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getMenuAction());
      return "OverlayMenuEntry(menuAction=" + var10000 + ", option=" + this.getOption() + ", target=" + this.getTarget() + ")";
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof OverlayMenuEntry)) {
         return false;
      } else {
         OverlayMenuEntry other = (OverlayMenuEntry)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$menuAction = this.getMenuAction();
               Object other$menuAction = other.getMenuAction();
               if (this$menuAction == null) {
                  if (other$menuAction == null) {
                     break label47;
                  }
               } else if (this$menuAction.equals(other$menuAction)) {
                  break label47;
               }

               return false;
            }

            Object this$option = this.getOption();
            Object other$option = other.getOption();
            if (this$option == null) {
               if (other$option != null) {
                  return false;
               }
            } else if (!this$option.equals(other$option)) {
               return false;
            }

            Object this$target = this.getTarget();
            Object other$target = other.getTarget();
            if (this$target == null) {
               if (other$target != null) {
                  return false;
               }
            } else if (!this$target.equals(other$target)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof OverlayMenuEntry;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $menuAction = this.getMenuAction();
      result = result * 59 + ($menuAction == null ? 43 : $menuAction.hashCode());
      Object $option = this.getOption();
      result = result * 59 + ($option == null ? 43 : $option.hashCode());
      Object $target = this.getTarget();
      result = result * 59 + ($target == null ? 43 : $target.hashCode());
      return result;
   }

   public MenuAction getMenuAction() {
      return this.menuAction;
   }

   public String getOption() {
      return this.option;
   }

   public String getTarget() {
      return this.target;
   }
}
