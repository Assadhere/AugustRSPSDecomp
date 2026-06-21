package net.runelite.api.events;

import javax.annotation.Nullable;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;

public class MenuOptionClicked {
   private final MenuEntry menuEntry;
   private boolean consumed;

   public int getParam0() {
      return this.menuEntry.getParam0();
   }

   public int getParam1() {
      return this.menuEntry.getParam1();
   }

   public String getMenuOption() {
      return this.menuEntry.getOption();
   }

   public String getMenuTarget() {
      return this.menuEntry.getTarget();
   }

   public MenuAction getMenuAction() {
      return this.menuEntry.getType();
   }

   public int getId() {
      return this.menuEntry.getIdentifier();
   }

   public boolean isItemOp() {
      return this.menuEntry.isItemOp();
   }

   public int getItemOp() {
      return this.menuEntry.getItemOp();
   }

   public int getItemId() {
      return this.menuEntry.getItemId();
   }

   @Nullable
   public Widget getWidget() {
      return this.menuEntry.getWidget();
   }

   public void consume() {
      this.consumed = true;
   }

   /** @deprecated */
   @Deprecated
   public int getActionParam() {
      return this.menuEntry.getParam0();
   }

   /** @deprecated */
   @Deprecated
   public int getWidgetId() {
      return this.menuEntry.getParam1();
   }

   public MenuOptionClicked(MenuEntry menuEntry) {
      this.menuEntry = menuEntry;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MenuOptionClicked)) {
         return false;
      } else {
         MenuOptionClicked other = (MenuOptionClicked)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getParam0() != other.getParam0()) {
            return false;
         } else if (this.getParam1() != other.getParam1()) {
            return false;
         } else if (this.getId() != other.getId()) {
            return false;
         } else {
            label54: {
               Object this$$getMenuOption = this.getMenuOption();
               Object other$$getMenuOption = other.getMenuOption();
               if (this$$getMenuOption == null) {
                  if (other$$getMenuOption == null) {
                     break label54;
                  }
               } else if (this$$getMenuOption.equals(other$$getMenuOption)) {
                  break label54;
               }

               return false;
            }

            Object this$$getMenuTarget = this.getMenuTarget();
            Object other$$getMenuTarget = other.getMenuTarget();
            if (this$$getMenuTarget == null) {
               if (other$$getMenuTarget != null) {
                  return false;
               }
            } else if (!this$$getMenuTarget.equals(other$$getMenuTarget)) {
               return false;
            }

            Object this$$getMenuAction = this.getMenuAction();
            Object other$$getMenuAction = other.getMenuAction();
            if (this$$getMenuAction == null) {
               if (other$$getMenuAction != null) {
                  return false;
               }
            } else if (!this$$getMenuAction.equals(other$$getMenuAction)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MenuOptionClicked;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getParam0();
      result = result * 59 + this.getParam1();
      result = result * 59 + this.getId();
      Object $$getMenuOption = this.getMenuOption();
      result = result * 59 + ($$getMenuOption == null ? 43 : $$getMenuOption.hashCode());
      Object $$getMenuTarget = this.getMenuTarget();
      result = result * 59 + ($$getMenuTarget == null ? 43 : $$getMenuTarget.hashCode());
      Object $$getMenuAction = this.getMenuAction();
      result = result * 59 + ($$getMenuAction == null ? 43 : $$getMenuAction.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getParam0();
      return "MenuOptionClicked(getParam0=" + var10000 + ", getParam1=" + this.getParam1() + ", getMenuOption=" + this.getMenuOption() + ", getMenuTarget=" + this.getMenuTarget() + ", getMenuAction=" + String.valueOf(this.getMenuAction()) + ", getId=" + this.getId() + ")";
   }

   public MenuEntry getMenuEntry() {
      return this.menuEntry;
   }

   public boolean isConsumed() {
      return this.consumed;
   }
}
