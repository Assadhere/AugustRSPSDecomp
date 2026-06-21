package net.runelite.api.events;

import java.util.Arrays;
import net.runelite.api.MenuEntry;

public class MenuOpened {
   private MenuEntry[] menuEntries;

   public MenuEntry getFirstEntry() {
      return this.menuEntries.length > 0 ? this.menuEntries[this.menuEntries.length - 1] : null;
   }

   public MenuEntry[] getMenuEntries() {
      return this.menuEntries;
   }

   public void setMenuEntries(MenuEntry[] menuEntries) {
      this.menuEntries = menuEntries;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MenuOpened)) {
         return false;
      } else {
         MenuOpened other = (MenuOpened)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            return Arrays.deepEquals(this.getMenuEntries(), other.getMenuEntries());
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MenuOpened;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + Arrays.deepHashCode(this.getMenuEntries());
      return result;
   }

   public String toString() {
      return "MenuOpened(menuEntries=" + Arrays.deepToString(this.getMenuEntries()) + ")";
   }
}
