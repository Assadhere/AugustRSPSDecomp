package net.runelite.client.menus;

import java.awt.Color;
import java.util.function.Consumer;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.ColorUtil;

public final class WidgetMenuOption {
   private String menuOption;
   private String menuTarget;
   private Color color;
   private final int[] widgetIds;
   Consumer<MenuEntry> callback;

   /** @deprecated */
   @Deprecated
   public WidgetMenuOption(String menuOption, String menuTarget, WidgetInfo widget) {
      this(menuOption, menuTarget, widget.getId());
   }

   public WidgetMenuOption(String menuOption, String menuTarget, int... widgetIds) {
      this.color = JagexColors.MENU_TARGET;
      this.menuOption = menuOption;
      this.setMenuTarget(menuTarget);
      this.widgetIds = widgetIds;
   }

   public void setMenuTarget(String target) {
      this.menuTarget = ColorUtil.wrapWithColorTag(target, this.color);
   }

   public String getMenuOption() {
      return this.menuOption;
   }

   public void setMenuOption(String menuOption) {
      this.menuOption = menuOption;
   }

   public String getMenuTarget() {
      return this.menuTarget;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   int[] getWidgetIds() {
      return this.widgetIds;
   }
}
