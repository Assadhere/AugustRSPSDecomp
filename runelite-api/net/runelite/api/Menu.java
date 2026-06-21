package net.runelite.api;

public interface Menu {
   MenuEntry createMenuEntry(int var1);

   MenuEntry[] getMenuEntries();

   void setMenuEntries(MenuEntry[] var1);

   void removeMenuEntry(MenuEntry var1);

   int getMenuX();

   int getMenuY();

   int getMenuWidth();

   int getMenuHeight();
}
