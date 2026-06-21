package net.runelite.api;

import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.widgets.Widget;

public interface MenuEntry {
   String getOption();

   MenuEntry setOption(String var1);

   String getTarget();

   MenuEntry setTarget(String var1);

   int getIdentifier();

   MenuEntry setIdentifier(int var1);

   MenuAction getType();

   MenuEntry setType(MenuAction var1);

   int getParam0();

   MenuEntry setParam0(int var1);

   int getParam1();

   MenuEntry setParam1(int var1);

   boolean isForceLeftClick();

   MenuEntry setForceLeftClick(boolean var1);

   int getWorldViewId();

   MenuEntry setWorldViewId(int var1);

   boolean isDeprioritized();

   MenuEntry setDeprioritized(boolean var1);

   MenuEntry onClick(Consumer<MenuEntry> var1);

   Consumer<MenuEntry> onClick();

   boolean isItemOp();

   int getItemOp();

   int getItemId();

   MenuEntry setItemId(int var1);

   @Nullable
   Widget getWidget();

   @Nullable
   NPC getNpc();

   @Nullable
   Player getPlayer();

   @Nullable
   Actor getActor();

   @Nullable
   Menu getSubMenu();

   @Nonnull
   Menu createSubMenu();

   void deleteSubMenu();
}
