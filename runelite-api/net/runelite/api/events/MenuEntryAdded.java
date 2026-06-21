package net.runelite.api.events;

import net.runelite.api.MenuEntry;

public class MenuEntryAdded {
   private final MenuEntry menuEntry;

   public String getOption() {
      return this.menuEntry.getOption();
   }

   public String getTarget() {
      return this.menuEntry.getTarget();
   }

   public int getType() {
      return this.menuEntry.getType().getId();
   }

   public int getIdentifier() {
      return this.menuEntry.getIdentifier();
   }

   public int getActionParam0() {
      return this.menuEntry.getParam0();
   }

   public int getActionParam1() {
      return this.menuEntry.getParam1();
   }

   public int getItemId() {
      return this.menuEntry.getItemId();
   }

   public MenuEntryAdded(MenuEntry menuEntry) {
      this.menuEntry = menuEntry;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MenuEntryAdded)) {
         return false;
      } else {
         MenuEntryAdded other = (MenuEntryAdded)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getType() != other.getType()) {
            return false;
         } else if (this.getIdentifier() != other.getIdentifier()) {
            return false;
         } else if (this.getActionParam0() != other.getActionParam0()) {
            return false;
         } else if (this.getActionParam1() != other.getActionParam1()) {
            return false;
         } else if (this.getItemId() != other.getItemId()) {
            return false;
         } else {
            Object this$$getOption = this.getOption();
            Object other$$getOption = other.getOption();
            if (this$$getOption == null) {
               if (other$$getOption != null) {
                  return false;
               }
            } else if (!this$$getOption.equals(other$$getOption)) {
               return false;
            }

            Object this$$getTarget = this.getTarget();
            Object other$$getTarget = other.getTarget();
            if (this$$getTarget == null) {
               if (other$$getTarget != null) {
                  return false;
               }
            } else if (!this$$getTarget.equals(other$$getTarget)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MenuEntryAdded;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getType();
      result = result * 59 + this.getIdentifier();
      result = result * 59 + this.getActionParam0();
      result = result * 59 + this.getActionParam1();
      result = result * 59 + this.getItemId();
      Object $$getOption = this.getOption();
      result = result * 59 + ($$getOption == null ? 43 : $$getOption.hashCode());
      Object $$getTarget = this.getTarget();
      result = result * 59 + ($$getTarget == null ? 43 : $$getTarget.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getOption();
      return "MenuEntryAdded(getOption=" + var10000 + ", getTarget=" + this.getTarget() + ", getType=" + this.getType() + ", getIdentifier=" + this.getIdentifier() + ", getActionParam0=" + this.getActionParam0() + ", getActionParam1=" + this.getActionParam1() + ", getItemId=" + this.getItemId() + ")";
   }

   public MenuEntry getMenuEntry() {
      return this.menuEntry;
   }
}
